package com.accenture.bby.sdp.web.beans;

import java.io.Serializable;

public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 345633514854391765L;
	private String username;
	private String uid;
	private boolean authenticated;
	private String roles;
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public boolean isAuthenticated() {
		return authenticated;
	}
	
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	@Override
	public String toString() {
		return "<UserBean" + 
			(" username=\"" + (username != null ? username : "[null]") + "\"") +
			(" uid=\"" + (uid != null ? uid : "[null]") + "\"") +
			(" roles=\"" + (roles != null ? roles : "[null]") + "\"") +
			(" authenticated=\"" + authenticated + "\"") +
			" />";
	}
}
