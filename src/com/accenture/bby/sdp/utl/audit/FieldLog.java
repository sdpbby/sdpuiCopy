package com.accenture.bby.sdp.utl.audit;

import java.util.Date;

import com.accenture.bby.sdp.utl.DateUtil;

/**
 * @author a224307, a719057
 *
 */
public class FieldLog {
	
	private int fieldId;
	private String oldValue;
	private String newValue;
	private Date createdDate;
	private int priority;
	
	public static final String MODIFIED_CHANGE_ANALYSIS = "MODIFIED VALUE";
	public static final String REMOVED_CHANGE_ANALYSIS = "REMOVED VALUE";
	public static final String ADDED_CHANGE_ANALYSIS = "ADDED VALUE";
	public static final String NO_CHANGE_ANALYSIS = "NO CHANGE";
	public static final String NO_VALUE_ANALYSIS = "NO VALUE";
	
	public FieldLog() {
	}
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the changeAnalysis
	 */
	public String getChangeAnalysis() {
		if (this.newValue == null && this.oldValue == null) {
			return NO_VALUE_ANALYSIS;
		} else if (this.oldValue == null) {
			return ADDED_CHANGE_ANALYSIS;
		} else if (this.newValue == null) {
			return REMOVED_CHANGE_ANALYSIS;
		} else if (this.newValue.equals(this.oldValue)) {
			return NO_CHANGE_ANALYSIS;
		} else {
			return MODIFIED_CHANGE_ANALYSIS;
		}
	}
	@Override
	public String toString() {
		return ("<FieldLog") +
				(" fieldId=\"" + fieldId + "\"") +
				(" oldValue=\"" + (oldValue != null ? oldValue : "[null]") + "\"") +
				(" newValue=\"" + (newValue != null ? newValue : "[null]") + "\"") +
				(" priority=\"" + priority + "\"") +
				(" createdDate=\"" + (createdDate != null ? DateUtil.getXmlDateString(createdDate) : "[null]") + "\"") +
				(" />");
	}
}
