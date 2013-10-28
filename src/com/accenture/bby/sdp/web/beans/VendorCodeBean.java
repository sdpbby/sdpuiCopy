package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class VendorCodeBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3732298489636141383L;
	private Integer vendorCodeRowId;
	private String vendorCode;
	private String sdpCode;
	private String vendorId;
	private Boolean canRetry;
	private String vendorCodeDescription;
	private	String createdByUserId;
	private String updatedByUserId;
	private Date createdDate;
	private Date updatedDate;
	public VendorCodeBean() {
		
	}
	public VendorCodeBean(VendorCodeBean vendorCodeBean) {
		this.vendorCodeRowId = vendorCodeBean.vendorCodeRowId;
		this.vendorCode = vendorCodeBean.vendorCode;
		this.sdpCode = vendorCodeBean.sdpCode;
		this.vendorId = vendorCodeBean.vendorId;
		this.canRetry = vendorCodeBean.canRetry;
		this.vendorCodeDescription = vendorCodeBean.vendorCodeDescription;
		this.createdByUserId = vendorCodeBean.createdByUserId;
		this.updatedByUserId = vendorCodeBean.updatedByUserId;
		this.createdDate = vendorCodeBean.createdDate;
		this.updatedDate = vendorCodeBean.updatedDate;
	}
	public VendorCodeBean(Integer vendorCodeRowId, String vendorCode, String sdpCode, String vendorId, Boolean canRetry, String vendorCodeDescription, String createdByUserId, String updatedByUserId, Date createdDate, Date updatedDate) {
		this.vendorCodeRowId = vendorCodeRowId;
		this.vendorCode = vendorCode;
		this.sdpCode = sdpCode;
		this.vendorId = vendorId;
		this.canRetry = canRetry;
		this.vendorCodeDescription = vendorCodeDescription;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	public Integer getVendorCodeRowId() {
		return vendorCodeRowId;
	}
	public void setVendorCodeRowId(Integer vendorCodeRowId) {
		this.vendorCodeRowId = vendorCodeRowId;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getSdpCode() {
		return sdpCode;
	}
	public void setSdpCode(String sdpCode) {
		this.sdpCode = sdpCode;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getCanRetryString() {
		return textValue(canRetry);
	}
	public Boolean getCanRetry() {
		return canRetry;
	}
	public void setCanRetry(Boolean canRetry) {
		this.canRetry = canRetry;
	}
	public String getVendorCodeDescription() {
		return vendorCodeDescription;
	}
	public void setVendorCodeDescription(String vendorCodeDescription) {
		this.vendorCodeDescription = vendorCodeDescription;
	}
	public String getCreatedByUserId() {
		return createdByUserId;
	}
	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	public String getUpdatedByUserId() {
		return updatedByUserId;
	}
	public void setUpdatedByUserId(String updatedByUserId) {
		this.updatedByUserId = updatedByUserId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getVendorName() {
		return Constants.getVendorName(this.getVendorId());
	}

	@Override
	public String toString() {
		return "<VendorCodeBean" + 
		(" vendorCodeRowId=\"" + (vendorCodeRowId != null ? vendorCodeRowId : "[null]") + "\"") +
		(" vendorCode=\"" + (vendorCode != null ? vendorCode : "[null]") + "\"") +
		(" sdpCode=\"" + (sdpCode != null ? sdpCode : "[null]") + "\"") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" canRetry=\"" + canRetry + "\"") +
		(" vendorCodeDescription=\"" + (vendorCodeDescription != null ? vendorCodeDescription : "[null]") + "\"") +
		(" createdByUserId=\"" + (createdByUserId != null ? createdByUserId : "[null]") + "\"") +
		(" updatedByUserId=\"" + (updatedByUserId != null ? updatedByUserId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" updatedDate=\"" + (updatedDate != null ? DateUtil.getXmlDateString(updatedDate) : "[null]") + "\"") +
		" />";
	}
	@Override
	public String getSerialNumberForAudit() {
		return null;
	}
	@Override
	public String getLineItemIdForAudit() {
		return null;
	}
	@Override
	public String getPrimarySkuForAudit() {
		return null;
	}
	@Override
	public String getContractIdForAudit() {
		return null;
	}
	@Override
	public String getVendorIdForAudit() {
		return vendorId;
	}
	@Override
	public String getMasterItemIdForAudit() {
		return null;
	}
	@Override
	public Map<Field, String> getAuditableFields() {
		Map<Field, String> fields = new HashMap<Field, String>();
		if (this.vendorCodeRowId != null)
			fields.put(Field.VENDOR_CODE_ROW_ID.priority(1), this.vendorCodeRowId.toString());
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(2), this.vendorId);
		if (this.vendorCode != null)
			fields.put(Field.VENDOR_CODE.priority(3), this.vendorCode);
		if (this.sdpCode != null)
			fields.put(Field.SDP_CODE.priority(4), this.sdpCode);
		if (this.canRetry != null)
			fields.put(Field.CAN_RETRY_FLAG.priority(5), textValue(this.canRetry));
		if (this.vendorCodeDescription != null)
			fields.put(Field.VENDOR_CODE_DESCRIPTION.priority(6), this.vendorCodeDescription);
		return fields;
	}
}
