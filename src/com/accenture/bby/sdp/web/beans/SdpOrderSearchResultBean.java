package com.accenture.bby.sdp.web.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;

public class SdpOrderSearchResultBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sdpOrderId;
	private Date createdDate;
	private Date fpkTransactionDate;
	private String fpkStoreId;
	private String fpkRegisterId;
	private String fpkTransactionId;
	private String fpkLineId;
	private String sdpOrderStatus;
	private String primarySku;
	private String relatedSku;
	private String confirmationCode;
	private String lineItemId;
	private String customerFirstName;
	private String customerLastName;
	private String deliveryEmail;
	private String serialNumber;
	private String sourceSystem;
	private String masterItemId;
	private String vendorId;
	private String contractId;
	/**
	 * default constructor
	 */
	public SdpOrderSearchResultBean() {
	}
	/**
	 * constructs a SdpOrderSearchResultBean from an existing resultset.
	 * this is only used by the <code>SdpOrderSearchDBWrapper</code> class.
	 * the calling class is responsible for creating and executing the 
	 * correct query and for closing the resources.
	 * 
	 * @see com.accenture.bby.sdp.db.SdpOrderSearchDBWrapper
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public SdpOrderSearchResultBean(ResultSet rs) throws SQLException {
		this.setSdpOrderId(rs.getString(1));
		this.setCreatedDate(rs.getTimestamp(2));
		this.setFpkTransactionDate(rs.getTimestamp(3));
		this.setFpkStoreId(rs.getString(4));
		this.setFpkRegisterId(rs.getString(5));
		this.setFpkTransactionId(rs.getString(6));
		this.setFpkLineId(rs.getString(7));
		this.setSdpOrderStatus(rs.getString(8));
		this.setPrimarySku(rs.getString(9));
		this.setRelatedSku(rs.getString(10));
		this.setConfirmationCode(rs.getString(11));
		this.setLineItemId(rs.getString(12));
		this.setCustomerFirstName(rs.getString(13));
		this.setCustomerLastName(rs.getString(14));
		this.setDeliveryEmail(rs.getString(15));
		this.setSerialNumber(rs.getString(16));
		this.setSourceSystem(rs.getString(17));
		this.setMasterItemId(rs.getString(18));
		this.setVendorId(rs.getString(19));
		this.setContractId(rs.getString(20));
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
		this.fpkStoreId = fpkStoreId;
	}
	public String getFpkRegisterId() {
		return fpkRegisterId;
	}
	public void setFpkRegisterId(String fpkRegisterId) {
		this.fpkRegisterId = fpkRegisterId;
	}
	public String getFpkTransactionId() {
		return fpkTransactionId;
	}
	public void setFpkTransactionId(String fpkTransactionId) {
		this.fpkTransactionId = fpkTransactionId;
	}
	public String getFpkLineId() {
		return fpkLineId;
	}
	public void setFpkLineId(String fpkLineId) {
		this.fpkLineId = fpkLineId;
	}
	public String getSdpOrderStatus() {
		return sdpOrderStatus;
	}
	public void setSdpOrderStatus(String sdpOrderStatus) {
		this.sdpOrderStatus = sdpOrderStatus;
	}
	public String getSdpOrderStatusName() {
		if (this.getSdpOrderStatus() == null) {
			return null;
		} else {
			return Constants.getStatusName(this.getSdpOrderStatus());
		}
	}
	public String getPrimarySku() {
		return primarySku;
	}
	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}
	public String getRelatedSku() {
		return relatedSku;
	}
	public void setRelatedSku(String relatedSku) {
		this.relatedSku = relatedSku;
	}
	public String getConfirmationCode() {
		return confirmationCode;
	}
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	public String getCustomerFullName() {
		return (getCustomerFirstName() != null ? (getCustomerFirstName() + " ") : "") + (getCustomerLastName() != null ? getCustomerLastName() : "");
	}
	public String getDeliveryEmail() {
		return deliveryEmail;
	}
	public void setDeliveryEmail(String deliveryEmail) {
		this.deliveryEmail = deliveryEmail;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getMasterItemId() {
		return masterItemId;
	}
	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		if (this.getVendorId() == null) {
			return null;
		} else {
			return Constants.getVendorName(this.getVendorId());
		}
	}
	@Override
	public String toString() {
		return ("<SdpOrderSearchResultBean") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" fpkTransactionDate=\"" + (fpkTransactionDate != null ? DateUtil.getStoreDateString(fpkTransactionDate) : "[null]") + "\"") +
				(" fpkStoreId=\"" + fpkStoreId != null ? fpkStoreId : "" + "\"") +
				(" fpkRegisterId=\"" + fpkRegisterId != null ? fpkRegisterId : "" + "\"") +
				(" fpkTransactionId=\"" + fpkTransactionId != null ? fpkTransactionId : "" + "\"") +
				(" fpkLineId=\"" + fpkLineId != null ? fpkLineId : "" + "\"") +
				(" sdpOrderStatus=\"" + sdpOrderStatus != null ? sdpOrderStatus : "" + "\"") +
				(" primarySku=\"" + primarySku != null ? primarySku : "" + "\"") +
				(" relatedSku=\"" + relatedSku != null ? relatedSku : "" + "\"") +
				(" confirmationCode=\"" + confirmationCode != null ? confirmationCode : "" + "\"") +
				(" lineItemId=\"" + lineItemId != null ? lineItemId : "" + "\"") +
				(" customerFirstName=\"" + customerFirstName != null ? customerFirstName : "" + "\"") +
				(" customerLastName=\"" + customerLastName != null ? customerLastName : "" + "\"") +
				(" deliveryEmail=\"" + deliveryEmail != null ? deliveryEmail : "" + "\"") +
				(" serialNumber=\"" + serialNumber != null ? serialNumber : "" + "\"") +
				(" sourceSystem=\"" + sourceSystem != null ? sourceSystem : "" + "\"") +
				(" masterItemId=\"" + masterItemId != null ? masterItemId : "" + "\"") +
				(" vendorId=\"" + vendorId != null ? vendorId : "" + "\"") +
				(" />");
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}
