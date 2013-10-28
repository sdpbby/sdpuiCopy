package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class KcbProductBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1890334059246848021L;
	private Integer productId;
	private String vendorId;
	private String nonMerchandiseSku;
	private String description;
	private Integer loadSize;
	private String masterItemId;
	private String merchandiseSku;
	private Integer threshold;
	private Date createdDate;
	private Date updatedDate;
	private String createdByUserId;
	private String updatedByUserId;
	private String relatedSKU;
	public KcbProductBean() {
		
	}
	public KcbProductBean(Integer productId, String vendorId, String merchandiseSku, String nonMerchandiseSku, String masterItemId, String description, Integer loadSize, Integer threshold, Date createdDate, Date updatedDate, String createdByUserId, String updatedByUserId ,String relatedSKU) {
		this.productId = productId;
		this.vendorId = vendorId;
		this.merchandiseSku = merchandiseSku;
		this.nonMerchandiseSku = nonMerchandiseSku;
		this.masterItemId = masterItemId;
		this.description = description;
		this.loadSize = loadSize;
		this.threshold = threshold;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.relatedSKU = relatedSKU;
	}
	public KcbProductBean(KcbProductBean kcbProductBean) {
		this.productId = kcbProductBean.productId;
		this.vendorId = kcbProductBean.vendorId;
		this.merchandiseSku = kcbProductBean.merchandiseSku;
		this.nonMerchandiseSku = kcbProductBean.nonMerchandiseSku;
		this.masterItemId = kcbProductBean.masterItemId;
		this.description = kcbProductBean.description;
		this.loadSize = kcbProductBean.loadSize;
		this.threshold = kcbProductBean.threshold;
		this.createdDate = kcbProductBean.createdDate;
		this.updatedDate = kcbProductBean.updatedDate;
		this.createdByUserId = kcbProductBean.createdByUserId;
		this.updatedByUserId = kcbProductBean.updatedByUserId;
		this.relatedSKU = kcbProductBean.relatedSKU;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getNonMerchandiseSku() {
		return nonMerchandiseSku;
	}
	public void setNonMerchandiseSku(String nonMerchandiseSku) {
		this.nonMerchandiseSku = nonMerchandiseSku;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLoadSize() {
		return loadSize;
	}
	public void setLoadSize(Integer loadSize) {
		this.loadSize = loadSize;
	}
	public String getMasterItemId() {
		return masterItemId;
	}
	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}
	public String getMerchandiseSku() {
		return merchandiseSku;
	}
	public void setMerchandiseSku(String merchandiseSKU) {
		this.merchandiseSku = merchandiseSKU;
	}
	public Integer getThreshold() {
		return threshold;
	}
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
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
	
	public String getVendorName() {
		return Constants.getVendorName(this.vendorId);
	}
	
	public String getRelatedSKU() {
		return relatedSKU;
	}
	public void setRelatedSKU(String relatedSKU) {
		this.relatedSKU = relatedSKU;
	}
	
	@Override
	public String toString() {
		return ("<KcbProductBean") +
		(" productId=\"" + (productId != null ? productId : "[null]") + "\"") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" sku=\"" + (nonMerchandiseSku != null ? nonMerchandiseSku : "[null]") + "\"") +
		(" description=\"" + (description != null ? description : "[null]") + "\"") +
		(" loadSize=\"" + (loadSize != null ? loadSize : "[null]") + "\"") +
		(" masterItemId=\"" + (masterItemId != null ? masterItemId : "[null]") + "\"") +
		(" merchandiseSKU=\"" + (merchandiseSku != null ? merchandiseSku : "[null]") + "\"") +
		(" relatedSku=\"" + (relatedSKU != null ? relatedSKU : "[null]") + "\"") +
		(" threshold=\"" + (threshold != null ? threshold : "[null]") + "\"") +
		(" createdByUserId=\"" + (createdByUserId != null ? createdByUserId : "[null]") + "\"") +
		(" updatedByUserId=\"" + (updatedByUserId != null ? updatedByUserId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" updatedDate=\"" + (updatedDate != null ? DateUtil.getXmlDateString(updatedDate) : "[null]") + "\"") +
		(" />");
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
		return merchandiseSku;
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
		return masterItemId;
	}
	@Override
	public Map<Field, String> getAuditableFields() {
		Map<Field, String> fields = new HashMap<Field, String>();
		if (this.productId != null)
			fields.put(Field.PRODUCT_ID.priority(1), this.productId.toString());
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(2), this.vendorId);
		if (this.description != null)
			fields.put(Field.PRIMARY_SKU_DESCRIPTION.priority(3), this.description);
		if (this.merchandiseSku != null)
			fields.put(Field.MERCHANDISE_SKU.priority(4), this.merchandiseSku);
		if (this.nonMerchandiseSku != null)
			fields.put(Field.NON_MERCHANDISE_SKU.priority(5), this.nonMerchandiseSku);
		if (this.relatedSKU != null)
			fields.put(Field.PARENT_SKU.priority(6), this.relatedSKU);
		if (this.masterItemId != null)
			fields.put(Field.MASTER_ITEM_ID.priority(7), this.masterItemId);
		if (this.loadSize != null)
			fields.put(Field.LOAD_SIZE.priority(8), this.loadSize.toString());
		if (this.threshold != null)
			fields.put(Field.THRESHOLD.priority(9), this.threshold.toString());
		return fields;
	}
	

}
