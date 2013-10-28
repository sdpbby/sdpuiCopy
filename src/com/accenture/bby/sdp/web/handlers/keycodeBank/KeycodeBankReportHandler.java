package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ViewScoped
@ManagedBean (name="keycodeBankReportHandler")
public class KeycodeBankReportHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static final Logger logger = Logger.getLogger(KeycodeBankReportHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankReportTabbedPaneHandler}")
	private KeycodeBankReportTabbedPaneHandler keycodeBankReportTabbedPaneHandler;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	/*
	 * getters
	 */
	public KeycodeBankReportTabbedPaneHandler getKeycodeBankReportTabbedPaneHandler() { return keycodeBankReportTabbedPaneHandler; }
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	
	/*
	 * setters
	 */
	public void setKeycodeBankReportTabbedPaneHandler(KeycodeBankReportTabbedPaneHandler keycodeBankReportTabbedPaneHandler) { this.keycodeBankReportTabbedPaneHandler = keycodeBankReportTabbedPaneHandler; }
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	
	public String dashboardButtonClick() {
		keycodeBankReportTabbedPaneHandler.displayDashboardTab();
		return NavigationStrings.KEYCODEBANK_DEFAULT_PAGE;
	}
	

	public String catalogButtonClick() {
		return NavigationStrings.KEYCODEBANK_CATALOG_DEFAULT_PAGE;
	}
	
	public String loadButtonClick() {
		return NavigationStrings.KEYCODEBANK_LOAD_DEFAULT_PAGE;
	}
	
	public String manageLoadButtonClick() {
		return NavigationStrings.KEYCODEBANK_MANAGELOAD_DEFAULT_PAGE;
	}
	
}
