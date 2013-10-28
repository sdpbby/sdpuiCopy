package com.accenture.bby.sdp.web.handlers.eccui;

import java.io.Serializable;
import java.rmi.RemoteException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.xmlbeans.XmlException;

import com.accenture.bby.sdp.db.SdpTransactionDBWrapper;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.utl.webservice.RequestManager;
import com.accenture.bby.sdp.web.beans.ECCUIResultBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.UserManager;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceResponseDocument;

@ViewScoped
@ManagedBean (name="eccuiHandler")
public class ECCUIHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ECCUIHandler.class.getName());
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	
	private ECCUIResultBean newECCUIBean;
	private ECCUIResultBean oldECCUIBean;
	
	/*
	 * getters
	 */
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	public UserManager getUserManager() { return userManager; }
	public ECCUIResultBean getNewECCUIBean() { return newECCUIBean; }
	public ECCUIResultBean getOldECCUIBean() { return oldECCUIBean; }
	
	/*
	 * setters
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }
	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	public void setNewECCUIBean(ECCUIResultBean newECCUIBean) { this.newECCUIBean = newECCUIBean; }
	public void setOldECCUIBean(ECCUIResultBean oldECCUIBean) { this.oldECCUIBean = oldECCUIBean; }
	
	/*
	 * Action Listeners
	 */
	
	
	public void processSendEmailRequestBean(ECCUIResultBean bean) throws DataAccessException, DataSourceLookupException, AuditTrailException, RemoteException, XmlException, MissingPropertyException, WebServiceCallFailedException {
		
		// uncheck the checkbox
		bean.setSelected(false);
		
		SdpTransactionDataBean data = SdpTransactionDBWrapper.getSdpTransactionDataBySdpOrderId(bean.getSdpOrderId());
		SdpTransactionDataBean originalData = new SdpTransactionDataBean(data);
		
		// set delivery email for commsat request
		data.setDeliveryEmail(TextFilter.filter(bean.getDeliveryEmail()));
		
		// send the request and log it
		RequestManager requestManager = RequestManager.getInstance();
		SendEmailServiceResponseDocument responseMassage = requestManager.sendEmailRequestECC(data, originalData);
		bean.setResult(1);
		if (responseMassage != null) {
			bean.setResult(responseMassage.getSendEmailServiceResponse().getServiceResult().getStatusCode().intValue());
		}
		
		// update delivery email in sdp database
		if (bean.isSuccessful()) {
			SdpTransactionDBWrapper.setDeliveryEmailByCustomerId(data.getCustomerId(), data.getDeliveryEmail());
			logger.log(Level.INFO, "Updated delevery email for CUSTOMERID=[" + data.getCustomerId() + "]");
		} else {
			logger.log(Level.WARN, "Customer delivery email not updated. Expected Status Code <0> from BCS_SendEmail but got <" + bean.getResult() + ">");
		}
	}
	

}
