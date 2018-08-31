package com.cjit.achieveTemp.model;

//中间表合并实体类customer
public class achieveTempModel {
	
	
	private int CUSTOMER_NO;
	private String CUSTOMER_NAME; 
	private String CUSTOMER_TAXNO;
	private String CUSTOMER_ADDRESSAND;
	private String TAXPAYER_TYPE;
	private String CUSTOMER_PHONE;
	private String CUSTOMER_BANKAND;
	private String CUSTOMER_ACCOUNT;
	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}
	public void setCUSTOMER_NAME(String cUSTOMER_NAME) {
		CUSTOMER_NAME = cUSTOMER_NAME;
	}
	public String getCUSTOMER_TAXNO() {
		return CUSTOMER_TAXNO;
	}
	public void setCUSTOMER_TAXNO(String cUSTOMER_TAXNO) {
		CUSTOMER_TAXNO = cUSTOMER_TAXNO;
	}
	public int getCUSTOMER_NO() {
		return CUSTOMER_NO;
	}
	public void setCUSTOMER_NO(int cUSTOMER_NO) {
		CUSTOMER_NO = cUSTOMER_NO;
	}
	public String getCUSTOMER_ADDRESSAND() {
		return CUSTOMER_ADDRESSAND;
	}
	public void setCUSTOMER_ADDRESSAND(String cUSTOMER_ADDRESSAND) {
		CUSTOMER_ADDRESSAND = cUSTOMER_ADDRESSAND;
	}
	public String getTAXPAYER_TYPE() {
		return TAXPAYER_TYPE;
	}
	public void setTAXPAYER_TYPE(String tAXPAYER_TYPE) {
		TAXPAYER_TYPE = tAXPAYER_TYPE;
	}
	public String getCUSTOMER_PHONE() {
		return CUSTOMER_PHONE;
	}
	public void setCUSTOMER_PHONE(String cUSTOMER_PHONE) {
		CUSTOMER_PHONE = cUSTOMER_PHONE;
	}
	public String getCUSTOMER_BANKAND() {
		return CUSTOMER_BANKAND;
	}
	public void setCUSTOMER_BANKAND(String cUSTOMER_BANKAND) {
		CUSTOMER_BANKAND = cUSTOMER_BANKAND;
	}
	public String getCUSTOMER_ACCOUNT() {
		return CUSTOMER_ACCOUNT;
	}
	public void setCUSTOMER_ACCOUNT(String cUSTOMER_ACCOUNT) {
		CUSTOMER_ACCOUNT = cUSTOMER_ACCOUNT;
	}
	public achieveTempModel(String cUSTOMER_NAME, String cUSTOMER_TAXNO, String cUSTOMER_ADDRESSAND,
			String tAXPAYER_TYPE, String cUSTOMER_PHONE, String cUSTOMER_BANKAND, String cUSTOMER_ACCOUNT) {
		super();
		CUSTOMER_NAME = cUSTOMER_NAME;
		CUSTOMER_TAXNO = cUSTOMER_TAXNO;
		CUSTOMER_ADDRESSAND = cUSTOMER_ADDRESSAND;
		TAXPAYER_TYPE = tAXPAYER_TYPE;
		CUSTOMER_PHONE = cUSTOMER_PHONE;
		CUSTOMER_BANKAND = cUSTOMER_BANKAND;
		CUSTOMER_ACCOUNT = cUSTOMER_ACCOUNT;
	}
	public achieveTempModel() {
		super();
	}
	
	

	

	
}
