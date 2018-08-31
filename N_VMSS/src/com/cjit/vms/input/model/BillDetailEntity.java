package com.cjit.vms.input.model;

import java.math.BigDecimal;

/**
 * 发票明细信息
 * @author jxjin
 *
 */
public class BillDetailEntity {
	//主键
	private String id;
	//开票日期
	private String billDate;
	//纳税人识别号
	private String taxNo;
	//供应商名称
	private String name;
	//发票代码
	private String billId;
	//认证状态
	private String billStatu;
	//发票号码
	private String billCode;
	//分摊机构
	private String shareInst;
	//金额
	private BigDecimal amt;
	//税额
	private BigDecimal tax;
	//税率
	private BigDecimal taxRate;
	//价税合计
	private BigDecimal sumAmt;
	//是否抵免
	private String isCredit;
	//转出金额
	private BigDecimal rollOutAmt;
	//转出状态
	private String rollOutStatus;
	//转出原因
	private String remark;
	//用途
	private String purpose;
	//发票类型
	private String billType;
	//认证日期
	private String dealNo;
	//转出比例值
	private String rollOutval;
	
	private String rollOutRatio;
	
	public String getRollOutRatio() {
		return rollOutRatio;
	}
	public void setRollOutRatio(String rollOutRatio) {
		this.rollOutRatio = rollOutRatio;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getIsCredit() {
		return isCredit;
	}
	public void setIsCredit(String isCredit) {
		this.isCredit = isCredit;
	}
	public BigDecimal getRollOutAmt() {
		return rollOutAmt;
	}
	public void setRollOutAmt(BigDecimal rollOutAmt) {
		this.rollOutAmt = rollOutAmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShareInst() {
		return shareInst;
	}
	public void setShareInst(String shareInst) {
		this.shareInst = shareInst;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRollOutStatus() {
		return rollOutStatus;
	}
	public void setRollOutStatus(String rollOutStatus) {
		this.rollOutStatus = rollOutStatus;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillStatu() {
		return billStatu;
	}
	public void setBillStatu(String billStatu) {
		this.billStatu = billStatu;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public String getRollOutval() {
		return rollOutval;
	}
	public void setRollOutval(String rollOutval) {
		this.rollOutval = rollOutval;
	}
	
}
