package com.accenture.bby.sdp.web.handlers.audittrail;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SimpleAuditLog;
import com.accenture.bby.sdp.web.extensions.SortableList;
import com.icesoft.faces.component.ext.HtmlDataTable;

public class AuditTrailDatatableHandler extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1212404790151008832L;
	
	private static final Logger logger = Logger.getLogger(AuditTrailDatatableHandler.class.getName());

	private static String logIdColumnName = "Log ID";
	private static String dateColumnName = "Date";
	private static String contractIdColumnName = "Contract ID";
	private static String serialNumberColumnName = "Serial Number";
	private static String lineItemIdColumnName = "LineItem ID";
	private static String actionColumnName = "Action";
	private static String userIdColumnName = "User";
	private static String transactionDateColumnName = "Trans Date";
	private static String storeIdColumnName = "Stor #";
	private static String registerIdColumnName = "Reg #";
	private static String transactionIdColumnName = "Trans #";
	private static String lineIdColumnName = "Line #";
	private static String primarySkuColumnName = "Product SKU";
	private static String relatedSkuColumnName = "Plan SKU";
	private static String customerNameColumnName = "Customer Name";
	private static String deliveryEmailColumnName = "Delivery Email";
	
	private Integer maxRowsDisplay;
	
	public String getDateColumnName() {
		return dateColumnName;
	}
	public String getContractIdColumnName() {
		return contractIdColumnName;
	}
	public String getSerialNumberColumnName() {
		return serialNumberColumnName;
	}
	public String getLineItemIdColumnName() {
		return lineItemIdColumnName;
	}
	public String getActionColumnName() {
		return actionColumnName;
	}
	public String getUserIdColumnName() {
		return userIdColumnName;
	}
	public String getLogIdColumnName() {
		return logIdColumnName;
	}
	public String getTransactionDateColumnName() {
		return transactionDateColumnName;
	}
	public String getStoreIdColumnName() {
		return storeIdColumnName;
	}
	public String getRegisterIdColumnName() {
		return registerIdColumnName;
	}
	public String getTransactionIdColumnName() {
		return transactionIdColumnName;
	}
	public String getLineIdColumnName() {
		return lineIdColumnName;
	}
	public String getPrimarySkuColumnName() {
		return primarySkuColumnName;
	}
	public String getRelatedSkuColumnName() {
		return relatedSkuColumnName;
	}
	public String getCustomerNameColumnName() {
		return customerNameColumnName;
	}
	public String getDeliveryEmailColumnName() {
		return deliveryEmailColumnName;
	}
	public AuditTrailDatatableHandler() {
		super(dateColumnName);
		logger.log(Level.DEBUG, "Initalized.");
	}

	private HtmlDataTable dataTable;
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	private List<SimpleAuditLog> rows;

	
	public List<SimpleAuditLog> getRows() throws DataAccessException, DataSourceLookupException { 
		if (rows == null) {
			rows = AuditTrailDBWrapper.getSimpleAuditLogsByAll(null, null, null, null, null, null, null, null, null, null, null, 20);
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(List<SimpleAuditLog> rows) { this.rows = rows; }
	
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
		Comparator<SimpleAuditLog> comparator = new Comparator<SimpleAuditLog>() {
            @Override
			public int compare(SimpleAuditLog o1, SimpleAuditLog o2) {
            	SimpleAuditLog c1 = o1;
            	SimpleAuditLog c2 = o2;

            	
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(logIdColumnName)) {
                    return ascending ? ignoreNull(c1.getLogId()).compareTo(ignoreNull(c2.getLogId())) :
                            ignoreNull(c2.getLogId()).compareTo(ignoreNull(c1.getLogId()));
                    
                } else if (sortColumnName.equals(dateColumnName)) {
                    return ascending ? ignoreNull(c1.getDate()).compareTo(ignoreNull(c2.getDate())) :
                        ignoreNull(c2.getDate()).compareTo(ignoreNull(c1.getDate()));
            
                } else if (sortColumnName.equals(contractIdColumnName)) {
                    return ascending ? ignoreNull(c1.getContractId()).compareTo(ignoreNull(c2.getContractId())) :
                        ignoreNull(c2.getContractId()).compareTo(ignoreNull(c1.getContractId()));
            
                } else if (sortColumnName.equals(serialNumberColumnName)) {
                    return ascending ? ignoreNull(c1.getSerialNumber()).compareTo(ignoreNull(c2.getSerialNumber())) :
                        ignoreNull(c2.getSerialNumber()).compareTo(ignoreNull(c1.getSerialNumber()));
            
                } else if (sortColumnName.equals(lineItemIdColumnName)) {
                    return ascending ? ignoreNull(c1.getLineItemId()).compareTo(ignoreNull(c2.getLineItemId())) :
                        ignoreNull(c2.getLineItemId()).compareTo(ignoreNull(c1.getLineItemId()));
            
                } else if (sortColumnName.equals(actionColumnName)) {
                    return ascending ? ignoreNull(c1.getAction()).compareTo(ignoreNull(c2.getAction())) :
                        ignoreNull(c2.getAction()).compareTo(ignoreNull(c1.getAction()));
            
                } else if (sortColumnName.equals(userIdColumnName)) {
                    return ascending ? ignoreNull(c1.getUserId()).compareTo(ignoreNull(c2.getUserId())) :
                        ignoreNull(c2.getUserId()).compareTo(ignoreNull(c1.getUserId()));
            
                } else if (sortColumnName.equals(transactionDateColumnName)) {
                    return ascending ? ignoreNull(c1.getTransactionDate()).compareTo(ignoreNull(c2.getTransactionDate()) ):
                        ignoreNull(c2.getTransactionDate()).compareTo(ignoreNull(c1.getTransactionDate()));
            
                } else if (sortColumnName.equals(storeIdColumnName)) {
                    return ascending ? ignoreNull(c1.getStoreId()).compareTo(ignoreNull(c2.getStoreId()) ):
                        ignoreNull(c2.getStoreId()).compareTo(ignoreNull(c1.getStoreId()));
            
                } else if (sortColumnName.equals(registerIdColumnName)) {
                    return ascending ? ignoreNull(c1.getRegisterId()).compareTo(ignoreNull(c2.getRegisterId()) ):
                        ignoreNull(c2.getRegisterId()).compareTo(ignoreNull(c1.getRegisterId()));
            
                } else if (sortColumnName.equals(transactionIdColumnName)) {
                    return ascending ? ignoreNull(c1.getTransactionId()).compareTo(ignoreNull(c2.getTransactionId()) ):
                        ignoreNull(c2.getTransactionId()).compareTo(ignoreNull(c1.getTransactionId()));
            
                } else if (sortColumnName.equals(lineIdColumnName)) {
                    return ascending ? ignoreNull(c1.getLineId()).compareTo(ignoreNull(c2.getLineId()) ):
                        ignoreNull(c2.getLineId()).compareTo(ignoreNull(c1.getLineId()));
            
                } else if (sortColumnName.equals(primarySkuColumnName)) {
                    return ascending ? ignoreNull(c1.getPrimarySku()).compareTo(ignoreNull(c2.getPrimarySku()) ):
                        ignoreNull(c2.getPrimarySku()).compareTo(ignoreNull(c1.getPrimarySku()));
            
                } else if (sortColumnName.equals(relatedSkuColumnName)) {
                    return ascending ? ignoreNull(c1.getRelatedSku()).compareTo(ignoreNull(c2.getRelatedSku()) ):
                        ignoreNull(c2.getRelatedSku()).compareTo(ignoreNull(c1.getRelatedSku()));
            
                } else if (sortColumnName.equals(customerNameColumnName)) {
                    return ascending ? ignoreNull(c1.getCustomerName()).compareTo(ignoreNull(c2.getCustomerName()) ):
                        ignoreNull(c2.getCustomerName()).compareTo(ignoreNull(c1.getCustomerName()));
            
                } else if (sortColumnName.equals(deliveryEmailColumnName)) {
                    return ascending ? ignoreNull(c1.getDeliveryEmail()).compareTo(ignoreNull(c2.getDeliveryEmail()) ):
                        ignoreNull(c2.getDeliveryEmail()).compareTo(ignoreNull(c1.getDeliveryEmail()));
            
                } else return 0;
            }
        };
        Collections.sort(rows, comparator);
	}
	
	public String viewButtonClick() throws DataSourceLookupException, DataAccessException {
		SimpleAuditLog selectedRow = (SimpleAuditLog) dataTable.getRowData();
		logger.log(Level.DEBUG, "Selected related audit log.");
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, "Selected Row >> \n" + selectedRow);
		}
		return NavigationStrings.getParameterizedUrl(NavigationStrings.AUDIT_TRAIL_DETAIL_DEFAULT_PAGE, AuditTrailDetailHandler.REQUEST_PARAM, selectedRow.getLogId());
	}
	
	public Integer getMaxRowsDisplay() {
		return maxRowsDisplay;
	}
	public void setMaxRowsDisplay(Integer maxRowsDisplay) {
		this.maxRowsDisplay = maxRowsDisplay;
	}
	
	public String refreshButtonClick() {
			return NavigationStrings.CURRENT_VIEW;
	}
}
