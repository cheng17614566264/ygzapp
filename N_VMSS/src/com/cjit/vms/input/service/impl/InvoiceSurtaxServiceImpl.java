package com.cjit.vms.input.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.SubjectDic;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.InvoiceSurtaxService;
import com.sun.corba.se.spi.orbutil.fsm.Input;

public class InvoiceSurtaxServiceImpl extends GenericServiceImpl  implements InvoiceSurtaxService {

	@SuppressWarnings("unchecked")
	public List<BillDetailEntity> findInvoiceInSurtaxList(InputInfo info,
			PaginationList paginationList) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		
//		parameters.put("inputInfo", info);
		
		System.out.println(info.getLstAuthInstId().toString()+",list..............");
		System.out.println(info.getBillStatu()+",statu............");//null
		System.out.println(info.getBillDate()+",date.............");//null
		System.out.println(info.getName()+",name...............");//null
		System.out.println(info.getBillCode()+",code............");//null
		System.out.println(info.getBillId()+",id................");//null
		System.out.println(info.getBillType()+",type..............");//null
		System.out.println(info.getBillInst()+",inst.................");//null
		System.out.println(info.getDealNoStarDate()+",stardate.............");//null
		System.out.println(info.getDealNoEndDate()+",enddate.............");//null
		System.out.println(info.getRollOutStatus()+",outstatus.................");//0
		
 		@SuppressWarnings("unchecked")
 		List<Organization> instIds=info.getLstAuthInstId();
 		List<String> lstTmp=new ArrayList<String>();
 		for(int i=0;i<instIds.size();i++){
 			Organization org=(Organization)instIds.get(i);
 			lstTmp.add(org.getId());
 		}
 		parameters.put("auth_inst_ids", lstTmp); 
 		List<String> list=new ArrayList<String>();
 		if ("2".equals(info.getBillStatu()+"")) {
 			list.add("2");//已转出
 		}else {
 			list.add("0");
 			list.add("1");
 		}
 		parameters.put("billStatu", list);
 		if(null!=info.getBillDate()){
 			parameters.put("bill_date", info.getBillDate());
 		}
 		parameters.put("vendor_name", info.getName());
 		parameters.put("bill_code", info.getBillCode());
 		parameters.put("bill_id", info.getBillId());
 		parameters.put("fapiao_type", info.getBillType());
 		parameters.put("inst_id", info.getBillInst());
 		parameters.put("tbillDate", info.getDealNoStarDate());
 		parameters.put("ebillDate", info.getDealNoEndDate());
 		parameters.put("rollOutStatus", info.getRollOutStatus());
 		
 		if(info.getBillInst()!=null&&info.getBillInst().length()>0){
 			parameters.put("billInst", info.getBillInst().split(" ")[0]);
 		}
		if(null==paginationList){ //paginationList不为空
			return this.find("getInputInfoDetailList", parameters);
		}
 		return this.find("getInputInfoDetailList", parameters, paginationList);
	}

	public InputInvoiceInfo findVmsInputInvoiceInfoByBillId(String bill_id) {
		HashMap params=new HashMap();
		params.put("bill_id", bill_id);
		return (InputInvoiceInfo) this.findForObject("findVmsInputInvoiceInfoByBillId", params);
	}

	public List findVmsInputInvoiceItemsByBillId(String bill_id) {
		HashMap params=new HashMap();
		params.put("bill_id", bill_id);
		return  this.find("findVmsInputInvoiceItemsByBillId", params);
	}

	public void updateVmsInputInvoiceInfoVatOut(String vat_out_amt,
			String vat_out_proportion, String remark,String bill_id) {
		HashMap params=new HashMap();
		params.put("vat_out_amt", vat_out_amt);
		params.put("vat_out_proportion", vat_out_proportion);
		params.put("remark", remark);
		params.put("bill_id", bill_id);
		this.update("updateVmsInputInvoiceInfoVatOut", params);
	}

	public void updateVmsInputInvoiceInfoDatastatus(String[] bill_id,
			String datastatus) {
		if(null==bill_id){
			return;
		}
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<bill_id.length;i++){
			String item_bill_id=bill_id[i];
			Map<String,String> params=new HashMap<String,String>();
			params.put("datastatus", datastatus);
			params.put("bill_id", item_bill_id);
			list.add(params);
		}
		this.updateBatch("updateVmsInputInvoiceInfoDatastatus", list);
	}
	
	public void updateVmsInputInvoiceInfoForRollBack(String[] billIds) {
		if(null==billIds){
			return;
		}
		for(int i=0;i<billIds.length;i++){
			String item_bill_id=billIds[i];
			Map params=new HashMap();
			params.put("bill_id", item_bill_id);
			this.update("updateVmsInputInvoiceInfoForRollBack", params);
		}
	}
	public void updateVmsInputInvoiceInfoForBatchRollOut(List<String> list) {
		this.updateBatch("updateVmsInputInvoiceInfoForBatchRollOut", list);
	}
	
	/**
	 * 进项转出
	 */
	public void transInputInfo(List<String> idList) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("idList", idList);
		this.update("transInputInfo", map);
	}

	@Override
	public void updateVmsInputInvoiceInfoDatastatus(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVmsInputInvoiceInfoForRollBack(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proportionality> findProportionality(Proportionality proportionality, String val,PaginationList paginationList) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instid", proportionality.getInstId());
		map.put("available", proportionality.getAvailable());
		if(null==proportionality.getYearMonth()&&"Y".equals(val)){
			Calendar calendar=Calendar.getInstance();
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			map.put("month", new SimpleDateFormat("yyyyMM").format(calendar.getTime()));
		}else{
			map.put("month", proportionality.getYearMonth());
		}
		if("Y".equals(val)){
			map.put("Y", "Y");
		}else{
			map.put("N", "N");
		}
		if("1".equals(proportionality.getDatasource())){
			map.put("datasource", "1");
		}
		map.put("operateStartDate", proportionality.getOperateStartDate());
		map.put("operateEndDate", proportionality.getOperateEndDate());
		List<Proportionality> list=null;
		if(paginationList!=null){
			list=find("findProportionality", map,paginationList);
		}else{
			list=find("findProportionality", map);
		}
		return list;
	}

	@Override
	public BillDetailEntity findInvoiceInSurtaxList(InputInfo info) {
		Map<String, String> map =new HashMap<String, String>();
		map.put("bill_id", info.getBillId());
		map.put("bill_code", info.getBillCode());
		List<BillDetailEntity> list=find("findBillDetailEntity", map);
		return list.get(0);
	}

	@Override
	public List<String> getReportInst(String inst) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", inst);
		return this.find("getReportInst", map);
	}

	@Override
	public List<SubjectEntity> findSubjectEntityList(SubjectEntity sEntity) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("DIRECTION_NAME", sEntity.getDirectionName());
		map.put("TAX_RATE_CODE", sEntity.getTaxRateCode());
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,1);
 		map.put("YEAR_MONTH", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).substring(0, 7));
		map.put("INST_ID", sEntity.getInstId());
 		return this.find("findSubjectEntityList", map);
	}

	@Override
	public List<SubjectEntity> findSubjectEntityCreditDescSum(SubjectEntity sEntity) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("DIRECTION_NAME", sEntity.getDirectionName());
		map.put("isTaxexemption", sEntity.getIsTaxexemption());
		//map.put("TAX_RATE_CODE", sEntity.getTaxRateCode());
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,1);
 		map.put("YEAR_MONTH", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).substring(0, 7));
		map.put("INST_ID", sEntity.getInstId());
		return this.find("findSubjectEntitySUM", map);
	}

	@Override
	public List<String> findCodeDictionary(String string ,String codeName) {
		Map<String, String> idMap =new HashMap<String, String>();
		idMap.put("CODE_TYPE", string);
		idMap.put("CODE_NAME", codeName);
		return find("findCodeDictionary", idMap);
	}

	@Override
	public InputInfo getrollOutAmtSum() {
		Map<String, String> map =new HashMap<String, String>();
		Calendar calendar =Calendar.getInstance();
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		map.put("Moth", new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
		List<InputInfo> list=find("getrollOutAmtSum", map);
		return list.get(0);
	}

	@Override
	public void saveRolloutAudit(Proportionality proportionality) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("operateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("dividend", proportionality.getDividend());
		map.put("divisor", proportionality.getDivisor());
		map.put("result", proportionality.getResult());
		map.put("instId", proportionality.getInstId());
		map.put("orig_instId", proportionality.getOrigInstId());
		map.put("dataSource", "1");
		map.put("available", "2");
		map.put("yearMonth", proportionality.getYearMonth());
		map.put("apply_id", proportionality.getApply_proposer_id());
		map.put("apply_name", proportionality.getApply_proposer_name());
		this.save("saveProportionaLity", map);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findReportInst(String instId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("instId", instId);
		return this.find("getReportInst", map);
	}

	@Override
	public List<Proportionality> findProportionality(Proportionality proportionality) {
		Map<String, String> map= new HashMap<String, String>();
		map.put("instId", proportionality.getInstId());
		map.put("datasource", proportionality.getDatasource());
		Calendar calendar=Calendar.getInstance();
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		calendar.add(Calendar.MONTH, -1); //2018-07-31更改
		map.put("yearMonth", new SimpleDateFormat("yyyyMM").format(calendar.getTime()));
		return find("getRolloutByInstId", map);
	}

	@Override
	public List<Proportionality> findProportionalityAudit(Proportionality proportionality) {
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("operateStartDate", proportionality.getOperateStartDate());
		map.put("operateEndDate", proportionality.getOperateEndDate());
		map.put("instId", proportionality.getInstId());
		map.put("datasource", proportionality.getDatasource());
		map.put("available", proportionality.getAvailable());
		return find("findProportionalityAudit", map);
	}

	@Override
	public List<Proportionality> findProportionalityForRolliut(Proportionality proportionality) {
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("operateStartDate", proportionality.getOperateStartDate());
		map.put("operateEndDate", proportionality.getOperateEndDate());
		map.put("instId", proportionality.getInstId());
		map.put("datasource", proportionality.getDatasource());
		if("A".equals(proportionality.getAvailable())){
			map.put("Y", "Y");
		}else{
			map.put("available", proportionality.getAvailable());
		}
		map.put("month", proportionality.getYearMonth());
		return find("findProportionalityForRolliut", map);
	}
	
	public List<TimeTaskEntity> findTimeTisk(String dataMold) {
		Map<String, String> map =new HashMap<String, String>() ;
		map.put("dataMold", dataMold);
		return this.find("findTimeTask", map);
	}

	@Override
	public void insertSubjectDictionary(Map map) {
		save("insertSubjectDictionary",map);
	}
	/**
	 * 修改
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 */
	@Override
	public List<SubjectEntity> getSubjectLedgerMoney(Map map) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,1);
		map.put("YEAR_MONTH", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).substring(0, 7));
		return this.find("getSubjectLedgerMoney", map);
	}
	//end 2018-08-22
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
	/**
	 * 修改
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：将查询到的结果写入数据库表t_code_dictionary中
	 */
	@Override
	public void insertCodeDictionary(Map resultMap) {
		resultMap.put("codeType", "SUBJECT_NAME");  //在此字典表中的标识
		resultMap.put("status", "N");  //标识此数据的状态：N-未计算  Y-已计算
		this.save("insertCodeDictionary", resultMap);
	}
	//end 2018-08-22
	
	/**
	 * 修改
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：查询字典表中的信息
	 */
	@Override
	public List<CodeDictionary> selectCodeDictionary(String string) {
		Map map = new HashMap();
		map.put("status", string);
		return this.find("selectCodeDictionary", map);
	}
	//end 2018-08-22

	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 说明：从字典表中删除对应的信息
	 */
	@Override
	public void deleteCodeDictionary(Map map) {
		this.delete("deleteCodeDictionary", map);
	}

	@Override
	public List<CodeDictionary> selectCodeDictionaryAll() {
		Map map = new HashMap();
		return this.find("selectCodeDictionaryAll",map);
	}
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 说明：科目字典查看
	 */
	@Override
	public List<SubjectDic> selectSubjectDicAll() {
		Map map = new HashMap();
		return this.find("selectSubjectDicAll", map);
	}
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 说明：科目字典修改，设置该科目是否启用
	 */
	@Override
	public void updateSubjectDic(Map map) {
		this.update("updateSubjectDic", map);
	}
	
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 说明：机构转换
	 */
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
	//end 20180827


}
