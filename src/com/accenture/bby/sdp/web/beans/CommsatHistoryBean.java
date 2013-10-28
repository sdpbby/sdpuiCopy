package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;

public class CommsatHistoryBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date createdDate;
	private String templateId;
	private String deliveryEmail;
	private String keycode;
	private String sdpOrderId;
	private String commsatHistoryId;
	public String getCommsatHistoryId() {
		return commsatHistoryId;
	}
	public void setCommsatHistoryId(String commsatHistoryId) {
		this.commsatHistoryId = commsatHistoryId;
	}
	public String getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = filter(templateId);
	}
	public String getDeliveryEmail() {
		return deliveryEmail;
	}
	public void setDeliveryEmail(String deliveryEmail) {
		this.deliveryEmail = filter(deliveryEmail);
	}
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = filter(keycode);
	}
	@Override
	public String toString() {
		return ("<CommsatHistoryBean") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" commsatHistoryId=\"" + commsatHistoryId != null ? commsatHistoryId : "" + "\"") +
				(" templateId=\"" + templateId != null ? templateId : "" + "\"") +
				(" deliveryEmail=\"" + deliveryEmail != null ? deliveryEmail : "" + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" keycode=\"" + keycode != null ? keycode : "" + "\"") +
				(" />");
	}
	
}
