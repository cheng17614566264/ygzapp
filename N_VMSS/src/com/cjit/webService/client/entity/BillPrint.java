package com.cjit.webService.client.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cjit.common.util.UUID;

public class BillPrint {
	// 请求类型
	private String requestionType;
	// 请求序列号
	private String reqserialNo;
	// 日期
	private String flowinTime;
	//交易流水号
	private String businessId;
	//操作:开票、红冲、作废
	private String dataStatus;
	//发票代码
	private String billCode;
	//发票号码
	private String billNo;
	//发票类型
	private String fapiaoype;
	public BillPrint() {
		this.requestionType="";
		this.flowinTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		this.reqserialNo=UUID.randomUUID().toString();
	}
	public String getRequestionType() {
		return requestionType;
	}
	public void setRequestionType(String requestionType) {
		this.requestionType = requestionType;
	}
	public String getReqserialNo() {
		return reqserialNo;
	}
	public void setReqserialNo(String reqserialNo) {
		this.reqserialNo = reqserialNo;
	}
	public String getFlowinTime() {
		return flowinTime;
	}
	public void setFlowinTime(String flowinTime) {
		this.flowinTime = flowinTime;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
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
	public String getFapiaoype() {
		return fapiaoype;
	}
	public void setFapiaoype(String fapiaoype) {
		this.fapiaoype = fapiaoype;
	}
}
