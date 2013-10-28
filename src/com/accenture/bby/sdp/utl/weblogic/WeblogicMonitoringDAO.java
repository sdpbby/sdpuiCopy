package com.accenture.bby.sdp.utl.weblogic;

import java.util.List;

public abstract class WeblogicMonitoringDAO {
	
	
	
	public static WeblogicMonitoringDAO getSdpInstance(String username, String password, String hostAndPort) {
		return new SBLayerMonitoringDAO(username, password, hostAndPort);
	}
	
	/**
	 * This method queries the SO2 instance and returns a List<JMSDestinationBean> representing
	 * all JMS destinations.
	 * 
	 * @param moduleNames
	 */
	public abstract List<JMSDestinationBean> getAllJmsDestinations();
	
	/**
	 * This method queries the SO2 instance and returns a List<JMSDestinationBean> where the
	 * jms module name matches passed parameter. If the parameter is null, it will not be taken 
	 * into account and all {@link JMSDestinationBean}s regardless of the value nulled will be returned.
	 * 
	 * @param moduleName
	 */
	public abstract List<JMSDestinationBean> getJmsDestinationsByModuleName(String moduleName);
	

	
	/**
	 * This method queries the SO2 instance and returns a List<JMSDestinationBean> where the
	 * jms module name and queue name match the passed module name string and one of the strings in passed queue
	 * name list parameter respectively. If either parameter is null, it will not be taken 
	 * into account and all {@link JMSDestinationBean}s regardless of the value nulled will be returned.
	 * 
	 * @param moduleNames
	 */
	public abstract List<JMSDestinationBean> getJmsDestinationsByModuleAndDestinationName(String moduleName, List<String> queueName);
	
	public abstract void pauseDestinationConsumption(JMSDestinationBean destination);
	public abstract void resumeDestinationConsumption(JMSDestinationBean destination);
	
	public abstract List<ServerStatusBean> getServerStatus();
	
}