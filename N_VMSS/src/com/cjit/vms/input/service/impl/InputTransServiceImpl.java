package com.cjit.vms.input.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.service.InputTransService;

import edu.emory.mathcs.backport.java.util.Arrays;

public class InputTransServiceImpl extends GenericServiceImpl implements
		InputTransService {

	public List findBussTypeList() {
		Map map = new HashMap();
		List list = find("bussTypeList", map);
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<InputInfo> inputTransList(InputInfo inputTrans,
			PaginationList paginationList) {
		Map<String,Object> map = new HashMap<String,Object>();
		List instIds=inputTrans.getLstAuthInstId();
		List<String> lstTmp=new ArrayList<String>();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("inputInfo", inputTrans);
		return find("getInputInfoList", map,paginationList);
	}
	public InputTrans findInputTransByIdCodeNo(String id, String code,
			String no){
		Map map = new HashMap();
		map.put("DEAL_NO", id);
		map.put("BILL_CODE", code);
		map.put("BILL_NO", no);
		List list = find("findInputTransByIdCodeNo", map);
		if (list != null && list.size() > 0){
			InputTrans inputTrans = (InputTrans) list.get(0);
			return inputTrans;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public InputInfo findInputTransById(Map<String, String> map) {
		List<InputInfo> list=this.find("getInputInfo", map);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	public void saveInputTrans(InputInfo inputTrans) {
		Map map = new HashMap();
		if(inputTrans.getBillCode().isEmpty()){
			inputTrans.setBillCode(null);
		}
		if(inputTrans.getBillCode().isEmpty()){
			inputTrans.setBillCode(null);
		}
		map.put("inputTrans", inputTrans);
		save("saveInputTrans", map);
	}

	public void importInputInvoice(List dataList) {
		for (int i = 0; i < dataList.size(); i++) {
			Map map = (Map) dataList.get(i);
			this.save("addInputTrans", map);
			this.save("addTransInvoice", map);
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteInputTrans(String ids) {
		List<String> idList=Arrays.asList(ids.split(","));
		Map<String,List<String>> paraMap = new HashMap<String,List<String>>();
		paraMap.put("ids", idList);
		this.delete("deleteInputVoiceNull",paraMap);
		this.delete("deleteSelectedTrans", paraMap);
	}

	public List inputTransListExport(InputTrans inputTrans,
			PaginationList paginationList, String sql) {
		Map map = new HashMap();
		map.put("inputTrans", inputTrans);
		map.put("sql", sql);
		List instIds=inputTrans.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp); 
		return find("inputTransListExport", map, paginationList);
	}

	@Override
	public List selectInvoiceInfo(Map parameters) {
		// TODO Auto-generated method stub
		return find("selectInvoiceInfo", parameters);
	}

	@Override
	public List<String> findInputTransDataByDealNoAndBatchNo(Map<String,String> map) {
		return find("findInputTransDataCompareInputTransInfo", map);
	}
	


	@Override
	public void saveInputTransData(List<Map<String, String>> sheet1List,List<Map<String, String>> sheet2List,
			List<Map<String, String>> sheet1ReList,List<Map<String, String>> sheet2ReList) {
		this.insertBatch("saveInputTransData", sheet1List);
		this.insertBatch("saveInputTransDataItem", sheet2List);
		this.insertBatch("insertErrorInput", sheet1List);
		this.insertBatch("insertErrorInputItem", sheet2List);
	}
	public void saveInputTransDataItem(List<Map<String, String>> list) {

		this.insertBatch("saveInputTransDataItem", list);
	}

	@Override
	public void saveInputTransInfo(Map<String, String> map) {
		this.save("saveInputTransInfo", map);

	}

	@Override
	public List<String> findInputTransDataComparebillNo(Map<String, String> map) {
		// TODO Auto-generated method stub
		return find("findInputTransDataComparebillNo", map);
	}

	@Override
	public void saveInputTransCopyData(Map<String, String> map) {
		this.save("saveInputTransCopyData", map);
	}

	@Override
	public List<InputTrans> findbillcodeInInputInvoice(Map<String, String> map) {
		
		return find("findbillcodeInInputInvoice",map);
	}

	@Override
	public void saveTransInvoice(Map<String, String> map) {
		
		this.save("saveInputTransInvoice", map);
	}
	
	// 20160512 中科软添加
	// 获取行业类型
	public List findIndustryTypeList()
	{
		Map map = new HashMap();
		List list = find("industryTypeList", map);
		return list;
	}

	public List inputInvoiceNewList(InputInfo inputInvoiceNew, PaginationList paginationList)
	{
		Map map = new HashMap();
		List lstTmp = new ArrayList();
//		map.put("deducBeginDate", inputInvoiceNew.getDeducBeginDate());
//		map.put("deducEndDate", inputInvoiceNew.getDeducEndDate());
		map.put("billCode", inputInvoiceNew.getBillCode());
//		map.put("billNo", inputInvoiceNew.getBillNo());
//		map.put("instId", inputInvoiceNew.getInstId());
		return find("inputInvoiceNewList", map, paginationList);
	}
	
	public List<String> findInputInvoiceNewDataByDealNoAndDealCode(Map<String,String> map){
		return find("findInputInvoiceNewDataByDealNoAndDealCode", map);
	}
	
	public List<String> findInputInstByInstCode(Map<String,String> map){
		return find("findInputInstByInstCode",map);
	}
	
	public void saveInputInvoiceNewData(Map<String, String> map){
		this.save("saveInputInvoiceNewData", map);
		String date = "2016-05-01";
		System.out.println(map.get("billDate"));
		System.out.println(map.get("billDate").compareTo(date) < 0);
		if(map.get("billDate").compareTo(date) < 0){
			
		}else{
			this.save("saveInputInvoiceNewTempData", map);
		}
	}
	
	public List<String> findInvoiceNewByCodeNo(String billCode,String billNo) {
		Map map=new HashMap();
		
		InputInvoiceNew tInputInvoiceNew=new InputInvoiceNew();
		tInputInvoiceNew.setBillCode(billCode);
		tInputInvoiceNew.setBillNo(billNo);
		map.put("InputInvoiceNew", tInputInvoiceNew);
		List list= find("findInvoiceNewByCodeNo", map);
//		tInputInvoiceNew=new InputInvoiceNew();
//		
//		if(list!=null &&list.size()>0){
//			tInputInvoiceNew=(InputInvoiceNew)list.get(0);
//		}
		return list;
	}
	
	public void saveInvoiceNew(InputInvoiceNew inputInvoiceNew){
		Map map=new HashMap();
		map.put("invoiceNew", inputInvoiceNew);
		this.save("saveInputInvoiceNewD", map);
		//this.save("saveinputInvoiceNewTemp", map);
		
		
	}
	
	//删除进项发票信息  add by 李松
	public void deleteInputTransNew(Map findby) {

		this.delete("deleteSelectedTransNew", findby);
	}
	
	@Override
	public InputInfo findInputTransByBillNoAndCode(String billNoAndCode) {
		HashMap map = new HashMap<String,String>();
		map.put("billNoAndCode", billNoAndCode);
		List list = find("findInputTransByBillNoAndCode", map);
		if (list != null && list.size() > 0){
			InputInfo inputInvoiceNew = (InputInfo) list.get(0);
			return inputInvoiceNew;
		}
		return null;
	}

	@Override
	public void saveInputTransNew(InputInvoiceNew inputInvoiceNew) {
		Map map = new HashMap();
		
		map.put("invoiceNew", inputInvoiceNew);
		save("saveInputTransNew", map);
	}
	
	public List<String> findDup(Map<String,String> map){
		return find("findDup",map);
	}

	@Override
	public void saveInputTransEdit(InputInvoiceNew inputInvoiceNew,String userId) {
        Map map = new HashMap();
		
		map.put("inputTrans", inputInvoiceNew);
		map.put("userId", userId);
		save("saveInputTransEdit", map); 
		
	}

	@Override
	public List<String> findInputTransEditById(String billNoAndCode,PaginationList paginationList) {
		HashMap map = new HashMap<String,String>();
		map.put("billNoAndCode", billNoAndCode);
			return find("findInputTransEditByBillNoAndCode", map, paginationList);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findBillReNo(List<String> billInfoList) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("billInfoList", billInfoList);
		return find("findRebillCode", map);
	}

}
