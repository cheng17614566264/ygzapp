package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillTrackService;

public class BillTrackServiceImpl extends GenericServiceImpl implements
		BillTrackService {

	public List findBillInfoList(BillInfo billInfo,	PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List lstTmp = new ArrayList();
		lstTmp.add(billInfo.getInstCode());
		map.put("auth_inst_ids", lstTmp);
		return findLargeData("findBillTrack", map, paginationList);
	}

	public List findTransByBillId(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		
		return find("findTransByBillId", map, paginationList);
	}
	
	//2018-03-23新增
	public List findBillByBillId(String billId,PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		
		return find("findBillByBillId", map, paginationList);
	}

	public BillInfo findBillInfoById(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		
		List result = find("findBillInfo", map);
		if (result != null && result.size() > 0) {
			return (BillInfo)result.get(0);
		}
		return null;
	}
	public BillInfo findBillInfoById1(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		
		List result = find("findBillInfo2", map);
		if (result != null && result.size() > 0) {
			return (BillInfo)result.get(0);
		}
		return null;
	}

	public List findBillItemByBillId(String billId) {
		Map map = new HashMap();
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillId(billId);
		map.put("billItem", billItem);
		
		return find("findBillItemInfo", map);
	}

	public void setBillDataStatus(String billIds, String dataStatus) {
		Map map = new HashMap();
		if (billIds.contains("','")) {
			map.put("billIds", billIds);
		} else {
			map.put("billId", billIds);
		}
		map.put("dataStatus", dataStatus);
		save("updateBillDataStatus", map);
	}

	public List findBillItemByBillIds(String[] selectBillIds, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billIds", selectBillIds);
		
		return find("findBillTrack", map, paginationList);
	}

	public void deleteBillInfoById(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		
		delete("deleteBillInfo", map);
		
	}

	public void updateBillInfoStatus(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billId", billInfo.getBillId());
		map.put("dataStatus", billInfo.getDataStatus());
		
		update("updateBillDataStatus", map);
		
	}

	public void updateTransInfoStatus(String dataStatus, String billId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("billId", billId);
		
		update("updateTransInfoStatus", map);
		
	}

	public void deleteTransBillInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		
		delete("deleteTransBillInfo", map);
	}

	public void deleteBillItemInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		
		delete("deleteBillItemInfo", map);
	}

	public List findBillBySelect(String[] billIds) {
		Map map = new HashMap();
		map.put("billIds", billIds);
		
		return find("findBillTrack", map);
	}

	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		
		return find("findBillTrack", map);
	}

	public String findRegisteredInfo(String taxDisNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatebillInfoIssueResult(BillInfo bill) {
		// TODO Auto-generated method stub
		
	}

	public List findInvalidPaperInvoice() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List findBillInfoList4CurrentUserInst(BillInfo billInfo,
			PaginationList paginationList, List authInstList) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List currentUserInstList = new ArrayList();
		for (int i = 0; i < authInstList.size(); i++) {
			currentUserInstList.add(((Organization) authInstList.get(i)).getCustomId());
		}
		map.put("auth_inst_ids", currentUserInstList);
		return findLargeData("findBillTrack", map, paginationList);
	}

	@Override
	public List findBillInfoList4CurrentUserInst(BillInfo billInfo,
			List authInstList) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List currentUserInstList = new ArrayList();
		for (int i = 0; i < authInstList.size(); i++) {
			currentUserInstList.add(((Organization) authInstList.get(i)).getCustomId());
		}
		map.put("auth_inst_ids", currentUserInstList);
		return find("findBillTrack", map);
	}

}
