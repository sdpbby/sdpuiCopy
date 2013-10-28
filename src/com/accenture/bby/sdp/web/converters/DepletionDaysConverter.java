package com.accenture.bby.sdp.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter( value="depletionDaysConverter" )
public class DepletionDaysConverter implements Converter {
	public DepletionDaysConverter() {
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return getAsString(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (Float.valueOf(String.valueOf(value)).compareTo(Float.MAX_VALUE) == 0) {
			return "N/A";
		} else {
			return String.valueOf(value);
		}
	}
}
