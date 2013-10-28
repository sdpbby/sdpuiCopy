package com.accenture.bby.sdp.utl;

import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.UtilityDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;

public class Constants {
	
	private static final Logger logger = Logger.getLogger(Constants.class.getName());
	
	private Constants() {	
	}

	public final static TimeZone TIMEZONE = TimeZone.getTimeZone("US/Central");
	public final static String CALENDAR_DATE_PATTERN = "MM/dd/yyyy hh:mm a";
	public final static String LONG_DATE_PATTERN = "EEE MMM d, yyyy h:mm a z";
	public final static String XML_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String EXPORT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String STORE_DATE_PATTERN = "MM/dd/yyyy";
	public final static String XML_DATE_PATTERN_SHORT = "yyyy-MM-dd";
	
	public final static String DEFAULT_SOURCE_SYSTEM = "SDP_OPS_UI";
	
	public final static String NEEDS_VENDOR_PROVISIONING = "NeedsVendorProvisioning";
	public final static String CONTRACT_TRIGGER = "ContractTrigger";
	public final static String PREORDER_HOLD = "PreOrderHold";
	public final static String NEED_CA = "NeedCA";
	public final static String NEEDS_COMMSAT_INTEG = "NeedsCommSatInteg";
	public final static String NEEDS_VENDOR_KEY = "NeedsVendorKey";
	public final static String REDIRECT_TO_OLD_ARCH = "ReDirectToOldArch";
	
	private static ConcurrentHashMap<String, String> VENDOR_ID_MAP;
	private static ConcurrentHashMap<String, String> STATUS_CODE_MAP;
	
	public static void refreshVendorMap() {
		try {
			VENDOR_ID_MAP = UtilityDBWrapper.getVendorIdMap();
		} catch (DataSourceLookupException e) {
			VENDOR_ID_MAP = new ConcurrentHashMap<String, String>(0);
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor IDs", e);
		} catch (DataAccessException e) {
			VENDOR_ID_MAP = new ConcurrentHashMap<String, String>(0);
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor IDs", e);
		}
	}
	
	public static String getVendorName(String arg) {
		if (arg == null) {
			return null;
		}
		if (VENDOR_ID_MAP == null || VENDOR_ID_MAP.size() < 1 || VENDOR_ID_MAP.get(arg) == null) {
			refreshVendorMap();
		}
		final String val = VENDOR_ID_MAP.get(arg); 
		if (val == null) {
			logger.log(Level.WARN, "The Vendor ID " + arg + " does not have a matching vendor name mapping in the SDP_VENDOR_CONFIG table. If this code has been added, try restarting the application.");
		}
		return val; 
	}
	
	public static void refreshStatusMap() {
		try {
			STATUS_CODE_MAP = UtilityDBWrapper.getStatusCodeMap();
		} catch (DataSourceLookupException e) {
			STATUS_CODE_MAP = new ConcurrentHashMap<String, String>(0);
			logger.log(Level.ERROR, "Failed to retrieve map of Status Codes", e);
		} catch (DataAccessException e) {
			STATUS_CODE_MAP = new ConcurrentHashMap<String, String>(0);
			logger.log(Level.ERROR, "Failed to retrieve map of Status Codes", e);
		}
	}
	
	public static String getStatusName(String arg) { 
		if (arg == null) {
			return null;
		}
		if (STATUS_CODE_MAP == null || STATUS_CODE_MAP.size() < 1 || STATUS_CODE_MAP.get(arg) == null) {
			refreshStatusMap();
		}
		final String val = STATUS_CODE_MAP.get(arg); 
		if (val == null) {
			logger.log(Level.WARN, "The status code " + arg + " does not have a matching status name mapping in the SDP_STATUS_MASTER table. If this code has been added, try restarting the application.");
		}
		return val; 
	}
	
}
