package com.accenture.bby.sdp.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KeycodeBatchBean;
import com.accenture.bby.sdp.web.beans.KeycodeBean;

public class KcbBatchLoadDBWrapper extends KcbDBWrapper {

	/**
	 * This method will return the next value of the sequence from the DB.
	 * 
	 * @throws DataSourceLookupException
	 * @throws AuditTrailDataAccessException
	 */
	public static long getNextBatchLoadId() throws DataAccessException,
			DataSourceLookupException {
		final String sqlCommand = "select SEQ_LOAD_ID_SEQ.nextval from DUAL";
		long result = 0L;
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					final ResultSet rs = stmt.executeQuery();
					try {
						if (rs.next()) {
							result = rs.getLong(1);
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
					"Failed to retrieve Batch ID value from database.", e);
		}
		return result;
	}

	/**
	 * 
	 * This method will return an array of productIds from the kcb catalog.
	 * vendorId is mandatory, throws exception if null productSku or
	 * masterItemId is mandatory, throws exception if both are null
	 * 
	 * @param vendorId
	 * @param productSku
	 * @param masterItemId
	 * @return product id array
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static Integer[] getProductId(String vendorId, String productSku,
			String masterItemId) throws DataAccessException,
			DataSourceLookupException {

		if (vendorId == null) {
			throw new DataAccessException("vendorId is null");
		} else if (productSku == null && masterItemId == null) {
			throw new DataAccessException(
					"productSku and masterItemId are null");
		}

		final List<Integer> productsFound = new ArrayList<Integer>();
		final String sqlCommand = "select KCB_PRODUCT.PRODUCTID from KCB_PRODUCT where KCB_PRODUCT.VENDORID=? "
				+ (masterItemId != null ? " and KCB_PRODUCT.MSTR_ITM_ID=? "
						: "")
				+ (productSku != null ? " and KCB_PRODUCT.SKU=? " : "");
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
    				int index = 1;
    				stmt.setString(index++, vendorId);
    				if (masterItemId != null) {
    					stmt.setString(index++, masterItemId);
    				}
    				if (productSku != null) {
    					stmt.setString(index, productSku);
    				}

				
					final ResultSet rs = stmt.executeQuery();
					try {
						while (rs.next()) {
							productsFound.add(rs.getInt(1));
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
					"Failed to retrieve Product ID value from database.", e);
		}
		return productsFound.toArray(new Integer[productsFound.size()]);
	}

	/**
	 * @param keycodeBatchBean
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean insertBatchLoad(
			KeycodeBatchBean keycodeBatchBean) throws DataAccessException,
			DataSourceLookupException {
		if (keycodeBatchBean == null) {
			throw new DataAccessException("KCB Batch kcbBatchBean was null");
		}
		if (keycodeBatchBean.getBatchLoadId() == null) {
			keycodeBatchBean.setBatchLoadId(getNextBatchLoadId());
		}
		final String sqlCommand = "insert into KCB_BATCH_LOAD (LOADID, PRODUCTID, LOAD_TS, "
				+ (keycodeBatchBean.getMinDate() != null ? "BEG_TS, " : "")
				+ (keycodeBatchBean.getMaxDate() != null ? "END_TS, " : "")
				+ " REC_CRT_USR_ID) values (?, ?, sysdate, "
				+ (keycodeBatchBean.getMinDate() != null ? "?, " : "")
				+ (keycodeBatchBean.getMaxDate() != null ? "?, " : "") + "?)";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int i = 0;
					stmt.setLong(++i, keycodeBatchBean.getBatchLoadId());
					stmt.setInt(++i, keycodeBatchBean.getProductId());
					if (keycodeBatchBean.getMinDate() != null) {
						stmt.setTimestamp(++i,
								getConvertedTimestamp(keycodeBatchBean
										.getMinDate()));
					}
					if (keycodeBatchBean.getMaxDate() != null) {
						stmt.setTimestamp(++i,
								getConvertedTimestamp(keycodeBatchBean
										.getMaxDate()));
					}
					stmt.setString(++i, keycodeBatchBean.getCreatedByUserId());
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
			throw new DataAccessException("Failed to insert batch.", e);
		}
		return keycodeBatchBean;
	}

	/**
	 * updates date range by batchload id. dates can be null.
	 * 
	 * @param batchLoadId
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static int updateBatchLoadDateRange(long batchLoadId, Date minDate,
			Date maxDate) throws DataAccessException, DataSourceLookupException {

		final String sqlCommand = "update KCB_BATCH_LOAD set BEG_TS="
				+ (minDate != null ? "?" : "null") + " , END_TS="
				+ (maxDate != null ? "?" : "null") + " where loadid=?";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					int i = 0;
					if (minDate != null) {
						stmt.setTimestamp(++i, getConvertedTimestamp(minDate));
					}
					if (maxDate != null) {
						stmt.setTimestamp(++i, getConvertedTimestamp(maxDate));
					}
					stmt.setLong(++i, batchLoadId);
					return stmt.executeUpdate();
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
			throw new DataAccessException("Failed to insert batch.", e);
		}
	}

	/**
	 * Inserts keycodes into the keycode bank.
	 * 
	 * @param batchLoadId
	 * @param productId
	 * @param keycodesBeans
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBean[] loadKeycodes(Long batchLoadId,
			Integer productId, KeycodeBean[] keycodesBeans)
			throws DataAccessException, DataSourceLookupException {
		final List<KeycodeBean> failedKeyCodes = new ArrayList<KeycodeBean>();
		final String sqlCommand = "{call keyCodeBank.loadFromBatch(?,?,?,?,?) }";
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement stmt = conn.prepareCall(sqlCommand);
				try {
					int quantity = keycodesBeans.length;
					for (int i = 0; i < quantity; i++) {
						stmt.setLong(1, batchLoadId);
						stmt.setInt(2, productId);
						stmt.setString(3, TextFilter.filter(keycodesBeans[i].getKeycode()));
						stmt.setString(4, TextFilter.filter(keycodesBeans[i].getSerialNumber()));
						stmt.registerOutParameter(5,
								oracle.jdbc.OracleTypes.NUMBER);
						try {
							stmt.execute();
						} catch (SQLException e) {
							// Insertion failed; add to the failure set
							failedKeyCodes.add(keycodesBeans[i]);
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
			throw new DataAccessException("Failed to load keycodes.", e);
		}
		return failedKeyCodes.toArray(new KeycodeBean[failedKeyCodes.size()]);
	}

	/**
	 * returns all batch load records with a matching load id (ideally 0 or 1
	 * records returned)
	 * 
	 * @param loadid
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean[] getByBatchLoadId(int loadid)
			throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "select  KCB_BATCH_LOAD.LOADID,   KCB_BATCH_LOAD.BEG_TS,   KCB_BATCH_LOAD.END_TS,   KCB_BATCH_LOAD.LOAD_TS,   KCB_BATCH_LOAD.PRODUCTID,  KCB_PRODUCT.SKU,  KCB_PRODUCT.NONMRCH_SKU,   KCB_PRODUCT.MSTR_ITM_ID,   KCB_PRODUCT.NAME,   KCB_PRODUCT.VENDORID from   KCB_BATCH_LOAD   inner join KCB_PRODUCT  on KCB_BATCH_LOAD.PRODUCTID = KCB_PRODUCT.PRODUCTID where  loadid=? order by   KCB_BATCH_LOAD.LOAD_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, loadid);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<KeycodeBatchBean> results = new ArrayList<KeycodeBatchBean>();
						while (rs.next()) {
							KeycodeBatchBean bean = new KeycodeBatchBean();
							bean.setBatchLoadId(rs.getLong(1));
							bean.setMinDate(rs.getTimestamp(2));
							bean.setMaxDate(rs.getTimestamp(3));
							bean.setCreatedDate(rs.getTimestamp(4));
							bean.setProductId(rs.getInt(5));
							bean.setMerchandiseSku(rs.getString(6));
							bean.setNonMerchandiseSku(rs.getString(7));
							bean.setMasterItemId(rs.getString(8));
							bean.setDescription(rs.getString(9));
							bean.setVendorId(rs.getString(10));
							results.add(bean);
						}
						return results.toArray(new KeycodeBatchBean[results
								.size()]);
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
					"Failed to retrieve batch data where LOADID=[" + loadid
							+ "]", e);
		}
	}

	/**
	 * returns all batch load records with a matching merchandise sku 
	 * 
	 * @param merchandise sku
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean[] getByMerchandiseSku(String arg) throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "select  KCB_BATCH_LOAD.LOADID,   KCB_BATCH_LOAD.BEG_TS,   KCB_BATCH_LOAD.END_TS,   KCB_BATCH_LOAD.LOAD_TS,   KCB_BATCH_LOAD.PRODUCTID,  KCB_PRODUCT.SKU,  KCB_PRODUCT.NONMRCH_SKU,   KCB_PRODUCT.MSTR_ITM_ID,   KCB_PRODUCT.NAME,   KCB_PRODUCT.VENDORID from   KCB_BATCH_LOAD   inner join KCB_PRODUCT  on KCB_BATCH_LOAD.PRODUCTID = KCB_PRODUCT.PRODUCTID where  KCB_PRODUCT.SKU=? order by   KCB_BATCH_LOAD.LOAD_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<KeycodeBatchBean> results = new ArrayList<KeycodeBatchBean>();
						while (rs.next()) {
							KeycodeBatchBean bean = new KeycodeBatchBean();
							bean.setBatchLoadId(rs.getLong(1));
							bean.setMinDate(rs.getTimestamp(2));
							bean.setMaxDate(rs.getTimestamp(3));
							bean.setCreatedDate(rs.getTimestamp(4));
							bean.setProductId(rs.getInt(5));
							bean.setMerchandiseSku(rs.getString(6));
							bean.setNonMerchandiseSku(rs.getString(7));
							bean.setMasterItemId(rs.getString(8));
							bean.setDescription(rs.getString(9));
							bean.setVendorId(rs.getString(10));
							results.add(bean);
						}
						return results.toArray(new KeycodeBatchBean[results
								.size()]);
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
					"Failed to retrieve batch data where MERCHANDISESKU=["
							+ arg + "]", e);
		}
	}
	
	/**
	 * returns all batch load records with a matching non-merchandise sku 
	 * 
	 * @param non-erchandise sku
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean[] getByNonMerchandiseSku(String arg) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select  KCB_BATCH_LOAD.LOADID,   KCB_BATCH_LOAD.BEG_TS,   KCB_BATCH_LOAD.END_TS,   KCB_BATCH_LOAD.LOAD_TS,   KCB_BATCH_LOAD.PRODUCTID,  KCB_PRODUCT.SKU,  KCB_PRODUCT.NONMRCH_SKU,   KCB_PRODUCT.MSTR_ITM_ID,   KCB_PRODUCT.NAME,   KCB_PRODUCT.VENDORID from   KCB_BATCH_LOAD   inner join KCB_PRODUCT  on KCB_BATCH_LOAD.PRODUCTID = KCB_PRODUCT.PRODUCTID where  KCB_PRODUCT.NONMRCH_SKU=? order by   KCB_BATCH_LOAD.LOAD_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<KeycodeBatchBean> results = new ArrayList<KeycodeBatchBean>();
						while (rs.next()) {
							KeycodeBatchBean bean = new KeycodeBatchBean();
							bean.setBatchLoadId(rs.getLong(1));
							bean.setMinDate(rs.getTimestamp(2));
							bean.setMaxDate(rs.getTimestamp(3));
							bean.setCreatedDate(rs.getTimestamp(4));
							bean.setProductId(rs.getInt(5));
							bean.setMerchandiseSku(rs.getString(6));
							bean.setNonMerchandiseSku(rs.getString(7));
							bean.setMasterItemId(rs.getString(8));
							bean.setDescription(rs.getString(9));
							bean.setVendorId(rs.getString(10));
							results.add(bean);
						}
						return results.toArray(new KeycodeBatchBean[results
								.size()]);
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
					"Failed to retrieve batch data where NONMERCHSKU=["
							+ arg + "]", e);
		}
	}
	
	/**
	 * returns all batch load records with a matching master item id 
	 * 
	 * @param master item id
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean[] getByMasterItemId(String arg) throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select  KCB_BATCH_LOAD.LOADID,   KCB_BATCH_LOAD.BEG_TS,   KCB_BATCH_LOAD.END_TS,   KCB_BATCH_LOAD.LOAD_TS,   KCB_BATCH_LOAD.PRODUCTID,  KCB_PRODUCT.SKU,  KCB_PRODUCT.NONMRCH_SKU,   KCB_PRODUCT.MSTR_ITM_ID,   KCB_PRODUCT.NAME,   KCB_PRODUCT.VENDORID from   KCB_BATCH_LOAD   inner join KCB_PRODUCT  on KCB_BATCH_LOAD.PRODUCTID = KCB_PRODUCT.PRODUCTID where  KCB_PRODUCT.MSTR_ITM_ID=? order by   KCB_BATCH_LOAD.LOAD_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, arg);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<KeycodeBatchBean> results = new ArrayList<KeycodeBatchBean>();
						while (rs.next()) {
							KeycodeBatchBean bean = new KeycodeBatchBean();
							bean.setBatchLoadId(rs.getLong(1));
							bean.setMinDate(rs.getTimestamp(2));
							bean.setMaxDate(rs.getTimestamp(3));
							bean.setCreatedDate(rs.getTimestamp(4));
							bean.setProductId(rs.getInt(5));
							bean.setMerchandiseSku(rs.getString(6));
							bean.setNonMerchandiseSku(rs.getString(7));
							bean.setMasterItemId(rs.getString(8));
							bean.setDescription(rs.getString(9));
							bean.setVendorId(rs.getString(10));
							results.add(bean);
						}
						return results.toArray(new KeycodeBatchBean[results
								.size()]);
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
					"Failed to retrieve batch data where MASTERITEMID=["
							+ arg + "]", e);
		}
	}

	/**
	 * returns all batch load records with matching vendor id
	 * 
	 * @param vendorId
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static KeycodeBatchBean[] getByVendorId(String vendorId)
			throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "select  KCB_BATCH_LOAD.LOADID,   KCB_BATCH_LOAD.BEG_TS,   KCB_BATCH_LOAD.END_TS,   KCB_BATCH_LOAD.LOAD_TS,   KCB_BATCH_LOAD.PRODUCTID,  KCB_PRODUCT.SKU,  KCB_PRODUCT.NONMRCH_SKU,   KCB_PRODUCT.MSTR_ITM_ID,   KCB_PRODUCT.NAME,   KCB_PRODUCT.VENDORID from   KCB_BATCH_LOAD   inner join KCB_PRODUCT  on KCB_BATCH_LOAD.PRODUCTID = KCB_PRODUCT.PRODUCTID where KCB_PRODUCT.VENDORID=? order by   KCB_BATCH_LOAD.LOAD_TS desc";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, vendorId);
					final ResultSet rs = stmt.executeQuery();
					try {
						final List<KeycodeBatchBean> results = new ArrayList<KeycodeBatchBean>();
						while (rs.next()) {
							KeycodeBatchBean bean = new KeycodeBatchBean();
							bean.setBatchLoadId(rs.getLong(1));
							bean.setMinDate(rs.getTimestamp(2));
							bean.setMaxDate(rs.getTimestamp(3));
							bean.setCreatedDate(rs.getTimestamp(4));
							bean.setProductId(rs.getInt(5));
							bean.setMerchandiseSku(rs.getString(6));
							bean.setNonMerchandiseSku(rs.getString(7));
							bean.setMasterItemId(rs.getString(8));
							bean.setDescription(rs.getString(9));
							bean.setVendorId(rs.getString(10));
							results.add(bean);
						}
						return results.toArray(new KeycodeBatchBean[results
								.size()]);
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
					"Failed to retrieve batch data where VENDORID=[" + vendorId
							+ "]", e);
		}
	}

	/**
	 * returns number of keycodes loaded per give batch load id
	 * 
	 * @param loadid
	 * @return
	 * @throws DataAccessException
	 * @throws DataSourceLookupException
	 */
	public static int getCountByBatchLoadId(long loadid)
			throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "select count(loadid) from kcb_keycode where loadid=?";

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					stmt.setLong(1, loadid);
					final ResultSet rs = stmt.executeQuery();
					try {
						if (rs.next()) {
							return rs.getInt(1);
						} else {
							return 0;
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
					"Failed to retrieve keycode count for batch load id=["
							+ loadid + "].", e);
		}
	}

	public static void deleteBatchLoadByLoadId(long batchLoadId)
			throws DataAccessException, DataSourceLookupException {
		final String sqlCommand = "{call keyCodeBank.dropKeycodesByLoadId(?) }";

		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement stmt = conn.prepareCall(sqlCommand);
				try {
					stmt.setLong(1, batchLoadId);
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
			throw new DataAccessException(
					"Failed to delete keycodes for batch load id=["
							+ batchLoadId + "].", e);
		}
	}

}
