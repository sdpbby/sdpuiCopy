package com.accenture.bby.sdp.web.handlers.eccui;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.xmlbeans.XmlException;

import com.accenture.bby.sdp.db.ECCUIDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.web.beans.ECCUIResultBean;
import com.accenture.bby.sdp.web.beans.ECCUISearchBean;
import com.accenture.bby.sdp.web.extensions.SortableList;

@ManagedBean(name = "eccuiDatatable")
@ViewScoped
public class ECCUIDatatable extends SortableList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(ECCUIDatatable.class.getName());

	public static String offrNameColumnName = "Offer Name";
	public static String cusNameColumnName = "Customer Name";
	public static String keyCodeColumnName = "Keycode";
	public static String bbyOrderIdColumnName = "BBY Order ID";
	public static String storeColumnName = "Stor#";
	public static String registerColumnName = "Reg#";
	public static String transactionIdColumnName = "Trans#";
	public static String dateOfSaleColumnName = "Date Of Sale";
	public static String statusColumnName = "Status";
	private static String emailColumnName = "Email";

	private List<ECCUIResultBean> rows;

	private String sendEmailField;
	private ECCUISearchBean searchFields = new ECCUISearchBean();
	public ECCUISearchBean getSearchFields() {
		return this.searchFields;
	}
	
	private int displayFlag;
	private final static int DISPLAY_BBYORDERID = 0;
	private final static int DISPLAY_FPK = 1;
	
	public boolean isDisplayBBYOrderId() { return displayFlag == DISPLAY_BBYORDERID; }
	public boolean isDisplayFpk() { return displayFlag == DISPLAY_FPK; }

	
	
	public ECCUIDatatable() throws DataSourceLookupException, DataAccessException {
		super(dateOfSaleColumnName);
		displayFlag = DISPLAY_BBYORDERID;
	}

	public List<ECCUIResultBean> getRows() throws DataSourceLookupException, DataAccessException {
		if (rows == null) {
			rows = new ArrayList<ECCUIResultBean>();
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows;
	}

	@Override
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}

	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	@Override
	protected void sort() {
		Comparator<ECCUIResultBean> comparator = new Comparator<ECCUIResultBean>() {
			@Override
			public int compare(ECCUIResultBean o1, ECCUIResultBean o2) {
				ECCUIResultBean c1 = o1;
				ECCUIResultBean c2 = o2;

				if (sortColumnName == null) {
					return 0;
				}
				if (sortColumnName.equals(offrNameColumnName)) {
					return ascending ? c1.getOffrName().compareTo(c2.getOffrName()) : c2.getOffrName().compareTo(c1.getOffrName());
				} else if (sortColumnName.equals(cusNameColumnName)) {
					return ascending ? c1.getCusName().compareTo(c2.getCusName()) : c2.getCusName().compareTo(c1.getCusName());

				} else if (sortColumnName.equals(keyCodeColumnName)) {
					return ascending ? c1.getKeyCode().compareTo(c2.getKeyCode()) : c2.getKeyCode().compareTo(c1.getKeyCode());

				} else if (sortColumnName.equals(bbyOrderIdColumnName)) {
					return ascending ? c1.getFpkTransactionId().compareTo(c2.getFpkTransactionId()) : c2.getFpkTransactionId().compareTo(c1.getFpkTransactionId());

				} else if (sortColumnName.equals(storeColumnName)) {
					return ascending ? c1.getFpkStoreId().compareTo(c2.getFpkStoreId()) : c2.getFpkStoreId().compareTo(c1.getFpkStoreId());

				} else if (sortColumnName.equals(registerColumnName)) {
					return ascending ? c1.getFpkRegisterId().compareTo(c2.getFpkRegisterId()) : c2.getFpkRegisterId().compareTo(c1.getFpkRegisterId());

				} else if (sortColumnName.equals(transactionIdColumnName)) {
					return ascending ? c1.getFpkTransactionId().compareTo(c2.getFpkTransactionId()) : c2.getFpkTransactionId().compareTo(c1.getFpkTransactionId());

				} else if (sortColumnName.equals(dateOfSaleColumnName)) {
					return ascending ? c1.getFpkTransactionDate().compareTo(c2.getFpkTransactionDate()) : c2.getFpkTransactionDate().compareTo(c1.getFpkTransactionDate());

				} else if (sortColumnName.equals(statusColumnName)) {
					return ascending ? c1.getStatus().compareTo(c2.getStatus()) : c2.getStatus().compareTo(c1.getStatus());

				} else if (sortColumnName.equals(emailColumnName)) {
					return ascending ? c1.getDeliveryEmail().compareTo(c2.getDeliveryEmail()) : c2.getDeliveryEmail().compareTo(c1.getDeliveryEmail());

				} else
					return 0;
			}
		};
		Collections.sort(rows, comparator);
	}

	public String searchOrderDButtonClick() {
		if (this.getBbyOrderIDSearchField() != null && this.getBbyOrderIDSearchField().length() > 0) {
			displayFlag = DISPLAY_BBYORDERID;
			this.setStoreId4pkSearchField(null);
			this.setRegisterId4pkSearchField(null);
			this.setTransactionId4pkSearchField(null);
			this.setDate4pkSearchField(null);
			if (TextFilter.isSpecialCharacterPresent(this.getBbyOrderIDSearchField().trim())) {
				FacesContext context = FacesContext.getCurrentInstance();
				logger.log(Level.DEBUG, "Special Character Found (ContractId) ::"
						+ this.getBbyOrderIDSearchField().trim());
				context.addMessage(
						null,
						new FacesMessage("Special characters are not allowed."));
					return NavigationStrings.CURRENT_VIEW;
			} 
			// capture audit log and do search
			try {
				AuditUtil.audit(Action.ECC_ORDER_LOOKUP, searchFields);
				this.setRows(ECCUIDBWrapper.getOrderByBBYOrderID(this.getBbyOrderIDSearchField()));
			} catch (AuditTrailException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to capture audit log. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to retrieve order. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataAccessException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to retrieve order. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
			
			
			
		} else if ((this.getStoreId4pkSearchField() != null && this.getStoreId4pkSearchField().length() > 0)
				&& (this.getRegisterId4pkSearchField() != null && this.getRegisterId4pkSearchField().length() > 0)
				&& (this.getTransactionId4pkSearchField() != null && this.getTransactionId4pkSearchField().length() > 0) 
				&& (this.getDate4pkSearchField() != null)) {
			if ((TextFilter.isSpecialCharacterPresent(this.getStoreId4pkSearchField()
					.trim())) || (TextFilter.isSpecialCharacterPresent(this.getRegisterId4pkSearchField()
							.trim())) || (TextFilter.isSpecialCharacterPresent(this.getTransactionId4pkSearchField()
									.trim()))){
				FacesContext context = FacesContext.getCurrentInstance();
				logger.log(Level.DEBUG, "Special Character Found (StoreId4pkSearchField/RegisterId4pkSearchField/TransactionId4pkSearchField)");
				context.addMessage(
						null,
						new FacesMessage("Special characters are not allowed."));
					return NavigationStrings.CURRENT_VIEW;
			}
			displayFlag = DISPLAY_FPK;
			this.setBbyOrderIDSearchField(null);
			
			// capture audit log and do search
			try {
				AuditUtil.audit(Action.ECC_ORDER_LOOKUP, searchFields);
				this.setRows(ECCUIDBWrapper.getOrderByFPK(this.getStoreId4pkSearchField(), this.getRegisterId4pkSearchField(), this.getTransactionId4pkSearchField(), this.getDate4pkSearchField()));
			} catch (AuditTrailException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to capture audit log. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to retrieve order. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataAccessException e) {
				this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to retrieve order. This request could not be processed at this time.");
				logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
						
			
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Please enter valid BBYORDER ID or Store#,Reg#,Trans# and Date:"));
		}
		
		return NavigationStrings.CURRENT_VIEW;
	}

	public String clearButtonClick() {
		this.searchFields = new ECCUISearchBean();
		return NavigationStrings.CURRENT_VIEW;
	}

	public String sendEmailButtonClick() {
		for (ECCUIResultBean bean : rows) {
			if (bean.isSelected()) {
				if(TextFilter.isSpecialCharacterPresent(bean.getDeliveryEmail())){
					FacesContext context = FacesContext.getCurrentInstance();
					logger.log(Level.DEBUG, "Special Character Found (StoreId4pkSearchField/RegisterId4pkSearchField/TransactionId4pkSearchField)");
					context.addMessage(
							null,
							new FacesMessage("Special characters are not allowed."));
						return NavigationStrings.CURRENT_VIEW;
				}
				try {
					this.getEccuiHandler().processSendEmailRequestBean(bean);
				} catch (RemoteException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to submit email request to SDP. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (DataAccessException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to retrieve customer details from the database. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (DataSourceLookupException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to connect to the database. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (AuditTrailException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "Failed to connect to the database. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (XmlException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "The original BBYOrder message was found but was not readable. This request could not be processed at this time.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (MissingPropertyException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "Required web service configuration is missing. This request could not be processed at this time. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} catch (WebServiceCallFailedException e) {
					this.getEccuiHandler().getExceptionHandler().initialize(e, "The web service call was not successful. Please notify your site administrator.");
					logger.log(Level.ERROR, this.getEccuiHandler().getExceptionHandler().toString(), e);
					return NavigationStrings.GENERAL_EXCEPTION_PAGE;
				} 
			}
		}
		return NavigationStrings.CURRENT_VIEW;
	}

	public String getBbyOrderIDSearchField() {
		return this.searchFields.getBbyOrderId();
	}

	public void setBbyOrderIDSearchField(String bbyOrderIDSearchField) {
		this.searchFields.setBbyOrderId(bbyOrderIDSearchField);
	}

	public String getStoreId4pkSearchField() {
		return this.searchFields.getFpkStoreId();
	}

	public void setStoreId4pkSearchField(String storeId4pkSearchField) {
		this.searchFields.setFpkStoreId(storeId4pkSearchField);
	}

	public String getRegisterId4pkSearchField() {
		return this.searchFields.getFpkRegisterId();
	}

	public void setRegisterId4pkSearchField(String registerId4pkSearchField) {
		this.searchFields.setFpkRegisterId(registerId4pkSearchField);
	}

	public String getTransactionId4pkSearchField() {
		return this.searchFields.getFpkTransactionId();
	}

	public void setTransactionId4pkSearchField(String transactionId4pkSearchField) {
		this.searchFields.setFpkTransactionId(transactionId4pkSearchField);
	}

	public Date getDate4pkSearchField() {
		return this.searchFields.getFpkTransactionDate();
	}

	public void setDate4pkSearchField(Date date4pkSearchField) {
		this.searchFields.setFpkTransactionDate(date4pkSearchField);
	}

	public void setRows(List<ECCUIResultBean> rows) {
		this.rows = rows;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOffrNameColumnName() {
		return offrNameColumnName;
	}

	public String getCusNameColumnName() {
		return cusNameColumnName;
	}
	
	public String getKeyCodeColumnName() {
		return keyCodeColumnName;
	}

	public String getBbyOrderIdColumnName() {
		return bbyOrderIdColumnName;
	}

	public String getStoreColumnName() {
		return storeColumnName;
	}

	public String getRegisterColumnName() {
		return registerColumnName;
	}

	public String getTransactionIdColumnName() {
		return transactionIdColumnName;
	}

	public String getDateOfSaleColumnName() {
		return dateOfSaleColumnName;
	}

	public String getStatusColumnName() {
		return statusColumnName;
	}

	public String getSendEmailField() {
		return sendEmailField;
	}

	public void setSendEmailField(String sendEmailField) {
		this.sendEmailField = sendEmailField;
	}

	public String getEmailColumnName() {
		return emailColumnName;
	}

	/*
	 * Managed Properties
	 */
	
	@ManagedProperty (value="#{eccuiHandler}")
	private ECCUIHandler eccuiHandler;
	public ECCUIHandler getEccuiHandler() { return eccuiHandler; }
	public void setEccuiHandler(ECCUIHandler eccuiHandler) { this.eccuiHandler = eccuiHandler; }

}
