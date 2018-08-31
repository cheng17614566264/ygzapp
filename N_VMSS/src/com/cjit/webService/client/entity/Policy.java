package com.cjit.webService.client.entity;

import java.math.BigDecimal;

/**
 * 保单类
 * @author jxjin
 *
 */
public class Policy implements Cloneable{
	/*-------------------报文中包含的字段-----------------------*/
	//交易流水号
	private String businessId;
	//业务机构
	private String instId;
	//团个标志
	private String qdFlag;
	//保单号
	private String chernum;
	//保全受理号
	private String repnum;
	//投保单号
	private String ttmprcno;
	//客户
	private Customer customer ;
	//交易币种
	private String origcurr;
	//原币种交易金额
	private BigDecimal origamt;
	//本币交易金额
	private BigDecimal acctamt;
	//交易日期
	private String trdt;
	//交易描述
	private String batctrcde;
	//发票类型
	private String invtyp;
	//费用类型
	private String feetyp;
	//交费频率
	private String billfreq;
	//保单年度
	private String polyear;
	//承保日期
	private String hissdte;
	//主险名称
	private String planlongdesc;
	//交费起始日期
	private String instfrom;
	//交费终止日期
	private String instto;
	//生效日期
	private String occdate;
	//续期期数
	private String premterm;
	//险种
	private Cover cover;
	//犹豫期
	private String hesitatePeriod;
	//批单号
	private String batchNo;
	//受理类型
	private String batchType;
	/*-------------------报文中不包含的字段-----------------------*/
	//含税标志Y-含/N-不含
	private String taxFlag;
	//未开票税额
	private BigDecimal taxCnyBalance;
	//未开票金额
	private BigDecimal balance;
	//数据来源
	private String dsource;
	//数据状态
	private String dataStatus;
	//是否预开
	private String isYK;
	//交易ID
	private String transId;
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getOrigcurr() {
		return origcurr;
	}
	public void setOrigcurr(String origcurr) {
		this.origcurr = origcurr;
	}
	public String getTrdt() {
		return trdt;
	}
	public void setTrdt(String trdt) {
		this.trdt = trdt;
	}
	public String getBatctrcde() {
		return batctrcde;
	}
	public void setBatctrcde(String batctrcde) {
		this.batctrcde = batctrcde;
	}
	public String getInvtyp() {
		return invtyp;
	}
	public void setInvtyp(String invtyp) {
		this.invtyp = invtyp;
	}
	public String getFeetyp() {
		return feetyp;
	}
	public void setFeetyp(String feetyp) {
		this.feetyp = feetyp;
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
	public String getHesitatePeriod() {
		return hesitatePeriod;
	}
	public void setHesitatePeriod(String hesitatePeriod) {
		this.hesitatePeriod = hesitatePeriod;
	}
	public Cover getCover() {
		return cover;
	}
	public void setCover(Cover cover) {
		this.cover = cover;
	}
	
	public BigDecimal getOrigamt() {
		return origamt;
	}
	public void setOrigamt(BigDecimal origamt) {
		this.origamt = origamt;
	}
	public BigDecimal getAcctamt() {
		return acctamt;
	}
	public void setAcctamt(BigDecimal acctamt) {
		this.acctamt = acctamt;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getTaxCnyBalance() {
		return taxCnyBalance;
	}
	public void setTaxCnyBalance(BigDecimal taxCnyBalance) {
		this.taxCnyBalance = taxCnyBalance;
	}
	public String getTaxFlag() {
		return taxFlag;
	}
	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}
	
	
	public String getDsource() {
		return dsource;
	}
	public void setDsource(String dsource) {
		this.dsource = dsource;
	}
	
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
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
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	@Override
	public String toString() {
		return "Policy [businessId=" + businessId + ", instId=" + instId + ", qdFlag=" + qdFlag + ", chernum=" + chernum
				+ ", repnum=" + repnum + ", ttmprcno=" + ttmprcno + ", customer=" + customer + ", origcurr=" + origcurr
				+ ", origamt=" + origamt + ", acctamt=" + acctamt + ", trdt=" + trdt + ", batctrcde=" + batctrcde
				+ ", invtyp=" + invtyp + ", feetyp=" + feetyp + ", billfreq=" + billfreq + ", polyear=" + polyear
				+ ", hissdte=" + hissdte + ", planlongdesc=" + planlongdesc + ", instfrom=" + instfrom + ", instto="
				+ instto + ", occdate=" + occdate + ", premterm=" + premterm + ", cover=" + cover + ", hesitatePeriod="
				+ hesitatePeriod + ", batchNo=" + batchNo + ", batchType=" + batchType + ", taxFlag=" + taxFlag
				+ ", taxCnyBalance=" + taxCnyBalance + ", balance=" + balance + ", dsource=" + dsource + ", dataStatus="
				+ dataStatus + ", isYK=" + isYK + ", transId=" + transId + "]";
	}
	
}
