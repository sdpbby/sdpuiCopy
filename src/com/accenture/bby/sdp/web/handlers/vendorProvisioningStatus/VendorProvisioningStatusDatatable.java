package com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.VendorProvisioningStatusDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.RequestResponseLogBean;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class VendorProvisioningStatusDatatable {
	
	private static final Logger logger = Logger.getLogger(VendorProvisioningStatusDatatable.class.getName());
	
	private String sdpId;
	private String requestType;
	
	private boolean hide;
	public boolean isHide() { return hide; }
	public String toggleHide() { hide = !hide; logger.log(Level.DEBUG, "hide=" + hide); return NavigationStrings.CURRENT_VIEW; }
	
	public VendorProvisioningStatusDatatable(String sdpId, String requestType) {
		this.sdpId = sdpId;
		this.requestType = requestType;
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<RequestResponseLogBean> rows;
	
	public List<RequestResponseLogBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (isTableEmpty() && !hide) {
			rows = VendorProvisioningStatusDBWrapper.getRequestResponseLogsBySdpIdAndRequestType(this.sdpId, this.requestType);
		} else if (isTableEmpty() && hide) {
			rows = new ArrayList<RequestResponseLogBean>(0);
		}
		logger.log(Level.DEBUG, "Request Response records found >> " + rows.size());
		return rows; 
	}
	
	public void setRows(List<RequestResponseLogBean> rows) { this.rows = rows; }
	
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
}
