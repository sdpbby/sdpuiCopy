package com.accenture.bby.sdp.utl.xml;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class XmlPrettyPrinter {
	
	private static final Logger logger = Logger.getLogger(XmlPrettyPrinter.class.getName());
	
	/**
	 * @param xml
	 * @return formatted xml string or original input string if unable to parse to xml or null if input is null
	 */
	public static String formatString(String xml) {
		if (xml == null) {
			return null;
		}
		try {
			XmlObject xmlObject = XmlObject.Factory.parse(xml);
			return xmlObject.toString();
		} catch (XmlException e) {
			logger.log(Level.WARN, "Input xml string was not well formed and could not be parsed. Returning original string.");
			logger.log(Level.DEBUG, "Invalid XML String:\n" + xml);
			return xml;
		}
	}
}
