package com.accenture.bby.sdp.utl.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.accenture.bby.sdp.utl.DateUtil;

public class TransactionLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2548283547723936458L;
	private String sessionId; 
	private String userId;
	private Date loggedInTime;
	private Date loggedOutTime;
	private String lineItemId;
	private String vendorKey;
	private String contractId;
	private String primarySku;
	private String vendorId;
	private String masterItemId;
	private Action action;
	private String roleName;
	private List<FieldLog> auditLogs;
	
	private int audTransId;
	
	public TransactionLog() {
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLoggedInTime() {
		return loggedInTime;
	}

	public void setLoggedInTime(Date loggedInTime) {
		this.loggedInTime = loggedInTime;
	}

	public Date getLoggedOutTime() {
		return loggedOutTime;
	}

	public void setLoggedOutTime(Date loggedOutTime) {
		this.loggedOutTime = loggedOutTime;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getVendorKey() {
		return vendorKey;
	}

	public void setVendorKey(String vendorKey) {
		this.vendorKey = vendorKey;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getPrimarySku() {
		return primarySku;
	}

	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<FieldLog> getAuditLogs() {
		return auditLogs;
	}

	public void setAuditLogs(List<FieldLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public int getAudTransId() {
		return audTransId;
	}

	public void setAudTransId(int audTransId) {
		this.audTransId = audTransId;
	}
	
	

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getMasterItemId() {
		return masterItemId;
	}

	public void setMasterItemId(String masterItemId) {
		this.masterItemId = masterItemId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(("<TransactionLog") +
				(" sessionId=\"" + (sessionId != null ? sessionId : "[null]") + "\"") +
				(" userId=\"" + (userId != null ? userId : "[null]") + "\"") +
				(" lineItemId=\"" + (lineItemId != null ? lineItemId : "[null]") + "\"") +
				(" vendorKey=\"" + (vendorKey != null ? vendorKey : "[null]") + "\"") +
				(" contractId=\"" + (contractId != null ? contractId : "[null]") + "\"") +
				(" primarySku=\"" + (primarySku != null ? primarySku : "[null]") + "\"") +
				(" vendorId=\"" + (vendorId != null ? vendorId : "[null]") + "\"") +
				(" masterItemId=\"" + (masterItemId != null ? masterItemId : "[null]") + "\"") +
				(" action=\"" + (action != null ? action : "[null]") + "\"") +
				(" roleName=\"" + (roleName != null ? roleName : "[null]") + "\"") +
				(" loggedInTime=\"" + (loggedInTime != null ? DateUtil.getXmlDateString(loggedInTime) : "[null]") + "\"") +
				(" loggedOutTime=\"" + (loggedOutTime != null ? DateUtil.getXmlDateString(loggedOutTime) : "[null]") + "\"")
				);
		if (auditLogs != null && auditLogs.size() > 0) {
			for (FieldLog log : auditLogs) {
				builder.append("\n\t  " + log);
			}
			builder.append("\n</SessionAuditLog>");
		} else {
			builder.append(" />");
		}
		return builder.toString();
	}
}
