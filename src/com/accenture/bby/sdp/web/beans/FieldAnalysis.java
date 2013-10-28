package com.accenture.bby.sdp.web.beans;

import java.util.List;

public class FieldAnalysis {
	private String id;
	private String fieldLabel;
	private String originalValue;
	private String newValue;
	private String changeAnalysis;
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
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
	/**
	 * @param fieldLabel the fieldLabel to set
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	/**
	 * @return the originalValue
	 */
	public String getOriginalValue() {
		return originalValue;
	}
	/**
	 * @param originalValue the originalValue to set
	 */
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}
	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	/**
	 * @return the changeAnalysis
	 */
	public String getChangeAnalysis() {
		return changeAnalysis;
	}
	/**
	 * @param analysis the analysis to set
	 */
	public void setChangeAnalysis(String changeAnalysis) {
		this.changeAnalysis = changeAnalysis;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("\"" + (getFieldLabel() == null ? "" : getFieldLabel()) + "\",");
		builder.append("\"" + (getOriginalValue() == null ? "" : getOriginalValue()) + "\",");
		builder.append("\"" + (getNewValue() == null ? "" : getNewValue()) + "\",");
		builder.append("\"" + (getChangeAnalysis() == null ? "" : getChangeAnalysis()) + "\",");
				
		return builder.toString();
	}
	
	/**
	 * Formats list of <code>FieldAnalysis</code> to be formatted into CSV comma delimited format. 
	 * 
	 * @param fieldAnalysisList list of <code>FieldAnalysis</code> to be exported to CSV.
	 * @return String of comma separated data to be inserted into a CSV file.
	 */
	public static String getCsvExportString(List<FieldAnalysis> fieldAnalysisList) {
		StringBuilder builder = new StringBuilder();
		builder.append("\"LABEL\",");
		builder.append("\"ORIGINAL VALUE\",");
		builder.append("\"NEW VALUE\",");
		builder.append("\"CHANGE ANALYSIS\",\n");
		
		if (fieldAnalysisList != null) {
			for (FieldAnalysis itr : fieldAnalysisList) {
				builder.append(itr.toString());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}
