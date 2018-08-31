package com.cjit.vms.stock.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.DocFlavor.STRING;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.alibaba.fastjson.JSON;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.BillInventory;
import com.cjit.vms.stock.entity.LostRecycle;
import com.cjit.vms.stock.service.StockService;
import com.cjit.vms.stock.util.ExcelUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.ImageUtil;

/**
 * 发票遗失和回收
 *
 */
public class BillLostRecycleAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private StockService stockService;
	private LostRecycle lostRecycle=new LostRecycle();
	/**
	 * 查询发票遗失记录列表
	 */
	public String billLostList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			
			String instId=request.getParameter("lostRecycle.instId");
			if(instId!=null&&instId.length()!=0){
				instId=instId.split(" ")[0];
				lostRecycle.setInstId(instId);
			}else{
				User user=this.getCurrentUser();
				lostRecycle.setInstId(user.getOrgId());
			}
			lostRecycle.setFlag("0");
			this.paginationList.setShowCount("false");
			stockService.findLostRecycle(lostRecycle, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillLostRecycleAction-billRecycleList", e);
		}
		return ERROR;
	}
	
	/**
	 * 查询发票回收记录列表
	 */
	public String billRecycleList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String instId=request.getParameter("lostRecycle.instId");
			if(instId!=null&&instId.length()!=0){
				instId=instId.split(" ")[0];
				lostRecycle.setInstId(instId);
			}else{
				User user=this.getCurrentUser();
				lostRecycle.setInstId(user.getOrgId());
			}
			lostRecycle.setFlag("1");
			this.paginationList.setShowCount("false");
			stockService.findLostRecycle(lostRecycle, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillLostRecycleAction-billRecycleList", e);
		}
		return ERROR;
	}
	
	/**
	 * 查询发票作废记录列表
	 */
	public String SelectNoneAction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String instId=request.getParameter("lostRecycle.instId");
			if(instId!=null&&instId.length()!=0){
				instId=instId.split(" ")[0];
				lostRecycle.setInstId(instId);
			}else{
				User user=this.getCurrentUser();
				lostRecycle.setInstId(user.getOrgId());
			}
			lostRecycle.setFlag("2");
			this.paginationList.setShowCount("false");
			stockService.findLostRecycle(lostRecycle, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillLostRecycleAction-billRecycleList", e);
		}
		return ERROR;
	}
	
	/**
	 * 查询发票作废记录
	 */
	public String SelectAction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String instId=request.getParameter("lostRecycle.instId");
			if(instId!=null&&instId.length()!=0){
				instId=instId.split(" ")[0];
				lostRecycle.setInstId(instId);
			}else{
				User user=this.getCurrentUser();
				lostRecycle.setInstId(user.getOrgId());
			}
			lostRecycle.setFlag("2");
			this.paginationList.setShowCount("false");
			stockService.findLostRecycle(lostRecycle, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillLostRecycleAction-billRecycleList", e);
		}
		return ERROR;
	}
	
	/**
	 * 作废申请撤销
	 * @return
	 */
	public void zfcannot(){
		try {
			String id =request.getParameter("Id");
			lostRecycle.setId(id);
			lostRecycle.setState("3");
			List<LostRecycle> lostRecycleList=new ArrayList<LostRecycle>();
			lostRecycleList.add(lostRecycle);
			stockService.updatetLostRecycle(lostRecycleList);
			returnResult(new AjaxReturn(true));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillLostRecycleAction-zfcannot", e);
		}
	}
	
	/**
	 *  票据添加
	 * @return
	 */
	public String lostRecycleAdd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			// 添加机构权限
			User currentUser = this.getCurrentUser();
			request.setAttribute("instId", currentUser.getOrgId());
			request.setAttribute("instName", currentUser.getOrgName());
			request.setAttribute("name", currentUser.getUsername());
			request.setAttribute("id", currentUser.getId());
			
			lostRecycle.setBillType(request.getParameter("billType"));
			lostRecycle.setBillId(request.getParameter("billid"));
			lostRecycle.setBillStartNo(request.getParameter("billStartNo"));
			lostRecycle.setBillEndNo(request.getParameter("billEndNo"));
			lostRecycle.setDisId(request.getParameter("disId"));
			request.setAttribute("lostRecycle", lostRecycle);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			log.error("BillLostRecycleAction-lostRecycleAdd", e);
		}
		return SUCCESS;
	}
	/**
	 *  票据遗失 回收 保存
	 * @return
	 */
 	public String savelostRecycle(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		lostRecycle.setOperateDate(new Date());
		lostRecycle.setState("0");
		lostRecycle.setBillId(request.getParameter("billId"));
		lostRecycle.setBillStartNo(request.getParameter("billStarNo"));
		lostRecycle.setBillEndNo(request.getParameter("billEndNo"));
		lostRecycle.setBillType(DataUtil.getFapiaoTypeCode(request.getParameter("billType")));
		lostRecycle.setCount(Integer.valueOf(request.getParameter("count")));
		lostRecycle.setInstId(request.getParameter("instId").split(" ")[0]);
		lostRecycle.setInstName(request.getParameter("instId").split(" ")[2]);
		lostRecycle.setKpyId(request.getParameter("kpyId"));
		lostRecycle.setKpyName(request.getParameter("kpyName"));
		lostRecycle.setFlag(request.getParameter("flag"));
		lostRecycle.setDisId(request.getParameter("disid"));
		lostRecycle.setDatastatus(request.getParameter("datastatus"));
		lostRecycle.setRemark(request.getParameter("mark"));
		List<LostRecycle> lostRecycleList=new ArrayList<LostRecycle>();
		lostRecycleList.add(lostRecycle);
		stockService.insertLostRecycle(lostRecycleList);
		return SUCCESS;
	} 
	
	public String billTrackUploaAaction(){
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
 		try {
 			mRequest.setCharacterEncoding("UTF-8");
 		} catch (UnsupportedEncodingException e1) {
 			e1.printStackTrace();
 		}
 		String fileName =mRequest.getParameter("fileName");
 		File[] files = mRequest.getFiles("theFile");
 		String [] flNames=mRequest.getFileNames("theFile");
 		if (files != null && files.length > 0) {
			try {
				ImageUtil.info(fileName, files, flNames,true);
				String errorMessage="";
				AjaxReturn coreMessage = new AjaxReturn(true, errorMessage);
			} catch (Exception e) {
				log.error(e);
				this.setResultMessages("上传文件失败:" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			this.setResultMessages("上传文件失败!");
		}
 		return SUCCESS;
	}

	/**
	 *  票据 填写信息验证
	 * @return
	 */
	public String checkjy(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String result="Y";//
		String billType=request.getParameter("billType");
		String billId=request.getParameter("billId");
		int billStarNo=Integer.valueOf(request.getParameter("billStarNo"));
		int billEndNo=Integer.valueOf(request.getParameter("billEndNo"));
		List<Integer> listadd=new ArrayList<Integer>();
		for(int i=billStarNo;i<=billEndNo ;i++){
			listadd.add(i);
		}
		
		lostRecycle.setBillType(DataUtil.getFapiaoTypeCode(billType));
		lostRecycle.setBillId(billId);
		List<LostRecycle> list=stockService.findLostRecycle(lostRecycle);
		List<String> commonList = new ArrayList<String>();// 保存公共的集合
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size(); i++){
				lostRecycle=list.get(i);
 				int startno=Integer.valueOf(lostRecycle.getBillStartNo());
 				int endno=Integer.valueOf(lostRecycle.getBillEndNo());
 				for(int j=startno;j<=endno;j++){
 					if(listadd.contains(j)){
 						commonList.add(j+"");
 					}
 				}
			}
		}
		if(commonList.size()>0){
			result="N";
		}
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *  遗失 回收 导出
	 * @return
	 */
	public String createExecllostRecycle(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			String massage=request.getParameter("massage");
			StringBuffer fileName=null;
			List<LostRecycle> LostRecyclelist=null;
			if(lostRecycle.getInstId()==null&&lostRecycle.getInstId().length()!=0){
				User user=this.getCurrentUser();
				lostRecycle.setInstId(user.getOrgId());
			}
			if("lost".equals(massage)){
				lostRecycle.setFlag("0");
				LostRecyclelist=stockService.findLostRecycle(lostRecycle);
				fileName=new StringBuffer("空白发票遗失统计表");
			}else if("recycle".equals(massage)){
				lostRecycle.setFlag("1");
				LostRecyclelist=stockService.findLostRecycle(lostRecycle);
				fileName=new StringBuffer("空白发票回收统计表");
			}else if ("ZF".equals(massage)) {
				lostRecycle.setFlag("2");
				LostRecyclelist=stockService.findLostRecycle(lostRecycle);
				fileName=new StringBuffer("空白发票作废统计表");
			}else if ("ZFMC".equals(massage)) {
				lostRecycle.setFlag("2");
				LostRecyclelist=stockService.findLostRecycle(lostRecycle);
				fileName=new StringBuffer("空白发票作记录计表");
			}
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			if("lost".equals(massage)||"recycle".equals(massage)||"ZF".equals(massage)){
				String[] titles={"编号","发票类型0-专票 1-普票","机构代码","机构名称","录入时间","发票代码","发票起始号码","发票截止号码","统计张数","标记0-遗失 1-回收 2-作废 ","状态确认 0-未确认 1-已确认 3-撤销"};
				String[] fields={"id","billType","instId","instName","operateDate","billId","billStartNo","billEndNo","count","flag","state"};
				ExcelUtil.exportExcel(fileName.toString(), LostRecycle.class, fields, titles, LostRecyclelist, os);
			}else{
				String[] titles={"编号","发票类型0-专票 1-普票","机构代码","机构名称","录入时间","发票代码","发票起始号码","发票截止号码","统计张数","标记 2-作废","状态确认 0-已申请 1-申请成功 3-申请拒绝","作废原因"};
				String[] fields={"id","billType","instId","instName","operateDate","billId","billStartNo","billEndNo","count","flag","state","remark"};
				ExcelUtil.exportExcel(fileName.toString(), LostRecycle.class, fields, titles, LostRecyclelist, os);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("BillLostRecycleAction-createExecllostRecycle", e);
		}
		return null;
	}
	
	/**
	 *  信息确认操作
	 * @return
	 */
/*	public String updatestar(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String id=request.getParameter("Id");
		boolean reslut=true;
		lostRecycle.setState("1");
		lostRecycle.setId(id);
		List<LostRecycle> lostRecycleList=new ArrayList<LostRecycle>();
		lostRecycleList.add(lostRecycle);
		// 确认操作
		stockService.updatetLostRecycle(lostRecycleList);
		List<LostRecycle> list=stockService.findLostRecycle(lostRecycle);
		BillDistribution billDistribution=new BillDistribution();
		if(list!=null&&list.size()>0){
			LostRecycle lRecycle=list.get(0);
			// 得到税控盘数据信息
			billDistribution.setDisId(lRecycle.getDisId());
			billDistribution.setBillId(lRecycle.getBillId());
			billDistribution.setBillType(lRecycle.getBillType());
			List<BillDistribution> listbilldis=stockService.findDistribution(billDistribution);
			if(listbilldis!=null&&listbilldis.size()>0){
				billDistribution=listbilldis.get(0);
				// 的到 当前信息下的 起始-截止号码
				String billstart=billDistribution.getBillStartNo();
				String billend=billDistribution.getBillEndNo();
				// 操作信息 终止-起始号一致
				if(Integer.valueOf(lRecycle.getBillStartNo()).equals(Integer.valueOf(lRecycle.getBillEndNo()))){
					if(Integer.valueOf(lRecycle.getBillStartNo()).equals(Integer.valueOf(billstart))){
						String billStarNo=String.valueOf(Integer.valueOf(lRecycle.getBillStartNo())+1);
						String billEndNo=null;
						Integer FFcount=Integer.valueOf(billend)-(Integer.valueOf(lRecycle.getBillStartNo())+1)+1;
						Integer sycount=FFcount;
						this.update(lRecycle.getDisId(), billStarNo, billEndNo, FFcount, sycount);
					}
					if (Integer.valueOf(lRecycle.getBillStartNo()).equals(Integer.valueOf(billend))) {
						String billStarNo=null;
						String billEndNo=String.valueOf(Integer.valueOf(lRecycle.getBillStartNo())-1);
						Integer FFcount=Integer.valueOf(lRecycle.getBillStartNo())-Integer.valueOf(billstart);
						Integer sycount=FFcount;
						this.update(lRecycle.getDisId(), billStarNo, billEndNo, FFcount, sycount);
					}
					if((int)Integer.valueOf(billstart)<(int)Integer.valueOf(lRecycle.getBillStartNo())&&
							(int)Integer.valueOf(lRecycle.getBillStartNo())<(int)Integer.valueOf(billend)){
						BillDistribution billDistribution2=new BillDistribution();
						billDistribution2.setBillEndNo(String.valueOf(Integer.valueOf(lRecycle.getBillStartNo())-1));
						billDistribution2.setDisId(lRecycle.getDisId());
						billDistribution2.setFfCount((Integer.valueOf(lRecycle.getBillStartNo())-1)-(Integer.valueOf(billstart))+1);
						billDistribution2.setSyCount((Integer.valueOf(lRecycle.getBillStartNo())-1)-(Integer.valueOf(billstart))+1);
						List<BillDistribution> billDistributionList=new ArrayList<BillDistribution>();
						billDistributionList.add(billDistribution2);
						stockService.updateBillDistribution(billDistributionList);
						billDistribution.setBillStartNo(String.valueOf(Integer.valueOf(lRecycle.getBillEndNo())+1));
						billDistribution.setFfCount((Integer.valueOf(billend))-(Integer.valueOf(lRecycle.getBillEndNo())+1)+1);
						billDistribution.setSyCount((Integer.valueOf(billend))-(Integer.valueOf(lRecycle.getBillEndNo())+1)+1);
						billDistributionList.clear();
						billDistributionList.add(billDistribution);
						stockService.insertBillDistribution(billDistributionList);
					}
				}else if((int)Integer.valueOf(lRecycle.getBillStartNo())<(int)Integer.valueOf(lRecycle.getBillEndNo())){
					if((int)Integer.valueOf(billstart)<(int)Integer.valueOf(lRecycle.getBillStartNo())&&
							(int)Integer.valueOf(lRecycle.getBillEndNo())<(int)Integer.valueOf(billend)){
						BillDistribution billDistribution2=new BillDistribution();
						billDistribution2.setBillEndNo(String.valueOf(Integer.valueOf(lRecycle.getBillStartNo())-1));
						billDistribution2.setDisId(lRecycle.getDisId());
						billDistribution2.setFfCount(Integer.valueOf(lRecycle.getBillStartNo())-Integer.valueOf(billstart));
						billDistribution2.setSyCount(Integer.valueOf(lRecycle.getBillStartNo())-Integer.valueOf(billstart));
						List<BillDistribution> billDistributionList=new ArrayList<BillDistribution>();
						billDistributionList.add(billDistribution2);
						stockService.updateBillDistribution(billDistributionList);
						billDistribution.setBillStartNo(String.valueOf(Integer.valueOf(lRecycle.getBillEndNo())+1));
						billDistribution.setFfCount(Integer.valueOf(billend)-Integer.valueOf(lRecycle.getBillEndNo()+1)+1);
						billDistribution.setSyCount(Integer.valueOf(billend)-Integer.valueOf(lRecycle.getBillEndNo()+1)+1);
						billDistributionList.clear();
						billDistributionList.add(billDistribution);
						stockService.insertBillDistribution(billDistributionList);
					}else if(Integer.valueOf(lRecycle.getBillStartNo()).equals(Integer.valueOf(billstart))&&
							(int)Integer.valueOf(lRecycle.getBillEndNo())<(int)Integer.valueOf(billend)){
						String billStarNo=String.valueOf(Integer.valueOf(lRecycle.getBillEndNo())+1);
						String billEndNo=billend;
						Integer FFcount=Integer.valueOf(billEndNo)-Integer.valueOf(billStarNo)+1;
						Integer sycount=FFcount;
						this.update(lRecycle.getDisId(), billStarNo, billEndNo, FFcount, sycount);
					}else if(Integer.valueOf(lRecycle.getBillEndNo()).equals(Integer.valueOf(billend))&&
							(int)Integer.valueOf(billstart)<(int)Integer.valueOf(lRecycle.getBillStartNo())){
						String billStarNo=billstart;
						String billEndNo=String.valueOf(Integer.valueOf(lRecycle.getBillStartNo())-1);
						Integer FFcount=Integer.valueOf(billEndNo)-Integer.valueOf(billStarNo)+1;
						Integer sycount=FFcount;
						this.update(lRecycle.getDisId(), billStarNo, billEndNo, FFcount, sycount);
					}else if(Integer.valueOf(lRecycle.getBillStartNo()).equals(Integer.valueOf(billstart))&&
							Integer.valueOf(lRecycle.getBillEndNo()).equals(Integer.valueOf(billend))){
						stockService.updateBillDistribution(lRecycle.getDisId());
					}
				}else{
					reslut=false;
				}
			}
			//回收 重新入库
			if ("recycle".equals(request.getParameter("massage"))) {
				BillInventory billInventory=new BillInventory();
				List<BillInventory> billInventoryList=new ArrayList<BillInventory>();
				save(lRecycle,billInventory,billInventoryList);	
			}
		}
		try {
			returnResult(new AjaxReturn(reslut));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	public String updatestar(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		String id=request.getParameter("Id");
		boolean reslut=true;
		lostRecycle.setState("1");
		lostRecycle.setId(id.split("-")[0]);
		List<LostRecycle> lostRecycleList=new ArrayList<LostRecycle>();
		lostRecycleList.add(lostRecycle);
		// 确认操作
		stockService.updatetLostRecycle(lostRecycleList);
		// 得到税控盘数据信息
		BillDistribution billDistribution=new BillDistribution();
		billDistribution.setDisId(id.split("-")[1]);
		List<BillDistribution> listbilldis=stockService.findDistribution(billDistribution);
		List<BillDistribution> billDistributionList=new ArrayList<BillDistribution>();
		if(listbilldis!=null&&listbilldis.size()>0){
			billDistribution=listbilldis.get(0);
			if("ys".equals(id.split("-")[3])){
				billDistribution.setSyfpYsCount(billDistribution.getSyfpYsCount()+Integer.parseInt(id.split("-")[2]));
			}else if("hs".equals(id.split("-")[3])){
				billDistribution.setSyfpHsCount(billDistribution.getSyfpHsCount()+Integer.parseInt(id.split("-")[2]));
				// 回炉再造
				List<LostRecycle> lostRecycles=stockService.findLostRecycle(lostRecycle);
				LostRecycle lRecycle=lostRecycles.get(0);
				BillInventory billInventory=new BillInventory();
				billInventory.setBillId(lRecycle.getBillId());
				billInventory.setBillStartNo(lRecycle.getBillStartNo());
				billInventory.setBillEndNo(lRecycle.getBillEndNo());
				billInventory.setBillType(lRecycle.getBillType());
				billInventory.setPutInDate(new Date());
				List<BillInventory> billInventoryList=new ArrayList<BillInventory>();
				billInventoryList.add(billInventory);
				stockService.insertBillInventory(billInventoryList);
			}else if("zf".equals(id.split("-")[3])){
				billDistribution.setSyfpZfCount(billDistribution.getSyfpZfCount()+Integer.parseInt(id.split("-")[2]));
			}
			billDistribution.setSyCount(billDistribution.getSyCount()-Integer.parseInt(id.split("-")[2]));
		}
		billDistributionList.add(billDistribution);
		stockService.updateBillDistribution(billDistributionList);
		return null;
	}
	
	public void save(LostRecycle lRecycle, BillInventory billInventory,List<BillInventory> billInventoryList){
		billInventory.setBillStartNo(lRecycle.getBillStartNo());
		billInventory.setBillEndNo(lRecycle.getBillEndNo());
		billInventory.setCount(Integer.valueOf(lRecycle.getBillEndNo())-Integer.valueOf(lRecycle.getBillStartNo())+1);
		billInventory.setSyCount(String.valueOf(Integer.valueOf(lRecycle.getBillEndNo())-Integer.valueOf(lRecycle.getBillStartNo())+1));
		billInventory.setBillId(lRecycle.getBillId());
		billInventory.setBillType(lRecycle.getBillType());
		billInventory.setInstId(lRecycle.getInstId());
		billInventory.setInstName(lRecycle.getInstName());
		billInventory.setPutInDate(new Date());
		billInventoryList.add(billInventory);
		stockService.insertBillInventory(billInventoryList);
	}
	private void update(String disid,String billStarNo,String billEndNo,Integer FFcount,Integer sycount){
		BillDistribution billDistribution2=new BillDistribution();
		if(billStarNo!=null){
			billDistribution2.setBillStartNo(billStarNo);
		}
		if(billEndNo!=null){
			billDistribution2.setBillEndNo(billEndNo);
		}
		billDistribution2.setDisId(disid);
		billDistribution2.setFfCount(FFcount);
		billDistribution2.setSyCount(sycount);
		List<BillDistribution> billDistributionList=new ArrayList<BillDistribution>();
		billDistributionList.add(billDistribution2);
		stockService.updateBillDistribution(billDistributionList);
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

	public LostRecycle getLostRecycle() {
		return lostRecycle;
	}

	public void setLostRecycle(LostRecycle lostRecycle) {
		this.lostRecycle = lostRecycle;
	}
	
	private static String fleName ;
	public String viewImgFromLostRecycle(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		// id - start - end 
		String fileName = request.getParameter("fileName");
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
	public void viewImgFromLostRecycleJAjax(){
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
