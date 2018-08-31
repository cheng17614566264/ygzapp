package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.service.InputInnovationService;
import com.cjit.vms.input.service.InputTransService;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.system.service.LogEmpService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.TransTypeService;
import com.cjit.vms.trans.util.CheckUtil;

import cjit.crms.util.ExcelUtil;
import edu.emory.mathcs.backport.java.util.Arrays;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class InputTransAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	// 交易开始日期
	private String transBeginDate;
	// 交易结束日期
	private String transEndDate;
	
	private List<InputInfo> inputTransList;
//	private InputInfo inputTrans;

	private InputTransService inputTransService;
	private InputInnovationService inputInnovationService;

	private String fromFlag;
	protected UserInterfaceConfigService userInterfaceConfigService;
	// 交易流水号
	private String dealNo;
	// 交易种类列表(transKind)
	private List transKindList;
	// 交易类型列表(transType)
	private List transTypeList;
	
	// 交易类型
	private String transType;
	// 供应商纳税人识别号
	private String customerCode;
	// 发票号码
	private String invoiceNo;
	// 发票代码
	private String invoiceCode;
	// 供应商中文名称
	private String vendorName;
	// 交易机构
	private String bankCode;

	private String bankName;
	private LogEmpService logEmpService;

	private String selectedIds;
	private final static String deal_No_file = "文件中交易编号";
	private final static String bill_No_file = "文件中发票代码发票号码";
	private final static String deal_No_Sys = "系统中中交易编号";
	private final static String bill_No_Sys = "系统中发票代码发票号码";

	// 20160511中科软王春燕增加
	// 开票起止日期
	private InputInfo inputInvoiceNew;
	private List inputInvoiceNewList;
	private List industryTypeList;
	private List inputInvoiceEditList;

	private TransTypeService transTypeService;
	// 交易类型设置页面的查询条件对象
	VerificationInfo transTypeCondition = new VerificationInfo();


	public List getInputInvoiceNewList() {
		return inputInvoiceNewList;
	}

	public void setInputInvoiceNewList(List inputInvoiceNewList) {
		this.inputInvoiceNewList = inputInvoiceNewList;
	}

	public List getIndustryTypeList() {
		return industryTypeList;
	}

	public void setIndustryTypeList(List industryTypeList) {
		this.industryTypeList = industryTypeList;
	}

	// 前台传入的发票号码和编码
	private String billNoAndCode;

	public String getBillNoAndCode() {
		return billNoAndCode;
	}

	public void setBillNoAndCode(String billNoAndCode) {
		this.billNoAndCode = billNoAndCode;
	}

	public String listInputTrans() {
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
			inputInvoiceNew = null;
			this.setTransBeginDate(null);
			this.setTransEndDate(null);
			this.setVendorName(null);
			this.setBankCode(null);
			this.setDealNo(null);
			this.paginationList.setCurrentPage(1);
		}
		@SuppressWarnings("rawtypes")
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		InputInfo inputTransPara=new InputInfo();
		inputTransPara.setTransBeginDate(transBeginDate);
		inputTransPara.setTransEndDate(transEndDate);
		inputTransPara.setName(vendorName);
		inputTransPara.setBillInst(bankCode);
		inputTransPara.setLstAuthInstId(lstAuthInstId);
		// 权限控制
		User currentUser = this.getCurrentUser();
		if (currentUser != null) {
			inputTransPara.setUserId(currentUser.getId());
		}
		inputTransList = inputTransService.inputTransList(inputTransPara, paginationList);

		String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
		this.request.setAttribute("currMonth", currMonth);
		this.request.setAttribute("paginationList", paginationList);

		return SUCCESS;
	}

	public String importInputTrans() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportFile(files[0]);
				files = null;
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				this.setResultMessages("上传文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			this.setResultMessages("上传文件失败!");
			return ERROR;
		}
	}

	/**
	 * 解析并存储进项主表信息
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Map<String, String> saveSheet1(Hashtable<String, String[][]> hs,List<Map<String, String>> dataList,List<Map<String, String>> reDataList) throws ParseException {
		List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("INPUT_TRANS_IMP", "", "");
		//用于记录返回的结果
		Map<String, String> resultMap=new HashMap<String, String>();
		String[][] sheet = (String[][]) hs.get("0");
		// 获取表头列表
		String[] heads = sheet[0];
		String result = "";
		// 创建数据List对象
		// 获取每行
		String batchNo = StringUtil.getBatchNo();
		String resultId = "";
		Set<String> set = new HashSet<String>();
		Set<String> repeatId = new HashSet<String>();
		for (int i = 1; i < sheet.length; i++) {
			String[] row = sheet[i];
			Map<String, String> map = new HashMap<String, String>();
			map = CheckUtil.CreatMap(heads, this.dictionaryMap(headList), row);
			// 用于存储发票代码和发票号码
			String id = null;
			if (map.get("billId") != null && map.get("billCode") != null) {
				id = map.get("billId") + "-" + map.get("billCode");
				int size = set.size();
				set.add(id);
				if (set.size() == size) {
					repeatId.add(id);
				}
			}
			// 验证文件格式是否正确
			result = checkformat(map, i, sheet, result);
			dataList.add(map);
		}
		// 验证文件内的交易编号是否重复
		resultId = CheckUtil.checkId(repeatId, deal_No_file);
		result += resultId;
		if (result.length() > 0) {
			resultMap.put("result", result);
			return resultMap;
		}
		//判断发票号码在数据库中是否已存在
		List<String> billInfoList=new ArrayList<String>();
		for(int i = 0; i < dataList.size(); i++){
			Map<String, String> map = dataList.get(i);
			String billInfo=map.get("billId")+map.get("billCode");
			billInfoList.add(billInfo);
		}
		//获取导入信息在数据库中已存在的发票号码代码
		List<String> list=inputTransService.findBillReNo(billInfoList);
		//移除重复的发票信息
		for (String billCode : list) {
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> map=dataList.get(i);
				if (billCode.equals(map.get("billId")+map.get("billCode"))) {
					reDataList.add(dataList.get(i));
					dataList.remove(i);
					break;
				}
			}
		}
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> map = dataList.get(i);
			map.put("batchNo", batchNo);
			resultMap.put(map.get("billId")+map.get("billCode"), "1");
		}
		return resultMap;

	}

	/**
	 * 解析进项明细
	 * 
	 * @param hs
	 * @return
	 */
	private String saveSheet2(Hashtable<String, String[][]> hs,Map<String, String> billMap,Map<String, String> reBillMap,List<Map<String, String>> dataList,List<Map<String, String>> reDataList) {
		List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("INPUT_TRANS_ITEM", "", "");
		String[][] sheet = (String[][]) hs.get("1");
		String[] heads = sheet[0];
		String result = "发票明细,第";
		for (int i = 1; i < sheet.length; i++) {
			String[] row = sheet[i];
			Map<String, String> map = new HashMap<String, String>();
			map = CheckUtil.CreatMap(heads, this.dictionaryMap(headList), row);
			result = checkSheet2format(map, i, sheet, result);
			//如过发票号码和发票代码在主表中，才向明细表中插入该条数据
			if (billMap.get(map.get("billId")+map.get("billCode"))!=null) {
				dataList.add(map);
			}else if (reBillMap.get(map.get("billId")+map.get("billCode"))!=null) {
				reDataList.add(map);
			}
		}
		if (result.length() > 6) {
			result = result.substring(0, result.length() - 1) + "行数据有问题，请检查！";
			return result;
		}
		return "";
	}

	private String checkSheet2format(Map<String, String> map, int i, String[][] sheet, String result) {
		List<String> list = new ArrayList<String>();
		list.add(map.get("billId"));
		list.add(map.get("billCode"));
		list.add(map.get("shareInst"));
		list.add(map.get("amt"));
		list.add(map.get("tax"));
		list.add(map.get("taxRate"));
		list.add(map.get("sumAmt"));
		list.add(map.get("isCredit"));
		boolean notNull = CheckUtil.checkNotNull(list);
		if (!notNull) {
			result = result + i + ",";
		}
		return result;

	}

	/**
	 * 进项EXCEL表解析
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private String doImportFile(File file) throws Exception {

		@SuppressWarnings("unchecked")
		Hashtable<String, String[][]> hs = ExcelUtil.parseExcel(null, file, 2);
		if (hs != null) {
			List<Map<String, String>> sheet1List =new ArrayList<Map<String,String>>();
			//主表中存在的重复数据 
			List<Map<String, String>> sheet1ReList =new ArrayList<Map<String,String>>();
			List<Map<String, String>> sheet2List =new ArrayList<Map<String,String>>();
			//明细表存在的重复数据
			List<Map<String, String>> sheet2ReList =new ArrayList<Map<String,String>>();
			// 存储主表信息
			Map<String,String> billMap=this.saveSheet1(hs,sheet1List,sheet1ReList);
			Map<String, String> reBillMap=new HashMap<String, String>();
			for (Map<String, String> map : sheet1ReList) {
				reBillMap.put(map.get("billId")+map.get("billCode"), "1");
			}
			String result = billMap.get("result");
			if (result!=null) {
				setResultMessages(result);
				return ERROR;
			}
			// 存储明细表信息
			result=this.saveSheet2(hs,billMap,reBillMap,sheet2List,sheet2ReList);
			if (result.length() > 0) {
				setResultMessages(result);
				return ERROR;
			}
			inputTransService.saveInputTransData(sheet1List,sheet2List,sheet1ReList,sheet2ReList);
			setResultMessages("导入成功");
			return SUCCESS;
		}
		return ERROR;

	}

	private Map<String, String> dictionaryMap(List<Dictionary> list) {
		Map<String, String> map = new HashMap<String, String>();
		for (Dictionary dictionary : list) {
			map.put(dictionary.getTypeName(), dictionary.getName());
		}
		return map;
	}

	public Map<String, String> checkInputTrans(Map<String, String> map, Map<String, String> mapcheckBusi,
			String[][] sheet, int i, int j) {
		String resultBusi = StringUtil.isNotEmpty(mapcheckBusi.get("resultBusi")) ? mapcheckBusi.get("resultBusi") : "";
		String billCode = (String) map.get("billCode");
		String billNo = (String) map.get("billNo");
		List<String> list = inputTransService.findInputTransDataByDealNoAndBatchNo(map);
		if (list.size() == 0) {

		}
		if (StringUtil.isNotEmpty(billNo) && StringUtil.isNotEmpty(billCode)) {

		}

		return mapcheckBusi;
	}

	/*
	 * 1 Deal_No 交易编号 VARCHAR2(18) N PK
	 * 
	 * 4 AMT_CNY 金额_人民币 NUMBER(16,2) N 5 TAX_AMT_CNY 税额_人民币 NUMBER(16,2) N 11
	 * Vendor_ID 供应商ID VARCHAR2(40) N
	 */
	private String checkformat(Map<String, String> map, int i, String[][] sheet, String result) {
		List<String> list = new ArrayList<String>();
		list.add(map.get("billId"));
		list.add(map.get("billCode"));
		list.add(map.get("curreny"));
		list.add(map.get("billDate"));
		list.add(map.get("billInst"));
		list.add(map.get("taxNo"));
		list.add(map.get("name"));
		list.add(map.get("purpose"));
		list.add(map.get("billType"));
		Map<String, Boolean> mapCheck = new HashMap<String, Boolean>();
		mapCheck.put("checkNull", CheckUtil.checkNotNull(list));
		mapCheck.put("checkDate", CheckUtil.checkDate(map.get("billDate")));
		result = CheckUtil.checkData(mapCheck, i, result, sheet.length);
		return result;

	}

	/**
	 * 导出供应商
	 * 
	 * @throws Exception
	 */
	public void exportInputTrans() throws Exception {
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
			inputInvoiceNew = null;
			this.setTransBeginDate(null);
			this.setTransEndDate(null);
			this.setVendorName(null);
			this.setBankCode(null);
			this.setDealNo(null);
			this.paginationList.setCurrentPage(1);
		}

		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);

		InputTrans inputTransPara = new InputTrans();
		inputTransPara.setTransBeginDate(transBeginDate);
		inputTransPara.setTransEndDate(transEndDate);
		inputTransPara.setVendorCname(vendorName);
		inputTransPara.setBankCode(bankCode);
		inputTransPara.setDealNo(dealNo);
		inputTransPara.setLstAuthInstId(lstAuthInstId);

		// 权限控制
		User currentUser = this.getCurrentUser();
		if (currentUser != null) {
			inputTransPara.setUserId(currentUser.getId());
		}
		List objList = userInterfaceConfigService.getDictionarys1("INPUT_TRANS_IMP", "", "1");
		int count = objList.size();
		String sql = " ";
		for (int i = 0; i < count; i++) {
			String column = ((Dictionary) objList.get(i)).getValueStandardLetter();
			if (i < count - 1) {
				sql = sql + "VIT." + column + ",";
			} else {
				sql = sql + "VIT." + column;
			}
		}
		inputTransList = inputTransService.inputTransListExport(inputTransPara, paginationList, sql);

		String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
		this.request.setAttribute("currMonth", currMonth);

		StringBuffer fileName = new StringBuffer("进项交易信息表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, inputTransList, objList);
		os.flush();
		os.close();

	}

	private void writeToExcel(OutputStream os, List content, List objList) throws IOException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		Map map = new HashMap();
		Map map1 = new HashMap();
		map1 = (Map) content.get(0);
		Set<String> keySet = map1.keySet();
		String name1 = "";
		for (String str : keySet) {
			name1 = name1 + str + ",";
		}
		String[] array = (String[]) name1.split(",");
		ws = wb.createSheet("进项交易信息", 0);
		int count1 = objList.size();
		for (int k = 0; k < array.length; k++) {

			for (int i = 0; i < count1; i++) {
				if (array[k].equalsIgnoreCase(((Dictionary) objList.get(i)).getValueStandardLetter())) {
					String name = ((Dictionary) objList.get(i)).getTypeName();
					ws.addCell(new Label(k, 0, name, JXLTool.getHeader()));
				}
			}
		}

		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			int column = count++;
			map = (Map) content.get(c);
			setWritableSheet(ws, map, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, Map map, int column) throws WriteException {
		int i = 0;
		Set<String> keySet = map.keySet();
		for (String str : keySet) {
			String name = map.get(str) + " ";
			ws.addCell(new Label(i++, column, name, JXLTool.getContentFormat()));

		}
		Object[] values = map.values().toArray();
		String value;
		for (int z = 0; z < values.length; z++) {
			if (values[z] == null) {
				value = " ";
			} else {
				value = values[z].toString() + "";
			}
			ws.addCell(new Label(z, column, value, JXLTool.getContentFormat()));
		}

	}

	/**
	 * 发票查询
	 * 
	 * 20160511中科软
	 */
	public String listInputInvoiceNew() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User currentUser = this.getCurrentUser();
		if (currentUser == null) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		try {
			// 获取行业类型
			industryTypeList = inputTransService.findIndustryTypeList();

			String userId = currentUser.getId();
			Map map = new HashMap();
			List lstAuthInstId = organizationService.findUserOrganization(currentUser, map);
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				this.paginationList.setCurrentPage(1);
				InputInfo inputInvoiceNewTemp = new InputInfo();
				inputInvoiceNewTemp.setUserId(userId);
				inputInvoiceNewTemp.setLstAuthInstId(lstAuthInstId);
				inputInvoiceNewList = inputTransService.inputInvoiceNewList(inputInvoiceNewTemp, paginationList);
			} else {
				this.inputInvoiceNew.setLstAuthInstId(lstAuthInstId);

				this.inputInvoiceNew.setUserId(userId);
				inputInvoiceNewList = inputTransService.inputInvoiceNewList(inputInvoiceNew, paginationList);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0001.0012", "进项税管理", "", "发票管理", "0");
			log.error("InputTransAction-listInputInvoiceNew", e);
		}
		return ERROR;
	}

	public String doImportInvoiceNewFile(File file) throws Exception {
		List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("INPUT_INVOICENEW_IMP", "", "");

		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		Map<String, String> mapBusi = new HashMap<String, String>();

		if (hs != null) {
			String[][] sheet = (String[][]) hs.get("0");
			// 获取表头列表
			String[] heads = sheet[0];
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			String result = "";
			String resultBusi = "";
			// 创建数据List对象
			// 获取每行
			String batchNo = StringUtil.getBatchNo();
			String resultId = "";
			Map<String, Boolean> m = new HashMap<String, Boolean>();
			Set<String> set = new HashSet<String>();
			String startDate = StringUtil.getCurrentDate();
			for (int i = 1; i < sheet.length; i++) {
				String[] row = sheet[i];
				Map<String, String> map = new HashMap<String, String>();
				map = CheckUtil.CreatMap(heads, headList, row);
				// set=CheckUtil.checkIdNo(m, map.get("billCode"),
				// map.get("billNo"),set);

				// 验证文件格式是否正确
				result = checkNewformat(map, i, sheet, result);
				dataList.add(map);
			}
			if (dataList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < dataList.size(); j++) {
					Map<String, String> map = dataList.get(j);
					System.out.println("做一个重复判断！");
					// StringBuffer sb = new StringBuffer();

					String bc = map.get("billCode");
					String bc1 = map.get("billNo");

					System.out.println(bc);
					System.out.println(bc1);

					List<String> instList = inputTransService.findDup(map);
					/*
					 * select u.bill_Code，u.bill_No from vms_input_invoice_new u
					 * where u.billCode = #billCode# and u.bill_No = #billNo#
					 */

					System.out.println("执行查询");
					int s = instList.size();
					System.out.println(s);

					if (instList.size() > 0) {

						System.out.println("进入if");

						/*
						 * setResultMessages("发票代码:"+
						 * map.get("billCode")+"发票号码:"+
						 * map.get("billNo")+"已存在，不可重复插入;\\n");
						 */

						System.out.println("是否有弹框");

					} else {
						System.out.println("可插入！");
						inputTransService.saveInputInvoiceNewData(map);
						System.out.println("保存数据成功！");

					}
				}

			}
			/* setResultMessages("导入成功"); */
			{
				LogEmp log = new LogEmp();
				log.setSuccessNo(Integer.toString(dataList.size()));
				log.setBatchNo(batchNo);
				log.setEndDate(StringUtil.getCurrentDate());
				log.setFailedNo(Integer.toString(0));
				log.setId(StringUtil.getUUID());
				log.setFailedLog("");
				log.setStartDate(startDate);
				log.setSys("进项票据信息");
				logEmpService.saveLogEmp(log);
			}
			return SUCCESS;

		}

		return ERROR;
	}

	public String importInvoiceNew() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportInvoiceNewFile(files[0]);
				// this.setResultMessages("上传文件完成!");
				files = null;
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				this.setResultMessages("上传文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			this.setResultMessages("上传文件失败!");
			return ERROR;
		}
	}

	/*
	 * 进项导入 格式校验
	 * 
	 * 1 instId 机构编号 VARCHAR2(18) N
	 * 
	 * 
	 */
	private String checkNewformat(Map<String, String> map, int i, String[][] sheet, String result) {
		List<String> list = new ArrayList<String>();
		list.add(map.get("instId"));
		list.add(map.get("instName"));
		list.add(map.get("billCode"));
		list.add(map.get("billNo"));
		list.add(map.get("taxNo"));
		list.add(map.get("industryType"));
		list.add(map.get("vendorName"));
		list.add(map.get("amt"));
		list.add(map.get("taxRate"));
		list.add(map.get("taxAmt"));
		list.add(map.get("amtTaxSum"));
		list.add(map.get("dataStatus"));
		Map<String, Boolean> mapCheck = new HashMap<String, Boolean>();
		mapCheck.put("checkNull", CheckUtil.checkNotNull(list));
		mapCheck.put("checkDate", CheckUtil.checkDate(map.get("billDate")));
		// mapCheck.put("checkDate", CheckUtil.checkDate(map.get("deducDate")));
		result = CheckUtil.checkData(mapCheck, i, result, sheet.length);
		return result;

	}

	/**
	 * 查看进项票据信息明细
	 * @return
	 */
	public String inputTransDetail() {
		// 获取行业类型
		industryTypeList = inputTransService.findIndustryTypeList();

		User currentUser = this.getCurrentUser();
		if (currentUser == null) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		Map<String,String> map = new HashMap<String,String>();
		String billCode=request.getParameter("billCode");
		String billId=request.getParameter("billId");
		map.put("billCode", billCode);
		map.put("billId", billId);
		inputInvoiceNew = inputTransService.findInputTransById(map);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: saveInvoiceNew|描述: 保存新增
	 * </p>
	 * 
	 * @return
	 */
	public String saveInvoiceNew() {
		// 获取行业类型
		industryTypeList = inputTransService.findIndustryTypeList();
		String BillCode = request.getParameter("billCode");
		String BillNo = request.getParameter("billNo");
		String InstId = request.getParameter("instId");
		System.out.println("BillNo===" + BillNo);
		List<String> invoiceList = null;
		if (StringUtil.isNotEmpty(BillCode) && StringUtil.isNotEmpty(BillNo)) {
			invoiceList = inputTransService.findInvoiceNewByCodeNo(BillCode, BillNo);
		} else {
			setResultMessages("发票编码及发票号码不可为空;请先确认");
			return SUCCESS;
		}
		System.out.println("invoiceList.size()===" + invoiceList.size());
		System.out.println(invoiceList.size() <= 0);
		try {
			if (invoiceList.size() <= 0) {
				inputInvoiceNew = new InputInfo();
				inputInvoiceNew.setBillInst(InstId);
				String instName = request.getParameter("instName");
//				inputInvoiceNew.setInstName(instName);
				inputInvoiceNew.setBillCode(BillCode);
				inputInvoiceNew.setBillId(BillNo);
//				inputInvoiceNew.setTransferRatio(
//						BigDecimal.valueOf(Double.parseDouble(request.getParameter("transferRatio"))));
//				inputInvoiceNew
//						.setTransferAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("transferAmt"))));
//
//				if (request.getParameter("purposeCode") != null && !"".equals(request.getParameter("purposeCode"))) {
//					inputInvoiceNew.setPorpuseCode(request.getParameter("purposeCode"));
//				}
//
//				if (request.getParameter("dataStatus") != null && !"".equals(request.getParameter("dataStatus"))) {
//					inputInvoiceNew.setDataStatus(request.getParameter("dataStatus"));
//				}
//
//				if (request.getParameter("vendorName") != null && !"".equals(request.getParameter("vendorName"))) {
//					inputInvoiceNew.setVendorName(request.getParameter("vendorName"));
//				}
//
//				if (request.getParameter("TaxNo") != null && !"".equals(request.getParameter("TaxNo"))) {
//					inputInvoiceNew.setTaxNo(request.getParameter("TaxNo"));
//				}
//
//				if (request.getParameter("amt") != null && !"".equals(request.getParameter("amt"))) {
//					inputInvoiceNew.setAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("amt"))));
//				}
//				if (request.getParameter("taxAmt") != null && !"".equals(request.getParameter("taxAmt"))) {
//					inputInvoiceNew.setTaxAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("taxAmt"))));
//				}
//
//				String amtRate = request.getParameter("amtRate");
//				if (amtRate != null && !"".equals(amtRate)) {
//					amtRate = amtRate.replace("%", "");
//					inputInvoiceNew.setTaxRate(BigDecimal.valueOf(Double.parseDouble(amtRate) / 100));
//				}
//				if (request.getParameter("amtTaxAmt") != null && !"".equals(request.getParameter("amtTaxAmt"))) {
//					inputInvoiceNew
//							.setAmtTaxSum(BigDecimal.valueOf(Double.parseDouble(request.getParameter("amtTaxAmt"))));
//				}
//				if (request.getParameter("billDate") != null && !"".equals(request.getParameter("billDate"))) {
//					inputInvoiceNew.setBillDate(request.getParameter("billDate"));
//				}
//				if (request.getParameter("auditDate") != null && !"".equals(request.getParameter("auditDate"))) {
//					inputInvoiceNew.setAuditDate(request.getParameter("auditDate"));
//				}
//				if (request.getParameter("directionId") != null && !"".equals(request.getParameter("directionId"))) {
//					inputInvoiceNew.setDirectionId(request.getParameter("directionId"));
//				}
//
//				if (request.getParameter("billType") != null && !"".equals(request.getParameter("billType"))) {
//					inputInvoiceNew.setBillType(request.getParameter("billType"));
//				}
//
//				if (request.getParameter("directionName") != null
//						&& !"".equals(request.getParameter("directionName"))) {
//					inputInvoiceNew.setDirectionName(request.getParameter("directionName"));
//				}
//				if (request.getParameter("trans_item") != null && !"".equals(request.getParameter("trans_item"))) {
//					inputInvoiceNew.setTemp2(request.getParameter("trans_item"));
//				}
//
//				if (request.getParameter("industry") != null && !"".equals(request.getParameter("industry"))) {
//					inputInvoiceNew.setIndustryType(request.getParameter("industry"));
//				}
//				inputInvoiceNew.setTransferStatus("0");
//				inputInvoiceNew.setDataSource("3");
//
//				inputTransService.saveInvoiceNew(inputInvoiceNew);

				// }else{
				// setResultMessages("发票编码及发票号码信息已存在;请先确认");
				// }
				setResultMessages("新增进项信息提交成功!");
			} else {
				setResultMessages("发票编码及发票号码信息已存在!请重新输入!");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			setResultMessages("系统出错！保存失败!");
			return ERROR;
		}
		return SUCCESS;

	}

	/**
	 * 进项发票导出 20160511中科软王春燕增加
	 * 
	 * @throws Exception
	 */
	public void exportInputInvoiceNew() throws Exception {
		// 权限控制
		User currentUser = this.getCurrentUser();
		String userId = "";
		if (currentUser != null) {
			userId = currentUser.getId();
		}

		Map map = new HashMap();
		List lstAuthInstId = organizationService.findUserOrganization(currentUser, map);
		if ("menu".equalsIgnoreCase(fromFlag)) {

			fromFlag = null;
			this.paginationList.setCurrentPage(1);
			InputInvoiceNew inputInvoiceNewTemp = new InputInvoiceNew();
			inputInvoiceNewTemp.setUserId(userId);
			inputInvoiceNewTemp.setLstAuthInstId(lstAuthInstId);
//			inputInvoiceNewList = inputTransService.inputInvoiceNewList(inputInvoiceNewTemp, paginationList);
		} else {
			this.inputInvoiceNew.setLstAuthInstId(lstAuthInstId);
			this.inputInvoiceNew.setUserId(userId);
			inputInvoiceNewList = inputTransService.inputInvoiceNewList(inputInvoiceNew, paginationList);
		}

		String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
		this.request.setAttribute("currMonth", currMonth);

		StringBuffer fileName = new StringBuffer("进项发票表");
		fileName.append(".xls");
		// String name = "attachment;filename=" +
		// URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		String name = "attachment;filename=" + new String(fileName.toString().getBytes("gb2312"), "ISO-8859-1");
		// String name = "attachment;filename=" +
		// HttpUtility.UrlEncode(fileName,
		// System.Text.Encoding.UTF8).toString();
		// String name=setFileHeader(request,response,fileName.toString());
		System.out.println("filename:" + name);
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		// writeToExcelNew(os, inputInvoiceNewList, headerList);
		writeToExcelNew(os, inputInvoiceNewList);
		os.flush();
		os.close();
	}

	// 20160511中科软王春燕增加
	private void writeToExcelNew(OutputStream os, List content) throws IOException, JXLException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项发票", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
		excelInfo.setBgColor(Colour.YELLOW2);
		excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(i++, 0, "机构代码", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(i++, 0, "机构名称", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(i++, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(i++, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(i++, 0, "供应商名称", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(i++, 0, "供应商纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(i++, 0, "金额", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(i++, 0, "税率", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(i++, 0, "税额", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(i++, 0, "发票总额", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(i++, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header12 = new Label(i++, 0, "发票状态", JXLTool.getHeaderC(excelInfo));
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

		JXLTool.setAutoWidth(ws, setwriteWidth(content));

		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			int column = count++;
			InputInvoiceNew inputInvoiceNew = (InputInvoiceNew) content.get(c);
			if (count > 65535) {
				setResultMessages("导出数据超过excel最大承受量");
				break;
			}
			setWritableSheetNew(ws, inputInvoiceNew, column);
		}
		wb.write();
		wb.close();

	}

	// 20160511中科软王春燕增加
	public List setwriteWidth(List list) throws JXLException {
		List rowlist = null;
		List sheetList = new ArrayList();
		InputInvoiceNew inputInvoiceNew = null;
		for (int i = 0; i < list.size(); i++) {
			rowlist = new ArrayList();
			inputInvoiceNew = (InputInvoiceNew) list.get(i);
			rowlist.add(inputInvoiceNew.getInstId());
			rowlist.add(inputInvoiceNew.getInstName());
			rowlist.add(inputInvoiceNew.getBillCode());
			rowlist.add(inputInvoiceNew.getBillNo());
			rowlist.add(inputInvoiceNew.getVendorName());
			rowlist.add(inputInvoiceNew.getTaxNo());
			rowlist.add(inputInvoiceNew.getAmt() + "");
			rowlist.add(inputInvoiceNew.getTaxRate() + "");
			rowlist.add(inputInvoiceNew.getTaxAmt() + "");
			rowlist.add(inputInvoiceNew.getAmtTaxSum() + "");
			rowlist.add(inputInvoiceNew.getBillDate());
			rowlist.add(inputInvoiceNew.getDataStatus());

			sheetList.add(rowlist);
		}
		return sheetList;
	}

	/**
	 * 20160511中科软王春燕增加
	 * 
	 * 导出除表头外的数据
	 * 
	 * @return
	 */
	private void setWritableSheetNew(WritableSheet ws, InputInvoiceNew info, int column) throws WriteException {
		// 发票代码
		Label cell1 = new Label(0, column, info.getInstId(), JXLTool.getContentFormat());
		// 发票号码
		Label cell2 = new Label(1, column, info.getInstName(), JXLTool.getContentFormat());
		// 开票日期
		Label cell3 = new Label(2, column, info.getBillCode(), JXLTool.getContentFormat());
		// 所属机构
		Label cell4 = new Label(3, column, info.getBillNo(), JXLTool.getContentFormat());
		// 供应商名称
		Label cell5 = new Label(4, column, info.getVendorName(), JXLTool.getContentFormat());
		// 供应商纳税人识别号
		Label cell6 = new Label(5, column, info.getTaxNo(), JXLTool.getContentFormat());
		// 供应商名称
		Label cell7 = new Label(6, column, info.getAmt().toString(), JXLTool.getContentFormat());
		//
		Label cell8 = new Label(7, column, info.getTaxRate().toString(), JXLTool.getContentFormat());
		// 税额
		Label cell9 = new Label(8, column, info.getTaxAmt().toString(), JXLTool.getContentFormat());
		// 价税合计
		Label cell10 = new Label(9, column, info.getAmtTaxSum().toString(), JXLTool.getContentFormat());
		// 开票日期
		Label cell11 = new Label(10, column, info.getBillDate(), JXLTool.getContentFormat());
		// 发票状态
		Label cell12 = new Label(11, column, info.getDataStatus(), JXLTool.getContentFormat());

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

	private void writeToExcelNew2(OutputStream os, List content, List objList) throws IOException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项发票", 0);

		Label header1 = new Label(i++, 0, "机构代码", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "机构名称", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "行业类型", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "供应商名称", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "金额", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "税率", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "发票总额", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "是否认证通过", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "抵扣日期", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "是否属于购建不动产并以此性质抵扣的进项", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "抵扣比例", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "抵扣金额", JXLTool.getHeader());
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

		// 取出表头字段，自动写表头方式，待研究
		// List headerList =
		// userInterfaceConfigService.getDictionarys1("INPUT_INVOICENEW_IMP",
		// "", "");
		// int count = objList.size();
		// String sql = " ";
		// for (int i = 0; i < count; i++)
		// {
		// String column = ((Dictionary)
		// objList.get(i)).getValueStandardLetter();
		// if (i < count - 1)
		// {
		// sql = sql + "VIT." + column + ",";
		// } else
		// {
		// sql = sql + "VIT." + column;
		// }
		// }
		// inputTransList =
		// inputTransService.inputTransListExport(inputTransPara,
		// paginationList, sql);
		// JXLTool.setAutoWidth(ws, setwriteWidth(content));

		// Dictionary dictionary = new Dictionary();
		// String typename = "";
		// for (int j = 0; j < objList.size(); j++)
		// {
		// dictionary = (Dictionary) objList.get(j);
		// typename = dictionary.getTypeName();
		// Label header = new Label(j, 0, typename, JXLTool.getHeader());
		// ws.addCell(header);
		// }

		int count = 1;
		Map map = new HashMap();
		for (int c = 0; c < content.size(); c++) {
			int column = count++;
			InputInvoiceNew inputInvoiceNew = (InputInvoiceNew) content.get(c);
			setWritableSheetNew(ws, inputInvoiceNew, column);
		}
		wb.write();
		wb.close();
		// JXLTool.setAutoWidth(ws, setwriteWidth(content));

		// WritableWorkbook wb = Workbook.createWorkbook(os);
		// WritableSheet ws = null;
		// Map map = new HashMap();
		// Map map1 = new HashMap();
		// map1 = (Map) content.get(0);
		// Set<String> keySet = map1.keySet();
		// String name1 = "";
		// for (String str : keySet)
		// {
		// name1 = name1 + str + ",";
		// }
		// String[] array = (String[]) name1.split(",");
		// ws = wb.createSheet("进项发票", 0);
		// int count1 = objList.size();
		// for (int k = 0; k < array.length; k++)
		// {
		// for (int i = 0; i < count1; i++)
		// {
		// if (array[k].equalsIgnoreCase(((Dictionary)
		// objList.get(i)).getValueStandardLetter()))
		// {
		// String name = ((Dictionary) objList.get(i)).getTypeName();
		// ws.addCell(new Label(k, 0, name, JXLTool.getHeader()));
		// }
		// }
		// }

	}

	private void setWritableSheetNew2(WritableSheet ws, Map map, int column) throws WriteException {
		int i = 0;
		Set<String> keySet = map.keySet();
		for (String str : keySet) {
			String name = map.get(str) + " ";
			ws.addCell(new Label(i++, column, name, JXLTool.getContentFormat()));

		}
		Object[] values = map.values().toArray();
		String value;
		for (int z = 0; z < values.length; z++) {
			if (values[z] == null) {
				value = " ";
			} else {
				value = values[z].toString() + "";
			}
			ws.addCell(new Label(z, column, value, JXLTool.getContentFormat()));
		}

		// Label cell1 = new Label(i++, column, trans.getDealNo(),
		// JXLTool.getContentFormat());
		// Label cell2 = new Label(i++, column, trans.getTransDate(),
		// JXLTool.getContentFormat());
		// Label cell3 = new Label(i++, column, trans.getBankCode(),
		// JXLTool.getContentFormat());
		// Label cell4 = new Label(i++, column,
		// trans.getBillNo(),JXLTool.getContentFormat());
		// Label cell5 = new Label(i++, column,
		// trans.getBillCode(),JXLTool.getContentFormat());
		// Label cell6 = new Label(i++, column,
		// trans.getRemark(),JXLTool.getContentFormat());
		// Label cell7 = new Label(i++, column, trans.getAmtCny() +
		// "",JXLTool.getContentFormat());
		// Label cell8 = new Label(i++, column, trans.getTaxAmtCny() + "",
		// JXLTool.getContentFormat());
		// Label cell9 = new Label(i++, column, trans.getIncomeCny() + "",
		// JXLTool.getContentFormat());
		// Label cell10 = new Label(i++, column, trans.getSurtax1AmtCny() + "",
		// JXLTool.getContentFormat());
		// Label cell11 = new Label(i++, column, trans.getSurtax2AmtCny() + "",
		// JXLTool.getContentFormat());
		// Label cell12 = new Label(i++, column, trans.getSurtax3AmtCny() + "",
		// JXLTool.getContentFormat());
		// Label cell13 = new Label(i++, column, trans.getSurtax4AmtCny() + "",
		// JXLTool.getContentFormat());
		// Label cell14 = new Label(i++, column, trans.getVendorTaxno(),
		// JXLTool.getContentFormat());

		// ws.addCell(cell1);
		// ws.addCell(cell2);
		// ws.addCell(cell3);
		// ws.addCell(cell4);
		// ws.addCell(cell5);
		// ws.addCell(cell6);
		// ws.addCell(cell7);
		// ws.addCell(cell8);
		// ws.addCell(cell9);
		// ws.addCell(cell10);
		// ws.addCell(cell11);
		// ws.addCell(cell12);
		// ws.addCell(cell13);
		// ws.addCell(cell14);
	}

	// 中科软李松
	public String deleteInputTransNew() {
		String codeAndNos[] = selectedIds.split(",");
		@SuppressWarnings("unchecked")
		List<String> codeAndNoList = Arrays.asList(codeAndNos);
		Map<String,List<String>> findbyCodeAndNo = new HashMap<String, List<String>>();
		findbyCodeAndNo.put("noList", codeAndNoList);
		inputTransService.deleteInputTransNew(findbyCodeAndNo);
		return SUCCESS;
	}

	// 编辑 新增
	public String editInputTransNew() {

		inputInvoiceNew = inputTransService.findInputTransByBillNoAndCode(billNoAndCode);
		request.setAttribute("inputInvoiceNew", inputInvoiceNew);
		return SUCCESS;
	}

//	public String saveInputTransNew() {
//
//		User currentUser = this.getCurrentUser();
//		if (currentUser == null) {
//			request.setAttribute("msg", "用户失效");
//			return ERROR;
//		}
//
//		String userId = currentUser.getId();
//		inputInvoiceNew = new InputInvoiceNew();
//		inputInvoiceNew.setPk(request.getParameter("pk"));
//		if (request.getParameter("instName") != null && !"".equals(request.getParameter("instName"))) {
//			String instCodeName = request.getParameter("instName");
//			inputInvoiceNew.setInstName(instCodeName);
//		}
//		if (request.getParameter("instId") != null && !"".equals(request.getParameter("instId"))) {
//			inputInvoiceNew.setInstId(request.getParameter("instId"));
//		}
//		if (request.getParameter("billCode") != null && !"".equals(request.getParameter("billCode"))) {
//			inputInvoiceNew.setBillCode(request.getParameter("billCode"));
//		}
//		if (request.getParameter("billNo") != null && !"".equals(request.getParameter("billNo"))) {
//			inputInvoiceNew.setBillNo(request.getParameter("billNo"));
//		}
//
//		if (request.getParameter("porpuseCode") != null && !"".equals(request.getParameter("porpuseCode"))) {
//			inputInvoiceNew.setPorpuseCode(request.getParameter("porpuseCode"));
//		}
//		if (request.getParameter("vendorCode") != null && !"".equals(request.getParameter("vendorCode"))) {
//			inputInvoiceNew.setVendorName(request.getParameter("vendorCode"));
//		}
//		if (request.getParameter("vendorName") != null && !"".equals(request.getParameter("vendorName"))) {
//			inputInvoiceNew.setVendorName(request.getParameter("vendorName"));
//		}
//		if (request.getParameter("taxNo") != null && !"".equals(request.getParameter("taxNo"))) {
//			inputInvoiceNew.setTaxNo(request.getParameter("taxNo"));
//		}
//		if (request.getParameter("amt") != null && !"".equals(request.getParameter("amt"))) {
//			inputInvoiceNew.setAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("amt"))));
//		}
//		if (request.getParameter("taxRate") != null && !"".equals(request.getParameter("taxRate"))) {
//			inputInvoiceNew.setTaxRate(BigDecimal.valueOf(Double.parseDouble(request.getParameter("taxRate"))));
//		}
//		if (request.getParameter("amtTaxSum") != null && !"".equals(request.getParameter("amtTaxSum"))) {
//			inputInvoiceNew.setAmtTaxSum(BigDecimal.valueOf(Double.parseDouble(request.getParameter("amtTaxSum"))));
//		}
//		if (request.getParameter("directionID") != null && !"".equals(request.getParameter("directionID"))) {
//			inputInvoiceNew.setDirectionId(request.getParameter("directionID"));
//		}
//		if (request.getParameter("directionName") != null && !"".equals(request.getParameter("directionName"))) {
//			inputInvoiceNew.setDirectionName(request.getParameter("directionName"));
//		}
//		if (request.getParameter("taxAmt") != null && !"".equals(request.getParameter("taxAmt"))) {
//			inputInvoiceNew.setTaxAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("taxAmt"))));
//		}
//		if (request.getParameter("billDate") != null && !"".equals(request.getParameter("billDate"))) {
//			inputInvoiceNew.setBillDate(request.getParameter("billDate"));
//		}
//		if (request.getParameter("billType") != null && !"".equals(request.getParameter("billType"))) {
//			inputInvoiceNew.setBillType(request.getParameter("billType"));
//		}
//
//		if (request.getParameter("trans_item") != null && !"".equals(request.getParameter("trans_item"))) {
//			inputInvoiceNew.setTemp2(request.getParameter("trans_item"));
//		}
//
//		if (request.getParameter("dataStatus") != null && !"".equals(request.getParameter("dataStatus"))) {
//			inputInvoiceNew.setDataStatus(request.getParameter("dataStatus"));
//		}
//		if (request.getParameter("transferRatio") != null && !"".equals(request.getParameter("transferRatio"))) {
//			inputInvoiceNew
//					.setTransferRatio(BigDecimal.valueOf(Double.parseDouble(request.getParameter("transferRatio"))));
//		}
//		if (request.getParameter("transferAmt") != null && !"".equals(request.getParameter("transferAmt"))) {
//			inputInvoiceNew.setTransferAmt(BigDecimal.valueOf(Double.parseDouble(request.getParameter("transferAmt"))));
//		}
//		if (request.getParameter("auditDate") != null && !"".equals(request.getParameter("auditDate"))) {
//			inputInvoiceNew.setAuditDate(request.getParameter("auditDate"));
//		}
//		inputTransService.saveInputTransNew(inputInvoiceNew);
//		return SUCCESS;
//	}

//	public String editInputTrans() {
//		inputInvoiceNew = inputTransService.findInputTransById(dealNo);
//		return SUCCESS;
//	}

	public String saveInputTrans() {
		inputTransService.saveInputTrans(inputInvoiceNew);
		return SUCCESS;
	}

	public String inputInvoiceEditList() {
		inputInvoiceEditList = inputTransService.findInputTransEditById(billNoAndCode, paginationList);
		// request.setAttribute("paginationList", paginationList);
		System.out.println("1111===========" + paginationList.getRecordCount());
		return SUCCESS;
	}

	public String deleteInputTrans() {
		String selectedIds =  this.selectedIds.replace(",", "','") ;
		selectedIds=selectedIds.replaceAll("'", "");
		inputTransService.deleteInputTrans(selectedIds);
		return SUCCESS;
	}

	public List getInputTransList() {
		return inputTransList;
	}

	public void setInputTransList(List inputTransList) {
		this.inputTransList = inputTransList;
	}


	public InputInnovationService getInputInnovationService() {
		return inputInnovationService;
	}

	public void setInputInnovationService(InputInnovationService inputInnovationService) {
		this.inputInnovationService = inputInnovationService;
	}

	public List getTransTypeList() {
		return transTypeList;
	}

	public void setTransTypeList(List transTypeList) {
		this.transTypeList = transTypeList;
	}

	public InputTransService getInputTransService() {
		return inputTransService;
	}

	public void setInputTransService(InputTransService inputTransService) {
		this.inputTransService = inputTransService;
	}

	public String getFromFlag() {
		return fromFlag;
	}

	public void setFromFlag(String fromFlag) {
		this.fromFlag = fromFlag;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public List getTransKindList() {
		return transKindList;
	}

	public void setTransKindList(List transKindList) {
		this.transKindList = transKindList;
	}

	public String getTransBeginDate() {
		return transBeginDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}


	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public LogEmpService getLogEmpService() {
		return logEmpService;
	}

	public void setLogEmpService(LogEmpService logEmpService) {
		this.logEmpService = logEmpService;
	}

	public String inputInvoiceNewList() {
		inputInvoiceNew = inputTransService.findInputTransByBillNoAndCode(billNoAndCode);
		return SUCCESS;
	}

	public List getInputInvoiceEditList() {
		return inputInvoiceEditList;
	}

	public void setInputInvoiceEditList(List inputInvoiceEditList) {
		this.inputInvoiceEditList = inputInvoiceEditList;
	}



	public InputInfo getInputInvoiceNew() {
		return inputInvoiceNew;
	}

	public void setInputInvoiceNew(InputInfo inputInvoiceNew) {
		this.inputInvoiceNew = inputInvoiceNew;
	}

	public String getDealNo() {
		return dealNo;
	}

	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}

}
