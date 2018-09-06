package com.cjit.vms.electronics.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.electronics.service.ElectronicsService;
import com.cjit.vms.electronics.service.RedElectronicsBillService;
import com.cjit.vms.taxdisk.servlet.model.parseXml.Bill;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.RedReceiptTransInfo;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.webService.client.InvoicePrintServiceServiceStub;
import com.cjit.webService.client.SubmitData;
import com.cjit.webService.client.SubmitDataResponse;
import com.cjit.webService.client.Util.WebServiceUtil;
import com.cjit.webService.client.entity.BillEntity;
import com.cjit.webService.client.entity.HeXinCallBack;

public class RedElectronicsBillReceiptAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private RedElectronicsBillService redElectronicsBillService;
	private ElectronicsService electronicsService;
	private BillInfo billInfo;
	private List billInfoList;
	private List transInfoList;
	private String taxParam;// 税控参数
	private String message;

	/**
	 * @return 【红冲申请】页面数据查询
	 */
	public String listElectroniceRedBillSelect() {
		try {
			if ("menu".equals(fromFlag)) {
				billInfo = new BillInfo();
				this.billInfo.setApplyBeginDate(null);
				this.billInfo.setApplyEndDate(null);
				this.request.getSession().setAttribute("curPage",
						new Integer(1));
				this.request.getSession().setAttribute("pageSize",
						new Integer(20));
				// 区分电子发票
				billInfo.setFapiaoType("2");
				// 默认第一次进入状态为未开具红票
				billInfo.setDataStatus("302");
				fromFlag = null;
				System.out.println("第一次进入...");

			} else {
				System.out.println("第二次进入...");
				billInfo = new BillInfo();
				// 区分电子发票
				billInfo.setFapiaoType("2");
				this.billInfo.setCustomerName(this.request.getParameter(
						"billInfo.customerName").trim());
				System.out.println(this.request
						.getParameter("billInfo.customerName")
						+ ",customerName");// 啥也没有

				this.billInfo.setBillNo(this.request.getParameter(
						"billInfo.billNo").trim());
				System.out.println(this.request.getParameter("billInfo.billNo")
						+ ",billNo");// 一个1

				this.billInfo.setBillCode(this.request.getParameter(
						"billInfo.billCode").trim());
				System.out.println(this.request
						.getParameter("billInfo.billCode") + ",billCode");// 一个1

				this.billInfo.setCherNum(this.request.getParameter(
						"billInfo.cherNum").trim());
				System.out.println(this.request
						.getParameter("billInfo.cherNum") + ",cherNum");// 一个1

				this.billInfo.setApplyBeginDate(this.request.getParameter(
						"billInfo.applyBeginDate").trim());
				System.out.println(this.request
						.getParameter("billInfo.applyBeginDate")
						+ ",applyBeginDate");// 啥也没有

				this.billInfo.setApplyEndDate(this.request.getParameter(
						"billInfo.applyEndDate").trim());
				System.out.println(this.request
						.getParameter("billInfo.applyEndDate")
						+ ",applyEndDate");// 啥也没有
				// 批单号
				this.billInfo.setBatchNo(this.request.getParameter(
						"billInfo.batchNo").trim());
				System.out.println(this.request
						.getParameter("billInfo.batchNo") + ",batchNo");// 啥也没有

				this.billInfo.setInstCode(this.request.getParameter(
						"billInfo.instCode").trim());
				System.out.println(this.request
						.getParameter("billInfo.instCode") + ",instCode");// 一个1

				this.billInfo.setDataStatus(this.request.getParameter(
						"billInfo.dataStatus").trim());
				System.out.println(this.request
						.getParameter("billInfo.dataStatus") + ",dataStatus");// 啥也没有

				this.billInfo.setFapiaoType(this.request.getParameter(
						"billInfo.fapiaoType").trim());
				System.out.println(this.request
						.getParameter("billInfo.fapiaoType") + ",fapiaoType");// 一个1

				this.billInfo.setBillDate(this.request.getParameter(
						"billInfo.billDate").trim());
				System.out.println(this.request
						.getParameter("billInfo.billDate") + ",billDate");// 一个1
				System.out.println("--------------------");
			}
			User currentUser = this.getCurrentUser();
			billInfo.setUserId(currentUser.getId());

			//cheng 0906 如果是第二次进入 则将datastatus设置为 默认值 '302' 电票 
			   // 如果为查询则设为空 
			    
			if (!"chaxun".equals(fromFlag) && StringUtil.isEmpty(billInfo.getDataStatus())){
				// 设置查询状态
				billInfo.setDataStatus("302"); //第二次进入查本页面可以展示的全部状态 待增加 TODO cheng 0906 
				   // 区分电子发票 
				   billInfo.setFapiaoType("2"); // cheng 修 0906 改发票类型为 "2" 电票 
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfo.setSumAmtBegin(new BigDecimal(0));
			// 计数新增
			paginationList.setShowCount("true");
			redElectronicsBillService.findRedElectronicsList(billInfo,
					paginationList);
			this.request.setAttribute("paginationList", paginationList);
			// 根据登陆机构获取所有下级机构
			String instId = this.getCurrentUser().getOrgId();
			List instcodes = electronicsService
					.findInstCodeByUserInstId(instId);
			this.request.setAttribute("instcodes", instcodes);

			taxParam = electronicsService.findTaxValuebyName("税控参数");
			// 查询空白发票作废数量
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);

			if (this.billInfo.getApplyBeginDate() != null) {
				this.request.getSession().setAttribute("applyBeginDate",
						this.billInfo.getApplyBeginDate());
			}
			if (this.billInfo.getApplyEndDate() != null) {
				this.request.getSession().setAttribute("applyEndDate",
						this.billInfo.getApplyEndDate());
			}
			if (this.billInfo.getCustomerName() != null) {
				this.request.getSession().setAttribute("customerName",
						this.billInfo.getCustomerName());
			}
			if (this.billInfo.getCustomerTaxno() != null) {
				this.request.getSession().setAttribute("customerTaxno",
						this.billInfo.getCustomerTaxno());
			}
			if (this.billInfo.getIsHandiwork() != null) {
				this.request.getSession().setAttribute("isHandiwork",
						this.billInfo.getIsHandiwork());
			}
			if (this.billInfo.getDataStatus() != null) {
				this.request.getSession().setAttribute("dataStatus",
						this.billInfo.getDataStatus());
			}
			if (this.billInfo.getFapiaoType() != null) {
				this.request.getSession().setAttribute("fapiaoType",
						this.billInfo.getFapiaoType());
			}
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));

			logManagerService.writeLog(request, currentUser, "0016", "发票开具",
					"销项税管理", "查询可供进行开具处理的票据信息列表", "1");
			System.out.println("tax");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0016",
					"发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "0");
			log.error("BillIssueAction-listIssueBill", e);
		}
		return ERROR;
	}

	/**
	 * 蓝票红冲申请
	 * 
	 * @throws Exception
	 */

	public void redEclectronicsbillRedCancel() throws Exception {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			returnResult(new AjaxReturn(false));
		}
		String billids = this.request.getParameter("billIds");
		System.out.println(billids);
		System.out.println(billids);
		if (null != billids && !"".equals(billids)) {
			String[] billidArray = billids.split("/");
			JSONArray validateResult = redElectronicsBillService
					.validate(billidArray);

			if (validateResult.size() < 1) {
				if (null != billidArray) {
					for (String billid : billidArray) {
						try {
							createRedBill(billid);
						} catch (Exception e) {
							this.message = billid + "failed";
							e.printStackTrace();
							break;
						}
						this.message = "sucess";
					}
				}
			} else {
				try {
					List<BillInfo> billList = null; 
					   billList = redElectronicsBillService.findBillInfo(billidArray); 
					   if (billList == null) { 
					   request.setAttribute("message", "数据错误"); 
					   return; 
					   } 
					   for(int i = 0 ; i < billList.size() ; i++) { 
					   billList.get(i).setDataStatus("303"); //电票红冲后 
					   redElectronicsBillService.updateRedBill(billList.get(i)); 
					   } 
					this.message = java.net.URLEncoder.encode(
							validateResult.toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		returnResult(new AjaxReturn(true, "红冲申请成功！"));
	}
	
	/** 
	   * 发票红冲 
	   * cheng 0906 引入发票红冲 
	   * @return 
	   */ 
	   public String redReceiptReleaseTrans() { 
	      String result = request.getParameter("result"); 
	   String billId = request.getParameter("billId"); 
	   String transId; 
	   BillInfo bill = null; 
	   bill = billInfoService.findBillInfo1(billId); 
	   if (bill == null) { 
	   request.setAttribute("message", "数据错误"); 
	   return ERROR; 
	   } 
	   String notic = request.getParameter("notic"); 
	   bill.setNoticeNo(notic); 
	   bill.setDataStatus(DataUtil.BILL_STATUS_18); 
	   billInfoService.saveBillInfo1(bill, true); 
	   bill.setDataStatus(DataUtil.BILL_STATUS_26); 
	   billInfoService.updateRedBill(bill); 
	   return SUCCESS; 
	   } 

	private void createRedBill(String billId) {
		// 查询待开具的bill
		BillInfo bill = redElectronicsBillService.findBillInfoByBillId(billId);

		List<TransBillInfo> list = redElectronicsBillService
				.finTransBillByBillId(billId);

		transBillMergeOrSplit_New3(billId, list);// added 20170110

		// 获取所有红冲后的新bill
		List redBill = redElectronicsBillService.findRedBillByOriBillId(billId);

		BillInfo redBillInfo;
		// 合计金额净额
		BigDecimal redAmtSum = new BigDecimal("0");// 净价
		BigDecimal redSumAmt = new BigDecimal("0");// 价税合计

		for (int i = 0; i < redBill.size(); i++) {
			redBillInfo = (BillInfo) redBill.get(i);
			redAmtSum = redAmtSum.add(redBillInfo.getAmtSum());
			redSumAmt = redSumAmt.add(redBillInfo.getSumAmt());
		}
		bill.setBalance(bill.getBalance() == null ? bill.getSumAmt().add(
				redSumAmt) : bill.getBalance().add(redSumAmt));
		bill.setOperateStatus(bill.getDataStatus());
		// 原billinfo状态：[16：红冲待审核]
		bill.setDataStatus(DataUtil.BILL_STATUS_16);
		bill.setCancelInitiator(getCurrentUser().getId());
		bill.setCancelReason(null);
		// 更新原billInfo
		redElectronicsBillService.saveBillInfo(bill, true);

	}

	private void transBillMergeOrSplit_New3(String oldBillId,
			List<TransBillInfo> transBillList) {
		if (transBillList != null && transBillList.size() > 0) {
			// 生成新billId
			String strBillId = createBillId("B");

			BillItemInfo billItem = new BillItemInfo();

			billItem.setBillId(oldBillId);
			List<BillItemInfo> billItemInfolist = redElectronicsBillService
					.findBillItemInfoList(billItem);

			Map<String, List<TransBillInfo>> map = groupTransBillByItemId(transBillList);
			for (Object itemId : map.keySet()) {
				String strBillItemId = createBusinessId("BI");// 生成新billItemId
				String oldBillItemId = map.get(itemId).get(0).getBillItemId();

				create_TransBill(strBillId, strBillItemId, map.get(itemId));
				for (int i = 0; i < billItemInfolist.size(); i++) {
					if (oldBillItemId.equals(billItemInfolist.get(i)
							.getBillItemId())) {
						create_BillItem(strBillId, strBillItemId,
								billItemInfolist.get(i));
						billItemInfolist.remove(i);
						break;
					}
				}
			}
			createAndSaveRedBill(strBillId, oldBillId);
		} else {
			String strBillId = createBillId("B");
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(oldBillId);
			List<BillItemInfo> billItemInfolist = redElectronicsBillService
					.findBillItemInfoList(billItem);
			for (int i = 0; i < billItemInfolist.size(); i++) {
				String strBillItemId = createBusinessId("BI");// 生成新billItemId
				create_BillItem(strBillId, strBillItemId,
						billItemInfolist.get(i));
			}

			createAndSaveRedBill(strBillId, oldBillId);
		}
	}

	/**
	 * 按billItemId分组
	 * 
	 * @param transBillList
	 * @return
	 */
	private Map<String, List<TransBillInfo>> groupTransBillByItemId(
			List<TransBillInfo> transBillList) {
		Map<String, List<TransBillInfo>> result = new HashMap<String, List<TransBillInfo>>();
		for (TransBillInfo tb : transBillList) {
			if (!result.containsKey(tb.getBillItemId())) {
				List<TransBillInfo> temp = new ArrayList<TransBillInfo>();
				temp.add(tb);
				result.put(tb.getBillItemId(), temp);
			} else {
				result.get(tb.getBillItemId()).add(tb);
			}
		}
		return result;
	}

	private void create_TransBill(String strBillId, String strBillItemId,
			List<TransBillInfo> transList) {
		for (int i = 0; i < transList.size(); i++) {
			TransBillInfo transBill = transList.get(i);

			// 插入交易票据对应信息
			if (!"".equals(transBill.getTransId())
					&& transBill.getTransId() != null) {
				transInfoService.saveTransBill(transBill.getTransId(),
						strBillId, strBillItemId,
						new BigDecimal(0).subtract(transBill.getAmtCny()),
						new BigDecimal(0).subtract(transBill.getTaxAmtCny()),
						new BigDecimal(0).subtract(transBill.getIncomeCny()));
			}
		}
	}

	private void create_BillItem(String strBillId, String strBillItemId,
			BillItemInfo billItem) {
		billItem.setBillId(strBillId);
		billItem.setBillItemId(strBillItemId);
		billItem.setAmt(new BigDecimal(0).subtract(billItem.getAmt()));
		billItem.setTaxAmt(new BigDecimal(0).subtract(billItem.getTaxAmt()));
		billItem.setGoodsNo(new BigDecimal(0).subtract(billItem.getGoodsNo()));
		billItem.setTaxFlag("N");
		// 保存新生成billItem
		redElectronicsBillService.saveBillItemInfo(billItem, false);
	}

	private void createAndSaveRedBill(String newBilled, String oldBillId) {
		BillInfo billInfo = (BillInfo) (billInfoService.findBillInfo(oldBillId));
		billInfo.setBillId(newBilled);
		billInfo.setAmtSum(new BigDecimal(0).subtract(billInfo.getAmtSum()));
		billInfo.setBalance(new BigDecimal(0).subtract(billInfo.getSumAmt()));// 对整张发票红冲
		billInfo.setTaxAmtSum(new BigDecimal(0).subtract(billInfo
				.getTaxAmtSum()));
		billInfo.setSumAmt(new BigDecimal(0).subtract(billInfo.getSumAmt()));
		billInfo.setDataStatus(DataUtil.BILL_STATUS_20);// 红冲待审核
		billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_2);// 人工审核
		billInfo.setOriBillCode(billInfo.getBillCode());
		billInfo.setOriBillNo(billInfo.getBillNo());
		billInfo.setBillCode("");
		billInfo.setBillNo("");
		billInfo.setBillDate(billInfo.getHissDte());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
		// 保存bill
		billInfoService.saveBillInfo(billInfo, false);
	}

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	/**
	 * 查询交易
	 * 
	 * @return
	 */

	public String listElectronicsBillTransInfo() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);
				fromFlag = null;
			}
			String billId = (String) this.request.getParameter("billId");
			request.setAttribute("billId", billId);
			paginationList.setShowCount("true");
			transInfoList = redElectronicsBillService
					.findTransByElectronicsBillId(billId, paginationList);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("transInfoList", transInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoDetailAction-listBillTransInfo", e);
		}
		return ERROR;
	}

	// 【交易发票关联】页面数据查询
	// 根据勾选红票对应交易的保单号匹配该保单所有已开具蓝票，进行页面展示
	public String billElectronicsInfoAndTransList() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		this.billInfo = new BillInfo();
		// 获取负数发票的保单号
		String cherNum = request.getParameter("cherNum");
		if (cherNum == null || "".equals(cherNum)) {
			cherNum = "000";
		}
		System.out.println(request.getParameter("fromFlag"));
		if ("menu".equals(fromFlag)) {
			this.request.getSession().setAttribute("curPage", new Integer(1));
			this.request.getSession().setAttribute("pageSize", new Integer(20));
			fromFlag = null;
		} else {
			// 条件查询
			this.billInfo.setBillNo(this.request
					.getParameter("billInfo.billNo").trim());
			System.out.println(this.request.getParameter("billInfo.billNo")
					.trim());

			this.billInfo.setBillCode(this.request.getParameter(
					"billInfo.billCode").trim());
			System.out.println(this.request.getParameter("billInfo.billCode")
					.trim());

			this.billInfo.setApplyBeginDate(this.request.getParameter(
					"billInfo.applyBeginDate").trim());
			System.out.println(this.request.getParameter(
					"billInfo.applyBeginDate").trim());

			this.billInfo.setApplyEndDate(this.request.getParameter(
					"billInfo.applyEndDate").trim());
			System.out.println(this.request.getParameter(
					"billInfo.applyEndDate").trim());

			this.billInfo.setInstCode(this.request.getParameter(
					"billInfo.instCode").trim());
			System.out.println(this.request.getParameter("billInfo.instCode")
					.trim());

			this.billInfo.setDataStatus(this.request.getParameter(
					"billInfo.dataStatus").trim());
			System.out.println(this.request.getParameter("billInfo.dataStatus")
					.trim());

			this.billInfo.setBillDate(this.request.getParameter(
					"billInfo.billDate").trim());
			System.out.println(this.request.getParameter("billInfo.billDate")
					.trim());
		}

		// 区分电子票
		this.billInfo.setFapiaoType("1");
		System.out.println(cherNum);
		this.billInfo.setCherNum(cherNum);
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		this.billInfo.setLstAuthInstId(lstAuthInstId);
		this.billInfo.setUserId(this.getCurrentUser().getId());

		// 查找相关联蓝票子票
		redElectronicsBillService.findRedElectronicsList(billInfo,
				paginationList);

		String instId = this.getCurrentUser().getOrgId();
		List instcodes = electronicsService.findInstCodeByUserInstId(instId);

		this.request.setAttribute("instcodes", instcodes);
		this.request.setAttribute("paginationList", paginationList);
		this.request.setAttribute("cherNum", cherNum);

		if (this.billInfo.getDataStatus() != null) {
			this.request.getSession().setAttribute("billInfo.dataStatus",
					this.billInfo.getDataStatus());
		}

		if (this.billInfo.getInstCode() != null) {
			this.request.getSession().setAttribute("billInfo.instCode",
					this.billInfo.getInstCode());
		}

		return SUCCESS;
	}

	/**
	 * 红票关联蓝票
	 */
	public String redBillrelatblueBill() {
		
		
		
		
		
		
		
		

		return SUCCESS;
	}

	/**
	 * 导出按钮
	 * 
	 * @throws Exception
	 */
	public void redElectronicsReceiptBillToExcel() throws Exception {
		System.out.println("11");
		User currentUser = this.getCurrentUser();
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		billInfo.setLstAuthInstId(lstAuthInstId);
		System.out.println(request.getParameter("billId"));
		String[] billIds = request.getParameter("billId").split("/");
		List<BillInfo> billInfosList = new ArrayList<BillInfo>();
		for (int i = 0; i < billIds.length; i++) {
			String[] split2 = billIds[i].split("-");
			BillInfo billInfo = new BillInfo();
			billInfo.setLstAuthInstId(lstAuthInstId);
			System.out.println(billIds[i]);
			billInfo.setBillCode(split2[0].trim() == ""
					|| split2[0].trim() == null ? null : split2[0].trim());
			System.out.println(split2[0].trim());
			billInfo.setBillNo(split2[1].trim() == ""
					|| split2[1].trim() == null ? null : split2[1].trim());
			System.out.println(split2[1].trim());
			billInfo.setBillId(split2[2].trim() == ""
					|| split2[2].trim() == null ? null : split2[2].trim());
			System.out.println(split2[2].trim());
			billInfosList.add(billInfo);
		}
		System.out.println(billInfosList.size());
		List<BillInfo> billsList = new ArrayList<BillInfo>();
		for (int i = 0; i < billInfosList.size(); i++) {
			billInfoList = redElectronicsBillService
					.findRedElectronicsList(billInfosList.get(i));
			billsList.add((BillInfo) billInfoList.get(0));
		}
		System.out.println(billsList.size());
		System.out.println("-----");

		OutputStream os = response.getOutputStream();

		StringBuffer fileName = new StringBuffer("红冲申请信息表");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		redReceiptApproveToExcel1(os, billsList);

		os.flush();
		os.close();

	}

	public void redReceiptApproveToExcel1(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);

		WritableSheet ws = null;
		ws = wb.createSheet("红冲申请信息表", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "开票机构", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "录单机构", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "客户名称", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "保单号", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "批单号", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "发票代码", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "发票号码", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "发票类型", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "	状态", JXLTool.getHeader());

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
		for (int i = 0; i < 15; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillInfo billInfo = (BillInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			map.put("applyBillDate", billInfo.getApplyDate());
			map.put("billDate", billInfo.getBillDate());
			map.put("instCode", billInfo.getInstCode());
			map.put("instFrom", billInfo.getInstFrom());
			map.put("customerName", billInfo.getCustomerName());
			map.put("cherNum", billInfo.getCherNum());
			map.put("batchNo", billInfo.getBatchNo());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("amtSum", billInfo.getAmtSum());
			map.put("taxAmtSum", billInfo.getTaxAmtSum());
			map.put("sumAmt", billInfo.getSumAmt());
			map.put("fapiaoType", billInfo.getFapiaoType());
			map.put("dataStatus", billInfo.getDataStatus());
			setWritableSheetForRedReceiptApprove(ws, map, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheetForRedReceiptApprove(WritableSheet ws, Map o,
			int column) throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		// Id
		Label cell1 = new Label(0, column, o.get("Id").toString(),
				tempCellFormat);

		// applyBillDate
		Label cell2 = new Label(1, column, o.get("applyBillDate") == null
				|| o.get("applyBillDate").equals("") ? "" : o.get(
				"applyBillDate").toString(), tempCellFormat);
		// billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		// instCode
		Label cell4 = new Label(3, column, o.get("instCode") == null
				|| o.get("instCode").equals("") ? "" : o.get("instCode")
				.toString(), tempCellFormat);
		// instFrom
		Label cell5 = new Label(4, column, o.get("instFrom") == null
				|| o.get("instFrom").equals("") ? "" : o.get("instFrom")
				.toString(), tempCellFormat);
		// customerName
		Label cell6 = new Label(5, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o
				.get("customerName").toString(), tempCellFormat);

		// cherNum
		Label cell7 = new Label(6, column, o.get("cherNum") == null
				|| o.get("cherNum").equals("") ? "" : o.get("cherNum")
				.toString(), tempCellFormat);
		// batchNo
		Label cell8 = new Label(7, column, o.get("batchNo") == null
				|| o.get("batchNo").equals("") ? "" : o.get("batchNo")
				.toString(), tempCellFormat);

		// billCode
		Label cell9 = new Label(8, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		// billNo
		Label cell10 = new Label(9, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), tempCellFormat);

		// amtSum
		Label cell11 = new Label(10, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o
						.get("amtSum").toString(), tempCellFormat);
		// taxAmtSum
		Label cell12 = new Label(11, column, o.get("taxAmtSum") == null
				|| o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum")
				.toString(), tempCellFormat);
		// sumAmt
		Label cell13 = new Label(12, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o
						.get("sumAmt").toString(), tempCellFormat);

		// fapiaoType
		String fapiaoType = "";
		if (o.get("fapiaoType") == null || o.get("fapiaoType").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("fapiaoType").equals("1")) {
				fapiaoType = "增值税电子发票";
			}
			Label cell14 = new Label(13, column, fapiaoType, tempCellFormat);
			// dataStatus
			String dataStatus = "";
			if (o.get("dataStatus") == null || o.get("dataStatus").equals("")) {
				dataStatus = "";
			} else {
				if (o.get("dataStatus").equals("1")) {
					dataStatus = "未开具红票";
				} else if (o.get("dataStatus").equals("2")) {
					dataStatus = "已开具蓝票";
				} else if (o.get("dataStatus").equals("3")) {
					dataStatus = "审核中";
				} else if (o.get("dataStatus").equals("4")) {
					dataStatus = "审核退回";
				} else if (o.get("dataStatus").equals("5")) {
					dataStatus = "合并红冲开票中";
				} else if (o.get("dataStatus").equals("6")) {
					dataStatus = "单笔红冲开票中";
				}
			}
			Label cell15 = new Label(14, column, dataStatus, tempCellFormat);
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
		}
	}

	public WritableCellFormat getBodyCellStyle() {

		/*
		 * WritableFont.createFont("宋体")：设置字体为宋体 10：设置字体大小
		 * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
		 * UnderlineStyle.NO_UNDERLINE：没有下划线
		 */
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat bodyFormat = new WritableCellFormat(font);
		try {
			// 设置单元格背景色：表体为白色
			bodyFormat.setBackground(Colour.WHITE);
			// 设置表头表格边框样式
			// 整个表格线为细线、黑色
			bodyFormat
					.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		} catch (WriteException e) {
			System.out.println("表体单元格样式设置失败！");
		}
		return bodyFormat;
	}

	public RedElectronicsBillService getRedElectronicsBillService() {
		return redElectronicsBillService;
	}

	public void setRedElectronicsBillService(
			RedElectronicsBillService redElectronicsBillService) {
		this.redElectronicsBillService = redElectronicsBillService;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
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

}
