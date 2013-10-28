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
import com.accenture.bby.sdp.web.beans.ExceptionResultBean;

public class ExceptionPageDBWrapper extends SomSdpDBWrapper{

	private static final Logger logger = Logger
			.getLogger(ExceptionPageDBWrapper.class.getName());

	private static final String MAX_DATE_LESS_THAN_MIN = "Max date was less than min date.";

	
	/**
	 * Retrieves a list of recent sdp exception ids from the sdp exception
	 * database table filtering on exception group, then builds the SdpExceptionBean list and returns the
	 * list
	 * 
	 * @param arg
	 *            maximum records to return
	 * @param maxDays
	 * 			  maximum number of days to search back from sysdate
	 * @return list of SdpExceptionBean
	 * @throws SQLException
	 */
	public static List<ExceptionResultBean> getSimpleRecentSdpExceptionListByMaxLimit(final int arg, final String group, final int maxDays) 
	throws DataAccessException, DataSourceLookupException {
		return getSimpleSdpExceptionListByExceptionIdList(getRecentSdpExceptionIdListByMaxLimit(arg, group, maxDays));
	}

	private static List<String> getRecentSdpExceptionIdListByMaxLimit(final int arg, final String group, final int maxDays)
	throws DataAccessException, DataSourceLookupException  {

		String sqlCommand;
		if (group == null) {
			sqlCommand = "select ROW_NUM from (select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM from SDP_EXCEPTION_MESSAGE_LOG where SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS > sysdate-(?) order by SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS desc) where rownum<=?";
		} else {
			sqlCommand = "select ROW_NUM from (select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM from SDP_EXCEPTION_MESSAGE_LOG inner join SDP_ERROR_CODE_REF on SDP_ERROR_CODE_REF.ERR_CDE=SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE where SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS > sysdate-(?) and SDP_ERROR_CODE_REF.ERR_GRP=? order by SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS desc) where rownum<=?";
		}
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int index = 1;
					stmt.setInt(index++, maxDays);
					if (group != null ) {
						stmt.setString(index++, group);
					}
					stmt.setInt(index++, arg);
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
	 * Retrieves a list of recent sdp exception ids from the sdp exception
	 * database table, then builds the SdpExceptionBean list and returns the
	 * list
	 * 
	 * @param arg
	 *            maximum records to return
	 * @param maxDays
	 * 			  maximum number of days to search back from sysdate
	 * @return list of SdpExceptionBean
	 * @throws SQLException
	 */
	public static List<ExceptionResultBean> getSimpleRecentSdpExceptionListByMaxLimit(final int arg, final int maxDays) 
	throws DataAccessException, DataSourceLookupException {
		return getSimpleSdpExceptionListByExceptionIdList(getRecentSdpExceptionIdListByMaxLimit(arg, null, maxDays));
	}
	/**
	 * Retrieves a list of sdp exception ids by SDPID from the sdp exception
	 * database table, then builds the SdpExceptionBean list and returns the
	 * list
	 * 
	 * @param arg
	 *            the SDPID
	 * @return list of SdpExceptionBean
	 * @throws SQLException
	 */
	public static List<ExceptionResultBean> getSimpleSdpExceptionListBySdpId(
			final String arg) throws DataAccessException,
			DataSourceLookupException {
		 if (arg == null) {
		 throw new DataAccessException("SdpId is Missing");
		 }
		 return	getSimpleSdpExceptionListByExceptionIdList(getSdpExceptionIdListBySdpId(arg));
	}
	
	/**
	 * Retrieves a list of sdp exception ids by SDPID in descending order, sorted by
	 * record creation date.
	 * 
	 * @param arg
	 *            the SDPID
	 * @return list of ids
	 * @throws SQLException
	 */
	private static List<String> getSdpExceptionIdListBySdpId(final String arg)
	throws DataAccessException, DataSourceLookupException  {

		final String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM from SDP_EXCEPTION_MESSAGE_LOG where SDP_EXCEPTION_MESSAGE_LOG.SDP_ID=? order by SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS desc";

		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
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
	 * Retrieves a list of sdp exception ids by date range from the sdp
	 * exception database table, then builds the SdpExceptionBean list and
	 * returns the list. Since this search can potentially pull thousands of
	 * records, this method also requires a max resultset size restriction.
	 * Throws IllegalArgumentException if max date is less than min date or if
	 * either date value is null.
	 * 
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of SdpExceptionBean
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public static List<ExceptionResultBean> getSimpleSdpExceptionListByDateRange(
			final Date min, final Date max, final int sizeLimit)
			throws DataAccessException, DataSourceLookupException {
		if (min == null || max == null || max.compareTo(min) < 0) {
			throw new DataAccessException(MAX_DATE_LESS_THAN_MIN);
		} else {
			return getSimpleSdpExceptionListByExceptionIdList(getSdpExceptionIdListByDateRange(
					min, max, sizeLimit));
		}
	}
	
	/**
	 * Retrieves a list of sdp exceptions ids by date range in descending order,
	 * sorted by record creation date. This method also requires a max resultset
	 * size restriction.
	 * 
	 * @param min
	 *            date range start boundary (inclusive)
	 * @param max
	 *            date range end boundary (inclusive)
	 * @param sizeLimit
	 *            max number of records to return
	 * @return list of ids
	 * @throws SQLException
	 */
	private static List<String> getSdpExceptionIdListByDateRange(final Date min, final Date max, final int sizeLimit) throws DataAccessException, DataSourceLookupException  {

		final String sqlCommand = "select ROW_NUM from (select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM from SDP_EXCEPTION_MESSAGE_LOG where SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS between ? and ? order by SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS desc) where rownum <= ?";

		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setDate(1, getSqlDate(min));
					stmt.setDate(2, getSqlDate(max));
					stmt.setInt(3, sizeLimit);
					logger.log(Level.DEBUG, "PARAMS:\n?[0]=" + min
							+ "\n?[1]=" + max);
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
	 * Retrieves a list of SdpExceptionBean from the database matching the
	 * exception id list. If a record is not found that matches an id provided
	 * then nothing will be added to the returned list. If no records are found
	 * an empty list is returned.
	 * 
	 * @param arg
	 *            list of SDP order ids
	 * @return SdpExceptionBean list
	 * @throws SQLException
	 */
	private static List<ExceptionResultBean> getSimpleSdpExceptionListByExceptionIdList(final List<String> arg) 
	throws DataAccessException, DataSourceLookupException {
		final List<ExceptionResultBean> returnValue = new ArrayList<ExceptionResultBean>();

		final int size = arg.size();
		for (int i = 0; i < size; i++) {
			final ExceptionResultBean bean = getSimpleSdpExceptionByExceptionId(arg.get(i));
			if (bean != null) {
				returnValue.add(bean);
			} else {
				throw new IllegalStateException("Failed to retrieve record with id=" + i);
			}
		}
		return returnValue;
	}
	
	/**
	 * Retrieves a single SdpExceptionBean from the database matching the
	 * exception id. Although the values in this column should be unique, this
	 * method will return the first record found the result set, if any.
	 * 
	 * This method follows the Lazy Loading pattern and will return
	 * SdpExceptionBean populated with ONLY enough data to populate the search
	 * results screen with the required data. This method should NOT be used to
	 * pull the full detail of the exception (see
	 * retrieveSdpExceptionDetailByExceptionId()).
	 * 
	 * @param arg
	 *            SDP order id
	 * @return SdpExceptionBean or null if no result found
	 * @throws SQLException
	 */
	private static ExceptionResultBean getSimpleSdpExceptionByExceptionId(final String arg) throws DataAccessException, DataSourceLookupException  {

		//String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM, SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS, SDP_EXCEPTION_MESSAGE_LOG.SRC_SYS_ID, SDP_EXCEPTION_MESSAGE_LOG.SDP_ID, SDP_STATUS_MASTER.STAT_GRP||' / '||SDP_STATUS_MASTER.STAT_DESC from SDP_EXCEPTION_MESSAGE_LOG left join SDP_STATUS_MASTER on SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE=SDP_STATUS_MASTER.STAT_CDE where SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM=?";
		final String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM, SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS," +
				" SDP_EXCEPTION_MESSAGE_LOG.SRC_SYS_ID, SDP_EXCEPTION_MESSAGE_LOG.SDP_ID, SDP_ERROR_CODE_REF.ERR_CDE, SDP_ERROR_CODE_REF.ERR_DESC" +
				" from SDP_EXCEPTION_MESSAGE_LOG left join SDP_ERROR_CODE_REF on SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE=SDP_ERROR_CODE_REF.ERR_CDE" +
				" where SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM=?";

		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
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
						ExceptionResultBean returnValue = new ExceptionResultBean();
						if (rs.next()) {
							returnValue.setExceptionId(rs.getString(1));
							returnValue.setCreatedDate(getUtilDate(rs.getTimestamp(2)));
							returnValue.setSourceSystem(rs.getString(3));
							returnValue.setSdpId(rs.getString(4));
							returnValue.setCode(rs.getString(5));
							returnValue.setDescription(rs.getString(6));
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
	 * Retrieves a single, more detailed SdpExceptionBean from the database
	 * matching the exception id. Although the values in this column should be
	 * unique, this method will return the first record found the result set, if
	 * any.
	 * 
	 * The returned SdpExceptionBean will contain as much detail as required to
	 * populate the exception detail screen. This method should NOT be used to
	 * populate the search result screen (see
	 * retrieveSimpleSdpExceptionByExceptionId()).
	 * 
	 * @param arg
	 *            SDP exception id
	 * @return SdpExceptionBean or null if no result found
	 * @throws SQLException
	 */
	public static ExceptionResultBean getSdpExceptionDetailByExceptionId(final String arg) 
	throws DataAccessException, DataSourceLookupException  {

		//String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM, SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS, SDP_EXCEPTION_MESSAGE_LOG.SRC_SYS_ID, SDP_EXCEPTION_MESSAGE_LOG.SDP_ID, SDP_STATUS_MASTER.STAT_GRP||' / '||SDP_STATUS_MASTER.STAT_DESC from SDP_EXCEPTION_MESSAGE_LOG left join SDP_STATUS_MASTER on SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE=SDP_STATUS_MASTER.STAT_CDE where SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM=?";
		final String sqlCommand = "select SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM, SDP_EXCEPTION_MESSAGE_LOG.REC_CRT_TS" +
				", SDP_EXCEPTION_MESSAGE_LOG.SRC_SYS_ID, SDP_EXCEPTION_MESSAGE_LOG.SDP_ID" +
				", SDP_ERROR_CODE_REF.ERR_DESC, SDP_EXCEPTION_MESSAGE_LOG.SDP_ORDER_ID," +
				" SDP_EXCEPTION_MESSAGE_LOG.RQST_MSG, SDP_EXCEPTION_MESSAGE_LOG.STACK_TRC" +
				", SDP_ERROR_CODE_REF.ERR_CDE from SDP_EXCEPTION_MESSAGE_LOG" +
				" left join SDP_ERROR_CODE_REF on SDP_EXCEPTION_MESSAGE_LOG.ERR_CDE=SDP_ERROR_CODE_REF.ERR_CDE" +
				" where SDP_EXCEPTION_MESSAGE_LOG.ROW_NUM=?";
		
		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
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
						ExceptionResultBean returnValue = new ExceptionResultBean();
						if (rs.next()) {
							returnValue.setExceptionId(rs.getString(1));
							returnValue.setCreatedDate(getUtilDate(rs.getTimestamp(2)));
							returnValue.setSourceSystem(rs.getString(3));
							returnValue.setSdpId(rs.getString(4));
							returnValue.setDescription(rs.getString(5));
							returnValue.setSdpOrderId(rs.getString(6));
							returnValue.setRequestMessage(rs.getString(7));
							returnValue.setStackTrace(rs.getString(8));
							returnValue.setCode(rs.getString(9));
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

	public static List<SelectItem> getExceptionGroupDropDownList()
	throws DataAccessException, DataSourceLookupException  {

		final String sqlCommand = "select distinct SDP_ERROR_CODE_REF.ERR_GRP, SDP_ERROR_CODE_REF.ERR_GRP from SDP_ERROR_CODE_REF where SDP_ERROR_CODE_REF.ERR_GRP is not null order by SDP_ERROR_CODE_REF.ERR_GRP asc";

		logger.log(Level.DEBUG, "sqlCommand=" + sqlCommand);
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
}
