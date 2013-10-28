package com.accenture.bby.sdp.utl.exceptions;

public class KeycodeLoadException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KeycodeLoadException(String msg) {
		super(msg);
	}
	
	public KeycodeLoadException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
