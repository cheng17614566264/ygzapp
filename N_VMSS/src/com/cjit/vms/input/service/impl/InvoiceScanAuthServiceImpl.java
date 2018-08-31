package com.cjit.vms.input.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.model.VendorInfo;
import com.cjit.vms.input.service.InvoiceScanAuthService;
import com.cjit.vms.trans.model.UBaseInst;

import edu.emory.mathcs.backport.java.util.Arrays;

public class InvoiceScanAuthServiceImpl extends GenericServiceImpl implements InvoiceScanAuthService {

	public String findSequenceBillItemId(){
		Map para = new HashMap();
		List list = this.find("getBillItemIdSequence", para);
		return (String) list.get(0);
	}

	public List findInvoiceScanAuthItemsByBillId(String billId) {
		HashMap params=new HashMap();
		params.put("bill_id", billId);
		return  this.find("findVmsInputInvoiceItemsByBillId", params);
	}

	public InputTransInfo findInvoiceScanAuthTransInfoByBillId(String bill_code,String bill_no) {
		Map params = new HashMap();
		params.put("bill_code", bill_code);
		params.put("bill_no", bill_no);
		return (InputTransInfo) this.findForObject("findInvoiceScanAuthTransInfoByBillId", params);
	}

	public void updateVmsInputInvoiceInfoForScanAuth(
			InputInvoiceInfo inputInvoiceInfo, String o_bill_id) {
//		if(lstGoodAdded!=null&&lstGoodAdded.size()>0){
//			for(int i=0;i<lstGoodAdded.size();i++){
//				InputInvoiceItem item=(InputInvoiceItem) lstGoodAdded.get(i);
//				Map addMap = new HashMap();
//				addMap.put("info", item);
//				this.save("saveVmsInputInvoiceItem", addMap);
//			}
//		}
		Map params = new HashMap();
		inputInvoiceInfo.setBillId(o_bill_id);
		params.put("info", inputInvoiceInfo); 
		this.update("updateVmsInputInvoiceInfoForScanAuth", params);
	}
	/**
	 * 新增商品信息
	 * @param item
	 */
	public void insertVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("insertVmsInputInvoiceItem", addMap);
	}
	/**
	 * 编辑更新商品信息 
	 * @param item 
	 */
	public void updateVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("saveVmsInputInvoiceItemInfo", addMap);
	}
	/**
	 * 删除商品信息
	 * @param item
	 */
	public void deleteVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("deleteVmsInputInvoiceItem", addMap);
	}
	
	
	public void saveVmsInputInvoiceInfoImport(List dataList){
		for(int i=0;i<dataList.size();i++){
			Map info = (Map) dataList.get(i);
			Map checkParams = new HashMap();
			checkParams.put("bill_code", info.get("billCode"));
			checkParams.put("bill_no", info.get("billNo"));
			InputInvoiceInfo oldObj=(InputInvoiceInfo) this.findForObject("findInvoiceScanAuthByBillCodeAndBillNo", checkParams);
			//导入数据整理
			if((null==info.get("conformFlg"))||(info.get("conformFlg").toString().isEmpty())){
				info.put("conformFlg","1");
			}
			if((null==info.get("balance"))||(info.get("balance").toString().isEmpty())){
				info.put("balance","0");
			}
			if((null==info.get("faPiaoType"))||(info.get("faPiaoType").toString().isEmpty())){
				info.put("faPiaoType","0");
			}
//			if((null==info.get("scanDate"))||(info.get("scanDate").toString().isEmpty())){
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//				info.put("scanDate",df.format(new Date()));
//			}
			InputInvoiceInfo inputInvoiceInfo = new InputInvoiceInfo();
			inputInvoiceInfo.setBillId((String)info.get("billId"));
			inputInvoiceInfo.setBillCode((String)info.get("billCode"));
			inputInvoiceInfo.setBillNo((String)info.get("billNo"));
			inputInvoiceInfo.setBillDate((String)info.get("billDate"));
			String taxNo = (String)info.get("vendorTaxNo");
			//String instCode = (String)info.get("instCode");
			inputInvoiceInfo.setTaxno(taxNo);
			Map instMap = new HashMap();
			/* 机构 跟纳税人识别号是一对一 已向大龙确认*/
			instMap.put("taxNo", taxNo);
			//instMap.put("instCode", instCode);
			
			UBaseInst inst = (UBaseInst)this.findForObject("findBaseInstByTaxNoAndInstCode", instMap); 
			if (null != inst) {
				if (StringUtils.isNotEmpty(inst.getTaxperName())){
					inputInvoiceInfo.setName(inst.getTaxperName());
				}
				if (StringUtils.isNotEmpty(inst.getTaxAddress()) || StringUtils.isNotEmpty(inst.getTaxTel())){
					inputInvoiceInfo.setAddressandphone(inst.getTaxAddress() + inst.getTaxTel());
				}
				if (StringUtils.isNotEmpty(inst.getTaxBank()) || StringUtils.isNotEmpty(inst.getAccount())){
					inputInvoiceInfo.setBankandaccount(inst.getTaxBank() + inst.getAccount());
				}
			}
			inputInvoiceInfo.setAmtSum(strToBigDecimal((String)info.get("amtSum")));
			inputInvoiceInfo.setTaxAmtSum(strToBigDecimal((String)info.get("taxAmtSum")));
			inputInvoiceInfo.setSumAmt(inputInvoiceInfo.getAmtSum().add(inputInvoiceInfo.getTaxAmtSum()));
			inputInvoiceInfo.setRemark((String)info.get("remark"));
			inputInvoiceInfo.setDrawer((String)info.get("drawer"));
			inputInvoiceInfo.setReviewer((String)info.get("reviewer"));
			inputInvoiceInfo.setPayee((String)info.get("payee"));
			String vendor_taxno=(String)info.get("vendorTaxNo");
			String vendorName="";
			String vendor_bankandaccount="";
			String vendor_addressandphone="";
			VendorInfo vendorInfo=findVendorInfo(vendor_taxno);
			if(null!=vendorInfo){
				if(StringUtils.isEmpty(vendorInfo.getVendorCName())){
					vendorName=vendorInfo.getVendorEName();
				}else{
					vendorName=vendorInfo.getVendorCName();
				}
				vendor_addressandphone=vendorInfo.getVendorAddress()+vendorInfo.getVendorPhone();
				if(StringUtils.isEmpty(vendorInfo.getVendorCBank())){
					vendor_bankandaccount=vendorInfo.getVendorEBank()+vendorInfo.getVendorAccount();
				}else{
					vendor_bankandaccount=vendorInfo.getVendorCBank()+vendorInfo.getVendorAccount();
				}
			}
			inputInvoiceInfo.setVendorName(vendorName);
			inputInvoiceInfo.setVendorTaxno(vendor_taxno);
			inputInvoiceInfo.setVendorAddressandphone(vendor_addressandphone);
			inputInvoiceInfo.setVendorBankandaccount(vendor_bankandaccount);
			inputInvoiceInfo.setInstcode((String)info.get("instCode"));
			inputInvoiceInfo.setNoticeNo((String)info.get("noticeNo"));
			inputInvoiceInfo.setDescription((String)info.get("description"));
			inputInvoiceInfo.setVatOutProportion((String)info.get("vatOutProportion"));
			inputInvoiceInfo.setVatOutAmt(strToBigDecimal((String)info.get("vatOutAmt")));
			inputInvoiceInfo.setConformFlg((String)info.get("conformFlg"));
			inputInvoiceInfo.setBalance((String)info.get("balance"));
			inputInvoiceInfo.setFapiaoType((String)info.get("faPiaoType"));
			inputInvoiceInfo.setScanOperator((String)info.get("scanOperator"));
			inputInvoiceInfo.setUrlBillImage((String)info.get("urlBillImage"));
			/*导入逻辑处理*/
			String curDatastatus=(String) info.get("dataStatus");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String today = df.format(new Date());
			if ("0".equals(curDatastatus)){
				inputInvoiceInfo.setScanDate(today);
			} else if ("1".equals(curDatastatus)){
				inputInvoiceInfo.setIdentifyDate(today);
			}
			if(null==oldObj){//发票代码和发票号码不存在时，做插入处理
			//	String addDatastatus=getInvoiceInfoDatastatus(curDatastatus,oldObj,"add");
				//if(StringUtils.isNotEmpty(addDatastatus)){
					
					inputInvoiceInfo.setDatastatus((String) info.get("dataStatus"));
					inputInvoiceInfo.setOperateStatus((String) info.get("dataStatus"));
					Map addInfoMap = new HashMap();
					addInfoMap.put("info", inputInvoiceInfo);
					this.save("saveVmsInputInvoiceInfoForImport", addInfoMap);
				//}
			}else{
				
				//String updateDatastatus=getInvoiceInfoDatastatus(curDatastatus,oldObj,"update");
				//if(StringUtils.isNotEmpty(updateDatastatus)&& !"do_nothing".equals(updateDatastatus)){
					//do update
					inputInvoiceInfo.setDatastatus((String) info.get("dataStatus"));
					inputInvoiceInfo.setOperateStatus(oldObj.getOperateStatus());
					Map uptInfoMap = new HashMap();
					uptInfoMap.put("info", inputInvoiceInfo);
					this.update("updateVmsInputInvoiceInfoForImport", uptInfoMap);
				//}
			}
		}
	}
	
	private VendorInfo findVendorInfo(String vendor_taxno){
		Map params = new HashMap();
		params.put("vendor_taxno", vendor_taxno);
		return (VendorInfo) this.findForObject("findVendorInfoByVendorTaxno", params);
	}
	
	private BigDecimal strToBigDecimal(String data){
			BigDecimal bd = null;
			if(data!=null&&!"".equals(data)){
				bd = new BigDecimal(data);
			}else{
				return null;
			}
			return bd;
	}
	private String getInvoiceInfoDatastatus(String curDatastatus,InputInvoiceInfo oldObj,String opFlg){
		if("add".equals(opFlg)){
			if("0".equals(curDatastatus)){
				return "1";
			}
			if("1".equals(curDatastatus)){
				return "3";
			}
			if("2".equals(curDatastatus)){
				return "4";
			}
			return null;
		}
		if("update".equals(opFlg)){
			if("1".equals(curDatastatus)){
				if("1".equals(oldObj.getDatastatus())){
					return "3";
				}
				if("4".equals(oldObj.getDatastatus())){
					return "5";
				}
				if("6".equals(oldObj.getDatastatus())){
					return "5";
				}
				return null;
			}
			if("2".equals(curDatastatus)){
				if("1".equals(oldObj.getDatastatus())){
					return "4";
				}
				if("4".equals(oldObj.getDatastatus())){
					return "6";
				}
				if("6".equals(oldObj.getDatastatus())){
					return "6";
				}
				return null;
			}
			if("0".equals(curDatastatus)){
				Map dsMap = new HashMap(){{
					put("3","3");
					put("5","5");
					put("7","7");
				}};
				if(dsMap.containsKey(oldObj.getDatastatus())){
					return "do_nothing";
				}else{
					return "1";
				}
			}
		}
		return null;
	}
	public InputInvoiceInfo findInvoiceScanAuthByBillCodeAndBillNo(
			String billCode, String billNo) {
		Map checkParams = new HashMap();
		checkParams.put("bill_code", billCode);
		checkParams.put("bill_no", billNo);
		InputInvoiceInfo oldObj=
			(InputInvoiceInfo) this.findForObject("findInvoiceScanAuthByBillCodeAndBillNo", checkParams);
		return oldObj;
	}
	public UBaseInst findUbaseInstByTaxNo(String taxNo) {
		Map map=new HashMap();
		map.put("taxNo", taxNo);
	List list=	find("findBaseInstByTaxNoAndInstCode", map);
	UBaseInst uInst=new UBaseInst();
	if(list.size()>0){
		uInst=(UBaseInst)list.get(0);
	}
		return uInst;
	}
	@Override
	public void saveInputinvoiceData(Map<String, String> map) {

		this.save("saveInputinvoiceData", map);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findinputInvoiceCompareinvoiceData(
			Map<String, String> map) {

		return find("findinputInvoiceCompareinvoiceData", map);
	}
	@Override
	public void saveInputinvoiceInfo(Map<String, String> map) {
		this.save("saveInputinvoiceInfo", map);
	}
	@Override
	public void updateInputInvoiceYconformFlg(Map<String, String> map) {
		this.save("updateInputInvoiceYconformFlg", map);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<InputInfo> findListInvoiceScanAuth(InputInfo info, PaginationList paginationList) {
		Map<String, Object> map=new HashMap<String, Object>();
		List instIds=info.getLstAuthInstId();
		List<String> lstTmp=new ArrayList<String>();
		String billInst=info.getBillInst(); //null
		if (StringUtils.isNotBlank(billInst)) {
			List<String> instList = Arrays.asList(billInst.split("，"));
			for (String str : instList) {
				if (StringUtils.isNotEmpty(str)) {
					lstTmp.add(str.split(" - ")[0]);
				}
				
			}
		}else {
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
		}
		
		map.put("auth_inst_ids", lstTmp);
		map.put("inputInfo", info);
		
		System.out.println(info.getBillId()+",id........."); //null
		System.out.println(info.getBillCode()+",code........."); //null
		System.out.println(info.getName()+",name........."); //null
		System.out.println(info.getTransBeginDate()+",begin........"); //2017-01-22
		System.out.println(info.getTransEndDate()+",end........."); //2018-01-22
		System.out.println(info.getBillStatu()+",statu.........."); //0
		System.out.println(info.getBillType()+",type.........."); //null
		System.out.println(lstTmp.toString()); //'86','8601','860101','86010101','86010102','8602','860201','86020101','86020102','60202','86020201','86020202','8603','8611'
		
		return this.find("getInputInfoList", map,paginationList);
	}
	

	@Override
	public InputInfo findInvoiceScanAuthByBillId(String billId) {
		Map<String,String> params = new HashMap<String,String>();
		String[] bill = billId.split("-");
		if (bill.length==2) {
			params.put("billId", bill[0]);
			params.put("billCode", bill[1]);
		}
		return (InputInfo) this.findForObject("getInputInfo", params);
		
	}
	
	@Override
	public List<InputInfo> findInvoiceScanAuthListByBillId(String billId) {
		Map<String,String> params = new HashMap<String,String>();
		String[] bill = billId.split("-");
		if (bill.length==2) {
			params.put("billId", bill[0]);
			params.put("billCode", bill[1]);
		}
		return this.findForList("getInputInfo", params);
	}

	@Override
	public void updateVmsInputInvoiceInfoForAuthSubmit(String datastatus, String billId, String billCode) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("billId", billId);
		map.put("billCode", billCode);
		map.put("billStatu", datastatus);
		map.put("operateStatus", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		this.update("updateInputInfoBillStatu", map);
	}

	@Override
	public void updateVmsInputInvoiceInfoForScanAuth(InputInfo inputInfo, String o_bill_id) {
		
	}

	@Override
	public void updateNeedEnterInputBill(List<String> billList) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("bill", billList);
		this.update("findNeedEnterInputBill", map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<InputInfo> findListInvoiceScanAuth(InputInfo info) {
		Map<String, Object> map=new HashMap<String, Object>();
		List instIds=info.getLstAuthInstId();
		List<String> lstTmp=new ArrayList<String>();
		String billInst=info.getBillInst();
		if (StringUtils.isNotBlank(billInst)) {
			List<String> instList = Arrays.asList(billInst.split("，"));
			for (String str : instList) {
				if (StringUtils.isNotEmpty(str)) {
					lstTmp.add(str.split(" - ")[0]);
				}
				
			}
		}else {
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
		}
		
		map.put("auth_inst_ids", lstTmp);
		map.put("inputInfo", info);
		return this.find("getInputInfoList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillDetailEntity> finBillDetailEntites(List<String> billList) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("bill", billList);
		return this.find("getBillDetailList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRepeatBill(List<String> bills) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("bill", bills);
		return this.find("getRepeatBill", map);
	}

	@Override
	public void insertEnterOkInputBill(List<String> bills) {
		Map<String, List<String>> map=new HashMap<String, List<String>>();
		map.put("bill", bills);
		this.update("updatetEnterOkInputBill", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findWebServiceUrl(String name) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", name);
		return this.find("findWebserviceUrl", map);
	}

	//数据更新
	//从中间表中查出数据（主表）
	@Override
	public List<InputInfo> findDataByPrimary() {
		return this.find("findDataByPrimary", null);
	}
	//从中间表中查出数据（明细表）
	@Override
	public List<InputInvoiceNew> findDataByDetails() {
		return this.find("findDataByDetails", null);
	}
	
	//将数据插入到应用表（主表）
	@Override
	public void insertDataByPrimary(List<InputInfo> data) {
		Map<String,InputInfo> map = new HashMap<String,InputInfo>();
		this.delete("deleteDataByPrimary", map);
		for(int i = 0;i < data.size();i++){
			InputInfo inputInfo = data.get(i);
			System.out.println(inputInfo+"--------------------");
			map.put("inputInfo", inputInfo);
			this.save("insertDataByPrimary", map);
		}
	}
	//将数据插入到应用表（明细表）
	@Override
	public void insertDataByDetails(List<InputInvoiceNew> data) {
		Map<String,InputInvoiceNew> map = new HashMap<String,InputInvoiceNew>();
		this.delete("deleteDataByDetails", map);
		for(int i = 0;i < data.size();i++){
			InputInvoiceNew inputInvoiceNew = data.get(i);
			System.out.println(inputInvoiceNew+"--------------------");
			map.put("inputInvoiceNew", inputInvoiceNew);
			this.save("insertDataByDetails", map);
		}
	}

	//定时器
	@Override
	public List<TimeTaskEntity> findTimeTisk(String dataMold) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("dataMold", dataMold);
		return this.find("findTimeTask", map);
	}
	
	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 功能：通过远程连接总账的数据库(oracle)
	 */
	//start
	@Override
	public void deleteGeneralLedger(Map monthMap) {
		this.delete("deleteGeneralLedger", monthMap);
	}
	@Override
	public void insertGeneralLedger(Map map) {
		this.save("insertGeneralLedger", map);
	}
	//end

	

}
