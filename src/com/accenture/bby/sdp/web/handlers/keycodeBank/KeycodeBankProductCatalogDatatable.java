package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import com.accenture.bby.sdp.db.KcbCatalogDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.extensions.SortableList;

@ManagedBean (name="keycodeBankProductCatalogDatatable")
@ViewScoped
public class KeycodeBankProductCatalogDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static String productIdColumnName = "Product ID";
	private static String vendorIdColumnName = "Vendor ID";
	private static String vendorNameColumnName = "Vendor Name";
	private static String merchandiseSkuColumnName = "Merchandise Sku";
	private static String nonMerchandiseSkuColumnName = "Non-Merchandise Sku";
	private static String masterItemIdColumnName = "Master Item ID";
	private static String relatedSkuColumnName = "Related Sku";
	
	private static String productDescriptionColumnName = "Description";
	private static String loadSizeColumnName = "Load Size";
	private static String thresholdColumnName = "Depletion Threshold";
	private static String actionColumnName = "Action";
	
	public String getProductIdColumnName() {	return productIdColumnName;	}
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getMerchandiseSkuColumnName() { return merchandiseSkuColumnName; }
	public String getNonMerchandiseSkuColumnName() { return nonMerchandiseSkuColumnName; }
	public String getMasterItemIdColumnName() { return masterItemIdColumnName; }
	public String getProductDescriptionColumnName() { return productDescriptionColumnName; }
	public String getLoadSizeColumnName() { return loadSizeColumnName; }
	public String getThresholdColumnName() { return thresholdColumnName; }
	public String getActionColumnName() { return actionColumnName; }
	public String getRelatedSkuColumnName() { return relatedSkuColumnName;}
	
	public KeycodeBankProductCatalogDatatable() throws DataSourceLookupException, DataAccessException {
		super(productDescriptionColumnName); 
	}

	
	private KcbProductBean[] rowItemArray = new KcbProductBean[0];
	private DataModel<KcbProductBean> rows;
	
	public KcbProductBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<KcbProductBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rowItemArray = KcbCatalogDBWrapper.getAllProducts();
			rows = new ArrayDataModel<KcbProductBean>(rowItemArray);
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(DataModel<KcbProductBean> rows) { this.rows = rows; }
	public void setRowItemArray(KcbProductBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

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
		Comparator<KcbProductBean> comparator = new Comparator<KcbProductBean>() {
            @Override
			public int compare(KcbProductBean o1, KcbProductBean o2) {
            	KcbProductBean c1 = o1;
            	KcbProductBean c2 = o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(productIdColumnName)) {
                    return ascending ? ignoreNull(c1.getProductId()).compareTo(ignoreNull(c2.getProductId())):
                            ignoreNull(c2.getProductId()).compareTo(ignoreNull(c1.getProductId()));
                    
                } else if (sortColumnName.equals(vendorIdColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorId()).compareTo(ignoreNull(c2.getVendorId())):
                        ignoreNull(c2.getVendorId()).compareTo(ignoreNull(c1.getVendorId()));
                
                } else if (sortColumnName.equals(vendorNameColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorName()).compareTo(ignoreNull(c2.getVendorName())):
                        ignoreNull(c2.getVendorName()).compareTo(ignoreNull(c1.getVendorName()));
            
                } else if (sortColumnName.equals(merchandiseSkuColumnName)) {
                    return ascending ? ignoreNull(c1.getMerchandiseSku()).compareTo(ignoreNull(c2.getMerchandiseSku())):
                        ignoreNull(c2.getMerchandiseSku()).compareTo(ignoreNull(c1.getMerchandiseSku()));
            
                } else if (sortColumnName.equals(nonMerchandiseSkuColumnName)) {
                    return ascending ? ignoreNull(c1.getNonMerchandiseSku()).compareTo(ignoreNull(c2.getNonMerchandiseSku())):
                        ignoreNull(c2.getNonMerchandiseSku()).compareTo(ignoreNull(c1.getNonMerchandiseSku()));
            
                } else if (sortColumnName.equals(masterItemIdColumnName)) {
                    return ascending ? ignoreNull(c1.getMasterItemId()).compareTo(ignoreNull(c2.getMasterItemId())):
                        ignoreNull(c2.getMasterItemId()).compareTo(ignoreNull(c1.getMasterItemId()));
            
                } else if (sortColumnName.equals(relatedSkuColumnName)) {
                    return ascending ? ignoreNull(c1.getRelatedSKU()).compareTo(ignoreNull(c2.getRelatedSKU())):
                        ignoreNull(c2.getRelatedSKU()).compareTo(ignoreNull(c1.getRelatedSKU()));
            
                } else if (sortColumnName.equals(productDescriptionColumnName)) {
                    return ascending ? ignoreNull(c1.getDescription()).compareTo(ignoreNull(c2.getDescription())):
                        ignoreNull(c2.getDescription()).compareTo(ignoreNull(c1.getDescription()));
            
                } else if (sortColumnName.equals(loadSizeColumnName)) {
                    return ascending ? ignoreNull(c1.getLoadSize()).compareTo(ignoreNull(c2.getLoadSize())):
                        ignoreNull(c2.getLoadSize()).compareTo(ignoreNull(c1.getLoadSize()));
            
                } else if (sortColumnName.equals(thresholdColumnName)) {
                    return ascending ? ignoreNull(c1.getThreshold()).compareTo(ignoreNull(c2.getThreshold())):
                        ignoreNull(c2.getThreshold()).compareTo(ignoreNull(c1.getThreshold()));
            
                } else return 0;
            }
        };
        Arrays.sort(rowItemArray, comparator);
	}
	
	public String editButtonClick() {
		KcbProductBean tmp = rows.getRowData();
		this.getKeycodeBankProductCatalogHandler().setOldKcbProductBean(new KcbProductBean(tmp));
		this.getKeycodeBankProductCatalogHandler().setNewKcbProductBean(new KcbProductBean(tmp));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displayEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String merchSkuSearchField;
	public String getMerchSkuSearchField() { return merchSkuSearchField; }
	public void setMerchSkuSearchField(String merchSkuSearchField) { this.merchSkuSearchField = merchSkuSearchField; }
	
	public String searchByMerchSkuButtonClick() throws DataSourceLookupException, DataAccessException {
// This is backwards. getProductsBySku is actually searching by merchandise sku
// this backwards naming convention is currently set in the database
		if (isNotNull(merchSkuSearchField)
				&& TextFilter.isSpecialCharacterPresent(merchSkuSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("kcbcatalog_merchsku_search_form:search_merchSku", new FacesMessage(
					"Special characters not allowed."));
			return NavigationStrings.CURRENT_VIEW;
		}this.setRowItemArray(KcbCatalogDBWrapper.getProductsBySku(merchSkuSearchField));
		this.setRows(new ArrayDataModel<KcbProductBean>(rowItemArray));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String nonMerchSkuSearchField;
	public String getNonMerchSkuSearchField() { return nonMerchSkuSearchField; }
	public void setNonMerchSkuSearchField(String nonMerchSkuSearchField) { this.nonMerchSkuSearchField = nonMerchSkuSearchField; }
	
	public String searchByNonMerchSkuButtonClick() throws DataSourceLookupException, DataAccessException {
// This is backwards. getProductsByMerchSku is actually searching by NON-merchandise sku
// this backwards naming convention is currently set in the database
		if (isNotNull(nonMerchSkuSearchField)
				&& TextFilter
						.isSpecialCharacterPresent(nonMerchSkuSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Special characters not allowed."));
			return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbCatalogDBWrapper.getProductsByNonMerchSku(nonMerchSkuSearchField));
		this.setRows(new ArrayDataModel<KcbProductBean>(rowItemArray));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String masterItemIdSearchField;
	public String getMasterItemIdSearchField() { return masterItemIdSearchField; }
	public void setMasterItemIdSearchField(String masterItemIdSearchField) { this.masterItemIdSearchField = masterItemIdSearchField; }
	public String searchByMasterItemIdButtonClick() throws DataSourceLookupException, DataAccessException {
		if (isNotNull(masterItemIdSearchField)
				&& TextFilter.isSpecialCharacterPresent(masterItemIdSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("kcbcatalog_masterItemId_search_form:search_masterItemId", new FacesMessage(
					"Special characters not allowed."));
			return NavigationStrings.CURRENT_VIEW;
		}
		this.setRowItemArray(KcbCatalogDBWrapper.getProductsByMasterItemId(masterItemIdSearchField));
		this.setRows(new ArrayDataModel<KcbProductBean>(rowItemArray));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	private String vendorIdSearchField;
	public String getVendorIdSearchField() { return vendorIdSearchField; }
	public void setVendorIdSearchField(String vendorIdSearchField) { this.vendorIdSearchField = vendorIdSearchField; }
	public String searchByVendorIdButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRowItemArray(KcbCatalogDBWrapper.getProductsByVendorId(vendorIdSearchField));
		this.setRows(new ArrayDataModel<KcbProductBean>(rowItemArray));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String searchByAllButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRowItemArray(KcbCatalogDBWrapper.getAllProducts());
		this.setRows(new ArrayDataModel<KcbProductBean>(rowItemArray));
		this.getKeycodeBankProductCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankProductCatalogTabbedPaneHandler}")
	private KeycodeBankProductCatalogTabbedPaneHandler keycodeBankProductCatalogTabbedPaneHandler;
	public KeycodeBankProductCatalogTabbedPaneHandler getKeycodeBankProductCatalogTabbedPaneHandler() { return keycodeBankProductCatalogTabbedPaneHandler; }
	public void setKeycodeBankProductCatalogTabbedPaneHandler(KeycodeBankProductCatalogTabbedPaneHandler keycodeBankProductCatalogTabbedPaneHandler) { this.keycodeBankProductCatalogTabbedPaneHandler = keycodeBankProductCatalogTabbedPaneHandler; }
	
	@ManagedProperty (value="#{keycodeBankProductCatalogHandler}")
	private KeycodeBankProductCatalogHandler keycodeBankProductCatalogHandler;
	public KeycodeBankProductCatalogHandler getKeycodeBankProductCatalogHandler() { return keycodeBankProductCatalogHandler; }
	public void setKeycodeBankProductCatalogHandler(KeycodeBankProductCatalogHandler keycodeBankProductCatalogHandler) { this.keycodeBankProductCatalogHandler = keycodeBankProductCatalogHandler; }
	
	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
	
}
