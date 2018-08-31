package com.cjit.vms.trans.action;

import java.util.List;

import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.service.BillIssueService;

public class BillIssueDetailAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String flag;
	private String billId;
	private List transInfoList;
	private BillIssueService billIssueService;//ys
	private String dsource;


	/**
	 * 发票开具页面查看交易
	 * 
	 * @return
	 */
	public String listBillIssueTrans() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				fromFlag = null;
			}
			BillInfo billInfo = new BillInfo();
			billInfo.setBillId(billId);
			billInfo.setUserId(this.getCurrentUser().getId());
            //transInfoList = billIssueService.findTransByBillId(billId, paginationList);
			//2018-05-02新增 分数据来源进入不同方法
			paginationList.setShowCount("true"); 
			if("SG".equals(dsource)){
				transInfoList = billIssueService.findBillByBillId(billId,paginationList);
				this.request.setAttribute("status","BILL");
			}else{
				transInfoList = billIssueService.findTransByBillId(billId, paginationList);
				this.request.setAttribute("status","Trans");
			}
			request.setAttribute("paginationList", paginationList);
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "查看交易", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "查看交易", "0");
			log.error("BillIssueDetailAction-listBillIssueTrans", e);
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

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}
}
