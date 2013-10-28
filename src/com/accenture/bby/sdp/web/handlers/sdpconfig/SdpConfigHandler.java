package com.accenture.bby.sdp.web.handlers.sdpconfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.UtilityDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.SdpConfigProperties;
import com.accenture.bby.sdp.utl.audit.Action;
import com.accenture.bby.sdp.utl.audit.AuditUtil;
import com.accenture.bby.sdp.utl.exceptions.AuditTrailException;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SdpPropertyBean;
import com.accenture.bby.sdp.web.extensions.SortableList;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ViewScoped
@ManagedBean(name = "sdpConfigHandler")
public class SdpConfigHandler extends SortableList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4625310818404937698L;
	private static final Logger logger = Logger
			.getLogger(SdpConfigHandler.class.getName());
	private HtmlDataTable sdpConfigTable;
	private List<SdpPropertyBean> properties;
	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	private static String propNameColumnName = "Property Name";
	private static String propValueColumnName = "Property Value";
	private static String sysdateColumnName = "Created Date";

	private HtmlDataTable dataTable;

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public String getPropNameColumnName() {
		return propNameColumnName;
	}

	public String getPropValueColumnName() {
		return propValueColumnName;
	}

	public String getSysdateColumnName() {
		return sysdateColumnName;
	}

	public SdpConfigHandler() {
		super(propNameColumnName);
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public HtmlDataTable getSdpConfigTable() {
		return sdpConfigTable;
	}

	public void setSdpConfigTable(HtmlDataTable sdpConfigTable) {
		this.sdpConfigTable = sdpConfigTable;
	}

	public List<SdpPropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(List<SdpPropertyBean> properties) {
		this.properties = properties;
	}

	/**
	 * Refreshes OpsUI properties cache and fetches a list of all properties to
	 * be displayed in the UI
	 * 
	 * @return navigation string
	 */
	public String retrievePropertiesFromDatabase() {
		try {
			properties = UtilityDBWrapper.getSdpConfigProperties();
			logger.log(Level.DEBUG, "Reloaded properties. Found >> "
					+ properties.size());
		} catch (DataSourceLookupException e) {
			exceptionHandler
					.initialize(e,
							"Failure occurred while retrieving sdp-config properties from database.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler
					.initialize(e,
							"Failure occurred while retrieving sdp-config properties from database.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
		return NavigationStrings.CURRENT_VIEW;
	}

	/**
	 * Updates UI property cache
	 * 
	 * @return navigation string
	 */
	public String forcePropertiesRefresh() {
		SdpConfigProperties.loadSdpConfigProperties();
		return NavigationStrings.CURRENT_VIEW;
	}

	/**
	 * Replaces all properties in the database with properties in the
	 * sdp-config.properties file stored in the sdp-opsui or sdp-opsui-b domain.
	 * THIS CANNOT BE UNDONE!
	 * 
	 * @return navigation string
	 */
	public String insertPropertiesToDatabaseFromFile() {
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			@SuppressWarnings("rawtypes")
			Map context = FacesContext.getCurrentInstance()
					.getExternalContext().getInitParameterMap();

			File sdpConfigFile = new File((String) context
					.get("sdp-config-location"));
			if (!sdpConfigFile.exists()) {
				throw new IllegalStateException(
						"sdp-config file does not exist at expected location: "
								+ sdpConfigFile.getAbsolutePath());
			}

			Properties props = new Properties();

			fileInputStream = new FileInputStream(sdpConfigFile);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			props.load(bufferedInputStream);

			bufferedInputStream.close();
			fileInputStream.close();

			UtilityDBWrapper.loadSdpConfigPropertiesIntoDatabase(props);

			AuditUtil.audit(Action.REFRESHED_SDPCONFIG);

			SdpConfigProperties.loadSdpConfigProperties();

			return retrievePropertiesFromDatabase();
		} catch (AuditTrailException e) {
			exceptionHandler.initialize(e, "Failed to capture audit log.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e, "Failed to update database.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e, "Failed to connect to data source.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (FileNotFoundException e) {
			exceptionHandler.initialize(e, "Properties file not found.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (IOException e) {
			exceptionHandler.initialize(e, "Failed to read properties file.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				exceptionHandler.initialize(e,"Failed to read properties file.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}

	}

	@Override
	protected void sort() {
		Comparator<SdpPropertyBean> comparator = new Comparator<SdpPropertyBean>() {
			@Override
			public int compare(SdpPropertyBean o1, SdpPropertyBean o2) {
				SdpPropertyBean c1 = o1;
				SdpPropertyBean c2 = o2;

				if (sortColumnName == null) {
					return 0;
				}
				if (sortColumnName.equals(propNameColumnName)) {
					return ascending ? c1.getPropertyName().compareTo(
							c2.getPropertyName()) : c2.getPropertyName()
							.compareTo(c1.getPropertyName());

				} else if (sortColumnName.equals(propValueColumnName)) {
					return ascending ? c1.getPropertyValue().compareTo(
							c2.getPropertyValue()) : c2.getPropertyValue()
							.compareTo(c1.getPropertyValue());

				} else
					return 0;
			}
		};
		Collections.sort(rows, comparator);
	}

	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	@Override
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}

	private List<SdpPropertyBean> rows;

	public List<SdpPropertyBean> getRows() throws DataSourceLookupException,
			DataAccessException {
		if (rows == null) {
			rows = UtilityDBWrapper.getSdpConfigProperties();
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows;
	}

	public Integer getPropertiesCount() throws DataSourceLookupException,
			DataAccessException {
		if (rows == null) {
			getRows();
		}
		if (rows == null) {
			return 0;
		}
		return getRows().size();
	}

	public void setRows(List<SdpPropertyBean> rows) {
		this.rows = rows;
	}

}
