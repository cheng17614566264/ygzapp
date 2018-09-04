package com.cjit.vms.electronics.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.electronics.service.ElectronicsService;
import com.cjit.vms.electronics.service.RedElectronicsBillInvoiceAuditService;
import com.cjit.vms.electronics.service.RedElectronicsBillService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;

public class RedElectronicsInvoiceAudit extends DataDealAction {

	private static final String BillInfo = null;
	private RedElectronicsBillInvoiceAuditService redElectronicsBillInvoiceAuditService;
	private RedElectronicsBillService redElectronicsBillService;
	private ElectronicsService electronicsService;
	private String message;
	private List billInfoList;
	private BillInfo billInfo;

	// 【红冲审核】页面数据查询
	public String listElectronicsRedInvoiceAudit() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			User currentUser = this.getCurrentUser();
			if (null != billInfo && billInfo.getBillId() != null) {
				billInfo = null;
			}
			String strFromViewFlg = request.getParameter("fromFlag");
			billInfo = new BillInfo();
			// 区分电票
			billInfo.setFapiaoType("1");
			if ("menu".equals(strFromViewFlg)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);

			} else {
				this.billInfo.setCustomerName(this.request.getParameter(
						"billInfo.customerName").trim());
				System.out.println(this.request
						.getParameter("billInfo.customerName")
						+ ",customerName");

				this.billInfo.setOriBillNo(this.request.getParameter(
						"billInfo.oriBillNo").trim());
				System.out.println(this.request
						.getParameter("billInfo.oriBillNo")
						+ ":billInfo.oriBillNo");

				this.billInfo.setOriBillCode(this.request.getParameter(
						"billInfo.oriBillCode").trim());
				System.out.println(this.request
						.getParameter("billInfo.oriBillCode")
						+ ":billInfo.oribillCode");

				this.billInfo.setBatchNo(this.request.getParameter(
						"billInfo.batchNo").trim());
				System.out
						.println(this.request.getParameter("billInfo.batchNo")
								+ ":billInfo.batchNo");

				this.billInfo.setCherNum(this.request.getParameter(
						"billInfo.cherNum").trim());
				System.out
						.println(this.request.getParameter("billInfo.cherNum")
								+ ":billInfo.cherNum");

				this.billInfo.setBillBeginDate(this.request.getParameter(
						"billInfo.billBeginDate").trim());
				System.out.println(this.request
						.getParameter("billInfo.billBeginDate")
						+ ":billInfo.billBeginDate");

				this.billInfo.setBillEndDate(this.request.getParameter(
						"billInfo.billEndDate").trim());
				System.out.println(this.request
						.getParameter("billInfo.billEndDate")
						+ ":billInfo.billEndDate");
				// 疑问 是录单机构，还是交易机构
				this.billInfo.setInstFrom(this.request.getParameter(
						"billInfo.instFrom").trim());
				System.out.println(this.request
						.getParameter("billInfo.instFrom")
						+ ":billInfo.instFrom");

			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			// 2018-06-08计数新增
			paginationList.setShowCount("true");
			billInfoList = redElectronicsBillInvoiceAuditService
					.findRedElectronicsAuditList("findRedElectronicsAuditList",
							billInfo, currentUser.getId(), paginationList);
			// 根据登陆机构获取所有下级机构
			String instId = this.getCurrentUser().getOrgId();
			List instcodes = electronicsService
					.findInstCodeByUserInstId(instId);
			this.request.setAttribute("instcodes", instcodes);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("configCustomerFlag",
					this.configCustomerFlag);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲审核", "销项税管理", "查询可供进行红冲审核列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲审核", "销项税管理", "查询可供进行红冲审核列表", "0");
			log.error("RedElectronicsInvoiceAudit-findRedElectronicsAuditList",
					e);
		}
		return ERROR;
	}

	// 获取交易信息

	public void redElectronicsInvoiceAuditGetTransInfo() {
		AjaxReturn ajax = new AjaxReturn();
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			try {
				returnResult(new AjaxReturn(false));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.billInfo = new BillInfo();
		this.billInfo.setTtmpRcno(this.request.getParameter("ttmpRcno"));
		this.billInfo.setCherNum(this.request.getParameter("cherNum"));
		List findRedElectronicsList = redElectronicsBillService
				.findRedElectronicsList(billInfo);
		if (findRedElectronicsList.size() != 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			BillInfo billinfos = (BillInfo) findRedElectronicsList.get(0);
			map.put("billId", billinfos.getBillId());
			ajax.setAttributes(map);
			ajax.setIsNormal(true);
		} else {
			ajax.setIsNormal(false);
		}
		try {
			returnResult(ajax);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	// // 【红冲审核】页面[审核通过],[审核拒绝]
	// public String redReceiptApprove() throws Exception {
	// if (!sessionInit(true)) {
	// request.setAttribute("message", "用户失效");
	// return ERROR;
	// }
	// String billId = request.getParameter("billId");
	// String[] ids = billId.split(",");
	// String result = request.getParameter("result");
	// BillInfo bill;
	// if (result.equals("17")) {
	// for (int i = 0; i < ids.length; i++) {
	// bill = new BillInfo();
	// if (!ids[i].equals("") && ids[i] != null) {
	// bill = redReceiptApplyInfoService.findBillInfo1(ids[i]);
	// if (bill == null) {
	// request.setAttribute("message", "数据错误");
	// return ERROR;
	// }
	//
	// bill.setCancelAuditor(getCurrentUser().getId());
	// // 原bill状态：[17：红冲已审核]
	// bill.setDataStatus(DataUtil.BILL_STATUS_17);
	// redReceiptApplyInfoService.saveBillInfo1(bill, true);
	// // 红冲bill状态：[21：红冲审核已通过]
	// bill.setDataStatus(DataUtil.BILL_STATUS_21);
	// redReceiptApplyInfoService.updateRedBill(bill);
	// }
	// }
	// this.message = "审核成功";
	// this.request.setAttribute("message", this.message);
	// } else {
	// String cancelReason = request.getParameter("cancelReason");
	// cancelReason = URLDecoder.decode(cancelReason, "utf-8");
	// for (int i = 0; i < ids.length; i++) {
	// bill = new BillInfo();
	// if (!ids[i].equals("") && ids[i] != null) {
	// bill = redReceiptApplyInfoService.findBillInfo1(ids[i]);
	// if (bill == null) {
	// request.setAttribute("message", "数据错误");
	// return ERROR;
	// }
	// Map map = new HashMap();
	// map.put("searchCondition", "t.DATASTATUS in (16)");
	// RedReceiptApplyInfo rrai =
	// redReceiptApplyInfoService.findListByBillId(ids[i], map);
	// if (null != rrai && rrai.getFapiaoType().equals("0")) {
	// redReceiptApplyInfoService.deleteApplyInfo(bill.getBillNo());
	// }
	// bill.setCancelInitiator("");
	// bill.setDataStatus(bill.getOperateStatus());
	// bill.setOperateStatus("");
	// bill.setCancelReason(cancelReason);
	// bill.setBalance(bill.getSumAmt());
	// redReceiptApplyInfoService.saveBillInfo1(bill, true);
	// // 删除billInfo
	// Map dataMap = new HashMap();
	// dataMap.put("oriBillCode", bill.getBillCode());
	// dataMap.put("oriBillNo", bill.getBillNo());
	// List list = redReceiptApplyInfoService.findReleaseTrans(dataMap);
	// // 删除VMS_HC_APPLY_INFO
	// redReceiptApplyInfoService.deleteApplyInfo(bill.getBillCode(),
	// bill.getBillNo());
	// BillItemInfo bii;
	// BillItemInfo tempItem = new BillItemInfo();
	// RedReceiptTransInfo rrti = (RedReceiptTransInfo) list.get(0);
	// redReceiptApplyInfoService.deleteBillInfo(rrti.getBillId());
	// tempItem.setBillId(rrti.getBillId());
	// List itemList =
	// redReceiptApplyInfoService.findBillItemInfoList(tempItem);
	// for (int j = 0; j < itemList.size(); j++) {
	// bii = (BillItemInfo) itemList.get(j);
	// redReceiptApplyInfoService.deleteBillItemInfo(rrti.getBillId(),
	// bii.getBillItemId());
	// }
	// for (int k = 0; k < list.size(); k++) {
	// rrti = (RedReceiptTransInfo) list.get(k);
	// transInfoService.deleteTransBill(rrti.getTransId(), rrti.getBillId());
	// }
	// }
	// }
	// this.message = "成功拒绝申请！";
	// this.setResultMessages(message);
	// printWriterResult("sucess");
	// }
	//
	// return SUCCESS;
	// }

	public RedElectronicsBillInvoiceAuditService getRedElectronicsBillInvoiceAuditService() {
		return redElectronicsBillInvoiceAuditService;
	}

	public void setRedElectronicsBillInvoiceAuditService(
			RedElectronicsBillInvoiceAuditService redElectronicsBillInvoiceAuditService) {
		this.redElectronicsBillInvoiceAuditService = redElectronicsBillInvoiceAuditService;
	}

	public ElectronicsService getElectronicsService() {
		return electronicsService;
	}

	public void setElectronicsService(ElectronicsService electronicsService) {
		this.electronicsService = electronicsService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public RedElectronicsBillService getRedElectronicsBillService() {
		return redElectronicsBillService;
	}

	public void setRedElectronicsBillService(
			RedElectronicsBillService redElectronicsBillService) {
		this.redElectronicsBillService = redElectronicsBillService;
	}

}
