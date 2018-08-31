package com.cjit.vms.trans.service.createBill;

import java.util.List;
import java.util.Map;

public interface BatchRunService {
	//跑批方法--交易信息
	public List batchRunTransInfo(Map map);
	
	//跑批方法---客户信息
	public List batchRunCustomerInfo(Map map);
	
	//根据为电子发票的交易中的客户id查找客户信息---客户信息
	public List batchRunCustomerInfoByIDForElectron(String stat);
	
	//跑批方法--交易信息插入到应用表
	
	public void insertBatchRunTransInfo(Map map);
	
	//跑批方法--客户信息插入到应用表
	public void insertBatchRunCustomerInfo(Map map);
	
	//根据保单号获取相同的险种信息
	public List batchRunTransInfoOfINS(String stat);
	
	//跑批方法--修改中间表状态
	
	public void updateTempStatus(Map map);
}
