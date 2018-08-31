package com.cjit.vms.trans.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.ImageUtil;
import com.cjit.vms.trans.util.JXLTool;

public class BillTrackAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private BillTrackService billTrackService;
	private VmsCommonService vmsCommonService;
	private List billInfoList;
	private List transInfoList;
	private BillInfo billInfo = new BillInfo();
	private String[] selectBillIds;
	private String message;
	private String flag;
	private String falg;
	private String billId;
	private String filePath;
	private Map chanNelList;
	// 发票状态列表
	private List billDataStatusList = new ArrayList();

	/**
	 * 查询票据列表
	 * 
	 * @return String
	 */
	public String trackBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				fromFlag = null;
			} else if ("select".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			chanNelList = this.vmsCommonService
					.findCodeDictionary("CHANNEL_TYPE");
			billInfo.setDsouRce(request.getParameter("billInfo.dsource"));
			billInfo.setUserId(this.getCurrentUser().getId());
			this.paginationList.setCurrentPage(1);
			//2018-06-08计数新增
			paginationList.setShowCount("true");
			billInfoList = billTrackService.findBillInfoList4CurrentUserInst(billInfo, paginationList,this.getAuthInstList());//added by xl 2016-08-16 
			this.request.setAttribute("paginationList", paginationList);
			billDataStatusList = DataUtil.getAllBillDataStatusList();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-trackBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票跟踪页面查看交易
	 */
	public String viewTransFromBill() {
		try {
			String billId = (String) this.request.getParameter("billId");
			//2018-03-23新增
			String dsource = (String)this.request.getParameter("dsource");
			System.out.println(paginationList.getShowCount());
			if("SG".equals(dsource)){
				transInfoList = billTrackService.findBillByBillId(billId,
						paginationList);
				this.request.setAttribute("status","BILL");
			}else{
				transInfoList = billTrackService.findTransByBillId(billId,
						paginationList);
				this.request.setAttribute("status","TRANS");
			}
			this.request.setAttribute("transInfoList", transInfoList);
			this.request.setAttribute("paginationList", paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewTransFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票跟踪页面查看票据 或者选择一条记录点击打回按钮
	 */
	public String viewBillFromBill() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);
			List billItemList = billTrackService.findBillItemByBillId(billId);

			this.request.setAttribute("billInfo", billInfo);
			this.request.setAttribute("billItemList", billItemList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewBillFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票跟踪页面查看票样
	 */
	public String viewImgFromBill() {
		try {
			falg = (String) request.getParameter("falg");
			String billId = (String) this.request.getParameter("billId");
			BillInfo bill = billTrackService.findBillInfoById(billId);
			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", bill.getFapiaoType());
			map.put("billCode", bill.getBillCode());
			map.put("billNo", bill.getBillNo());
			map.put("billDate", bill.getBillDate());
			map.put("customerName", bill.getName());
			map.put("customerTaxno", bill.getTaxno());
			map.put("customerAddressandphone", bill.getAddressandphone());
			map.put("customerBankandaccount", bill.getBankandaccount());
			// map.put("billPasswd",billInfo);
			map.put("billItemList", billItemList);
			map.put("cancelName", bill.getCustomerName());
			map.put("cancelTaxno", bill.getCustomerTaxno());
			map.put("cancelAddressandphone", bill.getCustomerAddressandphone());
			map.put("cancelBankandaccount", bill.getCustomerBankandaccount());
			map.put("payeeName", bill.getPayee());
			map.put("reviewerName", bill.getReviewer());
			map.put("drawerName", bill.getDrawer());
			map.put("remark", bill.getRemark());

			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;
			request.setAttribute("falg", falg);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票跟踪页面，点击撤销按钮跳转状态选择页面
	 * 
	 * @return
	 */
	public String toBackBill() {
		String billIds = request.getParameter("billIds");
		billInfoList = billTrackService.findBillItemByBillIds(
				billIds.split(","), paginationList);

		this.request.setAttribute("billInfoList", billInfoList);
		return SUCCESS;
	}

	/**
	 * 发票跟踪页面打回按钮
	 */
	public void backBill() {
		String ids = request.getParameter("ids");
		String newStatuses = request.getParameter("newStatuses");
		String[] dataStatuses = newStatuses.split(",");
		String[] billIds = ids.split(",");
		for (int i = 0; i < billIds.length; i++) {
			BillInfo bill = new BillInfo();
			bill.setBillId(billIds[i].trim());
			if ("3".equals(dataStatuses[i])) {
				// 删除vms_bill_info信息
				billTrackService.deleteBillInfoById(billIds[i].trim());
				// 更改vms_trans_info信息状态为1-未开票
				billTrackService.updateTransInfoStatus("1", billIds[i].trim());
				// 删除vms_trans_bill信息
				billTrackService.deleteTransBillInfo(billIds[i].trim());
				// 删除vms_bill_item_info信息
				billTrackService.deleteBillItemInfo(billIds[i].trim());
			} else {
				// 更改vms_bill_info信息状态
				bill.setDataStatus(dataStatuses[i].trim());
				billTrackService.updateBillInfoStatus(bill);
				// 更改vms_bill_info信息状态为2-开票编辑锁定中
				billTrackService.updateTransInfoStatus("2", billIds[i].trim());
			}
		}
	}

	/**
	 * 导出
	 * 
	 * @throws Exception
	 */
	public void exportBill() throws Exception {
		//billInfoList = billTrackService.findBillInfoList(billInfo); annotated by xl 2016-08-16
		billInfoList = billTrackService.findBillInfoList4CurrentUserInst(billInfo,this.getAuthInstList());//added by xl 2016-08-16

		StringBuffer fileName = new StringBuffer("票据查询信息列表");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, billInfoList);
		os.flush();
		os.close();
	}

	/**
	 * 按查询条件，导出xml格式的所有已开具和已打印的票据信息
	 * @throws Exception
	 */
	public void exportBillAsXml() throws Exception {
		try {
			this.log.info("导出票据查询结果为xml开始");
			List billInfos = new ArrayList();
			billInfo.setDataStatus("5");//查询条件的datastatus设为已开具
			billInfos.addAll(billTrackService.findBillInfoList4CurrentUserInst(billInfo,this.getAuthInstList()));//添加已开具的票据
			billInfo.setDataStatus("8");//查询条件的datastatus设为已打印
			billInfos.addAll(billTrackService.findBillInfoList4CurrentUserInst(billInfo,this.getAuthInstList()));//添加已打印的票价
			
			this.log.info("获取票价查询结果");
			StringBuffer fileName = new StringBuffer("票据查询信息列表");
			fileName.append(".xml");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeBillAsXmlToResponseStream(os,billInfos);
			this.log.info("导出票据查询结果为xml结束");
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			this.log.error(baos.toString());
			throw e;
		}
		
	}

	private void writeBillAsXmlToResponseStream(OutputStream os,
			List billInfoList) throws IOException {
		//生成xml格式的发票信息
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Kp");
		
		Element Fpxx = root.addElement("Fpxx");
		
		Element Zsl = Fpxx.addElement("Zsl");
		Zsl.addText(billInfoList.size()+"");
		
		Element Fpsj = Fpxx.addElement("Fpsj");
		for (int i = 0; i < billInfoList.size(); i++) {
			BillInfo tempBill = (BillInfo) billInfoList.get(i);
			Element tempFp = Fpsj.addElement("Fp");
			
			tempFp.addElement("Djh");
			if("".equals(tempBill.getFapiaoType())){
				tempFp.addElement("Fpzl").addText("专用发票");
			}else{
				tempFp.addElement("Fpzl").addText("普通发票");
			}
			tempFp.addElement("Lbdm").addText(tempBill.getBillCode());
			tempFp.addElement("Fphm").addText(tempBill.getBillNo());
			tempFp.addElement("Kprq").addText(tempBill.getBillDate().replaceAll("-", ""));
			tempFp.addElement("Gfmc").addText(tempBill.getCustomerName());
			tempFp.addElement("Gfsh").addText(tempBill.getCustomerTaxno());
			tempFp.addElement("Gfyhzh").addText(tempBill.getCustomerBankandaccount()==null ? "" : tempBill.getCustomerBankandaccount());
			tempFp.addElement("Gfdzdh").addText(tempBill.getCustomerAddressandphone()==null ? "" : tempBill.getCustomerAddressandphone());
			tempFp.addElement("Xfmc").addText(tempBill.getName());
			tempFp.addElement("Xfsh").addText(tempBill.getTaxno());
			tempFp.addElement("Xfyhzh").addText(tempBill.getBankandaccount());
			tempFp.addElement("Xfdzdh").addText(tempBill.getAddressandphone());
			tempFp.addElement("Hjje").addText(NumberUtils.format(tempBill.getAmtSum(), "", 2));
			tempFp.addElement("Hjse").addText(NumberUtils.format(tempBill.getTaxAmtSum(), "", 2));
			tempFp.addElement("Bz").addText(tempBill.getRemark());
			tempFp.addElement("Kpr").addText(tempBill.getDrawer());
			tempFp.addElement("Fhr").addText(tempBill.getReviewer());
			tempFp.addElement("Skr").addText(tempBill.getPayee());
			
			Element Spxx = tempFp.addElement("Spxx");
			List billItemList =this.billInfoService.findBillItemInfoList(new BillItemInfo(tempBill.getBillId(), null));
			for(int j = 0;j<billItemList.size();j++){
				BillItemInfo billItemInfo  = (BillItemInfo) billItemList.get(j);
				Element Sph = Spxx.addElement("Sph");
				Sph.addElement("Xh").addText(""+(j+1));
				Sph.addElement("Spmc").addText(billItemInfo.getGoodsName());
				Sph.addElement("Ggxh").addText(billItemInfo.getSpecandmodel());
				Sph.addElement("Jldw").addText(billItemInfo.getGoodsUnit());
				Sph.addElement("Dj").addText(NumberUtils.format(billItemInfo.getGoodsPrice(), "", 2));
				Sph.addElement("Sl").addText(NumberUtils.format(billItemInfo.getGoodsNo(), "", 2));
				Sph.addElement("Je").addText(NumberUtils.format(billItemInfo.getAmt(), "", 2));
				Sph.addElement("Slv").addText(NumberUtils.format(billItemInfo.getTaxRate(), "", 2));
				Sph.addElement("Se").addText(NumberUtils.format(billItemInfo.getTaxAmt(), "", 2));
			}
			//NumberUtils.format
		}
		
		
		//设置文件编码  
        OutputFormat xmlFormat = new OutputFormat();  
        xmlFormat.setEncoding("GBK"); 
        // 设置换行 
        xmlFormat.setNewlines(true); 
        // 生成缩进 
        xmlFormat.setIndent(true); 
        // 使用1个空格进行缩进, 可以兼容文本编辑器 
        xmlFormat.setIndent(" "); 
        
        
        XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(os),xmlFormat);
        xmlWriter.write(document);
        xmlWriter.close();
	}

	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("票据查询信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "机构", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "申请开票日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "客户地址电话", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "客户银行账号", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "开票人", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "复核人", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "收款人", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "撤销发起人", JXLTool.getHeader());
		Label header18 = new Label(i++, 0, "撤销审核人", JXLTool.getHeader());
		Label header19 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header20 = new Label(i++, 0, "开票机号", JXLTool.getHeader());
		Label header21 = new Label(i++, 0, "通知单编号", JXLTool.getHeader());
		Label header22 = new Label(i++, 0, "原始票据代码", JXLTool.getHeader());
		Label header23 = new Label(i++, 0, "原始票据号码", JXLTool.getHeader());
		Label header24 = new Label(i++, 0, "是否手工录入", JXLTool.getHeader());
		Label header25 = new Label(i++, 0, "开具类型", JXLTool.getHeader());
		Label header26 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header27 = new Label(i++, 0, "状态", JXLTool.getHeader());
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
		ws.addCell(header19);
		ws.addCell(header20);
		ws.addCell(header21);
		ws.addCell(header22);
		ws.addCell(header23);
		ws.addCell(header24);
		ws.addCell(header25);
		ws.addCell(header26);
		ws.addCell(header27);
		for (int j = 0; j < 27; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			BillInfo bill = (BillInfo) content.get(c);
			int column = count++;
			setWritableSheet(ws, bill, column);
		}
		wb.write();
		wb.close();

	}

	private void setWritableSheet(WritableSheet ws, BillInfo bill, int column)
			throws Exception {
		int i = 0;
		Label cell1 = new Label(i++, column, column + "",
				JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, bill.getBankName(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, bill.getApplyDate(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, bill.getBillDate(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, bill.getBillCode(),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, bill.getBillNo(),
				JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, bill.getCustomerName(),
				JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, bill.getCustomerTaxno(),
				JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, bill.getCustomerAddressandphone(),
				JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, bill.getCustomerBankandaccount(),
				JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, NumberUtils.format(
				bill.getAmtSum(), "", 2), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, NumberUtils.format(
				bill.getTaxAmtSum(), "", 2), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, NumberUtils.format(
				bill.getSumAmt(), "", 2), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, bill.getDrawerName(),
				JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, bill.getReviewerName(),
				JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, bill.getPayee(),
				JXLTool.getContentFormat());
		Label cell17 = new Label(i++, column, bill.getCancelInitiatorName(),
				JXLTool.getContentFormat());
		Label cell18 = new Label(i++, column, bill.getCancelAuditorName(),
				JXLTool.getContentFormat());
		Label cell19 = new Label(i++, column, bill.getTaxDiskNo(),
				JXLTool.getContentFormat()); // 税控盘号
		Label cell20 = new Label(i++, column, bill.getMachineNo(),
				JXLTool.getContentFormat());
		Label cell21 = new Label(i++, column, bill.getNoticeNo(),
				JXLTool.getContentFormat());
		Label cell22 = new Label(i++, column, bill.getOriBillCode(),
				JXLTool.getContentFormat());
		Label cell23 = new Label(i++, column, bill.getOriBillNo(),
				JXLTool.getContentFormat());
		Label cell24 = new Label(i++, column, DataUtil.getIsHandiworkCH(bill
				.getIsHandiwork()), JXLTool.getContentFormat());
		Label cell25 = new Label(i++, column, DataUtil.getIssueTypeCH(bill
				.getIssueType()), JXLTool.getContentFormat());
		Label cell26 = new Label(i++, column, DataUtil.getFapiaoTypeCH(bill
				.getFapiaoType()), JXLTool.getContentFormat());
		Label cell27 = new Label(i++, column, DataUtil.getDataStatusCH(
				bill.getDataStatus(), "BILL"), JXLTool.getContentFormat());

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
		ws.addCell(cell19);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
		ws.addCell(cell24);
		ws.addCell(cell25);
		ws.addCell(cell26);
		ws.addCell(cell27);

	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public List getBillDataStatusList() {
		return billDataStatusList;
	}

	public void setBillDataStatusList(List billDataStatusList) {
		this.billDataStatusList = billDataStatusList;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFalg() {
		return falg;
	}

	public void setFalg(String falg) {
		this.falg = falg;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}
	
	private static String fleName ;
	public  String viewImgFromBillSY(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		
		String fileName=request.getParameter("fileName");
		fleName=fileName;
		String path=ImageUtil.imageShowEach(fileName, 0);
		String imageType="N";
 		if(path.endsWith(".pdf")){
 			imageType="Y";
 		} 
 		request.setAttribute("imageType", imageType);
		int count=ImageUtil.FilelengthEach(fileName);
		request.setAttribute("count", count-1);
 		if(!path.endsWith("/")){
 			request.getSession().setAttribute("fileName", path);
 			System.err.println("要获取的文件路径："+path);
 			return SUCCESS;
 		}else{
 			return "tax";
 		}
	}
	
	public void viewImgFromInvoiceFJOfBillAjax(){
		try{
			int count =ImageUtil.FilelengthEach(fleName);
			int index=Integer.parseInt(request.getParameter("index"));
			String val=request.getParameter("val");
			int dex = 0;
			if("1".equals(val)){
				dex = count-index ;
			}else{
				dex = count-index-2;
			}
			String path =ImageUtil.imageShowEach(fleName, dex);
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
