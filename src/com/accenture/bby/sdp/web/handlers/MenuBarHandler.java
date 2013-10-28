package com.accenture.bby.sdp.web.handlers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.accenture.bby.sdp.utl.NavigationStrings;

@ManagedBean (name="menuBarHandler")
@ViewScoped
public class MenuBarHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8566151928412989202L;

	public String gotoQuerySdpOrdersButtonClick() {
		return NavigationStrings.SDP_ORDER_SEARCH_PAGE;
	}
	
	public String gotoRecentSdpOrdersButtonClick() {
		return NavigationStrings.SDP_ORDER_SEARCH_RESULTS_PAGE;
	}
	
	public String gotoExceptionsButtonClick() {
		return NavigationStrings.EXCEPTION_DEFAULT_PAGE;
	}
	
	public String gotoAuditTrailButtonClick() {
		return NavigationStrings.AUDIT_TRAIL_SEARCH_DEFAULT_PAGE;
	}
	
	public String gotoEccConsoleButtonClick() {
		return NavigationStrings.ECCUI_DEFAULT_PAGE;
	}
	
	public String gotoFlowSdpTransactionButtonClick() {
		return NavigationStrings.SDP_REQUEST_DEFAULT_PAGE;
	}
	
	public String gotoFlowDirectToVendorButtonClick() {
		return NavigationStrings.VENDOR_REQUEST_DEFAULT_PAGE;
	}
	
	public String gotoKcbDashboardButtonClick() {
		return NavigationStrings.KEYCODEBANK_DEFAULT_PAGE;
	}
	
	public String gotoLoadKeycodesButtonClick() {
		return NavigationStrings.KEYCODEBANK_LOAD_DEFAULT_PAGE;
	}
	
	public String gotoManageLoadedKeycodesButtonClick() {
		return NavigationStrings.KEYCODEBANK_MANAGELOAD_DEFAULT_PAGE;
	}
	
	public String gotoKcbCatalogButtonClick() {
		return NavigationStrings.KEYCODEBANK_CATALOG_DEFAULT_PAGE;
	}
	
	public String gotoSdpCatalogButtonClick() {
		return NavigationStrings.CATALOG_DEFAULT_PAGE;
	}
	
	public String gotoVendorSetupButtonClick() {
		return NavigationStrings.VENDOR_DEFAULT_PAGE;
	}
	
	public String gotoPreorderManagementButtonClick() {
		return NavigationStrings.PREORDER_DEFAULT_PAGE;
	}
	
	public String gotoSdpSupportHomeButtonClick() {
		return NavigationStrings.CURRENT_VIEW;
	}
	
	public String gotoSdpQueueMonitoringButtonClick() {
		return NavigationStrings.MONITORING_DEFAULT_PAGE;
	}
	
	public String gotoSdpConfigButtonClick() {
		return NavigationStrings.SDP_CONFIG_DEFAULT_PAGE;
	}
	
	public String gotoLoggingButtonClick() {
		return NavigationStrings.LOGGING_DEFAULT_PAGE;
	}
}
