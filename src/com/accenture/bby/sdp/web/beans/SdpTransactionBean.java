package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.xml.XmlPrettyPrinter;

public class SdpTransactionBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logId;
	private String sdpOrderId;
	private String sdpId;
	private String lineItemId;
	private String sourceSystemId;
	private String requestMessage;
	private String responseMessage;
	private String requestType;
	private Date createdDate;
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = filter(sdpOrderId);
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = filter(sdpId);
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = filter(lineItemId);
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = filter(requestType);
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = filter(sourceSystemId);
	}
	public String getFormattedRequestMessage() {
		return XmlPrettyPrinter.formatString(requestMessage);
	}
	public String getFormattedResponseMessage() {
		return XmlPrettyPrinter.formatString(responseMessage);
	}
	public boolean isLogIdPresent() {
		return logId != null;
	}
	@Override
	public String toString() {
		return ("<SdpTransactionBean") +
				(" logId=\"" + logId != null ? logId : "" + "\"") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" sdpId=\"" + sdpId != null ? sdpId : "" + "\"") +
				(" lineItemId=\"" + lineItemId != null ? lineItemId : "" + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" requestType=\"" + requestType != null ? requestType : "" + "\"") +
				(" sourceSystemId=\"" + sourceSystemId != null ? sourceSystemId : "" + "\"") +
				(">") +
				(requestMessage != null ? "\n  <requestMessage>\n<![CDATA[\n" + requestMessage + "\n]]>\n  </requestMessage>" : "  <requestMessage />") +
				(responseMessage != null ? "\n  <responseMessage>\n<![CDATA[\n" + responseMessage + "\n]]>\n  </responseMessage>" : "  <responseMessage />") +
				("\n</SdpTransactionBean>");
	}
}
