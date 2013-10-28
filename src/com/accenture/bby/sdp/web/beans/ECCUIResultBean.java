package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.SdpConfigProperties;

import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;

public class ECCUIResultBean extends WebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1086947867339205732L;
	private String sdpOrderId;
	private String primarySku;
	private String offrName;
	private String cusFirstName;
	private String cusLastName;
	private String keyCode;
	private String storeId;
	private String registerId;
	private String transId;
	private Date dateOfSale;
	private String status;
	private String deliveryEmail;
	private String lineItemId;
	// for check box
	private boolean selected;
	private Integer result;

	public ECCUIResultBean() {

	}

	public ECCUIResultBean(String sdpOrderId, String primarySku, String offrName, String cusLastName, String cusFirstName, String keyCode, String storeId, String registerId, String transId, Date dateOfSale, String status,
			String deliveryEmail, String lineItemId) {
		this.sdpOrderId = sdpOrderId;
		this.primarySku = primarySku;
		this.offrName = offrName;
		this.cusLastName = cusLastName;
		this.cusFirstName = cusFirstName;
		this.keyCode = keyCode;
		this.storeId = storeId;
		this.registerId = registerId;
		this.transId = transId;
		this.dateOfSale = dateOfSale;
		this.status = status;
		this.deliveryEmail = deliveryEmail;
		this.lineItemId = lineItemId;
	}

	public ECCUIResultBean(ECCUIResultBean eccuiBean) {
		this.sdpOrderId = eccuiBean.sdpOrderId;
		this.primarySku = eccuiBean.primarySku;
		this.offrName = eccuiBean.offrName;
		this.cusLastName = eccuiBean.cusLastName;
		this.cusFirstName = eccuiBean.cusFirstName;
		this.keyCode = eccuiBean.keyCode;
		this.storeId = eccuiBean.storeId;
		this.registerId = eccuiBean.registerId;
		this.transId = eccuiBean.transId;
		this.dateOfSale = eccuiBean.dateOfSale;
		this.status = eccuiBean.status;
		this.deliveryEmail = eccuiBean.deliveryEmail;
		this.lineItemId = eccuiBean.lineItemId;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getOffrName() {
		return offrName;
	}

	public void setOffrName(String offrName) {
		this.offrName = offrName;
	}

	public String getCustomerFirstName() {
		return cusFirstName;
	}

	public void setCustomerFirstName(String cusFirstName) {
		this.cusFirstName = cusFirstName;
	}

	public String getCustomerLastName() {
		return cusLastName;
	}

	public String getCusName() {
		return (cusLastName != null ? cusLastName : "") + ", " + (cusFirstName != null ? cusFirstName : "");
	}
	
	public void setCustomerLastName(String cusLastName) {
		this.cusLastName = cusLastName;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getFpkStoreId() {
		return storeId;
	}

	public void setFpkStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getFpkRegisterId() {
		return registerId;
	}

	public void setFpkRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getFpkTransactionId() {
		return transId;
	}

	public void setFpkTransactionId(String transId) {
		this.transId = transId;
	}

	public String getFpkTransactionDateAsString() {
		if (dateOfSale == null) {
			return null;
		} else {
			return new SimpleDateFormat(Constants.STORE_DATE_PATTERN).format(dateOfSale);
		}
	}
	
	public Date getFpkTransactionDate() {
		return dateOfSale;
	}

	public void setFpkTransactionDate(Date dateOfSale) {
		this.dateOfSale = dateOfSale;
	}

	public String getStatus() {
		if (this.status != null) {
			return Constants.getStatusName(this.status);
		} else {
			return null;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSendEmailAllowed() throws MissingPropertyException {
		return this.status.equals(SdpConfigProperties.getStatusCompleted());
	}

	public String getDeliveryEmail() {
		return deliveryEmail;
	}

	public void setDeliveryEmail(String deliveryEmail) {
		this.deliveryEmail = deliveryEmail;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public Integer getResult() {
		return result;
	}
	
	public boolean isSubmitted() {
		return result != null;
	}
	
	public boolean isSuccessful() {
		return isSubmitted() && result == 0;
	}
	
	public boolean isFailed() {
		return isSubmitted() && result == 1;
	}

	@Override
	public String toString() {
		return "<ECCUIBean" + 
		(" sdpOrderId=\"" + (sdpOrderId != null ? sdpOrderId : "[null]") + "\"") +
		(" primarySku=\"" + (primarySku != null ? primarySku : "[null]") + "\"") +
		(" offrName=\"" + (offrName != null ? offrName : "[null]") + "\"") +
		(" cusLastName=\"" + (cusLastName != null ? cusLastName : "[null]") + "\"") +
		(" cusFirstName=\"" + (cusFirstName != null ? cusFirstName : "[null]") + "\"") +
		(" keyCode=\"" + (keyCode != null ? keyCode : "[null]") + "\"") +
		(" storeId=\"" + (storeId != null ? storeId : "[null]") + "\"") +
		(" registerId=\"" + (registerId != null ? registerId : "[null]") + "\"") +
		(" transId=\"" + (transId != null ? transId : "[null]") + "\"") +
		(" dateOfSale=\"" + (dateOfSale != null ? DateUtil.getXmlDateString(dateOfSale) : "[null]") + "\"") +
		(" status=\"" + (status != null ? status : "[null]") + "\"") +
		(" deliveryEmail=\"" + (deliveryEmail != null ? deliveryEmail : "[null]") + "\"") +
		" />";
	}
	public String getSdpOrderId() {
		return sdpOrderId;
	}

	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	public String getPrimarySku() {
		return primarySku;
	}

	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}

}
