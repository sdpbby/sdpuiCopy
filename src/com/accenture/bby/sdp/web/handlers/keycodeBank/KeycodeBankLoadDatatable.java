package com.accenture.bby.sdp.web.handlers.keycodeBank;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KeycodeBean;

@ManagedBean (name="keycodeBankLoadDatatable")
@ViewScoped
public class KeycodeBankLoadDatatable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String keycodeColumnName = "Invalid Record";
		
	public String getKeycodeColumnName() { return keycodeColumnName; }
	
	private KeycodeBean[] rowItemArray = new KeycodeBean[0];
	private DataModel<KeycodeBean> rows;
	
	public KeycodeBean[] getRowItemArray() { return rowItemArray; }
	public DataModel<KeycodeBean> getRows() throws DataSourceLookupException, DataAccessException { 
		
		if (this.getKeycodeBankLoadHandler().getValidator() != null) {
			rowItemArray = this.getKeycodeBankLoadHandler().getValidator().getInvalidKeycodes();
		} else {
			rowItemArray = new KeycodeBean[0];
		}
		rows = new ArrayDataModel<KeycodeBean>(rowItemArray);
		
		return rows; 
	}
	
	public void setRows(DataModel<KeycodeBean> rows) { this.rows = rows; }
	public void setRowItemArray(KeycodeBean[] rowItemArray) { this.rowItemArray = rowItemArray; }

	public boolean isTableEmpty() {
		return rowItemArray == null || rowItemArray.length == 0;
	}

	
	/*
	 * Managed Properties
	 */
	@ManagedProperty (value="#{keycodeBankLoadTabbedPaneHandler}")
	private KeycodeBankLoadTabbedPaneHandler keycodeBankLoadTabbedPaneHandler;
	public KeycodeBankLoadTabbedPaneHandler getKeycodeBankLoadTabbedPaneHandler() { return keycodeBankLoadTabbedPaneHandler; }
	public void setKeycodeBankLoadTabbedPaneHandler(KeycodeBankLoadTabbedPaneHandler keycodeBankLoadTabbedPaneHandler) { this.keycodeBankLoadTabbedPaneHandler = keycodeBankLoadTabbedPaneHandler; }
	
	@ManagedProperty (value="#{keycodeBankLoadHandler}")
	private KeycodeBankLoadHandler keycodeBankLoadHandler;
	public KeycodeBankLoadHandler getKeycodeBankLoadHandler() { return keycodeBankLoadHandler; }
	public void setKeycodeBankLoadHandler(KeycodeBankLoadHandler keycodeBankLoadHandler) { this.keycodeBankLoadHandler = keycodeBankLoadHandler; }
	
	public String loadNowButtonClick() {
		rowItemArray = this.getKeycodeBankLoadHandler().getValidator().getInvalidKeycodes();
		rows = new ArrayDataModel<KeycodeBean>(rowItemArray);
		return this.getKeycodeBankLoadHandler().loadNow();
	}
	
}
