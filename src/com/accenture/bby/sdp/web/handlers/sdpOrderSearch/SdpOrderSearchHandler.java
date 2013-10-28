package com.accenture.bby.sdp.web.handlers.sdpOrderSearch;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;


@ViewScoped
@ManagedBean (name="sdpOrderSearchHandler")
public class SdpOrderSearchHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String LINEITEM_ID_PARAM_NAME = "lineItemId";
	
	private SdpOrderSearchResultsDatatable sdpOrderSearchResultsDatatable;
	
	private SdpOrderSearchHelper sdpOrderSearchHelper;
	
	// search fields
	private String lineItemId;
		
	public SdpOrderSearchHandler() {
		// all of the context params to pre-populate the search page
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.lineItemId = facesContext.getExternalContext().getRequestParameterMap().get(LINEITEM_ID_PARAM_NAME);
	}

	public SdpOrderSearchResultsDatatable getSdpOrderSearchResultsDatatable() {
		if (sdpOrderSearchResultsDatatable == null) {
			sdpOrderSearchResultsDatatable = new SdpOrderSearchResultsDatatable();
		}
		return sdpOrderSearchResultsDatatable;
	}

	public SdpOrderSearchHelper getSdpOrderSearchHelper() {
		if (sdpOrderSearchHelper == null) {
			sdpOrderSearchHelper = new SdpOrderSearchHelper(true);
		}
		return sdpOrderSearchHelper;
	}
}
