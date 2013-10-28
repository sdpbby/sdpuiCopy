package com.accenture.bby.sdp.web.beans;

public class VendorCatalogBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6774422641365261411L;

	private String vendorId;
	private String vendorName;
	private String primarySku;
	private String primarySkuDescription;
	private String vendorCategory;
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
	public String getVendorCategory() {
		return vendorCategory;
	}
	public void setVendorCategory(String vendorCategory) {
		this.vendorCategory = vendorCategory;
	}
	
	
	
	
}
