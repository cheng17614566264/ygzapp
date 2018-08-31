package com.cjit.vms.BatchRun.service;

import java.sql.Time;
import java.util.List;

import com.cjit.vms.BatchRun.model.BatchRunTime;


public interface BatchRunTimeService {
	
	//查看当前跑批时间
	public List findBatchRunTimeList(String cname) throws Exception;
 	
	
	//修改跑批时间
	public void updateBatchRunTime(BatchRunTime time) throws Exception;
}
