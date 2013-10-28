package com.accenture.bby.sdp.web.handlers.vendor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

import com.accenture.bby.sdp.db.VendorDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.VendorBean;
import com.accenture.bby.sdp.web.extensions.SortableList;

@ManagedBean (name="vendorDatatable")
@ViewScoped
public class VendorDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static String vendorIdColumnName = "Vendor ID";
	private static String vendorNameColumnName = "Vendor Name";
	private static String serviceProviderIdColumnName = "Service Provider ID";
	private static String aggregationFrequencyColumnName = "Aggregation Frequency";
	private static String aggregationMaxColumnName = "Aggregation Max";
	private static String retryFrequencyColumnName = "Retry Frequency";
	private static String retryMaxColumnName = "Retry Max";
	private static String lastAggregationColumnName = "Last Aggregation";
	private static String throttleFactorColumnName = "Throttle Factor";
	private static String categoryColumnName = "Vendor Category";
	private static String actionColumnName = "Action";
	
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getServiceProviderIdColumnName() { return serviceProviderIdColumnName; }
	public String getAggregationFrequencyColumnName() { return aggregationFrequencyColumnName; }
	public String getAggregationMaxColumnName() { return aggregationMaxColumnName; }
	public String getRetryFrequencyColumnName() { return retryFrequencyColumnName; }
	public String getRetryMaxColumnName() { return retryMaxColumnName; }
	public String getThrottleFactorColumnName() { return throttleFactorColumnName; }
	public String getLastAggregationColumnName() { return lastAggregationColumnName; }
	public String getCategoryColumnName() { return categoryColumnName; }
	public String getActionColumnName() { return actionColumnName; }
	
	
	public VendorDatatable() throws DataSourceLookupException, DataAccessException {
		super(vendorNameColumnName);
	}
	
	private VendorBean[] rowItemArray = new VendorBean[0];
	private DataModel<VendorBean> rows;
	
	public VendorBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<VendorBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rowItemArray = VendorDBWrapper.getAll();
			rows = new ArrayDataModel<VendorBean>(rowItemArray);
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(DataModel<VendorBean> rows) { this.rows = rows; }
	public void setRowItemArray(VendorBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

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
		Comparator<VendorBean> comparator = new Comparator<VendorBean>() {
            @Override
			public int compare(VendorBean o1, VendorBean o2) {
            	VendorBean c1 = o1;
            	VendorBean c2 = o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(vendorIdColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorId()).compareTo(ignoreNull(c2.getVendorId())) :
                            ignoreNull(c2.getVendorId()).compareTo(ignoreNull(c1.getVendorId()));
                    
                } else if (sortColumnName.equals(vendorNameColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorName()).compareTo(ignoreNull(c2.getVendorName())) :
                        ignoreNull(c2.getVendorName()).compareTo(ignoreNull(c1.getVendorName()));
            
                } else if (sortColumnName.equals(serviceProviderIdColumnName)) {
                    return ascending ? ignoreNull(c1.getServiceProviderId()).compareTo(ignoreNull(c2.getServiceProviderId())) :
                        ignoreNull(c2.getServiceProviderId()).compareTo(ignoreNull(c1.getServiceProviderId()));
            
                } else if (sortColumnName.equals(aggregationFrequencyColumnName)) {
                    return ascending ? ignoreNull(c1.getAggregationFrequency()).compareTo(ignoreNull(c2.getAggregationFrequency())) :
                        ignoreNull(c2.getAggregationFrequency()).compareTo(ignoreNull(c1.getAggregationFrequency()));
            
                } else if (sortColumnName.equals(aggregationMaxColumnName)) {
                    return ascending ? ignoreNull(c1.getAggregationMax()).compareTo(ignoreNull(c2.getAggregationMax())) :
                        ignoreNull(c2.getAggregationMax()).compareTo(ignoreNull(c1.getAggregationMax()));
            
                } else if (sortColumnName.equals(retryFrequencyColumnName)) {
                    return ascending ? ignoreNull(c1.getRetryFrequency()).compareTo(ignoreNull(c2.getRetryFrequency())) :
                        ignoreNull(c2.getRetryFrequency()).compareTo(ignoreNull(c1.getRetryFrequency()));
            
                } else if (sortColumnName.equals(retryMaxColumnName)) {
                    return ascending ? ignoreNull(c1.getRetryMax()).compareTo(ignoreNull(c2.getRetryMax())) :
                        ignoreNull(c2.getRetryMax()).compareTo(ignoreNull(c1.getRetryMax()));
            
                } else if (sortColumnName.equals(throttleFactorColumnName)) {
                    return ascending ? ignoreNull(c1.getThrottleFactor()).compareTo(ignoreNull(c2.getThrottleFactor())) :
                        ignoreNull(c2.getThrottleFactor()).compareTo((c1.getThrottleFactor()));
            
                } else if (sortColumnName.equals(lastAggregationColumnName)) {
                    return ascending ? ignoreNull(c1.getLastAggregation()).compareTo(ignoreNull(c2.getLastAggregation())) :
                        ignoreNull(c2.getLastAggregation()).compareTo(ignoreNull(c1.getLastAggregation()));
            
                } else if (sortColumnName.equals(categoryColumnName)) {
                    return ascending ? ignoreNull(c1.getCategory()).compareTo(ignoreNull(c2.getCategory())) :
                        ignoreNull(c2.getCategory()).compareTo(ignoreNull(c1.getCategory()));
            
                } else return 0;
            }
        };
        Arrays.sort(rowItemArray, comparator);
	}
	
	public String searchByAllButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRowItemArray(VendorDBWrapper.getAll());
		this.setRows(new ArrayDataModel<VendorBean>(rowItemArray));
		this.getVendorTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String editButtonClick() {
		VendorBean tmp = rows.getRowData();
		this.getVendorHandler().setOldVendorBean(new VendorBean(tmp));
		this.getVendorHandler().setNewVendorBean(new VendorBean(tmp));
		this.getVendorTabbedPaneHandler().displayEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String editCodesButtonClick() throws DataSourceLookupException, DataAccessException {
		VendorBean tmp = rows.getRowData();
		return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_CODE_DEFAULT_PAGE, VendorCodeDatatable.REQUEST_PARAM, tmp.getVendorId());
	}
	
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{vendorTabbedPaneHandler}")
	private VendorTabbedPaneHandler vendorTabbedPaneHandler;
	public VendorTabbedPaneHandler getVendorTabbedPaneHandler() { return vendorTabbedPaneHandler; }
	public void setVendorTabbedPaneHandler(VendorTabbedPaneHandler vendorTabbedPaneHandler) { this.vendorTabbedPaneHandler = vendorTabbedPaneHandler; }
	
	@ManagedProperty (value="#{vendorHandler}")
	private VendorHandler vendorHandler;
	public VendorHandler getVendorHandler() { return vendorHandler; }
	public void setVendorHandler(VendorHandler vendorHandler) { this.vendorHandler = vendorHandler; }
	
}
