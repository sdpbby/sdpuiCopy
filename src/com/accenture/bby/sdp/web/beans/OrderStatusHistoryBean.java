package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;

public class OrderStatusHistoryBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date createdDate;
	private String status;
	private String sdpId;
	private String sdpOrderId;
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public String getStatusName() {
		if (this.getStatus() != null) {
			return Constants.getStatusName(this.getStatus());
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "<OrderStatusHistoryBean" + 
		(" sdpOrderId=\"" + (sdpOrderId != null ? sdpOrderId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" status=\"" + (status != null ? status : "[null]") + "\"") +
		(" sdpId=\"" + (sdpId != null ? sdpId : "[null]") + "\"") +
		" />";
	}
}
