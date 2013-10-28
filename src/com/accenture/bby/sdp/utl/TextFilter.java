package com.accenture.bby.sdp.utl;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TextFilter {

	private static final Logger logger = Logger.getLogger(TextFilter.class.getName());
	private static final String MASK_CHAR = "*";

	public static String textValue(Boolean arg) {
		if (arg == null)
			return "";
		else if (arg)
			return "TRUE";
		else
			return "FALSE";
	}

	public static String textValue(String arg) {
		if (arg == null)
			return "";
		else
			return arg;
	}

	public static String textValue(Integer arg) {
		if (arg == null)
			return "";
		else
			return arg.toString();
	}

	public static String textValue(Date arg) {
		if (arg == null)
			return "";
		else
			return DateUtil.getExportDateString(arg);
	}

	/**
	 * Removes invalid characters from the input string. If the input is null or
	 * empty string then null is returned.
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		if (getInvalidCharacterRegex() == null) {
			logger.log(Level.WARN, "getInvalidCharacterRegex returned an empty string");
			if (str == null || str.trim().length() < 1) {
				return null;
			}
			return str.trim();
		}
		if (str == null || str.trim().length() < 1) {
			return null;
		} else {
			Pattern pattern = Pattern.compile(getInvalidCharacterRegex());
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				logger.log(Level.DEBUG, "Invalid characters caught by text filter >> " + str);
				str=str.replaceAll(getInvalidCharacterRegex(), "");
			}
			Pattern pattern2 = Pattern.compile(getInvalidCharacterRegexPartTwo());
			Matcher matcher2 = pattern2.matcher(str);
			if (matcher2.find()) {
				logger.log(Level.DEBUG, "Invalid characters caught by text filter >> " + str);
				str=str.replaceAll(getInvalidCharacterRegexPartTwo(), "");
			}
			return str.trim();
		}
	}
	
	/**
	 * Check for invalid characters from the input string. If the input is null or
	 * empty string then false is returned.
	 * 
	 * @param str
	 * @return
	 */
	
	public static boolean isSpecialCharacterPresent(String str) {
		boolean isSpecialChar = false;
		if (getInvalidCharacterRegex() == null) {
			if (str == null || str.trim().length() < 1) {
				logger.log(Level.DEBUG,"Parameter is NULL (str) ::" + str);
			}
		} else {
			if (str == null || str.trim().length() < 1) {
				logger.log(Level.DEBUG,"Parameter is NULL (str) ::" + str);
			} else {
				Pattern pattern = Pattern.compile(getInvalidCharacterRegex());
				Matcher matcher = pattern.matcher(str);
				isSpecialChar = matcher.find();
				logger.log(Level.DEBUG,"Special Character Found in String(str) ::" +isSpecialChar);
			}
		}
		if(!isSpecialChar){
			if (getInvalidCharacterRegexPartTwo() == null) {
				if (str == null || str.trim().length() < 1) {
					logger.log(Level.DEBUG,"Parameter is NULL (str) ::" + str);
				}
			} else {
				if (str == null || str.trim().length() < 1) {
					logger.log(Level.DEBUG,"Parameter is NULL (str) ::" + str);
				} else {
					Pattern pattern = Pattern.compile(getInvalidCharacterRegexPartTwo());
					Matcher matcher = pattern.matcher(str);
					isSpecialChar = matcher.find();
					logger.log(Level.DEBUG,"Special Character Found in String(str) ::" +isSpecialChar);
				}
			}
		}
		return isSpecialChar;
	}
	
	private static String getInvalidCharacterRegex() {
		// TODO Auto-generated method stub
//		return "[^\\w\\d\\s\\.,-;@]+";
		return "[^\\w\\d\\s.,-;@$]+";
	}
	
	private static String getInvalidCharacterRegexPartTwo(){
		return "[/:]+";
	}

	/**
	 * Replaces all characters in the first half of the string value with a mask
	 * character. If value is null or zero length then the value is returned
	 * unmodified. If value has length of 1 then replaces that character with
	 * the mask. If the string length % 2 == 1 then the first half of the string
	 * plus the next character will masked. Otherwise, the first half of the
	 * string will be masked.
	 * 
	 * @param value
	 *            string to mask
	 * @return masked string
	 */
	public static String getMaskedValue(String value) {
		if (value == null) {
			return null;
		}
		final int size = value.length();
		if (size == 0) {
			return value;
		} else if (size == 1) {
			return MASK_CHAR;
		} else {
			final int halfSize = (size / 2) + (size % 2 == 1 ? 1 : 0);
			final StringBuilder builder = new StringBuilder();
			for (int i = 0; i < halfSize; i++) {
				builder.append(MASK_CHAR);
			}
			return builder.toString() + value.substring(halfSize, size);
		}

	}
}
