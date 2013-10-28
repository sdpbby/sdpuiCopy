package com.accenture.bby.sdp.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.VendorBean;

public class VendorDBWrapper extends SomSdpDBWrapper {
	/**
	 * Updates the vendor config table.
	 * 
	 * @param currentVendorId
	 * @param newVendorId
	 * @param newVendorName
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String update(String currentVendorId, String newVendorId, String newVendorName, Integer newServiceProviderId, Integer newAggregationFrequency, Integer newAggregationMax,
			Integer newRetryFrequency, Integer newRetryMax, Integer throttleFactor, String userId, String category) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(newVendorId)) {
			throw new DataAccessException("New VendorId is null. ");
		} else if (!isNotNull(userId)) {
			throw new DataAccessException("UserId is Missing");
		} else if (!isNotNull(currentVendorId)) {
			throw new DataAccessException("Old VendorId is null. ");
		}
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call VendorConfig.updateVendor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				try {
					proc.setString(1, TextFilter.filter(newVendorId.trim()));
					proc.setString(2, TextFilter.filter(newVendorName));
					proc.setInt(3, newServiceProviderId != null ? newServiceProviderId : -1);
					proc.setInt(4, newAggregationFrequency != null ? newAggregationFrequency : -1);
					proc.setInt(5, newAggregationMax != null ? newAggregationMax : -1);
					proc.setInt(6, newRetryFrequency != null ? newRetryFrequency : -1);
					proc.setInt(7, newRetryMax != null ? newRetryMax : -1);
					proc.setInt(8, throttleFactor != null ? throttleFactor : -1);
					proc.setString(9, TextFilter.filter(userId.trim()));
					proc.setString(10, TextFilter.filter(category));
					proc.setString(11, TextFilter.filter(currentVendorId.trim()));
					
					proc.execute();
				} finally {
					if (proc != null) {
						proc.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to update vendor. VENDORID=[" + currentVendorId + "]", e);
		}
		return newVendorId;
	}

	/**
	 * Deletes from the vendor config table.
	 * 
	 * @param vendorId
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String delete(String vendorId, String userId) throws DataSourceLookupException, DataAccessException {
		throw new UnsupportedOperationException("Vendor delete is not supported. This method should never be called!");
	}

	/**
	 * Inserts into the vendor config table.
	 * 
	 * @param vendorId
	 * @param vendorName
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String insert(String newVendorId, String newVendorName, Integer newServiceProviderId, Integer newAggregationFrequency, Integer newAggregationMax, Integer newRetryFrequency,
			Integer newRetryMax, Integer throttleFactor, String userId, String category) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(newVendorId)) {
			throw new DataAccessException("New VendorId is null. ");
		} else if (!isNotNull(userId)) {
			throw new DataAccessException("UserId is Missing");
		}
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call VendorConfig.insertVendor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				try {
					proc.setString(1, TextFilter.filter(newVendorId.trim()));
					proc.setString(2, TextFilter.filter(newVendorName));
					proc.setInt(3, newServiceProviderId != null ? newServiceProviderId : -1);
					proc.setInt(4, newAggregationFrequency != null ? newAggregationFrequency : -1);
					proc.setInt(5, newAggregationMax != null ? newAggregationMax : -1);
					proc.setInt(6, newRetryFrequency != null ? newRetryFrequency : -1);
					proc.setInt(7, newRetryMax != null ? newRetryMax : -1);
					proc.setInt(8, throttleFactor != null ? throttleFactor : -1);
					proc.setString(9, TextFilter.filter(userId.trim()));
					proc.setString(10, TextFilter.filter(category));
					
					proc.execute();
				} finally {
					if (proc != null) {
						proc.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to insert vendor. VENDORID=[" + newVendorId + "]", e);
		}
		return newVendorId;
	}

	/**
	 * Returns all vendor records
	 * 
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static VendorBean[] getAll() throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call vendorConfig.getAllVendor(?) }");
				try {
					proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(1);
					try {
						final List<VendorBean> beans = new ArrayList<VendorBean>();
						if (rs != null) {
							while (rs.next()) {
								final VendorBean bean = new VendorBean();
								bean.setVendorId(rs.getString(1));
								bean.setVendorName(rs.getString(2));
								bean.setServiceProviderId(rs.getInt(3));
								bean.setAggregationFrequency(rs.getInt(4));
								bean.setAggregationMax(rs.getInt(5));
								bean.setRetryFrequency(rs.getInt(6));
								bean.setRetryMax(rs.getInt(7));
								bean.setThrottleFactor(rs.getInt(8));
								bean.setLastAggregation(rs.getTimestamp(9));
								bean.setCreatedByUserId(rs.getString(10));
								bean.setUpdatedByUserId(rs.getString(11));
								bean.setCreatedDate(rs.getTimestamp(12));
								bean.setUpdatedDate(rs.getTimestamp(13));
								bean.setCategory(rs.getString(14));
								beans.add(bean);
							}
						}
						return beans.toArray(new VendorBean[beans.size()]);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (proc != null) {
						proc.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to retrieve vendor array.", e);
		}
	}
	
	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
