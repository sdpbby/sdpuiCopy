package com.accenture.bby.sdp.web.beans;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.web.beans.WebBean;

public class AdjustmentHistoryBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2462955282490007429L;
	private String transactionId;
	private String sdpOrderId;
	private String sdpId;
	private Date fpkTransactionDate;
	private String fpkStoreId;
	private String fpkRegisterId;
	private String fpkTransactionId;
	private String fpkLineId;
	private Date timestamp;
	private String serialNumber;
	private String primarySku;
	private String relatedSku;
	private String masterItemId;
	
	

	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = filter(transactionId);
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



	public Date getFpkTransactionDate() {
		return fpkTransactionDate;
	}



	public void setFpkTransactionDate(Date fpkTransactionDate) {
		this.fpkTransactionDate = fpkTransactionDate;
	}



	public String getFpkStoreId() {
		return fpkStoreId;
	}



	public void setFpkStoreId(String fpkStoreId) {
		this.fpkStoreId = filter(fpkStoreId);
	}



	public String getFpkRegisterId() {
		return fpkRegisterId;
	}



	public void setFpkRegisterId(String fpkRegisterId) {
		this.fpkRegisterId = filter(fpkRegisterId);
	}



	public String getFpkTransactionId() {
		return fpkTransactionId;
	}



	public void setFpkTransactionId(String fpkTransactionId) {
		this.fpkTransactionId = filter(fpkTransactionId);
	}



	public String getFpkLineId() {
		return fpkLineId;
	}



	public void setFpkLineId(String fpkLineId) {
		this.fpkLineId = filter(fpkLineId);
	}



	public Date getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}



	public String getSerialNumber() {
		return serialNumber;
	}



	public void setSerialNumber(String serialNumber) {
		this.serialNumber = filter(serialNumber);
	}



	public String getPrimarySku() {
		return primarySku;
	}



	public void setPrimarySku(String primarySku) {
		this.primarySku = filter(primarySku);
	}



	public String getRelatedSku() {
		return relatedSku;
	}



	public void setRelatedSku(String relatedSku) {
		this.relatedSku = filter(relatedSku);
	}



	public String getMasterItemId() {
		return masterItemId;
	}



	public void setMasterItemId(String masterItemId) {
		this.masterItemId = filter(masterItemId);
	}



	public String toString() {
		return ("<AdjustmentHistoryBean") +
				(" transactionId=\"" + transactionId != null ? transactionId : "" + "\"") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" sdpId=\"" + sdpId != null ? sdpId : "" + "\"") +
				(" fpkTransactionDate=\"" + (fpkTransactionDate != null ? DateUtil.getStoreDateString(fpkTransactionDate) : "[null]") + "\"") +
				(" fpkStoreId=\"" + fpkStoreId != null ? fpkStoreId : "" + "\"") +
				(" fpkRegisterId=\"" + fpkRegisterId != null ? fpkRegisterId : "" + "\"") +
				(" fpkTransactionId=\"" + fpkTransactionId != null ? fpkTransactionId : "" + "\"") +
				(" fpkLineId=\"" + fpkLineId != null ? fpkLineId : "" + "\"") +
				(" timestamp=\"" + (timestamp != null ? DateUtil.getXmlDateString(timestamp) : "[null]") + "\"") +
				(" serialNumber=\"" + serialNumber != null ? serialNumber : "" + "\"") +
				(" primarySku=\"" + primarySku != null ? primarySku : "" + "\"") +
				(" relatedSku=\"" + relatedSku != null ? relatedSku : "" + "\"") +
				(" masterItemId=\"" + masterItemId != null ? masterItemId : "" + "\"") +
				(" />");
	}
}
