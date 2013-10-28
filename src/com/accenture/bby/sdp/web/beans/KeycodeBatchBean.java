package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class KeycodeBatchBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7508301234088915740L;
	private Long batchLoadId;
	private Integer productId;
	private String merchandiseSku;
	private String nonMerchandiseSku;
	private String masterItemId;
	private String vendorId;
	private String description;
	private String fileName;
	private Date minDate;
	private Date maxDate;
	private Date createdDate;
	private Date updatedDate;
	private String createdByUserId;
	private String updatedByUserId;
	private Integer loadSize;
	public KeycodeBatchBean() {
		
	}
	public KeycodeBatchBean(Long batchLoadId, Integer productId, String merchandiseSku, String nonMerchandiseSku, String masterItemId, String vendorId, String description, String fileName, Date minDate, Date maxDate, Date createdDate, Date updatedDate, String createdByUserId, String updatedByUserId, Integer loadSize) {
		this.batchLoadId = batchLoadId;
		this.productId = productId;
		this.merchandiseSku = merchandiseSku;
		this.nonMerchandiseSku = nonMerchandiseSku;
		this.masterItemId = masterItemId;
		this.vendorId = vendorId;
		this.description = description;
		this.fileName = fileName;
		this.minDate = minDate;
		this.maxDate = maxDate;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.loadSize = loadSize;
	}
	public KeycodeBatchBean(KeycodeBatchBean keycodeBatchBean) {
		this.batchLoadId = keycodeBatchBean.batchLoadId;
		this.productId = keycodeBatchBean.productId;
		this.merchandiseSku = keycodeBatchBean.merchandiseSku;
		this.nonMerchandiseSku = keycodeBatchBean.nonMerchandiseSku;
		this.masterItemId = keycodeBatchBean.masterItemId;
		this.vendorId = keycodeBatchBean.vendorId;
		this.description = keycodeBatchBean.description;
		this.fileName = keycodeBatchBean.fileName;
		this.minDate = keycodeBatchBean.minDate;
		this.maxDate = keycodeBatchBean.maxDate;
		this.createdDate = keycodeBatchBean.createdDate;
		this.updatedDate = keycodeBatchBean.updatedDate;
		this.createdByUserId = keycodeBatchBean.createdByUserId;
		this.updatedByUserId = keycodeBatchBean.updatedByUserId;
		this.loadSize = keycodeBatchBean.loadSize;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getMerchandiseSku() {
		return merchandiseSku;
	}
	public void setMerchandiseSku(String merchandiseSku) {
		this.merchandiseSku = merchandiseSku;
	}
	public String getNonMerchandiseSku() {
		return nonMerchandiseSku;
	}
	public void setNonMerchandiseSku(String nonMerchandiseSku) {
		this.nonMerchandiseSku = nonMerchandiseSku;
	}
	public String getMasterItemId() {
		return masterItemId;
	}
	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}
	public Date getMinDate() {
		return minDate;
	}
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public Long getBatchLoadId() {
		return batchLoadId;
	}
	public void setBatchLoadId(Long batchLoadId) {
		this.batchLoadId = batchLoadId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
		return Constants.getVendorName(vendorId);
	}
	public Integer getLoadSize() {
		return loadSize;
	}
	public void setLoadSize(Integer loadSize) {
		this.loadSize = loadSize;
	}
	@Override
	public String toString() {
		return "<KeycodeBatchBean" + 
		(" batchLoadId=\"" + (batchLoadId != null ? batchLoadId : "[null]") + "\"") +
		(" productId=\"" + (productId != null ? productId : "[null]") + "\"") +
		(" merchandiseSku=\"" + (merchandiseSku != null ? merchandiseSku : "[null]") + "\"") +
		(" nonMerchandiseSku=\"" + (nonMerchandiseSku != null ? nonMerchandiseSku : "[null]") + "\"") +
		(" masterItemId=\"" + (masterItemId != null ? masterItemId : "[null]") + "\"") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" description=\"" + (description != null ? description : "[null]") + "\"") +
		(" fileName=\"" + (fileName != null ? fileName : "[null]") + "\"") +
		(" loadSize=\"" + (loadSize != null ? loadSize : "[null]") + "\"") +
		(" minDate=\"" + (minDate != null ? DateUtil.getXmlDateString(minDate) : "[null]") + "\"") +
		(" maxDate=\"" + (maxDate != null ? DateUtil.getXmlDateString(maxDate) : "[null]") + "\"") +
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
		if (this.batchLoadId != null)
			fields.put(Field.BATCH_LOAD_ID.priority(1), this.batchLoadId.toString());
		if (this.productId != null)
			fields.put(Field.PRODUCT_ID.priority(2), this.productId.toString());
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(3), this.vendorId);
		if (this.merchandiseSku != null)
			fields.put(Field.MERCHANDISE_SKU.priority(4), this.merchandiseSku);
		if (this.nonMerchandiseSku != null)
			fields.put(Field.NON_MERCHANDISE_SKU.priority(5), this.nonMerchandiseSku);
		if (this.masterItemId != null)
			fields.put(Field.MASTER_ITEM_ID.priority(6), this.masterItemId);
		if (this.minDate != null)
			fields.put(Field.START_DATE.priority(7), DateUtil.getCalendarDateString(this.minDate));
		if (this.maxDate != null)
			fields.put(Field.END_DATE.priority(8), DateUtil.getCalendarDateString(this.maxDate));
		return fields;
	}
}
