package com.accenture.bby.sdp.utl.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.handlers.UserManager;

public class AuditUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(AuditUtil.class.getName());

	/**
	 * creates an audit trail session and stores the tracking id in the input UserManager
	 * 
	 * @param userManager
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 * @throws NullPointerException if userManager is null
	 */
	public static void createSessionLog(UserManager userManager) throws DataSourceLookupException, DataAccessException {
		logger.log(Level.DEBUG, "BEGIN capturing session log.");
		if (userManager == null) {
			logger.log(Level.ERROR, "FAILED to capture session log because userManager was null.");
			throw new NullPointerException("UserManager was null. Cannot create session log in audit trail.");
		}
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		String userId = userManager.getUserBean().getUsername() != null ? userManager.getUserBean().getUsername() : "UNKNOWN";
		userManager.getUserBean().setUid(UUID.randomUUID().toString());
		
		AuditTrailDBWrapper.insertSession(userManager.getUserBean().getUid(), userId, userManager.getUserBean().getRoles(), new Date(session.getCreationTime()), null);
		logger.log(Level.DEBUG, "FINISH capturing session log.");
	}
	
	/**
	 * captures audit trail log of current transaction data and original transaction data
	 * 
	 * @param action
	 * @param currentRequestData
	 * @param originalRequestData
	 * @return
	 * @throws AuditTrailException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static TransactionLog audit(Action action, Auditable currentRequestData, Auditable originalRequestData) throws AuditTrailException, DataAccessException, DataSourceLookupException {
		logger.log(Level.DEBUG, "BEGIN capturing transaction audit log.");
		
		UserManager userManager = (UserManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userManagerBean");
		
		if (userManager == null) {
			logger.log(Level.ERROR, "FAILED to capture transaction audit log because userManager was null.");
			throw new AuditTrailException("\"userManagerBean\" was not found in the session. Unable to capture audit logs.");
		}
		
		TransactionLog auditLog = new TransactionLog();
		
		auditLog.setUserId(userManager.getUserBean().getUsername());
		auditLog.setAction(action);
		
		// set the audit trail session id using the usermanager uuid
		auditLog.setSessionId(userManager.getUserBean().getUid());
		
		if (currentRequestData != null) {
		
    		// set the trackable transaction fields
    		auditLog.setContractId(currentRequestData.getContractIdForAudit());
    		auditLog.setPrimarySku(currentRequestData.getPrimarySkuForAudit());
    		auditLog.setLineItemId(currentRequestData.getLineItemIdForAudit());
    		auditLog.setVendorKey(currentRequestData.getSerialNumberForAudit());
    		auditLog.setVendorId(currentRequestData.getVendorIdForAudit());
    		auditLog.setMasterItemId(currentRequestData.getMasterItemIdForAudit());
		
    		// list of audit logs
    		List<FieldLog> auditLogs = new ArrayList<FieldLog>();
    		// map of new values to log
    		Map<Field, String> fields = currentRequestData.getAuditableFields();
    		// map of original values to log. if no original values captured then create empty map
    		Map<Field, String> originalFields;
    		if (originalRequestData != null) {
    			originalFields = originalRequestData.getAuditableFields();
    		} else {
    			originalFields = new HashMap<Field, String>();
    		}
    		
    		// iterate over new values map and create audit logs.
    		// also checks old values map for equivalent value and, if found, logs it and
    		// discards it so it is not counted twice later on.
    		for (Map.Entry<Field, String> field : fields.entrySet()) {
    			if (field.getValue() != null) {
        			FieldLog log = new FieldLog();
        			if (field.getKey().isSensitiveData()) {
        				log.setNewValue(TextFilter.getMaskedValue(field.getValue()));
        				log.setOldValue(TextFilter.getMaskedValue(originalFields.get(field.getKey())));
        			} else {
        				log.setNewValue(field.getValue());
        				log.setOldValue(originalFields.get(field.getKey()));
        			}
        			originalFields.remove(field.getKey());
        			log.setFieldId(field.getKey().getFieldId());
        			log.setPriority(field.getKey().getPriority());
        			auditLogs.add(log);
    			}
    		}
    		
    		
    		// iterate over any values left in the old values map. This could only have
    		// values in it (since they were removed above) if the original request had a particular
    		// field populated but was not populated in the new request.
    		for (Map.Entry<Field, String> field : originalFields.entrySet()) {
    			if (field.getValue() != null) {
        			FieldLog log = new FieldLog();
        			if (field.getKey().isSensitiveData()) {
        				log.setOldValue(TextFilter.getMaskedValue(field.getValue()));
        			} else {
        				log.setOldValue(field.getValue());
        			}
        			log.setFieldId(field.getKey().getFieldId());
        			log.setPriority(field.getKey().getPriority());
        			auditLogs.add(log);
    			}
    		}
		
		
		
    		auditLog.setAuditLogs(auditLogs);
    		
    		
    		
    		if (logger.isDebugEnabled()) {
    			logger.log(Level.DEBUG, "Captured audit log:\n" + auditLog.toString());
    		}
    		
    		// write logs to database
    		AuditTrailDBWrapper.insertAuditLogTransaction(auditLog);
    		
    		// cleanup
    		fields = null; 
    		auditLogs = null;
    		originalFields = null; 
    	
		} else {
			if (logger.isDebugEnabled()) {
    			logger.log(Level.DEBUG, "Captured audit log:\n" + auditLog.toString());
    		}
    		
    		// write logs to database
    		AuditTrailDBWrapper.insertAuditLogTransaction(auditLog);
		}
		
		// return the audit log
		logger.log(Level.DEBUG, "FINISH capturing transaction audit log.");
		return auditLog;
	}
	
	/**
	 * captures audit trail log of current action.
	 * 
	 * @param action
	 * @return
	 * @throws AuditTrailException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static TransactionLog audit(Action action) throws AuditTrailException, DataAccessException, DataSourceLookupException {
		return audit(action, null, null);
	}
	
	/**
	 * captures audit trail log of current transaction data
	 * 
	 * @param action
	 * @param requestData
	 * @return
	 * @throws AuditTrailException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static TransactionLog audit(Action action, Auditable requestData) throws AuditTrailException, DataAccessException, DataSourceLookupException {
		return audit(action, requestData, null);
	}
	
    /**
     * @param auditLog
     * @param requestMessage
     * @throws DataSourceLookupException
     * @throws DataAccessException
     */
    public static void audit(TransactionLog auditLog, XmlObject requestMessage) throws DataSourceLookupException, DataAccessException {
    	logger.log(Level.DEBUG, "BEGIN capturing xml request audit log.");
    	AuditTrailDBWrapper.insertAuditXml(auditLog, requestMessage.toString(), null, null);
    	logger.log(Level.DEBUG, "FINISH capturing xml request audit log.");
    }
    /**
     * @param auditLog
     * @param requestMessage
     * @param responseMessage
     * @throws DataSourceLookupException
     * @throws DataAccessException
     */
    public static void audit(TransactionLog auditLog, XmlObject requestMessage, XmlObject responseMessage) throws DataSourceLookupException, DataAccessException {
    	logger.log(Level.DEBUG, "BEGIN capturing xml request audit log.");
    	AuditTrailDBWrapper.insertAuditXml(auditLog, requestMessage.toString(), responseMessage.toString(), null);
    	logger.log(Level.DEBUG, "FINISH capturing xml request audit log.");
    }
	/**
	 * @param auditLog
	 * @param currentRequestMessage
	 * @param currentResponseMessage
	 * @param originalRequestMessage
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static void audit(TransactionLog auditLog, XmlObject currentRequestMessage, XmlObject currentResponseMessage, XmlObject originalRequestMessage) throws DataSourceLookupException, DataAccessException {
		logger.log(Level.DEBUG, "BEGIN capturing xml request audit log.");
		AuditTrailDBWrapper.insertAuditXml(auditLog, currentRequestMessage.toString(), currentResponseMessage.toString(), originalRequestMessage.toString());
		logger.log(Level.DEBUG, "FINISH capturing xml request audit log.");
	}
	
}