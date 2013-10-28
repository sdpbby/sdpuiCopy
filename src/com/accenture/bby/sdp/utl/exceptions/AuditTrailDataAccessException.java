package com.accenture.bby.sdp.utl.exceptions;

public class AuditTrailDataAccessException extends DataAccessException {

	public AuditTrailDataAccessException(String msg) {
		super(msg);
	}
	
	public AuditTrailDataAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
