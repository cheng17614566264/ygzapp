package com.cjit.webService.client.entity;

import java.math.BigDecimal;

/**
 * 险种
 * @author jxjin
 *
 */
public class Cover {
	//交易类型
	private String transtype;
	//险种代码
	private String insCod;
	//险种名称
	private String insNam;
	//金额_人民币
	private BigDecimal amtCny;
	//税额_人民币
	private BigDecimal taxAmtCny;
	//收入_人民币
	private BigDecimal incomeCny;
	//税率S-6% N-3%	Z-0% P-17% F-免税
	private double taxRate;
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
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
	
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}
	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public BigDecimal getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}
	
}
