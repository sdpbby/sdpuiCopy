package com.accenture.bby.sdp.utl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.AuditLog;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.beans.KcbReportBean;
import com.accenture.bby.sdp.web.beans.FieldAnalysis;
import com.accenture.bby.sdp.web.beans.SimpleAuditLog;

public class CsvExportUtil {
	private static final Logger logger = Logger.getLogger(CsvExportUtil.class.getName());

	
	public static String getDepletionReportExportCsvString(List<KcbReportBean> beans, int days) {
		StringBuilder builder = new StringBuilder();
		builder.append(depletionReportHeaderString.replace("?", String.valueOf(days)));
		int size = beans.size();
		for (int i = 0; i < size; i++) {
			builder.append(getDepletionReportCsvString(beans.get(i)));
		}
		return builder.toString();
	}

	public static String getCatalogExportCsvString(List<CatalogBean> beans) {
		StringBuilder builder = new StringBuilder();
		builder.append(catalogHeaderString);
		int size = beans.size();
		for (int i = 0; i < size; i++) {
			builder.append(getCatalogCsvString(beans.get(i)));
		}
		return builder.toString();
	}
	
	public static String getKcbCatalogExportCsvString(KcbProductBean[] beans) {
		StringBuilder builder = new StringBuilder();
		builder.append(kcbCatalogHeaderString);
		int size = beans.length;
		for (int i = 0; i < size; i++) {
			builder.append(getKcbCatalogCsvString(beans[i]));
		}
		return builder.toString();
	}
	
	public static String getAuditTrailDetailExportCsvString(AuditLog auditLog) {
		SimpleDateFormat df = new SimpleDateFormat("M/d/yy h:mm:ss a");
		StringBuilder builder = new StringBuilder();
		builder.append("\"Audit Log\",\"" + (auditLog.getId() == null ? "" : auditLog.getId()) + "\",\n");
		builder.append("\"Action\",\"" + (auditLog.getAction() == null ? "" : auditLog.getAction()) + "\",\n");
		builder.append("\"User Id\",\"" + (auditLog.getUserId() == null ? "" : auditLog.getUserId()) + "\",\n");
		builder.append("\"Date\",\"" + (auditLog.getDate() == null ? "" : df.format(auditLog.getDate())) + "\",\n");
		builder.append("\n");
		
		builder.append(getCsvFieldAnalysisExportString(auditLog.getFieldAnalysisList()));
		
		builder.append("\n\"Related Audit Logs:\",\n");
		
		builder.append(getCsvSimpleAuditLogExportString(auditLog.getRelatedAuditLogs()));
		
		return builder.toString();
	}

	private static final String catalogHeaderString = "\"CATALOG ID\","
			+ "\"VENDOR ID\"," + "\"VENDOR NAME\"," + "\"MASTER VENDOR ID\","
			+ "\"PRODUCT TYPE\"," + "\"PRIMARY SKU\"," + "\"DESCRIPTION\","
			+ "\"PARENT SKU\"," + "\"VENDOR TRIGGER SKU\"," + "\"OFFER TYPE\","
			+ "\"CATEGORY\"," + "\"SUB CATEGORY\"," + "\"ROLE\","
			+ "\"RETRY COUNT\"," + "\"COMMSAT TEMPLATE ID\","
			+ "\"PREORDER STREET DATE\"," + "\"PREORDER STATUS\","
			+ "\"PREORDER HOLD\"," + "\"CONTRACT TRIGGER\","
			+ "\"NEEDS VENDOR PROVISIONING\"," + "\"NEEDS VENDOR KEY\","
			+ "\"NEEDS COMMSAT INTEG\"," + "\"NEED CA\","
			+ "\"REDIRECT TO OLD ARCH\"," + "\"CREATED DATE\","
			+ "\"CREATED BY USER\"," + "\n";

	private static final String depletionReportHeaderString = "\"VENDOR ID\","
			+ "\"VENDOR NAME\"," + "\"DESCRIPTION\"," + "\"MERCHANDISE SKU\","
			+ "\"NON-MERCHANDISE SKU\"," + "\"MASTER ITEM ID\"," + "\"AVAILABLE\","
			+ "\"EXPIRED\"," + "\"KEYS USED LAST ? DAYS\"," + "\"DAYS TO DEPLETION\"," + "\n";

	private static final String kcbCatalogHeaderString = "\"PRODUCT ID\","
			+ "\"VENDOR ID\"," + "\"VENDOR NAME\"," + "\"MERCHANDISE SKU\","
			+ "\"NON-MERCHANDISE SKU\"," + "\"MASTER ITEM ID\"," + "\"RELATED SKU\"," + "\"DESCRIPTION\","
			+ "\"DEPLETION THRESHOLD\"," + "\"LOAD SIZE\"," + "\n";

	private static String getCatalogCsvString(CatalogBean bean) {
		try {
			bean.setWorkFlowAttributes(CatalogDBWrapper
					.getWorkFlowAttributes(bean.getCatalogId()));
		} catch (DataSourceLookupException e) {
			// no action. attributes just wont show up in the export file
			logger.log(Level.ERROR, "Failed to retrieve workflow attributes in export file.", e);
		} catch (DataAccessException e) {
			// no action. attributes just wont show up in the export file
			logger.log(Level.ERROR, "Failed to retrieve workflow attributes in export file.", e);
		}
		return "\"" + textValue(bean.getCatalogId()) + "\"," + "\""
				+ textValue(bean.getVendorId()) + "\"," + "\""
				+ textValue(bean.getVendorName()) + "\"," + "\""
				+ textValue(bean.getMasterVendorId()) + "\"," + "\""
				+ textValue(bean.getProductType()) + "\"," + "\""
				+ textValue(bean.getPrimarySku()) + "\"," + "\""
				+ textValue(bean.getPrimarySkuDescription()) + "\"," + "\""
				+ textValue(bean.getParentSku()) + "\"," + "\""
				+ textValue(bean.getVendorTriggerSku()) + "\"," + "\""
				+ textValue(bean.getOfferType()) + "\"," + "\""
				+ textValue(bean.getCategory()) + "\"," + "\""
				+ textValue(bean.getSubCategory()) + "\"," + "\""
				+ textValue(bean.getRole()) + "\"," + "\""
				+ textValue(bean.getRetryCount()) + "\"," + "\""
				+ textValue(bean.getCommSatTemplateId()) + "\"," + "\""
				+ textValue(bean.getPreorderStreetDate()) + "\"," + "\""
				+ textValue(bean.getPreorderStatus()) + "\"," + "\""
				+ textValue(bean.getPreOrderHold()) + "\"," + "\""
				+ textValue(bean.getContractTrigger()) + "\"," + "\""
				+ textValue(bean.getNeedsVendorProvisioning()) + "\"," + "\""
				+ textValue(bean.getNeedsVendorKey()) + "\"," + "\""
				+ textValue(bean.getNeedsCommsatInteg()) + "\"," + "\""
				+ textValue(bean.getNeedCA()) + "\"," + "\""
				+ textValue(bean.getRedirectToOldArch()) + "\"," + "\""
				+ textValue(bean.getCreatedDate()) + "\"," + "\""
				+ textValue(bean.getCreatedByUserId()) + "\"," + "\n";
	}
	
	private static String getDepletionReportCsvString(KcbReportBean bean) {
		return "\"" + textValue(bean.getVendorId()) + "\"," + "\""
				+ textValue(bean.getVendorName()) + "\"," + "\""
				+ textValue(bean.getProductDescription()) + "\"," + "\""
				+ textValue(bean.getMerchandiseSku()) + "\"," + "\""
				+ textValue(bean.getNonMerchandiseSku()) + "\"," + "\""
				+ textValue(bean.getMasterItemId()) + "\"," + "\""
				+ textValue(bean.getRemainingKeys()) + "\"," + "\""
				+ textValue(bean.getKeysExpired()) + "\"," + "\""
				+ textValue(bean.getKeysUsedLastXDays()) + "\"," + "\""
				+ textValue(bean.getEstDaysToDepletion().compareTo(Float.MAX_VALUE) == 0 ? "N/A" : String.valueOf(bean.getEstDaysToDepletion())) + "\"," + "\n";
	}
	
	private static String getKcbCatalogCsvString(KcbProductBean bean) {
		return "\"" + textValue(bean.getProductId()) + "\"," + "\""
				+ textValue(bean.getVendorId()) + "\"," + "\""
				+ textValue(bean.getVendorName()) + "\"," + "\""
				+ textValue(bean.getMerchandiseSku()) + "\"," + "\""
				+ textValue(bean.getNonMerchandiseSku()) + "\"," + "\""
				+ textValue(bean.getMasterItemId()) + "\"," + "\""
				+ textValue(bean.getRelatedSKU()) + "\"," + "\""
				+ textValue(bean.getDescription()) + "\"," + "\""
				+ textValue(bean.getThreshold()) + "\"," + "\""
				+ textValue(bean.getLoadSize()) + "\"," + "\n";
	}

//	private static String textValue(Boolean arg) {
//		if (arg == null)
//			return "";
//		else if (arg)
//			return "TRUE";
//		else
//			return "FALSE";
//	}

	private static String textValue(String arg) {
		if (arg == null)
			return "";
		else
			return arg;
	}

	private static String textValue(Integer arg) {
		if (arg == null)
			return "";
		else
			return arg.toString();
	}

	private static String textValue(Long arg) {
		if (arg == null)
			return "";
		else
			return arg.toString();
	}

	private static String textValue(Date arg) {
		if (arg == null)
			return "";
		else
			return DateUtil.getExportDateString(arg);
	}
	
	/**
	 * Formats list of <code>FieldAnalysis</code> to be formatted into CSV comma delimited format. 
	 * 
	 * @param fieldAnalysisList list of <code>FieldAnalysis</code> to be exported to CSV.
	 * @return String of comma separated data to be inserted into a CSV file.
	 */
	private static String getCsvFieldAnalysisExportString(List<FieldAnalysis> fieldAnalysisList) {
		StringBuilder builder = new StringBuilder();
		builder.append("\"LABEL\",");
		builder.append("\"ORIGINAL VALUE\",");
		builder.append("\"NEW VALUE\",");
		builder.append("\"CHANGE ANALYSIS\",\n");
		
		if (fieldAnalysisList != null) {
			for (FieldAnalysis itr : fieldAnalysisList) {
				builder.append(itr.toString());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	/**
	 * Formats list of <code>SimpleAuditLog</code> to be formatted into CSV comma delemited format. 
	 * 
	 * @param simpleAuditLogs list of <code>SimpleAuditLog</code> to be exported to CSV.
	 * @return String of comma separated data to be inserted into a CSV file.
	 */
	public static String getCsvSimpleAuditLogExportString(List<SimpleAuditLog> simpleAuditLogs) {
		StringBuilder builder = new StringBuilder();
		builder.append("\"ROW\",");
		builder.append("\"DATE\",");
		builder.append("\"CONTRACT ID\",");
		builder.append("\"VENDOR KEY (SN)\",");
		builder.append("\"LINEITEM ID\",");
		builder.append("\"ACTION\",");
		builder.append("\"USER ID\",");
		builder.append("\"LOG ID\",");
		builder.append("\"TRANSACTION ID\",");
		builder.append("\"STORE ID\",");
		builder.append("\"REGISTER ID\",");
		builder.append("\"TRANSACTION ID,\",");
		builder.append("\"PRODUCT SKU\",");
		builder.append("\"PLAN SKU\",");
		builder.append("\"CUSTOMER NAME\",");
		builder.append("\"DELIVERY EMAIL\",");
		builder.append("\n");
		
		if (simpleAuditLogs != null) {
			int rowNum = 0;
			for (SimpleAuditLog itr : simpleAuditLogs) {
				builder.append("\"" + (++rowNum) + "\",");
				builder.append(itr.toString());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}
