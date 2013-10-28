package com.accenture.bby.sdp.utl.weblogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/*
 * This DAO can be swapped out for a more robust implementation
 * by simply changing the object returned it's parent class's getInstance() method.
 * 
 */ 
class SBLayerMonitoringDAO extends WeblogicMonitoringDAO {
	
	private String username;
	private String password;
	private String hostAndPort;

	// private constructor; use factory methods to instantiate.
	protected SBLayerMonitoringDAO(String username, String password, String hostAndPort) {
		this.username = username;
		this.password = password;
		this.hostAndPort = hostAndPort;
	}
	
	private static final ObjectName service;
	
	static {
		try {
			service = new ObjectName("com.bea:Name=DomainRuntimeService,"
					+ "Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
			
		} catch (MalformedObjectNameException e) {
			throw new IllegalStateException("Encountered MalformedObjectNameException creating DomainRuntimeService object name", e);
		}
	}
	
	@Override
	public List<ServerStatusBean> getServerStatus() {
		List<ServerStatusBean> serverStatusBeans = new ArrayList<ServerStatusBean>();
		try {
			MBeanServerConnection conn = JMXConnectionManager.getJMXConnection(username, password, hostAndPort);
			ObjectName[] serverRuntimes = (ObjectName[]) conn.getAttribute(service, "ServerRuntimes");
			int count = serverRuntimes.length;
			for (int i = 0; i < count; i++) {
				ServerStatusBean serverStatusBean = new ServerStatusBean();
				serverStatusBean.setServerName((String)conn.getAttribute(serverRuntimes[i], "Name"));
				serverStatusBean.setServerType((String)conn.getAttribute(serverRuntimes[i], "Type"));
				serverStatusBean.setServerState((String)conn.getAttribute(serverRuntimes[i], "State"));
				serverStatusBean.setServerHealth("PLACE HOLDER");
				//serverStatusBean.setServerHealth(HealthState.mapToString(((HealthState)conn.getAttribute(serverRuntimes[i], "HealthState")).getState()));
				serverStatusBeans.add(serverStatusBean);
			}
			return serverStatusBeans;
		} catch (AttributeNotFoundException e) {
			throw new IllegalStateException("Encountered AttributeNotFoundException creating DomainRuntimeService object name", e);
		} catch (InstanceNotFoundException e) {
			throw new IllegalStateException("Encountered InstanceNotFoundException creating DomainRuntimeService object name", e);
		} catch (MBeanException e) {
			throw new IllegalStateException("Encountered MBeanException creating DomainRuntimeService object name", e);
		} catch (ReflectionException e) {
			throw new IllegalStateException("Encountered ReflectionException creating DomainRuntimeService object name", e);
		} catch (IOException e) {
			throw new IllegalStateException("Encountered IOException creating DomainRuntimeService object name", e);
		}
	}
	
	public List<JMSDestinationBean> getAllJmsDestinations() {
		
		
		List<JMSDestinationBean> resultList = new ArrayList<JMSDestinationBean>();
		
		try {
		
		MBeanServerConnection conn = JMXConnectionManager.getJMXConnection(username, password, hostAndPort);
		
		ObjectName[] serverRuntimes = (ObjectName[]) conn.getAttribute(service, "ServerRuntimes");
		
		
		for (ObjectName serverRuntime : serverRuntimes) {
			
			ObjectName jmsRuntime = (ObjectName) conn.getAttribute(serverRuntime, "JMSRuntime");
			ObjectName[] jmsServers = (ObjectName[]) conn.getAttribute(jmsRuntime, "JMSServers");
			
			for (ObjectName jmsServer : jmsServers) {
				
				ObjectName[] destinations = (ObjectName[]) conn.getAttribute(jmsServer, "Destinations");
				
				for (ObjectName destination : destinations) {
					
					String destinationName = (String) conn.getAttribute(destination, "Name");
					
					String[] destinationNameFields = destinationName.split("[!@]");
					
					JMSDestinationBean currentDestBean = new JMSDestinationBean();
					
					
					try {
						currentDestBean.setModuleName(destinationNameFields[0]);
						currentDestBean.setJmsServerName(destinationNameFields[1]);
						currentDestBean.setDestinationName(destinationNameFields[2]);
					} catch (ArrayIndexOutOfBoundsException  e) {
						// Kludge, need to find a better way to get these values
						e.printStackTrace();
					}
					
					
					currentDestBean.setMessagesCurrentCount((Long) conn.getAttribute(destination, "MessagesCurrentCount"));
					currentDestBean.setConsumersCurrentCount((Long) conn.getAttribute(destination, "ConsumersCurrentCount"));
					currentDestBean.setState((String) conn.getAttribute(destination, "State"));
					currentDestBean.setJmxObjectName(destination);
					
					resultList.add(currentDestBean);
				}
			}
		}
		
		} catch (AttributeNotFoundException e) {
			throw new IllegalStateException("Encountered AttributeNotFoundException creating DomainRuntimeService object name", e);
		} catch (InstanceNotFoundException e) {
			throw new IllegalStateException("Encountered InstanceNotFoundException creating DomainRuntimeService object name", e);
		} catch (MBeanException e) {
			throw new IllegalStateException("Encountered MBeanException creating DomainRuntimeService object name", e);
		} catch (ReflectionException e) {
			throw new IllegalStateException("Encountered ReflectionException creating DomainRuntimeService object name", e);
		} catch (IOException e) {
			throw new IllegalStateException("Encountered IOException creating DomainRuntimeService object name", e);
		}
		
		return resultList;
	}

	@Override
	public List<JMSDestinationBean> getJmsDestinationsByModuleName(String pModuleName) {
		
		List<JMSDestinationBean> results = getAllJmsDestinations();
		
		if (pModuleName != null) {
			
			List<JMSDestinationBean> rejectionBeans = new ArrayList<JMSDestinationBean>();
			
			for (JMSDestinationBean currentBean : results) {
				
				if (!pModuleName.equals(currentBean.getModuleName())) {
					rejectionBeans.add(currentBean);
				}
			}
			results.removeAll(rejectionBeans);
		}
		return results;
	}

	@Override
	public List<JMSDestinationBean> getJmsDestinationsByModuleAndDestinationName(String pModuleName, List<String> pDestNames) {
		
		List<JMSDestinationBean> results = getAllJmsDestinations();
		
		//filter by module name
		if (pModuleName != null) {
			
			List<JMSDestinationBean> rejectionBeans = new ArrayList<JMSDestinationBean>();
			
			for (JMSDestinationBean currentBean : results) {
				
				if (!pModuleName.equals(currentBean.getModuleName())) {
					rejectionBeans.add(currentBean);
				}
			}
			results.removeAll(rejectionBeans);
		}
		
		//filter by queue name
		if (pDestNames != null) {
			
			List<JMSDestinationBean> rejectionBeans = new ArrayList<JMSDestinationBean>();
			
			for (JMSDestinationBean currentBean : results) {
				
				if (!pDestNames.contains(currentBean.getDestinationName())) {
					rejectionBeans.add(currentBean);
				}
			}
			results.removeAll(rejectionBeans);
		}
		
		return results;
	}

	@Override
	public void pauseDestinationConsumption(JMSDestinationBean destination) {
		if (destination == null || destination.getJmxObjectName() == null) {
			throw new IllegalArgumentException("JMSDestinationBean == null or has null ObjectName");
		}
		
		MBeanServerConnection conn = JMXConnectionManager.getJMXConnection(username, password, hostAndPort);
		
		try {
			conn.invoke(destination.getJmxObjectName(), "pauseConsumption", null, null);
		} catch (InstanceNotFoundException e) {
			throw new IllegalStateException("Encountered InstanceNotFoundException: " + destination.getJmxObjectName(), e);
		} catch (MBeanException e) {
			throw new IllegalStateException("Encountered MBeanException" , e);
		} catch (ReflectionException e) {
			throw new IllegalStateException("Encountered ReflectionException" , e);
		} catch (IOException e) {
			throw new IllegalStateException("Encountered IOException" , e);
		}
	}

	@Override
	public void resumeDestinationConsumption(JMSDestinationBean destination) {
		if (destination == null || destination.getJmxObjectName() == null) {
			throw new IllegalArgumentException("JMSDestinationBean == null or has null ObjectName");
		}
		
		MBeanServerConnection conn = JMXConnectionManager.getJMXConnection(username, password, hostAndPort);
		
		try {
			conn.invoke(destination.getJmxObjectName(), "resumeConsumption", null, null);
		} catch (InstanceNotFoundException e) {
			throw new IllegalStateException("Encountered InstanceNotFoundException: " + destination.getJmxObjectName(), e);
		} catch (MBeanException e) {
			throw new IllegalStateException("Encountered MBeanException" , e);
		} catch (ReflectionException e) {
			throw new IllegalStateException("Encountered ReflectionException" , e);
		} catch (IOException e) {
			throw new IllegalStateException("Encountered IOException" , e);
		}
	}
	
	
}
