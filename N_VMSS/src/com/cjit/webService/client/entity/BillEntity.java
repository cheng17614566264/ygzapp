package com.cjit.webService.client.entity;

import java.math.BigDecimal;

public class BillEntity {
	//发票代码
	private String billCode;
	//发票号码
	private String billNo;
	//发票类型
	private String fapiaoType;
	//发票状态
	private String dataStatus;
	//交易流水号
	private String businessId;
	//开票金额
	private String amt;
	//开票税额
	private String taxAmt;
	//发票抬头
	private String customerName;
	//保单号
	private String chernum;
	//投保单号
	private String ttmprcno;
	//失败原因
	private String errorInfo;
	/*-------非报文字段，仅用来红冲时使用Start--*/
	//交易ID
	private String transId;
	//交易剩余未开票金额
	private BigDecimal balance;
	//交易总金额
	private BigDecimal amtCny;
	//交易剩余未开票税额
	private BigDecimal taxCnyBalance;
	//交易合计税额
	private BigDecimal taxCny;
	/*------------END-------------------*/
	
	/**
	 * 程  新增字段
	 * 2018/08/23
	 */
	private String transDate; // 新增  交易时间（MUFG:VALDAT）
	private String repNum;        //保全受理号       REPNUM旧保单号  EdorAcceptNo
	private String  feetype;     //费用类型
	private String  planlongdesc;//主险名称
	private String  dataFlag;    // 是否部分开票    DATA_FLAG  用  balance的大小判断 
	private String customerId;	 //客户ID
	private String oriBillCode;// 原发票代码
	private String oriBillNo;// 原发票号码
	private String gdFlag;// 团个标志
	
	
	
	
	 
	
	
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getRepNum() {
		return repNum;
	}
	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public String getPlanlongdesc() {
		return planlongdesc;
	}
	public void setPlanlongdesc(String planlongdesc) {
		this.planlongdesc = planlongdesc;
	}
	public String getDataFlag() {
		return dataFlag;
	}
	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOriBillCode() {
		return oriBillCode;
	}
	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}
	public String getOriBillNo() {
		return oriBillNo;
	}
	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}
	public String getGdFlag() {
		return gdFlag;
	}
	public void setGdFlag(String gdFlag) {
		this.gdFlag = gdFlag;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getChernum() {
		return chernum;
	}
	public void setChernum(String chernum) {
		this.chernum = chernum;
	}
	public String getTtmprcno() {
		return ttmprcno;
	}
	public void setTtmprcno(String ttmprcno) {
		this.ttmprcno = ttmprcno;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public BigDecimal getTaxCnyBalance() {
		return taxCnyBalance;
	}
	public void setTaxCnyBalance(BigDecimal taxCnyBalance) {
		this.taxCnyBalance = taxCnyBalance;
	}
	public BigDecimal getTaxCny() {
		return taxCny;
	}
	public void setTaxCny(BigDecimal taxCny) {
		this.taxCny = taxCny;
	}
	
}
