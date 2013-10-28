package com.accenture.bby.sdp.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accenture.bby.sdp.db.CatalogDBWrapper;
import com.accenture.bby.sdp.utl.CsvExportUtil;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.web.beans.CatalogBean;
import com.accenture.bby.sdp.web.handlers.UserManager;

public class CatalogFileExportServlet extends HttpServlet {
	
	private final static Logger logger = Logger.getLogger(CatalogFileExportServlet.class.getName());
	private final static String FILE_NAME = "SDP_CATALOG_EXPORT.csv";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5335356202020659950L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.isUserInRole(UserManager.MONITOR) || request.isUserInRole(UserManager.OPSUISER) || request.isUserInRole(UserManager.OPSADMIN)) {
			try {
				// Retrieve data.
				List<CatalogBean> beans = CatalogDBWrapper.getAll();
				byte[] bytes = CsvExportUtil.getCatalogExportCsvString(beans).getBytes();
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
	