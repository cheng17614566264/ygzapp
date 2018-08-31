package com.cjit.vms.input.listener;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.transport.http.ForbidSessionCreationWrapper;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.cjit.common.action.BaseAction;
import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.action.InvoiceSurtaxAction;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.PullDataService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.webService.client.CemClient;
import com.cjit.webService.client.entity.TaxTransferInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 转出比例计算
 * 
 *
 */
public class ProportionalityAction extends BaseAction implements Runnable{
	private static final long serialVersionUID = 1L;
	private PullDataService service;
	private Logger log=Logger.getLogger(ProportionalityAction.class);
	@SuppressWarnings("rawtypes")
	
	private Map session;
	public ProportionalityAction(){
		
	}
	public ProportionalityAction(PullDataService service) {
		super();
		this.service = service;
	}
	/**
	 * 得到当前用户
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		User user = (User) session.get(Constants.USER);
		if (user == null) {
			throw new RuntimeException("用户尚未登录");
		}
		return user;
	}
	/**
	 * 当关账之后，传转出比例给费控系统
	 */
	@Override
	public void run() {
		/*//获取上个月的年月
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE, 1);
		Date currentDate = calendar.getTime();
		Map<String, String> param = new HashMap<String, String>();
		param.put("yearMonth", new SimpleDateFormat("yyyyMM").format(currentDate));
		param.put("yearAndMonth", new SimpleDateFormat("yyyy-MM").format(currentDate));
		//获取批处理时间
		Date startDate = getRunTime();
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		if (!(sFormat.format(date).equals(sFormat.format(startDate)))) {
			return;
		}
		List<String> urList=service.findWebServiceUrl("feikong");
		CemClient cemClient=new CemClient(urList.get(0));
		Gson gson=new GsonBuilder().serializeNulls().create();
		List<TaxTransferInfo> outRateInfo=service.findRollOutRatio(param);
		String json="{\"outRateInfo\":"+gson.toJson(outRateInfo)+"}";
		String cemOut=cemClient.receiver("wsVatServer", "turnOutRateHandle", json);
		log.info(cemOut);*/
		        //查找所有要计算比例的机构
				List<String> listinstId=service.findtimeproportioninstId();
				Map<String, String> param = new HashMap<String, String>();
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
				calendar.set(Calendar.DATE,1 );
				Date currentDate = calendar.getTime();
				param.put("yearMonth", new SimpleDateFormat("yyyyMM").format(currentDate));
				param.put("yearAndMonth", new SimpleDateFormat("yyyy-MM").format(currentDate));
				param.put("category", "2");
				param.put("taxRateCode", "1");
				for (String string : listinstId) {
					param.put("instId", string);
					List<String> reportInst=service.findReportInst(param.get("instId"));
					List<Map<String, String>> proportionalList = rollouthxMothed(param, reportInst);
					if(proportionalList.size()>0){
						//修改有效值
						service.delDistinctData(param);
						service.insertProportionality(proportionalList);
 						this.run(string);
					}else {
						System.err.println(param.get("instId")+"比例计算失败,总帐暂无上月数据");
					}
				}
	}
	
	/**
	 * 得到比例计算时间节点    时间过不可执行比例计算
	 * @return
	 * @throws NumberFormatException
	 */
	private Date getRunTime() throws NumberFormatException {
		List<TimeTaskEntity> findTimeTisk = service.findTimeTisk("1");
		String time="";
		for (TimeTaskEntity timeTaskEntity : findTimeTisk) {
			if (timeTaskEntity.getClassName().equals("timeNode")) {
				time=timeTaskEntity.getStartTime();
				break;
			}
		}
		String value=time.split("-")[2];
		if (value.indexOf("0")==0) {
			value=value.substring(1,value.length());
		}
		String[] values=value.split(" ");
		int day=Integer.valueOf(values[0]);
		Calendar cal=Calendar.getInstance();
		
 		String []strings =values[1].split(":");
		int h=Integer.valueOf(strings[0]);
		int m=Integer.valueOf(strings[1]);
		int s=Integer.valueOf(strings[2]);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		cal.set(Calendar.SECOND, s); 
		Date startDate=cal.getTime();
		return startDate;
	}
	public void run(String instId) {
		//获取上个月的年月
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE, 1);
		Date currentDate = calendar.getTime();
		Map<String, String> param = new HashMap<String, String>();
		param.put("yearMonth", new SimpleDateFormat("yyyyMM").format(currentDate));
		param.put("yearAndMonth", new SimpleDateFormat("yyyy-MM").format(currentDate));
		param.put("yearAndMonth", new SimpleDateFormat("yyyy-MM").format(currentDate));
		param.put("instId", instId);
		
		//国富项目修改
		//List<String> urList=service.findWebServiceUrl("feikong");
		//CemClient cemClient=new CemClient(urList.get(0));
		Gson gson=new GsonBuilder().serializeNulls().create();
		List<TaxTransferInfo> outRateInfo=service.findRollOutRatio(param);
		String json="{\"outRateInfo\":"+gson.toJson(outRateInfo)+"}";
		//String cemOut=cemClient.receiver("wsVatServer", "turnOutRateHandle", json);
		//log.info(cemOut);
	}
	public void reckonAction(){
		//得到最后执行计算比例时间
		Date startDate = getRunTime();
		AjaxReturn coreMessage =null;
		String errorMessage = "";
		Calendar calendar = Calendar.getInstance();
		Calendar startcal=Calendar.getInstance();
		startcal.setTime(startDate);
		int res=calendar.compareTo(startcal);
		if(res<0){
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
			calendar.set(Calendar.DATE,1 );
			Date currentDate = calendar.getTime();
			Map<String, String> param = new HashMap<String, String>();
			param.put("yearMonth", new SimpleDateFormat("yyyyMM").format(currentDate));
			param.put("yearAndMonth", new SimpleDateFormat("yyyy-MM").format(currentDate));
			param.put("category", "2");
			param.put("taxRateCode", "1");
			param.put("instId", this.getCurrentUser().getOrgId());
			param.put("apply_id", this.getCurrentUser().getOrgId());
			param.put("apply_name", this.getCurrentUser().getOrgName());
//本地测试不回传费控---服务传给费控（放开）
			boolean result=this.ratioCal(param,errorMessage);
			if (result) {
				coreMessage = new AjaxReturn(true,"比例计算已完成");
				this.run(this.getCurrentUser().getOrgId());
 			}else {
 				
 				coreMessage = new AjaxReturn(false, errorMessage);
 			}
		}else{
			errorMessage="比例计算已过时间，不可计算！";
			coreMessage = new AjaxReturn(false, errorMessage);
			System.err.println(errorMessage);
		}
		try {
			returnResult(coreMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}
	/**
	 * 比例计算，并保存到数据库
	 */
	private boolean ratioCal(Map<String, String> param,String errorMessage){
		System.out.println(param.get("instId"));
		//2018-07-31更改
		/*List<String> reportInst=service.findReportInst(param.get("instId"));
		if (CollectionUtil.isEmpty(reportInst)) {
			errorMessage="当前机构无比例计算权限！";
			return false;
		}
		List<String> reportInst = new ArrayList<String>();
		reportInst.add(param.get("instId"));
		List<Map<String, String>> proportionalList = rollouthxMothed(param, reportInst);*/
		//2018-08-20新增start 执行转出比例计算
		Map subjectEntityMap = new HashMap();
		subjectEntityMap.put("instId", param.get("instId"));
		//遍历总的免税信息
		List<CodeDictionary> codeDictionaryList =  service.selectCodeDictionaryAll();
		BigDecimal generalLedgerOfTaxfree = new BigDecimal(0.00);
		for(CodeDictionary codeDictionary : codeDictionaryList) {
			generalLedgerOfTaxfree = generalLedgerOfTaxfree.add(BigDecimal.valueOf(Double.parseDouble( codeDictionary.getBACKUP_NUM())));
		}
		//获取总的免税+应税信息
		List<SubjectEntity> subjectEntityListAll = service.getSubjectLedgerAll(subjectEntityMap);
		//将结果计算后写入数据库表vms_timertask_proportionality中
		if(subjectEntityListAll.size()>0) {
			Map resultMap = new HashMap();
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			
			String result= generalLedgerOfTaxfree.divide(subjectEntityListAll.get(0).getCreditDescSum(), 2, RoundingMode.HALF_EVEN).toString();
			resultMap.put("operateDate", now);
			resultMap.put("dividend", generalLedgerOfTaxfree);
			resultMap.put("divisor", subjectEntityListAll.get(0).getCreditDescSum());
			resultMap.put("result", result);
			resultMap.put("instId", param.get("instId"));//2018-08-23改，将数据库中的INST_ID改为了ORIG_INST_ID
			resultMap.put("dataSource", "0");
			resultMap.put("yearMonth", param.get("yearMonth"));
			resultMap.put("available", "1");
			resultMap.put("apply_id", param.get("apply_id"));
			resultMap.put("apply_name", param.get("apply_name"));
			try {
				
				//查询业务机构对应财务机构
				List<String> instList = service.findInstRelation(param.get("instId"));
				String parentId = "";
				for(int i=0;i<instList.size();i++){
					//查询财务机构对应上级机构
					List<String> listParent = service.findInstLast(instList.get(i));
					if(instList.contains(listParent.get(0)) && listParent != null){
						continue;
					}
					parentId = instList.get(i);
				}
				
				resultMap.put("newInstId", parentId);
				List<Map<String, String>> li = new ArrayList<Map<String, String>>();
				li.add(resultMap);
				service.insertTransferRatio(li);
				
				resultMap.put("orig_instId", parentId); //2018-08-23增，将数据库中的INST_ID改为了ORIG_INST_ID
				//删除比例表中的重复数据
				service.delDistinctData(param);
				//将结果写入中间表中
				service.insertProportionality(resultMap);
				
				
				
				return true;
			}catch(Exception e) {
				e.printStackTrace();
				errorMessage="比例计算失败,发生未知错误，请联系管理员";
				return false;
			}
		}
		//2018-08-20新增end
		/*if(proportionalList.size()>0){
			//删除比例表中的重复数据
			service.delDistinctData(param);
			service.insertProportionality(proportionalList);
			
			//2018-04-03新增 将比例结果存入费控中间表中（主表）
			for(Map<String,String> map : proportionalList){
				if(param.get("instId").equals(map.get("instId"))){
					//查询费控对应机构
					//List<String> lis = service.findInstMapping(param.get("instId"));
					
					//查询业务机构对应财务机构
					List<String> instList = service.findInstRelation(param.get("instId"));
					String parentId = "";
					for(int i=0;i<instList.size();i++){
						//查询财务机构对应上级机构
						List<String> listParent = service.findInstLast(instList.get(i));
						if(instList.contains(listParent.get(0)) && listParent != null){
							continue;
						}
						parentId = instList.get(i);
					}
					
					//map.put("newInstId", lis.get(0));
					map.put("newInstId", parentId);
					List<Map<String, String>> li = new ArrayList<Map<String, String>>();
					li.add(map);
					service.insertTransferRatio(li);
				}
			}
			return true;
		}*/else {
			errorMessage="比例计算失败,总帐暂无上月数据";
			return false;
		}
		
	}
	/**
	 * @param param
	 * @param reportInst
	 * @return
	 */
	private List<Map<String, String>> rollouthxMothed(Map<String, String> param, List<String> reportInst) {
		//查找免税收入
		List<Map<String, Object>> transList = service.findAccBanlanceInfo(param);
		
		if (CollectionUtil.isEmpty(transList)) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("INSTID", param.get("instId"));
			map.put("RATE", "0");
			transList.add(map);
		}
		param.remove("category");
		param.remove("taxRateCode");
		//查找总收入
		List<Map<String, Object>> balanceInfoList = service.findAccBanlanceInfo(param);
		
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Map<String, String>> proportionalList = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map : transList) {
			String instId = map.get("INSTID").toString();
			if (instId == null) {
				continue;
			}
			//免税收入
			BigDecimal dividend = new BigDecimal(map.get("RATE").toString());
			//总收入
			BigDecimal divisor = null;
			String result = "0";
			for (int i=0;i<balanceInfoList.size();i++) {
				Map<String, Object> bMap=balanceInfoList.get(i);
				if (instId.equals(bMap.get("INSTID").toString())) {
					divisor = new BigDecimal(bMap.get("RATE").toString());  // 免税+应税
					if (divisor.compareTo(BigDecimal.ZERO) != 0) {
						result = dividend.divide(divisor, 2, RoundingMode.HALF_EVEN).toString();
					}
					String yearMonth=param.get("yearMonth");
					for (String inst : reportInst) {
						Map<String,String> paramTempMap=new HashMap<String, String>();
						paramTempMap.put("operateDate", now);
						paramTempMap.put("dividend", dividend.toString());
						paramTempMap.put("divisor", divisor.toString());
						paramTempMap.put("result", result);
						paramTempMap.put("instId", inst);
						paramTempMap.put("dataSource", "0");
						paramTempMap.put("yearMonth", yearMonth);
						paramTempMap.put("available", "1");
						paramTempMap.put("apply_id", (String) map.get("apply_id"));
						paramTempMap.put("apply_name", (String) map.get("apply_name"));
						proportionalList.add(paramTempMap);
					}
					
				}
			}
		}
		return proportionalList;
	}
	
	public void rollout(){
		//得到最后执行计算比例时间
		Date startDate = getRunTime();
		Calendar calendar = Calendar.getInstance();
		Calendar startcal=Calendar.getInstance();
		startcal.setTime(startDate);
		int res=calendar.compareTo(startcal);
		if(res<0){
			String rup=request.getParameter("rup");
			String KjInstId=request.getParameter("KjInstId");
			String YearMonth=request.getParameter("YearMonth");
			String instid=request.getParameter("instid");
			String operateDate=request.getParameter("operateDate");
			//修改有效值
			Proportionality proportionality=new Proportionality();
			proportionality.setKjInstId(KjInstId);
			proportionality.setYearMonth(YearMonth);
			proportionality.setOperateDate(operateDate);
			service.updateProportionality(proportionality,"Y");
			
			List<String> reportInst=service.findReportInst(instid);
			// 加入调整比例值
			for(String instId:reportInst){
				List<Map<String, String>> proportionalList = new ArrayList<Map<String, String>>();
				Map<String,String> paramTempMap=new HashMap<String, String>();
				paramTempMap.put("operateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				paramTempMap.put("dividend", "0");
				paramTempMap.put("divisor", "0");
				paramTempMap.put("result", rup);
				paramTempMap.put("instId", instId);
				paramTempMap.put("available", "1");
				paramTempMap.put("dataSource", "1");
				paramTempMap.put("yearMonth", YearMonth);
				proportionalList.add(paramTempMap);
				service.insertProportionality(proportionalList);
			}
			
			//回传费控
			List<String> list=service.findWebServiceUrl("feikong");
			CemClient client=new CemClient(list.get(0));
			Gson gson=new GsonBuilder().serializeNulls().create();
			Map<String, String> param=new HashMap<String, String>();
			param.put("yearMonth", YearMonth);
			param.put("instId", instid);
			List<TaxTransferInfo> outRateInfo=service.findRollOutRatio(param);
			String json="{\"outRateInfo\":"+gson.toJson(outRateInfo)+"}";
			String cemOut=client.receiver("wsVatServer", "turnOutRateHandle", json);
			log.info(cemOut);
			response.setCharacterEncoding("UTF-8");
 	    	try {
				response.getWriter().print("Y");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			response.setCharacterEncoding("UTF-8");
 	    	try {
				response.getWriter().print("N");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 转出比例调整审核  审核通过
	 * @return
	 */
	@SuppressWarnings("unused")
	public void AuditRollout(){
		String date=request.getParameter("date");
		String instId=date.split("-")[0];//登录用户的机构id 86
		//String kjinstId=service.findInstIdACCOUNTING(instId);//增值税系统中机构Id对应费控中的机构id 200011
		String audit=request.getParameter("audit");
		String yearMoth =date.split("-")[1];
		String operateDate=date.split("-")[3]+"-"+date.split("-")[4]+"-"+date.split("-")[5]; 
		if("1".equals(audit)){
			//tong guo 
			//得到最后执行计算比例时间
			Date startDate = getRunTime();
			Calendar calendar = Calendar.getInstance();
			Calendar startcal=Calendar.getInstance();
			startcal.setTime(startDate);
			int res=calendar.compareTo(startcal);
			if(res<0){
//				//修改有效值
 				Proportionality proportionality=new Proportionality();
 				//proportionality.setKjInstId(kjinstId);
 				proportionality.setInstId(instId);
				proportionality.setYearMonth(yearMoth);
				proportionality.setAvailable("1");
				proportionality.setAuditor_proposer_id(this.getCurrentUser().getOrgId());
				System.out.println("符合人用户名编码"+this.getCurrentUser().getOrgId()+"   复合人名称"+this.getCurrentUser().getUsername());
				proportionality.setAuditor_proposer_name(this.getCurrentUser().getUsername());
  				service.updateProportionality(proportionality,"Y");
  				proportionality.setOperateDate(operateDate);
  				proportionality.setAvailable("2");
  				service.updateProportionality(proportionality,"N");
  				//回传费控
// 本地操作测试不回传费控---服务回传费控（放开）
  			/*	List<String> list=service.findWebServiceUrl("feikong");
  				CemClient client=new CemClient(list.get(0));
  				Gson gson=new GsonBuilder().serializeNulls().create();
  				Map<String, String> param=new HashMap<String, String>();
  				param.put("yearMonth", yearMoth);
  				param.put("instId", instId);
  				List<TaxTransferInfo> outRateInfo=service.findRollOutRatio(param);
  				String json="{\"outRateInfo\":"+gson.toJson(outRateInfo)+"}";
   				String cemOut=client.receiver("wsVatServer", "turnOutRateHandle", json);
   				log.info(cemOut);
  				response.setCharacterEncoding("UTF-8");
  				try {
  					response.getWriter().print("Y");
  				} catch (IOException e) {
  					e.printStackTrace();
  				}*/
  				
  				//2018-07-20新增，更改与费控对接比例表中的转出比例
  				Map<String,String> map = new HashMap();
  				String result = date.split("-")[2];
  				/*List<String> lis = service.findInstMapping(instId);
  				if(lis.size() > 0){
  					String fkInstId = lis.get(0);
  					map.put("fkInstId", fkInstId);
  				}*/
  				
  				//查询业务机构对应财务机构
				List<String> instList = service.findInstRelation(instId);
				String parentId = "";
				for(int i=0;i<instList.size();i++){
					//查询财务机构对应上级机构
					List<String> listParent = service.findInstLast(instList.get(i));
					if(instList.contains(listParent.get(0)) && listParent != null){
						continue;
					}
					parentId = instList.get(i);
				}
				map.put("parentId", parentId);
  				map.put("yearMoth", yearMoth);
  				map.put("result", result);
  				map.put("operateDate", operateDate);
  				service.updateTransferRatio(map);
  				
  				response.setCharacterEncoding("UTF-8");
  				try {
					response.getWriter().print("Y");
				} catch (IOException e) {
					e.printStackTrace();
				}
  				
			}else{
				response.setCharacterEncoding("UTF-8");
	 	    	try {
					response.getWriter().print("N");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
   /**
    * 转出比例调整审核  审核拒绝
    */
	public void AuditRolloutBack(){
		String date=request.getParameter("date");
		String instId=date.split("-")[0];
		//String kjinstId=service.findInstIdACCOUNTING(instId); 2018-07-30国富更改
		String audit=request.getParameter("audit");
		String yearMoth =date.split("-")[1];
		String cancelReason=date.split("-")[6];
		String operateDate=date.split("-")[3]+"-"+date.split("-")[4]+"-"+date.split("-")[5];
		Proportionality proportionality=new Proportionality();
		//proportionality.setKjInstId(kjinstId);
		proportionality.setInstId(instId);
		proportionality.setYearMonth(yearMoth);
		proportionality.setAvailable("2");
		proportionality.setReason(cancelReason);
		proportionality.setOperateDate(operateDate);
		//***** 0615新增
		proportionality.setAuditor_proposer_id(this.getCurrentUser().getOrgId());
		proportionality.setAuditor_proposer_name(this.getCurrentUser().getUsername());
		service.updateProportionality(proportionality,"S");
		response.setCharacterEncoding("UTF-8");
	   	try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PullDataService getService() {
		return service;
	}
	public void setService(PullDataService service) {
		this.service = service;
	}
	@SuppressWarnings("rawtypes")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session) {
		this.session = session;
	}
}
