package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.audit.Auditable;
import com.accenture.bby.sdp.utl.audit.Field;

public class ECCUISearchBean extends WebBean implements Serializable, Auditable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1677939014991993658L;
	private Date fpkTransactionDate;
	private String fpkStoreId;
	private String fpkRegisterId;
	private String fpkTransactionId;
	private String bbyOrderId;
	
	public ECCUISearchBean() {
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
	public String getBbyOrderId() {
		return bbyOrderId;
	}
	public void setBbyOrderId(String bbyOrderId) {
		this.bbyOrderId = bbyOrderId;
	}
	@Override
	public String toString() {
		return "<ECCUISearchBean" + 
		(" fpkStoreId=\"" + (fpkStoreId != null ? fpkStoreId : "[null]") + "\"") +
		(" fpkRegisterId=\"" + (fpkRegisterId != null ? fpkRegisterId : "[null]") + "\"") +
		(" fpkTransactionId=\"" + (fpkTransactionId != null ? fpkTransactionId : "[null]") + "\"") +
		(" bbyOrderId=\"" + (bbyOrderId != null ? bbyOrderId : "[null]") + "\"") +
		(" fpkTransactionDate=\"" + (fpkTransactionDate != null ? DateUtil.getStoreDateString(fpkTransactionDate) : "[null]") + "\"") +
		" />";
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
		return null;
	}
	@Override
	public String getContractIdForAudit() {
		return null;
	}
	@Override
	public String getVendorIdForAudit() {
		return null;
	}
	@Override
	public String getMasterItemIdForAudit() {
		return null;
	}
	@Override
	public Map<Field, String> getAuditableFields() {
		Map<Field, String> fields = new HashMap<Field, String>();
		if (this.fpkTransactionDate != null)
			fields.put(Field.FPK_TRANSACTION_DATE.priority(1), DateUtil.getStoreDateString(this.fpkTransactionDate));
		if (this.fpkStoreId != null)
			fields.put(Field.FPK_STORE_ID.priority(2), this.fpkStoreId);
		if (this.fpkRegisterId != null)
			fields.put(Field.FPK_REGISTER_ID.priority(3), this.fpkRegisterId);
		if (this.fpkTransactionId != null)
			fields.put(Field.FPK_TRANSACTION_ID.priority(4), this.fpkTransactionId);
		if (this.bbyOrderId != null)
			fields.put(Field.BBY_ORDER_ID.priority(5), this.bbyOrderId);
		return fields;
	}
}
