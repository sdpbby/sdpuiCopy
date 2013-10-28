package com.accenture.bby.sdp.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.beans.KcbVendorBean;

/**
 * @author a719057
 * 
 */
public class KcbCatalogDBWrapper extends KcbDBWrapper {

	/**
	 * updates vendor in kcb catalog
	 * 
	 * @param currentVendorId
	 * @param newVendorId
	 * @param newVendorName
	 * @return new vendor id
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String updateVendor(String currentVendorId,
			KcbVendorBean kcbVendorBean) throws DataSourceLookupException,
			DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.updateKCBVendor(?, ?, ?, ?) }");
				try {
					if (kcbVendorBean.getVendorId() != null
							&& kcbVendorBean.getVendorId().trim().length() > 0) {
						proc.setString(1, kcbVendorBean.getVendorId().trim());
					} else {
						throw new DataAccessException("New VendorId is Missing");
					}

					if (kcbVendorBean.getVendorName() != null
							&& kcbVendorBean.getVendorName().trim().length() > 0) {
						proc.setString(2, kcbVendorBean.getVendorName().trim());
					} else {
						throw new DataAccessException(
								"New Vendor Name is Missing");
					}

					if (kcbVendorBean.getUpdatedByUserId() != null
							&& kcbVendorBean.getUpdatedByUserId().trim()
									.length() > 0) {
						proc.setString(3, kcbVendorBean.getUpdatedByUserId()
								.trim());
					} else {
						throw new DataAccessException("UserId is Missing");
					}

					if (currentVendorId != null
							&& currentVendorId.trim().length() > 0) {
						proc.setString(4, currentVendorId.trim());
					} else {
						throw new DataAccessException(
								"Current VendorId is Missing");
					}

					proc.executeQuery();
					return kcbVendorBean.getVendorId();
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
			throw new DataAccessException(
					"Failed to update kcb vendor. VENDORID=[" + currentVendorId
							+ "]", e);
		}
	}

	/**
	 * Removes existing vendor in kcb catalog
	 * 
	 * @param vendorId
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String deleteVendor(String vendorId)
			throws DataSourceLookupException, DataAccessException {
		throw new UnsupportedOperationException(
				"kcb vendor delete is not supported. This method should never be called!");
	}

	/**
	 * inserts new vendor in kcb catalog
	 * 
	 * @param newVendorId
	 * @param newVendorName
	 * @return id of new kcb vendor
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String insertVendor(KcbVendorBean kcbVendorBean)
			throws DataSourceLookupException, DataAccessException {

		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.insertKCBVendor(?, ?, ?) }");
				try {
					if (kcbVendorBean.getVendorId() != null
							&& kcbVendorBean.getVendorId().trim().length() > 0) {
						proc.setString(1, kcbVendorBean.getVendorId().trim());
					} else {
						throw new DataAccessException("New VendorId is Missing");
					}

					if (kcbVendorBean.getVendorName() != null
							&& kcbVendorBean.getVendorName().trim().length() > 0) {
						proc.setString(2, kcbVendorBean.getVendorName().trim());
					} else {
						throw new DataAccessException(
								"New Vendor Name is Missing");
					}

					if (kcbVendorBean.getCreatedByUserId() != null
							&& kcbVendorBean.getCreatedByUserId().trim()
									.length() > 0) {
						proc.setString(3, kcbVendorBean.getCreatedByUserId()
								.trim());
					} else {
						throw new DataAccessException("UserId is Missing");
					}
					proc.executeQuery();
					return kcbVendorBean.getVendorId();
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
			throw new DataAccessException(
					"Failed to insert kcb product. VENDORID=["
							+ kcbVendorBean.getVendorId() + "]", e);
		}
	}

	/**
	 * Returns array of kcb vendors matching input vendor id
	 * 
	 * @param vendorId
	 * @return array of KcbVendorBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbVendorBean[] getVendorsByVendorId(String vendorId)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(vendorId)) {
			throw new DataAccessException("vendorId is Missing");
		}
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.getKCBVendorByVendorId(?, ?) }");
				try {
					proc.setString(1, vendorId.trim());
					proc
							.registerOutParameter(2,
									oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(2);
					try {
						final List<KcbVendorBean> beans = new ArrayList<KcbVendorBean>();
						if (rs != null) {
							while (rs.next()) {
								final KcbVendorBean bean = new KcbVendorBean();
								bean.setVendorId(rs.getString(1));
								bean.setVendorName(rs.getString(2));
								bean.setCreatedDate(rs.getTimestamp(3));
								bean.setUpdatedDate(rs.getTimestamp(4));
								bean.setCreatedByUserId(rs.getString(5));
								bean.setUpdatedByUserId(rs.getString(6));
								beans.add(bean);
							}
						}
						return beans.toArray(new KcbVendorBean[beans.size()]);
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
			throw new DataAccessException(
					"Failed to retrieve kcb vendors. VENDORID=[" + vendorId
							+ "]", e);
		}
	}

	/**
	 * Updates existing product in kcb catalog
	 * 
	 * @param kcbProductBean
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static Integer updateProduct(KcbProductBean kcbProductBean)
			throws DataSourceLookupException, DataAccessException {

		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.updateKCBProduct(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				try {
					if (kcbProductBean.getVendorId() != null
							&& kcbProductBean.getVendorId().trim().length() > 0) {
						proc.setString(1, kcbProductBean.getVendorId().trim());
					} else {
						proc.setString(1, null);
					}

					if (kcbProductBean.getMerchandiseSku() != null
							&& kcbProductBean.getMerchandiseSku().trim()
									.length() > 0) {
						proc.setString(2, kcbProductBean.getMerchandiseSku()
								.trim());
					} else {
						throw new DataAccessException("New SKU is Missing");
					}

					if (kcbProductBean.getDescription() != null
							&& kcbProductBean.getDescription().trim().length() > 0) {
						proc.setString(3, kcbProductBean.getDescription()
								.trim());
					} else {
						proc.setString(3, null);
					}

					if (kcbProductBean.getLoadSize() != null) {
						proc.setInt(4, kcbProductBean.getLoadSize());
					} else {
						proc.setString(4, null);
					}

					if (kcbProductBean.getMasterItemId() != null
							&& kcbProductBean.getMasterItemId().trim().length() > 0) {
						proc.setString(5, kcbProductBean.getMasterItemId()
								.trim());
					} else {
						proc.setString(5, null);
					}

					if (kcbProductBean.getNonMerchandiseSku() != null
							&& kcbProductBean.getNonMerchandiseSku().trim()
									.length() > 0) {
						proc.setString(6, kcbProductBean.getNonMerchandiseSku()
								.trim());
					} else {
						proc.setString(6, null);
					}

					if (kcbProductBean.getThreshold() != null) {
						proc.setInt(7, kcbProductBean.getThreshold());
					} else {
						proc.setString(7, null);
					}

					if (kcbProductBean.getUpdatedByUserId() != null
							&& kcbProductBean.getUpdatedByUserId().trim()
									.length() > 0) {
						proc.setString(8, kcbProductBean.getUpdatedByUserId()
								.trim());
					} else {
						throw new DataAccessException("UserId is Missing");
					}

					if (kcbProductBean.getProductId() != null) {
						proc.setInt(9, kcbProductBean.getProductId());
					} else {
						throw new DataAccessException("ProductId is Missing");
					}

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
			throw new DataAccessException(
					"Failed to update kcb product. PRODUCTID=["
							+ kcbProductBean.getProductId() + "]", e);
		}
		return kcbProductBean.getProductId();
	}

	/**
	 * Removes existing product in kcb catalog
	 * 
	 * @param productId
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String deleteProduct(String productId)
			throws DataSourceLookupException, DataAccessException {
		throw new UnsupportedOperationException(
				"kcb product delete is not supported. This method should never be called!");
	}

	/**
	 * inserts new product in kcb catalog
	 * 
	 * @param newVendorId
	 * @param newSku
	 * @param newDescription
	 * @param newMasterItemId
	 * @return id of new kcb product
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static Integer insertProduct(KcbProductBean kcbProductBean)
			throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {

				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.insertKCBProduct(?, ?, ?, ?, ?, ?, ?, ?,?) }");
				try {
					if (kcbProductBean.getVendorId() != null
							&& kcbProductBean.getVendorId().trim().length() > 0) {
						proc.setString(1, kcbProductBean.getVendorId().trim());
					} else {
						proc.setString(1, null);
					}

					if (kcbProductBean.getMerchandiseSku() != null
							&& kcbProductBean.getMerchandiseSku().trim()
									.length() > 0) {
						proc.setString(2, kcbProductBean.getMerchandiseSku()
								.trim());
					} else {
						throw new DataAccessException("New SKU is Missing");
					}

					if (kcbProductBean.getDescription() != null
							&& kcbProductBean.getDescription().trim().length() > 0) {
						proc.setString(3, kcbProductBean.getDescription()
								.trim());
					} else {
						proc.setString(3, null);
					}

					if (kcbProductBean.getLoadSize() != null) {
						proc.setInt(4, kcbProductBean.getLoadSize());
					} else {
						proc.setString(4, null);
					}

					if (kcbProductBean.getMasterItemId() != null
							&& kcbProductBean.getMasterItemId().trim().length() > 0) {
						proc.setString(5, kcbProductBean.getMasterItemId()
								.trim());
					} else {
						proc.setString(5, null);
					}

					if (kcbProductBean.getNonMerchandiseSku() != null
							&& kcbProductBean.getNonMerchandiseSku().trim()
									.length() > 0) {
						proc.setString(6, kcbProductBean.getNonMerchandiseSku()
								.trim());
					} else {
						proc.setString(6, null);
					}

					if (kcbProductBean.getThreshold() != null) {
						proc.setInt(7, kcbProductBean.getThreshold());
					} else {
						proc.setString(7, null);
					}

					if (kcbProductBean.getCreatedByUserId() != null
							&& kcbProductBean.getCreatedByUserId().trim()
									.length() > 0) {
						proc.setString(8, kcbProductBean.getCreatedByUserId()
								.trim());
					} else {
						throw new DataAccessException("UserId is Missing");
					}

					proc.registerOutParameter(9,
							oracle.jdbc.OracleTypes.INTEGER);
					proc.executeQuery();
					return proc.getInt(9);
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
			throw new DataAccessException("Failed to insert kcb product. SKU=["
					+ kcbProductBean.getNonMerchandiseSku()
					+ "] MASTERITEMID=[" + kcbProductBean.getMasterItemId()
					+ "]", e);
		}
	}

	public static void insertRelatedSKU(Integer productID, String relatedSKU,
			String userID) throws DataSourceLookupException,
			DataAccessException {
		final String[] sku = relatedSKU.split(";");
		try {
			final Connection conn = getConnection();
			try {
				for (int index = 0; index < sku.length; index++) {

					final CallableStatement proc = conn
							.prepareCall("{call KCBVendorProduct.insertKCBRelatedSKU(?, ?, ?) }");
					try {
						if (productID != null) {
							proc.setInt(1, productID);
						} else {
							throw new DataAccessException(
									"ProductId is Missing");
						}
						if (sku[index] != null
								&& sku[index].trim().length() > 0) {
							proc.setString(2, sku[index].trim());
						} else {
							throw new DataAccessException(
									"Related Sku is Missing");
						}
						if (isNotNull(userID)) {
							proc.setString(3, userID.trim());
						} else {
							proc.setString(3, null);
						}
						proc.executeQuery();
					} finally {
						if (proc != null) {
							proc.close();
						}
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(
					"Failed to insert related sku product. SKU=[" + relatedSKU
							+ "] ProductId =[" + productID + "]", e);
		}
	}

	/**
	 * Returns array of kcb products matching input vendor id
	 * 
	 * @param vendorId
	 * @return array of kcbProductBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getProductsByVendorId(String vendorId)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(vendorId)) {
			throw new DataAccessException("Vendor ID is Missing");
		}
		return getProductsByArg(
				"{call KCBVendorProduct.getKCBProductsByVendorId(?, ?) }",
				vendorId);
	}

	/**
	 * Returns array of kcb products matching input sku
	 * 
	 * @param sku
	 * @return array of kcbProductBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getProductsBySku(String sku)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(sku)) {
			throw new DataAccessException("Merchandise SKU is Missing");
		}
		return getProductsByArg(
				"{call KCBVendorProduct.getKCBProductsByProductSku(?, ?) }",
				sku);
	}

	/**
	 * Returns array of kcb products matching input masterItemId
	 * 
	 * @param sku
	 * @return array of kcbProductBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getProductsByMasterItemId(String masterItemId)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(masterItemId)) {
			throw new DataAccessException("Master Item ID is Missing");
		}
		return getProductsByArg(
				"{call KCBVendorProduct.getKCBProductsByMasterItemId(?, ?) }",
				masterItemId);
	}

	/**
	 * Returns array of kcb products matching input merchSku, sorted in
	 * descending order by created/updated date.
	 * 
	 * @param nonMerchSku
	 * @return array of kcbProductBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getProductsByNonMerchSku(String nonMerchSku)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(nonMerchSku)) {
			throw new DataAccessException("Non-Merchandise SKU is Missing");
		}
		return getProductsByArg(
				"{call KCBVendorProduct.getKCBProductsByMerchSku(?, ?) }",
				nonMerchSku);
	}

	private static KcbProductBean[] getProductsByArg(String sqlCommand,
			String arg) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall(sqlCommand);
				try {
					proc.setString(1, arg.trim());
					proc
							.registerOutParameter(2,
									oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(2);
					try {
						final List<KcbProductBean> beans = new ArrayList<KcbProductBean>();
						if (rs != null) {

							while (rs.next()) {
								final KcbProductBean bean = new KcbProductBean();
								bean.setProductId(rs.getInt(1));
								bean.setVendorId(rs.getString(2));
								bean.setMerchandiseSku(rs.getString(3));
								bean.setDescription(rs.getString(4));
								bean.setLoadSize(rs.getInt(5));
								bean.setMasterItemId(rs.getString(6));
								bean.setNonMerchandiseSku(rs.getString(7));
								bean.setThreshold(rs.getInt(8));
								bean.setCreatedDate(rs.getTimestamp(9));
								bean.setUpdatedDate(rs.getTimestamp(10));
								bean.setCreatedByUserId(rs.getString(11));
								bean.setUpdatedByUserId(rs.getString(12));
								bean.setRelatedSKU(KcbCatalogDBWrapper.getRelatedSKUByProductID(rs.getInt(1),conn));
								beans.add(bean);
							}
						}
						return beans.toArray(new KcbProductBean[beans.size()]);
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
			throw new DataAccessException("Failed retrieve products. PARAM=["
					+ arg + "]", e);
		}
	}

	/**
	 * Returns array of all kcb products, sorted in descending order by
	 * created/updated date.
	 * 
	 * @return array of kcbProductBean
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getAllProducts()
			throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.getAllKCBProducts(?) }");
				try {
					proc
							.registerOutParameter(1,
									oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(1);
					try {
						List<KcbProductBean> beans = new ArrayList<KcbProductBean>();
						if (rs != null) {
							while (rs.next()) {
								final KcbProductBean bean = new KcbProductBean();
								bean.setProductId(rs.getInt(1));
								bean.setVendorId(rs.getString(2));
								bean.setMerchandiseSku(rs.getString(3));
								bean.setDescription(rs.getString(4));
								bean.setLoadSize(rs.getInt(5));
								bean.setMasterItemId(rs.getString(6));
								bean.setNonMerchandiseSku(rs.getString(7));
								bean.setThreshold(rs.getInt(8));
								bean.setCreatedDate(rs.getTimestamp(9));
								bean.setUpdatedDate(rs.getTimestamp(10));
								bean.setCreatedByUserId(rs.getString(11));
								bean.setUpdatedByUserId(rs.getString(12));
								bean.setRelatedSKU(KcbCatalogDBWrapper.getRelatedSKUByProductID(rs.getInt(1),conn));
								beans.add(bean);
							}
						}
						return beans.toArray(new KcbProductBean[beans.size()]);
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
			throw new DataAccessException("Failed retrieve products", e);
		}
	}

	private static String getRelatedSKUByProductID(Integer productId,
			Connection conn) throws DataAccessException {
		try {
				final CallableStatement proc = conn
						.prepareCall("{call KCBVendorProduct.getRelatedSKUByProductID(?,?) }");
				try {
					proc.setInt(1, productId);
					proc
							.registerOutParameter(2,
									oracle.jdbc.OracleTypes.CURSOR);
					proc.executeQuery();
					final ResultSet rs = (ResultSet) proc.getObject(2);
					try {
						StringBuilder relatedSku = new StringBuilder();
						if (rs != null) {
							while (rs.next()) {
								if(relatedSku.length() > 0){
									relatedSku.append(";");
								}
								relatedSku.append(rs.getString(1));
							}
						}
						return relatedSku.toString();
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
			}catch (SQLException e) {
			throw new DataAccessException("Failed to related SKU", e);
		}
	}

	/**
	 * Returns array of all kcb products with matching input criteria.
	 * 
	 * @param vendorId
	 * @param merchandiseSku
	 * @param nonMerchandiseSku
	 * @param masterItemId
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static KcbProductBean[] getAllProducts(String vendorId,
			String merchandiseSku, String nonMerchandiseSku, String masterItemId)
			throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(merchandiseSku) && !isNotNull(nonMerchandiseSku)
				&& !isNotNull(masterItemId)) {
			return new KcbProductBean[0];
		} else {
			boolean appendAnd = false;
			StringBuilder builder = new StringBuilder();
			if (isNotNull(vendorId)) {
				builder.append(" VENDORID=? ");
				appendAnd = true;
			}
			if (isNotNull(merchandiseSku)) {
				if (appendAnd) {
					builder.append(" and ");
				} else {
					appendAnd = true;
				}
				builder.append(" SKU=? ");
			}
			if (isNotNull(nonMerchandiseSku)) {
				if (appendAnd) {
					builder.append(" and ");
				} else {
					appendAnd = true;
				}
				builder.append(" NONMRCH_SKU=? ");
			}
			if (isNotNull(masterItemId )) {
				if (appendAnd) {
					builder.append(" and ");
				}
				builder.append(" MSTR_ITM_ID=? ");
			}

			final String sqlCommand = "select KCB_PRODUCT.PRODUCTID, KCB_PRODUCT.VENDORID, KCB_PRODUCT.SKU, KCB_PRODUCT.NONMRCH_SKU, KCB_PRODUCT.MSTR_ITM_ID, KCB_PRODUCT.NAME, KCB_PRODUCT.LOAD_SIZE, KCB_PRODUCT.THRLD_CNT, KCB_PRODUCT.REC_CRT_TS, KCB_PRODUCT.REC_CRT_USR_ID, KCB_PRODUCT.REC_UPD_TS, KCB_PRODUCT.REC_UPD_USR_ID from KCB_PRODUCT where "
					+ builder.toString();

			try {
				final Connection conn = getConnection();
				try {
					final PreparedStatement stmt = conn
							.prepareStatement(sqlCommand);
					try {
						int i = 0;
						if (isNotNull(vendorId))
							stmt.setString(++i, vendorId.trim());
						if (isNotNull(merchandiseSku))
							stmt.setString(++i, merchandiseSku);
						if (isNotNull(nonMerchandiseSku))
							stmt.setString(++i, nonMerchandiseSku.trim());
						if (isNotNull(masterItemId))
							stmt.setString(++i, masterItemId.trim());
						final ResultSet rs = stmt.executeQuery();
						try {
							List<KcbProductBean> beans = new ArrayList<KcbProductBean>();
							while (rs.next()) {
								KcbProductBean bean = new KcbProductBean();
								bean.setProductId(rs.getInt(1));
								bean.setVendorId(rs.getString(2));
								bean.setMerchandiseSku(rs.getString(3));
								bean.setNonMerchandiseSku(rs.getString(4));
								bean.setMasterItemId(rs.getString(5));
								bean.setDescription(rs.getString(6));
								bean.setLoadSize(rs.getInt(7));
								bean.setThreshold(rs.getInt(8));
								bean.setCreatedDate(rs.getTimestamp(9));
								bean.setCreatedByUserId(rs.getString(10));
								bean.setUpdatedDate(rs.getTimestamp(11));
								bean.setUpdatedByUserId(rs.getString(12));
								bean.setRelatedSKU(KcbCatalogDBWrapper.getRelatedSKUByProductID(rs.getInt(1),conn));
								beans.add(bean);
							}
							return beans.toArray(new KcbProductBean[beans
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
				throw new DataAccessException("Failed retrieve products. SKU=["
						+ nonMerchandiseSku + "], NONMRCH_SKU=["
						+ merchandiseSku + "], MASTERITEMID=[" + masterItemId
						+ "]", e);
			}
		}
	}

	public static boolean duplicateSkuCheck(String relatedSKU)
			throws DataAccessException, DataSourceLookupException {
		boolean skuExist = false;
		final String[] sku = relatedSKU.split(";");
		try {
			final Connection conn = getConnection();
			// Duplicate Sku Check in Product Table
			try {
				final String sqlProductSku = "select KCB_PRODUCT.SKU from KCB_PRODUCT WHERE KCB_PRODUCT.SKU IS NOT NULL";
				final PreparedStatement stmtProduct = conn
						.prepareStatement(sqlProductSku);
				try {
					final ResultSet rs = stmtProduct.executeQuery();
					try {
						List<String> skuList = new ArrayList<String>();
						if (rs != null) {
							while (rs.next()) {
								skuList.add(rs.getString(1));
							}
						}
						for (int index = 0; index < sku.length; index++) {
							Iterator<String> it = skuList.iterator();
							while (it.hasNext()) {
								String s = (String) it.next();
								if (s.equalsIgnoreCase(sku[index])) {
									return true;
								}

							}
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmtProduct != null) {
						stmtProduct.close();
					}
				}

				// Duplicate Sku Check in Related Table
				if (!skuExist) {
					final String sqlRelatedSku = "select KCB_RELATED_PRODUCT_SKU_MAP.RELATEDSKU from "
							+ "KCB_RELATED_PRODUCT_SKU_MAP WHERE KCB_RELATED_PRODUCT_SKU_MAP.RELATEDSKU IS NOT NULL";
					final PreparedStatement stmtRelated = conn
							.prepareStatement(sqlRelatedSku);
					try {
						final ResultSet rs = stmtRelated.executeQuery();

						try {
							List<String> skuList = new ArrayList<String>();
							
							if (rs != null) {
								while (rs.next()) {
									skuList.add(rs.getString(1));
								}
							}
							for (int index = 0; index < sku.length; index++) {
								Iterator<String> it = skuList.iterator();
								while (it.hasNext()) {
									String s = (String) it.next();
									if (s.equalsIgnoreCase(sku[index])) {
										return true;
									}
								}
							}
						} finally {
							if (rs != null) {
								rs.close();
							}
						}
					} finally {
						if (stmtRelated != null) {
							stmtRelated.close();
						}
					}
				}
				// Duplicate Sku Check in NON-Merchandise Table

				if (!skuExist) {
					final String sqlNONMerchandiseSKU = "select KCB_PRODUCT.NONMRCH_SKU from KCB_PRODUCT WHERE KCB_PRODUCT.NONMRCH_SKU IS NOT NULL";
					final PreparedStatement stmtNONMerchandise = conn
							.prepareStatement(sqlNONMerchandiseSKU);
					try {
						final ResultSet rs = stmtNONMerchandise.executeQuery();

						try {
							List<String> skuList = new ArrayList<String>();
							if (rs != null) {
								while (rs.next()) {
									skuList.add(rs.getString(1));
								}
							}
							for (int index = 0; index < sku.length; index++) {
								Iterator<String> it = skuList.iterator();
								while (it.hasNext()) {
									String s = (String) it.next();
									if (s.equalsIgnoreCase(sku[index])) {
										return true;
									}
								}
							}
						} finally {
							if (rs != null) {
								rs.close();
							}
						}
					} finally {
						if (stmtNONMerchandise != null) {
							stmtNONMerchandise.close();
						}
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed retrieve products. ", e);
		}
		return skuExist;

	}

	public static void deleteRelatedSKU(Integer productId) throws DataSourceLookupException, DataAccessException {
		
		final String sqlCommand ="DELETE FROM KCB_RELATED_PRODUCT_SKU_MAP WHERE  KCB_RELATED_PRODUCT_SKU_MAP.PRODUCTID = ?";
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					if(productId != null){
						stmt.setInt(1, productId);
					}else{
						throw new DataAccessException("ProductId is missing");
					}
					stmt.executeQuery();

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
			throw new DataAccessException("Failed to delete related SKU", e);
		}
	}

	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}

}
