package com.cjit.vms.electronics.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.electronics.service.RedElectronicsBillService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.TransBillInfo;

public class RedElectronicsBillServiceImpl extends GenericServiceImpl implements
		RedElectronicsBillService {

	private static final long serialVersionUID = 1L;

	@Override
	public List findRedElectronicsList(BillInfo billInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = billInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			billInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		if(!dataStatus.equals("")){  
			map.put("dataStatus", dataStatus.split(","));//cheng0906 新增 用于sql遍历 状态  
		}
		map.put("billInfo", billInfo);
		return find("findRedElectronicsList", map, paginationList);
	}

	@Override
	public List findRedElectronicsList(BillInfo billInfo) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = billInfo.getDataStatus();
		map.put("billInfo", billInfo);
		return find("findRedElectronicsList", map);
	}

	@Override
	public List findTransByElectronicsBillId(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		return find("findTransByElectronicsBillId", map, paginationList);
	}

	@Override
	public List findElectronicsRedReceiptApplyList(String sqlId,
			RedReceiptApplyInfo applyInfo, String userID,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = applyInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		StringBuffer searchCondition = new StringBuffer();
		map.put("applyInfo", applyInfo);
		return find(sqlId, map, paginationList);
	}

	/**
	 * 【发票红冲】页面数据查询
	 */
	@Override
	public List findElectronicsRedReceiptTrans(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("redElectronicsReceiptTransList", map, paginationList);
	}

	@Override
	public JSONArray validate(String[] billidArray) {
		Map map = new HashMap();
		List billids = Arrays.asList(billidArray);
		map.put("billids", billids);
		List queryList = this.find("findElectronicsAliveBillsInSameBusinessid", map);
		queryList = queryList == null ? new ArrayList() : queryList;

		Map<String, List<Map>> map2 = new HashMap<String, List<Map>>();
		for (Object o : queryList) {
			Map<String, String> tMap = (Map) o;

			List<Map> tList = null;
			if (map2.containsKey(tMap.get("BUSINESSID"))) {
				tList = map2.get(tMap.get("BUSINESSID"));
			} else {
				tList = new ArrayList<Map>();
				map2.put(tMap.get("BUSINESSID"), tList);
			}
			tList.add(tMap);
		}

		JSONArray result = new JSONArray();
		JSONObject jsonO = null;
		for (String key : map2.keySet()) {
			jsonO = new JSONObject();
			map = new HashMap<String, String>();
			map.put("businessid", key);
			long conut = this.getRowCount("findElectronicsTransUnCreateBill", map);
			if (conut > 0) {
				jsonO.put("hasTransUnCreateBill", true);
			} else {
				jsonO.put("hasTransUnCreateBill", false);
			}
			JSONArray selected = new JSONArray();
			JSONArray unselected = new JSONArray();
			List<Map> tList = map2.get(key);

			for (Map map3 : tList) {
				if (billids.contains(map3.get("BILLID"))) {
					selected.add(JSONObject.fromObject(map3));
				} else {
					unselected.add(JSONObject.fromObject(map3));
				}
			}

			if (unselected.size() > 0
					|| jsonO.getBoolean("hasTransUnCreateBill")) {
				jsonO.put("selected", selected);
				jsonO.put("unselected", unselected);
				result.add(jsonO);
			}
		}
		return result;
	}

	@Override
	public BillInfo findBillInfoByBillId(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List list = find("findElectronicsBillInfoByBillId", map);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<TransBillInfo> finTransBillByBillId(String billId) {
		Map param = new HashMap();
		param.put("billId", billId);
		return (List<TransBillInfo>) this.find(
				"findElectronicsTransBillByBillId", param);
	}

	@Override
	public List findRedBillByOriBillId(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("findElectronicsRedBillByOriBillId", map);

	}

	@Override
	public void saveBillInfo(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		if (isUpdate) {
			this.save("updateElectronicsBill", param);
		} else {
			this.save("saveElectronicsBill", param);
		}
	}
	
	@Override
	public List findBillItemInfoList(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findElectronicsBillItemInfo", map);
	}
	@Override
	public void saveBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billItemInfo", billItemInfo);
		if (isUpdate) {
			this.save("updateElectronicsBillItem", param);
		} else {
			this.save("saveElectronicsBillItem", param);
		}
	}
	
	@Override  
	public List<BillInfo> findBillInfo(String[] billidArray){  
		BillInfo billInfo = new BillInfo();  
		Map map = new HashMap();  
		map.put("billidArray", billidArray);  
		List list = find("findElectronicesBillInfo", map);  
		 return list;  
	}  
	@Override  
	public void updateRedBill(BillInfo billInfo) {  
		Map param = new HashMap();  
		param.put("billInfo", billInfo);  
		this.save("updateElectronicesRedBill", param);  
	 }
	/**
	 * 电子发票红冲查询票据
	 * cheng 0907 新增
	 * @return
	 */
	public BillInfo findElectronicsBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List list = find("findElectronicsBillInfoByBillId", map);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}
	
	
	
	
}
