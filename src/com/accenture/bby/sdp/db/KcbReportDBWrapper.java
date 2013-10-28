package com.accenture.bby.sdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.KcbReportBean;

public class KcbReportDBWrapper extends KcbDBWrapper {
	
	private static final String sqlCommandDailyReport = 
			"	select "
			+"	  KCB_DAILY.VENDORID, "// "VENDOR ID",
			+"	  KCB_DAILY.VENDORNAME, "// "VENDOR NAME",
			+"	  KCB_DAILY.SKU, "// "PRODUCT SKU",
			+"	  KCB_DAILY.NAME, "// "PRODUCT NAME",
			+"	  KCB_DAILY.MSTR_ITM_ID, "// "MASTER ITEM ID",
			+"	  KCB_DAILY.NONMRCH_SKU, "// "NON-MERCH SKU",
			+"	  KCB_DAILY.RESERVED, "// "RESERVED",
			+"	  KCB_DAILY.ACTIVATED, "// "ACTIVATED",
			+"	  KCB_DAILY.REMAINING "// "REMAINING"
			+"	from "
			+"	( "
			+"	  select  "
			+"	    KCB_PRODUCT.SKU as SKU, "
			+"	    KCB_PRODUCT.MSTR_ITM_ID as MSTR_ITM_ID, "
			+"	    KCB_PRODUCT.NONMRCH_SKU as NONMRCH_SKU, "
			+"	    KCB_PRODUCT.VENDORID as VENDORID, "
			+"	    KCB_VENDOR.NAME as VENDORNAME, "
			+"	    KCB_PRODUCT.NAME as NAME, "
			+"	    KCB_PRODUCT.LOAD_SIZE as LOAD_SIZE, "
			+"	    case  "
			+"	      when KCB_REMAINING.COUNT is null then 0  "
			+"	      else KCB_REMAINING.COUNT  "
			+"	    end as REMAINING, "
			+"	    case  "
			+"	      when KCB_RESERVED.COUNT is null then 0  "
			+"	      else KCB_RESERVED.COUNT  "
			+"	    end as RESERVED, "
			+"	    case "
			+"	      when KCB_ACTIVATED.COUNT is null then 0 "
			+"	      else KCB_ACTIVATED.COUNT "
			+"	    end as ACTIVATED"
			+"	  from  "
			+"	    KCB_PRODUCT  "
			+"	    left join  "
			+"	    ( "
			+"	      select  "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"	      from  "
			+"	        KCB_KEYCODE "
			+"	        LEFT JOIN KCB_BATCH_LOAD "
			+"	        on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"	      where  "
			+"	        to_char(KCB_KEYCODE.CREATED, 'yyyy-mm-dd') <= ? "
			+"	        and (to_char(KCB_BATCH_LOAD.BEG_TS, 'yyyy-mm-dd') <= ? or KCB_BATCH_LOAD.BEG_TS is null) "
			+"	        and (to_char(KCB_BATCH_LOAD.END_TS, 'yyyy-mm-dd') > ? or KCB_BATCH_LOAD.END_TS is null) "
			+"	        and "
			+"		        ( "
			+"	          KCB_KEYCODE.STATUS='FREE' "
			+"	          or to_char(KCB_KEYCODE.RESERVED, 'yyyy-mm-dd') > ? "
			+"	        ) "
			+"	      group by  "
			+"	        KCB_KEYCODE.PRODUCTID "
			+"	    ) KCB_REMAINING "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_REMAINING.PRODUCTID  "
			+"	    left join  "
			+"	    ( "
			+"	      select  "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"	      from  "
			+"	        KCB_KEYCODE "
			+"	      where  "
			+"	        to_char(KCB_KEYCODE.RESERVED, 'yyyy-mm-dd')=? "
			+"	      group by  "
			+"	        KCB_KEYCODE.PRODUCTID "
			+"	    ) KCB_RESERVED "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_RESERVED.PRODUCTID  "
			+"	    left join   "
			+"	    (  "
			+"	      select   "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID,  "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT   "
			+"	      from   "
			+"	       KCB_KEYCODE  "
			+"	      where   "
			+"	         to_char(KCB_KEYCODE.ACTIVATED, 'yyyy-mm-dd')=? "
			+"	      group by   "
			+"	        KCB_KEYCODE.PRODUCTID  "
			+"	    ) KCB_ACTIVATED  "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_ACTIVATED.PRODUCTID   "
			+"	    INNER JOIN KCB_VENDOR "
			+"	    on KCB_PRODUCT.VENDORID = KCB_VENDOR.VENDORID "
			+"	    ) KCB_DAILY "
			+"	    order by "
			+"	    KCB_DAILY.ACTIVATED desc ";
	
	private static final String sqlCommandMonthlyReport = 
			"	select "
			+"	  KCB_MONTHLY.VENDORID, "// "VENDOR ID",
			+"	  KCB_MONTHLY.VENDORNAME, "// "VENDOR NAME",
			+"	  KCB_MONTHLY.SKU, "// "PRODUCT SKU",
			+"	  KCB_MONTHLY.NAME, "// "PRODUCT NAME",
			+"	  KCB_MONTHLY.MSTR_ITM_ID, "// "MASTER ITEM ID",
			+"	  KCB_MONTHLY.NONMRCH_SKU, "// "NON-MERCH SKU",
			+"	  KCB_MONTHLY.RESERVED, "// "RESERVED",
			+"	  KCB_MONTHLY.ACTIVATED, "// "ACTIVATED",
			+"	  KCB_MONTHLY.REMAINING "// "REMAINING"
			+"	from "
			+"	( "
			+"	  select  "
			+"	    KCB_PRODUCT.SKU as SKU, "
			+"	    KCB_PRODUCT.MSTR_ITM_ID as MSTR_ITM_ID, "
			+"	    KCB_PRODUCT.NONMRCH_SKU as NONMRCH_SKU, "
			+"	    KCB_PRODUCT.VENDORID as VENDORID, "
			+"	    KCB_VENDOR.NAME as VENDORNAME, "
			+"	    KCB_PRODUCT.NAME as NAME, "
			+"	    KCB_PRODUCT.LOAD_SIZE as LOAD_SIZE, "
			+"	    case  "
			+"	      when KCB_REMAINING.COUNT is null then 0  "
			+"	      else KCB_REMAINING.COUNT  "
			+"	    end as REMAINING, "
			+"	    case  "
			+"	      when KCB_RESERVED.COUNT is null then 0  "
			+"	      else KCB_RESERVED.COUNT  "
			+"	    end as RESERVED, "
			+"	    case "
			+"	      when KCB_ACTIVATED.COUNT is null then 0 "
			+"	      else KCB_ACTIVATED.COUNT "
			+"	    end as ACTIVATED"
			+"	  from  "
			+"	    KCB_PRODUCT  "
			+"	    left join  "
			+"	    ( "
			+"	      select  "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"	      from  "
			+"	        KCB_KEYCODE "
			+"	        LEFT JOIN KCB_BATCH_LOAD "
			+"	        on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"	      where  "
			+"	        to_char(KCB_KEYCODE.CREATED, 'yyyy-mm') <= ? "
			+"	        and (to_char(KCB_BATCH_LOAD.BEG_TS, 'yyyy-mm') <= ? or KCB_BATCH_LOAD.BEG_TS is null) "
			+"	        and (to_char(KCB_BATCH_LOAD.END_TS, 'yyyy-mm') > ? or KCB_BATCH_LOAD.END_TS is null) "
			+"	        and "
			+"		        ( "
			+"	          KCB_KEYCODE.STATUS='FREE' "
			+"	          or to_char(KCB_KEYCODE.RESERVED, 'yyyy-mm') > ? "
			+"	        ) "
			+"	      group by  "
			+"	        KCB_KEYCODE.PRODUCTID "
			+"	    ) KCB_REMAINING "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_REMAINING.PRODUCTID  "
			+"	    left join  "
			+"	    ( "
			+"	      select  "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"	      from  "
			+"	        KCB_KEYCODE "
			+"	      where  "
			+"	        to_char(KCB_KEYCODE.RESERVED, 'yyyy-mm')=? "
			+"	      group by  "
			+"	        KCB_KEYCODE.PRODUCTID "
			+"	    ) KCB_RESERVED "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_RESERVED.PRODUCTID  "
			+"	    left join   "
			+"	    (  "
			+"	      select   "
			+"	        KCB_KEYCODE.PRODUCTID as PRODUCTID,  "
			+"	        count(KCB_KEYCODE.KEYCODEID) as COUNT   "
			+"	      from   "
			+"	       KCB_KEYCODE  "
			+"	      where   "
			+"	         to_char(KCB_KEYCODE.ACTIVATED, 'yyyy-mm')=? "
			+"	      group by   "
			+"	        KCB_KEYCODE.PRODUCTID  "
			+"	    ) KCB_ACTIVATED  "
			+"	    on KCB_PRODUCT.PRODUCTID=KCB_ACTIVATED.PRODUCTID   "
			+"	    INNER JOIN KCB_VENDOR "
			+"	    on KCB_PRODUCT.VENDORID = KCB_VENDOR.VENDORID "
			+"	    ) KCB_MONTHLY "
			+"	    order by "
			+"	    KCB_MONTHLY.ACTIVATED desc ";
	
	public static KcbReportBean[] getDateReportData(int year, int month, int day) throws DataSourceLookupException, DataAccessException {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return getDateReportData(sqlCommandDailyReport, formattedDate);
	}
	
	public static KcbReportBean[] getDateReportData(int year, int month) throws DataSourceLookupException, DataAccessException {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		String formattedDate = new SimpleDateFormat("yyyy-MM").format(c.getTime());
		return getDateReportData(sqlCommandMonthlyReport, formattedDate);
	}
	
	private static KcbReportBean[] getDateReportData(String sqlCommand, String formattedDate) throws DataSourceLookupException, DataAccessException {

		final Collection<KcbReportBean> list = new ArrayList<KcbReportBean>();
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setString(1, formattedDate);
					stmt.setString(2, formattedDate);
					stmt.setString(3, formattedDate);
					stmt.setString(4, formattedDate);
					stmt.setString(5, formattedDate);
					stmt.setString(6, formattedDate);

					final ResultSet rs = stmt.executeQuery();
					try {
						while (rs.next()) {
							KcbReportBean bean = new KcbReportBean();
							bean.setVendorId(rs.getString(1));
							//bean.setVendorName(rs.getString(2));
							bean.setMerchandiseSku(rs.getString(3));
							bean.setProductDescription(rs.getString(4));
							bean.setMasterItemId(rs.getString(5));
							bean.setNonMerchandiseSku(rs.getString(6));
							bean.setReservedKeys(rs.getLong(7));
							bean.setActivatedKeys(rs.getLong(8));
							bean.setRemainingKeys(rs.getLong(9));
							list.add(bean);
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to generate Date Report", e);
		}
		return list.toArray(new KcbReportBean[list.size()]);
	}
	

	private static final String sqlCommandDepletionByDays = 
			"		select "
			+"		  KCB_PRODUCT.SKU, " //PRODUCT SKU
			+"		  KCB_PRODUCT.MSTR_ITM_ID, " //MASTER ITEM ID
			+"		  KCB_PRODUCT.NONMRCH_SKU, " //NON-MERCH SKU
			+"		  KCB_PRODUCT.VENDORID, " //VENDOR ID
			+"		  KCB_VENDOR.NAME, " //VENDOR NAME
			+"		  KCB_PRODUCT.NAME, " //PRODUCT NAME
			+"		  case  "
			+"		    when KCB_REMAINING.COUNT is null then 0  "
			+"		    else KCB_REMAINING.COUNT "
			+"		  end as remaining, " //KEYS REMAINING
			+"		  case "
			+"		    when KCB_EXPIRED.COUNT is null then 0 "
			+"		    else KCB_EXPIRED.COUNT "
			+"		  end, " //KEYS EXPIRED
			+"		  case  "
			+"		    when KCB_RESERVED.COUNT is null then 0  "
			+"		    else KCB_RESERVED.COUNT  "
			+"		  end, " //RESERVED LAST 7 DAYS
			+"		  case  "
			+"		    when KCB_RESERVED.COUNT is null then null "
			+"		    when (KCB_REMAINING.COUNT is null or KCB_REMAINING.COUNT=0) and (KCB_RESERVED.COUNT is not null and KCB_RESERVED.COUNT>0) then 0 "
			+"		    when KCB_RESERVED.COUNT=0 then null "
			+"		    else KCB_REMAINING.COUNT/(KCB_RESERVED.COUNT/?)  "
			+"		  end as depletion " //DAYS TO DEPLETION
			+"		from  "
			+"		  KCB_PRODUCT "
			+"		  left join  "
			+"		  ( "
			+"		select KCB_NOT_EXPIRED.PRODUCTID as PRODUCTID, "
			+"		SUM(KCB_NOT_EXPIRED.Available) as COUNT "
			+"		from  "
			+"		( "
			+"		select KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		KCB_KEYCODE.LOADID as LOADID, "
			+"		count (KCB_KEYCODE.KEYCODEID) as Available "
			+"		from KCB_KEYCODE "
			+"		LEFT JOIN KCB_BATCH_LOAD "
			+"		on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"		where KCB_KEYCODE.STATUS = 'FREE' "
			+"		and (KCB_BATCH_LOAD.END_TS > sysdate "
			+"		or KCB_BATCH_LOAD.END_TS is null) "
			+"		and (KCB_BATCH_LOAD.BEG_TS < sysdate "
			+"		or KCB_BATCH_LOAD.BEG_TS is null) "
			+"		group by KCB_KEYCODE.PRODUCTID, "
			+"		KCB_KEYCODE.LOADID "
			+"		) KCB_NOT_EXPIRED "
			+"		group by KCB_NOT_EXPIRED.PRODUCTID "
			+"		  ) KCB_REMAINING "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_REMAINING.PRODUCTID  "
			+"		    left join  "
			+"		  ( "
			+"		    select  "
			+"		      KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		      count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"		    from  "
			+"		      KCB_KEYCODE "
			+"		      INNER JOIN KCB_BATCH_LOAD "
			+"		      on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"		      and KCB_BATCH_LOAD.END_TS < sysdate "
			+"		    where  "
			+"		      KCB_KEYCODE.STATUS='FREE' "
			+"		    group by  "
			+"		      KCB_KEYCODE.PRODUCTID "
			+"		  ) KCB_EXPIRED "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_EXPIRED.PRODUCTID  "
			+"		  left join  "
			+"		  ( "
			+"		    select  "
			+"		      KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		      count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"		    from  "
			+"		      KCB_KEYCODE "
			+"		    where  "
			+"		      KCB_KEYCODE.STATUS<>'FREE' "
			+"		      and KCB_KEYCODE.RESERVED > sysdate-? "
			+"		    group by  "
			+"		      KCB_KEYCODE.PRODUCTID "
			+"		  ) KCB_RESERVED "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_RESERVED.PRODUCTID "
			+"		  INNER JOIN  "
			+"		  KCB_VENDOR "
			+"		  on KCB_PRODUCT.VENDORID = KCB_VENDOR.VENDORID "
			+"		order by  "
			+"		  depletion asc, remaining asc ";
	
	private static final String sqlCommandDepletionByDaysAndThreshold = 
			"		select   "
			+"		KCB_PRODUCT.SKU, " //PRODUCT SKU
			+"		KCB_PRODUCT.MSTR_ITM_ID, " //MASTER ITEM ID
			+"		KCB_PRODUCT.NONMRCH_SKU, " //NON-MERCH SKU
			+"		KCB_PRODUCT.VENDORID, " //VENDOR ID
			+"		KCB_VENDOR.NAME, " //VENDOR NAME
			+"		KCB_PRODUCT.NAME, " //PRODUCT NAME
			+"		case  "
			+"		when KCB_REMAINING.COUNT is null then 0  "
			+"		else KCB_REMAINING.COUNT "
			+"		end as remaining, " //KEYS REMAINING,
			+"		case "
			+"		when KCB_EXPIRED.COUNT is null then 0 "
			+"		else KCB_EXPIRED.COUNT "
			+"		end, " // "KEYS EXPIRED",
			+"		case  "
			+"		when KCB_RESERVED.COUNT is null then 0  "
			+"		else KCB_RESERVED.COUNT  "
			+"		end, " // "RESERVED LAST 7 DAYS",
			+"		case  "
			+"		when KCB_RESERVED.COUNT is null then null "
			+"		when (KCB_REMAINING.COUNT is null or KCB_REMAINING.COUNT=0) and (KCB_RESERVED.COUNT is not null and KCB_RESERVED.COUNT>0) then 0 "
			+"		when KCB_RESERVED.COUNT=0 then null "
			+"		else KCB_REMAINING.COUNT/(KCB_RESERVED.COUNT/?)  "
			+"		end as depletion " // "DAYS TO DEPLETION"
			+"		from  "
			+"		KCB_PRODUCT "
			+"		left join  "
			+"		( "
			+"		select KCB_NOT_EXPIRED.PRODUCTID as PRODUCTID, "
			+"		SUM(KCB_NOT_EXPIRED.Available) as COUNT "
			+"		from  "
			+"		( "
			+"		select KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		KCB_KEYCODE.LOADID as LOADID, "
			+"		count (KCB_KEYCODE.KEYCODEID) as Available "
			+"		from KCB_KEYCODE "
			+"		LEFT JOIN KCB_BATCH_LOAD "
			+"		on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"		where KCB_KEYCODE.STATUS = 'FREE' "
			+"		and (KCB_BATCH_LOAD.END_TS > sysdate "
			+"		or KCB_BATCH_LOAD.END_TS is null) "
			+"		and (KCB_BATCH_LOAD.BEG_TS < sysdate "
			+"		or KCB_BATCH_LOAD.BEG_TS is null) "
			+"		group by KCB_KEYCODE.PRODUCTID, "
			+"		KCB_KEYCODE.LOADID "
			+"		) KCB_NOT_EXPIRED "
			+"		group by KCB_NOT_EXPIRED.PRODUCTID "
			+"		  ) KCB_REMAINING "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_REMAINING.PRODUCTID  "
			+"		    left join  "
			+"		  ( "
			+"		    select  "
			+"		      KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		      count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"		    from  "
			+"		      KCB_KEYCODE "
			+"		      INNER JOIN KCB_BATCH_LOAD "
			+"		      on KCB_KEYCODE.LOADID = KCB_BATCH_LOAD.LOADID "
			+"		      and KCB_BATCH_LOAD.END_TS < sysdate "
			+"		    where  "
			+"		      KCB_KEYCODE.STATUS='FREE' "
			+"		    group by  "
			+"		      KCB_KEYCODE.PRODUCTID "
			+"		  ) KCB_EXPIRED "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_EXPIRED.PRODUCTID  "
			+"		  left join  "
			+"		  ( "
			+"		    select  "
			+"		      KCB_KEYCODE.PRODUCTID as PRODUCTID, "
			+"		      count(KCB_KEYCODE.KEYCODEID) as COUNT  "
			+"		    from  "
			+"		      KCB_KEYCODE "
			+"		    where  "
			+"		      KCB_KEYCODE.STATUS<>'FREE' "
			+"		      and KCB_KEYCODE.RESERVED > sysdate-? "
			+"		    group by  "
			+"		      KCB_KEYCODE.PRODUCTID "
			+"		  ) KCB_RESERVED "
			+"		  on KCB_PRODUCT.PRODUCTID=KCB_RESERVED.PRODUCTID "
			+"		  INNER JOIN  "
			+"		  KCB_VENDOR "
			+"		  on KCB_PRODUCT.VENDORID = KCB_VENDOR.VENDORID "
			+"		  where KCB_REMAINING.COUNT < KCB_PRODUCT.THRLD_CNT "
			+"		  or (KCB_PRODUCT.THRLD_CNT > 0 and KCB_REMAINING.COUNT is null) "
			+"		order by  "
			+"		  depletion asc, remaining asc ";
	
	public static KcbReportBean[] getDepletionReportData(int days) throws DataSourceLookupException, DataAccessException {
		return getDepletionReportData(sqlCommandDepletionByDays, days);
	}
	public static KcbReportBean[] getDepletionThresholdReportData(int days) throws DataSourceLookupException, DataAccessException {
		return getDepletionReportData(sqlCommandDepletionByDaysAndThreshold, days);
	}
	private static KcbReportBean[] getDepletionReportData(String sqlCommand, int days) throws DataSourceLookupException, DataAccessException {
		final List<KcbReportBean> list = new ArrayList<KcbReportBean>();
		
		try {
			final Connection conn = getConnection();
			try {
				final PreparedStatement stmt = conn.prepareStatement(sqlCommand);
				try {
					stmt.setInt(1, days);
					stmt.setInt(2, days);
					final ResultSet rs = stmt.executeQuery();
					try {
						while (rs.next()) {
							KcbReportBean bean = new KcbReportBean();
							bean.setMerchandiseSku(rs.getString(1));
							bean.setMasterItemId(rs.getString(2));
							bean.setNonMerchandiseSku(rs.getString(3));
							bean.setVendorId(rs.getString(4));
							//bean.setVendorName(rs.getString(5));
							bean.setProductDescription(rs.getString(6));
							bean.setRemainingKeys(rs.getLong(7));
							bean.setKeysExpired(rs.getLong(8));
							bean.setKeysUsedLastXDays(rs.getLong(9));
					
							if (rs.getObject(10) != null) {
								bean.setEstDaysToDepletion(rs.getFloat(10));
							} else {
								bean.setEstDaysToDepletion(Float.MAX_VALUE);
							}
							list.add(bean);
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to generate Depletion Report", e);
		}
		return list.toArray(new KcbReportBean[list.size()]);
	}
	

	
}
