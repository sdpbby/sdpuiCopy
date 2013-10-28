package com.accenture.bby.sdp.web.handlers.monitoring;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.weblogic.JMSDestinationBean;
import com.accenture.bby.sdp.utl.weblogic.JMSDestinationBeanGroup;
import com.accenture.bby.sdp.utl.weblogic.ServerStatusBean;
import com.accenture.bby.sdp.utl.weblogic.WeblogicMonitoringDAO;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;
import com.accenture.common.configurator.ConfigManager;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ManagedBean (name="queueMonitoringHandler")
@ViewScoped
public class QueueMonitoringHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5269092265416659256L;
	
	private static final Logger logger = Logger.getLogger(QueueMonitoringHandler.class.getName());
	
	public QueueMonitoringHandler() {
		logger.log(Level.DEBUG, "Initialized.");
	}
	
	// managed beans
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	// string constants
	public static final String ENVIRONMENT_PROP_NAME = "sdp.OpsUI.deploymentEnvironment";
	
	// environment variables
	private static String workingEnvironmentName = SdpConfigProperties.WORKING_ENVIRONMENT_NAME;
		
	// querying constants.
	String module;
	
	// backing bean values
	private String jmxUsername;
	private String jmxPassword;
	private QueueMonitoringTabHandler tab = new QueueMonitoringTabHandler();
	
	
	public QueueMonitoringTabHandler getTab() {
		return tab;
	}

	public void setTab(QueueMonitoringTabHandler tab) {
		this.tab = tab;
	}

	private List<JMSDestinationBeanGroup> jmsDestinationGroups;
	private List<ServerStatusBean> serverStatusBeans;
	private HtmlDataTable destinationGroupTable;
	private HtmlDataTable destinationTable;
	
	private WeblogicMonitoringDAO dao;
	
	// string constants
	private static final String JMX_HOSTANDPORT = "sdp.sdpconsole.jmx.hostandport.sdp.";
	
	public String prepareJmxSdpSbPage() throws MalformedURLException, IOException {
		logger.log(Level.DEBUG, "Preparing sdp-sb jmx page.");
		try {
			module = "BBY_SDPModule";
			
			String hostAndPortProperty = ConfigManager.getProperty(JMX_HOSTANDPORT + workingEnvironmentName);
			
			if (hostAndPortProperty == null) {
				throw new MissingPropertyException("property: " + JMX_HOSTANDPORT
						+ workingEnvironmentName + " not found in in sdp config properties");
			}
			
			dao = WeblogicMonitoringDAO.getSdpInstance(jmxUsername, jmxPassword, hostAndPortProperty);
			logger.log(Level.DEBUG, "Accessing weblogic....");
			return prepareJmxPage();
		} catch (Throwable e) {
			exceptionHandler.initialize(e, "Failed to prepare SDP jmx page.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String prepareJmxPage() {
		logger.log(Level.DEBUG, "Preparing jmx page...");
		try {
			serverStatusBeans = dao.getServerStatus();
			
			List<JMSDestinationBean> unsortedDestbeans = dao.getJmsDestinationsByModuleName(module);
			
			// sort results into JMSDestinationGroups named by Module name for
			// display
			jmsDestinationGroups = new ArrayList<JMSDestinationBeanGroup>();
			
			for (JMSDestinationBean currentBean : unsortedDestbeans) {
				
				boolean currentBeanHasGroup = false;
				
				for (JMSDestinationBeanGroup currentGroup : jmsDestinationGroups) {
					if (currentGroup.getGroupName().equals(currentBean.getDestinationName())) {
						currentGroup.getJmsDestinationBeans().add(currentBean);
						currentBeanHasGroup = true;
					}
				}
				
				if (!currentBeanHasGroup) {
					
					JMSDestinationBeanGroup newBeanGroup = new JMSDestinationBeanGroup();
					newBeanGroup.setGroupName(currentBean.getDestinationName());
					
					List<JMSDestinationBean> newBeanList = new ArrayList<JMSDestinationBean>();
					newBeanList.add(currentBean);
					newBeanGroup.setJmsDestinationBeans(newBeanList);
					
					jmsDestinationGroups.add(newBeanGroup);
				}
			}
			
			Collections.sort(jmsDestinationGroups);
			
			for (JMSDestinationBeanGroup group : jmsDestinationGroups) {
				Collections.sort(group.getJmsDestinationBeans());
			}
			tab.displayQueuesTab();
			logger.log(Level.DEBUG, "JMX Preparation completed.");
			return NavigationStrings.CURRENT_VIEW;
		} catch (Throwable e) {
			exceptionHandler.initialize(e, "Got DAO instance but unable to prepare JMX page.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String pauseSelectedQueue() {
		try {
			logger.log(Level.INFO, "Preparing to pause queue!");
			JMSDestinationBean jms = (JMSDestinationBean)destinationTable.getRowData();
			dao.pauseDestinationConsumption(jms);
			prepareJmxPage();
			logger.log(Level.INFO, "Queue paused! >> " + jms.getDestinationName());
			AuditUtil.audit(Action.MONITORING_PAUSED_QUEUE_CONSUMPTION);
			return NavigationStrings.CURRENT_VIEW;
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (Throwable e) {
			exceptionHandler.initialize(e, "Failure when pausing selection!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	public String resumeSelectedQueue() throws MalformedURLException, IOException {
		try {
			logger.log(Level.INFO, "Preparing to resume queue!");
			JMSDestinationBean jms = (JMSDestinationBean)destinationTable.getRowData();
			dao.resumeDestinationConsumption(jms);
			prepareJmxPage();
			logger.log(Level.INFO, "Queue paused! >> " + jms.getDestinationName());
			AuditUtil.audit(Action.MONITORING_RESUMED_QUEUE_CONSUMPTION);
			return NavigationStrings.CURRENT_VIEW;
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (Throwable e) {
			exceptionHandler.initialize(e, "Failure when resuming selection!");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getJmxUsername() {
		return jmxUsername;
	}

	public void setJmxUsername(String jmxUsername) {
		this.jmxUsername = jmxUsername;
	}

	public String getJmxPassword() {
		return jmxPassword;
	}

	public void setJmxPassword(String jmxPassword) {
		this.jmxPassword = jmxPassword;
	}

	public List<JMSDestinationBeanGroup> getJmsDestinationGroups() {
		return jmsDestinationGroups;
	}

	public void setJmsDestinationGroups(List<JMSDestinationBeanGroup> jmsDestinationGroups) {
		this.jmsDestinationGroups = jmsDestinationGroups;
	}

	public List<ServerStatusBean> getServerStatusBeans() {
		return serverStatusBeans;
	}

	public void setServerStatusBeans(List<ServerStatusBean> serverStatusBeans) {
		this.serverStatusBeans = serverStatusBeans;
	}

	public HtmlDataTable getDestinationGroupTable() {
		return destinationGroupTable;
	}

	public void setDestinationGroupTable(HtmlDataTable destinationGroupTable) {
		this.destinationGroupTable = destinationGroupTable;
	}

	public HtmlDataTable getDestinationTable() {
		return destinationTable;
	}

	public void setDestinationTable(HtmlDataTable destinationTable) {
		this.destinationTable = destinationTable;
	}
	
	
}
