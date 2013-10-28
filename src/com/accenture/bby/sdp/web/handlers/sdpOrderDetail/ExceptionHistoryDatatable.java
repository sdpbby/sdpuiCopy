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
import com.accenture.bby.sdp.web.beans.ExceptionResultBean;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.exception.ExceptionPageHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class ExceptionHistoryDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9171823356154779547L;

	private static final Logger logger = Logger.getLogger(ExceptionHistoryDatatable.class.getName());
	
	private String sdpOrderId;
	
	private boolean hide = true;
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
	
	public ExceptionHistoryDatatable(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<ExceptionResultBean> rows;
	
	public List<ExceptionResultBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = SdpOrderDetailDBWrapper.getExceptionHistoryBySdpOrderId(this.sdpOrderId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<ExceptionResultBean>(0);
		}
		logger.log(Level.DEBUG, "Exception records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<ExceptionResultBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
//	public String viewButtonClick() {
//		SimpleAuditLog selectedRow = (SimpleAuditLog) dataTable.getRowData();
//		if (selectedRow != null) {
//			return NavigationStrings.getParameterizedUrl(NavigationStrings.EXCEPTION_DEFAULT_PAGE, ExceptionPageHandler.REQUEST_PARAM, selectedRow.getLogId());
//		} else {
//			logger.log(Level.ERROR, "SelectedRow returned null so cannot prepare exception log detail page.");
//			exceptionHandler.initialize(new NullPointerException("Selected row was null in exception history table."), "Unable to prepare exception log detail page.");
//			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
//		}
//	}
	
	public String viewButtonClick() {
		ExceptionResultBean selectedRow = (ExceptionResultBean) dataTable.getRowData();
		if (selectedRow != null) {
			return NavigationStrings.getParameterizedUrl(NavigationStrings.EXCEPTION_DETAIL_DEFAULT_PAGE, ExceptionPageHandler.REQUEST_PARAM, selectedRow.getExceptionId());
		} else {
			logger.log(Level.ERROR, "SelectedRow returned null so cannot prepare exception log detail page.");
			exceptionHandler.initialize(new NullPointerException("Selected row was null in exception history table."), "Unable to prepare exception log detail page.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
}
