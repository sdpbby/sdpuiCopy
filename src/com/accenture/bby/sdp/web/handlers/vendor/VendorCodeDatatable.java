package com.accenture.bby.sdp.web.handlers.vendor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.VendorCodeDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.VendorCodeBean;
import com.accenture.bby.sdp.web.extensions.SortableList;

@ManagedBean (name="vendorCodeDatatable")
@ViewScoped
public class VendorCodeDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String REQUEST_PARAM = "vendorId";
	
	private static final Logger logger = Logger.getLogger(VendorCodeDatatable.class.getName());
	
	private static String rowIdColumnName = "Row ID";
	private static String vendorIdColumnName = "Vendor ID";
	private static String vendorNameColumnName = "Vendor Name";
	private static String vendorStatusCodeColumnName = "Vendor Status Code";
	private static String sdpStatusCodeColumnName = "SDP Status Code";
	private static String canRetryColumnName = "Can Retry";
	private static String statusCodeDescriptionColumnName = "Status Code Description";
	private static String actionColumnName = "Action";
	
	public String getRowIdColumnName() { return rowIdColumnName; }
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getVendorStatusCodeColumnName() { return vendorStatusCodeColumnName; }
	public String getSdpStatusCodeColumnName() { return sdpStatusCodeColumnName; }
	public String getCanRetryColumnName() { return canRetryColumnName; }
	public String getStatusCodeDescriptionColumnName() { return statusCodeDescriptionColumnName; }
	public String getActionColumnName() { return actionColumnName; }
	
	private String passedVendorId;
	public String getPassedVendorId() { return passedVendorId; }
	public String getVendorName() {
		return Constants.getVendorName(passedVendorId);
	}
	
	public VendorCodeDatatable() throws DataSourceLookupException, DataAccessException {
		super(vendorStatusCodeColumnName);
		
		// get the vendorId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.passedVendorId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		logger.log(Level.DEBUG, "VendorId param passed=" + this.passedVendorId);

	}
	
	private VendorCodeBean[] rowItemArray = new VendorCodeBean[0];
	private DataModel<VendorCodeBean> rows;
	
	public VendorCodeBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<VendorCodeBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (passedVendorId == null) {
			rowItemArray = new VendorCodeBean[0];
			rows = new ArrayDataModel<VendorCodeBean>(rowItemArray);
			logger.log(Level.ERROR, "VendorId param passed was null. Vendor code list displayed with no values.");
		} else if (rows == null) {
			rowItemArray = VendorCodeDBWrapper.getAllByVendorId(passedVendorId);
			rows = new ArrayDataModel<VendorCodeBean>(rowItemArray);
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(DataModel<VendorCodeBean> rows) { this.rows = rows; }
	public void setRowItemArray(VendorCodeBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

	public String deleteButtonClick() {
		throw new UnsupportedOperationException("Vendor setup does not support delete. This method should never be called!");
	}
	
	@Override
	public boolean isTableEmpty() {
		return rowItemArray == null || rowItemArray.length == 0;
	}
	
	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}
	
	@Override
	protected void sort() {
		Comparator<VendorCodeBean> comparator = new Comparator<VendorCodeBean>() {
            @Override
			public int compare(VendorCodeBean o1, VendorCodeBean o2) {
            	VendorCodeBean c1 = o1;
            	VendorCodeBean c2 = o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(rowIdColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorCodeRowId()).compareTo(ignoreNull(c2.getVendorCodeRowId())) :
                            ignoreNull(c2.getVendorCodeRowId()).compareTo(ignoreNull(c1.getVendorCodeRowId()));
                    
                } else if (sortColumnName.equals(vendorNameColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorName()).compareTo(ignoreNull(c2.getVendorName())) :
                        ignoreNull(c2.getVendorName()).compareTo(ignoreNull(c1.getVendorName()));
            
                } else if (sortColumnName.equals(vendorIdColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorId()).compareTo(ignoreNull(c2.getVendorId())) :
                        ignoreNull(c2.getVendorId()).compareTo(ignoreNull(c1.getVendorId()));
            
                } else if (sortColumnName.equals(vendorStatusCodeColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorCode()).compareTo(ignoreNull(c2.getVendorCode())) :
                        ignoreNull(c2.getVendorCode()).compareTo(ignoreNull(c1.getVendorCode()));
            
                } else if (sortColumnName.equals(sdpStatusCodeColumnName)) {
                    return ascending ? ignoreNull(c1.getSdpCode()).compareTo(ignoreNull(c2.getSdpCode())) :
                        ignoreNull(c2.getSdpCode()).compareTo(ignoreNull(c1.getSdpCode()));
            
                } else if (sortColumnName.equals(canRetryColumnName)) {
                    return ascending ? ignoreNull(c1.getCanRetry()).compareTo(ignoreNull(c2.getCanRetry())) :
                        ignoreNull(c2.getCanRetry()).compareTo(ignoreNull(c1.getCanRetry()));
            
                } else if (sortColumnName.equals(statusCodeDescriptionColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorCodeDescription()).compareTo(ignoreNull(c2.getVendorCodeDescription())) :
                        ignoreNull(c2.getVendorCodeDescription()).compareTo(ignoreNull(c1.getVendorCodeDescription()));
            
                } else return 0;
            }
        };
        Arrays.sort(rowItemArray, comparator);
	}
	
	public String editButtonClick() {
		VendorCodeBean tmp = rows.getRowData();
		this.getVendorCodeHandler().setOldVendorCodeBean(new VendorCodeBean(tmp));
		this.getVendorCodeHandler().setNewVendorCodeBean(new VendorCodeBean(tmp));
		this.getVendorCodeTabbedPaneHandler().displayVendorCodeEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{vendorCodeTabbedPaneHandler}")
	private VendorCodeTabbedPaneHandler vendorCodeTabbedPaneHandler;
	public VendorCodeTabbedPaneHandler getVendorCodeTabbedPaneHandler() { return vendorCodeTabbedPaneHandler; }
	public void setVendorCodeTabbedPaneHandler(VendorCodeTabbedPaneHandler vendorCodeTabbedPaneHandler) { this.vendorCodeTabbedPaneHandler = vendorCodeTabbedPaneHandler; }
	
	@ManagedProperty (value="#{vendorCodeHandler}")
	private VendorCodeHandler vendorCodeHandler;
	public VendorCodeHandler getVendorCodeHandler() { return vendorCodeHandler; }
	public void setVendorCodeHandler(VendorCodeHandler vendorCodeHandler) { this.vendorCodeHandler = vendorCodeHandler; }
	
	
	
}
