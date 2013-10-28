package com.accenture.bby.sdp.web.beans;

public class AttributeBean extends WebBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3978338827415438917L;
	private String key;
	private String value;
	public AttributeBean() {		
	}
	public AttributeBean(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public AttributeBean(AttributeBean attributeBean) {
		this.key = attributeBean.key;
		this.value = attributeBean.value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = filter(key);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = filter(value);
	}
	@Override
	public String toString() {
		return ("<AttributeBean") +
		(" key=\"" + (key != null ? key : "[null]") + "\"") +
		(" value=\"" + (value != null ? value : "[null]") + "\"") +
		(" />");
	}
}
