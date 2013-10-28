package com.accenture.bby.sdp.web.handlers.sdpOrderSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.SdpOrderSearchDBWrapper;
import com.accenture.bby.sdp.db.VendorProvisioningStatusDBWrapper;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpOrderSearchResultBean;
import com.accenture.bby.sdp.web.beans.VendorProvisioningStatusBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;
import com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus.VendorProvisioningStatusHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ManagedBean(name = "sdpOrderSearchResultsDatatable")
@ViewScoped
public class SdpOrderSearchResultsDatatable implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(SdpOrderSearchResultsDatatable.class.getName());

	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	protected static int MAX_QUERY_RESULTS = 500;
	protected static final String NULL_PARAMETER_PASSED = "null parameter passed";
	private boolean initializeByRecent;

	public SdpOrderSearchResultsDatatable() {
		if (this.getFpkTransactionDateSearchField() == null) {
			this.setFpkTransactionDateSearchField(new Date(System.currentTimeMillis()));
		}
		if (this.getErrorStatusDateStart() == null) {
			this.setErrorStatusDateStart(DateUtil.clearTime(new Date()));
			if (this.getErrorStatusDateEnd() == null) {
				this.setErrorStatusDateEnd(DateUtil.clearTime(new Date(System.currentTimeMillis() + (86400000))));
			}
		}
		
		if (this.getCustSearchDateStart() == null) {
			this.setCustSearchDateStart(DateUtil.clearTime(new Date()));
			if (this.getCustSearchDateEnd() == null) {
				this.setCustSearchDateEnd(DateUtil.clearTime(new Date(System.currentTimeMillis() + (86400000))));
			}
		}
		
		if (this.getVendorSearchDateStart() == null) {
			this.setVendorSearchDateStart(DateUtil.clearTime(new Date()));
			if (this.getVendorSearchDateEnd() == null) {
				this.setVendorSearchDateEnd(DateUtil.clearTime(new Date(System.currentTimeMillis() + (86400000))));
			}
		}
		this.setInitializeByRecent(true);
	}

	public boolean isInitializeByRecent() {
		return initializeByRecent;
	}

	public void setInitializeByRecent(boolean initializeByRecent) {
		this.initializeByRecent = initializeByRecent;
	}

	// public SdpOrderSearchResultsDatatable(boolean initializeByRecent) {
	// this.initializeByRecent = initializeByRecent;
	// }

	private HtmlDataTable orderDataTable;

	public HtmlDataTable getOrderDataTable() {
		return orderDataTable;
	}

	public void setOrderDataTable(HtmlDataTable dataTable) {
		this.orderDataTable = dataTable;
	}

	private HtmlDataTable vendorDataTable;

	public HtmlDataTable getVendorDataTable() {
		return vendorDataTable;
	}

	public void setVendorDataTable(HtmlDataTable dataTable) {
		this.vendorDataTable = dataTable;
	}

	private List<SdpOrderSearchResultBean> rows;

	public List<SdpOrderSearchResultBean> getRows()
			throws DataSourceLookupException, DataAccessException {
		if (rows == null && initializeByRecent) {
			rows = SdpOrderSearchDBWrapper.getSdpOrderSearchResultsByRecent(
					SdpConfigProperties.getMaxOrderQueryResults(), maxRows);
		}
		logger.log(Level.DEBUG, "Sdp Order records found >> " + rows.size());
		return rows;
	}

	public void setRows(List<SdpOrderSearchResultBean> rows) {
		this.rows = rows;
	}

	private List<VendorProvisioningStatusBean> vendorRows;

	public List<VendorProvisioningStatusBean> getVendorRows() {
		return vendorRows;
	}

	public void setVendorRows(List<VendorProvisioningStatusBean> vendorRows) {
		this.vendorRows = vendorRows;
	}

	public boolean isTableVendorEmpty() {
		return vendorRows == null || vendorRows.size() == 0;
	}

	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}

	private Integer maxRows = 20;

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public String viewOrderDetailButtonClick() {
		SdpOrderSearchResultBean selectedRow = (SdpOrderSearchResultBean) orderDataTable
				.getRowData();
		if (selectedRow != null) {
			SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
			sdpOrderDetailHandler.viewOrderDetailButtonClick(selectedRow.getSdpOrderId());
			return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
		} else {
			logger.log(Level.DEBUG, "selectedRow returned null so "
					+ SdpOrderDetailHandler.REQUEST_PARAM
					+ " param will not be set.");
			return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
		}
	}

	public String refreshButtonClick() {
		try {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "BEGIN retrieving " + maxRows
						+ " most recent records going back "
						+ SdpConfigProperties.getMaxOrderQueryResults()
						+ " days.");
			}
			rows = SdpOrderSearchDBWrapper.getSdpOrderSearchResultsByRecent(
					SdpConfigProperties.getMaxOrderQueryResults(), maxRows);
			logger.log(Level.DEBUG, "FINISH retrieving " + maxRows
					+ " most recent records, found >> " + rows.size()
					+ " records.");
			return NavigationStrings.CURRENT_VIEW;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}

	// ******************************************************************************************************
	// Search Page Methods

	// ******************************************************************************************************
	// Single Field Search
	private String contractIdSearchField;
	private String serialNumberSearchField;
	private String lineItemIdSearchField;
	private String phoneNumSearchField;
	private String customerEmailSearchField;
	private String deliveryEmailSearchField;
	private String confirmationCodeSearchField;

	public String searchByContractIdButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getContractIdSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getContractIdSearchField()
					.trim())) {
				logger.log(Level.DEBUG, "Special Character Found (ContractId) ::"
						+ this.getContractIdSearchField());
				context.addMessage(
						"order_cntrctID_search_form:search_cntrctID",
						new FacesMessage("Special characters are not allowed."));
					return NavigationStrings.CURRENT_VIEW;
			} 
			try {
				this.setRows(SdpOrderSearchDBWrapper.getByContractId(this
						.getContractIdSearchField().trim()));
				this.setInitializeByRecent(false);
				this.getSdpOrderSearchTabbedPaneHandler()
						.displaySearchResultTab();
				// } else {
				// context.addMessage(null, new FacesMessage(
				// "Order Not Found."));
				// }
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_cntrctID_search_form:search_cntrctID",
				new FacesMessage("Please enter valid Contract ID."));

		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchBySerialNumberButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getSerialNumberSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getSerialNumberSearchField()
					.trim())) {
				logger.log(Level.DEBUG, "Special Character Found (SerialNumber) ::"
						+ this.getSerialNumberSearchField());
				context.addMessage(
						"order_serialNum_search_form:search_serialNum",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			} 
			try {
				this.setRows(SdpOrderSearchDBWrapper.getBySerialNumber(this
						.getSerialNumberSearchField().trim()));
				this.setInitializeByRecent(false);
				this.getSdpOrderSearchTabbedPaneHandler()
						.displaySearchResultTab();
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_serialNum_search_form:search_serialNum",
				new FacesMessage("Please enter valid Serial Number."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByLineItemIdButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getLineItemIdSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getLineItemIdSearchField()
					.trim())) {
				logger.log(Level.DEBUG, "Special Character Found (LineItemId) ::"
						+ this.getLineItemIdSearchField());
				context.addMessage(
						"order_lineItemId_search_form:search_lineItemId",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				this.setRows(SdpOrderSearchDBWrapper.getByLineItemId(this
						.getLineItemIdSearchField().trim()));
				this.setInitializeByRecent(false);
				this.getSdpOrderSearchTabbedPaneHandler()
						.displaySearchResultTab();

				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_lineItemId_search_form:search_lineItemId",
				new FacesMessage("Please enter valid Line Item Id."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByPhoneNumButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getPhoneNumSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getPhoneNumSearchField().trim())) {
				logger.log(Level.DEBUG, "Special Character Found (PhoneNum)::"
						+ this.getPhoneNumSearchField());
				context.addMessage(
						"order_phoneNum_search_form:search_phoneNum",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				this.setRows(SdpOrderSearchDBWrapper.getByPhoneNum(this
						.getPhoneNumSearchField().trim()));
				this.setInitializeByRecent(false);
				this.getSdpOrderSearchTabbedPaneHandler()
						.displaySearchResultTab();
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_phoneNum_search_form:search_phoneNum",
				new FacesMessage("Please enter valid Phone Number."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByCustomerEmailButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getCustomerEmailSearchField())) {
			if (EmailValidation(this.getCustomerEmailSearchField().trim())) {
				if (TextFilter.isSpecialCharacterPresent(this
						.getCustomerEmailSearchField().trim())) {
					logger.log(Level.DEBUG, "Special Character Found (CustomerEmail)::"
							+ this.getCustomerEmailSearchField());
					context
							.addMessage(
									"order_cusEmail_search_form:search_cusEmail",
									new FacesMessage(
											"Special characters not allowed."));
					return NavigationStrings.CURRENT_VIEW;
				}
				try {
					this.setRows(SdpOrderSearchDBWrapper
							.getByCustomerEmail(this
									.getCustomerEmailSearchField().trim()));
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchResultTab();
					return NavigationStrings.CURRENT_VIEW;
				} catch (DataAccessException e) {
					exceptionHandler.initialize(e,
							"Failure occurred while retrieving order list.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (DataSourceLookupException e) {
					exceptionHandler.initialize(e,
							"Failed to open data source connection.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				}
			}
			context.addMessage("order_cusEmail_search_form:search_cusEmail",
					new FacesMessage("Please enter valid Customer Email."));
			return NavigationStrings.CURRENT_VIEW;
		}
		context.addMessage("order_cusEmail_search_form:search_cusEmail",
				new FacesMessage("Please enter Customer Email."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByDeliveryEmailButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getDeliveryEmailSearchField())) {
			if (EmailValidation(this.getDeliveryEmailSearchField().trim())) {
				if (TextFilter.isSpecialCharacterPresent(this
						.getDeliveryEmailSearchField().trim())) {
					logger.log(Level.DEBUG, "Special Character Found (DeliveryEmail)::"
							+ this.getDeliveryEmailSearchField());
					context
							.addMessage(
									"order_deliveryEmail_search_form:search_deliveryEmail",
									new FacesMessage(
											"Special characters not allowed."));
					return NavigationStrings.CURRENT_VIEW;
				}
				try {
					this.setRows(SdpOrderSearchDBWrapper
							.getByDeliveryEmail(this
									.getDeliveryEmailSearchField().trim()));
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchResultTab();

					return NavigationStrings.CURRENT_VIEW;
				} catch (DataAccessException e) {
					exceptionHandler.initialize(e,
							"Failure occurred while retrieving order list.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (DataSourceLookupException e) {
					exceptionHandler.initialize(e,
							"Failed to open data source connection.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				}
			}
			context.addMessage(
					"order_deliveryEmail_search_form:search_deliveryEmail",
					new FacesMessage("Please enter valid Delivery Email."));
			return NavigationStrings.CURRENT_VIEW;
		}
		context.addMessage(
				"order_deliveryEmail_search_form:search_deliveryEmail",
				new FacesMessage("Please enter Delivery Email."));

		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByConfirmationCodeButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getConfirmationCodeSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getConfirmationCodeSearchField()
					.trim())) {
				logger.log(Level.DEBUG, "Special Character Found (ConfirmationCode)::"
						+ this.getConfirmationCodeSearchField());
				context.addMessage(
						"order_confirmCde_search_form:search_confirmCde",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				this.setRows(SdpOrderSearchDBWrapper.getByConfirmationCode(this
						.getConfirmationCodeSearchField().trim()));
				this.setInitializeByRecent(false);
				this.getSdpOrderSearchTabbedPaneHandler()
						.displaySearchResultTab();

				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_confirmCde_search_form:search_confirmCde",
				new FacesMessage("Please enter valid confirmation Code."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String getContractIdSearchField() {
		return contractIdSearchField;
	}

	public void setContractIdSearchField(String contractIdSearchField) {
		this.contractIdSearchField = contractIdSearchField;
	}

	public String getSerialNumberSearchField() {
		return serialNumberSearchField;
	}

	public void setSerialNumberSearchField(String serialNumberSearchField) {
		this.serialNumberSearchField = 
				serialNumberSearchField;
	}

	public String getLineItemIdSearchField() {
		return lineItemIdSearchField;
	}

	public void setLineItemIdSearchField(String lineItemIdSearchField) {
		this.lineItemIdSearchField = lineItemIdSearchField;
	}

	public String getPhoneNumSearchField() {
		return phoneNumSearchField;
	}

	public void setPhoneNumSearchField(String phoneNumSearchField) {
		this.phoneNumSearchField = phoneNumSearchField;
	}

	public String getCustomerEmailSearchField() {
		return customerEmailSearchField;
	}

	public void setCustomerEmailSearchField(String customerEmailSearchField) {
		this.customerEmailSearchField = 
				customerEmailSearchField;
	}

	public String getDeliveryEmailSearchField() {
		return deliveryEmailSearchField;
	}

	public void setDeliveryEmailSearchField(String deliveryEmailSearchField) {
		this.deliveryEmailSearchField = 
				deliveryEmailSearchField;
	}

	public String getConfirmationCodeSearchField() {
		return confirmationCodeSearchField;
	}

	public void setConfirmationCodeSearchField(
			String confirmationCodeSearchField) {
		this.confirmationCodeSearchField = 
				confirmationCodeSearchField;
	}

	// ******************************************************************************************************
	// Search by Transaction
	private Date fpkTransactionDateSearchField;
	private String fpkStoreIdSearchField;
	private String fpkRegisterIdSearchField;
	private String fpkTransactionIdSearchField;
	private String fpkLineIdSearchField;

	public String searchByFivePartButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (this.getFpkTransactionDateSearchField() != null) {
			if ((isNotNull(this.getFpkStoreIdSearchField()))
					|| (isNotNull(this.getFpkRegisterIdSearchField()))
					|| (isNotNull(this.getFpkTransactionIdSearchField()))) {
				if ((TextFilter.isSpecialCharacterPresent(this.getFpkStoreIdSearchField()
						.trim()))
						|| (TextFilter.isSpecialCharacterPresent(this
								.getFpkRegisterIdSearchField().trim()))
						|| (TextFilter.isSpecialCharacterPresent(this
								.getFpkTransactionIdSearchField().trim()))) {
					logger.log(Level.DEBUG, "Special Character Found (StoreId)::"
							+ this.getFpkStoreIdSearchField() +" or (RegisterId) ::"+ this.getFpkRegisterIdSearchField() +" or (TransactionId) ::"+ this.getFpkTransactionIdSearchField());
					context
							.addMessage("order_5pk_search:search_date_4pk",
									new FacesMessage(
											"Special characters not allowed."));
					return NavigationStrings.CURRENT_VIEW;
				} 
				if ((isNotNull(this.getFpkLineIdSearchField()))
						&& (TextFilter.isSpecialCharacterPresent(this
								.getFpkLineIdSearchField().trim()))) {
					logger.log(Level.DEBUG, "Special Character Found (LineId)::"
							+ this.getFpkLineIdSearchField());
					context.addMessage("order_5pk_search:search_date_4pk",
							new FacesMessage(
									"Special characters not allowed."));
					return NavigationStrings.CURRENT_VIEW;
				}
				try {
					this.setRows(SdpOrderSearchDBWrapper.getByFivePartKey(this
							.getFpkTransactionDateSearchField(), this
							.getFpkStoreIdSearchField(), this
							.getFpkRegisterIdSearchField(), this
							.getFpkTransactionIdSearchField(), this
							.getFpkLineIdSearchField()));
					if (this.getRows() != null) {
						this.setInitializeByRecent(false);
						this.getSdpOrderSearchTabbedPaneHandler()
								.displaySearchResultTab();
					} else {
						// context.addMessage("search_panel:order_not_found",
						// new FacesMessage(
						// //"Order Not Found."));
					}
					return NavigationStrings.CURRENT_VIEW;
				} catch (DataAccessException e) {
					exceptionHandler.initialize(e,
							"Failure occurred while retrieving order list.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (DataSourceLookupException e) {
					exceptionHandler.initialize(e,
							"Failed to open data source connection.");
					logger.log(Level.ERROR, exceptionHandler.toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				}
			} else {
				context
						.addMessage(
								"order_5pk_search:search_date_4pk",
								new FacesMessage(
										"Transaction Date and at least one/all of [store id, register id, transaction id] parameters is/are manadatory."));
				return NavigationStrings.CURRENT_VIEW;
			}
		}
		context
				.addMessage("order_5pk_search:search_date_4pk",
						new FacesMessage(
								"Date [MM/dd/yyyy] parameters cannot be null."));
		return NavigationStrings.CURRENT_VIEW;
	}

	public Date getFpkTransactionDateSearchField() {
		return fpkTransactionDateSearchField;
	}

	public void setFpkTransactionDateSearchField(
			Date fpkTransactionDateSearchField) {
		this.fpkTransactionDateSearchField = fpkTransactionDateSearchField;
	}

	public String getFpkStoreIdSearchField() {
		return fpkStoreIdSearchField;
	}

	public void setFpkStoreIdSearchField(String fpkStoreIdSearchField) {
		this.fpkStoreIdSearchField = fpkStoreIdSearchField;
	}

	public String getFpkRegisterIdSearchField() {
		return fpkRegisterIdSearchField;
	}

	public void setFpkRegisterIdSearchField(String fpkRegisterIdSearchField) {
		this.fpkRegisterIdSearchField = 
				fpkRegisterIdSearchField;
	}

	public String getFpkTransactionIdSearchField() {
		return fpkTransactionIdSearchField;
	}

	public void setFpkTransactionIdSearchField(
			String fpkTransactionIdSearchField) {
		this.fpkTransactionIdSearchField = 
				fpkTransactionIdSearchField;
	}

	public String getFpkLineIdSearchField() {
		return fpkLineIdSearchField;
	}

	public void setFpkLineIdSearchField(String fpkLineIdSearchField) {
		this.fpkLineIdSearchField = fpkLineIdSearchField;
	}

	private String bbyOrderIdSearchField;

	public String getBbyOrderIdSearchField() {
		return bbyOrderIdSearchField;
	}

	public void setBbyOrderIdSearchField(String bbyOrderIdSearchField) {
		this.bbyOrderIdSearchField = bbyOrderIdSearchField;
	}

	public String searchByTransactionIdExecuteButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getBbyOrderIdSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this.getBbyOrderIdSearchField()
					.trim())) {
				logger.log(Level.DEBUG, "Special Character Found (BBYOrderID)::"
						+ this.getBbyOrderIdSearchField());
				context.addMessage("order_bbyId_search:search_BBYID",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				this.setRows(SdpOrderSearchDBWrapper
						.getByDotcomOrderNumber(this.getBbyOrderIdSearchField()
								.trim()));
				if (this.getRows() != null) {
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchResultTab();
				} else {
					// context.addMessage("search_panel:order_not_found", new
					// FacesMessage(
					// //"Order Not Found."));
				}
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("order_bbyId_search:search_BBYID", new FacesMessage(
				"Please Enter valid BBY OrderId."));
		return NavigationStrings.CURRENT_VIEW;
	}

	// ******************************************************************************************************
	// Search by Order Status

	private String primarySkuSearchField;
	private String masterItemIdSearchField;
	private String orderStatusSearchField;
	private String errorCodeSearchField;
	private Date errorStatusDateStart;
	private Date errorStatusDateEnd;

	public Date getErrorStatusDateStart() {
		return errorStatusDateStart;
	}

	public void setErrorStatusDateStart(Date errorStatusDateStart) {
		this.errorStatusDateStart = errorStatusDateStart;
	}

	public Date getErrorStatusDateEnd() {
		return errorStatusDateEnd;
	}

	public void setErrorStatusDateEnd(Date errorStatusDateEnd) {
		this.errorStatusDateEnd = errorStatusDateEnd;
	}

	public void setPrimarySkuSearchField(String primarySkuSearchField) {
		this.primarySkuSearchField = primarySkuSearchField;
	}

	public String getPrimarySkuSearchField() {
		return primarySkuSearchField;
	}

	public String getMasterItemIdSearchField() {
		return masterItemIdSearchField;
	}

	public void setMasterItemIdSearchField(String masterItemIdSearchField) {
		this.masterItemIdSearchField = 
				masterItemIdSearchField;
	}

	public String getOrderStatusSearchField() {
		return orderStatusSearchField;
	}

	public void setOrderStatusSearchField(String orderStatusSearchField) {
		this.orderStatusSearchField = orderStatusSearchField;
	}

	public String getErrorCodeSearchField() {
		return errorCodeSearchField;
	}

	public void setErrorCodeSearchField(String errorCodeSearchField) {
		this.errorCodeSearchField = errorCodeSearchField;
	}

	public String searchbyOrderStatusExecuteButtonClick() {

		FacesContext context = FacesContext.getCurrentInstance();

		if (((isNotNull(this.getPrimarySkuSearchField())
				|| isNotNull(this.getMasterItemIdSearchField()) || isNotNull(this
				.getOrderStatusSearchField())))
				&& (this.getErrorStatusDateStart() != null)) {
			if ((TextFilter.isSpecialCharacterPresent(this.getPrimarySkuSearchField()
					.trim()))
					|| (TextFilter.isSpecialCharacterPresent(this
							.getMasterItemIdSearchField().trim()))) {
				logger.log(Level.DEBUG, "Special Character Found (PrimarySku)::"
						+ this.getPrimarySkuSearchField() +" or(MasterItemId) ::" + this.getMasterItemIdSearchField());
				context.addMessage("error_status_search:search_sku",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				if (this.getErrorStatusDateEnd() == null
						|| this.getErrorStatusDateEnd().equals(
								this.getErrorStatusDateStart())) {
					this.setErrorStatusDateEnd(new Date(this
							.getErrorStatusDateStart().getTime() + 86400000));
				}
				this.setRows(SdpOrderSearchDBWrapper.getByOrderStatus(this
						.getPrimarySkuSearchField(), this
						.getMasterItemIdSearchField(), this
						.getOrderStatusSearchField(), this
						.getErrorStatusDateStart(), new Date(this
						.getErrorStatusDateEnd().getTime() + 86400000),
						MAX_QUERY_RESULTS + 1));
				if (this.getRows() != null) {
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchResultTab();
				} else {
					// context.addMessage("search_panel:order_not_found", new
					// FacesMessage(
					// //"Order Not Found."));
				}
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context
				.addMessage(
						"error_status_search:search_sku",
						new FacesMessage(
								"Please Enter Primary Sku / Master Item Id/ order Status and Start Date "));
		return NavigationStrings.CURRENT_VIEW;
	}

	// ******************************************************************************************************

	// ******************************************************************************************************
	// Search by Customer name

	private String firstNameSearchField;
	private String lastNameSearchField;
	private Date custSearchDateStart;
	private Date custSearchDateEnd;

	public String getFirstNameSearchField() {
		return firstNameSearchField;
	}

	public void setFirstNameSearchField(String firstNameSearchField) {
		this.firstNameSearchField = firstNameSearchField;
	}

	public String getLastNameSearchField() {
		return lastNameSearchField;
	}

	public void setLastNameSearchField(String lastNameSearchField) {
		this.lastNameSearchField = lastNameSearchField;
	}

	public Date getCustSearchDateStart() {
		return custSearchDateStart;
	}

	public void setCustSearchDateStart(Date custSearchDateStart) {
		this.custSearchDateStart = custSearchDateStart;
	}

	public Date getCustSearchDateEnd() {
		return custSearchDateEnd;
	}

	public void setCustSearchDateEnd(Date custSearchDateEnd) {
		this.custSearchDateEnd = custSearchDateEnd;
	}

	public String searchByNameExecuteButtonClick() {

		FacesContext context = FacesContext.getCurrentInstance();

		if (((isNotNull(this.getFirstNameSearchField())) || (isNotNull(this
				.getLastNameSearchField())))
				&& (this.getCustSearchDateStart() != null)) {

			if ((TextFilter.isSpecialCharacterPresent(this.getFirstNameSearchField()
					.trim()))
					|| (TextFilter.isSpecialCharacterPresent(this
							.getLastNameSearchField().trim()))) {
				logger.log(Level.DEBUG, "Special Character Found (FirstName)::"
						+ this.getFirstNameSearchField() +" or (LastName) ::"+this.getLastNameSearchField());
				context.addMessage(
						"cus_name_search:search_frst_name",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			if (this.getCustSearchDateEnd() == null
					|| this.getCustSearchDateEnd().equals(
							this.getCustSearchDateStart())) {
				this.setCustSearchDateEnd(new Date(this
						.getCustSearchDateStart().getTime() + 86400000));
			}
			try {

				final String firstNameParameter = this
						.getFirstNameSearchField() != null ? this
						.getFirstNameSearchField().trim() : null;
				final String lastNameParameter = this.getLastNameSearchField() != null ? this
						.getLastNameSearchField().trim()
						: null;

				// if values are zero length string then set to null instead.

				this
						.setRows(SdpOrderSearchDBWrapper
								.getByCustomerName(
										!"".equals(firstNameParameter) ? firstNameParameter
												: null,
										!"".equals(lastNameParameter) ? lastNameParameter
												: null, this
												.getCustSearchDateStart(),
										new Date(this.getCustSearchDateEnd()
												.getTime() + 86400000),
										MAX_QUERY_RESULTS + 1));
				if (this.getRows() != null) {
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchResultTab();
				} else {
					// context.addMessage("search_panel:order_not_found", new
					// FacesMessage(
					// //"Order Not Found."));
				}
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("cus_name_search:search_frst_name",
				new FacesMessage(
						"Please Enter First Name, Last Name and Start Date"));
		return NavigationStrings.CURRENT_VIEW;

	}

	// ******************************************************************************************************
	// Search by Vendor provisioning

	private List<SelectItem> vendorCodeList = new ArrayList<SelectItem>();
	private String responseCodeSearchField;
	private String vendorIdSearchField;
	private Date vendorSearchDateStart;
	private Date vendorSearchDateEnd;

//	public void updateVendorCodeList(ValueChangeEvent vce) {
//		try {
//			this.setVendorIdSearchField((String) vce.getNewValue());
//			if (this.getVendorIdSearchField() == null
//					|| "ALL".equals(this.getVendorIdSearchField())
//					|| "".equals(this.getVendorIdSearchField())) {
//				this.setVendorCodeList(SdpOrderSearchDBWrapper
//						.getVendorCodeDropDownListByAllVendors());
//			} else {
//				this.setVendorCodeList(SdpOrderSearchDBWrapper
//						.getVendorCodeDropDownListByVendorId(this
//								.getVendorIdSearchField().trim()));
//			}
//		} catch (DataAccessException e) {
//			exceptionHandler.initialize(e,
//					"Failure occurred while retrieving order list.");
//			logger.log(Level.ERROR, exceptionHandler.toString(), e);
//		} catch (DataSourceLookupException e) {
//			exceptionHandler.initialize(e,
//					"Failed to open data source connection.");
//			logger.log(Level.ERROR, exceptionHandler.toString(), e);
//		}
//
//	}

	public void updateVendorCodeList(final ValueChangeEvent event) throws DataSourceLookupException, DataAccessException {
		this.setVendorIdSearchField(event.getNewValue().toString());
		updateVendorCodeList();
	}
	
	public void updateVendorCodeList() throws DataSourceLookupException, DataAccessException{
		if (this.getVendorIdSearchField() == null || "ALL".equals(this.getVendorIdSearchField()) || "".equals(this.getVendorIdSearchField())) {
			this.setVendorCodeList(SdpOrderSearchDBWrapper.getVendorCodeDropDownListByAllVendors());
		} else {
			this.setVendorCodeList(SdpOrderSearchDBWrapper.getVendorCodeDropDownListByVendorId(this.getVendorIdSearchField()));
		}
	}
	
	public List<SelectItem> getVendorCodeList() {
		return vendorCodeList;
	}

	public void setVendorCodeList(List<SelectItem> vendorCodeList) {
		this.vendorCodeList = vendorCodeList;
	}

	public String searchByVendorErrorCodeButtonClick() {

		FacesContext context = FacesContext.getCurrentInstance();

		if ((this.getVendorSearchDateStart() != null) && ((isNotNull(this
				.getVendorIdSearchField())) && (isNotNull(this
				.getResponseCodeSearchField())))) {

			try { // : Clean up

				Date endDate = null;

				if (this.getVendorSearchDateEnd() == null
						|| this.getVendorSearchDateEnd().equals(
								this.getVendorSearchDateStart())) {
					endDate = new Date(this.getVendorSearchDateStart()
							.getTime() + 86400000);
				} else {
					endDate = new Date(
							this.getVendorSearchDateEnd().getTime() + 86400000);
				}
				List<VendorProvisioningStatusBean> vendorProvisioningStatusBean = new ArrayList<VendorProvisioningStatusBean>();

				if ("ALL".equals(this.getVendorIdSearchField())) {
					vendorProvisioningStatusBean = VendorProvisioningStatusDBWrapper
							.getVPSListByVendorIdErrorCode(null, this
									.getResponseCodeSearchField(), this
									.getVendorSearchDateStart(), endDate,
									MAX_QUERY_RESULTS + 1);
				} else if ("ALL".equals(this.getResponseCodeSearchField())) {
					vendorProvisioningStatusBean = VendorProvisioningStatusDBWrapper
							.getVPSListByVendorIdErrorCode(this
									.getVendorIdSearchField(), null, this
									.getVendorSearchDateStart(), endDate,
									MAX_QUERY_RESULTS + 1);
				} else {
					vendorProvisioningStatusBean = VendorProvisioningStatusDBWrapper
							.getVPSListByVendorIdErrorCode(this
									.getVendorIdSearchField(), this
									.getResponseCodeSearchField(), this
									.getVendorSearchDateStart(), endDate,
									MAX_QUERY_RESULTS + 1);
				}

//				if (!validateResultListSize(vendorProvisioningStatusBean)) {
//					context.addMessage("vndr_provsn_search:search_cusEmails",
//							new FacesMessage("Order Not found"));
//
//				} else {
					this.setInitializeByRecent(false);
					this.getSdpOrderSearchTabbedPaneHandler()
							.displaySearchVendorResultTab();
					this.setVendorRows(vendorProvisioningStatusBean);
//				}
				// this.getVendorProvisioningStatusHandler().getVendorProvisioningStatusDatatable().setRows(vendorProvisioningStatusBean);

				return NavigationStrings.CURRENT_VIEW;

			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context
				.addMessage(
						"vndr_provsn_search:search_vendorid",
						new FacesMessage(
								"Vendor Id , Response code and Start Date are Mandatory"));
		return NavigationStrings.CURRENT_VIEW;
	}

	public String getVendorIdSearchField() {
		return vendorIdSearchField;
	}

	public void setVendorIdSearchField(String vendorIdSearchField) {
		this.vendorIdSearchField = vendorIdSearchField;
	}

	public String getResponseCodeSearchField() {
		return responseCodeSearchField;
	}

	public Date getVendorSearchDateStart() {
		return vendorSearchDateStart;
	}

	public void setVendorSearchDateStart(Date vendorSearchDateStart) {
		this.vendorSearchDateStart = vendorSearchDateStart;
	}

	public Date getVendorSearchDateEnd() {
		return vendorSearchDateEnd;
	}

	public void setVendorSearchDateEnd(Date vendorSearchDateEnd) {
		this.vendorSearchDateEnd = vendorSearchDateEnd;
	}

	public String viewVpsButtonClick() {
		VendorProvisioningStatusBean selectedRow = (VendorProvisioningStatusBean) vendorDataTable
				.getRowData();
		if (selectedRow != null) {
			VendorProvisioningStatusHandler vpsHandler= BasePageHandler.getVPSHandler();
			vpsHandler.viewVPSDetails(selectedRow.getVpsId());
			return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
//			return NavigationStrings.getParameterizedUrl(
//					NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE,
//					VendorProvisioningStatusHandler.REQUEST_PARAM, selectedRow
//							.getVpsId());
		} else {
			logger.log(Level.ERROR, "selectedRow returned null so "
					+ VendorProvisioningStatusHandler.REQUEST_PARAM
					+ " param will not be set.");
			return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
		}
	}

	public void setResponseCodeSearchField(String responseCodeSearchField) {
		this.responseCodeSearchField = responseCodeSearchField;
	}

	// *************************************************************************************************
	// Clear all

	public String clearAllButtonClick() {
		// this.setContractIdSearchField(null);
		// this.setSerialNumberSearchField(null);
		// this.setLineItemIdSearchField(null);
		// this.setPhoneNumSearchField(null);
		// this.setCustomerEmailSearchField(null);
		// this.setDeliveryEmailSearchField(null);
		// this.setConfirmationCodeSearchField(null);
		// this.setDateDependentQueryDateRangeFromBoundry(new Date(System
		// .currentTimeMillis()));
		// this
		// .setDateDependentQueryDateRangeToBoundry(new Date(
		// this.getDateDependentQueryDateRangeFromBoundry()
		// .getTime() + 86400000));
		// this.setFpkTransactionDateSearchField(null);
		// this.setFpkStoreIdSearchField(null);
		// this.setFpkRegisterIdSearchField(null);
		// this.setFpkTransactionIdSearchField(null);
		// this.setFpkLineIdSearchField(null);
		// this.setBbyOrderIdSearchField(null);
		// this.setPrimarySkuSearchField(null);
		// this.setMasterItemIdSearchField(null);
		// this.setOrderStatusSearchField(null);
		// this.setErrorCodeSearchField(null);
		// this.setFirstNameSearchField(null);
		// this.setLastNameSearchField(null);
		// this.setVendorId(null);
		// this.setResponseCodeSearchField(null);
		return NavigationStrings.SDP_ORDER_SEARCH_PAGE;
	}

	// *************************************************************************************************

	/**
	 * This method adds <code>FacesMessage</code> errors to the current
	 * <code>FacesContext</code> if the size of collection argument is zero or
	 * greater than the currently set <code>MAX_QUERY_RESULTS</code> value and
	 * returns false. Otherwise it returns true.
	 * 
	 * @param queryResults
	 * @return true if queryResults.size() is greater than zero and less than
	 *         the maximum result size. False otherwise.
	 */
	protected boolean validateResultListSize(final Collection<?> queryResults)
			throws DataAccessException {

		if (queryResults == null) {
			throw new DataAccessException(NULL_PARAMETER_PASSED);
		}

		final FacesContext fContext = FacesContext.getCurrentInstance();

		if (queryResults.size() == 0) {
			final FacesMessage fMessage = new FacesMessage();
			fMessage.setDetail("No results found based on search criteria");
			fMessage.setSummary("No results found based on search criteria");
			fContext.addMessage(null, fMessage);
			return false;
		}
		// if (queryResults.size() >= MAX_QUERY_RESULTS) {
		// final FacesMessage fMessage = new FacesMessage();
		// fMessage.setDetail("Your search returned more than max results: "
		// + MAX_QUERY_RESULTS);
		// fMessage.setSummary("Your search returned more than max results: "
		// + MAX_QUERY_RESULTS);
		// fContext.addMessage(null, fMessage);
		// return false;
		// }
		return true;
	}

	private boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}

	public boolean EmailValidation(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern p = Pattern.compile(EMAIL_PATTERN);
		// Pattern p = Pattern.compile("[a-zA-Z.]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
		Matcher m = p.matcher(email);
		// Matcher m=p.matcher(args[0]);
		return (m.matches());
	}

	@ManagedProperty(value = "#{vendorProvisioningStatusHandler}")
	private VendorProvisioningStatusHandler vendorProvisioningStatusHandler;

	public VendorProvisioningStatusHandler getVendorProvisioningStatusHandler() {
		return vendorProvisioningStatusHandler;
	}

	public void setVendorProvisioningStatusHandler(
			VendorProvisioningStatusHandler vendorProvisioningStatusHandler) {
		this.vendorProvisioningStatusHandler = vendorProvisioningStatusHandler;
	}

	@ManagedProperty(value = "#{sdpOrderSearchTabbedPaneHandler}")
	private SdpOrderSearchTabbedPaneHandler sdpOrderSearchTabbedPaneHandler;

	public SdpOrderSearchTabbedPaneHandler getSdpOrderSearchTabbedPaneHandler() {
		return sdpOrderSearchTabbedPaneHandler;
	}

	public void setSdpOrderSearchTabbedPaneHandler(
			SdpOrderSearchTabbedPaneHandler sdpOrderSearchTabbedPaneHandler) {
		this.sdpOrderSearchTabbedPaneHandler = sdpOrderSearchTabbedPaneHandler;
	}

	@ManagedProperty(value = "#{sdpOrderSearchHandler}")
	private SdpOrderSearchHandler sdpOrderSearchHandler;

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@ManagedProperty(value = "#{sdpOrderSearchHelper}")
	private SdpOrderSearchHelper sdpOrderSearchHelper;

	public SdpOrderSearchHelper getSdpOrderSearchHelper() {
		return sdpOrderSearchHelper;
	}

	public void setSdpOrderSearchHelper(
			SdpOrderSearchHelper sdpOrderSearchHelper) {
		this.sdpOrderSearchHelper = sdpOrderSearchHelper;
	}

	private List<SelectItem> vendorIdList;
	private List<SelectItem> orderStatusList;

	public List<SelectItem> getVendorIdList() {
		try {
			if (vendorIdList == null || vendorIdList.size() < 1) {
				vendorIdList = SdpOrderSearchDBWrapper
						.getProvisioningVendorList();
			}
			this.setInitializeByRecent(false);
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		}
		return vendorIdList;
	}

	public List<SelectItem> getOrderStatusList() {
		try {
			orderStatusList = SdpOrderSearchDBWrapper
					.getSdpOrderStatusDropDownList();
			// logger.log(Level.ERROR, orderStatusList);
			this.setInitializeByRecent(false);
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		}
		return orderStatusList;
	}

	public SdpOrderSearchHandler getSdpOrderSearchHandler() {
		return sdpOrderSearchHandler;
	}

	public void setSdpOrderSearchHandler(
			SdpOrderSearchHandler sdpOrderSearchHandler) {
		this.sdpOrderSearchHandler = sdpOrderSearchHandler;
	}

}
