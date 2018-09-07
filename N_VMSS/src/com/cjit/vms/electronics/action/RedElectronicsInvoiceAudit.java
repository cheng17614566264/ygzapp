package com.cjit.vms.electronics.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.electronics.model.ElectroniscStatusUtil;
import com.cjit.vms.electronics.service.ElectronicsService;
import com.cjit.vms.electronics.service.RedElectronicsBillInvoiceAuditService;
import com.cjit.vms.electronics.service.RedElectronicsBillService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.RedReceiptTransInfo;
import com.cjit.vms.trans.util.DataUtil;

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
			billInfo.setFapiaoType("2"); 
			// 该查询默认为待审核电子红票
			billInfo.setDataStatus("303");
			if ("menu".equals(strFromViewFlg)) {
				
				fromFlag = null;
				System.out.println("第一次进入...");
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);

			} else {
				System.out.println("第二次进入...");
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

	// 电子【红冲审核】页面[审核通过],[审核拒绝]
		public String redElectronicsReceiptApprove() throws Exception {
			if (!sessionInit(true)) {
				request.setAttribute("message", "用户失效");
				return ERROR;
			}
			String billId = request.getParameter("billId"); 
			System.err.println("billId@  "+billId);
			String[] ids = billId.split(",");
			String result = request.getParameter("result");
			BillInfo bill;
			if (result.equals("307")) {
				
				for (int i = 0; i < ids.length; i++) {
					bill = new BillInfo();
					if (!ids[i].equals("") && ids[i] != null) {
						bill = redElectronicsBillInvoiceAuditService.findElectronicsBillInfo(ids[i]);
						if (bill == null) {
							request.setAttribute("message", "数据错误");
							return ERROR;
						}

						bill.setCancelAuditor(getCurrentUser().getId());
						bill.setDataStatus(ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_307);
						redElectronicsBillInvoiceAuditService.saveElectronicsBillInfo(bill, true);
						/*// 红冲bill状态：[21：红冲审核已通过]
						bill.setDataStatus(DataUtil.BILL_STATUS_21);
						redReceiptApplyInfoService.updateRedBill(bill);*/
					}
				}
				this.message = "审核成功";
				this.request.setAttribute("message", this.message);
			} else {
				String cancelReason = request.getParameter("cancelReason");
				cancelReason = URLDecoder.decode(cancelReason, "utf-8");
				for (int i = 0; i < ids.length; i++) {
					bill = new BillInfo();
					if (!ids[i].equals("") && ids[i] != null) {
						bill = redElectronicsBillInvoiceAuditService.findElectronicsBillInfo(ids[i]);
						if (bill == null) {
							request.setAttribute("message", "数据错误");
							return ERROR;
						}
						bill.setCancelAuditor(getCurrentUser().getId());
						bill.setCancelReason(cancelReason);
						bill.setDataStatus(ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_304);
						redElectronicsBillInvoiceAuditService.saveElectronicsBillInfo(bill, true);
						
					}
				}
				this.message = "成功拒绝申请！";
				this.setResultMessages(message);
				printWriterResult("sucess");
			}

			return SUCCESS;
		}
		
		// 电子【红冲审核】[审核拒绝]
		 public void redElectronicsRefuse() throws Exception{ 
		    	String billId = request.getParameter("billId");
				String[] ids = billId.split(",");
				BillInfo bill;
				
				String cancelReason = request.getParameter("cancelReason");
				cancelReason = URLDecoder.decode(cancelReason, "utf-8");
				System.out.println("billId%%"+billId);
				for (int i = 0; i < ids.length; i++) {
					bill = new BillInfo();
					if (!ids[i].equals("") && ids[i] != null) {
						bill = redElectronicsBillInvoiceAuditService.findElectronicsBillInfo(ids[i]);
						if (bill == null) {
							request.setAttribute("message", "数据错误");
							return;
						}
						bill.setCancelAuditor(getCurrentUser().getId());
						bill.setCancelReason(cancelReason);
						bill.setDataStatus(ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_304);
						redElectronicsBillInvoiceAuditService.saveElectronicsBillInfo(bill, true);
					}
				}
				this.message = "成功拒绝申请！";
				this.setResultMessages(message);
				try {
					printWriterResult("sucess");
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		
		
		/**
		 * cheng 新增  0907
		 * 添加拒绝原因
		 * 
		 */
		public String toElectronicsRedReceiptRefuse() {
			request.setAttribute("billId", request.getParameter("billId")); 
			request.setAttribute("result", request.getParameter("result"));
			
			System.err.println("request.getAttribute();"+request.getAttribute("billId")+   "    @billId@"+request.getParameter("billId"));
			return SUCCESS;
		}
		
		
		
   
	private void printWriterResult(String result) throws Exception {
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
			out.close();
		}
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
