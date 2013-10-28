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
import com.accenture.bby.sdp.web.beans.RequestResponseLogBean;
import com.accenture.bby.sdp.web.beans.VendorProvisioningStatusBean;

public class VendorProvisioningStatusDBWrapper extends SomSdpDBWrapper {

	private static final Logger logger = Logger
			.getLogger(VendorProvisioningStatusDBWrapper.class.getName());

	private static final String MAX_DATE_LESS_THAN_MIN = "Max date was less than min date.";

	/**
	 * Retrieves a vendor provisioning status records from the database matching
	 * the vps id or null if not found
	 * 
	 * @param arg1
	 *            Vendor Provisioning Status id
	 * 
	 * @return VendorProvisioningStatusBean
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static VendorProvisioningStatusBean getVendorProvisioningStatusBeanByVendorProvisioningStatusId(
			final String arg1) throws DataSourceLookupException,
			DataAccessException {
		if (arg1 == null) {
			throw new DataAccessException("arg1 was null. Expected VPS ID.");
		}

		final String sqlCommand = "select vendor_provisioning_status.rownumber, vendor_provisioning_status.vndr_key_id, vendor_provisioning_status.csm_id, vendor_provisioning_status.vndr_id, vendor_provisioning_status.sdp_id, vendor_provisioning_status.req_type, vendor_provisioning_status.status_code, vendor_provisioning_status.status, vendor_provisioning_status.msg_xml, vendor_provisioning_status.created, sdp_vendor_config.cat from vendor_provisioning_status left join sdp_vendor_config on sdp_vendor_config.vndr_id = vendor_provisioning_status.vndr_id where vendor_provisioning_status.rownumber= ?";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg1);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						if (rs.next()) {
							final VendorProvisioningStatusBean output = new VendorProvisioningStatusBean();
							output.setVpsId(rs.getString(1));
							output.setSerialNumber(rs.getString(2));
							output.setSdpOrderId(rs.getString(3));
							output.setVendorId(rs.getString(4));
							output.setSdpId(rs.getString(5));
							output.setRequestType(rs.getString(6));
							output.setResponseCode(rs.getString(7));
							output.setStatus(rs.getString(8));
							output.setBbyOrder(rs.getString(9));
							output.setCreatedDate(rs.getTimestamp(10));
							output.setCategory(rs.getString(11));
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
			throw new DataAccessException(
					"Failed to retrieve vendor provisioning status record", e);
		}
	}

	/**
	 * Retrieves a list of RequestResponseLogBean records from the database
	 * matching the sdp id and request type.
	 * 
	 * @param arg1
	 *            sdp id
	 * @param arg2
	 *            Request type
	 * 
	 * @return List<RequestResponseLogBean>
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static List<RequestResponseLogBean> getRequestResponseLogsBySdpIdAndRequestType(
			final String sdpId, final String requestType)
			throws DataSourceLookupException, DataAccessException {

		if (sdpId == null) {
			throw new DataAccessException("arg1 was null. Expected a SDPID.");
		}

		if (requestType == null) {
			throw new DataAccessException(
					"arg1 was null. Expected a Request Type.");
		}

		final String sqlCommand = "select VENDOR_REQ_LOG.REQ_TYPE, VENDOR_REQ_LOG.CREATED, VENDOR_REQ_LOG.MSG_XML, VENDOR_RESP_LOG.CREATED, VENDOR_RESP_LOG.MSG_XML from VPS_REQUEST_LINK inner join VENDOR_REQ_LOG on VPS_REQUEST_LINK.REQ_LOG_ID = VENDOR_REQ_LOG.ROWNUMBER left join VENDOR_RESP_LOG on VENDOR_REQ_LOG.ROWNUMBER = VENDOR_RESP_LOG.REQ_LOG_ID where VENDOR_REQ_LOG.REQ_TYPE=? and VPS_REQUEST_LINK.VPS_SDP_ID=? order by VPS_REQUEST_LINK.CREATED desc";
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, requestType);
					stmt.setString(2, sdpId);
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + requestType
								+ "\n?[1]=" + sdpId);
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<RequestResponseLogBean> output = new ArrayList<RequestResponseLogBean>();
						while (rs.next()) {
							RequestResponseLogBean bean = new RequestResponseLogBean();
							bean.setRequestType(rs.getString(1));
							bean.setRequestCreatedDate(rs.getTimestamp(2));
							bean.setRequestMessage(rs.getString(3));
							bean.setResponseCreatedDate(rs.getTimestamp(4));
							bean.setResponseMessage(rs.getString(5));
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
					"Failed to retrieve request response logs", e);
		}
	}

	/**
	 * Retrieves a list of vendor provisioning status ids by vendor adapter id
	 * and error code and date range from the vps database table, then builds
	 * the VendorProvisioningStatusBean list and returns the list. Since this
	 * search can potentially pull thousands of records, this method also
	 * requires a max resultset size restriction. If either vendor id name or
	 * error code are null then those values will be omitted from the search.
	 * Throws IllegalArgumentException if max date is less than min date or if
	 * either date value is null. Throws IllegalArgumentException if BOTH vendor
	 * id and error code are null.
	 * 
	 * @param vendorId
	 *            the vendor adapter id, can be null
	 * @param errorCode
	 *            the vendor error code
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of VendorProvisioningStatusBean
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public static List<VendorProvisioningStatusBean> getVPSListByVendorIdErrorCode(
			final String vendorId, final String errorCode, final Date min,
			final Date max, final int sizeLimit)
			throws DataSourceLookupException, DataAccessException {
		if (vendorId == null && errorCode == null) {
			throw new DataAccessException(
					"vendor id and error code cannot be null.");
		} else if (min == null || max == null || max.compareTo(min) < 0) {
			throw new DataAccessException(MAX_DATE_LESS_THAN_MIN);
		} else {
			return getSimpleVendorProvisioningStatusListByVpsIdList(getVendorProvisioningStatusIdListByVendorIdAndErrorCode(
					vendorId, errorCode, min, max, sizeLimit));
		}
	}

	/**
	 * Retrieves a list of vps ids by vendor id and error code and date range in
	 * descending order, sorted by record creation date. This method also
	 * requires a max resultset size restriction.
	 * 
	 * @param vendorId
	 *            the vendor adapter id, can be null
	 * @param errorCode
	 *            the vendor error code
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of OpsSubscriptionAbstractionBean
	 * @throws SQLException
	 */
	private static List<Integer> getVendorProvisioningStatusIdListByVendorIdAndErrorCode(
			final String vendorId, final String errorCode, final Date min,
			final Date max, final int sizeLimit)
			throws DataSourceLookupException, DataAccessException {

		final String sqlCommand = "select ROWNUMBER from (select VENDOR_PROVISIONING_STATUS.ROWNUMBER from VENDOR_PROVISIONING_STATUS where "
				+ (vendorId != null ? "VENDOR_PROVISIONING_STATUS.VNDR_ID=? and "
						: " ")
				+ (errorCode != null ? "VENDOR_PROVISIONING_STATUS.STATUS_CODE=? and "
						: " ")
				+ "VENDOR_PROVISIONING_STATUS.CREATED between ? and ? order by VENDOR_PROVISIONING_STATUS.CREATED desc) where rownum <= ?";

		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int index = 0;
					// dynamic index value in case that vendor id and error code
					// parameters are omitted from the query.
					if (vendorId != null) {
						stmt.setString(++index, vendorId);
					}
					if (errorCode != null) {
						stmt.setString(++index, errorCode);
					}
					stmt.setTimestamp(++index, getSqlDateTime(min));
					stmt.setTimestamp(++index, getSqlDateTime(max));
					stmt.setInt(++index, sizeLimit);
					// if (logger.isDebugEnabled()) {
					// logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg1);
					// }
					final ResultSet rs = stmt.executeQuery();
					final List<Integer> returnValue = new ArrayList<Integer>();

					try {
						while (rs.next()) {
							returnValue.add(rs.getInt(1));
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
					"Failed to retrieve vendor provisioning status record", e);
		}
	}

	/**
	 * Retrieves a list of VendorProvisioningStatusBean from the database
	 * matching the order id list. If a record is not found that matches an id
	 * provided then nothing will be added to the returned list. If no records
	 * are found an empty list is returned.
	 * 
	 * @param arg
	 *            list of vendor provisioning status ids
	 * @return VendorProvisioningStatusBean list
	 * @throws SQLException
	 */
	private static List<VendorProvisioningStatusBean> getSimpleVendorProvisioningStatusListByVpsIdList(
			final List<Integer> args) throws DataSourceLookupException,
			DataAccessException {
		final List<VendorProvisioningStatusBean> returnValue = new ArrayList<VendorProvisioningStatusBean>();
		final Connection conn = getConnection();
		try {
			final int size = args.size();
			for (int i = 0; i < size; i++) {
				final VendorProvisioningStatusBean bean = getVendorProvisioningStatusBeanResultByVendorProvisioningStatusId(
						args.get(i).toString(), conn);
				if (bean != null) {
					returnValue.add(bean);
				} else {
					throw new DataAccessException(
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

	/*
	 * 
	 */
	private static VendorProvisioningStatusBean getVendorProvisioningStatusBeanResultByVendorProvisioningStatusId(
			final String arg, final Connection conn)
			throws DataAccessException, DataSourceLookupException {

		final String sqlCommand = "	select vendor_provisioning_status.rownumber, vendor_provisioning_status.vndr_key_id"
				+ ", vendor_provisioning_status.csm_id, vendor_provisioning_status.vndr_id, vendor_provisioning_status.sdp_id"
				+ ", vendor_provisioning_status.req_type, vendor_provisioning_status.status_code, vendor_provisioning_status.status"
				+ ", vendor_provisioning_status.created, sdp_vendor_status_code.MESSAGE, SDP_VENDOR_CONFIG.VNDR_NM from vendor_provisioning_status"
				+ " left join sdp_vendor_status_code on vendor_provisioning_status.vndr_id = sdp_vendor_status_code.vndr_id and vendor_provisioning_status.status_code = sdp_vendor_status_code.VNDR_CODE"
				+ " left join SDP_VENDOR_CONFIG on SDP_VENDOR_CONFIG.VNDR_ID= vendor_provisioning_status.VNDR_ID "
				+ "where vendor_provisioning_status.rownumber = ?";
		try {
			final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			try {
				stmt.setString(1, arg);
				logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + arg);
				final ResultSet rs = stmt.executeQuery();
				try {
					final VendorProvisioningStatusBean output = new VendorProvisioningStatusBean();
					if (rs.next()) {
						output.setVpsId(rs.getString(1));
						output.setSerialNumber(rs.getString(2));
						output.setSdpOrderId(rs.getString(3));
						output.setVendorId(rs.getString(4));
						output.setSdpId(rs.getString(5));
						output.setRequestType(rs.getString(6));
						output.setResponseCode(rs.getString(7));
						output.setStatus(rs.getString(8));
						output.setCreatedDate(rs.getTimestamp(9));
						output.setDescription(rs.getString(10));
						output.setVendorName(rs.getString(11));
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

		} catch (SQLException e) {
			throw new DataAccessException(
					"Failed to retrieve recent sdp orders", e);
		}

	}

}
