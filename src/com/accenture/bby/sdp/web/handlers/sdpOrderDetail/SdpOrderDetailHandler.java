package com.accenture.bby.sdp.web.handlers.sdpOrderDetail;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.MissingPropertyException;
import com.accenture.bby.sdp.utl.exceptions.WebServiceCallFailedException;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.directToVendor.DirectToVendorHandler;

@SessionScoped
@ManagedBean(name = "sdpOrderDetailHandler")
public class SdpOrderDetailHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String REQUEST_PARAM = "sdpOrderId";
	private static final Logger logger = Logger
			.getLogger(SdpOrderDetailHandler.class.getName());

	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	public ExceptionHandler getExceptionHandler() {
		return this.exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	private SdpTransactionDataBean data;
	private TransactionHistoryDatatable transactionHistoryDatatable;
	private VendorRequestHistoryDatatable vendorRequestHistoryDatatable;
	private CommsatHistoryDatatable commsatHistoryDatatable;
	private StatusHistoryDatatable statusHistoryDatatable;
	private ExceptionHistoryDatatable exceptionHistoryDatatable;
	private AdjustmentHistoryDatatable adjustmentHistoryDatatable;
	private RelatedAuditLogDatatable relatedAuditLogDatatable;
	private String sdpOrderId;

	public SdpOrderDetailHandler() {

	}

	public SdpOrderDetailHandler(String sdpOrderId1)
			throws DataAccessException, DataSourceLookupException {
		// get the sdpOrderId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.sdpOrderId = sdpOrderId1;

		// if sdpOrderId not found in context param, check the session.
		// if found in the context then store the sdpOrderId in the session for
		// later use.
		// if (this.sdpOrderId == null) {
		// logger.log(Level.DEBUG, "sdpOrderId variable not found in request
		// param. checking session map.");
		// this.sdpOrderId =
		// (String)facesContext.getExternalContext().getSessionMap().get(REQUEST_PARAM);
		// } else {
		// logger.log(Level.DEBUG, "sdpOrderId variable found in request param.
		// storing in session map.");
		// facesContext.getExternalContext().getSessionMap().put(REQUEST_PARAM,
		// sdpOrderId);
		// }

		logger.log(Level.DEBUG, "sdpOrderId param passed=" + sdpOrderId);
		transactionHistoryDatatable = new TransactionHistoryDatatable(
				sdpOrderId, this.getData().getLineItemId());
		vendorRequestHistoryDatatable = new VendorRequestHistoryDatatable(
				sdpOrderId);
		commsatHistoryDatatable = new CommsatHistoryDatatable(sdpOrderId);
		statusHistoryDatatable = new StatusHistoryDatatable(sdpOrderId);
		exceptionHistoryDatatable = new ExceptionHistoryDatatable(sdpOrderId);
		adjustmentHistoryDatatable = new AdjustmentHistoryDatatable(sdpOrderId);
		relatedAuditLogDatatable = new RelatedAuditLogDatatable(this.getData()
				.getContractId(), this.getData().getSerialNumber(), this
				.getData().getLineItemId());
		if (!commsatHistoryDatatable.isTableEmpty()) {
			commsatHistoryDatatable.setResubmitAllowed(true);
		} else {
			commsatHistoryDatatable.setResubmitAllowed(false);
		}
	}

	public String newVendorRequestButtonClick() {
		return NavigationStrings.getParameterizedUrl(
				NavigationStrings.VENDOR_REQUEST_DEFAULT_PAGE,
				DirectToVendorHandler.SDP_ORDER_REQUEST_PARAM, this.sdpOrderId);
	}

	public String resendCommsatButtonClick() {
		return commsatHistoryDatatable.resubmitInit(this.getData());
	}

	public String sendCommsatRequestNowButtonClick() {
		try {
			return commsatHistoryDatatable.sendRequestNow();
		} catch (IllegalStateException e) {
			exceptionHandler.initialize(e,
					"Send Email request was not successful.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (AuditTrailException e) {
			exceptionHandler
					.initialize(e,
							"Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (MissingPropertyException e) {
			exceptionHandler
					.initialize(
							e,
							"Required web service configuration is missing. This request could not be processed at this time. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (WebServiceCallFailedException e) {
			exceptionHandler
					.initialize(
							e,
							"The web service call was not successful. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler
					.initialize(
							e,
							"Failed to retrieve customer details from the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler
					.initialize(e,
							"Failed to connect to the database. Please notify your site administrator.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}

	public SdpTransactionDataBean getData() {
		if (data == null) {
			logger.log(Level.DEBUG, "BEGIN Retrieving SDP Order ID="
					+ sdpOrderId);

			if (sdpOrderId == null) {
				throw new IllegalArgumentException("Invalid SDP Order ID: "
						+ sdpOrderId);
			}

			try {
				data = SdpOrderDetailDBWrapper
						.getSdpOrderDetailBySdpOrderId(sdpOrderId);
			} catch (DataAccessException e) {
				logger.log(Level.ERROR,
						"Failed to retrieve SDP Order details from the database with SDP ORDER ID="
								+ sdpOrderId);
				throw new IllegalStateException(
						"Failed to retrieve SDP Order details from the database with SDP ORDER ID="
								+ sdpOrderId);
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to data source.");
				throw new IllegalStateException(
						"Failed to connect to data source.");
			}

			if (data == null) {
				logger.log(Level.DEBUG, "SDP Order search returned null.");
				throw new IllegalArgumentException("Invalid SDP Order ID: "
						+ sdpOrderId);
			}

			logger.log(Level.DEBUG, "FINISH Retrieving SDP Order ID="
					+ sdpOrderId);
			logger.log(Level.DEBUG, "BEGIN Capturing audit log.");
			try {
				AuditUtil.audit(Action.LOOKUP_SUBSCRIPTION_DATA, data);
			} catch (AuditTrailException e) {
				logger.log(Level.ERROR, "Audit trail failure!");
				throw new IllegalStateException(
						"Failed to connect to data source.");
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to capture audit log.");
				throw new IllegalStateException(
						"Failed to connect to data source.");
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to data source.");
				throw new IllegalStateException(
						"Failed to connect to data source.");
			}
			logger.log(Level.DEBUG, "FINISH Capturing audit log.");
		}

		return data;
	}

	public void setData(SdpTransactionDataBean data) {
		if (data == null) {
			data = new SdpTransactionDataBean();
		} else {
			this.data = data;
		}
	}

	public boolean isDigitalAttributesPresent() {
		return data.isDigitalAttributesPresent();
	}

	public String getVendorName() {
		return Constants.getVendorName(data.getVendorId());
	}

	public String getSdpOrderStatusName() {
		return Constants.getStatusName(data.getSdpOrderStatus());
	}

	public TransactionHistoryDatatable getTransactionHistoryDatatable() {
		return transactionHistoryDatatable;
	}

	public VendorRequestHistoryDatatable getVendorRequestHistoryDatatable() {
		return vendorRequestHistoryDatatable;
	}

	public CommsatHistoryDatatable getCommsatHistoryDatatable() {
		return commsatHistoryDatatable;
	}

	public StatusHistoryDatatable getStatusHistoryDatatable() {
		return statusHistoryDatatable;
	}

	public ExceptionHistoryDatatable getExceptionHistoryDatatable() {
		return exceptionHistoryDatatable;
	}

	public AdjustmentHistoryDatatable getAdjustmentHistoryDatatable() {
		return adjustmentHistoryDatatable;
	}

	public RelatedAuditLogDatatable getRelatedAuditLogDatatable() {
		return relatedAuditLogDatatable;
	}

	public void viewOrderDetailButtonClick(String sdpOrderId2) {

		this.data = null;

		// TODO Auto-generated method stub
		// get the sdpOrderId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.sdpOrderId = sdpOrderId2;

		// if sdpOrderId not found in context param, check the session.
		// if found in the context then store the sdpOrderId in the session for
		// later use.

		logger.log(Level.DEBUG, "sdpOrderId param passed=" + sdpOrderId);
		transactionHistoryDatatable = new TransactionHistoryDatatable(
				sdpOrderId, this.getData().getLineItemId());
		vendorRequestHistoryDatatable = new VendorRequestHistoryDatatable(
				sdpOrderId);
		try {
			commsatHistoryDatatable = new CommsatHistoryDatatable(sdpOrderId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataSourceLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		statusHistoryDatatable = new StatusHistoryDatatable(sdpOrderId);
		exceptionHistoryDatatable = new ExceptionHistoryDatatable(sdpOrderId);
		adjustmentHistoryDatatable = new AdjustmentHistoryDatatable(sdpOrderId);
		relatedAuditLogDatatable = new RelatedAuditLogDatatable(this.getData()
				.getContractId(), this.getData().getSerialNumber(), this
				.getData().getLineItemId());
		if (!commsatHistoryDatatable.isTableEmpty()) {
			commsatHistoryDatatable.setResubmitAllowed(true);
		} else {
			commsatHistoryDatatable.setResubmitAllowed(false);
		}
	}
}
