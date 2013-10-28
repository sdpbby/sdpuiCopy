package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;

public abstract class SomSdpDBWrapper extends DBWrapper {
		
	protected static Connection getConnection() throws DataSourceLookupException, DataAccessException {
		return getConnection(SdpConfigProperties.getSomJNDI());
	}

	/**
	 * Helper method to convert from java.util.Date to java.sql.Date used in the date range query methods.
	 * 
	 * @param date java.util.Date
	 * @return date java.sql.Date or null if input param was null
	 */
	protected static java.sql.Date getSqlDate(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			try {
				return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(date)).getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException("The input value " + date + " could not be parsed to a date in the format yyyy-MM-dd.", e);
			}
		}
	}
	
	/**
	 * Helper method to convert from java.sql.Timestamp to java.util.Date used in the date range query methods.
	 * 
	 * @param date java.sql.Timestamp
	 * @return date java.util.Date or null if input param was null
	 */
	protected static java.util.Date getUtilDate(java.sql.Timestamp date) {
		if (date == null) {
			return null;
		} else {
			return new java.util.Date(date.getTime());
		}
	}
	
	protected static java.sql.Timestamp getSqlDateTime(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			try {
				return new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm a")
						.parse(new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date))
						.getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException(
						"The input value "
								+ date
								+ " could not be parsed to a date in the format yyyy-MM-dd.",
						e);
			}
		}
	}
}
