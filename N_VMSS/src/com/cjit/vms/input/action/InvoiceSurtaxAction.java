package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sound.midi.VoiceStatus;

import org.apache.poi.hssf.record.CalcCountRecord;
import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.tools.ant.taskdefs.Sleep;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInfoUtil;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.Proportionality;
import com.cjit.vms.input.model.ProportionalityMore;
import com.cjit.vms.input.model.SubjectDic;
import com.cjit.vms.input.model.SubjectEntity;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.InvoiceSurtaxService;
import com.cjit.vms.input.service.PullDataService;
import com.cjit.vms.input.util.Util;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.util.ExcelUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.ImageUtil;
import com.cjit.vms.trans.util.JSPUtil;

import edu.emory.mathcs.backport.java.util.Arrays;
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
import net.sf.json.JSONObject;

/**
 * 票据管理：进项税转出Action类
 * 
 * @author jobell
 */ 
public class InvoiceSurtaxAction extends DataDealAction{

	private static final long serialVersionUID = 1L;
	private InputInfo inputInfo=new InputInfo();
	/**
	 * 新增
	 * 日期：2018-08-20
	 * 作者：刘俊杰
	 */
	private String result;  //用于ajax返回json
	private Map<String,String> generalLedgerResult = new HashMap<String,String>();  //存储比例计算值
	private Map<String,String> generalLedgerResultOfHand = new HashMap<String,String>();  //存储手动调整值
	private String generalLedgerMoneyOfAllCount;
	
	/**
	 * 新增
	 * 日期：2018-08-27
	 * 作者：刘俊杰
	 * 功能：存储查询到的科目字典信息
	 */
	private List<SubjectDic> subjectDicList;
	//end
	
	
	/**
	 * 票据管理:进项税转出查询页面
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public String listInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			this.getAuthInstList(lstAuthInstId);
			inputInfo.setLstAuthInstId(lstAuthInstId);
			//inputInfo.setBillStatu("1");
			String rollOutStatus=inputInfo.getRollOutStatus();
			if(rollOutStatus==null){
				inputInfo.setRollOutStatus("0");
			}
			List paperInfoList=invoiceSurtaxService.findInvoiceInSurtaxList(inputInfo, paginationList);
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			String massage= (String) request.getSession().getAttribute("ssionmassage");
			if("ssionmassage".equals(massage)){
				setResultMessages("未计算转出比例");
				request.getSession().removeAttribute("ssionmassage");
			}
			User user=this.getCurrentUser();
			List<String> reportList=invoiceSurtaxService.getReportInst(user.getOrgId());
			if(reportList.size()>0){
				request.setAttribute("mess", "Y");
			}else{
				request.setAttribute("mess", "N");
			}
			InputInfo InputInfo=invoiceSurtaxService.getrollOutAmtSum();
			Calendar calendar =Calendar.getInstance();
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			request.setAttribute("Year-Moth", new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
			request.setAttribute("rollOutAmtSum", InputInfo.getSumTax()==null?0:InputInfo.getSumTax());
			this.request.setAttribute("paperInfoList", paperInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	/**
	 * 票据管理:进项税转出编辑
	 * 
	 * @return
	 * @author jobell
	 */
	public String editInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		inputInvoiceInfo=invoiceSurtaxService.findVmsInputInvoiceInfoByBillId(bill_id);
		lstInputInvoiceItem=invoiceSurtaxService.findVmsInputInvoiceItemsByBillId(bill_id);
		return SUCCESS;
	}
	
	public String uptSaveInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		invoiceSurtaxService.updateVmsInputInvoiceInfoVatOut(vatOutAmt, vatOutProportion, remark,bill_id);
		return SUCCESS;
	}

	/**
	 * @Action
	 * 
	 * 销项附加税sheet1的作成
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private void writeToExcel1(OutputStream os, List lstInputInvoiceInfo,WritableWorkbook wb) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("进项附加税", 0);
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "金额", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "税额", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "发票种类", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "供应商名称", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "供应商纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "认证结果", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "转出金额", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(10, 0, "认证日期", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 18);
		ws.addCell(header3);
		ws.setColumnView(3, 18);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 15);
		ws.addCell(header6);
		ws.setColumnView(6, 10);
		ws.addCell(header7);
		ws.setColumnView(7, 15);
		ws.addCell(header8);
		ws.setColumnView(8, 15);
		ws.addCell(header9);
		ws.setColumnView(9, 15);
		ws.addCell(header10);
		ws.setColumnView(10, 15);
		mapVatType=vmsCommonService.findCodeDictionary("VAT_TYPE");
		if(null != lstInputInvoiceInfo && lstInputInvoiceInfo.size()>0){
			for(int i=0;i<lstInputInvoiceInfo.size();i++){
				InputInvoiceInfo inInfo=(InputInvoiceInfo) lstInputInvoiceInfo.get(i);
				setWritableSheet1(ws, inInfo, i+1);
			}
		}
		
	}
	
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, InputInvoiceInfo info, int column) throws WriteException {
		Label cell1 = new Label(0, column, info.getBillCode() == null ? "" : info.getBillCode(), JXLTool.getContentFormat());
		Label cell2 = new Label(1, column, info.getBillNo() == null ? "" : info.getBillNo(), JXLTool.getContentFormat());
		Label cell3 = new Label(2, column, info.getBillDate() == null ? "" : info.getBillDate(), JXLTool.getContentFormat());
		Label cell4 = new Label(3, column, info.getAmtSum() == null ? "" : info.getAmtSum().toString(), JXLTool.getContentFormat());
		Label cell5 = new Label(4, column, info.getTaxAmtSum() == null ? "" : info.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell6 = new Label(5, column, info.getFapiaoType() == null ? "" : (String)mapVatType.get(info.getFapiaoType()), JXLTool.getContentFormat());
		Label cell7 = new Label(6, column, info.getVendorName() == null ? "" : info.getVendorName(), JXLTool.getContentFormat());
		Label cell8 = new Label(7, column, info.getVendorTaxno() == null ? "" : info.getVendorTaxno(), JXLTool.getContentFormat());
		Label cell9 = new Label(8, column, info.getDatastatusName() == null ? "" : info.getDatastatusName(), JXLTool.getContentFormat());
		Label cell10 = new Label(9, column, info.getVatOutAmt() == null ? "" : info.getVatOutAmt().toString(), JXLTool.getContentFormat());
		Label cell11 = new Label(10, column, info.getVerifyData() == null ? "" : info.getVerifyData(), JXLTool.getContentFormat());

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
	}
	
	/**
	 * @Action
	 * 
	 * 票据管理，进项税转出 转出提交处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String rollOutSubmitInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			@SuppressWarnings("unchecked")
			List<String> list = Arrays.asList(billId.split(","));
			invoiceSurtaxService.updateVmsInputInvoiceInfoDatastatus(list);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action
	 * 
	 * 票据管理，进项税转出 批量转出处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String batchRollOutInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			@SuppressWarnings("unchecked")
			List<String> list=Arrays.asList(billId.split(","));
			invoiceSurtaxService.updateVmsInputInvoiceInfoForBatchRollOut(list); 
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action
	 * 
	 * 票据管理，进项税转出 撤回数据处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String batchRollBackInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			@SuppressWarnings("unchecked")
			List<String> list=Arrays.asList(billId.split(","));
			invoiceSurtaxService.updateVmsInputInvoiceInfoForRollBack(list);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	/**
	 * 进项转出
	 */
	public String transInputInfo(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String billIds=request.getParameter("selectedIds");
		String[] bills=billIds.split(",");
		@SuppressWarnings("unchecked")
		List<String> idList=Arrays.asList(bills);
		invoiceSurtaxService.transInputInfo(idList);
		return SUCCESS;
	}
	
	public String sgrollout(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User user=this.getCurrentUser();
		List<String> reportList=invoiceSurtaxService.getReportInst(user.getOrgId());
		if(reportList.size()>0){
			request.setAttribute("mass", "Y");
		}else{
			request.setAttribute("mass", "N");
		}
		Proportionality proportionality=new Proportionality();
		proportionality.setInstId(user.getOrgId());
		proportionality.setAvailable("1");
		proportionality.setDatasource("0");
		List<Proportionality> list=invoiceSurtaxService.findProportionality(proportionality, "N",null);
		if(list.size()>0){
			request.setAttribute("proportionality", list.get(0));
			return SUCCESS;
		}else{
			request.getSession().setAttribute("ssionmassage", "ssionmassage");
			return "tex";
		}
	}
	
	public void exportJX(){
		this.getAuthInstList(lstAuthInstId);
		inputInfo.setLstAuthInstId(lstAuthInstId);
		inputInfo.setBillStatu("2");
		List<BillDetailEntity> paperInfoList=invoiceSurtaxService.findInvoiceInSurtaxList(inputInfo, null);
		try {
		StringBuffer fileName=new StringBuffer("进项转出信息表");
		fileName.append(".xls");
			String name="attachment;filename="+URLEncoder.encode(fileName.toString(),"UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os =response.getOutputStream();
			this.writeToExcel(os,paperInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeToExcel(OutputStream oStream,List<BillDetailEntity> list) throws Exception{
		WritableWorkbook workbook=Workbook.createWorkbook(oStream);
		WritableSheet ws=workbook.createSheet("进项转出信息表", 0);
		JxlExcelInfo excelInfo=new JxlExcelInfo();
		excelInfo.setBgColor(Colour.YELLOW2);
		excelInfo.setBorderColor(Colour.BLACK);
		String [] heads={"编号","报销机构编号","发票代码","发票号码","开票日期","金额","税率","税额","价税合计金额","发票类型","供应商名称",
				"供应商纳税人识别号","认证结果","认证时间","转出状态","转出原因","转出税额","转出率"};
		for(int i=0;i<heads.length;i++){
			Label header = new Label(i, 0, heads[i], JXLTool.getHeaderC(excelInfo));
			ws.addCell(header);
			if(i!=0){
				ws.setColumnView(i, 20);
			}
		}
		int count =1;
		for(int i=0;i<list.size();i++){
			BillDetailEntity bEntity=list.get(i);
			int coulumn=count++;
			this.setWritableSheet(ws,bEntity,coulumn);
		}
		workbook.write();
		workbook.close();
	}
	
	private void setWritableSheet(WritableSheet wSheet,BillDetailEntity bEntity,int column) throws Exception{
		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		Label label1=new Label(0, column, String.valueOf(column), tempCellFormat);
		Label label17=new Label(1, column, bEntity.getShareInst(), tempCellFormat);
		Label label2=new Label(2, column, bEntity.getBillId()==null?"":bEntity.getBillId(), tempCellFormat);
		Label label3=new Label(3, column, bEntity.getBillCode()==null?"":bEntity.getBillCode(), tempCellFormat);
		Label label4=new Label(4, column, bEntity.getBillDate()==null?"":bEntity.getBillDate(), tempCellFormat);
		Label label5=new Label(5, column, bEntity.getAmt()==null?"":NumberUtils.format(bEntity.getAmt()," ",2), tempCellFormat);
		Label label6=new Label(6, column, bEntity.getTaxRate()==null?"":NumberUtils.format(bEntity.getTaxRate()," ",2), tempCellFormat);
		Label label7=new Label(7, column, bEntity.getTax()==null?"":NumberUtils.format(bEntity.getTax()," ",2), tempCellFormat);
		Label label8=new Label(8, column, bEntity.getSumAmt()==null?"":NumberUtils.format(bEntity.getSumAmt()," ",2), tempCellFormat);
		Label label9=new Label(9, column, bEntity.getBillType()==null?"":DataUtil.getFapiaoTypeCH(bEntity.getBillType()), tempCellFormat);
		Label label10=new Label(10, column, bEntity.getName()==null?"0":bEntity.getName(), tempCellFormat);
	
		Label label11=new Label(11, column, bEntity.getTaxNo()==null?"":bEntity.getTaxNo(), tempCellFormat);
		Label label12=new Label(12, column, bEntity.getBillStatu()==null?"":JSPUtil.getValue(InputInfoUtil.billStatuMap, bEntity.getBillStatu()), tempCellFormat);
		Label label13=new Label(13, column, bEntity.getDealNo()==null?"":bEntity.getDealNo(), tempCellFormat);
		Label label14=new Label(14, column, bEntity.getRollOutStatus()==null?"":JSPUtil.getValue(InputInfoUtil.rollOutStatusMap, bEntity.getRollOutStatus()), tempCellFormat);
		Label label15=new Label(15, column, bEntity.getRemark()==null?"":JSPUtil.getValue(InputInfoUtil.remarkMap, bEntity.getRemark()), tempCellFormat);
		Label label16=new Label(16, column, bEntity.getRollOutAmt()==null?"":NumberUtils.format(bEntity.getRollOutAmt(), " ", 2), tempCellFormat);
		Label label18=new Label(17, column, bEntity.getRollOutval()==null?"":bEntity.getRollOutval(), tempCellFormat);
		wSheet.addCell(label1);
		wSheet.addCell(label17);
		wSheet.addCell(label2);
		wSheet.addCell(label3);
		wSheet.addCell(label4);
		wSheet.addCell(label5);
		wSheet.addCell(label6);
		wSheet.addCell(label7);
		wSheet.addCell(label8);
		wSheet.addCell(label9);
		wSheet.addCell(label10);
		wSheet.addCell(label11);
		wSheet.addCell(label12);
		wSheet.addCell(label13);
		wSheet.addCell(label14);
		wSheet.addCell(label15);
		wSheet.addCell(label16);
		wSheet.addCell(label18);
	}
	 /** 
     * 表头单元格样式的设定 
     */  
    public WritableCellFormat getBodyCellStyle(){
          
        /* 
         * WritableFont.createFont("宋体")：设置字体为宋体 
         * 10：设置字体大小 
         * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗     NO_BOLD：不加粗） 
         * false：设置非斜体 
         * UnderlineStyle.NO_UNDERLINE：没有下划线 
         */  
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                                             10,   
                                             WritableFont.NO_BOLD,   
                                             false,  
                                             UnderlineStyle.NO_UNDERLINE);  
          
        WritableCellFormat bodyFormat = new WritableCellFormat(font);  
        try {  
            //设置单元格背景色：表体为白色  
            bodyFormat.setBackground(Colour.WHITE);  
            //设置表头表格边框样式  
            //整个表格线为细线、黑色  
            bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);  
              
        } catch (WriteException e) {  
            System.out.println("表体单元格样式设置失败！");  
        }  
        return bodyFormat;  
    }
	
	private String billDate;//开票日期
	private String vendorName;//供应商名称
	private String datastatus;//认证结果
	private String billCode;//发票代码
	private String fapiaoType;//票据种类
	private String vatOutAmt;
	private String vatOutProportion;
	private String remark;
	private String billNo;
	
	private int billStatu;
	
	private String bill_id;//票据ID
	private String billId;
	private Map mapVatType;
	private Map mapDatastatus;//发票状态下拉列表Map[查询，展示]
	
	private InputInvoiceInfo inputInvoiceInfo;
	private List lstInputInvoiceItem;
	private List lstAuthInstId = new ArrayList();//所属机构下拉列表[查询]
	private String instId;
	private String instName;
	
	
	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
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
	public InputInvoiceInfo getInputInvoiceInfo() {
		return inputInvoiceInfo;
	}
	public void setInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo) {
		this.inputInvoiceInfo = inputInvoiceInfo;
	}
	public List getLstInputInvoiceItem() {
		return lstInputInvoiceItem;
	}
	public void setLstInputInvoiceItem(List lstInputInvoiceItem) {
		this.lstInputInvoiceItem = lstInputInvoiceItem;
	}

	public String getVatOutAmt() {
		return vatOutAmt;
	}

	public void setVatOutAmt(String vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}

	public String getVatOutProportion() {
		return vatOutProportion;
	}

	public void setVatOutProportion(String vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}


	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public Map getMapDatastatus() {
		return mapDatastatus;
	}

	public void setMapDatastatus(Map mapDatastatus) {
		this.mapDatastatus = mapDatastatus;
	}

	/*service 声明*/
	private InvoiceSurtaxService invoiceSurtaxService;

	public InvoiceSurtaxService getInvoiceSurtaxService() {
		return invoiceSurtaxService;
	}

	public void setInvoiceSurtaxService(InvoiceSurtaxService invoiceSurtaxService) {
		this.invoiceSurtaxService = invoiceSurtaxService;
	}

	public int getBillStatu() {
		return billStatu;
	}

	public void setBillStatu(int billStatu) {
		this.billStatu = billStatu;
	}

	public InputInfo getInputInfo() {
		return inputInfo;
	}

	public void setInputInfo(InputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}
	/**
	 * 进项转出比例计算
	 * @return
	 */
	public String listInvoiceInSurtaxBLJS(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			//massages : 1-科目
			String  massages=request.getParameter("massages");
			System.out.println(massages+"++++++++++++++++");//null
			User user=this.getCurrentUser();
			System.out.println(user.getOrgId()+"***************");//输出86
			/*List<String> reportList=invoiceSurtaxService.getReportInst(user.getOrgId()); //2018-08-02更改
			if(reportList.size()>0){
				request.setAttribute("mass", "Y");
			}else{
				request.setAttribute("mass", "N");
			}*/
			
			//2018-08-20新增start
			//查询自动计算的比例值
			Proportionality proportionality=new Proportionality();
			proportionality.setInstId(user.getOrgId());
			proportionality.setAvailable("1");
			proportionality.setDatasource("0");
			List<Proportionality> list=invoiceSurtaxService.findProportionality(proportionality);//查询比例计算值
			List<CodeDictionary> codeDictionaryList = null;  //保存从字典表中查询到的结果
			List<Map> resultList = new ArrayList<Map>();
			if(list.size()>0){
				/*request.setAttribute("proportionality", list.get(0));*/
				generalLedgerResult.put("generalLedgerMoneyOfTaxfree", list.get(0).getDividend()); //存储比例计算后的免税金额
				generalLedgerResult.put("generalLedgerMoney", list.get(0).getDivisor());  //存储比例计算后的免税+应税金额
				generalLedgerResult.put("generalLedgerResult", list.get(0).getResult());  //存储比例计算值
				/**
				 * 修改
				 * 日期：2018-08-22
				 * 作者：刘俊杰
				 * 说明：若查询到已经计算过比例值，则将已计算的信息展示在前台页面上
				 */
				codeDictionaryList = invoiceSurtaxService.selectCodeDictionaryAll();  //查询所有的
				//获取所有的免税+应税信息(上月)
				Map subjectEntityMap = new HashMap();
				subjectEntityMap.put("instId", user.getOrgId());
				
				for(CodeDictionary code : codeDictionaryList) {
					Map map1 = new HashMap();
					Map map2 = new HashMap();
					map1.put("subjectName", code.getCodeTypeDesc());
					map1.put("instId", this.getCurrentUser().getOrgId());
					List<SubjectEntity> subjectLedgerMoney = invoiceSurtaxService.getSubjectLedgerMoney(map1);
					map2.put("subjectName", code.getCodeTypeDesc());
					map2.put("subjectId", code.getCodeName());
					map2.put("result", subjectLedgerMoney.get(0).getCreditDescSum());
					resultList.add(map2);
				}
				
				List<SubjectEntity> subjectEntityListAll = invoiceSurtaxService.getSubjectLedgerAll(subjectEntityMap);
				if(subjectEntityListAll.size()==0 || subjectEntityListAll.get(0).getCreditDescSum() == null) {
					generalLedgerMoneyOfAllCount = "";
				}else {
					generalLedgerMoneyOfAllCount = subjectEntityListAll.get(0).getCreditDescSum().toString();
				}
				generalLedgerResult.put("generalLedgerMoneyAll",generalLedgerMoneyOfAllCount); //存储比例计算前的免税+应税金额
			}
			else{
				/*request.setAttribute("proportionality", new Proportionality());*/
				
				/**
				 * 修改
				 * 日期：2018-08-22
				 * 作者：刘俊杰
				 * 说明：若查询到已经计算过比例值，则将未计算的信息展示在前台页面上
				 */
				codeDictionaryList = invoiceSurtaxService.selectCodeDictionaryAll();  //N--未计算
				
				//获取所有的免税+应税信息(上月)
				Map subjectEntityMap = new HashMap();
				subjectEntityMap.put("instId", user.getOrgId());
				
				for(CodeDictionary code : codeDictionaryList) {
					Map map1 = new HashMap();
					Map map2 = new HashMap();
					map1.put("subjectName", code.getCodeTypeDesc());
					map1.put("instId", this.getCurrentUser().getOrgId());
					List<SubjectEntity> subjectLedgerMoney = invoiceSurtaxService.getSubjectLedgerMoney(map1);
					map2.put("subjectName", code.getCodeTypeDesc());
					map2.put("subjectId", code.getCodeName());
					map2.put("result", subjectLedgerMoney.get(0).getCreditDescSum());
					resultList.add(map2);
				}
				
				List<SubjectEntity> subjectEntityListAll = invoiceSurtaxService.getSubjectLedgerAll(subjectEntityMap);
				if(subjectEntityListAll.size()==0 || subjectEntityListAll.get(0).getCreditDescSum() == null) {
					generalLedgerMoneyOfAllCount = "";
				}else {
					generalLedgerMoneyOfAllCount = subjectEntityListAll.get(0).getCreditDescSum().toString();
				}
				generalLedgerResult.put("generalLedgerMoneyOfTaxfree", "");//存储比例计算后的免税金额
				generalLedgerResult.put("generalLedgerMoneyAll",generalLedgerMoneyOfAllCount); //存储比例计算前的免税+应税金额
				generalLedgerResult.put("generalLedgerResult", "");//存储比例计算值
				generalLedgerResult.put("generalLedgerMoney", "");//存储比例计算后的免税+应税金额
			}
			request.setAttribute("codeDictionaryList", codeDictionaryList); //将查询到的结果写回页面
			request.setAttribute("resultList", resultList); //将查询到的结果写回页面
			//查询手动调整的比例值
			Proportionality proportionality2=new Proportionality();
			proportionality2.setInstId(user.getOrgId());
			proportionality2.setAvailable("1");
			proportionality2.setDatasource("1");
			List<Proportionality> list2=invoiceSurtaxService.findProportionality(proportionality2);//查询比例计算值
			if(list2.size()>0){
				generalLedgerResultOfHand.put("generalLedgerMoneyOfTaxfree", list2.get(0).getDividend());
				generalLedgerResultOfHand.put("generalLedgerMoneyAll", list2.get(0).getDivisor());
				generalLedgerResultOfHand.put("generalLedgerResult", list2.get(0).getResult());
			}else {
				generalLedgerResultOfHand.put("generalLedgerMoneyOfTaxfree", "");
				generalLedgerResultOfHand.put("generalLedgerMoneyAll", "");
				generalLedgerResultOfHand.put("generalLedgerResult", "");
			}
			//2018-08-20新增end
			
			//以下操作已废除2018-08-22
			/*List<Map<String, List<SubjectEntity>>> biglist=findSubjectEntityMassage("",massages==null?"0":massages.split("-")[0], user.getOrgId());//查询贷方本位币发生总额
			if(biglist!=null&&biglist.size()>0){
				for (Map<String, List<SubjectEntity>> map : biglist) {
					Set<String> kSet=map.keySet();
					for (String string : kSet) {
						if("FZ".equals(string)){
							request.setAttribute("FZ", map.get(string));
						}else{
							request.setAttribute("FM", map.get(string));
						}
					}
				}
			}*/
			Date startDate = getRunTime();
			Calendar calendar = Calendar.getInstance();
			Calendar startcal=Calendar.getInstance();
			startcal.setTime(startDate);
			int res=calendar.compareTo(startcal);
			if(res<0){
				request.setAttribute("massage", "Y");
			}else{
				request.setAttribute("massage", "N");
			}
			
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceSurtaxAction-listInvoiceInSurtaxBLJS", e);
		}
		return ERROR ;
	}
	
	/**
	 * 进项转出比例计算 数据信息导出  ：主表   各数据信息表<按照 ， 科目类别分类>
	 */
	public  String exportExeclInvoiceBLJS(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
		}
		try {
			User user=this.getCurrentUser();
			StringBuffer fileName=new StringBuffer("进项转出比例表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			//sheet 名称
			List<String> list = invoiceSurtaxService.findCodeDictionary("SUBJECT_NAME","");
			WritableWorkbook workbook=Workbook.createWorkbook(os);
			String [] massage={"0","1"};
			for (String massages : massage) {
				if("0".equals(massages)){
					//得到主的贷方数据和
					List<Map<String, List<SubjectEntity>>> biglist=findSubjectEntityMassage("", massages, user.getOrgId());
					writexcel(workbook,os, biglist, "主表", 0, "Z");
				}else{
					for(int i=0;i< list.size();i++){
						String sheetName=list.get(i);
						//sheet name
						List<Map<String, List<SubjectEntity>>> biglist=this.findSubjectEntityMassage(sheetName.split("-")[0]+"-"+sheetName.split("-")[1], massages, user.getOrgId());
						//sheetCount 第几张
						int sheetCount=1;
						writexcel(workbook,os, biglist, sheetName, sheetCount, "");
						sheetCount++;
					}
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private void writexcel(WritableWorkbook workbook,OutputStream oStream , List<Map<String, List<SubjectEntity>>> biglist,String sheetName,int sheetCount,String val) throws Exception{
		JxlExcelInfo excelInfo = new JxlExcelInfo();
		excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
        if("Z".equals(val)){
        	WritableSheet ws=workbook.createSheet(sheetName, sheetCount);
        	String [] headers ={"数据类型","科目名称","贷方本位币发生总额"};
        	for(int i=0 ;i<headers.length;i++){
        		Label header = new Label(i, 0, headers[i], JXLTool.getHeaderC(excelInfo));
        		ws.addCell(header);
        		ws.setColumnView(i,25);
        	}
        	Map<String, List<SubjectEntity>> lMap=biglist.get(0);
        	List<SubjectEntity> fzList = lMap.get("FZ");
        	List<SubjectEntity> fmList = lMap.get("FM");
        	if(fzList!=null){
        		setsheet(fzList, ws, "FZ" ,0 ,"Z");
        	}
        	if(fmList!=null){
        		setsheet(fmList, ws, "FM" ,fzList.size(),"Z");
        	}
        }else{
        	// mingxin qingkuangbiao 
        	WritableSheet ws=workbook.createSheet(sheetName, sheetCount);
        	String [] headers ={"报税机构","科目名称","月度","险种名称","税率编码","税率","贷方本位币发生额"};
        	for(int i=0 ;i<headers.length;i++){
        		Label header = new Label(i, 0, headers[i], JXLTool.getHeaderC(excelInfo));
        		ws.addCell(header);
        		ws.setColumnView(i,25);
        	}
        	Map<String, List<SubjectEntity>> lMap=biglist.get(0);
        	List<SubjectEntity> fzList = lMap.get("FZ");
        	List<SubjectEntity> fmList = lMap.get("FM");
        	if(fzList!=null){
        		setsheet(fzList, ws, "p", 0, "p");
        	}
        	if(fmList!=null){
        		setsheet(fmList, ws, "p", 0, "p");
        	}
        }
	}
	
	private void setsheet(List<SubjectEntity> list, WritableSheet wSheet,String val ,int  column ,String str) throws Exception{
		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		for (SubjectEntity subjectEntity : list) {
			column=column+1;
			if("Z".equals(str)){
				String fl="";
				if("FZ".equals(val)){
					fl="分子（免税）";
				}else{
					fl="分母（免税+应税）";
				}
				Label label1=new Label(0, column, fl, tempCellFormat);
				Label label2=new Label(1, column, subjectEntity.getDirectionName(), tempCellFormat);
				Label label3=new Label(2, column, subjectEntity.getCreditDescSum()==null?"0":subjectEntity.getCreditDescSum().toString(), tempCellFormat);
				wSheet.addCell(label1);
				wSheet.addCell(label2);
				wSheet.addCell(label3);
			}else{
				// mingxin xinx xin 
				Label label1=new Label(0, column, subjectEntity.getInstName(), tempCellFormat);
				Label label2=new Label(1, column, subjectEntity.getDirectionName(), tempCellFormat);
				Label label3=new Label(2, column, subjectEntity.getYearMonth(), tempCellFormat);
				Label label4=new Label(3, column, subjectEntity.getPlanLongDescName(), tempCellFormat);
				Label label5=new Label(4, column, subjectEntity.getTaxRateCode(), tempCellFormat);
				Label label6=new Label(5, column, subjectEntity.getTaxRateName(), tempCellFormat);
				Label label7=new Label(6, column, subjectEntity.getCreditDesc()==null?"0":subjectEntity.getCreditDesc().toString(), tempCellFormat);
				wSheet.addCell(label1);
				wSheet.addCell(label2);
				wSheet.addCell(label3);
				wSheet.addCell(label4);
				wSheet.addCell(label5);
				wSheet.addCell(label6);
				wSheet.addCell(label7);
			}
		}
		if("Z".equals(str)){
			User user=this.getCurrentUser();
			Proportionality proportionality=new Proportionality();
			proportionality.setInstId(user.getOrgId());
			proportionality.setAvailable("1");
			proportionality.setDatasource("0");
			List<Proportionality> ls=invoiceSurtaxService.findProportionality(proportionality,"N",null);
			proportionality =ls.get(0);
			Label kLabe4=new Label(0, column+1, "", tempCellFormat);
			Label hjLabe4=new Label(1, column+1, "", tempCellFormat);
			Label bvLabe4=new Label(2, column+1, "", tempCellFormat);
			Label kLabe3=new Label(0, column+2, "", tempCellFormat);
			Label hjLabe3=new Label(1, column+2, "分子和", tempCellFormat);
			Label bvLabe3=new Label(2, column+2, fz.toString(), tempCellFormat);
			Label kLabe2=new Label(0, column+3, "", tempCellFormat);
			Label hjLabe2=new Label(1, column+3, "分母和", tempCellFormat);
			Label bvLabe2=new Label(2, column+3, fm.toString(), tempCellFormat);
			Label kLabe1=new Label(0, column+4, "", tempCellFormat);
			Label hjLabel=new Label(1, column+4, "比例值", tempCellFormat);
			Label bvLabel=new Label(2, column+4, proportionality.getResult(), tempCellFormat);
			wSheet.addCell(hjLabel);
			wSheet.addCell(bvLabel);
			wSheet.addCell(hjLabe2);
			wSheet.addCell(bvLabe2);
			wSheet.addCell(hjLabe3);
			wSheet.addCell(bvLabe3);
		}
	}
	private static BigDecimal fz;
	private static BigDecimal fm;
	/**
	 * 
	 * @param codeName 科目
	 * @param massage  贷方总额 为0/ 科目信息值为1
	 * @param instId   机构
	 * @return
	 */
	private List<Map<String, List<SubjectEntity>>> findSubjectEntityMassage(String codeName,String massage,String instId){
		List<Map<String, List<SubjectEntity>>> biglist= new ArrayList<Map<String, List<SubjectEntity>>>();
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		String yearMonth=new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		Map<String, List<SubjectEntity>> Map=new HashMap<String, List<SubjectEntity>>();
		List<SubjectEntity> FZlist=new ArrayList<SubjectEntity>();
		List<SubjectEntity> FMlist=new ArrayList<SubjectEntity>();
		SubjectEntity sEntity=new SubjectEntity();
		sEntity.setYearMonth(yearMonth);
		sEntity.setInstId(instId);
		fz = new BigDecimal(0) ; 
		fm = new BigDecimal(0) ; 
		List<String> list = invoiceSurtaxService.findCodeDictionary("SUBJECT_NAME",codeName);//根据SUBJECT_NAME查出CODE_NAME：FZ-保费收入...
		for (String directionName : list) {
			sEntity.setDirectionName(directionName.split("-")[1]);
			if("FZ".equals(directionName.split("-")[0])){
				//sEntity.setTaxRateCode("S0-1");
				sEntity.setIsTaxexemption("Y");
				if("1".equals(massage)){
					// 得到分z数据信息
					List<SubjectEntity> listfz=invoiceSurtaxService.findSubjectEntityList(sEntity);
					for (SubjectEntity subjectEntity : listfz) {
						FZlist.add(subjectEntity);
					}
				}else{
					// 得到分z科目 贷方金额
					List<SubjectEntity> listfz=invoiceSurtaxService.findSubjectEntityCreditDescSum(sEntity);//查出贷方本位币发生总额
					for (SubjectEntity subjectEntity : listfz) {
						FZlist.add(subjectEntity);
						if(null!=subjectEntity.getCreditDescSum()){
							fz=fz.add(subjectEntity.getCreditDescSum());
						}
					}
				}
				
			}else {
				sEntity.setIsTaxexemption("N");
				if("1".equals(massage)){
					// 得到分母数据信息
					List<SubjectEntity> listfm=invoiceSurtaxService.findSubjectEntityList(sEntity);
					for (SubjectEntity subjectEntity : listfm) {
						FMlist.add(subjectEntity);
					}
				}else{
					// 得到分母科目 贷方金额
					//sEntity.setDirectionName("");
					List<SubjectEntity> listfm=invoiceSurtaxService.findSubjectEntityCreditDescSum(sEntity);
					for (SubjectEntity subjectEntity : listfm) {
						//FMlist.add(subjectEntity);
						if(null!=subjectEntity.getCreditDescSum()){
							fm=fm.add(subjectEntity.getCreditDescSum());
						}
					}
				}
			}
		}
		/*sEntity.setDirectionName("");
		List<SubjectEntity> listfm=invoiceSurtaxService.findSubjectEntityCreditDescSum(sEntity);
		if(listfm.size()>0){
			fm = listfm.get(0).getCreditDescSum();
		}*/
		
		SubjectEntity se = new SubjectEntity();
		se.setDirectionName("总收入");
		se.setCreditDescSum(fm);
		FMlist.add(se);
		
		if(FZlist.size()>0&&FZlist!=null){
			Map.put("FZ", FZlist);
		}
		if(FMlist.size()>0&&FMlist!=null){
			Map.put("FM", FMlist);
		}
		biglist.add(Map);
		return biglist;
	} 
	
	/**
	 * 转出比例查看
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public String listInvoiceInSurtaxBLSelect(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String instid=request.getParameter("instId");
			String StartDate= request.getParameter("StartDate");
			String EndDate = request.getParameter("EndDate");
			if(instid==null||""==instid){
				User user = this.getCurrentUser();
				instid=user.getOrgId();
			}else{
				instid=instid.split(" ")[0];
			}
			Proportionality proportionality=new Proportionality();
			proportionality.setInstId(instid);
			proportionality.setAvailable("1");
			if(StartDate==null&&EndDate==null||StartDate.length()==0&&EndDate.length()==0){
				Calendar calendar=Calendar.getInstance();
				proportionality.setOperateEndDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
				//calendar.set(Calendar.DATE, 1);
				//calendar.set(Calendar.MONTH, 0);
				calendar.add(calendar.YEAR, -1); 
				calendar.add(calendar.MONTH, 1);
				calendar.set(calendar.DATE, 1);//2018-07-20更改
				proportionality.setOperateStartDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}else{
				proportionality.setOperateStartDate(StartDate);
				proportionality.setOperateEndDate(EndDate);
			}
		
			List<Proportionality> list=invoiceSurtaxService.findProportionalityForRolliut(proportionality);
			List<Proportionality> biglist = getInvoiceSurtaxSelectBiglist(list, proportionality.getOperateStartDate(), proportionality.getOperateEndDate());
			
			request.setAttribute("mList", Util.getMonthlist(proportionality.getOperateStartDate(), proportionality.getOperateEndDate()));
			request.setAttribute("biglist", biglist);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceSurtaxAction-listInvoiceInSurtaxBLSelect-比例查看", e);
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @param list 
	 * @param StartDate kaishitian
	 * @param EndDate   jieshutian
	 * @return
	 * @throws ParseException 
	 */
	private List<Proportionality> getInvoiceSurtaxSelectBiglist(List<Proportionality> list, String StartDate,
			String EndDate) throws ParseException {
		List<Proportionality> biglist=new ArrayList<Proportionality>();
		Set<String> instSet=new LinkedHashSet<String>();
		List<String> yList=new ArrayList<String>();//月度集合01,02,04,
		for (Proportionality proportionality : list) {
			instSet.add(proportionality.getInstId());
		}
		for(String string : instSet){
			//86
			Proportionality proportionality =new Proportionality();
			List<String> resultList=new ArrayList<String>();
			for (Proportionality Proportionality : list) {
				if(string.equals(Proportionality.getInstId())){
					String mString=Proportionality.getYearMonth();//月度
					String rString=Proportionality.getResult();//比例值
					String datasource=Proportionality.getDatasource();// 方式
					yList.add(mString);//201705 201706 
					resultList.add(mString+"-"+rString+"-"+datasource);
					proportionality=Proportionality;
				}
			}
			List<String> mothlist=Util.getMonthlist(StartDate, EndDate);
			for (String moth : mothlist) {
				if(!yList.contains(moth)){
					resultList.add(moth+"-"+"X"+"-X");
					Collections.sort(resultList, new Comparator<String>() {
						@Override
						public int compare(String moth ,String ymoth) {
							return ymoth.compareTo(moth);
						}
					});
				}
			}
			yList.clear();
			proportionality.setResultList(resultList);
			biglist.add(proportionality);
		}
		return biglist;
	}
	
	/**
	 * 进项转出结果导出数据
	 */
	public String exportBLSelect(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
		}
		try {
			String instid=request.getParameter("instId");
			String StartDate= request.getParameter("StartDate");
			String EndDate = request.getParameter("EndDate");
			if(instid==null||""==instid){
				User user = this.getCurrentUser() ;
				instid=user.getOrgId();
			}else{
				instid=instid.split(" ")[0];
			}
			Proportionality proportionality=new Proportionality();
			proportionality.setInstId(instid);
			proportionality.setAvailable("1");
			if(StartDate==null&&EndDate==null||StartDate.length()==0&&EndDate.length()==0 ){
				Calendar calendar=Calendar.getInstance();
				proportionality.setOperateEndDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.MONTH, 0);
				proportionality.setOperateStartDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}else{
				proportionality.setOperateStartDate(StartDate);
				proportionality.setOperateEndDate(EndDate);
			}
			List<Proportionality> list=invoiceSurtaxService.findProportionalityForRolliut(proportionality);
			List<Proportionality> biglist = getInvoiceSurtaxSelectBiglist(list, proportionality.getOperateStartDate(), proportionality.getOperateEndDate());
			// 创建表格
			StringBuffer  fileName=new StringBuffer("进项转出比例查看信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			
			OutputStream oStream=response.getOutputStream();
			this.writeToExcelForInvoiceSelect(oStream, biglist,Util.getMonthlist(proportionality.getOperateStartDate(), proportionality.getOperateEndDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param oStream
	 * @param list      数据
	 * @param mothlist  月度
	 * @throws IOException
	 * @throws WriteException
	 */
	private void writeToExcelForInvoiceSelect(OutputStream oStream, List<Proportionality> list, List<String> mothlist) throws IOException, WriteException{
		List<Label> Labellist=new ArrayList<Label>();
		WritableWorkbook workbook =Workbook.createWorkbook(oStream);
		WritableSheet sheet= workbook.createSheet("进项转出比例信息", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
		excelInfo.setBgColor(Colour.YELLOW2);
		excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "编号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "报税机构名称", JXLTool.getHeaderC(excelInfo));
		Labellist.add(header1);
		Labellist.add(header2);
		int count=2;
		for(int i=mothlist.size()-1;i>=0;i--){
			//String m = mothlist.get(i);
			Label header3 = new Label(count, 0, "月度", JXLTool.getHeaderC(excelInfo));
			Label header4 = new Label(count+1, 0, "比例值", JXLTool.getHeaderC(excelInfo));
			Label header5 = new Label(count+2, 0, "类型", JXLTool.getHeaderC(excelInfo));
			count=count+3;
			Labellist.add(header3);
			Labellist.add(header4);
			Labellist.add(header5);
		}
		for(int i=0;i<Labellist.size();i++){
			sheet.addCell(Labellist.get(i));
			sheet.setColumnView(i, 20);
		}
		int cou = 1; 
		for(Proportionality proportionality :list){
			int column = cou++;
			this.setWritableSheetForInvoiceSelect(sheet, proportionality, column);
		}
		workbook.write();
		workbook.close();
	}
	
	private void setWritableSheetForInvoiceSelect(WritableSheet sheet , Proportionality proportionality , int column) throws WriteException{
		List<Label>  list=new ArrayList<Label>();
		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		Label label1=new Label(0, column, String.valueOf(column), tempCellFormat);
		Label label2=new Label(1, column, proportionality.getInstName()==null?"":proportionality.getInstName(), tempCellFormat);
		list.add(label1);
		list.add(label2);
		int m=2;
		for(int i=0;i<proportionality.getResultList().size();i++){
			String sl = proportionality.getResultList().get(i);
			Label label3=new Label(m,column,sl.split("-")[0], tempCellFormat);
			Label label4=new Label(m+1,column,sl.split("-")[1].equals("X")?" ":sl.split("-")[1], tempCellFormat);
			Label label5=new Label(m+2,column,Util.getDatasource(sl.split("-")[2]), tempCellFormat);
			m=m+3;
			list.add(label3);
			list.add(label4);
			list.add(label5);
		}
		for (Label label : list) {
			sheet.addCell(label);
		}
	}
	
	private static String  massage;
	public String invoiceSelectMassage(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String massages;
		try {
			if(request.getParameter("massages")==null){
				massages=massage;
			}else{
				massages = new String(request.getParameter("massages").getBytes("iso8859-1"),"UTF-8");
				//massages = request.getParameter("massages"); //weblogic 解析
				massage=massages;
			}
			User user=this.getCurrentUser();
			String instId =request.getParameter("instId");
			if(instId==null){
				instId=user.getOrgId();
			}
			
			instId=instId.split(" ")[0];
			String [] maStrings=massages.split("-");
			
			//将字母转换成中文，与数据库进行匹配
		/*	String name = maStrings[1];
			if("BFSR".equals(name)){
				name = "保费收入";
			}else if("TZSY".equals(name)){
				name = "投资收益";
			}else if("QTSR".equals(name)){
				name = "其他收益";
			}else if("LXSR".equals(name)){
				name = "利息收入";
			}else{
				name = "营业外支出";
			}*/
			
			List<Map<String, List<SubjectEntity>>> list=this.findSubjectEntityMassage(maStrings[2]+"-"+maStrings[1], maStrings[0], instId);
			//List<Map<String, List<SubjectEntity>>> list=this.findSubjectEntityMassage(maStrings[2]+"-"+name, maStrings[0], instId);
			if(list.size()==1){
				Map<String, List<SubjectEntity>> map = list.get(0);
				Set<String> kSet = map.keySet();
				if(kSet.size()==1){
					for (String string : kSet) {
						request.setAttribute("invoiceSelectMassage", map.get(string));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String rolloutSGAudit(){
		
 		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
 		try {
			mRequest.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
 		User user = this.getCurrentUser();
 		Calendar calendar=Calendar.getInstance();
 		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
 		String fileName = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
 		//List<String> reportInst=invoiceSurtaxService.findReportInst(user.getOrgId()); //2018-07-30国富进项更改
 		Proportionality proportionality =new Proportionality();
 		String vol="";
 		//for (String string : reportInst) {  //2018-07-30国富进项更改
 			//proportionality.setInstId(string);
 			proportionality.setInstId(user.getOrgId());
 			
 			//查询业务机构对应财务机构
			List<String> instList = invoiceSurtaxService.findInstRelation(user.getOrgId());
			String parentId = "";
			for(int i=0;i<instList.size();i++){
				//查询财务机构对应上级机构
				List<String> listParent = invoiceSurtaxService.findInstLast(instList.get(i));
				if(instList.contains(listParent.get(0)) && listParent != null){
					continue;
				}
				parentId = instList.get(i);
			}
			proportionality.setOrigInstId(parentId);
 			
 			proportionality.setYearMonth(fileName);
 			proportionality.setApply_proposer_id(this.getCurrentUser().getOrgId());
 			proportionality.setApply_proposer_name(this.getCurrentUser().getUsername());
 			String U_FZ=mRequest.getParameter("U_FZ");
 			String U_FM=mRequest.getParameter("U_FM");
 			proportionality.setDividend(U_FZ);
 			proportionality.setDivisor(U_FM);
 			BigDecimal fz = new BigDecimal(U_FZ);
 			BigDecimal fm = new BigDecimal(U_FM);
 			proportionality.setResult(fz.divide(fm, 2, BigDecimal.ROUND_HALF_UP).toString());
 			invoiceSurtaxService.saveRolloutAudit(proportionality);
		//}
 		vol=proportionality.getResult();
		File[] files = mRequest.getFiles("theFile");
		String [] flNames=mRequest.getFileNames("theFile");
		/*if (files != null && files.length > 0) {
			try {
				ImageUtil.info(fileName+"-"+user.getOrgId()+"-"+vol, files, flNames,true);
				String errorMessage="比例调整申请提交！";
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
		}*/
		return SUCCESS;
	}
	
	/**
	 * 转出比例调整审核展示
	 * @return
	 */
	public String listInvoiceInSurtaxRolloutAudit(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User user=this.getCurrentUser();
		Proportionality proportionality=new Proportionality();
		if(null==request.getParameter("instId")||""==request.getParameter("instId")){
			proportionality.setInstId(user.getOrgId());
		}else{
			proportionality.setInstId(request.getParameter("instId").split(" ")[0]);
		}
		proportionality.setAvailable("2");
		proportionality.setDatasource("1");
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		proportionality.setYearMonth(new SimpleDateFormat("yyyyMM").format(calendar.getTime()));
		String yeString = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
		System.out.println(yeString);
		List<Proportionality> list=invoiceSurtaxService.findProportionalityForRolliut(proportionality);
		request.setAttribute("RolloutAudit", list);
		return SUCCESS;
	}
	public void  exportInvoiceRolloutAudit(){
		User user=this.getCurrentUser();
		Proportionality proportionality=new Proportionality();
		if(null==request.getParameter("instId")||""==request.getParameter("instId")){
			proportionality.setInstId(user.getOrgId());
		}else{
			proportionality.setInstId(request.getParameter("instId").split(" ")[0]);
		}
		proportionality.setAvailable("2");
		proportionality.setDatasource("1");
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		proportionality.setYearMonth(new SimpleDateFormat("yyyyMM").format(calendar.getTime()));
		List<Proportionality> list=invoiceSurtaxService.findProportionalityForRolliut(proportionality);
		try {
			//创建Excel表格
			StringBuffer fileName=new StringBuffer("转出比例调整待审核表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			String[] titles={"报税机构编号","报税机构名称","月度","会计机构名称","调整值","操作日期","机构汇总编号"};
			String[] fields={"instId","instName","yearMonth","kjIstName","result","operateDate","relationInstId"};
			
			ExcelUtil.exportExcel(fileName.toString(), Proportionality.class, fields, titles, list, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceSurtaxAction-exportInvoiceRolloutAudit", e);
		}
	}
	
	private static String fleName ;
	public String viewImgFromInvoiceJF(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		Calendar calendar=Calendar.getInstance();
 		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
 		String fl=request.getParameter("fileName");
 		//String fileName = new SimpleDateFormat("yyyyMM").format(calendar.getTime())+"-"+fl;
 		String fileName = fl.split("-")[1]+"-"+fl.split("-")[0]+"-"+fl.split("-")[2];
 		fleName=fileName;
 		String path=ImageUtil.imageShow(fileName, 0);
 		String imageType="N";
 		if(path.endsWith(".pdf")){
 			imageType="Y";
 		} 
 		request.setAttribute("imageType", imageType);
		int count=ImageUtil.Filelength(fileName);
		request.setAttribute("count", count-1);
 		if(!path.endsWith("\\")){
 			request.getSession().setAttribute("fileName", path);
 			System.err.println("要获取的文件路径："+path);
 			return SUCCESS;
 		}else{
 			return "tax";
 		}
	}
	public void viewImgFromInvoiceFJAjax(){
		try{
			int count =ImageUtil.Filelength(fleName);
			int index=Integer.parseInt(request.getParameter("index"));
			String val=request.getParameter("val");
			int dex = 0;
			if("1".equals(val)){
				dex = count-index ;
			}else{
				dex = count-index-2;
			}
//			if(count > index){
			String path =ImageUtil.imageShow(fleName, dex);
			String imageType="N";
			if(path.endsWith(".pdf")){
				imageType="Y";
			} 
			if(!"".equals(path)){
				request.getSession().setAttribute("fileName", path);
				System.err.println("要获取的文件路径："+path);
			}
				
			response.setCharacterEncoding("UTF-8");
			if("1".equals(val)){
				response.getWriter().println(imageType+"-"+(index - 1));
			}else{
				response.getWriter().println(imageType+"-"+(index + 1));
			}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public String InvoiceCancel() {
		String billId = request.getParameter("selects");
		request.setAttribute("billId", billId);
		return SUCCESS;
	}
	private Proportionality proportionality =new Proportionality();
	
	
	public Proportionality getProportionality() {
		return proportionality;
	}

	public void setProportionality(Proportionality proportionality) {
		this.proportionality = proportionality;
	}

	/**
	 * 审核信息展示
	 * @return
	 */
	@SuppressWarnings("unused")
	public String listInvoiceInSurtaxRollBackAudit(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		String datasource=proportionality.getDatasource();
		String operateStartDate = proportionality.getOperateStartDate();
		String operateEndDate = proportionality.getOperateEndDate();
		
		String instId=request.getParameter("instId");
	
		User user=this.getCurrentUser();
		Calendar calendar= Calendar.getInstance();
		SimpleDateFormat sFormat =new SimpleDateFormat("yyyy-MM-dd");
		Proportionality proportionality=new Proportionality();
		if(operateEndDate==null){
			proportionality.setOperateEndDate(sFormat.format(calendar.getTime()));
		}else{
			if(0==operateEndDate.length()){
				proportionality.setOperateEndDate(sFormat.format(calendar.getTime()));
			}else{
				proportionality.setOperateEndDate(operateEndDate);
			}
		}
		if(operateStartDate==null){
			calendar.set(Calendar.DATE, 1);
			proportionality.setOperateStartDate(sFormat.format(calendar.getTime()));
		}else{
			if(0==operateStartDate.length()){
				calendar.set(Calendar.DATE, 1);
				proportionality.setOperateStartDate(sFormat.format(calendar.getTime()));
			}else{
				proportionality.setOperateStartDate(operateStartDate);
			}
		}
		proportionality.setDatasource("1");//sg
		if(instId==null){
			proportionality.setInstId(user.getOrgId());
		}else{
			if(0==instId.length()){
				proportionality.setInstId(user.getOrgId());
			}else{
				proportionality.setInstId(instId.split(" ")[0]);
			}
		}
		if(datasource==null||datasource==""){
			proportionality.setAvailable("3");
		}else{
			proportionality.setAvailable(datasource);
		}
		List<Proportionality> list=invoiceSurtaxService.findProportionalityAudit(proportionality);
 		request.setAttribute("RolloutBackAudit", list);
		return SUCCESS ;
	}
	
	public void exportInvoiceAudit(){
		String datasource=proportionality.getDatasource();
		String operateStartDate = proportionality.getOperateStartDate();
		String operateEndDate = proportionality.getOperateEndDate();
		String instId=request.getParameter("instId");
		User user=this.getCurrentUser();
		Calendar calendar= Calendar.getInstance();
		SimpleDateFormat sFormat =new SimpleDateFormat("yyyy-MM-dd");
		Proportionality proportionality=new Proportionality();
		if(operateEndDate==null){
			proportionality.setOperateEndDate(sFormat.format(calendar.getTime()));
		}else{
			if(0==operateEndDate.length()){
				proportionality.setOperateEndDate(sFormat.format(calendar.getTime()));
			}else{
				proportionality.setOperateEndDate(operateEndDate);
			}
		}
		if(operateStartDate==null){
			calendar.set(Calendar.DATE, 1);
			proportionality.setOperateStartDate(sFormat.format(calendar.getTime()));
		}else{
			if(0==operateStartDate.length()){
				calendar.set(Calendar.DATE, 1);
				proportionality.setOperateStartDate(sFormat.format(calendar.getTime()));
			}else{
				proportionality.setOperateStartDate(operateStartDate);
			}
		}
		proportionality.setDatasource("1");//sg
		if(instId==null){
			proportionality.setInstId(user.getOrgId());
		}else{
			if(0==instId.length()){
				proportionality.setInstId(user.getOrgId());
			}else{
				proportionality.setInstId(instId.split(" ")[0]);
			}
		}
		if(datasource==null||datasource==""){
			proportionality.setAvailable("3");
		}else{
			proportionality.setAvailable(datasource);
		}
		List<Proportionality> list=invoiceSurtaxService.findProportionalityAudit(proportionality);
		try {
			//创建Excel表格
			StringBuffer fileName=new StringBuffer("审核详情统计表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			String[] titles={"报税机构编号","报税机构名称","月度","会计机构名称","调整值","操作日期","机构汇总编号","审核状态3-审核拒绝  1-审核通过(有效)","回退原因"};
			String[] fields={"instId","instName","yearMonth","kjIstName","result","operateDate","relationInstId","available","reason"};
			
			ExcelUtil.exportExcel(fileName.toString(), Proportionality.class, fields, titles, list, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceSurtaxAction-exportInvoiceAudit", e);
		}
		
	}
	public void AuditRolloutCheck(){
		String date = request.getParameter("date");
		String instId=date.split("-")[0];
		User user=this.getCurrentUser();
		List<String> reportList=invoiceSurtaxService.getReportInst(user.getOrgId());
		String string ="N";
		if(reportList.contains(instId)){
			string = "Y";
		}
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private Date getRunTime() throws NumberFormatException {
		List<TimeTaskEntity> findTimeTisk = invoiceSurtaxService.findTimeTisk("1");
		String time="";
		for (TimeTaskEntity timeTaskEntity : findTimeTisk) {
			if (timeTaskEntity.getClassName().equals("timeNode")) {
				time=timeTaskEntity.getStartTime();
				break;
			}
		}
		String value=time.split("-")[2];
		if (value.indexOf("0")==0) {
			value=value.substring(1,value.length());
		}
		String[] values=value.split(" ");
		int day=Integer.valueOf(values[0]);
		Calendar cal=Calendar.getInstance();
		
 		String []strings =values[1].split(":");
		int h=Integer.valueOf(strings[0]);
		int m=Integer.valueOf(strings[1]);
		int s=Integer.valueOf(strings[2]);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		cal.set(Calendar.SECOND, s); 
		Date startDate=cal.getTime();
		return startDate;
	}
	
	public String listInvoiceInSurtaxSelectTrack(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String instid=request.getParameter("instId");
		String StartDate= request.getParameter("StartDate");
		String EndDate = request.getParameter("EndDate");
		if(instid==null||""==instid){
			User user = this.getCurrentUser() ;
			instid=user.getOrgId();
		}else{
			instid=instid.split(" ")[0];
		}
		Proportionality proportionality=new Proportionality();
		proportionality.setInstId(instid);
		if(StartDate==null&&EndDate==null||StartDate.length()==0&&EndDate.length()==0){
			Calendar calendar = Calendar.getInstance() ;
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			proportionality.setYearMonth(new SimpleDateFormat("yyyyMM").format(calendar.getTime()));
		}else{
			proportionality.setOperateStartDate(StartDate);
			proportionality.setOperateEndDate(EndDate);
		}
		proportionality.setAvailable("A");
		List<Proportionality> list=invoiceSurtaxService.findProportionalityForRolliut(proportionality);
///*********************
		list=getProportionalityMap(list);
///***************************
		request.setAttribute("track", list);
		return SUCCESS;
	}
	
	public List<Proportionality> getProportionalityMap(List<Proportionality> list){
		List<Proportionality> ls = new ArrayList<Proportionality>();
		Set<String> set =new LinkedHashSet<String>();
		if(list!=null){
			for (Proportionality proportionality : list) {
				set.add(proportionality.getInstId()+"-"+proportionality.getYearMonth());
			}
			for(String string : set){
				Proportionality prop = new Proportionality();
				List<String> obj = new ArrayList<String>();
				for(int k=0;k<list.size();k++){
					Proportionality proportionality = list.get(k);
					if(string.equals(proportionality.getInstId()+"-"+proportionality.getYearMonth())){
						prop.setInstId(string);
						prop.setYearMonth(proportionality.getYearMonth());
						prop.setInstName(proportionality.getInstName());
						obj.add(proportionality.getDatasource()+"-"+proportionality.getDividend()+"-"+proportionality.getDivisor()
							+"-"+proportionality.getResult()+"-"+proportionality.getAvailable());
						prop.setObj(obj);
					}
				}
 				ls.add(prop);
			}
		}
		return ls;
	}
	/**
	 * 科目字典录入
	 * @return
	 */
	public String insertSubjectDic() {
		return SUCCESS;
	}
	//保存录入信息
	public String saveSubjectDic() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String subjectId = request.getParameter("subjectId");
		String subjectName = request.getParameter("subjectName");
		String category = request.getParameter("category");
		String validState = request.getParameter("validState");
		String ratio = request.getParameter("ratio");
		String remark = request.getParameter("remark");
		Map map = new HashMap();
		map.put("subjectId", subjectId);
		map.put("subjectName", subjectName);
		map.put("category", category);
		map.put("validState", validState);
		map.put("ratio", ratio);
		map.put("remark", remark);
		try {
			invoiceSurtaxService.insertSubjectDictionary(map);
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 *新增
	 *日期：2018-08-27
	 *作者：刘俊杰
	 *功能：科目字典查看
	 */
	public String findSubjectDic() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		//获取所有的科目字典
		subjectDicList = invoiceSurtaxService.selectSubjectDicAll();
		return SUCCESS;
	}
	/**
	 *新增
	 *日期：2018-08-27
	 *作者：刘俊杰
	 *功能：科目字典修改
	 */
	public String editSubjectDic() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String id = request.getParameter("id");
		String state = request.getParameter("state");
		Map map = new HashMap();
		map.put("id", id);
		map.put("state", state);
		invoiceSurtaxService.updateSubjectDic(map);
		return SUCCESS;
	}
	//根据模糊查询获取此科目在总账中的余额
	public String getSubjectLedgerMoney() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String subjectName = request.getParameter("subjectName");
		System.out.println(subjectName);
		if("".equals(subjectName.trim())) {
			Map map = new HashMap();
			map.put("money", null);
			map.put("id", 0);
			JSONObject json = JSONObject.fromObject(map);
			result = json.toString();
			return SUCCESS;
		}
		Map map = new HashMap();
		map.put("subjectName", subjectName);
		map.put("instId", this.getCurrentUser().getOrgId());
		try {
			List<SubjectEntity> subjectLedgerMoney = invoiceSurtaxService.getSubjectLedgerMoney(map);
			Map resultMap = new HashMap();
			/**
			 * 修改
			 * 日期：2018-08-22
			 * 作者：刘俊杰
			 * 说明：将查询到的结果写入数据库表t_code_dictionary中
			 */
			//start 2018-08-22
			if(subjectLedgerMoney.get(0).getCreditDescSum() != null) {
				resultMap.put("money", subjectLedgerMoney.get(0).getCreditDescSum()); //查询到的余额
				resultMap.put("id", subjectLedgerMoney.get(0).getDirectionId()); //查询到的科目id
				resultMap.put("name", subjectName); //查询到的科目名称
				//判断该信息是否已经存在于字典表中
				List<CodeDictionary> codeDictionaryList = invoiceSurtaxService.selectCodeDictionaryAll();
				for(CodeDictionary code : codeDictionaryList) {
					if(subjectLedgerMoney.get(0).getDirectionId().equals(code.getCodeName())) {
						map.put("subjectId", subjectLedgerMoney.get(0).getDirectionId());
						invoiceSurtaxService.deleteCodeDictionary(map);
					}
				}
				
			}else {
				resultMap.put("money", 0);
				resultMap.put("id", UUID.randomUUID().toString());
				resultMap.put("name", subjectName); //查询到的科目名称
			}
			//将查询到的信息写入数据库中
			invoiceSurtaxService.insertCodeDictionary(resultMap);
			
			JSONObject json = JSONObject.fromObject(resultMap);
			result = json.toString();
			System.out.println(result);
			//end 2018-08-22
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	//删除已经选取的免税信息
	public String deleteSubjectLedgerMoney() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String id = request.getParameter("subjectId");
			System.out.println(id);
			Map map = new HashMap();
			map.put("subjectId", id);
			List<CodeDictionary> codeDictionaryList = invoiceSurtaxService.selectCodeDictionaryAll();
			for(CodeDictionary code : codeDictionaryList) {
				if(id.equals(code.getCodeName())) {
					invoiceSurtaxService.deleteCodeDictionary(map);
				}
			}
			map.put("message","删除成功");
			
			JSONObject json = JSONObject.fromObject(map);
			result = json.toString();
			return SUCCESS;
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, String> getGeneralLedgerResult() {
		return generalLedgerResult;
	}

	public void setGeneralLedgerResult(Map<String, String> generalLedgerResult) {
		this.generalLedgerResult = generalLedgerResult;
	}

	public Map<String, String> getGeneralLedgerResultOfHand() {
		return generalLedgerResultOfHand;
	}

	public void setGeneralLedgerResultOfHand(Map<String, String> generalLedgerResultOfHand) {
		this.generalLedgerResultOfHand = generalLedgerResultOfHand;
	}

	public List<SubjectDic> getSubjectDicList() {
		return subjectDicList;
	}

	public void setSubjectDicList(List<SubjectDic> subjectDicList) {
		this.subjectDicList = subjectDicList;
	}
	
}