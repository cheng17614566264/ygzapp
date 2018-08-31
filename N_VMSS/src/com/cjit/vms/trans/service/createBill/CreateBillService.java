package com.cjit.vms.trans.service.createBill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.sun.corba.se.spi.orb.StringPair;

public interface CreateBillService {
	public BillsTaxNoContext constructBill(List transInfoList, User currentUser);

	public List<CheckResult> constructBillAndSaveAsMerge(List transInfoList,
			User currentUser,String payee);

//	public  List<CheckResult> constructBillAndSaveAsSingle(List transInfoList,
//			User currentUser);
	public  List<CheckResult> constructBillAndSaveAsSingle(List transInfoList,
			User currentUser,String payee);

	public  List<CheckResult> constructBillAndSaveAsSplit(List transInfoList,
			User currentUser,String payee);

	public List findGoodsInfo(TransInfo transInfo);

	public List findTaxInfoList(TransInfo transInfo);

	public TransInfo findTransInfo(TransInfo transInfo);

	public List findTransList(TransInfo transInfo,PaginationList paginationList);

	public void saveBillGoodsInfo(BillGoodsInfo billGoodsInfo);

	public void saveBillInfo(BillInfo billInfo);
	
	public BigDecimal findMaxAmt(TransInfo transInfo);

	public void saveBillTrans(BillTransInfo billTrans);

	public String saveLog(BillsTaxNoContext billsTaxNoContext, User currentUser);

	public void saveTransProcessing(List transList);
	
	public List findTransMergeFlagList();
	
	public List findTransList(TransInfo transInfo,PaginationList paginationList,String strFlagType,String strMergeSql);
	/**
	 *李松加
	 */
	public Integer findSum(String[]strs);	
	
	public Integer findBussisedSum(String[]strs);
	
	public String findFlag(String selectTransId);
	/**
	 * 根据机构ID查找开票人名称
	 * @param instId
	 * @return
	 */
	public String findPayee(String instId);
	
	//2018-07-06新增
	public List<BillInfo> eleConstructBillAndSaveAsMerge(List transInfoList);
	
}
