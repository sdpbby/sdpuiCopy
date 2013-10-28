package com.accenture.bby.sdp.utl.weblogic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

/*
 * resources for JMX development against weblogic 10gr3 servers
 * http://download.oracle.com/docs/cd/E15051_01/wls/docs103/jmx/accessWLS.html
 * http://download.oracle.com/docs/cd/E12840_01/wls/docs103/wlsmbeanref/core/index.html
 * 
 */
public class JMXConnectionManager {

	// configurable settings
	public static final String[] validEnvironments = {"devtest_a", "devtest_b", "prodtest_a", "prodtest_b", "pldr", "prod"};
	
	public static MBeanServerConnection getJMXConnection(String jmxUsername, String jmxPassword, String hostAndPort){
		
		JMXConnector jmxConnector = null;
		
		try {
			
			jmxConnector = getJMXConnector(jmxUsername, jmxPassword, hostAndPort);
			return jmxConnector.getMBeanServerConnection();
			
		} catch (MalformedURLException e) {
			throw new IllegalStateException("encountered " + e.getClass().toString(), e);
		} catch (IOException e) {
			throw new IllegalStateException("encountered " + e.getClass().toString(), e);
		}
	}
	
	private static JMXConnector getJMXConnector(String jmxUsername, String jmxPassword, String hostAndPort) throws MalformedURLException, IOException {
		String[] hostAndPortArray = hostAndPort.split(":");
		
		String jndiRoot = "/jndi/";
		String protocol = "t3";
		String mserver = "weblogic.management.mbeanservers.domainruntime";
		
		JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostAndPortArray[0], Integer.valueOf(hostAndPortArray[1]), jndiRoot + mserver);
		
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		
		h.put(Context.SECURITY_PRINCIPAL, jmxUsername);
		h.put(Context.SECURITY_CREDENTIALS, jmxPassword);
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
		return JMXConnectorFactory.connect(serviceURL, h);
	}
}
