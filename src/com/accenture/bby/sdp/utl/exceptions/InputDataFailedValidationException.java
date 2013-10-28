package com.accenture.bby.sdp.utl.exceptions;

public class InputDataFailedValidationException extends Exception {
	
	public InputDataFailedValidationException(String msg) {
		super(msg);
	}
	
	public InputDataFailedValidationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4137512076805479234L;

}
