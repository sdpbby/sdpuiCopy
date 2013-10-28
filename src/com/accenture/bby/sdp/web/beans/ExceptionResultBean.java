package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;

public class ExceptionResultBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionId;
	private String sdpId;
	private String sdpOrderId;
	private String sourceSystem;
	private String code;
	private String description;
	private Date createdDate;
	private String requestMessage;
	private String stackTrace;
	
	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getSdpId() {
		return sdpId;
	}

	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}

	public String getSdpOrderId() {
		return sdpOrderId;
	}

	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "<ExceptionResultBean" + 
		(" exceptionId=\"" + (exceptionId != null ? exceptionId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" sdpId=\"" + (sdpId != null ? sdpId : "[null]") + "\"") +
		(" sdpOrderId=\"" + (sdpOrderId != null ?sdpOrderId : "[null]") + "\"") +
		(" sourceSystem=\"" + (sourceSystem != null ? sourceSystem : "[null]") + "\"") +
		(" code=\"" + (code != null ? code : "[null]") + "\"") +
		(" description=\"" + (description != null ? description : "[null]") + "\"") +
		(" requestMessage=\"" + (requestMessage != null ? requestMessage : "[null]") + "\"") +
		(" stackTrace=\"" + (stackTrace != null ? stackTrace : "[null]") + "\"") +
		" />";
	}
}
