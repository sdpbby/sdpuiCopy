package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpOrderSearchResultBean;

public class SdpOrderSearchDBWrapper extends SomSdpDBWrapper {

	private static final Logger logger = Logger
			.getLogger(SdpOrderSearchDBWrapper.class.getName());

	private static final String MAX_DATE_LESS_THAN_MIN = "Max date was less than min date.";

	/**
	 * Retrieves a list of sdp order search results from the database.
	 * 
	 * @param maxDays
	 *            resultset will include only transactions with created/updated
	 *            date that falls within sysdate - maxDays
	 * 
	 * @param min
	 *            min range by rownum
	 * 
	 * @param max
	 *            max range by rownum
	 * 
	 * @return List<SdpOrderSearchResultBean>
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static List<SdpOrderSearchResultBean> getSdpOrderSearchResultsByRecent(
			final int maxDays, final int maxRows) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select sdp_order_id, rec_crt_ts, trans_dt, stor_id, rgst_id, trans_id, ln_id, ord_stat_cde, prm_sku_id, prnt_sku_id, conf_id, ln_item_id, cust_frst_nm, cust_last_nm, dlvr_email_txt, bsns_key, src_sys_id, mstr_itm_id, vndr_id, cntrct_id from ( select sdp_order.sdp_order_id, sdp_order.rec_crt_ts, sdp_order.trans_dt, sdp_order.stor_id, sdp_order.rgst_id, sdp_order.trans_id, sdp_order.ln_id, sdp_order.ord_stat_cde, sdp_order.prm_sku_id, sdp_order.prnt_sku_id, sdp_order.conf_id, sdp_order.ln_item_id, sdp_customer.cust_frst_nm, sdp_customer.cust_last_nm, sdp_customer.dlvr_email_txt, sdp_order.bsns_key, sdp_order.src_sys_id, sdp_order.mstr_itm_id, sdp_catalog.vndr_id, sdp_subscription.cntrct_id from sdp_order LEFT JOIN sdp_customer ON sdp_order.sdp_cust_id = sdp_customer.sdp_cust_id LEFT JOIN sdp_catalog ON sdp_order.ctlg_id = sdp_catalog.ctlg_id left join sdp_subscription on sdp_order.sdp_order_id = sdp_subscription.sdp_order_id where SDP_ORDER.REC_UPD_TS > sysdate-(?) order by SDP_ORDER.REC_UPD_TS desc )  where rownum < ? ";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, maxDays);
					stmt.setInt(2, maxRows + 1);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + maxDays
							+ "\n?[1]=" + maxRows);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<SdpOrderSearchResultBean> output = new ArrayList<SdpOrderSearchResultBean>();
						while (rs.next()) {
							SdpOrderSearchResultBean bean = new SdpOrderSearchResultBean(
									rs);
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByContractId(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("ContractId is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByContractId(arg));
	}

	private static List<String> getSdpOrderIdListByContractId(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER inner join SDP_SUBSCRIPTION on SDP_ORDER.SDP_ORDER_ID=SDP_SUBSCRIPTION.SDP_ORDER_ID where SDP_SUBSCRIPTION.CNTRCT_ID = ? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getBySerialNumber(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("SerialNumber is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListBySerialNumber(arg));
	}

	private static List<String> getSdpOrderIdListBySerialNumber(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER where SDP_ORDER.BSNS_KEY=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg.toUpperCase());
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByLineItemId(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("LineItemId is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByLineItemId(arg));

	}

	private static List<String> getSdpOrderIdListByLineItemId(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER where SDP_ORDER.LN_ITEM_ID=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByPhoneNum(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("PhoneNum is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByPhoneNumber(arg));
	}

	private static List<String> getSdpOrderIdListByPhoneNumber(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER inner join SDP_CUSTOMER on SDP_ORDER.SDP_CUST_ID=SDP_CUSTOMER.SDP_CUST_ID where SDP_CUSTOMER.PH_NBR=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByCustomerEmail(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("CustomerEmail is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByCustomerEmail(arg));
	}

	private static List<String> getSdpOrderIdListByCustomerEmail(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER inner join SDP_CUSTOMER on SDP_ORDER.SDP_CUST_ID=SDP_CUSTOMER.SDP_CUST_ID where upper(SDP_CUSTOMER.CUST_EMAIL_TXT)=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg.toUpperCase());
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByDeliveryEmail(String arg)
			throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("DeliveryEmail is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByDeliveryEmail(arg));
	}

	private static List<String> getSdpOrderIdListByDeliveryEmail(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER inner join SDP_CUSTOMER on SDP_ORDER.SDP_CUST_ID=SDP_CUSTOMER.SDP_CUST_ID where upper(SDP_CUSTOMER.DLVR_EMAIL_TXT)=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg.toUpperCase());
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	public static List<SdpOrderSearchResultBean> getByConfirmationCode(
			String arg) throws DataAccessException, DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("ConfirmationCode is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByConfirmationCode(arg));
	}

	private static List<String> getSdpOrderIdListByConfirmationCode(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER where SDP_ORDER.CONF_ID=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/**
	 * Retrieves a list of sdp order ids by 5-part-key from the sdp order
	 * database table, then builds the OpsSubscriptionAbstractionBean list and
	 * returns the list. lineId is the only optional parameter. Throws
	 * IllegalArgumentException if date, storeId, registerId, or transactionId
	 * are null.
	 * 
	 * @param transDate
	 *            5-part-key element
	 * @param storeId
	 *            5-part-key element
	 * @param registerId
	 *            5-part-key element
	 * @param transactionId
	 *            5-part-key element
	 * @param lineId
	 *            5-part-key element
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public static List<SdpOrderSearchResultBean> getByFivePartKey(
			final Date transDate, final String storeId,
			final String registerId, final String transactionId,
			final String lineId) throws DataAccessException,
			DataSourceLookupException {
		if (transDate == null
				|| (storeId == null && registerId == null
						&& transactionId == null && lineId == null)) {
			throw new DataAccessException(
					"Date or all of [store id, register id, transaction id, line id] parameters cannot be null.");
		} else {
			return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByFivePartKey(
					transDate, storeId, registerId, transactionId, lineId));
		}
	}

	/**
	 * Retrieves a list of sdp order ids by 5-part-key in descending order,
	 * sorted by record creation date. Throws IllegalArgumentException if
	 * transDate is null.
	 * 
	 * @param transDate
	 *            5-part-key element
	 * @param storeId
	 *            5-part-key element
	 * @param registerId
	 *            5-part-key element
	 * @param transactionId
	 *            5-part-key element
	 * @param lineId
	 *            5-part-key element
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 */
	private static List<String> getSdpOrderIdListByFivePartKey(
			final Date transDate, final String storeId,
			final String registerId, final String transactionId,
			final String lineId) throws DataAccessException,
			DataSourceLookupException {
		if (transDate == null) {
			throw new DataAccessException(
					"Transaction date is required for five part key search but value was null.");
		}

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER where SDP_ORDER.TRANS_DT=? "
				+ (isNotNull(storeId) ? "and SDP_ORDER.STOR_ID=? " : " ")
				+ (isNotNull(registerId) ? "and SDP_ORDER.RGST_ID=? " : " ")
				+ (isNotNull(transactionId) ? "and SDP_ORDER.TRANS_ID=? " : " ")
				+ (isNotNull(lineId) ? "and SDP_ORDER.LN_ID=? " : " ")
				+ "order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int index = 0;
					stmt.setDate(++index, getSqlDate(transDate));
					if (isNotNull(storeId)) {
						stmt.setString(++index, storeId);
					}
					if (isNotNull(registerId)) {
						stmt.setString(++index, registerId);
					}
					if (isNotNull(transactionId)) {
						stmt.setString(++index, transactionId);
					}
					if (isNotNull(lineId)) {
						stmt.setString(++index, lineId);
					}
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + transDate
							+ "\nPARAMS:\n?[1]=" + storeId + "\nPARAMS:\n?[2]="
							+ registerId + "\nPARAMS:\n?[3]=" + transactionId
							+ "\nPARAMS:\n?[4]=" + lineId);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/**
	 * Retrieves a list of OpsSubscriptionAbstractionBean from the database
	 * matching the order id list. If a record is not found that matches an id
	 * provided then nothing will be added to the returned list. If no records
	 * are found an empty list is returned.
	 * 
	 * @param arg
	 *            list of SDP order ids
	 * @return OpsSubscriptionAbstractionBean list
	 * @throws SQLException
	 */
	private static List<SdpOrderSearchResultBean> getSimpleSdpOrderListByOrderIdList(
			final List<String> arg) throws DataAccessException,
			DataSourceLookupException {
		final List<SdpOrderSearchResultBean> returnValue = new ArrayList<SdpOrderSearchResultBean>();

		final Connection conn = getConnection();
		try {
			final int size = arg.size();
			for (int i = 0; i < size; i++) {
				final SdpOrderSearchResultBean bean = getSimpleSdpOrderByOrderId(
						arg.get(i), conn);
				if (bean != null) {
					returnValue.add(bean);
				} else {
					throw new IllegalStateException(
							"Failed to retrieve record with id=" + i);
				}
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				throw new DataAccessException(
						"Failed to retrieve recent sdp orders", e);
			}
		}

		return returnValue;
	}

	/**
	 * Retrieves a list of sdp order ids by DotcomOrderNumber from the sdp order
	 * database table, then builds the OpsSubscriptionAbstractionBean list and
	 * returns the list
	 * 
	 * @param arg
	 *            the OLS (dotcom) order number
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 */
	public static List<SdpOrderSearchResultBean> getByDotcomOrderNumber(
			final String arg) throws DataAccessException,
			DataSourceLookupException {
		if (arg == null) {
			throw new DataAccessException("BBY Order Id is Missing");
		}
		return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByDotcomOrderNumber(arg));
	}

	/**
	 * Retrieves a list of sdp order ids by DotcomOrderNumber in descending
	 * order, sorted by record creation date.
	 * 
	 * @param arg
	 *            the OLS (dotcom) order number
	 * @return list of ids
	 * @throws SQLException
	 */
	private static List<String> getSdpOrderIdListByDotcomOrderNumber(
			final String arg) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER where SDP_ORDER.TRANS_ID=? order by SDP_ORDER.REC_CRT_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/**
	 * Retrieves a list of sdp order ids by Order Status and date range from the
	 * sdp order database table, then builds the OpsSubscriptionAbstractionBean
	 * list and returns the list. Since this search can potentially pull
	 * thousands of records, this method also requires a max resultset size
	 * restriction. Throws IllegalArgumentException if max date is less than min
	 * date or if either date value is null.
	 * 
	 * @param arg
	 *            the order status
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public static List<SdpOrderSearchResultBean> getByOrderStatus(
			final String primarySku, final String masterItemId,
			final String orderStatus, final Date min, final Date max,
			final int sizeLimit) throws DataAccessException,
			DataSourceLookupException {
		if (min == null || max == null || max.compareTo(min) < 0) {
			throw new DataAccessException(MAX_DATE_LESS_THAN_MIN);
		} else {
			return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByOrderStatus(
					primarySku, masterItemId, orderStatus, min, max, sizeLimit));
		}
	}

	/**
	 * Retrieves a list of sdp order ids by Order Status and date range in
	 * descending order, sorted by record creation date. This method also
	 * requires a max resultset size restriction.
	 * 
	 * @param orderStatus
	 *            the order status
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 */

	private static List<String> getSdpOrderIdListByOrderStatus(
			final String primarySku, final String masterItemId,
			final String orderStatus, final Date min, final Date max,
			final int sizeLimit) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER_ID from (select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER"
				+ " where SDP_ORDER.REC_CRT_TS between ? and ?"
				+ (isNotNull(primarySku) ? " and SDP_ORDER.prm_sku_id=?" : "")
				+ (isNotNull(masterItemId) ? " and SDP_ORDER.mstr_itm_id=?" : "")
				+ (isNotNull(orderStatus) ? " and SDP_ORDER.ORD_STAT_CDE=?" : "")
				+ " order by SDP_ORDER.REC_CRT_TS desc)" + " where rownum < ?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int index = 0;
					stmt.setDate(++index, getSqlDate(min));
					stmt.setDate(++index, getSqlDate(max));
					if (isNotNull(primarySku)) {
						stmt.setString(++index, primarySku);
					}
					if (isNotNull(masterItemId)) {
						stmt.setString(++index, masterItemId);
					}
					if (isNotNull(orderStatus)) {
						stmt.setString(++index, orderStatus);
					}

					stmt.setInt(++index, sizeLimit);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + primarySku
							+ "PARAMS:\n?[0]=" + masterItemId
							+ "PARAMS:\n?[0]=" + orderStatus + "PARAMS:\n?[0]="
							+ min + "PARAMS:\n?[0]=" + max);

					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/**
	 * Retrieves a list of sdp order ids by customer name and date range from
	 * the sdp order database table, then builds the
	 * OpsSubscriptionAbstractionBean list and returns the list. Since this
	 * search can potentially pull thousands of records, this method also
	 * requires a max resultset size restriction. If either first name or last
	 * name are null then those values will be omitted from the search. Throws
	 * IllegalArgumentException if max date is less than min date or if either
	 * date value is null. Throws IllegalArgumentException if BOTH first name
	 * and last name are null.
	 * 
	 * @param firstName
	 *            customer's first name
	 * @param lastName
	 *            customer's last name
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public static List<SdpOrderSearchResultBean> getByCustomerName(
			final String firstName, final String lastName, final Date min,
			final Date max, final int sizeLimit) throws DataAccessException,
			DataSourceLookupException {
		if (firstName == null && lastName == null) {
			throw new DataAccessException(
					"Both customer name parameters cannot be null.");
		} else if (min == null || max == null || max.compareTo(min) < 0) {
			throw new DataAccessException(MAX_DATE_LESS_THAN_MIN);
		} else {
			return getSimpleSdpOrderListByOrderIdList(getSdpOrderIdListByCustomerName(
					firstName, lastName, min, max, sizeLimit));
		}
	}

	/**
	 * Retrieves a list of sdp order ids by customer name and date range in
	 * descending order, sorted by record creation date. This method also
	 * requires a max resultset size restriction.
	 * 
	 * @param firstName
	 *            customer's first name
	 * @param lastName
	 *            customer's last name
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 */
	private static List<String> getSdpOrderIdListByCustomerName(
			final String firstName, final String lastName, final Date min,
			final Date max, final int sizeLimit) throws DataAccessException,
			DataSourceLookupException {

		final String sqlCommand = "select SDP_ORDER_ID from (select SDP_ORDER.SDP_ORDER_ID from SDP_ORDER" 
				+" inner join SDP_CUSTOMER on SDP_ORDER.SDP_CUST_ID=SDP_CUSTOMER.SDP_CUST_ID where"
				+ (isNotNull(firstName) ? " upper(SDP_CUSTOMER.CUST_FRST_NM)=? and" : "")
				+ (isNotNull(lastName) ? " upper(SDP_CUSTOMER.CUST_LAST_NM)=? and"	: "")
				+ " SDP_ORDER.REC_CRT_TS between ? and ? order by SDP_ORDER.REC_CRT_TS desc) where rownum <= ?";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int index = 0;
					if (isNotNull(firstName)) {
						stmt.setString(++index, firstName.toUpperCase());
					}
					if (isNotNull(lastName)) {
						stmt.setString(++index, lastName.toUpperCase());
					}
					stmt.setDate(++index, getSqlDate(min));
					stmt.setDate(++index, getSqlDate(max));
					stmt.setInt(++index, sizeLimit);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + firstName
							+ "PARAMS:\n?[1]=" + lastName);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<String> returnValue = new ArrayList<String>();
						while (rs.next()) {
							returnValue.add(rs.getString(1));
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/*
	 * 
	 */
	private static SdpOrderSearchResultBean getSimpleSdpOrderByOrderId(
			final String arg, final Connection conn)
			throws DataAccessException, DataSourceLookupException {

		final String sqlCommand = "	SELECT sdp_order.sdp_order_id, "
				+ "	  sdp_order.rec_crt_ts, "
				+ "	  sdp_order.trans_dt, "
				+ "	  sdp_order.stor_id, "
				+ "	  sdp_order.rgst_id, "
				+ "	  sdp_order.trans_id, "
				+ "	  sdp_order.ln_id, "
				+ "	  sdp_order.ord_stat_cde, "
				+ "	  sdp_order.prm_sku_id, "
				+ "	  sdp_order.prnt_sku_id, "
				+ "	  sdp_order.conf_id, "
				+ "	  sdp_order.ln_item_id, "
				+ "	  sdp_customer.cust_frst_nm, "
				+ "	  sdp_customer.cust_last_nm, "
				+ "	  sdp_customer.dlvr_email_txt, "
				+ "	  sdp_subscription.cntrct_id, "
				+ "	  sdp_subscription.cntrct_end_dt, "
				+ "	  sdp_order.bsns_key, "
				+ "	  sdp_order.src_sys_id, "
				+ "	  sdp_order.mstr_itm_id, "
				+ "	  sdp_catalog.vndr_id "
				+ "	FROM sdp_order "
				+ "	LEFT JOIN sdp_customer ON sdp_order.sdp_cust_id = sdp_customer.sdp_cust_id "
				+ "	LEFT JOIN sdp_subscription ON sdp_order.sdp_order_id = sdp_subscription.sdp_order_id "
				+ "	LEFT JOIN sdp_catalog ON sdp_order.ctlg_id = sdp_catalog.ctlg_id "
				+ "	LEFT JOIN sdp_vendor_config ON sdp_catalog.vndr_id = sdp_vendor_config.vndr_id "
				+ "	WHERE sdp_order.sdp_order_id = ?";
		try {
			final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			try {
				stmt.setString(1, arg);
				logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
				final ResultSet rs = stmt.executeQuery();
				try {
					SdpOrderSearchResultBean returnValue = new SdpOrderSearchResultBean();
					if (rs.next()) {
						returnValue = new SdpOrderSearchResultBean();
						returnValue.setSdpOrderId(rs.getString(1));
						returnValue.setCreatedDate(getUtilDate(rs
								.getTimestamp(2)));
						returnValue.setFpkTransactionDate(getUtilDate(rs
								.getTimestamp(3)));
						returnValue.setFpkStoreId(rs.getString(4));
						returnValue.setFpkRegisterId(rs.getString(5));
						returnValue.setFpkTransactionId(rs.getString(6));
						returnValue.setFpkLineId(rs.getString(7));
						returnValue.setSdpOrderStatus(rs.getString(8));
						returnValue.setPrimarySku(rs.getString(9));
						returnValue.setRelatedSku(rs.getString(10));
						returnValue.setConfirmationCode(rs.getString(11));
						returnValue.setLineItemId(rs.getString(12));
						returnValue.setCustomerFirstName(rs.getString(13));
						returnValue.setCustomerLastName(rs.getString(14));
						returnValue.setDeliveryEmail(rs.getString(15));
						returnValue.setContractId(rs.getString(16));
						// returnValue.setContractEndDate(getUtilDate(rs.getTimestamp(17)));
						returnValue.setSerialNumber(rs.getString(18));
						returnValue.setSourceSystem(rs.getString(19));
						returnValue.setMasterItemId(rs.getString(20));
						returnValue.setVendorId(rs.getString(21));
					}
					return returnValue;
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

		} catch (SQLException e) {
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}

	}

	/**
	 * 
	 * @param arg
	 *            id of the dropdown list described in the UI_SELECTITEM_COLLECT
	 *            table in the sdp database schema
	 * @return a <code>List</code> of the <code>SelectItem</code> to
	 *         populate drop down list
	 * @throws SQLException
	 */
	public static List<SelectItem> getProvisioningVendorList()
			throws DataSourceLookupException, DataAccessException {

		final String sqlCommand = "select distinct SDP_VENDOR_CONFIG.VNDR_ID, SDP_VENDOR_CONFIG.VNDR_NM from SDP_VENDOR_CONFIG inner join SDP_VENDOR_STATUS_CODE on SDP_VENDOR_CONFIG.VNDR_ID = SDP_VENDOR_STATUS_CODE.VNDR_ID order by SDP_VENDOR_CONFIG.VNDR_NM asc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					final ResultSet rs = stmt.executeQuery();
					try {
						List<SelectItem> returnValue = new ArrayList<SelectItem>();
						SelectItem selectItem = new SelectItem();
						selectItem.setValue("ALL");
						selectItem.setLabel("ALL VENDORS");
						returnValue.add(selectItem);
						while (rs.next()) {
							selectItem = new SelectItem();
							selectItem.setValue(rs.getString(1));
							selectItem.setLabel(rs.getString(2));
							returnValue.add(selectItem);
						}
						return returnValue;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}

	}

	/**
	 * Query vendor data by vendor id and vendor error code
	 * 
	 * @param vendorId
	 *            the vendor id value to filter on or null to get all
	 * @return a <code>List</code> of the <code>SelectItem</code> to
	 *         populate drop down list
	 * @throws SQLException
	 */
	public static List<SelectItem> getVendorCodeDropDownListByAllVendors()
			throws DataSourceLookupException, DataAccessException {

		final String sqlCommand = "select distinct sdp_vendor_status_code.vndr_code||' - '||sdp_vendor_status_code.message as label, sdp_vendor_status_code.vndr_code as value from sdp_vendor_status_code order by sdp_vendor_status_code.vndr_code asc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<SelectItem> theList = new ArrayList<SelectItem>();
						while (rs.next()) {
							SelectItem selectItem = new SelectItem();
							selectItem.setLabel(rs.getString("label"));
							selectItem.setValue(rs.getString("value"));
							theList.add(selectItem);
						}
						return theList;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}
	}

	/**
	 * Query vendor data by vendor id and vendor error code
	 * 
	 * @param vendorId
	 *            the vendor id value to filter on or null to get all
	 * @return a <code>List</code> of the <code>SelectItem</code> to
	 *         populate drop down list
	 * @throws SQLException
	 */
	public static List<SelectItem> getVendorCodeDropDownListByVendorId(
			String vendorId) throws DataSourceLookupException,
			DataAccessException {

		final String sqlCommand = "select distinct sdp_vendor_status_code.vndr_code||' - '||sdp_vendor_status_code.message as label, sdp_vendor_status_code.vndr_code as value from sdp_vendor_status_code where vndr_id=? order by sdp_vendor_status_code.vndr_code asc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, vendorId);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<SelectItem> theList = new ArrayList<SelectItem>();
						SelectItem selectItem = new SelectItem();
						selectItem.setLabel("ALL CODES");
						selectItem.setValue("ALL");
						theList.add(selectItem);
						while (rs.next()) {
							selectItem = new SelectItem();
							selectItem.setLabel(rs.getString("label"));
							selectItem.setValue(rs.getString("value"));
							theList.add(selectItem);
						}
						return theList;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}

	}

	/**
	 * Query order status data
	 * 
	 * @return a <code>List</code> of the <code>SelectItem</code> to
	 *         populate drop down list
	 * @throws SQLException
	 */

	public static List<SelectItem> getSdpOrderStatusDropDownList()
			throws DataSourceLookupException, DataAccessException {

		String sqlCommand = "select SDP_STATUS_MASTER.STAT_CDE, SDP_STATUS_MASTER.STAT_DESC from SDP_STATUS_MASTER order by SDP_STATUS_MASTER.STAT_DESC asc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<SelectItem> theList = new ArrayList<SelectItem>();
						while (rs.next()) {
							SelectItem selectItem = new SelectItem();
							selectItem.setValue(rs.getString(1));
							selectItem.setLabel(rs.getString(2));
							theList.add(selectItem);
						}
						return theList;
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
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}

	}

	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
