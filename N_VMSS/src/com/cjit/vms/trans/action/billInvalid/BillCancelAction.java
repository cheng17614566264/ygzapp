package com.cjit.vms.trans.action.billInvalid;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.util.SystemPropertyUtils;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.ImageUtil;
import com.cjit.webService.client.InvoicePrintServiceServiceStub;
import com.cjit.webService.client.SubmitData;
import com.cjit.webService.client.SubmitDataResponse;
import com.cjit.webService.client.Util.JsonUtil;
import com.cjit.webService.client.Util.WebServiceUtil;
import com.cjit.webService.client.entity.BillEntity;
import com.cjit.webService.client.entity.HeXinCallBack;

import cjit.crms.util.DateUtil;
import cjit.crms.util.StringUtil;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BillCancelAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private String message;
	private BillCancelInfo billCancelInfo = new BillCancelInfo();
	private BillInfo billInfo = new BillInfo();
	private List billCancelInfoList;
	private String flag;
	private String submitFlag;
	private String[] selectBillIds;
	private TransInfoService transInfoService;
	private BillTrackService billTrackService;
	private String filePath;
	private ParamConfigVmssService paramConfigVmssService;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private VmsCommonService vmsCommonService;
	private String taxParam;// 税控参数

	public String billCancelApply() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {

			this.message = (String) this.request.getAttribute("message");
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			//oracle
			/*billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());*/
			//mysql
			billCancelInfo.setCancelTime(map.get("selected_value").toString());
			
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
//oracle版本
/*				billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());*/
//mysql
				billCancelInfo.setCancelTime(map.get("selected_value").toString());
				
				
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			// billCancelInfo.setBillBeginDate(this.getBillBeginDate()); WJM
			// billCancelInfo.setBillEndDate(this.getBillEndDate());
			billCancelInfo.setBillBeginDate(request.getParameter("billBeginDate"));
			billCancelInfo.setBillEndDate(request.getParameter("billEndDate"));
			billCancelInfo.setHissBeginDte(request.getParameter("hissBeginDte"));
			billCancelInfo.setHissEndDte(request.getParameter("hissEndDte"));
			billCancelInfo.setdSource(request.getParameter("dSource"));
			billCancelInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			//2018-06-08计数修改
			this.paginationList.setShowCount("true");
			billCancelInfoList = billCancelService.findBillCancelInfoList(billCancelInfo, currentUser.getId(),
					paginationList);

			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate", billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate", billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-billCancelApply", e);
		}
		return ERROR;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String updateBillCancelStatus() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			
			MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
	 		try {
				mRequest.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
	 		String dataStatus = mRequest.getParameter("dataStatus");
	 		String insureId = mRequest.getParameter("insureId");
	 		
	 		Map map = transInfoService.findSysParam("OBSOLTETTIME");
	 		//billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString()); 报空指针异常，注意大小写的区分
	 		billCancelInfo.setCancelTime(map.get("selected_value").toString());
	 		if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("dataStatus", dataStatus);
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					billCancelService.updateBillCancelOperateStatus(params);// 保存交易原始状态
				}
			}
	 		//附件 zuofei 保单-投保单-发票状态
	 		String fileName=insureId+"/"+insureId+"-13";
	 		File[] files = mRequest.getFiles("theFile");
			String [] flNames=mRequest.getFileNames("theFile");
			if (files != null && files.length > 0) {
				try {
					ImageUtil.info(fileName, files, flNames,true);
					String errorMessage="";
					AjaxReturn coreMessage = new AjaxReturn(true, errorMessage);
					return SUCCESS;
				} catch (Exception e) {
					log.error(e);
					this.setResultMessages("上传文件失败:" + e.getMessage());
					e.printStackTrace();
					return ERROR;
				}
			} else {
				this.setResultMessages("上传文件失败!");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateBillCancelStatus", e);
			return ERROR;
		}
	}

	public String updateBillCancelAuditingStatus() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			String dataStatus = request.getParameter("dataStatus");
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("dataStatus", dataStatus);
					params.put("cancelReason", "");
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					// 改变交易状态dataStatus
					billCancelService.updateBillCanceldataStatus(params);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateBillCancelAuditingStatus", e);
			return ERROR;
		}
	}

	public String billCancelToRefuse() {
		String[] billIds = request.getParameter("billId").split(",");
		if (billIds != null && billIds.length > 0) {
			String billId = "";
			for (int i = 0; i < billIds.length; i++) {
				billId = billId.equals("") ? billIds[i] : billId + "," + billIds[i];
			}
			request.setAttribute("billId", billId);
			return SUCCESS;
		}
		return ERROR;
	}

	/**
	 * 审核拒绝将状态改变为作废前的原始状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public void updateDataStatusToOriginalStatus() throws Exception {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
		}
		try {
			String[] billIds = request.getParameter("billId").split(",");
			String cancelReason = request.getParameter("cancelReason");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");

			if (billIds != null && billIds.length > 0) {
				for (int i = 0; i < billIds.length; i++) {
					// billCancelInfo =
					// billCancelService.findBillCancelInfo(billIds[i]);
					// billCancelInfo.setDataStatus(billCancelInfo.getOperateStatus());

					Map params = new HashMap();
					params.put("billId", billIds[i]);
					params.put("cancelReason", cancelReason);
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					billCancelService.updateBillCanceldataStatus(params);// 将原始交易状态赋值dataStatus
				}
			}
			printWriterResult("success");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateDataStatusToOriginalStatus", e);
			printWriterResult("error");
		}
	}

	/**
	 * 撤销交易后将状态改变为作废前的原始状态
	 * 
	 * @return
	 */
	public String revokeBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			User currentUser = this.getCurrentUser();
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillCancelInfo billCancelInfo = billCancelService.findBillCancelInfo(selectBillIds[i]);
					// 如果不是当天的废票，需要【作废已审核】撤销后，将状态更新为【作废待审核】
					// 否则的话，直接改变为作废前的原始状态。
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("cancelReason", "");
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					if (!DateUtil.parseDateToString(new Date()).equals(billCancelInfo.getBillDate().substring(0, 10))) {
						params.put("dataStatus", DataUtil.BILL_STATUS_13);
						billCancelService.updateBillCanceldataStatus(params);
					} else {
						billCancelService.updateBillCanceldataStatus(params);
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-revokeBill", e);
			return ERROR;
		}
	}

	public void billCancelDispose() throws Exception {
		String result = "";
		StringBuffer sb = new StringBuffer();
		String taxDiskNo = request.getParameter("taxDiskNo");
		String billId = request.getParameter("billId");

		// 获取注册码
		String registeredInfo = billCancelService.getRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}
		sb.append(registeredInfo);

		// 查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = billCancelService.getTaxDiskInfo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}
		sb.append("|" + taxDiskInfo.getTaxDiskPsw() + "|" + taxDiskInfo.getTaxCertPsw());

		sb.append("|" + billId);

		// 查询需要开具的发票
		BillInfo bill = billInfoService.findBillById(billId);
		sb.append("|" + bill.getFapiaoType()); // 发票类型
		sb.append("|1"); // 作废类型
		sb.append("|" + bill.getBillCode()); // 发票代码
		sb.append("|" + bill.getBillNo()); // 发票号码
		sb.append("|" + this.getCurrentUser().getName()); // 作废人
		sb.append("|" + bill.getAmtSum()); // 发票金额

		result = sb.toString();
		// System.out.println(result);
		printWriterResult(result);
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		// out.print(result);
		out.write(result);
		out.close();
	}

	public void updateCancelBillResult() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return;
		}
		try {
			this.message = (String) this.request.getAttribute("message");
			String cancelRes = request.getParameter("cancelRes");
			System.out.println(cancelRes + "===");
			String[] results = cancelRes.split("\\|");
			// 0|已开具发票作废，当前发票已作废|B2016011100000001200|
			System.out.println("319");
			if ("1".equals(results[0])) {
				Map params = new HashMap();
				params.put("billId", results[2]);
				params.put("dataStatus", DataUtil.BILL_STATUS_15);
				params.put("cancelReason", "");
				billCancelService.updateBillCanceldataStatus(params);// 改变交易状态dataStatus

				List lstAuthInstId = new ArrayList();
				this.getAuthInstList(lstAuthInstId);
				// 数据回传
				billInfo = billInfoService.findBillById(results[2]);
				List list = pageTaxInvoiceService.findpaperAutoInvoicebyBusId(billInfo.getBillNo(), lstAuthInstId,
						billInfo.getBillCode());
				if (list != null) {
					PaperAutoInvoice pa = (PaperAutoInvoice) list.get(0);
					pageTaxInvoiceService.updateIssueCancleNum(pa.getInvoiceBeginNo(), pa.getInvoiceEndNo(),
							billInfo.getBillCode());
				}
				// 查询对应交易信息，修改交易状态为开票中
				TransInfo transInfo = new TransInfo();
				transInfo.setBillId(results[2]);

				transInfo.setLstAuthInstId(lstAuthInstId);
				List transList = transInfoService.findTransInfoForBillCancelList(transInfo);
				for (Iterator t = transList.iterator(); t.hasNext();) {
					TransInfo trans = (TransInfo) t.next();

					trans.setBalance(trans.getBalance().add(trans.getTbincomeCny()));
					// 在vms_trans_info表中，修改状态为未开票，并且将作废发票中的金额增加到未开票金额中
					transInfoService.updateTransStatusAndBalance(trans.getTransId(), trans.getBalance(),
							DataUtil.TRANS_STATUS_1);
				}

			}
			printWriterResult(results[1]);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateCancelBillResult", e);
		}

	}

	/** 与核心交互 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void cancelBillAccessCore() throws Exception {
		AjaxReturn coreMessage = null;
		String errorMessage = "";
		try {
			String billId = request.getParameter("billId");
			if (!sessionInit(true)) {
				errorMessage = "用户失效";
			}
			List<String> billIds = Arrays.asList(billId.split(","));
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			TransInfo transInfo = new TransInfo();
			transInfo.setBillId(billId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			List<BillEntity> billEntities =transInfoService.findBillEntitys(billIds);
			for (BillEntity billEntity : billEntities) {
				billEntity.setDataStatus(WebServiceUtil.BILL_CANCEL);
			}
			String url = transInfoService.findWebServiceUrl(WebServiceUtil.HEXIN_SERVICE_NAME).get(0);
			InvoicePrintServiceServiceStub invoicePrintServiceStub = new InvoicePrintServiceServiceStub(url);
			SubmitData submitData = new SubmitData();
			submitData.setRequestJSON(JsonUtil.billIssueJson(billEntities, "TRUE", true));
			SubmitDataResponse submitDataResponse = invoicePrintServiceStub.submitData(submitData);
			HeXinCallBack callBack = JsonUtil.callBacks(submitDataResponse.getSubmitDataReturn());
			if (callBack==null) {
				coreMessage = new AjaxReturn(false, "回写核心失败");
			}else {
				if ("1".equals(callBack.getResulttype())) {
					for (BillEntity billEntity : billEntities) {
						billEntity.setErrorInfo(callBack.getErrorInfo());
					}
					coreMessage = new AjaxReturn(false, "回写核心失败");
				} else {
					// 接口调用成功
					coreMessage = new AjaxReturn(true);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateCancelBillResult", e);
			coreMessage = new AjaxReturn(false, "系统内部异常");
		}
		returnResult(coreMessage);
	}

	public String listBillCancelAuditing() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}

		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			billCancelInfo.setBillBeginDate(request.getParameter("billBeginDate"));
			billCancelInfo.setBillEndDate(request.getParameter("billEndDate"));
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			//2018-06-08计数新增
			paginationList.setShowCount("true");
			billCancelInfoList = billCancelService.findBillCancelAuditingList(billCancelInfo, currentUser.getId(),
					paginationList);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate", billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate", billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-listBillCancelAuditing", e);
		}
		return ERROR;
	}

	/**
	 * 根据billId获取当前交易下需要作废的发票代码和发票号码
	 * 
	 * @return
	 */
	public void findNeedCancelBill() {
		String billId = request.getParameter("billId");
		List<BillInfo> list = transInfoService.findNeedBillById(billId);
		List<Map<String, String>> billList = new ArrayList<Map<String, String>>();
		Set<String> set = new HashSet<String>();
		for (BillInfo billCancelInfo : list) {
			int i = set.size();
			set.add(billCancelInfo.getBillId());
			int j = set.size();
			if (j > i) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("billId", billCancelInfo.getBillId());
				map.put("billCode", billCancelInfo.getBillCode());
				map.put("billNo", billCancelInfo.getBillNo());
				billList.add(map);
			}
		}
		JSONArray jsonObject = new JSONArray();
		JSONObject billJsonObject = new JSONObject();
		for (Map<String, String> map : billList) {
			billJsonObject.put("billId", map.get("billId"));
			billJsonObject.put("billCode", map.get("billCode"));
			billJsonObject.put("billNo", map.get("billNo"));
			jsonObject.add(billJsonObject);
		}

		AjaxReturn ajaxReturn = new AjaxReturn(true, jsonObject.toString());
		try {
			returnResult(ajaxReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String listBillCancel() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}

		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {
			User currentUser = this.getCurrentUser();
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			
			//oracle版本
			/*billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());*/
			//mysql版本
			billCancelInfo.setCancelTime(map.get("selected_value").toString());
			
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}

			billCancelInfo.setBillBeginDate(request.getParameter("billBeginDate"));
			billCancelInfo.setBillEndDate(request.getParameter("billEndDate"));
			billCancelInfo.setDataStatus(request.getParameter("dataStatus"));
			billCancelInfo.setdSource(request.getParameter("dSource"));
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billCancelService.findBillCancelList(billCancelInfo, currentUser.getId(),
					paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			this.request.setAttribute("paginationList", paginationList);
			System.out.println("flag-----" + billCancelInfoList.toString());
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate", billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate", billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			return "disk";

		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-listBillCancel", e);
		}
		return ERROR;
	}

	/**
	 * 作废查看交易
	 * 
	 * @return
	 */
	public String CheckTrans() {
		String billid = request.getParameter("billId");
		List list = billCancelService.getBillCancelList(billid);
		String transid = null;
		int count = 0;
		int cou = 0;
		int q = 0;
		int w = 0;
		int m = 0;
		int t = 0;
		String ttmpRcno = null;
		String insureid = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				BillCancelInfo billCancelInfo = (BillCancelInfo) list.get(0);
				transid = billCancelInfo.getTransId();//
				Map map = new HashMap();
				map.put("transid", transid);
				// 的到同一笔交易下的所有billid
				List listbillid = billCancelService.getbillid(map);
				// 统计交易信息
				count = listbillid.size() - 1;// 出去本身
				for (int k = 0; k < listbillid.size(); k++) {
					BillCancelInfo bill = (BillCancelInfo) listbillid.get(k);
					// 得到发票状态 本月可以作废的信息
					Map idmap = new HashMap();
					idmap.put("bill", bill);
					bill = billCancelService.findBillCancelInfo(idmap);
					String dataStatus = bill.getDataStatus() == null ? "W" : bill.getDataStatus();
					// 的到保单号
					ttmpRcno = bill.getTtmpRcno();
					insureid = bill.getInsureId();
					if ("5".equals(dataStatus) || "13".equals(dataStatus) || "14".equals(dataStatus)) {
						cou++; // 统计发票
					}
				}
				m = count;
				count = 0;
				t = count;
				q = cou;
				cou = 0;
				w = cou;
			}

		}
		// 统计发票 除去本身
		int billcount = q + w - 1;
		// 统计所有的 交易
		int transbillcount = m + t - billcount;
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("投保单号 ：" + ttmpRcno + "\t" + "保单号：" + insureid + "\n还未处理交易 ：" + transbillcount
					+ "\t条\n还未处理发票 ：" + billcount + "\t条\n" + "确定将选中票据进行作废处理？");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
		return null;
	}

	/**
	 * 发票作废页面查看交易
	 */
	public String seeTransWithBill() {
		try {
			String reqSource = request.getParameter("reqSource");
			String billId = (String) this.request.getParameter("billId");
			billCancelInfoList = billTrackService.findTransByBillId(billId, paginationList);
			this.request.setAttribute("reqSource", reqSource);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-seeTransWithBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票作废页面查看票样
	 */
	public String viewImgFromBill() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);

			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
			map.put("billItemList", billItemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone", billInfo.getCustomerAddressandphone());
			map.put("cancelBankandaccount", billInfo.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());

			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 查看发票作废信息明细界面
	 * 
	 * @return String
	 */
	public String viewbillCancelInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			String reqSource = request.getParameter("reqSource");
			String billId = request.getParameter("billId");
			billCancelInfo = billCancelService.findBillCancelInfo(billId);
			this.request.setAttribute("billCancelInfo", billCancelInfo);
			this.request.setAttribute("reqSource", reqSource);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-viewbillCancelInfo", e);
		}
		return ERROR;
	}

	/**
	 * 导出Excel
	 * 
	 * @return String
	 */
	public void cancelBillToExcel() throws Exception {
		try {
			String reqExportSource = request.getParameter("reqExportSource");
			// 开票日期 开始 结束
			billCancelInfo.setBillBeginDate(this.billCancelInfo.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.billCancelInfo.getBillEndDate());
			// 客户纳税人名称
			billCancelInfo.setCustomerName(this.billCancelInfo.getCustomerName());
			// 发票代码
			billCancelInfo.setBillCode(this.billCancelInfo.getBillCode());
			// 发票号码
			billCancelInfo.setBillNo(this.billCancelInfo.getBillNo());
			// 发票类型
			billCancelInfo.setFapiaoType(this.billCancelInfo.getFapiaoType());
			// 状态（已开具、已打印、打印失败、已收回、13-作废待审核、14-作废已审核、15-已作废）
			if (!reqExportSource.equals("billCancelAuditing")) {
				if (this.billCancelInfo.getDataStatus() != null) {
					billCancelInfo.setDataStatus(this.billCancelInfo.getDataStatus());
					reqExportSource = "";
				}
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			User currentUser = this.getCurrentUser();
			billCancelInfoList = billCancelService.findBillCancelInfoQuery(billCancelInfo, currentUser.getId(),
					reqExportSource);
			this.request.getSession().setAttribute("transBeginDate", billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("transEndDate", billInfo.getBillEndDate());

			StringBuffer fileName = new StringBuffer("作废发票信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, billCancelInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = wb.createSheet("作废发票信息表", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
		excelInfo.setBgColor(Colour.YELLOW2);
		excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(4, 0, "客户纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(6, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(7, 0, "开票人", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(8, 0, "税控盘号", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(9, 0, "开票机号", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeaderC(excelInfo));
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeaderC(excelInfo));
		Label header13 = new Label(12, 0, "税率", JXLTool.getHeaderC(excelInfo));
		Label header14 = new Label(13, 0, "价税合计", JXLTool.getHeaderC(excelInfo));
		Label header15 = new Label(14, 0, "是否手工录入", JXLTool.getHeaderC(excelInfo));
		Label header16 = new Label(15, 0, "开具类型", JXLTool.getHeaderC(excelInfo));
		Label header17 = new Label(16, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header18 = new Label(17, 0, "状态", JXLTool.getHeaderC(excelInfo));

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
		ws.addCell(header18);
		for (int i = 0; i < 18; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillCancelInfo o = (BillCancelInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			map.put("applyDate", o.getApplyDate());
			map.put("billDate", o.getBillDate());
			map.put("customerName", o.getCustomerName());
			map.put("customerTaxNo", o.getCustomerTaxno());
			map.put("billCode", o.getBillCode());
			map.put("billNo", o.getBillNo());
			map.put("drawer", o.getDrawer());
			map.put("taxDiskNo", o.getTaxDiskNo());
			map.put("machineNo", o.getMachineNo());
			map.put("amtSum", o.getAmtSum());
			map.put("taxAmtSum", o.getTaxAmtSum());
			map.put("taxRate", o.getTaxRate());
			map.put("sumAmt", o.getSumAmt());
			map.put("isHandiwork", o.getIsHandiwork());
			map.put("issueType", o.getIssueType());
			map.put("fapiaoType", o.getFapiaoType());
			map.put("dataStatus", o.getDataStatus());

			setWritableSheet(ws, map, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, Map o, int column) throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		// 序号
		Label cell1 = new Label(0, column, o.get("Id").toString(), tempCellFormat);

		// 申请开票日期applyDate
		Label cell2 = new Label(1, column,
				o.get("applyDate") == null || o.get("applyDate").equals("") ? "" : o.get("applyDate").toString(),
				tempCellFormat);

		// 开票日期billDate
		Label cell3 = new Label(2, column,
				o.get("billDate") == null || o.get("billDate").equals("") ? "" : o.get("billDate").toString(),
				tempCellFormat);

		// 客户纳税人名称customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null || o.get("customerName").equals("") ? ""
				: o.get("customerName").toString(), tempCellFormat);
		// 客户纳税人识别号customerTaxNo
		Label cell5 = new Label(4, column, o.get("customerTaxNo") == null || o.get("customerTaxNo").equals("") ? ""
				: o.get("customerTaxNo").toString(), tempCellFormat);

		// 发票代码billCode
		Label cell6 = new Label(5, column,
				o.get("billCode") == null || o.get("billCode").equals("") ? "" : o.get("billCode").toString(),
				tempCellFormat);

		// 发票号码billNo
		Label cell7 = new Label(6, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o.get("billNo").toString(),
				tempCellFormat);

		// 开票人drawer
		Label cell8 = new Label(7, column,
				o.get("drawer") == null || o.get("drawer").equals("") ? "" : o.get("drawer").toString(),
				tempCellFormat);

		// 税控盘号taxDiskNo
		Label cell9 = new Label(8, column,
				o.get("taxDiskNo") == null || o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo").toString(),
				tempCellFormat);

		// 开票机号machineNo
		Label cell10 = new Label(9, column,
				o.get("machineNo") == null || o.get("machineNo").equals("") ? "" : o.get("machineNo").toString(),
				tempCellFormat);

		// 合计金额amtSum
		Label cell11 = new Label(10, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o.get("amtSum").toString(),
				tempCellFormat);
		// 合计税额taxAmtSum
		Label cell12 = new Label(11, column,
				o.get("taxAmtSum") == null || o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum").toString(),
				tempCellFormat);

		// 税率taxRate
		Label cell13 = new Label(12, column,
				o.get("taxRate") == null || o.get("taxRate").equals("") ? "" : o.get("taxRate").toString(),
				tempCellFormat);

		// 价税合计sumAmt
		Label cell14 = new Label(13, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o.get("sumAmt").toString(),
				tempCellFormat);

		// 是否手工录入isHandiwork
		String isHandiwork = "";
		if (o.get("isHandiwork") == null || o.get("isHandiwork").equals("")) {
			isHandiwork = "";
		} else {
			if (o.get("isHandiwork").equals("1")) {
				isHandiwork = "自动开票";
			} else if (o.get("isHandiwork").equals("2")) {
				isHandiwork = "人工审核";
			} else {
				isHandiwork = "人工开票";
			}
		}
		Label cell15 = new Label(14, column, isHandiwork, tempCellFormat);

		// 开具类型issueType
		String issueType = "";
		if (o.get("issueType") == null || o.get("issueType").equals("")) {
			issueType = "";
		} else {
			if (o.get("issueType").equals("1")) {
				issueType = "单笔";
			} else if (o.get("issueType").equals("2")) {
				issueType = "合并";
			} else {
				issueType = "拆分";
			}
		}
		Label cell16 = new Label(15, column, issueType, tempCellFormat);

		// 发票类型fapiaoType
		String fapiaoType = "";
		if (o.get("fapiaoType") == null || o.get("fapiaoType").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("fapiaoType").equals("0")) {
				fapiaoType = "增值税专用发票";
			} else {
				fapiaoType = "增值税普通发票";
			}
		}
		Label cell17 = new Label(16, column, fapiaoType, tempCellFormat);

		// 状态dataStatus
		String dataStatus = "";
		if (o.get("dataStatus") == null || o.get("dataStatus").equals("")) {
			dataStatus = "";
		} else {
			if (o.get("dataStatus").equals("5")) {
				dataStatus = "已开具";
			} else if (o.get("dataStatus").equals("8")) {
				dataStatus = "已打印";
			} else if (o.get("dataStatus").equals("9")) {
				dataStatus = "打印失败";
			} else if (o.get("dataStatus").equals("19")) {
				dataStatus = "已收回";
			} else if (o.get("dataStatus").equals("13")) {
				dataStatus = "作废待审核";
			} else if (o.get("dataStatus").equals("14")) {
				dataStatus = "作废已审核";
			} else if (o.get("dataStatus").equals("15")) {
				dataStatus = "已作废";
			} else {
				dataStatus = "";
			}
		}
		Label cell18 = new Label(17, column, dataStatus, tempCellFormat);

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
		ws.addCell(cell18);
	}

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	/**
	 * 表头单元格样式的设定
	 */
	public WritableCellFormat getBodyCellStyle() {

		/*
		 * WritableFont.createFont("宋体")：设置字体为宋体 10：设置字体大小
		 * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
		 * UnderlineStyle.NO_UNDERLINE：没有下划线
		 */
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat bodyFormat = new WritableCellFormat(font);
		try {
			// 设置单元格背景色：表体为白色
			bodyFormat.setBackground(Colour.WHITE);
			// 设置表头表格边框样式
			// 整个表格线为细线、黑色
			bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		} catch (WriteException e) {
			System.out.println("表体单元格样式设置失败！");
		}
		return bodyFormat;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BillCancelInfo getBillCancelInfo() {
		return billCancelInfo;
	}

	public void setBillCancelInfo(BillCancelInfo billCancelInfo) {
		this.billCancelInfo = billCancelInfo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSubmitFlag() {
		return submitFlag;
	}

	public void setSubmitFlag(String submitFlag) {
		this.submitFlag = submitFlag;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}

	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public List getBillCancelInfoList() {
		return billCancelInfoList;
	}

	public void setBillCancelInfoList(List billCancelInfoList) {
		this.billCancelInfoList = billCancelInfoList;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}
}
