package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean (name="keycodeBankReportTabbedPaneHandler")
@ViewScoped
public class KeycodeBankReportTabbedPaneHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int index;
	private static final int DASHBOARD_INDEX = 0;
	private static final int DAILY_REPORT_INDEX = 1;
	private static final int MONTHLY_REPORT_INDEX = 2;
	private static final int YEARLY_REPORT_INDEX = 3;
	private static final int DEPLETION_REPORT_INDEX = 4;
	private static final int ERROR_FREQUENCY_REPORT_INDEX = 5;
	private static final int MISMATCH_REPORT_INDEX = 6;
	
	
	public KeycodeBankReportTabbedPaneHandler() {
		index = DASHBOARD_INDEX;
	}
	
	// action listeners that set the current tab
	public void displayDashboardTab() { index = DASHBOARD_INDEX; }
	public void displayDailyReportTab() { index = DAILY_REPORT_INDEX; }
	public void displayMonthlyReportTab() { index = MONTHLY_REPORT_INDEX; }
	public void displayYearlyReportTab() { index = YEARLY_REPORT_INDEX; }
	public void displayDepletionReportTab() { index = DEPLETION_REPORT_INDEX; }
	public void displayErrorFrequencyReportTab() { index = ERROR_FREQUENCY_REPORT_INDEX; }
	public void displayMismatchReportTab() { index = MISMATCH_REPORT_INDEX; }
	
	// methods for determining the current tab
	public boolean isDashboardTabCurrent() { return index == DASHBOARD_INDEX; }
	public boolean isDailyReportTabCurrent() { return index == DAILY_REPORT_INDEX; }
	public boolean isMonthlyReportTabCurrent() { return index == MONTHLY_REPORT_INDEX; }
	public boolean isYearlyReportTabCurrent() { return index == YEARLY_REPORT_INDEX; }
	public boolean isDepletionReportTabCurrent() { return index == DEPLETION_REPORT_INDEX; }
	public boolean isErrorFrequencyReportTabCurrent() { return index == ERROR_FREQUENCY_REPORT_INDEX; }
	public boolean isMismatchReportTabCurrent() { return index == MISMATCH_REPORT_INDEX; }
	
	public boolean isReportTabCurrent() {
		return isDailyReportTabCurrent() || isMonthlyReportTabCurrent() || isYearlyReportTabCurrent() || isDepletionReportTabCurrent() || isErrorFrequencyReportTabCurrent() || isMismatchReportTabCurrent();
	}
}
