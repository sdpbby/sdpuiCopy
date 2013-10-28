package com.accenture.bby.sdp.web.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrder;
import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrderDocument;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;
import com.accenture.bby.sdp.utl.xml.SdpTransactionDataXml;
import com.accenture.bby.sdp.utl.xml.XmlPrettyPrinter;

public class VendorProvisioningStatusBean extends WebBean implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String vpsId;
	private String vendorId;
	private String sdpId;
	private Date createdDate;
	private String serialNumber;
	private String status;
	private String responseCode;
	private String requestType;
	private String sdpOrderId;
	private String bbyOrder;
	private String vendorName;
	private String description;
	private String category;
	
	public VendorProvisioningStatusBean() {
		
	}
	
	public VendorProvisioningStatusBean(ResultSet rs) throws SQLException {
		this.vpsId = rs.getString(1);
		this.vendorId = rs.getString(2);
		this.serialNumber = rs.getString(3);
		this.sdpOrderId = rs.getString(4);
		this.sdpId = rs.getString(5);
		this.requestType = rs.getString(6);
		this.responseCode = rs.getString(7);
		this.status = rs.getString(8);
		this.createdDate = rs.getTimestamp(9);
	}
	
	public String getVpsId() {
		return vpsId;
	}

	public void setVpsId(String vpsId) {
		this.vpsId = vpsId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = filter(vendorId);
	}

	public String getSdpId() {
		return sdpId;
	}

	public void setSdpId(String sdpId) {
		this.sdpId = filter(sdpId);
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = filter(serialNumber);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = filter(status);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = filter(responseCode);
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = filter(requestType);
	}

	public String getSdpOrderId() {
		return sdpOrderId;
	}

	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = filter(sdpOrderId);
	}

	public String getBbyOrder() {
		return bbyOrder;
	}

	public void setBbyOrder(String bbyOrder) {
		this.bbyOrder = bbyOrder;
	}
	
	public String getBbyOrderXml() {
		return XmlPrettyPrinter.formatString(bbyOrder);
	}
	

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName =  filter(vendorName);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description =  filter(description);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = filter(category);
	}

	@Override
	public String toString() {
		return ("<VendorProvisioningStatusBean") +
				(" vpsId=\"" + vpsId != null ? vpsId : "" + "\"") +
				(" vendorId=\"" + vendorId != null ? vendorId : "" + "\"") +
				(" sdpId=\"" + sdpId != null ? sdpId : "" + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" serialNumber=\"" + serialNumber != null ? serialNumber : "" + "\"") +
				(" status=\"" + status != null ? status : "" + "\"") +
				(" responseCode=\"" + responseCode != null ? responseCode : "" + "\"") +
				(" requestType=\"" + requestType != null ? requestType : "" + "\"") +
				(" sdpOrderId=\"" + sdpOrderId != null ? sdpOrderId : "" + "\"") +
				(" vendorName=\"" + vendorName != null ? vendorName : "" + "\"") +
				(" description=\"" + description != null ? description : "" + "\"") +
				(" category=\"" + category != null ? category : "" + "\"") +
				(">") +
				(bbyOrder != null ? "\n  <Data>\n<![CDATA[\n" + getBbyOrderXml() + "\n]]>\n  </Data>" : "  <Data />") +
				("\n</VendorProvisioningStatusBean>");
	}

	@Override
	public String getSerialNumberForAudit() {
		return this.serialNumber;
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
	public String getMasterItemIdForAudit() {
		return null;
	}

	@Override
	public String getContractIdForAudit() {
		return null;
	}

	@Override
	public String getVendorIdForAudit() {
		return this.vendorId;
	}

//	@Override
//	public Map<Field, String> getAuditableFields() {
//		Map<Field, String> fields = new HashMap<Field, String>();
//		int priority = 0; // incrementing priority value
//
//		if (this.sdpId != null)
//			fields.put(Field.SDP_ID.priority(++priority), this.sdpId);
//		if (this.serialNumber != null)
//			fields.put(Field.SERIAL_NUMBER.priority(++priority), this.serialNumber);
//		if (this.vendorId != null)
//			fields.put(Field.VENDOR_ID.priority(++priority), this.vendorId);
//		return fields;
//	}

	@Override
	public Map<Field, String> getAuditableFields() {
		Map<Field, String> fields = new HashMap<Field, String>();
		int priority = 0; // incrementing priority value

		if (bbyOrder == null) {
			if (this.sdpId != null)
				fields.put(Field.SDP_ID.priority(++priority), this.sdpId);
			if (this.serialNumber != null)
				fields.put(Field.SERIAL_NUMBER.priority(++priority),
						this.serialNumber);
			if (this.vendorId != null)
				fields.put(Field.VENDOR_ID.priority(++priority), this.vendorId);
			return fields;
		} else {
			SdpTransactionDataBean bean = null;
			try {
				XmlObject xml = XmlObject.Factory.parse(bbyOrder);
				if (xml instanceof BBYOrder || xml instanceof BBYOrderDocument) {
					bean = SdpTransactionDataXml.getSdpTransactionDataBean(xml);
				}
			} catch (XmlException e) {
				bean = new SdpTransactionDataBean();
			} catch (ParseException e) {
				bean = new SdpTransactionDataBean();
			}
			// BBYOrder order = null;
			// try {
			// order = BBYOrderDocument.Factory.parse(bbyOrder).getBBYOrder();
			// } catch (XmlException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// BBYOrderBean bean = new BBYOrderBean(order);

			if(bean == null){
				bean = new SdpTransactionDataBean();
			}
			if (bean.getSourceSystemId() != null)
				fields.put(Field.SOURCE_SYSTEM_ID.priority(++priority), bean
						.getSourceSystemId());
			if (bean.getFpkTransactionDate() != null)
				fields.put(Field.FPK_TRANSACTION_DATE.priority(++priority),
						DateUtil.getStoreDateString(bean
								.getFpkTransactionDate()));
			if (bean.getFpkStoreId() != null)
				fields.put(Field.FPK_STORE_ID.priority(++priority), bean
						.getFpkStoreId());
			if (bean.getFpkRegisterId() != null)
				fields.put(Field.FPK_REGISTER_ID.priority(++priority), bean
						.getFpkRegisterId());
			if (bean.getFpkTransactionId() != null)
				fields.put(Field.FPK_TRANSACTION_ID.priority(++priority), bean
						.getFpkTransactionId());
			if (bean.getFpkLineId() != null)
				fields.put(Field.FPK_LINE_ID.priority(++priority), bean
						.getFpkLineId());
			if (bean.getOriginalFpkTransactionDate() != null)
				fields.put(Field.FPK_ORIGINAL_TRANSACTION_DATE
						.priority(++priority), DateUtil.getStoreDateString(bean
						.getOriginalFpkTransactionDate()));
			if (bean.getOriginalFpkStoreId() != null)
				fields.put(Field.FPK_ORIGINAL_STORE_ID.priority(++priority),
						bean.getOriginalFpkStoreId());
			if (bean.getOriginalFpkRegisterId() != null)
				fields.put(Field.FPK_ORIGINAL_REGISTER_ID.priority(++priority),
						bean.getOriginalFpkRegisterId());
			if (bean.getOriginalFpkTransactionId() != null)
				fields.put(Field.FPK_ORIGINAL_TRANSACTION_ID
						.priority(++priority), bean
						.getOriginalFpkTransactionId());
			if (bean.getOriginalFpkLineId() != null)
				fields.put(Field.FPK_ORIGINAL_LINE_ID.priority(++priority),
						bean.getOriginalFpkLineId());
			if (bean.getLineItemId() != null)
				fields.put(Field.LINE_ITEM_ID.priority(++priority), bean
						.getLineItemId());
			if (this.sdpId != null)
				fields.put(Field.SDP_ID.priority(++priority), this.sdpId);
			if (bean.getPrimarySku() != null)
				fields.put(Field.PRIMARY_SKU.priority(++priority), bean
						.getPrimarySku());
			if (bean.getPrimarySkuPrice() != null)
				fields.put(Field.PRIMARY_SKU_PRICE.priority(++priority), bean
						.getPrimarySkuPrice());
			if (bean.getPrimarySkuTaxAmount() != null)
				fields.put(Field.PRIMARY_SKU_TAX_AMOUNT.priority(++priority),
						bean.getPrimarySkuTaxAmount());
			if (bean.getPrimarySkuTaxRate() != null)
				fields.put(Field.PRIMARY_SKU_TAX_RATE.priority(++priority),
						bean.getPrimarySkuTaxRate());
			if (bean.getRelatedSku() != null)
				fields.put(Field.PARENT_SKU.priority(++priority), bean
						.getRelatedSku());
			if (bean.getRelatedSkuPrice() != null)
				fields.put(Field.PARENT_SKU_PRICE.priority(++priority), bean
						.getRelatedSkuPrice());
			if (bean.getRelatedSkuTaxAmount() != null)
				fields.put(Field.PARENT_SKU_TAX_AMOUNT.priority(++priority),
						bean.getRelatedSkuTaxAmount());
			if (bean.getRelatedSkuTaxRate() != null)
				fields.put(Field.PARENT_SKU_TAX_RATE.priority(++priority), bean
						.getRelatedSkuTaxRate());
			if (bean.getVendorProductSku() != null)
				fields.put(Field.VENDOR_TRIGGER_SKU.priority(++priority), bean
						.getVendorProductSku());
			if (bean.getDigitalId() != null)
				fields.put(Field.DIGITAL_ID.priority(++priority), bean
						.getDigitalId());
			if (bean.getQuantity() != null)
				fields.put(Field.QUANTITY.priority(++priority), bean
						.getQuantity());
			if (bean.getKeyCode() != null)
				fields.put(Field.KEYCODE.priority(++priority), bean
						.getKeyCode());
			if (this.serialNumber != null)
				fields.put(Field.SERIAL_NUMBER.priority(++priority),
						this.serialNumber);
			if (bean.getRegId() != null)
				fields.put(Field.REG_ID.priority(++priority), bean.getRegId());
			if (bean.getPspId() != null)
				fields.put(Field.PSP_ID.priority(++priority), bean.getPspId());
			if (bean.getContractId() != null)
				fields.put(Field.CONTRACT_ID.priority(++priority), bean
						.getContractId());
			if (bean.getContractEndDate() != null)
				fields.put(Field.CONTRACT_END_DATE.priority(++priority),
						DateUtil.getStoreDateString(bean.getContractEndDate()));
			if (bean.getValuePackageId() != null)
				fields.put(Field.VALUE_PACKAGE_ID.priority(++priority), bean
						.getValuePackageId());
			if (bean.getDeliveryEmail() != null)
				fields.put(Field.DELIVERY_EMAIL.priority(++priority), bean
						.getDeliveryEmail());
			if (bean.getConfirmationCode() != null)
				fields.put(Field.CONFIRMATION_CODE.priority(++priority), bean
						.getConfirmationCode());
			if (bean.getCancelReasonCode() != null)
				fields.put(Field.CANCEL_REASON_CODE.priority(++priority), bean
						.getCancelReasonCode());
			if (bean.getAdjustmentReasonCode() != null)
				fields.put(Field.ADJUSTMENT_REASON_CODE.priority(++priority),
						bean.getAdjustmentReasonCode());
			if (this.vendorId != null)
				fields.put(Field.VENDOR_ID.priority(++priority), this.vendorId);
			if (bean.getCaPartyId() != null)
				fields.put(Field.CA_PARTY_ID.priority(++priority), bean
						.getCaPartyId());
			if (bean.getCustomerFirstName() != null)
				fields.put(Field.CUSTOMER_FIRST_NAME.priority(++priority), bean
						.getCustomerFirstName());
			if (bean.getCustomerMiddleName() != null)
				fields.put(Field.CUSTOMER_MIDDLE_NAME.priority(++priority),
						bean.getCustomerMiddleName());
			if (bean.getCustomerLastName() != null)
				fields.put(Field.CUSTOMER_LAST_NAME.priority(++priority), bean
						.getCustomerLastName());
			if (bean.getAddressLabel() != null)
				fields.put(Field.ADDRESS_LABEL.priority(++priority), bean
						.getAddressLabel());
			if (bean.getAddressLine1() != null)
				fields.put(Field.ADDRESS_LINE_1.priority(++priority), bean
						.getAddressLine1());
			if (bean.getAddressLine2() != null)
				fields.put(Field.ADDRESS_LINE_2.priority(++priority), bean
						.getAddressLine2());
			if (bean.getAddressCity() != null)
				fields.put(Field.ADDRESS_CITY.priority(++priority), bean
						.getAddressCity());
			if (bean.getAddressState() != null)
				fields.put(Field.ADDRESS_STATE.priority(++priority), bean
						.getAddressState());
			if (bean.getAddressZipcode() != null)
				fields.put(Field.ADDRESS_ZIPCODE.priority(++priority), bean
						.getAddressZipcode());
			if (bean.getPhoneLabel() != null)
				fields.put(Field.PHONE_LABEL.priority(++priority), bean
						.getPhoneLabel());
			if (bean.getPhoneNumber() != null)
				fields.put(Field.PHONE_NUMBER.priority(++priority), bean
						.getPhoneNumber());
			if (bean.getRewardZoneNumber() != null)
				fields.put(Field.REWARD_ZONE_NUMBER.priority(++priority), bean
						.getRewardZoneNumber());
			if (bean.getCustomerContactEmail() != null)
				fields.put(Field.CUSTOMER_CONTACT_EMAIL.priority(++priority),
						bean.getCustomerContactEmail());
			if (bean.getCreditCardCustomerName() != null)
				fields.put(
						Field.CREDIT_CARD_CUSTOMER_NAME.priority(++priority),
						bean.getCreditCardCustomerName());
			if (bean.getCreditCardToken() != null)
				fields.put(Field.CREDIT_CARD_TOKEN.priority(++priority), bean
						.getCreditCardToken());
			if (bean.getCreditCardType() != null)
				fields.put(Field.CREDIT_CARD_TYPE.priority(++priority), bean
						.getCreditCardType());
			if (bean.getCreditCardExpireDate() != null)
				fields.put(Field.CREDIT_CARD_EXPIRE_DATE.priority(++priority),
						bean.getCreditCardExpireDate());
			if (bean.getMasterItemId() != null)
				fields.put(Field.MASTER_ITEM_ID.priority(++priority), bean
						.getMasterItemId());
			if (bean.getMasterVendorId() != null)
				fields.put(Field.MASTER_VENDOR_ID.priority(++priority), bean
						.getMasterVendorId());
			if (bean.getVendorDigitalId() != null)
				fields.put(Field.VENDOR_DIGITAL_ID.priority(++priority), bean
						.getVendorDigitalId());
			if (bean.getDigitalProductType() != null)
				fields.put(Field.DIGITAL_PRODUCT_TYPE.priority(++priority),
						bean.getDigitalProductType());
			if (bean.getProductType() != null)
				fields.put(Field.PRODUCT_TYPE.priority(++priority), bean
						.getProductType());
			if (bean.getDigitalPrice() != null)
				fields.put(Field.DIGITAL_PRICE.priority(++priority), bean
						.getDigitalPrice());
			if (bean.getDigitalTaxAmount() != null)
				fields.put(Field.DIGITAL_TAX_AMOUNT.priority(++priority), bean
						.getDigitalTaxAmount());
			if (bean.getDigitalTaxRate() != null)
				fields.put(Field.DIGITAL_TAX_RATE.priority(++priority), bean
						.getDigitalTaxRate());
			if (bean.getInProductMessageCode() != null)
				fields.put(Field.IN_PRODUCT_MESSAGE_CODE.priority(++priority),
						bean.getInProductMessageCode());
			if (bean.getPromoSku() != null)
				fields.put(Field.PROMO_SKU.priority(++priority), bean
						.getPromoSku());
			if (bean.getPromoPrice() != null)
				fields.put(Field.PROMO_PRICE.priority(++priority), bean
						.getPromoPrice());
			if (bean.getPromoStartDate() != null)
				fields.put(Field.PROMO_START_DATE.priority(++priority),
						DateUtil.getStoreDateString(bean.getPromoStartDate()));
			if (bean.getPromoEndDate() != null)
				fields.put(Field.PROMO_END_DATE.priority(++priority), DateUtil
						.getStoreDateString(bean.getPromoEndDate()));
			if (bean.getPromoCode() != null)
				fields.put(Field.PROMO_CODE.priority(++priority), bean
						.getPromoCode());
			return fields;
		}

	}

}
