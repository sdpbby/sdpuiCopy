package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class KcbVendorBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1821160723346360170L;
	private String vendorId;
	private String vendorName;
	private Date createdDate;
	private Date updatedDate;
	private String createdByUserId;
	private String updatedByUserId;
	public KcbVendorBean() {
		
	}
	public KcbVendorBean(String vendorId, String vendorName, Date createdDate, Date updatedDate, String createdByUserId, String updatedByUserId) {
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
	}
	public KcbVendorBean(KcbVendorBean kcbVendorBean) {
		this.vendorId = kcbVendorBean.vendorId;
		this.vendorName = kcbVendorBean.vendorName;
		this.createdDate = kcbVendorBean.createdDate;
		this.updatedDate = kcbVendorBean.updatedDate;
		this.createdByUserId = kcbVendorBean.createdByUserId;
		this.updatedByUserId = kcbVendorBean.updatedByUserId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	@Override
	public String toString() {
		return ("<KcbVendorBean") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" vendorName=\"" + (vendorName != null ? vendorName : "[null]") + "\"") +
		(" createdByUserId=\"" + (createdByUserId != null ? createdByUserId : "[null]") + "\"") +
		(" updatedByUserId=\"" + (updatedByUserId != null ? updatedByUserId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" updatedDate=\"" + (updatedDate != null ? DateUtil.getXmlDateString(updatedDate) : "[null]") + "\"") +
		(" />");
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
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(1), this.vendorId);
		if (this.vendorName != null)
			fields.put(Field.VENDOR_NAME.priority(2), this.vendorName);
		return fields;
	}
}
