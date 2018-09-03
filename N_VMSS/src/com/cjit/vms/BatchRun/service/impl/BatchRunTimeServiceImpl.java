package com.cjit.vms.BatchRun.service.impl;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.BatchRun.service.BatchRunTimeService;


public class BatchRunTimeServiceImpl extends GenericServiceImpl implements BatchRunTimeService{

	@Override
	public List findBatchRunTimeList(String cname) throws Exception {
		Map map = new HashMap();
		map.put("cname", cname); // 对参数进行封装
		
		return this.find("findBatchRunTime",map);
	}
	
	@Override
	public void updateBatchRunTime(BatchRunTime time) throws Exception {
		Map map = new HashMap();
		map.put("Time", time); // 对时间进行封装
		this.update("updateBatchRunTime",map);
	}
	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：查询所有的跑批部门
	 * @return
	 * @throws Exception
	 */
	@Override
	public List findBatchRunTimeDepartList() throws Exception {
		Map map = new HashMap();
		return this.find("findBatchRunTimeDepart", map);
	}
	//end 2018-09-03
}
