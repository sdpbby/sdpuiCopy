package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

import com.accenture.bby.sdp.db.KcbBatchLoadDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KeycodeBatchBean;

@ManagedBean (name="keycodeBankManageLoadDatatable")
@ViewScoped
public class KeycodeBankManageLoadDatatable implements Serializable {
	
	private static final Logger logger = Logger.getLogger(KeycodeBankManageLoadDatatable.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String batchLoadIdColumnName = "Load ID";
	private static final String loadedDateColumnName = "Loaded Date";
	private static final String minDateColumnName = "Start Date";
	private static final String maxDateColumnName = "Expire Date";
	private static final String merchandiseSkuColumnName = "Merchandise SKU";
	private static final String nonMerchandiseSkuColumnName = "Non-Merchandise SKU";
	private static final String masterItemIdColumnName = "Master Item ID";
	private static final String descriptionColumnName = "Description";
	private static final String vendorIdColumnName = "Vendor ID";
	private static final String vendorNameColumnName = "Vendor Name";
	private static final String actionColumnName = "Action";
	
	
	
	public String getBatchLoadIdColumnName() { return batchLoadIdColumnName; }
	public String getLoadedDateColumnName() { return loadedDateColumnName; }
	public String getMinDateColumnName() { return minDateColumnName; }
	public String getMaxDateColumnName() { return maxDateColumnName; }
	public String getMerchandiseSkuColumnName() { return merchandiseSkuColumnName; }
	public String getNonMerchandiseSkuColumnName() { return nonMerchandiseSkuColumnName; }
	public String getMasterItemIdColumnName() { return masterItemIdColumnName; }
	public String getDescriptionColumnName() { return descriptionColumnName; }
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getActionColumnName() { return actionColumnName; }
	
	
	private KeycodeBatchBean[] rowItemArray = new KeycodeBatchBean[0];
	private DataModel<KeycodeBatchBean> rows;
	
	public KeycodeBatchBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<KeycodeBatchBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rowItemArray = new KeycodeBatchBean[0];
			rows = new ArrayDataModel<KeycodeBatchBean>(rowItemArray);
		}
		return rows; 
	}
	
	public void setRows(DataModel<KeycodeBatchBean> rows) { this.rows = rows; }
	public void setRowItemArray(KeycodeBatchBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

	public boolean isTableEmpty() {
		return rowItemArray == null || rowItemArray.length == 0;
	}
	
	public String editButtonClick() {
		KeycodeBatchBean tmp = rows.getRowData();
		try {
			tmp.setLoadSize(KcbBatchLoadDBWrapper.getCountByBatchLoadId(tmp.getBatchLoadId()));
		} catch (DataAccessException e) {
			this.getKeycodeBankManageLoadHandler().getExceptionHandler().initialize(e, "Failure occurred while retrieving keycode count.");
			logger.log(Level.ERROR, this.getKeycodeBankManageLoadHandler().getExceptionHandler().toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			this.getKeycodeBankManageLoadHandler().getExceptionHandler().initialize(e, "Failed to open data source connection.");
			logger.log(Level.ERROR, this.getKeycodeBankManageLoadHandler().getExceptionHandler().toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		this.getKeycodeBankManageLoadHandler().setOldKeycodeBatchBean(new KeycodeBatchBean(tmp));
		this.getKeycodeBankManageLoadHandler().setNewKeycodeBatchBean(new KeycodeBatchBean(tmp));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displayEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String merchandiseSkuSearchField;
	public String getMerchandiseSkuSearchField() { return merchandiseSkuSearchField; }
	public void setMerchandiseSkuSearchField(String merchandiseSkuSearchField) { this.merchandiseSkuSearchField = merchandiseSkuSearchField; }
	
	public String searchByMerchandiseSkuButtonClick() throws DataAccessException, DataSourceLookupException {
		if (isNotNull(this.merchandiseSkuSearchField)&& TextFilter.isSpecialCharacterPresent(this.merchandiseSkuSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.log(Level.DEBUG, "Special Character Found (merchandiseSkuSearchField) ::"
					+ this.merchandiseSkuSearchField);
			context.addMessage(
					"kcbmanageLoad_merchsku_search_form:search_merchsku",
					new FacesMessage("Special characters are not allowed."));
				return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbBatchLoadDBWrapper.getByMerchandiseSku(this.merchandiseSkuSearchField.trim()));
		this.setRows(new ArrayDataModel<KeycodeBatchBean>(rowItemArray));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String nonMerchandiseSkuSearchField;
	public String getNonMerchandiseSkuSearchField() { return nonMerchandiseSkuSearchField; }
	public void setNonMerchandiseSkuSearchField(String nonMerchandiseSkuSearchField) { this.nonMerchandiseSkuSearchField = nonMerchandiseSkuSearchField; }
	
	public String searchByNonMerchandiseSkuButtonClick() throws DataSourceLookupException, DataAccessException {
		if (isNotNull(this.nonMerchandiseSkuSearchField)&& TextFilter.isSpecialCharacterPresent(this.nonMerchandiseSkuSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.log(Level.DEBUG, "Special Character Found (nonMerchandiseSkuSearchField) ::"
					+ this.nonMerchandiseSkuSearchField);
			context.addMessage(
					"kcbmanageLoad_nonmerchsku_search_form:search_nonmerchsku",
					new FacesMessage("Special characters are not allowed."));
				return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbBatchLoadDBWrapper.getByNonMerchandiseSku(nonMerchandiseSkuSearchField.trim()));
		this.setRows(new ArrayDataModel<KeycodeBatchBean>(rowItemArray));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String masterItemIdSearchField;
	public String getMasterItemIdSearchField() { return masterItemIdSearchField; }
	public void setMasterItemIdSearchField(String masterItemIdSearchField) { this.masterItemIdSearchField = masterItemIdSearchField; }
	
	public String searchByMasterItemIdButtonClick() throws DataSourceLookupException, DataAccessException {
		if (isNotNull(this.masterItemIdSearchField)&& TextFilter.isSpecialCharacterPresent(this.masterItemIdSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.log(Level.DEBUG, "Special Character Found (masterItemIdSearchField) ::"
					+ this.masterItemIdSearchField);
			context.addMessage(
					"kcbmanageLoad_masteritemid_search_form:search_masteritemid",
					new FacesMessage("Special characters are not allowed."));
				return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbBatchLoadDBWrapper.getByMasterItemId(this.masterItemIdSearchField.trim()));
		this.setRows(new ArrayDataModel<KeycodeBatchBean>(rowItemArray));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	
	
	
	private Integer batchLoadIdSearchField;
	public Integer getBatchLoadIdSearchField() { return batchLoadIdSearchField; }
	public void setBatchLoadIdSearchField(Integer batchLoadIdSearchField) { this.batchLoadIdSearchField = batchLoadIdSearchField; }
	
	public String searchByBatchLoadIdButtonClick() throws DataSourceLookupException, DataAccessException {
		if (this.batchLoadIdSearchField != null && TextFilter.isSpecialCharacterPresent(this.batchLoadIdSearchField.toString())) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.log(Level.DEBUG, "Special Character Found (batchLoadIdSearchField) ::"
					+ this.batchLoadIdSearchField);
			context.addMessage(
					"kcbmanageLoad_loadid_search_form:search_loadid",
					new FacesMessage("Special characters are not allowed."));
				return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbBatchLoadDBWrapper.getByBatchLoadId(this.batchLoadIdSearchField));
		this.setRows(new ArrayDataModel<KeycodeBatchBean>(rowItemArray));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String vendorIdSearchField;
	public String getVendorIdSearchField() { return vendorIdSearchField; }
	public void setVendorIdSearchField(String vendorIdSearchField) { this.vendorIdSearchField = vendorIdSearchField; }
	public String searchByVendorIdButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRowItemArray(KcbBatchLoadDBWrapper.getByVendorId(vendorIdSearchField));
		this.setRows(new ArrayDataModel<KeycodeBatchBean>(rowItemArray));
		this.getKeycodeBankManageLoadTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}

	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankManageLoadTabbedPaneHandler}")
	private KeycodeBankManageLoadTabbedPaneHandler keycodeBankManageLoadTabbedPaneHandler;
	public KeycodeBankManageLoadTabbedPaneHandler getKeycodeBankManageLoadTabbedPaneHandler() { return keycodeBankManageLoadTabbedPaneHandler; }
	public void setKeycodeBankManageLoadTabbedPaneHandler(KeycodeBankManageLoadTabbedPaneHandler keycodeBankManageLoadTabbedPaneHandler) { this.keycodeBankManageLoadTabbedPaneHandler = keycodeBankManageLoadTabbedPaneHandler; }
	
	@ManagedProperty (value="#{keycodeBankManageLoadHandler}")
	private KeycodeBankManageLoadHandler keycodeBankManageLoadHandler;
	public KeycodeBankManageLoadHandler getKeycodeBankManageLoadHandler() { return keycodeBankManageLoadHandler; }
	public void setKeycodeBankManageLoadHandler(KeycodeBankManageLoadHandler keycodeBankManageLoadHandler) { this.keycodeBankManageLoadHandler = keycodeBankManageLoadHandler; }
	
	private boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
