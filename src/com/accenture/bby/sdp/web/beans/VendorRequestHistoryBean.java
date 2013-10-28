package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;

public class VendorRequestHistoryBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rowNumber;
	private String sdpId;
	private String requestType;
	private String status;
	private String serialNumber;
	private String vendorId;
	private String vendorCode;
	private Integer sdpOrderId;
	private Date createdDate;
	
	public VendorRequestHistoryBean() {	
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = filter(sdpId);
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = filter(requestType);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = filter(status);
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = filter(serialNumber);
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = filter(vendorId);
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = filter(vendorCode);
	}
	public Integer getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(Integer sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return ("<VendorRequestHistoryBean") +
				(" rowNumber=\"" + rowNumber != null ? rowNumber : "" + "\"") +
				(" sdpId=\"" + sdpId != null ? sdpId : "" + "\"") +
				(" requestType=\"" + requestType != null ? requestType : "" + "\"") +
				(" status=\"" + status != null ? status : "" + "\"") +
				(" serialNumber=\"" + serialNumber != null ? serialNumber : "" + "\"") +
				(" vendorId=\"" + vendorId != null ? vendorId : "" + "\"") +
				(" vendorCode=\"" + vendorCode != null ? vendorCode : "" + "\"") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" />");
	}
}
