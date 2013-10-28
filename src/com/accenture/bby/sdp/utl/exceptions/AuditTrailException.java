package com.accenture.bby.sdp.utl.exceptions;

public class AuditTrailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AuditTrailException(String msg) {
		super(msg);
	}
	public AuditTrailException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
