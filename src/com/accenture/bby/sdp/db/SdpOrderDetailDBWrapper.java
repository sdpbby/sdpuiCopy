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
import com.accenture.bby.sdp.web.beans.AdjustmentHistoryBean;
import com.accenture.bby.sdp.web.beans.CommsatHistoryBean;
import com.accenture.bby.sdp.web.beans.ExceptionResultBean;
import com.accenture.bby.sdp.web.beans.OrderStatusHistoryBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionBean;
import com.accenture.bby.sdp.web.beans.SdpTransactionDataBean;
import com.accenture.bby.sdp.web.beans.VendorRequestHistoryBean;

public class SdpOrderDetailDBWrapper extends SomSdpDBWrapper {
	
	private static final Logger logger = Logger.getLogger(SdpOrderDetailDBWrapper.class.getName());
	
	/**
	 * Retrieves a single sdp order from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @return SdpTransactionDataBean
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static SdpTransactionDataBean getSdpOrderDetailBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		return SdpTransactionDBWrapper.getSdpTransactionDataBySdpOrderId(arg1);
	}
	
	
	public static String retrieveSdpIdBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		final String sqlCommand = "select sdp_id from SDP_ORDER where SDP_ORDER_id = ? ";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		String sdpId = null;
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						 
						while (rs.next()) {
							sdpId = rs.getString(1);
						
						}
						return sdpId;
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
			throw new DataAccessException("Failed to retrieve status history logs", e);
		}
	}
	
	public static String retrieveTransactionBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		final String sqlCommand = "select sdp_id from SDP_ORDER_TRANSACTION where SDP_ORDER_id = ? ";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		String sdpId = null;
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						 
						while (rs.next()) {
							sdpId = rs.getString(1);
						
						}
						return sdpId;
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
			throw new DataAccessException("Failed to retrieve status history logs", e);
		}
	}
	
	
	/**
	 * Retrieves a list of commsat history logs from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @return List<CommsatHistoryBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<CommsatHistoryBean> getCommsatHistoryBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		
		final String sqlCommand = "SELECT SDP_SUBSCPN_COMMSAT_HIST.SDP_ORDER_ID, SDP_SUBSCPN_COMMSAT_HIST.COMMSAT_HIST_ID, SDP_SUBSCPN_COMMSAT_HIST.REC_CRT_TS, SDP_SUBSCPN_COMMSAT_HIST.TMPLT_ID, SDP_SUBSCPN_COMMSAT_HIST.EMAIL_ID, SDP_SUBSCPN_COMMSAT_HIST.RDMP_CDE_LIST_TXT from SDP_SUBSCPN_COMMSAT_HIST where SDP_ORDER_ID=? order by COMMSAT_HIST_ID desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<CommsatHistoryBean> output = new ArrayList<CommsatHistoryBean>();
						while (rs.next()) {
							CommsatHistoryBean bean = new CommsatHistoryBean();
							bean.setSdpOrderId(rs.getString(1));
							bean.setCommsatHistoryId(rs.getString(2));
							bean.setCreatedDate(rs.getTimestamp(3));
							bean.setTemplateId(rs.getString(4));
							bean.setDeliveryEmail(rs.getString(5));
							bean.setKeycode(rs.getString(6));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve commsat history logs", e);
		}
	}
	
	/**
	 * Retrieves a list of status history logs from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @return List<OrderStatusHistoryBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<OrderStatusHistoryBean> getStatusHistoryBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		
		final String sqlCommand = "select SDP_ORDER_STATUS_LOG.SDP_ORDER_ID, SDP_ORDER_STATUS_LOG.REC_CRT_TS, SDP_ORDER_STATUS_LOG.STAT_CDE, SDP_ORDER_STATUS_LOG.SDP_ID from SDP_ORDER_STATUS_LOG where SDP_ORDER_STATUS_LOG.SDP_ORDER_ID=? order by SDP_ORDER_STATUS_LOG.REC_CRT_TS desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<OrderStatusHistoryBean> output = new ArrayList<OrderStatusHistoryBean>();
						while (rs.next()) {
							OrderStatusHistoryBean bean = new OrderStatusHistoryBean();
							bean = new OrderStatusHistoryBean();
							bean.setSdpId(rs.getString(1));
							bean.setCreatedDate(rs.getTimestamp(2));
							bean.setStatus(rs.getString(3));
							bean.setSdpId(rs.getString(4));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve status history logs", e);
		}
	}
	
	/**
	 * Retrieves a list of detailed sdp request / response logs from the
	 * database matching the sdp order id OR lineitem id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @param arg2
	 *            line item id
	 * @return List<SdpTransactionBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<SdpTransactionBean> getTransactionHistoryBySdpOrderIdOrLineItemId(final String arg1, final String arg2) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		if (arg2 == null) {
			throw new DataAccessException("arg2 was null. Expected an SDP LineItem ID.");
		}
		
		final String sqlCommand = "select SDP_ORD_RQST_RESP_LOG.RQST_ID, SDP_ORD_RQST_RESP_LOG.SDP_ORDER_ID, SDP_ORD_RQST_RESP_LOG.LN_ITEM_ID, SDP_ORD_RQST_RESP_LOG.SDP_ID, SDP_ORD_RQST_RESP_LOG.SRC_SYS_ID, SDP_ORD_RQST_RESP_LOG.RQST_TYP, SDP_ORD_RQST_RESP_LOG.REC_CRT_TS from SDP_ORD_RQST_RESP_LOG where SDP_ORD_RQST_RESP_LOG.SDP_ORDER_ID=? or SDP_ORD_RQST_RESP_LOG.LN_ITEM_ID=? order by SDP_ORD_RQST_RESP_LOG.REC_CRT_TS desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					stmt.setString(2, arg2);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1 + "\n?[1]=" + arg2);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<SdpTransactionBean> output = new ArrayList<SdpTransactionBean>();
						while (rs.next()) {
							SdpTransactionBean bean = new SdpTransactionBean();
							bean.setLogId(rs.getString(1));
							bean.setSdpOrderId(rs.getString(2));
							bean.setLineItemId(rs.getString(3));
							bean.setSdpId(rs.getString(4));
							bean.setSourceSystemId(rs.getString(5));
							bean.setRequestType(rs.getString(6));
							bean.setCreatedDate(rs.getTimestamp(7));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve request response logs", e);
		}
	}
	
	/**
	 * Retrieves a single sdp request / response log record from the
	 * database matching the log id. Returns null if record not found.
	 * 
	 * @param logId
	 *            log id
	 * @return SdpTransactionBean
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static SdpTransactionBean getRequestResponseLogByLogId(final String logId) throws DataAccessException, DataSourceLookupException {
		
		if (logId == null) {
			throw new DataAccessException("Log ID was null. Expected an unable to lookup log record.");
		}
		
		final String sqlCommand = "select SDP_ORD_RQST_RESP_LOG.RQST_ID, SDP_ORD_RQST_RESP_LOG.SDP_ORDER_ID, SDP_ORD_RQST_RESP_LOG.LN_ITEM_ID, SDP_ORD_RQST_RESP_LOG.SDP_ID, SDP_ORD_RQST_RESP_LOG.SRC_SYS_ID, SDP_ORD_RQST_RESP_LOG.RQST_TYP, SDP_ORD_RQST_RESP_LOG.REC_CRT_TS, SDP_ORD_RQST_RESP_LOG.RQST_MSG, SDP_ORD_RQST_RESP_LOG.RESP_MSG from SDP_ORD_RQST_RESP_LOG where SDP_ORD_RQST_RESP_LOG.RQST_ID=?";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, logId);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + logId);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						if (rs.next()) {
							final SdpTransactionBean bean = new SdpTransactionBean();
							bean.setLogId(rs.getString(1));
							bean.setSdpOrderId(rs.getString(2));
							bean.setLineItemId(rs.getString(3));
							bean.setSdpId(rs.getString(4));
							bean.setSourceSystemId(rs.getString(5));
							bean.setRequestType(rs.getString(6));
							bean.setCreatedDate(rs.getTimestamp(7));
							bean.setRequestMessage(rs.getString(8));
							bean.setResponseMessage(rs.getString(9));
							return bean;
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
			throw new DataAccessException("Failed to retrieve request response log with RQST_ID=[" + logId + "]", e);
		}
	}
	
	/**
	 * Retrieves a list of vendor request logs from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 *            
	 * @return List<VendorProvisioningStatusBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<VendorRequestHistoryBean> getVendorRequestHistoryBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected SDP Order ID.");
		}
		
		final String sqlCommand = "select VENDOR_PROVISIONING_STATUS.ROWNUMBER, VENDOR_PROVISIONING_STATUS.VNDR_ID, VENDOR_PROVISIONING_STATUS.VNDR_KEY_ID, VENDOR_PROVISIONING_STATUS.REQ_TYPE, VENDOR_PROVISIONING_STATUS.CREATED, VENDOR_PROVISIONING_STATUS.SDP_ID, VENDOR_PROVISIONING_STATUS.STATUS, VENDOR_PROVISIONING_STATUS.STATUS_CODE from VENDOR_PROVISIONING_STATUS where VENDOR_PROVISIONING_STATUS.CSM_ID=? order by VENDOR_PROVISIONING_STATUS.CREATED desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<VendorRequestHistoryBean> output = new ArrayList<VendorRequestHistoryBean>();
						while (rs.next()) {
							VendorRequestHistoryBean bean = new VendorRequestHistoryBean();
							bean.setRowNumber(rs.getInt(1));
							bean.setVendorId(rs.getString(2));
							bean.setSerialNumber(rs.getString(3));
							bean.setRequestType(rs.getString(4));
							bean.setCreatedDate(rs.getTimestamp(5));
							bean.setSdpId(rs.getString(6));
							bean.setStatus(rs.getString(7));
							bean.setVendorCode(rs.getString(8));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve vendor request logs", e);
		}
	}
	
	/**
	 * Retrieves a list of exception history logs from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @return List<ExceptionResultBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<ExceptionResultBean> getExceptionHistoryBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		
		final String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM, SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS, SDP_EXCEPTION_MESSAGE_LOG.SDP_ID, SDP_EXCEPTION_MESSAGE_LOG.SRC_SYS_ID, SDP_ERROR_CODE_REF.ERR_DESC, SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE, SDP_EXCEPTION_MESSAGE_LOG.SDP_ORDER_ID from SDP_EXCEPTION_MESSAGE_LOG left join SDP_ERROR_CODE_REF on SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE=SDP_ERROR_CODE_REF.ERR_CDE where SDP_EXCEPTION_MESSAGE_LOG.SDP_ORDER_ID=? order by SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<ExceptionResultBean> output = new ArrayList<ExceptionResultBean>();
						while (rs.next()) {
							ExceptionResultBean bean = new ExceptionResultBean();
							bean.setExceptionId(rs.getString(1));
							bean.setCreatedDate(rs.getTimestamp(2));
							bean.setSdpId(rs.getString(3));
							bean.setSourceSystem(rs.getString(4));
							bean.setDescription(rs.getString(5));
							bean.setCode(rs.getString(6));
							bean.setSdpOrderId(rs.getString(7));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve exception history logs", e);
		}
	}
	
	/**
	 * Retrieves a list of adjustment history logs from the
	 * database matching the sdp order id.
	 * 
	 * @param arg1
	 *            SDP order id
	 * @return List<AdjustmentHistoryBean>
	 * @throws DataAccessException 
	 * @throws DataSourceLookupException 
	 */
	public static List<AdjustmentHistoryBean> getAdjustmentHistoryBySdpOrderId(final String arg1) throws DataAccessException, DataSourceLookupException {
		
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected an SDP Order ID.");
		}
		
		final String sqlCommand = "select sdp_order_transaction.sdp_order_id, sdp_order_transaction.sdp_trans_id, sdp_order_transaction.sdp_id" +
				", sdp_order_transaction.stor_id, sdp_order_transaction.rgst_id, sdp_order_transaction.trans_id, sdp_order_transaction.ln_id" +
				", sdp_order_transaction.trans_dt, sdp_order_transaction.bsns_key, sdp_order_transaction.prm_sku" +
				", sdp_order_transaction.prnt_sku, sdp_order_transaction.mstr_itm_id from sdp_order_transaction" + 
				" join sdp_ord_rqst_resp_log on sdp_ord_rqst_resp_log.sdp_order_id = sdp_order_transaction.sdp_order_id" +
				" and sdp_ord_rqst_resp_log.rqst_typ = 'ADJ' and sdp_order_transaction.sdp_order_id=? " +
				" and sdp_order_transaction.sdp_id = sdp_ord_rqst_resp_log.sdp_id order by sdp_order_transaction.trans_ts desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<AdjustmentHistoryBean> output = new ArrayList<AdjustmentHistoryBean>();
						while (rs.next()) {
							AdjustmentHistoryBean bean = new AdjustmentHistoryBean();
							bean.setSdpOrderId(rs.getString(1));
							bean.setTransactionId(rs.getString(2));
							bean.setSdpId(rs.getString(3));
							bean.setFpkStoreId(rs.getString(4));
							bean.setFpkRegisterId(rs.getString(5));
							bean.setFpkTransactionId(rs.getString(6));
							bean.setFpkLineId(rs.getString(7));
							bean.setFpkTransactionDate(rs.getTimestamp(8));
							bean.setSerialNumber(rs.getString(9));
							bean.setPrimarySku(rs.getString(10));
							bean.setRelatedSku(rs.getString(11));
							bean.setMasterItemId(rs.getString(12));
							output.add(bean);
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
			throw new DataAccessException("Failed to retrieve adjustment history logs", e);
		}
	}
	
	
}
