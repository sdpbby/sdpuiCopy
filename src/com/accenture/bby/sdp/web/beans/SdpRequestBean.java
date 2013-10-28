package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;


public class SdpRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2967398930479466145L;
	private SdpTransactionDataBean data;
	private SdpTransactionDataBean originalData;
	private String creditCardMonth;
	private String creditCardYear;
	private boolean noCreditCardExpire;
	
	public SdpRequestBean() {
		data = new SdpTransactionDataBean();
	}
	
	public SdpRequestBean(SdpRequestBean sdpRequestBean) {
		this.data = sdpRequestBean.data;
		if (data == null) {
			data = new SdpTransactionDataBean();
		}
	}
	
	public SdpRequestBean(SdpTransactionDataBean data) {
		this.data = data;
		if (data == null) {
			this.data = new SdpTransactionDataBean();
		}
	}
	
	public SdpTransactionDataBean getData() {
		return data;
	}

	public void setData(SdpTransactionDataBean data) {
		if (data == null) {
			this.data = new SdpTransactionDataBean();
		} else {
			this.data = new SdpTransactionDataBean(data);
		}
	}
	
	public SdpTransactionDataBean getOriginalData() {
		return originalData;
	}
	
	public void setOriginalData(SdpTransactionDataBean data) {
		if (data == null) {
			this.originalData = null;
		} else {
			this.originalData = new SdpTransactionDataBean(data);
		}
	}

	public boolean isAddressRequired() {
		return !(data.getAddressLabel() != null && data.getAddressLabel().equals(data.getIgnoreAllFieldsString()));
	}

	public boolean isPhoneRequired() {
		return !(data.getPhoneLabel() != null && data.getPhoneLabel().equals(data.getIgnoreAllFieldsString()));
	}

	public boolean isCreditCardRequired() {
		return !(data.getCreditCardType() != null && data.getCreditCardType().equals(data.getIgnoreAllFieldsString()));
	}

	public String getCreditCardMonth() {
		return creditCardMonth;
	}

	public void setCreditCardMonth(String creditCardMonth) {
		this.creditCardMonth = creditCardMonth;
	}

	public String getCreditCardYear() {
		return creditCardYear;
	}

	public void setCreditCardYear(String creditCardYear) {
		this.creditCardYear = creditCardYear;
	}

	public boolean isNoCreditCardExpire() {
		return noCreditCardExpire;
	}

	public void setNoCreditCardExpire(boolean noCreditCardExpire) {
		this.noCreditCardExpire = noCreditCardExpire;
	}

	@Override
	public String toString() {
		return ("<SdpRequestBean") +
				(" addressRequired=\"" + isAddressRequired() + "\"") +
				(" phoneRequired=\"" + isPhoneRequired() + "\"") +
				(" creditCardRequired=\"" + isCreditCardRequired() + "\"") +
				(">") +
				(data != null ? "\n  <Data>\n<![CDATA[\n" + data.toString() + "\n]]>\n  </Data>" : "  <Data />") +
				("\n</SdpRequestBean>");
	}
}
