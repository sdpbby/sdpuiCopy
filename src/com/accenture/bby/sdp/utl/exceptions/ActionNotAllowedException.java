package com.accenture.bby.sdp.utl.exceptions;

public class ActionNotAllowedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionNotAllowedException(String msg) {
		super(msg);
	}
	
	public ActionNotAllowedException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
