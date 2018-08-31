package com.cjit.vms.stock.action;

import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.trans.action.DataDealAction;

/**
 * 发票分发
 *
 */
public class BillDistributeAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private StockService stockService;
	private BillDistribution billDistribution;
	private BillInventory billInventory=new BillInventory();
	/**
	 * 查询发票分发记录列表
	 */
	public String billDistributeList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User user=this.getCurrentUser();
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			//获取页面查询条件
			String string=request.getParameter("instId");
			if(string!=null&&string.length()!=0){
				String instId=string.split(" ")[0];
				billInventory.setInstId(instId);
			}else{
				billInventory.setInstId(user.getOrgId());
			}
			this.paginationList.setShowCount("false");
			stockService.findBillInventory(billInventory,paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInventoryAction-BillInventoryList", e);
		}
		return ERROR;
	
	}
	
	public StockService getStockService() {
		return stockService;
	}
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public BillDistribution getBillDistribution() {
		return billDistribution;
	}

	public void setBillDistribution(BillDistribution billDistribution) {
		this.billDistribution = billDistribution;
	}
}
