package com.accenture.bby.sdp.db;

import java.sql.Connection;

import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;

public abstract class KcbDBWrapper extends DBWrapper {
	
		
	protected static Connection getConnection() throws DataSourceLookupException, DataAccessException {
		return getConnection(SdpConfigProperties.getKcbJNDI());
	}
}
