package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.accenture.bby.sdp.db.KcbBatchLoadDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KeycodeBatchBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

/**
 * @author a719057
 *
 */
@ViewScoped
@ManagedBean (name="keycodeBankManageLoadHandler")
public class KeycodeBankManageLoadHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(KeycodeBankManageLoadHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankManageLoadTabbedPaneHandler}")
	private KeycodeBankManageLoadTabbedPaneHandler keycodeBankManageLoadTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	/*
	 * Model Beans
	 */
	private KeycodeBatchBean newKeycodeBatchBean;
	private KeycodeBatchBean oldKeycodeBatchBean;
	
	/*
	 * getters
	 */
	public KeycodeBankManageLoadTabbedPaneHandler getKeycodeBankManageLoadTabbedPaneHandler() { return keycodeBankManageLoadTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public KeycodeBatchBean getNewKeycodeBatchBean() { return newKeycodeBatchBean; }
	public KeycodeBatchBean getOldKeycodeBatchBean() { return oldKeycodeBatchBean; }
	
	/*
	 * setters
	 */
	public void setKeycodeBankManageLoadTabbedPaneHandler(KeycodeBankManageLoadTabbedPaneHandler keycodeBankManageLoadTabbedPaneHandler) { this.keycodeBankManageLoadTabbedPaneHandler = keycodeBankManageLoadTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewKeycodeBatchBean(KeycodeBatchBean newKeycodeBatchBean) { this.newKeycodeBatchBean = newKeycodeBatchBean; }
	public void setOldKeycodeBatchBean(KeycodeBatchBean oldKeycodeBatchBean) { this.oldKeycodeBatchBean = oldKeycodeBatchBean; }
	

	/*
	 * Action Listeners
	 */
	public String saveChangesButtonClick() {
		
		try {
			
			captureEditAuditLog();
			updateBatchLoadDetails();
		
			keycodeBankManageLoadTabbedPaneHandler.displaySearchTab();

		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. KCB updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while updating batch load record.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}

		logger.log(Level.INFO, "FINISH: Save kcb batch load changes.");
		return NavigationStrings.KEYCODEBANK_MANAGELOAD_DEFAULT_PAGE;
	}
	
	public String cancelChangesButtonClick() {
		// No action to take, just display the search tab.
		keycodeBankManageLoadTabbedPaneHandler.displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
		
	
	/**
	 * Captures audit log on vendor updates.
	 * 
	 * @throws AuditTrailException
	 * @throws DataSourceLookupException 
	 * @throws DataAccessException 
	 */
	private void captureEditAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.UPDATED_KCB_KEYCODE_LOAD, newKeycodeBatchBean, oldKeycodeBatchBean);
	}
	
	private void updateBatchLoadDetails() throws DataSourceLookupException, DataAccessException {
		this.getNewKeycodeBatchBean().setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
		this.getNewKeycodeBatchBean().setUpdatedDate(new Date());
		logger.log(Level.INFO, "BEGIN: Save kcb batch load changes: \n\nOLD:\n" + this.getOldKeycodeBatchBean() + "\nNEW:\n" + this.getNewKeycodeBatchBean());
		KcbBatchLoadDBWrapper.updateBatchLoadDateRange(this.getNewKeycodeBatchBean().getBatchLoadId(), this.getNewKeycodeBatchBean().getMinDate(), this.getNewKeycodeBatchBean().getMaxDate());
	}
	
	public String deleteBatchButtonClick() {

		try {
			
			captureDeleteAuditLog();
			deleteBatchLoad();
		
			keycodeBankManageLoadTabbedPaneHandler.displaySearchTab();

		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to create audit trail record. KCB updates have not started.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failure occurred while deleting batch.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}

		logger.log(Level.INFO, "FINISH: Delete keycode batch load.");
		return NavigationStrings.KEYCODEBANK_MANAGELOAD_DEFAULT_PAGE;
	}
	
	/**
	 * @throws AuditTrailException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	private void captureDeleteAuditLog() throws AuditTrailException, DataAccessException, DataSourceLookupException {
		AuditUtil.audit(Action.DELETED_KCB_KEYCODE_LOAD, newKeycodeBatchBean, oldKeycodeBatchBean);
	}
	
	private void deleteBatchLoad() throws DataAccessException, DataSourceLookupException {
		this.getNewKeycodeBatchBean().setUpdatedByUserId(this.getUserManager().getUserBean().getUsername());
		this.getNewKeycodeBatchBean().setUpdatedDate(new Date());
		logger.log(Level.INFO, "BEGIN: Delete keycode batch load:\n" + this.getOldKeycodeBatchBean());
		KcbBatchLoadDBWrapper.deleteBatchLoadByLoadId(this.getOldKeycodeBatchBean().getBatchLoadId());
	}
	
	
	public String dashboardButtonClick() {
		return NavigationStrings.KEYCODEBANK_DEFAULT_PAGE;
	}
	
}
