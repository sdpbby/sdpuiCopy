package com.accenture.bby.sdp.utl.exceptions;

public class WebServiceCallFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4641758131290221845L;
	
	public WebServiceCallFailedException(String msg) {
		super(msg);
	}
	
	public WebServiceCallFailedException(String msg, Throwable t) {
		super(msg, t);
	}

}
