package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;

public abstract class DBWrapper {
	
	private static final Logger logger = Logger.getLogger(DBWrapper.class.getName());
	
	/**
	 * Gets a database connection using JNDI name as input
	 * 
	 * @param jndiName JNDI name of targeted data source
	 * @return Connection to data source
	 * @throws DataSourceLookupException if JNDI lookup fails
	 * @throws DataAccessException if unable to get database connection (SQLException is thrown)
	 */
	protected static Connection getConnection(String jndiName) throws DataSourceLookupException, DataAccessException {
		if (jndiName == null) {
			logger.log(Level.FATAL, "Input JNDI name was null. JNDI=[null]");
			throw new DataSourceLookupException("DataSource lookup failed. JNDI=[null]");
		}
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(jndiName);
			if (dataSource == null) {
				logger.log(Level.FATAL, "Context data source lookup returned null. JNDI=[" + jndiName + "]");
				throw new DataSourceLookupException("No data source. See server logs for details.");
			} else {
				return dataSource.getConnection();
			}
		} catch (NamingException e) {
			logger.log(Level.FATAL, "DataSource lookup failed. JNDI=[" + jndiName + "]", e);
			throw new DataSourceLookupException("DataSource lookup failed. See server logs for details.", e);
		} catch (SQLException e) {
			logger.log(Level.FATAL, "No connection. JNDI=[" + jndiName + "]", e);
			throw new DataAccessException("No connection. See server logs for details.", e);
		}
	}
	
	/**
	 * Converts java.sql.Date to java.util.Date
	 * 
	 * @param date java.sql.Date
	 * @return java.util.Date
	 */
	protected static java.util.Date getConvertedDate(java.sql.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.util.Date(date.getTime());
		}
	}
	
	/**
	 * Converts java.util.Date to java.sql.Date
	 * 
	 * @param date java.util.Date
	 * @return java.sql.Date
	 */
	protected static java.sql.Date getConvertedDate(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.sql.Date(date.getTime());
		}
	}
	
	/**
	 * Converts java.util.Date to java.sql.Timestamp
	 * 
	 * @param date java.util.Date
	 * @return java.sql.Date
	 */
	protected static java.sql.Timestamp getConvertedTimestamp(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			return new java.sql.Timestamp(date.getTime());
		}
	}
}
