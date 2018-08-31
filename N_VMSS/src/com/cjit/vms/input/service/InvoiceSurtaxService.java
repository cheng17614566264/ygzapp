package com.cjit.vms.input.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.SubjectDic;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;

public interface InvoiceSurtaxService {
	
	public void transInputInfo(List<String> idList);
	
	public List<BillDetailEntity> findInvoiceInSurtaxList(InputInfo info,
			PaginationList paginationList);

	public InputInvoiceInfo findVmsInputInvoiceInfoByBillId(String bill_id);

	public List findVmsInputInvoiceItemsByBillId(String bill_id);
	
	/**
	 * 更新进项发票转出
	 * @param vat_out_amt
	 * @param vat_out_proportion
	 */
	public void updateVmsInputInvoiceInfoVatOut(String vat_out_amt,String vat_out_proportion,String remark,String bill_id);
	
	/**
	 * 转出提交
	 * @param bill_id
	 * @param datastatus
	 */
	public void updateVmsInputInvoiceInfoDatastatus(List<String> list);
	
	/**
	 * 撤回数据
	 * @param billIds
	 */
	public void updateVmsInputInvoiceInfoForRollBack(List<String> list) ;
	/**
	 * 批量转出
	 * @param billIds
	 */
	public void updateVmsInputInvoiceInfoForBatchRollOut(List<String> list);
	
	/**
	 * 根据机构获取转出比例
	 * @return
	 */
	public List<Proportionality> findProportionality(Proportionality proportionality,String val ,PaginationList paginationList);
	public BillDetailEntity findInvoiceInSurtaxList(InputInfo info);
	public List<String> getReportInst(String inst);
	public List<TimeTaskEntity> findTimeTisk(String dataMold);
	public List<Proportionality> findProportionality(Proportionality proportionality);
	
	/**
	 * 转出审核详情
	 * @param proportionality
	 * @return
	 */
	public List<Proportionality> findProportionalityAudit(Proportionality proportionality);
	
	//转出比例值查看
	public List<Proportionality> findProportionalityForRolliut(Proportionality proportionality);
	
	
	/**
	 * 获取科目信息
	 * @param 
	 * @return
	 */
	public List<SubjectEntity> findSubjectEntityList(SubjectEntity sEntity);
	public List<SubjectEntity> findSubjectEntityCreditDescSum(SubjectEntity sEntity);
	public List<String> findCodeDictionary(String string,String codeName);
	
	public InputInfo getrollOutAmtSum();
	
	public void saveRolloutAudit(Proportionality proportionality);
	public List<String> findReportInst(String instId);
	//录入科目信息
	public void insertSubjectDictionary(Map map);
	//根据模糊查询获取此科目在总账中的余额
	public List<SubjectEntity> getSubjectLedgerMoney(Map map);
	//获取所有的免税信息(上月)
	public List<SubjectEntity> getSubjectLedgerOfTaxfree(Map subjectEntityMap);
	//获取所有的免税+应税信息(上月)
	public List<SubjectEntity> getSubjectLedgerAll(Map subjectEntityMap);
	/**
	 * 修改
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：将查询到的结果写入数据库表t_code_dictionary中
	 */
	public void insertCodeDictionary(Map resultMap);
	//end 2018-08-22
	/**
	 * 修改
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：查询字典表中的信息
	 */
	public List<CodeDictionary> selectCodeDictionary(String string);
	public List<CodeDictionary> selectCodeDictionaryAll();
	//end 2018-08-22
	
	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：从字典表中删除对应的信息
	 */
	public void deleteCodeDictionary(Map map);
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 说明：科目字典查询
	 */
	public List<SubjectDic> selectSubjectDicAll();
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 说明：科目字典修改，设置改科目是否启用
	 */
	public void updateSubjectDic(Map map);

	public List<String> findInstRelation(String orgId);

	public List<String> findInstLast(String string);

	
}
