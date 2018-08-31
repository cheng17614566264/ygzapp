package com.cjit.vms.trans.service.createBill.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

public class CreateBillServiceManualImpl extends CreateBillServicelAbs {

	@Override
	public List<CheckResult> constructBillAndSaveAsMerge(List transInfoList, User currentUser,String payee) {
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			saveContextAsMerge(billContext, currentUser.getName(),payee);
		}

		return null;
	}

	@Override
	public List<CheckResult> constructBillAndSaveAsSingle(List transInfoList, User currentUser,String payee) {
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			saveContextAsSingle(billContext, currentUser.getName(),payee);
		}
		return null;
	}

	@Override
	public List<CheckResult> constructBillAndSaveAsSplit(List transInfoList, User currentUser,String payee) {
		System.out.println("22222222222222222");
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			System.out.println(billContext);
			saveContextAsSplit(billContext, currentUser.getName(),payee);
		}
		return null;
	}

	@Override
	public TransInfo findTransInfo(TransInfo transInfo) {
		Map<String,TransInfo> map = new HashMap<String,TransInfo>();
		map.put("transInfo", transInfo);
		@SuppressWarnings("unchecked")
		List<TransInfo> list = find("findTransCreateBill", map);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	@Override
	public List findTransList(TransInfo transInfo, PaginationList paginationList) {
		return null;
	}

	@Override
	public List findTransMergeFlagList() {
		return null;
	}

	@Override
	public List findTransList(TransInfo transInfo, PaginationList paginationList, String strFlagType,
			String strMergeSql) {
		return null;
	}

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	@Override
	public void setBillStaticInfoMerge(BillInfo billInfo) {

		String billStatus = DataUtil.BILL_STATUS_1;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_2; // 发票开具类型1-单笔

		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public void setBillStaticInfoSingle(BillInfo billInfo) {

		String billStatus = DataUtil.BILL_STATUS_1;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_1; // 发票开具类型1-单笔

		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public void setBillStaticInfoSplit(BillInfo billInfo) {
		String billStatus = DataUtil.BILL_STATUS_1;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_3; // 发票开具类型1-单笔
		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}

	@Override
	public void updateTransAmtAndStatus(List<BillTransInfo> billTrans) {
		for (int i = 0; i < billTrans.size(); i++) {
			BillTransInfo billTransInfo = billTrans.get(i);
			System.out.println(billTransInfo.getTransId());
			System.out.println(billTransInfo.getAmtCny());
			System.out.println(billTransInfo.getTaxAmtCny());
			System.out.println(billTransInfo.getBillId());
			System.out.println(billTransInfo.getBalance());
			System.out.println(billTransInfo.getBillItemId());
			Map map = new HashMap();
			map.put("billTrans", billTransInfo);
			save("updateTransAmtAndStatusManual", map);
		}
	}

	@Override
	public Integer findSum(String[] strs) {
		Map<String, List<String>> idMap = new HashMap<String, List<String>>();
		List<String> idList = new ArrayList<String>();
		for (String str : strs) {
			idList.add(str);
		}
		idMap.put("idList", idList);
		@SuppressWarnings("unchecked")
		List<Integer> list = find("findTransSum", idMap);
		if (list.size() == 0) {
			return 0;
		}
		int sum = list.get(0);
		return sum;
	}

	@Override
	public Integer findBussisedSum(String[] strs) {
		Map<String, List<String>> idMap = new HashMap<String, List<String>>();
		List<String> idList = new ArrayList<String>();
		for (String str : strs) {
			idList.add(str);
		}
		idMap.put("idList", idList);
		List<Integer> list = find("findBussisedSum", idMap);
		if (list.size() == 0) {
			return 0;
		}
		int sum = list.get(0);
		return sum;
	}

	@Override
	public String findFlag(String selectTransId) {
		Map<String, String> transIdMap = new HashMap<String, String>();
		transIdMap.put("flag", selectTransId);
		List<String> flagList = find("findFlag", transIdMap);
		return flagList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findPayee(String instId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("instId", instId);
		List<String> list = this.find("getPayee", map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}

	//2018-07-06新增
	@Override
	public List<BillInfo> eleConstructBillAndSaveAsMerge(List transInfoList) {
		BillsTaxNoContext billsTaxNoContext = constructBillService
				.constructBill(transInfoList);
		// 保存票据
		List<BillInfo> list = new ArrayList<BillInfo>();
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			billContext.getBillInfo().setDrawer("开票员");
			billContext.getBillInfo().setReviewer("复核员");
			billContext.getBillInfo().setPayee("收款员");
			saveContextAsMerge(billContext);
			list.add(billContext.getBillInfo());
			
		}
		
		return list;
	}


}
