package com.accenture.bby.sdp.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * <code>SimpleAuditLog</code> is designed to populated the search results table.
 * For full audit log detail use the <code>AuditLog</code> class.
 * 
 * @author Ross Stockman
 *
 */
public class SimpleAuditLog {
	private Date date;
	private String contractId;
	private String serialNumber;
	private String lineItemId;
	private String action;
	private String userId;
	private String logId;
	private String transactionDate;
	private String storeId;
	private String registerId;
	private String transactionId;
	private String lineId;
	private String primarySku;
	private String relatedSku;
	private String firstName;
	private String lastName;
	private String deliveryEmail;
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the vendorKey to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the lineItemId
	 */
	public String getLineItemId() {
		return lineItemId;
	}
	/**
	 * @param lineItemId the lineItemId to set
	 */
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the logId
	 */
	public String getLogId() {
		return logId;
	}
	/**
	 * @param logId the logId to set
	 */
	public void setLogId(String logId) {
		this.logId = logId;
	}
	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}
	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	/**
	 * @return the registerId
	 */
	public String getRegisterId() {
		return registerId;
	}
	/**
	 * @param registerId the registerId to set
	 */
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the primarySku
	 */
	public String getPrimarySku() {
		return primarySku;
	}
	/**
	 * @param primarySku the primarySku to set
	 */
	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}
	
	
	
	/**
	 * @return the relatedSku
	 */
	public String getRelatedSku() {
		return relatedSku;
	}
	/**
	 * @param relatedSku the relatedSku to set
	 */
	public void setRelatedSku(String relatedSku) {
		this.relatedSku = relatedSku;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		if (firstName == null && lastName == null)
			return null;
		else if (firstName == null)
			return lastName;
		else if (lastName == null)
			return firstName;
		else
			return firstName + " " + lastName;
	}
	/**
	 * @return the deliveryEmail
	 */
	public String getDeliveryEmail() {
		return deliveryEmail;
	}
	/**
	 * @param deliveryEmail the deliveryEmail to set
	 */
	public void setDeliveryEmail(String deliveryEmail) {
		this.deliveryEmail = deliveryEmail;
	}
	
	public SimpleAuditLog() {
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat df = new SimpleDateFormat("M/d/yy h:mm:ss a");
		
		builder.append("\"" + (getDate() == null ? "" : df.format(getDate())) + "\",");
		builder.append("\"" + (getContractId() == null ? "" : getContractId()) + "\",");
		builder.append("\"" + (getSerialNumber() == null ? "" : getSerialNumber()) + "\",");
		builder.append("\"" + (getLineItemId() == null ? "" : getLineItemId()) + "\",");
		builder.append("\"" + (getAction() == null ? "" : getAction()) + "\",");
		builder.append("\"" + (getUserId() == null ? "" : getUserId()) + "\",");
		builder.append("\"" + (getLogId() == null ? "" : getLogId()) + "\",");
		builder.append("\"" + (getTransactionDate() == null ? "" : getTransactionDate()) + "\",");
		builder.append("\"" + (getStoreId() == null ? "" : getStoreId()) + "\",");
		builder.append("\"" + (getRegisterId() == null ? "" : getRegisterId()) + "\",");
		builder.append("\"" + (getTransactionId() == null ? "" : getTransactionId()) + "\",");
		builder.append("\"" + (getPrimarySku() == null ? "" : getPrimarySku()) + "\",");
		builder.append("\"" + (getRelatedSku() == null ? "" : getRelatedSku()) + "\",");
		builder.append("\"" + (getCustomerName() == null ? "" : getCustomerName()) + "\",");
		builder.append("\"" + (getDeliveryEmail() == null ? "" : getDeliveryEmail()) + "\",");
		
		return builder.toString();
	}
	
	/**
	 * Formats list of <code>SimpleAuditLog</code> to be formatted into CSV comma delemited format. 
	 * 
	 * @param simpleAuditLogs list of <code>SimpleAuditLog</code> to be exported to CSV.
	 * @return String of comma separated data to be inserted into a CSV file.
	 */
	public static String getCsvExportString(List<SimpleAuditLog> simpleAuditLogs) {
		StringBuilder builder = new StringBuilder();
		builder.append("\"ROW\",");
		builder.append("\"DATE\",");
		builder.append("\"CONTRACT ID\",");
		builder.append("\"VENDOR KEY (SN)\",");
		builder.append("\"LINEITEM ID\",");
		builder.append("\"ACTION\",");
		builder.append("\"USER ID\",");
		builder.append("\"LOG ID\",");
		builder.append("\"TRANSACTION ID\",");
		builder.append("\"STORE ID\",");
		builder.append("\"REGISTER ID\",");
		builder.append("\"TRANSACTION ID,\",");
		builder.append("\"PRODUCT SKU\",");
		builder.append("\"PLAN SKU\",");
		builder.append("\"CUSTOMER NAME\",");
		builder.append("\"DELIVERY EMAIL\",");
		builder.append("\"START DATE\",");
		builder.append("\"END DATE\",\n");
		
		if (simpleAuditLogs != null) {
			int rowNum = 0;
			for (SimpleAuditLog itr : simpleAuditLogs) {
				builder.append("\"" + (++rowNum) + "\",");
				builder.append(itr.toString());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the lineId
	 */
	public String getLineId() {
		return lineId;
	}
	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	
	
}
