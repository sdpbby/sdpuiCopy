package com.accenture.bby.sdp.web.handlers.catalog;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.beans.WorkFlowAttributeBean;
import com.accenture.bby.sdp.web.beans.WorkFlowAttributeBean.OperationFlag;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ViewScoped
@ManagedBean (name="catalogHandler")
public class CatalogHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(CatalogHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{catalogTabbedPaneHandler}")
	private CatalogTabbedPaneHandler catalogTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	
	/*
	 * Model Beans
	 */
	private CatalogBean newCatalogBean;
	private CatalogBean oldCatalogBean;

	private boolean specialCharacterFoundInsertFlag;

	
	/*
	 * getters
	 */
	public CatalogTabbedPaneHandler getCatalogTabbedPaneHandler() { return catalogTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public CatalogBean getNewCatalogBean() { return newCatalogBean; }
	public CatalogBean getOldCatalogBean() { return oldCatalogBean; }

	
	/*
	 * setters
	 */
	public void setCatalogTabbedPaneHandler(final CatalogTabbedPaneHandler catalogTabbedPaneHandler) { this.catalogTabbedPaneHandler = catalogTabbedPaneHandler; }
	public void setExceptionHandler(final ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(final UserManager userManager) { this.userManager = userManager; }
	public void setNewCatalogBean(final CatalogBean newCatalogBean) { this.newCatalogBean = newCatalogBean; }
	public void setOldCatalogBean(final CatalogBean oldCatalogBean) { this.oldCatalogBean = oldCatalogBean; }

	
	/*
	 * Action Listeners
	 */
	public String cancelButtonClick() {
		displayDuplicateValueError = false;
		// No action to take, just display the search tab.
		catalogTabbedPaneHandler.displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String cancelChangesButtonClick() {
		displayDuplicateValueError = false;
		// No action to take, just display the search tab.
		catalogTabbedPaneHandler.displaySearchTab();
		return NavigationStrings.CATALOG_DEFAULT_PAGE;
	}
	
	public String saveChangesButtonClick() {
		
		boolean specialCharFoundFlag = false;
		if (isNotNull(this.getNewCatalogBean().getPrimarySkuDescription()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getPrimarySkuDescription().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:primarySkuDescription",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getMasterVendorId()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getMasterVendorId().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:masterVendorId",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getPrimarySku()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getPrimarySku().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:primarySku",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getParentSku()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getParentSku().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:parentSku",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getVendorTriggerSku()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getVendorTriggerSku().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:vendorTriggerSku",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getProductType()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getProductType().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:productType",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewCatalogBean().getCommSatTemplateId()) && TextFilter.isSpecialCharacterPresent(this.getNewCatalogBean().getCommSatTemplateId().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_edit_form:commSatTemplateId",
					new FacesMessage("Special characters are not allowed."));
			specialCharFoundFlag= true;
		}
		
		if(specialCharFoundFlag){
			return NavigationStrings.CURRENT_VIEW;
		}else{try {
			if (this.getOldCatalogBean() == null || this.getNewCatalogBean().getCatalogId() == null) {
				captureInsertAuditLog();
				insertCatalog();
			} else {
				captureUpdateAuditLog();
				updateCatalog(); // Note: this method could set displayDuplicateValueError to true
			}

			if (displayDuplicateValueError) {
				return NavigationStrings.CURRENT_VIEW;
			} else {
				logger.log(Level.INFO, "FINISH: Save Catalog changes.");
				catalogTabbedPaneHandler.displaySearchTab();
				return NavigationStrings.CATALOG_DEFAULT_PAGE;
			}

		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. Catalog updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while updating catalog record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		}
	}
	
	protected static final int CREATE_ANY = 0;
	protected static final int CREATE_PRODUCT = 1;
	protected static final int CREATE_PROFILE = 2;
	protected int catalogTypeFlag = CREATE_ANY;
	
	public boolean isCreateProduct() { return catalogTypeFlag == CREATE_PRODUCT; }
	public boolean isCreateProfile() { return catalogTypeFlag == CREATE_PROFILE; }
	
	public String createCatalogProductButtonClick() {
		catalogTypeFlag = CREATE_PRODUCT;
		return createCatalog();
	}
	
	public String createCatalogProfileButtonClick() {
		catalogTypeFlag = CREATE_PROFILE;
		return createCatalog();
	}
	
	private String createCatalog() {
		displayDuplicateValueError = false;
		catalogTabbedPaneHandler.displayEditTab();
		this.setOldCatalogBean(new CatalogBean());
		this.setNewCatalogBean(new CatalogBean());
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/**
	 * Has conditional check to determine if CatalogBean is a product or profile
	 * based on existence of MasterVendorId
	 * 
	 * @return Navigation String
	 */
	public String editButtonClick() {
		displayDuplicateValueError = false;
		if (this.getOldCatalogBean() != null && this.getOldCatalogBean().getMasterVendorId() != null) {
			catalogTypeFlag = CREATE_PROFILE;
		} else {
			catalogTypeFlag = CREATE_PRODUCT;
		}
		catalogTabbedPaneHandler.displayEditTab();
		this.setNewCatalogBean(new CatalogBean(this.getOldCatalogBean()));
		return NavigationStrings.CURRENT_VIEW;
	}
	
	
	/**
	 * Has conditional check to determine if CatalogBean is a product or profile
	 * based on existence of MasterVendorId
	 * 
	 * @return Navigation String
	 */
	public String cloneButtonClick() {
		displayDuplicateValueError = false;
		if (this.getOldCatalogBean() != null && this.getOldCatalogBean().getMasterVendorId() != null) {
			catalogTypeFlag = CREATE_PROFILE;
		} else {
			catalogTypeFlag = CREATE_PRODUCT;
		}
		catalogTabbedPaneHandler.displayEditTab();
		this.setNewCatalogBean(new CatalogBean(this.getOldCatalogBean()));
		this.getNewCatalogBean().setCatalogId(null);
		this.getNewCatalogBean().setPrimarySku(null);
		this.getNewCatalogBean().setMasterVendorId(null);
		this.getNewCatalogBean().setPrimarySkuDescription(null);
		this.getNewCatalogBean().setVendorTriggerSku(null);
		for (WorkFlowAttributeBean attr : this.getNewCatalogBean().getWorkFlowAttributes()) {
			attr.setOperationFlag(OperationFlag.INSERT);
		}
		this.setOldCatalogBean(new CatalogBean());
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private boolean displayDuplicateValueError = false;
	private boolean displaySpecialCharacterError = false;
	public boolean isDisplayDuplicateValueError() { return displayDuplicateValueError; }
	public boolean isDisplaySpecialCharacterError() { return displaySpecialCharacterError; }
	
	public void validateProduct(ValueChangeEvent e) throws DataAccessException, DataSourceLookupException {
		displayDuplicateValueError = false;
		displaySpecialCharacterError = false;
		if (e != null && e.getNewValue() != null) {
			if(TextFilter.isSpecialCharacterPresent(e.getNewValue().toString())){
				displaySpecialCharacterError = true;
			}else  if (this.getOldCatalogBean() == null || this.getOldCatalogBean().getPrimarySku() == null || !this.getOldCatalogBean().getPrimarySku().equals(e.getNewValue().toString())) {
		
				List<String> catalogs = CatalogDBWrapper.getProductIdBySku(e.getNewValue().toString());
				if (catalogs.size() > 0) {
					displayDuplicateValueError = true;
				}
			}
		}
	}
	
	public void needsCommsatIntegValueChanged(ValueChangeEvent e){
		String attributeFlag = (String) e.getNewValue();
		if (attributeFlag == null) {
			attributeFlag = "FALSE";
		}
		this.getNewCatalogBean().setNeedsCommsatInteg(attributeFlag.toUpperCase());
	}
	
	public void captureInsertAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.CREATED_CATALOG_CONFIGURATION, newCatalogBean, oldCatalogBean);
	}

	public void captureUpdateAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_CATALOG_CONFIGURATION, newCatalogBean, oldCatalogBean);
	}
	
	public void insertCatalog() throws DataSourceLookupException, DataAccessException {
		this.getNewCatalogBean().setCreatedDate(new Date());
		this.getNewCatalogBean().setCreatedByUserId(this.userManager.getUserBean().getUsername());
		this.getNewCatalogBean().setUpdatedDate(new Date());
		this.getNewCatalogBean().setUpdatedByUserId(this.userManager.getUserBean().getUsername());
		
		// check if duplicate before inserting
		List<String> catalogs = new ArrayList<String>();
		if (this.isCreateProduct()) {
			catalogs = CatalogDBWrapper.getProductIdBySku(this.getNewCatalogBean().getPrimarySku());
		} else if (this.isCreateProfile()) {
			catalogs = CatalogDBWrapper.getProductIdByMasterVendorIdAndProductType(this.getNewCatalogBean().getMasterVendorId(), this.getNewCatalogBean().getProductType());
		}
		if (catalogs.size() > 0) {
			displayDuplicateValueError = true;
			logger.log(Level.INFO, "NO ACTION: Duplicate primarySku or masterVendorId/productType blocked because it already exists: \n\nOLD:\n" + this.getOldCatalogBean() + "\nNEW:\n" + this.getNewCatalogBean());
		} else {
			displayDuplicateValueError = false;
			logger.log(Level.INFO, "BEGIN: Save Catalog changes: \n\nOLD:\n" + this.getOldCatalogBean() + "\nNEW:\n" + this.getNewCatalogBean());
			CatalogDBWrapper.insert(this.getNewCatalogBean());
		}
		
	}

	public void updateCatalog() throws DataSourceLookupException, DataAccessException {
		this.getNewCatalogBean().setUpdatedDate(new Date());
		this.getNewCatalogBean().setUpdatedByUserId(this.userManager.getUserBean().getUsername());
		
		// check if duplicate before updating
		if (this.getOldCatalogBean() != null && this.getNewCatalogBean() != null) {
			
			
			if (this.isCreateProduct()) {
				displayDuplicateValueError = isProductDuplicate();
				
			} else if (this.isCreateProfile()) {
				displayDuplicateValueError = isProfileDuplicate();
			}
		}
		if (!displayDuplicateValueError) {
			logger.log(Level.INFO, "BEGIN: Save Catalog changes: \n\nOLD:\n" + this.getOldCatalogBean() + "\nNEW:\n" + this.getNewCatalogBean());
			CatalogDBWrapper.update(this.getNewCatalogBean());
		} else {
			logger.log(Level.INFO, "NO ACTION: Duplicate primarySku or masterVendorId/productType blocked because it already exists: \n\nOLD:\n" + this.getOldCatalogBean() + "\nNEW:\n" + this.getNewCatalogBean());
		}
	}
	
	private boolean isProductDuplicate() throws DataAccessException, DataSourceLookupException {
		if (this.getNewCatalogBean().getPrimarySku() == null) {
			throw new IllegalArgumentException("Primary SKU is a mandatory field but got null.");
		}
		if (this.getOldCatalogBean().getPrimarySku() == null || !this.getOldCatalogBean().getPrimarySku().equals(this.getNewCatalogBean().getPrimarySku()) ) {
			if (CatalogDBWrapper.getProductIdBySku(this.getNewCatalogBean().getPrimarySku()).size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isProfileDuplicate() throws DataAccessException, DataSourceLookupException {
		if (this.getNewCatalogBean().getMasterVendorId() == null || this.getNewCatalogBean().getProductType() == null) {
			throw new IllegalArgumentException("Master Vendor ID and Product Type are mandatory fields but got null.");
		}
		if (this.getOldCatalogBean().getMasterVendorId() == null 
				|| !(this.getOldCatalogBean().getMasterVendorId().equals(this.getNewCatalogBean().getMasterVendorId()) && this.getOldCatalogBean().getProductType().equals(this.getNewCatalogBean().getProductType()))) {
			if (CatalogDBWrapper.getProductIdByMasterVendorIdAndProductType(this.getNewCatalogBean().getMasterVendorId(), this.getNewCatalogBean().getProductType()).size() > 0) {
				return true;
			}
		}
		return false;
	}
	public boolean isSpecialCharacterFoundInsertFlag() {
		return specialCharacterFoundInsertFlag;
	}
	public void setSpecialCharacterFoundInsertFlag(
			boolean specialCharacterFoundInsertFlag) {
		this.specialCharacterFoundInsertFlag = specialCharacterFoundInsertFlag;
	}
	
	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
