package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class VendorBean extends WebBean implements Serializable, Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5988334685723253951L;
	private String vendorId;
	private String vendorName;
	private Integer serviceProviderId;
	private Integer aggregationFrequency;
	private Integer aggregationMax;
	private Integer retryFrequency;
	private Integer retryMax;
	private Date lastAggregation;
	private Integer throttleFactor;
	private	String createdByUserId;
	private String updatedByUserId;
	private Date createdDate;
	private Date updatedDate;
	private String category;
	public VendorBean() {
		
	}
	public VendorBean(VendorBean vendorBean) {
		this.vendorId = vendorBean.vendorId;
		this.vendorName = vendorBean.vendorName;
		this.serviceProviderId = vendorBean.serviceProviderId;
		this.aggregationFrequency = vendorBean.aggregationFrequency;
		this.aggregationMax = vendorBean.aggregationMax;
		this.retryFrequency = vendorBean.retryFrequency;
		this.retryMax = vendorBean.retryMax;
		this.lastAggregation = vendorBean.lastAggregation;
		this.throttleFactor = vendorBean.throttleFactor;
		this.createdByUserId = vendorBean.createdByUserId;
		this.updatedByUserId = vendorBean.updatedByUserId;
		this.createdDate = vendorBean.createdDate;
		this.updatedDate = vendorBean.updatedDate;
		this.category = vendorBean.category;
	}
	public VendorBean(String vendorId, String vendorName, Integer serviceProviderId, Integer aggregationFrequency, Integer aggregatorMax, Integer retryFrequency, Integer retryMax, Date lastAggregation, Integer throttleFactor, String createdByUserId, String updatedByUserId, Date createdDate, Date updatedDate, String category) {
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.serviceProviderId = serviceProviderId;
		this.aggregationFrequency = aggregationFrequency;
		this.aggregationMax = aggregatorMax;
		this.retryFrequency = retryFrequency;
		this.retryMax = retryMax;
		this.lastAggregation = lastAggregation;
		this.throttleFactor = throttleFactor;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public Integer getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public Integer getAggregationFrequency() {
		return aggregationFrequency;
	}
	public void setAggregationFrequency(Integer aggregationFrequency) {
		this.aggregationFrequency = aggregationFrequency;
	}
	public Integer getAggregationMax() {
		return aggregationMax;
	}
	public void setAggregationMax(Integer aggregationMax) {
		this.aggregationMax = aggregationMax;
	}
	public Integer getRetryFrequency() {
		return retryFrequency;
	}
	public void setRetryFrequency(Integer retryFrequency) {
		this.retryFrequency = retryFrequency;
	}
	public Integer getRetryMax() {
		return retryMax;
	}
	public void setRetryMax(Integer retryMax) {
		this.retryMax = retryMax;
	}
	public Date getLastAggregation() {
		return lastAggregation;
	}
	public void setLastAggregation(Date lastAggregation) {
		this.lastAggregation = lastAggregation;
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
	public Integer getThrottleFactor() {
		return throttleFactor;
	}
	public void setThrottleFactor(Integer throttleFactor) {
		this.throttleFactor = throttleFactor;
	}
	@Override
	public String toString() {
		return ("<VendorBean") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" vendorName=\"" + (vendorName != null ? vendorName : "[null]") + "\"") +
		(" serviceProviderId=\"" + (serviceProviderId != null ? serviceProviderId : "[null]") + "\"") +
		(" aggregationFrequency=\"" + (aggregationFrequency != null ? aggregationFrequency : "[null]") + "\"") +
		(" aggregationMax=\"" + (aggregationMax != null ? aggregationMax : "[null]") + "\"") +
		(" retryFrequency=\"" + (retryFrequency != null ? retryFrequency : "[null]") + "\"") +
		(" retryMax=\"" + (retryMax != null ? retryMax : "[null]") + "\"") +
		(" lastAggregation=\"" + (lastAggregation != null ? DateUtil.getXmlDateString(lastAggregation) : "[null]") + "\"") +
		(" throttleFactor=\"" + (throttleFactor != null ? throttleFactor : "[null]") + "\"") +
		(" category=\"" + (category != null ? category : "[null]") + "\"") +
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
		if (this.serviceProviderId != null)
			fields.put(Field.SERVICE_PROVIDER_ID.priority(3), this.serviceProviderId.toString());
		if (this.aggregationFrequency != null)
			fields.put(Field.AGGREGATION_FREQUENCY.priority(4), this.aggregationFrequency.toString());
		if (this.aggregationMax != null)
			fields.put(Field.AGGREGATION_MAX.priority(5), this.aggregationMax.toString());
		if (this.retryFrequency != null)
			fields.put(Field.RETRY_FREQUENCY.priority(6), this.retryFrequency.toString());
		if (this.retryMax != null)
			fields.put(Field.RETRY_MAX.priority(7), this.retryMax.toString());
		if (this.throttleFactor != null)
			fields.put(Field.THROTTLE_FACTOR.priority(8), this.throttleFactor.toString());
		if (this.category != null)
			fields.put(Field.CATEGORY.priority(9), this.category.toString());
		return fields;
	}
}
