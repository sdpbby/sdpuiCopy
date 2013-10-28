package com.accenture.bby.sdp.utl.weblogic;

public class ServerStatusBean {
	private String serverName;
	private String serverType;
	private String serverState;
	private String serverHealth;
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the serverType
	 */
	public String getServerType() {
		return serverType;
	}
	/**
	 * @param serverType the serverType to set
	 */
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	/**
	 * @return the serverState
	 */
	public String getServerState() {
		return serverState;
	}
	/**
	 * @param serverState the serverState to set
	 */
	public void setServerState(String serverState) {
		this.serverState = serverState;
	}
	/**
	 * @return the serverHealth
	 */
	public String getServerHealth() {
		return serverHealth;
	}
	/**
	 * @param serverHealth the serverHealth to set
	 */
	public void setServerHealth(String serverHealth) {
		this.serverHealth = serverHealth;
	}
}
