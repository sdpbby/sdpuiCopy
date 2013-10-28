package com.accenture.bby.sdp.web.handlers.sdpOrderDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.SdpOrderDetailDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.OrderStatusHistoryBean;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class StatusHistoryDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5540605374590370007L;

	private static final Logger logger = Logger.getLogger(StatusHistoryDatatable.class.getName());
	
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
	
	public StatusHistoryDatatable(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<OrderStatusHistoryBean> rows;
	
	public List<OrderStatusHistoryBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = SdpOrderDetailDBWrapper.getStatusHistoryBySdpOrderId(this.sdpOrderId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<OrderStatusHistoryBean>(0);
		}
		logger.log(Level.DEBUG, "Commsat records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<OrderStatusHistoryBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
}
