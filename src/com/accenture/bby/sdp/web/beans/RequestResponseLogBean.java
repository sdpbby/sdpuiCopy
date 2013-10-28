package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.xml.XmlPrettyPrinter;

public class RequestResponseLogBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date requestCreatedDate;
	private Date responseCreatedDate;
	private String requestMessage;
	private String responseMessage;
	private String requestType;
	
	public Date getRequestCreatedDate() {
		return requestCreatedDate;
	}

	public void setRequestCreatedDate(Date requestCreatedDate) {
		this.requestCreatedDate = requestCreatedDate;
	}

	public Date getResponseCreatedDate() {
		return responseCreatedDate;
	}

	public void setResponseCreatedDate(Date responseCreatedDate) {
		this.responseCreatedDate = responseCreatedDate;
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
	
	public String getRequestMessageXml() {
		return XmlPrettyPrinter.formatString(requestMessage);
	}
	
	public String getResponseMessageXml() {
		return XmlPrettyPrinter.formatString(responseMessage);
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return ("<RequestResponseLogBean") +
				(" requestType=\"" + requestType != null ? requestType : "" + "\"") +
				(" requestCreatedDate=\"" + (requestCreatedDate != null ? DateUtil.getXmlDateString(requestCreatedDate) : "[null]") + "\"") +
				(" responseCreatedDate=\"" + (responseCreatedDate != null ? DateUtil.getXmlDateString(responseCreatedDate) : "[null]") + "\"") +
				(">") +
				(requestMessage != null ? "\n  <requestMessage>\n<![CDATA[\n" + getRequestMessageXml() + "\n]]>\n  </requestMessage>" : "  <requestMessage />") +
				(responseMessage != null ? "\n  <responseMessage>\n<![CDATA[\n" + getResponseMessageXml() + "\n]]>\n  </responseMessage>" : "  <responseMessage />") +
				("\n</RequestResponseLogBean>");
	}
}
