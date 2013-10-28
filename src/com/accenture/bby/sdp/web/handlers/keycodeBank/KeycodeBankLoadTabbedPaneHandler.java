package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean (name="keycodeBankLoadTabbedPaneHandler")
@ViewScoped
public class KeycodeBankLoadTabbedPaneHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int FORM_INDEX = 0;
	private static final int VALIDATE_INDEX = 1;
	private static final int COMPLETE_INDEX = 2;
	
	public KeycodeBankLoadTabbedPaneHandler() {
		index = FORM_INDEX;
	}
	
	// action listeners that set the current tab
	public void displayFormTab() { index = FORM_INDEX; }
	public void displayValidateTab() { index = VALIDATE_INDEX; }
	public void displayCompleteTab() { index = COMPLETE_INDEX; }
	
	// methods for determining the current tab
	public boolean isFormTabCurrent() { return index == FORM_INDEX; }
	public boolean isValidateTabCurrent() { return index == VALIDATE_INDEX; }
	public boolean isCompleteTabCurrent() { return index == COMPLETE_INDEX; }
	
}
