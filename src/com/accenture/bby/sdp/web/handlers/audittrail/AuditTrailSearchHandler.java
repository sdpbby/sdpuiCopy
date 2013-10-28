package com.accenture.bby.sdp.web.handlers.audittrail;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;

@ManagedBean (name="auditTrailSearchHandler")
@ViewScoped
public class AuditTrailSearchHandler implements Serializable {
	
	private static final Logger logger = Logger.getLogger(AuditTrailSearchHandler.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6578979593765185606L;
	private String contractId;
	private String serialNumber;
	private String lineItemId;
	private String primarySku;
	private String vendorId;
	private String masterItemId;
	private Integer actionId;
	private Date min;
	private Date max;
	private String userId;
	private Integer logId;
	private Integer displayMax;
	
	private AuditTrailDatatableHandler searchResults = new AuditTrailDatatableHandler();
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	
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
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public Date getMin() {
		return min;
	}
	public void setMin(Date min) {
		this.min = min;
	}
	public String getMinStr() {
		if (min == null) {
			return null;
		} else {
			return DateUtil.getShortXmlDateString(min);
		}
	}
	public Date getMax() {
		return max;
	}
	public void setMax(Date max) {
		this.max = max;
	}
	public String getMaxStr() {
		if (max == null) {
			return null;
		} else {
			return DateUtil.getShortXmlDateString(max);
		}
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public Integer getDisplayMax() {
		return displayMax;
	}
	public void setDisplayMax(Integer displayMax) {
		this.displayMax = displayMax;
	}
	public String getPrimarySku() {
		return primarySku;
	}
	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getMasterItemId() {
		return masterItemId;
	}
	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}
	public AuditTrailSearchHandler() {
		displayMax = 20;
	}
	
	public String searchButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean specialCharFound = false;
		if (isNotNull(contractId) && TextFilter.isSpecialCharacterPresent(contractId.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (ContractId) ::"
					+ contractId.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_contract_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(serialNumber) && TextFilter.isSpecialCharacterPresent(serialNumber.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (serialNumber) ::"
					+ serialNumber.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_serial_number",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(lineItemId) && TextFilter.isSpecialCharacterPresent(lineItemId.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (lineItemId) ::"
					+ lineItemId.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_line_item_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(userId) && TextFilter.isSpecialCharacterPresent(userId.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (userId) ::"
					+ userId.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_user_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(vendorId) && TextFilter.isSpecialCharacterPresent(vendorId.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (vendorId) ::"
					+ vendorId.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_vendor_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(primarySku) && TextFilter.isSpecialCharacterPresent(primarySku.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (primarySku) ::"
					+ primarySku.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_primary_sku",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(masterItemId) && TextFilter.isSpecialCharacterPresent(masterItemId.trim())) {
			logger.log(Level.DEBUG, "Special Character Found (masterItemId) ::"
					+ masterItemId.trim());
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_master_item_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (logId != null && TextFilter.isSpecialCharacterPresent(logId.toString().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (logId) ::"
					+ logId);
			specialCharFound= true;
			context.addMessage(
					"audit_search_form:search_log_id",
					new FacesMessage("Special characters are not allowed."));
		} 
		if(specialCharFound){
			return NavigationStrings.CURRENT_VIEW;
		}else{try {
			searchResults.setRows(AuditTrailDBWrapper.getSimpleAuditLogsByAll(contractId, serialNumber, lineItemId, actionId, min, max, userId, logId, vendorId, primarySku, masterItemId, displayMax.intValue()));
			return NavigationStrings.CURRENT_VIEW;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve audit trail logs.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		}
	}
	
	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
	public String clearButtonClick() {
		return NavigationStrings.AUDIT_TRAIL_SEARCH_DEFAULT_PAGE;
	}
}
