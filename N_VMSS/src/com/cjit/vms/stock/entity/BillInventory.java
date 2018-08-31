package com.cjit.vms.stock.entity;

import java.util.Date;

/**
 * 库存表   VMS_BILL_INVENTORY	
 */
public class BillInventory {
	// 库存编号
	private String inventoryId;
	// 机构代码
	private String instId;
	// 机构名称
	private String instName;
	// 入库日期
	private Date putInDate;
	//入库起始日期
	private String putInStartDate;
	//入库截止日期
	private String putInEndDate;
	// 总张数
	private Integer count;
	// 发票代码
	private String billId;
	// 发票起始号码
	private String billStartNo;
	// 发票截止号码
	private String billEndNo;

	//发票类型
	private String billType;
	//剩余张数
	private String syCount;

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public Date getPutInDate() {
		return putInDate;
	}

	public void setPutInDate(Date putInDate) {
		this.putInDate = putInDate;
	}

	public String getPutInStartDate() {
		return putInStartDate;
	}

	public void setPutInStartDate(String putInStartDate) {
		this.putInStartDate = putInStartDate;
	}

	public String getPutInEndDate() {
		return putInEndDate;
	}

	public void setPutInEndDate(String putInEndDate) {
		this.putInEndDate = putInEndDate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public String getSyCount() {
		return syCount;
	}

	public void setSyCount(String syCount) {
		this.syCount = syCount;
	}

	@Override
	public String toString() {
		return "BillInventory [inventoryId=" + inventoryId +", putInDate=" + putInDate + ", count=" + count  + "]";
	}
}
