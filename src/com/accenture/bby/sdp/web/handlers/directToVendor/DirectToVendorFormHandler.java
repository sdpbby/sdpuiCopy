package com.accenture.bby.sdp.web.handlers.directToVendor;

import java.io.Serializable;

public class DirectToVendorFormHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5270077698556225678L;

	private static final int DEFAULT_FORM_INDEX = 0;
	private static final int ACT_S2_INDEX = 1;
	private static final int CNL_S2_INDEX = 3;
	private static final int RNW_S2_INDEX = 4;
	private static final int UPD_S2_INDEX = 5;
	private static final int ACT_PSA_INDEX = 6;
	private static final int CNL_PSA_INDEX = 7;
	private static final int ACT_POSA_INDEX = 8;
	private static final int CNL_POSA_INDEX = 9;
	private int index = DEFAULT_FORM_INDEX;

	public boolean isDefaultFormCurrent() {
		return index == DEFAULT_FORM_INDEX;
	}

	public boolean isActivateS2FormCurrent() {
		return index == ACT_S2_INDEX;
	}

	public boolean isCancelS2FormCurrent() {
		return index == CNL_S2_INDEX;
	}

	public boolean isRenewS2FormCurrent() {
		return index == RNW_S2_INDEX;
	}

	public boolean isUpdateStatusS2FormCurrent() {
		return index == UPD_S2_INDEX;
	}

	public boolean isActivatePsaFormCurrent() {
		return index == ACT_PSA_INDEX;
	}

	public boolean isCancelPsaFormCurrent() {
		return index == CNL_PSA_INDEX;
	}

	public boolean isActivatePosaFormCurrent() {
		return index == ACT_POSA_INDEX;
	}

	public boolean isCancelPosaFormCurrent() {
		return index == CNL_POSA_INDEX;
	}

	public void displayDefaultForm() {
		index = DEFAULT_FORM_INDEX;
	}

	public void displayActivateS2Form() {
		index = ACT_S2_INDEX;
	}

	public void displayCancelS2Form() {
		index = CNL_S2_INDEX;
	}

	public void displayRenewS2Form() {
		index = RNW_S2_INDEX;
	}

	public void displayUpdateStatusS2Form() {
		index = UPD_S2_INDEX;
	}

	public void displayActivatePsaForm() {
		index = ACT_PSA_INDEX;
	}

	public void displayCancelPsaForm() {
		index = CNL_PSA_INDEX;
	}

	public void displayActivatePosaForm() {
		index = ACT_POSA_INDEX;
	}

	public void displayCancelPosaForm() {
		index = CNL_POSA_INDEX;
	}

}
