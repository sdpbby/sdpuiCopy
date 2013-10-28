package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import com.accenture.bby.sdp.db.KcbCatalogDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

/**
 * @author a719057
 * 
 */
@ViewScoped
@ManagedBean(name = "keycodeBankProductCatalogHandler")
public class KeycodeBankProductCatalogHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(KeycodeBankProductCatalogHandler.class.getName());

	/*
	 * Managed Properties
	 */
	@ManagedProperty(value = "#{keycodeBankProductCatalogTabbedPaneHandler}")
	private KeycodeBankProductCatalogTabbedPaneHandler keycodeBankProductCatalogTabbedPaneHandler;
	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty(value = "#{userManagerBean}")
	private UserManager userManager;

	/*
	 * Model Beans
	 */
	private KcbProductBean newKcbProductBean;
	private KcbProductBean oldKcbProductBean;

	private boolean specialCharacterFoundInsertFlag;

	/*
	 * getters
	 */
	public KeycodeBankProductCatalogTabbedPaneHandler getKeycodeBankProductCatalogTabbedPaneHandler() {
		return keycodeBankProductCatalogTabbedPaneHandler;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public KcbProductBean getNewKcbProductBean() {
		return newKcbProductBean;
	}

	public KcbProductBean getOldKcbProductBean() {
		return oldKcbProductBean;
	}

	/*
	 * setters
	 */
	public void setKeycodeBankProductCatalogTabbedPaneHandler(
			KeycodeBankProductCatalogTabbedPaneHandler keycodeBankProductCatalogTabbedPaneHandler) {
		this.keycodeBankProductCatalogTabbedPaneHandler = keycodeBankProductCatalogTabbedPaneHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setNewKcbProductBean(KcbProductBean newKcbProductBean) {
		this.newKcbProductBean = newKcbProductBean;
	}

	public void setOldKcbProductBean(KcbProductBean oldKcbProductBean) {
		this.oldKcbProductBean = oldKcbProductBean;
	}

	/*
	 * Action Listeners
	 */
	public String saveChangesButtonClick() {
		if (displayDuplicateValueError || displaySpecialCharacterError || displayRelatedSkuDupValueError || displayRelatedSkuSpecCharError) {
			return NavigationStrings.CURRENT_VIEW;
		}else{
		boolean specialCharFoundFlag = false;
		if (isNotNull(this.getNewKcbProductBean().getMerchandiseSku()) && TextFilter.isSpecialCharacterPresent(this.getNewKcbProductBean().getMerchandiseSku().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewKcbProductBean().getNonMerchandiseSku()) && TextFilter.isSpecialCharacterPresent(this.getNewKcbProductBean().getNonMerchandiseSku().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewKcbProductBean().getMasterItemId()) && TextFilter.isSpecialCharacterPresent(this.getNewKcbProductBean().getMasterItemId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewKcbProductBean().getDescription()) && TextFilter.isSpecialCharacterPresent(this.getNewKcbProductBean().getDescription().trim())) {
			specialCharFoundFlag= true;
		}
		
		if(specialCharFoundFlag){
			specialCharacterFoundInsertFlag =true;
			logger.log(Level.DEBUG, "specialCharacterFoundInsertFlag :: "+isSpecialCharacterFoundInsertFlag());
			
			return NavigationStrings.CURRENT_VIEW;
		}else{ 
		try {
			if (this.getOldKcbProductBean() == null
					|| this.getOldKcbProductBean().getProductId() == null) {
				if (this.getNewKcbProductBean().getRelatedSKU().trim() != null
						&& this.getNewKcbProductBean().getRelatedSKU().trim()
								.length() > 0) {
					// to check duplicate SKU
					if (duplicateSkuCheck()) {
						FacesContext context = FacesContext
								.getCurrentInstance();
						context
								.addMessage(
										"vendor_code_edit:relatedSku",
										new FacesMessage(
												"Related SKU present in database please enter unique Related SKU."));
						return NavigationStrings.CURRENT_VIEW;
					}
				}
				captureInsertAuditLog();
				insertProduct();

			} else {
				if ((this.getNewKcbProductBean().getRelatedSKU().trim() != null
						&& this.getNewKcbProductBean().getRelatedSKU().trim()
								.length() > 0) && this
								.getNewKcbProductBean().getProductId() != null) {
					KcbCatalogDBWrapper.deleteRelatedSKU(this
							.getNewKcbProductBean().getProductId());
				}
				if (this.getNewKcbProductBean().getRelatedSKU().trim() != null
						&& this.getNewKcbProductBean().getRelatedSKU().trim()
								.length() > 0) {

					if (duplicateSkuCheck()) {
						if ((this.getOldKcbProductBean().getRelatedSKU().trim() != null
								 && this.getOldKcbProductBean().getRelatedSKU().trim()
								.length() > 0)
								&& this.getOldKcbProductBean().getProductId() != null) {
							KcbCatalogDBWrapper.insertRelatedSKU(this
									.getOldKcbProductBean().getProductId(),
									this.getOldKcbProductBean().getRelatedSKU()
											.trim(), this
											.getOldKcbProductBean()
											.getCreatedByUserId());
						}
						FacesContext context = FacesContext
								.getCurrentInstance();
						context
								.addMessage(
										"vendor_code_edit:relatedSku",
										new FacesMessage(
												"Related SKU present in database please enter unique Related SKU."));
						return NavigationStrings.CURRENT_VIEW;
					}
				}
				captureEditAuditLog();
				updateProduct();

			}
			keycodeBankProductCatalogTabbedPaneHandler.displaySearchTab();

		} catch (AuditTrailException e) {
			exceptionHandler
					.initialize(e,
							"Failed to create audit trail record. KCB updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while updating product record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}

		logger.log(Level.INFO, "FINISH: Save kcb product changes.");
		return NavigationStrings.KEYCODEBANK_CATALOG_DEFAULT_PAGE;
		}
		}
	}

	private boolean duplicateSkuCheck() throws DataAccessException,
			DataSourceLookupException {
		return KcbCatalogDBWrapper.duplicateSkuCheck(this
				.getNewKcbProductBean().getRelatedSKU());
	}

	public String cancelChangesButtonClick() {
		// No action to take, just display the search tab.
		keycodeBankProductCatalogTabbedPaneHandler.displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}

	/**
	 * Captures audit log on vendor inserts.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	private void captureInsertAuditLog() throws AuditTrailException,
			DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.CREATED_KCB_PRODUCT_CONFIGURATION,
				newKcbProductBean, oldKcbProductBean);
	}

	/**
	 * Captures audit log on vendor updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	private void captureEditAuditLog() throws AuditTrailException,
			DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_KCB_PRODUCT_CONFIGURATION,
				newKcbProductBean, oldKcbProductBean);
	}

	private void insertProduct() throws DataSourceLookupException,
			DataAccessException {
		this.getNewKcbProductBean().setCreatedByUserId(
				this.getUserManager().getUserBean().getUsername());
		this.getNewKcbProductBean().setUpdatedByUserId(
				this.getUserManager().getUserBean().getUsername());
		this.getNewKcbProductBean().setCreatedDate(new Date());
		this.getNewKcbProductBean().setUpdatedDate(new Date());
		logger.log(Level.INFO, "BEGIN: Save kcb product changes: \n\nOLD:\n"
				+ this.getOldKcbProductBean() + "\nNEW:\n"
				+ this.getNewKcbProductBean());
		final Integer productID = KcbCatalogDBWrapper.insertProduct(this
				.getNewKcbProductBean());
		if ((this.getNewKcbProductBean().getRelatedSKU().trim() != null 
				&& this.getNewKcbProductBean().getRelatedSKU().trim().length() > 0)
				&& productID != null) {
			KcbCatalogDBWrapper.insertRelatedSKU(productID, this
					.getNewKcbProductBean().getRelatedSKU().trim(), this
					.getNewKcbProductBean().getCreatedByUserId());
		}
	}

	private void updateProduct() throws DataSourceLookupException,
			DataAccessException {
		this.getNewKcbProductBean().setUpdatedByUserId(
				this.getUserManager().getUserBean().getUsername());
		this.getNewKcbProductBean().setUpdatedDate(new Date());
		logger.log(Level.INFO, "BEGIN: Save kcb product changes: \n\nOLD:\n"
				+ this.getOldKcbProductBean() + "\nNEW:\n"
				+ this.getNewKcbProductBean());
		KcbCatalogDBWrapper.updateProduct(this.getNewKcbProductBean());
		if ((this.getNewKcbProductBean().getRelatedSKU().trim() != null 
				&& this.getNewKcbProductBean().getRelatedSKU().trim().length() > 0)
				&& this.getNewKcbProductBean().getProductId() != null) {
			KcbCatalogDBWrapper.insertRelatedSKU(this.getNewKcbProductBean()
					.getProductId(), this.getNewKcbProductBean()
					.getRelatedSKU().trim(), this.getNewKcbProductBean()
					.getCreatedByUserId());
		}
	}

	public String createCatalogButtonClick() {
		this.setOldKcbProductBean(new KcbProductBean());
		this.setNewKcbProductBean(new KcbProductBean());
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displayEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}

	public String dashboardButtonClick() {
		return NavigationStrings.KEYCODEBANK_DEFAULT_PAGE;
	}

	/*
	 * Constructor
	 */
	public KeycodeBankProductCatalogHandler() {
	}

	private boolean displayDuplicateValueError;

	private boolean displaySpecialCharacterError;

	public boolean isDisplayDuplicateValueError() {
		return displayDuplicateValueError;
	}

	public void setDisplayDuplicateValueError(boolean displayDuplicateValueError) {
		this.displayDuplicateValueError = displayDuplicateValueError;
	}

	public void validateProduct(ValueChangeEvent e) throws DataAccessException,
			DataSourceLookupException {
		displayDuplicateValueError = false;
		displaySpecialCharacterError = false;
		if (e != null && e.getNewValue() != null) {
			if(TextFilter.isSpecialCharacterPresent(e.getNewValue().toString())){
				displaySpecialCharacterError = true;
			}else if (this.getOldKcbProductBean() == null
					|| this.getOldKcbProductBean().getMerchandiseSku() == null
					|| !this.getOldKcbProductBean().getMerchandiseSku().equals(
							e.getNewValue().toString())) {

				KcbProductBean[] products = KcbCatalogDBWrapper
						.getProductsBySku(e.getNewValue().toString());
				if (products.length > 0) {
					displayDuplicateValueError = true;
				}
			}
		}
	}

	public boolean isDisplaySpecialCharacterError() {
		return displaySpecialCharacterError;
	}

	public void setDisplaySpecialCharacterError(boolean displaySpecialCharacterError) {
		this.displaySpecialCharacterError = displaySpecialCharacterError;
	}

	private boolean displayRelatedSkuDupValueError;
	private boolean displayRelatedSkuSpecCharError;
	public void validateRelatedSKU(ValueChangeEvent e) throws DataAccessException, DataSourceLookupException{
		displayRelatedSkuDupValueError = false;
		displayRelatedSkuSpecCharError = false;
		if (e != null && e.getNewValue() != null) {
			if(TextFilter.isSpecialCharacterPresent(e.getNewValue().toString())){
				displayRelatedSkuSpecCharError = true;
			}else if (KcbCatalogDBWrapper.duplicateSkuCheck(e.getNewValue().toString())) {
				displayRelatedSkuDupValueError = true;
			}
		}
	}

	public boolean isDisplayRelatedSkuSpecCharError() {
		return displayRelatedSkuSpecCharError;
	}

	public void setDisplayRelatedSkuSpecCharError(
			boolean displayRelatedSkuSpecCharError) {
		this.displayRelatedSkuSpecCharError = displayRelatedSkuSpecCharError;
	}

	public boolean isDisplayRelatedSkuDupValueError() {
		return displayRelatedSkuDupValueError;
	}

	public void setDisplayRelatedSkuDupValueError(
			boolean displayRelatedSkuDupValueError) {
		this.displayRelatedSkuDupValueError = displayRelatedSkuDupValueError;
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
