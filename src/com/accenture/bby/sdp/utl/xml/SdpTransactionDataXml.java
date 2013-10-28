package com.accenture.bby.sdp.utl.xml;

import identifier.utilities.bby.sdp.com.accenture.xml.ArrayofExternalID;
import identifier.utilities.bby.sdp.com.accenture.xml.ExternalID;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import product.om.bby.sdp.com.accenture.xml.Product;

import vendoroffer.om.bby.sdp.com.accenture.xml.VendorOffer;

import attribute.om.bby.sdp.com.accenture.xml.ArrayOfSDPAttribute;
import attribute.om.bby.sdp.com.accenture.xml.Attribute;
import bbyoffer.om.bby.sdp.com.accenture.xml.BBYOffer;
import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrder;
import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrderDocument;
import billinginformation.om.bby.sdp.com.accenture.xml.BillingInformation;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;

import com.bestbuy.schemas.sdp.adjustactivation.AdjustActivationRequestDocument;
import com.bestbuy.schemas.sdp.adjustactivation.AdjustmentOrderType;
import com.bestbuy.schemas.sdp.cancelfulfillment.CancelFulfillmentRequestDocument;
import com.bestbuy.schemas.sdp.cancelfulfillment.CancellationOrderType;
import com.bestbuy.schemas.sdp.fulfillactivation.FulfillActivationRequestDocument;
import com.bestbuy.schemas.sdp.reactivatefulfillment.ReActivateFulfillmentRequestDocument;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceRequestDocument;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceRequestDocument.SendEmailServiceRequest;
import com.bestbuy.www.sdp.tpa.ActionType;
import com.bestbuy.www.sdp.tpa.RequestMessageType;
import com.bestbuy.www.sdp.tpa.wsdl_tpa_services.TpaProvisioningRequestDocument;
import com.bestbuy.www.sdp.tpa.TpaProvisioningRequestType;

import customer.cm.bby.sdp.com.accenture.xml.Customer;

public class SdpTransactionDataXml implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6875928801303132790L;
	
	private static final Logger logger = Logger.getLogger(SdpTransactionDataXml.class.getName());

	// credit card type icdm mappings.
	private static final String INTERNAL_VISA = "VS";
	private static final String INTERNAL_DISCOVER = "DI";
	private static final String INTERNAL_MASTERCARD = "MC";
	private static final String INTERNAL_AMEX = "AM";
	private static final String INTERNAL_BBYCONSUMER = "HA";
	private static final String INTERNAL_BBYCOMMERCIAL = "H1";
	private static final String INTERNAL_CHASE = "CA";
	private static final String INTERNAL_MULTISERVICECOMMERCIAL = "MS";

	// credit card type external system mappings.
	private static final String EXTERNAL_VISA = "V";
	private static final String EXTERNAL_DISCOVER = "D";
	private static final String EXTERNAL_MASTERCARD = "M";
	private static final String EXTERNAL_AMEX = "A";
	private static final String EXTERNAL_BBYCONSUMER = "B";
	private static final String EXTERNAL_BBYCOMMERCIAL = "C";
	private static final String EXTERNAL_CHASE = "H";
	private static final String EXTERNAL_MULTISERVICECOMMERCIAL = "T"; 
	
	/**
	 * Map icdm credit card type code to external system credit card type codes.
	 * If no match is found then return input value.
	 * 
	 * @param icdmCreditCardType
	 * @return
	 */
	private static String getExternalCreditCardType(String icdmCreditCardType) {
		if (INTERNAL_VISA.equals(icdmCreditCardType)) {
			return EXTERNAL_VISA;
		} else if (INTERNAL_DISCOVER.equals(icdmCreditCardType)) {
			return EXTERNAL_DISCOVER;
		} else if (INTERNAL_MASTERCARD.equals(icdmCreditCardType)) {
			return EXTERNAL_MASTERCARD;
		} else if (INTERNAL_AMEX.equals(icdmCreditCardType)) {
			return EXTERNAL_AMEX;
		} else if (INTERNAL_BBYCONSUMER.equals(icdmCreditCardType)) {
			return EXTERNAL_BBYCONSUMER;
		} else if (INTERNAL_BBYCOMMERCIAL.equals(icdmCreditCardType)) { 
			return EXTERNAL_BBYCOMMERCIAL;
		} else if (INTERNAL_CHASE.equals(icdmCreditCardType)) { 
			return EXTERNAL_CHASE;
		} else if (INTERNAL_MULTISERVICECOMMERCIAL.equals(icdmCreditCardType)) { 
			return EXTERNAL_MULTISERVICECOMMERCIAL;
		} else { // Other credit card type
			return icdmCreditCardType;
		}
	}
	
	private static final String TRANSACTION_DATE_TIME = "TransactionDateTime";
	private static final String TRANSACTION_ID = "TransactionId";
	private static final String REGISTER_ID = "RegisterId";
	private static final String TRANSACTION_DATE = "Date";
	private static final String STORE_ID = "StoreId";
	private static final String LINE_ID = "LineID";
	
	private static final String TRIGGER_SKU = "TriggerSku";
	private static final String VENDOR_TRIGGER_SKU = "VendorTriggerSku";
	private static final String KEYCODE = "KeyCode";
	private static final String CONFIRMATION_CODE = "ConfirmationCode";
	private static final String MASTER_ITEM_ID = "MasterItemId";
	private static final String MASTER_VENDOR_ID = "MasterVendorId";
	private static final String DIGITAL_PRODUCT_TYPE = "DigitalProductType";
	private static final String SERIAL_NUMBER = "SerialNumber";
	private static final String PSP_ID = "PspId";
	private static final String REG_ID = "REGID";
	private static final String RENEWAL_FLAG = "RenewalFlag";
	private static final String PROMO_SKU = "PromoSku";
	private static final String PROMO_PRICE = "PromoPrice";
	private static final String PROMO_START_DATE = "PromoStartDate";
	private static final String PROMO_END_DATE = "PromoEndDate";
	private static final String PROMO_CODE = "PromoType";
	
		
	private static final String QUANTITY = "Quantity";
	private static final String TRIGGER_EXT_PRICE = "TriggerExtPrice";
	private static final String TRIGGER_TAX_AMOUNT = "TriggerTaxAmount";
	private static final String TRIGGER_TAX_RATE = "TriggerTaxRate";
	private static final String PLAN_SKU = "PlanSku";
	private static final String PLAN_EXT_PRICE = "PlanExtPrice";
	private static final String PLAN_TAX_AMOUNT = "PlanTaxAmount";
	private static final String PLAN_TAX_RATE = "PlanTaxRate";
	private static final String DIGITAL_EXT_PRICE = "DigitalExtPrice";
	private static final String DIGITAL_TAX_AMOUNT = "DigitalTaxAmount";
	private static final String DIGITAL_TAX_RATE = "DigitalTaxRate";
	private static final String DELIVERY_EMAIL = "DeliveryEmail";
	private static final String LINE_ITEM_ID = "LineItemId";
	private static final String SDP_ID = "SDPID";
	private static final String VENDOR_ID = "VendorID";
	private static final String CANCEL_REASON_CODE = "CancelReasonCode";
	private static final String ADJUSTMENT_REASON_CODE = "AdjustmentReasonCode";
	private static final String VENDOR_DIGITAL_ID = "VendorDigitalID";
	private static final String VALUE_PACKAGE_ID = "ValuePackageID";
	
	private static final String CUST_NAME_DEFAILT = "[not-applicable]";
	private static final String PHONE_NUMBER_DEFAILT = "0000000000";
	private static final String CVV_CODE_DEFAULT = "0000";
	private static final String COUNTRY_DEFAULT = "US";
	private static final String ACTING_PARTY_STORE_ID_DEFAULT = "[NotApplicable]";
	private static final String ACTING_PARTY_SYSTEM_ID_DEFAILT = "Value to be decided";
	
	private static final String SDP_ORDER_ID_DEFAULT = "0";
	
	private static boolean isPhoneNumberValid(String phoneNumber) {
		if (phoneNumber == null) {
			return false;
		} else if (phoneNumber.length() != 10) {
			logger.log(Level.WARN, "Phone number is not in valid format and cannot be parsed. Expected <10> characters but got <" + phoneNumber.length() + ">. >> " + phoneNumber);
			return false;
		}
		
		if (!phoneNumber.matches("\\d{10}")) {
			logger.log(Level.WARN, "Phone number does not contain all numeric characters, however, since the interface technically allows this then treating this as a valid phone number. >> " + phoneNumber);
		}
		
		return true;
	}
	
	public static final int DEFAULT_NO_ORDER = 0;

	
	/**
	 * default constructor
	 */
	private SdpTransactionDataXml() {
	}
	
	/**
	 * This constructor populates SdpTransactionDataXml based on bbyOrder input
	 * 
	 * @param xmlObject
	 * @throws ParseException if unable to parse the bbyOrder xml
	 * @throws IllegalArgumentException if unable to cast xmlObject to BBYOrder
	 * @throws NullPointerException if input XmlObject is null.
	 */
	public static SdpTransactionDataBean getSdpTransactionDataBean(XmlObject xmlObject) throws ParseException {
		if (xmlObject == null) {
			throw new NullPointerException("Input XmlObject was null.");
		}
		if (xmlObject instanceof BBYOrder) {
			return getSdpTransactionDataBean((BBYOrder)xmlObject);
		} else if (xmlObject instanceof BBYOrderDocument) {
			BBYOrderDocument doc = (BBYOrderDocument) xmlObject;
			return getSdpTransactionDataBean(doc.getBBYOrder());
		} else {
			throw new IllegalArgumentException("Unable to parse xmlObject as BBYOrder");
		}
	}
	
	/**
	 * @param bbyOrder
	 * @return
	 * @throws ParseException
	 * @throws NullPointerException if input BBYOrder is null.
	 */
	public static SdpTransactionDataBean getSdpTransactionDataBean(BBYOrder bbyOrder) throws ParseException {
		if (bbyOrder == null) {
			throw new NullPointerException("Input BBYOrder was null.");
		}
		SdpTransactionDataBean bean = new SdpTransactionDataBean();
		
		if (bbyOrder.getCustomer() != null) {
			Customer customer = bbyOrder.getCustomer();

			if (customer.getCustomerName() != null) {
				bean.setCustomerFirstName(customer.getCustomerName().getFirstName());
				bean.setCustomerMiddleName(customer.getCustomerName().getMiddleName());
				bean.setCustomerLastName(customer.getCustomerName().getLastName());
			}

			bean.setCustomerContactEmail(customer.getEmailAddress());
			bean.setCustomerId(customer.getC4CustomerID());
			bean.setCaPartyId(customer.getECCustomerID());
			bean.setRewardZoneNumber(customer.getRewardZoneNumber());

			if (customer.getTelephoneNumber() != null) {
				bean.setPhoneNumber(customer.getTelephoneNumber().getAreaCode() + "" + customer.getTelephoneNumber().getExchCode() + "" + customer.getTelephoneNumber().getLocalNumCode());
				bean.setPhoneLabel(customer.getTelephoneNumber().getLabel());
			}

		}

		if (bbyOrder.getBBYOfferArray() != null && bbyOrder.getBBYOfferArray().length > 0 && bbyOrder.getBBYOfferArray(0) != null) {
			BBYOffer bbyOffer = bbyOrder.getBBYOfferArray(0);

			if (bbyOffer.getBBYOfferIdentifier() != null && bbyOffer.getBBYOfferIdentifier().getExternalIDCollection() != null
					&& bbyOffer.getBBYOfferIdentifier().getExternalIDCollection().getExternalIDArray() != null
					&& bbyOffer.getBBYOfferIdentifier().getExternalIDCollection().getExternalIDArray().length > 0
					&& bbyOffer.getBBYOfferIdentifier().getExternalIDCollection().getExternalIDArray(0) != null) {
				ExternalID[] externalIdList = bbyOffer.getBBYOfferIdentifier().getExternalIDCollection().getExternalIDArray();
				for (ExternalID externalId : externalIdList) {
					if (TRANSACTION_DATE.equals(externalId.getType())) {
						bean.setFpkTransactionDate(DateUtil.getShortXmlDate(externalId.getID()));
					} else if (STORE_ID.equals(externalId.getType())) {
						bean.setFpkStoreId(externalId.getID());
					} else if (REGISTER_ID.equals(externalId.getType())) {
						bean.setFpkRegisterId(externalId.getID());
					} else if (TRANSACTION_ID.equals(externalId.getType())) {
						bean.setFpkTransactionId(externalId.getID());
					} else if (LINE_ID.equals(externalId.getType())) {
						bean.setFpkLineId(externalId.getID());
					} else if (TRANSACTION_DATE_TIME.equals(externalId.getType())) {
						bean.setTransactionDateTime(externalId.getID());
					}
				}
			}

			if (bbyOffer.getVendorOfferArray() != null && bbyOffer.getVendorOfferArray().length > 0 && bbyOffer.getVendorOfferArray(0) != null) {
				VendorOffer vendorOffer = bbyOffer.getVendorOfferArray(0);

				if (vendorOffer.getVendorOfferIdentifier() != null) {
					bean.setCatalogId(vendorOffer.getVendorOfferIdentifier().getConnect4CatalogueID());
					bean.setSdpOrderId(vendorOffer.getVendorOfferIdentifier().getConnect4SubscriptionOfferID());
				}

				bean.setCategory(vendorOffer.getCategory());
				bean.setSubCategory(vendorOffer.getOfferSubCategory());
				bean.setSdpOrderStatus(vendorOffer.getStatus());

				if (vendorOffer.getBaseProductCollection() != null && vendorOffer.getBaseProductCollection().getSubCategoryArray() != null
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray().length > 0 && vendorOffer.getBaseProductCollection().getSubCategoryArray(0) != null
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray() != null
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray().length > 0
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray(0) != null
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray() != null
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray().length > 0
						&& vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray(0) != null) {

					Product product = vendorOffer.getBaseProductCollection().getSubCategoryArray(0).getTypeArray(0).getProductArray(0);

					if (product.getProductIdentifier() != null && product.getProductIdentifier().getExternalIDCollection() != null
							&& product.getProductIdentifier().getExternalIDCollection().getExternalIDArray() != null) {

						ExternalID[] externalIds = product.getProductIdentifier().getExternalIDCollection().getExternalIDArray();

						for (ExternalID externalId : externalIds) {

							if (TRIGGER_SKU.equals(externalId.getType())) {
								bean.setPrimarySku(externalId.getID());
							} else if (SERIAL_NUMBER.equals(externalId.getType())) {
								bean.setSerialNumber(externalId.getID());
							} else if (PSP_ID.equals(externalId.getType())) {
								bean.setPspId(externalId.getID());
							} else if (REG_ID.equals(externalId.getType())) {
								bean.setRegId(externalId.getID());
							} else if (RENEWAL_FLAG.equals(externalId.getType())) {
								bean.setInProductMessageCode(externalId.getID());
							} else if (VENDOR_TRIGGER_SKU.equals(externalId.getType())) {
								bean.setVendorProductSku(externalId.getID());
							} else if (CONFIRMATION_CODE.equals(externalId.getType())) {
								bean.setConfirmationCode(externalId.getID());
							} else if (KEYCODE.equals(externalId.getType())) {
								bean.setKeyCode(externalId.getID());
							} else if (MASTER_ITEM_ID.equals(externalId.getType())) {
								bean.setMasterItemId(externalId.getID());
							} else if (MASTER_VENDOR_ID.equals(externalId.getType())) {
								bean.setMasterVendorId(externalId.getID());
							} else if (VENDOR_DIGITAL_ID.equals(externalId.getType())) {
								bean.setVendorDigitalId(externalId.getID());
							} else if (DIGITAL_PRODUCT_TYPE.equals(externalId.getType())) {
								bean.setDigitalProductType(externalId.getID());
							}
						}
					}

					if (product.getProductDetails() != null && product.getProductDetails().getMoreDetails() != null && product.getProductDetails().getMoreDetails().getAttributeArray() != null) {

						Attribute[] attributes = product.getProductDetails().getMoreDetails().getAttributeArray();

						for (Attribute attribute : attributes) {

							if (PLAN_SKU.equals(attribute.getName())) {
								bean.setRelatedSku(attribute.getValue());
							} else if (TRIGGER_EXT_PRICE.equals(attribute.getName())) {
								bean.setPrimarySkuPrice(attribute.getValue());
							} else if (PLAN_EXT_PRICE.equals(attribute.getName())) {
								bean.setRelatedSkuPrice(attribute.getValue());
							} else if (DELIVERY_EMAIL.equals(attribute.getName())) {
								bean.setDeliveryEmail(attribute.getValue());
							} else if (VALUE_PACKAGE_ID.equals(attribute.getName())) {
								bean.setValuePackageId(attribute.getValue());
							} else if (LINE_ITEM_ID.equals(attribute.getName())) {
								bean.setLineItemId(attribute.getValue());
							} else if (QUANTITY.equals(attribute.getName())) {
								bean.setQuantity(attribute.getValue());
							} else if (SDP_ID.equals(attribute.getName())) {
								bean.setSdpId(attribute.getValue());
							} else if (VENDOR_ID.equals(attribute.getName())) {
								bean.setVendorId(attribute.getValue());
							} else if (CANCEL_REASON_CODE.equals(attribute.getName())) {
								bean.setCancelReasonCode(attribute.getValue());
							} else if (ADJUSTMENT_REASON_CODE.equals(attribute.getName())) {
								bean.setAdjustmentReasonCode(attribute.getValue());
							} else if (TRIGGER_TAX_AMOUNT.equals(attribute.getName())) {
								bean.setPrimarySkuTaxAmount(attribute.getValue());
							} else if (TRIGGER_TAX_RATE.equals(attribute.getName())) {
								bean.setPrimarySkuTaxRate(attribute.getValue());
							} else if (PLAN_TAX_AMOUNT.equals(attribute.getName())) {
								bean.setRelatedSkuTaxAmount(attribute.getValue());
							} else if (PLAN_TAX_RATE.equals(attribute.getName())) {
								bean.setRelatedSkuTaxRate(attribute.getValue());
							} else if (DIGITAL_EXT_PRICE.equals(attribute.getName())) {
								bean.setDigitalPrice(attribute.getValue());
							} else if (DIGITAL_TAX_AMOUNT.equals(attribute.getName())) {
								bean.setDigitalTaxAmount(attribute.getValue());
							} else if (DIGITAL_TAX_RATE.equals(attribute.getName())) {
								bean.setDigitalTaxRate(attribute.getValue());
							} else if (PROMO_SKU.equals(attribute.getName())) {
								bean.setPromoSku(attribute.getValue());
							} else if (PROMO_PRICE.equals(attribute.getName())) {
								bean.setPromoPrice(attribute.getValue());
							} else if (PROMO_START_DATE.equals(attribute.getName())) {
								bean.setPromoStartDate(DateUtil.getShortXmlDate(attribute.getValue()));
							} else if (PROMO_END_DATE.equals(attribute.getName())) {
								bean.setPromoEndDate(DateUtil.getShortXmlDate(attribute.getValue()));
							} else if (PROMO_CODE.equals(attribute.getName())) {
								bean.setPromoCode(attribute.getValue());
							}
						}
					}

				}

				if (vendorOffer.isSetBillingInformation() && vendorOffer.getBillingInformation() != null) {
					BillingInformation billing = vendorOffer.getBillingInformation();

					if (billing.isSetCreditCard()) {
						bean.setCreditCardCustomerName(billing.getCreditCard().getCreditCardOwner().getFullName());
						bean.setCreditCardExpireDate(DateUtil.getFulfillmentServicesBillingDateFromIcdm(billing.getCreditCard().getExpDate()));
						bean.setCreditCardToken(billing.getCreditCard().getCardNumber() + "");
						bean.setCreditCardType(getExternalCreditCardType(billing.getCreditCard().getType()));
					}

					if (billing.isSetAddress()) {
						bean.setAddressLine1(billing.getAddress().getUnParsedAddress().getAddressLine1());
						bean.setAddressLine2(billing.getAddress().getUnParsedAddress().getAddressLine2());
						bean.setAddressLabel(billing.getAddress().getUnParsedAddress().getLabel());
						bean.setAddressCity(billing.getAddress().getUnParsedAddress().getDemographics().getCity());
						bean.setAddressState(billing.getAddress().getUnParsedAddress().getDemographics().getState());
						bean.setAddressZipcode(billing.getAddress().getUnParsedAddress().getDemographics().getZipCode());
					}

					if (bean.getPhoneNumber() == null && billing.isSetTelephoneNumber()) {
						bean.setPhoneNumber(billing.getTelephoneNumber().getAreaCode() + "" + billing.getTelephoneNumber().getExchCode() + "" + billing.getTelephoneNumber().getLocalNumCode());
						bean.setPhoneLabel(billing.getTelephoneNumber().getLabel());
					}

					if (billing.isSetContract()) {
						bean.setContractId(billing.getContract().getContractId());
						if (billing.getContract().isSetEndDate()) {
							bean.setContractEndDate(billing.getContract().getEndDate().getTime());
						}
					}

				}

			}

		}

		if (bbyOrder.isSetTransactionInformation()) {
			if (bbyOrder.getTransactionInformation().isSetActingParty()) {
				bean.setSourceSystemId(bbyOrder.getTransactionInformation().getActingParty().getPartyID());
			}
		}
		return bean;
	}

	

	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static FulfillActivationRequestDocument getFulfillActivationRequest(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		final FulfillActivationRequestDocument doc = FulfillActivationRequestDocument.Factory.newInstance();
		final com.bestbuy.schemas.sdp.fulfillactivation.ActivationOrderType order = doc.addNewFulfillActivationRequest().addNewActivationOrder();
		
		order.setTransactionDateTime(Calendar.getInstance());
		order.setSourceID(data.getSourceSystemId());
		order.setStoreID(data.getFpkStoreId());
		order.setRegisterID(data.getFpkRegisterId());
		order.setTransactionID(data.getFpkTransactionId());
		if (data.getFpkTransactionDate() != null) {
			order.setDate(convertDateToCalendarSansTimezone(data.getFpkTransactionDate()));
		}
		if (isNotNull(data.getCaPartyId())) {
			order.setCAPartyID(data.getCaPartyId());
		}
		if (isNotNull(data.getRewardZoneNumber())) {
			order.setRewardZoneID(data.getRewardZoneNumber());
		}
		com.bestbuy.schemas.sdp.fulfillactivation.LineItemDetailsType lineItemDetails = order.addNewLineItemDetails();

		lineItemDetails.setLineID(data.getFpkLineId());

		if (isNotNull(data.getKeyCode())) {
			lineItemDetails.addNewIsRTACall();
			lineItemDetails.getIsRTACall().setSuccessful(true);
			lineItemDetails.getIsRTACall().setKeyCode(data.getKeyCode());
		}

		lineItemDetails.addNewSKU();
		lineItemDetails.getSKU().setSKUNum(data.getPrimarySku());
		if (isNotNull(data.getPrimarySkuPrice())) {
			lineItemDetails.getSKU().setPrice(data.getPrimarySkuPrice());
		}
		if (isNotNull(data.getPrimarySkuTaxAmount())) {
			lineItemDetails.getSKU().setTaxAmount(data.getPrimarySkuTaxAmount());
		}
		if (isNotNull(data.getPrimarySkuTaxRate())) {
			lineItemDetails.getSKU().setTaxRate(data.getPrimarySkuTaxRate());
		}

		if (data.isDigitalAttributesPresent()) {
			com.bestbuy.schemas.sdp.fulfillactivation.DigitalAttributesType digitalAttributes = lineItemDetails.addNewDigitalAttributes();
			if (isNotNull(data.getMasterItemId())) {
				digitalAttributes.setMasterItemId(data.getMasterItemId());
			}
			if (isNotNull(data.getMasterVendorId())) {
				digitalAttributes.setVendorID(data.getMasterVendorId());
			}
			if (isNotNull(data.getVendorDigitalId())) {
				digitalAttributes.setVendorDigitalID(data.getVendorDigitalId());
			}
			if (isNotNull(data.getDigitalProductType())) {
				digitalAttributes.setDigitalProductType(data.getDigitalProductType());
			}
			if (isNotNull(data.getDigitalPrice())) {
				digitalAttributes.setPrice(data.getDigitalPrice());
			}
			if (isNotNull(data.getDigitalTaxAmount())) {
				digitalAttributes.setTaxAmount(data.getDigitalTaxAmount());
			}
			if (isNotNull(data.getDigitalTaxRate())) {
				digitalAttributes.setTaxRate(data.getDigitalTaxRate());
			}
		}

		if (data.isRelatedSkuPresent()) {
			lineItemDetails.addNewRelatedSKU();
			if (isNotNull(data.getRelatedSku())) {
				lineItemDetails.getRelatedSKU().setSKUNum(data.getRelatedSku());
			}
			if (isNotNull(data.getRelatedSkuPrice())) {
				lineItemDetails.getRelatedSKU().setPrice(data.getRelatedSkuPrice());
			}
			if (isNotNull(data.getRelatedSkuTaxAmount())) {
				lineItemDetails.getRelatedSKU().setTaxAmount(data.getRelatedSkuTaxAmount());
			}
			if (isNotNull(data.getRelatedSkuTaxRate())) {
				lineItemDetails.getRelatedSKU().setTaxRate(data.getRelatedSkuTaxRate());
			}
		}

		if (isNotNull(data.getValuePackageId())) {
			lineItemDetails.setValuePackageID(data.getValuePackageId());
		}
		if (isNotNull(data.getDeliveryEmail())) {
			lineItemDetails.setDeliveryEmail(data.getDeliveryEmail());
		}
		if (isNotNull(data.getSerialNumber())) {
			lineItemDetails.setSerialNumber(data.getSerialNumber());
		}

		com.bestbuy.schemas.sdp.fulfillactivation.AttributeType attr;
		
		if (isNotNull(data.getRegId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("REGID");
			attr.setValue(data.getRegId());
		}
		if (isNotNull(data.getPspId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("PSPID");
			attr.setValue(data.getPspId());
		}

		if (data.isCustomerNamePresent() || data.isAddressPresent() || isNotNull(data.getCustomerContactEmail()) || data.isPhonePresent()
				|| data.isCreditCardPresent()) {

			lineItemDetails.addNewBillingData();

			if (data.isCreditCardPresent()) {
				lineItemDetails.getBillingData().addNewCreditCard();
				lineItemDetails.getBillingData().getCreditCard().setName(data.getCreditCardCustomerName());
				lineItemDetails.getBillingData().getCreditCard().setType(data.getCreditCardType());
				lineItemDetails.getBillingData().getCreditCard().setToken(data.getCreditCardToken());
				if (data.getCreditCardExpireDate() != null) {
					lineItemDetails.getBillingData().getCreditCard().setExpirationDate(DateUtil.getFulfillmentServicesBillingDateFromIcdm(data.getCreditCardExpireDate()));
				}
			}

			if (data.isAddressPresent()) {

				lineItemDetails.getBillingData().addNewAddress();
				if (isNotNull(data.getAddressLabel())) {
					lineItemDetails.getBillingData().getAddress().setLabel(data.getAddressLabel());
				}
				lineItemDetails.getBillingData().getAddress().addLine(data.getAddressLine1());
				if (isNotNull(data.getAddressLine2())) {
					lineItemDetails.getBillingData().getAddress().addLine(data.getAddressLine2());
				}
				lineItemDetails.getBillingData().getAddress().setCity(data.getAddressCity());
				lineItemDetails.getBillingData().getAddress().setState(data.getAddressState());
				lineItemDetails.getBillingData().getAddress().setPostalCode(data.getAddressZipcode());
			}

			if (data.isCustomerNamePresent() || isNotNull(data.getCustomerContactEmail()) || data.isPhonePresent()) {
				lineItemDetails.addNewCustomerContactDetails();
				if (isNotNull(data.getCustomerFirstName())) {
					lineItemDetails.getCustomerContactDetails().setFirstName(data.getCustomerFirstName());
				}
				if (isNotNull(data.getCustomerMiddleName())) {
					lineItemDetails.getCustomerContactDetails().setMiddleName(data.getCustomerMiddleName());
				}
				if (isNotNull(data.getCustomerLastName())) {
					lineItemDetails.getCustomerContactDetails().setLastName(data.getCustomerLastName());
				}
				if (isNotNull(data.getCustomerContactEmail())) {
					lineItemDetails.getCustomerContactDetails().setContactEmail(data.getCustomerContactEmail());
				}
			}

			if (data.isPhonePresent()) {
				lineItemDetails.getCustomerContactDetails().addNewPhone();
				lineItemDetails.getCustomerContactDetails().getPhone().setNumber(data.getPhoneNumber());
				if (isNotNull(data.getPhoneLabel())) {
					if ("HOME".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.fulfillactivation.PhoneType.Label.HOME);
					} else if ("MOBILE".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.fulfillactivation.PhoneType.Label.MOBILE);
					} else if ("WORK".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.fulfillactivation.PhoneType.Label.WORK);
					} else {
						// do not set label
					}
				}
			}

		}
		return doc;
	}

	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static ReActivateFulfillmentRequestDocument getReactivateFulfillmentRequest(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		final ReActivateFulfillmentRequestDocument doc = ReActivateFulfillmentRequestDocument.Factory.newInstance();
		final com.bestbuy.schemas.sdp.reactivatefulfillment.ActivationOrderType order = doc.addNewReActivateFulfillmentRequest().addNewActivationOrder();
		
		order.setTransactionDateTime(Calendar.getInstance());
		order.setSourceID(data.getSourceSystemId());
		order.setStoreID(data.getFpkStoreId());
		order.setRegisterID(data.getFpkRegisterId());
		order.setTransactionID(data.getFpkTransactionId());
		if (data.getFpkTransactionDate() != null) {
			order.setDate(convertDateToCalendarSansTimezone(data.getFpkTransactionDate()));
		}
		if (isNotNull(data.getCaPartyId())) {
			order.setCAPartyID(data.getCaPartyId());
		}
		if (isNotNull(data.getRewardZoneNumber())) {
			order.setRewardZoneID(data.getRewardZoneNumber());
		}
		com.bestbuy.schemas.sdp.reactivatefulfillment.LineItemDetailsType lineItemDetails = order.addNewLineItemDetails();
		
		lineItemDetails.addNewOriginalTransactionDetails();
		lineItemDetails.getOriginalTransactionDetails().setStoreID(data.getOriginalFpkStoreId());
		lineItemDetails.getOriginalTransactionDetails().setRegisterID(data.getOriginalFpkRegisterId());
		lineItemDetails.getOriginalTransactionDetails().setTransactionID(data.getOriginalFpkTransactionId());
		lineItemDetails.getOriginalTransactionDetails().setLineID(data.getOriginalFpkLineId());
		if (data.getFpkTransactionDate() != null) {
			lineItemDetails.getOriginalTransactionDetails().setDate(convertDateToCalendarSansTimezone(data.getOriginalFpkTransactionDate()));
		}

		lineItemDetails.setLineID(data.getFpkLineId());

		if (isNotNull(data.getKeyCode())) {
			lineItemDetails.addNewIsRTACall();
			lineItemDetails.getIsRTACall().setSuccessful(true);
			lineItemDetails.getIsRTACall().setKeyCode(data.getKeyCode());
		}

		lineItemDetails.addNewSKU();
		lineItemDetails.getSKU().setSKUNum(data.getPrimarySku());
		if (isNotNull(data.getPrimarySkuPrice())) {
			lineItemDetails.getSKU().setPrice(data.getPrimarySkuPrice());
		}
		if (isNotNull(data.getPrimarySkuTaxAmount())) {
			lineItemDetails.getSKU().setTaxAmount(data.getPrimarySkuTaxAmount());
		}
		if (isNotNull(data.getPrimarySkuTaxRate())) {
			lineItemDetails.getSKU().setTaxRate(data.getPrimarySkuTaxRate());
		}

		if (data.isDigitalAttributesPresent()) {
			com.bestbuy.schemas.sdp.reactivatefulfillment.DigitalAttributesType digitalAttributes = lineItemDetails.addNewDigitalAttributes();
			if (isNotNull(data.getMasterItemId())) {
				digitalAttributes.setMasterItemId(data.getMasterItemId());
			}
			if (isNotNull(data.getMasterVendorId())) {
				digitalAttributes.setVendorID(data.getMasterVendorId());
			}
			if (isNotNull(data.getVendorDigitalId())) {
				digitalAttributes.setVendorDigitalID(data.getVendorDigitalId());
			}
			if (isNotNull(data.getDigitalProductType())) {
				digitalAttributes.setDigitalProductType(data.getDigitalProductType());
			}
			if (isNotNull(data.getDigitalPrice())) {
				digitalAttributes.setPrice(data.getDigitalPrice());
			}
			if (isNotNull(data.getDigitalTaxAmount())) {
				digitalAttributes.setTaxAmount(data.getDigitalTaxAmount());
			}
			if (isNotNull(data.getDigitalTaxRate())) {
				digitalAttributes.setTaxRate(data.getDigitalTaxRate());
			}
		}

		if (data.isRelatedSkuPresent()) {
			lineItemDetails.addNewRelatedSKU();
			if (isNotNull(data.getRelatedSku())) {
				lineItemDetails.getRelatedSKU().setSKUNum(data.getRelatedSku());
			}
			if (isNotNull(data.getRelatedSkuPrice())) {
				lineItemDetails.getRelatedSKU().setPrice(data.getRelatedSkuPrice());
			}
			if (isNotNull(data.getRelatedSkuTaxAmount())) {
				lineItemDetails.getRelatedSKU().setTaxAmount(data.getRelatedSkuTaxAmount());
			}
			if (isNotNull(data.getRelatedSkuTaxRate())) {
				lineItemDetails.getRelatedSKU().setTaxRate(data.getRelatedSkuTaxRate());
			}
		}
		
		if (isNotNull(data.getValuePackageId())) {
			lineItemDetails.setValuePackageID(data.getValuePackageId());
		}
		if (isNotNull(data.getDeliveryEmail())) {
			lineItemDetails.setDeliveryEmail(data.getDeliveryEmail());
		}
		if (isNotNull(data.getSerialNumber())) {
			lineItemDetails.setSerialNumber(data.getSerialNumber());
		}

		com.bestbuy.schemas.sdp.reactivatefulfillment.AttributeType attr;
		
		if (isNotNull(data.getRegId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("REGID");
			attr.setValue(data.getRegId());
		}
		if (isNotNull(data.getPspId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("PSPID");
			attr.setValue(data.getPspId());
		}

		if (data.isCustomerNamePresent() || data.isAddressPresent() || isNotNull(data.getCustomerContactEmail()) || data.isPhonePresent()
				|| data.isCreditCardPresent()) {

			lineItemDetails.addNewBillingData();

			if (data.isCreditCardPresent()) {
				lineItemDetails.getBillingData().addNewCreditCard();
				lineItemDetails.getBillingData().getCreditCard().setName(data.getCreditCardCustomerName());
				lineItemDetails.getBillingData().getCreditCard().setType(data.getCreditCardType());
				lineItemDetails.getBillingData().getCreditCard().setToken(data.getCreditCardToken());
				if (data.getCreditCardExpireDate() != null) {
					lineItemDetails.getBillingData().getCreditCard().setExpirationDate(DateUtil.getFulfillmentServicesBillingDateFromIcdm(data.getCreditCardExpireDate()));
				}
			}

			if (data.isAddressPresent()) {

				lineItemDetails.getBillingData().addNewAddress();
				if (isNotNull(data.getAddressLabel())) {
					lineItemDetails.getBillingData().getAddress().setLabel(data.getAddressLabel());
				}
				lineItemDetails.getBillingData().getAddress().addLine(data.getAddressLine1());
				if (isNotNull(data.getAddressLine2())) {
					lineItemDetails.getBillingData().getAddress().addLine(data.getAddressLine2());
				}
				lineItemDetails.getBillingData().getAddress().setCity(data.getAddressCity());
				lineItemDetails.getBillingData().getAddress().setState(data.getAddressState());
				lineItemDetails.getBillingData().getAddress().setPostalCode(data.getAddressZipcode());
			}

			if (data.isCustomerNamePresent() || isNotNull(data.getCustomerContactEmail()) || data.isPhonePresent()) {
				lineItemDetails.addNewCustomerContactDetails();
				if (isNotNull(data.getCustomerFirstName())) {
					lineItemDetails.getCustomerContactDetails().setFirstName(data.getCustomerFirstName());
				}
				if (isNotNull(data.getCustomerMiddleName())) {
					lineItemDetails.getCustomerContactDetails().setMiddleName(data.getCustomerMiddleName());
				}
				if (isNotNull(data.getCustomerLastName())) {
					lineItemDetails.getCustomerContactDetails().setLastName(data.getCustomerLastName());
				}
				if (isNotNull(data.getCustomerContactEmail())) {
					lineItemDetails.getCustomerContactDetails().setContactEmail(data.getCustomerContactEmail());
				}
			}

			if (data.isPhonePresent()) {
				lineItemDetails.getCustomerContactDetails().addNewPhone();
				lineItemDetails.getCustomerContactDetails().getPhone().setNumber(data.getPhoneNumber());
				if (isNotNull(data.getPhoneLabel())) {
					if ("HOME".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.reactivatefulfillment.PhoneType.Label.HOME);
					} else if ("MOBILE".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.reactivatefulfillment.PhoneType.Label.MOBILE);
					} else if ("WORK".equals(data.getPhoneLabel().toUpperCase())) {
						lineItemDetails.getCustomerContactDetails().getPhone().setLabel(com.bestbuy.schemas.sdp.reactivatefulfillment.PhoneType.Label.WORK);
					} else {
						// do not set label
					}
				}
			}

		}
		return doc;
	}

	/**
	 * Helper method. Converts <code>java.util.Date</code> to
	 * <code>java.util.Calendar</code>. Timezone is not set in the returned
	 * Calendar.
	 * 
	 * @param arg
	 *            java.util.Date to convert to java.util.Calendar
	 * @return java.util.Calendar with no timezone set
	 */
	private static Calendar convertDateToCalendarSansTimezone(Date arg) {
		// create a temp calendar object and set as the fpk trans date
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(arg);
		// create a new calendar object and clear it so it will not hold
		// timezone information
		Calendar val = Calendar.getInstance();
		val.clear();
		// copy values over to new calendar and set the value
		val.set(Calendar.YEAR, tmp.get(Calendar.YEAR));
		val.set(Calendar.MONTH, tmp.get(Calendar.MONTH));
		val.set(Calendar.DAY_OF_MONTH, tmp.get(Calendar.DAY_OF_MONTH));
		return val;
	}

	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static CancelFulfillmentRequestDocument getCancelFulfillmentRequest(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		final CancelFulfillmentRequestDocument doc = CancelFulfillmentRequestDocument.Factory.newInstance();
		final CancellationOrderType order = doc.addNewCancelFulfillmentRequest().addNewCancellationOrder();
		
		order.setTransactionDateTime(Calendar.getInstance());
		order.setSourceID(data.getSourceSystemId());
		order.setStoreID(data.getFpkStoreId());
		order.setRegisterID(data.getFpkRegisterId());
		order.setTransactionID(data.getFpkTransactionId());
		if (data.getFpkTransactionDate() != null) {
			order.setDate(convertDateToCalendarSansTimezone(data.getFpkTransactionDate()));
		}
		if (isNotNull(data.getCaPartyId())) {
			order.setCAPartyID(data.getCaPartyId());
		}
		if (isNotNull(data.getRewardZoneNumber())) {
			order.setRewardZoneID(data.getRewardZoneNumber());
		}
		if (isNotNull(data.getCancelReasonCode())) {
			order.setCancelReasonCode(data.getCancelReasonCode());
		}
		
		com.bestbuy.schemas.sdp.cancelfulfillment.LineItemDetailsType lineItemDetails = order.addNewLineItemDetails();

		lineItemDetails.setLineID(data.getFpkLineId());

		lineItemDetails.addNewOriginalTransactionDetails();
		lineItemDetails.getOriginalTransactionDetails().setStoreID(data.getOriginalFpkStoreId());
		lineItemDetails.getOriginalTransactionDetails().setRegisterID(data.getOriginalFpkRegisterId());
		lineItemDetails.getOriginalTransactionDetails().setTransactionID(data.getOriginalFpkTransactionId());
		lineItemDetails.getOriginalTransactionDetails().setLineID(data.getOriginalFpkLineId());
		if (data.getOriginalFpkTransactionDate() != null) {
			lineItemDetails.getOriginalTransactionDetails().setDate(convertDateToCalendarSansTimezone(data.getOriginalFpkTransactionDate()));
		}
		if (isNotNull(data.getSerialNumber())) {
			lineItemDetails.setSerialNumber(data.getSerialNumber());
		}
		com.bestbuy.schemas.sdp.cancelfulfillment.AttributeType attr;
		
		if (isNotNull(data.getRegId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("REGID");
			attr.setValue(data.getRegId());
		}
		if (isNotNull(data.getPspId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("PSPID");
			attr.setValue(data.getPspId());
		}
		
		com.bestbuy.schemas.sdp.cancelfulfillment.SKUType sku = lineItemDetails.addNewSKU();
		sku.setSKUNum(data.getPrimarySku());
		if (isNotNull(data.getPrimarySkuPrice())) {
			sku.setPrice(data.getPrimarySkuPrice());
		}
		if (isNotNull(data.getPrimarySkuTaxAmount())) {
			sku.setTaxAmount(data.getPrimarySkuTaxAmount());
		}
		if (isNotNull(data.getPrimarySkuTaxRate())) {
			sku.setTaxRate(data.getPrimarySkuTaxRate());
		}
		
		if (data.isRelatedSkuPresent()) {
    		com.bestbuy.schemas.sdp.cancelfulfillment.RelatedSKUType relatedSku = lineItemDetails.addNewRelatedSKU();
    		relatedSku.setSKUNum(data.getRelatedSku());
    		if (isNotNull(data.getRelatedSkuPrice())) {
    			relatedSku.setPrice(data.getRelatedSkuPrice());
    		}
    		if (isNotNull(data.getRelatedSkuTaxAmount())) {
    			relatedSku.setTaxAmount(data.getRelatedSkuTaxAmount());
    		}
    		if (isNotNull(data.getRelatedSkuTaxRate())) {
    			relatedSku.setTaxRate(data.getRelatedSkuTaxRate());
    		}
		}

		return doc;
	}
	
	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static AdjustActivationRequestDocument getAdjustActivationRequest(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		final AdjustActivationRequestDocument doc = AdjustActivationRequestDocument.Factory.newInstance();
		final AdjustmentOrderType order = doc.addNewAdjustActivationRequest().addNewAdjustmentOrder();
		
		order.setTransactionDateTime(Calendar.getInstance());
		order.setSourceID(data.getSourceSystemId());
		order.setStoreID(data.getFpkStoreId());
		order.setRegisterID(data.getFpkRegisterId());
		order.setTransactionID(data.getFpkTransactionId());
		if (data.getFpkTransactionDate() != null) {
			order.setDate(convertDateToCalendarSansTimezone(data.getFpkTransactionDate()));
		}
		if (isNotNull(data.getCaPartyId())) {
			order.setCAPartyID(data.getCaPartyId());
		}
		if (isNotNull(data.getRewardZoneNumber())) {
			order.setRewardZoneID(data.getRewardZoneNumber());
		}
		if (isNotNull(data.getAdjustmentReasonCode())) {
			order.setAdjustmentReasonCode(data.getAdjustmentReasonCode());
		}
		
		com.bestbuy.schemas.sdp.adjustactivation.LineItemDetailsType lineItemDetails = order.addNewLineItemDetails();

		lineItemDetails.setLineID(data.getFpkLineId());

		lineItemDetails.addNewOriginalTransactionDetails();
		lineItemDetails.getOriginalTransactionDetails().setStoreID(data.getOriginalFpkStoreId());
		lineItemDetails.getOriginalTransactionDetails().setRegisterID(data.getOriginalFpkRegisterId());
		lineItemDetails.getOriginalTransactionDetails().setTransactionID(data.getOriginalFpkTransactionId());
		lineItemDetails.getOriginalTransactionDetails().setLineID(data.getOriginalFpkLineId());
		if (data.getOriginalFpkTransactionDate() != null) {
			lineItemDetails.getOriginalTransactionDetails().setDate(convertDateToCalendarSansTimezone(data.getOriginalFpkTransactionDate()));
		}

		if (isNotNull(data.getSerialNumber())) {
			lineItemDetails.setSerialNumber(data.getSerialNumber());
		}
		
		com.bestbuy.schemas.sdp.adjustactivation.AttributeType attr;
		
		if (isNotNull(data.getRegId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("REGID");
			attr.setValue(data.getRegId());
		}
		if (isNotNull(data.getPspId())) {
			attr = lineItemDetails.addNewAttribute();
			attr.setName("PSPID");
			attr.setValue(data.getPspId());
		}
		
		com.bestbuy.schemas.sdp.adjustactivation.SKUType sku = lineItemDetails.addNewSKU();
		sku.setSKUNum(data.getPrimarySku());
		if (isNotNull(data.getPrimarySkuPrice())) {
			sku.setPrice(data.getPrimarySkuPrice());
		}
		if (isNotNull(data.getPrimarySkuTaxAmount())) {
			sku.setTaxAmount(data.getPrimarySkuTaxAmount());
		}
		if (isNotNull(data.getPrimarySkuTaxRate())) {
			sku.setTaxRate(data.getPrimarySkuTaxRate());
		}

		return doc;
	}

	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static SendEmailServiceRequestDocument getCommsatRequest(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		SendEmailServiceRequestDocument doc = SendEmailServiceRequestDocument.Factory.newInstance();
		SendEmailServiceRequest request = doc.addNewSendEmailServiceRequest();
		request.setBBYOrder(getBBYOrder(data));
		
		return doc;
	}
	
	
	
	/**
	 * @param data
	 * @return
	 * @throws NullPointerException if input SdpTransactionDataBean is null.
	 */
	public static BBYOrder getBBYOrder(SdpTransactionDataBean data) {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		final BBYOrder bbyOrder = BBYOrder.Factory.newInstance();
		if (data.isCustomerPresent()) {
			final Customer customer = bbyOrder.addNewCustomer();
			customer.addNewCustomerName();
			customer.getCustomerName().setFirstName(data.getCustomerFirstName() != null ? data.getCustomerFirstName() : CUST_NAME_DEFAILT);
			if (data.getCustomerMiddleName() != null) {
				customer.getCustomerName().setMiddleName(data.getCustomerMiddleName());
			}
			customer.getCustomerName().setLastName(data.getCustomerLastName() != null ? data.getCustomerLastName() : CUST_NAME_DEFAILT);
			customer.addNewContactAddress();
			if (data.isPhonePresent()) {
				if (isPhoneNumberValid(data.getPhoneNumber())) {
					customer.addNewTelephoneNumber();
					customer.getTelephoneNumber().setAreaCode(data.getPhoneNumber().substring(0, 3));
					customer.getTelephoneNumber().setExchCode(data.getPhoneNumber().substring(3, 6));
					customer.getTelephoneNumber().setLocalNumCode(data.getPhoneNumber().substring(6, 10));
					if (data.getPhoneLabel() != null) {
						customer.getTelephoneNumber().setLabel(data.getPhoneLabel());
					}
				} else {
					logger.log(Level.ERROR, "Unable to parse phone number >> " + data.getPhoneNumber());
					throw new IllegalArgumentException("Unable to parse phone number. Expected exactly 10 characters >> " + data.getPhoneNumber());
				}
			} else {
				customer.addNewTelephoneNumber();
				customer.getTelephoneNumber().setAreaCode(PHONE_NUMBER_DEFAILT.substring(0, 3));
				customer.getTelephoneNumber().setExchCode(PHONE_NUMBER_DEFAILT.substring(3, 6));
				customer.getTelephoneNumber().setLocalNumCode(PHONE_NUMBER_DEFAILT.substring(6, 10));
				if (data.getPhoneLabel() != null) {
					customer.getTelephoneNumber().setLabel(data.getPhoneLabel());
				}
			}
			if (data.getRewardZoneNumber() != null) {
				customer.setRewardZoneNumber(data.getRewardZoneNumber());
			}
			if (data.getCustomerId() != null) {
				customer.setC4CustomerID(data.getCustomerId());
			}
			if (data.getCaPartyId() != null) {
				customer.setECCustomerID(data.getCaPartyId());
			}
			if (data.getCustomerContactEmail() != null) {
				customer.setEmailAddress(data.getCustomerContactEmail());
			}
		}
		final ArrayofExternalID bbyOfferIdentifiers = bbyOrder.addNewBBYOffer().addNewBBYOfferIdentifier().addNewExternalIDCollection();
		
		if (data.getTransactionDateTime() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(TRANSACTION_DATE_TIME);
			externalId.setID(data.getTransactionDateTime());
		}
		
		if (data.getFpkTransactionId() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(TRANSACTION_ID);
			externalId.setID(data.getFpkTransactionId());
		}
		if (data.getFpkRegisterId() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(REGISTER_ID);
			externalId.setID(data.getFpkRegisterId());
		}
		if (data.getFpkTransactionDate() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(TRANSACTION_DATE);
			externalId.setID(DateUtil.getShortXmlDateString(data.getFpkTransactionDate()));
		}
		if (data.getFpkStoreId() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(STORE_ID);
			externalId.setID(data.getFpkStoreId());
		}
		if (data.getFpkLineId() != null) {
			final ExternalID externalId = bbyOfferIdentifiers.addNewExternalID();
			externalId.setType(LINE_ID);
			externalId.setID(data.getFpkLineId());
		}
		final VendorOffer vendorOffer = bbyOrder.getBBYOfferArray(0).addNewVendorOffer();
		
		vendorOffer.addNewVendorOfferIdentifier();
		if (data.isVendorOfferIdentifierPresent()) {
			if (data.getProductDescription() != null) {
				vendorOffer.getVendorOfferIdentifier().setName(data.getProductDescription());
			}
			if (data.getCatalogId() != null) {
				vendorOffer.getVendorOfferIdentifier().setConnect4CatalogueID(data.getCatalogId());
			}
			if (data.getSdpOrderId() != null) {
				vendorOffer.getVendorOfferIdentifier().setConnect4SubscriptionOfferID(data.getSdpOrderId());
			}
		}
		
		if (data.getCategory() != null) {
			vendorOffer.setCategory(data.getCategory());
		}
		if (data.getSubCategory() != null) {
			vendorOffer.setOfferSubCategory(data.getSubCategory());
		}
		if (data.getSdpOrderStatus() != null) {
			vendorOffer.setStatus(data.getSdpOrderStatus());
		}
		
		final Product product = vendorOffer.addNewBaseProductCollection().addNewSubCategory().addNewType().addNewProduct();
		
		final ArrayofExternalID productIdentifiers = product.addNewProductIdentifier().addNewExternalIDCollection();
		
		if (data.getPrimarySku() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(TRIGGER_SKU);
			externalId.setID(data.getPrimarySku());
		}
		if (data.getVendorProductSku() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(VENDOR_TRIGGER_SKU);
			externalId.setID(data.getVendorProductSku());
		}
		if (data.getKeyCode() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(KEYCODE);
			externalId.setID(data.getKeyCode());
		}
		if (data.getConfirmationCode() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(CONFIRMATION_CODE);
			externalId.setID(data.getConfirmationCode());
		}
		if (data.getMasterItemId() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(MASTER_ITEM_ID);
			externalId.setID(data.getMasterItemId());
		}
		if (data.getMasterVendorId() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(MASTER_VENDOR_ID);
			externalId.setID(data.getMasterVendorId());
		}
		if (data.getDigitalProductType() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(DIGITAL_PRODUCT_TYPE);
			externalId.setID(data.getDigitalProductType());
		}
		if (data.getSerialNumber() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(SERIAL_NUMBER);
			externalId.setID(data.getSerialNumber());
		}
		if (data.getInProductMessageCode() != null) {
			final ExternalID externalId = productIdentifiers.addNewExternalID();
			externalId.setType(RENEWAL_FLAG);
			externalId.setID(data.getInProductMessageCode());
		}
		
		final ArrayOfSDPAttribute attributes = product.addNewProductDetails().addNewMoreDetails();
		
		if (data.getQuantity() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(QUANTITY);
			attribute.setValue(data.getQuantity());
		}
		if (data.getPrimarySkuPrice() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(TRIGGER_EXT_PRICE);
			attribute.setValue(data.getPrimarySkuPrice());
		}
		if (data.getPrimarySkuTaxAmount() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(TRIGGER_TAX_AMOUNT);
			attribute.setValue(data.getPrimarySkuTaxAmount());
		}
		if (data.getPrimarySkuTaxRate() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(TRIGGER_TAX_RATE);
			attribute.setValue(data.getPrimarySkuTaxRate());
		}
		if (data.getRelatedSku() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PLAN_SKU);
			attribute.setValue(data.getRelatedSku());
		}
		if (data.getRelatedSkuPrice() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PLAN_EXT_PRICE);
			attribute.setValue(data.getRelatedSkuPrice());
		}
		if (data.getRelatedSkuTaxAmount() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PLAN_TAX_AMOUNT);
			attribute.setValue(data.getRelatedSkuTaxAmount());
		}
		if (data.getRelatedSkuTaxRate() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PLAN_TAX_RATE);
			attribute.setValue(data.getRelatedSkuTaxRate());
		}
		if (data.getDigitalPrice() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(DIGITAL_EXT_PRICE);
			attribute.setValue(data.getDigitalPrice());
		}
		if (data.getDigitalTaxAmount() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(DIGITAL_TAX_AMOUNT);
			attribute.setValue(data.getDigitalTaxAmount());
		}
		if (data.getDigitalTaxRate() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(DIGITAL_TAX_RATE);
			attribute.setValue(data.getDigitalTaxRate());
		}
		if (data.getDeliveryEmail() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(DELIVERY_EMAIL);
			attribute.setValue(data.getDeliveryEmail());
		}
		if (data.getLineItemId() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(LINE_ITEM_ID);
			attribute.setValue(data.getLineItemId());
		}
		if (data.getSdpId() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(SDP_ID);
			attribute.setValue(data.getSdpId());
		}
		if (data.getVendorId() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(VENDOR_ID);
			attribute.setValue(data.getVendorId());
		}
		if (data.getCancelReasonCode() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(CANCEL_REASON_CODE);
			attribute.setValue(data.getCancelReasonCode());
		}
		if (data.getAdjustmentReasonCode() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(ADJUSTMENT_REASON_CODE);
			attribute.setValue(data.getAdjustmentReasonCode());
		}
		if (data.getPromoSku() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PROMO_SKU);
			attribute.setValue(data.getPromoSku());
		}
		if (data.getPromoPrice() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PROMO_PRICE);
			attribute.setValue(data.getPromoPrice());
		}
		if (data.getPromoStartDate() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PROMO_START_DATE);
			attribute.setValue(DateUtil.getShortXmlDateString(data.getPromoStartDate()));
		}
		if (data.getPromoEndDate() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PROMO_END_DATE);
			attribute.setValue(DateUtil.getShortXmlDateString(data.getPromoEndDate()));
		}
		if (data.getPromoCode() != null) {
			final Attribute attribute = attributes.addNewAttribute();
			attribute.setName(PROMO_CODE);
			attribute.setValue(data.getPromoCode());
		}
		
		if (data.isBillingInformationPresent()) {
			vendorOffer.addNewBillingInformation();
			
			if (data.isContractPresent()) {
				vendorOffer.getBillingInformation().addNewContract();
				if (data.getContractId() != null) {
					vendorOffer.getBillingInformation().getContract().setContractId(data.getContractId());
				}
				if (data.getContractEndDate() != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(data.getContractEndDate());
					vendorOffer.getBillingInformation().getContract().setEndDate(c);
				}
			}
			if (data.isCreditCardPresent()) {
				vendorOffer.getBillingInformation().addNewCreditCard().addNewCreditCardOwner();
				vendorOffer.getBillingInformation().getCreditCard().getCreditCardOwner().setFirstName(CUST_NAME_DEFAILT);
				vendorOffer.getBillingInformation().getCreditCard().getCreditCardOwner().setLastName(CUST_NAME_DEFAILT);
				if (data.getCreditCardCustomerName() != null) {
					vendorOffer.getBillingInformation().getCreditCard().getCreditCardOwner().setFullName(data.getCreditCardCustomerName());
				}
				if (data.getCreditCardToken() != null) {
					try {
						vendorOffer.getBillingInformation().getCreditCard().setCardNumber(Long.valueOf(data.getCreditCardToken()));
					} catch (NumberFormatException e) {
						logger.log(Level.ERROR, "Expected java.lang.Long type for credit card number but got >> " + data.getCreditCardToken(), e);
						throw new IllegalArgumentException("Expected java.lang.Long as credit card number but got >> " + data.getCreditCardToken(), e);
					}
				}
				if (data.getCreditCardExpireDate() != null) {
					vendorOffer.getBillingInformation().getCreditCard().setExpDate(DateUtil.getIcdmBillingDateFromFulfillmentServices(data.getCreditCardExpireDate()));
				}
				if (data.getCreditCardType() != null) {
					vendorOffer.getBillingInformation().getCreditCard().setType(data.getCreditCardType());
				}
				vendorOffer.getBillingInformation().getCreditCard().setCVVCode(CVV_CODE_DEFAULT);
			}
			if (data.isAddressPresent()) {
				vendorOffer.getBillingInformation().addNewAddress();
				vendorOffer.getBillingInformation().getAddress().addNewUnParsedAddress();
				if (data.getAddressLine1() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().setAddressLine1(data.getAddressLine1());
				}
				if (data.getAddressLine2() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().setAddressLine2(data.getAddressLine2());
				}
				vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().addNewDemographics();
				if (data.getAddressZipcode() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().getDemographics().setZipCode(data.getAddressZipcode());
				}
				if (data.getAddressCity() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().getDemographics().setCity(data.getAddressCity());
				}
				if (data.getAddressState() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().getDemographics().setState(data.getAddressState());
				}
				vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().getDemographics().setCountry(data.getAddressCountry() != null ? data.getAddressCountry() : COUNTRY_DEFAULT);
				if (data.getAddressLabel() != null) {
					vendorOffer.getBillingInformation().getAddress().getUnParsedAddress().setLabel(data.getAddressLabel());
				}
			}
			if (data.isPhonePresent()) {
				if (isPhoneNumberValid(data.getPhoneNumber())) {
					vendorOffer.getBillingInformation().addNewTelephoneNumber();
					vendorOffer.getBillingInformation().getTelephoneNumber().setAreaCode(data.getPhoneNumber().substring(0, 3));
					vendorOffer.getBillingInformation().getTelephoneNumber().setExchCode(data.getPhoneNumber().substring(3, 6));
					vendorOffer.getBillingInformation().getTelephoneNumber().setLocalNumCode(data.getPhoneNumber().substring(6, 10));
					if (data.getPhoneLabel() != null) {
						vendorOffer.getBillingInformation().getTelephoneNumber().setLabel(data.getPhoneLabel());
					}
				} else {
					logger.log(Level.ERROR, "Unable to parse phone number >> " + data.getPhoneNumber());
					throw new IllegalArgumentException("Unable to parse phone number. Expected exactly 10 characters >> " + data.getPhoneNumber());
				}
			}
		}
		bbyOrder.addNewTransactionInformation().addNewActingParty();
		bbyOrder.getTransactionInformation().getActingParty().setStoreID(ACTING_PARTY_STORE_ID_DEFAULT);
		if (data.getSourceSystemId() != null) {
			bbyOrder.getTransactionInformation().getActingParty().setPartyID(data.getSourceSystemId());
		}
		bbyOrder.getTransactionInformation().getActingParty().setSystemID(ACTING_PARTY_SYSTEM_ID_DEFAILT);
		
		return bbyOrder;
	}
	
	public static TpaProvisioningRequestDocument getVendorActivateRequest(SdpTransactionDataBean data) throws XmlException {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		if (data.getSdpOrderId() == null) {
			data.setSdpOrderId(SDP_ORDER_ID_DEFAULT);
		}
		TpaProvisioningRequestDocument doc = TpaProvisioningRequestDocument.Factory.newInstance();
		TpaProvisioningRequestType request = doc.addNewTpaProvisioningRequest();
		request.setSdpID(data.getSdpId());
		if (isNotNull(data.getSdpOrderId())) {
			request.setCsmOfferID(data.getSdpOrderId());
		}
		if (isNotNull(data.getSerialNumber())) {
			request.setVendorKey(data.getSerialNumber());
		}
		if (isNotNull(data.getVendorId())) {
			request.setVendor(data.getVendorId());
		}
		request.setAction(ActionType.ACTIVATE);
		RequestMessageType message = request.addNewRequestMessage();
		
		// if BBYOrderDocument is not on the classpath then a BBYOrder root element
		// must be added to the XSD_BBYOrder.xsd schema. then the schema must be
		// recompiled with xmlbeans
		BBYOrderDocument bbyOrder = BBYOrderDocument.Factory.newInstance();
		bbyOrder.setBBYOrder(getBBYOrder(data));
		
		XmlObject any = XmlObject.Factory.newInstance();
		XmlCursor childCursor = any.newCursor();
		childCursor.toNextToken();

		childCursor.beginElement("ANY");

		childCursor.toStartDoc();
		childCursor.toNextToken();

		XmlCursor rootCursor = message.newCursor();
		rootCursor.toEndToken();
		childCursor.moveXml(rootCursor);

		childCursor.dispose();
		rootCursor.dispose();

		String str = doc.toString().replace("<ANY xmlns=\"\"/>", bbyOrder.toString());

		return TpaProvisioningRequestDocument.Factory.parse(str);
	}
	
	public static TpaProvisioningRequestDocument getVendorCancelRequest(SdpTransactionDataBean data) throws XmlException {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		if (data.getSdpOrderId() == null) {
			data.setSdpOrderId(SDP_ORDER_ID_DEFAULT);
		}
		TpaProvisioningRequestDocument doc = TpaProvisioningRequestDocument.Factory.newInstance();
		TpaProvisioningRequestType request = doc.addNewTpaProvisioningRequest();
		request.setSdpID(data.getSdpId());
		if (isNotNull(data.getSdpOrderId())) {
			request.setCsmOfferID(data.getSdpOrderId());
		}
		if (isNotNull(data.getSerialNumber())) {
			request.setVendorKey(data.getSerialNumber());
		}
		if (isNotNull(data.getVendorId())) {
			request.setVendor(data.getVendorId());
		}
		request.setAction(ActionType.CANCEL);
		RequestMessageType message = request.addNewRequestMessage();
		
		
		// if BBYOrderDocument is not on the classpath then a BBYOrder root element
		// must be added to the XSD_BBYOrder.xsd schema. then the schema must be
		// recompiled with xmlbeans
		BBYOrderDocument bbyOrder = BBYOrderDocument.Factory.newInstance();
		bbyOrder.setBBYOrder(getBBYOrder(data));
		
		XmlObject any = XmlObject.Factory.newInstance();
		XmlCursor childCursor = any.newCursor();
		childCursor.toNextToken();

		childCursor.beginElement("ANY");

		childCursor.toStartDoc();
		childCursor.toNextToken();

		XmlCursor rootCursor = message.newCursor();
		rootCursor.toEndToken();
		childCursor.moveXml(rootCursor);

		childCursor.dispose();
		rootCursor.dispose();

		String str = doc.toString().replace("<ANY xmlns=\"\"/>", bbyOrder.toString());

		return TpaProvisioningRequestDocument.Factory.parse(str);
	}
	
	
	public static TpaProvisioningRequestDocument getVendorRenewRequest(SdpTransactionDataBean data) throws XmlException {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		if (data.getSdpOrderId() == null) {
			data.setSdpOrderId(SDP_ORDER_ID_DEFAULT);
		}
		TpaProvisioningRequestDocument doc = TpaProvisioningRequestDocument.Factory.newInstance();
		TpaProvisioningRequestType request = doc.addNewTpaProvisioningRequest();
		request.setSdpID(data.getSdpId());
		if (isNotNull(data.getSdpOrderId())) {
			request.setCsmOfferID(data.getSdpOrderId());
		}
		if (isNotNull(data.getSerialNumber())) {
			request.setVendorKey(data.getSerialNumber());
		}
		if (isNotNull(data.getVendorId())) {
			request.setVendor(data.getVendorId());
		}
		request.setAction(ActionType.RENEW);
		RequestMessageType message = request.addNewRequestMessage();
		
		// if BBYOrderDocument is not on the classpath then a BBYOrder root element
		// must be added to the XSD_BBYOrder.xsd schema. then the schema must be
		// recompiled with xmlbeans
		BBYOrderDocument bbyOrder = BBYOrderDocument.Factory.newInstance();
		bbyOrder.setBBYOrder(getBBYOrder(data));
		
		XmlObject any = XmlObject.Factory.newInstance();
		XmlCursor childCursor = any.newCursor();
		childCursor.toNextToken();

		childCursor.beginElement("ANY");

		childCursor.toStartDoc();
		childCursor.toNextToken();

		XmlCursor rootCursor = message.newCursor();
		rootCursor.toEndToken();
		childCursor.moveXml(rootCursor);

		childCursor.dispose();
		rootCursor.dispose();

		String str = doc.toString().replace("<ANY xmlns=\"\"/>", bbyOrder.toString());

		return TpaProvisioningRequestDocument.Factory.parse(str);
	}
	
	
	public static TpaProvisioningRequestDocument getVendorModifyStatusRequest(SdpTransactionDataBean data) throws XmlException {
		if (data == null) {
			throw new NullPointerException("Input SdpTransactionDataBean was null.");
		}
		if (data.getSdpOrderId() == null) {
			data.setSdpOrderId(SDP_ORDER_ID_DEFAULT);
		}
		TpaProvisioningRequestDocument doc = TpaProvisioningRequestDocument.Factory.newInstance();
		TpaProvisioningRequestType request = doc.addNewTpaProvisioningRequest();
		request.setSdpID(data.getSdpId());
		if (isNotNull(data.getSdpOrderId())) {
			request.setCsmOfferID(data.getSdpOrderId());
		}
		if (isNotNull(data.getSerialNumber())) {
			request.setVendorKey(data.getSerialNumber());
		}
		if (isNotNull(data.getVendorId())) {
			request.setVendor(data.getVendorId());
		}
		request.setAction(ActionType.UPDATE_STATUS);
		RequestMessageType message = request.addNewRequestMessage();
		
		// if BBYOrderDocument is not on the classpath then a BBYOrder root element
		// must be added to the XSD_BBYOrder.xsd schema. then the schema must be
		// recompiled with xmlbeans
		BBYOrderDocument bbyOrder = BBYOrderDocument.Factory.newInstance();
		bbyOrder.setBBYOrder(getBBYOrder(data));
		
		XmlObject any = XmlObject.Factory.newInstance();
		XmlCursor childCursor = any.newCursor();
		childCursor.toNextToken();

		childCursor.beginElement("ANY");

		childCursor.toStartDoc();
		childCursor.toNextToken();

		XmlCursor rootCursor = message.newCursor();
		rootCursor.toEndToken();
		childCursor.moveXml(rootCursor);

		childCursor.dispose();
		rootCursor.dispose();

		String str = doc.toString().replace("<ANY xmlns=\"\"/>", bbyOrder.toString());

		return TpaProvisioningRequestDocument.Factory.parse(str);
	}
	
//	public static void main(String[] args) throws XmlException, ParseException {
//		SdpTransactionDataBean bean = new SdpTransactionDataBean();
//		bean.setCreditCardExpireDate("2013-01");
//		BBYOrder bbyOrder = getBBYOrder(bean);
//		System.out.println(bbyOrder);
//		bean = SdpTransactionDataXml.getSdpTransactionDataBean(bbyOrder);
//		System.out.println(bean.getCreditCardExpireDate());
//		System.out.println(SdpTransactionDataXml.getFulfillActivationRequest(bean));
//	}
	
	private static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}