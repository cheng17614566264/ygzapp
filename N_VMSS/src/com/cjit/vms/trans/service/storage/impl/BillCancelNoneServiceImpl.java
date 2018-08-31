package com.cjit.vms.trans.service.storage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.service.storage.BillCancelNoneService;

import cjit.crms.util.json.JsonUtil;
import com.cjit.vms.trans.model.storage.InvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
public class BillCancelNoneServiceImpl extends GenericServiceImpl implements BillCancelNoneService{

	@Override
	public List getInvoiceStockDetailList(InvoiceStockDetail stockDetail,PaginationList paginationList) {
		Map map = new HashMap();		
		map.put("instId", stockDetail.getInstId());
		map.put("invoiceType", stockDetail.getInvoiceType());
		map.put("startTime", stockDetail.getStartTime());
		map.put("endTime", stockDetail.getEndTime());
		map.put("userId", stockDetail.getUserId());
		List instIds=stockDetail.getLstAuthInstId();
		List lstTmp=new ArrayList();
		if(instIds!=null){
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
			map.put("auth_inst_ids", lstTmp);
		}	
		return find("getInvoiceStockDetailList", map, paginationList);
	}
	
	public List getInstInfoList(InstInfo info,PaginationList paginationList){
		
		Map map = new HashMap();
		List instIds=info.getLstAuthInstIds();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("inasId", info.getInstId());
		map.put("user_id", info.getUserId());
		map.put("instName", info.getInstName());
		map.put("taxNo", info.getTanNo());
		
		return this.find("getInstInfoList", map);
	}

	@Override
	public void updateBillNoneCancel(InvoiceStockDetail stockDetail) {
		Map map = new HashMap();
		map.put("userId", stockDetail.getUserId());
		map.put("invoiceCode", stockDetail.getInvoiceCode());
		map.put("invoiceNo", stockDetail.getInvoiceNo());
		map.put("invalidReason", stockDetail.getInvalidReason());
		map.put("invoiceId", stockDetail.getInvoiceId());
		update("updateBillNoneCancel", map);
	}

	@Override
	public List getInvoiceStockDetailOfList(InvoiceStockDetail stockDetail) {
		Map map = new HashMap();
		List instIds=stockDetail.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("userId", stockDetail.getUserId());
		map.put("invoiceCode", stockDetail.getInvoiceCode());
		map.put("invoiceNo", stockDetail.getInvoiceNo());
		map.put("invoiceType", stockDetail.getInvoiceType());
		return find("getInvoiceStockDetailOfList", map);
	}

	@Override
	public void revokeBillNoneCancel(InvoiceStockDetail stockDetail) {
		Map map = new HashMap();
		map.put("invoiceId", stockDetail.getInvoiceId());
		List list=find("findYCancelInvoiceForRevokeById", map);
		if(list.size()==1){
			stockDetail=(InvoiceStockDetail)list.get(0);
			map.put("num", -1);
			map.put("billCode", stockDetail.getInvoiceCode());
			map.put("billNo", stockDetail.getInvoiceNo());
			update("updateInvoiceDistrInReturn", map);
			update("updateInvoiceReCurInReturn", map);
			update("updateStockDetialNumInReturn", map);
			update("updateAutoInvoiceNumInreturn", map);
			delete("deleteCancelFoREvockeById", map);
		}
		//update("revokeBillNoneCancel", map);
	}

	@Override
	public String billNoneInvoiceReason(InvoiceStockDetail stockDetail) {
		Map paraMap = new HashMap();
		paraMap.put("invoiceId", stockDetail.getInvoiceId());
		paraMap.put("invoiceNo", stockDetail.getInvoiceNo());
		paraMap.put("invoiceCode", stockDetail.getInvoiceCode());
		List list = find("billNoneInvoiceReason", paraMap);
		Map resultMap = new HashMap();
		InvoiceStockDetail invoiceStockDetail = (InvoiceStockDetail) list.get(0);
		return invoiceStockDetail.getInvalidReason();
	}
	@Override
	public List findInvoiceByBillCodeAndBillNo(String billCode, String billNo) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		return find("findYInvoiceCodeByCodeAndNo", map);
	}

	@Override
	public List findYBankInvoiceByCodeAndNo(String billCode, String billNo) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		return find("findYBankInvoiceByCodeAndNo", map);
	}
	@Override
	public List<PaperAutoInvoice> findpaperAutoInvoicebyBusId(
			String invoiceNo, List<Organization> authInstIds, String invoiceCode) {
		  List lstTmp = new ArrayList();
	        for (int i = 0; i < authInstIds.size(); i++) {
	            Organization org = (Organization) authInstIds.get(i);
	            lstTmp.add(org.getId());
	        }
		Map map=new HashMap();
		map.put("invoiceNo", invoiceNo);
		map.put("auth_inst_ids", lstTmp);
		map.put("invoiceCode", invoiceCode);
		
		List<PaperAutoInvoice> list=find("findpaperBankAutoInvoicebyBusId", map);
		if(list.size()>0&&list.size()==1){
			return list;
		}
		
		return null;
	}

	@Override
	public String checkBillCodeAndBillNo(String billCode, String billNo,
			List<Organization> list) {
	List listStock=findpaperAutoInvoicebyBusId(billNo, list, billCode);
	List listCancel=findYBankInvoiceByCodeAndNo(billCode, billNo);
	List listBill=findInvoiceByBillCodeAndBillNo(billCode, billNo);
	String stock="",cancel="",bill="";
	List listjson=new ArrayList();
	if(listStock!=null&&listStock.size()==1){
				PaperAutoInvoice n = (PaperAutoInvoice) listStock.get(0);
				stock = n.getInvoiceType();
		}
	if(listCancel.size()!=0){
		cancel="cancelexist";
	}
	if(listBill.size()!=0){
		bill="billExist";
	}
	listjson.add(stock);
	listjson.add(cancel);
	listjson.add(bill);
	
		return JsonUtil.toJsonString(listjson);
	}

	/*updateInvoiceDistrInReturn   <!-- 更改分发表的数量 --> #num# #billCode# #billNo#

	updateInvoiceReCurInReturn	<!-- 更改当前领用当前表的数量  回写 -->			 #num# #billCode# #billNo#
	updateStockDetialNumInReturn <!-- 更改库存详情表的数量 --> #num# #billCode# #billNo#

	saveInvoiceBankCancel  <!-- 添加纸质发票作废信息 -->  #stockDetail.invoiceCode#,
	  #stockDetail.invoiceNo#,
	  #stockDetail.invoiceStatus#,
	  #stockDetail.invalidReason#

	updateAutoInvoiceNumInreturn <!-- 更改电子库存发票的数量 --> #num# #billCode# #billNo#*/
	@Override
	public void saveBillBankCancel(InvoiceStockDetail stockDetail) {
		try{
			Map map=new HashMap();
			map.put("billNo",stockDetail.getInvoiceNo());
			map.put("num", 1);
			map.put("billCode", stockDetail.getInvoiceCode());
			List<PaperAutoInvoice> AutoList=find("findAutoInvoiceForCancel", map);
			PaperAutoInvoice pa=AutoList.get(0);
			stockDetail.setReceiveInvoiceTime(pa.getReceiveInvoiceTime());
			stockDetail.setUserId(pa.getUserId());
			map.put("stockDetail",stockDetail);
			update("updateInvoiceDistrInReturn", map);
			update("updateInvoiceReCurInReturn", map);
			update("updateStockDetialNumInReturn", map);
			update("updateAutoInvoiceNumInreturn", map);
			save("saveInvoiceBankCancel", map);
			List listNum=find("findInvoiceCurSurNumforCancel", map);
			//System.out.println("-------------"+listNum.get(0));
			if(listNum!=null&&listNum.size()==1){
				int num=(Integer)listNum.get(0);
				if(num==0){
				delete("deleteInvoiceCurInCancel", map);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

 
}
