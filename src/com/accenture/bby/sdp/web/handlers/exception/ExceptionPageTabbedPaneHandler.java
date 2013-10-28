package com.accenture.bby.sdp.web.handlers.exception;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="exceptionPageTabbedPaneHandler")
@ViewScoped
public class ExceptionPageTabbedPaneHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int SEARCH_INDEX = 0;
	private static final int DETAIL_INDEX = 1;
	
	public ExceptionPageTabbedPaneHandler() {
		index = SEARCH_INDEX;
	}
	
	// action listeners that set the current tab
	public void displaySearchTab() { index = SEARCH_INDEX; }
	public void displayViewTab() { index = DETAIL_INDEX; }
	
	// methods for determining the current tab
	public boolean isSearchTabCurrent() { return index == SEARCH_INDEX; }
	public boolean isViewTabCurrent() { return index == DETAIL_INDEX; }

}
