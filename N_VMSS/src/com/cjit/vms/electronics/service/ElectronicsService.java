package com.cjit.vms.electronics.service;

import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;

public interface ElectronicsService {

	public List findElectronics(Map<String, Object> map);

	/**
	 * 电子发票展示
	 * 
	 * @param transInfo
	 *            发票信息
	 * @param paginationList
	 *            分页
	 * @return 查询到的分页信息
	 */
	public List findElectronicsList(
			com.cjit.vms.trans.model.TransInfo transInfo,
			PaginationList paginationList);

	/**
	 * 电子发票查询
	 * 
	 * @param transInfo
	 *            发票信息
	 * @return 查询到的发票信息
	 */
	public List findElectronicsList(com.cjit.vms.trans.model.TransInfo transInfo);

	/**
	 * 
	 * @param dataStatus
	 *            状态
	 * @param fapiaoType
	 *            发票类型
	 * @param instId
	 *            机构代码
	 * @return
	 */
	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType,
			String instId);

	/**
	 * 查找税控参数
	 * 
	 * @param itemCname
	 *            税控名
	 * @return
	 */
	public String findTaxValuebyName(String itemCname);

	/**
	 * 根据用户机构查询下属机构群
	 * 
	 * @param userInstd
	 *            用户机构代码
	 * @return 返回下属机构群
	 */
	public List findInstCodeByUserInstId(String userInstd);

	/**
	 * 根据TransId查找TransInfo
	 * 
	 * @param info
	 * @return 返回该transInfo
	 */
	public TransInfo findTransInfoByTransId(TransInfo info);

	/**
	 * 查找收款人
	 * 
	 * @param instId
	 *            机构代码
	 * @return 返回收款人姓名
	 */
	public String findElectronicsPayee(String instId);

	/**
	 * 生成电票票据信息
	 * 
	 * @param transInfoList
	 * @return
	 */
	public List<BillInfo> eleConstructBillAndSaveAsMerge(List transInfoList);

	/**
	 * 创建Bill入口
	 * 
	 * @param transInfoList
	 * @return
	 */
	public BillsTaxNoContext constructBill(List transInfoList);

	/**
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public void setBillStaticInfoMerge(BillInfo billInfo);

	void updateTransAmtAndStatus(List<BillTransInfo> billTrans);

	/**
	 * 拆分票据信息
	 * 
	 * @param transInfo
	 * @return
	 */
	public List<TransInfo> spilitTrans(TransInfo transInfo);

	/**
	 * 生成票据
	 * 
	 * @param billsTaxNoContext
	 * @param transInfo
	 * @return
	 */
	public BillContext addTransToContext(BillsTaxNoContext billsTaxNoContext,
			TransInfo transInfo);


}