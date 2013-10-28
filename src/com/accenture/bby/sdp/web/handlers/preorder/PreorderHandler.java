package com.accenture.bby.sdp.web.handlers.preorder;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.accenture.bby.sdp.db.PreorderDBWrapper;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.ActionNotAllowedException;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.web.beans.PreorderBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ViewScoped
@ManagedBean (name="preorderHandler")
public class PreorderHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(PreorderHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{preorderTabbedPaneHandler}")
	private PreorderTabbedPaneHandler preorderTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	/*
	 * Model Beans
	 */
	private PreorderBean newPreorderBean;
	private PreorderBean oldPreorderBean;
	
	/*
	 * Preorder screen search fields
	 */
	private String productSkuLookupField;
	
	/*
	 * Page Message Flags
	 */
	private static final int DEFAULT = 0;
	private static final int PRODUCT_NOT_FOUND = 1;
	private static final int PRODUCT_NOT_EDITABLE = 2;
	private static final int PRODUCT_HAS_PREORDER_SCHEDULED = 3;
	private static final int specialCharacterFoundFlag = 4;
	private int messageFlag = DEFAULT;
	
	/*
	 * getters
	 */
	public PreorderTabbedPaneHandler getPreorderTabbedPaneHandler() { return preorderTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public PreorderBean getNewPreorderBean() { return newPreorderBean; }
	public PreorderBean getOldPreorderBean() { return oldPreorderBean; }
	public String getProductSkuLookupField() { return productSkuLookupField; }
	public boolean isProductNotFound() { return messageFlag == PRODUCT_NOT_FOUND; }
	public boolean isPreorderNotEditable() { return messageFlag == PRODUCT_NOT_EDITABLE; }
	public boolean isProductAlreadyHasPreorderScheduled() { return messageFlag == PRODUCT_HAS_PREORDER_SCHEDULED; }
	public boolean isSpecialCharacterFoundFlag() { return messageFlag == specialCharacterFoundFlag; }
	
	/*
	 * setters
	 */
	public void setPreorderTabbedPaneHandler(PreorderTabbedPaneHandler preorderTabbedPaneHandler) { this.preorderTabbedPaneHandler = preorderTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewPreorderBean(PreorderBean newPreorderBean) { this.newPreorderBean = newPreorderBean; }
	public void setOldPreorderBean(PreorderBean oldPreorderBean) { this.oldPreorderBean = oldPreorderBean; }
	public void setProductSkuLookupField(String productSkuLookupField) { this.productSkuLookupField = productSkuLookupField; }
	
	/*
	 * Action Listeners
	 */
	public String createPreorderSchedulerButtonClick() {
		// reset the message flags
		messageFlag = DEFAULT;
		preorderTabbedPaneHandler.displayLookupTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String saveChangesButtonClick() {
		try {
			logger.log(Level.INFO, "BEGIN: Save pre-order changes.");
			if (!this.oldPreorderBean.isEditAllowed()) {
				Throwable e = new ActionNotAllowedException("Street date is less than the current timestamp.");
				exceptionHandler.initialize(e, "Updating pre-order schedule is not allowed at this time.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} else {
				captureEditAuditLog();
				updatePreorder();
				preorderTabbedPaneHandler.displaySearchTab();
				logger.log(Level.INFO, "FINISH: Save pre-order changes.");
				return NavigationStrings.PREORDER_DEFAULT_PAGE;
			}
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. Pre-order updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while updating pre-order record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String saveNewScheduleButtonClick() {
		try {
			logger.log(Level.INFO, "BEGIN: Save new pre-order.");
			captureInsertAuditLog();
			insertPreorder();
			preorderTabbedPaneHandler.displaySearchTab();
			logger.log(Level.INFO, "FINISH: Save new pre-order.");
			return NavigationStrings.PREORDER_DEFAULT_PAGE;
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. Pre-order updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while inserting pre-order record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String removePreorderButtonClick() {
		try {
			logger.log(Level.INFO, "BEGIN: Remove pre-order.");
			if (!this.oldPreorderBean.isDeleteAllowed()) {
				Throwable e = new ActionNotAllowedException("Record status not released.");
				exceptionHandler.initialize(e, "Deleting pre-order schedule is not allowed at this time.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} else {
				captureDeleteAuditLog();
				removePreorder();
				preorderTabbedPaneHandler.displaySearchTab();
				logger.log(Level.INFO, "FINISH: Remove pre-order.");
				return NavigationStrings.PREORDER_DEFAULT_PAGE;
			}
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. Pre-order updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while removing pre-order record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (MissingPropertyException e) {
			exceptionHandler.initialize(e, "Missing sdp-config property [sdp.sdpui.preorder.status.released].");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String cancelChangesButtonClick() {
		// No action to take, just display the search tab.
		preorderTabbedPaneHandler.displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String retrieveCatalogDataButtonClick() {
		try {
			
			if (TextFilter.isSpecialCharacterPresent(this.productSkuLookupField)) {
				logger.log(Level.DEBUG,
				"Special Character found. specialCharacterFoundFlag set.");
				messageFlag = specialCharacterFoundFlag;
				
			} else {PreorderBean tmp = PreorderDBWrapper.getProductByProductSku(this.productSkuLookupField);
			if (tmp == null) {
				messageFlag = PRODUCT_NOT_FOUND; // displays product not found message
				preorderTabbedPaneHandler.displayLookupTab();
			} else if (tmp.getStreetDate() != null && tmp.getStreetDate().before(new Date())) {
				messageFlag = PRODUCT_NOT_EDITABLE; // displays product not editable message
				preorderTabbedPaneHandler.displayLookupTab();
			} else {
				this.setOldPreorderBean(new PreorderBean(tmp));
				this.setNewPreorderBean(new PreorderBean(tmp));
				if (this.getOldPreorderBean().getStreetDate() != null) {
					messageFlag = PRODUCT_HAS_PREORDER_SCHEDULED; // displays preorder already configured message
					preorderTabbedPaneHandler.displayEditTab(); // update the existing record
				} else {
					this.getNewPreorderBean().setStreetDate(getDefaultDate());
					messageFlag = DEFAULT; // displays no messages
					preorderTabbedPaneHandler.displayCreateTab(); // create new preorder record
				}
			}
			}
			return NavigationStrings.CURRENT_VIEW;
			
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while retrieving record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		
	}
	
	/**
	 * Captures audit log on new pre-orders.
	 * Remove street date from oldPreorderBean before capturing audit log
	 * so that default values do not get captured as original values.
	 * After audit log is captured, copy the values back.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureInsertAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		Date tmpDate = this.oldPreorderBean.getStreetDate();
		this.oldPreorderBean.setStreetDate(null);
		AuditUtil.audit(Action.CREATED_PREORDER_SCHEDULE, newPreorderBean, oldPreorderBean);
		this.oldPreorderBean.setStreetDate(tmpDate);
	}
	
	/**
	 * Captures audit log on pre-order updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureEditAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_PREORDER_SCHEDULE, newPreorderBean, oldPreorderBean);
	}
	
	/**
	 * Captures audit log on pre-order deletions.
	 * Remove street date from newPreorderBean before capturing audit log
	 * so that it correctly logs as these fields are deleted.
	 * After audit log is captured, copy the values back.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureDeleteAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		Date tmpDate = this.newPreorderBean.getStreetDate();
		this.newPreorderBean.setStreetDate(null);
		AuditUtil.audit(Action.DELETED_PREORDER_SCHEDULE, newPreorderBean, oldPreorderBean);
		this.newPreorderBean.setStreetDate(tmpDate);
	}
	
	private void insertPreorder() throws DataSourceLookupException, DataAccessException {
		PreorderDBWrapper.insert(this.getNewPreorderBean().getCatalogId(), this.getNewPreorderBean().getStreetDate(), this.getUserManager().getUserBean().getUsername());
	}
	
	private void updatePreorder() throws DataSourceLookupException, DataAccessException {
		PreorderDBWrapper.update(this.getNewPreorderBean().getCatalogId(), this.getNewPreorderBean().getStreetDate(), this.getUserManager().getUserBean().getUsername());
	}
	
	private void removePreorder() throws DataSourceLookupException, DataAccessException {
		PreorderDBWrapper.delete(this.getNewPreorderBean().getCatalogId(), this.getUserManager().getUserBean().getUsername());
	}
	
	/**
	 * @return current date without a time value
	 */
	private static Date getDefaultDate() {
		return DateUtil.clearTime(DateUtil.getNextDay(new Date()));
	}
	
}
