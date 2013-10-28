package com.accenture.bby.sdp.web.handlers.vendor;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="vendorCodeTabbedPaneHandler")
@ViewScoped
public class VendorCodeTabbedPaneHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	private static final int VENDOR_CODE_DEFAULT_INDEX = 1;
	private static final int VENDOR_CODE_EIDT_INDEX = 2;

	
	public VendorCodeTabbedPaneHandler() {
		index = VENDOR_CODE_DEFAULT_INDEX;
	}
	
	// action listeners that set the current tab
	public void displayVendorCodeDefaultTab() { index = VENDOR_CODE_DEFAULT_INDEX; }
	public void displayVendorCodeEditTab() { index = VENDOR_CODE_EIDT_INDEX; }
	
	// methods for determining the current tab
	public boolean isDefaultTabCurrent() { return index == VENDOR_CODE_DEFAULT_INDEX; }
	public boolean isEditTabCurrent() { return index == VENDOR_CODE_EIDT_INDEX; }
	
}
