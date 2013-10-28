package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpPropertyBean;

public class UtilityDBWrapper extends SomSdpDBWrapper {

	final static int CATALOG_CATEGORY_MAP = 11;
	final static int BOOLEAN_FLAG_MAP = 12;
	final static int ADDRESS_TYPE_MAP = 13;
	final static int PHONE_TYPE_MAP = 14;
	final static int CREDITCARD_TYPE_MAP = 15;
	final static int CANCEL_REASON_CODE_MAP = 16;
	final static int ADJUSTMENT_REASON_CODE_MAP = 17;
	final static int VENDOR_CATEGORY_MAP = 18;
	final static int OCIS_CANCEL_REASON_CODE_MAP = 19;
	final static int IN_PRODUCT_MESSAGE_CODE_MAP = 20;

	final static String DEFAULT_EMPTY_STRING = "";

	private static final Logger logger = Logger.getLogger(UtilityDBWrapper.class.getName());

	/**
	 * Returns a List where vendor id is the key and vendor name is the value.
	 * 
	 * @return List<SelectItem> of vendor id and vendor name
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static ConcurrentHashMap<String, String> getVendorIdMap() throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select SDP_VENDOR_CONFIG.VNDR_ID, SDP_VENDOR_CONFIG.VNDR_NM from SDP_VENDOR_CONFIG";
		try {
			return getMap(sqlCommand);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve vendor id HashMap.", e);
			throw new DataAccessException("Failed to retrieve vendor id HashMap.", e);
		}
	}

	/**
	 * Returns a ConcurrentHashMap<String, String> where status code is the key
	 * and status name is the value.
	 * 
	 * @return ConcurrentHashMap<String, String> of status code and status name
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static ConcurrentHashMap<String, String> getStatusCodeMap() throws DataSourceLookupException, DataAccessException {
		final String sqlCommand = "select SDP_STATUS_MASTER.STAT_CDE, SDP_STATUS_MASTER.STAT_DESC from SDP_STATUS_MASTER";
		try {
			return getMap(sqlCommand);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve status code HashMap.", e);
			throw new DataAccessException("Failed to retrieve status code HashMap.", e);
		}
	}

	private static ConcurrentHashMap<String, String> getMap(final String sqlCommand) throws DataSourceLookupException, DataAccessException, SQLException {

		final Connection conn = getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			try {
				ResultSet rs = stmt.executeQuery();
				try {
					ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
					while (rs.next()) {
						String val = rs.getString(2);
						map.put(rs.getString(1), val != null ? val : DEFAULT_EMPTY_STRING);
					}
					if (logger.isDebugEnabled()) {
						logger.log(Level.DEBUG, "Retrieved Map >> " + map.toString());
					}
					return map;
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
	}

	/**
	 * Returns a List of vendor IDs.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getVendorIdList() throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				PreparedStatement stmt = conn.prepareStatement("select SDP_VENDOR_CONFIG.VNDR_ID, SDP_VENDOR_CONFIG.VNDR_NM from SDP_VENDOR_CONFIG");
				try {
					ResultSet rs = stmt.executeQuery();
					try {
						List<SelectItem> selectItems = new ArrayList<SelectItem>();
						while (rs.next()) {
							selectItems.add(new SelectItem(rs.getString(1), rs.getString(2)));
						}

						return selectItems;
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
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}
	
	/**
	 * Returns a List of audit trail action types.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getAuditTrailActionTypeList() throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				PreparedStatement stmt = conn.prepareStatement("select ref_aud_actn_id, actn_nm from aud_actn order by actn_nm asc");
				try {
					ResultSet rs = stmt.executeQuery();
					try {
						List<SelectItem> selectItems = new ArrayList<SelectItem>();
						while (rs.next()) {
							selectItems.add(new SelectItem(rs.getString(1), rs.getString(2)));
						}

						return selectItems;
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
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getCatalogCategoryList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(CATALOG_CATEGORY_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getVendorCategoryList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(VENDOR_CATEGORY_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getBooleanFlagList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(BOOLEAN_FLAG_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getAddressLabelList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(ADDRESS_TYPE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getPhoneLabelList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(PHONE_TYPE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getCreditCardTypeList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(CREDITCARD_TYPE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getCancelReasonCodeList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(CANCEL_REASON_CODE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getOpenCCancelReasonCodeList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(OCIS_CANCEL_REASON_CODE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getAdjustmentReasonCodeList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(ADJUSTMENT_REASON_CODE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	/**
	 * Returns a List where db value is the key and display label is the value.
	 * 
	 * @return List<SelectItem> of value and label
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getInProductMessageCodeList() throws DataSourceLookupException, DataAccessException {
		try {
			return getUISelectItemList(IN_PRODUCT_MESSAGE_CODE_MAP);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Failed to retrieve selectitem HashMap.", e);
			throw new DataAccessException("Failed to retrieve selectitem HashMap.", e);
		}
	}

	private static List<SelectItem> getUISelectItemList(int UI_SELECTITEM_COLLECT_ID) throws DataSourceLookupException, DataAccessException, SQLException {
		final String sqlCommand = "select UI_SELECTITEM_OPT.OPT_VAL, UI_SELECTITEM_OPT.OPT_LBL_TXT from UI_SELECTITEM_OPT where UI_SELECTITEM_OPT.UI_COLLECT_ID=? order by UI_SELECTITEM_OPT.PRTY_NBR asc";
		final Connection conn = getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sqlCommand);
			try {
				stmt.setInt(1, UI_SELECTITEM_COLLECT_ID);
				ResultSet rs = stmt.executeQuery();
				try {
					List<SelectItem> selectItems = new ArrayList<SelectItem>();
					while (rs.next()) {
						selectItems.add(new SelectItem(rs.getString(1), rs.getString(2)));
					}
					if (logger.isDebugEnabled()) {
						StringBuilder builder = new StringBuilder();
						builder.append("{");
						for (SelectItem selectItem : selectItems) {
							builder.append("[" + selectItem.getValue() + "," + selectItem.getLabel() + "]");
						}
						builder.append("}");
						logger.log(Level.DEBUG, "Retrieved List<SelectItem> >> " + builder.toString());
					}
					return selectItems;
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
	}

	public static List<SdpPropertyBean> getSdpConfigProperties() throws DataSourceLookupException, DataAccessException {

		final String sqlCommand = "select SDP_CONFIG.PROP_NAME, SDP_CONFIG.PROP_VALUE from SDP_CONFIG order by SDP_CONFIG.PROP_NAME asc, SDP_CONFIG.PROP_VALUE asc";
		try {
    		final Connection conn = getConnection();
    		try {
    			final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
    			try {
    				final ResultSet rs = stmt.executeQuery();
    				try {
    					final List<SdpPropertyBean> returnValue = new ArrayList<SdpPropertyBean>();
    					while (rs.next()) {
    						SdpPropertyBean bean = new SdpPropertyBean();
    						bean.setPropertyName(rs.getString(1));
    						bean.setPropertyValue(rs.getString(2));
    						returnValue.add(bean);
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
			throw new DataAccessException("Failed to retrieve sdp-config properties from database.", e);
		}
	}

	public static void loadSdpConfigPropertiesIntoDatabase(Properties props) throws DataAccessException, DataSourceLookupException {
		if (props.size() > 0) {
			try {
    			final Connection conn = getConnection();
    			try {
    				if (conn != null) {
    					conn.setAutoCommit(false);
    				
        				try {
        					deleteSdpConfigProperties(conn);
        				} catch (DataAccessException e) {
        					conn.rollback();
        					throw new DataAccessException("Failed to delete properties from database. Rolling back.", e);
        				}
        				
        				final PreparedStatement stmt = conn.prepareStatement("insert into SDP_CONFIG (PROP_NAME, PROP_VALUE) values (?, ?)");
        				try {
        					Set<Map.Entry<Object, Object>> entrySet = props.entrySet();
        
        					for (Map.Entry<Object, Object> entry : entrySet) {
        						stmt.setString(1, entry.getKey() != null ? entry.getKey().toString() : null);
        						stmt.setString(2, entry.getValue() != null ? entry.getValue().toString() : null);
        						stmt.addBatch();
        					}
        					stmt.executeBatch();
        				} catch (SQLException e) {
            				if (conn != null) {
            					conn.rollback();
            				}
            				throw new DataAccessException("Failed to load config properties to database. Rolling back.", e);
        				} finally {
        					if (stmt != null) {
        						stmt.close();
        					}
        				}
        				conn.commit();
    				} else {
    					throw new DataSourceLookupException("Data source lookup returned null connection.");
    				}
    			} catch (SQLException e) {
    				if (conn != null) {
    					conn.rollback();
    				}
    				throw new DataAccessException("Failed to load config properties to database. Rolling back.", e);
    			} finally {
    				if (conn != null) {
    					conn.close();
    				}
    			}
			} catch (SQLException e) {
				throw new DataAccessException("Failure while loading properties to database.", e);
			}
		}
		
	}
	
	private static void deleteSdpConfigProperties(final Connection conn) throws DataAccessException {
		try {
    		final PreparedStatement stmt = conn.prepareStatement("delete from SDP_CONFIG");
    		try {
    			stmt.execute();
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    		}
		} catch (SQLException e) {
			throw new DataAccessException("Failure while deleting properties from database.", e);
		}
		logger.log(Level.INFO, "Deleted all sdp-config properties from database - pending commit.");
	}
}
