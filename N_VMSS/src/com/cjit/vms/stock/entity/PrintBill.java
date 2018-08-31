package com.cjit.vms.stock.entity;

import java.math.BigDecimal;

public class PrintBill {
	// 发票ID
	private String billId;
	// 投保单号
	private String ttmprcNo;
	// 保单号
	private String insureId;
	// 发票代码
	private String billCode;
	// 发票号码
	private String billNo;
	// 金额
	private BigDecimal amtSum;
	// 税额
	private BigDecimal taxAmtSum;
	// 价税合计
	private BigDecimal sumAmt;
	// 开票日期
	private String billDate;
	//开票起始日期
	private String billStartDate;
	//开票截止日期
	private String billEndDate;
	// 客户名称
	private String customerName;
	// 发票类型
	private String billType;
	// 发票状态
	private String datastatus;
	// 回收状态
	private String recycleStatus;
	//开票机构编号
	private String instId;
	// 所属号段起始号
	private String SbillStartNo;
	// 所属号段截止号
	private String SbillEndNo;
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getTtmprcNo() {
		return ttmprcNo;
	}
	public void setTtmprcNo(String ttmprcNo) {
		this.ttmprcNo = ttmprcNo;
	}
	public String getInsureId() {
		return insureId;
	}
	public void setInsureId(String insureId) {
		this.insureId = insureId;
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
	public BigDecimal getAmtSum() {
		return amtSum;
	}
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	public BigDecimal getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}
	public String getRecycleStatus() {
		return recycleStatus;
	}
	public void setRecycleStatus(String recycleStatus) {
		this.recycleStatus = recycleStatus;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getBillStartDate() {
		return billStartDate;
	}
	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}
	public String getBillEndDate() {
		return billEndDate;
	}
	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public String getSbillStartNo() {
		return SbillStartNo;
	}
	public void setSbillStartNo(String sbillStartNo) {
		SbillStartNo = sbillStartNo;
	}
	public String getSbillEndNo() {
		return SbillEndNo;
	}
	public void setSbillEndNo(String sbillEndNo) {
		SbillEndNo = sbillEndNo;
	}
	
}
