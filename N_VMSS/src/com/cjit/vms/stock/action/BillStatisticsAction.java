package com.cjit.vms.stock.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.fastjson.JSON;
import com.cjit.common.constant.Constants;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.LostRecycle;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.stock.util.ExcelUtil;
import com.cjit.vms.stock.util.StockUtil;
import com.cjit.vms.taxdisk.single.model.parseXml.ReportTaxDiskRetrieveReturnXml;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.ibm.db2.jcc.b.r;

/**
 * 发票统计
 *
 */
public class BillStatisticsAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private StockService stockService;
	private BillDistribution billDistribution = new BillDistribution();
	/**
	 * 查询发票统计记录列表
	 */
	public String billStatisticsList() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String instId = billDistribution.getInstId();
		if (StringUtils.isNotEmpty(instId)&&instId.length()!=0) {
			billDistribution.setInstId(instId.split(" ")[0]);
		}else{
			User user=this.getCurrentUser();
			billDistribution.setInstId(user.getOrgId());
		}
		this.paginationList.setShowCount("false");
		List<BillDistribution> billDistributionList = stockService.findDistribution(billDistribution, paginationList);
		this.request.setAttribute("billDistributionList", billDistributionList);
		String massage=request.getParameter("massage");
		if("track".equals(massage)){
			return "tax";
		}
		return "success";
	}
	/**
	 * 发票接收确认
	 * @return
	 */
	public void jsEnter(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ;
		}
		String disId=request.getParameter("disId");
		BillDistribution billDistribution=new BillDistribution();
		billDistribution.setDisId(disId);
		billDistribution.setJsEnter(StockUtil.JS_ENTENER_YES);
		List<BillDistribution> list=new ArrayList<BillDistribution>();
		list.add(billDistribution);
		stockService.updateBillDistribution(list);
		try {
			returnResult(new AjaxReturn(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void exportBillDistribution() throws IOException{
		String instId = billDistribution.getInstId();
		if (instId!=null&&instId.length()!=0) {
			billDistribution.setInstId(instId.split(" ")[0]);
		}else{
			User user=this.getCurrentUser();
			billDistribution.setInstId(user.getOrgId());
		}
		List<BillDistribution> dataList = stockService.findDistribution(billDistribution);
		StringBuffer fileName=new StringBuffer("发票库存信息统计表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		//Content-Disposition:attachment;filename=%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E5%88%97%E8%A1%A8.xls
		//Content-Type:application/vnd.ms-excel
		//Date:Wed, 14 Dec 2016 02:09:13 GMT
		//Server:Apache-Coyote/1.1
		//Transfer-Encoding:chunked
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		String[] titles={"分发日期","发票类型0-专票 1-普票","开票员编号","开票员名称","开票终端编号","发票代码","发票起始号码","发票截止号码","分发张数","接收确认 0:未确认 1:已确认","已开票张数","已打印张数","已作废张数","已红冲张数","开票员所属机构","机构名称","空白遗失张数","空白回收张数","空白作废张数"};
		String[] fields={"disDate","billType","kpyId","kpyName","taxNo","billId","billStartNo","billEndNo","ffCount","jsEnter","ykpCount","ydyCount","yffCount","yhcCount","instId","instName","syfpYsCount","syfpHsCount","syfpZfCount"};
		ExcelUtil.exportExcel(fileName.toString(), BillDistribution.class, fields, titles, dataList, os);
		os.flush();
		os.close();
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

	public BillDistribution getBillDistribution() {
		return billDistribution;
	}

	public void setBillDistribution(BillDistribution billDistribution) {
		this.billDistribution = billDistribution;
	}

	public PaginationList getPaginationList() {
		return paginationList;
	}

	public void setPaginationList(PaginationList paginationList) {
		this.paginationList = paginationList;
	}

	public String goSelect(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		//得到要查看的分发编号
		String bill =request.getParameter("disId");
		LostRecycle lostRecycle=new LostRecycle();
		lostRecycle.setBillId(bill.split("-")[1]);
		List<LostRecycle> ysList=new ArrayList<LostRecycle>();
		List<LostRecycle> hsList=new ArrayList<LostRecycle>();
		List<LostRecycle> zfList=new ArrayList<LostRecycle>();
		List<LostRecycle> list=stockService.findLostRecycle(lostRecycle);
		BillDistribution billDistribution=new BillDistribution();
		billDistribution.setDisId(bill.split("-")[0]);
		billDistribution=stockService.findBbillDistributionbykyid(billDistribution);
		
		//统计遗失  回收  作废发票信息
		request.setAttribute("List", list);
		request.setAttribute("billDistribution", billDistribution);
		List<BillInfo> billInfos=stockService.findBillMakeUseDistribution(bill.split("-")[1], bill.split("-")[2], bill.split("-")[3]);
//		request.setAttribute("billInfos", billInfos);
		request.setAttribute("JCXX", bill);
		List<BillInfo> kjList=new ArrayList<BillInfo>();
		List<BillInfo> kjzfList=new ArrayList<BillInfo>();
		List<BillInfo> kjhcList=new ArrayList<BillInfo>();
		List<BillInfo> qtList=new ArrayList<BillInfo>();
		for (BillInfo billInfo : billInfos) {
			if("5".equals(billInfo.getDataStatus())){
				kjList.add(billInfo);
			}else if("15".equals(billInfo.getDataStatus())){
				kjzfList.add(billInfo);
			}else if("18".equals(billInfo.getDataStatus())||"99".equals(billInfo.getDataStatus())){
				kjhcList.add(billInfo);
			}else{
				qtList.add(billInfo);
			}
		}
		request.setAttribute("kjList", kjList);
		request.setAttribute("kjzfList", kjzfList);
		request.setAttribute("kjhcList", kjhcList);
		request.setAttribute("qtList", qtList);
		
		return SUCCESS;
	}
	
}
