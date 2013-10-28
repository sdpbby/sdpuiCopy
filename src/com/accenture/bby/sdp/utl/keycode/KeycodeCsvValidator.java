package com.accenture.bby.sdp.utl.keycode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.accenture.bby.sdp.db.KcbCatalogDBWrapper;
import com.accenture.bby.sdp.utl.exceptions.DataAccessException;
import com.accenture.bby.sdp.utl.exceptions.DataSourceLookupException;
import com.accenture.bby.sdp.utl.exceptions.KeycodeLoadException;
import com.accenture.bby.sdp.web.beans.KcbProductBean;
import com.accenture.bby.sdp.web.beans.KeycodeBean;

public class KeycodeCsvValidator implements KeycodeFileValidator, Serializable {

	private static final Logger logger = Logger.getLogger(KeycodeCsvValidator.class.getName());
	
	private static final String LEGAL_CHARACTERS = "[\\s\\w\\n'\\|','-]*";
	
	
	private String merchandiseSku;
	private String nonMerchandiseSku;
	private String masterItemId;
	private KeycodeBean[] validKeycodes = new KeycodeBean[0];
	private KeycodeBean[] invalidKeycodes = new KeycodeBean[0];
	private KcbProductBean[] products;
	private String fileName;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5035094788292693582L;

	public KeycodeCsvValidator(File csvFile, String fileName) throws KeycodeLoadException, FileNotFoundException, DataSourceLookupException, DataAccessException {
		this.fileName = fileName;
		if (csvFile == null) {
			throw new KeycodeLoadException("Upload file was null.");
		}
		try {
			if (!isCsvFile(fileName)) {
				throw new KeycodeLoadException("Upload file was not a CSV file.");			
			} else {
				extract(csvFile);
			}
		} finally {
			if (csvFile.delete()) {
				logger.log(Level.INFO, "Successfully deleted upload file.");
			} else {
				logger.log(Level.WARN, "Failed to delete upload file from file system: " + csvFile.getName());
			}
		}
	}
	
	private boolean isCsvFile(String fileName) {
		return "CSV".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf(".")+1));
	}
	
	private void extract(File file) throws FileNotFoundException, KeycodeLoadException, DataSourceLookupException, DataAccessException {
		Scanner scanner = new Scanner(file);
		if (scanner.hasNext()) {
			String[] tmp = illegalCharactersFilter(scanner.nextLine()).split(",");
			if (tmp.length > 1) {
				merchandiseSku = trim(tmp[1]);
			}
		}
		if (scanner.hasNext()) {
			String[] tmp = illegalCharactersFilter(scanner.nextLine()).split(",");
			if (tmp.length > 1) {
				nonMerchandiseSku = trim(tmp[1]);
			}
		}
		if (scanner.hasNext()) {
			String[] tmp = illegalCharactersFilter(scanner.nextLine()).split(",");
			if (tmp.length > 1) {
				masterItemId = trim(tmp[1]);
			}
		}
		
		// validate that product exists.
		lookupProduct();
			
		// skip blank row and skip keycode/serialnumber header row
		if (scanner.hasNext()) {
			illegalCharactersFilter(scanner.nextLine());
			if (scanner.hasNext()) {
				illegalCharactersFilter(scanner.nextLine());
			} else {
				throw new KeycodeLoadException("Upload file did not have any keycodes or the upload file was not in expected format.");
			}
		} else {
			throw new KeycodeLoadException("Upload file did not have any keycodes or the upload file was not in expected format.");
		}
		
		// read keycodes beginning on line 6
		List<KeycodeBean> validKeycodeList = new ArrayList<KeycodeBean>();
		List<KeycodeBean> invalidKeycodeList = new ArrayList<KeycodeBean>();
		while (scanner.hasNext()) {
			final String line = scanner.nextLine();
			if (isLegalChacters(line)) {
				String[] tmp = line.split(",");
				if (tmp.length == 1 && trim(tmp[0]) != null) {
					validKeycodeList.add(new KeycodeBean(trim(tmp[0])));
				} else if (tmp.length == 2 && trim(tmp[0]) != null) {
					validKeycodeList.add(new KeycodeBean(trim(tmp[0]), trim(tmp[1])));
				} else {
					// ignore blank lines.
				}
			} else {
				// if line is invalid, do not create KeycodeBean
				invalidKeycodeList.add(new KeycodeBean(line));
			}
		}
		scanner.close();
		this.validKeycodes = validKeycodeList.toArray(new KeycodeBean[validKeycodeList.size()]);
		this.invalidKeycodes = invalidKeycodeList.toArray(new KeycodeBean[invalidKeycodeList.size()]);
	}
	
	@Override
	public KeycodeBean[] getValidKeycodes() {
		return validKeycodes;
	}
	
	@Override
	public KeycodeBean[] getInvalidKeycodes() {
		return invalidKeycodes;
	}

	@Override
	public KcbProductBean[] getProducts() {
		if (products == null) {
			return new KcbProductBean[0];
		} else {
			return products;
		}
	}

	@Override
	public boolean isValid() {
		return products != null 
				&& products.length == 1 
				&& products[0] != null 
				&& validKeycodes != null 
				&& validKeycodes.length > 0
				&& (invalidKeycodes == null || invalidKeycodes.length == 0);
	}
	
	private String trim(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}
	
	/**
	 * @throws DataSourceLookupException
	 * @throws DataAccessException
	 */
	private void lookupProduct() throws DataSourceLookupException, DataAccessException {
		if (merchandiseSku == null && nonMerchandiseSku == null && masterItemId == null) {
			logger.log(Level.WARN, "Upload file is invalid. User must provide at least one of the following: Merchandise SKU, Non-Merchandise SKU, Master Item ID. All of these parameters were null.");	
			products = new KcbProductBean[0];
		} else {
			products = KcbCatalogDBWrapper.getAllProducts(null, merchandiseSku, nonMerchandiseSku, masterItemId);
		}
		if (products.length == 1) {
			logger.log(Level.INFO, "Product lookup returned one product:\n" + products[0]);
		} else if (products.length < 1) {
			logger.log(Level.WARN, "Product lookup returned no results");
		} else {
			StringBuilder builder = new StringBuilder();
			for (KcbProductBean product : products) {
				builder.append(product + "\n");
			}
			logger.log(Level.WARN, "Product lookup returned multiple results:\n" + builder.toString()); 
		}
	}
	
	private static String illegalCharactersFilter(String str) throws KeycodeLoadException {
		if (isLegalChacters(str)) {
			return str;
		} else {
			throw new KeycodeLoadException("Unable to process load, illegal characters detected in line \"" + str + "\"");
		}
	}
	
	private static boolean isLegalChacters(String str) {
		return str.matches(LEGAL_CHARACTERS);
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public int getInvalidKeyCount() {
		if (invalidKeycodes == null) return 0;
		else return invalidKeycodes.length;
	}

	@Override
	public int getValidKeyCount() {
		if (validKeycodes == null) return 0;
		else return validKeycodes.length;
	}
	
	@Override
	public void setInvalidKeycodes(KeycodeBean[] invalidKeycodes) {
		this.invalidKeycodes = invalidKeycodes;
	}
	
}
