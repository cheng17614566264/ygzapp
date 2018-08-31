package com.cjit.vms.input.model;

import java.math.BigDecimal;
/**
 * vms_general_ledger
 * 科目类
 */
public class SubjectEntity {
	// 机构编号
	private String instId;
	//机构名称
	private String instName;
	//月度
	private String yearMonth;
	//科目明细
	private String directionId;
	//科目名称
	private String directionName;
	//产品编号
	private String planLongDescId;
	//险种名称
	private String planLongDescName;
	//借方原币发生额
	private BigDecimal DebitSource;
	//贷方原币发生额
	private BigDecimal CreditSource;
	//借方本位币发生额
	private BigDecimal DebitDesc;
	//贷方本位币发生额
	private BigDecimal CreditDesc;//***//
	//TAX_RATE_CODE 税率段编码
	private String taxRateCode;
	//TAX_RATE_NAME  产品名称
	private String taxRateName;
	// 贷方本位币发生额合计
	private BigDecimal CreditDescSum ;
	
	private String isTaxexemption; //应免税标识
	
	public String getIsTaxexemption() {
		return isTaxexemption;
	}
	public void setIsTaxexemption(String isTaxexemption) {
		this.isTaxexemption = isTaxexemption;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getDirectionId() {
		return directionId;
	}
	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}
	public String getDirectionName() {
		return directionName;
	}
	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}
	public String getPlanLongDescId() {
		return planLongDescId;
	}
	public void setPlanLongDescId(String planLongDescId) {
		this.planLongDescId = planLongDescId;
	}
	public String getPlanLongDescName() {
		return planLongDescName;
	}
	public void setPlanLongDescName(String planLongDescName) {
		this.planLongDescName = planLongDescName;
	}
	public BigDecimal getDebitSource() {
		return DebitSource;
	}
	public void setDebitSource(BigDecimal debitSource) {
		DebitSource = debitSource;
	}
	public BigDecimal getCreditSource() {
		return CreditSource;
	}
	public void setCreditSource(BigDecimal creditSource) {
		CreditSource = creditSource;
	}
	public BigDecimal getDebitDesc() {
		return DebitDesc;
	}
	public void setDebitDesc(BigDecimal debitDesc) {
		DebitDesc = debitDesc;
	}
	public BigDecimal getCreditDesc() {
		return CreditDesc;
	}
	public void setCreditDesc(BigDecimal creditDesc) {
		CreditDesc = creditDesc;
	}
	public String getTaxRateCode() {
		return taxRateCode;
	}
	public void setTaxRateCode(String taxRateCode) {
		this.taxRateCode = taxRateCode;
	}
	public String getTaxRateName() {
		return taxRateName;
	}
	public void setTaxRateName(String taxRateName) {
		this.taxRateName = taxRateName;
	}
	
	/**
	 * @return the instName
	 */
	public String getInstName() {
		return instName;
	}
	/**
	 * @param instName the instName to set
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	/**
	 * @return the creditDescSum
	 */
	public BigDecimal getCreditDescSum() {
		return CreditDescSum;
	}
	/**
	 * @param creditDescSum the creditDescSum to set
	 */
	public void setCreditDescSum(BigDecimal creditDescSum) {
		CreditDescSum = creditDescSum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubjectEntity [instName=" + instName + ", yearMonth=" + yearMonth + ", directionName=" + directionName
				+ ", CreditDesc=" + CreditDesc + ", taxRateName=" + taxRateName + "]";
	}

	
	
	
}
