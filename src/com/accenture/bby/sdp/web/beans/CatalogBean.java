package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;
import com.accenture.bby.sdp.web.beans.WorkFlowAttributeBean.OperationFlag;

public class CatalogBean extends WebBean implements Serializable, Auditable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1127430960376909613L;

	private String catalogId;
	private String masterVendorId;
	private String productType;
	private String vendorId;
	private String primarySku;
	private String primarySkuDescription;
	private String vendorTriggerSku;
	private String parentSku;
	private String offerType;
	private String category;
	private String subCategory;
	private Integer role;
	private Integer retryCount;
	private String commSatTemplateId;
	private Date preorderStreetDate;
	private String preorderStatus;
	private Date createdDate;
	private Date updatedDate;
	private String createdByUserId;
	private String updatedByUserId;
	
	private List<WorkFlowAttributeBean> workFlowAttributes = new ArrayList<WorkFlowAttributeBean>();
	
	public CatalogBean() {
		
	}
	public CatalogBean(CatalogBean catalogBean) {
		this.catalogId = catalogBean.catalogId;
		this.masterVendorId = catalogBean.masterVendorId;
		this.productType = catalogBean.productType;
		this.vendorId = catalogBean.vendorId;
		this.primarySku = catalogBean.primarySku;
		this.primarySkuDescription = catalogBean.primarySkuDescription;
		this.vendorTriggerSku = catalogBean.vendorTriggerSku;
		this.parentSku = catalogBean.parentSku;
		this.offerType = catalogBean.offerType;
		this.category = catalogBean.category;
		this.subCategory = catalogBean.subCategory;
		this.role = catalogBean.role;
		this.retryCount = catalogBean.retryCount;
		this.commSatTemplateId = catalogBean.commSatTemplateId;
		this.preorderStreetDate = catalogBean.preorderStreetDate;
		this.preorderStatus = catalogBean.preorderStatus;
		this.createdDate = catalogBean.createdDate;
		this.updatedDate = catalogBean.updatedDate;
		this.createdByUserId = catalogBean.createdByUserId;
		this.updatedByUserId = catalogBean.updatedByUserId;
		this.workFlowAttributes = catalogBean.workFlowAttributes;
	}
	public CatalogBean(String catalogId, String masterVendorId, String productType, String vendorId, String primarySku, String primarySkuDescription, String vendorTriggerSku, String parentSku, String offerType, String category, String subCategory, Integer role, Integer retryCount, String commSatTemplateId, Date preorderStreetDate, String preorderStatus,	Date createdDate, Date updatedDate, String createdByUserId, String updatedByUserId, List<WorkFlowAttributeBean> workFlowAttributes ) {
		this.catalogId = catalogId;
		this.masterVendorId = masterVendorId;
		this.productType = productType;
		this.vendorId = vendorId;
		this.primarySku = primarySku;
		this.primarySkuDescription = primarySkuDescription;
		this.vendorTriggerSku = vendorTriggerSku;
		this.parentSku = parentSku;
		this.offerType = offerType;
		this.category = category;
		this.subCategory = subCategory;
		this.role = role;
		this.retryCount = retryCount;
		this.commSatTemplateId = commSatTemplateId;
		this.preorderStreetDate = preorderStreetDate;
		this.preorderStatus = preorderStatus;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdByUserId = createdByUserId;
		this.updatedByUserId = updatedByUserId;
		this.workFlowAttributes = workFlowAttributes;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public String getMasterVendorId() {
		return masterVendorId;
	}
	public void setMasterVendorId(String masterVendorId) {
		this.masterVendorId = masterVendorId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getPrimarySku() {
		return primarySku;
	}
	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}
	public String getPrimarySkuDescription() {
		return primarySkuDescription;
	}
	public void setPrimarySkuDescription(String primarySkuDescription) {
		this.primarySkuDescription = primarySkuDescription;
	}
	public String getVendorTriggerSku() {
		return vendorTriggerSku;
	}
	public void setVendorTriggerSku(String vendorTriggerSku) {
		this.vendorTriggerSku = vendorTriggerSku;
	}
	public String getParentSku() {
		return parentSku;
	}
	public void setParentSku(String parentSku) {
		this.parentSku = parentSku;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
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
	public String getNeedsVendorProvisioning() {
		return getWorkFlowAttribute(Constants.NEEDS_VENDOR_PROVISIONING);
	}
	public void setNeedsVendorProvisioning(String needsVendorProvisioning) {
		setWorkFlowAttribute(Constants.NEEDS_VENDOR_PROVISIONING, needsVendorProvisioning);
	}
	public String getContractTrigger() {
		return getWorkFlowAttribute(Constants.CONTRACT_TRIGGER);
	}
	public void setContractTrigger(String contractTrigger) {
		setWorkFlowAttribute(Constants.CONTRACT_TRIGGER, contractTrigger);
	}
	public String getPreOrderHold() {
		return getWorkFlowAttribute(Constants.PREORDER_HOLD);
	}
	public void setPreOrderHold(String preOrderHold) {
		setWorkFlowAttribute(Constants.PREORDER_HOLD, preOrderHold);
	}
	public String getNeedCA() {
		return getWorkFlowAttribute(Constants.NEED_CA);
	}
	public void setNeedCA(String needCA) {
		setWorkFlowAttribute(Constants.NEED_CA, needCA);
	}
	public String getNeedsCommsatInteg() {
		return getWorkFlowAttribute(Constants.NEEDS_COMMSAT_INTEG);
	}
	public void setNeedsCommsatInteg(String needsCommsatInteg) {
		setWorkFlowAttribute(Constants.NEEDS_COMMSAT_INTEG, needsCommsatInteg);
	}
	public String getNeedsVendorKey() {
		return getWorkFlowAttribute(Constants.NEEDS_VENDOR_KEY);
	}
	public void setNeedsVendorKey(String needsVendorKey) {
		setWorkFlowAttribute(Constants.NEEDS_VENDOR_KEY, needsVendorKey);
	}
	public String getRedirectToOldArch() {
		return getWorkFlowAttribute(Constants.REDIRECT_TO_OLD_ARCH);
	}
	public void setRedirectToOldArch(String redirectToOldArch) {
		setWorkFlowAttribute(Constants.REDIRECT_TO_OLD_ARCH, redirectToOldArch);
	}
	public String getCommSatTemplateId() {
		return commSatTemplateId;
	}
	public void setCommSatTemplateId(String commSatTemplateId) {
		this.commSatTemplateId = commSatTemplateId;
	}
	public Date getPreorderStreetDate() {
		return preorderStreetDate;
	}
	public void setPreorderStreetDate(Date preorderStreetDate) {
		this.preorderStreetDate = preorderStreetDate;
	}
	public String getPreorderStatus() {
		return preorderStatus;
	}
	public String getPreorderStatusName() {
		return preorderStatus != null ? Constants.getStatusName(preorderStatus) : null;
	}
	public void setPreorderStatus(String preorderStatus) {
		this.preorderStatus = preorderStatus;
	}
	public String getVendorName() {
		return Constants.getVendorName(this.vendorId);
	}
	
	public List<WorkFlowAttributeBean> getWorkFlowAttributes() {
		return workFlowAttributes;
	}
	public void setWorkFlowAttributes(List<WorkFlowAttributeBean> workFlowAttributes) {
		this.workFlowAttributes = workFlowAttributes;
	}
	
	public boolean isCommsatFlagTrue() {
		String flag = getNeedsCommsatInteg();
		return flag != null && "TRUE".equals(flag.toUpperCase());
	}
	
	private void setWorkFlowAttribute(String key, String value) {
		for (WorkFlowAttributeBean attr : this.workFlowAttributes) {
			if (key.equals(attr.getAttributeName())) {
				attr.setAttributeValue(value);
				return;
			}
		}
		this.workFlowAttributes.add(new WorkFlowAttributeBean(key, value, OperationFlag.INSERT));
	}
	
	private String getWorkFlowAttribute(String key) {
		for (WorkFlowAttributeBean attr : this.workFlowAttributes) {
			if (key.equals(attr.getAttributeName())) {
				return attr.getAttributeValue();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(); builder.append( ("<CatalogBean") +
		(" catalogId=\"" + (catalogId != null ? catalogId : "[null]") + "\"") +
		(" masterVendorId=\"" + (masterVendorId != null ? masterVendorId : "[null]") + "\"") +
		(" productType=\"" + (productType != null ? productType : "[null]") + "\"") +
		(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
		(" primarySku=\"" + (primarySku != null ? primarySku : "[null]") + "\"") +
		(" primarySkuDescription=\"" + (primarySkuDescription != null ? primarySkuDescription : "[null]") + "\"") +
		(" vendorTriggerSku=\"" + (vendorTriggerSku != null ? vendorTriggerSku : "[null]") + "\"") +
		(" parentSku=\"" + (parentSku != null ? parentSku : "[null]") + "\"") +
		(" offerType=\"" + (offerType != null ? offerType : "[null]") + "\"") +
		(" category=\"" + (category != null ? category : "[null]") + "\"") +
		(" subCategory=\"" + (subCategory != null ? subCategory : "[null]") + "\"") +
		(" role=\"" + (role != null ? role : "[null]") + "\"") +
		(" retryCount=\"" + (retryCount != null ? retryCount : "[null]") + "\"") +
		(" needsVendorProvisioning=\"" + (getNeedsVendorProvisioning() != null ? getNeedsVendorProvisioning() : "[null]") + "\"") +
		(" contractTrigger=\"" + (getContractTrigger() != null ? getContractTrigger() : "[null]") + "\"") +
		(" preOrderHold=\"" + (getPreOrderHold() != null ? getPreOrderHold() : "[null]") + "\"") +
		(" needCA=\"" + (getNeedCA() != null ? getNeedCA() : "[null]") + "\"") +
		(" needsCommsatInteg=\"" + (getNeedsCommsatInteg() != null ? getNeedsCommsatInteg() : "[null]") + "\"") +
		(" needsVendorKey=\"" + (getNeedsVendorKey() != null ? getNeedsVendorKey() : "[null]") + "\"") +
		(" redirectToOldArch=\"" + (getRedirectToOldArch() != null ? getRedirectToOldArch() : "[null]") + "\"") +
		(" commSatTemplateId=\"" + (commSatTemplateId != null ? commSatTemplateId : "[null]") + "\"") +
		(" preorderStatus=\"" + (preorderStatus != null ? preorderStatus : "[null]") + "\"") +
		(" preorderStreetDate=\"" + (preorderStreetDate != null ? DateUtil.getXmlDateString(preorderStreetDate) : "[null]") + "\"") +
		(" createdByUserId=\"" + (createdByUserId != null ? createdByUserId : "[null]") + "\"") +
		(" updatedByUserId=\"" + (updatedByUserId != null ? updatedByUserId : "[null]") + "\"") +
		(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
		(" updatedDate=\"" + (updatedDate != null ? DateUtil.getXmlDateString(updatedDate) : "[null]") + "\"")
		);
		if (workFlowAttributes != null) {
			for (WorkFlowAttributeBean attr : workFlowAttributes) {
				builder.append("\n  " + attr);
			}
			builder.append("\n</CatalogBean>");
		} else {
			builder.append(" />");
		}
		return builder.toString();
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
		return primarySku;
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
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(2), this.vendorId);
		if (this.primarySkuDescription != null)
			fields.put(Field.PRIMARY_SKU_DESCRIPTION.priority(3), this.primarySkuDescription);
		if (this.masterVendorId != null)
			fields.put(Field.MASTER_VENDOR_ID.priority(4), this.masterVendorId);
		if (this.primarySku != null)
			fields.put(Field.PRIMARY_SKU.priority(5), this.primarySku);
		if (this.parentSku != null)
			fields.put(Field.PARENT_SKU.priority(6), this.parentSku);
		if (this.vendorTriggerSku != null)
			fields.put(Field.VENDOR_TRIGGER_SKU.priority(7), this.vendorTriggerSku);
		if (this.productType != null)
			fields.put(Field.PRODUCT_TYPE.priority(8), this.productType);
		if (this.offerType != null)
			fields.put(Field.OFFER_TYPE.priority(9), this.offerType);
		if (this.category != null)
			fields.put(Field.CATEGORY.priority(10), this.category);
		if (this.subCategory != null)
			fields.put(Field.SUB_CATEGORY.priority(11), this.subCategory);
		if (this.role != null)
			fields.put(Field.ROLE.priority(12), this.role.toString());
		if (this.retryCount != null)
			fields.put(Field.RETRY_COUNT.priority(13), this.retryCount.toString());
		if (this.getNeedsVendorProvisioning() != null)
			fields.put(Field.NEEDS_VENDOR_PROVISIONING_FLAG.priority(14), this.getNeedsVendorProvisioning());
		if (this.getContractTrigger() != null)
			fields.put(Field.CONTRACT_TRIGGER_FLAG.priority(15), this.getContractTrigger());
		if (this.getNeedCA() != null)
			fields.put(Field.NEED_CA_FLAG.priority(16), this.getNeedCA());
		if (this.getNeedsCommsatInteg() != null)
			fields.put(Field.NEEDS_COMMSAT_INTEG_FLAG.priority(17), this.getNeedsCommsatInteg());
		if (this.getNeedsVendorKey() != null)
			fields.put(Field.NEEDS_VENDOR_KEY_FLAG.priority(18), this.getNeedsVendorKey());
		if (this.getRedirectToOldArch() != null)
			fields.put(Field.REDIRECT_TO_OLD_ARCH_FLAG.priority(19), this.getRedirectToOldArch());
		if (this.commSatTemplateId != null)
			fields.put(Field.COMMSAT_TEMPLATE_ID.priority(20), this.commSatTemplateId);
		return fields;
	}
	
	
}
