package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.accenture.bby.sdp.web.handlers.UserManager;

@ManagedBean (name="pageHeaderBean")
@SessionScoped
public class PageHeaderBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty (value="#{userManagerBean}")
	private UserManager userManager;
	
	private static final String buildNum;
	static {
		buildNum = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("CUSTOM_VERSION");
	}
	
	public String getBuildNum() { return buildNum; }
	public UserManager getUserManager() { return userManager; }

	public void setUserManager(UserManager userManager) { this.userManager = userManager; }
	
}
