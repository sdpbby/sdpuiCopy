package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.ECCUIResultBean;

public class ECCUIDBWrapper extends SomSdpDBWrapper {
	
	private static final Logger logger = Logger.getLogger(ECCUIDBWrapper.class.getName());
	
	private static final String SELECTED_COLUMNS = " select SDP_ORDER.SDP_ORDER_ID, SDP_CATALOG.PRM_SKU_ID, SDP_CATALOG.DSP_NM, SDP_CUSTOMER.CUST_LAST_NM, SDP_CUSTOMER.CUST_FRST_NM, SDP_ORDER.KEY_CDE, SDP_ORDER.TRANS_ID, SDP_ORDER.STOR_ID, SDP_ORDER.RGST_ID, SDP_ORDER.TRANS_DT, SDP_ORDER.ORD_STAT_CDE, SDP_CUSTOMER.DLVR_EMAIL_TXT, SDP_ORDER.LN_ITEM_ID ";
	private static final String JOIN_TABLES = " from SDP_ORDER inner join SDP_CATALOG on SDP_ORDER.CTLG_ID = SDP_CATALOG.CTLG_ID left join SDP_CUSTOMER on SDP_ORDER.SDP_CUST_ID = SDP_CUSTOMER.SDP_CUST_ID inner join SDP_SKU_COMMSAT_TEMPL_MAP on SDP_CATALOG.CTLG_ID = SDP_SKU_COMMSAT_TEMPL_MAP.CTLG_ID ";
	private static final String ORDER_BY = " order by SDP_ORDER.REC_UPD_TS desc ";

	public static List<ECCUIResultBean> getOrderByBBYOrderID(String bbyOrderID) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = SELECTED_COLUMNS + JOIN_TABLES + " where SDP_ORDER.TRANS_ID=? " + ORDER_BY;
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, bbyOrderID);
					final ResultSet rs = stmt.executeQuery();
					try {
						List<ECCUIResultBean> beans = new ArrayList<ECCUIResultBean>();
						while (rs.next()) {
							ECCUIResultBean bean = new ECCUIResultBean();
							bean.setSdpOrderId(rs.getString(1));
							bean.setPrimarySku(rs.getString(2));
							bean.setOffrName(rs.getString(3));
							bean.setCustomerLastName(rs.getString(4));
							bean.setCustomerFirstName(rs.getString(5));
							bean.setKeyCode(rs.getString(6));
							bean.setFpkTransactionId(rs.getString(7));
							bean.setFpkStoreId(rs.getString(8));
							bean.setFpkRegisterId(rs.getString(9));
							bean.setFpkTransactionDate(rs.getTimestamp(10));
							bean.setStatus(rs.getString(11));
							bean.setDeliveryEmail(rs.getString(12));
							bean.setLineItemId(rs.getString(13));
							beans.add(bean);
						}
						return beans;
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
			logger.log(Level.ERROR, "Failure occurred when retrieving transaction data for ECC Console search by TRANS_ID=[" + bbyOrderID + "]", e);
			throw new DataAccessException("Unable to retrieve transaction data at this time. Please notify your site administrator.", e);
		}
	}
	public static List<ECCUIResultBean> getOrderByFPK(String storeId, String regId, String transId, Date transDate) throws DataSourceLookupException, DataAccessException {
		
		if (storeId == null && regId == null && transId == null && transDate == null) {
			throw new DataAccessException("No search parameters provided. This should not be allowed!");
		}
		
		final String storeIdParam = " SDP_ORDER.STOR_ID=? ";
		final String regIdParam = " SDP_ORDER.RGST_ID=? ";
		final String transIdParam = " SDP_ORDER.TRANS_ID=? ";
		final String transDateParam = " SDP_ORDER.TRANS_DT=? ";
		
		List<String> params = new ArrayList<String>();
		if (storeId != null) params.add(storeIdParam);
		if (regId != null) params.add(regIdParam);
		if (transId != null) params.add(transIdParam);
		if (transDate != null) params.add(transDateParam);
		
		final String sqlCommand = SELECTED_COLUMNS + JOIN_TABLES + " where " + getParams(params) + ORDER_BY;
		try {
			final Connection conn = getConnection();
			try {
				logger.log(Level.DEBUG, "sqlCommand >> " + sqlCommand);
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					int i = 0;
					if (storeId != null) stmt.setString(++i, storeId);
					if (regId != null) stmt.setString(++i, regId);
					if (transId != null) stmt.setString(++i, transId);
					if (transDate != null) stmt.setDate(++i, getConvertedDate(transDate));
					if (logger.isDebugEnabled()) {
						int j = 0;
						StringBuilder builder = new StringBuilder();
						builder.append(storeId != null ? "\n?[" + j++ + "]=" + storeId : "");
						builder.append(regId != null ? "\n?[" + j++ + "]=" + regId : "");
						builder.append(transId != null ? "\n?[" + j++ + "]=" + transId : "");
						builder.append(transDate != null ? "\n?[" + j++ + "]=" + getConvertedDate(transDate) : "");
						logger.log(Level.DEBUG, "Params >> " + builder.toString());
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						List<ECCUIResultBean> beans = new ArrayList<ECCUIResultBean>();
						while (rs.next()) {
							ECCUIResultBean bean = new ECCUIResultBean();
							bean.setSdpOrderId(rs.getString(1));
							bean.setPrimarySku(rs.getString(2));
							bean.setOffrName(rs.getString(3));
							bean.setCustomerLastName(rs.getString(4));
							bean.setCustomerFirstName(rs.getString(5));
							bean.setKeyCode(rs.getString(6));
							bean.setFpkTransactionId(rs.getString(7));
							bean.setFpkStoreId(rs.getString(8));
							bean.setFpkRegisterId(rs.getString(9));
							bean.setFpkTransactionDate(rs.getTimestamp(10));
							bean.setStatus(rs.getString(11));
							bean.setDeliveryEmail(rs.getString(12));
							bean.setLineItemId(rs.getString(13));
							
							beans.add(bean);
						}
						return beans;
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
			logger.log(Level.ERROR, "Failure occurred when retrieving transaction data for ECC Console search.", e);
			throw new DataAccessException("Unable to retrieve transaction data at this time. Please notify your site administrator.", e);
		}
	}
	
//	public static String[] getLastSEMMessage(String sdpOrderId) throws DataSourceLookupException, DataAccessException { 
//		final String sqlCommand = "select VENDOR_PROVISIONING_STATUS.MSG_XML from VENDOR_PROVISIONING_STATUS where VENDOR_PROVISIONING_STATUS.CSM_ID=? and VENDOR_PROVISIONING_STATUS.REQ_TYPE='SEM' and VENDOR_PROVISIONING_STATUS.CREATED=(select MAX(VENDOR_PROVISIONING_STATUS.CREATED) from VENDOR_PROVISIONING_STATUS WHERE VENDOR_PROVISIONING_STATUS.CSM_ID=? and VENDOR_PROVISIONING_STATUS.REQ_TYPE='SEM')";
//
//		try {
//			final Connection conn = getConnection();
//			try {
//				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
//				try {
//					stmt.setString(1, sdpOrderId);
//					stmt.setString(2, sdpOrderId);
//					final ResultSet rs = stmt.executeQuery();
//					try {
//						List<String> list = new ArrayList<String>();
//						while (rs.next()) {
//							list.add(rs.getString(1));
//						}
//						return list.toArray(new String[list.size()]);
//					} finally {
//						if (rs != null) {
//							rs.close();
//						}
//					}
//				} finally {
//					if (stmt != null) {
//						stmt.close();
//					}
//				}
//			} finally {
//				if (conn != null) {
//					conn.close();
//				}
//			}
//		} catch (SQLException e) {
//			logger.log(Level.ERROR, "Failure occurred when retrieving transaction data for ECC Console search by CSM_ID=[" + sdpOrderId + "]", e);
//			throw new DataAccessException("Unable to retrieve transaction data at this time. Please notify your site administrator.", e);
//		}
//	}
	
	private static String getParams(List<String> args) {
		final int count = args.size();	
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count; i++) {
			builder.append((i > 0 ? " AND " : "") + args.get(i));
		}
		return builder.toString();
	}
	
	
}
