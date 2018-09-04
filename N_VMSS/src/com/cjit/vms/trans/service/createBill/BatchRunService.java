package com.cjit.vms.trans.service.createBill;

import java.util.List;
import java.util.Map;

public interface BatchRunService {
	//跑批方法--交易信息
	public List batchRunTransInfo(Map map);
	
	//跑批方法---客户信息
	public List batchRunCustomerInfo(Map map);
	
	//根据为电子发票的交易中的客户id查找客户信息---客户信息
	public List batchRunCustomerInfoOfINS(Map map);
	public List batchRunCustomerInfoOfINSForHesitate(Map map);
	
	//跑批方法--交易信息插入到应用表
	
	public void insertBatchRunTransInfo(Map map);
	
	//跑批方法--客户信息插入到应用表
	public void insertBatchRunCustomerInfo(Map map);
	
	//根据保单号获取相同的险种信息
	public List batchRunTransInfoOfINS(Map map);
	public List batchRunTransInfoOfINSForHesitate(Map map);
	
	//跑批方法--修改中间表状态
	
	public void updateTempStatus(Map map);
	//跑批方法--删除客户表中重复的数据
	public void deleteBatchRunCustomerInfo(Map map);
	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：跑批方法--犹豫期退保，更新vms_trans_info表中对应个险犹豫期状态
	 * @param map
	 */
	public void updateTransInfoOfYouyuqi(Map map);
	//end 2018-09-03
}
