package com.cjit.vms.trans.model;

import java.util.ArrayList;
import java.util.List;

public class BussInfo {
	private String businessId;// 交易流水号
	private String instId;// 业务机构
	private String qdFlag;// 团个标识
	private String chernum;// 保单号
	private String repnum;// 业务交易号
	private String ttmprcno;// 投保单号
	private String customerName;// 客户纳税人名称
	private String customerTaxno;// 客户纳税人识别号
	private String customerAddressand;// 客户地址
	private String taxpayerType;// 客户纳税人类别
	private String customerPhone;// 客户电话
	private String customerBankand;// 客户开户行
	private String customerAccount;// 客户开户行账号
	private String origcurr;// 交易币种
	private String origamt;// 原币种交易金额
	private String acctamt;// 本币交易金额
	private String trdt;// 交易日期
	private String invtyp;// 发票类型
	private String bustyp;// 业务类型
	private String billfreq;// 交费频率
	private String polyear;// 保单年度
	private String hissdte;// 承保日期
	private String planlongdesc;// 主险名称
	private String instfrom;// 交费起始日期
	private String instto;// 交费终止日期
	private String occdate;// 生效日期
	private String premterm;// 期数
	private List<CovInfo> covList=new ArrayList<CovInfo>();

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getQdFlag() {
		return qdFlag;
	}

	public void setQdFlag(String qdFlag) {
		this.qdFlag = qdFlag;
	}

	public String getChernum() {
		return chernum;
	}

	public void setChernum(String chernum) {
		this.chernum = chernum;
	}

	public String getRepnum() {
		return repnum;
	}

	public void setRepnum(String repnum) {
		this.repnum = repnum;
	}

	public String getTtmprcno() {
		return ttmprcno;
	}

	public void setTtmprcno(String ttmprcno) {
		this.ttmprcno = ttmprcno;
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

	public String getCustomerAddressand() {
		return customerAddressand;
	}

	public void setCustomerAddressand(String customerAddressand) {
		this.customerAddressand = customerAddressand;
	}

	public String getTaxpayerType() {
		return taxpayerType;
	}

	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerBankand() {
		return customerBankand;
	}

	public void setCustomerBankand(String customerBankand) {
		this.customerBankand = customerBankand;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getOrigcurr() {
		return origcurr;
	}

	public void setOrigcurr(String origcurr) {
		this.origcurr = origcurr;
	}

	public String getOrigamt() {
		return origamt;
	}

	public void setOrigamt(String origamt) {
		this.origamt = origamt;
	}

	public String getAcctamt() {
		return acctamt;
	}

	public void setAcctamt(String acctamt) {
		this.acctamt = acctamt;
	}

	public String getTrdt() {
		return trdt;
	}

	public void setTrdt(String trdt) {
		this.trdt = trdt;
	}

	public String getInvtyp() {
		return invtyp;
	}

	public void setInvtyp(String invtyp) {
		this.invtyp = invtyp;
	}

	public String getBustyp() {
		return bustyp;
	}

	public void setBustyp(String bustyp) {
		this.bustyp = bustyp;
	}

	public String getBillfreq() {
		return billfreq;
	}

	public void setBillfreq(String billfreq) {
		this.billfreq = billfreq;
	}

	public String getPolyear() {
		return polyear;
	}

	public void setPolyear(String polyear) {
		this.polyear = polyear;
	}

	public String getHissdte() {
		return hissdte;
	}

	public void setHissdte(String hissdte) {
		this.hissdte = hissdte;
	}

	public String getPlanlongdesc() {
		return planlongdesc;
	}

	public void setPlanlongdesc(String planlongdesc) {
		this.planlongdesc = planlongdesc;
	}

	public String getInstfrom() {
		return instfrom;
	}

	public void setInstfrom(String instfrom) {
		this.instfrom = instfrom;
	}

	public String getInstto() {
		return instto;
	}

	public void setInstto(String instto) {
		this.instto = instto;
	}

	public String getOccdate() {
		return occdate;
	}

	public void setOccdate(String occdate) {
		this.occdate = occdate;
	}

	public String getPremterm() {
		return premterm;
	}

	public void setPremterm(String premterm) {
		this.premterm = premterm;
	}

	public List<CovInfo> getCovList() {
		return covList;
	}

	public void setCovList(List<CovInfo> covList) {
		this.covList = covList;
	}

}
