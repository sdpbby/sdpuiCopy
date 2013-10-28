package com.accenture.bby.sdp.utl.audit;

import java.util.Map;

/**
 * @author a719057
 * 
 */
public interface Auditable {
	/**
	 * @return the serial number if the Auditable object has one, else null
	 */
	public String getSerialNumberForAudit();

	/**
	 * @return the lineitem id if the Auditable object has one, else null
	 */
	public String getLineItemIdForAudit();

	/**
	 * @return the primary sku if the Auditable object has one, else null
	 */
	public String getPrimarySkuForAudit();
	
	/**
	 * @return the master item id if the Auditable object has one, else null
	 */
	public String getMasterItemIdForAudit();
	
	/**
	 * @return the contract id if the Auditable object has one, else null
	 */
	public String getContractIdForAudit();
	
	/**
	 * @return the vendor id if the Auditable object has one, else null
	 */
	public String getVendorIdForAudit();

	/**
	 * <p>
	 * Returns a list of all auditable fields from the Auditable object. Note
	 * that this also includes and fields mapped to the following methods:
	 * </p>
	 * <ul>
	 * <li>getSerialNumberForAudit()</li>
	 * <li>getSerialNumberForAudit()</li>
	 * <li>getSerialNumberForAudit()</li>
	 * <li>getSerialNumberForAudit()</li>
	 * <li>getVendorIdForAudit()</li>
	 * </ul>
	 * 
	 * @return map of all Auditable fields in the Auditable object or an empty
	 *         map if none.
	 */
	public Map<Field, String> getAuditableFields();
}
