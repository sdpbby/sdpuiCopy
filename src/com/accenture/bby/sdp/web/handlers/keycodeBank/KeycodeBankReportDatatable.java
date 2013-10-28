package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import com.accenture.bby.sdp.db.KcbReportDBWrapper;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbReportBean;
import com.accenture.bby.sdp.web.extensions.SortableList;

@ManagedBean (name="keycodeBankReportDatatable")
@ViewScoped
public class KeycodeBankReportDatatable extends SortableList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * Report variables and getters/setters
	 */
	private final Calendar c = Calendar.getInstance();
	private static final int DAYS_DEFAULT = 7;
	private final int SELECTED_DAY_OF_MONTH_DEFAULT = c.get(Calendar.DAY_OF_MONTH);
	private final int SELECTED_MONTH_DEFAULT = c.get(Calendar.MONTH);
	private final int SELECTED_YEAR_DEFAULT = c.get(Calendar.YEAR);
	private final Date DATE_RANGE_MIN_DEFAULT = DateUtil.clearTime(new Date());
	private final Date DATE_RANGE_MAX_DEFAULT = DateUtil.clearTime(DateUtil.getNextDay(new Date()));
	
	private boolean showBelowThresholdOnly;
	public boolean isShowBelowThresholdOnly() { return showBelowThresholdOnly; }
	public void setShowBelowThresholdOnly(boolean showBelowThresholdOnly) { this.showBelowThresholdOnly = showBelowThresholdOnly; }
	
	private Integer daysToDepletion = DAYS_DEFAULT;
	public Integer getDaysToDepletion() { return daysToDepletion; }
	public void setDaysToDepletion(Integer daysToDepletion) { this.daysToDepletion = daysToDepletion; }
	public SelectItem[] getDaysToDepletionOptions() {
		SelectItem[] items = new SelectItem[3];
		items[0] = new SelectItem(7, "7 Days");
		items[1] = new SelectItem(14, "14 Days");
		items[2] = new SelectItem(30, "30 Days");
		return items;
	}
	
	private int dayOfMonth = SELECTED_DAY_OF_MONTH_DEFAULT;
	public int getDayOfMonth() { return dayOfMonth; }
	public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }
	
	private int month = SELECTED_MONTH_DEFAULT;
	public int getMonth() { return month; }
	public void setMonth(int month) { this.month = month; }
	
	private int year = SELECTED_YEAR_DEFAULT;
	public int getYear() { return year; }
	public void setYear(int year) { this.year = year; }
	
	private Date minDate = DATE_RANGE_MIN_DEFAULT;
	public Date getMinDate() { return minDate; }
	public void setMinDate(Date minDate) { this.minDate = minDate; }
	
	private Date maxDate = DATE_RANGE_MAX_DEFAULT;
	public Date getMaxDate() { return maxDate; }
	public void setMaxDate(Date maxDate) { this.maxDate = maxDate; }
	
	private String vendorId;
	public String getVendorId() { return vendorId; }
	public void setVendorId(String vendorId) { this.vendorId = vendorId; }
	
	private String sku;
	public String getSku() { return sku; }
	public void setSku(String sku) { this.sku = sku; }
	
	public void resetReportParams() {
		daysToDepletion = DAYS_DEFAULT;
		dayOfMonth = SELECTED_DAY_OF_MONTH_DEFAULT;
		month = SELECTED_MONTH_DEFAULT;
		year = SELECTED_YEAR_DEFAULT;
		minDate = DATE_RANGE_MIN_DEFAULT;
		maxDate = DATE_RANGE_MAX_DEFAULT;
	}
	
	public SelectItem[] getYearsList() {
		int minYear = 2008;
		SelectItem[] selectItems = new SelectItem[(SELECTED_YEAR_DEFAULT+1) - minYear];
		
		for (int i = 0; i < selectItems.length; i++) {
			selectItems[i] = new SelectItem(minYear+i, (minYear+i)+"");
		}
		return selectItems;
	}
	
	public SelectItem[] getMonthsList() {
		SelectItem[] selectItems = new SelectItem[12];
		for (int i = 0; i < 12; i++) {
			selectItems[i] = new SelectItem(i, DateUtil.getMonthsList()[i]);
		}
		return selectItems;
	}
	
	private SelectItem[] daysDropdownList;
	public SelectItem[] getDaysDropdownList() { return daysDropdownList; }
	public void setDaysDropdownList(SelectItem[] daysDropdownList) { this.daysDropdownList = daysDropdownList; }
	
	/*
	 * Value change listeners
	 */
	public void processYearChange(ValueChangeEvent event) {
		year = (Integer) event.getNewValue();
		resetDaysList(year, month);
		FacesContext.getCurrentInstance().renderResponse();
	}
	public void processMonthChange(ValueChangeEvent event) {
		month = (Integer) event.getNewValue();
		resetDaysList(year, month);
		FacesContext.getCurrentInstance().renderResponse();
	}
	
	private void resetDaysList(int year, int month) {
		int days = DateUtil.getMaxDaysInMonth(year, month);
		SelectItem[] selectItems = new SelectItem[days];
		for (int i = 0; i < selectItems.length; i++) {
			selectItems[i] = new SelectItem(i+1, (i+1)+"");
		}
		setDaysDropdownList(selectItems);
	}
	
	/*
	 * Table headers
	 */
	private String reportMainHeader;
	public String getReportMainHeader() { return reportMainHeader; }
	public void setReportMainHeader(String reportMainHeader) { this.reportMainHeader = reportMainHeader; }
	
	private static final String dailyReportTitle = "DAILY REPORT: ?.";
	public String getDailyReportTitle() {
		return dailyReportTitle.replace("?", year + "/" + (month+1) + "/" + dayOfMonth);
	}
	private static final String monthlyReportTitle = "MONTHLY REPORT: ?.";
	public String getMonthlyReportTitle() {
		return monthlyReportTitle.replace("?", year + "/" + (month+1));
	}
	private static final String depletionReportTitle = "DEPLETION REPORT: Keycodes used in the last ? days.";
	public String getDepletionReportTitle() {
		return depletionReportTitle.replace("?", daysToDepletion + "");
	}
	
	private static String vendorIdColumnName = "Vendor ID";
	private static String vendorNameColumnName = "Vendor Name";
	private static String merchandiseSkuColumnName = "Merchandise SKU";
	private static String nonMerchandiseSkuColumnName = "Non-Merchandise SKU";
	private static String masterItemIdColumnName = "Master Item ID";
	private static String productDescriptionColumnName = "Description";
	private static String reservedKeysColumnName = "Reserved";
	private static String activatedKeysColumnName = "Activated";
	private static String remainingKeysColumnName = "Available";
	private static String inactiveKeysColumnName = "Inactive";
	private static String mismatchedKeysColumnName = "Mismatched";
	private static String burnRateColumnName = "Burn Rate";
	private static String errorsPer1KOrdersColumnName = "Errors Per 1K";
	private static String maxPerDayColumnName = "Max Per Day";
	private static String maxPerMonthColumnName = "Max Per Month";
	private static String keysUsedLastXDaysColumnName = "Keys Used Last ? Days";
	private static String estDaysToDepletionColumnName = "Days To Depletion";
	private static String orderIdColumnName = "Order ID";
	private static String keyCodeReservedColumnName = "Keycode Reserved";
	private static String keyCodeActualColumnName = "Keycode Actual";
	private static String dateColumnName = "Date";
	private static String expiredKeysColumnName = "Expired";
	
	
	
	public String getVendorIdColumnName() { return vendorIdColumnName;	}
	public String getVendorNameColumnName() { return vendorNameColumnName; }
	public String getMerchandiseSkuColumnName() { return merchandiseSkuColumnName; }
	public String getNonMerchandiseSkuColumnName() { return nonMerchandiseSkuColumnName; }
	public String getMasterItemIdColumnName() { return masterItemIdColumnName; }
	public String getProductDescriptionColumnName() { return productDescriptionColumnName; }
	public String getReservedKeysColumnName() { return reservedKeysColumnName; }
	public String getActivatedKeysColumnName() { return activatedKeysColumnName; }
	public String getRemainingKeysColumnName() { return remainingKeysColumnName; }
	public String getInactiveKeysColumnName() { return inactiveKeysColumnName; }
	public String getMismatchedKeysColumnName() { return mismatchedKeysColumnName; }
	public String getBurnRateColumnName() { return burnRateColumnName; }
	public String getErrorsPer1KOrdersColumnName() { return errorsPer1KOrdersColumnName; }
	public String getMaxPerDayColumnName() { return maxPerDayColumnName; }
	public String getMaxPerMonthColumnName() { return maxPerMonthColumnName; }
	public String getKeysUsedLastXDaysColumnName() { return keysUsedLastXDaysColumnName.replace("?", daysToDepletion + ""); }
	public String getEstDaysToDepletionColumnName() { return estDaysToDepletionColumnName; }
	public String getOrderIdColumnName() { return orderIdColumnName; }
	public String getKeyCodeReservedColumnName() { return keyCodeReservedColumnName; }
	public String getKeyCodeActualColumnName() { return keyCodeActualColumnName; }
	public String getDateColumnName() { return dateColumnName; }
	public String getExpiredKeysColumnName() { return expiredKeysColumnName; }
	
	
	public KeycodeBankReportDatatable() throws DataSourceLookupException, DataAccessException {
		super(productDescriptionColumnName);
	}
	
	/*
	 * Report data array
	 */
	private KcbReportBean[] previewDepletionReport;
	public KcbReportBean[] getPreviewDepletionReport() throws DataSourceLookupException, DataAccessException {
		if (previewDepletionReport == null || previewDepletionReport.length < 1) {
			previewDepletionReport = KcbReportDBWrapper.getDepletionReportData(DAYS_DEFAULT);
		}
		return previewDepletionReport;
	}
	
	private KcbReportBean[] rowItemArray = new KcbReportBean[0];
	private DataModel<KcbReportBean> rows;
		
	public KcbReportBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<KcbReportBean> getRows() throws DataSourceLookupException, DataAccessException { 
		if (rows == null) {
			rowItemArray = new KcbReportBean[0];
			rows = new ArrayDataModel<KcbReportBean>(rowItemArray);
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows; 
	}
	
	public void setRows(DataModel<KcbReportBean> rows) { this.rows = rows; }
	public void setRowItemArray(KcbReportBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

	public String deleteButtonClick() {
		throw new UnsupportedOperationException("Catalog setup does not support delete. This method should never be called!");
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
		Comparator<KcbReportBean> comparator = new Comparator<KcbReportBean>() {
            @Override
			public int compare(KcbReportBean o1, KcbReportBean o2) {
            	KcbReportBean c1 = o1;
            	KcbReportBean c2 = o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(vendorIdColumnName)) {
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
            
                } else if (sortColumnName.equals(productDescriptionColumnName)) {
                    return ascending ? ignoreNull(c1.getProductDescription()).compareTo(ignoreNull(c2.getProductDescription())):
                        ignoreNull(c2.getProductDescription()).compareTo(ignoreNull(c1.getProductDescription()));
            
                } else if (sortColumnName.equals(reservedKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getReservedKeys()).compareTo(ignoreNull(c2.getReservedKeys())):
                        ignoreNull(c2.getReservedKeys()).compareTo(ignoreNull(c1.getReservedKeys()));
            
                } else if (sortColumnName.equals(activatedKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getActivatedKeys()).compareTo(ignoreNull(c2.getActivatedKeys())):
                        ignoreNull(c2.getActivatedKeys()).compareTo(ignoreNull(c1.getActivatedKeys()));
            
                } else if (sortColumnName.equals(remainingKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getRemainingKeys()).compareTo(ignoreNull(c2.getRemainingKeys())):
                        ignoreNull(c2.getRemainingKeys()).compareTo(ignoreNull(c1.getRemainingKeys()));
            
                } else if (sortColumnName.equals(inactiveKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getInactiveKeys()).compareTo(ignoreNull(c2.getInactiveKeys())):
                        ignoreNull(c2.getInactiveKeys()).compareTo(ignoreNull(c1.getInactiveKeys()));
            
                } else if (sortColumnName.equals(mismatchedKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getMismatchedKeys()).compareTo(ignoreNull(c2.getMismatchedKeys())):
                        ignoreNull(c2.getMismatchedKeys()).compareTo(ignoreNull(c1.getMismatchedKeys()));
            
                } else if (sortColumnName.equals(burnRateColumnName)) {
                    return ascending ? ignoreNull(c1.getBurnRate()).compareTo(ignoreNull(c2.getBurnRate())):
                        ignoreNull(c2.getBurnRate()).compareTo(ignoreNull(c1.getBurnRate()));
            
                } else if (sortColumnName.equals(errorsPer1KOrdersColumnName)) {
                    return ascending ? ignoreNull(c1.getErrorsPer1KOrders()).compareTo(ignoreNull(c2.getErrorsPer1KOrders())):
                        ignoreNull(c2.getErrorsPer1KOrders()).compareTo(ignoreNull(c1.getErrorsPer1KOrders()));
            
                } else if (sortColumnName.equals(maxPerDayColumnName)) {
                    return ascending ? ignoreNull(c1.getMaxPerDay()).compareTo(ignoreNull(c2.getMaxPerDay())):
                        ignoreNull(c2.getMaxPerDay()).compareTo(ignoreNull(c1.getMaxPerDay()));
            
                } else if (sortColumnName.equals(maxPerMonthColumnName)) {
                    return ascending ? ignoreNull(c1.getMaxPerMonth()).compareTo(ignoreNull(c2.getMaxPerMonth())):
                        ignoreNull(c2.getMaxPerMonth()).compareTo(ignoreNull(c1.getMaxPerMonth()));
            
                } else if (sortColumnName.equals(keysUsedLastXDaysColumnName)) {
                    return ascending ? ignoreNull(c1.getKeysUsedLastXDays()).compareTo(ignoreNull(c2.getKeysUsedLastXDays())):
                        ignoreNull(c2.getKeysUsedLastXDays()).compareTo(ignoreNull(c1.getKeysUsedLastXDays()));
            
                } else if (sortColumnName.equals(estDaysToDepletionColumnName)) {
                    return ascending ? ignoreNull(c1.getEstDaysToDepletion()).compareTo(ignoreNull(c2.getEstDaysToDepletion())):
                        ignoreNull(c2.getEstDaysToDepletion()).compareTo(ignoreNull(c1.getEstDaysToDepletion()));
            
                } else if (sortColumnName.equals(orderIdColumnName)) {
                    return ascending ? ignoreNull(c1.getOrderId()).compareTo(ignoreNull(c2.getOrderId())) :
                        ignoreNull(c2.getOrderId()).compareTo(ignoreNull(c1.getOrderId()));
            
                } else if (sortColumnName.equals(keyCodeReservedColumnName)) {
                    return ascending ? ignoreNull(c1.getKeyCodeReserved()).compareTo(ignoreNull(c2.getKeyCodeReserved())) :
                        ignoreNull(c2.getKeyCodeReserved()).compareTo(ignoreNull(c1.getKeyCodeReserved()));
            
                } else if (sortColumnName.equals(keyCodeActualColumnName)) {
                    return ascending ? ignoreNull(c1.getKeyCodeActual()).compareTo(ignoreNull(c2.getKeyCodeActual())) :
                        ignoreNull(c2.getKeyCodeActual()).compareTo(ignoreNull(c1.getKeyCodeActual()));
            
                } else if (sortColumnName.equals(dateColumnName)) {
                    return ascending ? ignoreNull(c1.getDate()).compareTo(ignoreNull(c2.getDate())) :
                        ignoreNull(c2.getDate()).compareTo(ignoreNull(c1.getDate()));
            
                } else if (sortColumnName.equals(expiredKeysColumnName)) {
                    return ascending ? ignoreNull(c1.getKeysExpired()).compareTo(ignoreNull(c2.getKeysExpired())) :
                        ignoreNull(c2.getKeysExpired()).compareTo(ignoreNull(c1.getKeysExpired()));
            
                } else return 0;
            }
        };
        Arrays.sort(rowItemArray, comparator);
	}
	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankReportTabbedPaneHandler}")
	private KeycodeBankReportTabbedPaneHandler keycodeBankReportTabbedPaneHandler;
	public KeycodeBankReportTabbedPaneHandler getKeycodeBankReportTabbedPaneHandler() { return keycodeBankReportTabbedPaneHandler; }
	public void setKeycodeBankReportTabbedPaneHandler(KeycodeBankReportTabbedPaneHandler keycodeBankReportTabbedPaneHandler) { this.keycodeBankReportTabbedPaneHandler = keycodeBankReportTabbedPaneHandler; }
	
	@ManagedProperty (value="#{keycodeBankReportHandler}")
	private KeycodeBankReportHandler keycodeBankReportHandler;
	public KeycodeBankReportHandler getKeycodeBankReportHandler() { return keycodeBankReportHandler; }
	public void setKeycodeBankReportHandler(KeycodeBankReportHandler keycodeBankReportHandler) { this.keycodeBankReportHandler = keycodeBankReportHandler; }
	
	/*
	 * Report generators
	 */
	public String generateDailyReportButtonClick() throws DataSourceLookupException, DataAccessException {
		this.getKeycodeBankReportTabbedPaneHandler().displayDailyReportTab();
		this.setReportMainHeader(this.getDailyReportTitle());
		this.resetDaysList(year, month);
		rowItemArray = KcbReportDBWrapper.getDateReportData(year, month, dayOfMonth);
		rows = new ArrayDataModel<KcbReportBean>(rowItemArray);
		return NavigationStrings.CURRENT_VIEW;
	}
	public String generateMonthlyReportButtonClick() throws DataSourceLookupException, DataAccessException {
		this.getKeycodeBankReportTabbedPaneHandler().displayMonthlyReportTab();
		this.setReportMainHeader(this.getMonthlyReportTitle());
		rowItemArray = KcbReportDBWrapper.getDateReportData(year, month);
		rows = new ArrayDataModel<KcbReportBean>(rowItemArray);
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String generateDepletionReportButtonClick() throws DataSourceLookupException, DataAccessException {
		this.getKeycodeBankReportTabbedPaneHandler().displayDepletionReportTab();
		this.setReportMainHeader(this.getDepletionReportTitle());
		if (this.showBelowThresholdOnly) {
			rowItemArray = KcbReportDBWrapper.getDepletionThresholdReportData(daysToDepletion);
		} else {
			rowItemArray = KcbReportDBWrapper.getDepletionReportData(daysToDepletion);
		}
		rows = new ArrayDataModel<KcbReportBean>(rowItemArray);
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String backButtonClick() {
		this.getKeycodeBankReportTabbedPaneHandler().displayDashboardTab();
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String getDepletionParams() {
		return "arg0=" + this.showBelowThresholdOnly + "&arg1=" + this.getDaysToDepletion().toString();
	}
}
