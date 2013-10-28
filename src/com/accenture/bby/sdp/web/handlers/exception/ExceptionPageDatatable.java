package com.accenture.bby.sdp.web.handlers.exception;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.ExceptionPageDBWrapper;
import com.accenture.bby.sdp.utl.NavigationStrings;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.ExceptionResultBean;
import com.accenture.bby.sdp.web.extensions.SortableList;
import com.accenture.bby.sdp.web.handlers.ExceptionHandler;
import com.icesoft.faces.component.ext.HtmlDataTable;

@ManagedBean(name = "exceptionPageDatatable")
@ViewScoped
public class ExceptionPageDatatable extends SortableList implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(ExceptionPageDatatable.class.getName());

	protected  SimpleDateFormat DATE_FORMAT_MM_dd_yy = new SimpleDateFormat(
			"MM/dd/yy");
	private static String createDateColumnName = "Create Date";
	private static String sourceSystemColumnName = "Source System";
	private static String errorCodeColumnName = "Error Code";
	private static String descriptionColumnName = "Description";
	private static String sdpIdColumnName = "SDP ID";
	private static String actionColumnName = "Action";

	protected static int MAX_DAYS_RECENT_EXCEPTIONS = 30;
	final private static String SEARCH_BY_ALL = "SEARCH_BY_ALL";

	private ExceptionResultBean exceptionResultBean;

	public ExceptionPageDatatable() throws DataSourceLookupException,
			DataAccessException {
		super(createDateColumnName);
		if (this.getDateRangeStart() == null) {
			this.setDateRangeStart(new Date(System.currentTimeMillis()));
			if (this.getDateRangeEnd() == null) {
				this.setDateRangeEnd(new Date(System.currentTimeMillis() + (86400000)));
			}
		}
		
	}

	private HtmlDataTable dataTable;

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	private List<ExceptionResultBean> rows;

	public List<ExceptionResultBean> getRows()
			throws DataSourceLookupException, DataAccessException {
		if (rows == null) {
			this.setRows(getRecentExceptions(Integer.parseInt("25")));
		}
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}
		return rows;
	}

	public void setRows(List<ExceptionResultBean> rows) {
		this.rows = rows;
	}

	@Override
	public boolean isTableEmpty() {
		return rows == null || rows.size() == 0;
	}

	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	@Override
	protected void sort() {
		Comparator<ExceptionResultBean> comparator = new Comparator<ExceptionResultBean>() {
			@Override
			public int compare(ExceptionResultBean o1, ExceptionResultBean o2) {
				ExceptionResultBean c1 = o1;
				ExceptionResultBean c2 = o2;

				if (sortColumnName == null) {
					return 0;
				}
				if (sortColumnName.equals(createDateColumnName)) {
					return ascending ? ignoreNull(c1.getCreatedDate())
							.compareTo(ignoreNull(c2.getCreatedDate()))
							: ignoreNull(c2.getCreatedDate()).compareTo(
									ignoreNull(c1.getCreatedDate()));

				} else if (sortColumnName.equals(sourceSystemColumnName)) {
					return ascending ? ignoreNull(c1.getSourceSystem())
							.compareTo(ignoreNull(c2.getSourceSystem()))
							: ignoreNull(c2.getSourceSystem()).compareTo(
									ignoreNull(c1.getSourceSystem()));

				} else if (sortColumnName.equals(errorCodeColumnName)) {
					return ascending ? ignoreNull(c1.getCode()).compareTo(
							ignoreNull(c2.getCode()))
							: ignoreNull(c2.getCode()).compareTo(
									ignoreNull(c1.getCode()));

				} else if (sortColumnName.equals(descriptionColumnName)) {
					return ascending ? ignoreNull(c1.getDescription())
							.compareTo(ignoreNull(c2.getDescription()))
							: ignoreNull(c2.getDescription()).compareTo(
									ignoreNull(c1.getDescription()));

				} else if (sortColumnName.equals(sdpIdColumnName)) {
					return ascending ? ignoreNull(c1.getSdpId()).compareTo(
							ignoreNull(c2.getSdpId())) : ignoreNull(
							c2.getSdpId()).compareTo(ignoreNull(c1.getSdpId()));

				} else
					return 0;
			}
		};
		Collections.sort(rows, comparator);
	}

	protected static int MAX_QUERY_RESULTS = 500;
	protected static final String NULL_PARAMETER_PASSED = "null parameter passed";

	private int recentExceptionListSize;

	private String exceptionGroup;

	protected List<ExceptionResultBean> getRecentExceptions(final int maxRows)
			throws DataAccessException, DataSourceLookupException {

		if (!SEARCH_BY_ALL.equals(this.getExceptionGroup())) {
			return ExceptionPageDBWrapper
					.getSimpleRecentSdpExceptionListByMaxLimit(maxRows, this
							.getExceptionGroup(), MAX_DAYS_RECENT_EXCEPTIONS);
		} else {
			return ExceptionPageDBWrapper
					.getSimpleRecentSdpExceptionListByMaxLimit(maxRows,
							MAX_DAYS_RECENT_EXCEPTIONS);
		}

	}

	public String refreshListLinkClick() {
		try {
			if (this.getRecentExceptionListSize() != 0) {
				this.setRows(getRecentExceptions(this
						.getRecentExceptionListSize()));
			} else {
				this.setRows(getRecentExceptions(Integer.parseInt("25")));
			}

			this.getExceptionPageTabbedPaneHandler().displaySearchTab();
			return NavigationStrings.CURRENT_VIEW;
		} catch (DataAccessException e) {
			exceptionHandler.initialize(e,
					"Failure occurred while retrieving order list.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		} catch (DataSourceLookupException e) {
			exceptionHandler.initialize(e,
					"Failed to open data source connection.");
			logger.log(Level.ERROR, exceptionHandler.toString(), e);
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}

	public String searchbySdpIdExecuteButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (isNotNull(this.getSdpIdSearchField())) {
			if (TextFilter.isSpecialCharacterPresent(this
					.getSdpIdSearchField().trim())) {
				logger.log(Level.DEBUG, "Special Character Found (SdpId) ::"
						+ this.getSdpIdSearchField().trim());
				context.addMessage("exception_sdpid_search_form:search_sdp_id",
						new FacesMessage("Special characters not allowed."));
				return NavigationStrings.CURRENT_VIEW;
			}
			try {
				final String sdpIdParameter = this.getSdpIdSearchField().trim();
				this.setRows(ExceptionPageDBWrapper
						.getSimpleSdpExceptionListBySdpId(sdpIdParameter));
				this.getExceptionPageTabbedPaneHandler().displaySearchTab();
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context.addMessage("exception_sdpid_search_form:search_sdp_id", new FacesMessage(
				"Please enter valid SDP ID."));

		return NavigationStrings.CURRENT_VIEW;
	}

	public String searchByDateRangeButtonClick() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (this.getDateRangeStart() != null) {
			try {

				Date startDate = null;
				Date endDate = null;
				startDate = getDateRangeStart();

				if (this.getDateRangeEnd() != null) {
					endDate = this.getDateRangeEnd();
				} else {
					endDate = new Date(startDate.getTime() + 86400000);
					final String endDateStr = DATE_FORMAT_MM_dd_yy
							.format(endDate);
					endDate = DATE_FORMAT_MM_dd_yy.parse(endDateStr);

				}
				if (this.getDateRangeEnd() != null
						&& this.getDateRangeEnd().equals(
								this.getDateRangeStart())) {
					endDate = new Date(startDate.getTime() + 86400000);
					final String endDateStr = DATE_FORMAT_MM_dd_yy
							.format(endDate);
					endDate = DATE_FORMAT_MM_dd_yy.parse(endDateStr);
				}

				this.setRows(ExceptionPageDBWrapper
						.getSimpleSdpExceptionListByDateRange(startDate,
								endDate, MAX_DAYS_RECENT_EXCEPTIONS));
				this.getExceptionPageTabbedPaneHandler().displaySearchTab();
				return NavigationStrings.CURRENT_VIEW;
			} catch (DataAccessException e) {
				exceptionHandler.initialize(e,
						"Failure occurred while retrieving order list.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (DataSourceLookupException e) {
				exceptionHandler.initialize(e,
						"Failed to open data source connection.");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			} catch (ParseException e) {
				exceptionHandler.initialize(e, "Invalid Date");
				logger.log(Level.ERROR, exceptionHandler.toString(), e);
				return NavigationStrings.GENERAL_EXCEPTION_PAGE;
			}
		}
		context
				.addMessage("exception_date_search_form:search_strt_date",
						new FacesMessage(
								"Date [MM/dd/yyyy] parameters cannot be null."));

		return NavigationStrings.CURRENT_VIEW;

	}

//	public String viewButtonClick() {
//		try {
//			ExceptionResultBean selectedRow = (ExceptionResultBean) dataTable
//					.getRowData();
//			ExceptionResultBean detailRow = ExceptionPageDBWrapper
//					.getSdpExceptionDetailByExceptionId(selectedRow
//							.getExceptionId());
//
//			// selectedRow.setRequestMessage(detailRow.getRequestMessage());
//			// selectedRow.setStackTrace(detailRow.getStackTrace());
//			this.setExceptionResultBean(detailRow);
//			this.getExceptionPageTabbedPaneHandler().displayViewTab();
//			return NavigationStrings.CURRENT_VIEW;
//		} catch (DataAccessException e) {
//			exceptionHandler.initialize(e,
//					"Failure occurred while retrieving order list.");
//			logger.log(Level.ERROR, exceptionHandler.toString(), e);
//			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
//		} catch (DataSourceLookupException e) {
//			exceptionHandler.initialize(e,
//					"Failed to open data source connection.");
//			logger.log(Level.ERROR, exceptionHandler.toString(), e);
//			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
//		}
//	}

	public String viewButtonClick() {
		ExceptionResultBean selectedRow = (ExceptionResultBean) dataTable.getRowData();
		if (selectedRow != null) {
			return NavigationStrings.getParameterizedUrl(NavigationStrings.EXCEPTION_DETAIL_DEFAULT_PAGE, ExceptionPageHandler.REQUEST_PARAM, selectedRow.getExceptionId());
		} else {
			logger.log(Level.ERROR, "SelectedRow returned null so cannot prepare exception log detail page.");
			exceptionHandler.initialize(new NullPointerException("Selected row was null in exception history table."), "Unable to prepare exception log detail page.");
			return NavigationStrings.GENERAL_EXCEPTION_PAGE;
		}
	}
	private Integer maxRows = 20;

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	private Date dateRangeStart;
	private Date dateRangeEnd;

	public Date getDateRangeStart() {
		return dateRangeStart;
	}

	public void setDateRangeStart(Date dateRangeStart) {
		this.dateRangeStart = dateRangeStart;
	}

	public Date getDateRangeEnd() {
		return dateRangeEnd;
	}

	public void setDateRangeEnd(Date dateRangeEnd) {
		this.dateRangeEnd = dateRangeEnd;
	}

	private String sdpIdSearchField;

	private String filterByGroupSearchField;


	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public String getSdpIdSearchField() {
		return sdpIdSearchField;
	}

	public void setSdpIdSearchField(String sdpIdSearchField) {
		this.sdpIdSearchField = sdpIdSearchField;
	}

	public String getFilterByGroupSearchField() {
		return filterByGroupSearchField;
	}

	public void setFilterByGroupSearchField(String filterByGroupSearchField) {
		this.filterByGroupSearchField = filterByGroupSearchField;
	}

	private List<SelectItem> exceptionGroupList;

	public List<SelectItem> getExceptionGroupList() {
		try {
			exceptionGroupList = ExceptionPageDBWrapper
					.getExceptionGroupDropDownList();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataSourceLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exceptionGroupList;
	}

	/*
	 * Managed Properties
	 */
	@ManagedProperty(value = "#{exceptionPageTabbedPaneHandler}")
	private ExceptionPageTabbedPaneHandler exceptionPageTabbedPaneHandler;

	public ExceptionPageTabbedPaneHandler getExceptionPageTabbedPaneHandler() {
		return exceptionPageTabbedPaneHandler;
	}

	public void setExceptionPageTabbedPaneHandler(
			ExceptionPageTabbedPaneHandler exceptionPageTabbedPaneHandler) {
		this.exceptionPageTabbedPaneHandler = exceptionPageTabbedPaneHandler;
	}

	// @ManagedProperty(value = "#{exceptionPageHandler}")
	// private ExceptionPageHandler exceptionPageHandler;
	//
	// public ExceptionPageHandler getExceptionPageHandler() {
	// return exceptionPageHandler;
	// }
	//
	// public void setExceptionPageHandler(
	// ExceptionPageHandler exceptionPageHandler) {
	// this.exceptionPageHandler = exceptionPageHandler;
	// }

	@ManagedProperty(value = "#{exceptionHandler}")
	private ExceptionHandler exceptionHandler;

	public int getRecentExceptionListSize() {
		return recentExceptionListSize;
	}

	public void setRecentExceptionListSize(int recentExceptionListSize) {
		this.recentExceptionListSize = recentExceptionListSize;
	}

	public String getExceptionGroup() {
		return exceptionGroup;
	}

	public void setExceptionGroup(String exceptionGroup) {
		this.exceptionGroup = exceptionGroup;
	}

	public String getActionColumnName() {
		return actionColumnName;
	}

	public ExceptionResultBean getExceptionResultBean() {
		return exceptionResultBean;
	}

	public void setExceptionResultBean(ExceptionResultBean exceptionResultBean) {
		this.exceptionResultBean = exceptionResultBean;
	}

//	public String viewOrderDetailButtonClick() {
//		if (exceptionResultBean.getSdpOrderId() != null) {
//
//			FacesContext facesContext = FacesContext.getCurrentInstance();
//			facesContext.getExternalContext().getSessionMap().put(
//					SdpOrderDetailHandler.REQUEST_PARAM,
//					exceptionResultBean.getSdpOrderId());
//			return NavigationStrings.SDP_ORDER_DETAIL_PAGE;
//		}
//		return NavigationStrings.CURRENT_VIEW;
//	}

	public String getCreateDateColumnName() {
		return createDateColumnName;
	}

	public String getSourceSystemColumnName() {
		return sourceSystemColumnName;
	}

	public String getErrorCodeColumnName() {
		return errorCodeColumnName;
	}

	public String getDescriptionColumnName() {
		return descriptionColumnName;
	}

	public String getSdpIdColumnName() {
		return sdpIdColumnName;
	}

	public boolean isNotNull(String arg) {
		if (arg != null && arg.length() > 0) {
			return true;
		}
		return false;
	}
}
