package com.accenture.bby.sdp.web.handlers.logging;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name = "opsLoggerHandler")
public class OpsLoggerHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8946526446428666441L;
	
	public String getCurrentLogLevel() {
		return Logger.getRootLogger().getLevel().toString();
	}
}
