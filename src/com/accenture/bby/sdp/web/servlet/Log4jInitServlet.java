package com.accenture.bby.sdp.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Log4jInitServlet extends HttpServlet {
	private final static Logger logger = Logger.getLogger(Log4jInitServlet.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1408169745027205629L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		System.out.println("Log4jInitServlet is initializing log4j");
		String log4jLocation = config.getInitParameter("log4j-properties-location");
		
		if (log4jLocation == null) {
			System.err.println("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else {
			File file = new File(log4jLocation);
			if (file.exists()) {
				System.out.println("Initializing log4j with: " + log4jLocation);
				PropertyConfigurator.configure(log4jLocation);
			} else {
				System.err.println("*** " + log4jLocation + " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		logger.log(Level.INFO, "Log4j initialized.");
		super.init(config);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getRemoteUser();
		if (userName == null) {
			userName = "UNKNOWN";
		}
		if (request.isUserInRole("Monitor")) {
			logger.log(Level.INFO, "User [" + userName + "] attempted access to Log4jInitServlet.");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<h1>This is the Log4jInitServlet</h1>");
			String logLevel = request.getParameter("logLevel");
			String reloadPropertiesFile = request.getParameter("reloadPropertiesFile");
			if (logLevel != null) {
				setLogLevelWithParameter(out, logLevel);
			} else if (reloadPropertiesFile != null) {
				loadLog4jPropertiesFile(out);
			} else {
				out.println("<p>No logLevel or reloadPropertiesFile parameters were found</p>");
			}
			out.println("<h2>Log level currently set to " + Logger.getRootLogger().getLevel() + "<h2>");
		} else {
			logger.log(Level.WARN, "Unauthorized user [" + userName + "] attempted to access Log4jInitServlet. Returning 404 response instead.");
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		}
	}
	
	private void setLogLevelWithParameter(PrintWriter out, String logLevel) {
		Logger root = Logger.getRootLogger();
		boolean logLevelRecognized = true;
		if ("DEBUG".equalsIgnoreCase(logLevel)) {
			root.setLevel(Level.DEBUG);
		} else if ("INFO".equalsIgnoreCase(logLevel)) {
			root.setLevel(Level.INFO);
		} else if ("WARN".equalsIgnoreCase(logLevel)) {
			root.setLevel(Level.WARN);
		} else if ("ERROR".equalsIgnoreCase(logLevel)) {
			root.setLevel(Level.ERROR);
		} else if ("FATAL".equalsIgnoreCase(logLevel)) {
			root.setLevel(Level.FATAL);
		} else {
			logLevelRecognized = false;
		}
		
		if (logLevelRecognized) {
			out.println("<p>Setting log level using logLevel parameter: <strong>" + logLevel + "</strong></p>");
			logger.log(Level.INFO, "Log level has been set using logLevel parameter: " + logLevel);
		} else {
			out.println("<p>logLevel parameter '" + logLevel + "' level not recognized</p>");
			logger.log(Level.ERROR, "logLevel parameter '" + logLevel + "' level not recognized. No action taken.");
		}
	}
	
	private void loadLog4jPropertiesFile(PrintWriter out) {
		String log4jLocation = getInitParameter("log4j-properties-location");
		if (log4jLocation == null) {
			out.println("<p>*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator</p>");
			logger.log(Level.ERROR, "No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else {
			File file = new File(log4jLocation);
			if (file.exists()) {
				out.println("<p>Initializing log4j with: " + log4jLocation + "</p>");
				logger.log(Level.INFO, "Initializing log4j with: " + log4jLocation);
				PropertyConfigurator.configure(log4jLocation);
			} else {
				out.println("<p>*** " + log4jLocation + " file not found, so initializing log4j with BasicConfigurator</p>");
				logger.log(Level.ERROR, log4jLocation + " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		logger.log(Level.INFO, "Log4j properties updated.");
	}
}
