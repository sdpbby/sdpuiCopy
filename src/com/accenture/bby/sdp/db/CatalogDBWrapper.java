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
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.beans.VendorCatalogBean;
import com.accenture.bby.sdp.web.beans.WorkFlowAttributeBean;
import com.accenture.bby.sdp.web.beans.WorkFlowAttributeBean.OperationFlag;

public class CatalogDBWrapper extends SomSdpDBWrapper {

	/**
	 * updates the catalog record, workflow attributes, and commsat template
	 * 
	 * @param catalogBean
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String update(CatalogBean catalogBean) throws DataSourceLookupException, DataAccessException {

		try {
			final Connection conn = getConnection();
			try {
				CallableStatement proc = conn .prepareCall("{call CATALOG.updateSDPCatalog(?, ?, ?,?,?,?,?,?,?,?,?,?,?,?, ?) }");
				try {

					if (catalogBean.getMasterVendorId() != null && catalogBean.getMasterVendorId().trim().length() > 0) {
						proc.setString(1, catalogBean.getMasterVendorId() .trim());
					} else {
						proc.setString(1, null);
					}

					if (catalogBean.getProductType() != null && catalogBean.getProductType().trim().length() > 0) {
						proc.setString(2, catalogBean.getProductType().trim());
					} else {
						proc.setString(2, null);
					}

					if (catalogBean.getVendorId() != null && catalogBean.getVendorId().trim().length() > 0) {
						proc.setString(3, catalogBean.getVendorId().trim());
					} else {
						throw new DataAccessException("VendorID is null");
					}

					if (catalogBean.getPrimarySku() != null && catalogBean.getPrimarySku().trim().length() > 0) {
						proc.setString(4, catalogBean.getPrimarySku().trim());
					} else {
						proc.setString(4, null);
					}

					if (catalogBean.getPrimarySkuDescription() != null && catalogBean.getPrimarySkuDescription().trim().length() > 0) {
						proc.setString(5, catalogBean.getPrimarySkuDescription().trim());
					} else {
						throw new DataAccessException("newPrimarySkuDescription is null");
					}

					if (catalogBean.getVendorTriggerSku() != null && catalogBean.getVendorTriggerSku().trim().length() > 0) {
						proc.setString(6, catalogBean.getVendorTriggerSku().trim());
					} else {
						proc.setString(6, null);
					}

					if (catalogBean.getParentSku() != null && catalogBean.getParentSku().trim().length() > 0) {
						proc.setString(7, catalogBean.getParentSku().trim());
					} else {
						proc.setString(7, null);
					}

					if (catalogBean.getOfferType() != null && catalogBean.getOfferType().trim().length() > 0) {
						proc.setString(8, catalogBean.getOfferType().trim());
					} else {
						proc.setString(8, null);
					}

					if (catalogBean.getCategory() != null && catalogBean.getCategory().trim().length() > 0) {
						proc.setString(9, catalogBean.getCategory().trim());
					} else {
						proc.setString(9, null);
					}

					if (catalogBean.getSubCategory() != null && catalogBean.getSubCategory().trim().length() > 0) {
						proc.setString(10, catalogBean.getSubCategory().trim());
					} else {
						proc.setString(10, null);
					}

					if (catalogBean.getRole() != null) {
						proc.setInt(11, catalogBean.getRole());
					} else {
						proc.setInt(11, 0);
					}

					if (catalogBean.getRetryCount() != null) {
						proc.setInt(12, catalogBean.getRetryCount());
					} else {
						proc.setInt(12, 0);
					}

					if (catalogBean.getCommSatTemplateId() != null && catalogBean.getCommSatTemplateId().trim().length() > 0) {
						proc.setString(13, catalogBean.getCommSatTemplateId().trim());
					} else {
						proc.setString(13, null);
					}

					if (catalogBean.getUpdatedByUserId() != null && catalogBean.getUpdatedByUserId().trim().length() > 0) {
						proc.setString(14, catalogBean.getUpdatedByUserId().trim());
					} else {
						throw new DataAccessException("userId is null");
					}

					if (catalogBean.getCatalogId() != null && catalogBean.getCatalogId().trim().length() > 0) {
						proc.setString(15, catalogBean.getCatalogId().trim());
					} else {
						throw new DataAccessException("CatalogId is null");
					}

					proc.executeQuery();
					if (catalogBean.getWorkFlowAttributes() != null) {
						manageWorkFlowAttributes(catalogBean.getCatalogId(), catalogBean.getUpdatedByUserId(), catalogBean.getWorkFlowAttributes());
					} else {
						throw new DataAccessException("userId is null");
					}
					return catalogBean.getCatalogId();
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
			throw new DataAccessException("Failed to update catalog. CATALOGID=[" + catalogBean.getCatalogId() + "]", e);
		}
	}

	/**
	 * Deletes from the catalog table.
	 * 
	 * @param currentCatalogId
	 * @param userId
	 *            id of the user who made the change
	 * @return catalog id
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String delete(String currentCatalogId, String userId)
			throws DataSourceLookupException, DataAccessException {
		throw new UnsupportedOperationException(
				"Vendor delete is not supported. This method should never be called!");
	}

	
	/**
	 * Inserts into catalog table, workflow attributes, and commsat template tables
	 * 
	 * @param catalogBean
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String insert(CatalogBean catalogBean) throws DataSourceLookupException, DataAccessException {

		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call CATALOG.insertSDPCatalog(?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?) }");
    			try {
    				
        			if (catalogBean.getMasterVendorId() != null && catalogBean.getMasterVendorId().trim().length() > 0) {
        				proc.setString(1, catalogBean.getMasterVendorId().trim());
        			} else {
        				proc.setString(1, null);
        			}
        
        			if (catalogBean.getProductType() != null && catalogBean.getProductType().trim().length() > 0) {
        				proc.setString(2, catalogBean.getProductType().trim());
        			} else {
        				proc.setString(2, null);
        			}
        
        			if (catalogBean.getVendorId() != null && catalogBean.getVendorId().trim().length() > 0) {
        				proc.setString(3, catalogBean.getVendorId().trim());
        			} else {
        				throw new DataAccessException("VendorID is null");
        			}
        
        			if (catalogBean.getPrimarySku() != null && catalogBean.getPrimarySku().trim().length() > 0) {
        				proc.setString(4, catalogBean.getPrimarySku().trim());
        			} else {
        				proc.setString(4, null);
        			}
        
        			if (catalogBean.getPrimarySkuDescription() != null && catalogBean.getPrimarySkuDescription().trim().length() > 0) {
        				proc.setString(5, catalogBean.getPrimarySkuDescription().trim());
        			} else {
        				throw new DataAccessException(
        						"newPrimarySkuDescription is null");
        			}
        
        			if (catalogBean.getVendorTriggerSku() != null && catalogBean.getVendorTriggerSku().trim().length() > 0) {
        				proc.setString(6, catalogBean.getVendorTriggerSku().trim());
        			} else {
        				proc.setString(6, null);
        			}
        
        			if (catalogBean.getParentSku() != null && catalogBean.getParentSku().trim().length() > 0) {
        				proc.setString(7, catalogBean.getParentSku().trim());
        			} else {
        				proc.setString(7, null);
        			}
        
        			if (catalogBean.getOfferType() != null && catalogBean.getOfferType().trim().length() > 0) {
        				proc.setString(8, catalogBean.getOfferType().trim());
        			} else {
        				proc.setString(8, null);
        			}
        
        			if (catalogBean.getCategory() != null && catalogBean.getCategory().trim().length() > 0) {
        				proc.setString(9, catalogBean.getCategory().trim());
        			} else {
        				proc.setString(9, null);
        			}
        
        			if (catalogBean.getSubCategory() != null && catalogBean.getSubCategory().trim().length() > 0) {
        				proc.setString(10, catalogBean.getSubCategory().trim());
        			} else {
        				proc.setString(10, null);
        			}
        
        			if (catalogBean.getRole() != null) {
        				proc.setInt(11, catalogBean.getRole());
        			} else {
        				proc.setInt(11, 0);
        			}
        
        			if (catalogBean.getRole() != null) {
        				proc.setInt(12, catalogBean.getRetryCount());
        			} else {
        				proc.setInt(12, 0);
        			}
        			if (catalogBean.getCommSatTemplateId() != null && catalogBean.getCommSatTemplateId().trim().length() > 0) {
        				proc.setString(13, catalogBean.getCommSatTemplateId().trim());
        			} else {
        				proc.setString(13, null);
        			}
        
        			if (catalogBean.getUpdatedByUserId() != null && catalogBean.getUpdatedByUserId().trim().length() > 0) {
        				proc.setString(14, catalogBean.getUpdatedByUserId().trim());
        			} else {
        				throw new DataAccessException("userId is null");
        			}
        			proc.registerOutParameter(15, oracle.jdbc.OracleTypes.VARCHAR);
        			proc.executeQuery();
        			
        			catalogBean.setCatalogId(proc.getString(15));
        			if (catalogBean.getWorkFlowAttributes() != null) {
        				manageWorkFlowAttributes(catalogBean.getCatalogId(), catalogBean.getUpdatedByUserId(), catalogBean.getWorkFlowAttributes());
        			} else {
        				throw new DataAccessException("userId is null");
        			}
        			
        			return catalogBean.getCatalogId();
        			
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
			throw new DataAccessException("Failed to insert vendor. PRIMARYSKU=[" + catalogBean.getPrimarySku() + "], MASTERVENDORID=[" + catalogBean.getMasterVendorId() + "]", e);
		}
	}


	/**
	 * Insert / Update work flow attribute based on Function Key passed.
	 * 
	 * @param catalogId
	 * @param userId
	 * @param workFlowAttributes
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	private static String manageWorkFlowAttributes(String catalogId, String userId, List<WorkFlowAttributeBean> workFlowAttributes) throws DataSourceLookupException, DataAccessException {
		
		try {

			final Connection conn = getConnection();
			try {
    			Iterator<WorkFlowAttributeBean> attributeIterator = workFlowAttributes.iterator();
    			while (attributeIterator.hasNext()) {
    				WorkFlowAttributeBean workFlowAttributeBean = attributeIterator.next();
    				if (workFlowAttributeBean != null) {
    					if (workFlowAttributeBean.getOperationFlag() == OperationFlag.UPDATE) {
    						final CallableStatement proc = conn.prepareCall("{call CATALOG.updateWorkflowAttributes(?, ?, ?, ?) }");
    						try {
        						if (workFlowAttributeBean.getAttributeName() != null
        								&& workFlowAttributeBean.getAttributeName()
        										.trim().length() > 0) {
        							proc.setString(1, workFlowAttributeBean
        									.getAttributeName().trim());
        						} else {
        							throw new DataAccessException(
        									"Work Flow Attribute is Missing");
        						}
        
        						if (workFlowAttributeBean.getAttributeValue() != null
        								&& workFlowAttributeBean.getAttributeValue()
        										.trim().length() > 0) {
        							proc.setString(2, workFlowAttributeBean
        									.getAttributeValue().trim());
        						} else {
        							throw new DataAccessException(
        									"Work Flow Attribute flag is Missing");
        						}
        
        						if (userId != null && userId.trim().length() > 0) {
        							proc.setString(3, userId.trim());
        						} else {
        							throw new DataAccessException("UserId is missing");
        						}
        
        						if (catalogId != null && catalogId.trim().length() > 0) {
        							proc.setString(4, catalogId.trim());
        						} else {
        							throw new DataAccessException(
        									"CatalogID is missing");
        						}
        						proc.executeQuery();
    						} finally {
    							if (proc != null) {
    								proc.close();
    							}
    						}
    					} else {
    						final CallableStatement proc = conn.prepareCall("{call CATALOG.insertWorkflowAttributes(?, ?, ?, ?) }");
    						try {
        						if (workFlowAttributeBean.getAttributeName() != null
        								&& workFlowAttributeBean.getAttributeName()
        										.trim().length() > 0) {
        							proc.setString(1, workFlowAttributeBean
        									.getAttributeName().trim());
        						} else {
        							throw new DataAccessException(
        									"Work Flow Attribute is Missing");
        						}
        
        						if (workFlowAttributeBean.getAttributeValue() != null
        								&& workFlowAttributeBean.getAttributeValue()
        										.trim().length() > 0) {
        							proc.setString(2, workFlowAttributeBean
        									.getAttributeValue().trim());
        						} else {
        							throw new DataAccessException(
        									"Work Flow Attribute flag is Missing");
        						}
        
        						if (userId != null && userId.trim().length() > 0) {
        							proc.setString(3, userId.trim());
        						} else {
        							throw new DataAccessException("UserId is missing");
        						}
        
        						if (catalogId != null && catalogId.trim().length() > 0) {
        							proc.setString(4, catalogId.trim());
        						} else {
        							throw new DataAccessException(
        									"CatalogID is missing");
        						}
        						proc.executeQuery();
    						} finally {
    							if (proc != null) {
    								proc.close();
    							}
    						}
    					}
    				}
    			}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to insert workflow attribute. CATALOGID=[" + catalogId + "]", e);
		}
		return catalogId;
	}

	/**
	 * Returns all catalog records
	 * 
	 * @return array of CatalogBeans
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<CatalogBean> getAll() throws DataSourceLookupException, DataAccessException {
		
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call CATALOG.getALLCatalog(?) }");
    			try {
        			proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(1);
        			try {
        				final List<CatalogBean> beans = new ArrayList<CatalogBean>();
            			if (rs != null) {
            				
            				while (rs.next()) {
            					final CatalogBean bean = new CatalogBean();
            					bean.setCatalogId(rs.getString(1));
            					bean.setMasterVendorId(rs.getString(2));
            					bean.setProductType(rs.getString(3));
            					bean.setVendorId(rs.getString(4));
            					bean.setPrimarySku(rs.getString(5));
            					bean.setPrimarySkuDescription(rs.getString(6));
            					bean.setVendorTriggerSku(rs.getString(7));
            					bean.setParentSku(rs.getString(8));
            					bean.setOfferType(rs.getString(9));
            					bean.setCategory(rs.getString(10));
            					bean.setSubCategory(rs.getString(11));
            					bean.setRole(rs.getInt(12));
            					bean.setRetryCount(rs.getInt(13));
            					bean.setCommSatTemplateId(rs.getString(14));
            					bean.setPreorderStatus(rs.getString(15));
            					bean.setPreorderStreetDate(rs.getTimestamp(16));
            					bean.setCreatedDate(rs.getTimestamp(17));
            					bean.setUpdatedDate(rs.getTimestamp(18));
            					bean.setCreatedByUserId(rs.getString(19));
            					bean.setUpdatedByUserId(rs.getString(20));
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
			throw new DataAccessException("Failed to retrieve catalog array.", e);
		}
	}

	/**
	 * Returns catalog records matching the input product sku
	 * (SDP_CATALOG.PRM_SKU_ID)
	 * 
	 * Note: any workflow attributes that have not been set in the catalog will
	 * be returned as null
	 * 
	 * @param productSku
	 * @return array of CatalogBeans
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<CatalogBean> getByProductSku(String productSku) throws DataSourceLookupException, DataAccessException {
		return getByArg("{call CATALOG.getCatalogByProductSku(?, ?) }", productSku);
	}

	/**
	 * Returns catalog records matching input vendor id
	 * 
	 * Note: any workflow attributes that have not been set in the catalog will
	 * be returned as null
	 * 
	 * @param vendorId
	 * @return array of CatalogBeans
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<CatalogBean> getByVendorId(String vendorId) throws DataSourceLookupException, DataAccessException {
		return getByArg("{call CATALOG.getCatalogByVendorId(?, ?) }", vendorId);
	}

	/**
	 * Returns catalog records matching the input type. For example, used to
	 * return list of all S2 products or all PSA products, etc...
	 * 
	 * Note: any workflow attributes that have not been set in the catalog will
	 * be returned as null
	 * 
	 * @param category
	 * @return array of CatalogBeans
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static List<CatalogBean> getByCategory(String category) throws DataSourceLookupException, DataAccessException {
		if (category == null) {
			throw new DataAccessException("Could not search by category because the value was null.");
		}
		return getByArg("{call CATALOG.getCatalogByType(?, ?) }", category);
	}
	
	private static List<CatalogBean> getByArg(String sqlCommand, String arg) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall(sqlCommand);
				try {
        			proc.setString(1, arg);
        			proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(2);
        			try {
            			final List<CatalogBean> beans = new ArrayList<CatalogBean>();
            			if (rs != null) {
            				while (rs.next()) {
            					final CatalogBean bean = new CatalogBean();
            					bean.setCatalogId(rs.getString(1));
            					bean.setMasterVendorId(rs.getString(2));
            					bean.setProductType(rs.getString(3));
            					bean.setVendorId(rs.getString(4));
            					bean.setPrimarySku(rs.getString(5));
            					bean.setPrimarySkuDescription(rs.getString(6));
            					bean.setVendorTriggerSku(rs.getString(7));
            					bean.setParentSku(rs.getString(8));
            					bean.setOfferType(rs.getString(9));
            					bean.setCategory(rs.getString(10));
            					bean.setSubCategory(rs.getString(11));
            					bean.setRole(rs.getInt(12));
            					bean.setRetryCount(rs.getInt(13));
            					bean.setCommSatTemplateId(rs.getString(14));
            					bean.setPreorderStatus(rs.getString(15));
            					bean.setPreorderStreetDate(rs.getTimestamp(16));
            					bean.setCreatedDate(rs.getTimestamp(17));
            					bean.setUpdatedDate(rs.getTimestamp(18));
            					bean.setCreatedByUserId(rs.getString(19));
            					bean.setUpdatedByUserId(rs.getString(20));
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
			throw new DataAccessException( "Failed to retrieve catalog array. ARG=[" + arg + "]", e);
		}
	}
	
	public static List<VendorCatalogBean> getVendorCatalogByPrimarySku(String primarySku) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareCall("select sdp_catalog.VNDR_ID, sdp_vendor_config.VNDR_NM, sdp_catalog.PRM_SKU_ID, sdp_catalog.DSP_NM, sdp_vendor_config.CAT from sdp_catalog inner join sdp_vendor_config on sdp_catalog.VNDR_ID = sdp_vendor_config.VNDR_ID where sdp_catalog.PRM_SKU_ID=?");
				try {
					stmt.setString(1, primarySku);
        			final ResultSet rs = stmt.executeQuery();
        			try {
            			final List<VendorCatalogBean> beans = new ArrayList<VendorCatalogBean>();
            			if (rs != null) {
            				while (rs.next()) {
            					final VendorCatalogBean bean = new VendorCatalogBean();
            					bean.setVendorId(rs.getString(1));
            					bean.setVendorName(rs.getString(2));
            					bean.setPrimarySku(rs.getString(3));
            					bean.setPrimarySkuDescription(rs.getString(4));
            					bean.setVendorCategory(rs.getString(5));
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
			throw new DataAccessException( "Failed to retrieve catalog array. ARG=[" + primarySku + "]", e);
		}
	}

	public static List<WorkFlowAttributeBean> getWorkFlowAttributes(String catalogId) throws DataSourceLookupException, DataAccessException {
		if (catalogId == null) {
			throw new DataAccessException("catalogId is Missing");
		}
		
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call Catalog.getWorkFlowAttributes(?, ?) }");
    			try {
        			proc.setString(1, catalogId);
        			proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(2);
        			try {
            			final List<WorkFlowAttributeBean> workFlowAttributeBeans = new ArrayList<WorkFlowAttributeBean>();
            			if (rs != null) {
            				while (rs.next()) {
            					final WorkFlowAttributeBean bean = new WorkFlowAttributeBean();
            					bean.setAttributeName(rs.getString(1));
            					bean.setAttributeValue(rs.getString(2));
            					bean.setOperationFlag(OperationFlag.UPDATE);
            					workFlowAttributeBeans.add(bean);
            				}
            			}
            			return workFlowAttributeBeans;
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
			throw new DataAccessException("Failed to retrieve catalog array. TYPE=[" + catalogId + "]", e);
		}
	}

	public static List<String> getProductIdByMasterVendorIdAndProductType(
			String masterVendorId, String productType)
			throws DataAccessException, DataSourceLookupException {
		return getProductId(
				"select ctlg_id from sdp_catalog where mstr_vndr_id=? and prod_typ=?",
				masterVendorId, productType);
	}

	public static List<String> getProductIdBySku(String primarySku)
			throws DataAccessException, DataSourceLookupException {
		return getProductId(
				"select ctlg_id from sdp_catalog where prm_sku_id=?",
				primarySku);
	}

	private static List<String> getProductId(String sqlCommand, String... args)
			throws DataAccessException, DataSourceLookupException {

		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn
						.prepareStatement(sqlCommand);
				try {
					if (args != null) {
						for (int i = 0; i < args.length; i++) {
							stmt.setString(i + 1, args[i]);
						}
					}
					final ResultSet rs = stmt.executeQuery();
					try {
						List<String> results = new ArrayList<String>();
						while (rs.next()) {
							results.add(rs.getString(1));
						}
						return results;
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
	}
	
	public static List<CatalogBean> getCatalogByMasterVndrIdAndProdType(String masterVendorId, String productType) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
				final CallableStatement proc = conn.prepareCall("{call CATALOG.getCatlogByMstrVndrIdProdType(?, ?, ?) }");
				try {
        			proc.setString(1, masterVendorId);
        			proc.setString(2, productType);
        			proc.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(3);
        			try {
            			final List<CatalogBean> beans = new ArrayList<CatalogBean>();
            			if (rs != null) {
            				while (rs.next()) {
            					final CatalogBean bean = new CatalogBean();
            					bean.setCatalogId(rs.getString(1));
            					bean.setMasterVendorId(rs.getString(2));
            					bean.setProductType(rs.getString(3));
            					bean.setVendorId(rs.getString(4));
            					bean.setPrimarySku(rs.getString(5));
            					bean.setPrimarySkuDescription(rs.getString(6));
            					bean.setVendorTriggerSku(rs.getString(7));
            					bean.setParentSku(rs.getString(8));
            					bean.setOfferType(rs.getString(9));
            					bean.setCategory(rs.getString(10));
            					bean.setSubCategory(rs.getString(11));
            					bean.setRole(rs.getInt(12));
            					bean.setRetryCount(rs.getInt(13));
            					bean.setCommSatTemplateId(rs.getString(14));
            					bean.setPreorderStatus(rs.getString(15));
            					bean.setPreorderStreetDate(rs.getTimestamp(16));
            					bean.setCreatedDate(rs.getTimestamp(17));
            					bean.setUpdatedDate(rs.getTimestamp(18));
            					bean.setCreatedByUserId(rs.getString(19));
            					bean.setUpdatedByUserId(rs.getString(20));
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
			throw new DataAccessException( "Failed to retrieve catalog array. masterVendorId=[" + masterVendorId + "] AND productType=["+productType+"]", e);
		}
	}
}