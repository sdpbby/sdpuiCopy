package com.accenture.bby.sdp.utl.xml;

import serviceresult.bby.sdp.com.accenture.xml.ServiceResultDocument;
import serviceresult.bby.sdp.com.accenture.xml.ServiceResultDocument.ServiceResult;

public class ServiceResultWrapper {
	
	private String statusCode;
	private String errorCode;
	private String severity;
	private String serviceResult;
	
	public ServiceResultWrapper() {
		
	}
	
	public ServiceResultWrapper(ServiceResultDocument doc) {
		if (doc != null) {
			serviceResult = doc.toString();
    		if (doc.getServiceResult() != null) {
    			ServiceResult serviceResult = doc.getServiceResult();
    			statusCode = serviceResult.getStatusCode() != null ? serviceResult.getStatusCode().toString() : null;
    			errorCode = serviceResult.getErrorCode();
    			severity = serviceResult.getErrorSeverity();
    		}
		}
	}
	
	public String getServiceResult() {
		return serviceResult;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getSeverity() {
		return severity;
	}

}
