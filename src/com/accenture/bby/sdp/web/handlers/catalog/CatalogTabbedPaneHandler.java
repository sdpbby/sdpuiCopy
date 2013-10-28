package com.accenture.bby.sdp.web.handlers.catalog;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="catalogTabbedPaneHandler")
@ViewScoped
public class CatalogTabbedPaneHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int SEARCH_INDEX = 0;
	private static final int VIEW_INDEX = 1;
	private static final int EDIT_INDEX = 2;
	
	public CatalogTabbedPaneHandler() {
		index = SEARCH_INDEX;
	}
	
	// action listeners that set the current tab
	public void displaySearchTab() { index = SEARCH_INDEX; }
	public void displayViewTab() { index = VIEW_INDEX; }
	public void displayEditTab() { index = EDIT_INDEX; }
	
	// methods for determining the current tab
	public boolean isSearchTabCurrent() { return index == SEARCH_INDEX; }
	public boolean isViewTabCurrent() { return index == VIEW_INDEX; }
	public boolean isEditTabCurrent() { return index == EDIT_INDEX; }
	
}
