package com.accenture.bby.sdp.web.beans;

public class WorkFlowAttributeBean extends WebBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418264373635330364L;
	private String attributeName;
	private String attributeValue;
	private OperationFlag operationFlag;
	public enum OperationFlag {
		INSERT, UPDATE;
	}
	public WorkFlowAttributeBean() {
		
	}
	public WorkFlowAttributeBean(String attributeName, String attributeValue, OperationFlag operationFlag) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.operationFlag = operationFlag;
	}
	public WorkFlowAttributeBean(WorkFlowAttributeBean workFlowAttributeBean) {
		this.attributeName = workFlowAttributeBean.attributeName;
		this.attributeValue = workFlowAttributeBean.attributeValue;
		this.operationFlag = workFlowAttributeBean.operationFlag;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = filter(attributeName);
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = filter(attributeValue);
	}
	public OperationFlag getOperationFlag() {
		return operationFlag;
	}
	public void setOperationFlag(OperationFlag operationFlag) {
		this.operationFlag = operationFlag;
	}
	@Override
	public String toString() {
		return ("<WorkFlowAttributeBean") +
		(" attributeName=\"" + (attributeName != null ? attributeName : "[null]") + "\"") +
		(" attributeValue=\"" + (attributeValue != null ? attributeValue : "[null]") + "\"") +
		(" operationFlag=\"" + (operationFlag != null ? operationFlag : "[null]") + "\"") +
		(" />");
	}
}
