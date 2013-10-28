package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;
import java.util.Date;

import com.accenture.bby.sdp.utl.TextFilter;

public abstract class WebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2228012235763294413L;
	protected String textValue(Boolean arg) {
		return TextFilter.textValue(arg);
	}	
	protected String textValue(String arg) {
		return TextFilter.textValue(arg);
	}
	protected String textValue(Integer arg) {
		return TextFilter.textValue(arg);
	}
	protected String textValue(Date arg) {
		return TextFilter.textValue(arg);
	}
	/**
	 * removes invalid characters from the input string.
	 * if the input is null then null is returned.
	 * 
	 * @param str
	 * @return
	 */
	protected String filter(String str) {
		return TextFilter.filter(str);
	}
}
