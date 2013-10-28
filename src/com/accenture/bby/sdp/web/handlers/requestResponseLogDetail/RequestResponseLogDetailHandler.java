package com.accenture.bby.sdp.web.handlers.requestResponseLogDetail;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrder;
import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrderDocument;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.xml.SdpTransactionDataXml;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;
import com.accenture.bby.sdp.web.handlers.sdpRequest.SdpRequestHandler;
@ViewScoped
@ManagedBean (name="requestResponseLogDetailHandler")
public class RequestResponseLogDetailHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971505199972158400L;
	public static final String REQUEST_PARAM = "currentRequestResponseLog";
	private static final Logger logger = Logger.getLogger(RequestResponseLogDetailHandler.class.getName());
	
	private SdpTransactionBean log;
	public SdpTransactionBean getLog() {
		return log;
	}
	public void setLog(SdpTransactionBean log) {
		this.log = log;
	}
	public SdpTransactionDataBean data;
	public SdpTransactionDataBean getData() {
		return data;
	}
	public void setData(SdpTransactionDataBean data) {
		this.data = data;
	}
	
	private String vendorName;
	public String getVendorName() {
		if (vendorName == null) {
			if (data == null) {
				return null;
			}
			vendorName = Constants.getVendorName(data.getVendorId());
		}
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	private boolean reflowable = true;
	public boolean isReflowable() {
		return reflowable;
	}
	public void setReflowable(boolean reflowable) {
		this.reflowable = reflowable;
	}
	
	private String logId;
	
	/**
	 * @throws DataSourceLookupException if unable to connect to data source
	 * @throws DataAccessException if db lookup fails
	 * @throws NullPointerException if logId is null in external context
	 */
	public RequestResponseLogDetailHandler() throws DataAccessException, DataSourceLookupException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		logId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		logger.log(Level.DEBUG, "Initializing request response detail page with logId=" + logId);
		if (logId == null) {
			logger.log(Level.ERROR, "logId was null or not set in context. Cannot initialize page.");
			throw new NullPointerException("logId was null or not set in context. Cannot initialize page.");
		}
		this.log = SdpOrderDetailDBWrapper.getRequestResponseLogByLogId(logId);
		if (this.log == null) {
			logger.log(Level.ERROR, "Failed to retrieve request response log with logId=[" + logId + "]");
			throw new DataAccessException("Failed to retrieve request response log with logId=[" + logId + "]");
		}
		if (log.getRequestMessage() != null) {
			try {
				XmlObject xml = XmlObject.Factory.parse(log.getRequestMessage());
				if (xml instanceof BBYOrder || xml instanceof BBYOrderDocument) {
					data = SdpTransactionDataXml.getSdpTransactionDataBean(xml);
					
//					if (data.getSourceSystemId() == null || SdpConfigProperties.isSourceSystemReflowableInFulfillmentServices(data.getSourceSystemId())) {
					if(data.getMasterVendorId() != null && data.getDigitalProductType() != null){
						List<CatalogBean> catalogs = CatalogDBWrapper.getCatalogByMasterVndrIdAndProdType(data.getMasterVendorId(),data.getDigitalProductType());
						if (catalogs == null || catalogs.size() < 1) {
							logger.log(Level.WARN, "No catalog entries were found matching primary sku=[" + data.getPrimarySku() + "] Reflow options will be disabled.");
							reflowable = false;
						} else if (catalogs.size() > 1) {
							logger.log(Level.WARN, "Multiple catalog entries were found matching primary sku=[" + data.getPrimarySku() + "] Reflow options will be disabled.");
							reflowable = false;
						} else {
							if (logger.isDebugEnabled()) {
								logger.log(Level.DEBUG, "Found catalog entry >> \n" + catalogs.get(0));
							}
							CatalogBean catalog = catalogs.get(0);
							data.setCategory(catalog.getCategory());
							data.setProductDescription(catalog.getPrimarySkuDescription());
							data.setVendorId(catalog.getVendorId());
							if (data.getSourceSystemId() == null || SdpConfigProperties.isSourceSystemReflowableInFulfillmentServices(data.getSourceSystemId())) {
								reflowable = catalogs.size()!=1?false:isRequestTypeReflowable(log.getRequestType());
							} else {
								logger.log(Level.DEBUG, "Source System ID [" + data.getSourceSystemId() + "] is not a reflowable source. This can be configured in sdp-config. Reflow options will be disabled.");
								reflowable = false;
							}
							logger.log(Level.DEBUG, "RequestResponseLogDetailHandler INITIALIZED >> reflowable=[" + reflowable + "]");
						}
					}else if (data.getPrimarySku() != null) {
    						List<CatalogBean> catalogs = CatalogDBWrapper.getByProductSku(data.getPrimarySku());
    						if (catalogs == null || catalogs.size() < 1) {
    							logger.log(Level.WARN, "No catalog entries were found matching primary sku=[" + data.getPrimarySku() + "] Reflow options will be disabled.");
    							reflowable = false;
    						} else if (catalogs.size() > 1) {
    							logger.log(Level.WARN, "Multiple catalog entries were found matching primary sku=[" + data.getPrimarySku() + "] Reflow options will be disabled.");
    							reflowable = false;
    						} else {
    							if (logger.isDebugEnabled()) {
    								logger.log(Level.DEBUG, "Found catalog entry >> \n" + catalogs.get(0));
    							}
    							CatalogBean catalog = catalogs.get(0);
    							data.setCategory(catalog.getCategory());
    							data.setProductDescription(catalog.getPrimarySkuDescription());
    							data.setVendorId(catalog.getVendorId());
    							if (data.getSourceSystemId() == null || SdpConfigProperties.isSourceSystemReflowableInFulfillmentServices(data.getSourceSystemId())) {
    								reflowable = catalogs.size()!=1?false:isRequestTypeReflowable(log.getRequestType());
    							} else {
    								logger.log(Level.DEBUG, "Source System ID [" + data.getSourceSystemId() + "] is not a reflowable source. This can be configured in sdp-config. Reflow options will be disabled.");
    								reflowable = false;
    							}
    							logger.log(Level.DEBUG, "RequestResponseLogDetailHandler INITIALIZED >> reflowable=[" + reflowable + "]");
    						}
    					} else {
    						logger.log(Level.DEBUG, "Parsed BBYOrder but did not contain TriggerSku or (master vendorid or product type). Reflow options will be disabled.");
    						reflowable = false;
    					}
//					} else {
//						logger.log(Level.DEBUG, "Source System ID [" + data.getSourceSystemId() + "] is not a reflowable source. This can be configured in sdp-config. Reflow options will be disabled.");
//						reflowable = false;
//					}
				} else {
					logger.log(Level.DEBUG, "Request document was not a BBYOrder. Disabling reflow options.");
					data = new SdpTransactionDataBean();
					reflowable = false;
				}
			} catch (XmlException e) {
				logger.log(Level.ERROR, "Failed to parse request response logs RQST_MSG string to an xml document. Reflow options will be disabled: >> \n" + log.getRequestMessage(), e);
				data = new SdpTransactionDataBean();
				reflowable = false;
			} catch (ParseException e) {
				logger.log(Level.ERROR, "Failed to parse request response logs RQST_MSG string to an xml document. Reflow options will be disabled: >> \n" + log.getRequestMessage(), e);
				data = new SdpTransactionDataBean();
				reflowable = false;
			}
		} else {
			data = new SdpTransactionDataBean();
			reflowable = false;
		}
	}
	
	private static boolean isRequestTypeReflowable(String requestType) {
		if (requestType == null) {
			return false;
		} else if (requestType.equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
			return true;
		} else if (requestType.equalsIgnoreCase(SdpConfigProperties.getRequestTypeRealTimeActivate())) {
			return true;
		} else if (requestType.equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
			return true;
		} else if (requestType.equalsIgnoreCase(SdpConfigProperties.getRequestTypeReactivate())) {
			return true;
		} else if (requestType.equalsIgnoreCase(SdpConfigProperties.getRequestTypeAdjustment())) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public String reprocessButtonClick() {
		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_REQUEST_DEFAULT_PAGE, SdpRequestHandler.REQUEST_PARAM, logId);
	}
	
	public boolean isOrderExists() {
		return this.log.getSdpOrderId() != null;
	}
	
	public String viewOrderButtonClick() {
		SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
		sdpOrderDetailHandler.viewOrderDetailButtonClick(this.log.getSdpOrderId());
		return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.log.getSdpOrderId());
	}
}
