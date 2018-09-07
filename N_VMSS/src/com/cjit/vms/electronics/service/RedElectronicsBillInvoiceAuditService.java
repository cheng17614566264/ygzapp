package com.cjit.vms.electronics.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;

public interface RedElectronicsBillInvoiceAuditService {

	/**
	 * 电子发票红冲审核页面查询
	 * 
	 * @param sqlId
	 * @param applyInfo
	 * @param userID
	 * @param paginationList
	 * @return
	 */
	public List findRedElectronicsAuditList(String sqlId, BillInfo billInfo,
			String userID, PaginationList paginationList);

	
	/**
	 * 电子发票红冲查询票据
	 * cheng 0907 新增
	 * @return
	 */
	public com.cjit.vms.trans.model.BillInfo findElectronicsBillInfo(
			String string);

	/**
	 * 电子发票红冲审核更新
	 * cheng 0907 新增
	 * @return
	 */
	public void saveElectronicsBillInfo(com.cjit.vms.trans.model.BillInfo bill,
			boolean b);

}
