package com.cjit.webService.client.entity;
/**
 * 费控转出比例所需字段
 *
 */
public class TaxTransferInfo {
	//机构编码（费控机构代码值）
	private String orginCode;
	//转出比例期间（格式：YYYY-MM）
	private String periodName;
	//转出比例，无需百分号（如：转出比例为16.55%，返回16.55即可）
	private String rate;
	public String getOrginCode() {
		return orginCode;
	}
	public void setOrginCode(String orginCode) {
		this.orginCode = orginCode;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	
}
