package com.cjit.vms.trans.model;

public class CovInfo {
	private String insCod;// 险种代码
	private String insNam;// 险种名称
	private String feetyp;// 费用类型
	private String amtCny;// 金额_人民币
	private String taxAmtCny;// 税额_人民币
	private String incomeCny;// 收入_人民币
	private String taxRate;// 税率

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

	public String getFeetyp() {
		return feetyp;
	}

	public void setFeetyp(String feetyp) {
		this.feetyp = feetyp;
	}

	public String getAmtCny() {
		return amtCny;
	}

	public void setAmtCny(String amtCny) {
		this.amtCny = amtCny;
	}

	public String getTaxAmtCny() {
		return taxAmtCny;
	}

	public void setTaxAmtCny(String taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public String getIncomeCny() {
		return incomeCny;
	}

	public void setIncomeCny(String incomeCny) {
		this.incomeCny = incomeCny;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

}
