package com.accenture.bby.sdp.web.handlers.sdpOrderDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import serviceresult.bby.sdp.com.accenture.xml.ServiceResultDocument.ServiceResult;

import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.db.SdpTransactionDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.utl.webservice.RequestManager;
import com.accenture.bby.sdp.web.beans.CommsatHistoryBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceResponseDocument;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class CommsatHistoryDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5779933943078004225L;

	private static final Logger logger = Logger.getLogger(CommsatHistoryDatatable.class.getName());
	
	private String sdpOrderId;
	
	private boolean resubmitAllowed = false;
	
	private boolean resubmitFormRendered = false;
	

	public boolean isResubmitAllowed() {
		return resubmitAllowed;
	}

	public void setResubmitAllowed(boolean resubmitAllowed) {
		this.resubmitAllowed = resubmitAllowed;
	}

	public boolean isResubmitFormRendered() {
		return resubmitFormRendered;
	}

	public void setResubmitFormRendered(boolean resubmitFormRendered) {
		this.resubmitFormRendered = resubmitFormRendered;
	}

	private SdpTransactionDataBean currentRequest = new SdpTransactionDataBean();
	private SdpTransactionDataBean originalRequest = null;
	
	public SdpTransactionDataBean getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(SdpTransactionDataBean currentRequest) {
		this.currentRequest = currentRequest;
	}

	private boolean hide;
	public boolean isHide() { return hide; }
	
	public String toggleHide() { 
		hide = !hide; 
		logger.log(Level.DEBUG, "hide=" + hide); 
		return NavigationStrings.CURRENT_VIEW; 
	}
	
	public String getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}
	
	public CommsatHistoryDatatable(String sdpOrderId) throws DataAccessException, DataSourceLookupException {
		this.sdpOrderId = sdpOrderId;
		if (isTableEmpty() && !hide) {
			rows = SdpOrderDetailDBWrapper.getCommsatHistoryBySdpOrderId(this.sdpOrderId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<CommsatHistoryBean>(0);
		}
		if (!isTableEmpty()) {
			this.resubmitAllowed = true;
		}
		logger.log(Level.DEBUG, "Commsat records found >> " + rows.size());
	}
	
	
	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<CommsatHistoryBean> rows;
	
	public List<CommsatHistoryBean> getRows() { 
		return rows; 
	}
	
	public void setRows(List<CommsatHistoryBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
	
	String resubmitInit(SdpTransactionDataBean requestData) {
		resubmitFormRendered = true;
		this.currentRequest = new SdpTransactionDataBean(requestData);
		this.originalRequest = new SdpTransactionDataBean(requestData);
		return NavigationStrings.CURRENT_VIEW;
	}
	
	
	String sendRequestNow() throws MissingPropertyException, DataSourceLookupException, DataAccessException, WebServiceCallFailedException, AuditTrailException {
		logger.log(Level.INFO, "BEGIN send Commsat request from Order Detail.");
		
			RequestManager requestManager = RequestManager.getInstance();
			SendEmailServiceResponseDocument responseMessage = requestManager.sendEmailRequest(this.currentRequest, originalRequest);
			
			int statusCode = 1;
			ServiceResult serviceResult = null;
			if (responseMessage != null && responseMessage.getSendEmailServiceResponse() != null) {
				serviceResult = responseMessage.getSendEmailServiceResponse().getServiceResult();
			}
			if (responseMessage != null && responseMessage.getSendEmailServiceResponse() != null && responseMessage.getSendEmailServiceResponse().getServiceResult() != null && responseMessage.getSendEmailServiceResponse().getServiceResult().getStatusCode() != null) {
				statusCode = responseMessage.getSendEmailServiceResponse().getServiceResult().getStatusCode().intValue();
			}
			// update delivery email in sdp database
			if (statusCode == 0) {
				SdpTransactionDBWrapper.setDeliveryEmailByCustomerId(currentRequest.getCustomerId(), currentRequest.getDeliveryEmail());
				logger.log(Level.INFO, "Updated delevery email for CUSTOMERID=[" + currentRequest.getCustomerId() + "]");
			} else {
				throw new IllegalStateException("BCS_SendEmail returned an error: " + serviceResult != null ? serviceResult.toString() : "SERVICERESULT WAS NULL!");
			}
		
		logger.log(Level.INFO, "FINISH send Commsat request from Order Detail.");
		SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
		sdpOrderDetailHandler.viewOrderDetailButtonClick(this.sdpOrderId);
		return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.sdpOrderId);
	}
}
