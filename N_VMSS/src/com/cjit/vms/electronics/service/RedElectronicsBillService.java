package com.cjit.vms.electronics.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.TransBillInfo;

public interface RedElectronicsBillService {
	/**
	 * 红冲页面查询
	 * 
	 * @param billInfo
	 * @param paginationList
	 *            票据集合
	 * @return
	 */
	public List findRedElectronicsList(BillInfo billInfo,
			PaginationList paginationList);

	/**
	 * 查询billInfo
	 * 
	 * @param billInfo
	 * @return
	 */
	public List findRedElectronicsList(BillInfo billInfo);

	/**
	 * 查询交易i
	 * 
	 * @param billId
	 *            票据id
	 * @param paginationList
	 *            交易集合
	 * @return
	 */
	public List findTransByElectronicsBillId(String billId,
			PaginationList paginationList);

	/**
	 * 查询负数电子票对应蓝票
	 * 
	 * @param sqlId
	 * @param applyInfo
	 * @param userID
	 * @param paginationList
	 * @return
	 */
	public List findElectronicsRedReceiptApplyList(String sqlId,
			RedReceiptApplyInfo applyInfo, String userID,
			PaginationList paginationList);

	/**
	 * 发票红冲】页面数据查询
	 * 
	 * @param billId
	 * @param paginationList
	 * @return
	 */
	public List findElectronicsRedReceiptTrans(String billId,
			PaginationList paginationList);

	/**
	 * 校验票据信息
	 * 
	 * @param billidArray
	 * @return
	 */
	public JSONArray validate(String[] billidArray);

	/**
	 * 根据billId 查询待红冲的BillInfo
	 * 
	 * @param billId
	 * @return
	 */

	public BillInfo findBillInfoByBillId(String billId);

	public List<TransBillInfo> finTransBillByBillId(String billId);

	/**
	 *  获取所有红冲后的新bill
	 * 
	 * @param billId
	 * @return
	 */
	public List findRedBillByOriBillId(String billId);

	/**
	 * 跟新状态
	 * 
	 * @param billInfo
	 * @param isUpdate
	 */
	public void saveBillInfo(BillInfo billInfo, boolean isUpdate);

	/**
	 * 
	 * @param billItemInfo
	 * @return
	 */
	public List findBillItemInfoList(BillItemInfo billItemInfo);

	/**
	 *  保存新生成billItem
	 * 
	 * @param billItemInfo
	 * @param isUpdate
	 */
	public void saveBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate);
	/** 
   * 查询票据  
   *  
   * @param billItemInfo 
   * @param isUpdate 
   */ 
   public List<BillInfo> findBillInfo(String[] billidArray); 
      /** 
   * 更新票的状态  
   *  
   * @param billItemInfo 
   * @param isUpdate 
   */ 
   public void updateRedBill(BillInfo billInfo); 

}
