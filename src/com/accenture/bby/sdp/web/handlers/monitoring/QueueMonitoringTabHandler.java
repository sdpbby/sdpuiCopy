package com.accenture.bby.sdp.web.handlers.monitoring;

public class QueueMonitoringTabHandler {
	private static final int LOGIN_INDEX = 0;
	private static final int QUEUES_INDEX = 1;
	private int current_index = LOGIN_INDEX;
	
	public boolean isLoginTabCurrent() { return current_index == LOGIN_INDEX; }
	public boolean isQueuesTabCurrent() { return current_index == QUEUES_INDEX; }
	
	public void displayLoginTab() { this.current_index = LOGIN_INDEX; }
	public void displayQueuesTab() { this.current_index = QUEUES_INDEX; }
}
