package com.cjit.vms.stock.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.entity.LostRecycle;
import com.cjit.vms.stock.entity.PrintBill;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.trans.model.BillInfo;
/**
 *库存service实现类 
 *调用的sqlmap配置文件为sqlmap-vms-stock.xml
 */
public class StockServiceImpl extends GenericServiceImpl implements StockService{

	@SuppressWarnings("unchecked")
	@Override
	public List<BillInventory> findBillInventory(BillInventory billInventory,PaginationList paginationList) {
		Map<String, BillInventory> map = new HashMap<String, BillInventory>();
		map.put("billInventory", billInventory);
			return this.find("findBillInventoryList", map ,paginationList);
	}

	@Override
	public void insertBillInventory(List<BillInventory> billInventoryList) {
		this.insertBatch("insertBillInventoryList", billInventoryList);
	}

	@Override
	public void updateBillInventory(List<BillInventory> billInventoryList) {
		this.updateBatch("updateBillInventoryList", billInventoryList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillDistribution> findDistribution(BillDistribution billDistribution,PaginationList paginationList) {
		Map<String, BillDistribution> map = new HashMap<String,BillDistribution>();
		map.put("billDistribution", billDistribution);
		return this.find("findBillDistribution", map,paginationList);
	}

	@Override
	public void insertBillDistribution(List<BillDistribution> billDistributionList) {
		this.insertBatch("insertBillDistributionList", billDistributionList);
	}

	@Override
	public void updateBillDistribution(List<BillDistribution> billDistributionList) {
		this.updateBatch("updateBillDistributionList", billDistributionList);
	}

	@Override
	public void insertLostRecycle(List<LostRecycle> lostRecycleList) {
		this.insertBatch("insertLostRecycle", lostRecycleList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LostRecycle> findLostRecycle(LostRecycle lostRecycle) {
		Map<String, LostRecycle> map=new HashMap<String, LostRecycle>();
		map.put("lostRecycle", lostRecycle);
		return this.find("findLostRecycle", map);
	}

	@Override
	public void updatetLostRecycle(List<LostRecycle> lostRecycleList) {
		this.updateBatch("updateLostRecycle", lostRecycleList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillInventory> findBillInventory(BillInventory billInventory) {
		Map<String, BillInventory> map = new HashMap<String, BillInventory>();
		map.put("billInventory", billInventory);
		return this.find("findBillInventoryList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillDistribution> findDistribution(BillDistribution billDistribution) {
		Map<String, BillDistribution> map = new HashMap<String,BillDistribution>();
		map.put("billDistribution", billDistribution);
		return this.find("findBillDistribution", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LostRecycle> findLostRecycle(LostRecycle lostRecycle, PaginationList paginationList) {
		Map<String, LostRecycle> map=new HashMap<String, LostRecycle>();
		map.put("lostRecycle", lostRecycle);
		return this.find("findLostRecycle", map,paginationList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getDictionarys(String codeType, String codeSym, String backup_num) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("codeType", codeType);
		map.put("codeSym", codeSym);
		map.put("backupNum", backup_num);
		return this.find("getDictionarys", map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BillDistribution findBbillDistributionbykyid(BillDistribution billDistribution) {
		Map<String, BillDistribution> map=new HashMap<String, BillDistribution>();
		map.put("billDistribution", billDistribution);
		List<BillDistribution> list=find("finduserbyid", map);
		if(list!=null&&list.size()>0){
			BillDistribution distribution=(BillDistribution) list.get(0);
			return distribution;
		}
		return null;
	}

	@Override
	public String getInstName(String InstId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("InstId", InstId);
		return (String) findForObject("getInsrtName", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsersByOrgId(String orgId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("orgId", orgId);
		return find("findUserByOrgIdX", map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrintBill> findPrintBills(PrintBill printBill) {
		Map<String, PrintBill> map=new HashMap<String, PrintBill>();
		map.put("printBill", printBill);
		return this.find("findPrintBill", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrintBill> findPrintBills(PrintBill printBill, PaginationList paginationList) {
		Map<String, PrintBill> map=new HashMap<String, PrintBill>();
		map.put("printBill", printBill);
		return this.find("findPrintBill", map,paginationList);
	}

	@Override
	public void updatePrintBill(List<PrintBill> list) {
		this.updateBatch("updatePrintBillRecycleStatus", list);
	}

	@Override
	public void updateBillDistribution(String disid) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("disid", disid);
		this.delete("deleteBillDistribution", map);
	}
	
	@Override
	public List<BillInfo> findBillMakeUseDistribution(String billid ,String billStarNo ,String billEndNo) {
		List<String> fpList=new ArrayList<String>();
		List<BillInfo> billList=new ArrayList<BillInfo>();
		for(int i=Integer.parseInt(billStarNo);i<Integer.parseInt(billEndNo);i++){
			//发票拼接：代码号码
			fpList.add(billid.concat(i+""));
		}
		Map<String, String> map=new HashMap<String, String>();
		List<BillInfo> list=this.find("findBillMakeUseDistribution", map);
		for (BillInfo billInfo : list) {
			String fpBill=billInfo.getBillCode().concat(billInfo.getBillNo());
			if(fpList.contains(fpBill)){
				billList.add(billInfo);
			}
		}
		return billList;
	}


}
