package com.cjit.vms.trans.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.vms.metlife.model.TtmPrcnoMatch;
import com.cjit.vms.taxdisk.servlet.model.parseXml.Bill;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.webService.client.entity.BillEntity;
import com.cjit.webService.client.entity.HeXinCallBack;
import com.cjit.ws.jdkClient.VatServiceClient;
import com.sinosoft.lis.TaxInvoiceWebService.GClient;
import com.sinosoft.ws.job.client.callservice.Client;

public class BillIssueServiceImpl extends GenericServiceImpl implements
		BillIssueService {
	public List findBillInfoList(BillInfo billInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = billInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			billInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		map.put("billInfo", billInfo);
		return find("findBillTrack", map, paginationList);
	}

	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		String dataStatus = billInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			billInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		map.put("billInfo", billInfo);

		return find("findBillTrack", map);
	}

	public BillInfo findBillInfoById(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		//List list = find("findBillInfo", map);
		//2018-03-07国富更改
		List list = find("findBillInfo1", map);
		if ((list != null) && (list.size() == 1)) {
			return ((BillInfo) list.get(0));
		}
		return null;
	}

	public void updatebillInfoIssueResult(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		update("updatebillInfoIssueResult", map);
	}

	public void updateBillInfoStatus(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billId", billInfo.getBillId());
		map.put("dataStatus", billInfo.getDataStatus());

		update("updateBillDataStatus", map);
	}

	public void deleteBillInfoById(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteBillInfo", map);
	}

	public List findBillItemByBillId(String billId) {
		Map map = new HashMap();
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillId(billId);
		map.put("billItem", billItem);

		return find("findBillItemInfo", map);
	}

	public void deleteBillItemInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteBillItemInfo", map);
	}

	public List findTransByBillId(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransByBillId", map);
	}

	public List findTransInfo(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		//新增  增加查询字段  程2018/08/23
		return find("findTransInfoDiskById", map);
	//	return find("findTransInfoById", map);
	}
	
	public void updateTransTemp(TransInfo transTemp){
		Map map = new HashMap();
		map.put("transTemp", transTemp);
		update("updateTransTemp", map);
	}

	public List findTransByBillId(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransByBillId", map, paginationList);
	}

	//2018-05-02新增
	public List findBillByBillId(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findBillByBillId", map, paginationList);
	}
	
	public void deleteTransBillInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteTransBillInfo", map);
	}

	public void updateTransInfoStatus(String dataStatus, String billId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("billId", billId);

		update("updateTransInfoStatus", map);
	}

	public List findInvalidPaperInvoice(String dataStatus, String fapiaoType) {
		Map map = new HashMap();
		PaperInvoiceUseDetail paperInvoiceUseDetail = new PaperInvoiceUseDetail();
		paperInvoiceUseDetail.setInvoiceStatus(dataStatus);

		map.put("paperInvoiceUseDetail", paperInvoiceUseDetail);
		return find("findInvalidPaperInvoice", map);
	}

	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType,
			String instId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("fapiaoType", fapiaoType);
		map.put("instId", instId);

		return getRowCount("findInvalidEmptyPaperInvoiceCount", map);
	}

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice) {
		Map map = new HashMap();
		map.put("paperInvoiceUseDetail", invalidInvoice);
		update("updatePaperInvoiceStatus", map);
	}

	public String findRegisteredInfo(String taxDiskNo) {
		Map map = new HashMap();
		DiskRegInfo diskRegInfo = new DiskRegInfo();
		diskRegInfo.setTaxDiskNo(taxDiskNo);
		map.put("diskRegInfo", diskRegInfo);
		List list = find("findRegisteredInfo", map);
		if ((list != null) && (list.size() == 1)) {
			diskRegInfo = (DiskRegInfo) list.get(0);
			return diskRegInfo.getRegisteredInfo();
		}
		return null;
	}

	public TaxDiskInfo findTaxDiskInfoByTaxDiskNo(String taxDiskNo) {
		Map map = new HashMap();
		TaxDiskInfo taxDiskInfo = new TaxDiskInfo();
		taxDiskInfo.setTaxDiskNo(taxDiskNo);
		map.put("taxDiskInfo", taxDiskInfo);
		List list = find("findTaxDiskInfoByTaxDiskNo", map);
		if ((list != null) && (list.size() == 1)) {
			return ((TaxDiskInfo) list.get(0));
		}
		return null;
	}

	public PaperInvoiceUseDetail getPaperInvoiceUseDetail(String invoiceStatus,
			String invoiceCode, String invoiceNo) {
		Map map = new HashMap();
		map.put("dataStatus", invoiceStatus);
		map.put("invoiceCode", invoiceCode);
		map.put("invoiceNo", invoiceNo);
		List list = find("getpaperInvoiceUseDetailBycode", map);
		PaperInvoiceUseDetail paperInvoiceUseDetail = null;
		if ((list.size() != 0) && (list.size() == 1)) {
			paperInvoiceUseDetail = (PaperInvoiceUseDetail) list.get(0);
		}
		return paperInvoiceUseDetail;
	}

	public void saveMatchInfoList(List<TtmPrcnoMatch> matchSaveList) {
		insertBatch("saveMatchInfoList", matchSaveList);
	}

	public List findMatchInfoList(List matchFindList, boolean flag) {
		Map map = new HashMap();
		map.put("matchList", matchFindList);
		if (flag) {
			map.put("datastatus", "'3','4','7'");
		}
		return find("findMatchInfoList", map);
	}

	public void updateMatchInfoList(List<TtmPrcnoMatch> matchUpdateList) {
		updateRptDataBatch("updateMatchInfoList", matchUpdateList);
	}

	public List findAllMatchInfoList(TtmPrcnoMatch match) {
		Map map = new HashMap();
		map.put("match", match);
		return find("findAllMatchInfoList", map);
	}

	public String invoiceIssueAccessCore(LogManagerService logManagerService,
			HttpServletRequest request, User user, List transList,
			BillInfo billInfo,Log log) {
		try {
			String message = "";
//			InputStream is = super.getClass().getResourceAsStream("/config/config.properties");
//			Properties prop = new Properties();
//			prop.load(is);
//
//			String interfaceParam = "";
//			log.debug("aa"+billInfo.getDsouRce());
//			log.debug("hahahha::"+(String) prop.get("LIS.interface.fapiaoInfo"));
//			interfaceParam = (String) prop.get("LSP.interface");
//			String[] interfaceParams = interfaceParam.split("\\|");

			if (transList.size() == 0) {
				message = "没有查到交易信息";
				return "1|" + message;
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			DecimalFormat decimalFormat = new DecimalFormat("000");
			String serialno = dateFormat.format(new Date())
					+ decimalFormat.format(new Random().nextInt(1000));

			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("GBK");
			Element Package=document.addElement("Package");
			Element Header=Package.addElement("Header");
			Header.addElement("RequestType").addText("InvoiceBack");
			Header.addElement("UUID").addText("c0d258b4-eb69-427a-b8a9-9cb8ea0de86a");
			Date tDate=new Date(); 
			//System.out.println("tDate:"+tDate);
			SimpleDateFormat tSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2016-06-08 14:36:36
			String currentDate=tSimpleDateFormat.format(tDate);
			//Header.addElement("SendTime").addText("2016-06-08 14:36:36");//add
			Header.addElement("SendTime").addText(currentDate);//modify by LQ 20160704
			Element Request = Package.addElement("Request");
			
			String instname = null;//团个标识  
			Element billListDoc = Request.addElement("BUSSLIST");
		//	for (Iterator it = transList.iterator(); it.hasNext();) {
				TransInfo trans = (TransInfo) transList.get(0);
				
				Element billDoc = billListDoc.addElement("BUSSINFO");
				instname=trans.getInstname();
				
				
				
				
				if("I".equals(instname)){//个险
					List<TransInfo> transInfoList = findTransInfoToCore(billInfo.getBillId());
					if(transInfoList.size()==1){
						billDoc.addElement("BUSINESS_ID").addText(trans.getReverseTransId());
						if(trans.getTransBusId()==null){
							billDoc.addElement("REPNUM").addText("");//团险可为空
						}else{
							billDoc.addElement("REPNUM").addText(trans.getTransBusId());
						}
					}else{
						String bussinessid = transInfoList.get(0).getReverseTransId();
						String repNum = transInfoList.get(0).getTransBusId();
						for(int i = 1;i<transInfoList.size();i++){
							bussinessid=bussinessid+"|"+transInfoList.get(i).getReverseTransId();
							repNum=repNum+"|"+transInfoList.get(i).getTransBusId();
						}
						billDoc.addElement("BUSINESS_ID").addText(bussinessid);
						if(trans.getTransBusId()==null){
							billDoc.addElement("REPNUM").addText("");//团险可为空
						}else{
							billDoc.addElement("REPNUM").addText(repNum);
						}
						
						
					}
					
				//判断完个险之后判断团险 防止放两次	
				}else {
					billDoc.addElement("BUSINESS_ID").addText(trans.getReverseTransId());
					
					if(trans.getTransBusId()==null){
						billDoc.addElement("REPNUM").addText("");//团险可为空
					}else{
						billDoc.addElement("REPNUM").addText(trans.getTransBusId());//reverseTransId
					}
					
				}
				
				billDoc.addElement("TAX_STATUS").addText("1");//
				billDoc.addElement("BILL_CODE").addText(billInfo.getBillCode());
				billDoc.addElement("BILL_NO").addText(billInfo.getBillNo());
				billDoc.addElement("FAPIAO_TYPE").addText(billInfo.getFapiaoType());
				billDoc.addElement("AMT_CNY").addText(billInfo.getSumAmt().toString());//总数
				billDoc.addElement("TAX_AMT_CNY").addText(billInfo.getTaxAmtSum().toString());//税额
				billDoc.addElement("INCOME_CNY").addText(billInfo.getAmtSum().toString());//不含税的收入billInfo.getAmtSum().toString()
				String printDate="";
				if(!"".equals(billInfo.getBillDate())&&billInfo.getBillDate()!=null){
					if(billInfo.getBillDate().length()>10){
						printDate=billInfo.getBillDate().substring(0, 10);
					}else{
						printDate=billInfo.getBillDate();
					}
				}else{
					printDate=currentDate.substring(0,10);
				}
				//billDoc.addElement("PRINTDATE").addText(billInfo.getBillDate());//add
				billDoc.addElement("PRINTDATE").addText(printDate);
				billDoc.addElement("PRINTOPERATER").addText(billInfo.getDrawer());
				billDoc.addElement("ERRORDESC").addText("");
				
			//}

			String requestXml = document.asXML();
			logManagerService.writeLog(request, user, serialno, "发票开具回写核心接口", "请求报文", requestXml, "1");
			if(XmlUtil.validateXML(requestXml)){
				System.out.println("发票开具回写核心接口:"+requestXml);
				log.debug("发票开具回写核心接口:"+requestXml);

				String responseXml = "";
				VatServiceClient vatServiceClient = new VatServiceClient();
				Client iClient=new Client();
				GClient gClient=new GClient();   
				String resss=instname;
				if("I".equals(instname)){//个险
					responseXml=iClient.invoke(requestXml);
				}else if("G".equals(instname)){//团险
					responseXml=gClient.invoke(requestXml);
				}
				logManagerService.writeLog(request, user, serialno, "发票开具响应核心接口", "响应报文", responseXml, "1");
				log.info("核心返回："+responseXml);
				log.debug("核心返回："+responseXml);
				System.out.println("核心返回："+responseXml);
				Document rootDocument = DocumentHelper.parseText(responseXml);//解析返回报文的问题，可能解析失败，节点不对应
				Node responseNode = rootDocument.selectSingleNode("Package");

				Node resultNode = responseNode.selectSingleNode("Header");
				String resultType = resultNode.selectSingleNode("ResponseCode").getStringValue();
				if ("N".equals(resultType)) {
					Node ErrorList = resultNode.selectSingleNode("ErrorList");
					Node ERROR = ErrorList.selectSingleNode("ERROR");
					String ERROR_ID = ERROR.selectSingleNode("ERROR_ID").getStringValue();
					message = ERROR.selectSingleNode("ERRMSG").getStringValue();
					return ERROR_ID+" | " + message;
				}
			}else{
				return "1|回写核心接口请求报文格式不正确";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillIssueServiceImpl-invoiceIssueAccessCore", e);
		}
		return "0|成功";
	}
	
	/**
	 * 李松加  
	 * @param billId
	 * @return
	 */
	public List findTransInfoToCore(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		return find("findTransInfoToCore", map);
	}

	@Override
	public List<String> findWebServiceUrl(String serviceName) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", serviceName);
		return this.find("findWebserviceUrl", map);
	}

	@Override
	public void insertFailInfo(List<BillEntity> list) {
		this.insertBatch("insertFailInfo", list);
	}
	@Override
	public void updatePaperInvoiceListInfo(BillInfo billInfo) {
		String billCode=billInfo.getBillCode();
		String billNo=billInfo.getBillNo();
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		List list=find("findPaperInvoiceListInfo", map);
		if(list!=null){
			PaperInvoiceListInfo paperInvoiceListInfo=(PaperInvoiceListInfo) list.get(0);
			//的到张数  已使用 张数
			int invoiceNum=paperInvoiceListInfo.getInvoiceNum()-1;
			int userdNum=paperInvoiceListInfo.getUserdNum()+1;
			map.put("INVOICE_NUM", invoiceNum);
			map.put("USERD_NUM", userdNum);
			//更新总张数   使用张数情况
			update("updatePaperInvoiceListInfoInvoiceNum", map);
		}
	}

	@Override
	public boolean isAllIssure(String billId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("billId", billId);
		String count=this.find("isAllIssure", map).get(0).toString();
		int sum=new Integer(count);
		if (sum>0) {
			return false;
		}
		return true;
	}
}