package com.accenture.bby.sdp.web.handlers.audittrail;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.AuditLog;
import com.accenture.bby.sdp.web.beans.XmlAnalysis;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;

@ManagedBean (name="auditTrailDetailHandler")
@ViewScoped
public class AuditTrailDetailHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1503325053116196890L;

	private static final Logger logger = Logger.getLogger(AuditTrailDetailHandler.class.getName());
	
	public static final String REQUEST_PARAM = "auditLogId";
	
	private String logId;
	
	private AuditLog log;
	
	private boolean hideXmlView = true;
	
	private AuditTrailDatatableHandler searchResults = new AuditTrailDatatableHandler();
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	
	public AuditTrailDetailHandler() throws NumberFormatException, DataAccessException, DataSourceLookupException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		logId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		logger.log(Level.DEBUG, "Initializing AuditTrailDetailHandler page with logId=" + logId);
		if (logId == null) {
			logger.log(Level.DEBUG, "auditLogId was null or not set in request param. Checking session.");
			logId = (String)facesContext.getExternalContext().getSessionMap().get(REQUEST_PARAM);
			if (logId == null) {
				logger.log(Level.ERROR, "auditLogId was null or not set in session. Cannot initialize page.");
				throw new NullPointerException("auditLogId was null or not set in request param or in session. Cannot initialize page.");
			}
		} else {
			logger.log(Level.DEBUG, "auditLogId variable found in request param. storing in session map.");
			facesContext.getExternalContext().getSessionMap().put(REQUEST_PARAM, logId);
		}
		this.log = AuditTrailDBWrapper.getAuditLogByAudTransId(new Integer(logId).intValue());
		if (this.log == null) {
			logger.log(Level.ERROR, "Failed to retrieve audit trail log with auditLogId=[" + logId + "]");
			throw new DataAccessException("Failed to retrieve audit trail log with auditLogId=[" + logId + "]");
		}
		searchResults.setRows(AuditTrailDBWrapper.getSimpleAuditLogsByAny(log.getContractId(), log.getSerialNumber(), log.getLineItemId()));
	}
	
	public void toggleXmlViewButtonClick(ActionEvent e) {
		hideXmlView = !hideXmlView;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public AuditLog getLog() {
		return log;
	}

	public void setLog(AuditLog log) {
		this.log = log;
	}

	public boolean isHideXmlView() {
		return hideXmlView;
	}

	public void setHideXmlView(boolean hideXmlView) {
		this.hideXmlView = hideXmlView;
	}

	public AuditTrailDatatableHandler getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(AuditTrailDatatableHandler searchResults) {
		this.searchResults = searchResults;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	public String getCurrentXml() {
		if (log != null) {
			List<XmlAnalysis> xml = log.getXmlAnalysisList();
			if (xml != null && xml.size() > 0) {
				return xml.get(0).getCurrentXml();
			}
		}
		return null;
	}
	
	public String getUpdatedXml() {
		if (log != null) {
			List<XmlAnalysis> xml = log.getXmlAnalysisList();
			if (xml != null && xml.size() > 0) {
				return xml.get(0).getUpdatedXml();
			}
		}
		return null;
	}
	
	public String getResponseXml() {
		if (log != null) {
			List<XmlAnalysis> xml = log.getXmlAnalysisList();
			if (xml != null && xml.size() > 0) {
				return xml.get(0).getResponseXml();
			}
		}
		return null;
	}
		
}
