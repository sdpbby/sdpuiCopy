package com.accenture.bby.sdp.web.phaselistener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class NoCachePhaseListener implements PhaseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3594105519496279667L;

	private static final Logger logger = Logger.getLogger(NoCachePhaseListener.class.getName());
	
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	@Override
	public void afterPhase(PhaseEvent event) {
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Cache-Control", "must-revalidate");
		response.addHeader("Cache-Control", "private");
		response.addHeader("Cache-Control", "post-check=0");
		response.addHeader("Cache-Control", "pre-check=0");
		// some date in the past
		response.addDateHeader("Expires", -1);
		if (logger.isTraceEnabled()) {
			logger.log(Level.TRACE, "Pragma: no-cache");
			logger.log(Level.TRACE, "Cache-Control: no-cache");
			logger.log(Level.TRACE, "Cache-Control: no-store");
			logger.log(Level.TRACE, "Cache-Control: must-revalidate");
			logger.log(Level.TRACE, "Cache-Control: private");
			logger.log(Level.TRACE, "Cache-Control: post-check=0");
			logger.log(Level.TRACE, "Cache-Control: pre-check=0");
			logger.log(Level.TRACE, "Expires: -1");
		}
	}
}
