package com.accenture.bby.sdp.utl.xml;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

public class XmlSchemaValidator {

	public static String getSchemaErrors(XmlObject xml) {
		ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		boolean isValid = xml.validate(validationOptions);
		
		if (!isValid) {
			
			StringBuilder builder = new StringBuilder();
			final int size = validationErrors.size();
			for (int i = 0; i < size; i++) {
				builder.append(">> " + validationErrors.get(i) + (i < size-1 ? "\n" : ""));
			}
			return builder.toString();
		}
		return null;
	}
}
