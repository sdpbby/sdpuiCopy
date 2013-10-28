package com.accenture.bby.sdp.web.handlers.sdpOrderDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SimpleAuditLog;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.accenture.bby.sdp.web.handlers.audittrail.AuditTrailDetailHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class RelatedAuditLogDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1887956267369850584L;

	private static final Logger logger = Logger.getLogger(RelatedAuditLogDatatable.class.getName());
	
	private boolean hide = true;
	public boolean isHide() { return hide; }
	public String toggleHide() { hide = !hide; logger.log(Level.DEBUG, "hide=" + hide); return NavigationStrings.CURRENT_VIEW; }
	
	private String contractId;
	private String serialNumber;
	private String lineItemId;
	
	@ManagedProperty (value="#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;
	public ExceptionHandler getExceptionHandler() { return exceptionHandler; }
	
	public RelatedAuditLogDatatable(String contractId, String serialNumber, String lineItemId) {
		this.contractId = contractId;
		this.serialNumber = serialNumber;
		this.lineItemId = lineItemId;
	}
	
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	
	public String viewButtonClick() {
		SimpleAuditLog selectedRow = (SimpleAuditLog) dataTable.getRowData();
		if (selectedRow != null) {
			return NavigationStrings.getParameterizedUrl(NavigationStrings.AUDIT_TRAIL_DETAIL_DEFAULT_PAGE, AuditTrailDetailHandler.REQUEST_PARAM, selectedRow.getLogId());
		} else {
			logger.log(Level.ERROR, "SelectedRow returned null so cannot prepare audit trail log detail page.");
			exceptionHandler.initialize(new NullPointerException("Selected row was null in related audit log table."), "Unable to prepare audit trail log detail page.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	
	private List<SimpleAuditLog> rows;
	public List<SimpleAuditLog> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = AuditTrailDBWrapper.getSimpleAuditLogsByAny(contractId, serialNumber, lineItemId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<SimpleAuditLog>(0);
		}
		logger.log(Level.DEBUG, "Provisioning records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<SimpleAuditLog> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
}
