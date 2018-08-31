package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.webService.client.entity.TaxTransferInfo;

public interface PullDataService {
	/**
	 * 同步总账数据
	 */
	/*public void getGeneralIedger();*/
	public String getGeneralIedger();
	/**
	 * 查找定时任务
	 */
	public List<TimeTaskEntity> findTimeTisk(String dataMold);
	/**
	 * 从总账查询科目
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findAccBanlanceInfo(Map<String, String> param);
	/**
	 * 存储比例计算结果
	 * @param proportionalList
	 */
	public void insertProportionality(List<Map<String, String>> proportionalList);
	/**
	 * 删除比例计算结果中的重复数据
	 * @param param
	 */
	public void delDistinctData(Map<String, String> param);
	/**
	 * 查找webservice的url
	 * @param string
	 * @return
	 */
	public List<String> findWebServiceUrl(String serviceName) ;
	/**
	 * 查询比例计算的关账时间
	 * @param string
	 * @return
	 */
	public List<String> findCloseTimeValue(String name);
	/**
	 * 查找上个月各机构的转出比例
	 * @param param
	 * @return
	 */
	public List<TaxTransferInfo> findRollOutRatio(Map<String, String> param);
	
	/**
	 * 手工添加转出比例
	 * @param param
	 */
	public void updaterollOutAmt(Map<String, String> param);
	
	/**
	 * 修改转出比例
	 * @param proportionality
	 */
	public void updateProportionality(Proportionality proportionality,String str);
	
	/**
	 * 查询当前纳税申报的机构都需要汇总哪些机构
	 * @param string
	 * @return
	 */
	public List<String> findReportInst(String string);
	
	public String findInstIdACCOUNTING(String instId);
	
	/**
	 * 
	 */
	public List<String> findtimeproportioninstId();
	
	/**
	 * 将转出比例插入费控中间表
	 */
	public void insertTransferRatio(List<Map<String, String>> transferRatio);
	
	/**
	 * 增值税机构与费控机构对应
	 */
	public List<String> findInstMapping(String inst);
	
	/**
	 * 修改费控对接表中的转出比例
	 */
	public void updateTransferRatio(Map<String, String>  map);
	
	/**
	 * 查询业务机构对应财务机构
	 */
	public List<String> findInstRelation(String instId);
	
	/**
	 * 查询财务机构对应上级机构
	 */
	public List<String> findInstLast(String instId);
	//获取所有的免税信息(上月)
	public List<SubjectEntity> getSubjectLedgerOfTaxfree(Map subjectEntityMap);
	//获取所有的免税+应税信息(上月)
	public List<SubjectEntity> getSubjectLedgerAll(Map subjectEntityMap);
	//将比例计算的结果写入中间表中
	public void insertProportionality(Map resultMap);
	/**
	 * 新增
	 * 日期：2018-08-23
	 * 作者：刘俊杰
	 * 查询未计算的总的免税信息
	 */
	public List<CodeDictionary> selectCodeDictionaryAll();
	//计算完成之后更改字典表免税信息中的状态
	public void updateCodeDictionarySataus();
}