package com.cjit.vms.trans.action;

import java.util.List;

import com.cjit.vms.trans.service.BillTrackService;

public class BillInfoDetailAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String flag;
	private String billId;
	private List transInfoList;
	private BillTrackService billTrackService;//ys
	private String dsource;

	/**
	 * 发票编辑页面查看交易
	 * 
	 * @return
	 */
	public String listBillTrans() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				fromFlag = null;
			}
			//2018-03-29新增 分数据来源进入不同方法
			paginationList.setShowCount("true");
			if("SG".equals(dsource)){
				transInfoList = billTrackService.findBillByBillId(billId,paginationList);
				this.request.setAttribute("status","BILL");
			}else{
				transInfoList = billTrackService.findTransByBillId(billId, paginationList);
				this.request.setAttribute("status","Trans");
			}
			request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoDetailAction-viewTransFromBill", e);
		}
		return ERROR;
	}

	public String getDsource() {
		return dsource;
	}

	public void setDsource(String dsource) {
		this.dsource = dsource;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}
}
