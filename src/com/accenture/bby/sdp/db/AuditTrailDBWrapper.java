package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.audit.FieldLog;
import com.accenture.bby.sdp.utl.audit.TransactionLog;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailDataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.AuditLog;
import com.accenture.bby.sdp.web.beans.FieldAnalysis;
import com.accenture.bby.sdp.web.beans.SimpleAuditLog;
import com.accenture.bby.sdp.web.beans.XmlAnalysis;

public class AuditTrailDBWrapper extends SomSdpDBWrapper {

	private static final Logger logger = Logger.getLogger(AuditTrailDBWrapper.class.getName());
	
	/**
	 * This method will return the next value of the sequence from the DB. It
	 * takes the table name for which the sequence should be generated as
	 * argument.
	 * 
	 * @throws AuditTrailDataAccessException
	 */
	private static int getNextIdFromSeq(String seqName, Connection conn) throws AuditTrailDataAccessException {
		final String sqlCommand = "SELECT " + seqName + ".nextval FROM DUAL";
		int result = 0;
		try {
			final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (rs.next()) {
						result = rs.getInt(1);
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
		} catch (SQLException e) {
			throw new AuditTrailDataAccessException("Failed to retrieve sequence value from database.", e);
		}
		return result;
	}

	public static void insertSession(String trackingId, String userId, String role, Date loggedInTime, Date loggedOutTime) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "INSERT INTO AUD_SESS (AUD_SESS_ID, REF_SYS_ID, USR_ID, USR_ROLE_TXT, SESS_BEG_TS, SESS_STOP_TS,  REC_CRT_TS, REC_CRT_USR_ID, REC_UPD_TS, REC_UPD_USR_ID ) VALUES (?, 2, ?, ?, ?, ?,  SYSDATE, ?, SYSDATE, ?)";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, TextFilter.filter(trackingId));
					stmt.setString(2, TextFilter.filter(userId));
					// if role > 50 characters then use substring of first 50
					// chars for DB column size constraint
					stmt.setString(3, (role != null && role.length() > 50 ? TextFilter.filter(role.substring(0, 50)) : TextFilter.filter(role)));
					stmt.setTimestamp(4, getConvertedTimestamp(loggedInTime));
					stmt.setTimestamp(5, getConvertedTimestamp(loggedOutTime));
					stmt.setString(6, TextFilter.filter(userId));
					stmt.setString(7, TextFilter.filter(userId));
					stmt.execute();
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
			throw new AuditTrailDataAccessException("Failed to insert session log in audit trail.", e);
		}
	}

	/**
	 * Insert details of the audit transaction
	 * 
	 * @param auditLog
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static void insertAuditLogTransaction(TransactionLog auditLog) throws DataAccessException, DataSourceLookupException {
		final Connection conn = getConnection();
		try {
			auditLog.setAudTransId(getNextIdFromSeq("AUD_TRANS_ID_SEQ", conn));

			final String sqlCommand = "INSERT INTO AUD_TRAN (AUD_TRANS_ID, AUD_SESS_ID, REF_AUD_ACTN_ID, TRANS_BEG_TS, TRANS_END_TS, USR_ID, LN_ITEM_ID, VNDR_KEY, CNTRCT_ID, VNDR_ID, MSTR_ITM_ID, PRM_SKU_ID, REC_CRT_TS, REC_CRT_USR_ID ) "
					+ "VALUES (?, ?, ?, SYSDATE, SYSDATE, ?, ?, ?, ?, ?,?,?, SYSDATE, ?)";
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				;
				try {

					stmt.setInt(1, auditLog.getAudTransId());
					stmt.setString(2, auditLog.getSessionId());
					stmt.setInt(3, auditLog.getAction().getFieldTypeId());
					stmt.setString(4, TextFilter.filter(auditLog.getUserId()));
					stmt.setString(5, TextFilter.filter(auditLog.getLineItemId()));
					stmt.setString(6, TextFilter.filter(auditLog.getVendorKey()));
					stmt.setString(7, TextFilter.filter(auditLog.getContractId()));
					stmt.setString(8, TextFilter.filter(auditLog.getVendorId()));
					stmt.setString(9, TextFilter.filter(auditLog.getMasterItemId()));
					stmt.setString(10, TextFilter.filter(auditLog.getPrimarySku()));
					stmt.setString(11, TextFilter.filter(auditLog.getUserId()));

					stmt.execute();
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
				// if audit fields are captured then log them
				if (auditLog.getAuditLogs() != null && auditLog.getAuditLogs().size() > 0) {
					insertAuditLogFieldValues(auditLog, conn);
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new AuditTrailDataAccessException("Failed to insert transaction log in audit trail.", e);
		}
	}

	/**
	 * Insert new and original form field values for the transaction
	 * 
	 * @param sessionLog
	 * @param conn
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	private static void insertAuditLogFieldValues(TransactionLog sessionLog, Connection conn) throws DataAccessException {
		if (sessionLog != null && sessionLog.getAuditLogs() != null) {

			String sqlCommand = "INSERT INTO AUD_DTA_FLD (AUDIT_DTA_FLD_ID, AUD_TRANS_ID, REF_FLD_TYP_ID,  OLD_VAL, NEW_VAL, CREATED_TS, CHG_ANAL_TXT, FLD_SORT_PRTY_NBR,  REC_CRT_TS, REC_CRT_USR_ID )  VALUES (?, ?, ?, ?, ?, ?, ?, ?,  SYSDATE, ?)";
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					for (FieldLog auditLog : sessionLog.getAuditLogs()) {

						stmt.setInt(1, getNextIdFromSeq("AUDIT_DTA_FLD_ID_SEQ", conn));
						stmt.setInt(2, sessionLog.getAudTransId());
						stmt.setInt(3, auditLog.getFieldId());
						stmt.setString(4, auditLog.getOldValue());
						stmt.setString(5, auditLog.getNewValue());
						stmt.setTimestamp(6, getConvertedTimestamp(auditLog.getCreatedDate()));
						stmt.setString(7, auditLog.getChangeAnalysis());
						stmt.setInt(8, auditLog.getPriority());
						stmt.setString(9, sessionLog.getUserId());

						stmt.execute();
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} catch (SQLException e) {
				throw new AuditTrailDataAccessException("Failed to insert field value log in audit trail.", e);
			}
		}
	}

	public static void insertAuditXml(TransactionLog auditLog, String currentRequest, String currentResponse, String originalRequest) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "INSERT INTO AUD_DTA_XML (AUD_DTA_XML_ID, AUD_TRANS_ID, CURR_XML_TXT, UPD_XML_TXT, RESP_XML_TXT, CREATED_TS, REC_CRT_TS, REC_CRT_USR_ID) VALUES(?, ?, ?, ?, ?, SYSDATE, SYSDATE, ?)";
		try {
			final Connection conn = getConnection();
			try {
				final int id = getNextIdFromSeq("AUD_DTA_XML_ID_SEQ", conn);
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					stmt.setInt(2, auditLog.getAudTransId());
					stmt.setString(3, TextFilter.filter(originalRequest));
					stmt.setString(4, TextFilter.filter(currentRequest));
					stmt.setString(5, TextFilter.filter(currentResponse));
					stmt.setString(6, TextFilter.filter(auditLog.getUserId()));

					stmt.execute();

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
			throw new AuditTrailDataAccessException("Failed to insert xml request response log.", e);
		}
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Queries the
	 * database for audit logs that fit all of the specified parameters. All
	 * parameters except for displayLimit are nullable.
	 * 
	 * @param contractId
	 * @param vendorKey
	 * @param lineItemId
	 * @param actionId
	 * @param start
	 * @param end
	 * @param userId
	 * @param logId
	 * @param vendorId
	 * @param primarySku
	 * @param masterItemId
	 * @param displayLimit
	 * @return list of SimpleAuditLogs to be displayed in search results table
	 * @throws SQLException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static List<SimpleAuditLog> getSimpleAuditLogsByAll(String contractId, String vendorKey, String lineItemId, Integer actionId, Date start, Date end, String userId, Integer logId,
			String vendorId, String primarySku, String masterItemId, int displayLimit) throws DataAccessException, DataSourceLookupException {

		StringBuilder sqlCommand = new StringBuilder();

		// build dynamic sql query based on input values
		sqlCommand.append("select aud_trans_id from (select aud_trans_id from aud_tran ");
		List<String> params = new ArrayList<String>();

		// omit null values from the query
		if (isNotNull(contractId)) {
			params.add(" cntrct_id=? ");
		}
		if (isNotNull(vendorKey)) {
			params.add(" vndr_key=? ");
		}
		if (isNotNull(lineItemId)) {
			params.add(" ln_item_id=? ");
		}
		if (actionId != null) {
			params.add(" ref_aud_actn_id=? ");
		}
		if (start != null) {
			params.add(" trans_beg_ts>=? ");
		}
		if (end != null) {
			params.add(" trans_beg_ts<=? ");
		}
		if (isNotNull(userId)) {
			params.add(" usr_id=? ");
		}
		if (logId != null) {
			params.add(" aud_trans_id=? ");
		}
		if (isNotNull(vendorId)) {
			params.add(" vndr_id=? ");
		}
		if (isNotNull(primarySku)) {
			params.add(" prm_sku_id=? ");
		}
		if (isNotNull(masterItemId)) {
			params.add(" mstr_itm_id=? ");
		}

		// if no parameter then don't append 'where'
		// if last parameter then don't append 'and'
		int s = params.size();
		for (int i = 0; i < s; i++) {
			if (i == 0) {
				sqlCommand.append(" where ");
			}
			sqlCommand.append(params.get(i));
			if (i < s - 1) {
				sqlCommand.append(" and ");
			}
		}

		// finish query
		sqlCommand.append(" order by aud_trans_id desc) where rownum<=?");

		final List<Integer> ids = new ArrayList<Integer>();
		try {
			final Connection conn = getConnection();
			try {
				logger.log(Level.DEBUG, "SQL Command >> " + sqlCommand.toString());
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand.toString());
				try {

					if (logger.isDebugEnabled()) {
						StringBuilder builder = new StringBuilder();
						int index = 0;
						if (isNotNull(contractId )) {
							builder.append("\n?[" + (index++) + "=" + contractId);
						}
						if (isNotNull(vendorKey)) {
							builder.append("\n?[" + (index++) + "=" + vendorKey);
						}
						if (isNotNull(lineItemId)) {
							builder.append("\n?[" + (index++) + "=" + lineItemId);
						}
						if (actionId != null) {
							builder.append("\n?[" + (index++) + "=" + actionId);
						}
						if (start != null) {
							builder.append("\n?[" + (index++) + "=" + new java.sql.Date(start.getTime()));
						}
						if (end != null) {
							builder.append("\n?[" + (index++) + "=" + new java.sql.Date(end.getTime()));
						}
						if (isNotNull(userId)) {
							builder.append("\n?[" + (index++) + "=" + userId);
						}
						if (logId != null) {
							builder.append("\n?[" + (index++) + "=" + logId);
						}
						if (isNotNull(vendorId)) {
							builder.append("\n?[" + (index++) + "=" + vendorId);
						}
						if (isNotNull(primarySku )) {
							builder.append("\n?[" + (index++) + "=" + primarySku);
						}
						if (isNotNull(masterItemId)) {
							builder.append("\n?[" + (index++) + "=" + masterItemId);
						}
						builder.append("\n?[" + (index++) + "=" + displayLimit);
						logger.log(Level.DEBUG, "SQL Params >> " + builder.toString());
					}
					
					int index = 1;
					if (isNotNull(contractId)) {
						stmt.setString(index++, contractId.trim());
					}
					if (isNotNull(vendorKey)) {
						stmt.setString(index++, vendorKey.trim());
					}
					if (isNotNull(lineItemId)) {
						stmt.setString(index++, lineItemId.trim());
					}
					if (actionId != null) {
						stmt.setInt(index++, actionId);
					}
					if (start != null) {
						stmt.setDate(index++, new java.sql.Date(start.getTime()));
					}
					if (end != null) {
						stmt.setDate(index++, new java.sql.Date((AuditTrailDBWrapper.getNextDay(end)).getTime()));
					}
					if (isNotNull(userId)) {
						stmt.setString(index++, userId.trim());
					}
					if (logId != null) {
						stmt.setInt(index++, logId);
					}
					if (isNotNull(vendorId)) {
						stmt.setString(index++, vendorId.trim());
					}
					if (isNotNull(primarySku)) {
						stmt.setString(index++, primarySku.trim());
					}
					if (isNotNull(masterItemId)) {
						stmt.setString(index++, masterItemId.trim());
					}
					stmt.setInt(index, displayLimit);

					final ResultSet rs = stmt.executeQuery();
					try {
						while (rs.next()) {
							ids.add(rs.getInt(1));
						}
						logger.log(Level.DEBUG, "Results returned=" + ids.size());
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
			throw new DataAccessException("Failure while retrieving audit trail log IDs.", e);
		}

		final List<SimpleAuditLog> simpleAuditLogs = new ArrayList<SimpleAuditLog>();

		for (int id : ids) {
			simpleAuditLogs.add(getSimpleAuditLogByAudTransId(id));
		}
		
		logger.log(Level.DEBUG, "Simple Audit Logs returned=" + simpleAuditLogs.size());

		return simpleAuditLogs;
	}

	private static Date getNextDay(Date date) {
		return date == null ? null : new Date(date.getTime() + 1000 * 60 * 60 * 24);
	}
	
	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves a
	 * SimpleAuditLog associated with the specified audit trans id
	 * 
	 * @param id
	 *            audit transaction id
	 * @return SimpleAuditLog
	 * @throws SQLException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	private static SimpleAuditLog getSimpleAuditLogByAudTransId(int id) throws DataSourceLookupException, DataAccessException {
		// get transaction details
		SimpleAuditLog log = getSimpleSessionTransDataByTransId(id);
		// get field attribute values
		HashMap<String, String> attributes = getSimpleAuditLogAttributesByTransId(id);
		// map attributes to SimpleAuditLog
		log.setContractId(attributes.get("Contract ID"));
		log.setSerialNumber(attributes.get("Serial Number"));
		log.setLineItemId(attributes.get("Line Item ID"));
		log.setTransactionDate(attributes.get("Transaction Date"));
		log.setStoreId(attributes.get("Store ID"));
		log.setRegisterId(attributes.get("Register ID"));
		log.setTransactionId(attributes.get("Transaction ID"));
		log.setLineId(attributes.get("Line ID"));
		log.setPrimarySku(attributes.get("Product Sku"));
		log.setRelatedSku(attributes.get("Plan Sku"));
		log.setFirstName(attributes.get("First Name"));
		log.setLastName(attributes.get("Last Name"));
		log.setDeliveryEmail(attributes.get("Delivery Email"));
		return log;
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves an
	 * AuditLog associated with the specified audit trans id
	 * 
	 * @param id
	 *            audit transaction id
	 * @return AuditLog
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public static AuditLog getAuditLogByAudTransId(int id) throws DataAccessException, DataSourceLookupException {
		// get transaction details
		AuditLog log = getSessionTransDataByTransId(id);
		log.setFieldAnalysisList(getFieldAnalysisByTransId(id));
		log.setXmlAnalysisList(getXmlAnalysisByTransId(id));
		log.setRelatedAuditLogs(getSimpleAuditLogsByAny(log.getContractId(), log.getSerialNumber(), log.getLineItemId()));
		return log;
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves
	 * necessary transaction data to be displayed in Audit Trail search results
	 * table.
	 * 
	 * @param id
	 *            audit transaction id
	 * @return SimpleAuditLog with logId, userId, Action, and Date values set.
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 * @throws SQLException
	 */
	private static SimpleAuditLog getSimpleSessionTransDataByTransId(int id) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select aud_sess.usr_id, aud_actn.actn_nm, aud_tran.trans_beg_ts from aud_sess, aud_tran, aud_actn where aud_tran.aud_sess_id = aud_sess.aud_sess_id and aud_tran.ref_aud_actn_id = aud_actn.ref_aud_actn_id and aud_trans_id=?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					try {
						final SimpleAuditLog log = new SimpleAuditLog();
						if (rs.next()) {
							log.setUserId(rs.getString(1));
							log.setAction(rs.getString(2));
							log.setDate(new Date(rs.getTimestamp(3).getTime()));
							log.setLogId(id + "");
						}
						return log;
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
			throw new DataAccessException("Failed to retrieve audlit log session data.", e);
		}
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves
	 * necessary transaction data to be displayed in Audit Trail search results
	 * table.
	 * 
	 * @param id
	 *            audit transaction id
	 * @return AuditLog with logId, userId, Action, Contract ID, Vendor Key,
	 *         Line Item ID, and Date values set.
	 * @throws SQLException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	private static AuditLog getSessionTransDataByTransId(int id) throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "select aud_sess.usr_id, aud_actn.actn_nm, aud_tran.trans_beg_ts, aud_tran.cntrct_id, aud_tran.vndr_key, aud_tran.ln_item_id from aud_sess, aud_tran, aud_actn where aud_tran.aud_sess_id = aud_sess.aud_sess_id and aud_tran.ref_aud_actn_id = aud_actn.ref_aud_actn_id and aud_trans_id=?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					try {
						final AuditLog log = new AuditLog();
						if (rs.next()) {
							log.setUserId(rs.getString(1));
							log.setAction(rs.getString(2));
							log.setDate(new Date(rs.getTimestamp(3).getTime()));
							log.setContractId(rs.getString(4));
							log.setSerialNumber(rs.getString(5));
							log.setLineItemId(rs.getString(6));
							log.setId(id + "");
						}
						return log;
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
			throw new DataAccessException("Failed to retrieve audit trail transaction data.", e);
		}
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves
	 * field data for the specified transaction id
	 * 
	 * @param id
	 *            audit transaction id
	 * @return HashMap<String, String> of field attributes
	 * @throws SQLException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	private static HashMap<String, String> getSimpleAuditLogAttributesByTransId(int id) throws DataAccessException, DataSourceLookupException {

		final String sqlCommand = "select aud_fld_typ.fld_typ_nm, aud_dta_fld.new_val from aud_dta_fld, aud_fld_typ where aud_dta_fld.ref_fld_typ_id = aud_fld_typ.ref_fld_typ_id and aud_dta_fld.aud_trans_id=?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					try {
						final HashMap<String, String> attributes = new HashMap<String, String>();
						while (rs.next()) {
							attributes.put(rs.getString(1), rs.getString(2));
						}
						return attributes;
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
			throw new DataAccessException("Failed to retrieve audit trail attributes.", e);
		}
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Queries the
	 * database for audit logs that fit any of the specified parameters. All
	 * parameters except for displayLimit are nullable. if all parameters are
	 * null then an empty list is returned.
	 * 
	 * @param contractId
	 * @param vendorKey
	 * @param lineItemId
	 * @return list of SimpleAuditLog to display related audit logs
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 * @throws SQLException
	 */
	public static List<SimpleAuditLog> getSimpleAuditLogsByAny(String contractId, String vendorKey, String lineItemId) throws DataSourceLookupException, DataAccessException {
		final List<SimpleAuditLog> simpleAuditLogs = new ArrayList<SimpleAuditLog>();

		// if all parameters are null then return empty list.
		if (!isNotNull(contractId) && !isNotNull(vendorKey) && !isNotNull(lineItemId)) {
			return simpleAuditLogs;
		}

		StringBuilder sqlCommand = new StringBuilder();
		sqlCommand.append("select aud_trans_id from aud_tran where ");

		if (isNotNull(contractId)) {
			sqlCommand.append("cntrct_id=? " + ((!isNotNull(vendorKey) && !isNotNull(lineItemId)) ? "" : "or "));
		}
		if (isNotNull(vendorKey)) {
			sqlCommand.append("vndr_key=? " + ((!isNotNull(lineItemId )) ? "" : "or "));
		}
		if (isNotNull(lineItemId)) {
			sqlCommand.append("ln_item_id=?");
		}
		sqlCommand.append(" order by rec_crt_ts desc ");

		final List<Integer> ids = new ArrayList<Integer>();
		try {
			final Connection conn = getConnection();

			try {

				final PreparedStatement stmt = conn.prepareStatement(sqlCommand.toString());
				try {
					int index = 1;
					if (isNotNull(contractId)) {
						stmt.setString(index++, contractId);
					}
					if (isNotNull(vendorKey )) {
						stmt.setString(index++, vendorKey);
					}
					if (isNotNull(lineItemId)) {
						stmt.setString(index, lineItemId);
					}

					final ResultSet rs = stmt.executeQuery();
					try {
						while (rs.next()) {
							ids.add(rs.getInt(1));
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
			throw new DataAccessException("Failed to retrieve related audit trail logs.", e);
		}

		for (int id : ids) {
			simpleAuditLogs.add(getSimpleAuditLogByAudTransId(id));
		}
		return simpleAuditLogs;
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves
	 * field analysis data for the specified transaction id
	 * 
	 * @param id
	 *            audit transaction id
	 * @return List<FieldAnalysis>
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 * @throws SQLException
	 */
	private static List<FieldAnalysis> getFieldAnalysisByTransId(int id) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select aud_fld_typ.FLD_TYP_NM, aud_dta_fld.OLD_VAL, aud_dta_fld.NEW_VAL, aud_dta_fld.CHG_ANAL_TXT from aud_fld_typ, aud_dta_fld where aud_fld_typ.REF_FLD_TYP_ID = aud_dta_fld.REF_FLD_TYP_ID and aud_dta_fld.AUD_TRANS_ID=? order by aud_dta_fld.FLD_SORT_PRTY_NBR asc";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<FieldAnalysis> fields = new ArrayList<FieldAnalysis>();
						while (rs.next()) {
							FieldAnalysis field = new FieldAnalysis();
							field.setFieldLabel(rs.getString(1));
							field.setOriginalValue(rs.getString(2));
							field.setNewValue(rs.getString(3));
							field.setChangeAnalysis(rs.getString(4));
							fields.add(field);
						}
						return fields;
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
			throw new DataAccessException("Failed to retrieve audit trail field analysis list.", e);
		}
	}

	/**
	 * First implemented: SDP OpsUI Audit Trail - Ask an Agent R2b Retrieves
	 * field analysis data for the specified transaction id
	 * 
	 * @param id
	 *            audit transaction id
	 * @return List<XmlAnalysis>
	 * @throws SQLException
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	private static List<XmlAnalysis> getXmlAnalysisByTransId(int id) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select aud_dta_xml.CURR_XML_TXT, aud_dta_xml.UPD_XML_TXT, aud_dta_xml.RESP_XML_TXT, aud_dta_xml.CHG_ANAL_TXT from aud_dta_xml where aud_trans_id=?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<XmlAnalysis> messages = new ArrayList<XmlAnalysis>();
						while (rs.next()) {
							XmlAnalysis xml = new XmlAnalysis();
							xml.setCurrentXml(rs.getString(1));
							xml.setUpdatedXml(rs.getString(2));
							xml.setResponseXml(rs.getString(3));
							xml.setChangeAnalysis(rs.getString(4));
							messages.add(xml);
						}
						return messages;
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
			throw new DataAccessException("Failed to audit trail xml analysis list.", e);
		}
	}

}
