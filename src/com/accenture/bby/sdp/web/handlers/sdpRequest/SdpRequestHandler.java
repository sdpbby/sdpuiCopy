package com.accenture.bby.sdp.web.handlers.sdpRequest;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrder;
import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrderDocument;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.db.SdpTransactionDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.utl.webservice.RequestManager;
import com.accenture.bby.sdp.utl.xml.SdpTransactionDataXml;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.beans.SdpRequestBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.DropdownListManager;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.requestResponseLogDetail.RequestResponseLogDetailHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderSearch.SdpOrderSearchHandler;

@ManagedBean (name="sdpRequestHandler")
@ViewScoped
public class SdpRequestHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4306506788617253859L;
	private static final Logger logger = Logger.getLogger(SdpRequestHandler.class.getName());
	
	@ManagedProperty (value="#{dropdownListManager}")
	private DropdownListManager dropdownListManager;
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	
	private SdpRequestTabHandler sdpRequestTabHandler = new SdpRequestTabHandler();
	public SdpRequestTabHandler getSdpRequestTabHandler() { return sdpRequestTabHandler; }
	private SdpRequestFormHandler sdpRequestFormHandler = new SdpRequestFormHandler();
	public SdpRequestFormHandler getSdpRequestFormHandler() { return sdpRequestFormHandler; }
	
	private SdpRequestBean sdpRequestBean = new SdpRequestBean();
	private String inputSku;
	private String primarySku;
	private String vendor;
	private String description;
	private String category;
	private Operation operation;
	private String sourceSystem;
	private static final List<SelectItem> months;
	private static final List<SelectItem> years;
	
	private List<SdpTransactionDataBean> existingTransactions;
	
	static {
		months = new ArrayList<SelectItem>(12);
		months.add(new SelectItem("01", "JAN"));
		months.add(new SelectItem("02", "FEB"));
		months.add(new SelectItem("03", "MAR"));
		months.add(new SelectItem("04", "APR"));
		months.add(new SelectItem("05", "MAY"));
		months.add(new SelectItem("06", "JUN"));
		months.add(new SelectItem("07", "JUL"));
		months.add(new SelectItem("08", "AUG"));
		months.add(new SelectItem("09", "SEP"));
		months.add(new SelectItem("10", "OCT"));
		months.add(new SelectItem("11", "NOV"));
		months.add(new SelectItem("12", "DEC"));
		years = new ArrayList<SelectItem>();
		Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		for (int i = 2008; i < currentYear + 7; i++) {
			years.add(new SelectItem(i, i+""));
		}
	}
	
	public enum Operation {
		NO_OPERATION(null), 
		FULFILL_ACTIVATION("Fulfill Activation"), 
		CANCEL_FULFILLMENT("Cancel Fulfillment"),
		ADJUST_ACTIVATION("Adjust Activation"),
		REACTIVATE_FULFILLMENT("Reactivate Fulfillment");
		private Operation(String name) {
			this.name = name;
		}
		private String name;
		public String getName() {
			return name;
		}
	}
	
	private String logId;
	
	public SdpRequestHandler() throws DataAccessException, DataSourceLookupException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String logId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		
		
		// if null then blank reflow form is initialized and user manually populates the form
		// if not null then retrieves the data from the existing log record to prepopulate the form
		if (logId != null) {
			this.logId = logId;
			SdpTransactionBean log = SdpOrderDetailDBWrapper.getRequestResponseLogByLogId(logId);
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "Found request response log >> \n" + log.toString());
			}
			if (log != null && log.getRequestMessage() != null && log.getRequestType() != null) {
				BBYOrderDocument doc = getBBYOrderDocument(log.getRequestMessage());
				if (doc != null) {
					try {
						SdpTransactionDataBean data = SdpTransactionDataXml.getSdpTransactionDataBean(doc);
						this.inputSku = data.getPrimarySku();
						if (this.inputSku == null) {
							logger.log(Level.WARN, "Primary SKU retrieved from BBYOrder was missing or null. Displaying blank reflow form instead.");
							this.logId = null;
							return;
						}
						
						this.searchBySkuButtonClick();
						
						if (this.isCategoryUnsupported()) {
							throw new IllegalArgumentException("Request Response log with ID=[" + logId + "] does not have a supported category for reflow.");
						}
						
						if (this.isCategoryS2()) {
							
							if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
								this.displayFulfillActivationS2ButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRealTimeActivate())) {
								this.displayFulfillActivationS2ButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
								this.displayCancelFulfillmentS2ButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeAdjustment())) {
								this.displayAdjustActivationS2ButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeReactivate())) {
								this.displayReactivateFulfillmentS2ButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else {
								throw new UnsupportedOperationException("OpsUI does not support reflows for S2 request type [" + log.getRequestType() + "]");
							}
						} else if (this.isCategoryPsa()) {
							if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
								this.displayFulfillActivationPsaButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRealTimeActivate())) {
								this.displayFulfillActivationPsaButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
								this.displayCancelFulfillmentPsaButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeAdjustment())) {
								this.displayAdjustActivationPsaButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeReactivate())) {
								this.displayReactivateFulfillmentPsaButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else {
								throw new UnsupportedOperationException("OpsUI does not support reflows for PSA request type [" + log.getRequestType() + "]");
							}
						} else if (this.isCategoryDigital()) {
							if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
								this.displayFulfillActivationDigitalButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRealTimeActivate())) {
								this.displayFulfillActivationDigitalButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
								this.displayCancelFulfillmentDigitalButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeAdjustment())) {
								this.displayAdjustActivationDigitalButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeReactivate())) {
								this.displayReactivateFulfillmentDigitalButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
								if (!data.isPhonePresent()) {
									data.setPhoneLabel(data.getIgnoreAllFieldsString());
								}
								if (!data.isAddressPresent()) {
									data.setAddressLabel(data.getIgnoreAllFieldsString());
								}
							} else {
								throw new UnsupportedOperationException("OpsUI does not support reflows for RTA request type [" + log.getRequestType() + "]");
							}
						} else if (this.isCategoryTechSupport()) {
							if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
								this.displayFulfillActivationTechSupportButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRealTimeActivate())) {
								this.displayFulfillActivationTechSupportButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
								this.displayCancelFulfillmentTechSupportButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeAdjustment())) {
								this.displayAdjustActivationTechSupportButtonClick();
							} else if (log.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeReactivate())) {
								this.displayReactivateFulfillmentTechSupportButtonClick();
								if (!data.isCreditCardPresent()) {
									data.setCreditCardType(data.getIgnoreAllFieldsString());
								} else {
									if (data.getCreditCardExpireDate() != null && data.getCreditCardExpireDate().matches("\\d\\d\\d\\d-\\d\\d")) {
										this.sdpRequestBean.setCreditCardMonth(data.getCreditCardExpireDate().substring(5, 7));
										this.sdpRequestBean.setCreditCardYear(data.getCreditCardExpireDate().substring(0, 4));
									} else {
										this.sdpRequestBean.setNoCreditCardExpire(true);
									}
								}
							} else {
								throw new UnsupportedOperationException("OpsUI does not support reflows for TECH SUPPORT request type [" + log.getRequestType() + "]");
							}
						} else {
							throw new UnsupportedOperationException("OpsUI does not support reflows for [" + this.category + "] request type [" + log.getRequestType() + "]");
						}
						
						this.sdpRequestBean.setData(data);
						this.sdpRequestBean.setOriginalData(data);
						
					} catch (ParseException e) {
						logger.log(Level.ERROR, "Failed to parse BBYOrderDocument. Displaying blank reflow form instead", e);
					}
				} else {
					logger.log(Level.DEBUG, "Request message is not an instance of BBYOrder or BBYOrderDocument. Displaying blank reflow form instead.");
				}
			} else {
				logger.log(Level.WARN, "Request Response Log record with ID=[" + logId + "] was not found. Displaying blank reflow form instead.");
			}
		} else {
			// if no input parameters then initialize blank form for reflow from scratch
			// this is the normal path if clicking reflow sdp from the home page
		}
	}
	
	private static BBYOrderDocument getBBYOrderDocument(String requestMessage) {
		if (requestMessage == null) {
			logger.log(Level.WARN, "Request message was null!");
			return null;
		}
		try {
			XmlObject xml = XmlObject.Factory.parse(requestMessage);
			if (xml instanceof BBYOrderDocument) {
				return (BBYOrderDocument) xml;
			} else if (xml instanceof BBYOrder) {
				BBYOrderDocument doc = BBYOrderDocument.Factory.newInstance();
				doc.setBBYOrder((BBYOrder)xml);
				return doc;
			} else {
				logger.log(Level.DEBUG, "XmlObject was not an instance of BBYOrder or BBYOrderDocument. Returning null. >> \n" + requestMessage);
				return null;
			}
		} catch (XmlException e) {
			logger.log(Level.ERROR, "Failed to parse request message string to xml. It may not be well formed. Returning null. >> " + requestMessage, e);
			return null;
		}
	}
	
	private static final int defaultMessageFlag = 0;
	private static final int skuNotFoundFlag = 1;
	private static final int multipleSkusFoundFlag = 2;
	private static final int reprocessNotSupportedFlag = 3;
	private static final int activateExistingRequestFlag = 4;
	private static final int activateRequestAlreadyCompletedFlag = 5;
	private static final int reactivateRequestFlag = 6;
	private static final int noActiveRequestFlag = 7;
	private static final int specialCharacterFoundFlag = 8;
	private static final int specialCharacterFoundInsertFlag =9;
	private int messageFlag = defaultMessageFlag;
	
	public boolean isDefaultFlag() { return messageFlag == defaultMessageFlag; }
	public boolean isSkuNotFoundFlag() { return messageFlag == skuNotFoundFlag; }
	public boolean isMultipleSkusFoundFlag() { return messageFlag == multipleSkusFoundFlag; }
	public boolean isReprocessNotSupportedFlag() { return messageFlag == reprocessNotSupportedFlag; }
	public boolean isActivateExistingRequestFlag() { return messageFlag == activateExistingRequestFlag; }
	public boolean isActivateRequestAlreadyCompletedFlag() { return messageFlag == activateRequestAlreadyCompletedFlag; }
	public boolean isReactivateRequestFlag() { return messageFlag == reactivateRequestFlag; }
	public boolean isNoActiveRequestFlag() { return messageFlag == noActiveRequestFlag; }
	public boolean isSpecialCharacterFoundFlag() { return messageFlag == specialCharacterFoundFlag; }
	public boolean isSpecialCharacterFoundInsertFlag() { return messageFlag == specialCharacterFoundInsertFlag; }
	
	public String getAddressFieldStyle() {
		if (sdpRequestBean.isAddressRequired()) {
			return "medium_field field_spacing";
		} else {
			return "medium_field field_spacing disabled";
		}
	}
	
	public String getPhoneFieldStyle() {
		if (sdpRequestBean.isPhoneRequired()) {
			return "medium_field field_spacing";
		} else {
			return "medium_field field_spacing disabled";
		}
	}
	
	public String getCreditCardFieldStyle() {
		if (sdpRequestBean.isCreditCardRequired()) {
			return "medium_field field_spacing";
		} else {
			return "medium_field field_spacing disabled";
		}
	}
	
	public String getCreditCardExpireDateFieldStyle() {
		if (sdpRequestBean.isCreditCardRequired()) {
			return "";
		} else {
			return "disabled";
		}
	}
	
	private boolean renderPopup = false;
	public boolean isRenderPopup() { return renderPopup; }
	public void setRenderPopup(boolean renderPopup) { this.renderPopup = renderPopup;}
	public void togglePopup(ActionEvent e) { 
		renderPopup = !renderPopup; 
		logger.log(Level.DEBUG, "renderPopup=" + renderPopup);
	}
	
	public String searchBySkuButtonClick() {
		messageFlag = defaultMessageFlag;
		logger.log(Level.DEBUG, "BEGIN Searching for primary sku >> " + this.inputSku);
		try {
			retrieveTransactionDetails();
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve catalog details. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		logger.log(Level.DEBUG, "FINISH Searching for primary sku >> " + this.inputSku);
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public void retrieveTransactionDetails(String inputSku) throws DataSourceLookupException, DataAccessException {
		if (TextFilter.isSpecialCharacterPresent(inputSku)) {
			logger.log(Level.DEBUG,
			"Special Character found. specialCharacterFoundFlag set.");
			messageFlag = specialCharacterFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
		} else{List<CatalogBean> catalogBeans = CatalogDBWrapper.getByProductSku(inputSku);
		if (catalogBeans == null || catalogBeans.size() < 1) {
			logger.log(Level.DEBUG, "No results found. skuNotFoundFlag set.");
			messageFlag = skuNotFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
		} else if (catalogBeans.size() > 1) {
			logger.log(Level.DEBUG, catalogBeans.size() + " results found. multipleSkusFoundFlag set.");
			messageFlag = multipleSkusFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
		} else {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "Found 1 catalog entry:\n" + catalogBeans.get(0));
			}
			this.category = catalogBeans.get(0).getCategory();
			this.description = catalogBeans.get(0).getPrimarySkuDescription();
			this.vendor = catalogBeans.get(0).getVendorName();
			this.primarySku = catalogBeans.get(0).getPrimarySku();
			
		}
		}
	}
	
	private void retrieveTransactionDetails() throws DataSourceLookupException, DataAccessException {
		retrieveTransactionDetails(this.inputSku);
	}
	
	private void setDefaults() {
		
		if (this.category == null) {
			throw new IllegalStateException("This SKU does not have a category value set in the catalog.");
		}
		if (this.category.equals(SdpConfigProperties.getCategoryDigital())) {
			this.getSdpRequestBean().getData().setAddressLabel(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
			this.getSdpRequestBean().getData().setPhoneLabel(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
			this.getSdpRequestBean().getData().setCreditCardType(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
		} else if (this.category.equals(SdpConfigProperties.getCategoryS2())) {
			this.getSdpRequestBean().getData().setCreditCardType(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
		} else if (this.category.equals(SdpConfigProperties.getCategoryTechSupport())) {
			this.getSdpRequestBean().getData().setCreditCardType(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
		} else if (this.category.equals(SdpConfigProperties.getCategoryPsa())) {
			this.getSdpRequestBean().getData().setAddressLabel(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
			this.getSdpRequestBean().getData().setPhoneLabel(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
			this.getSdpRequestBean().getData().setCreditCardType(this.getSdpRequestBean().getData().getIgnoreAllFieldsString());
		} else {
			throw new IllegalStateException("This SKU does not have a valid category for reprocessing >> " + this.category);
		}

		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.sdpRequestBean.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.getSdpRequestBean().getData().setPrimarySku(this.primarySku);
	}
	
	
	public boolean isCategorySet() { return this.category != null; }
	public boolean isCategoryS2() { return this.category.equals(SdpConfigProperties.getCategoryS2()); }
	public boolean isCategoryPsa() { return this.category.equals(SdpConfigProperties.getCategoryPsa()); }
	public boolean isCategoryDigital() { return this.category.equals(SdpConfigProperties.getCategoryDigital()); }
	public boolean isCategoryTechSupport() { return this.category.equals(SdpConfigProperties.getCategoryTechSupport()); }
	public boolean isCategoryUnsupported() { return isCategorySet() && !isCategoryS2() && !isCategoryPsa() && !isCategoryDigital() && !isCategoryTechSupport(); }
	

	public void addressValueChanged(ValueChangeEvent e){
		if (e != null && e.getNewValue() != null) {
			sdpRequestBean.getData().setAddressLabel((String)e.getNewValue());
		} else {
			sdpRequestBean.getData().setAddressLabel(null);
		}
	}
	
	public void phoneValueChanged(ValueChangeEvent e){
		if (e != null && e.getNewValue() != null) {
			sdpRequestBean.getData().setPhoneLabel((String)e.getNewValue());
		} else {
			sdpRequestBean.getData().setPhoneLabel(null);
		}
	}
	
	public void creditCardValueChanged(ValueChangeEvent e){
		if (e != null && e.getNewValue() != null) {
			sdpRequestBean.getData().setCreditCardType((String)e.getNewValue());
		} else {
			sdpRequestBean.getData().setCreditCardType(null);
		}
	}
	
	
	public SdpRequestBean getSdpRequestBean() {
		return sdpRequestBean;
	}

	public void setSdpRequestBean(SdpRequestBean sdpRequestBean) {
		this.sdpRequestBean = sdpRequestBean;
	}
	public String getInputSku() {
		return inputSku;
	}
	public void setInputSku(String inputSku) {
		this.inputSku = inputSku;
	}
	public String getPrimarySku() {
		return primarySku;
	}
	public String getVendor() {
		return vendor;
	}
	public String getDescription() {
		return description;
	}
	public Operation getOperation() {
		return operation;
	}
	public String getOperationName() {
		if (operation == null) return null;
		else return operation.getName();
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getCategory() {
		return category;
	}
	public List<SelectItem> getMonths() {
		return months;
	}
	public List<SelectItem> getYears() {
		return years;
	}
	public DropdownListManager getDropdownListManager() {
		return dropdownListManager;
	}
	public void setDropdownListManager(DropdownListManager dropdownListManager) {
		this.dropdownListManager = dropdownListManager;
	}
	
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	public String changeFormButtonClick() {
		this.sdpRequestBean.setData(new SdpTransactionDataBean());
		this.sdpRequestTabHandler.displayOperationsTab();
		this.operation = Operation.NO_OPERATION;
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayFulfillActivationS2ButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayFulfillActivationS2Form();
		this.operation = Operation.FULFILL_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayCancelFulfillmentS2ButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayCancelFulfillmentS2Form();
		this.operation = Operation.CANCEL_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayAdjustActivationS2ButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayAdjustActivationS2Form();
		this.operation = Operation.ADJUST_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayReactivateFulfillmentS2ButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayReactivateFulfillmentS2Form();
		this.operation = Operation.REACTIVATE_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayFulfillActivationPsaButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayFulfillActivationPsaForm();
		this.operation = Operation.FULFILL_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayCancelFulfillmentPsaButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayCancelFulfillmentPsaForm();
		this.operation = Operation.CANCEL_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayAdjustActivationPsaButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayAdjustActivationPsaForm();
		this.operation = Operation.ADJUST_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayReactivateFulfillmentPsaButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayReactivateFulfillmentPsaForm();
		this.operation = Operation.REACTIVATE_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayFulfillActivationDigitalButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayFulfillActivationDigitalForm();
		this.operation = Operation.FULFILL_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayCancelFulfillmentDigitalButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayCancelFulfillmentDigitalForm();
		this.operation = Operation.CANCEL_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayAdjustActivationDigitalButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayAdjustActivationDigitalForm();
		this.operation = Operation.ADJUST_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayReactivateFulfillmentDigitalButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayReactivateFulfillmentDigitalForm();
		this.operation = Operation.REACTIVATE_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayFulfillActivationTechSupportButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayFulfillActivationTechSupportForm();
		this.operation = Operation.FULFILL_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayCancelFulfillmentTechSupportButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayCancelFulfillmentTechSupportForm();
		this.operation = Operation.CANCEL_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayAdjustActivationTechSupportButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayAdjustActivationTechSupportForm();
		this.operation = Operation.ADJUST_ACTIVATION;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String displayReactivateFulfillmentTechSupportButtonClick() {
		this.sdpRequestTabHandler.displayFormTab();
		this.sdpRequestFormHandler.displayReactivateFulfillmentTechSupportForm();
		this.operation = Operation.REACTIVATE_FULFILLMENT;
		setDefaults();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/**
	 * populates alternate text in the popup informing user that multiple records were found.
	 */
	private boolean multipleRecordsFound = false;
	public boolean isMultipleRecordsFound() { return multipleRecordsFound; }
	public String getGeneratedLineItemId() {
		return this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey();
	}
	public String viewOrderButtonClick() {
		logger.log(Level.INFO, "BEGIN view order button click.");
		
		try {
			existingTransactions = SdpTransactionDBWrapper.getSdpTransactionDataByLineItemId(this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey());
			
			if (existingTransactions == null || existingTransactions.size() < 1) {
				renderPopup = !renderPopup;
				logger.log(Level.INFO, "FINISH view order button click. No orders found.");
				return NavigationStrings.CURRENT_VIEW;
			} else if (existingTransactions.size() > 1) {
				logger.log(Level.WARN, "Found multiple results <" + existingTransactions.size() + "> with matching lineitem id <" + this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey() + ">." );
				multipleRecordsFound = true;
				renderPopup = !renderPopup;
				logger.log(Level.INFO, "FINISH view order button click. Multiple orders found.");
				return NavigationStrings.CURRENT_VIEW;
			} else {
				logger.log(Level.INFO, "FINISH view order button click. Navigating to order detail page.");
				SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
				sdpOrderDetailHandler.viewOrderDetailButtonClick(existingTransactions.get(0).getSdpOrderId());
				return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//				return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, existingTransactions.get(0).getSdpOrderId());
			}
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve existing transactions. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH view order button click with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH view order button click with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}		
	}
	
	public String manualSearchButtonClick() {
		if (logger.isInfoEnabled()) {
			logger.log(Level.INFO, "Manual search button clicked. Navigation set >> " + NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_SEARCH_PAGE, SdpOrderSearchHandler.LINEITEM_ID_PARAM_NAME, this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey()));
		}
		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_SEARCH_PAGE, SdpOrderSearchHandler.LINEITEM_ID_PARAM_NAME, this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey());
	}
	
	public String processRequestButtonClick() {
		boolean specialCharFoundFlag = false;
		if (isNotNull(this.getSdpRequestBean().getData().getFpkStoreId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getFpkStoreId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getFpkTransactionId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getFpkTransactionId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getFpkRegisterId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getFpkRegisterId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getFpkLineId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getFpkLineId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getPrimarySku()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getPrimarySku().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getPrimarySkuPrice()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getPrimarySkuPrice().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getPrimarySkuTaxAmount()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getPrimarySkuTaxAmount().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getPrimarySkuTaxRate()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getPrimarySkuTaxRate().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getRelatedSku()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getRelatedSku().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getRelatedSkuPrice()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getRelatedSkuPrice().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getRelatedSkuTaxAmount()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getRelatedSkuTaxAmount().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getRelatedSkuTaxRate()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getRelatedSkuTaxRate().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getDeliveryEmail()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getDeliveryEmail().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getSerialNumber()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getSerialNumber().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getValuePackageId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getValuePackageId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getRewardZoneNumber()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getRewardZoneNumber().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getCaPartyId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCaPartyId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getCustomerFirstName()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCustomerFirstName().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getCustomerMiddleName()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCustomerMiddleName().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getCustomerLastName()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCustomerLastName().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getCustomerContactEmail()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCustomerContactEmail().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getAddressLine1()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getAddressLine1().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getAddressLine2()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getAddressLine2().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getAddressCity()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getAddressCity().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getAddressState()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getAddressState().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getAddressZipcode()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getAddressZipcode().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getCreditCardCustomerName()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCreditCardCustomerName().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getCreditCardToken()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getCreditCardToken().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getOriginalFpkStoreId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getOriginalFpkStoreId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getOriginalFpkRegisterId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getOriginalFpkRegisterId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getOriginalFpkTransactionId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getOriginalFpkTransactionId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getSdpRequestBean().getData().getOriginalFpkLineId()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getOriginalFpkLineId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getSdpRequestBean().getData().getPhoneNumber()) && TextFilter.isSpecialCharacterPresent(this.getSdpRequestBean().getData().getPhoneNumber().trim())) {
			specialCharFoundFlag= true;
		}
		
		
		if(specialCharFoundFlag){
			messageFlag= specialCharacterFoundInsertFlag;
			return NavigationStrings.CURRENT_VIEW;
		}else{
			messageFlag = defaultMessageFlag;
			logger.log(Level.DEBUG, "Setting renderPopup=TRUE");
		try {
			existingTransactions = SdpTransactionDBWrapper.getSdpTransactionDataByLineItemId(this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey());
			
			// log warning if multiple records are found, then proceed with first record and discard the rest.
			if (existingTransactions != null && existingTransactions.size() > 1) {
				logger.log(Level.WARN, "Found multiple results <" + existingTransactions.size() + "> with matching lineitem id <" + this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey() + ">. Using first result found and ignoring the rest." );
			}
			
			switch (this.operation) {
			case FULFILL_ACTIVATION :
				if (existingTransactions == null || existingTransactions.size() < 1) {
					messageFlag = defaultMessageFlag;
				} else if (existingTransactions.get(0).getSdpOrderStatus() != null && existingTransactions.get(0).getSdpOrderStatus().equals(SdpConfigProperties.getStatusCompleted())) {
					messageFlag = activateRequestAlreadyCompletedFlag;
				} else if (existingTransactions.get(0).getSdpOrderStatus() != null && existingTransactions.get(0).getSdpOrderStatus().equals(SdpConfigProperties.getStatusCancelled())) {
					messageFlag = reactivateRequestFlag;
				} else {
					messageFlag = activateExistingRequestFlag;
				}
				break;
			case REACTIVATE_FULFILLMENT :
				if (existingTransactions == null || existingTransactions.size() < 1) {
					messageFlag = defaultMessageFlag;
				} else if (existingTransactions.get(0).getSdpOrderStatus() != null && existingTransactions.get(0).getSdpOrderStatus().equals(SdpConfigProperties.getStatusCompleted())) {
					messageFlag = activateRequestAlreadyCompletedFlag;
				} else {
					messageFlag = activateExistingRequestFlag;
				}
				break;
			case CANCEL_FULFILLMENT :
				if (existingTransactions == null || existingTransactions.size() < 1) {
					messageFlag = noActiveRequestFlag;
				} else {
					messageFlag = defaultMessageFlag;;
				}
				break;
			case ADJUST_ACTIVATION :
				if (existingTransactions == null || existingTransactions.size() < 1) {
					messageFlag = noActiveRequestFlag;
				} else {
					messageFlag = defaultMessageFlag;;
				}
				break;
			default :
				throw new IllegalStateException("Operation was not set. This should never happen.");
			}
			
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve existing transactions. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		renderPopup = !renderPopup;
		return NavigationStrings.CURRENT_VIEW;
		}
	}
	
	/**
	 * sends a request to the sdp fulfillment services webservice. Note: if the request operation is activate but an existing order
	 * already exists in the sdp database that is in a cancelled state, then a reactivate request will be generated and submitted
	 * to sdp instead.
	 * @return navigation string.
	 */
	public String sendRequestNowButtonClick() {
		logger.log(Level.INFO, "BEGIN sending request to BS_FulfillmentServices.");
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, "Processing the following request data:\n" + this.getSdpRequestBean().toString());
		}
		try {
			switch (operation) {
			case FULFILL_ACTIVATION :
				if (this.getSdpRequestBean().getData().isCreditCardPresent()) {
					if (!this.getSdpRequestBean().isNoCreditCardExpire()) {
						this.getSdpRequestBean().getData().setCreditCardExpireDate(this.getSdpRequestBean().getCreditCardYear() + '-' + this.getSdpRequestBean().getCreditCardMonth());
					} else {
						this.getSdpRequestBean().getData().setCreditCardExpireDate(null);
					}
				}
				RequestManager.getInstance().sendFulfillActivationRequestToSDP(this.getSdpRequestBean().getData(), this.getSdpRequestBean().getOriginalData());
				break;
			case CANCEL_FULFILLMENT :
				RequestManager.getInstance().sendCancelFulfillmentRequestToSDP(this.getSdpRequestBean().getData(), this.getSdpRequestBean().getOriginalData());
				break;
			case ADJUST_ACTIVATION :
				RequestManager.getInstance().sendAdjustActivationRequestToSDP(this.getSdpRequestBean().getData(), this.getSdpRequestBean().getOriginalData());
				break;
			case REACTIVATE_FULFILLMENT :
				if (this.getSdpRequestBean().getData().isCreditCardPresent()) {
					if (!this.getSdpRequestBean().isNoCreditCardExpire()) {
						this.getSdpRequestBean().getData().setCreditCardExpireDate(this.getSdpRequestBean().getCreditCardYear() + '-' + this.getSdpRequestBean().getCreditCardMonth());
					} else {
						this.getSdpRequestBean().getData().setCreditCardExpireDate(null);
					}
				}
				RequestManager.getInstance().sendReActivateFulfillmentRequestToSDP(this.getSdpRequestBean().getData(), null);
				break;
			default :
				logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
				throw new IllegalStateException("No operation was set. This should never happen.");
			}
		
		} catch (MissingPropertyException e) {
			this.getExceptionHandler().initialize(e, "Required web service configuration is missing. This request could not be processed at this time. Please notify your site administrator.");
			logger.log(Level.ERROR, this.getExceptionHandler().toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (WebServiceCallFailedException e) {
			this.getExceptionHandler().initialize(e, "The web service call was not successful. Please notify your site administrator.");
			logger.log(Level.ERROR, this.getExceptionHandler().toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (AuditTrailException e) {
			this.getExceptionHandler().initialize(e, "Audit Trail failure occurred. Please notify your site administrator.");
			logger.log(Level.ERROR, this.getExceptionHandler().toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		
		logger.log(Level.DEBUG, "Request submitted. Checking database for existing SDP Order Records.");
		
		// check the database to see if a transaction exists with matching lineitem id. if found navigate directly to order detail page
		// if not found display order submitted tab.
		try {
			if (existingTransactions == null || existingTransactions.size() < 1) {
				existingTransactions = SdpTransactionDBWrapper.getSdpTransactionDataByLineItemId(this.getSdpRequestBean().getData().generateLineItemIdByFivePartKey());
			}
			
			// if 1 record is found navigate directly to order detail page
			if (existingTransactions != null && existingTransactions.size() == 1) {
				logger.log(Level.DEBUG, "1 Order record found. Navigating to Order Detail screen.");
				logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices.");
				SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
				sdpOrderDetailHandler.viewOrderDetailButtonClick(existingTransactions.get(0).getSdpOrderId());
				return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//				return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, existingTransactions.get(0).getSdpOrderId());
			} else if (existingTransactions == null || existingTransactions.size() < 1) {
				logger.log(Level.DEBUG, "No records found. Displaying record submitted screen.");
			} else {
				logger.log(Level.DEBUG, "Multiple records found. Displaying record submitted screen.");
			}
			
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve existing transactions. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}	
		
		this.getSdpRequestTabHandler().displayFormSubmittedTab();
		logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices.");
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public static final String REQUEST_PARAM = "logId";
	
	public boolean isReflowFromTransLog() {
		return this.logId != null;
	}

	public String backToTransLogButtonClick() {
		return NavigationStrings.getParameterizedUrl(NavigationStrings.REQUEST_RESPONSE_LOG_DETAIL_DEFAULT_PAGE, RequestResponseLogDetailHandler.REQUEST_PARAM, this.logId);
	}
	
	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}

}
