package com.accenture.bby.sdp.web.handlers.sdpOrderSearch;

import java.util.List;

import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.SdpOrderSearchDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;

public class SdpOrderSearchHelper {

	private static final Logger logger = Logger
			.getLogger(SdpOrderSearchResultsDatatable.class.getName());

	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	private List<SelectItem> vendorIdList;
	private List<SelectItem> orderStatusList;

	private boolean initializeByRecent;

	public boolean isInitializeByRecent() {
		return initializeByRecent;
	}

	public void setInitializeByRecent(boolean initializeByRecent) {
		this.initializeByRecent = initializeByRecent;
	}

	public SdpOrderSearchHelper(boolean initializeByRecent) {
		this.initializeByRecent = initializeByRecent;
	}

	public List<SelectItem> getVendorIdList() {
		try {
			if (vendorIdList == null || vendorIdList.size() < 1) {
				vendorIdList = SdpOrderSearchDBWrapper
						.getProvisioningVendorList();
			}
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		}
		return vendorIdList;
	}

	public List<SelectItem> getOrderStatusList() {
		try {
			orderStatusList = SdpOrderSearchDBWrapper.getSdpOrderStatusDropDownList();
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
		}
		return orderStatusList;
	}

}
