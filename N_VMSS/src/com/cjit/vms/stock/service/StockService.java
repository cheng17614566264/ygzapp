package com.cjit.vms.stock.service;

/**
 *库存service接口 
 *
 */

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.entity.LostRecycle;
import com.cjit.vms.stock.entity.PrintBill;
import com.cjit.vms.trans.model.BillInfo;
public interface StockService {

	/**

	 * 根据BillInventory查找库存记录
	 */
	public List<BillInventory> findBillInventory(BillInventory billInventory,PaginationList paginationList);
	/**
	 * 根据BillInventory查找库存记录
	 */
	public List<BillInventory> findBillInventory(BillInventory billInventory);
	/**
	 * 插入库存信息
	 */
	public void insertBillInventory(List<BillInventory> billInventoryList);
	/**
	 * 更新库存信息
	 */
	public void updateBillInventory(List<BillInventory> billInventoryList);
	/**
	 * 根据BillDistribution查找分发记录
	 */
	public List<BillDistribution> findDistribution(BillDistribution billDistribution,PaginationList paginationList);
	/**
	 * 根据BillDistribution查找分发记录
	 */
	public List<BillDistribution> findDistribution(BillDistribution billDistribution);
	/**
	 * 插入分发记录
	 */
	public void insertBillDistribution(List<BillDistribution> billDistributionList);
	/**
	 * 更新分发记录
	 */
	public void updateBillDistribution(List<BillDistribution> billDistributionList);
	/**
	 * 插入发票遗失或回收信息
	 */
	public void insertLostRecycle(List<LostRecycle> lostRecycleList);
	/**
	 * 查询发票遗失或回收信息
	 */
	public List<LostRecycle> findLostRecycle(LostRecycle lostRecycle,PaginationList paginationList);
	/**
	 * 查询发票遗失或回收信息
	 */
	public List<LostRecycle> findLostRecycle(LostRecycle lostRecycle);
	/**
	 * 更新发票遗失或回收信息
	 */
	public void updatetLostRecycle(List<LostRecycle> lostRecycleList);
	/**
	 * 查询excel表所包含的字段
	 */
	public List<Dictionary> getDictionarys(String codeType, String codeSym,String backup_num);
	
	public BillDistribution findBbillDistributionbykyid(BillDistribution billDistribution);
	
	public String getInstName(String InstId);
	
	public List<User> findUsersByOrgId(String orgId);
	/**
	 * 查询已打印发票
	 */
	public List<PrintBill> findPrintBills(PrintBill printBill);
	/**
	 * 查询已打印发票
	 */
	public List<PrintBill> findPrintBills(PrintBill printBill, PaginationList paginationList);
	/**
	 * 更新已打印发票回收状态
	 * @param list
	 */
	public void updatePrintBill(List<PrintBill> list);
	
	public void updateBillDistribution(String disid);
	
	/**
	 * 发票使用统计
	 * @return
	 */
	public List<BillInfo> findBillMakeUseDistribution(String billid ,String billStarNo ,String billEndNo);
}
