package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;

import com.accenture.bby.sdp.utl.Constants;

public class KcbReportBean extends WebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2051365311211130334L;
	private String vendorId;
	private String merchandiseSku;
	private String nonMerchandiseSku;
	private String masterItemId;
	private String productDescription;
	private Long reservedKeys;
	private Long activatedKeys;
	private Long remainingKeys;
	private Long inactiveKeys;
	private Long mismatchedKeys;
	private Float burnRate;
	private Float errorsPer1KOrders;
	private Long maxPerDay;
	private Long maxPerMonth;
	private Long keysUsedLastXDays;
	private Float estDaysToDepletion;
	private Long keysExpired;
	private String orderId;
	private String keyCodeReserved;
	private String keyCodeActual;
	private Date date;
	public KcbReportBean() {
	}
	public KcbReportBean(String vendorId, String merchandiseSku, String nonMerchandiseSku, String masterItemId, String productDescription, 
			Long reservedKeys, Long activatedKeys, Long remainingKeys, Long inactiveKeys, Long mismatchedKeys,
			Float burnRate, Float errorsPer1KOrders, Long maxPerDay, Long maxPerMonth, Long keysUsedLastXDays,
			Float estDaysToDepletion, Long keysExpired, String orderId, String keyCodeReserved, String keyCodeActual, Date date) {
		this.vendorId = vendorId;
		this.merchandiseSku = merchandiseSku;
		this.nonMerchandiseSku = nonMerchandiseSku;
		this.masterItemId = masterItemId;
		this.productDescription = productDescription;
		this.reservedKeys = reservedKeys;
		this.activatedKeys = activatedKeys;
		this.remainingKeys = remainingKeys;
		this.inactiveKeys = inactiveKeys;
		this.mismatchedKeys = mismatchedKeys;
		this.burnRate = burnRate;
		this.errorsPer1KOrders = errorsPer1KOrders;
		this.maxPerDay = maxPerDay;
		this.maxPerMonth = maxPerMonth;
		this.keysUsedLastXDays = keysUsedLastXDays;
		this.estDaysToDepletion = estDaysToDepletion;
		this.keysExpired = keysExpired;
		this.orderId = orderId;
		this.keyCodeReserved = keyCodeReserved;
		this.keyCodeActual = keyCodeActual;
		this.date = date;
	}
	public KcbReportBean(KcbReportBean kcbReportBean) {
		this.vendorId = kcbReportBean.vendorId;
		this.merchandiseSku = kcbReportBean.merchandiseSku;
		this.nonMerchandiseSku = kcbReportBean.nonMerchandiseSku;
		this.masterItemId = kcbReportBean.masterItemId;
		this.productDescription = kcbReportBean.productDescription;
		this.reservedKeys = kcbReportBean.reservedKeys;
		this.activatedKeys = kcbReportBean.activatedKeys;
		this.remainingKeys = kcbReportBean.remainingKeys;
		this.inactiveKeys = kcbReportBean.inactiveKeys;
		this.mismatchedKeys = kcbReportBean.mismatchedKeys;
		this.burnRate = kcbReportBean.burnRate;
		this.errorsPer1KOrders = kcbReportBean.errorsPer1KOrders;
		this.maxPerDay = kcbReportBean.maxPerDay;
		this.maxPerMonth = kcbReportBean.maxPerMonth;
		this.keysUsedLastXDays = kcbReportBean.keysUsedLastXDays;
		this.estDaysToDepletion = kcbReportBean.estDaysToDepletion;
		this.orderId = kcbReportBean.orderId;
		this.keyCodeReserved = kcbReportBean.keyCodeReserved;
		this.keyCodeActual = kcbReportBean.keyCodeActual;
		this.date = kcbReportBean.date;
		this.keysExpired = kcbReportBean.keysExpired;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return Constants.getVendorName(this.getVendorId());
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
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Long getReservedKeys() {
		return reservedKeys;
	}
	public void setReservedKeys(Long reservedKeys) {
		this.reservedKeys = reservedKeys;
	}
	public Long getActivatedKeys() {
		return activatedKeys;
	}
	public void setActivatedKeys(Long activatedKeys) {
		this.activatedKeys = activatedKeys;
	}
	public Long getRemainingKeys() {
		return remainingKeys;
	}
	public void setRemainingKeys(Long remainingKeys) {
		this.remainingKeys = remainingKeys;
	}
	public Long getInactiveKeys() {
		return inactiveKeys;
	}
	public void setInactiveKeys(Long inactiveKeys) {
		this.inactiveKeys = inactiveKeys;
	}
	public Long getMismatchedKeys() {
		return mismatchedKeys;
	}
	public void setMismatchedKeys(Long mismatchedKeys) {
		this.mismatchedKeys = mismatchedKeys;
	}
	public Float getBurnRate() {
		return burnRate;
	}
	public void setBurnRate(Float burnRate) {
		this.burnRate = burnRate;
	}
	public Float getErrorsPer1KOrders() {
		return errorsPer1KOrders;
	}
	public void setErrorsPer1KOrders(Float errorsPer1KOrders) {
		this.errorsPer1KOrders = errorsPer1KOrders;
	}
	public Long getMaxPerDay() {
		return maxPerDay;
	}
	public void setMaxPerDay(Long maxPerDay) {
		this.maxPerDay = maxPerDay;
	}
	public Long getMaxPerMonth() {
		return maxPerMonth;
	}
	public void setMaxPerMonth(Long maxPerMonth) {
		this.maxPerMonth = maxPerMonth;
	}
	public Long getKeysUsedLastXDays() {
		return keysUsedLastXDays;
	}
	public void setKeysUsedLastXDays(Long keysUsedLastXDays) {
		this.keysUsedLastXDays = keysUsedLastXDays;
	}
	public Float getEstDaysToDepletion() {
		return estDaysToDepletion;
	}
	public void setEstDaysToDepletion(Float estDaysToDepletion) {
		this.estDaysToDepletion = estDaysToDepletion;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getKeyCodeReserved() {
		return keyCodeReserved;
	}
	public void setKeyCodeReserved(String keyCodeReserved) {
		this.keyCodeReserved = keyCodeReserved;
	}
	public String getKeyCodeActual() {
		return keyCodeActual;
	}
	public void setKeyCodeActual(String keyCodeActual) {
		this.keyCodeActual = keyCodeActual;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getKeysExpired() {
		return keysExpired;
	}
	public void setKeysExpired(Long keysExpired) {
		this.keysExpired = keysExpired;
	}
}
