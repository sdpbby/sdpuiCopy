package com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.VendorProvisioningStatusDBWrapper;
import com.accenture.bby.sdp.utl.Constants;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.xml.SdpTransactionDataXml;
import com.accenture.bby.sdp.web.beans.VendorProvisioningStatusBean;
import com.accenture.bby.sdp.web.handlers.BasePageHandler;
import com.accenture.bby.sdp.web.handlers.directToVendor.DirectToVendorHandler;
import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;

@SessionScoped
@ManagedBean (name="vendorProvisioningStatusHandler")
public class VendorProvisioningStatusHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String REQUEST_PARAM = "vpsId";
	private static final Logger logger = Logger.getLogger(VendorProvisioningStatusHandler.class.getName());
	
	private boolean hide = true;
	public boolean isHide() { return hide; }
	public String toggleHide() { hide = !hide; return NavigationStrings.CURRENT_VIEW; }
	
	private VendorProvisioningStatusBean data;
	private VendorProvisioningStatusDatatable vendorProvisioningStatusDatatable;
	private String vpsId;
	
	public boolean isOrderExists() {
		return getData() != null && data.getSdpOrderId() != null && !data.getSdpOrderId().equals(SdpTransactionDataXml.DEFAULT_NO_ORDER + "");
	}
	
	public String viewOrderButtonClick() {
		SdpOrderDetailHandler sdpOrderDetailHandler= BasePageHandler.getSdpOrderDetailHandler();
		sdpOrderDetailHandler.viewOrderDetailButtonClick(this.getData().getSdpOrderId());
		return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//		return NavigationStrings.getParameterizedUrl(NavigationStrings.SDP_ORDER_DETAIL_PAGE, SdpOrderDetailHandler.REQUEST_PARAM, this.getData().getSdpOrderId());
	}
		
public VendorProvisioningStatusHandler() {
		
	}
		
	public VendorProvisioningStatusHandler(String vpsID1) {
		// get the sdpOrderId param value from the context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.vpsId = facesContext.getExternalContext().getRequestParameterMap().get(REQUEST_PARAM);
		
		// if vpsId not found in context param, check the session.
		// if found in the context then store the vpsId in the session for later use.
		if (this.vpsId == null) {
			logger.log(Level.DEBUG, "vpsId variable not found in context param. checking session map.");
			this.vpsId = (String)facesContext.getExternalContext().getSessionMap().get(REQUEST_PARAM);
		} else {
			logger.log(Level.DEBUG, "vpsId variable found in context param. storing in session map.");
			facesContext.getExternalContext().getSessionMap().put(REQUEST_PARAM, vpsId);
		}
		
		logger.log(Level.DEBUG, "vpsId param passed=" + vpsId);
	}
	
	public VendorProvisioningStatusBean getData() {
		if (data == null) {
			logger.log(Level.DEBUG, "BEGIN Retrieving VPS ID=" + vpsId);
			
			if (vpsId == null) {
				throw new IllegalArgumentException("Invalid VPS ID: " + vpsId);
			}
			
			try {
				data = VendorProvisioningStatusDBWrapper.getVendorProvisioningStatusBeanByVendorProvisioningStatusId(vpsId);
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to retrieve VPS details from the database with VPS ID=" + vpsId);
				throw new IllegalStateException("Failed to retrieve VPS details from the database with VPS ID=" + vpsId);
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to data source.");
				throw new IllegalStateException("Failed to connect to data source.");
			}
			
			if (data == null) {
				logger.log(Level.DEBUG, "VPS search returned null.");
				throw new IllegalArgumentException("Invalid VPS ID: " + vpsId);
			}
			
			logger.log(Level.DEBUG, "FINISH Retrieving VPS ID=" + vpsId);
			try {
				AuditUtil.audit(Action.LOOKUP_VENDOR_DATA, data);
			} catch (AuditTrailException e) {
				logger.log(Level.ERROR, "Audit trail failure!");
				throw new IllegalStateException("Failed to connect to data source.");
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to capture audit log.");
				throw new IllegalStateException("Failed to connect to data source.");
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to connect to data source.");
				throw new IllegalStateException("Failed to connect to data source.");
			}
		}
		return data;
	}

	public void setData(VendorProvisioningStatusBean data) {
		if (data == null) {
			data = new VendorProvisioningStatusBean();
		} else {
			this.data = data;
		}
	}
	
	public String getSource() {
		if (getData().getRequestType().equals(SdpConfigProperties.getRequestTypeSendEmail())) {
			return "COMMSAT";
		} else {
			return "VENDOR";
		}
	}
	
	public String getVendorName() {
		return Constants.getVendorName(getData().getVendorId());
	}

	public VendorProvisioningStatusDatatable getVendorProvisioningStatusDatatable() {
		if (vendorProvisioningStatusDatatable == null) {
			vendorProvisioningStatusDatatable = new VendorProvisioningStatusDatatable(this.getData().getSdpId(), this.getData().getRequestType());
		}
		return vendorProvisioningStatusDatatable;
	}
	
	public boolean isReflowable() {
		if (this.getData().getRequestType() != null) {
			if (this.getData().getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeActivate()) && SdpConfigProperties.isCategoryReflowableInVendorRequest(this.getData().getCategory())) {
				return true;
			}
			if (this.getData().getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeCancel())&& SdpConfigProperties.isCategoryReflowableInVendorRequest(this.getData().getCategory())) {
				return true;
			}
			if (this.getData().getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeRenew())&& SdpConfigProperties.isCategoryReflowableInVendorRequest(this.getData().getCategory())) {
				return true;
			}
			if (this.getData().getRequestType().equalsIgnoreCase(SdpConfigProperties.getRequestTypeModifyStatus())&& SdpConfigProperties.isCategoryReflowableInVendorRequest(this.getData().getCategory())) {
				return true;
			}
		}
		return false;
	}
	
	public String reprocessButtonClick() {
		return NavigationStrings.getParameterizedUrl(NavigationStrings.VENDOR_REQUEST_DEFAULT_PAGE, DirectToVendorHandler.VPS_REQUEST_PARAM, this.vpsId);
	}
	
	public void viewVPSDetails(String rowNumber) {
		// get the sdpOrderId param value from the context
		vendorProvisioningStatusDatatable = null;
		data = null;
		this.vpsId = rowNumber;
	}
}
