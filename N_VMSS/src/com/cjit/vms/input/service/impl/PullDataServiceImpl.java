package com.cjit.vms.input.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.vms.input.jdbcLink.JdbcGetGeneralIedger;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.PullDataService;
import com.cjit.webService.client.entity.TaxTransferInfo;

public class PullDataServiceImpl extends GenericServiceImpl implements PullDataService{
	JdbcGetGeneralIedger jdbcGetGeneralIedger = new JdbcGetGeneralIedger();
	public JdbcGetGeneralIedger getJdbcGetGeneralIedger() {
		return jdbcGetGeneralIedger;
	}

	public void setJdbcGetGeneralIedger(JdbcGetGeneralIedger jdbcGetGeneralIedger) {
		this.jdbcGetGeneralIedger = jdbcGetGeneralIedger;
	}

	@Override
	/*public  void getGeneralIedger() {
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		int month=calendar.get(Calendar.MONTH)+1;
		String mon=month+"";
		if (mon.length()<2) {
			mon="0"+mon;
		}
		Map<String, String> map=new HashMap<String, String>();
		map.put("month", year+"-"+mon);
		this.delete("deleteGeneralLedger", map);
 		this.save("insertGeneralLedger", map);
	}*/
	
	
	/*public  void getGeneralIedger() {*/
	public String getGeneralIedger() {
		System.out.println("进入执行程序...");
		List list;
		try {
			System.out.println("进入try...");
			System.out.println(jdbcGetGeneralIedger);
			list = jdbcGetGeneralIedger.jdbcGetGeneralIedger();
			System.out.println("获取到数据...");
			Calendar calendar = Calendar.getInstance();
			//int year=calendar.get(Calendar.YEAR);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			/*int month=calendar.get(Calendar.MONTH)+1;
			System.out.println(month);
			String mon=month+"";
			System.out.println(mon);
			if (mon.length()<2) {
				mon="0"+mon;
			}*/
			//获取上月日期
			String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
			System.out.println(list.size());
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i)+":lllllllll");
				if(list.get(i) != null){
					Map map=(Map) list.get(i);
					
					//map.put("month", year+"-"+mon);
					map.put("month", yearMonth);
					System.out.println(map);
					
					this.delete("deleteGeneralLedger", map);
			 		this.save("insertGeneralLedger", map);
				}
			}
			return SUCCESS;
		} catch (ClassNotFoundException e) {
			System.out.println("PullDataServiceImpl类出错啦");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("PullDataServiceImpl类出错啦");
		}
		return ERROR;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TimeTaskEntity> findTimeTisk(String dataMold) {
		Map<String, String> map =new HashMap<String, String>() ;
		map.put("dataMold", dataMold);
		return this.find("findTimeTask", map);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findAccBanlanceInfo(Map<String, String> param) {
		//国富新增 2018-02-26
		String date = param.get("yearAndMonth");
		String mon = date.split("-")[1];
		if(mon.equals("12")){
			param.put("yearAndMonth1", date.split("-")[0]+"-13");
			param.put("yearAndMonth2", date.split("-")[0]+"-14");
			param.put("yearAndMonth3", date.split("-")[0]+"-15");
		}
		return this.find("getBalanceInfoRate", param);
	}
	
	@Override
	public void insertProportionality(List<Map<String, String>> proportionalList) {
		this.insertBatch("saveProportionaLity", proportionalList);
	}

	@Override
	public void delDistinctData(Map<String, String> param) {
		this.update("delDistinctData", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findWebServiceUrl(String serviceName) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", serviceName);
		return this.find("findWebserviceUrl", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findCloseTimeValue(String name) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", name);
		return this.find("getCloseTimeValue", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaxTransferInfo> findRollOutRatio(Map<String, String> param) {
		return this.find("getRollOutRatio", param);
	}

	@Override
	public void updaterollOutAmt(Map<String, String> param) {
		this.update("updaterollout", param);
	}

	@Override
	public void updateProportionality(Proportionality proportionality ,String str) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("result", proportionality.getResult());
		map.put("instId", proportionality.getInstId());
		map.put("kjInstId", proportionality.getKjInstId());
		map.put("yearMonth", proportionality.getYearMonth());
		map.put("operateDate", proportionality.getOperateDate());
		map.put("available", proportionality.getAvailable());
		map.put("auditor_proposer_id", proportionality.getAuditor_proposer_id());
		map.put("auditor_proposer_name", proportionality.getAuditor_proposer_name());
		if ("Y".equals(str)) {
			map.put("Y", "Y");
		}else if("N".equals(str)){
			map.put("N", "N");
		}else{
			map.put("reason", proportionality.getReason());
			map.put("S", "S");
		}
		update("updateProportionality", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findReportInst(String instId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", instId);
		return this.find("getReportInst", map);
	}

	@Override
	public String findInstIdACCOUNTING(String instId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", instId);
		List<String> list= find("findInstIdACCOUNTING", map);
		return list.get(0);
	}

	@Override
	public List<String> findtimeproportioninstId() {
		return find("findtimeproportioninstId", null);
	}

	@Override
	public void insertTransferRatio(List<Map<String, String>> transferRatio) {
		this.insertBatch("saveTransferRatio", transferRatio);
	}

	@Override
	public List<String> findInstMapping(String instId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", instId);
		return this.find("findInstMapping", map);
	}

	@Override
	public void updateTransferRatio(Map<String, String> map) {
		this.update("updateTransferRatio", map);
	}

	@Override
	public List<String> findInstRelation(String instId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", instId);
		return this.find("findInstRelation", map);
	}

	@Override
	public List<String> findInstLast(String costcenter) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("costcenter", costcenter);
		return this.find("findInstLast", map);
	}

	@Override
	public List<SubjectEntity> getSubjectLedgerOfTaxfree(Map subjectEntityMap) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,1);
		subjectEntityMap.put("YEAR_MONTH", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).substring(0, 7));
		return this.find("getSubjectLedgerOfTaxfree", subjectEntityMap);
	}

	@Override
	public List<SubjectEntity> getSubjectLedgerAll(Map subjectEntityMap) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,1);
		subjectEntityMap.put("YEAR_MONTH", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).substring(0, 7));
		return this.find("getSubjectLedgerAll", subjectEntityMap);
	}

	@Override
	public void insertProportionality(Map resultMap) {
		this.save("saveProportionaLity", resultMap);
	}
	/**
	 * 新增
	 * 日期：2018-08-23
	 * 作者：刘俊杰
	 * 查询总的免税信息
	 */
	@Override
	public List<CodeDictionary> selectCodeDictionaryAll() {
		Map map = new HashMap();
		return this.find("selectCodeDictionaryAll", map);
	}
	//比例计算完成之后更改字典表中免税信息的状态
	@Override
	public void updateCodeDictionarySataus() {
		Map map = new HashMap();
		this.update("updateCodeDictionarySataus", map);
	}
	
}
