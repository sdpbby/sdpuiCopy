package com.accenture.bby.sdp.web.handlers.directToVendor;

import java.io.Serializable;

public class DirectToVendorTabHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5270077698556225678L;

	private static final int OPERATION_INDEX = 0;
	private static final int FORM_INDEX = 1;
	private static final int SUBMITTED_INDEX = 2;
	private int index = OPERATION_INDEX;

	public boolean isOperationsTabCurrent() {
		return index == OPERATION_INDEX;
	}

	public boolean isFormTabCurrent() {
		return index == FORM_INDEX;
	}

	public boolean isFormSubmittedTabCurrent() {
		return index == SUBMITTED_INDEX;
	}

	public void displayOperationsTab() {
		index = OPERATION_INDEX;
	}

	public void displayFormTab() {
		index = FORM_INDEX;
	}

	public void displayFormSubmittedTab() {
		index = SUBMITTED_INDEX;
	}

}
