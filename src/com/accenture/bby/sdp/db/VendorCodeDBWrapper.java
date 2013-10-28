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
import com.accenture.bby.sdp.web.beans.VendorCodeBean;

public class VendorCodeDBWrapper extends SomSdpDBWrapper {
	/**
	 * Updates the vendor status code table.
	 * 
	 * @param currentVendorCodeRowId
	 * @param newVendorCode
	 * @param newVendorId
	 * @param newCanRetry
	 * @param newVendorCodeDescription
	 * @param userId id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static Integer update(Integer currentVendorCodeRowId, String newVendorCode, String newSdpCode, 
			String newVendorId, Boolean newCanRetry, String newVendorCodeDescription, String userId) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call VendorConfig.updateVendorCode(?, ?, ?, ?, ?, ?) }");
    			try {
    			
        			if(newVendorCode != null && newVendorCode.trim().length() > 0){
        				proc.setString(1,TextFilter.filter(newVendorCode.trim()));
        			}else{
        				proc.setString(1,null);
        			}
        			
        			if(newVendorId != null && newVendorId.trim().length() > 0){
        				proc.setString(2, TextFilter.filter(newVendorId.trim()));
        			}else{
        				throw new DataAccessException("New VendorId is Missing");
        			}
        			
        			if(newVendorCodeDescription != null && newVendorCodeDescription.trim().length() > 0){
        				proc.setString(3, TextFilter.filter(newVendorCodeDescription.trim()));
        			}else{
        				proc.setString(3, null);
        			}
        			
        			if (newCanRetry){
        				proc.setString(4, "Y");
        			}else{
        				proc.setString(4,"N");
        			}
        			if(userId != null && userId.trim().length() > 0){
        				proc.setString(5, TextFilter.filter(userId.trim()));
        			}else{
        				throw new DataAccessException("UserId is Missing");
        			}
        			
        			if (currentVendorCodeRowId != null){
        				proc.setInt(6, currentVendorCodeRowId);
        			}else{
        				throw new DataAccessException("Currnt VendorCodeRowId is Missing");
        			}
        			proc.executeQuery();
        			return currentVendorCodeRowId;
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
			throw new DataAccessException("Failed to update vendor code. ROWNUM=[" + currentVendorCodeRowId + "]", e);
		}
	}
	
	/**
	 * Deletes from the vendor status code table.
	 * 
	 * @param vendorCodeRowId
	 * @param userId id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String delete(String vendorCodeRowId, String userId) throws DataSourceLookupException, DataAccessException {
		
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call VendorConfig.deleteVendorCode(?, ?) }");
    			try {
        			if(userId != null && userId.trim().length() > 0){
        				proc.setString(1, TextFilter.filter(userId.trim()));
        			}else{
        				throw new DataAccessException("UserId is Missing");
        			}
        			
        			if(vendorCodeRowId != null){
        				proc.setInt(2, Integer.parseInt(vendorCodeRowId.trim()));
        			}else{
        				throw new DataAccessException("vendorCodeRowId is Missing");
        			}
        			proc.executeQuery();
        			return vendorCodeRowId;
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
			throw new DataAccessException("Failed to delete vendor code. ROWNUM=[" + vendorCodeRowId + "]", e);
		}
	}
	
	/**
	 * Inserts into the vendor status code table.
	 * 
	 * @param newVendorCode
	 * @param newVendorId
	 * @param newCanRetry
	 * @param newVendorCodeDescription
	 * @param userId id of the user who made the change
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static String insert(String newVendorCode, String newSdpCode, String newVendorId, Boolean newCanRetry, String newVendorCodeDescription, String userId) throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call VendorConfig.insertVendorCode(?, ?, ?, ?, ?, ?) }");
    			try {
        			if(newVendorCode != null && newVendorCode.trim().length() > 0){
        				proc.setString(1,TextFilter.filter(newVendorCode.trim()));
        			}else{
        				proc.setString(1,null);
        			}
        			
        			if(newVendorId != null && newVendorId.trim().length() > 0){
        				proc.setString(2, TextFilter.filter(newVendorId.trim()));
        			}else{
        				throw new DataAccessException("New VendorId is Missing");
        			}
        			
        			if(newVendorCodeDescription != null && newVendorCodeDescription.trim().length() > 0){
        				proc.setString(3, TextFilter.filter(newVendorCodeDescription.trim()));
        			}else{
        				proc.setString(3, null);
        			}
        			
        			if (newCanRetry)
        				proc.setString(4,"Y");
        			else
        				proc.setString(4,"N");
        			
        			if(userId != null && userId.trim().length() > 0){
        				proc.setString(5, TextFilter.filter(userId.trim()));
        			}else{
        				throw new DataAccessException("UserId is Missing");
        			}
        			proc.registerOutParameter(6, java.sql.Types.INTEGER);
        			proc.executeQuery();
        			return Integer.toString(proc.getInt(6)).trim();
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
			throw new DataAccessException("Failed to insert vendor code. VENDORCODE=[" + newVendorCode + "]", e);
		}
	}
	
	/**
	 * Returns all vendor status code records
	 * 
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static VendorCodeBean[] getAll() throws DataSourceLookupException, DataAccessException {
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call VendorConfig.getAllVendorCode(?) }");
    			try {
        			proc.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(1);
        			try {
        				List <VendorCodeBean> beans = new ArrayList<VendorCodeBean>();
            			if(rs != null){
            				while(rs.next()){
            					final VendorCodeBean bean = new VendorCodeBean();
            					bean.setVendorCodeRowId(rs.getInt(1));
            					bean.setVendorId(rs.getString(2));
            					bean.setVendorCode(rs.getString(3));
            					String s = rs.getString(4);
            					if (s != null)
            						s = s.trim();
            					bean.setCanRetry(s != null && s.equalsIgnoreCase("Y"));
            					bean.setVendorCodeDescription(rs.getString(5));
            					bean.setCreatedByUserId(rs.getString(6));
            					bean.setUpdatedByUserId(rs.getString(7));
            					bean.setCreatedDate(rs.getTimestamp(8));
            					bean.setUpdatedDate(rs.getTimestamp(9));
            					beans.add(bean);
            				}
            			}
            			return beans.toArray(new VendorCodeBean[beans.size()]);
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
			throw new DataAccessException("Failed to retrieve vendor code array.", e);
		}
	}
	
	/**
	 * Returns all vendor status code records of specified vendor id
	 * 
	* @param vendorId
	 * @return
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	public static VendorCodeBean[] getAllByVendorId(String vendorId) throws DataSourceLookupException, DataAccessException {
		if (!isNotNull(vendorId)) {
			throw new DataAccessException("Vendor ID is a mandatory parameter but was null.");
		}
		try {
			final Connection conn = getConnection();
			try {
    			final CallableStatement proc = conn.prepareCall("{call VendorConfig.getAllVendorCodeByVendorId(?, ?) }");
    			try {
        			proc.setString(1, vendorId.trim());
        			proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        			proc.executeQuery();
        			final ResultSet rs = (ResultSet) proc.getObject(2);
        			try {
        				final List<VendorCodeBean> beans = new ArrayList<VendorCodeBean>();
            			if(rs != null){
            				
            				while(rs.next()){
            					final VendorCodeBean bean = new VendorCodeBean();
            					bean.setVendorCodeRowId(rs.getInt(1));
            					bean.setVendorId(rs.getString(2));
            					bean.setVendorCode(rs.getString(3));
            					String s = rs.getString(4);
            					if (s != null)
            						s = s.trim();
            					bean.setCanRetry(s != null && s.equalsIgnoreCase("Y"));
            					bean.setVendorCodeDescription(rs.getString(5));
            					bean.setCreatedByUserId(rs.getString(6));
            					bean.setUpdatedByUserId(rs.getString(7));
            					bean.setCreatedDate(rs.getTimestamp(8));
            					bean.setUpdatedDate(rs.getTimestamp(9));
            					beans.add(bean);
            				}	
            			}
            			return beans.toArray(new VendorCodeBean[beans.size()]);
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
			throw new DataAccessException("Failed to retrieve vendor code array.", e);
		}
	}
	
	public static boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
