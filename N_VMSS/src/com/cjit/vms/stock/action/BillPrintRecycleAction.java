package com.cjit.vms.stock.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.PrintBill;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.stock.util.ExcelUtil;
import com.cjit.vms.stock.util.StockUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
/**
 * 已打印发票回收
 *
 */
public class BillPrintRecycleAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private StockService stockService;
	private PrintBill printBill=new PrintBill();
	/**
	 * 查询已打印发票列表
	 * @return
	 */
	public String billPrintRecycleList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User user=this.getCurrentUser();
		String currentInstId=user.getOrgId();
		
		String sbillStartNo=request.getParameter("SbillStartNo");
		String sbillEndNo=request.getParameter("SbillEndNo");
		List<PrintBill> biglist=new ArrayList<PrintBill>();
		
		if(sbillStartNo!=null&&sbillEndNo!=null&&sbillStartNo!=""&&sbillEndNo!=""){
			
			BillDistribution billDistribution=new BillDistribution();
			billDistribution.setBillStartNo(sbillStartNo);
			billDistribution.setBillEndNo(sbillEndNo);
			billDistribution.setBillId(printBill.getBillId());
			List<BillDistribution> list=stockService.findDistribution(billDistribution);
			List<PrintBill> listprint=stockService.findPrintBills(printBill);
			if (list!=null&&list.size()>0) {
				for (BillDistribution bill : list) {
					for(int i=0;i<listprint.size();i++){
						PrintBill pBill=listprint.get(i);
						if(pBill.getBillCode().equals(bill.getBillId())){
							if(Integer.parseInt(pBill.getBillNo())<=Integer.parseInt(bill.getBillEndNo())&&
									Integer.parseInt(bill.getBillStartNo())<=Integer.parseInt(pBill.getBillNo())){
								pBill.setSbillStartNo(bill.getBillStartNo());
								pBill.setSbillEndNo(bill.getBillEndNo());
								biglist.add(pBill);
							}
						}
					}
				}
			}
		}else{
			printBill.setInstId(currentInstId);
			this.paginationList.setShowCount("false");
			List<PrintBill> printBillList = stockService.findPrintBills(printBill, paginationList);
			if(printBillList!=null&&printBillList.size()>0){
				for(int i=0;i<printBillList.size();i++){
					PrintBill  print=printBillList.get(i);
					String billCode=print.getBillCode();
					String billNo=print.getBillNo();
					String billStarNoAndbillEndNo=getbillStarNoAndbillEndNo(billCode, billNo);
					if(billStarNoAndbillEndNo!=null&&billStarNoAndbillEndNo!=""){
						print.setSbillStartNo(billStarNoAndbillEndNo.split("-")[0]);
						print.setSbillEndNo(billStarNoAndbillEndNo.split("-")[1]);
					}else{
						print.setSbillStartNo("");
						print.setSbillEndNo("");
					}
					biglist.add(print);
				}
			}
		}
		this.request.setAttribute("list", biglist);
		//	this.request.setAttribute("printBillList", printBillList);
		return "success";
	}
	
	/**
	 * 导入
	 * @throws IOException
	 */
	public void exportPrintBill() throws IOException{
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ;
		}
		User user=this.getCurrentUser();
		String currentInstId=user.getOrgId();
		printBill.setInstId(currentInstId);
		List<PrintBill> dataList = stockService.findPrintBills(printBill);
		StringBuffer fileName=new StringBuffer("已打印发票回收统计表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		String[] titles={"投保单号","保单号","发票代码","发票号码","金额","税额","价税合计","开票日期","客户名称","发票类型","发票状态","回收状态","开票机构编号"};
		String[] fields={"ttmprcNo","insureId","billCode","billNo","amtSum","taxAmtSum","sumAmt","billDate","customerName","billType","datastatus","recycleStatus","instId"};
		ExcelUtil.exportExcel(fileName.toString(), PrintBill.class, fields, titles, dataList, os);
		os.flush();
		os.close();
	}
	/**
	 * 发票回收确认
	 * @return
	 */
	public void recycleEnter(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ;
		}
		String billId=request.getParameter("billId");
		PrintBill printBill=new PrintBill();
		printBill.setBillId(billId);
		printBill.setRecycleStatus(StockUtil.PRINT_BILL_RECYCLE_YES);
		List<PrintBill> list=new ArrayList<PrintBill>();
		list.add(printBill);
		stockService.updatePrintBill(list);
		try {
			returnResult(new AjaxReturn(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}
	public StockService getStockService() {
		return stockService;
	}
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}



	public PrintBill getPrintBill() {
		return printBill;
	}



	public void setPrintBill(PrintBill printBill) {
		this.printBill = printBill;
	}
	private String getbillStarNoAndbillEndNo(String billCode,String billNo){
		String billStarNoAndbillEndNo=null;
		BillDistribution billDistribution=new BillDistribution();
		billDistribution.setBillId(billCode);
		List<BillDistribution> list=stockService.findDistribution(billDistribution);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				billDistribution=list.get(i);
				String billStarNo=billDistribution.getBillStartNo();
				String billEndNo=billDistribution.getBillEndNo();
				if(billStarNo!=null&&billStarNo!=""&&billEndNo!=""&&billEndNo!=null&&billNo!=null&&billEndNo!=""&&
						Integer.parseInt(billStarNo)<=Integer.parseInt(billNo)&&Integer.parseInt(billNo)<=Integer.parseInt(billEndNo)){
						billStarNoAndbillEndNo=billStarNo+"-"+billEndNo;
				}else{
					billStarNoAndbillEndNo="";
				}
			}
		}else{
			billStarNoAndbillEndNo="";
		}
		return billStarNoAndbillEndNo;
	}
}
