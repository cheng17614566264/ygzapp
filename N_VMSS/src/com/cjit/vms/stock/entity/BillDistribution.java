package com.cjit.vms.stock.entity;

import java.util.Date;

/**
 *票据分发记录表  VMS_BILL_DISTRIBUTION	
 */
public class BillDistribution {
	// 分发编号
	private String disId;
	// 分发日期
	private Date disDate;
	//分发起始日期
	private String disStartDate;
	//分发截止日期
	private String disEndDate;
	// 开票员所属机构
	private String instId;
	// 开票员编号
	private String kpyId;
	// 开票员名称
	private String kpyName;
	// 开票终端编号
	private String taxNo;
	// 发票代码
	private String billId;
	// 发票起始号码
	private String billStartNo;
	// 发票截止号码
	private String billEndNo;
	// 分发张数
	private Integer ffCount;
	//剩余张数
	private Integer syCount;
	// 接收确认 0:未确认 1:已确认
	private String jsEnter;
	// 已开票张数
	private Integer ykpCount;
	// 已打印张数
	private Integer ydyCount;
	// 已作废张数
	private Integer yffCount;
	// 已红冲张数
	private Integer yhcCount;
	//发票类型
	private String billType;
	//来源库存ID
	private String inventoryId;
	//机构名称
	private String instName;
	
	private Integer  syfpYsCount;//遗失
	private Integer  syfpHsCount;//回收
	private Integer  syfpZfCount;//作废
	public String getDisId() {
		return disId;
	}
	public void setDisId(String disId) {
		this.disId = disId;
	}
	public Date getDisDate() {
		return disDate;
	}
	public void setDisDate(Date disDate) {
		this.disDate = disDate;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getKpyId() {
		return kpyId;
	}
	public void setKpyId(String kpyId) {
		this.kpyId = kpyId;
	}
	public String getKpyName() {
		return kpyName;
	}
	public void setKpyName(String kpyName) {
		this.kpyName = kpyName;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillStartNo() {
		return billStartNo;
	}
	public void setBillStartNo(String billStartNo) {
		this.billStartNo = billStartNo;
	}
	public String getBillEndNo() {
		return billEndNo;
	}
	public void setBillEndNo(String billEndNo) {
		this.billEndNo = billEndNo;
	}
	
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	
	public String getDisStartDate() {
		return disStartDate;
	}
	public void setDisStartDate(String disStartDate) {
		this.disStartDate = disStartDate;
	}
	public String getDisEndDate() {
		return disEndDate;
	}
	public void setDisEndDate(String disEndDate) {
		this.disEndDate = disEndDate;
	}
	
	public Integer getSyCount() {
		return syCount;
	}
	public void setSyCount(Integer syCount) {
		this.syCount = syCount;
	}
	
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	
	public Integer getFfCount() {
		return ffCount;
	}
	public void setFfCount(Integer ffCount) {
		this.ffCount = ffCount;
	}
	public String getJsEnter() {
		return jsEnter;
	}
	public void setJsEnter(String jsEnter) {
		this.jsEnter = jsEnter;
	}
	public Integer getYkpCount() {
		return ykpCount;
	}
	public void setYkpCount(Integer ykpCount) {
		this.ykpCount = ykpCount;
	}
	public Integer getYdyCount() {
		return ydyCount;
	}
	public void setYdyCount(Integer ydyCount) {
		this.ydyCount = ydyCount;
	}
	public Integer getYffCount() {
		return yffCount;
	}
	public void setYffCount(Integer yffCount) {
		this.yffCount = yffCount;
	}
	public Integer getYhcCount() {
		return yhcCount;
	}
	public void setYhcCount(Integer yhcCount) {
		this.yhcCount = yhcCount;
	}
	
	public Integer getSyfpYsCount() {
		return syfpYsCount;
	}
	public void setSyfpYsCount(Integer syfpYsCount) {
		this.syfpYsCount = syfpYsCount;
	}
	public Integer getSyfpHsCount() {
		return syfpHsCount;
	}
	public void setSyfpHsCount(Integer syfpHsCount) {
		this.syfpHsCount = syfpHsCount;
	}
	public Integer getSyfpZfCount() {
		return syfpZfCount;
	}
	public void setSyfpZfCount(Integer syfpZfCount) {
		this.syfpZfCount = syfpZfCount;
	}
	@Override
	public String toString() {
		return "BillDistribution [disId=" + disId + ", disDate=" + disDate + ", kpyName=" + kpyName + ", taxNo=" + taxNo
				+ ", billId=" + billId + ", billStartNo=" + billStartNo + ", billEndNo=" + billEndNo + ", jsEnter="
				+ jsEnter + ", ykpCount=" + ykpCount + ", ydyCount=" + ydyCount + ", yffCount=" + yffCount
				+ ", yhcCount=" + yhcCount + "]";
	}
	
}
