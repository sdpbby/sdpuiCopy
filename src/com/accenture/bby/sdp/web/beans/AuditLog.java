package com.accenture.bby.sdp.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class AuditLog {
	private String id;
	private List<FieldAnalysis> fieldAnalysisList;
	private String action;
	private String userId;
	private Date date;
	private List<XmlAnalysis> xmlAnalysisList;
	private List<SimpleAuditLog> relatedAuditLogs;
	private String contractId;
	private String serialNumber;
	private String lineItemId;

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
	 * @param serialNumber the serialNumber to set
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the fieldAnalysisList
	 */
	public List<FieldAnalysis> getFieldAnalysisList() {
		return fieldAnalysisList;
	}

	/**
	 * @param fieldAnalysisList the fieldAnalysisList to set
	 */
	public void setFieldAnalysisList(List<FieldAnalysis> fieldAnalysisList) {
		this.fieldAnalysisList = fieldAnalysisList;
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
	 * @return the xmlAnalysisList
	 */
	public List<XmlAnalysis> getXmlAnalysisList() {
		return xmlAnalysisList;
	}

	/**
	 * @param xmlAnalysisList the xmlAnalysisList to set
	 */
	public void setXmlAnalysisList(List<XmlAnalysis> xmlAnalysisList) {
		this.xmlAnalysisList = xmlAnalysisList;
	}

	/**
	 * @return the relatedAuditLogs
	 */
	public List<SimpleAuditLog> getRelatedAuditLogs() {
		return relatedAuditLogs;
	}

	/**
	 * @param relatedAuditLogs the relatedAuditLogs to set
	 */
	public void setRelatedAuditLogs(List<SimpleAuditLog> relatedAuditLogs) {
		this.relatedAuditLogs = relatedAuditLogs;
	}
	
	/**
	 * Formats <code>AuditLog</code> to be formatted into CSV comma delimited format. 
	 * 
	 * @param auditLog <code>AuditLog</code> to be exported to CSV.
	 * @return String of comma separated data to be inserted into a CSV file.
	 */
	public static String getCsvExportString(AuditLog auditLog) {
		SimpleDateFormat df = new SimpleDateFormat("M/d/yy h:mm:ss a");
		StringBuilder builder = new StringBuilder();
		builder.append("\"Audit Log\",\"" + (auditLog.getId() == null ? "" : auditLog.getId()) + "\",\n");
		builder.append("\"Action\",\"" + (auditLog.getAction() == null ? "" : auditLog.getAction()) + "\",\n");
		builder.append("\"User Id\",\"" + (auditLog.getUserId() == null ? "" : auditLog.getUserId()) + "\",\n");
		builder.append("\"Date\",\"" + (auditLog.getDate() == null ? "" : df.format(auditLog.getDate())) + "\",\n");
		builder.append("\n");
		
		builder.append(FieldAnalysis.getCsvExportString(auditLog.getFieldAnalysisList()));
		
		builder.append("\n\"Related Audit Logs:\",\n");
		
		builder.append(SimpleAuditLog.getCsvExportString(auditLog.getRelatedAuditLogs()));
		
		return builder.toString();
	}
	
}
