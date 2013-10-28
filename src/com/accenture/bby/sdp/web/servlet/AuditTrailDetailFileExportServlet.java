package com.accenture.bby.sdp.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accenture.bby.sdp.db.AuditTrailDBWrapper;
import com.accenture.bby.sdp.utl.CsvExportUtil;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.AuditLog;
import com.accenture.bby.sdp.web.handlers.UserManager;

public class AuditTrailDetailFileExportServlet extends HttpServlet {
	
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
				String arg0 = request.getParameter("arg0");
				if (arg0 == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		            return;
				}
				Integer logId;
				try {
					logId = Integer.valueOf(arg0);
				} catch (NumberFormatException e) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		            return;
				}
				
				AuditLog log = AuditTrailDBWrapper.getAuditLogByAudTransId(logId);
				
				final byte[] bytes;
				if (log != null) {
					bytes = CsvExportUtil.getAuditTrailDetailExportCsvString(log).getBytes();
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
	