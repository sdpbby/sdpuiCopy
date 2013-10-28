package com.accenture.bby.sdp.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.PreorderBean;

public class PreorderDBWrapper extends SomSdpDBWrapper {

	/**
	 * Updates the street date and throttle factor
	 * 
	 * @param catalogId
	 * @param newStreetDate
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String update(String catalogId, Date newStreetDate, String userId) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(catalogId )) {
			throw new DataAccessException("Catalog ID is missing.");
		} else if (newStreetDate == null) {
			throw new DataAccessException("Street Date is missing.");
		} else if (!isNotNull(userId)) {
			throw new DataAccessException("User ID is missing.");
		}
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call PreOrder.updateSDPPreOrder(?, ?, ?) }");
    			try {
        			proc.setTimestamp(1, getConvertedTimestamp(newStreetDate));
        			proc.setString(2, TextFilter.filter(userId.trim()));
        			proc.setString(3, TextFilter.filter(catalogId.trim()));
        			proc.executeQuery();
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
			throw new DataAccessException("Failed to update preorder. CATALOGID=[" + catalogId + "]", e);
		}
		return catalogId;
	}

	/**
	 * Removes the street date and throttle factor
	 * 
	 * @param catalogId
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String delete(String catalogId, String userId) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(catalogId )) {
			throw new DataAccessException("Catalog ID is missing.");
		} else if (!isNotNull(userId)) {
			throw new DataAccessException("User ID is missing.");
		}
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call PreOrder.deleteSDPPreOrder(?, ?) }");
    			try {
        			proc.setString(1, TextFilter.filter(userId.trim()));
        			proc.setString(2, TextFilter.filter(catalogId.trim()));
        			proc.executeQuery();
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
			throw new DataAccessException("Failed to delete preorder. CATALOGID=[" + catalogId + "]", e);
		}
		return catalogId;
	}

	/**
	 * Creates a new street date and throttle factor
	 * 
	 * @param catalogId
	 * @param newStreetDate
	 * @param newThrottleFactor
	 * @param userId
	 *            id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String insert(String catalogId, Date newStreetDate, String userId) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(catalogId)) {
			throw new DataAccessException("Catalog ID is missing.");
		} else if (newStreetDate == null) {
			throw new DataAccessException("Street Date is missing.");
		} else if (!isNotNull(userId)) {
			throw new DataAccessException("User ID is missing.");
		}
		try {
    		final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call PreOrder.insertSDPPreOrder(?, ?, ?) }");
    			try {
        			proc.setTimestamp(1, getConvertedTimestamp(newStreetDate));
        			proc.setString(2, TextFilter.filter(userId.trim()));
        			proc.setString(3, TextFilter.filter(catalogId.trim()));
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
			throw new DataAccessException("Failed to create preorder. CATALOGID=[" + catalogId + "]", e);
		}
		return catalogId;
	}

	/**
	 * Returns the product preorder data associated with the input product sku or null if not found
	 * 
	 * @param productSku
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static PreorderBean getProductByProductSku(String productSku) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(productSku)) {
			throw new DataAccessException("Product SKU is Missing");
		}
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call PreOrder.getPreorderByProductSku(?, ?) }");
				try {
    				proc.setString(1, productSku.trim());
    				proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
    				proc.executeQuery();
    				final ResultSet rs = (ResultSet) proc.getObject(2);
    				try {
        				if (rs.next()) {
        					final PreorderBean preorderBean = new PreorderBean();
        					preorderBean.setCatalogId(rs.getString(1));
        					preorderBean.setVendorId(rs.getString(2));
        					preorderBean.setVendorName(rs.getString(3));
        					preorderBean.setProductSku(rs.getString(4));
        					preorderBean.setDescription(rs.getString(5));
        					preorderBean.setStreetDate(rs.getTimestamp(6));
        					preorderBean.setPreorderCount(rs.getInt(7));
        					preorderBean.setStatusId(rs.getString(8));
        					preorderBean.setCreatedByUserId(rs.getString(9));
        					preorderBean.setUpdatedByUserId(rs.getString(10));
        					preorderBean.setCreatedDate(rs.getTimestamp(11));
        					preorderBean.setUpdatedDate(rs.getTimestamp(12));
        					return preorderBean;
        				} else {
        					return null;
        				}
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
			throw new DataAccessException("Failed to retrieve catalog id. PRODUCTSKU=[" + productSku + "]", e);
		}
	}

	/**
	 * Returns all preorder records
	 * 
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<PreorderBean> getAll() throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call PreOrder.getAll(?) }");
				try {
					proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(1);
					try {
						final List<PreorderBean> beans = new ArrayList<PreorderBean>();
						while (rs.next()) {
							final PreorderBean bean = new PreorderBean();
							bean.setCatalogId(rs.getString(1));
							bean.setVendorId(rs.getString(2));
							bean.setVendorName(rs.getString(3));
							bean.setProductSku(rs.getString(4));
							bean.setDescription(rs.getString(5));
							bean.setStreetDate(rs.getTimestamp(6));
							bean.setPreorderCount(rs.getInt(7));
							bean.setStatusId(rs.getString(8));
							bean.setCreatedByUserId(rs.getString(9));
							bean.setUpdatedByUserId(rs.getString(10));
							bean.setCreatedDate(rs.getTimestamp(11));
							bean.setUpdatedDate(rs.getTimestamp(12));
							beans.add(bean);
						}
						return beans;
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
			throw new DataAccessException("Failed to retrieve preorder records.", e);
		}
	}

	/**
	 * Returns all preorder records relating to the input product sku
	 * 
	 * @param productSku
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<PreorderBean> getByProductSku(String productSku) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(productSku)) {
			throw new DataAccessException("ProductSku is Missing");
		}
		return getByArg("{call PreOrder.getByProductSku(?, ?) }", productSku);
	}

	/**
	 * Returns all preorder records relating to the input vendor id
	 * 
	 * @param vendorId
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<PreorderBean> getByVendorId(String vendorId) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(vendorId)) {
			throw new DataAccessException("vendorId is Missing");
		}
		return getByArg("{call PreOrder.getByVendorId(?, ?) }", vendorId);
	}
	
	
	private static List<PreorderBean> getByArg(String sqlCommand, String arg) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall(sqlCommand);
				try {
					proc.setString(1, arg.trim());
					proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(2);
					try {
						final List<PreorderBean> beans = new ArrayList<PreorderBean>();
						if (rs != null) {
							while (rs.next()) {
								final PreorderBean bean = new PreorderBean();
								bean.setCatalogId(rs.getString(1));
								bean.setVendorId(rs.getString(2));
								bean.setVendorName(rs.getString(3));
								bean.setProductSku(rs.getString(4));
								bean.setDescription(rs.getString(5));
								bean.setStreetDate(rs.getTimestamp(6));
								bean.setPreorderCount(rs.getInt(7));
								bean.setStatusId(rs.getString(8));
								bean.setCreatedByUserId(rs.getString(9));
								bean.setUpdatedByUserId(rs.getString(10));
								bean.setCreatedDate(rs.getTimestamp(11));
								bean.setUpdatedDate(rs.getTimestamp(12));
								beans.add(bean);
							}
						}
						return beans;
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
			throw new DataAccessException("Failed to retrieve preorder records. PARAM=[" + arg + "]", e);
		}
	}

	/**
	 * Returns a SelectItem list of vendors that currently have products in the
	 * pre-order table. This list should NOT include any vendors that currently
	 * do not have any products set as pre-order. For each SelectItem, the VALUE
	 * will be set as the SDP Vendor ID (e.g., SPS00) and the LABEL will be set
	 * as the vendor name (e.g., Sony) (e.g., new SelectItem("SPS00", "Sony"))
	 * 
	 * @return list of SelectItem
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<SelectItem> getVendorList() throws DataSourceLookupException, DataAccessException {

		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call PreOrder.getVendorList(?) }");
				try {
					proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(1);
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
			throw new DataAccessException("Failed to retrieve vendor list.", e);
		}
	}

	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
