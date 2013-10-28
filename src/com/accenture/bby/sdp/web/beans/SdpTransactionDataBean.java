package com.accenture.bby.sdp.web.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

/**
 * @author a719057
 * 
 */
public class SdpTransactionDataBean extends WebBean implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2611404996318891823L;
	private Date recordCreatedDate;
	private String contractId;
	private Date contractEndDate;
	private String inProductMessageCode;
	private String serialNumber;
	private String regId;
	private String pspId;
	private String lineItemId;
	private String confirmationCode;
	private Date fpkTransactionDate;
	private String fpkStoreId;
	private String fpkRegisterId;
	private String fpkTransactionId;
	private String fpkLineId;
	private Date originalFpkTransactionDate;
	private String originalFpkStoreId;
	private String originalFpkRegisterId;
	private String originalFpkTransactionId;
	private String originalFpkLineId;
	private String customerFirstName;
	private String customerMiddleName;
	private String customerLastName;
	private String customerContactEmail;
	private String deliveryEmail;
	private String addressLine1;
	private String addressLine2;
	private String addressLabel;
	private String addressCity;
	private String addressState;
	private String addressZipcode;
	private String addressCountry;
	private String phoneNumber;
	private String phoneLabel;
	private String rewardZoneNumber;
	private String creditCardType;
	private String creditCardExpireDate;
	private String creditCardCustomerName;
	private String creditCardToken;
	private String primarySku;
	private String primarySkuPrice;
	private String primarySkuTaxAmount;
	private String primarySkuTaxRate;
	private String relatedSku;
	private String relatedSkuPrice;
	private String relatedSkuTaxAmount;
	private String relatedSkuTaxRate;
	private String vendorProductSku;
	private String sdpOrderStatus;
	private String sdpOrderId;
	private String valuePackageId;
	private String keyCode;
	private String quantity;
	private String cancelReasonCode;
	private String adjustmentReasonCode;
	private String vendorId;
	private String sdpId;
	private String sourceSystemId;
	private String caPartyId;
	private String customerId;
	private String digitalId;
	private String catalogId;
	private String category;
	private String subCategory;
	private String role;
	private String transactionDateTime;
	private boolean isRtaSuccessful;
	private String digitalPrice;
	private String digitalTaxAmount;
	private String digitalTaxRate;
	private String masterItemId;
	private String masterVendorId;
	private String vendorDigitalId;
	private String digitalProductType;
	private String productDescription;
	private String productType;
	private String promoSku;
	private String promoPrice;
	private String promoCode;
	private Date promoStartDate;
	private Date promoEndDate;

	public SdpTransactionDataBean() {

	}

	public SdpTransactionDataBean(SdpTransactionDataBean other) {
		this.recordCreatedDate = other.recordCreatedDate;
		this.contractId = other.contractId;
		this.contractEndDate = other.contractEndDate;
		this.inProductMessageCode = other.inProductMessageCode;
		this.serialNumber = other.serialNumber;
		this.regId = other.regId;
		this.pspId = other.pspId;
		this.lineItemId = other.lineItemId;
		this.confirmationCode = other.confirmationCode;
		this.fpkTransactionDate = other.fpkTransactionDate;
		this.fpkStoreId = other.fpkStoreId;
		this.fpkRegisterId = other.fpkRegisterId;
		this.fpkTransactionId = other.fpkTransactionId;
		this.fpkLineId = other.fpkLineId;
		this.originalFpkTransactionDate = other.originalFpkTransactionDate;
		this.originalFpkStoreId = other.originalFpkStoreId;
		this.originalFpkRegisterId = other.originalFpkRegisterId;
		this.originalFpkTransactionId = other.originalFpkTransactionId;
		this.originalFpkLineId = other.originalFpkLineId;
		this.customerFirstName = other.customerFirstName;
		this.customerMiddleName = other.customerMiddleName;
		this.customerLastName = other.customerLastName;
		this.customerContactEmail = other.customerContactEmail;
		this.deliveryEmail = other.deliveryEmail;
		this.addressLine1 = other.addressLine1;
		this.addressLine2 = other.addressLine2;
		this.addressLabel = other.addressLabel;
		this.addressCity = other.addressCity;
		this.addressState = other.addressState;
		this.addressZipcode = other.addressZipcode;
		this.addressCountry = other.addressCountry;
		this.phoneNumber = other.phoneNumber;
		this.phoneLabel = other.phoneLabel;
		this.rewardZoneNumber = other.rewardZoneNumber;
		this.creditCardType = other.creditCardType;
		this.creditCardExpireDate = other.creditCardExpireDate;
		this.creditCardCustomerName = other.creditCardCustomerName;
		this.creditCardToken = other.creditCardToken;
		this.primarySku = other.primarySku;
		this.primarySkuPrice = other.primarySkuPrice;
		this.primarySkuTaxAmount = other.primarySkuTaxAmount;
		this.primarySkuTaxRate = other.primarySkuTaxRate;
		this.relatedSku = other.relatedSku;
		this.relatedSkuPrice = other.relatedSkuPrice;
		this.relatedSkuTaxAmount = other.relatedSkuTaxAmount;
		this.relatedSkuTaxRate = other.relatedSkuTaxRate;
		this.vendorProductSku = other.vendorProductSku;
		this.sdpOrderStatus = other.sdpOrderStatus;
		this.sdpOrderId = other.sdpOrderId;
		this.valuePackageId = other.valuePackageId;
		this.keyCode = other.keyCode;
		this.quantity = other.quantity;
		this.cancelReasonCode = other.cancelReasonCode;
		this.vendorId = other.vendorId;
		this.sdpId = other.sdpId;
		this.sourceSystemId = other.sourceSystemId;
		this.caPartyId = other.caPartyId;
		this.customerId = other.customerId;
		this.digitalId = other.digitalId;
		this.catalogId = other.catalogId;
		this.category = other.category;
		this.subCategory = other.subCategory;
		this.role = other.role;
		this.transactionDateTime = other.transactionDateTime;
		this.isRtaSuccessful = other.isRtaSuccessful;
		this.digitalPrice = other.digitalPrice;
		this.digitalTaxAmount = other.digitalTaxAmount;
		this.digitalTaxRate = other.digitalTaxRate;
		this.masterItemId = other.masterItemId;
		this.masterVendorId = other.masterVendorId;
		this.vendorDigitalId = other.vendorDigitalId;
		this.digitalProductType = other.digitalProductType;
		this.productDescription = other.productDescription;
		this.productType = other.productType;
		this.promoSku = other.promoSku;
		this.promoPrice = other.promoPrice;
		this.promoCode = other.promoCode;
		this.promoStartDate = other.promoStartDate;
		this.promoEndDate = other.promoEndDate;
	}

	/**
	 * constructs a SdpTransactionDataBean from an existing resultset. this is
	 * only used by the <code>SdpTransactionDBWrapper</code> class. the calling
	 * class is responsible for creating and executing the correct query and for
	 * closing the resources.
	 * 
	 * @see com.accenture.bby.sdp.db.SdpTransactionDBWrapper
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public SdpTransactionDataBean(ResultSet rs) throws SQLException {
		this.setSdpOrderId(rs.getString(1));
		this.setRecordCreatedDate(rs.getTimestamp(2));
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
		this.setContractId(rs.getString(16));
		this.setContractEndDate(rs.getTimestamp(17));
		this.setSerialNumber(rs.getString(18));
		this.setSdpId(rs.getString(19));
		this.setPrimarySkuPrice(rs.getString(20));
		this.setPrimarySkuTaxAmount(rs.getString(21));
		this.setPrimarySkuTaxRate(rs.getString(22));
		this.setRelatedSkuPrice(rs.getString(23));
		this.setRelatedSkuTaxAmount(rs.getString(24));
		this.setRelatedSkuTaxRate(rs.getString(25));
		this.setQuantity(rs.getString(26));
		this.setValuePackageId(rs.getString(27));
		this.setKeyCode(rs.getString(28));
		this.setVendorId(rs.getString(29));
		this.setVendorProductSku(rs.getString(30));
		this.setCancelReasonCode(rs.getString(31));
		this.setCustomerId(rs.getString(32));
		this.setCustomerContactEmail(rs.getString(33));
		this.setCustomerMiddleName(rs.getString(34));
		this.setAddressLine1(rs.getString(35));
		this.setAddressLine2(rs.getString(36));
		this.setAddressLabel(rs.getString(37));
		this.setAddressCity(rs.getString(38));
		this.setAddressState(rs.getString(39));
		this.setAddressZipcode(rs.getString(40));
		this.setPhoneNumber(rs.getString(41));
		this.setPhoneLabel(rs.getString(42));
		this.setAddressCountry(rs.getString(43));
		this.setCreditCardToken(rs.getString(44));
		this.setCreditCardType(rs.getString(45));
		this.setCreditCardCustomerName(rs.getString(46));
		this.setCreditCardExpireDate(rs.getString(47));
		this.setCatalogId(rs.getString(48));
		this.setCategory(rs.getString(49));
		this.setSubCategory(rs.getString(50));
		this.setProductDescription(rs.getString(51));
		this.setProductType(rs.getString(52));
		this.setDigitalProductType(rs.getString(52));
		this.setMasterVendorId(rs.getString(53));
		this.setMasterItemId(rs.getString(54));
		this.setCustomerId(rs.getString(55));
		this.setSourceSystemId(rs.getString(56));
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getSourceSystemId() {
		return sourceSystemId;
	}

	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	public String getSdpId() {
		return sdpId;
	}

	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}

	public Date getRecordCreatedDate() {
		return recordCreatedDate;
	}

	public void setRecordCreatedDate(Date recordCreatedDate) {
		this.recordCreatedDate = recordCreatedDate;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getInProductMessageCode() {
		return inProductMessageCode;
	}

	public void setInProductMessageCode(String inProductMessageCode) {
		this.inProductMessageCode = inProductMessageCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @return the SerialNumber or PSPID or REGID in listed priority
	 */
	public String getVendorKey() {
		return serialNumber != null ? serialNumber : (pspId != null ? pspId : regId);
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getPspId() {
		return pspId;
	}

	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
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

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerMiddleName() {
		return customerMiddleName;
	}

	public void setCustomerMiddleName(String customerMiddleName) {
		this.customerMiddleName = customerMiddleName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerContactEmail() {
		return customerContactEmail;
	}

	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	public String getDeliveryEmail() {
		return deliveryEmail;
	}

	public void setDeliveryEmail(String deliveryEmail) {
		this.deliveryEmail = deliveryEmail;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressZipcode() {
		return addressZipcode;
	}

	public void setAddressZipcode(String addressZipcode) {
		this.addressZipcode = addressZipcode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneLabel() {
		return phoneLabel;
	}

	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	public String getRewardZoneNumber() {
		return rewardZoneNumber;
	}

	public void setRewardZoneNumber(String rewardZoneNumber) {
		this.rewardZoneNumber = rewardZoneNumber;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardExpireDate() {
		return creditCardExpireDate;
	}

	public void setCreditCardExpireDate(String creditCardExpireDate) {
		this.creditCardExpireDate = creditCardExpireDate;
	}

	public String getCreditCardCustomerName() {
		return creditCardCustomerName;
	}

	public void setCreditCardCustomerName(String creditCardCustomerName) {
		this.creditCardCustomerName = creditCardCustomerName;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}

	public String getPrimarySku() {
		return primarySku;
	}

	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}

	public String getPrimarySkuPrice() {
		return primarySkuPrice;
	}

	public void setPrimarySkuPrice(String primarySkuPrice) {
		this.primarySkuPrice = primarySkuPrice;
	}

	public String getPrimarySkuTaxAmount() {
		return primarySkuTaxAmount;
	}

	public void setPrimarySkuTaxAmount(String primarySkuTaxAmount) {
		this.primarySkuTaxAmount = primarySkuTaxAmount;
	}

	public String getPrimarySkuTaxRate() {
		return primarySkuTaxRate;
	}

	public void setPrimarySkuTaxRate(String primarySkuTaxRate) {
		this.primarySkuTaxRate = primarySkuTaxRate;
	}

	public String getRelatedSku() {
		return relatedSku;
	}

	public void setRelatedSku(String relatedSku) {
		this.relatedSku = relatedSku;
	}

	public String getRelatedSkuPrice() {
		return relatedSkuPrice;
	}

	public void setRelatedSkuPrice(String relatedSkuPrice) {
		this.relatedSkuPrice = relatedSkuPrice;
	}

	public String getRelatedSkuTaxAmount() {
		return relatedSkuTaxAmount;
	}

	public void setRelatedSkuTaxAmount(String relatedSkuTaxAmount) {
		this.relatedSkuTaxAmount = relatedSkuTaxAmount;
	}

	public String getRelatedSkuTaxRate() {
		return relatedSkuTaxRate;
	}

	public void setRelatedSkuTaxRate(String relatedSkuTaxRate) {
		this.relatedSkuTaxRate = relatedSkuTaxRate;
	}

	public String getVendorProductSku() {
		return vendorProductSku;
	}

	public void setVendorProductSku(String vendorProductSku) {
		this.vendorProductSku = vendorProductSku;
	}

	public String getSdpOrderStatus() {
		return sdpOrderStatus;
	}

	public void setSdpOrderStatus(String sdpOrderStatus) {
		this.sdpOrderStatus = sdpOrderStatus;
	}

	public String getSdpOrderId() {
		return sdpOrderId;
	}

	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	public String getValuePackageId() {
		return valuePackageId;
	}

	public void setValuePackageId(String valuePackageId) {
		this.valuePackageId = valuePackageId;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getCancelReasonCode() {
		return cancelReasonCode;
	}

	public void setCancelReasonCode(String cancelReasonCode) {
		this.cancelReasonCode = cancelReasonCode;
	}

	public String getAdjustmentReasonCode() {
		return adjustmentReasonCode;
	}

	public void setAdjustmentReasonCode(String adjustmentReasonCode) {
		this.adjustmentReasonCode = adjustmentReasonCode;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getCaPartyId() {
		return caPartyId;
	}

	public void setCaPartyId(String caPartyId) {
		this.caPartyId = caPartyId;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDigitalId() {
		return digitalId;
	}

	public void setDigitalId(String digitalId) {
		this.digitalId = digitalId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
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

	public String getCustomerFullName() {
		return (getCustomerFirstName() != null ? (getCustomerFirstName() + " ") : "") + (getCustomerLastName() != null ? getCustomerLastName() : "");
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getOriginalFpkTransactionDate() {
		return originalFpkTransactionDate;
	}

	public void setOriginalFpkTransactionDate(Date originalFpkTransactionDate) {
		this.originalFpkTransactionDate = originalFpkTransactionDate;
	}

	public String getOriginalFpkStoreId() {
		return originalFpkStoreId;
	}

	public void setOriginalFpkStoreId(String originalFpkStoreId) {
		this.originalFpkStoreId = originalFpkStoreId;
	}

	public String getOriginalFpkRegisterId() {
		return originalFpkRegisterId;
	}

	public void setOriginalFpkRegisterId(String originalFpkRegisterId) {
		this.originalFpkRegisterId = originalFpkRegisterId;
	}

	public String getOriginalFpkTransactionId() {
		return originalFpkTransactionId;
	}

	public void setOriginalFpkTransactionId(String originalFpkTransactionId) {
		this.originalFpkTransactionId = originalFpkTransactionId;
	}

	public String getOriginalFpkLineId() {
		return originalFpkLineId;
	}

	public void setOriginalFpkLineId(String originalFpkLineId) {
		this.originalFpkLineId = originalFpkLineId;
	}

	public boolean isRtaSuccessful() {
		return isRtaSuccessful;
	}

	public void setRtaSuccessful(boolean isRtaSuccessful) {
		this.isRtaSuccessful = isRtaSuccessful;
	}

	public String getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public String getDigitalPrice() {
		return digitalPrice;
	}

	public void setDigitalPrice(String digitalPrice) {
		this.digitalPrice = digitalPrice;
	}

	public String getDigitalTaxAmount() {
		return digitalTaxAmount;
	}

	public void setDigitalTaxAmount(String digitalTaxAmount) {
		this.digitalTaxAmount = digitalTaxAmount;
	}

	public String getDigitalTaxRate() {
		return digitalTaxRate;
	}

	public void setDigitalTaxRate(String digitalTaxRate) {
		this.digitalTaxRate = digitalTaxRate;
	}

	public String getMasterItemId() {
		return masterItemId;
	}

	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}

	public String getMasterVendorId() {
		return masterVendorId;
	}

	public void setMasterVendorId(String masterVendorId) {
		this.masterVendorId = masterVendorId;
	}

	public String getVendorDigitalId() {
		return vendorDigitalId;
	}

	public void setVendorDigitalId(String vendorDigitalId) {
		this.vendorDigitalId = vendorDigitalId;
	}

	public String getDigitalProductType() {
		return digitalProductType;
	}

	public void setDigitalProductType(String digitalProductType) {
		this.digitalProductType = digitalProductType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getPromoSku() {
		return promoSku;
	}

	public void setPromoSku(String promoSku) {
		this.promoSku = promoSku;
	}

	public String getPromoPrice() {
		return promoPrice;
	}

	public void setPromoPrice(String promoPrice) {
		this.promoPrice = promoPrice;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Date getPromoStartDate() {
		return promoStartDate;
	}

	public void setPromoStartDate(Date promoStartDate) {
		this.promoStartDate = promoStartDate;
	}

	public Date getPromoEndDate() {
		return promoEndDate;
	}

	public void setPromoEndDate(Date promoEndDate) {
		this.promoEndDate = promoEndDate;
	}

	public String generateLineItemIdByFivePartKey() {
		if (this.isOriginalFivePartKeyPresent()) {
			return generateLineItemIdByFivePartKey(this.getOriginalFpkTransactionDate(), this.getOriginalFpkStoreId(), this.getOriginalFpkRegisterId(), this.getOriginalFpkTransactionId(),
					this.getOriginalFpkLineId());
		} else {
			return generateLineItemIdByFivePartKey(this.getFpkTransactionDate(), this.getFpkStoreId(), this.getFpkRegisterId(), this.getFpkTransactionId(), this.getFpkLineId());
		}
	}

	public static String generateLineItemIdByFivePartKey(Date date, String storeId, String registerId, String transactionId, String lineId) {
		return storeId + "-" + registerId + "-" + transactionId + "-" + DateUtil.getShortXmlDateString(date) + "-" + lineId;
	}

	public String getVendorName() {
		return Constants.getVendorName(vendorId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("getRecordCreatedDate" + "\t" + this.getRecordCreatedDate() + "\n");
		builder.append("getContractId" + "\t" + this.getContractId() + "\n");
		builder.append("getContractEndDate" + "\t" + this.getContractEndDate() + "\n");
		builder.append("getInProductMessageCode" + "\t" + this.getInProductMessageCode() + "\n");
		builder.append("getSerialNumber" + "\t" + this.getSerialNumber() + "\n");
		builder.append("getRegId" + "\t" + this.getRegId() + "\n");
		builder.append("getPspId" + "\t" + this.getPspId() + "\n");
		builder.append("getLineItemId" + "\t" + this.getLineItemId() + "\n");
		builder.append("getConfirmationCode" + "\t" + this.getConfirmationCode() + "\n");
		builder.append("getFpkTransactionDate" + "\t" + this.getFpkTransactionDate() + "\n");
		builder.append("getFpkStoreId" + "\t" + this.getFpkStoreId() + "\n");
		builder.append("getFpkRegisterId" + "\t" + this.getFpkRegisterId() + "\n");
		builder.append("getFpkTransactionId" + "\t" + this.getFpkTransactionId() + "\n");
		builder.append("getFpkLineId" + "\t" + this.getFpkLineId() + "\n");
		builder.append("getOriginalFpkTransactionDate" + "\t" + this.getOriginalFpkTransactionDate() + "\n");
		builder.append("getOriginalFpkStoreId" + "\t" + this.getOriginalFpkStoreId() + "\n");
		builder.append("getOriginalFpkRegisterId" + "\t" + this.getOriginalFpkRegisterId() + "\n");
		builder.append("getOriginalFpkTransactionId" + "\t" + this.getOriginalFpkTransactionId() + "\n");
		builder.append("getOriginalFpkLineId" + "\t" + this.getOriginalFpkLineId() + "\n");
		builder.append("getCustomerFirstName" + "\t" + this.getCustomerFirstName() + "\n");
		builder.append("getCustomerMiddleName" + "\t" + this.getCustomerMiddleName() + "\n");
		builder.append("getCustomerLastName" + "\t" + this.getCustomerLastName() + "\n");
		builder.append("getCustomerContactEmail" + "\t" + this.getCustomerContactEmail() + "\n");
		builder.append("getDeliveryEmail" + "\t" + this.getDeliveryEmail() + "\n");
		builder.append("getAddressLine1" + "\t" + this.getAddressLine1() + "\n");
		builder.append("getAddressLine2" + "\t" + this.getAddressLine2() + "\n");
		builder.append("getAddressLabel" + "\t" + this.getAddressLabel() + "\n");
		builder.append("getAddressCity" + "\t" + this.getAddressCity() + "\n");
		builder.append("getAddressState" + "\t" + this.getAddressState() + "\n");
		builder.append("getAddressZipcode" + "\t" + this.getAddressZipcode() + "\n");
		builder.append("getAddressCountry" + "\t" + this.getAddressCountry() + "\n");
		builder.append("getPhoneNumber" + "\t" + this.getPhoneNumber() + "\n");
		builder.append("getPhoneLabel" + "\t" + this.getPhoneLabel() + "\n");
		builder.append("getRewardZoneNumber" + "\t" + this.getRewardZoneNumber() + "\n");
		builder.append("getCreditCardType" + "\t" + this.getCreditCardType() + "\n");
		builder.append("getCreditCardExpireDate" + "\t" + this.getCreditCardExpireDate() + "\n");
		builder.append("getCreditCardCustomerName" + "\t" + this.getCreditCardCustomerName() + "\n");
		builder.append("getCreditCardToken" + "\t" + this.getCreditCardToken() + "\n");
		builder.append("getPrimarySku" + "\t" + this.getPrimarySku() + "\n");
		builder.append("getPrimarySkuPrice" + "\t" + this.getPrimarySkuPrice() + "\n");
		builder.append("getPrimarySkuTaxAmount" + "\t" + this.getPrimarySkuTaxAmount() + "\n");
		builder.append("getPrimarySkuTaxRate" + "\t" + this.getPrimarySkuTaxRate() + "\n");
		builder.append("getRelatedSku" + "\t" + this.getRelatedSku() + "\n");
		builder.append("getRelatedSkuPrice" + "\t" + this.getRelatedSkuPrice() + "\n");
		builder.append("getRelatedSkuTaxAmount" + "\t" + this.getRelatedSkuTaxAmount() + "\n");
		builder.append("getRelatedSkuTaxRate" + "\t" + this.getRelatedSkuTaxRate() + "\n");
		builder.append("getVendorProductSku" + "\t" + this.getVendorProductSku() + "\n");
		builder.append("getSdpOrderStatus" + "\t" + this.getSdpOrderStatus() + "\n");
		builder.append("getSdpOrderId" + "\t" + this.getSdpOrderId() + "\n");
		builder.append("getValuePackageId" + "\t" + this.getValuePackageId() + "\n");
		builder.append("getKeyCode" + "\t" + this.getKeyCode() + "\n");
		builder.append("getQuantity" + "\t" + this.getQuantity() + "\n");
		builder.append("getCancelReasonCode" + "\t" + this.getCancelReasonCode() + "\n");
		builder.append("getVendorId" + "\t" + this.getVendorId() + "\n");
		builder.append("getSdpId" + "\t" + this.getSdpId() + "\n");
		builder.append("getSourceSystemId" + "\t" + this.getSourceSystemId() + "\n");
		builder.append("getCaPartyId" + "\t" + this.getCaPartyId() + "\n");
		builder.append("getCustomerId" + "\t" + this.getCustomerId() + "\n");
		builder.append("getDigitalId" + "\t" + this.getDigitalId() + "\n");
		builder.append("getCatalogId" + "\t" + this.getCatalogId() + "\n");
		builder.append("getCategory" + "\t" + this.getCategory() + "\n");
		builder.append("getSubCategory" + "\t" + this.getSubCategory() + "\n");
		builder.append("getRole" + "\t" + this.getRole() + "\n");
		builder.append("getTransactionDateTime" + "\t" + this.getTransactionDateTime() + "\n");
		builder.append("isRtaSuccessful" + "\t" + this.isRtaSuccessful() + "\n");
		builder.append("getDigitalPrice" + "\t" + this.getDigitalPrice() + "\n");
		builder.append("getDigitalTaxAmount" + "\t" + this.getDigitalTaxAmount() + "\n");
		builder.append("getDigitalTaxRate" + "\t" + this.getDigitalTaxRate() + "\n");
		builder.append("getMasterItemId" + "\t" + this.getMasterItemId() + "\n");
		builder.append("getMasterVendorId" + "\t" + this.getMasterVendorId() + "\n");
		builder.append("getVendorDigitalId" + "\t" + this.getVendorDigitalId() + "\n");
		builder.append("getDigitalProductType" + "\t" + this.getDigitalProductType() + "\n");
		builder.append("getVendorName" + "\t" + this.getVendorName());
		builder.append("getProductDescription" + "\t" + this.getProductDescription());

		return builder.toString();
	}

	@Override
	public String getSerialNumberForAudit() {
		return this.getSerialNumber();
	}

	@Override
	public String getLineItemIdForAudit() {
		return this.getLineItemId();
	}

	@Override
	public String getPrimarySkuForAudit() {
		return this.getPrimarySku();
	}

	@Override
	public String getContractIdForAudit() {
		return this.getContractId();
	}

	@Override
	public String getVendorIdForAudit() {
		return this.getVendorId();
	}

	@Override
	public String getMasterItemIdForAudit() {
		return masterItemId;
	}

	@Override
	public Map<Field, String> getAuditableFields() {
		Map<Field, String> fields = new HashMap<Field, String>();
		int priority = 0; // incrementing priority value
		if (this.sourceSystemId != null)
			fields.put(Field.SOURCE_SYSTEM_ID.priority(++priority), this.sourceSystemId);
		if (this.fpkTransactionDate != null)
			fields.put(Field.FPK_TRANSACTION_DATE.priority(++priority), DateUtil.getStoreDateString(this.fpkTransactionDate));
		if (this.fpkStoreId != null)
			fields.put(Field.FPK_STORE_ID.priority(++priority), this.fpkStoreId);
		if (this.fpkRegisterId != null)
			fields.put(Field.FPK_REGISTER_ID.priority(++priority), this.fpkRegisterId);
		if (this.fpkTransactionId != null)
			fields.put(Field.FPK_TRANSACTION_ID.priority(++priority), this.fpkTransactionId);
		if (this.fpkLineId != null)
			fields.put(Field.FPK_LINE_ID.priority(++priority), this.fpkLineId);
		if (this.originalFpkTransactionDate != null)
			fields.put(Field.FPK_ORIGINAL_TRANSACTION_DATE.priority(++priority), DateUtil.getStoreDateString(this.originalFpkTransactionDate));
		if (this.originalFpkStoreId != null)
			fields.put(Field.FPK_ORIGINAL_STORE_ID.priority(++priority), this.originalFpkStoreId);
		if (this.originalFpkRegisterId != null)
			fields.put(Field.FPK_ORIGINAL_REGISTER_ID.priority(++priority), this.originalFpkRegisterId);
		if (this.originalFpkTransactionId != null)
			fields.put(Field.FPK_ORIGINAL_TRANSACTION_ID.priority(++priority), this.originalFpkTransactionId);
		if (this.originalFpkLineId != null)
			fields.put(Field.FPK_ORIGINAL_LINE_ID.priority(++priority), this.originalFpkLineId);
		if (this.lineItemId != null)
			fields.put(Field.LINE_ITEM_ID.priority(++priority), this.lineItemId);
		if (this.sdpId != null)
			fields.put(Field.SDP_ID.priority(++priority), this.sdpId);
		if (this.primarySku != null)
			fields.put(Field.PRIMARY_SKU.priority(++priority), this.primarySku);
		if (this.primarySkuPrice != null)
			fields.put(Field.PRIMARY_SKU_PRICE.priority(++priority), this.primarySkuPrice);
		if (this.primarySkuTaxAmount != null)
			fields.put(Field.PRIMARY_SKU_TAX_AMOUNT.priority(++priority), this.primarySkuTaxAmount);
		if (this.primarySkuTaxRate != null)
			fields.put(Field.PRIMARY_SKU_TAX_RATE.priority(++priority), this.primarySkuTaxRate);
		if (this.relatedSku != null)
			fields.put(Field.PARENT_SKU.priority(++priority), this.relatedSku);
		if (this.relatedSkuPrice != null)
			fields.put(Field.PARENT_SKU_PRICE.priority(++priority), this.relatedSkuPrice);
		if (this.relatedSkuTaxAmount != null)
			fields.put(Field.PARENT_SKU_TAX_AMOUNT.priority(++priority), this.relatedSkuTaxAmount);
		if (this.relatedSkuTaxRate != null)
			fields.put(Field.PARENT_SKU_TAX_RATE.priority(++priority), this.relatedSkuTaxRate);
		if (this.vendorProductSku != null)
			fields.put(Field.VENDOR_TRIGGER_SKU.priority(++priority), this.vendorProductSku);
		if (this.digitalId != null)
			fields.put(Field.DIGITAL_ID.priority(++priority), this.digitalId);
		if (this.quantity != null)
			fields.put(Field.QUANTITY.priority(++priority), this.quantity);
		if (this.keyCode != null)
			fields.put(Field.KEYCODE.priority(++priority), this.keyCode);
		if (this.serialNumber != null)
			fields.put(Field.SERIAL_NUMBER.priority(++priority), this.serialNumber);
		if (this.regId != null)
			fields.put(Field.REG_ID.priority(++priority), this.regId);
		if (this.pspId != null)
			fields.put(Field.PSP_ID.priority(++priority), this.pspId);
		if (this.contractId != null)
			fields.put(Field.CONTRACT_ID.priority(++priority), this.contractId);
		if (this.contractEndDate != null)
			fields.put(Field.CONTRACT_END_DATE.priority(++priority), DateUtil.getStoreDateString(this.contractEndDate));
		if (this.valuePackageId != null)
			fields.put(Field.VALUE_PACKAGE_ID.priority(++priority), this.valuePackageId);
		if (this.deliveryEmail != null)
			fields.put(Field.DELIVERY_EMAIL.priority(++priority), this.deliveryEmail);
		if (this.confirmationCode != null)
			fields.put(Field.CONFIRMATION_CODE.priority(++priority), this.confirmationCode);
		if (this.cancelReasonCode != null)
			fields.put(Field.CANCEL_REASON_CODE.priority(++priority), this.cancelReasonCode);
		if (this.adjustmentReasonCode != null)
			fields.put(Field.ADJUSTMENT_REASON_CODE.priority(++priority), this.adjustmentReasonCode);
		if (this.vendorId != null)
			fields.put(Field.VENDOR_ID.priority(++priority), this.vendorId);
		if (this.caPartyId != null)
			fields.put(Field.CA_PARTY_ID.priority(++priority), this.caPartyId);
		if (this.customerFirstName != null)
			fields.put(Field.CUSTOMER_FIRST_NAME.priority(++priority), this.customerFirstName);
		if (this.customerMiddleName != null)
			fields.put(Field.CUSTOMER_MIDDLE_NAME.priority(++priority), this.customerMiddleName);
		if (this.customerLastName != null)
			fields.put(Field.CUSTOMER_LAST_NAME.priority(++priority), this.customerLastName);
		if (this.addressLabel != null)
			fields.put(Field.ADDRESS_LABEL.priority(++priority), this.addressLabel);
		if (this.addressLine1 != null)
			fields.put(Field.ADDRESS_LINE_1.priority(++priority), this.addressLine1);
		if (this.addressLine2 != null)
			fields.put(Field.ADDRESS_LINE_2.priority(++priority), this.addressLine2);
		if (this.addressCity != null)
			fields.put(Field.ADDRESS_CITY.priority(++priority), this.addressCity);
		if (this.addressState != null)
			fields.put(Field.ADDRESS_STATE.priority(++priority), this.addressState);
		if (this.addressZipcode != null)
			fields.put(Field.ADDRESS_ZIPCODE.priority(++priority), this.addressZipcode);
		if (this.phoneLabel != null)
			fields.put(Field.PHONE_LABEL.priority(++priority), this.phoneLabel);
		if (this.phoneNumber != null)
			fields.put(Field.PHONE_NUMBER.priority(++priority), this.phoneNumber);
		if (this.rewardZoneNumber != null)
			fields.put(Field.REWARD_ZONE_NUMBER.priority(++priority), this.rewardZoneNumber);
		if (this.customerContactEmail != null)
			fields.put(Field.CUSTOMER_CONTACT_EMAIL.priority(++priority), this.customerContactEmail);
		if (this.creditCardCustomerName != null)
			fields.put(Field.CREDIT_CARD_CUSTOMER_NAME.priority(++priority), this.creditCardCustomerName);
		if (this.creditCardToken != null)
			fields.put(Field.CREDIT_CARD_TOKEN.priority(++priority), this.creditCardToken);
		if (this.creditCardType != null)
			fields.put(Field.CREDIT_CARD_TYPE.priority(++priority), this.creditCardType);
		if (this.creditCardExpireDate != null)
			fields.put(Field.CREDIT_CARD_EXPIRE_DATE.priority(++priority), this.creditCardExpireDate);
		if (this.masterItemId != null)
			fields.put(Field.MASTER_ITEM_ID.priority(++priority), this.masterItemId);
		if (this.masterVendorId != null)
			fields.put(Field.MASTER_VENDOR_ID.priority(++priority), this.masterVendorId);
		if (this.vendorDigitalId != null)
			fields.put(Field.VENDOR_DIGITAL_ID.priority(++priority), this.vendorDigitalId);
		if (this.digitalProductType != null)
			fields.put(Field.DIGITAL_PRODUCT_TYPE.priority(++priority), this.digitalProductType);
		if (this.productType != null)
			fields.put(Field.PRODUCT_TYPE.priority(++priority), this.productType);
		if (this.digitalPrice != null)
			fields.put(Field.DIGITAL_PRICE.priority(++priority), this.digitalPrice);
		if (this.digitalTaxAmount != null)
			fields.put(Field.DIGITAL_TAX_AMOUNT.priority(++priority), this.digitalTaxAmount);
		if (this.digitalTaxRate != null)
			fields.put(Field.DIGITAL_TAX_RATE.priority(++priority), this.digitalTaxRate);
		if (this.inProductMessageCode != null)
			fields.put(Field.IN_PRODUCT_MESSAGE_CODE.priority(++priority), this.inProductMessageCode);
		if (this.promoSku != null)
			fields.put(Field.PROMO_SKU.priority(++priority), this.promoSku);
		if (this.promoPrice != null)
			fields.put(Field.PROMO_PRICE.priority(++priority), this.promoPrice);
		if (this.promoStartDate != null)
			fields.put(Field.PROMO_START_DATE.priority(++priority), DateUtil.getStoreDateString(this.promoStartDate));
		if (this.promoEndDate != null)
			fields.put(Field.PROMO_END_DATE.priority(++priority), DateUtil.getStoreDateString(this.promoEndDate));
		if (this.promoCode != null)
			fields.put(Field.PROMO_CODE.priority(++priority), this.promoCode);
		return fields;
	}
	
	public boolean isOriginalFivePartKeyPresent() {
		return this.getOriginalFpkTransactionDate() != null || this.getOriginalFpkStoreId() != null || this.getOriginalFpkRegisterId() != null || this.getOriginalFpkTransactionId() != null
				|| this.getOriginalFpkLineId() != null;
	}

	public boolean isContractPresent() {
		return this.getContractId() != null || this.getContractEndDate() != null;
	}

	public boolean isAddressPresent() {
		if (this.getAddressLabel() != null && this.getAddressLabel().equals(IGNORE_ALL_FIELDS)) {
			return false;
		} else {
			return this.getAddressLabel() != null || this.getAddressLine1() != null || this.getAddressLine2() != null || this.getAddressCity() != null || this.getAddressState() != null
					|| this.getAddressCountry() != null || this.getAddressZipcode() != null;
		}
	}

	public boolean isPhonePresent() {
		if (this.getPhoneLabel() != null && this.getPhoneLabel().equals(IGNORE_ALL_FIELDS)) {
			return false;
		} else {
			return this.getPhoneNumber() != null || this.getPhoneLabel() != null;
		}
	}

	public boolean isCustomerNamePresent() {
		return this.getCustomerFirstName() != null || this.getCustomerLastName() != null || this.getCustomerMiddleName() != null;
	}

	public boolean isCustomerPresent() {
		return isCustomerNamePresent() || this.getCustomerContactEmail() != null || this.getCaPartyId() != null || this.getCustomerId() != null || isPhonePresent()
				|| this.getRewardZoneNumber() != null;
	}

	public boolean isCreditCardPresent() {
		if (this.getCreditCardType() != null && this.getCreditCardType().equals(IGNORE_ALL_FIELDS)) {
			return false;
		} else {
			return this.getCreditCardCustomerName() != null || this.getCreditCardExpireDate() != null || this.getCreditCardToken() != null || this.getCreditCardType() != null;
		}
	}

	public boolean isBillingInformationPresent() {
		return isCreditCardPresent() || isContractPresent() || isAddressPresent() || isPhonePresent();
	}

	public boolean isVendorOfferIdentifierPresent() {
		return this.getProductDescription() != null || this.getCatalogId() != null || this.getSdpOrderId() != null;
	}

	public boolean isDigitalAttributesPresent() {
		return this.getMasterItemId() != null || this.getMasterVendorId() != null || this.getDigitalPrice() != null || this.getDigitalProductType() != null || this.getDigitalTaxAmount() != null
				|| this.getDigitalTaxRate() != null || this.getVendorDigitalId() != null;
	}

	public boolean isRelatedSkuPresent() {
		return this.getRelatedSku() != null || this.getRelatedSkuPrice() != null || this.getRelatedSkuTaxAmount() != null || this.getRelatedSkuTaxRate() != null;
	}

	public boolean isPrimarySkuPresent() {
		return this.getPrimarySku() != null || this.getPrimarySkuPrice() != null || this.getPrimarySkuTaxAmount() != null || this.getPrimarySkuTaxRate() != null;
	}

	/**
	 * If address type or phone type or creditcard type are EXPLICITLY set to
	 * this value then all address fields or phone fields or creditcard fields
	 * must be ignored and omitted from any generated xml requests.
	 */
	private static final String IGNORE_ALL_FIELDS = "NOT_PROVIDED";

	public String getIgnoreAllFieldsString() {
		return IGNORE_ALL_FIELDS;
	}

}
