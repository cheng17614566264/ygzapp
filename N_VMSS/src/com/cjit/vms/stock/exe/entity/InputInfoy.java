package com.cjit.vms.stock.exe.entity;

public class InputInfoy {

	private String amt;
	private String available ;
	private String billCode;
	private String billDate ;
	private String billType;
	private String curreny ;
	private String id;
	private String isCredit ;
	private String name ;
	private String purpose ;
	private String shareInst ;
	private String sumAmt ;
	private String tax ;
	private String taxNo ;
	private String taxRate ;
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getCurreny() {
		return curreny;
	}
	public void setCurreny(String curreny) {
		this.curreny = curreny;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsCredit() {
		return isCredit;
	}
	public void setIsCredit(String isCredit) {
		this.isCredit = isCredit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getShareInst() {
		return shareInst;
	}
	public void setShareInst(String shareInst) {
		this.shareInst = shareInst;
	}
	public String getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	@Override
	public String toString() {
		return "amt=" + amt + "\n available=" + available + "\n billCode=" + billCode + "\n billDate="
				+ billDate + "\n billType=" + billType + "\n curreny=" + curreny + "\n id=" + id + "\n isCredit=" + isCredit
				+ "\n name=" + name + "\n purpose=" + purpose + "\n shareInst=" + shareInst + "\n sumAmt=" + sumAmt
				+ "\n tax=" + tax + "\n taxNo=" + taxNo + "\n taxRate=" + taxRate ;
	}
	
	
}
