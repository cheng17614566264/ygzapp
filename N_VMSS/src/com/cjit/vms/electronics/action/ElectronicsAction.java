package com.cjit.vms.electronics.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.soapencoding.Array;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.electronics.model.ElectroniscStatusUtil;
import com.cjit.vms.electronics.service.ElectronicsService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.action.createBill.CreateBillAction;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;
import com.cjit.ws.common.utils.Utils;

public class ElectronicsAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private static final String TransInfo = null;
	private String message;
	private com.cjit.vms.trans.model.TransInfo transInfo;
	private List transInfoList;
	private BillValidationService billValidationService;
	private ElectronicsService electronicsService;
	/**
	 * 新增
	 * 日期：2018-09-06
	 * 作者：刘俊杰
	 * 说明：新增电子发票开具流程
	 */
	private CreateBillAction createBillAction;
	//end 2018-09-06
	/**
	 * 电子发票展示
	 * 
	 * @return
	 */
	public String findElectronicsList() {
		// 电子发票
		String msg = this.request.getParameter("msg");
		if (msg != null && !"".equals(msg)) {
			try {
				this.request.setAttribute("resultMessages", new String(
						new BASE64Decoder().decodeBuffer(msg)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if ("menu".equals(fromFlag)) {
				transInfo = new com.cjit.vms.trans.model.TransInfo();
				// 区分电子发票
				//TODO 遗留问题:核心推送的数据，团险发票类型为2，但个险的发票类型不确定
				transInfo.setFapiaoType("2");
				/**
				 * 新增
				 * 日期：2018-09-05
				 * 作者：刘俊杰
				 * 说明：电票手动开具页面，默认显示自动开具失败的信息
				 */
				transInfo.setDataStatus(ElectroniscStatusUtil.ELECTRONICS_TRANS_STATUS_201);
				//end 2018-09-05
				this.transInfo.setBillBeginDate(null);
				this.transInfo.setBillEndDate(null);
				this.request.getSession().setAttribute("curPage",
						new Integer(1));
				this.request.getSession().setAttribute("pageSize",
						new Integer(20));
				fromFlag = null;
			} else {
				transInfo = new com.cjit.vms.trans.model.TransInfo();
				// 区分电子发票
				transInfo.setFapiaoType("2");

				this.transInfo.setBatchNo(this.request.getParameter(
						"transInfo.batchNo").trim());

				this.transInfo.setCherNum(this.request.getParameter(
						"transInfo.cherNum").trim());

				this.transInfo.setTransBeginDate(this.request.getParameter(
						"transInfo.transBeginDate").trim());

				this.transInfo.setTransEndDate(this.request.getParameter(
						"transInfo.transEndDate").trim());
				this.transInfo.setCustomerName(this.request.getParameter(
						"transInfo.customerName").trim());

				this.transInfo.setDataStatus(this.request.getParameter(
						"transInfo.dataStatus").trim());

				this.transInfo.setInstCode(this.request.getParameter(
						"transInfo.instCode").trim());

			}
			User currentUser = this.getCurrentUser();
			transInfo.setUserId(currentUser.getId());

			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);

			transInfoList = electronicsService.findElectronicsList(transInfo,
					paginationList);

			this.request.setAttribute("paginationList", paginationList);

			String taxName = electronicsService.findTaxValuebyName("税控参数");

			String instId = this.getCurrentUser().getOrgId();
			// 根据登陆机构获取所有下级机构
			List instcodes = electronicsService
					.findInstCodeByUserInstId(instId);
			this.request.setAttribute("instcodes", instcodes);

			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);

			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));

			logManagerService.writeLog(request, currentUser, "00998", "电子发票开具",
					"销项税管理", "查询可供进行开具处理的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00998",
					"电子发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "0");
			log.error("ElectronicsAction-findElectronicsList", e);
		}
		return ERROR;
	}

	/**
	 * 开票失败原因
	 * 
	 * @return
	 */
	public String billElectronicsCancelReason() {
		try {
			String transId = request.getParameter("transId");
			System.out.println(transId);
			String fapiaoType = request.getParameter("fapiaoType");
			System.out.println(fapiaoType);
			TransInfo transInfos = new TransInfo();
			transInfos.setTransId(transId);
			transInfos.setFapiaoType(fapiaoType);
			TransInfo transInfo = electronicsService
					.findTransInfoByTransId(transInfos);
			System.out.println(transInfo.getFapiaoDate());
			System.out.println(transInfo.getCustomerName());
			System.out.println(transInfo.getCustomerTaxno());
			System.out.println(transInfo.getCancelReason());

			request.setAttribute("transInfo", transInfo.getCancelReason());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ERROR;
	}

	/**
	 * 电子发票导出
	 */
	public void exportElectronics() {
		try {
			User currentUser = this.getCurrentUser();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			List<com.cjit.vms.trans.model.TransInfo> list = new ArrayList<com.cjit.vms.trans.model.TransInfo>();
			String[] split = request.getParameter("number").split("_");
			for (int i = 0; i < split.length; i++) {
				String[] split2 = split[i].split("-");
				com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
				transInfo.setLstAuthInstId(lstAuthInstId);
				transInfo.setUserId(currentUser.getId());
				transInfo.setCherNum(split2[0].trim());
				transInfo.setTtmpRcno(split2[1].trim());
				list.add(transInfo);
			}
			List<com.cjit.vms.trans.model.TransInfo> transInfoList = new ArrayList<com.cjit.vms.trans.model.TransInfo>();
			for (int i = 0; i < list.size(); i++) {
				List findElectronics = electronicsService
						.findElectronicsList(list.get(i));
				transInfoList
						.add((com.cjit.vms.trans.model.TransInfo) findElectronics
								.get(0));
			}

			StringBuffer fileName = new StringBuffer("电子发票信息表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			electronicsWriteToExcel(os, transInfoList);
			os.flush();
			os.close();

			logManagerService.writeLog(request, this.getCurrentUser(), "00998",
					"电子发票开具", "销项税管理", "导出发票信息", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00998",
					"电子发票开具", "销项税管理", "导出发票信息", "0");
			log.error("BillIssueAction-exportIssueBill", e);
		}
	}

	/**
	 * 电子发票信息导出Excel
	 * 
	 * @param os
	 *            输出流
	 * @param content
	 *            内容List
	 * @throws Exception
	 */
	private void electronicsWriteToExcel(OutputStream os, List content)
			throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("发票信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "录单机构", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "投保单号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "保单号", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "批单号", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "交易日期", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "交易类型", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "客户名称", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "交易金额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "税率", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "未开票金额", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "累积未开票金额", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "交易状态", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "承保日期", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "开具失败原因", JXLTool.getHeader());

		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
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

		for (int j = 0; j < 16; j++) {
			ws.setColumnView(j, 16);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			com.cjit.vms.trans.model.TransInfo transBill = (com.cjit.vms.trans.model.TransInfo) content
					.get(c);
			int column = count++;
			electronicsSetWritableSheet(ws, transBill, column);
		}
		wb.write();
		wb.close();

	}

	/**
	 * 回填参内容
	 * 
	 * @param ws
	 *            表格
	 * @param bill
	 *            导出的数据
	 * @param column
	 *            行
	 * @throws Exception
	 */
	private void electronicsSetWritableSheet(WritableSheet ws,
			com.cjit.vms.trans.model.TransInfo bill, int column)
			throws Exception {
		int i = 0;
		Label cell1 = new Label(i++, column, column + "",
				JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, bill.getInstCode(),
				JXLTool.getContentFormat());
		// 投保单号
		Label cell3 = new Label(i++, column, bill.getTtmpRcno(),
				JXLTool.getContentFormat());
		// 保单号
		Label cell4 = new Label(i++, column, bill.getCherNum(),
				JXLTool.getContentFormat());
		// 批单号
		Label cell5 = new Label(i++, column, bill.getBatchNo(),
				JXLTool.getContentFormat());

		// 交易日期
		Label cell6 = new Label(i++, column, bill.getTransDate(),
				JXLTool.getContentFormat());
		// 交易类型
		Label cell7 = new Label(i++, column, bill.getTransTypeName(),
				JXLTool.getContentFormat());
		// 客户名称
		Label cell8 = new Label(i++, column, bill.getCustomerName(),
				JXLTool.getContentFormat());
		// 交易金额
		Label cell9 = new Label(i++, column, NumberUtils.format(bill.getAmt(),
				"", 2), JXLTool.getContentFormat());
		// 税率
		Label cell10 = new Label(i++, column, NumberUtils.format(
				bill.getTaxRate(), "", 2), JXLTool.getContentFormat());
		// 税额
		Label cell11 = new Label(i++, column, NumberUtils.format(
				bill.getTaxAmt(), "", 2), JXLTool.getContentFormat());
		// 价税合计
		Label cell12 = new Label(i++, column, NumberUtils.format(
				bill.getReverseAmt(), "", 2), JXLTool.getContentFormat());
		// 未开票金额
		Label cell13 = new Label(i++, column, NumberUtils.format(
				bill.getBalance(), "", 2), JXLTool.getContentFormat());
		// 累积未开票金额
		Label cell14 = new Label(i++, column, NumberUtils.format(
				bill.getWkze(), "", 2), JXLTool.getContentFormat());
		// 交易状态
		Label cell15 = new Label(i++, column, DataUtil.getDataStatusCH(
				bill.getDataStatus(), "TRANS"), JXLTool.getContentFormat());
		// 承保日期
		Label cell16 = new Label(i++, column, bill.getHissDte(),
				JXLTool.getContentFormat());

		Label cell17 = new Label(i++, column, bill.getCancelReason(),
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

	/**
	 * 手动电子发票开具
	 * @return
	 * @throws Exception
	 */
	public void transToElectronicsBill() throws Exception {
		AjaxReturn ajax = new AjaxReturn();
		List<TransInfo> list = new ArrayList<TransInfo>();
		StringBuffer sbMessage = new StringBuffer();
		int transSuccess = 0;
		String transIdss = "";
		try {
			// 获取transid和发票状态
			String fapiaoType = request.getParameter("fapiaoType");
			String[] transIds = request.getParameter("transIds").split("/");

			if (transIds != null && transIds.length > 0) {
				for (int i = 0; i < transIds.length; i++) {
					
					/**
					 * 新增
					 * 日期：2018-09-06
					 * 作者：刘俊杰
					 * 说明：执行电子发票开具
					 */
					//从vms_trans_info表中查询出此交易的交易信息
					Map map = new HashMap();
					if(transIds[i] != "") {
						map.put("transId", transIds[i]);
						List<TransInfoTemp> transinfoList = electronicsService.selectTransInfoOfElectronicsReuse(map);
						TransInfoTemp temp = transinfoList.get(0);
						createBillAction.getTransInfoForINSCOD(temp.getCHERNUM(),temp.getCUSTOMER_ID(),false);
						//开具电子发票
						createBillAction.batchRunTimeOfElectron(Utils.dfxj1001);
						do {
							Thread.sleep(1000);
						}while(createBillAction.getResult() == null);
						String resultOfMake = createBillAction.getResult();
						sbMessage.append(transIds[i]+resultOfMake);  //开具结果
					}
					
					/*TransInfo searPar = new TransInfo();
					searPar.setTransId(transIds[i]);
					searPar.setFapiaoType(fapiaoType);
					searPar = electronicsService
							.findTransInfoByTransId(searPar);
					if (searPar != null) {
						searPar.setSelectTransIds(transIds);
					}
					// 2018需要进行重写校验
					// Integer count =
					// billValidationService.checkingTransByCherNum(searPar,
					// false).size();

					StringBuffer sb = new StringBuffer();
					transIdss += transIds[i];
					sb.append("投保单号：【");
					sb.append((searPar.getTtmpRcno() != null
							&& !"".equals(searPar.getTtmpRcno()) ? searPar
							.getTtmpRcno() : "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null
							&& !"".equals(searPar.getCherNum()) ? searPar
							.getCherNum() : "         "));
					sb.append("】\n");
					sb.append("险种名称：【");
					sb.append(searPar.getInsNam() != null
							&& !"".equals(searPar.getInsNam()) ? searPar
							.getInsNam() : "         ");
					sb.append("】\n");
					searPar.setRemark(sb.toString());
					transIdss += transIds[i];
					transSuccess++;
					list.add(searPar);*/
				}
				/*// 校验
				CheckResult result = (CheckResult) billValidationService
						.shortCircuitValidation(list);
				System.out.println(result.getCheckFlag());
				if (CheckResult.CHECK_FAIL.equals(result.getCheckFlag())) {
					sbMessage = sbMessage.append(result.getCheckResultMsg());
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					// // 构建票据 并保存
					List<BillInfo> eleConstructBill = electronicsService
							.eleConstructBillAndSaveAsMerge(list);

					System.err.println(eleConstructBill.size());

					String billIds[] = new String[eleConstructBill.size()];
					for (int i = 0; i < transIds.length; i++) {
						System.out.println(eleConstructBill.get(i).getBillId());
						billIds[i] = eleConstructBill.get(i).getBillId();
					}
					map.put("billIds", billIds);
					ajax.setAttributes(map);

					ajax.setMessage("初始化票据信息成功！");

				}*/
			}
			if (sbMessage != null && sbMessage.toString().length() > 0) {
				ajax.setMessage(sbMessage.toString());
			}

			/*logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIdss.substring(0, transIdss.length() - 1)
							+ ")的交易开票 成功" + transSuccess + "笔", "1");*/
			returnResult(ajax);
		} catch (Exception e) {
			/*logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIdss.substring(0, transIdss.length() - 1)
							+ ")的交易开票", "0");*/
			log.error("TransInfoAction-transToEachBill", e);
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * 新增
	 * 日期：2018-09-06
	 * 作者：刘俊杰
	 * 功能：电票红冲-改变状态,生成红色票据,流向电票红冲页面
	 */
	public void listElectroniceRedToBill() {
		AjaxReturn ajax = new AjaxReturn();
		StringBuffer sbMessage = new StringBuffer();
		Map map = new HashMap();
		try {
			String fapiaoType = request.getParameter("fapiaoType");
			String[] transIds = request.getParameter("transIds").split("/");
			if (transIds != null && transIds.length > 0) {
				for (int i = 0; i < transIds.length; i++) {
					String transId = transIds[i];
					if(transId != "") {
						//改变状态为ELECTRONICS_REDBILL_STATUS_302-未开具红票
						map.put("transId", transId);
						map.put("dataStatus", ElectroniscStatusUtil.ELECTRONICS_REDBILL_STATUS_302);
						electronicsService.updateElectronicsTransRedStatusOfNotMake(map);
						//生成红色票据
						sbMessage.append("修改成功");
					}
				}
			}
			if (sbMessage != null && sbMessage.toString().length() > 0) {
				ajax.setMessage(sbMessage.toString());
			}
			returnResult(ajax);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public com.cjit.vms.trans.model.TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(com.cjit.vms.trans.model.TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public ElectronicsService getElectronicsService() {
		return electronicsService;
	}

	public void setElectronicsService(ElectronicsService electronicsService) {
		this.electronicsService = electronicsService;
	}

	public BillValidationService getBillValidationService() {
		return billValidationService;
	}

	public void setBillValidationService(
			BillValidationService billValidationService) {
		this.billValidationService = billValidationService;
	}

	public static String getTransinfo() {
		return TransInfo;
	}

	public CreateBillAction getCreateBillAction() {
		return createBillAction;
	}

	public void setCreateBillAction(CreateBillAction createBillAction) {
		this.createBillAction = createBillAction;
	}
	

}