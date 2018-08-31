package com.cjit.webService.server.entity;

import java.math.BigDecimal;

/**
 * 进项信息中间表
 * @author jxjin
 *
 */
public class InputInfoTemp {
	//主键
	private String id;
	//发票代码
	private String billId;
	//发票号码
	private String billCode;
	//币种默认为'RMB'
	private String curreny;
	//开票日期
	private String billDate;
	//销方名称
	private String name;
	//销方纳税人识别号
	private String taxNo;
	//发票类型 0-专票 1-普票 2-通行费用
	private String billType;
	//报销机构
	private String shareInst;
	//金额
	private BigDecimal amt;
	//税额
	private BigDecimal tax;
	//税率(小数 0.06)
	private BigDecimal taxRate;
	//价税合计
	private BigDecimal sumAmt;
	//是否抵免0-全部抵扣,1-部分抵扣,2-不可抵扣
	private String isCredit;
	//用途 i01-有形动产租赁,i02-不动产租赁,i03-运输服务,i04-电信服务,i05-建筑安装服务,i06-金融保险服务,i07-生活服务,i08-取得无形资产,i09-货物及加工、修理修配劳务,i10-受让土地使用权,i11-通行费,i12-固定资产（动产）,i13-其他
	private String purpose;
	//认证状态
	private String billStatus;
	//是否有效 0-无效 1-有效
	private String available;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCurreny() {
		return curreny;
	}
	public void setCurreny(String curreny) {
		this.curreny = curreny;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
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
	public String getShareInst() {
		return shareInst;
	}
	public void setShareInst(String shareInst) {
		this.shareInst = shareInst;
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
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	
}
