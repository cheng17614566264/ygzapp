package com.cjit.common.model;

import java.math.BigDecimal;

public class BillInfo {
	protected String billId;// 票据ID
	protected String billCode;// 票据代码
	protected String billNo;// 票据号码
	protected String billDate;// 开票日期
	protected String customerName;// 客户纳税人名称
	protected String customerTaxno;// 客户纳税人识别号
	protected String customerAddressandphone;// 客户地址电话
	protected String customerBankandaccount;// 客户银行账号
	protected BigDecimal amtSum = new BigDecimal("0");;// 合计金额
	protected BigDecimal taxAmtSum = new BigDecimal("0");;// 合计税额
	protected BigDecimal sumAmt = new BigDecimal("0");;// 价税合计
	protected String remark;// 备注
	protected String drawer; // 开票人
	protected String reviewer; // 复核人
	protected String payee; // 收款人
	protected String name;// 我方纳税人名称
	protected String taxno;// 我方纳税人识别号
	protected String addressandphone;// 我方地址电话
	protected String bankandaccount;// 我方银行账号
	protected String cancelInitiator;// 撤销发起人
	protected String cancelAuditor;// 撤销审核人
	protected String taxDiskNo; // 税控盘号
	protected String machineNo;// 开票机号
	protected String noticeNo; // 通知单编号
	protected String oriBillCode;// 原发票代码
	protected String oriBillNo;// 原发票号码
	protected String description;// 操作说明
	protected String isHandiwork;// 是否手工录入
	protected String issueType;// 开具类型
	protected String fapiaoType;// 发票类型
	protected String applyDate;  //申请开票日期
	protected String cancelReason; // 退回原因
	protected String customerId;// 客户号
	protected String insureId;// 保单号
	protected String cherNum;//保单号
	protected String repNum;// 旧保单号
	protected String ttmpRcno;// 投保单号
	protected String feeTyp;// 费用类型
	protected String billFreq;// 交费频率
	
	protected String dsouRce;// 数据来源
	protected String chanNel;//渠道
	protected String hissDte;//承保日期
	protected String hissBeginDte;//开始
	protected String hissEndDte;//结束
	
	protected String customerEmail;
	
	
	
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
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
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
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
	public String getCustomerTaxno() {
		return customerTaxno;
	}
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}
	public String getCustomerAddressandphone() {
		return customerAddressandphone;
	}
	public void setCustomerAddressandphone(String customerAddressandphone) {
		this.customerAddressandphone = customerAddressandphone;
	}
	public String getCustomerBankandaccount() {
		return customerBankandaccount;
	}
	public void setCustomerBankandaccount(String customerBankandaccount) {
		this.customerBankandaccount = customerBankandaccount;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDrawer() {
		return drawer;
	}
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxno() {
		return taxno;
	}
	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	public String getAddressandphone() {
		return addressandphone;
	}
	public void setAddressandphone(String addressandphone) {
		this.addressandphone = addressandphone;
	}
	public String getBankandaccount() {
		return bankandaccount;
	}
	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}
	public String getCancelInitiator() {
		return cancelInitiator;
	}
	public void setCancelInitiator(String cancelInitiator) {
		this.cancelInitiator = cancelInitiator;
	}
	public String getCancelAuditor() {
		return cancelAuditor;
	}
	public void setCancelAuditor(String cancelAuditor) {
		this.cancelAuditor = cancelAuditor;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsHandiwork() {
		return isHandiwork;
	}
	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getInsureId() {
		return insureId;
	}
	public void setInsureId(String insureId) {
		this.insureId = insureId;
	}
	public String getCherNum() {
		return cherNum;
	}
	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
	public String getRepNum() {
		return repNum;
	}
	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}
	public String getTtmpRcno() {
		return ttmpRcno;
	}
	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}
	public String getFeeTyp() {
		return feeTyp;
	}
	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}
	public String getBillFreq() {
		return billFreq;
	}
	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}
	public String getDsouRce() {
		return dsouRce;
	}
	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}
	public String getChanNel() {
		return chanNel;
	}
	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}
	public String getHissDte() {
		return hissDte;
	}
	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}
	public String getHissBeginDte() {
		return hissBeginDte;
	}
	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}
	public String getHissEndDte() {
		return hissEndDte;
	}
	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
	}
}
