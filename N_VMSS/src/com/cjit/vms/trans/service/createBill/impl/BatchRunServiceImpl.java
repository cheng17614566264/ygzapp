package com.cjit.vms.trans.service.createBill.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.vms.trans.service.createBill.BatchRunService;

public class BatchRunServiceImpl  extends GenericServiceImpl implements BatchRunService {
	
	//跑批方法---交易信息
	@Override
	public List<TransInfoTemp> batchRunTransInfo(Map map) {
		
		List<TransInfoTemp> transInfo = null;
		map.put("valueFlage", 0);
		/**
		 * 修改
		 * 日期：2018-09-04
		 * 作者：刘俊杰
		 * 功能：查询核心自动跑批时间，根据设置的跑批时间间隔，获取数据
		 */
		map.put("cname", "核心");
		List<BatchRunTime> batchRunTimeList = this.find("intervalFindBatchRunTimeByCname",map);
		for(BatchRunTime blist : batchRunTimeList) {
			double interval = blist.getIntervalHour()*60*60 + blist.getIntervalMinute()*60 + blist.getIntervalSecond();  //获取跑批时间间隔的秒数
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();  //获取当前日历对象
			Integer day = (int) Math.ceil(interval / (24*60*60));  //将跑批时间间隔转换为天,向上取整
			calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-day); //设置日历对应的天数为前day天
			String startTime = format.format(calendar.getTime());  //格式化日期获取对应的字符串
			startTime = startTime + " 00:00:00";  //设置开始查询时间为零点,防止重设时间后有遗漏
			map.put("startTime", startTime);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+day+1); //设置结束查询时间为第二天的零点
			String endTime = format.format(calendar.getTime());
			endTime = endTime + " 00:00:00";
			map.put("endTime", endTime);
			transInfo=find("batchRunTransInfo",map);
		}
		return transInfo;
	}

	//跑批方法----客户信息
	@Override
	public List<CustomerTemp> batchRunCustomerInfo(Map map) {
		List<CustomerTemp> customerInfo=null;
		map.put("valueFlage", 0);
		/**
		 * 修改
		 * 日期：2018-09-04
		 * 作者：刘俊杰
		 * 功能：查询核心自动跑批时间，根据设置的跑批时间间隔，获取数据
		 */
		map.put("cname", "核心");
		List<BatchRunTime> batchRunTimeList = this.find("intervalFindBatchRunTimeByCname",map);
		for(BatchRunTime blist : batchRunTimeList) {
			double interval = blist.getIntervalHour()*60*60 + blist.getIntervalMinute()*60 + blist.getIntervalSecond();  //获取跑批时间间隔的秒数
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();  //获取当前日历对象
			Integer day = (int) Math.ceil(interval / (24*60*60));  //将跑批时间间隔转换为天,向上取整
			calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-day); //设置日历对应的天数为前day天
			String startTime = format.format(calendar.getTime());  //格式化日期获取对应的字符串
			startTime = startTime + " 00:00:00";  //设置开始查询时间为零点,防止重设时间后有遗漏
			map.put("startTime", startTime);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+day+1); //设置结束查询时间为第二天的零点
			String endTime = format.format(calendar.getTime());
			endTime = endTime + " 00:00:00";
			map.put("endTime", endTime);
			customerInfo=find("batchRunCustomerInfo",map);
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
	public List batchRunCustomerInfoOfINS(Map map) {
		return find("batchRunCustomerInfoOfINS",map);
	}
	@Override
	public List batchRunCustomerInfoOfINSForHesitate(Map map) {
		return find("batchRunCustomerInfoOfINSForHesitate",map);
	}

	//根据保单号获取相同的险种信息
	@Override
	public List batchRunTransInfoOfINS(Map map) {
		return find("batchRunTransInfoOfINS",map);
	}
	@Override
	public List batchRunTransInfoOfINSForHesitate(Map map) {
		return find("batchRunTransInfoOfINSForHesitate",map);
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
