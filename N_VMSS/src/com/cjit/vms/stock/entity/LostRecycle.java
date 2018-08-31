package com.cjit.vms.stock.entity;

import java.util.Date;

/**
 * 
 * 发票遗失或回收信息记录表 VMS_LOST_RECYCLE
 *
 */
public class LostRecycle {
	// ID
	private String id;
	// 开票员所属机构
	private String instId;
	// 开票员编号
	private String kpyId;
	// 开票员名称
	private String kpyName;
	// 数据录入日期
	private Date operateDate;
	// 数据录入起始日期
	private String operateStartDate;
	// 数据录入截止日期
	private String operateEndDate;
	// 遗失、回收标志 0:遗失 1:回收2：作废
	private String flag;
	// 发票代码
	private String billId;
	// 发票起始号码
	private String billStartNo;
	// 发票截止号码
	private String billEndNo;
	// 回收或遗失张数
	private int count;
	// 是否确认 0:未确认 1:已确认
	private String state;
	// 分发编号
	private String disId;
	// 发票类型
	private String billType;
	// 开票员所属机构名称
	private String instName;
	// 
	private String datastatus;
	// 
	private String remark;
	
	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getDisId() {
		return disId;
	}

	public void setDisId(String disId) {
		this.disId = disId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getOperateStartDate() {
		return operateStartDate;
	}

	public void setOperateStartDate(String operateStartDate) {
		this.operateStartDate = operateStartDate;
	}

	public String getOperateEndDate() {
		return operateEndDate;
	}

	public void setOperateEndDate(String operateEndDate) {
		this.operateEndDate = operateEndDate;
	}
	
	
	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "LostRecycle [id=" + id + ", kpyId=" + kpyId + ", kpyName=" + kpyName + ", operateDate=" + operateDate
				+ ", flag=" + flag + ", billId=" + billId + ", billStartNo=" + billStartNo + ", billEndNo=" + billEndNo
				+ ", count=" + count + ", state=" + state + "]";
	}

}
