package com.cjit.vms.stock.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.stock.util.ExcelUtil;
import com.cjit.vms.stock.util.StockUtil;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.DataUtil;

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

public class BillInventoryAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private StockService stockService;
	private BillInventory billInventory=new BillInventory();
	private List<BillInventory> billInventoryList;
	/**
	 * 库存管理展示页面
	 * @return
	 */
	public String billInventoryList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User user=this.getCurrentUser();
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			//获取页面查询条件
			String string=request.getParameter("instId");
			if(string!=null&&string.length()!=0){
				String instId=string.split(" ")[0];
				billInventory.setInstId(instId);
			}else{
				billInventory.setInstId(user.getOrgId());
			}
			this.paginationList.setShowCount("false");
			stockService.findBillInventory(billInventory,paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInventoryAction-BillInventoryList", e);
		}
		return ERROR;
	
	}
	
	public String addbillInventory(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 *  发票入库库存信息录入
	 * @return
	 */
	public String savebillInventory(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			billInventory.setInstId(request.getParameter("instId").split(" ")[0]);
			billInventory.setInstName(request.getParameter("instId").split(" ")[2]);
			billInventory.setBillId(request.getParameter("billId"));
			billInventory.setBillStartNo(request.getParameter("billStarNo"));
			billInventory.setBillEndNo(request.getParameter("billEndNo"));
			billInventory.setBillType(request.getParameter("billType"));
			billInventory.setPutInDate(new Date());
			int billEndNocount=Integer.parseInt(request.getParameter("billEndNo"));
			int billStarNocount=Integer.parseInt(request.getParameter("billStarNo"));
			billInventory.setCount(billEndNocount-billStarNocount+1);
			billInventory.setSyCount(String.valueOf(billEndNocount-billStarNocount+1));
			billInventoryList=new ArrayList<BillInventory>();
			billInventoryList.add(billInventory);
			stockService.insertBillInventory(billInventoryList);
		}catch(Exception e){
			e.printStackTrace();
			log.error("BillInventoryAction-saveBillInventory", e);
		}
		return SUCCESS;
	}
	/**
	 *  发票新增校验
	 * @return
	 */
	public String addjy(){
		String  billid=request.getParameter("billId");
		int  StarNo=Integer.valueOf(request.getParameter("billStarNo"));
		int  EndNo=Integer.valueOf(request.getParameter("billEndNo"));
		String type=request.getParameter("billType");
		List<Integer> listadd=new ArrayList<Integer>();
		for(int i=StarNo;i<=EndNo ;i++){
			listadd.add(i);
		}
		String result="Y";
		BillInventory billInventory=new BillInventory();
		billInventory.setBillId(billid);
		billInventory.setBillType(type);
		List<BillInventory> list=stockService.findBillInventory(billInventory);
		List<String> commonList = new ArrayList<String>();// 保存公共的集合
		//同一种发票类型    同一发票代码
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				BillInventory bill=list.get(i);
				int billStartNo=Integer.valueOf(bill.getBillStartNo());
				int billEndNo=Integer.valueOf(bill.getBillEndNo());
				for(int j=billStartNo;j<=billEndNo;j++){
					if(listadd.contains(j)){
						commonList.add(j+"");
					}
				}	
 			}
		}
		if(commonList.size()>0){
			result="N";
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发票库存统计报表下载
	 * @return
	 */
	public String createbillInventoryExcel(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
		}
		try{
		//获取页面查询条件
		String string=request.getParameter("instId");
		if(string!=null&&string.length()!=0){
			String instId=string.split(" ")[0];
			billInventory.setInstId(instId);
		}else{
			User user=this.getCurrentUser();
			billInventory.setInstId(user.getOrgId());
		}
		List<BillInventory> list=stockService.findBillInventory(billInventory);
		//创建Excel表格
		StringBuffer fileName=new StringBuffer("发票库存信息统计表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
	//	this.writeToExcel(os, list);
		String[] titles={"编号","发票类型0-专票 1-普票","机构代码","机构名称","入库时间","发票代码","发票起始号码","发票截止号码","总张数","发票剩余张数"};
		String[] fields={"inventoryId","billType","instId","instName","putInDate","billId","billStartNo","billEndNo","count","syCount"};
		ExcelUtil.exportExcel(fileName.toString(), BillInventory.class, fields, titles, list, os);		
		
		
		
		os.flush();
		os.close();
		}catch(Exception e){
			e.printStackTrace();
			log.error("BillInventoryAction-createbillInventoryExcel", e);
		}
		return null;
	}
	
	private void writeToExcel(OutputStream oStream,List<BillInventory> list) throws IOException, WriteException{
		WritableWorkbook workbook=Workbook.createWorkbook(oStream);
		WritableSheet ws=workbook.createSheet("发票库存信息统计表", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "编号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "机构代码", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(2, 0, "机构名称", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(3, 0, "入库日期", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(4, 0, "总张数", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(6, 0, "发票起始号码", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(7, 0, "发票截止号码", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(8, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(9, 0, "发票剩余张数", JXLTool.getHeaderC(excelInfo));
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
		for (int i = 0; i <= 10; i++) {
			ws.setColumnView(i, 30);
		}
		int count =1;
		for(int i=0;i<list.size();i++){
			BillInventory billInventory=list.get(i);
			int column = count++;
			this.setWritableSheet(ws, billInventory, column);
		}
		workbook.write();
		workbook.close();
		
	}
	
	private void setWritableSheet(WritableSheet wSheet,BillInventory billInventory,int column) throws WriteException{
		
		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		Label label1=new Label(0, column, String.valueOf(column), tempCellFormat);
		Label label2=new Label(1, column, billInventory.getInstId()==null?"":billInventory.getInstId(), tempCellFormat);
		Label label3=new Label(2, column, billInventory.getInstName()==null?"":billInventory.getInstName(), tempCellFormat);
		Label label4=new Label(3, column, billInventory.getPutInDate()==null?"":StockUtil.getDate(billInventory.getPutInDate()), tempCellFormat);
		Label label5=new Label(4, column, billInventory.getCount()==0?"":String.valueOf(billInventory.getCount()), tempCellFormat);
		Label label6=new Label(5, column, billInventory.getBillId()==null?"":billInventory.getBillId(), tempCellFormat);
		Label label7=new Label(6, column, billInventory.getBillStartNo()==null?"":billInventory.getBillStartNo(), tempCellFormat);
		Label label8=new Label(7, column, billInventory.getBillEndNo()==null?"":billInventory.getBillEndNo(), tempCellFormat);
		Label label9=new Label(8, column, billInventory.getBillType()==null?"":DataUtil.getFapiaoTypeCH(billInventory.getBillType()), tempCellFormat);
		Label label10=new Label(9, column, billInventory.getSyCount()==null?"0":billInventory.getSyCount().toString(), tempCellFormat);
		wSheet.addCell(label1);
		wSheet.addCell(label2);
		wSheet.addCell(label3);
		wSheet.addCell(label4);
		wSheet.addCell(label5);
		wSheet.addCell(label6);
		wSheet.addCell(label7);
		wSheet.addCell(label8);
		wSheet.addCell(label9);
		wSheet.addCell(label10);
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
    /**
     * 跳转发票分发页面
     * @return
     */
    public String initDistrubute(){
    	if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
    	 String inventoryId=request.getParameter("inventoryId");
    	 billInventory=new BillInventory();
    	 billInventory.setInventoryId(inventoryId);
    	 List<BillInventory> list=stockService.findBillInventory(billInventory);
    	 request.setAttribute("list", list);
    	 return  SUCCESS;
    }
   
	public String massageinstId(){
    	if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
    	try{
	    	//获取开票员编号
	    	String instId=request.getParameter("instId").trim();
	    	List<User>  list=stockService.findUsersByOrgId(instId);
 	    	JSONObject jsonObject=new JSONObject();
 	    	if (list!=null&&list.size()>0) {
 	    		for(int i=0;i<list.size();i++){
 	    			User user=list.get(i);
 	    			jsonObject.put(user.getId(), user.getName());
 	    		}
 	    	}else{
 	    		jsonObject.put("N", "N");
 				}
 	    	response.setCharacterEncoding("UTF-8");
 	    	response.getWriter().print(jsonObject);
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("BillInventoryAction-massageinstId", e);
    	}
    	return null;
    }
    
    
    /**
     *  分发操作
     * @return
     */
    public String doDistrubute(){
    	if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
    	if(!"frm".equals(request.getParameter("massage"))){
    		return ERROR;
    	}
    	try {
    		String valuess=request.getParameter("valuess");
    		int count=Integer.valueOf(valuess);
    		BillDistribution billDistribution=new BillDistribution();
    		billDistribution.setInstId(request.getParameter("instName").split(" ")[0]);
    		billDistribution.setInstName(request.getParameter("instName").split(" ")[2]);
			billDistribution.setKpyId(request.getParameter("kpyId"));
			billDistribution.setKpyName(request.getParameter("kpyName"));
			billDistribution.setBillType(DataUtil.getFapiaoTypeCode(request.getParameter("billType")));
			billDistribution.setTaxNo(request.getParameter("taxNo"));
			billDistribution.setBillId(request.getParameter("billId"));
			billDistribution.setBillStartNo(request.getParameter("billStartNo"));
			billDistribution.setBillEndNo(request.getParameter("billEndNo"));
			billDistribution.setFfCount(Integer.valueOf(request.getParameter("count")));
			billDistribution.setInventoryId(request.getParameter("inventoryId"));
			billDistribution.setJsEnter("0");
			billDistribution.setTaxNo(request.getParameter("taxNo"));
			billDistribution.setDisDate(new Date());
			billDistribution.setSyCount(Integer.valueOf(request.getParameter("count")));
			billDistribution.setYkpCount(0);
			billDistribution.setYdyCount(0);;
			billDistribution.setYffCount(0);
			billDistribution.setYhcCount(0);
			List<BillDistribution> billDistributionList=new ArrayList<BillDistribution>();
			billDistributionList.add(billDistribution);
			stockService.insertBillDistribution(billDistributionList);
			billInventory.setInventoryId(request.getParameter("inventoryId"));
			//设置剩余张数
			billInventory.setSyCount(String.valueOf(count-Integer.valueOf(request.getParameter("count"))));
			billInventoryList=new ArrayList<BillInventory>();
			billInventoryList.add(billInventory);
			stockService.updateBillInventory(billInventoryList);
			return SUCCESS;
		} catch (Exception e) {
			e.getStackTrace();
			log.error("BillInventoryAction-doDistrubute", e);
		}
    	return SUCCESS;
    } 
    
    /**
     * 
     * @return
     */
    public String updatebillInventory(){
    	if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
    	String inventoryId=request.getParameter("inventoryId");
    	billInventory.setInventoryId(inventoryId);
    	List<BillInventory> list=stockService.findBillInventory(billInventory);
    	if(list!=null&&list.size()>0){
    		billInventory=list.get(0);
    	}
    	request.setAttribute("billInventory", billInventory);
    	return SUCCESS;
    }
    public String updatbill(){
    	if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
    	billInventory.setInventoryId(request.getParameter("inventoryId"));
    	billInventory.setBillId(request.getParameter("billId"));
    	billInventory.setBillStartNo(request.getParameter("billStartNo"));
    	billInventory.setBillEndNo(request.getParameter("billEndNo"));
    	billInventory.setBillType(request.getParameter("billType"));
    	billInventory.setInstId(request.getParameter("instName").split(" ")[0]);
    	billInventory.setInstName(request.getParameter("instName").split(" ")[2]);
    	billInventory.setCount(Integer.valueOf(request.getParameter("billEndNo"))-Integer.valueOf(request.getParameter("billStartNo"))+1);
    	billInventory.setSyCount(String.valueOf(Integer.valueOf(request.getParameter("billEndNo"))-Integer.valueOf(request.getParameter("billStartNo"))+1));
    	List<BillInventory> billInventoryList=new ArrayList<BillInventory>();
    	billInventoryList.add(billInventory);
    	stockService.updateBillInventory(billInventoryList);
    	return SUCCESS;
    }
    
    
	public StockService getStockService() {
		return stockService;
	}


	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public BillInventory getBillInventory() {
		return billInventory;
	}

	public void setBillInventory(BillInventory billInventory) {
		this.billInventory = billInventory;
	}

}
