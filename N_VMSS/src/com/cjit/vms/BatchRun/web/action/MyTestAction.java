package com.cjit.vms.BatchRun.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.BatchRun.service.MyTestService;
import com.cjit.vms.trans.action.DataDealAction;

public class MyTestAction extends DataDealAction{
	
	private static final long serialVersionUID = 1L;

	/*建立service的set和个体方法以便于spring注入*/
	private MyTestService myTestService;
	
	public MyTestService getMyTestService() {
		return myTestService;
	}

	public void setMyTestService(MyTestService myTestService) {
		this.myTestService = myTestService;
	}

	BatchRunTime btr=new BatchRunTime();
	
	List<BatchRunTime> testContentList;
	
	private String result;
	
	/**
	 * 测试页面展示 
	 * @return
	 */
	
	
	public String myTest(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			System.out.println("============");
			//batchRunTimeService.updateBatchRunTime();
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ERROR;
		}
	}
	
	//测试内容展示
	/*public String testContent(){
		
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			String cname=request.getParameter("cname");
			
			System.out.println(cname);
			
			
			testContentList = myTestService.findTestContent(cname);
			String name=testContentList.get(0).getCname();
			int hour=testContentList.get(0).getHour();
			int minute=testContentList.get(0).getMinute();
			int second=testContentList.get(0).getSecond();
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
			
			
			JsonConfig jc=new JsonConfig();
			
			JSONArray ja=JSONArray.fromObject(batchRunTimelist);
			
			result=ja;
			
			
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return SUCCESS;
		
	}*/
}
