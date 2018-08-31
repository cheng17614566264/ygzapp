package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.GeneralLedgerTemp;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInfoUtil;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.GetGeneralIedgerService;
import com.cjit.vms.input.service.InputInvoiceService;
import com.cjit.vms.input.service.InputTransService;
import com.cjit.vms.input.service.InvoiceScanAuthService;
import com.cjit.vms.input.service.PullDataService;
import com.cjit.vms.input.service.VendorInfoService;
import com.cjit.vms.system.service.LogEmpService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.webService.client.CemClient;

import cjit.crms.util.ExcelUtil;
import cjit.crms.util.StringUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.cglib.core.NamingPolicy;

/**
 * 进项税-发票扫描认证Action类
 *
 * @author jobell
 */
public class InvoiceScanAuthAction extends DataDealAction {
	private InputInfo inputInfo = new InputInfo();
	
	/**
	 * @Action 进项税-发票扫描认证 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String listInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			this.getAuthInstList(lstAuthInstId);
			mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			inputInfo.setLstAuthInstId(lstAuthInstId);
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.YEAR, -1);
			if(inputInfo.getTransBeginDate()==null&&inputInfo.getTransEndDate()==null){
				inputInfo.setTransBeginDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
				inputInfo.setTransEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			}
			if(inputInfo.getBillStatu()==null){
				inputInfo.setBillStatu("0");
			}
			List lstDataStatus = DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus = new HashMap();
			for (int i = 0; i < lstDataStatus.size(); i++) {
				SelectTag tag = (SelectTag) lstDataStatus.get(i);
				if (Integer.parseInt(tag.getValue()) > 9) {
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			System.out.println(paginationList.getPageSize()+",pagesize..........");//null
			invoiceScanAuthService.findListInvoiceScanAuth(inputInfo, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-listInvoiceScanAuth", e);
		}
		return ERROR;
	}

	/**
	 * @Action 进项税-发票扫描认证 认证提交处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String authSubmit() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String selects=request.getParameter("selects");
			String [] bill =selects.split(" ");
			List<Map<String, String>> billList=new ArrayList<Map<String,String>>();
			if(bill.length!=0){
				for(int i=0;i<bill.length;i++){
				 String	bills =bill[i];
				 billId=bills.split("-")[0];
				 billCode=bills.split("-")[1];
				 invoiceScanAuthService.updateVmsInputInvoiceInfoForAuthSubmit("1", billId, billCode);
				 Map<String, String> map=new HashMap<String, String>();
				 map.put("billId", billId);
				 map.put("billCode", billCode);
				 map.put("billStatu", "1");
				 billList.add(map);
				}
			}
			
			this.cemClien(billList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-authSubmit", e);
		}
		return ERROR;
	}
	
	/**
	 * 进项-认证不通过处理
	 * @return
	 */
	public String authSubmitRZBTG(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String[] bill = billId.split("-");
			String billCode = "";
			if (bill.length >= 2) {
				billId = bill[0];
				billCode = bill[1];
			}
		 	invoiceScanAuthService.updateVmsInputInvoiceInfoForAuthSubmit("3", billId, billCode);
			List<Map<String, String>> billList=new ArrayList<Map<String,String>>();
			Map<String, String> map=new HashMap<String, String>();
			map.put("billId", billId);
			map.put("billCode", billCode);
			map.put("billStatu", "3");
			billList.add(map);
			this.cemClien(billList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-authSubmit", e);
		}
		
		return ERROR;
	} 

	/**
	 * @Action 进项税-发票扫描认证 导出处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String exportInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			this.getAuthInstList(lstAuthInstId);
			mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			inputInfo.setLstAuthInstId(lstAuthInstId);
			;
			List<InputInfo> inputInfos = invoiceScanAuthService.findListInvoiceScanAuth(inputInfo, paginationList);
			if (inputInfos.size() == 0) {
				return null;
			}

			StringBuffer fileName = new StringBuffer("进项发票扫描认证");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			List<String> billList = new ArrayList<String>();
			for (InputInfo info : inputInfos) {
				billList.add(info.getBillId() + "-" + info.getBillCode());
			}
			List<BillDetailEntity> billDetailEntities = invoiceScanAuthService.finBillDetailEntites(billList);

			writeToExcel(os, inputInfos, billDetailEntities);

			os.flush();
			os.close();

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-exportInvoiceScanAuth", e);
		}
		return ERROR;
	}

	private void writeToExcel(OutputStream os, List<InputInfo> inputInfoList,
			List<BillDetailEntity> billDetailEntities) {
		WritableWorkbook wb = null;
		try {
			wb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			ws = wb.createSheet("进项发票主信息", 0);
			String[] titles = { "发票代码", "发票号码", "币种", "开票日期", "报销机构编码", "客户纳税人识别号", "客户名称", "客户开户账号", "客户开户银行中文名称",
					"客户电话", "客户地址", "发票类型", "发票状态", "认证时间", "总金额", "总税额", "总价税合计" };
			for (int i = 0; i < titles.length; i++) {
				Label header = new Label(i, 0, titles[i], JXLTool.getHeader());
				ws.addCell(header);
			}
			for (int i = 0; i < inputInfoList.size(); i++) {
				InputInfo inputInfo=inputInfoList.get(i);
				ws.addCell(new Label(0, i + 1, inputInfo.getBillId(), JXLTool.getContentFormat()));
				ws.addCell(new Label(1, i + 1, inputInfo.getBillCode(), JXLTool.getContentFormat()));
				ws.addCell(new Label(2, i + 1, inputInfo.getCurreny(), JXLTool.getContentFormat()));
				ws.addCell(new Label(3, i + 1, inputInfo.getBillDate(), JXLTool.getContentFormat()));
				ws.addCell(new Label(4, i + 1, inputInfo.getBillInst(), JXLTool.getContentFormat()));
				ws.addCell(new Label(5, i + 1, inputInfo.getTaxNo(), JXLTool.getContentFormat()));
				ws.addCell(new Label(6, i + 1, inputInfo.getName(), JXLTool.getContentFormat()));
				ws.addCell(new Label(7, i + 1, inputInfo.getBankNo(), JXLTool.getContentFormat()));
				ws.addCell(new Label(8, i + 1, inputInfo.getBankName(), JXLTool.getContentFormat()));
				ws.addCell(new Label(9, i + 1, inputInfo.getCustomerTel(), JXLTool.getContentFormat()));
				ws.addCell(new Label(10, i + 1,inputInfo.getCustomerAdd(), JXLTool.getContentFormat()));
//				ws.addCell(new Label(11, i + 1, InputInfoUtil.purposeMap.get(inputInfo.getPurpose()),
//						JXLTool.getContentFormat()));
				ws.addCell(new Label(11, i + 1, DataUtil.getFapiaoTypeCH(inputInfo.getBillType()),
						JXLTool.getContentFormat()));
				ws.addCell(new Label(12, i + 1, InputInfoUtil.billStatuMap.get(inputInfo.getBillStatu()),
						JXLTool.getContentFormat()));
				ws.addCell(new Label(13, i + 1, inputInfo.getDealNo(), JXLTool.getContentFormat()));
				ws.addCell(new Label(14, i + 1, inputInfo.getAmt().toString(), JXLTool.getContentFormat()));
				ws.addCell(new Label(15, i + 1, inputInfo.getTax().toString(), JXLTool.getContentFormat()));
				ws.addCell(
						new Label(16, i + 1, inputInfo.getSumTax().toString(), JXLTool.getContentFormat()));
			}

			WritableSheet sheet2 = wb.createSheet("进项发票明细信息", 1);

			String[] sheet2Title = { "发票代码", "发票号码", "金额", "税额", "税率", "价税合计金额", "是否抵免", "转出金额", "转出原因" ,"用途"};
			for (int i = 0; i < sheet2Title.length; i++) {
				Label header = new Label(i, 0, sheet2Title[i], JXLTool.getHeader());
				sheet2.addCell(header);
			}
			for (int i = 0; i < billDetailEntities.size(); i++) {
				BillDetailEntity billDetailEntity=billDetailEntities.get(i);
				sheet2.addCell(new Label(0, i + 1, billDetailEntity.getBillId(), JXLTool.getContentFormat()));
				sheet2.addCell(new Label(1, i + 1, billDetailEntity.getBillCode(), JXLTool.getContentFormat()));
				sheet2.addCell(new Label(2, i + 1, billDetailEntity.getAmt().toString(), JXLTool.getContentFormat()));
				sheet2.addCell(new Label(3, i + 1, billDetailEntity.getTax().toString(), JXLTool.getContentFormat()));
				sheet2.addCell(new Label(4, i + 1, billDetailEntity.getTaxRate().toString(),JXLTool.getContentFormat()));
				sheet2.addCell(new Label(5, i + 1, billDetailEntity.getSumAmt().toString(),JXLTool.getContentFormat()));
				sheet2.addCell(new Label(6, i + 1, InputInfoUtil.isCreditMap.get(billDetailEntity.getIsCredit()),JXLTool.getContentFormat()));
//				sheet2.addCell(new Label(7, i + 1,billDetailEntity.getRollOutRatio()==null?"":billDetailEntity.getRollOutRatio(), JXLTool.getContentFormat()));
				sheet2.addCell(new Label(7, i + 1,billDetailEntity.getRollOutAmt()==null?"":billDetailEntity.getRollOutAmt().toString(),JXLTool.getContentFormat()));
				sheet2.addCell(new Label(8, i + 1, InputInfoUtil.remarkMap.get(billDetailEntity.getRemark()),JXLTool.getContentFormat()));
				sheet2.addCell(new Label(9, i + 1, InputInfoUtil.purposeMap.get(billDetailEntity.getPurpose()),JXLTool.getContentFormat()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.write();
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Action
	 * 
	 * 		销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, InputInvoiceInfo info, int column) throws WriteException {
		// 发票代码
		Label cell1 = new Label(0, column, info.getBillCode(), JXLTool.getContentFormat());
		// 发票号码
		Label cell2 = new Label(1, column, info.getBillNo(), JXLTool.getContentFormat());
		// 开票日期
		Label cell3 = new Label(2, column, info.getBillDate(), JXLTool.getContentFormat());
		// 所属机构
		Label cell4 = new Label(3, column, info.getInstName(), JXLTool.getContentFormat());
		// 金额
		Label cell5 = new Label(4, column, info.getAmtSum().toString(), JXLTool.getContentFormat());
		// 税额
		Label cell6 = new Label(5, column, info.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		// 发票种类
		Label cell7 = new Label(6, column, DataUtil.getFapiaoTypeCH(info.getFapiaoType()), JXLTool.getContentFormat());
		// 供应商名称
		Label cell8 = new Label(7, column, info.getVendorName(), JXLTool.getContentFormat());
		// 供应商纳税人识别号
		Label cell9 = new Label(8, column, info.getVendorTaxno(), JXLTool.getContentFormat());
		// 发票状态
		Label cell10 = new Label(9, column, DataUtil.getDataStatusCH(info.getDatastatus(), "INPUT_INVOICE"),
				JXLTool.getContentFormat());
		// 认证日期
		Label cell11 = new Label(10, column, info.getIdentifyDate(), JXLTool.getContentFormat());
		// 扫描时间
		Label cell12 = new Label(11, column, info.getScanDate(), JXLTool.getContentFormat());

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
	}

	/**
	 * @Action 进项税-发票扫描认证 导入处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String importInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
			File[] files = mRequest.getFiles("theFile");
			if (files != null && files.length > 0) {
				try {
					doImportFile(files[0]);
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
			log.error("InvoiceScanAuthAction-importInvoiceScanAuth", e);
		}
		return ERROR;
	}

	/**
	 * 通过电子底账库导出的Excel进行发票的批量认证
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, String>> doImportFile(File file) throws Exception {
		User currentUser = this.getCurrentUser();
		ArrayList<Map<String, String>> errorList = new ArrayList<Map<String, String>>();

		List<Dictionary> headList = userInterfaceConfigService.getDictionarys("INPUT_INVOICE_IMP", "");
		Map<String, String> titleMap = new HashMap<String, String>();
		for (Dictionary dictionary : headList) {
			titleMap.put(dictionary.getTypeName(), dictionary.getName());
		}
		// 用来记录所需字段所在的列
		Map<Integer, String> columnMap = new HashMap<Integer, String>();
		Hashtable<?, ?> hs = ExcelUtil.parseExcel(null, file, 1);
		List<Map<String, String>> billList = new ArrayList<Map<String, String>>();
		if (hs != null) {
			String[][] sheet = (String[][]) hs.get("0");
			// 获取表头列表
			String[] heads = sheet[2];
			for (int i = 0; i < heads.length; i++) {
				String title = heads[i];
				String column=titleMap.get(title);
				if (StringUtils.isNotEmpty(column)){
					columnMap.put(i, column);
				}
			}
			//[序号, 发票代码, 发票号码, 开票日期, 销方名称, 金额, 税额, 认证方式, 确认/认证日期, 发票类型, 发票状态, null]
			
			Set<Integer> keys=columnMap.keySet();
			SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 3; i < sheet.length; i++) {
				String[] bill=sheet[i];
				Map<String, String> billMap=new HashMap<String, String>();
				for (Integer key : keys) {
					billMap.put("billInst", currentUser.getOrgId());
					if (StringUtils.isEmpty(bill[key])) {
						billMap=null;
						break;
					}else {
						if (columnMap.get(key).equals("billDate")) {
							Calendar calendar=new GregorianCalendar(1900, 0, -1);
							calendar.add(Calendar.DATE, Integer.valueOf(bill[key]));
							Date time = calendar.getTime();
							billMap.put(columnMap.get(key), sFormat.format(time));
						}else if (columnMap.get(key).equals("billType")) {
							if (bill[key].indexOf("专")>=0) {
								billMap.put(columnMap.get(key), "0");
							}else {
								billMap.put(columnMap.get(key), "1");
							}
						}else{
							billMap.put(columnMap.get(key), bill[key]);
						}
					}
				}
				if (billMap!=null) {
					billList.add(billMap);
				}
			}
			//取出所有待认证的的发票代码和发票号码，判断在库里面是否已经认证过。避免重复认证
			List<String> bills=new ArrayList<String>();
			for (Map<String, String> map : billList) {
				bills.add(map.get("billId")+map.get("billCode"));
				map.put("billStatu", "1");
			}
			
			// 查询表中在认证清单中并且未认证的增值税专用发票
			List<String> repeatBill=invoiceScanAuthService.findRepeatBill(bills);
			for (int i=0;i<billList.size();i++) {
				Map<String, String> map=billList.get(i);
				String bill=map.get("billId")+map.get("billCode");
				for (String reBill : repeatBill) {
					if (!bill.equals(reBill)) {
						billList.remove(i);
					}
				}
			}
			//更新认证通过的专票信息
			invoiceScanAuthService.insertEnterOkInputBill(bills);
			this.cemClien(billList);
			setResultMessages("导入认证成功");
		}
		return errorList;
	}
	/**
	 * 回写认证状态到费控系统
	 * @param billList
	 */
	private void cemClien(List<Map<String, String>> billList){
		
		List<String> urList=invoiceScanAuthService.findWebServiceUrl("feikong");
		CemClient cemClient=new CemClient(urList.get(0));
		
		String json=enterOkJson(billList);
		String cemOut=cemClient.receiver("wsVatServer", "invoiceVerificationHandle", json);
		log.info(cemOut);
	}
	
	/**
	 * 认证通过到发票json信息
	 * @param billList
	 */
	private String enterOkJson(List<Map<String, String>> billList) {
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonObject.put("taxTransferInfo", jsonArray);
		for (Map<String, String> map : billList) {
			JSONObject bill=new JSONObject();
			bill.put("billId", map.get("billId"));
			bill.put("billCode", map.get("billCode"));
			bill.put("billStatu", map.get("billStatu"));
			bill.put("billDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
			jsonArray.add(bill);
		}
		return jsonObject.toJSONString();
	}

	// 验证业务数据
	public Map<String, String> checkEmpInvoice(Map<String, String> map, Map<String, String> mapBusi, int i,
			int length) {
		String resultBusi = StringUtil.IsEmptyStr(mapBusi.get("resultBusi")) ? "" : mapBusi.get("resultBusi");// 业务验证字符
																												// 串
		String vendorMessage = StringUtil.IsEmptyStr(mapBusi.get("vendorMessage")) ? "" : mapBusi.get("vendorMessage");// 供应商信息验证
		String invoiceCertified = StringUtil.IsEmptyStr(mapBusi.get("invoiceCertified")) ? ""
				: mapBusi.get("vendorCertified");// 发票 是否审核验证
		String instTaxNo = StringUtil.IsEmptyStr(mapBusi.get("instTaxNo")) ? "" : mapBusi.get("instTaxNo");// 发票
																											// 是否审核验证
		List<?> list = vendorInfoService.findVendorByTaxNo(map.get("vendorTaxNo").toString());
		if (map.get("instcode").equals("")) {
			instTaxNo += i + ",";
		}
		if (list.size() == 0) {
			vendorMessage += Integer.toString(i) + ",";
		}
		String billCode = (String) map.get("billCode");
		String billNo = (String) map.get("billNo");
		InputInvoiceInfo oldObj = (InputInvoiceInfo) invoiceScanAuthService
				.findInvoiceScanAuthByBillCodeAndBillNo(billCode, billNo);
		System.out.println(billCode);
		if (oldObj != null && ("3".equals(oldObj.getDatastatus()) || "5".equals(oldObj.getDatastatus())
				|| "7".equals(oldObj.getDatastatus()))) {
			invoiceCertified += Integer.toString(i) + ",";
		}
		vendorMessage = i + 1 == length && vendorMessage.length() > 0
				? "第" + vendorMessage.substring(0, vendorMessage.length() - 1) + "行供应商纳税人识别号在供应商信息不存在" : vendorMessage;
		invoiceCertified = i + 1 == length && invoiceCertified.length() > 0
				? "第" + invoiceCertified.substring(0, invoiceCertified.length() - 1) + "行发票号码 发票代码 待审核中"
				: invoiceCertified;
		instTaxNo = i + 1 == length && instTaxNo.length() > 0
				? "第" + instTaxNo.substring(0, instTaxNo.length() - 1) + "行纳税人识别号对应的机构不存在" : instTaxNo;
		resultBusi = i + 1 == length ? vendorMessage + invoiceCertified + instTaxNo : resultBusi;
		mapBusi.put("vendorMessage", vendorMessage);
		mapBusi.put("invoiceCertified", invoiceCertified);
		mapBusi.put("resultBusi", resultBusi);
		mapBusi.put("instTaxNo", instTaxNo);
		return mapBusi;
	}

	/**
	 * @Action 进项税-发票扫描认证 编辑页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String editInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			// 发票主信息
			inputInfo = invoiceScanAuthService.findInvoiceScanAuthByBillId(o_bill_id);
			String bill_code = inputInfo.getBillId();
			String bill_no = inputInfo.getBillCode();
			// 发票商品信息
			lstInputInvoiceItem = invoiceScanAuthService.findInvoiceScanAuthItemsByBillId(o_bill_id);
			if (null != lstInputInvoiceItem && lstInputInvoiceItem.size() > 0 && lstInputInvoiceItem.size() < 8) {
				for (int i = 0; i < lstInputInvoiceItem.size(); i++) {
					InputInvoiceItem inputInvoiceItem = (InputInvoiceItem) lstInputInvoiceItem.get(i);
					if (null != inputInvoiceItem.getGoodsNo() && !"".equals(inputInvoiceItem.getGoodsNo())) {
						if (inputInvoiceItem.getGoodsNo().startsWith(".")) {
							inputInvoiceItem.setGoodsNo("0" + inputInvoiceItem.getGoodsNo());
						}
					}
					if (null != inputInvoiceItem.getGoodsPrice() && !"".equals(inputInvoiceItem.getGoodsPrice())) {
						if (inputInvoiceItem.getGoodsPrice().startsWith(".")) {
							inputInvoiceItem.setGoodsPrice("0" + inputInvoiceItem.getGoodsPrice());
						}
					}
					if (null != inputInvoiceItem.getAmt() && !"".equals(inputInvoiceItem.getAmt())) {
						if (inputInvoiceItem.getAmt().startsWith(".")) {
							inputInvoiceItem.setAmt("0" + inputInvoiceItem.getAmt());
						}
					}
					if (null != inputInvoiceItem.getTaxAmt() && !"".equals(inputInvoiceItem.getTaxAmt())) {
						if (inputInvoiceItem.getTaxAmt().toString().startsWith(".")) {
							inputInvoiceItem.setTaxAmt(
									BigDecimal.valueOf(Double.parseDouble("0" + inputInvoiceItem.getTaxAmt())));
						}
					}
					if (null != inputInvoiceItem.getTaxRate() && !"".equals(inputInvoiceItem.getTaxRate())) {
						if (inputInvoiceItem.getTaxRate().startsWith(".")) {
							inputInvoiceItem.setTaxRate("0" + inputInvoiceItem.getTaxRate());
						}
					}
				}
				for (int i = lstInputInvoiceItem.size(); i < 8; i++) {
					lstInputInvoiceItem.add(new InputInvoiceItem());
				}
			} else if (null == lstInputInvoiceItem || lstInputInvoiceItem.size() == 0) {
				for (int i = 0; i < 8; i++) {
					lstInputInvoiceItem.add(new InputInvoiceItem());
				}
			}

			String datastatus = inputInfo.getBillStatu();
			if ("3".equals(datastatus) || "5".equals(datastatus) || "7".equals(datastatus)) {
				authPassFlag = "1";
			} else {
				authPassFlag = "0";
			}

			List lstDataStatus = DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus = new HashMap();
			for (int i = 0; i < lstDataStatus.size(); i++) {
				SelectTag tag = (SelectTag) lstDataStatus.get(i);
				if (Integer.parseInt(tag.getValue()) > 9) {
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-editInvoiceScanAuth", e);
		}
		return ERROR;
	}

	/**
	 * 进项税-发票扫描认证 商品信息编辑保存处理
	 */
	public void updateInvoiceItemInfo() {
		try {
			InputInvoiceItem item = new InputInvoiceItem();
			item.setGoodsName(request.getParameter("goodsName"));
			item.setSpecandmodel(request.getParameter("specandmodel"));
			item.setGoodsNo(request.getParameter("goodsNo"));
			item.setGoodsPrice(request.getParameter("goodsPrice"));
			item.setAmt(request.getParameter("amt"));
			item.setTaxRate(request.getParameter("taxRate"));
			item.setTaxAmt(new BigDecimal(request.getParameter("taxAmt")));
			item.setBillId(request.getParameter("billId"));

			String billItemId = request.getParameter("billItemId");
			if (null != billItemId && !"".equals(billItemId)) {
				item.setBillItemId(billItemId);
				invoiceScanAuthService.updateVmsInputInvoiceItem(item);
			} else {
				billItemId = invoiceScanAuthService.findSequenceBillItemId();
				item.setBillItemId(billItemId);
				invoiceScanAuthService.insertVmsInputInvoiceItem(item);
			}

			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(billItemId);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}

	/**
	 * 进项税-发票扫描认证 商品信息删除处理
	 */
	public void deleteInvoiceItemInfo() {
		try {
			InputInvoiceItem item = new InputInvoiceItem();
			item.setBillId(request.getParameter("billId"));
			String billItemId = request.getParameter("billItemId");
			item.setBillItemId(billItemId);
			invoiceScanAuthService.deleteVmsInputInvoiceItem(item);

			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(billItemId);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}

	/**
	 * @Action 进项税-发票扫描认证 编辑保存处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String updateInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {

			invoiceScanAuthService.updateVmsInputInvoiceInfoForScanAuth(inputInfo, o_bill_id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-updateInvoiceScanAuth", e);
		}
		return ERROR;
	}

	/**
	 * @Action 进项税-发票扫描认证 查看详情页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String viewInvoiceScanAuth() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			List lstDataStatus = DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus = new HashMap();
			for (int i = 0; i < lstDataStatus.size(); i++) {
				SelectTag tag = (SelectTag) lstDataStatus.get(i);
				if (Integer.parseInt(tag.getValue()) > 9) {
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}

			//inputInfoView = invoiceScanAuthService.findInvoiceScanAuthByBillId(o_bill_id);
			List invoiceScanAuthList = invoiceScanAuthService.findInvoiceScanAuthListByBillId(o_bill_id);
			for(int i=0;i<invoiceScanAuthList.size();i++){
				if(invoiceScanAuthList != null){
					inputInfoView = (InputInfo) invoiceScanAuthList.get(i);
					List<BillDetailEntity> billDetailList = inputInfoView.getBillDetailList();
				}
			}
			//List<BillDetailEntity> billDetailList = inputInfoView.getBillDetailList();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-viewInvoiceScanAuth", e);
		}
		return ERROR;
	}

	public String viewInvoicePic() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-viewInvoicePic", e);
		}
		return ERROR;
	}
	
	/**
	 *数据更新
	 *1.从中间表中查出数据
	 *2.将查出的数据插入到应用表中
	 */
	public String dataUpdate(){
		System.out.println("开始更新数据...");
		List<InputInfo> inputInfo = new ArrayList<InputInfo>();
		List<InputInvoiceNew> inputInvoiceNew = new ArrayList<InputInvoiceNew>();
		
		//从中间表中查数据（主表）
		inputInfo = invoiceScanAuthService.findDataByPrimary();
		//从中间表中查数据（明细表）
		inputInvoiceNew = invoiceScanAuthService.findDataByDetails();
		
		System.out.println("从中间表中获取到数据");
		
		//将数据插入到应用表中（主表）
		if(inputInfo != null){
			invoiceScanAuthService.insertDataByPrimary(inputInfo);
		}
		//将数据插入到应用表中（明细表）
		if(inputInvoiceNew != null){
			invoiceScanAuthService.insertDataByDetails(inputInvoiceNew);
		}
		System.out.println("更新成功...");
		return SUCCESS;
	}
	
	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 功能：通过远程连接总账的数据库(oracle)
	 */
	//start
	public String generalUpdate() {
		try {
			List list =this.getGeneralIedger();
			System.out.println("远程从总账中获取到数据...");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			//获取上月日期
			String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
			System.out.println("上月:"+yearMonth);
			Map monthMap = new HashMap();
			monthMap.put("month", yearMonth);
			invoiceScanAuthService.deleteGeneralLedger(monthMap);
			
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i));
				Map map=(Map) list.get(i);
				
				System.out.println(map);
				invoiceScanAuthService.insertGeneralLedger(map);
			}
		
			System.out.println("总账数据更新成功...");
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		
	}
	private List getGeneralIedger() {
		//获取上月日期
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		System.out.println("开始获取数据...");
		
		Map map = new HashMap();
		map.put("yearMonth", yearMonth);
		List<GeneralLedgerTemp> generalLedgerTempList = getGeneralIedgerService.getGeneralIedgerTempOfLongdistance(map);
		
		List list = new ArrayList();
		
        String mon = yearMonth.split("-")[1];
		if("12".equals(mon)){
			
			map.put("yearMonth", yearMonth.split("-")[0] + "-13");
			List<GeneralLedgerTemp> generalLedgerTempList13 = getGeneralIedgerService.getGeneralIedgerTempOfLongdistance(map);
			map.put("yearMonth", yearMonth.split("-")[0] + "-14");
			List<GeneralLedgerTemp> generalLedgerTempList14 = getGeneralIedgerService.getGeneralIedgerTempOfLongdistance(map);
			map.put("yearMonth", yearMonth.split("-")[0] + "-15");
			List<GeneralLedgerTemp> generalLedgerTempList15 = getGeneralIedgerService.getGeneralIedgerTempOfLongdistance(map);
			
			for(GeneralLedgerTemp list13 : generalLedgerTempList13) {
				list.add(getJdbcGeneral(list13));
			}
			for(GeneralLedgerTemp list14 : generalLedgerTempList14) {
				list.add(getJdbcGeneral(list14));
			}
			for(GeneralLedgerTemp list15 : generalLedgerTempList15) {
				list.add(getJdbcGeneral(list15));
			}

		}
		for(GeneralLedgerTemp list1 : generalLedgerTempList) {
			list.add(getJdbcGeneral(list1));
		}
       
       return list;
	}
	private Map<String,String> getJdbcGeneral(GeneralLedgerTemp generalLedgerTemp){
    	Map<String,String> map=new HashMap<String,String>();
    	map.put("ORIG_INSTCODE", generalLedgerTemp.getINST_ID());  //无意义
    	
    	//2018-07-25新增
    	//2018-08-22修改  刘俊杰
		List authInstList = this.getAuthInstList();
    	for (int j = 0; j < authInstList.size(); j++) {
			Organization org = (Organization) authInstList.get(j);
			if ( generalLedgerTemp.getINST_ID().equals(org.getId())) {  //成本中心代码未放入session中
				Organization orgs = (Organization) authInstList.get(0);
				System.out.println(orgs);
				map.put("INST_ID", orgs.getId());
			}
		}
    	map.put("INST_ID", generalLedgerTemp.getINST_ID());
    	
        String YEAR_MONTH=generalLedgerTemp.getYEAR_MONTH();

        String[] YEAR_MONTH2=YEAR_MONTH.split("-");
        
        String year=YEAR_MONTH2[0];
        String month=YEAR_MONTH2[1];

        if(month.equals("13")||month.equals("14")||month.equals("15")){
        	map.put("YEAR_MONTH", year+"-12");
        	
        }else{
            map.put("YEAR_MONTH", generalLedgerTemp.getYEAR_MONTH());
        }
        map.put("DIRECTION_ID", generalLedgerTemp.getDIRECTION_ID());
        map.put("DIRECTION_NAME", generalLedgerTemp.getDIRECTION_NAME());
        map.put("PLANLONGDESC_ID", generalLedgerTemp.getPLANLONGDESC_ID());
        map.put("PLANLONGDESC_NAME", generalLedgerTemp.getPLANLONGDESC_NAME());
        
        String IS_TAXEXEMPTION=generalLedgerTemp.getIS_TAXEXEMPTION();
        if(IS_TAXEXEMPTION.equals("1")||IS_TAXEXEMPTION.equals("2")||IS_TAXEXEMPTION.equals("3")){
        	
        	map.put("IS_TAXEXEMPTION", "N");
        }else{
        	
        	map.put("IS_TAXEXEMPTION", "Y");
        }
        
        map.put("DEBIT_DESC", generalLedgerTemp.getDEBIT_DESC());
        map.put("CREDIT_DESC", generalLedgerTemp.getCREDIT_DESC());
        map.put("BALANCE_SOURCE", generalLedgerTemp.getBALANCE_SOURCE());
  
        return map;
		
	}
	//end

	// 页面传递参数定义
	private String billDate;// 开票日期[查询]
	private String customerName;// 供应商名称[查询]
	private String datastatus;// 发票状态[查询]
	private String instId;// 所属机构[查询]
	private String instName;
	private String billCode;// 发票代码[查询]
	private String billNo;// 发票号码[查询]
	private String fapiaoType;// 发票种类[查询]
	private String identifyDate;// 认证日期[查询]

	private String billId;// 认证提交
	private String o_bill_id;// 发票号码[查看，编辑]

	private List lstInputInvoiceItem;// [编辑，查看]
	private InputInfo inputInfoView;
	private String currentPage;// 当前页数[编辑，查看]

	private Map mapVatType;// 发票种类下拉列表Map[查询，展示]
	private Map mapDatastatus;// 发票状态下拉列表Map[查询，展示]
	private List lstAuthInstId = new ArrayList();// 所属机构下拉列表[查询]
	private String addGoodNum;

	private String authPassFlag;// 认证是否通过 3,5,7为通过

	public String getAddGoodNum() {
		return addGoodNum;
	}

	public void setAddGoodNum(String addGoodNum) {
		this.addGoodNum = addGoodNum;
	}

	public String getAuthPassFlag() {
		return authPassFlag;
	}

	public void setAuthPassFlag(String authPassFlag) {
		this.authPassFlag = authPassFlag;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getIdentifyDate() {
		return identifyDate;
	}

	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}

	public Map getMapDatastatus() {
		return mapDatastatus;
	}

	public void setMapDatastatus(Map mapDatastatus) {
		this.mapDatastatus = mapDatastatus;
	}

	public List getLstInputInvoiceItem() {
		return lstInputInvoiceItem;
	}

	public void setLstInputInvoiceItem(List lstInputInvoiceItem) {
		this.lstInputInvoiceItem = lstInputInvoiceItem;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getO_bill_id() {
		return o_bill_id;
	}

	public void setO_bill_id(String o_bill_id) {
		this.o_bill_id = o_bill_id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/* service 注入 */
	private InvoiceScanAuthService invoiceScanAuthService;
	private InputInvoiceService inputInvoiceService;
	private VendorInfoService vendorInfoService;
	private InputTransService inputTransService;
	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 功能：通过远程连接总账的数据库(oracle),创建属性及其get,set方法，方便spring注入
	 */
	//start
	private GetGeneralIedgerService getGeneralIedgerService;
	
	public GetGeneralIedgerService getGetGeneralIedgerService() {
		return getGeneralIedgerService;
	}

	public void setGetGeneralIedgerService(GetGeneralIedgerService getGeneralIedgerService) {
		this.getGeneralIedgerService = getGeneralIedgerService;
	}
	//end

	public InvoiceScanAuthService getInvoiceScanAuthService() {
		return invoiceScanAuthService;
	}

	public void setInvoiceScanAuthService(InvoiceScanAuthService invoiceScanAuthService) {
		this.invoiceScanAuthService = invoiceScanAuthService;
	}

	public InputInvoiceService getInputInvoiceService() {
		return inputInvoiceService;
	}

	public void setInputInvoiceService(InputInvoiceService inputInvoiceService) {
		this.inputInvoiceService = inputInvoiceService;
	}

	public VendorInfoService getVendorInfoService() {
		return vendorInfoService;
	}

	public void setVendorInfoService(VendorInfoService vendorInfoService) {
		this.vendorInfoService = vendorInfoService;
	}

	public InputTransService getInputTransService() {
		return inputTransService;
	}

	public void setInputTransService(InputTransService inputTransService) {
		this.inputTransService = inputTransService;
	}

	private LogEmpService logEmpService;

	public LogEmpService getLogEmpService() {
		return logEmpService;
	}

	public void setLogEmpService(LogEmpService logEmpService) {
		this.logEmpService = logEmpService;
	}

	public InputInfo getInputInfo() {
		return inputInfo;
	}

	public void setInputInfo(InputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	public InputInfo getInputInfoView() {
		return inputInfoView;
	}

	public void setInputInfoView(InputInfo inputInfoView) {
		this.inputInfoView = inputInfoView;
	}
	
}
