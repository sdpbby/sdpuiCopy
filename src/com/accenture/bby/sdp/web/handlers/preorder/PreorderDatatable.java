package com.accenture.bby.sdp.web.handlers.preorder;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.accenture.bby.sdp.db.PreorderDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.PreorderBean;
import com.accenture.bby.sdp.web.extensions.SortableList;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ManagedBean (name="preorderDatatable")
@ViewScoped
public class PreorderDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static String catalogIdColumnName = "Catalog ID";
	private static String vendorIdColumnName = "Vendor";
	private static String productSkuColumnName = "SKU";
	private static String descriptionColumnName = "Description";
	private static String streetDateColumnName = "Street Date";
	private static String preorderCountColumnName = "Pre-Order Count";
	private static String statusColumnName = "Status";
	private static String actionColumnName = "Action";
	
	public String getCatalogIdColumnName() { return catalogIdColumnName; }
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getProductSkuColumnName() { return productSkuColumnName; }
	public String getDescriptionColumnName() { return descriptionColumnName; }
	public String getStreetDateColumnName() { return streetDateColumnName; }
	public String getPreorderCountColumnName() { return preorderCountColumnName; }
	public String getStatusColumnName() { return statusColumnName; }
	public String getActionColumnName() { return actionColumnName; }

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	
	public PreorderDatatable() throws DataSourceLookupException, DataAccessException {
		super(streetDateColumnName);
	}
	
	private List<PreorderBean> rows;
	
	public List<PreorderBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rows = PreorderDBWrapper.getAll();
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(List<PreorderBean> rows) { this.rows = rows; }
	
	public String deleteButtonClick() {
		PreorderBean tmp = (PreorderBean) dataTable.getRowData();
		this.getPreorderHandler().setOldPreorderBean(new PreorderBean(tmp));
		this.getPreorderHandler().setNewPreorderBean(new PreorderBean(tmp));
		this.getPreorderTabbedPaneHandler().displayDeleteTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	@Override
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}
	
	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	@Override
	protected void sort() {
		Comparator<PreorderBean> comparator = new Comparator<PreorderBean>() {
            @Override
			public int compare(PreorderBean o1, PreorderBean o2) {
            	PreorderBean c1 = o1;
            	PreorderBean c2 = o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(catalogIdColumnName)) {
                    return ascending ? c1.getCatalogId().compareTo(c2.getCatalogId()) :
                            c2.getCatalogId().compareTo(c1.getCatalogId());
                    
                } else if (sortColumnName.equals(vendorIdColumnName)) {
                    return ascending ? c1.getVendorId().compareTo(c2.getVendorId()) :
                        c2.getVendorId().compareTo(c1.getVendorId());
            
                } else if (sortColumnName.equals(productSkuColumnName)) {
                    return ascending ? c1.getProductSku().compareTo(c2.getProductSku()) :
                        c2.getProductSku().compareTo(c1.getProductSku());
            
                } else if (sortColumnName.equals(descriptionColumnName)) {
                    return ascending ? c1.getDescription().compareTo(c2.getDescription()) :
                        c2.getDescription().compareTo(c1.getDescription());
            
                } else if (sortColumnName.equals(streetDateColumnName)) {
                    return ascending ? c1.getStreetDate().compareTo(c2.getStreetDate()) :
                        c2.getStreetDate().compareTo(c1.getStreetDate());
            
                } else if (sortColumnName.equals(preorderCountColumnName)) {
                    return ascending ? c1.getPreorderCount().compareTo(c2.getPreorderCount()) :
                        c2.getPreorderCount().compareTo(c1.getPreorderCount());
            
                } else if (sortColumnName.equals(statusColumnName)) {
                    return ascending ? c1.getStatusName().compareTo(c2.getStatusName()) :
                        c2.getStatusName().compareTo(c1.getStatusName());
            
                } else return 0;
            }
        };
        Collections.sort(rows, comparator);
	}
	
	public String searchByProductSkuButtonClick() throws DataSourceLookupException, DataAccessException {
		if (isNotNull(this.getProductSkuSearchField())
				&& TextFilter.isSpecialCharacterPresent(this
						.getProductSkuSearchField().trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"preorder_productsku_search_form:search_productSku",
					new FacesMessage("Special characters are not allowed."));
			return NavigationStrings.CURRENT_VIEW;
		}this.setRows(PreorderDBWrapper.getByProductSku(this.getProductSkuSearchField()));
		this.getPreorderTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String searchByVendorIdButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRows(PreorderDBWrapper.getByVendorId(this.getVendorIdSearchField()));
		this.getPreorderTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String searchByAllButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRows(PreorderDBWrapper.getAll());
		this.getPreorderTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String editButtonClick() {
		PreorderBean tmp = (PreorderBean) dataTable.getRowData();
		this.getPreorderHandler().setOldPreorderBean(new PreorderBean(tmp));
		this.getPreorderHandler().setNewPreorderBean(new PreorderBean(tmp));
		this.getPreorderTabbedPaneHandler().displayEditTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	/*
	 * Preorder screen search fields
	 */
	// search by product sku (existing pre-orders only)
	private String productSkuSearchField;
	// search by vendor id
	private String vendorIdSearchField;
	// list of vendors
	private List<SelectItem> vendors;
	
	public String getProductSkuSearchField() { return productSkuSearchField; }
	public String getVendorIdSearchField() { return vendorIdSearchField; }
	
	public List<SelectItem> getVendors() throws DataSourceLookupException, DataAccessException {
		if (vendors == null || vendors.size() < 1) {
			vendors = PreorderDBWrapper.getVendorList();
		}
		return vendors;
	}
	
	public void setProductSkuSearchField(String productSkuSearchField) { this.productSkuSearchField = productSkuSearchField; }
	public void setVendorIdSearchField(String vendorIdSearchField) { this.vendorIdSearchField = vendorIdSearchField; }
	
	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{preorderTabbedPaneHandler}")
	private PreorderTabbedPaneHandler preorderTabbedPaneHandler;
	public PreorderTabbedPaneHandler getPreorderTabbedPaneHandler() { return preorderTabbedPaneHandler; }
	public void setPreorderTabbedPaneHandler(PreorderTabbedPaneHandler preorderTabbedPaneHandler) { this.preorderTabbedPaneHandler = preorderTabbedPaneHandler; }
	
	@ManagedProperty (value="#{preorderHandler}")
	private PreorderHandler preorderHandler;
	public PreorderHandler getPreorderHandler() { return preorderHandler; }
	public void setPreorderHandler(PreorderHandler preorderHandler) { this.preorderHandler = preorderHandler; }
	
}
