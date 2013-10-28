package com.accenture.bby.sdp.web.beans;

public class VendorRequestBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5365499147231853849L;

	private SdpTransactionDataBean data;
	private SdpTransactionDataBean originalData;

	public VendorRequestBean() {
		data = new SdpTransactionDataBean();
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
}
