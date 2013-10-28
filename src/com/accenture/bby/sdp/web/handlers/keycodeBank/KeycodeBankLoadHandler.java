package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.icefaces.component.fileentry.FileEntry;
import org.icefaces.component.fileentry.FileEntryEvent;
import org.icefaces.component.fileentry.FileEntryResults;

import com.accenture.bby.sdp.db.KcbBatchLoadDBWrapper;
import com.accenture.bby.sdp.db.KcbCatalogDBWrapper;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.KeycodeLoadException;
import com.accenture.bby.sdp.utl.keycode.KeycodeCsvValidator;
import com.accenture.bby.sdp.utl.keycode.KeycodeFileValidator;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.beans.KeycodeBatchBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

/**
 * @author a719057
 *
 */
@ViewScoped
@ManagedBean (name="keycodeBankLoadHandler")
public class KeycodeBankLoadHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(KeycodeBankLoadHandler.class.getName());
	
	private KeycodeFileValidator validator;
	public KeycodeFileValidator getValidator() { return validator; }
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankLoadTabbedPaneHandler}")
	private KeycodeBankLoadTabbedPaneHandler keycodeBankLoadTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	/*
	 * Page Message Flags
	 */
	private static final int DEFAULT = 0;
	private static final int FILE_NOT_FOUND = 1;
	private static final int FILE_NOT_VALID = 2;
	private static final int INPUT_NOT_VALID = 3;
	private static final int specialCharacterFoundInsertFlag = 4;
	private int messageFlag = DEFAULT;
	
	private String customMessage = null;
	
	/*
	 * audit trail
	 */
	private KeycodeBatchBean newKeycodeBatchBean = new KeycodeBatchBean();
	private KeycodeBatchBean oldKeycodeBatchBean = new KeycodeBatchBean();
	
	/*
	 * getters
	 */
	public KeycodeBankLoadTabbedPaneHandler getKeycodeBankLoadTabbedPaneHandler() { return keycodeBankLoadTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public boolean isFileNotFound() { return messageFlag == FILE_NOT_FOUND; }
	public boolean isFileNotValid() { return messageFlag == FILE_NOT_VALID; }
	public boolean isInputNotValid() { return messageFlag == INPUT_NOT_VALID; }
	public boolean isSpecialCharacterFoundInsertFlag() { return messageFlag == specialCharacterFoundInsertFlag; }
	public boolean isCustomMessageSet() { return customMessage != null && customMessage.length() > 0; }
	public String getCustomMessage() { return customMessage; }
	public KeycodeBatchBean getNewKeycodeBatchBean() { return newKeycodeBatchBean; }
	public KeycodeBatchBean getOldKeycodeBatchBean() { return oldKeycodeBatchBean; }
	
	/*
	 * setters
	 */
	public void setKeycodeBankLoadTabbedPaneHandler(KeycodeBankLoadTabbedPaneHandler keycodeBankLoadTabbedPaneHandler) { this.keycodeBankLoadTabbedPaneHandler = keycodeBankLoadTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewKeycodeBatchBean(KeycodeBatchBean newKeycodeBatchBean) { this.newKeycodeBatchBean = newKeycodeBatchBean; }
	public void setOldKeycodeBatchBean(KeycodeBatchBean oldKeycodeBatchBean) { this.oldKeycodeBatchBean = oldKeycodeBatchBean; }

	/*
	 * action handlers
	 */
	public void listener(FileEntryEvent event) throws DataSourceLookupException, DataAccessException {
		FileEntry fileEntry = (FileEntry) event.getSource();
		FileEntryResults results = fileEntry.getResults();
		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			if (fileInfo.isSaved()) {
				try {
					validator = new KeycodeCsvValidator(fileInfo.getFile(), fileInfo.getFileName());
					messageFlag = DEFAULT;
					customMessage = "";
				} catch (FileNotFoundException e) {
					logger.log(Level.ERROR, "Upload file not found.", e);
					messageFlag = FILE_NOT_FOUND;
					customMessage = e.getMessage();
				} catch (KeycodeLoadException e) {
					logger.log(Level.WARN, "Upload file failed validation.", e);
					messageFlag = FILE_NOT_VALID;
					customMessage = e.getMessage();
				}
			}
		}
	}
	
	public String dashboardButtonClick() {
		return NavigationStrings.KEYCODEBANK_DEFAULT_PAGE;
	}
	
	public String validateFileButtonClick() {
		messageFlag = DEFAULT;
		boolean specialCharFoundFlag = false;
		if (isNotNull(this.getNewKeycodeBatchBean().getMerchandiseSku()) && TextFilter.isSpecialCharacterPresent(this.getNewKeycodeBatchBean().getMerchandiseSku())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewKeycodeBatchBean().getNonMerchandiseSku()) && TextFilter.isSpecialCharacterPresent(this.getNewKeycodeBatchBean().getNonMerchandiseSku())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getNewKeycodeBatchBean().getMasterItemId()) && TextFilter.isSpecialCharacterPresent(this.getNewKeycodeBatchBean().getMasterItemId())) {
			specialCharFoundFlag= true;
		}
		logger.log(Level.INFO, "BEGIN: File upload validation.");
		if (this.getNewKeycodeBatchBean().getVendorId() == null || (this.getNewKeycodeBatchBean().getMerchandiseSku() == null && this.getNewKeycodeBatchBean().getNonMerchandiseSku() == null && this.getNewKeycodeBatchBean().getMasterItemId() == null)) {
			messageFlag = INPUT_NOT_VALID;
			customMessage = "Must provide at least one of the following: Merchandise SKU, Non-Merchandise SKU, Master Item ID.";
			this.getKeycodeBankLoadTabbedPaneHandler().displayFormTab();
		} else if(specialCharFoundFlag){
			messageFlag= specialCharacterFoundInsertFlag;
			return NavigationStrings.CURRENT_VIEW;
		}else if (validator != null && validator.isValid()) {
			try {
				KcbProductBean[] products = KcbCatalogDBWrapper.getAllProducts(this.getNewKeycodeBatchBean().getVendorId(), this.getNewKeycodeBatchBean().getMerchandiseSku(), this.getNewKeycodeBatchBean().getNonMerchandiseSku(), this.getNewKeycodeBatchBean().getMasterItemId());
				if (products != null && products.length == 1) {			
					if (products[0].getProductId().equals(validator.getProducts()[0].getProductId())) {
						// Success Path
						logger.log(Level.INFO, "Keycode upload file passed validation!");
						this.getNewKeycodeBatchBean().setBatchLoadId(KcbBatchLoadDBWrapper.getNextBatchLoadId());
						this.getNewKeycodeBatchBean().setMerchandiseSku(products[0].getMerchandiseSku());
						this.getNewKeycodeBatchBean().setProductId(products[0].getProductId());
						this.getNewKeycodeBatchBean().setNonMerchandiseSku(products[0].getNonMerchandiseSku());
						this.getNewKeycodeBatchBean().setMasterItemId(products[0].getMasterItemId());
						this.getNewKeycodeBatchBean().setDescription(products[0].getDescription());
						this.getNewKeycodeBatchBean().setFileName(validator.getFileName());
						this.getNewKeycodeBatchBean().setCreatedByUserId(this.getUserManager().getUserBean().getUsername());
						this.getNewKeycodeBatchBean().setCreatedDate(new Date());
						this.getNewKeycodeBatchBean().setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
						this.getNewKeycodeBatchBean().setUpdatedDate(new Date());
						this.getKeycodeBankLoadTabbedPaneHandler().displayValidateTab();
					} else {
						logger.log(Level.WARN, "Input values did not match values in the upload file.");
						messageFlag = INPUT_NOT_VALID;
						customMessage = "Input values did not match values in the upload file.";
						this.getKeycodeBankLoadTabbedPaneHandler().displayFormTab();
					}
				} else {
					logger.log(Level.WARN, "Input values did not match any existing products.");
					messageFlag = INPUT_NOT_VALID;
					customMessage = "Input values did not match any existing products.";
					this.getKeycodeBankLoadTabbedPaneHandler().displayFormTab();
				}
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to the data source.");
				this.getExceptionHandler().initialize(e, "Unable to validate product records in KCB catalog at this time. Please notify your site administrator");
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to retrieve product details from database.");
				this.getExceptionHandler().initialize(e, "Unable to validate product records in KCB catalog at this time. Please notify your site administrator");
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		} else {
			logger.log(Level.WARN, "Upload file did not pass validation.");
			messageFlag = INPUT_NOT_VALID;
			customMessage = "Upload file did not pass validation. Make sure the product exists in the keycode bank product catalog.";
			this.getKeycodeBankLoadTabbedPaneHandler().displayFormTab();this.getKeycodeBankLoadTabbedPaneHandler().displayFormTab();
		}
		logger.log(Level.INFO, "FINISH: File upload validation.");
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String backButtonClick() {
		return NavigationStrings.KEYCODEBANK_LOAD_DEFAULT_PAGE;
	}
	
	
	
	public String loadNow() {
		
		try {
			AuditUtil.audit(Action.KCB_LOAD_KEYCODES, newKeycodeBatchBean, oldKeycodeBatchBean);
		} catch (AuditTrailException e) {
			logger.log(Level.ERROR, "Failed to insert audit log.", e);
			this.getExceptionHandler().initialize(e, "Unable to load keycodes at this time. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			logger.log(Level.ERROR, "Failed to insert audit log.", e);
			this.getExceptionHandler().initialize(e, "Unable to load keycodes at this time. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			logger.log(Level.ERROR, "Failed to connect to data source to insert audit log.", e);
			this.getExceptionHandler().initialize(e, "Unable to load keycodes at this time. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		
		try {
			this.setNewKeycodeBatchBean(KcbBatchLoadDBWrapper.insertBatchLoad(this.getNewKeycodeBatchBean()));
		} catch (DataAccessException e) {
			logger.log(Level.ERROR, "Failed to insert create keycode load.", e);
			this.getExceptionHandler().initialize(e, "Unable to load keycodes at this time. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			logger.log(Level.ERROR, "Failed to connect to data source.", e);
			this.getExceptionHandler().initialize(e, "Unable to load keycodes at this time. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		
		try {
			validator.setInvalidKeycodes(KcbBatchLoadDBWrapper.loadKeycodes(this.getNewKeycodeBatchBean().getBatchLoadId(), this.getNewKeycodeBatchBean().getProductId(), validator.getValidKeycodes()));
		} catch (DataAccessException e) {
			logger.log(Level.ERROR, "Failed to load keycodes.", e);
			this.getExceptionHandler().initialize(e, "Failed to load keycodes. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			logger.log(Level.ERROR, "Failed to load keycodes.", e);
			this.getExceptionHandler().initialize(e, "Failed to load keycodes. Please notify your site administrator");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		this.getKeycodeBankLoadTabbedPaneHandler().displayCompleteTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/*
	 * Constructor
	 */
	public KeycodeBankLoadHandler() {
		newKeycodeBatchBean.setMinDate(DateUtil.clearTime(new Date()));
	}
	
	public boolean isFailedKeycodes() {
		return validator != null && validator.getInvalidKeyCount() > 0;
	}
	
	private boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
