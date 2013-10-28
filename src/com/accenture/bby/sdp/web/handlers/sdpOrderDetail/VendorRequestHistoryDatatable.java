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
import com.accenture.bby.sdp.web.beans.VendorRequestHistoryBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus.VendorProvisioningStatusHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class VendorRequestHistoryDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1887956267369850584L;

	private static final Logger logger = Logger.getLogger(VendorRequestHistoryDatatable.class.getName());
	
	private String sdpOrderId;
	
	private boolean hide;
	public boolean isHide() { return hide; }
	public String toggleHide() { hide = !hide; logger.log(Level.DEBUG, "hide=" + hide); return NavigationStrings.CURRENT_VIEW; }
	
	public String getSdpOrderId() {
		return sdpOrderId;
	}
	public void setSdpOrderId(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}
	
	public VendorRequestHistoryDatatable(String sdpOrderId) {
		this.sdpOrderId = sdpOrderId;
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	
	public String viewVpsButtonClick() {
		VendorRequestHistoryBean selectedRow = (VendorRequestHistoryBean) dataTable.getRowData();
		if (selectedRow != null) {
			VendorProvisioningStatusHandler vpsHandler= BasePageHandler.getVPSHandler();
			
			vpsHandler.viewVPSDetails(String.valueOf(selectedRow.getRowNumber()));
			return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
//			return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE, VendorProvisioningStatusHandler.REQUEST_PARAM, selectedRow.getRowNumber());
		} else {
			logger.log(Level.ERROR, "selectedRow returned null so " + VendorProvisioningStatusHandler.REQUEST_PARAM + " param will not be set.");
			return NavigationStrings.VENDOR_PROVISIONING_STATUS_PAGE;
		}
	}
	
	private List<VendorRequestHistoryBean> rows;
	public List<VendorRequestHistoryBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = SdpOrderDetailDBWrapper.getVendorRequestHistoryBySdpOrderId(this.sdpOrderId);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<VendorRequestHistoryBean>(0);
		}
		logger.log(Level.DEBUG, "Provisioning records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<VendorRequestHistoryBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
}
