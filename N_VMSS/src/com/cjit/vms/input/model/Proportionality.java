package com.cjit.vms.input.model;

import java.util.Arrays;
import java.util.List;

/**
 * 转出比例类
 *
 */
public class Proportionality {
	//机构编号
	private String instId;
	
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 */
	private String origInstId;
	
	public String getOrigInstId() {
		return origInstId;
	}
	public void setOrigInstId(String origInstId) {
		this.origInstId = origInstId;
	}
	//end20180827
	
	
	//机构名称
	private String instName;
	//会计机构编号
	private String kjInstId;
	//会计机构名称
	private String kjIstName;
	//操作日期
	private String operateDate;
	//比例值
	private String result;
	//会计月度
	private String yearMonth;
	//有效值 0-无效  1-有效 2-待审核 3-审核拒绝
	private String available;
	//数据来源(0:定时任务  1:人工计算)
	private String datasource;
	
	private List<String> resultList;
	//机构汇总编号
	private String relationInstId;
	//回退原因
	private String reason;
	//操作开始日期
	private String operateStartDate ;
	//操作结束日期
	private String operateEndDate ;
	// 被除数 分子
	private String dividend ;
	//除数  分母
	private String divisor ;
	
	private String apply_proposer_name;
	private String apply_proposer_id;
	
	private String auditor_proposer_name;
	private String auditor_proposer_id;
	
	private List<String> obj ;
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
	public String getKjInstId() {
		return kjInstId;
	}
	public void setKjInstId(String kjInstId) {
		this.kjInstId = kjInstId;
	}
	public String getKjIstName() {
		return kjIstName;
	}
	public void setKjIstName(String kjIstName) {
		this.kjIstName = kjIstName;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public List<String> getResultList() {
		return resultList;
	}
	public void setResultList(List<String> resultList) {
		this.resultList = resultList;
	}
	public String getRelationInstId() {
		return relationInstId;
	}
	public void setRelationInstId(String relationInstId) {
		this.relationInstId = relationInstId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public String getDividend() {
		return dividend;
	}
	public void setDividend(String dividend) {
		this.dividend = dividend;
	}
	public String getDivisor() {
		return divisor;
	}
	public void setDivisor(String divisor) {
		this.divisor = divisor;
	}
	public String getApply_proposer_name() {
		return apply_proposer_name;
	}
	public void setApply_proposer_name(String apply_proposer_name) {
		this.apply_proposer_name = apply_proposer_name;
	}
	public String getApply_proposer_id() {
		return apply_proposer_id;
	}
	public void setApply_proposer_id(String apply_proposer_id) {
		this.apply_proposer_id = apply_proposer_id;
	}
	public String getAuditor_proposer_name() {
		return auditor_proposer_name;
	}
	public void setAuditor_proposer_name(String auditor_proposer_name) {
		this.auditor_proposer_name = auditor_proposer_name;
	}
	public String getAuditor_proposer_id() {
		return auditor_proposer_id;
	}
	public void setAuditor_proposer_id(String auditor_proposer_id) {
		this.auditor_proposer_id = auditor_proposer_id;
	}
	public List<String> getObj() {
		return obj;
	}
	public void setObj(List<String> obj) {
		this.obj = obj;
	}
	
	
}
