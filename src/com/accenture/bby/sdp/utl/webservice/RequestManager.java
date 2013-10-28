package com.accenture.bby.sdp.utl.webservice;

import icdm.bby.sdp.com.accenture.xml.FulfillmentStub;

import java.io.File;
import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import serviceresult.bby.sdp.com.accenture.xml.ServiceResultDocument;
import socommonheader.bby.sdp.com.accenture.xml.CommonHeaderDocument.CommonHeader;
import soheader.bby.sdp.com.accenture.xml.HeaderDocument;

import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.audit.TransactionLog;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.utl.xml.SdpTransactionDataXml;
import com.accenture.bby.sdp.utl.xml.ServiceResultWrapper;
import com.accenture.bby.sdp.utl.xml.XmlSchemaValidator;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.common.configurator.ConfigManager;
import com.bestbuy.schemas.sdp.adjustactivation.AdjustActivationRequestDocument;
import com.bestbuy.schemas.sdp.adjustactivation.AdjustActivationResponseDocument;
import com.bestbuy.schemas.sdp.cancelfulfillment.CancelFulfillmentRequestDocument;
import com.bestbuy.schemas.sdp.cancelfulfillment.CancelFulfillmentResponseDocument;
import com.bestbuy.schemas.sdp.fulfillactivation.FulfillActivationRequestDocument;
import com.bestbuy.schemas.sdp.fulfillactivation.FulfillActivationResponseDocument;
import com.bestbuy.schemas.sdp.reactivatefulfillment.ReActivateFulfillmentRequestDocument;
import com.bestbuy.schemas.sdp.reactivatefulfillment.ReActivateFulfillmentResponseDocument;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceRequestDocument;
import com.bestbuy.sdp.bcs.csm.bcs_sendemail.SendEmailServiceResponseDocument;
import com.bestbuy.sdp.bcs.csm.wsdl_bcs_sendemail.SoapBindingQSServiceStub;
import com.bestbuy.sdp.tpa.vendorprovisioningresponse.VendorProvisioningResponseDocument;
import com.bestbuy.www.sdp.tpa.wsdl_tpa_services.TpaProvisioningRequestDocument;
import com.bestbuy.www.sdp.tpa.wsdl_tpa_services.TPASynchronousServicesStub;

/**
 * 
 * This class performs all web service interaction, abstracting the
 * implementation of this functionality from the rest of the application.
 * 
 * @author a379562
 * 
 */
public class RequestManager {

	private static String trustStoreLocationPropPrefix = "sdp.OpsUI.trustStoreLoc.";
	private static String bsSendEmailURLPropPrefix = "sdp.OpsUI.sdp-sb.bsSendEmail.";
	private static String fulfillmentAsynchPrefix = "sdp.OpsUI.sdp-sb.asyncFulfillmentServicesUrl.";
	private static String synchronousVendorPrefix = "sdp.OpsUI.sdp-sb.sendTpaUrl.";

	private static final Logger logger = Logger.getLogger(RequestManager.class.getName());
	
	private static RequestManager uniqueManager = null;

	/**
	 * Singleton factory constructor.
	 * @throws MissingPropertyException 
	 */
	public static RequestManager getInstance() throws MissingPropertyException {
		if (uniqueManager == null) {
			uniqueManager = new RequestManager();
		}

		return uniqueManager;
	}

	/**
	 * <p>
	 * This private constructor will throw an exception if the trust store
	 * cannot be found on this server at the designated location.
	 * </p>
	 * <p>
	 * This file must be present for SSL handshakes to take place properly when
	 * sending requests.
	 * </p>
	 * @throws MissingPropertyException 
	 */
	private RequestManager() throws MissingPropertyException {
		String trustStoreLoc = ConfigManager.getProperty(trustStoreLocationPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (trustStoreLoc == null || trustStoreLoc.length() == 0) {
			throw new MissingPropertyException("property: " + trustStoreLocationPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}

		if (!new File(trustStoreLoc).exists()) {
			throw new MissingPropertyException("cannot find trust store at path: " + trustStoreLoc + System.getProperty("line.separator") + "path is relative: " + !new File(trustStoreLoc).isAbsolute());
		}
		System.setProperty("javax.net.ssl.trustStore", trustStoreLoc);
	}


	/**
	 * @param currentRequestData
	 * @param originalRequestData
	 * @throws WebServiceCallFailedException if web service call fails
	 * @throws AuditTrailException if failure occurs when inserting audit trail log
	 * @throws NullPointerException if input param is null
	 * @throws IllegalStateException if web service url is not found in sdp-config
	 */
	public SendEmailServiceResponseDocument sendEmailRequest(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {

		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.COMMSAT_REFLOW_EMAIL, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(bsSendEmailURLPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: "
					+ bsSendEmailURLPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME
					+ " not found in database");
		}
		
		// create the header
		HeaderDocument header = HeaderDocument.Factory.newInstance();
		
		CommonHeader commonHeader = header.addNewHeader().addNewCommonHeader();
		
		commonHeader.setServiceLabel("BCS_SENDEMAIL");
		commonHeader.setServiceID(currentRequestData.getSdpId() != null ? currentRequestData.getSdpId() : UUID.randomUUID().toString());
		
		// create and log the request
		SendEmailServiceRequestDocument requestDoc = SdpTransactionDataXml.getCommsatRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + header.toString() + "\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			SoapBindingQSServiceStub stub = new SoapBindingQSServiceStub(webServiceUrl);
			SendEmailServiceResponseDocument responseDoc = stub.sendEmail(requestDoc, header);
			
			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getCommsatRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			return responseDoc;
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl +"]\n" + SdpTransactionDataXml.getCommsatRequest(currentRequestData).toString(), e);
		}
	}
	
	/**
	 * @param currentRequestData
	 * @param originalRequestData
	 * @throws WebServiceCallFailedException if web service call fails
	 * @throws AuditTrailException if failure occurs when inserting audit trail log
	 * @throws NullPointerException if input param is null
	 * @throws IllegalStateException if web service url is not found in sdp-config
	 */
	public SendEmailServiceResponseDocument sendEmailRequestECC(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {
		
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.ECC_RESEND_EMAIL, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(bsSendEmailURLPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: "
					+ bsSendEmailURLPropPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME
					+ " not found in database");
		}
		
		// create the header
		HeaderDocument header = HeaderDocument.Factory.newInstance();
		
		CommonHeader commonHeader = header.addNewHeader().addNewCommonHeader();
		
		commonHeader.setServiceLabel("BCS_SENDEMAIL");
		commonHeader.setServiceID(currentRequestData.getSdpId() != null ? currentRequestData.getSdpId() : UUID.randomUUID().toString());
		
		// create and log the request
		SendEmailServiceRequestDocument requestDoc = SdpTransactionDataXml.getCommsatRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + header.toString() + "\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			SoapBindingQSServiceStub stub = new SoapBindingQSServiceStub(webServiceUrl);
			SendEmailServiceResponseDocument responseDoc = stub.sendEmail(requestDoc, header);
			
			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getCommsatRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			
			return responseDoc;
			
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl +"]\n" + SdpTransactionDataXml.getCommsatRequest(currentRequestData).toString(), e);
		}
	}

	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 */
	public void sendFulfillActivationRequestToSDP(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.SDP_NEW_ACTIVATION, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		FulfillActivationRequestDocument requestDoc = SdpTransactionDataXml.getFulfillActivationRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			FulfillmentStub stub = new FulfillmentStub(webServiceUrl);
			FulfillActivationResponseDocument responseDoc = stub.fulfillActivation(requestDoc);
			
			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getFulfillActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}

	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 */
	public void sendCancelFulfillmentRequestToSDP(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.SDP_NEW_CANCELLATION, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		CancelFulfillmentRequestDocument requestDoc = SdpTransactionDataXml.getCancelFulfillmentRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			FulfillmentStub stub = new FulfillmentStub(webServiceUrl);
			CancelFulfillmentResponseDocument responseDoc =  stub.cancelFulfillment(requestDoc);
			
			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getCancelFulfillmentRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}

	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException 
	 * @throws AuditTrailException 
	 * @throws RemoteException
	 */
	public void sendReActivateFulfillmentRequestToSDP(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.SDP_NEW_REACTIVATION, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		ReActivateFulfillmentRequestDocument requestDoc = SdpTransactionDataXml.getReactivateFulfillmentRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			FulfillmentStub stub = new FulfillmentStub(webServiceUrl);
			ReActivateFulfillmentResponseDocument responseDoc =  stub.reActivateFulfillment(requestDoc);

			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getReactivateFulfillmentRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 */
	public void sendAdjustActivationRequestToSDP(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.SDP_NEW_ADJUSTACTIVATION, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + fulfillmentAsynchPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		AdjustActivationRequestDocument requestDoc = SdpTransactionDataXml.getAdjustActivationRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			FulfillmentStub stub = new FulfillmentStub(webServiceUrl);
			AdjustActivationResponseDocument responseDoc = stub.adjustActivation(requestDoc);

			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getAdjustActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 * @throws XmlException 
	 */
	public ServiceResultWrapper sendDirectToVendorActivateRequest(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException, XmlException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.VENDOR_NEW_ACTIVATION_OVERRIDE, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		TpaProvisioningRequestDocument requestDoc = SdpTransactionDataXml.getVendorActivateRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			TPASynchronousServicesStub stub = new TPASynchronousServicesStub(webServiceUrl);
			VendorProvisioningResponseDocument responseDoc = stub.vendorSynchronousOperation(requestDoc);

			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getAdjustActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			if (responseDoc != null && responseDoc.getVendorProvisioningResponse() != null && responseDoc.getVendorProvisioningResponse().getServiceResult() != null) {
				ServiceResultDocument doc = ServiceResultDocument.Factory.newInstance();
				doc.setServiceResult(responseDoc.getVendorProvisioningResponse().getServiceResult());
				return new ServiceResultWrapper(doc);
			} else {
				return null;
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 * @throws XmlException 
	 */
	public ServiceResultWrapper sendDirectToVendorCancelRequest(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException, XmlException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.VENDOR_NEW_CANCELLATION_OVERRIDE, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		TpaProvisioningRequestDocument requestDoc = SdpTransactionDataXml.getVendorCancelRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			TPASynchronousServicesStub stub = new TPASynchronousServicesStub(webServiceUrl);
			VendorProvisioningResponseDocument responseDoc = stub.vendorSynchronousOperation(requestDoc);


			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getAdjustActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			if (responseDoc != null && responseDoc.getVendorProvisioningResponse() != null && responseDoc.getVendorProvisioningResponse().getServiceResult() != null) {
				ServiceResultDocument doc = ServiceResultDocument.Factory.newInstance();
				doc.setServiceResult(responseDoc.getVendorProvisioningResponse().getServiceResult());
				return new ServiceResultWrapper(doc);
			} else {
				return null;
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 * @throws XmlException 
	 */
	public ServiceResultWrapper sendDirectToVendorRenewRequest(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException, XmlException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.VENDOR_NEW_RENEWAL_OVERRIDE, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		TpaProvisioningRequestDocument requestDoc = SdpTransactionDataXml.getVendorRenewRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			TPASynchronousServicesStub stub = new TPASynchronousServicesStub(webServiceUrl);
			VendorProvisioningResponseDocument responseDoc = stub.vendorSynchronousOperation(requestDoc);


			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getAdjustActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			if (responseDoc != null && responseDoc.getVendorProvisioningResponse() != null && responseDoc.getVendorProvisioningResponse().getServiceResult() != null) {
				ServiceResultDocument doc = ServiceResultDocument.Factory.newInstance();
				doc.setServiceResult(responseDoc.getVendorProvisioningResponse().getServiceResult());
				return new ServiceResultWrapper(doc);
			} else {
				return null;
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
	
	/**
	 * <p>
	 * Submits a request to SDP.
	 * </p>
	 * 
	 * @param request
	 *            the document to be sent to SDP
	 * @return the response document
	 * @throws WebServiceCallFailedException
	 * @throws AuditTrailException 
	 * @throws XmlException 
	 */
	public ServiceResultWrapper sendDirectToVendorUpdateStatusRequest(SdpTransactionDataBean currentRequestData, SdpTransactionDataBean originalRequestData) throws WebServiceCallFailedException, AuditTrailException, XmlException {
		if (currentRequestData == null) {
			throw new NullPointerException("Input data was null. SOAP request will not be sent.");
		}
		
		// capture Audit Log
		TransactionLog log;
		try {
			log = AuditUtil.audit(Action.VENDOR_NEW_MODIFY_STATUS_OVERRIDE, currentRequestData, originalRequestData);
		} catch (DataAccessException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		} catch (DataSourceLookupException e) {
			throw new AuditTrailException("Failed to capture form data in audit trial. The request has not been submitted.", e);
		}
		
		// retrieve url from sdp-config
		String webServiceUrl = ConfigManager.getProperty(synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME);

		if (webServiceUrl == null || webServiceUrl.length() == 0) {
			throw new IllegalStateException("property: " + synchronousVendorPrefix + SdpConfigProperties.WORKING_ENVIRONMENT_NAME + " not found in database");
		}
		
		// create and log the request
		TpaProvisioningRequestDocument requestDoc = SdpTransactionDataXml.getVendorModifyStatusRequest(currentRequestData);
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Sending request:\n" + requestDoc.toString());
			logger.log(Level.INFO, "URL: " + webServiceUrl);
		}
		
		
		try {
			// send the request
			TPASynchronousServicesStub stub = new TPASynchronousServicesStub(webServiceUrl);
			VendorProvisioningResponseDocument responseDoc = stub.vendorSynchronousOperation(requestDoc);


			// validate and log the response
			if (SdpConfigProperties.isEnabledSchemaValidation() && !responseDoc.validate()) {
				logger.log(Level.WARN, "Received response with schema errors:\n" + XmlSchemaValidator.getSchemaErrors(responseDoc) + "\n" + responseDoc.toString());
			} else if (logger.isInfoEnabled()) {
				logger.log(Level.INFO, "Received response:\n" + responseDoc.toString());
			}
			
			// capture request response audit log
			try {
				if (originalRequestData != null) {
					AuditUtil.audit(log, requestDoc, responseDoc, SdpTransactionDataXml.getAdjustActivationRequest(originalRequestData));
				} else {
					AuditUtil.audit(log, requestDoc, responseDoc);
				}
			} catch (DataSourceLookupException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			} catch (DataAccessException e) {
				throw new AuditTrailException("The request was submitted but failed to capture request response in audit trail.", e);
			}
			if (responseDoc != null && responseDoc.getVendorProvisioningResponse() != null && responseDoc.getVendorProvisioningResponse().getServiceResult() != null) {
				ServiceResultDocument doc = ServiceResultDocument.Factory.newInstance();
				doc.setServiceResult(responseDoc.getVendorProvisioningResponse().getServiceResult());
				return new ServiceResultWrapper(doc);
			} else {
				return null;
			}
		} catch (RemoteException e) {
			throw new WebServiceCallFailedException("Failed to send request to [" + webServiceUrl + "]\n" + requestDoc.toString(), e);
		}
	}
}
