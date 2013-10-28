package com.accenture.bby.sdp.utl.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;

import com.accenture.bby.sdp.utl.Constants;

public class ExceptionUtil {
	
	private static final String DATA_SOURCE_LOOKUP_EXCEPTION = "An error has occurred while connecting with the database.";
	private static final String DATA_ACCESS_EXCEPTION = "An error has occurred while calling with the database.";
	private static final String AUDIT_TRAIL_EXCEPTION = "An error has occurred while capturing an audit log.";
	private static final String AUDIT_TRAIL_DATA_ACCESS_EXCEPTION = "An error has occurred while capturing an audit log.";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION = "An input parameter had an invalid value.";
	private static final String ILLEGAL_STATE_EXCEPTION = "The application has encountered an unexpected state.";
	private static final String OTHER_EXCEPTION = "An unexpected error has occurred.";
	
	public static String getCustomMessage(Throwable throwable) {
		if (throwable instanceof DataSourceLookupException) {
			return DATA_SOURCE_LOOKUP_EXCEPTION;
		} else if (throwable instanceof DataAccessException) {
			return DATA_ACCESS_EXCEPTION;
		} else if (throwable instanceof AuditTrailException) {
			return AUDIT_TRAIL_EXCEPTION;
		} else if (throwable instanceof AuditTrailDataAccessException) {
			return AUDIT_TRAIL_DATA_ACCESS_EXCEPTION;
		} else if (throwable instanceof IllegalArgumentException) {
			return ILLEGAL_ARGUMENT_EXCEPTION;
		} else if (throwable instanceof IllegalStateException) {
			return ILLEGAL_STATE_EXCEPTION;
		} else {
			return OTHER_EXCEPTION;
		}
	}
	
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		fillStackTrace(throwable, pw);
		return sw.toString();
	}
	
	private static void fillStackTrace(Throwable t, PrintWriter w) {
		if (t == null) {
			return;
		}
		t.printStackTrace(w);
		if (t instanceof ServletException) {
			Throwable cause = ((ServletException) t).getRootCause();
			if (cause != null) {
				w.println("Root cause:");
				fillStackTrace(cause, w);
			}
		} else if (t instanceof SQLException) {
			Throwable cause = ((SQLException) t).getNextException();
			if (cause != null) {
				w.println("Next Exception:");
				fillStackTrace(cause, w);
			}
		} else {
			Throwable cause = t.getCause();
			if (cause != null) {
				w.println("Cause:");
				fillStackTrace(cause, w);
			}
		}
	}
	
	public static String formatDate(Date date) {
		return new SimpleDateFormat(Constants.XML_DATE_PATTERN).format(date);
	}
}
