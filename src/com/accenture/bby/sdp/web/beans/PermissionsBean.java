package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;

import com.accenture.bby.sdp.utl.SdpConfigProperties;

public class PermissionsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8525867164154441746L;
	private boolean viewPreorderAllowed;
	private boolean createPreorderAllowed;
	private boolean updatePreorderAllowed;
	private boolean deletePreorderAllowed;
	private boolean viewVendorAllowed;
	private boolean createVendorAllowed;
	private boolean updateVendorAllowed;
	private boolean deleteVendorAllowed;
	private boolean viewCatalogAllowed;
	private boolean createCatalogAllowed;
	private boolean updateCatalogAllowed;
	private boolean deleteCatalogAllowed;
	private boolean loadKeycodesAllowed;
	private boolean updateKeycodesAllowed;
	private boolean deleteKeycodesAllowed;
	private boolean viewKeycodesAllowed;
	private boolean createKcbCatalogAllowed;
	private boolean updateKcbCatalogAllowed;
	private boolean deleteKcbCatalogAllowed;
	private boolean viewKcbCatalogAllowed;
	private boolean displayPrimaryKeys;
	private boolean viewStackTraceAllowed;
	private boolean sendEmailAllowed;
	private boolean supportPageAccessAllowed;
	private boolean sdpReprocessAllowed;
	private boolean directToVendorAllowed;
	private boolean directToVendorOverrideAllowed;
	
	public boolean isSdpReprocessAllowed() {
		return sdpReprocessAllowed;
	}

	public void setSdpReprocessAllowed(boolean sdpReprocessAllowed) {
		this.sdpReprocessAllowed = sdpReprocessAllowed;
	}

	public boolean isDirectToVendorAllowed() {
		return directToVendorAllowed;
	}

	public void setDirectToVendorAllowed(boolean directToVendorAllowed) {
		this.directToVendorAllowed = directToVendorAllowed;
	}

	public boolean isDirectToVendorOverrideAllowed() {
		return directToVendorOverrideAllowed;
	}

	public void setDirectToVendorOverrideAllowed(
			boolean directToVendorOverrideAllowed) {
		this.directToVendorOverrideAllowed = directToVendorOverrideAllowed;
	}

	public boolean isSupportPageAccessAllowed() {
		return supportPageAccessAllowed;
	}

	public void setSupportPageAccessAllowed(boolean supportPageAccessAllowed) {
		this.supportPageAccessAllowed = supportPageAccessAllowed;
	}

	public boolean isViewStackTraceAllowed() {
		return viewStackTraceAllowed;
	}
	
	public boolean isDisplayStackTrace() {
		return viewStackTraceAllowed || SdpConfigProperties.isEnabledShowStackTraceAllUsers();
	}

	public void setViewStackTraceAllowed(boolean viewStackTraceAllowed) {
		this.viewStackTraceAllowed = viewStackTraceAllowed;
	}

	public boolean isDisplayPrimaryKeys() {
		return displayPrimaryKeys;
	}

	public void setDisplayPrimaryKeys(boolean displayPrimaryKeys) {
		this.displayPrimaryKeys = displayPrimaryKeys;
	}

	public boolean isViewPreorderAllowed() {
		return viewPreorderAllowed;
	}
	
	public void setViewPreorderAllowed(boolean viewPreorderAllowed) {
		this.viewPreorderAllowed = viewPreorderAllowed;
	}
	
	public boolean isCreatePreorderAllowed() {
		return createPreorderAllowed;
	}
	
	public void setCreatePreorderAllowed(boolean createPreorderAllowed) {
		this.createPreorderAllowed = createPreorderAllowed;
	}
	
	public boolean isUpdatePreorderAllowed() {
		return updatePreorderAllowed;
	}
	
	public void setUpdatePreorderAllowed(boolean updatePreorderAllowed) {
		this.updatePreorderAllowed = updatePreorderAllowed;
	}
	
	public boolean isDeletePreorderAllowed() {
		return deletePreorderAllowed;
	}
	
	public void setDeletePreorderAllowed(boolean deletePreorderAllowed) {
		this.deletePreorderAllowed = deletePreorderAllowed;
	}
	
	public boolean isViewVendorAllowed() {
		return viewVendorAllowed;
	}

	public void setViewVendorAllowed(boolean viewVendorAllowed) {
		this.viewVendorAllowed = viewVendorAllowed;
	}

	public boolean isCreateVendorAllowed() {
		return createVendorAllowed;
	}

	public void setCreateVendorAllowed(boolean createVendorAllowed) {
		this.createVendorAllowed = createVendorAllowed;
	}

	public boolean isUpdateVendorAllowed() {
		return updateVendorAllowed;
	}

	public void setUpdateVendorAllowed(boolean updateVendorAllowed) {
		this.updateVendorAllowed = updateVendorAllowed;
	}

	public boolean isDeleteVendorAllowed() {
		return deleteVendorAllowed;
	}

	public void setDeleteVendorAllowed(boolean deleteVendorAllowed) {
		this.deleteVendorAllowed = deleteVendorAllowed;
	}

	public boolean isViewCatalogAllowed() {
		return viewCatalogAllowed;
	}

	public void setViewCatalogAllowed(boolean viewCatalogAllowed) {
		this.viewCatalogAllowed = viewCatalogAllowed;
	}

	public boolean isCreateCatalogAllowed() {
		return createCatalogAllowed;
	}

	public void setCreateCatalogAllowed(boolean createCatalogAllowed) {
		this.createCatalogAllowed = createCatalogAllowed;
	}

	public boolean isUpdateCatalogAllowed() {
		return updateCatalogAllowed;
	}

	public void setUpdateCatalogAllowed(boolean updateCatalogAllowed) {
		this.updateCatalogAllowed = updateCatalogAllowed;
	}

	public boolean isDeleteCatalogAllowed() {
		return deleteCatalogAllowed;
	}

	public void setDeleteCatalogAllowed(boolean deleteCatalogAllowed) {
		this.deleteCatalogAllowed = deleteCatalogAllowed;
	}
	
	public boolean isSendEmailAllowed() {
		return sendEmailAllowed;
	}

	public void setSendEmailAllowed(boolean sendEmailAllowed) {
		this.sendEmailAllowed = sendEmailAllowed;
	}
	

	public boolean isLoadKeycodesAllowed() {
		return loadKeycodesAllowed;
	}

	public void setLoadKeycodesAllowed(boolean loadKeycodesAllowed) {
		this.loadKeycodesAllowed = loadKeycodesAllowed;
	}

	public boolean isUpdateKeycodesAllowed() {
		return updateKeycodesAllowed;
	}

	public void setUpdateKeycodesAllowed(boolean updateKeycodesAllowed) {
		this.updateKeycodesAllowed = updateKeycodesAllowed;
	}

	public boolean isDeleteKeycodesAllowed() {
		return deleteKeycodesAllowed;
	}

	public void setDeleteKeycodesAllowed(boolean deleteKeycodesAllowed) {
		this.deleteKeycodesAllowed = deleteKeycodesAllowed;
	}

	public boolean isViewKeycodesAllowed() {
		return viewKeycodesAllowed;
	}

	public void setViewKeycodesAllowed(boolean viewKeycodesAllowed) {
		this.viewKeycodesAllowed = viewKeycodesAllowed;
	}

	public boolean isCreateKcbCatalogAllowed() {
		return createKcbCatalogAllowed;
	}

	public void setCreateKcbCatalogAllowed(boolean createKcbCatalogAllowed) {
		this.createKcbCatalogAllowed = createKcbCatalogAllowed;
	}

	public boolean isUpdateKcbCatalogAllowed() {
		return updateKcbCatalogAllowed;
	}

	public void setUpdateKcbCatalogAllowed(boolean updateKcbCatalogAllowed) {
		this.updateKcbCatalogAllowed = updateKcbCatalogAllowed;
	}

	public boolean isDeleteKcbCatalogAllowed() {
		return deleteKcbCatalogAllowed;
	}

	public void setDeleteKcbCatalogAllowed(boolean deleteKcbCatalogAllowed) {
		this.deleteKcbCatalogAllowed = deleteKcbCatalogAllowed;
	}

	public boolean isViewKcbCatalogAllowed() {
		return viewKcbCatalogAllowed;
	}

	public void setViewKcbCatalogAllowed(boolean viewKcbCatalogAllowed) {
		this.viewKcbCatalogAllowed = viewKcbCatalogAllowed;
	}


	@Override
	public String toString() {
		return "<PermissionsBean" + 
		(" viewPreorderAllowed=\"" + viewPreorderAllowed + "\"") +
		(" createPreorderAllowed=\"" + createPreorderAllowed + "\"") +
		(" updatePreorderAllowed=\"" + updatePreorderAllowed + "\"") +
		(" deletePreorderAllowed=\"" + deletePreorderAllowed + "\"") +
		(" viewVendorAllowed=\"" + viewVendorAllowed + "\"") +
		(" createVendorAllowed=\"" + createVendorAllowed + "\"") +
		(" updateVendorAllowed=\"" + updateVendorAllowed + "\"") +
		(" deleteVendorAllowed=\"" + deleteVendorAllowed + "\"") +
		(" viewCatalogAllowed=\"" + viewCatalogAllowed + "\"") +
		(" createCatalogAllowed=\"" + createCatalogAllowed + "\"") +
		(" updateCatalogAllowed=\"" + updateCatalogAllowed + "\"") +
		(" deleteCatalogAllowed=\"" + deleteCatalogAllowed + "\"") +
		(" loadKeycodesAllowed=\"" + loadKeycodesAllowed + "\"") +
		(" updateKeycodesAllowed=\"" + updateKeycodesAllowed + "\"") +
		(" deleteKeycodesAllowed=\"" + deleteKeycodesAllowed + "\"") +
		(" viewKeycodesAllowed=\"" + viewKeycodesAllowed + "\"") +
		(" createKcbCatalogAllowed=\"" + createKcbCatalogAllowed + "\"") +
		(" updateKcbCatalogAllowed=\"" + updateKcbCatalogAllowed + "\"") +
		(" deleteKcbCatalogAllowed=\"" + deleteKcbCatalogAllowed + "\"") +
		(" viewKcbCatalogAllowed=\"" + viewKcbCatalogAllowed + "\"") +
		(" displayPrimaryKeys=\"" + displayPrimaryKeys + "\"") +
		(" viewStackTraceAllowed=\"" + viewStackTraceAllowed + "\"") +
		(" sendEmailAllowed=\"" + sendEmailAllowed + "\"") +
		(" supportPageAccessAllowed=\"" + supportPageAccessAllowed + "\"") +
		(" sdpReprocessAllowed=\"" + sdpReprocessAllowed + "\"") +
		(" directToVendorAllowed=\"" + directToVendorAllowed + "\"") +
		(" directToVendorOverrideAllowed=\"" + directToVendorOverrideAllowed + "\"") +
		" />";
	}
}
