package com.accenture.bby.sdp.utl.exceptions;

public class MissingPropertyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MissingPropertyException(String msg) {
		super(msg);
	}
	
	public MissingPropertyException(String msg, String propertyName) {
		super(msg + propertyName != null ? ": Expected property <" + propertyName + ">." : "");
	}
	
	public MissingPropertyException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public MissingPropertyException(String msg, String propertyName, Throwable throwable) {
		super(msg + propertyName != null ? ": Expected property <" + propertyName + ">." : "", throwable);
	}
}
