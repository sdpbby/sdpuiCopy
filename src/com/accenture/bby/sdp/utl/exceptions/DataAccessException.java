package com.accenture.bby.sdp.utl.exceptions;

public class DataAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataAccessException(String msg) {
		super(msg);
	}
	
	public DataAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
