package com.accenture.bby.sdp.web.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.accenture.bby.sdp.db.UtilityDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;

/**
 * This class is used to retrieve and cache static data from the database such as dropdown lists
 * Ideally an instance of this class should be stored as a session variable.
 * 
 * @author a719057
 *
 */
@ManagedBean (name="dropdownListManager")
@SessionScoped
public class DropdownListManager implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9054256680357610207L;



	private static final Logger logger = Logger.getLogger(DropdownListManager.class.getName());
	
	public DropdownListManager() {
		
	}
	
	public static void refreshStaticDropdownLists() {
		logger.log(Level.DEBUG, "BEGIN refreshing all static dropdown lists.");
		refreshCatalogCategoryMap();
		refreshVendorCategoryMap();
		refreshBooleanFlagMap();
		refreshAddressLabelMap();
		refreshPhoneLabelMap();
		refreshCreditCardTypeMap();
		refreshCancelReasonCodeMap();
		refreshOpenCCancelReasonCodeMap();
		refreshAdjustmentReasonCodeMap();
		refreshInProductMessageCodeMap();
		refreshVendorMap();
		refreshAuditTrailActionTypeMap();
		logger.log(Level.DEBUG, "FINISH refreshing all static dropdown lists.");
	}

	/*
	 * Note: these static maps can be refreshed by calling the CacheRefreshServlet
	 */
	private static List<SelectItem> VENDOR_ID_MAP;
	private static List<SelectItem> CATALOG_CATEGORY_LIST;
	private static List<SelectItem> VENDOR_CATEGORY_LIST;
	private static List<SelectItem> BOOLEAN_FLAG_LIST;
	private static List<SelectItem> ADDRESS_LABEL_LIST;
	private static List<SelectItem> PHONE_LABEL_LIST;
	private static List<SelectItem> CREDITCARD_TYPE_LIST;
	private static List<SelectItem> CANCEL_REASON_CODE_LIST;
	private static List<SelectItem> OCIS_CANCEL_REASON_CODE_LIST;
	private static List<SelectItem> ADJUSTMENT_REASON_CODE_LIST;
	private static List<SelectItem> IN_PRODUCT_MESSAGE_CODE_LIST;
	private static List<SelectItem> AUDIT_TRAIL_ACTION_TYPE_LIST;
	
	public List<SelectItem> getSortedVendorSelectItems() { return VENDOR_ID_MAP; }
	public List<SelectItem> getSortedCatalogCategorySelectItems() { return CATALOG_CATEGORY_LIST; }
	public List<SelectItem> getSortedVendorCategorySelectItems() { return VENDOR_CATEGORY_LIST; }
	public List<SelectItem> getSortedBooleanFlagSelectItems() { return BOOLEAN_FLAG_LIST; }
	public List<SelectItem> getSortedAddressLabelSelectItems() { return ADDRESS_LABEL_LIST; }
	public List<SelectItem> getSortedPhoneLabelSelectItems() { return PHONE_LABEL_LIST; }
	public List<SelectItem> getSortedCreditCardTypeSelectItems() { return CREDITCARD_TYPE_LIST; }
	public List<SelectItem> getSortedCancelReasonCodeSelectItems() { return CANCEL_REASON_CODE_LIST; }
	public List<SelectItem> getSortedOpenCCancelReasonCodeSelectItems() { return OCIS_CANCEL_REASON_CODE_LIST; }
	public List<SelectItem> getSortedAdjustmentReasonCodeSelectItems() { return ADJUSTMENT_REASON_CODE_LIST; }
	public List<SelectItem> getSortedInProductMessageCodeSelectItems() { return IN_PRODUCT_MESSAGE_CODE_LIST; }
	public List<SelectItem> getSortedAuditTrailActionSelectItems() { return AUDIT_TRAIL_ACTION_TYPE_LIST; }
	
	public static void refreshVendorMap() {
		try {
			VENDOR_ID_MAP = UtilityDBWrapper.getVendorIdList();
		} catch (DataSourceLookupException e) {
			VENDOR_ID_MAP = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor IDs", e);
		} catch (DataAccessException e) {
			VENDOR_ID_MAP = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor IDs", e);
		}
	}
	public static void refreshCatalogCategoryMap() {
		try {
			CATALOG_CATEGORY_LIST = UtilityDBWrapper.getCatalogCategoryList();
		} catch (DataSourceLookupException e) {
			CATALOG_CATEGORY_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Catalog Categories", e);
		} catch (DataAccessException e) {
			CATALOG_CATEGORY_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Catalog Categories", e);
		}
	}
	
	public static void refreshVendorCategoryMap() {
		try {
			VENDOR_CATEGORY_LIST = UtilityDBWrapper.getVendorCategoryList();
		} catch (DataSourceLookupException e) {
			VENDOR_CATEGORY_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor Categories", e);
		} catch (DataAccessException e) {
			VENDOR_CATEGORY_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of Vendor Categories", e);
		}
	}
	
	public static void refreshBooleanFlagMap() {
		try {
			BOOLEAN_FLAG_LIST = UtilityDBWrapper.getBooleanFlagList();
		} catch (DataSourceLookupException e) {
			BOOLEAN_FLAG_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of boolean flags", e);
		} catch (DataAccessException e) {
			BOOLEAN_FLAG_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of boolean flags", e);
		}
	}
	
	public static void refreshAddressLabelMap() {
		try {
			ADDRESS_LABEL_LIST = UtilityDBWrapper.getAddressLabelList();
		} catch (DataSourceLookupException e) {
			ADDRESS_LABEL_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of address labels", e);
		} catch (DataAccessException e) {
			ADDRESS_LABEL_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of address labels", e);
		}
	}
	
	public static void refreshPhoneLabelMap() {
		try {
			PHONE_LABEL_LIST = UtilityDBWrapper.getPhoneLabelList();
		} catch (DataSourceLookupException e) {
			PHONE_LABEL_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of phone labels", e);
		} catch (DataAccessException e) {
			PHONE_LABEL_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of phone labels", e);
		}
	}
	
	public static void refreshCreditCardTypeMap() {
		try {
			CREDITCARD_TYPE_LIST = UtilityDBWrapper.getCreditCardTypeList();
		} catch (DataSourceLookupException e) {
			CREDITCARD_TYPE_LIST =new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of credit card types", e);
		} catch (DataAccessException e) {
			CREDITCARD_TYPE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of credit card types", e);
		}
	}
	
	public static void refreshCancelReasonCodeMap() {
		try {
			CANCEL_REASON_CODE_LIST = UtilityDBWrapper.getCancelReasonCodeList();
		} catch (DataSourceLookupException e) {
			CANCEL_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of cancel reason codes", e);
		} catch (DataAccessException e) {
			CANCEL_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of cancel reason codes", e);
		}
	}
	
	public static void refreshOpenCCancelReasonCodeMap() {
		try {
			OCIS_CANCEL_REASON_CODE_LIST = UtilityDBWrapper.getOpenCCancelReasonCodeList();
		} catch (DataSourceLookupException e) {
			OCIS_CANCEL_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of cancel reason codes", e);
		} catch (DataAccessException e) {
			OCIS_CANCEL_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of cancel reason codes", e);
		}
	}
	
	public static void refreshAdjustmentReasonCodeMap() {
		try {
			ADJUSTMENT_REASON_CODE_LIST = UtilityDBWrapper.getAdjustmentReasonCodeList();
		} catch (DataSourceLookupException e) {
			ADJUSTMENT_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of adjustment reason codes", e);
		} catch (DataAccessException e) {
			ADJUSTMENT_REASON_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of adjustment reason codes", e);
		}
	}
	
	public static void refreshInProductMessageCodeMap() {
		try {
			IN_PRODUCT_MESSAGE_CODE_LIST = UtilityDBWrapper.getInProductMessageCodeList();
		} catch (DataSourceLookupException e) {
			IN_PRODUCT_MESSAGE_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of in product message codes", e);
		} catch (DataAccessException e) {
			IN_PRODUCT_MESSAGE_CODE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of in product message codes", e);
		}
	}
	
	public static void refreshAuditTrailActionTypeMap() {
		try {
			AUDIT_TRAIL_ACTION_TYPE_LIST = UtilityDBWrapper.getAuditTrailActionTypeList();
		} catch (DataSourceLookupException e) {
			AUDIT_TRAIL_ACTION_TYPE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of audit trail action types", e);
		} catch (DataAccessException e) {
			AUDIT_TRAIL_ACTION_TYPE_LIST = new ArrayList<SelectItem>();
			logger.log(Level.ERROR, "Failed to retrieve map of audit trail action types", e);
		}
	}
	
}
