package com.cjit.webService.client.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cjit.common.util.UUID;

/**
 * 开票申请实体类
 * @author jxjin
 *
 */
public class ApplicationForm {
	//请求类型
	private String requestionType;
	//请求序列号
	private String reqserialNo;
	//日期
	private String flowinTime;
	//投保单号
	private String ttmprcNo;
	//保单号
	private String chernum;
	//交易开始时间
	private String transDateStart;
	//交易结束时间
	private String transDateEnd;
	//客户名称
	private String customerName;
	//数据类型 1-正常开票 2-预开票
	private String isYK;
	//批单号
	private String batchNo;
	//受理类型
	private String batchType;
	public ApplicationForm() {
		this.flowinTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		this.reqserialNo=UUID.randomUUID().toString();
		this.requestionType="0002";
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
	public String getTtmprcNo() {
		return ttmprcNo;
	}
	public void setTtmprcNo(String ttmprcNo) {
		this.ttmprcNo = ttmprcNo;
	}
	public String getChernum() {
		return chernum;
	}
	public void setChernum(String chernum) {
		this.chernum = chernum;
	}
	public String getTransDateStart() {
		return transDateStart;
	}
	public void setTransDateStart(String transDateStart) {
		this.transDateStart = transDateStart;
	}
	public String getTransDateEnd() {
		return transDateEnd;
	}
	public void setTransDateEnd(String transDateEnd) {
		this.transDateEnd = transDateEnd;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIsYK() {
		return isYK;
	}
	public void setIsYK(String isYK) {
		this.isYK = isYK;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	
}
