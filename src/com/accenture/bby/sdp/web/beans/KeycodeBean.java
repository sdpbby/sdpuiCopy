package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;

public class KeycodeBean extends WebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7899140365636991609L;
	private String keycode;
	private String serialNumber;
	public KeycodeBean() {
		
	}
	public KeycodeBean(String keycode, String serialNumber) {
		this.keycode = keycode;
		this.serialNumber = serialNumber;
	}
	public KeycodeBean(KeycodeBean keycode) {
		this.keycode = keycode.keycode;
		this.serialNumber = keycode.serialNumber;
	}
	public KeycodeBean(String keycode) {
		this.keycode = keycode;
		this.serialNumber = null;
	}
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = filter(keycode);
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = filter(serialNumber);
	}
	@Override
	public String toString() {
		return ("<KeycodeBean") +
		(" keycode=\"" + (keycode != null ? keycode : "[null]") + "\"") +
		(" serialNumber=\"" + (serialNumber != null ? serialNumber : "[null]") + "\"") +
		(" />");
	}
}
