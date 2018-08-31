package com.cjit.vms.input.model;

/**
 * 新增
 * @作者： 刘俊杰
 * @日期： 2018-08-22
 * @描述： 远程从总账获取数据所对应的实体类
 */
public class GeneralLedgerTemp {
	private String INST_ID;
	private String YEAR_MONTH;
	private String DIRECTION_ID;
	private String DIRECTION_NAME;
	private String PLANLONGDESC_ID;
	private String PLANLONGDESC_NAME;
	private String DEBIT_DESC;
	private String CREDIT_DESC;
	private String BALANCE_SOURCE;
	private String IS_TAXEXEMPTION;
	public String getINST_ID() {
		return INST_ID;
	}
	public void setINST_ID(String iNST_ID) {
		INST_ID = iNST_ID;
	}
	public String getYEAR_MONTH() {
		return YEAR_MONTH;
	}
	public void setYEAR_MONTH(String yEAR_MONTH) {
		YEAR_MONTH = yEAR_MONTH;
	}
	public String getDIRECTION_ID() {
		return DIRECTION_ID;
	}
	public void setDIRECTION_ID(String dIRECTION_ID) {
		DIRECTION_ID = dIRECTION_ID;
	}
	public String getDIRECTION_NAME() {
		return DIRECTION_NAME;
	}
	public void setDIRECTION_NAME(String dIRECTION_NAME) {
		DIRECTION_NAME = dIRECTION_NAME;
	}
	public String getPLANLONGDESC_ID() {
		return PLANLONGDESC_ID;
	}
	public void setPLANLONGDESC_ID(String pLANLONGDESC_ID) {
		PLANLONGDESC_ID = pLANLONGDESC_ID;
	}
	public String getPLANLONGDESC_NAME() {
		return PLANLONGDESC_NAME;
	}
	public void setPLANLONGDESC_NAME(String pLANLONGDESC_NAME) {
		PLANLONGDESC_NAME = pLANLONGDESC_NAME;
	}
	public String getDEBIT_DESC() {
		return DEBIT_DESC;
	}
	public void setDEBIT_DESC(String dEBIT_DESC) {
		DEBIT_DESC = dEBIT_DESC;
	}
	public String getCREDIT_DESC() {
		return CREDIT_DESC;
	}
	public void setCREDIT_DESC(String cREDIT_DESC) {
		CREDIT_DESC = cREDIT_DESC;
	}
	public String getBALANCE_SOURCE() {
		return BALANCE_SOURCE;
	}
	public void setBALANCE_SOURCE(String bALANCE_SOURCE) {
		BALANCE_SOURCE = bALANCE_SOURCE;
	}
	public String getIS_TAXEXEMPTION() {
		return IS_TAXEXEMPTION;
	}
	public void setIS_TAXEXEMPTION(String iS_TAXEXEMPTION) {
		IS_TAXEXEMPTION = iS_TAXEXEMPTION;
	}
	
}
