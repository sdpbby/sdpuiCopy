package com.accenture.bby.sdp.utl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static final long MILLIS_IN_ONE_DAY = 1000 * 60 * 60 * 24;
	private static final String[] MONTHS = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	private DateUtil() {
		
	}
	
	
	public static String getLongDateString(Date date) {
		return new SimpleDateFormat(Constants.LONG_DATE_PATTERN).format(date);
	}
	
	public static String getCalendarDateString(Date date) {
		return new SimpleDateFormat(Constants.CALENDAR_DATE_PATTERN).format(date);
	}
	
	public static String getXmlDateString(Date date) {
		return new SimpleDateFormat(Constants.XML_DATE_PATTERN).format(date);
	}
	
	public static String getExportDateString(Date date) {
		return new SimpleDateFormat(Constants.EXPORT_DATE_PATTERN).format(date);
	}
	
	public static String getStoreDateString(Date date) {
		return new SimpleDateFormat(Constants.STORE_DATE_PATTERN).format(date);
	}
	
	public static String getShortXmlDateString(Date date) {
		return new SimpleDateFormat(Constants.XML_DATE_PATTERN_SHORT).format(date);
	}
	
	public static Date getShortXmlDate(String dateString) throws ParseException {
		return new SimpleDateFormat(Constants.XML_DATE_PATTERN_SHORT).parse(dateString);
	}
	
	
	public static Date clearTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Date(c.getTimeInMillis());
	}
	
	public static Date getNextDay(Date date) {
		return new Date(date.getTime() + MILLIS_IN_ONE_DAY);
	}
	
	public static Date getPreviousDay(Date date) {
		return new Date(date.getTime() - MILLIS_IN_ONE_DAY);
	}
	
	public static String[] getMonthsList() {
		return MONTHS;
	}

	public static int getMaxDaysInMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static String getIcdmBillingDateFromFulfillmentServices(String date) {
		if (date == null) {
			return null;
		}
		if (date.matches("[0][0-9]/[0-9][0-9]") || date.matches("[1][0-2]/[0-9][0-9]")) {
			// already in correct format
			return date;
		} else if (date.matches("\\d\\d\\d\\d-\\d\\d")) {
			return date.substring(5, 7) + '/' + date.substring(2, 4);
		} else {
			throw new IllegalArgumentException("Failed to convert date to ICDM format (MM/YY). Expected input string with pattern (\\d\\d\\d\\d-\\d\\d) but got >> " + date);
		}
	}
	
	public static String getFulfillmentServicesBillingDateFromIcdm(String date) {
		if (date == null) {
			return null;
		}
		if (date.matches("\\d\\d\\d\\d-\\d\\d")) {
			// already in correct format
			return date;
		} else if (date.matches("[0][0-9]/[0-9][0-9]") || date.matches("[1][0-2]/[0-9][0-9]")) {
			return "20" + date.substring(3, 5) + '-' + date.substring(0, 2);
		} else {
			throw new IllegalArgumentException("Failed to convert date to FulfillmentServices format YYYY-MM. Expected input string with pattern ([0][0-9]/[0-9][0-9] or [1][0-2]/[0-9][0-9]) but got >> " + date);
		}
	}

}
