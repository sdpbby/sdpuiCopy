package com.accenture.bby.sdp.utl.keycode;

import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.beans.KeycodeBean;

public interface KeycodeFileValidator {
	public boolean isValid();
	public KeycodeBean[] getValidKeycodes();
	public KeycodeBean[] getInvalidKeycodes();
	public KcbProductBean[] getProducts();
	public int getValidKeyCount();
	public int getInvalidKeyCount();
	public String getFileName();
	public void setInvalidKeycodes(KeycodeBean[] keycodeBeans);
}
