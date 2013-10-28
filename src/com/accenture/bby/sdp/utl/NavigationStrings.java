package com.accenture.bby.sdp.utl;

public class NavigationStrings {
	/**
	 * used to redisplay the current view
	 */
	public static final String CURRENT_VIEW = null;
	public static final String LOGIN_PAGE_DEFAULT = "/pages/login/login?faces-redirect=true";
	public static final String HOME_PAGE_DEFAULT = "/SDPOpsUI/OpsUI/home.jsf";
	public static final String ADMIN_DEFAULT_PAGE = "/pages/admin/admin?faces-redirect=true";
	public static final String GENERAL_EXCEPTION_PAGE = "/pages/error/generalException?faces-redirect=true";
	public static final String PREORDER_DEFAULT_PAGE = "/pages/preorder/preorder?faces-redirect=true";
	public static final String VENDOR_DEFAULT_PAGE = "/pages/vendor/vendor?faces-redirect=true";
	public static final String VENDOR_CODE_DEFAULT_PAGE = "/pages/vendor/vendor_code?faces-redirect=true";
	public static final String CATALOG_DEFAULT_PAGE = "/pages/catalog/catalog?faces-redirect=true";
	public static final String ECCUI_DEFAULT_PAGE = "/pages/eccui/eccui?faces-redirect=true"; 
	public static final String KEYCODEBANK_DEFAULT_PAGE = "/pages/keycodeBank/keycodeBank?faces-redirect=true";
	public static final String KEYCODEBANK_CATALOG_DEFAULT_PAGE = "/pages/keycodeBank/keycodeBank_catalog?faces-redirect=true";
	public static final String KEYCODEBANK_LOAD_DEFAULT_PAGE = "/pages/keycodeBank/keycodeBank_load?faces-redirect=true";
	public static final String KEYCODEBANK_MANAGELOAD_DEFAULT_PAGE = "/pages/keycodeBank/keycodeBank_manageLoad?faces-redirect=true";
	public static final String SDP_ORDER_SEARCH_RESULTS_PAGE = "/pages/sdpOrderSearchResults/sdpOrderSearchResults?faces-redirect=true";
	public static final String SDP_ORDER_SEARCH_PAGE = "/pages/sdpOrderSearch/sdpOrderSearch1?faces-redirect=true";
	public static final String SDP_ORDER_DETAIL_PAGE = "/pages/sdpOrderDetail/sdpOrderDetail?faces-redirect=true";
	public static final String VENDOR_PROVISIONING_STATUS_PAGE = "/pages/vendorProvisioningStatus/vendorProvisioningStatus?faces-redirect=true";
	public static final String SDP_REQUEST_DEFAULT_PAGE = "/pages/sdpRequest/sdpRequest?faces-redirect=true";
	public static final String VENDOR_REQUEST_DEFAULT_PAGE = "/pages/vendorRequest/vendorRequest?faces-redirect=true";
	public static final String REQUEST_RESPONSE_LOG_DETAIL_DEFAULT_PAGE = "/pages/transLog/transLog?faces-redirect=true";
	public static final String EXCEPTION_DEFAULT_PAGE = "/pages/exception/exception?faces-redirect=true";
	public static final String AUDIT_TRAIL_SEARCH_DEFAULT_PAGE = "/pages/audit/audit_search?faces-redirect=true";
	public static final String AUDIT_TRAIL_DETAIL_DEFAULT_PAGE = "/pages/audit/audit_detail?faces-redirect=true";
	public static final String MONITORING_DEFAULT_PAGE = "/pages/monitoring/monitoring?faces-redirect=true";
	public static final String SDP_CONFIG_DEFAULT_PAGE = "/pages/support/sdpconfig?faces-redirect=true";
	public static final String LOGGING_DEFAULT_PAGE = "/pages/support/log4j?faces-redirect=true";
	public static final String EXCEPTION_DETAIL_DEFAULT_PAGE = "/pages/exceptionresult/exceptionresult?faces-redirect=true";
	
	/**
	 * <p>Used to generate a parameterized url string. For example the return result of 
	 * <code>NavigationStrings.getParameterizedUrl("/pages/sdpOrderDetail/sdpOrderDetail", "sdpOrderId", "12345");</code>
	 * will be <code>"/pages/sdpOrderDetail/sdpOrderDetail?sdpOrderId=12345"</code><br />
	 * or<br />
	 * <code>NavigationStrings.getParameterizedUrl("/pages/sdpOrderDetail/sdpOrderDetail?faces-redirect=true", "sdpOrderId", "12345");</code>
	 * will be <code>"/pages/sdpOrderDetail/sdpOrderDetail?faces-redirect=true&sdpOrderId=12345"</code></p>
	 * 
	 * @param urlString navigation url string
	 * @param paramKey parameter key
	 * @param paramValue parameter value
	 * @return
	 */
	public static String getParameterizedUrl(final String urlString, final String paramKey, final String paramValue) {
		return urlString + (urlString.contains("?") ? "&" : "?") + paramKey + "=" + paramValue;
	}
	/**
	 * <p>Used to generate a parameterized url string. For example the return result of 
	 * <code>NavigationStrings.getParameterizedUrl("/pages/sdpOrderDetail/sdpOrderDetail", "sdpOrderId", 12345);</code>
	 * will be <code>"/pages/sdpOrderDetail/sdpOrderDetail?sdpOrderId=12345"</code><br />
	 * or<br />
	 * <code>NavigationStrings.getParameterizedUrl("/pages/sdpOrderDetail/sdpOrderDetail?faces-redirect=true", "sdpOrderId", 12345);</code>
	 * will be <code>"/pages/sdpOrderDetail/sdpOrderDetail?faces-redirect=true&sdpOrderId=12345"</code></p>
	 * 
	 * @param urlString navigation url string
	 * @param paramKey parameter key
	 * @param paramValue parameter value
	 * @return
	 */
	public static String getParameterizedUrl(final String urlString, final String paramKey, final int paramValue) {
		return urlString + (urlString.contains("?") ? "&" : "?") + paramKey + "=" + paramValue;
	}
}