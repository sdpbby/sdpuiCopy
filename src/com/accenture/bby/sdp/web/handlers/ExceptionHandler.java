package com.accenture.bby.sdp.web.handlers;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.accenture.bby.sdp.utl.exceptions.ExceptionUtil;

@ManagedBean (name="exceptionHandler")
@SessionScoped
public class ExceptionHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable throwable;
	private Integer httpErrorCode;
	private Date timestamp;
	private String referenceNum;
	private String customMessage;
	private String userId;
	
	public ExceptionHandler() {
		FacesContext context = FacesContext.getCurrentInstance();
		userId = context.getExternalContext().getRemoteUser();
	}
	
	public String getCustomMessage() {
		if (this.customMessage == null) {
			return ExceptionUtil.getCustomMessage(this.throwable);
		} else {
			return customMessage;
		}
	}
	
	public String getExceptionType() {
		return throwable.getClass().getName();
	}
	
	public String getExceptionMessage() {
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
		builder.append("Custom Message: " + customMessage + "\n");
		builder.append("Timestamp: " + ExceptionUtil.formatDate(timestamp) + "\n");
		builder.append("Stack Trace: \n");
		builder.append(getStackTrace() + "\n");
		builder.append("]\n");
		return builder.toString();
	}
	
	public void initialize(Throwable throwable, String customMessage) {
		this.throwable = throwable;
		this.customMessage = customMessage;
		this.timestamp = new Date();
		this.referenceNum = java.util.UUID.randomUUID().toString();
		this.httpErrorCode = null;
	}
	
	public void initialize(Throwable throwable) {
		initialize(throwable, null);
	}
}
