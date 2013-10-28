package com.accenture.bby.sdp.web.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.PermissionsBean;
import com.accenture.bby.sdp.web.beans.UserBean;


@ManagedBean (name="userManagerBean")
@SessionScoped
public class UserManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserManager.class.getName());
	
	public static final String MONITOR = "Monitor";
	public static final String OPSUISER = "OpsUser";
	public static final String OPSADMIN = "OpsAdmin";
	public static final String ECCUSER = "ECCUser";
	private static final String NO_ROLES = "";
	
	private boolean eccUserRole = false;
	private boolean opsUserRole = false;
	private boolean opsAdminRole = false;
	private boolean monitorRole = false;

	public boolean isEccUserRole() { return eccUserRole; }
	public boolean isOpsUserRole() { return opsUserRole; }
	public boolean isOpsAdminRole() { return opsAdminRole; }
	public boolean isMonitorRole() { return monitorRole; }
	
	/**
	 * @return true unless user is only in ECCUser role
	 */
	public boolean isDisplayNavigationLinks() {
		return !(isEccUserRole() && !(isOpsUserRole() || isOpsAdminRole() || isMonitorRole()));
	}
	
	private PermissionsBean permissionsBean;

	private UserBean userBean;
	
	public UserManager() {
		
		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) external.getRequest();
		
		
		userBean = new UserBean();
		userBean.setUsername(request.getRemoteUser());
		
		
		permissionsBean = new PermissionsBean();
		List<String> roles = new ArrayList<String>();
		if (request.isUserInRole(MONITOR)) {
			monitorRole = true;
			permissionsBean.setDisplayPrimaryKeys(true);
			permissionsBean.setCreatePreorderAllowed(true);
			permissionsBean.setDeletePreorderAllowed(true);
			permissionsBean.setUpdatePreorderAllowed(true);
			permissionsBean.setViewPreorderAllowed(true);
			permissionsBean.setCreateVendorAllowed(true);
			permissionsBean.setDeleteVendorAllowed(true);
			permissionsBean.setUpdateVendorAllowed(true);
			permissionsBean.setViewVendorAllowed(true);
			permissionsBean.setCreateCatalogAllowed(true);
			permissionsBean.setDeleteCatalogAllowed(true);
			permissionsBean.setUpdateCatalogAllowed(true);
			permissionsBean.setViewCatalogAllowed(true);
			permissionsBean.setViewStackTraceAllowed(true);
			permissionsBean.setSendEmailAllowed(true);
			permissionsBean.setLoadKeycodesAllowed(true);
			permissionsBean.setUpdateKeycodesAllowed(true);
			permissionsBean.setDeleteKeycodesAllowed(true);
			permissionsBean.setViewKeycodesAllowed(true);
			permissionsBean.setCreateKcbCatalogAllowed(true);
			permissionsBean.setUpdateKcbCatalogAllowed(true);
			permissionsBean.setDeleteKcbCatalogAllowed(true);
			permissionsBean.setViewKcbCatalogAllowed(true);
			permissionsBean.setSupportPageAccessAllowed(true);
			permissionsBean.setSdpReprocessAllowed(true);
			permissionsBean.setDirectToVendorAllowed(true);
			permissionsBean.setDirectToVendorOverrideAllowed(true);
			roles.add(MONITOR);
		}	
		if (request.isUserInRole(OPSUISER)) {
			opsUserRole = true;
			permissionsBean.setViewPreorderAllowed(true);
			permissionsBean.setViewVendorAllowed(true);
			permissionsBean.setViewCatalogAllowed(true);
			permissionsBean.setViewKeycodesAllowed(true);
			permissionsBean.setViewKcbCatalogAllowed(true);
			permissionsBean.setSdpReprocessAllowed(true);
			permissionsBean.setDirectToVendorAllowed(true);
			roles.add(OPSUISER);
		}
		if (request.isUserInRole(OPSADMIN)) {
			opsAdminRole = true;
			permissionsBean.setCreatePreorderAllowed(true);
			permissionsBean.setDeletePreorderAllowed(true);
			permissionsBean.setUpdatePreorderAllowed(true);
			permissionsBean.setViewPreorderAllowed(true);
			permissionsBean.setViewVendorAllowed(true);
			permissionsBean.setCreateCatalogAllowed(true);
			permissionsBean.setDeleteCatalogAllowed(true);
			permissionsBean.setUpdateCatalogAllowed(true);
			permissionsBean.setViewCatalogAllowed(true);
			permissionsBean.setSendEmailAllowed(true);
			permissionsBean.setLoadKeycodesAllowed(true);
			permissionsBean.setUpdateKeycodesAllowed(true);
			permissionsBean.setDeleteKeycodesAllowed(true);
			permissionsBean.setViewKeycodesAllowed(true);
			permissionsBean.setCreateKcbCatalogAllowed(true);
			permissionsBean.setUpdateKcbCatalogAllowed(true);
			permissionsBean.setDeleteKcbCatalogAllowed(true);
			permissionsBean.setViewKcbCatalogAllowed(true);
			permissionsBean.setSdpReprocessAllowed(true);
			permissionsBean.setDirectToVendorAllowed(true);
			permissionsBean.setDirectToVendorOverrideAllowed(true);
			roles.add(OPSADMIN);
		}
		if (request.isUserInRole(ECCUSER)) {
			eccUserRole = true;
			permissionsBean.setSendEmailAllowed(true);
			roles.add(ECCUSER);
		}
		int size = roles.size();
		if (size < 1) {
			userBean.setRoles(NO_ROLES);
		} else if (size == 1) {
			userBean.setRoles(roles.get(0));
		} else {
			StringBuilder roleStr = new StringBuilder();
			roleStr.append(roles.get(0));
			for (int i = 1; i < size; i++) {
				roleStr.append(", " + roles.get(i));
			}
			userBean.setRoles(roleStr.toString());
		}
		
		// audit trail
		try {
			AuditUtil.createSessionLog(this);
		} catch (DataSourceLookupException e) {
			logger.log(Level.ERROR, "Unable to connect to data source. Audit trail session creation failed.", e);
			throw new IllegalStateException("Unable to connect to data source. Audit trail session creation failed.", e);
		} catch (DataAccessException e) {
			logger.log(Level.ERROR, "Unable to create audit log session.", e);
			throw new IllegalStateException("Unable to create audit log session.", e);
		}
	}

	public PermissionsBean getPermissionsBean() {
		return permissionsBean;
	}

	public void setPermissionsBean(PermissionsBean permissionsBean) {
		this.permissionsBean = permissionsBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<UserManager>\n");
		if (userBean != null) builder.append("  " + userBean.toString() + "\n");
		if (permissionsBean != null) builder.append("  " + permissionsBean.toString() + "\n");
		builder.append("</UserManager>");
		return builder.toString();
	}
	
}
