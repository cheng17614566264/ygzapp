package com.cjit.vms.interval.function.service;

import java.util.List;
import java.util.Map;

import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.trans.model.TransInfoTemp;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-08
 */
public interface IntervalDao {
	
	//-----------------------------初始化跑批任务表--------------------------------------
	public Long findBatchRunTimeCount();
	public void deleteBatchRunTimeForInit();
	public void insertBatchRunTimeInit();
	
	//------------------------------初始化任务队列---------------------------------------
	public List<BatchRunTime> findBatchRunTimeAllList();
	/*//------------------------------更新核心--------------------------------------------
	//跑批方法---交易信息
	public List<TransInfoTemp> batchRunTransInfo();
	//跑批方法--中间交易表插入到应用交易表
	public void insertBatchRunTransInfo(Map map);
	//跑批方法--修改中间表状态
	public void updateTempStatus(Map map);
	//跑批方法----客户信息
	public List<CustomerTemp> batchRunCustomerInfo();
	//跑批方法--客户信息插入到应用表
	public void insertBatchRunCustomerInfo(Map map);
	public void deleteBatchRunCustomerInfo();
	//---------------------------更新总账-----------------------------------------
	public void deleteGeneralLedger(Map map);
	public void insertGeneralLedger(Map map);
	
	//---------------------------更新费控------------------------------------------
	//从中间表中查出数据（主表）
	public List<InputInfo> findDataByPrimary();
	//从中间表中查出数据（明细表）
	public List<InputInvoiceNew> findDataByDetails();
	//将数据插入到应用表（主表）
	public void insertDataByPrimary(List<InputInfo> data);
	//将数据插入到应用表（明细表）
	public void insertDataByDetails(List<InputInvoiceNew> data);
	*/
	
	//从vms_trans_info表中查询出犹豫期的电子发票(个险,首期)
	public List selectTransInfoOfYouyuqi();
	
}
