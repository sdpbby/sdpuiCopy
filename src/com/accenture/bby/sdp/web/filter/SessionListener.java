package com.accenture.bby.sdp.web.filter;

import java.util.Date;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SessionListener implements HttpSessionListener {

	private static final Logger logger = Logger.getLogger(SessionListener.class.getName());
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		logger.log(Level.DEBUG, "Current Session created : " + event.getSession().getId() + " at " + new Date());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		logger.log(Level.DEBUG, "Current Session destroyed : " + event.getSession().getId() + " Logging out user...");
	}
	
}
