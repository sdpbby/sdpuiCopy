package com.accenture.bby.sdp.web.handlers.catalog;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.utl.CsvExportUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.extensions.SortableList;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ManagedBean (name="catalogDatatable")
@ViewScoped
public class CatalogDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static String catalogIdColumnName = "Catalog ID";
	private static String primarySkuColumnName = "Product SKU";
	private static String masterVendorIdColumnName = "Master Vendor ID";
	private static String primarySkuDescriptionColumnName = "Product Description";
	private static String vendorNameColumnName = "Vendor Name";
	private static String vendorIdColumnName = "Vendor ID";
	private static String categoryColumnName = "Category";
	private static String vendorTriggerSkuColumnName = "Vendor Trigger SKU";
	private static String actionColumnName = "Action";
	
	public String getCatalogIdColumnName() { return catalogIdColumnName; }
	public String getPrimarySkuColumnName() { return primarySkuColumnName; }
	public String getMasterVendorIdColumnName() { return masterVendorIdColumnName; }
	public String getPrimarySkuDescriptionColumnName() { return primarySkuDescriptionColumnName; }
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getVendorIdColumnName() { return vendorIdColumnName; }
	public String getCategoryColumnName() { return categoryColumnName; }
	public String getVendorTriggerSkuColumnName() { return vendorTriggerSkuColumnName; }
	public String getActionColumnName() { return actionColumnName; }
	
	public CatalogDatatable() throws DataSourceLookupException, DataAccessException {
		super(primarySkuDescriptionColumnName);
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<CatalogBean> rows;
	
	//public CatalogBean[] getRowItemArray() { return rowItemArray; }
	public List<CatalogBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rows = CatalogDBWrapper.getAll();
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(List<CatalogBean> rows) { this.rows = rows; }
	
	public String deleteButtonClick() {
		throw new UnsupportedOperationException("Catalog setup does not support delete. This method should never be called!");
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
		Comparator<CatalogBean> comparator = new Comparator<CatalogBean>() {
            @Override
			public int compare(CatalogBean o1, CatalogBean o2) {
            	CatalogBean c1 = o1;
            	CatalogBean c2 = o2;

            	
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(catalogIdColumnName)) {
                    return ascending ? ignoreNull(c1.getCatalogId()).compareTo(ignoreNull(c2.getCatalogId())) :
                            ignoreNull(c2.getCatalogId()).compareTo(ignoreNull(c1.getCatalogId()));
                    
                } else if (sortColumnName.equals(primarySkuColumnName)) {
                    return ascending ? ignoreNull(c1.getPrimarySku()).compareTo(ignoreNull(c2.getPrimarySku())) :
                        ignoreNull(c2.getPrimarySku()).compareTo(ignoreNull(c1.getPrimarySku()));
            
                } else if (sortColumnName.equals(masterVendorIdColumnName)) {
                    return ascending ? ignoreNull(c1.getMasterVendorId()).compareTo(ignoreNull(c2.getMasterVendorId())) :
                        ignoreNull(c2.getMasterVendorId()).compareTo(ignoreNull(c1.getMasterVendorId()));
            
                } else if (sortColumnName.equals(primarySkuDescriptionColumnName)) {
                    return ascending ? ignoreNull(c1.getPrimarySkuDescription()).compareTo(ignoreNull(c2.getPrimarySkuDescription())) :
                        ignoreNull(c2.getPrimarySkuDescription()).compareTo(ignoreNull(c1.getPrimarySkuDescription()));
            
                } else if (sortColumnName.equals(vendorNameColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorName()).compareTo(ignoreNull(c2.getVendorName())) :
                        ignoreNull(c2.getVendorName()).compareTo(ignoreNull(c1.getVendorName()));
            
                } else if (sortColumnName.equals(vendorIdColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorId()).compareTo(ignoreNull(c2.getVendorId())) :
                        ignoreNull(c2.getVendorId()).compareTo(ignoreNull(c1.getVendorId()));
            
                } else if (sortColumnName.equals(categoryColumnName)) {
                    return ascending ? ignoreNull(c1.getCategory()).compareTo(ignoreNull(c2.getCategory())) :
                        ignoreNull(c2.getCategory()).compareTo(ignoreNull(c1.getCategory()));
            
                } else if (sortColumnName.equals(vendorTriggerSkuColumnName)) {
                    return ascending ? ignoreNull(c1.getVendorTriggerSku()).compareTo(ignoreNull(c2.getVendorTriggerSku()) ):
                        ignoreNull(c2.getVendorTriggerSku()).compareTo(ignoreNull(c1.getVendorTriggerSku()));
            
                } else return 0;
            }
        };
        Collections.sort(rows, comparator);
	}
	
	public String searchByAllButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRows(CatalogDBWrapper.getAll());
		this.getCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String searchByProductSkuButtonClick() throws DataSourceLookupException, DataAccessException {
		if (TextFilter.isSpecialCharacterPresent(this.productSkuSearchField.trim())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(
					"catalog_productsku_search_form:search_productSku",
					new FacesMessage("Special characters are not allowed."));
				return NavigationStrings.CURRENT_VIEW;
		}
		this.setRows(CatalogDBWrapper.getByProductSku(productSkuSearchField));
		this.getCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
		
	}
	public String searchByVendorIdButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRows(CatalogDBWrapper.getByVendorId(vendorIdSearchField));
		this.getCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
		
	}
	public String searchByCategoryButtonClick() throws DataSourceLookupException, DataAccessException {
		this.setRows(CatalogDBWrapper.getByCategory(categorySearchField));
		this.getCatalogTabbedPaneHandler().displaySearchTab();
		return NavigationStrings.CURRENT_VIEW;
		
	}

	public String viewButtonClick() throws DataSourceLookupException, DataAccessException {
		CatalogBean selectedRow = (CatalogBean) dataTable.getRowData();
		
		if (selectedRow != null && selectedRow.getMasterVendorId() != null) {
			this.catalogHandler.catalogTypeFlag = CatalogHandler.CREATE_PROFILE;
		} else {
			this.catalogHandler.catalogTypeFlag = CatalogHandler.CREATE_PRODUCT;
		}
		
		selectedRow.setWorkFlowAttributes(CatalogDBWrapper.getWorkFlowAttributes(selectedRow.getCatalogId()));
		this.getCatalogHandler().setOldCatalogBean(new CatalogBean(selectedRow));
		this.getCatalogHandler().setNewCatalogBean(new CatalogBean(selectedRow));
		this.getCatalogTabbedPaneHandler().displayViewTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String exportButtonClick() throws IOException {
		String fileName = "SDP_CATALOG_Export.csv";
		byte[] exportBytes = CsvExportUtil.getCatalogExportCsvString(this.rows).getBytes();
		prepareHeaderForDownloadFile(exportBytes, fileName, "application/csv");

		// Download dialog will only appear if null is returned.
		// Returning actual navigation address will block download dialog.
		// return this.getNavigationAddress();
		return null;
//		return CsvExportUtil.export(rows);
	}
	
	private void prepareHeaderForDownloadFile(byte[] bytes, String fileName, String responseContentType) throws IOException {
		FacesContext faces = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

		response.setContentType(responseContentType);
		response.setContentLength(bytes.length);
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try {
			ServletOutputStream out;
			out = response.getOutputStream();
			out.write(bytes);
		} finally {
			faces.responseComplete();
		}
	}
	
	/*
	 * search fields
	 */
	private String productSkuSearchField;
	public String getProductSkuSearchField() { return productSkuSearchField; }
	public void setProductSkuSearchField(String productSkuSearchField) { this.productSkuSearchField = productSkuSearchField; }
	
	private String vendorIdSearchField;
	public String getVendorIdSearchField() { return vendorIdSearchField; }
	public void setVendorIdSearchField(String vendorIdSearchField) { this.vendorIdSearchField = vendorIdSearchField; }
	
	private String categorySearchField;
	public String getCategorySearchField() { return categorySearchField; }
	public void setCategorySearchField(String categorySearchField) { this.categorySearchField = categorySearchField; }
	
	
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{catalogTabbedPaneHandler}")
	private CatalogTabbedPaneHandler catalogTabbedPaneHandler;
	public CatalogTabbedPaneHandler getCatalogTabbedPaneHandler() { return catalogTabbedPaneHandler; }
	public void setCatalogTabbedPaneHandler(CatalogTabbedPaneHandler catalogTabbedPaneHandler) { this.catalogTabbedPaneHandler = catalogTabbedPaneHandler; }
	
	@ManagedProperty (value="#{catalogHandler}")
	private CatalogHandler catalogHandler;
	public CatalogHandler getCatalogHandler() { return catalogHandler; }
	public void setCatalogHandler(CatalogHandler catalogHandler) { this.catalogHandler = catalogHandler; }
	
	
	
}
