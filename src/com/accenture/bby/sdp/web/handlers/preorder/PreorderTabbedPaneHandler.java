package com.accenture.bby.sdp.web.handlers.preorder;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="preorderTabbedPaneHandler")
@ViewScoped
public class PreorderTabbedPaneHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int SEARCH_INDEX = 0;
	private static final int LOOKUP_INDEX = 1;
	private static final int EDIT_INDEX = 2;
	private static final int DELETE_INDEX = 3;
	private static final int CREATE_INDEX = 4;
	
	public PreorderTabbedPaneHandler() {
		index = SEARCH_INDEX;
	}
	
	// action listeners that set the current tab
	public void displaySearchTab() { index = SEARCH_INDEX; }
	public void displayLookupTab() { index = LOOKUP_INDEX; }
	public void displayEditTab() { index = EDIT_INDEX; }
	public void displayDeleteTab() { index = DELETE_INDEX; }
	public void displayCreateTab() { index = CREATE_INDEX; }
	
	// methods for determining the current tab
	public boolean isSearchTabCurrent() { return index == SEARCH_INDEX; }
	public boolean isLookupTabCurrent() { return index == LOOKUP_INDEX; }
	public boolean isEditTabCurrent() { return index == EDIT_INDEX; }
	public boolean isDeleteTabCurrent() { return index == DELETE_INDEX; }
	public boolean isCreateTabCurrent() { return index == CREATE_INDEX; }
	
}
