package com.accenture.bby.sdp.web.handlers.sdpRequest;

import java.io.Serializable;


public class SdpRequestFormHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5270077698556225678L;

	private static final int DEFAULT_FORM_INDEX = 0;
	private static final int ACT_S2_INDEX = 1;
	private static final int CNL_S2_INDEX = 2;
	private static final int ADJ_S2_INDEX = 3;
	private static final int RACT_S2_INDEX = 4;
	private static final int ACT_PSA_INDEX = 5;
	private static final int CNL_PSA_INDEX = 6;
	private static final int ADJ_PSA_INDEX = 7;
	private static final int RACT_PSA_INDEX = 8;
	private static final int ACT_DIGITAL_INDEX = 9;
	private static final int CNL_DIGITAL_INDEX = 10;
	private static final int ADJ_DIGITAL_INDEX = 11;
	private static final int RACT_DIGITAL_INDEX = 12;
	private static final int ACT_TECH_SUPPORT_INDEX = 13;
	private static final int CNL_TECH_SUPPORT_INDEX = 14;
	private static final int ADJ_TECH_SUPPORT_INDEX = 15;
	private static final int RACT_TECH_SUPPORT_INDEX = 16;
	private int index = DEFAULT_FORM_INDEX;
	
	public boolean isDefaultFormCurrent() { return index == DEFAULT_FORM_INDEX; }
	public boolean isFulfillActivationS2FormCurrent() { return index == ACT_S2_INDEX; }
	public boolean isCancelFulfillmentS2FormCurrent() { return index == CNL_S2_INDEX; }
	public boolean isAdjustActivationS2FormCurrent() { return index == ADJ_S2_INDEX; }
	public boolean isReactivateFulfillmentS2FormCurrent() { return index == RACT_S2_INDEX; }
	public boolean isFulfillActivationPsaFormCurrent() { return index == ACT_PSA_INDEX; }
	public boolean isCancelFulfillmentPsaFormCurrent() { return index == CNL_PSA_INDEX; }
	public boolean isAdjustActivationPsaFormCurrent() { return index == ADJ_PSA_INDEX; }
	public boolean isReactivateFulfillmentPsaFormCurrent() { return index == RACT_PSA_INDEX; }
	public boolean isFulfillActivationDigitalFormCurrent() { return index == ACT_DIGITAL_INDEX; }
	public boolean isCancelFulfillmentDigitalFormCurrent() { return index == CNL_DIGITAL_INDEX; }
	public boolean isAdjustActivationDigitalFormCurrent() { return index == ADJ_DIGITAL_INDEX; }
	public boolean isReactivateFulfillmentDigitalFormCurrent() { return index == RACT_DIGITAL_INDEX; }
	public boolean isFulfillActivationTechSupportFormCurrent() { return index == ACT_TECH_SUPPORT_INDEX; }
	public boolean isCancelFulfillmentTechSupportFormCurrent() { return index == CNL_TECH_SUPPORT_INDEX; }
	public boolean isAdjustActivationTechSupportFormCurrent() { return index == ADJ_TECH_SUPPORT_INDEX; }
	public boolean isReactivateFulfillmentTechSupportFormCurrent() { return index == RACT_TECH_SUPPORT_INDEX; }
	
	public void displayDefaultForm() { index = DEFAULT_FORM_INDEX; }
	public void displayFulfillActivationS2Form() { index = ACT_S2_INDEX; }
	public void displayCancelFulfillmentS2Form() { index = CNL_S2_INDEX; }
	public void displayAdjustActivationS2Form() { index = ADJ_S2_INDEX; }
	public void displayReactivateFulfillmentS2Form() { index = RACT_S2_INDEX; }
	public void displayFulfillActivationPsaForm() { index = ACT_PSA_INDEX; }
	public void displayCancelFulfillmentPsaForm() { index = CNL_PSA_INDEX; }
	public void displayAdjustActivationPsaForm() { index = ADJ_PSA_INDEX; }
	public void displayReactivateFulfillmentPsaForm() { index = RACT_PSA_INDEX; }
	public void displayFulfillActivationDigitalForm() { index = ACT_DIGITAL_INDEX; }
	public void displayCancelFulfillmentDigitalForm() { index = CNL_DIGITAL_INDEX; }
	public void displayAdjustActivationDigitalForm() { index = ADJ_DIGITAL_INDEX; }
	public void displayReactivateFulfillmentDigitalForm() { index = RACT_DIGITAL_INDEX; }
	public void displayFulfillActivationTechSupportForm() { index = ACT_TECH_SUPPORT_INDEX; }
	public void displayCancelFulfillmentTechSupportForm() { index = CNL_TECH_SUPPORT_INDEX; }
	public void displayAdjustActivationTechSupportForm() { index = ADJ_TECH_SUPPORT_INDEX; }
	public void displayReactivateFulfillmentTechSupportForm() { index = RACT_TECH_SUPPORT_INDEX; }
	
	
}
