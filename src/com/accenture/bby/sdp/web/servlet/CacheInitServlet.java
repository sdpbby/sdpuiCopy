package com.accenture.bby.sdp.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.web.handlers.DropdownListManager;
import com.accenture.common.configurator.ConfigManager;

public class CacheInitServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 116497615952630295L;
	
	private final static Logger logger = Logger.getLogger(CacheInitServlet.class.getName());
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.log(Level.INFO, "========= OpsUI cache initializing. =========");
		
		logger.log(Level.INFO, "Initializing SDP-Config properties.");
		SdpConfigProperties.initialize();
		SdpConfigProperties.loadSdpConfigProperties();
		logger.log(Level.INFO, "SDP-Config initialized. " + ConfigManager.getProperties().size() + " properties found.");
		
		logger.log(Level.DEBUG, "Initializing vendor map and status map.");
		Constants.refreshVendorMap();
		Constants.refreshStatusMap();
		
		logger.log(Level.INFO, "Initializing drop down list cache.");
		DropdownListManager.refreshStaticDropdownLists();
		
		logger.log(Level.INFO, "========= OpsUI cache initialized. =========");
		
		super.init(config);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getRemoteUser();
		if (userName == null) {
			userName = "UNKNOWN";
		}
		if (request.isUserInRole("Monitor")) {
			logger.log(Level.INFO, "User [" + userName + "] attempted access to CacheInitServlet.");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<h1>This is the CacheInitServlet</h1>");
			
			logger.log(Level.INFO, "========= OpsUI cache initializing. =========");
			
			logger.log(Level.DEBUG, "Resetting sdp-config properties.");
			SdpConfigProperties.loadSdpConfigProperties();
			
			logger.log(Level.DEBUG, "Resetting vendor map and status map.");
			Constants.refreshVendorMap();
			Constants.refreshStatusMap();
			
			logger.log(Level.DEBUG, "Resetting drop down list cache.");
			DropdownListManager.refreshStaticDropdownLists();
			
			logger.log(Level.INFO, "========= OpsUI cache initialized. =========");
			
			out.println("<p>Cache reset complete.</p>");			
		} else {
			logger.log(Level.WARN, "Unauthorized user [" + userName + "] attempted to access CacheInitServlet. Returning 404 response instead.");
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		}
	}
}
