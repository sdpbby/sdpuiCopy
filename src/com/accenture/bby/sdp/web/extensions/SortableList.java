package com.accenture.bby.sdp.web.extensions;

import java.io.Serializable;
import java.util.Date;


/**
 * The SortableList class is a utility class
 */
public abstract class SortableList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7067305724310544540L;
	protected String sortColumnName;
    protected boolean ascending;

    // we only want to resort if the order or column has changed.
    protected String oldSort;
    protected boolean oldAscending;


    protected SortableList(String defaultSortColumn) {
        sortColumnName = defaultSortColumn;
        ascending = isDefaultAscending(defaultSortColumn);
        oldSort = sortColumnName;
        // make sure sortColumnName on first render
        oldAscending = !ascending;
    }

    /**
     * Sort the list.
     */
    protected abstract void sort();

    /**
     * Is the default sortColumnName direction for the given column "ascending" ?
     */
    protected abstract boolean isDefaultAscending(String sortColumn);
    
    /**
     * Is the table empty?
     */
    public abstract boolean isTableEmpty();

    /**
     * Gets the sortColumnName column.
     *
     * @return column to sortColumnName
     */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /**
     * Sets the sortColumnName column
     *
     * @param sortColumnName column to sortColumnName
     */
    public void setSortColumnName(String sortColumnName) {
        oldSort = this.sortColumnName;
        this.sortColumnName = sortColumnName;

    }

    /**
     * Is the sortColumnName ascending.
     *
     * @return true if the ascending sortColumnName otherwise false.
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Set sortColumnName type.
     *
     * @param ascending true for ascending sortColumnName, false for desending sortColumnName.
     */
    public void setAscending(boolean ascending) {
        oldAscending = this.ascending;
        this.ascending = ascending;
    }
    
    protected String ignoreNull(String arg) {
    	if (arg == null) return "";
    	else return arg;
    }
    
    protected Integer ignoreNull(Integer arg) {
    	if (arg == null) return Integer.MIN_VALUE;
    	else return arg;
    }
    
    protected Long ignoreNull(Long arg) {
    	if (arg == null) return Long.MIN_VALUE;
    	else return arg;
    }
    
    protected Float ignoreNull(Float arg) {
    	if (arg == null) return Float.MIN_VALUE;
    	else return arg;
    }
    
    protected Date ignoreNull(Date arg) {
    	if (arg == null) return new Date(Integer.MIN_VALUE);
    	else return arg;
    }
    
    protected Boolean ignoreNull(Boolean arg) {
    	if (arg == null) return false;
    	else return arg;
    }
}
