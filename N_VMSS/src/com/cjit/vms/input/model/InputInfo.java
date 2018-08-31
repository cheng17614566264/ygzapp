package com.cjit.vms.input.model;

import java.math.BigDecimal;
import java.util.List;


/**
 * 进项票据信息VMS_INPUT_INVOICE_NEW 
 * VMS_INPUT_INVOICE_NEW_ITEM
 * @author jxjin
 *
 */
public class InputInfo {
	//当前用户id
	private String userId;
	private List lstAuthInstId;
	private String transBeginDate;
	private String transEndDate;
	private String billId;
	private String billCode;
	private String curreny;
	private String billDate;
	private String taxNo;
	private String name;
	private String bankNo;
	private String bankName;
	private String customerTel;
	private String customerAdd;
	private String billInst;
	private String billType;
	private String billStatu;
	//认证日期
	private String dealNo;
	private String dealNoStarDate;
	private String dealNoEndDate;
	//发票总的金额
	private BigDecimal amt;
	//发票总税额
	private BigDecimal tax;
	//发票总价税合计
	private BigDecimal sumTax;
	//转出状态
	private String rollOutStatus;
	
	/**
	 * 新增
	 * 日期：2018-08-24
	 * 作者：刘俊杰
	 * 说明：增加字段BANK_CODE，用于存储机构代码
	 */
	private String BANK_CODE;
	
	public String getBANK_CODE() {
		return BANK_CODE;
	}
	public void setBANK_CODE(String bANK_CODE) {
		BANK_CODE = bANK_CODE;
	}
	//end 2018-08-24
	private String zAmt;
	private String zTax;
	private String zSumTax;
	public String getzAmt() {
		return zAmt;
	}
	public void setzAmt(String zAmt) {
		this.zAmt = zAmt;
	}
	public String getzTax() {
		return zTax;
	}
	public void setzTax(String zTax) {
		this.zTax = zTax;
	}
	public String getzSumTax() {
		return zSumTax;
	}
	public void setzSumTax(String zSumTax) {
		this.zSumTax = zSumTax;
	}
	private List<BillDetailEntity> billDetailList;
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
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCustomerTel() {
		return customerTel;
	}
	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}
	public String getCustomerAdd() {
		return customerAdd;
	}
	public void setCustomerAdd(String customerAdd) {
		this.customerAdd = customerAdd;
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
	public String getTransBeginDate() {
		return transBeginDate;
	}
	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}
	public String getTransEndDate() {
		return transEndDate;
	}
	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public List<BillDetailEntity> getBillDetailList() {
		return billDetailList;
	}
	public void setBillDetailList(List<BillDetailEntity> billDetailList) {
		this.billDetailList = billDetailList;
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
	public BigDecimal getSumTax() {
		return sumTax;
	}
	public void setSumTax(BigDecimal sumTax) {
		this.sumTax = sumTax;
	}
	public String getBillInst() {
		return billInst;
	}
	public void setBillInst(String billInst) {
		this.billInst = billInst;
	}
	public String getRollOutStatus() {
		return rollOutStatus;
	}
	public void setRollOutStatus(String rollOutStatus) {
		this.rollOutStatus = rollOutStatus;
	}
	public String getDealNoStarDate() {
		return dealNoStarDate;
	}
	public void setDealNoStarDate(String dealNoStarDate) {
		this.dealNoStarDate = dealNoStarDate;
	}
	public String getDealNoEndDate() {
		return dealNoEndDate;
	}
	public void setDealNoEndDate(String dealNoEndDate) {
		this.dealNoEndDate = dealNoEndDate;
	}
	@Override
	public String toString() {
		return "InputInfo [userId=" + userId + ", lstAuthInstId="
				+ lstAuthInstId + ", transBeginDate=" + transBeginDate
				+ ", transEndDate=" + transEndDate + ", billId=" + billId
				+ ", billCode=" + billCode + ", curreny=" + curreny
				+ ", billDate=" + billDate + ", taxNo=" + taxNo + ", name="
				+ name + ", bankNo=" + bankNo + ", bankName=" + bankName
				+ ", customerTel=" + customerTel + ", customerAdd="
				+ customerAdd + ", billInst=" + billInst + ", billType="
				+ billType + ", billStatu=" + billStatu + ", dealNo=" + dealNo
				+ ", dealNoStarDate=" + dealNoStarDate + ", dealNoEndDate="
				+ dealNoEndDate + ", amt=" + amt + ", tax=" + tax + ", sumTax="
				+ sumTax + ", rollOutStatus=" + rollOutStatus + ", zAmt="
				+ zAmt + ", zTax=" + zTax + ", zSumTax=" + zSumTax
				+ ", billDetailList=" + billDetailList + "]";
	}
	
}
