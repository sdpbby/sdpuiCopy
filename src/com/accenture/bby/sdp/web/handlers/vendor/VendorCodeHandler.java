package com.accenture.bby.sdp.web.handlers.vendor;

import java.io.Serializable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.accenture.bby.sdp.db.VendorCodeDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.VendorCodeBean;
import com.accenture.bby.sdp.web.handlers.DropdownListManager;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ViewScoped
@ManagedBean (name="vendorCodeHandler")
public class VendorCodeHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(VendorCodeHandler.class.getName());
	
	public static final String REQUEST_PARAM = "vendorId";
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{vendorCodeTabbedPaneHandler}")
	private VendorCodeTabbedPaneHandler vendorCodeTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;

	
	
	/*
	 * Model Beans
	 */
	private VendorCodeBean newVendorCodeBean;
	private VendorCodeBean oldVendorCodeBean;
	
	/*
	 * Default vendor id when creating new code
	 */
	private String vendorId;
	public String getVendorId() {return vendorId;}
	public void setVendorId(String vendorId) {this.vendorId = vendorId;}

	
	/*
	 * getters
	 */
	public VendorCodeTabbedPaneHandler getVendorCodeTabbedPaneHandler() { return vendorCodeTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public VendorCodeBean getNewVendorCodeBean() { return newVendorCodeBean; }
	public VendorCodeBean getOldVendorCodeBean() { return oldVendorCodeBean; }
	
	/*
	 * setters
	 */
	public void setVendorCodeTabbedPaneHandler(VendorCodeTabbedPaneHandler vendorCodeTabbedPaneHandler) { this.vendorCodeTabbedPaneHandler = vendorCodeTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewVendorCodeBean(VendorCodeBean newVendorCodeBean) { this.newVendorCodeBean = newVendorCodeBean; }
	public void setOldVendorCodeBean(VendorCodeBean oldVendorCodeBean) { this.oldVendorCodeBean = oldVendorCodeBean; }
	
	/*
	 * Action Listeners
	 */
	public String createVendorCodeButtonClick() {

		vendorCodeTabbedPaneHandler.displayVendorCodeEditTab();
		this.setOldVendorCodeBean(new VendorCodeBean());
		this.setNewVendorCodeBean(new VendorCodeBean());
		this.getNewVendorCodeBean().setVendorId(this.vendorId);
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String backButtonClick() {

		return NavigationStrings.VENDOR_DEFAULT_PAGE;
	}
	
	public String saveChangesButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean specialCharFound = false;
		if (isNotNull(this.getNewVendorCodeBean().getVendorCode()) && TextFilter.isSpecialCharacterPresent(this.getNewVendorCodeBean().getVendorCode().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (vendorCode) ::"
					+ this.getNewVendorCodeBean().getVendorCode().trim());
			specialCharFound= true;
			context.addMessage(
					"vendor_code_edit:vendorCode",
					new FacesMessage("Special characters are not allowed."));
		} 
		if (isNotNull(this.getNewVendorCodeBean().getSdpCode()) && TextFilter.isSpecialCharacterPresent(this.getNewVendorCodeBean().getSdpCode().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (sdpCode) ::"
					+ this.getNewVendorCodeBean().getSdpCode().trim());
			specialCharFound= true;
			context.addMessage(
					"vendor_code_edit:sdpCode",
					new FacesMessage("Special characters are not allowed."));
		} 
		if (isNotNull(this.getNewVendorCodeBean().getVendorCodeDescription()) && TextFilter.isSpecialCharacterPresent(this.getNewVendorCodeBean().getVendorCodeDescription().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (VendorCodeDescription) ::"
					+ this.getNewVendorCodeBean().getVendorCodeDescription().trim());
			specialCharFound= true;
			context.addMessage(
					"vendor_code_edit:description",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		
		if(specialCharFound){
			return NavigationStrings.CURRENT_VIEW;
		}else{logger.log(Level.INFO, "BEGIN: Save vendor changes: \n\nOLD:\n" + this.getOldVendorCodeBean() + "\nNEW:\n" + this.getNewVendorCodeBean());
		
		try {
			logger.log(Level.INFO, "BEGIN: Save vendor changes in sdp catalog.");
			if (this.getOldVendorCodeBean() == null || this.getOldVendorCodeBean().getVendorCodeRowId() == null) {
				captureInsertAuditLog();
				insertVendor();
				logger.log(Level.INFO, "FINISH: Save vendor changes in sdp catalog.");
				
			} else {
				captureEditAuditLog();
				updateVendor();
				logger.log(Level.INFO, "FINISH: Save vendor changes in sdp catalog.");
				
			}
			vendorCodeTabbedPaneHandler.displayVendorCodeDefaultTab();

		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. Vendor updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while updating vendor record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		
		DropdownListManager.refreshVendorMap();
		
		logger.log(Level.INFO, "FINISH: Save vendor changes.");
		return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_CODE_DEFAULT_PAGE, VendorCodeDatatable.REQUEST_PARAM, this.getVendorId());
		}
	}
	
	public String cancelChangesButtonClick() {
		// No action to take, just display the search tab.
		vendorCodeTabbedPaneHandler.displayVendorCodeDefaultTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/**
	 * Captures audit log on vendor inserts.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureInsertAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.CREATED_VENDOR_CODE_CONFIGURATION, newVendorCodeBean, oldVendorCodeBean);
	}
	
	/**
	 * Captures audit log on vendor updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureEditAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_VENDOR_CODE_CONFIGURATION, newVendorCodeBean, oldVendorCodeBean);
	}
	
	private void insertVendor() throws DataSourceLookupException, DataAccessException {
		VendorCodeDBWrapper.insert(this.getNewVendorCodeBean().getVendorCode(), this.getNewVendorCodeBean().getSdpCode(), this.getNewVendorCodeBean().getVendorId(), this.getNewVendorCodeBean().getCanRetry(), this.getNewVendorCodeBean().getVendorCodeDescription(), this.getUserManager().getUserBean().getUsername());
	}
	
	private void updateVendor() throws DataSourceLookupException, DataAccessException {
		VendorCodeDBWrapper.update(this.getOldVendorCodeBean().getVendorCodeRowId(), this.getNewVendorCodeBean().getVendorCode(), this.getNewVendorCodeBean().getSdpCode(), this.getNewVendorCodeBean().getVendorId(), this.getNewVendorCodeBean().getCanRetry(), this.getNewVendorCodeBean().getVendorCodeDescription(), this.getUserManager().getUserBean().getUsername());
	}
	
	/**
	 * @throws IllegalArgumentException if context param is null or if vendor id does not match existing vendor
	 */
	public VendorCodeHandler() {
		// get the vendorId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.vendorId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		logger.log(Level.DEBUG, "VendorId param passed=" + this.vendorId);
		
		// if vendor does not exist throw exception
		if (this.vendorId == null || Constants.getVendorName(this.vendorId) == null) {
			throw new IllegalArgumentException("Invalid Vendor ID: " + this.vendorId);
		}
	}
	
	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
