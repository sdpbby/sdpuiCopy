package com.accenture.bby.sdp.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.CsvExportUtil;
import com.accenture.bby.sdp.utl.DateUtil;
import com.accenture.bby.sdp.utl.TextFilter;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.SimpleAuditLog;
import com.accenture.bby.sdp.web.handlers.UserManager;

public class AuditTrailFileExportServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3035639235177071500L;
	private final static Logger logger = Logger.getLogger(AuditTrailFileExportServlet.class.getName());
	private final static String FILE_NAME = "AUDIT_TRAIL_REPORT_EXPORT.csv";
	
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.isUserInRole(UserManager.MONITOR) || request.isUserInRole(UserManager.OPSUISER) || request.isUserInRole(UserManager.OPSADMIN)) {
			try {
				// Get request parameters
				String logId = TextFilter.filter(request.getParameter("arg0"));
				String contractId = TextFilter.filter(request.getParameter("arg1"));
				String serialNumber = TextFilter.filter(request.getParameter("arg2"));
				String lineItemId = TextFilter.filter(request.getParameter("arg3"));
				String vendorId = TextFilter.filter(request.getParameter("arg4"));
				String primarySku = TextFilter.filter(request.getParameter("arg5"));
				String masterItemId = TextFilter.filter(request.getParameter("arg6"));
				String actionId = TextFilter.filter(request.getParameter("arg7"));
				String minStr = TextFilter.filter(request.getParameter("arg8"));
				String maxStr = TextFilter.filter(request.getParameter("arg9"));
				String userId = TextFilter.filter(request.getParameter("arg10"));
				String displayMax = TextFilter.filter(request.getParameter("arg11"));
				
				if (logger.isDebugEnabled()) {
					StringBuilder builder = new StringBuilder();
					builder.append("\narg0=" + logId);
					builder.append("\narg1=" + contractId);
					builder.append("\narg2=" + serialNumber);
					builder.append("\narg3=" + lineItemId);
					builder.append("\narg4=" + vendorId);
					builder.append("\narg5=" + primarySku);
					builder.append("\narg6=" + masterItemId);
					builder.append("\narg7=" + actionId);
					builder.append("\narg8=" + minStr);
					builder.append("\narg9=" + maxStr);
					builder.append("\narg10=" + userId);
					builder.append("\narg11=" + displayMax);
					logger.log(Level.DEBUG, "Search parameters passed >> " + builder.toString());
				}
				Integer displayMaxInt;
				try {
					
					if (displayMax == null) {
						displayMaxInt = 20;
					} else {
						displayMaxInt = Integer.valueOf(displayMax);
						if (displayMaxInt == null) {
							logger.log(Level.ERROR, "Arg arg11 must be an integer between 0 and 500. Found null.");
	    					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
	    		            return;
						}
					}
    				if (displayMaxInt > 500 || displayMaxInt < 0) {
    					logger.log(Level.ERROR, "Arg arg11 must be an integer between 0 and 500. Found >> " + displayMax);
    					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
    		            return;
    				}
				} catch (RuntimeException e) {
					logger.log(Level.ERROR, "Arg arg11 must be an integer between 0 and 500.", e);
					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		            return;
				}
				
				Integer logIdInt = null;
				if (logId != null) {
					try {
						logIdInt = Integer.getInteger(logId);
    				} catch (RuntimeException e) {
    					logger.log(Level.ERROR, "Arg arg0 must be an integer.", e);
    					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
    		            return;
    				}	
				}
				
				Integer actionIdInt = null;
				if (actionId != null) {
					try {
						actionIdInt = Integer.getInteger(actionId);
    				} catch (RuntimeException e) {
    					logger.log(Level.ERROR, "Arg arg7 must be an integer.", e);
    					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
    		            return;
    				}	
				}
				
				Date min = null;
				Date max = null;
				try {
    				min = minStr == null ? null : DateUtil.getShortXmlDate(minStr);
    				max = maxStr == null ? null : DateUtil.getShortXmlDate(maxStr);
				} catch (ParseException e) {
					logger.log(Level.ERROR, "Date parameter not in expected format. Expected yyyy-mm-dd but got <" + minStr + ">,<" + maxStr + ">.", e);
					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		            return;
				}
				
				List<SimpleAuditLog> logs = AuditTrailDBWrapper.getSimpleAuditLogsByAll(contractId, serialNumber, lineItemId, actionIdInt, min, max, userId, logIdInt, vendorId, primarySku, masterItemId, displayMaxInt);
				
				final byte[] bytes;
				if (logs != null) {
					bytes = CsvExportUtil.getCsvSimpleAuditLogExportString(logs).getBytes();
				} else {
					bytes = new byte[0];
				}
				
				
				InputStream content = new ByteArrayInputStream(bytes);
				
				// Init servlet response.
				response.reset();
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setContentType("application/csv");
				response.setHeader("Content-Length", String.valueOf(bytes.length));
				response.setHeader("Content-Disposition", "attachment; filename=\"" + FILE_NAME + "\"");
				
				// Prepare streams.
				BufferedInputStream input = null;
				BufferedOutputStream output = null;
				
				try {
					// Open streams.
					input = new BufferedInputStream(content, DEFAULT_BUFFER_SIZE);
					output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
					
					// Write file contents to response.
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}
				} finally {
					// Gently close streams.
					close(output);
					close(input);
				}
				
			} catch (DataSourceLookupException e) {
				logger.log(Level.ERROR, "Failed to access database to create export file.");
			} catch (DataAccessException e) {
				logger.log(Level.ERROR, "Failed to retrieve export data from database.");
			}
		
		} else { // show 404 if user not in valid role.
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
		}
		
		
	}
	
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				logger.log(Level.ERROR, "Failed to close BufferedInputStream/BufferedOutputString resources.");
			}
		}
	}
}
	