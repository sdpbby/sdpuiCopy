package com.accenture.bby.sdp.utl.exceptions;

public class DataSourceLookupException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataSourceLookupException(String msg) {
		super(msg);
	}
	
	public DataSourceLookupException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
