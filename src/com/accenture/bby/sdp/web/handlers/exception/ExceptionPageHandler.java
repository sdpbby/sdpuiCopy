package com.accenture.bby.sdp.web.handlers.exception;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.ExceptionPageDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.ExceptionResultBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;

@ManagedBean (name="exceptionPageHandler")
@ViewScoped

public class ExceptionPageHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ExceptionPageHandler.class.getName());
	
	public static final String REQUEST_PARAM = "exceptionId";

	private ExceptionResultBean data;
//	private ExceptionPageDatatable exceptionPageDatatable;
	private String exceptionId;
	
	
//	
//	/*
//	 * Managed Properties
//	 */
//	@ManagedProperty (value="#{exceptionPageTabbedPaneHandler}")
//	private ExceptionPageTabbedPaneHandler exceptionPageTabbedPaneHandler;
////	@ManagedProperty (value="#{exceptionPageDatatable}")
////	private ExceptionPageDatatable exceptionPageDatatable;
//	
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	
	/*
	 * getters
	 */
//	public ExceptionPageTabbedPaneHandler getExceptionPageTabbedPaneHandler() {
//		if (exceptionPageTabbedPaneHandler == null) {
//			exceptionPageTabbedPaneHandler = new ExceptionPageTabbedPaneHandler();
//		}
//		return exceptionPageTabbedPaneHandler;
//	}
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
//
//	public ExceptionPageDatatable getExceptionPageDatatable() throws DataSourceLookupException, DataAccessException {
//		if (exceptionPageDatatable == null) {
//			System.out.println("\n\nExceptionPageDatatable is null\n\n");
//			exceptionPageDatatable = new ExceptionPageDatatable();
//		}
//		return exceptionPageDatatable;
//	}
	
	/*
	 * setters
	 */
//	public void setExceptionPageTabbedPaneHandler(final ExceptionPageTabbedPaneHandler exceptionPageTabbedPaneHandler) { this.exceptionPageTabbedPaneHandler = exceptionPageTabbedPaneHandler; }
	public void setExceptionHandler(final ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(final UserManager userManager) { this.userManager = userManager; }
//	public void setExceptionPageDatatable(
//			final ExceptionPageDatatable exceptionPageDatatable) {
//		this.exceptionPageDatatable = exceptionPageDatatable;
//	}

	
	public ExceptionPageHandler(){
		// get the exceptionId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.exceptionId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		
		// if exceptionId not found in context param, check the session.
		// if found in the context then store the exceptionId in the session for later use.
		if (this.exceptionId == null) {
			logger.log(Level.DEBUG, "ExceptionId variable not found in context param. checking session map.");
			this.exceptionId = (String)facesContext.getExternalContext().getSessionMap().get(REQUEST_PARAM);
		} else {
			logger.log(Level.DEBUG, "vpsId variable found in context param. storing in session map.");
			facesContext.getExternalContext().getSessionMap().put(REQUEST_PARAM, exceptionId);
		}
		
		logger.log(Level.DEBUG, "vpsId param passed=" + exceptionId);
	}
	
	public ExceptionResultBean getData() {
		if (data == null) {
			logger.log(Level.DEBUG, "BEGIN Retrieving exceptionId=" + exceptionId);
			
			if (exceptionId == null) {
				throw new IllegalArgumentException("Invalid exceptionId: " + exceptionId);
			}
			
			try {
				data = ExceptionPageDBWrapper.getSdpExceptionDetailByExceptionId(exceptionId);
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to retrieve exception details from the database with exceptionId=" + exceptionId);
				throw new IllegalStateException("Failed to retrieve exception details from the database with exceptionId=" + exceptionId);
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to data source.");
				throw new IllegalStateException("Failed to connect to data source.");
			}
			
			if (data == null) {
				logger.log(Level.DEBUG, "exceptionId search returned null.");
				throw new IllegalArgumentException("Invalid exception ID: " + exceptionId);
			}
			
			logger.log(Level.DEBUG, "FINISH Retrieving Exception ID=" + exceptionId);
		}
		return data;
	}

	public void setData(ExceptionResultBean data) {
		if (data == null) {
			data = new ExceptionResultBean();
		} else {
			this.data = data;
		}
	}
	
	public String viewOrderDetailButtonClick() {
		if (this.getData() != null) {
			SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
			sdpOrderDetailHandler.viewOrderDetailButtonClick(this.getData().getSdpOrderId());
			return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
		} else {
			logger.log(Level.DEBUG, "selectedRow returned null so "
					+ SdpOrderDetailHandler.REQUEST_PARAM
					+ " param will not be set.");
			return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
		}
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.getData().getSdpOrderId());
	}
}
