package com.accenture.bby.sdp.utl.weblogic;

import javax.management.ObjectName;

public class JMSDestinationBean implements Comparable<JMSDestinationBean>{
	
	private String jmsServerName;
	private String moduleName;
	private String destinationName;
	private String state;
	private long messagesCurrentCount;
	private long consumersCurrentCount;
	
	private ObjectName jmxObjectName;
	
	@Override
	public int compareTo(JMSDestinationBean o) {
		
		if (o == null) {
			throw new NullPointerException("null parameter");
		}
		
		if (this.getDestinationName() == null ||
			this.getJmsServerName() == null ||
			this.moduleName == null || 
			o.getDestinationName() == null ||
			o.getJmsServerName() == null ||
			o.moduleName == null) {
			throw new IllegalArgumentException("this or other JMSDestinationBean has null fields. Cannot compare.");
		}
		
		int compareResult;
		
		if ((compareResult = this.getDestinationName().compareTo(o.getDestinationName())) != 0) {
			return compareResult;
		} else if ((compareResult = this.getModuleName().compareTo(o.getModuleName())) != 0) {
			return compareResult;
		} else if ((compareResult = this.getJmsServerName().compareTo(o.getJmsServerName())) != 0) {
			return compareResult;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (consumersCurrentCount ^ (consumersCurrentCount >>> 32));
		result = prime * result + ((jmsServerName == null) ? 0 : jmsServerName.hashCode());
		result = prime * result + ((jmxObjectName == null) ? 0 : jmxObjectName.hashCode());
		result = prime * result + (int) (messagesCurrentCount ^ (messagesCurrentCount >>> 32));
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JMSDestinationBean other = (JMSDestinationBean) obj;
		if (consumersCurrentCount != other.consumersCurrentCount)
			return false;
		if (jmsServerName == null) {
			if (other.jmsServerName != null)
				return false;
		} else if (!jmsServerName.equals(other.jmsServerName))
			return false;
		if (jmxObjectName == null) {
			if (other.jmxObjectName != null)
				return false;
		} else if (!jmxObjectName.equals(other.jmxObjectName))
			return false;
		if (messagesCurrentCount != other.messagesCurrentCount)
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	public String getJmsServerName() {
		return jmsServerName;
	}
	public void setJmsServerName(String jmsServerName) {
		this.jmsServerName = jmsServerName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getMessagesCurrentCount() {
		return messagesCurrentCount;
	}
	public void setMessagesCurrentCount(long messagesCurrentCount) {
		this.messagesCurrentCount = messagesCurrentCount;
	}
	public long getConsumersCurrentCount() {
		return consumersCurrentCount;
	}
	public void setConsumersCurrentCount(long consumersCurrentCount) {
		this.consumersCurrentCount = consumersCurrentCount;
	}
	public ObjectName getJmxObjectName() {
		return jmxObjectName;
	}
	public void setJmxObjectName(ObjectName jmxObjectName) {
		this.jmxObjectName = jmxObjectName;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
}
