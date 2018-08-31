package com.cjit.ws.entity;

public class VmsTransInfo {

	private String businessId;//交易流水号
	private String instId;//机构编号
	private String qdFlag;//团个标识
	private String chernum;//保单号
	private String repnum;//保单受理号
	private String ttmprcno;//投保单号
	private String origcurr;//交易币种
	private double origamt;//原币种交易金额
	private double acctamt;//本币交易金额
	private String trdt;//交易日期
	private String invtyp;//交易类型
	private String bustyp;//业务类型
	private String billfreq;//缴费频率
	private Integer polyear;//保单年度
	private String hissdte;//承保日期
	private String planlongdesc;//主险名称
	private String instfrom;//缴费开始日期
	private String instto;//缴费结束日期
	private String occdate;//生效日期
	private Integer premterm;//期数
	private String feetyp;//费用类型
	private double amtCny;//金额人民币
	private double taxAmtCny;//税额人民币
	private double incomeCny;//收入金额
	private double taxRate;//税率
	private String transType;//交易类型
	private String insCod;
	private String insNam;
	private String customerid;//add by LQ 20160704 增加客户号逻辑
	private String customerAccount;//开户行账号
	//新增 2018-07-09
	private String transUUID;//uuId
	private String vatRateCode;//税率标号 S-N-Z-P-F
	
	
	public String getTransUUID() {
		return transUUID;
	}
	public void setTransUUID(String transUUID) {
		this.transUUID = transUUID;
	}
	public String getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
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
	public String getOrigcurr() {
		return origcurr;
	}
	public void setOrigcurr(String origcurr) {
		this.origcurr = origcurr;
	}
	public double getOrigamt() {
		return origamt;
	}
	public void setOrigamt(double origamt) {
		this.origamt = origamt;
	}
	public double getAcctamt() {
		return acctamt;
	}
	public void setAcctamt(double acctamt) {
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
	public Integer getPolyear() {
		return polyear;
	}
	public void setPolyear(Integer polyear) {
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
	public Integer getPremterm() {
		return premterm;
	}
	public void setPremterm(Integer premterm) {
		this.premterm = premterm;
	}
	public String getFeetyp() {
		return feetyp;
	}
	public void setFeetyp(String feetyp) {
		this.feetyp = feetyp;
	}
	public double getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(double amtCny) {
		this.amtCny = amtCny;
	}
	public double getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(double taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public double getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(double incomeCny) {
		this.incomeCny = incomeCny;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getInsCod() {
		return insCod;
	}
	public void setInsCod(String insCod) {
		this.insCod = insCod;
	}
	public String getInsNam() {
		return insNam;
	}
	public void setInsNam(String insNam) {
		this.insNam = insNam;
	}
	public String getVatRateCode() {
		return vatRateCode;
	}
	public void setVatRateCode(String vatRateCode) {
		this.vatRateCode = vatRateCode;
	}
	
}
