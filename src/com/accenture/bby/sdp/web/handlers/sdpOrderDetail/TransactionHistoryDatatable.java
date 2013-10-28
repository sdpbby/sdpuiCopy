package com.accenture.bby.sdp.web.handlers.sdpOrderDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpTransactionBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.requestResponseLogDetail.RequestResponseLogDetailHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class TransactionHistoryDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1909268126132958084L;

	private static final Logger logger = Logger.getLogger(TransactionHistoryDatatable.class.getName());
	
	private String sdpOrderId;
	private String lineItemId;
	
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
		
	private boolean hide;
	public boolean isHide() { return hide; }
	public String toggleHide() { hide = !hide; logger.log(Level.DEBUG, "hide=" + hide); return NavigationStrings.CURRENT_VIEW; }
	
	public String getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	
	public TransactionHistoryDatatable(String sdpOrderId, String lineItemId) {
		this.sdpOrderId = sdpOrderId;
		this.lineItemId = lineItemId;
	}
	
	public String viewRequestResponseLogButtonClick() {
		SdpTransactionBean selectedRow = (SdpTransactionBean) dataTable.getRowData();
		if (selectedRow != null) {
			return NavigationStrings.getParameterizedUrl(NavigationStrings.REQUEST_RESPONSE_LOG_DETAIL_DEFAULT_PAGE, RequestResponseLogDetailHandler.REQUEST_PARAM, selectedRow.getLogId());
		} else {
			logger.log(Level.ERROR, "SelectedRow returned null so cannot prepare request response log detail page.");
			exceptionHandler.initialize(new NullPointerException("Selected row was null in transaction history table."), "Unable to prepare request response log detail page.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<SdpTransactionBean> rows;
	
	public List<SdpTransactionBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = SdpOrderDetailDBWrapper.getTransactionHistoryBySdpOrderIdOrLineItemId(this.sdpOrderId, this.lineItemId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<SdpTransactionBean>(0);
		}
		logger.log(Level.DEBUG, "Transaction History records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<SdpTransactionBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
}
