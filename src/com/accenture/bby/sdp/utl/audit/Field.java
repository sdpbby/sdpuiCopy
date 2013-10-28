package com.accenture.bby.sdp.utl.audit;

/**
 * @author a719057
 *
 */
public enum Field {
	FPK_TRANSACTION_DATE(1),
	FPK_STORE_ID(2),
	FPK_REGISTER_ID(3),
	FPK_TRANSACTION_ID(4),
	PRIMARY_SKU(5),
	PARENT_SKU(6),
	VENDOR_TRIGGER_SKU(7),
	COVERED_HARDWARE_SKU(8),
	COVERED_HARDWARE_MODEL(9),
	COVERED_HARDWARE_DESCRIPTION(10),
	PRIMARY_SKU_PRICE(11),
	PARENT_SKU_PRICE(12),
	SERIAL_NUMBER(13),
	REG_ID(14),
	PSP_ID(15),
	VALUE_PACKAGE_ID(16),
	LINE_ITEM_ID(17),
	CONTRACT_ID(18),
	SOURCE_SYSTEM_ID(19),
	CONTRACT_END_DATE(20),
	CUSTOMER_FIRST_NAME(21),
	CUSTOMER_MIDDLE_NAME(22),
	CUSTOMER_LAST_NAME(23),
	ADDRESS_LABEL(24),
	ADDRESS_LINE_1(25),
	ADDRESS_LINE_2(26),
	ADDRESS_CITY(27),
	ADDRESS_STATE(28),
	ADDRESS_ZIPCODE(29),
	PHONE_LABEL(30),
	PHONE_NUMBER(31),
	DELIVERY_EMAIL(32),
	REWARD_ZONE_NUMBER(33),
	CREDIT_CARD_CUSTOMER_NAME(34),
	CREDIT_CARD_TOKEN(35),
	CREDIT_CARD_TYPE(36),
	CREDIT_CARD_EXPIRE_DATE(37),
	VENDOR_ID(38),
	CANCEL_REASON_CODE(39),
	CONFIRMATION_CODE(40),
	IN_PRODUCT_MESSAGE_CODE(41),
	VPS_STATUS(42),
	SDP_ID(43),
	VPS_VENDOR_KEY(44),
	KEYCODE(45),
	QUANTITY(46),
	FPK_LINE_ID(47),
	CUSTOMER_CONTACT_EMAIL(48),
	PRIMARY_SKU_TAX_AMOUNT(49),
	PRIMARY_SKU_TAX_RATE(50),
	PARENT_SKU_TAX_AMOUNT(51),
	PARENT_SKU_TAX_RATE(52),
	CA_PARTY_ID(53),
	DIGITAL_ID(54),
	VPS_VENDOR_ID(55),
	FPK_ORIGINAL_TRANSACTION_DATE(56),
	FPK_ORIGINAL_STORE_ID(57),
	FPK_ORIGINAL_REGISTER_ID(58),
	FPK_ORIGINAL_TRANSACTION_ID(59),
	FPK_ORIGINAL_LINE_ID(60),
	MASTER_ITEM_ID(61),
	MASTER_VENDOR_ID(62),
	VENDOR_DIGITAL_ID(63),
	DIGITAL_PRODUCT_TYPE(64),
	DIGITAL_PRICE(65),
	DIGITAL_TAX_AMOUNT(66),
	DIGITAL_TAX_RATE(67),
	STREET_DATE(68),
	THROTTLE_FACTOR(69),
	CATALOG_ID(70),
	VENDOR_NAME(71),
	SERVICE_PROVIDER_ID(72),
	AGGREGATION_FREQUENCY(73),
	AGGREGATION_MAX(74),
	RETRY_FREQUENCY(75),
	RETRY_MAX(76),
	CATEGORY(77),
	COMMSAT_TEMPLATE_ID(78),
	CONTRACT_TRIGGER_FLAG(79),
	NEED_CA_FLAG(80),
	NEEDS_COMMSAT_INTEG_FLAG(81),
	NEEDS_VENDOR_KEY_FLAG(82),
	NEEDS_VENDOR_PROVISIONING_FLAG(83),
	OFFER_TYPE(84),
	PREORDER_HOLD(85),
	PRIMARY_SKU_DESCRIPTION(86),
	PRODUCT_TYPE(87),
	REDIRECT_TO_OLD_ARCH_FLAG(88),
	RETRY_COUNT(89),
	ROLE(90),
	SUB_CATEGORY(91),
	PRODUCT_ID(92),
	LOAD_SIZE(93),
	FILE_NAME(94),
	START_DATE(95),
	END_DATE(96),
	VENDOR_CODE_ROW_ID(97),
	VENDOR_CODE(98),
	SDP_CODE(99),
	CAN_RETRY_FLAG(100),
	VENDOR_CODE_DESCRIPTION(101),
	MERCHANDISE_SKU(102),
	NON_MERCHANDISE_SKU(103),
	THRESHOLD(104),
	KCB_PRODUCT_TYPE(105),
	BATCH_LOAD_ID(106),
	BBY_ORDER_ID(107),
	ADJUSTMENT_REASON_CODE(108),
	PROMO_SKU(109),
	PROMO_PRICE(110),
	PROMO_START_DATE(111),
	PROMO_END_DATE(112),
	PROMO_CODE(113);
	
	private int fieldId;
	private int priority;
	private boolean sensitive;
	private Field(int fieldId) {
		this.fieldId = fieldId;
		this.priority = 0;
		this.sensitive = false;
	}
	public int getFieldId() {
		return fieldId;
	}
	public int getPriority() {
		return priority;
	}
	public Field priority(int priority) {
		this.priority = priority;
		return this;
	}
	public Field markSensitiveData() {
		this.sensitive = true;
		return this;
	}
	public Field unmarkSensitiveData() {
		this.sensitive = false;
		return this;
	}
	
	/**
	 * Indicates whether the audited field contains sensitive data. 
	 * The calling method should take extra action to handle sensitive data.
	 * For example sensitive data could be masked be masked.
	 * 
	 * @see {@link com.accenture.bby.sdp.utl.TextFilter#getMaskedValue(string)}
	 * 
	 * @return true if this field is sensitive data, false otherwise
	 */
	public boolean isSensitiveData() {
		return sensitive;
	}
}