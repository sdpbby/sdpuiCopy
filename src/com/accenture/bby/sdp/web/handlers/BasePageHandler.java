package com.accenture.bby.sdp.web.handlers;

import javax.faces.context.FacesContext;

import com.accenture.bby.sdp.web.handlers.sdpOrderDetail.SdpOrderDetailHandler;
import com.accenture.bby.sdp.web.handlers.vendorProvisioningStatus.VendorProvisioningStatusHandler;

public class BasePageHandler {

	private static final String VPS_HANDLER = "vendorProvisioningStatusHandler";
	private static final String SDPORDERDETAIL_HANDLER = "sdpOrderDetailHandler";

	public static VendorProvisioningStatusHandler getVPSHandler() {

		VendorProvisioningStatusHandler vpsHandler = (VendorProvisioningStatusHandler) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
						VPS_HANDLER);
		if (vpsHandler == null) {
			vpsHandler = new VendorProvisioningStatusHandler();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put(VPS_HANDLER, vpsHandler);

		}
		return vpsHandler;
	}

	public static SdpOrderDetailHandler getSdpOrderDetailHandler() {
		SdpOrderDetailHandler sdpOrderDetailHandler = (SdpOrderDetailHandler) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				SDPORDERDETAIL_HANDLER);
		if (sdpOrderDetailHandler == null) {
			sdpOrderDetailHandler = new SdpOrderDetailHandler();
			FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().
				put(SDPORDERDETAIL_HANDLER, sdpOrderDetailHandler);
		}
		return sdpOrderDetailHandler;
	}
}
