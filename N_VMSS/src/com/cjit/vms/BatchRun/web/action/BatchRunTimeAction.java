package com.cjit.vms.BatchRun.web.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.BatchRun.service.BatchRunTimeService;
import com.cjit.vms.interval.function.IntervalChange;
import com.cjit.vms.taxdisk.service.TaxKeyInterfaceService;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.trans.action.DataDealAction;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class BatchRunTimeAction extends DataDealAction{
	
	
	private static final long serialVersionUID = 1L;
	

	
	/*建立service的set和个体方法以便于spring注入*/
	private BatchRunTimeService batchRunTimeService;
	
	BatchRunTime btr=new BatchRunTime();
	
	List<BatchRunTime> batchRunTimelist;
	
	private String result;
	
	/**
	 * 修改
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：跑批页面展示 
	 * @return
	 */
	
	
	public String batchRuntime(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			//查询所有的跑批单位
			batchRunTimelist = batchRunTimeService.findBatchRunTimeDepartList();
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ERROR;
		}
		
		
	}
	
	
	/**
	 * 跑批时间展示
	 * @return
	 */
	public String findBatchRunTime(){
		
	/*	System.out.println(brt.getCname());*/
		
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			String cname=request.getParameter("cname");
			
			System.out.println(cname+"+++++++++++++++++");
			
			
			batchRunTimelist = batchRunTimeService.findBatchRunTimeList(cname);
			String name=batchRunTimelist.get(0).getCname();
			int hour=batchRunTimelist.get(0).getHour();
			int minute=batchRunTimelist.get(0).getMinute();
			int second=batchRunTimelist.get(0).getSecond();
			btr.setCname(name);
			btr.setHour(hour);
			btr.setMinute(minute);
			btr.setSecond(second);
			
			Map<String,Object> map = new HashMap<String,Object>();

			            map.put("hour", hour);
			            map.put("minute", minute);
			            map.put("second", second);
			       		map.put("cname", name);
			JSONObject json = JSONObject.fromObject(map);
			
			result=json.toString();
			
			/*
			JsonConfig jc=new JsonConfig();
			
			JSONArray ja=JSONArray.fromObject(batchRunTimelist);
			
			result=ja;
			*/
			
			System.out.println(result+"**********************");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return SUCCESS;
		
	}
	
	
	/**
	 * 修改
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：跑批时间修改
	 * @return
	 */
	
	public String updateBatchRunTime(){
		
		/*	System.out.println(brt.getCname());*/
			
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			
			String cname=request.getParameter("cname");
			int hour=Integer.parseInt(request.getParameter("hour"));
			int minute=Integer.parseInt(request.getParameter("minute"));
			int second=Integer.parseInt(request.getParameter("second"));
			int intervalHour=Integer.parseInt(request.getParameter("intervalHour"));
			int intervalMinute=Integer.parseInt(request.getParameter("intervalMinute"));
			int intervalSecond=Integer.parseInt(request.getParameter("intervalSecond"));
			
			System.out.println("updateBatchRunTime:"+hour);
			System.out.println("updateBatchRunTime:"+cname);
			btr.setCname(cname);
			btr.setHour(hour);
			btr.setMinute(minute);
			btr.setSecond(second);
			btr.setIntervalHour(intervalHour);
			btr.setIntervalMinute(intervalMinute);
			btr.setIntervalSecond(intervalSecond);
			
			
			batchRunTimeService.updateBatchRunTime(btr);
			
			
			Map<String,Object> map = new HashMap<String,Object>();
	            map.put("hour", hour);
	            map.put("minute", minute);
	            map.put("second", second);
	            map.put("intervalHour", intervalHour);
	            map.put("intervalMinute", intervalMinute);
	            map.put("intervalSecond", intervalSecond);
	       		map.put("cname", cname);
	       		
			JSONObject json = JSONObject.fromObject(map);
			
			result=json.toString();
			
			//改变定时器刷新时间
			IntervalChange intervalChange = new IntervalChange();
			intervalChange.intervalChange(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return SUCCESS;
			
		}
	
	
	
	
	

	public BatchRunTimeService getBatchRunTimeService() {
		return batchRunTimeService;
	}

	public void setBatchRunTimeService(BatchRunTimeService batchRunTimeService) {
		this.batchRunTimeService = batchRunTimeService;
	}

	public List<BatchRunTime> getBatchRunTimelist() {
		return batchRunTimelist;
	}

	public void setBatchRunTimelist(List<BatchRunTime> batchRunTimelist) {
		batchRunTimelist = batchRunTimelist;
	}

	public BatchRunTime getBtr() {
		return btr;
	}

	public void setBtr(BatchRunTime btr) {
		this.btr = btr;
	}





	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
