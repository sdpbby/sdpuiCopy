package com.accenture.bby.sdp.web.handlers.directToVendor;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import bbyorder.om.bby.sdp.com.accenture.xml.BBYOrderDocument;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.db.SdpTransactionDBWrapper;
import com.accenture.bby.sdp.db.VendorProvisioningStatusDBWrapper;
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
import com.accenture.bby.sdp.utl.xml.ServiceResultWrapper;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.beans.VendorCatalogBean;
import com.accenture.bby.sdp.web.beans.VendorProvisioningStatusBean;
import com.accenture.bby.sdp.web.beans.VendorRequestBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;
import com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus.VendorProvisioningStatusHandler;

@ManagedBean(name = "directToVendorHandler")
@ViewScoped
public class DirectToVendorHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1085557523984506184L;

	private static final Logger logger = Logger.getLogger(DirectToVendorHandler.class.getName());

	public static final String VPS_REQUEST_PARAM = "vpsId";
	public static final String SDP_ORDER_REQUEST_PARAM = "sdpOrderId";

	private String inputSku;
	private String primarySku;
	private String vendor;
	private String vendorId;
	private String description;
	private String category;
	private Operation operation = Operation.NO_OPERATION;
	private String sourceSystem;
	private String sdpOrderId;
	private String vpsId;

	private static final int defaultMessageFlag = 0;
	private static final int skuNotFoundFlag = 1;
	private static final int multipleSkusFoundFlag = 2;
	private static final int reprocessNotSupportedFlag = 3;
	private static final int specialCharacterFoundFlag = 4;
	private static final int specialCharacterFoundInsertFlag = 5;
	private int messageFlag = defaultMessageFlag;

	private ServiceResultWrapper serviceResult = new ServiceResultWrapper();

	public ServiceResultWrapper getServiceResult() {
		return serviceResult;
	}

	public void setServiceResult(ServiceResultWrapper serviceResult) {
		this.serviceResult = serviceResult;
	}

	public boolean isDefaultFlag() {
		return messageFlag == defaultMessageFlag;
	}

	public boolean isSkuNotFoundFlag() {
		return messageFlag == skuNotFoundFlag;
	}

	public boolean isMultipleSkusFoundFlag() {
		return messageFlag == multipleSkusFoundFlag;
	}

	public boolean isReprocessNotSupportedFlag() {
		return messageFlag == reprocessNotSupportedFlag;
	}
	
	public boolean isSpecialCharacterFoundFlag() {
		return messageFlag == specialCharacterFoundFlag;
	}
	
	public boolean isSpecialCharacterFoundInsertFlag() {
		return messageFlag == specialCharacterFoundInsertFlag;
	}

	private boolean multipleRecordsFound = false;

	public boolean isMultipleRecordsFound() {
		return multipleRecordsFound;
	}

	public boolean isNoCategoryFound() {
		return (this.category == null && this.primarySku !=null);
	}
	
	public boolean isCategorySet() {
		return this.category != null;
	}

	public boolean isCategoryS2() {
		return this.category.equals(SdpConfigProperties.getCategoryS2());
	}

	public boolean isCategoryPsa() {
		return this.category.equals(SdpConfigProperties.getCategoryPsa());
	}

	public boolean isCategoryPosa() {
		return this.category.equals(SdpConfigProperties.getCategoryPosa());
	}

	public boolean isCategoryUnsupported() {
		return isCategorySet() && !isCategoryS2() && !isCategoryPsa() && !isCategoryPosa();
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

	public void setPrimarySku(String primarySku) {
		this.primarySku = primarySku;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	private DirectToVendorFormHandler formHandler = new DirectToVendorFormHandler();;

	public DirectToVendorFormHandler getFormHandler() {
		return formHandler;
	}

	private DirectToVendorTabHandler tabHandler = new DirectToVendorTabHandler();

	public DirectToVendorTabHandler getTabHandler() {
		return tabHandler;
	}

	private boolean renderPopup;

	public boolean isRenderPopup() {
		return renderPopup;
	}

	public void togglePopup(ActionEvent e) {
		renderPopup = !renderPopup;
	}

	private VendorRequestBean vendorRequest = new VendorRequestBean();

	public VendorRequestBean getVendorRequest() {
		return vendorRequest;
	}

	public void setVendorRequest(VendorRequestBean vendorRequest) {
		this.vendorRequest = (vendorRequest != null ? vendorRequest : new VendorRequestBean());
	}

	public String changeFormButtonClick() {
		this.vendorRequest.setData(new SdpTransactionDataBean());
		this.tabHandler.displayOperationsTab();
		this.operation = Operation.NO_OPERATION;
		messageFlag = defaultMessageFlag;
		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchBySkuButtonClick() {
		logger.log(Level.DEBUG, "BEGIN search by sku button click >> " + this.inputSku);
		messageFlag = defaultMessageFlag;
		if (this.inputSku == null) {
			throw new IllegalArgumentException("No SKU details provided. Unable to retrieve catalog details.");
		}
		try {
			retrieveTransactionDetails();
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to retrieve catalog data. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		logger.log(Level.DEBUG, "FINISH search by sku button click >> " + this.inputSku);
		return NavigationStrings.CURRENT_VIEW;

	}

	public String sendRequestNowButtonClick() {
		logger.log(Level.DEBUG, "BEGIN send request now button click.");
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, "Processing the following request data:\n" + this.getVendorRequest().toString());
		}
		// set a new sdpID
		this.getVendorRequest().getData().setSdpId(UUID.randomUUID().toString());
		if(indefiniteSubscription){
			this.vendorRequest.getData().setContractEndDate(null);
		}
		try {
			switch (operation) {
			case ACT:
				serviceResult = RequestManager.getInstance().sendDirectToVendorActivateRequest(this.getVendorRequest().getData(), this.getVendorRequest().getOriginalData());
				break;
			case CNL:
				serviceResult = RequestManager.getInstance().sendDirectToVendorCancelRequest(this.getVendorRequest().getData(), this.getVendorRequest().getOriginalData());
				break;
			case RNW:
				serviceResult = RequestManager.getInstance().sendDirectToVendorRenewRequest(this.getVendorRequest().getData(), this.getVendorRequest().getOriginalData());
				break;
			case UPD:
				serviceResult = RequestManager.getInstance().sendDirectToVendorUpdateStatusRequest(this.getVendorRequest().getData(), this.getVendorRequest().getOriginalData());
				break;
			default:
				logger.log(Level.INFO, "FINISH sending request to BCS_VendorFulfillmentAdapter with errors.");
				throw new IllegalStateException("No operation was set. This should never happen.");
			}

		} catch (MissingPropertyException e) {
			this.exceptionHandler.initialize(e, "Required web service configuration is missing. This request could not be processed at this time. Please notify your site administrator.");
			logger.log(Level.ERROR, this.exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BCS_VendorFulfillmentAdapter with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (WebServiceCallFailedException e) {
			this.exceptionHandler.initialize(e, "The web service call was not successful. Please notify your site administrator.");
			logger.log(Level.ERROR, this.exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BCS_VendorFulfillmentAdapter with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (AuditTrailException e) {
			this.exceptionHandler.initialize(e, "Audit Trail failure occurred. Please notify your site administrator.");
			logger.log(Level.ERROR, this.exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BCS_VendorFulfillmentAdapter with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (XmlException e) {
			this.exceptionHandler.initialize(e, "Failed to generate request.");
			logger.log(Level.ERROR, this.exceptionHandler.toString(), e);
			logger.log(Level.INFO, "FINISH sending request to BCS_VendorFulfillmentAdapter with errors.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}

		logger.log(Level.DEBUG, "Request submitted. Checking database for existing SDP Order Records.");

		// check the database to see if a transaction exists with matching sdp
		// id. if found navigate directly to provisioning detail page
		// if not found display order submitted tab.
		try {

			List<VendorProvisioningStatusBean> results = SdpTransactionDBWrapper.getVendorProvisioningSearchResultBySdpId(this.getVendorRequest().getData().getSdpId());

			// if 1 record is found navigate directly to order detail page
			if (results != null && results.size() == 1) {
				logger.log(Level.DEBUG, "1 Order record found. Navigating to Provisioning Detail screen.");
				logger.log(Level.INFO, "FINISH sending request to BS_FulfillmentServices.");
				VendorProvisioningStatusHandler vpsHandler= BasePageHandler.getVPSHandler();
				vpsHandler.viewVPSDetails(results.get(0).getVpsId());
				return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
//				return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE, VendorProvisioningStatusHandler.REQUEST_PARAM, results.get(0).getVpsId());
			} else if (results == null || results.size() < 1) {
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

		this.tabHandler.displayFormSubmittedTab();
		logger.log(Level.DEBUG, "FINISH send request now button click.");
		return NavigationStrings.CURRENT_VIEW;
	}

	public String processRequestButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean specialCharFoundFlag = false;
		if (isNotNull(this.getVendorRequest().getData().getSerialNumber()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getSerialNumber().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getVendorRequest().getData().getContractId()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getContractId().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getVendorRequest().getData().getPrimarySku()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getPrimarySku().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getVendorRequest().getData().getRelatedSku()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getRelatedSku().trim())) {
			specialCharFoundFlag= true;
		}else if (isNotNull(this.getVendorRequest().getData().getFpkStoreId()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getFpkStoreId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getFpkRegisterId()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getFpkRegisterId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getFpkTransactionId()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getFpkTransactionId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getLineItemId()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getLineItemId().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getPromoSku()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getPromoSku().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getPromoPrice()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getPromoPrice().trim())) {
			specialCharFoundFlag= true;
		} else if (isNotNull(this.getVendorRequest().getData().getPromoCode()) && TextFilter.isSpecialCharacterPresent(this.getVendorRequest().getData().getPromoCode().trim())) {
			specialCharFoundFlag= true;
		} 
		
		if(specialCharFoundFlag){
			messageFlag= specialCharacterFoundInsertFlag;
			logger.log(Level.DEBUG, "specialCharacterFoundInsertFlag :: "+isSpecialCharacterFoundInsertFlag());
			
			return NavigationStrings.CURRENT_VIEW;
		}else{
			messageFlag = defaultMessageFlag;
		if(this.indefiniteSubscription){
			this.vendorRequest.getData().setContractEndDate(null);	
		}else{
			if(this.formHandler.isRenewS2FormCurrent() && this.vendorRequest.getData().getContractEndDate() ==null){
				context.addMessage("requestForm_panel_form:vendorRequest_s2_renew_contract_end_date",
						new FacesMessage("Please enter Contract end date."));
				return NavigationStrings.CURRENT_VIEW;
			}
			if(this.formHandler.isActivateS2FormCurrent() && this.vendorRequest.getData().getContractEndDate() ==null){
				context.addMessage("requestForm_panel_form:vendorRequest_s2_activate_contract_end_date",
						new FacesMessage("Please enter Contract end date."));
				return NavigationStrings.CURRENT_VIEW;
			}
			
		}
		logger.log(Level.DEBUG, "BEGIN process request button click.");
		renderPopup = !renderPopup;
		logger.log(Level.DEBUG, "FINISH process request button click.");
		return NavigationStrings.CURRENT_VIEW;
		}
	}

	public String viewOrderButtonClick() {
		logger.log(Level.DEBUG, "BEGIN view order button click.");

		try {
			List<VendorProvisioningStatusBean> results = SdpTransactionDBWrapper.getVendorProvisioningSearchResultBySdpId(this.getVendorRequest().getData().getSdpId());

			if (results == null || results.size() < 1) {
				multipleRecordsFound = false;
				renderPopup = !renderPopup;
				logger.log(Level.INFO, "FINISH view order button click. No orders found.");
				return NavigationStrings.CURRENT_VIEW;
			} else if (results.size() > 1) {
				logger.log(Level.WARN, "Found multiple results <" + results.size() + "> with matching lineitem id <" + this.getVendorRequest().getData().getSdpId() + ">.");
				multipleRecordsFound = true;
				renderPopup = !renderPopup;
				logger.log(Level.INFO, "FINISH view order button click. Multiple orders found.");
				return NavigationStrings.CURRENT_VIEW;
			} else {
				multipleRecordsFound = false;
				logger.log(Level.INFO, "FINISH view order button click. Navigating to order detail page.");
				VendorProvisioningStatusHandler vpsHandler= BasePageHandler.getVPSHandler();
				
				vpsHandler.viewVPSDetails(results.get(0).getVpsId());
				return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
//				return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE, VendorProvisioningStatusHandler.REQUEST_PARAM, results.get(0).getVpsId());
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

	private void retrieveTransactionDetails() throws DataSourceLookupException, DataAccessException {
		if (TextFilter.isSpecialCharacterPresent(inputSku)) {
			logger.log(Level.DEBUG,
			"Special Character found. specialCharacterFoundFlag set.");
			messageFlag = specialCharacterFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
		} else{List<VendorCatalogBean> catalogBeans = CatalogDBWrapper.getVendorCatalogByPrimarySku(this.inputSku);
		if (catalogBeans == null || catalogBeans.size() < 1) {
			logger.log(Level.DEBUG, "No results found. skuNotFoundFlag set.");
			messageFlag = skuNotFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
			this.vendorId = null;
		} else if (catalogBeans.size() > 1) {
			logger.log(Level.DEBUG, catalogBeans.size() + " results found. multipleSkusFoundFlag set.");
			messageFlag = multipleSkusFoundFlag;
			this.category = null;
			this.description = null;
			this.vendor = null;
			this.primarySku = null;
			this.vendorId = null;
		} else {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "Found 1 catalog entry:\n" + catalogBeans.get(0));
			}
			this.category = catalogBeans.get(0).getVendorCategory();
			this.description = catalogBeans.get(0).getPrimarySkuDescription();
			this.vendor = catalogBeans.get(0).getVendorName();
			this.primarySku = catalogBeans.get(0).getPrimarySku();
			this.vendorId = catalogBeans.get(0).getVendorId();
		}
		}
	}

	public String displayActivateS2ButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayActivateS2Form();
		this.operation = Operation.ACT;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayCancelS2ButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayCancelS2Form();
		this.operation = Operation.CNL;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayRenewS2ButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayRenewS2Form();
		this.operation = Operation.RNW;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayUpdateStatusS2ButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayUpdateStatusS2Form();
		this.operation = Operation.UPD;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		this.promotion = isPromotionMandatory(this.vendorRequest.getData().getInProductMessageCode(), SdpConfigProperties.getPromoTypes());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayActivatePsaButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayActivatePsaForm();
		this.operation = Operation.ACT;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayCancelPsaButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayCancelPsaForm();
		this.operation = Operation.CNL;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayActivatePosaButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayActivatePosaForm();
		this.operation = Operation.ACT;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String displayCancelPosaButtonClick() {
		this.tabHandler.displayFormTab();
		this.formHandler.displayCancelPosaForm();
		this.operation = Operation.CNL;
		this.sourceSystem = Constants.DEFAULT_SOURCE_SYSTEM;
		this.vendorRequest.getData().setSourceSystemId(Constants.DEFAULT_SOURCE_SYSTEM);
		this.vendorRequest.getData().setPrimarySku(this.primarySku);
		this.vendorRequest.getData().setVendorId(this.vendorId);
		this.vendorRequest.getData().setProductDescription(this.getDescription());
		this.vendorRequest.getData().setCategory(this.getCategory());
		return NavigationStrings.CURRENT_VIEW;
	}

	public String getOperationName() {
		if (operation == null) {
			return null;
		} else {
			return operation.getName();
		}
	}

	public enum Operation {
		NO_OPERATION(null), ACT("Activate"), CNL("Cancel"), RNW("Renew"), UPD("Update Status");
		private Operation(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}
	}

	// the following methods relate to the promo pricing fields in the
	// update status vendor form
	public boolean promotion;

	public boolean isPromotion() {
		return promotion;
	}

	public void setPromotion(boolean promotion) {
		this.promotion = promotion;
	}

	public void updateCodeValueChanged(ValueChangeEvent e) {
		logger.log(Level.DEBUG, "Value change listener fired: updateCodeValueChanged");
		if (e != null && e.getNewValue() != null) {
			String updateCode = (String) e.getNewValue();
			this.getVendorRequest().getData().setInProductMessageCode(updateCode);
			promotion = isPromotionMandatory(updateCode, SdpConfigProperties.getPromoTypes());
		} else {
			this.getVendorRequest().getData().setInProductMessageCode(null);
			promotion = false;
		}
	}

	public static boolean isPromotionMandatory(String updateCode, String[] promotionTypes) {
		if (promotionTypes != null && updateCode != null) {
			for (String promotionType : promotionTypes) {
				logger.log(Level.DEBUG, "PromotionType >> " + promotionType + " : " + updateCode);
				if (promotionType != null && updateCode.equalsIgnoreCase(promotionType.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Initialized the direct to vendor page handler. There are three possible
	 * outcomes from calling this constructor:
	 * <ol>
	 * <li>If vpsId is present in the context then will attempt to retrieve the
	 * vps record and initialize the page by setting the default properties,
	 * parse the transaciton data out of the vps BBYOrder, and then will attempt
	 * to determine the correct oparation based on the vps details. If
	 * unsuccessful then an UnsupportedOperationException is thrown.</li>
	 * <li>If vpsId is null but sdpOrderId is present in the context then will
	 * attempt to retrieve transaction details from the sdp_order and related
	 * tables directly. In this case the user will need to manually select the
	 * appropriate operation.</li>
	 * <li>If both vpsId and sdpOrderId are null then it assumed that the user
	 * is going to create a new vendor request from scratch by providing the sku
	 * and then selecting the operation</li>
	 * </ol>
	 * </p>
	 * 
	 * @throws DataAccessException
	 *             if unable to retrieve data to prepopulate the form
	 * @throws DataSourceLookupException
	 *             if unable to connect to data source
	 * @throws XmlException
	 *             if the BBYOrder xml for reflowing existing transactions is
	 *             invalid
	 * @throws ParseException
	 *             if unable to parse the BBYOrder xml when reflowing existing
	 *             transactions
	 * @throws IllegalArgumentException
	 *             if the vps record has an invalid request type
	 * @throws UnsupportedOperationException
	 *             if the vps operation is not supported or is null
	 */
	public DirectToVendorHandler() throws DataAccessException, DataSourceLookupException, XmlException, ParseException {
		// get the sdpOrderId and vpsId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String vpsId = facesContext.getExternalContext().getRequestParameterMap().get(VPS_REQUEST_PARAM);
		String sdpOrderId = facesContext.getExternalContext().getRequestParameterMap().get(SDP_ORDER_REQUEST_PARAM);

		// if vpsId found in context param, prepopulate the form with the bby
		// order
		if (vpsId != null) {
			this.vpsId = vpsId;
			logger.log(Level.DEBUG, "vpsId found >> " + vpsId + ". Attempting to retrieve vps record with matching id.");
			VendorProvisioningStatusBean vps = VendorProvisioningStatusDBWrapper.getVendorProvisioningStatusBeanByVendorProvisioningStatusId(vpsId);
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "Found VPS record >> \n" + vps.toString());
			}
			if (vps != null && vps.getBbyOrder() != null && vps.getRequestType() != null) {

				BBYOrderDocument doc = BBYOrderDocument.Factory.parse(vps.getBbyOrder());
				SdpTransactionDataBean bean = SdpTransactionDataXml.getSdpTransactionDataBean(doc);

				this.inputSku = bean.getPrimarySku();
				if (this.inputSku == null) {
					logger.log(Level.WARN, "Primary SKU retrieved from BBYOrder was missing or null. Displaying blank reflow form instead.");
					this.vpsId = null; // reset vpsId to null
					return;
				}
				this.sdpOrderId = bean.getSdpOrderId();

				this.searchBySkuButtonClick();
				if (this.isCategoryUnsupported()) {
					throw new IllegalArgumentException("VPS transaciton with ID [" + vpsId + "] does not have a supported category for reflow");
				}
				if (this.isCategoryS2()) {
					if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
						this.displayActivateS2ButtonClick();
					} else if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
						this.displayCancelS2ButtonClick();
					} else if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRenew())) {
						this.displayRenewS2ButtonClick();
					} else if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeModifyStatus())) {
						this.displayUpdateStatusS2ButtonClick();
					} else {
						throw new UnsupportedOperationException("OpsUI does not support reflows for S2 request type [" + vps.getRequestType() + "]");
					}
				} else if (this.isCategoryPsa()) {
					if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
						this.displayActivatePsaButtonClick();
					} else if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
						this.displayCancelPsaButtonClick();
					} else {
						throw new UnsupportedOperationException("OpsUI does not support reflows for PSA request type [" + vps.getRequestType() + "]");
					}
				} else if (this.isCategoryPosa()) {
					if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate())) {
						this.displayActivatePosaButtonClick();
					} else if (vps.getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())) {
						this.displayCancelPosaButtonClick();
					} else {
						throw new UnsupportedOperationException("OpsUI does not support reflows for POSA request type [" + vps.getRequestType() + "]");
					}
				} else {
					throw new UnsupportedOperationException("OpsUI does not support reflows for [" + this.category + "] request type [" + vps.getRequestType() + "]");
				}
				this.vendorRequest.setData(bean);
				this.vendorRequest.setOriginalData(bean);
			} else {
				logger.log(Level.ERROR, "VPS record with ID=[" + vpsId + "] was not found. Displaying blank reflow screen instead.");
			}

			// if sdp order id is populated in context param then
			// initialize sku category and force user to select the form
		} else if (sdpOrderId != null) {
			SdpTransactionDataBean bean = SdpOrderDetailDBWrapper.getSdpOrderDetailBySdpOrderId(sdpOrderId);
			this.inputSku = bean.getPrimarySku();
			this.sdpOrderId = bean.getSdpOrderId();
			this.searchBySkuButtonClick();
			this.vendorRequest.setData(bean);
		} else {
			// if no input parameters then initialize blank form for reflow from
			// scratch
		}
	}

	public boolean isReflowFromSdpOrderDetail() {
		return this.sdpOrderId != null;
	}

	public boolean isReflowFromVendorProvisioningStatus() {
		return this.vpsId != null;
	}

	public String backToSdpOrderDetailButtonClick() {
		SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
		sdpOrderDetailHandler.viewOrderDetailButtonClick(this.sdpOrderId);
		return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.sdpOrderId);
	}

	public String backToVendorProvisioningStatusButtonClick() {
		VendorProvisioningStatusHandler vpsHandler= BasePageHandler.getVPSHandler();
		
		vpsHandler.viewVPSDetails(this.vpsId);
		return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.vpsId);
	}
	
	private boolean indefiniteSubscription;
	
	public void indefiniteSubscriptionSet(ValueChangeEvent e){
		indefiniteSubscription = e.getNewValue().toString().equalsIgnoreCase("true");
		if(indefiniteSubscription){
			this.vendorRequest.getData().setContractEndDate(null);
		}
	}

	public boolean isIndefiniteSubscription() {
		return indefiniteSubscription;
	}

	public void setIndefiniteSubscription(boolean indefiniteSubscription) {
		this.indefiniteSubscription = indefiniteSubscription;
	}

	public static int getSpecialCharacterFoundFlag() {
		return specialCharacterFoundFlag;
	}
	
	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
