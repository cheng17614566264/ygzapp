package com.cjit.vms.interval.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.interval.function.service.IntervalDao;
import com.cjit.vms.trans.model.TransInfoTemp;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-08
 */
public class IntervalDaoImpl extends GenericServiceImpl implements IntervalDao  {
	//-----------------------------初始化跑批任务表--------------------------------------
	@Override
	public Long findBatchRunTimeCount() {
		return this.getResultCount("intervalFindBatchRunTimeCount");
	}

	@Override
	public void deleteBatchRunTimeForInit() {
		Map map=new HashMap();
		this.delete("intervalDeleteBatchRunTimeForInit", map);
	}

	@Override
	public void insertBatchRunTimeInit() {
		Map map=new HashMap();
		this.save("intervalInsertBatchRunTimeInit", map);
	}
	
	//------------------------------初始化任务队列---------------------------------------
	@Override
	public List<BatchRunTime> findBatchRunTimeAllList(){
		Map map = new HashMap();
		List<BatchRunTime> batchRunTimeList = this.find("intervalFindBatchRunTimeAllList",map);
		return batchRunTimeList;
	}
	
	/*//------------------------------更新核心--------------------------------------------
	
	//跑批方法---交易信息
	@Override
	public List<TransInfoTemp> batchRunTransInfo() {
		
		Map map=new HashMap();
		map.put("valueFlage", 0);
		List<TransInfoTemp> transInfo=this.find("intervalBatchRunTransInfo", map);
		return transInfo;
	}
	//跑批方法--中间交易表插入到应用交易表
	@Override
	public void insertBatchRunTransInfo(Map map) {
		this.save("intervalinsertBatchRunTransInfo", map);
	}
	
	//跑批方法--修改中间表状态
	@Override
	public void updateTempStatus(Map map) {
		this.update("intervalUpdateTempStatus", map);	
	}
	
	//跑批方法----客户信息
	@Override
	public List<CustomerTemp> batchRunCustomerInfo() {
		Map map=new HashMap();
		map.put("valueFlage", 0);
		List<CustomerTemp> customerInfo=this.find("intervalbatchRunCustomerInfo", map);
		return customerInfo;
	}
	
	//跑批方法--客户信息插入到应用表
	@Override
	public void insertBatchRunCustomerInfo(Map map) {
		this.save("intervalInsertBatchRunCustomerInfo", map);
	}
	@Override
	public void deleteBatchRunCustomerInfo() {
		Map map = new HashMap();
		this.delete("intervalDeleteBatchRunCustomerInfo",map);
	}
	
	//---------------------------更新总账-----------------------------------------
	@Override
	public void deleteGeneralLedger(Map map) {
		this.delete("intervalDeleteGeneralLedger", map);
	}
	@Override
	public void insertGeneralLedger(Map map) {
		this.save("intervalInsertGeneralLedger", map);
	}
	
	//---------------------------更新费控------------------------------------------
	//从中间表中查出数据（主表）
	@Override
	public List<InputInfo> findDataByPrimary() {
		Map map = new HashMap();
		return this.find("intervalFindDataByPrimary",map);
	}
	//从中间表中查出数据（明细表）
	@Override
	public List<InputInvoiceNew> findDataByDetails() {
		Map map = new HashMap();
		return this.find("intervalFindDataByDetails",map);
	}
	//将数据插入到应用表（主表）
	@Override
	public void insertDataByPrimary(List<InputInfo> data) {
		Map<String,InputInfo> map = new HashMap<String,InputInfo>();
		this.delete("intervalDeleteDataByPrimary",map);
		for(int i = 0;i < data.size();i++){
			InputInfo inputInfo = data.get(i);
			System.out.println(inputInfo+"--------------------");
			map.put("inputInfo", inputInfo);
			this.save("intervalInsertDataByPrimary", map);
		}
	}
	//将数据插入到应用表（明细表）
	@Override
	public void insertDataByDetails(List<InputInvoiceNew> data) {
		Map<String,InputInvoiceNew> map = new HashMap<String,InputInvoiceNew>();
		this.delete("intervalDeleteDataByDetails",map);
		for(int i = 0;i < data.size();i++){
			InputInvoiceNew inputInvoiceNew = data.get(i);
			System.out.println(inputInvoiceNew+"--------------------");
			map.put("inputInvoiceNew", inputInvoiceNew);
			this.save("intervalInsertDataByDetails", map);
		}
	}*/

	//从vms_trans_info表中查询出犹豫期的电子发票(个险,首期)
	@Override
	public List selectTransInfoOfYouyuqi() {
		Map map = new HashMap();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(calendar.getTime());
		map.put("date", date);
		return this.find("selectTransInfoOfYouyuqi", map);
	}

}
