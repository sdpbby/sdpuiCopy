package com.accenture.bby.sdp.web.handlers;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.accenture.bby.sdp.utl.exceptions.ExceptionUtil;

@ManagedBean (name="exceptionDefaultHandler")
@RequestScoped
public class ExceptionDefaultHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable throwable;
	private Integer httpErrorCode;
	private Date timestamp;
	private String referenceNum;
	private static final Logger logger = Logger.getLogger(ExceptionDefaultHandler.class.getName());
	private String userId;
	
	public ExceptionDefaultHandler() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> request = context.getExternalContext().getRequestMap();
		this.throwable = (Throwable) request.get("javax.servlet.error.exception");
		this.timestamp = new Date();
		this.referenceNum = java.util.UUID.randomUUID().toString();
		this.httpErrorCode = (Integer) request.get("javax.servlet.error.status_code");
		logger.log(Level.ERROR, "Exception caught by default handler. \n" + this.toString());
		userId = context.getExternalContext().getRemoteUser();
	}
	
	public String getCustomMessage() {
		if (httpErrorCode.equals(403) && this.throwable == null) {
			return "You are not authorized to view this resource or this resource does not exist. Please contact your site administrator for assistance.";
		}
		if (this.throwable == null) {
			return null;
		}
		return ExceptionUtil.getCustomMessage(this.throwable);
	}
	
	public String getExceptionType() {
		if (throwable == null || throwable.getClass().getClass() == null) {
			return null;
		}
		return throwable.getClass().getName();
	}
	
	public String getExceptionMessage() {
		if (this.throwable == null) {
			return null;
		}
		return throwable.getMessage();
	}
	
	public String getReferenceNum() {
		return referenceNum;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public Integer getHttpErrorCode() {
		return httpErrorCode;
	}
	
	public String getStackTrace() {
		if (this.throwable == null) {
			return "No Exceptions were found. HTTP Error Code " + httpErrorCode + " returned by server.";
		}
		return ExceptionUtil.getStackTrace(this.throwable);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		builder.append("Reference ID: " + referenceNum + "\n");
		builder.append("Remote User: " + userId + "\n");
		builder.append("Exception Type: " + getExceptionType() + "\n");
		builder.append("Exception Message: " + getExceptionMessage() + "\n");
		builder.append("HTTP Code: " + httpErrorCode + "\n");
		builder.append("Timestamp: " + ExceptionUtil.formatDate(timestamp) + "\n");
		builder.append("Stack Trace: \n");
		builder.append(getStackTrace() + "\n");
		builder.append("]\n");
		return builder.toString();
	}
}
