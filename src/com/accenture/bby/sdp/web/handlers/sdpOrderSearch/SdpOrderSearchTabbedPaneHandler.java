package com.accenture.bby.sdp.web.handlers.sdpOrderSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="sdpOrderSearchTabbedPaneHandler")
@ViewScoped
public class SdpOrderSearchTabbedPaneHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int SEARCH_INDEX = 0;
	private static final int SEARCHRESULT_INDEX = 1;
	private static final int SEARCHVENDORRESULT_INDEX = 2;
	
	public SdpOrderSearchTabbedPaneHandler() {
		index = SEARCH_INDEX;
	}
	
	// action listeners that set the current tab
	public void displaySearchTab() { index = SEARCH_INDEX; }
	public void displaySearchResultTab() { index = SEARCHRESULT_INDEX; }
	public void displaySearchVendorResultTab() { index = SEARCHVENDORRESULT_INDEX; }
	
	// methods for determining the current tab
	public boolean isSearchTabCurrent() { return index == SEARCH_INDEX; }
	public boolean isSearchResultTabCurrent() { return index == SEARCHRESULT_INDEX; }
	public boolean isSearchVendorResultTabCurrent() { return index == SEARCHVENDORRESULT_INDEX; }

}
