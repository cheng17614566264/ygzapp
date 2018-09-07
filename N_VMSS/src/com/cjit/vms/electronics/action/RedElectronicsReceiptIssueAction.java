package com.cjit.vms.electronics.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.vms.electronics.model.ElectroniscStatusUtil;
import com.cjit.vms.electronics.service.RedElectronicsIssueService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;
import com.cjit.vms.trans.util.DataUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 新建
 * 日期：2018-09-07
 * 作者：刘俊杰
 * 功能：电子发票红票开具页面对应的所有action
 */
public class RedElectronicsReceiptIssueAction extends DataDealAction {
	
	private static final long serialVersionUID = -112620524105020393L;
	private RedElectronicsIssueService redElectronicsIssueService;
	private RedReceiptApplyInfo redReceiptApplyInfo = new RedReceiptApplyInfo();
	
	/**
	 * 新增
	 * 日期：2018-09-07
	 * 作者：刘俊杰
	 * 功能：准备电子红票开具页面数据，查询电子发票红票申请通过的数据
	 */
	public String listElectronicsRedReceiptIssue() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			redReceiptApplyInfo.setDatastatus(ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_307);// 状态：红冲审核通过
			redReceiptApplyInfo.setBillBeginDate(request.getParameter("billBeginDate"));  //开票日期起始
			redReceiptApplyInfo.setBillEndDate(request.getParameter("billEndDate"));  //开票日期结束
			redReceiptApplyInfo.setHissDteBegin(request.getParameter("vtiHissDteBegin"));
			redReceiptApplyInfo.setHissDteEnd(request.getParameter("vtiHissDteEnd"));
			redReceiptApplyInfo.setInsureId(request.getParameter("vtiCherNum")); //保单号
			redReceiptApplyInfo.setTtmprcno(request.getParameter("vtiTtmprcon")); //投保单号
			redReceiptApplyInfo.setOriBillCode(request.getParameter("billCode")); //原发票代码
			redReceiptApplyInfo.setOriBillNo(request.getParameter("billNo"));  //原发票号码
			redReceiptApplyInfo.setCustomerName(request.getParameter("customerName")); //客户名称

			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);  //判断机构
			
			redElectronicsIssueService.findRedReceiptList(redReceiptApplyInfo, paginationList,ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_307);  //获取数据
			this.request.setAttribute("paginationList", paginationList);
			return SUCCESS;
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	/**
	 * 新增
	 * 日期：2018-09-07
	 * 作者：刘俊杰
	 * 功能：红票导出excel
	 */
	public void redElectronicsReceiptBillIssueToExcel() throws IOException, RowsExceededException, WriteException {
		// redReceiptApplyInfo.setDatastatus(DataUtil.BILL_STATUS_3);
		redReceiptApplyInfo.setDatastatus(ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_307); // 红冲审核通过
		List resultsList = redElectronicsIssueService.findRedReceiptList(redReceiptApplyInfo,ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_307);

		StringBuffer fileName = new StringBuffer("红票开具列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeIssueToExcel(os, resultsList);
		os.flush();
		os.close();
	}

	public void writeIssueToExcel(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("红票开具列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "是否手工录入", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "开具类型", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "状态", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
		ws.addCell(header13);
		ws.addCell(header14);
		ws.addCell(header15);
		ws.addCell(header16);
		ws.addCell(header17);

		for (int j = 0; j < 17; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			RedReceiptApplyInfo redReceiptApplyInfo = (RedReceiptApplyInfo) content.get(c);
			int column = count++;
			setWritIssueableSheet(ws, redReceiptApplyInfo, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritIssueableSheet(WritableSheet ws, RedReceiptApplyInfo redReceiptApplyInfo, int column)
			throws WriteException {
		int i = 0;
		Label cell1 = new Label(i++, column, column+"", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, redReceiptApplyInfo.getApplyDate(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, redReceiptApplyInfo.getBillDate(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, redReceiptApplyInfo.getCustomerName(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, redReceiptApplyInfo.getCustomerTaxno(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, redReceiptApplyInfo.getBillCode(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, redReceiptApplyInfo.getBillNo(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, redReceiptApplyInfo.getDrawer(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, redReceiptApplyInfo.getTaxDiskNo(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, redReceiptApplyInfo.getMachineNo(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, redReceiptApplyInfo.getAmtSum().toString(), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, redReceiptApplyInfo.getTaxAmtSum().toString(),
				JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, redReceiptApplyInfo.getSumAmt().toString(), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, DataUtil.getIsHandiworkCH(redReceiptApplyInfo.getIsHandiwork()),
				JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, DataUtil.getIssueTypeCH(redReceiptApplyInfo.getIssueType()),
				JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, DataUtil.getFapiaoTypeCH(redReceiptApplyInfo.getFapiaoType()),
				JXLTool.getContentFormat());
		Label cell17 = new Label(i++, column, redReceiptApplyInfo.getDatastatus(),
				JXLTool.getContentFormat());

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
		ws.addCell(cell10);
		ws.addCell(cell11);
		ws.addCell(cell12);
		ws.addCell(cell13);
		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell17);
	}

	public RedElectronicsIssueService getRedElectronicsIssueService() {
		return redElectronicsIssueService;
	}
	public void setRedElectronicsIssueService(RedElectronicsIssueService redElectronicsIssueService) {
		this.redElectronicsIssueService = redElectronicsIssueService;
	}
}
