package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;

public class PreorderBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 991872920083423824L;
	private String catalogId;
	private String vendorId;
	private String vendorName;
	private String productSku;
	private String description;
	private Date streetDate;
	private Integer preorderCount;
	private String statusId;
	private String createdByUserId;
	private String updatedByUserId;
	private Date createdDate;
	private Date updatedDate;
	
	public PreorderBean() {
		
	}
	public PreorderBean(String catalogId, String vendorId, String vendorName, String productSku, String description, Date streetDate, Integer preorderCount, String status, String createdByUserId, String updatedByUserId, Date createdDate, Date updatedDate) {
		this.catalogId = catalogId;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.productSku = productSku;
		this.description = description;
		this.streetDate = streetDate;
		this.preorderCount = preorderCount;
		this.statusId = status;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	public PreorderBean(PreorderBean preorderBean) {
		this.catalogId = preorderBean.catalogId;
		this.vendorId = preorderBean.vendorId;
		this.vendorName = preorderBean.vendorName;
		this.productSku = preorderBean.productSku;
		this.description = preorderBean.description;
		this.streetDate = preorderBean.streetDate;
		this.preorderCount = preorderBean.preorderCount;
		this.statusId = preorderBean.statusId;
		this.createdByUserId = preorderBean.createdByUserId;
		this.updatedByUserId = preorderBean.updatedByUserId;
		this.createdDate = preorderBean.createdDate;
		this.updatedDate = preorderBean.updatedDate;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
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
	public String getProductSku() {
		return productSku;
	}
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStreetDate() {
		return streetDate;
	}
	public void setStreetDate(Date streetDate) {
		this.streetDate = streetDate;
	}
	public Integer getPreorderCount() {
		return preorderCount;
	}
	public void setPreorderCount(Integer preorderCount) {
		this.preorderCount = preorderCount;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusName() {
		if (this.statusId != null) {
			return Constants.getStatusName(this.statusId);
		} else {
			return null;
		}
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
	public boolean isEditAllowed() {
		return this.streetDate == null || this.streetDate.after(new Date());
	}
	public boolean isDeleteAllowed() throws MissingPropertyException {
		return this.statusId == null || this.statusId.equals(SdpConfigProperties.getStatusReleased());
	}
	@Override
	public String toString() {
		return "<PreorderBean" + 
			(" catalogId=\"" + (catalogId != null ? catalogId : "[null]") + "\"") +
			(" productSku=\"" + (productSku != null ? productSku : "[null]") + "\"") +
			(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
			(" vendorName=\"" + (vendorName != null ? vendorName : "[null]") + "\"") +
			(" description=\"" + (description != null ? description : "[null]") + "\"") +
			(" preorderCount=\"" + (preorderCount != null ? preorderCount : "[null]") + "\"") +
			(" streetDate=\"" + (streetDate != null ? DateUtil.getXmlDateString(streetDate) : "[null]") + "\"") +
			(" statusId=\"" + (statusId != null ? statusId : "[null]") + "\"") +
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
		return productSku;
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
		if (this.catalogId != null)
			fields.put(Field.CATALOG_ID.priority(1), this.catalogId);
		if (this.productSku != null)
			fields.put(Field.PRIMARY_SKU.priority(2), this.productSku);
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(3), this.vendorId);
		if (this.streetDate != null)
			fields.put(Field.STREET_DATE.priority(4), DateUtil.getCalendarDateString(streetDate));
		return fields;
	}
}