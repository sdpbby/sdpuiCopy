package com.accenture.bby.sdp.web.handlers.vendor;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.accenture.bby.sdp.db.KcbCatalogDBWrapper;
import com.accenture.bby.sdp.db.VendorDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbVendorBean;
import com.accenture.bby.sdp.web.beans.VendorBean;
import com.accenture.bby.sdp.web.handlers.DropdownListManager;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ViewScoped
@ManagedBean (name="vendorHandler")
public class VendorHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(VendorHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{vendorTabbedPaneHandler}")
	private VendorTabbedPaneHandler vendorTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	
	/*
	 * Model Beans
	 */
	private VendorBean newVendorBean;
	private VendorBean oldVendorBean;

	
	/*
	 * getters
	 */
	public VendorTabbedPaneHandler getVendorTabbedPaneHandler() { return vendorTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public VendorBean getNewVendorBean() { return newVendorBean; }
	public VendorBean getOldVendorBean() { return oldVendorBean; }
	
	/*
	 * setters
	 */
	public void setVendorTabbedPaneHandler(VendorTabbedPaneHandler vendorTabbedPaneHandler) { this.vendorTabbedPaneHandler = vendorTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewVendorBean(VendorBean newVendorBean) { this.newVendorBean = newVendorBean; }
	public void setOldVendorBean(VendorBean oldVendorBean) { this.oldVendorBean = oldVendorBean; }
	
	/*
	 * Action Listeners
	 */
	public String createVendorButtonClick() {
		vendorTabbedPaneHandler.displayEditTab();
		this.setOldVendorBean(new VendorBean());
		this.setNewVendorBean(new VendorBean());
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String saveChangesButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean specialCharFound = false;
		if (isNotNull(this.getNewVendorBean().getVendorId()) && TextFilter.isSpecialCharacterPresent(this.getNewVendorBean().getVendorId().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (vendorId) ::"
					+ this.getNewVendorBean().getVendorId().trim());
			specialCharFound= true;
			context.addMessage(
					"vendor_edit:vendorId",
					new FacesMessage("Special characters are not allowed."));
		} 
		
		if (isNotNull(this.getNewVendorBean().getVendorName()) && TextFilter.isSpecialCharacterPresent(this.getNewVendorBean().getVendorName().trim())) {
			logger.log(Level.DEBUG, "Special Character Found (vendorName) ::"
					+ this.getNewVendorBean().getVendorName().trim());
			specialCharFound= true;
			context.addMessage(
					"vendor_edit:vendorName",
					new FacesMessage("Special characters are not allowed."));
		} 
		if(specialCharFound){
			return NavigationStrings.CURRENT_VIEW;
		}else{logger.log(Level.INFO, "BEGIN: Save vendor changes.");
		
		try {
			logger.log(Level.INFO, "BEGIN: Save vendor changes in sdp catalog: \n\nOLD:\n" + this.getOldVendorBean() + "\nNEW:\n" + this.getNewVendorBean());
			if (this.getOldVendorBean() == null || this.getOldVendorBean().getVendorId() == null) {
				captureInsertAuditLog();
				insertVendor();
				logger.log(Level.INFO, "FINISH: Save vendor changes in sdp catalog.");
				logger.log(Level.INFO, "BEGIN: Save vendor changes in kcb catalog.");
				KcbVendorBean[] kcbVendorBeans = KcbCatalogDBWrapper.getVendorsByVendorId(this.getNewVendorBean().getVendorId());
				if (kcbVendorBeans == null || kcbVendorBeans.length < 1) {
					captureKcbInsertAuditLog();
					insertKcbVendor();
				} else {
					captureKcbEditAuditLog();
					updatePreExistingKcbVendor();
				}
				logger.log(Level.INFO, "FINISH: Save vendor changes in kcb catalog.");
			} else {
				captureEditAuditLog();
				updateVendor();
				logger.log(Level.INFO, "FINISH: Save vendor changes in sdp catalog.");
				logger.log(Level.INFO, "BEGIN: Save vendor changes in kcb catalog.");
				captureKcbEditAuditLog();
				updateKcbVendor();
				logger.log(Level.INFO, "FINISH: Save vendor changes in kcb catalog.");
			}
			vendorTabbedPaneHandler.displaySearchTab();

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
		return NavigationStrings.VENDOR_DEFAULT_PAGE;
		}
	}
	
	public String cancelChangesButtonClick() {
		// No action to take, just display the search tab.
		vendorTabbedPaneHandler.displaySearchTab();
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
		AuditUtil.audit(Action.CREATED_VENDOR_CONFIGURATION, newVendorBean, oldVendorBean);
	}
	
	/**
	 * Captures audit log on vendor updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureEditAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_VENDOR_CONFIGURATION, newVendorBean, oldVendorBean);
	}
	
	private void insertVendor() throws DataSourceLookupException, DataAccessException {
		VendorDBWrapper.insert(this.getNewVendorBean().getVendorId(), this.getNewVendorBean().getVendorName(), this.getNewVendorBean().getServiceProviderId(), this.getNewVendorBean().getAggregationFrequency(), this.getNewVendorBean().getAggregationMax(), this.getNewVendorBean().getRetryFrequency(), this.getNewVendorBean().getRetryMax(), this.getNewVendorBean().getThrottleFactor(), this.getUserManager().getUserBean().getUsername(), this.getNewVendorBean().getCategory());
	}
	
	private void updateVendor() throws DataSourceLookupException, DataAccessException {
		VendorDBWrapper.update(this.getOldVendorBean().getVendorId(), this.getNewVendorBean().getVendorId(), this.getNewVendorBean().getVendorName(), this.getNewVendorBean().getServiceProviderId(), this.getNewVendorBean().getAggregationFrequency(), this.getNewVendorBean().getAggregationMax(), this.getNewVendorBean().getRetryFrequency(), this.getNewVendorBean().getRetryMax(), this.getNewVendorBean().getThrottleFactor(), this.getUserManager().getUserBean().getUsername(), this.getNewVendorBean().getCategory());
	}
	
	/**
	 * Captures audit log on vendor inserts.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureKcbInsertAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.CREATED_KCB_VENDOR_CONFIGURATION, newVendorBean, oldVendorBean);
	}
	
	/**
	 * Captures audit log on vendor updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureKcbEditAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_KCB_VENDOR_CONFIGURATION, newVendorBean, oldVendorBean);
	}
	
	private void insertKcbVendor() throws DataSourceLookupException, DataAccessException {
		KcbVendorBean kcbVendorBean = new KcbVendorBean();
		kcbVendorBean.setVendorId(this.getNewVendorBean().getVendorId());
		kcbVendorBean.setVendorName(this.getNewVendorBean().getVendorName());
		kcbVendorBean.setCreatedByUserId(this.getUserManager().getUserBean().getUsername());
		kcbVendorBean.setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
		kcbVendorBean.setCreatedDate(new Date());
		kcbVendorBean.setUpdatedDate(new Date());
		KcbCatalogDBWrapper.insertVendor(kcbVendorBean);
	}
	
	private void updatePreExistingKcbVendor() throws DataSourceLookupException, DataAccessException {
		KcbVendorBean kcbVendorBean = new KcbVendorBean();
		kcbVendorBean.setVendorId(this.getNewVendorBean().getVendorId());
		kcbVendorBean.setVendorName(this.getNewVendorBean().getVendorName());
		kcbVendorBean.setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
		kcbVendorBean.setUpdatedDate(new Date());
		
		KcbCatalogDBWrapper.updateVendor(this.getNewVendorBean().getVendorId(), kcbVendorBean);
	}
	
	private void updateKcbVendor() throws DataSourceLookupException, DataAccessException {
		KcbVendorBean kcbVendorBean = new KcbVendorBean();
		kcbVendorBean.setVendorId(this.getNewVendorBean().getVendorId());
		kcbVendorBean.setVendorName(this.getNewVendorBean().getVendorName());
		kcbVendorBean.setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
		kcbVendorBean.setUpdatedDate(new Date());
		
		KcbCatalogDBWrapper.updateVendor(this.getOldVendorBean().getVendorId(), kcbVendorBean);
	}

	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
