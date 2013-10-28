package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.beans.VendorProvisioningStatusBean;

public class SdpTransactionDBWrapper extends SomSdpDBWrapper {

	private static final Logger logger = Logger.getLogger(SdpTransactionDBWrapper.class.getName());
	
	private static final String WHERE = " where ";
	private static final String AND = " and ";
	private static final String SELECT_SDP_ORDER_DETAIL_FULL = 
			" SELECT sdp_order.sdp_order_id, "
			+"  sdp_order.rec_crt_ts, "
			+"  sdp_order.trans_dt, "
			+"  sdp_order.stor_id, "
			+"  sdp_order.rgst_id, "
			+"  sdp_order.trans_id, "
			+"  sdp_order.ln_id, "
			+"  sdp_order.ord_stat_cde, "
			+"  sdp_order.prm_sku_id, "
			+"  sdp_order.prnt_sku_id, "
			+"  sdp_order.conf_id, "
			+"  sdp_order.ln_item_id, "
			+"  sdp_customer.cust_frst_nm,"
			+"  sdp_customer.cust_last_nm, " 
			+"  sdp_customer.dlvr_email_txt, "
			+"  sdp_subscription.cntrct_id, " 
			+"  sdp_subscription.cntrct_end_dt, "
			+"  sdp_order.bsns_key, "
			+"  sdp_order.sdp_id, "
			+"  sdp_order.prm_sku_prc, "
			+"  sdp_order.prm_sku_tax, "
			+"  sdp_order.prm_sku_tax_rate, "
			+"  sdp_order.prnt_sku_prc, "
			+"  sdp_order.prnt_sku_tax, " 
			+"  sdp_order.prnt_sku_tax_rate, "
			+"  sdp_order.qty, "
			+"  sdp_order.val_pkg_id, "
			+"  sdp_order.key_cde, "
			+"  sdp_catalog.vndr_id, "
			+"  sdp_order.vndr_prod_id, "
			+"  sdp_order.cncl_reas_cde, "
			+"  sdp_customer.cust_id, " 
			+"  sdp_customer.cust_email_txt, "
			+"  sdp_customer.cust_mid_nm, "
			+"  sdp_customer.addr_line1_txt, "
			+"  sdp_customer.addr_line2_txt, "
			+"  sdp_customer.blg_addr_lbl_txt, "
			+"  sdp_customer.city_txt, "
			+"  sdp_customer.state_cde, "
			+"  sdp_customer.postal_cde, "
			+"  sdp_customer.ph_nbr, "
			+"  sdp_customer.ph_addr_lbl, "
			+"  sdp_customer.ctry_txt, "
			+"  sdp_customer.crcd_nbr, " 
			+"  sdp_customer.crcd_typ, "
			+"  sdp_customer.crcd_nm, " 
			+"  sdp_customer.crcd_exp_dt, "
			+"  sdp_catalog.ctlg_id, "
			+"  sdp_catalog.cat, "
			+"  sdp_catalog.sub_cat, "
			+"  sdp_catalog.dsp_nm, "
			+"  sdp_catalog.prod_typ, "
			+"  sdp_catalog.mstr_vndr_id, "
			+"  sdp_order.mstr_itm_id, "
			+"  sdp_customer.sdp_cust_id, "
			+"  sdp_order.src_sys_id "
			+"FROM sdp_order LEFT JOIN sdp_customer ON sdp_order.sdp_cust_id = sdp_customer.sdp_cust_id "
			+"LEFT JOIN sdp_subscription ON sdp_order.sdp_order_id = sdp_subscription.sdp_order_id "
			+"LEFT JOIN sdp_catalog ON sdp_order.ctlg_id = sdp_catalog.ctlg_id "
			+"LEFT JOIN sdp_vendor_config ON sdp_catalog.vndr_id = sdp_vendor_config.vndr_id ";
	
	private static final String VENDOR_PROVISIONING_DETAIL_FULL = 
			" SELECT rownumber, " +
			" vndr_id, " +
			" vndr_key_id, " +
			" csm_id, " +
			" sdp_id, " +
			" req_type, " +
			" status_code, " +
			" status, " +
			" created " +
			" FROM vendor_provisioning_status ";
	
	/**
	 * Retrieves a single, more detailed SdpTransactionDataXml from the
	 * database matching the order id. Although the values in this column should
	 * be unique, this method will return the first record found the result set,
	 * if any.
	 * 
	 * The returned SdpTransactionDataXml will contain as much detail
	 * as required to populate the subscription/order detail screen. This method
	 * should NOT be used to populate the search result screen
	 * 
	 * @param arg
	 *            SDP order id
	 * @return List of SdpTransactionDataXml
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static SdpTransactionDataBean getSdpTransactionDataBySdpOrderId(final String sdpOrderId) throws DataAccessException, DataSourceLookupException {
		if (sdpOrderId == null) {
			throw new DataAccessException("sdpOrderId was null.");
		}
		final String sqlCommand = SELECT_SDP_ORDER_DETAIL_FULL + "where sdp_order.sdp_order_id = ?" ;
		logger.log(Level.DEBUG, "Executing query >> " + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, sdpOrderId);
					logger.log(Level.DEBUG, "PreparedStatement params >> \n?[0]=" + sdpOrderId);
					
					final ResultSet rs = stmt.executeQuery();
					try {
						if (rs.next()) {
							SdpTransactionDataBean output = new SdpTransactionDataBean(rs);
							return output;
						} else {
							return null;
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to retrieve transaction record.", e);
		}
	}
	
	/**
	 * Retrieves a list of SdpTransactionDataBean from the
	 * database matching the order id. Although the values in this column should
	 * be unique, this method will return the first record found the result set,
	 * if any.
	 * 
	 * The returned SdpTransactionDataXml will contain as much detail
	 * as required to populate the subscription/order detail screen. This method
	 * should NOT be used to populate the search result screen
	 * 
	 * @param arg
	 *            SDP order id
	 * @return List of SdpTransactionDataBean
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	private static List<SdpTransactionDataBean> getSdpTransactionDataByArgs(final String[] criteria, final String[] values) throws DataAccessException, DataSourceLookupException {
		if (criteria == null || criteria.length < 1) {
			throw new DataAccessException("Criteria list was null or empty. This should never happen.");
		}
		if (values == null || values.length < 1) {
			throw new DataAccessException("Value list was null or empty. This should never happen.");
		}
		if (criteria.length != values.length) {
			throw new DataAccessException("Criteria list size <" +  criteria.length + "> did not match value list size <" +  values.length + ">. This should never happen.");
		}
		
		final String sqlCommand = buildQuery(SELECT_SDP_ORDER_DETAIL_FULL, criteria);
		logger.log(Level.DEBUG, "Executing query >> " + sqlCommand);
		
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					
					for (int i = 0; i < values.length; i++) {
						stmt.setString(i+1, values[i]);
					}
					if (logger.isDebugEnabled()) {
						final StringBuilder builder = new StringBuilder();
						for (int i = 0; i < values.length; i++) {
							builder.append("\narg[" + i + "]=" + values[i]);
						}
						logger.log(Level.DEBUG, "PreparedStatement params >> " + builder.toString());
					}
					
					final ResultSet rs = stmt.executeQuery();
					try {
						List<SdpTransactionDataBean> output = new ArrayList<SdpTransactionDataBean>();
						while (rs.next()) {
							SdpTransactionDataBean result = new SdpTransactionDataBean(rs);
							output.add(result);
						}
						return output;
						
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to retrieve transaction record.", e);
		}
	}
	
	private static String buildQuery(String query, String[] criteria) {
		StringBuilder builder = new StringBuilder();
		builder.append(query);
		final int size = criteria.length;
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				builder.append(WHERE + criteria[i]);
			} else {
				builder.append(AND + criteria[i]);
			}
		}
		return builder.toString();
	}
	
	public static List<SdpTransactionDataBean> getSdpTransactionDataByLineItemId(final String lineItemId) throws DataAccessException, DataSourceLookupException {
		final String[] criteria = {"sdp_order.ln_item_id = ?"};
		final String[] values = {lineItemId};
		return getSdpTransactionDataByArgs(criteria, values);
	}
	
	/**
	 * Retrieves a list of VendorProvisioningSearchResultBean from the
	 * database matching the order id. Although the values in this column should
	 * be unique, this method will return the first record found the result set,
	 * if any.
	 * 
	 * The returned VendorProvisioningSearchResultBean will contain as much detail
	 * as required to populate the subscription/order detail screen. This method
	 * should NOT be used to populate the search result screen
	 * 
	 * @param arg
	 *            SDP order id
	 * @return List of VendorProvisioningStatusBean
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	private static List<VendorProvisioningStatusBean> getVendorProvisioningSearchResultByArgs(final String[] criteria, final String[] values) throws DataAccessException, DataSourceLookupException {
		if (criteria == null || criteria.length < 1) {
			throw new DataAccessException("Criteria list was null or empty. This should never happen.");
		}
		if (values == null || values.length < 1) {
			throw new DataAccessException("Value list was null or empty. This should never happen.");
		}
		if (criteria.length != values.length) {
			throw new DataAccessException("Criteria list size <" +  criteria.length + "> did not match value list size <" +  values.length + ">. This should never happen.");
		}
		
		final String sqlCommand = buildQuery(VENDOR_PROVISIONING_DETAIL_FULL, criteria);
		logger.log(Level.DEBUG, "Executing query >> " + sqlCommand);
		
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					
					for (int i = 0; i < values.length; i++) {
						stmt.setString(i+1, values[i]);
					}
					if (logger.isDebugEnabled()) {
						final StringBuilder builder = new StringBuilder();
						for (int i = 0; i < values.length; i++) {
							builder.append("\narg[" + i + "]=" + values[i]);
						}
						logger.log(Level.DEBUG, "PreparedStatement params >> " + builder.toString());
					}
					
					final ResultSet rs = stmt.executeQuery();
					try {
						List<VendorProvisioningStatusBean> output = new ArrayList<VendorProvisioningStatusBean>();
						while (rs.next()) {
							VendorProvisioningStatusBean result = new VendorProvisioningStatusBean(rs);
							output.add(result);
						}
						return output;
						
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to retrieve transaction record.", e);
		}
	}
	
	public static List<VendorProvisioningStatusBean> getVendorProvisioningSearchResultBySdpId(final String sdpId) throws DataAccessException, DataSourceLookupException {
		final String[] criteria = {"sdp_id = ?"};
		final String[] values = {sdpId};
		return getVendorProvisioningSearchResultByArgs(criteria, values);
	}
	
	
	public static int setDeliveryEmailByCustomerId(final String sdpCustomerId, final String deliveryEmail) throws DataSourceLookupException, DataAccessException {
		
		final String sqlCommand = "update SDP_CUSTOMER set SDP_CUSTOMER.DLVR_EMAIL_TXT=? where SDP_CUSTOMER.SDP_CUST_ID=?";

		int success = 0;
		try {
			final Connection conn = getConnection();
			try {
				conn.setAutoCommit(false);
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, deliveryEmail);
					stmt.setString(2, sdpCustomerId);
					
					success = stmt.executeUpdate();
					
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to updated delivery email for CUSTOMER_ID=[" + sdpCustomerId + "]", e);
		}
		return success;
	}
}