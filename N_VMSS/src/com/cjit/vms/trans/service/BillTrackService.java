package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;

public interface BillTrackService {

	public List findBillInfoList(BillInfo billInfo, PaginationList paginationList);

	public List findTransByBillId(String billId, PaginationList paginationList);

	//2018-03-23新增
	public List findBillByBillId(String billId, PaginationList paginationList);
	
	public BillInfo findBillInfoById(String billId);

	public List findBillItemByBillId(String billId);

	public void setBillDataStatus(String billIds, String dataStatus);

	public List findBillItemByBillIds(String[] selectBillIds, PaginationList paginationList);

	public void deleteBillInfoById(String billId);

	public void updateTransInfoStatus(String dataStatus, String billId);

	public void updateBillInfoStatus(BillInfo billInfo);

	public void deleteTransBillInfo(String billId);

	public void deleteBillItemInfo(String billId);

	public List findBillBySelect(String[] billIds);

	public List findBillInfoList(BillInfo billInfo);

	public String findRegisteredInfo(String taxDisNo);

	public void updatebillInfoIssueResult(BillInfo bill);

	public List findInvalidPaperInvoice();

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice);

	public BillInfo findBillInfoById1(String billId);

	/**
	 * 按查询条件，查询当前用户下所有机构的票据信息
	 * @param billInfo
	 * @param paginationList
	 * @param authInstList当前用户的所有机构
	 * @return
	 */
	public List findBillInfoList4CurrentUserInst(BillInfo billInfo,
			PaginationList paginationList, List authInstList);

	/**
	 * 按查询条件，查询当前用户下所有机构的票据信息
	 * @param billInfo
	 * @param authInstList当前用户的所有机构
	 * @return
	 */
	public List findBillInfoList4CurrentUserInst(BillInfo billInfo,
			List authInstList);
}
