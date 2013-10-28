package com.accenture.bby.sdp.utl.weblogic;

import java.util.List;


public class JMSDestinationBeanGroup implements Comparable<JMSDestinationBeanGroup>{
	
	private String groupName;
	private List<JMSDestinationBean> jmsDestinationBeans;
	
	public static final String ROW_CLASS_WARNING = "warning_row";
	public static final String ROW_CLASS_ERROR = "error_row";
	public static final String ROW_CLASS_NORMAL_ODD = "odd";
	public static final String ROW_CLASS_NORMAL_EVEN = "even";
	
	public String getCssRowClasses() {
		if (jmsDestinationBeans == null) {
			return ROW_CLASS_NORMAL_ODD;
		}
		StringBuilder sb = new StringBuilder();

		int count = 1;
	    int jmsDestinationBeansSize = jmsDestinationBeans.size();
	    for (int i = 0; i < jmsDestinationBeansSize; i++) {
		    boolean even = count++ % 2 != 0;
	
		    if (isWarning(jmsDestinationBeans.get(i))) {
		    	sb.append(ROW_CLASS_ERROR);
		    } else if (isInfo(jmsDestinationBeans.get(i))) {
		    	sb.append(ROW_CLASS_WARNING);
		    } else {
		    	if (even) {
		    		sb.append(ROW_CLASS_NORMAL_EVEN);
		    	} else {
		    		sb.append(ROW_CLASS_NORMAL_ODD);
		    	}
		    }
		    if (i < jmsDestinationBeansSize-1) {
		    	sb.append(", ");
		    }
	    }
	    return sb.toString();
	}
	
	public boolean isInfo(JMSDestinationBean jmsDestinationBean) {
		if (jmsDestinationBean.getConsumersCurrentCount() < 1) {
			return true;
		}
		return false;
	}
	
	public boolean isWarning(JMSDestinationBean jmsDestinationBean) {
		if (jmsDestinationBean.getState().contains("paused_consumption")) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		return result;
	}
	
	@Override
	public int compareTo(JMSDestinationBeanGroup o) {
		
		if (o == null) {
			throw new NullPointerException("null parameter passed");
		}
		
		if (this.getGroupName() == null ||
				o.getGroupName() == null) {
				throw new IllegalArgumentException("this or other JMSDestinationBean has null groupName. Cannot compare.");
			}
		
		return getGroupName().compareTo(o.getGroupName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JMSDestinationBeanGroup other = (JMSDestinationBeanGroup) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public List<JMSDestinationBean> getJmsDestinationBeans() {
		return jmsDestinationBeans;
	}
	
	public void setJmsDestinationBeans(List<JMSDestinationBean> jmsDestinationBeans) {
		this.jmsDestinationBeans = jmsDestinationBeans;
	}
}