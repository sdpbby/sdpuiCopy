package com.accenture.bby.sdp.utl;

import java.util.Arrays;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.common.configurator.ConfigManager;

public class SdpConfigProperties {

	private final static Logger logger = Logger.getLogger(SdpConfigProperties.class.getName());

	private final static String[] VALID_WORKING_ENVIRONMENTS = { "devtest_a", "devtest_b", "prodtest_a", "prodtest_b", "pldr", "prod" };

	private final static String ENVIRONMENT_PROP_NAME = "sdp.OpsUI.deploymentEnvironment";
	public static final String CNF_CSM_JNDI_VALUE_PROP_NAME = "sdp.jndi.jdbc.datasource.csm";
	private static final String SDP_CONFIG_CNF_JNDI_VALUE_PROP_NAME = "sdp.jndi.jdbc.datasource.kcb";
	private static final String SDP_CONFIG_JNDI_VALUE_PROP_NAME = "sdp.jndi.jdbc.datasource.sdp";
	private static final String KCB_CONFIG_JNDI_VALUE_PROP_NAME = "sdp.jndi.jdbc.datasource.kcb";
	
	private final static String STATUS_RELEASED_ID_PROP_NAME = "sdp.OpsUI.preorder.status.released";
	private final static String STATUS_ORDER_COMPLETED_ID_PROP_NAME = "sdp.OpsUI.order.status.completed";
	private final static String STATUS_ORDER_CANCELLED_ID_PROP_NAME = "sdp.OpsUI.order.status.cancelled";
	
	private final static String CATEGORY_S2_PROP_NAME = "sdp.OpsUI.category.s2";
	private final static String CATEGORY_DIGITAL_PROP_NAME = "sdp.OpsUI.category.digital";
	private final static String CATEGORY_TECH_SUPPORT_PROP_NAME = "sdp.OpsUI.category.tech_support";
	private final static String CATEGORY_PSA_PROP_NAME = "sdp.OpsUI.category.psa";
	private final static String CATEGORY_POSA_PROP_NAME = "sdp.OpsUI.category.posa";
	private final static String CATEGORY_MSKEY_PROP_NAME = "sdp.OpsUI.category.mskey";
	
	private final static String PROMO_TYPE_PROP_NAME = "sdp.OpsUI.promo.promo_type";
	
	private final static String SHOW_STACKTRACE_ALL_USERS_PROP_NAME = "sdp.OpsUI.showStackTraceAllUsers";
	private final static String FULFILLMENT_SERVICES_SOURCE_SYSTEMS_PROP_NAME = "sdp.OpsUI.reprocess.fulfillment.sourceSystems";
	private final static String MAX_ORDER_QUERY_RESULTS_PROP_NAME = "sdp.OpsUI.maxQueryResults";
	private final static String INVALID_CHARACTER_REGEX_PROP_NAME = "sdp.OpsUI.invalid_character_regex";
	private final static String ENABLE_XML_SCHEMA_VALIDATION_PROP_NAME = "sdp.OpsUI.logging.enable_request_response_schema_validation";
	private final static String DIRECTTOVENDOR_CATEGORY_PROP_NAME = "sdp.OpsUI.reprocess.direct2vendor.category";
	
	private final static String REQUEST_TYPE_SEND_EMAIL_PROP_NAME = "sdp.OpsUI.request_type.send_email";
	private final static String REQUEST_TYPE_ACTIVATE_PROP_NAME = "sdp.OpsUI.request_type.activate";
	private final static String REQUEST_TYPE_CANCEL_PROP_NAME = "sdp.OpsUI.request_type.cancel";
	private final static String REQUEST_TYPE_RENEW_PROP_NAME = "sdp.OpsUI.request_type.renew";
	private final static String REQUEST_TYPE_REACTIVATE_PROP_NAME = "sdp.OpsUI.request_type.reactivate";
	private final static String REQUEST_TYPE_REAL_TIME_ACTIVATE_PROP_NAME = "sdp.OpsUI.request_type.realtime_activate";
	private final static String REQUEST_TYPE_ADJUSTMENT_PROP_NAME = "sdp.OpsUI.request_type.adjustment";
	private final static String REQUEST_TYPE_MODIFY_STATUS_PROP_NAME = "sdp.OpsUI.request_type.modify";
	
	/*
	 * cache
	 */
	private static String SDP_CONFIG_JNDI_VALUE;
	private static String SDP_CONFIG_CNF_JNDI_VALUE;
	private static String KCB_CONFIG_JNDI_VALUE;
	private static String CNF_CSM_JNDI_VALUE;
	private static String STATUS_RELEASED_ID;
	private static String STATUS_ORDER_COMPLETED_ID;
	private static String STATUS_ORDER_CANCELLED_ID;
	private static String SHOW_STACKTRACE_ALL_USERS;
	private static String FULFILLMENT_SERVICES_SOURCE_SYSTEMS;
	private static String MAX_ORDER_QUERY_RESULTS;
	private static String INVALID_CHARACTER_REGEX;
	private static String ENABLE_XML_SCHEMA_VALIDATION;
	private static String CATEGORY_S2;
	private static String CATEGORY_DIGITAL;
	private static String CATEGORY_TECH_SUPPORT;
	private static String CATEGORY_PSA;
	private static String CATEGORY_POSA;
	private static String CATEGORY_MSKEY;
	private static String REQUEST_TYPE_SEND_EMAIL;
	private static String REQUEST_TYPE_ACTIVATE;
	private static String REQUEST_TYPE_CANCEL;
	private static String REQUEST_TYPE_RENEW;
	private static String REQUEST_TYPE_REACTIVATE;
	private static String REQUEST_TYPE_REAL_TIME_ACTIVATE;
	private static String REQUEST_TYPE_ADJUSTMENT;
	private static String REQUEST_TYPE_MODIFY_STATUS;
	private static String PROMO_TYPE;
	private static String DIRECTTOVENDOR_PRODUCT_CATEGORY;

	public static String WORKING_ENVIRONMENT_NAME;

	public static void initialize() {
		WORKING_ENVIRONMENT_NAME = System.getProperty(ENVIRONMENT_PROP_NAME);
		if (WORKING_ENVIRONMENT_NAME == null) {
			logger.log(Level.FATAL, "No system property 'sdp.OpsUI.deploymentEnvironment' defined. This propery must be set via the" + "'-D" + ENVIRONMENT_PROP_NAME + "=xxx' command at server start. Valid values are: " + Arrays.asList(VALID_WORKING_ENVIRONMENTS));
			throw new IllegalStateException("No system property 'sdp.OpsUI.deploymentEnvironment' defined. This propery must be set via the" + "'-D" + ENVIRONMENT_PROP_NAME + "=xxx' command at server start. Valid values are: " + Arrays.asList(VALID_WORKING_ENVIRONMENTS));
		} else if (!Arrays.asList(VALID_WORKING_ENVIRONMENTS).contains(WORKING_ENVIRONMENT_NAME)) {
			logger.log(Level.FATAL, "Invalid system property 'sdp.OpsUI.deploymentEnvironment' defined: " + WORKING_ENVIRONMENT_NAME + ". Valid values are: " + Arrays.asList(VALID_WORKING_ENVIRONMENTS));
			throw new IllegalStateException("Invalid system property 'sdp.OpsUI.deploymentEnvironment' defined: " + WORKING_ENVIRONMENT_NAME + ". Valid values are: " + Arrays.asList(VALID_WORKING_ENVIRONMENTS));
		}
		logger.log(Level.DEBUG, "Working environment initialized as " + WORKING_ENVIRONMENT_NAME);
	}
	
	public static void loadSdpConfigProperties() {
		forcePropertiesRefresh();
		STATUS_RELEASED_ID = getProperty(STATUS_RELEASED_ID_PROP_NAME);
		STATUS_ORDER_COMPLETED_ID = getProperty(STATUS_ORDER_COMPLETED_ID_PROP_NAME);
		STATUS_ORDER_CANCELLED_ID = getProperty(STATUS_ORDER_CANCELLED_ID_PROP_NAME);
		SDP_CONFIG_CNF_JNDI_VALUE = getProperty(SDP_CONFIG_CNF_JNDI_VALUE_PROP_NAME);
                CNF_CSM_JNDI_VALUE = getProperty(CNF_CSM_JNDI_VALUE_PROP_NAME);
		SDP_CONFIG_JNDI_VALUE = getProperty(SDP_CONFIG_JNDI_VALUE_PROP_NAME);
		KCB_CONFIG_JNDI_VALUE = getProperty(KCB_CONFIG_JNDI_VALUE_PROP_NAME);
		SHOW_STACKTRACE_ALL_USERS = getProperty(SHOW_STACKTRACE_ALL_USERS_PROP_NAME);
		FULFILLMENT_SERVICES_SOURCE_SYSTEMS = getProperty(FULFILLMENT_SERVICES_SOURCE_SYSTEMS_PROP_NAME);
		MAX_ORDER_QUERY_RESULTS = getProperty(MAX_ORDER_QUERY_RESULTS_PROP_NAME);
		INVALID_CHARACTER_REGEX = getProperty(INVALID_CHARACTER_REGEX_PROP_NAME);
		ENABLE_XML_SCHEMA_VALIDATION = getProperty(ENABLE_XML_SCHEMA_VALIDATION_PROP_NAME);
		CATEGORY_S2 = getProperty(CATEGORY_S2_PROP_NAME);
		CATEGORY_DIGITAL = getProperty(CATEGORY_DIGITAL_PROP_NAME);
		CATEGORY_TECH_SUPPORT = getProperty(CATEGORY_TECH_SUPPORT_PROP_NAME);
		CATEGORY_PSA = getProperty(CATEGORY_PSA_PROP_NAME);
		CATEGORY_POSA = getProperty(CATEGORY_POSA_PROP_NAME);
		CATEGORY_MSKEY = getProperty(CATEGORY_MSKEY_PROP_NAME);
		REQUEST_TYPE_SEND_EMAIL = getProperty(REQUEST_TYPE_SEND_EMAIL_PROP_NAME);
		REQUEST_TYPE_ACTIVATE = getProperty(REQUEST_TYPE_ACTIVATE_PROP_NAME);
		REQUEST_TYPE_CANCEL = getProperty(REQUEST_TYPE_CANCEL_PROP_NAME);
		REQUEST_TYPE_RENEW = getProperty(REQUEST_TYPE_RENEW_PROP_NAME);
		REQUEST_TYPE_REACTIVATE = getProperty(REQUEST_TYPE_REACTIVATE_PROP_NAME);
		REQUEST_TYPE_REAL_TIME_ACTIVATE = getProperty(REQUEST_TYPE_REAL_TIME_ACTIVATE_PROP_NAME);
		REQUEST_TYPE_ADJUSTMENT = getProperty(REQUEST_TYPE_ADJUSTMENT_PROP_NAME);
		REQUEST_TYPE_MODIFY_STATUS = getProperty(REQUEST_TYPE_MODIFY_STATUS_PROP_NAME);
		PROMO_TYPE = getProperty(PROMO_TYPE_PROP_NAME);
		
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, "Retrieved sdp-config properties: " 
					+ "\n" + STATUS_RELEASED_ID_PROP_NAME + "=" + STATUS_RELEASED_ID
					+ "\n" + STATUS_ORDER_COMPLETED_ID_PROP_NAME + "=" + STATUS_ORDER_COMPLETED_ID
					+ "\n" + STATUS_ORDER_CANCELLED_ID_PROP_NAME + "=" + STATUS_ORDER_CANCELLED_ID
					+ "\n" + SDP_CONFIG_JNDI_VALUE_PROP_NAME + "=" + SDP_CONFIG_JNDI_VALUE
					+ "\n" + SDP_CONFIG_CNF_JNDI_VALUE_PROP_NAME + "=" + SDP_CONFIG_CNF_JNDI_VALUE
				       + "\n" + CNF_CSM_JNDI_VALUE_PROP_NAME + "=" + CNF_CSM_JNDI_VALUE
					+ "\n" + KCB_CONFIG_JNDI_VALUE_PROP_NAME + "=" + KCB_CONFIG_JNDI_VALUE
					+ "\n" + SHOW_STACKTRACE_ALL_USERS_PROP_NAME + "=" + SHOW_STACKTRACE_ALL_USERS
					+ "\n" + FULFILLMENT_SERVICES_SOURCE_SYSTEMS_PROP_NAME + "=" + FULFILLMENT_SERVICES_SOURCE_SYSTEMS
					+ "\n" + MAX_ORDER_QUERY_RESULTS_PROP_NAME + "=" + MAX_ORDER_QUERY_RESULTS
					+ "\n" + INVALID_CHARACTER_REGEX_PROP_NAME + "=" + INVALID_CHARACTER_REGEX
					+ "\n" + ENABLE_XML_SCHEMA_VALIDATION_PROP_NAME + "=" + ENABLE_XML_SCHEMA_VALIDATION
					+ "\n" + CATEGORY_S2_PROP_NAME + "=" + CATEGORY_S2
					+ "\n" + CATEGORY_DIGITAL_PROP_NAME + "=" + CATEGORY_DIGITAL
					+ "\n" + CATEGORY_TECH_SUPPORT_PROP_NAME + "=" + CATEGORY_TECH_SUPPORT
					+ "\n" + CATEGORY_PSA_PROP_NAME + "=" + CATEGORY_PSA
					+ "\n" + CATEGORY_POSA_PROP_NAME + "=" + CATEGORY_POSA
					+ "\n" + CATEGORY_MSKEY_PROP_NAME + "=" + CATEGORY_MSKEY
					+ "\n" + REQUEST_TYPE_SEND_EMAIL_PROP_NAME + "=" + REQUEST_TYPE_SEND_EMAIL
					+ "\n" + REQUEST_TYPE_ACTIVATE_PROP_NAME + "=" + REQUEST_TYPE_ACTIVATE
					+ "\n" + REQUEST_TYPE_CANCEL_PROP_NAME + "=" + REQUEST_TYPE_CANCEL
					+ "\n" + REQUEST_TYPE_RENEW_PROP_NAME + "=" + REQUEST_TYPE_RENEW
					+ "\n" + REQUEST_TYPE_REACTIVATE_PROP_NAME + "=" + REQUEST_TYPE_REACTIVATE
					+ "\n" + REQUEST_TYPE_REAL_TIME_ACTIVATE_PROP_NAME + "=" + REQUEST_TYPE_REAL_TIME_ACTIVATE
					+ "\n" + REQUEST_TYPE_ADJUSTMENT_PROP_NAME + "=" + REQUEST_TYPE_ADJUSTMENT
					+ "\n" + REQUEST_TYPE_MODIFY_STATUS_PROP_NAME + "=" + REQUEST_TYPE_MODIFY_STATUS
					+ "\n" + PROMO_TYPE_PROP_NAME + "=" + PROMO_TYPE
					);
		}
	}
	
	public static void forcePropertiesRefresh() {
		logger.log(Level.INFO, "Calling ConfigManager.forcePropertiesRefresh() to refresh sdp-config properties.");
		ConfigManager.forcePropertiesRefresh();
	}
	
	private static String getProperty(String propName) {
		String prop = ConfigManager.getProperty(propName);
		if (prop == null || prop.length() < 1) {
			logger.log(Level.WARN, "Property missing from sdp-config: " + propName);
		}
		return prop;
	}
	
	public static String getCnfJNDI() {
		if (SDP_CONFIG_CNF_JNDI_VALUE == null) { 
			SDP_CONFIG_CNF_JNDI_VALUE = getProperty(SDP_CONFIG_CNF_JNDI_VALUE_PROP_NAME);
		}
		return SDP_CONFIG_CNF_JNDI_VALUE;
	}

	public static String getCsmJNDI() {
		if (CNF_CSM_JNDI_VALUE == null) { 
			CNF_CSM_JNDI_VALUE = getProperty(CNF_CSM_JNDI_VALUE_PROP_NAME);
		}
		return CNF_CSM_JNDI_VALUE;
	}
	
	public static String getSomJNDI() {
		if (SDP_CONFIG_JNDI_VALUE == null) { 
			SDP_CONFIG_JNDI_VALUE = getProperty(SDP_CONFIG_JNDI_VALUE_PROP_NAME);
		}
		return SDP_CONFIG_JNDI_VALUE;
	}
	
	public static String getKcbJNDI() {
		if (KCB_CONFIG_JNDI_VALUE == null) { 
			KCB_CONFIG_JNDI_VALUE = getProperty(KCB_CONFIG_JNDI_VALUE_PROP_NAME);
		}
		return KCB_CONFIG_JNDI_VALUE;
	}

	public static String getStatusReleased() {
		if (STATUS_RELEASED_ID == null) { 
			STATUS_RELEASED_ID = getProperty(STATUS_RELEASED_ID_PROP_NAME);
		}
		return STATUS_RELEASED_ID;
	}

	public static String getStatusCompleted() {
		if (STATUS_ORDER_COMPLETED_ID == null) { 
			STATUS_ORDER_COMPLETED_ID = getProperty(STATUS_ORDER_COMPLETED_ID_PROP_NAME);
		}
		return STATUS_ORDER_COMPLETED_ID;
	}
	
	public static String getStatusCancelled() {
		if (STATUS_ORDER_CANCELLED_ID == null) { 
			STATUS_ORDER_CANCELLED_ID = getProperty(STATUS_ORDER_CANCELLED_ID_PROP_NAME);
		}
		return STATUS_ORDER_CANCELLED_ID;
	}
	
	public static String getCategoryS2() {
		if (CATEGORY_S2 == null) { 
			CATEGORY_S2 = getProperty(CATEGORY_S2_PROP_NAME);
		}
		return CATEGORY_S2;
	}
	
	public static String getCategoryDigital() {
		if (CATEGORY_DIGITAL == null) { 
			CATEGORY_DIGITAL = getProperty(CATEGORY_DIGITAL_PROP_NAME);
		}
		return CATEGORY_DIGITAL;
	}
	
	public static String getCategoryTechSupport() {
		if (CATEGORY_TECH_SUPPORT == null) { 
			CATEGORY_TECH_SUPPORT = getProperty(CATEGORY_TECH_SUPPORT_PROP_NAME);
		}
		return CATEGORY_TECH_SUPPORT;
	}
	
	public static String getCategoryPsa() {
		if (CATEGORY_PSA == null) { 
			CATEGORY_PSA = getProperty(CATEGORY_PSA_PROP_NAME);
		}
		return CATEGORY_PSA;
	}
	
	public static String getCategoryPosa() {
		if (CATEGORY_POSA == null) { 
			CATEGORY_POSA = getProperty(CATEGORY_POSA_PROP_NAME);
		}
		return CATEGORY_POSA;
	}
	
	public static String getCategoryMskey() {
		if (CATEGORY_MSKEY == null) { 
			CATEGORY_MSKEY = getProperty(CATEGORY_MSKEY_PROP_NAME);
		}
		return CATEGORY_MSKEY;
	}
	
	public static String getRequestTypeSendEmail() {
		if (REQUEST_TYPE_SEND_EMAIL == null) { 
			REQUEST_TYPE_SEND_EMAIL = getProperty(REQUEST_TYPE_SEND_EMAIL_PROP_NAME);
		}
		return REQUEST_TYPE_SEND_EMAIL;
	}
	
	public static String getRequestTypeActivate() {
		if (REQUEST_TYPE_ACTIVATE == null) { 
			REQUEST_TYPE_ACTIVATE = getProperty(REQUEST_TYPE_ACTIVATE_PROP_NAME);
		}
		return REQUEST_TYPE_ACTIVATE;
	}
	
	public static String getRequestTypeCancel() {
		if (REQUEST_TYPE_CANCEL == null) { 
			REQUEST_TYPE_CANCEL = getProperty(REQUEST_TYPE_CANCEL_PROP_NAME);
		}
		return REQUEST_TYPE_CANCEL;
	}
	
	public static String getRequestTypeRenew() {
		if (REQUEST_TYPE_RENEW == null) { 
			REQUEST_TYPE_RENEW = getProperty(REQUEST_TYPE_RENEW_PROP_NAME);
		}
		return REQUEST_TYPE_RENEW;
	}
	
	public static String getRequestTypeReactivate() {
		if (REQUEST_TYPE_REACTIVATE == null) { 
			REQUEST_TYPE_REACTIVATE = getProperty(REQUEST_TYPE_REACTIVATE_PROP_NAME);
		}
		return REQUEST_TYPE_REACTIVATE;
	}
	
	public static String getRequestTypeAdjustment() {
		if (REQUEST_TYPE_ADJUSTMENT == null) { 
			REQUEST_TYPE_ADJUSTMENT = getProperty(REQUEST_TYPE_ADJUSTMENT_PROP_NAME);
		}
		return REQUEST_TYPE_ADJUSTMENT;
	}
	
	public static String getRequestTypeRealTimeActivate() {
		if (REQUEST_TYPE_REAL_TIME_ACTIVATE == null) { 
			REQUEST_TYPE_REAL_TIME_ACTIVATE = getProperty(REQUEST_TYPE_REAL_TIME_ACTIVATE_PROP_NAME);
		}
		return REQUEST_TYPE_REAL_TIME_ACTIVATE;
	}
	
	public static String getRequestTypeModifyStatus() {
		if (REQUEST_TYPE_MODIFY_STATUS == null) { 
			REQUEST_TYPE_MODIFY_STATUS = getProperty(REQUEST_TYPE_MODIFY_STATUS_PROP_NAME);
		}
		return REQUEST_TYPE_MODIFY_STATUS;
	}
	
	public static String[] getPromoTypes() {
		if (PROMO_TYPE == null) { 
			PROMO_TYPE = getProperty(PROMO_TYPE);
		}
		if (PROMO_TYPE == null) {
			return new String[0];
		} else {
			PROMO_TYPE.replaceAll(" ", "");
			if (PROMO_TYPE.length() < 1) {
				return new String[0];
			} else {
				return PROMO_TYPE.split(",");
			}
		}
	}
	
	public static String getInvalidCharacterRegex() {
		if (INVALID_CHARACTER_REGEX == null) { 
			INVALID_CHARACTER_REGEX = getProperty(INVALID_CHARACTER_REGEX_PROP_NAME);
		}
		return INVALID_CHARACTER_REGEX;
	}
	
	public static boolean isEnabledShowStackTraceAllUsers() {
		if (SHOW_STACKTRACE_ALL_USERS == null) { 
			SHOW_STACKTRACE_ALL_USERS = getProperty(SHOW_STACKTRACE_ALL_USERS_PROP_NAME);
		}
		return SHOW_STACKTRACE_ALL_USERS != null && SHOW_STACKTRACE_ALL_USERS.toUpperCase().equals("TRUE");
	}
	
	public static boolean isEnabledSchemaValidation() {
		if (ENABLE_XML_SCHEMA_VALIDATION == null) { 
			ENABLE_XML_SCHEMA_VALIDATION = getProperty(ENABLE_XML_SCHEMA_VALIDATION_PROP_NAME);
		}
		return ENABLE_XML_SCHEMA_VALIDATION != null && ENABLE_XML_SCHEMA_VALIDATION.toUpperCase().equals("TRUE");
	}
		
	public static boolean isSourceSystemReflowableInFulfillmentServices(String sourceSystem) {
		if (sourceSystem == null) {
			return false;
		}
		if (FULFILLMENT_SERVICES_SOURCE_SYSTEMS == null) { 
			FULFILLMENT_SERVICES_SOURCE_SYSTEMS = getProperty(FULFILLMENT_SERVICES_SOURCE_SYSTEMS_PROP_NAME);
		}
		if (FULFILLMENT_SERVICES_SOURCE_SYSTEMS != null) {
			String[] sourceSystems = FULFILLMENT_SERVICES_SOURCE_SYSTEMS.replaceAll(" ","").split(",");
			return Arrays.asList(sourceSystems).contains(sourceSystem);
		} else {
			return false;
		}
	}
	
	public static boolean isCategoryReflowableInVendorRequest(String category) {
		if (category == null) {
			return false;
		}
		if (DIRECTTOVENDOR_PRODUCT_CATEGORY == null) { 
			DIRECTTOVENDOR_PRODUCT_CATEGORY = getProperty(DIRECTTOVENDOR_CATEGORY_PROP_NAME);
		}
		if (DIRECTTOVENDOR_PRODUCT_CATEGORY != null) {
			String[] categories = DIRECTTOVENDOR_PRODUCT_CATEGORY.replaceAll(" ","").split(",");
			return Arrays.asList(categories).contains(category);
		} else {
			return false;
		}
	}
	
	public static int getMaxOrderQueryResults() {
		if (MAX_ORDER_QUERY_RESULTS == null) {
			MAX_ORDER_QUERY_RESULTS = getProperty(MAX_ORDER_QUERY_RESULTS_PROP_NAME);
		}
		if (MAX_ORDER_QUERY_RESULTS == null) {
			logger.log(Level.ERROR, "sdp-config property " + MAX_ORDER_QUERY_RESULTS_PROP_NAME + " expected an int value but was null. Returning 0.");
			return 0;
		} else {
			try {
				return Integer.valueOf(MAX_ORDER_QUERY_RESULTS);
			} catch (NumberFormatException e) {
				logger.log(Level.ERROR, "sdp-config property " + MAX_ORDER_QUERY_RESULTS_PROP_NAME + " expected an int value but was " + MAX_ORDER_QUERY_RESULTS + ". Returning 0.", e);
				return 0;
			}
		}
	}
}
