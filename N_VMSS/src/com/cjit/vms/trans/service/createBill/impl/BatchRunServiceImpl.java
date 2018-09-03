package com.cjit.vms.trans.service.createBill.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.vms.trans.service.createBill.BatchRunService;

public class BatchRunServiceImpl  extends GenericServiceImpl implements BatchRunService {
	
	//跑批方法---交易信息
	@Override
	public List<TransInfoTemp> batchRunTransInfo(Map map) {
		map.put("valueFlage", 0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = format.format(Calendar.getInstance().getTime());
		startTime = startTime + " 00:00:00";
		map.put("startTime", startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		String endTime = format.format(calendar.getTime());
		endTime = endTime + " 00:00:00";
		map.put("endTime", endTime);
		List<TransInfoTemp> transInfo = null;
		if(map.get("chernum")==null) {
			transInfo=find("batchRunTransInfo",map);
		}else {
			transInfo=find("selectbatchRunTransInfoForchernum",map);
		}
		
		return transInfo;
	}

	//跑批方法----客户信息
	@Override
	public List<CustomerTemp> batchRunCustomerInfo(Map map) {
		map.put("valueFlage", 0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = format.format(Calendar.getInstance().getTime());
		startTime = startTime + " 00:00:00";
		map.put("startTime", startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		String endTime = format.format(calendar.getTime());
		endTime = endTime + " 00:00:00";
		map.put("endTime", endTime);
		List<CustomerTemp> customerInfo=null;
		if(map.get("chernum")==null) {
			customerInfo=find("batchRunCustomerInfo",map);
		}else {
			customerInfo=find("selectbatchRunCustomerInfoForchernum",map);
		}
		
		System.out.println("customerInfo:"+customerInfo.size());
		
		return customerInfo;
	}
	
	//跑批方法--中间交易表插入到应用交易表
	@Override
	public void insertBatchRunTransInfo(Map map) {
		
		save("insertBatchRunTransInfo",map);
	}

	//跑批方法--客户信息插入到应用表
	@Override
	public void insertBatchRunCustomerInfo(Map map) {
		save("insertBatchRunCustomerInfo",map);
		
	}

	//跑批方法--修改中间表状态
    @Override
	public void updateTempStatus(Map map) {
		update("updateTempStatus",map);
			
	}
    
    //根据为电子发票的交易中的客户id查找客户信息---客户信息
	@Override
	public List batchRunCustomerInfoByIDForElectron(String stat) {
		Map map = new HashMap();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = format.format(Calendar.getInstance().getTime());
		startTime = startTime + " 00:00:00";
		map.put("startTime", startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		String endTime = format.format(calendar.getTime());
		endTime = endTime + " 00:00:00";
		map.put("endTime", endTime);
		map.put("CUSTOMER_ID", stat);
		return find("batchRunCustomerInfoByIDForElectron",map);
	}

	//根据保单号获取相同的险种信息
	@Override
	public List batchRunTransInfoOfINS(String stat) {
		Map map = new HashMap();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = format.format(Calendar.getInstance().getTime());
		startTime = startTime + " 00:00:00";
		map.put("startTime", startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		String endTime = format.format(calendar.getTime());
		endTime = endTime + " 00:00:00";
		map.put("endTime", endTime);
		map.put("CHERNUM", stat);
		return find("batchRunTransInfoOfINS",map);
	}
	
	//删除客户表中重复的数据
	@Override
	public void deleteBatchRunCustomerInfo(Map map) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("newCustomerId", map.get("CUSTOMER_ID")+"-"+format.format(Calendar.getInstance().getTime()));
		this.delete("deleteBatchRunCustomerInfo", map);
	}

	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：跑批方法--犹豫期退保，更新vms_trans_info表中对应个险犹豫期状态
	 * @param map
	 */
	@Override
	public void updateTransInfoOfYouyuqi(Map map) {
		this.update("updateTransInfoOfYouyuqi", map);
	}
}
