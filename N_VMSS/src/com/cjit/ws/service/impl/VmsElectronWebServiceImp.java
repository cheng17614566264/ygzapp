package com.cjit.ws.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.management.RuntimeErrorException;
import javax.xml.rpc.ServiceException;

import org.apache.axis2.AxisFault;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tools.ant.filters.StringInputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.type.DbTimestampType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import sun.misc.BASE64Decoder;






import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxdisk.ciitc.service.EInvoice;
import com.cjit.vms.taxdisk.servlet.model.ElectronicsIssue;
import com.cjit.vms.taxdisk.servlet.model.Product;
import com.cjit.vms.taxdisk.single.model.parseXml.ReportTaxDiskBillQueryReturnXml;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.ws.common.utils.Utils;
import com.cjit.ws.common.utils.XmlUtils;
import com.cjit.ws.dao.VmsCustomerInfoDao;
import com.cjit.ws.dao.VmsTransInfoDao;
import com.cjit.ws.dao.VmsTransTypeDao;
import com.cjit.ws.entity.VmsCustomerInfo;
import com.cjit.ws.entity.VmsTransInfo;
import com.cjit.ws.utils.Axis2Client;
import com.cjit.ws.utils.Axis2Client4CXF;
import com.cjit.ws.utils.Email;
import com.cjit.ws.utils.HttpUtil;
import com.sun.org.apache.regexp.internal.REUtil;

public class VmsElectronWebServiceImp extends GenericServiceImpl{

	private VmsTransTypeDao vmsTransTypeDao;
	private VmsCustomerInfoDao vmsCustomerInfoDao;
	private VmsTransInfoDao vmsTransInfoDao;
	private BillValidationService billValidationServices;
	private CreateBillService createBillServiceManual;
	private TransactionTemplate transactionTemplate;
	

	
	private BillTrackService billTrackService;
	private RedReceiptApplyInfoService redReceiptApplyInfoService;
	private ParamConfigVmssService paramConfigVmssService;
	private BillIssueService billIssueService;
	private BillInfoService billInfoService;
	
	private BillIssueDiskAssitService billIssueDiskAssitService;
	
	

	
	private TransInfoService transInfoService;
	private static String timeStamp = "";
	// 用于生成ID的属性
	private static int busFlag = 1;
	
	
	
	public BillIssueDiskAssitService getBillIssueDiskAssitService() {
		return billIssueDiskAssitService;
	}

	public void setBillIssueDiskAssitService(
			BillIssueDiskAssitService billIssueDiskAssitService) {
		this.billIssueDiskAssitService = billIssueDiskAssitService;
	}

	
	
	public BillInfoService getBillInfoService() {
		return billInfoService;
	}

	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public VmsTransTypeDao getVmsTransTypeDao() {
		return vmsTransTypeDao;
	}

	public void setVmsTransTypeDao(VmsTransTypeDao vmsTransTypeDao) {
		this.vmsTransTypeDao = vmsTransTypeDao;
	}

	public VmsCustomerInfoDao getVmsCustomerInfoDao() {
		return vmsCustomerInfoDao;
	}

	public void setVmsCustomerInfoDao(VmsCustomerInfoDao vmsCustomerInfoDao) {
		this.vmsCustomerInfoDao = vmsCustomerInfoDao;
	}

	public VmsTransInfoDao getVmsTransInfoDao() {
		return vmsTransInfoDao;
	}

	public void setVmsTransInfoDao(VmsTransInfoDao vmsTransInfoDao) {
		this.vmsTransInfoDao = vmsTransInfoDao;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public BillValidationService getBillValidationServices() {
		return billValidationServices;
	}

	public void setBillValidationServices(BillValidationService billValidationServices) {
		this.billValidationServices = billValidationServices;
	}

	public CreateBillService getCreateBillServiceManual() {
		return createBillServiceManual;
	}

	public void setCreateBillServiceManual(CreateBillService createBillServiceManual) {
		this.createBillServiceManual = createBillServiceManual;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public RedReceiptApplyInfoService getRedReceiptApplyInfoService() {
		return redReceiptApplyInfoService;
	}

	public void setRedReceiptApplyInfoService(RedReceiptApplyInfoService redReceiptApplyInfoService) {
		this.redReceiptApplyInfoService = redReceiptApplyInfoService;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}


	/**
	 * 开具需要的类
	 * @param xml
	 * @return
	 */
	private int scale = 10;
	/***
	 * 开票查询移植-------------------------
	 */
	protected com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
	protected List transInfoList;
	// 客户纳税人类别列表
	protected List custTaxPayerTypeList = new ArrayList();
	// 交易状态列表
	protected List transDataStatusList = new ArrayList();
	protected String message;

	protected String[] selectTransIds;
	/**
	 * 
	 * @param xml
	 * @return
	 * 
	 * transactionTemplate 创建一个事物管理器
	 */

	/*public String transService(final String xml){*/
	public String transService(final Map xmlmap,final List<TransInfoTemp> batchRunTransInfoINSList) {
		@SuppressWarnings("unchecked")
		/*String result = transactionTemplate.execute(new TransactionCallback() {

			@Override
			public Object doInTransaction(TransactionStatus status) {*/
				Document doc=null;
				String sucessResult=null;
				try {				
					Element HeaderResp=null;
					
					String result=null;//返回的请求结果
					String invtypValue=null;
					String RequestType=null;
				
					String SendTime=null;
					/*final Document document = DocumentHelper.parseText(xml);
					System.out.println("核心请求的报文===================================="+xml);
					Element Package=document.getRootElement();
					Element Header=Package.element("HEADER");			
					Element RequestTypeElement=Header.element("REQUESTTYPE");
					Element UUIDElement=Header.element("UUID");
					String uuid = UUIDElement.getText();
					RequestType = RequestTypeElement.getText();
					doc = DocumentHelper.createDocument();
					doc.setXMLEncoding("UTF-8");
					if("0011".equals(RequestType)){*/
						try {
							/**
							 * 解析报文
							 * */
							
							final Map<BillInfo, String> billMap = paseXml(xmlmap,batchRunTransInfoINSList);
							//交易类型判断，契约交易直接开票，非契约交易信息保存
							for(BillInfo bi : billMap.keySet()){
								//if(bi.getFeeTyp().contains("契约")){
								/***
								 * 组件票据信息，开票
								 */
								//List<BillInfo> billList = transToEachBill(transidList);
								
								/*Element PackageResp = doc.addElement("PACKAGE");
								HeaderResp = PackageResp.addElement("HEADER");
								HeaderResp.addElement("REQUESTTYPE").addText("0011");
								HeaderResp.addElement("UUID").addText(uuid);
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
								Element Response = PackageResp.addElement("REQUEST");
								Element ErrorList=Response.addElement("BUSSLIST");*/
								boolean all_sucess=true;
								/*Element BUSSINFO=ErrorList.addElement("BUSSINFO");*/
								Element BUSSINFO=null;
								BigDecimal amtSum=new BigDecimal("0");
								BigDecimal amtTaxSum=new BigDecimal("0");
								for(BillInfo billInfos : billMap.keySet()){
									amtSum=amtSum.add(billInfos.getAmtSum());
									amtTaxSum=amtTaxSum.add(billInfos.getTaxAmtSum());
									
								}
								List<Map> mailList = new ArrayList<Map>();
								for (BillInfo billInfo : billMap.keySet()) {
									
									try {
											/**
											 * 电子发票开具 
											 */
											/*if(issueBill(billInfo, EInvoice.正票正常开具)){ //第一次请求航信
											// creadBlueXmlBody(BUSSINFO,"0", "保存数据成功",  "", "", billInfo.getAmtSum().toString(), billInfo.getTaxAmtSum().toString(), "");	 
											}*/
										ElectronicsIssue ei = new ElectronicsIssue();
										Map map = new HashMap();
										String billId = billInfo.getBillId();
										List<com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo> list = billIssueDiskAssitService.findBillItemByBillIdDisk(billId);
										List<Product> goodsList = new ArrayList<Product>();
										if (list.size() > 0) {
											for (int i = 0; i < list.size(); i++) {
												com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo billItem = list.get(i);
												Product goods = new Product(billItem);
												goodsList.add(goods);
											}
										}
										//Product product = new Product();
										map.put("billInfo", billInfo);
										map.put("goodsList", goodsList);
										String st = ei.taxWebservice(map);
										Map maps = new HashMap();
										maps = ei.printXML(st);
										System.out.println(maps.get("fapiaoCode")+":"+maps.get("fapiaoNo")+":"+maps.get("PDFURL")+":"+maps.get("result")+":"+billInfo.getCustomerEmail());
										maps.put("email", billInfo.getCustomerEmail());
										maps.put("customerName", billInfo.getCustomerName());
										maps.put("cherNum", billInfo.getCherNum());
										maps.put("addressandphone", billInfo.getAddressandphone());
										maps.put("billId", billInfo.getBillId());
										maps.put("bill_DATASTATUS", "5"); //票据状态：5--已开具
										maps.put("trans_DATASTATUS", "99"); //交易状态：99--已开具
										
										//更新数据库中票据信息，更改交易状态
										vmsTransInfoDao.updateBillMessage(maps);
										vmsTransInfoDao.updateTransState(maps);
										
										mailList.add(maps);
									} catch (HavaErrorMessageException hxe) {
										all_sucess =false;
										/*status.setRollbackOnly();*/
										 hxe.printStackTrace();
												creadBlueXmlBody(BUSSINFO,"1", hxe.getMessage(),  "", "", amtSum.toString(),amtTaxSum.toString(), "");
										break;
									}
									catch (Exception e) {
										all_sucess=false;
									/*status.setRollbackOnly();*/
									e.printStackTrace();
										creadBlueXmlBody(BUSSINFO, "1", "电子发票开票失败", "", "", amtSum.toString(), amtTaxSum.toString(), "");
										break;
									}
								
								}
								/**
								 * 新增
								 * 日期：2018-08-30
								 * 作者：刘俊杰
								 * 功能：自动开票成功后，给客户发送电子邮件
								 */
								
								if(all_sucess) {
									String subject = "国富人寿电子发票";//邮件主题
									for(Map emap: mailList) {
										StringBuffer content = new StringBuffer();
										content.append("<div>尊敬的客户:"+emap.get("customerName")+",您好!<br />");
										content.append("<p style='text-indent:2em;'>您在我公司所购买的保单["+emap.get("cherNum")+"]，其发票已开具成功!<br />");
										content.append("如需下载电子发票, 请点击:"+emap.get("PDFURL")+"开始下载</p><br />");
										content.append("如需其他帮助, 请联系"+emap.get("addressandphone")+"<br /></div>");
										String message = Email.sendEmail(emap.get("email").toString(), subject, content.toString());
										System.out.println(message);
									}
									
								}
								/**
								 * 新增
								 * 日期：2018-08-30
								 * 作者：刘俊杰
								 * 功能：自动开票成功后，保存pdf到本地
								 */
								if(all_sucess) {
									for(Map emap: mailList) {
										String pdfresult = HttpUtil.getURLContent(emap.get("PDFURL").toString());
										System.out.println(pdfresult);
									}
								}
								
								
								
								/*if(all_sucess){
									 creadBlueXmlBody(BUSSINFO,"0", "保存数据成功",  "", "", amtTaxSum.toString(), amtTaxSum.toString(), "");//发票开具成功后就返回信息给微管家	 
								}*/
								/*if(all_sucess){
									for (final BillInfo billInfo : billMap.keySet()) {
										new Thread(new Runnable() {
											
											@Override
											public void run() {
												try {
													createEInvoicePDF(billInfo, false);//新开启一个线程进行二次请求航信，因为发票开具与pdf生成之间间隔较长，防止响应时间过长，生成失败
												} catch (Exception e) {
													e.printStackTrace();
												}
												
											}
										}).start();
										createEInvoicePDF(billInfo, false);//新开启一个线程进行二次请求航信，因为发票开具与pdf生成之间间隔较长，防止响应时间过长，生成失败
									}
								}*/
							//}	
								
						}
					}catch(HavaErrorMessageException HEMe){
						HEMe.printStackTrace();
						/*status.setRollbackOnly();*/
						System.out.println("解析报文出错");
						/*String renturnerrorxml= blueErrorReturnXnl(RequestType, uuid, "", "", "",HEMe.getMessage());*/
						String renturnerrorxml= null;
						return renturnerrorxml;
					}
						
						catch (Exception e) {
							/*status.setRollbackOnly();*/
						e.printStackTrace();
						/*String renturnerrorxml= blueErrorReturnXnl(RequestType, uuid, "", "", "","解析报文出错");*/
						String renturnerrorxml= null;
						//new RuntimeException(renturnerrorxml);
						return renturnerrorxml;
					}
			/*}
			/**
			 * 电票红冲
			 */
			/*else{
				
				try {
					
					*//**
					 * TODO 解析核心红冲报文的请求
					 * list 存放解析后报文流水号，发票代码发票号码组成的字符串
					 *//*
					List<String> list= paseRedXml(document,uuid,RequestType);
						 try {
							 *//**
							  * TODO 创建红票信息
							  * redBillId 解析
							  *//*
							  List<String> redBillId = finBlueBillId(list);
							  doc = DocumentHelper.createDocument();
							  doc.setXMLEncoding("UTF-8");
								Element PackageResp = doc.addElement("PACKAGE");
								HeaderResp = PackageResp.addElement("HEADER");
								HeaderResp.addElement("REQUESTTYPE").addText("0012");
								HeaderResp.addElement("UUID").addText(uuid);
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
								Element Response = PackageResp.addElement("REQUEST");
								Element ErrorList=Response.addElement("BUSSLIST");
								String billNo=list.get(0).split("@")[0];
								String billCode=list.get(0).split("@")[1];
								String businessId =list.get(0).split("@")[2];
								final Map<com.cjit.common.model.BillInfo,String> bills = new HashMap<com.cjit.common.model.BillInfo,String>();
							    for(int i=0;i<redBillId.size();i++){
							   
							    	try {
										//组件返回报文
							    		com.cjit.vms.trans.model.BillInfo billInfo=	creadRedBillInfo(redBillId.get(i));
							    		String temp_businessId=list.get(i).split("@")[2];
							    		bills.put(billInfo, temp_businessId);
									} catch (Exception e) {
										System.out.println("=====================creadRedBillInfo() 创建红票信息的时候出现了异常");
										e.printStackTrace();
										status.setRollbackOnly();
										//组件创建红票发票信息错误报文	
										return redErrorReturnXnl(RequestType, uuid, businessId, billCode, billNo, "创建红票信息的时候出现了异常");
									}
							    }
							    for (com.cjit.common.model.BillInfo billInfo : bills.keySet()) {
							    	try {
							    		issueBill(billInfo, EInvoice.错票重开红票);
							    		
									}catch (HavaErrorMessageException he) {
										
										System.out.println("=====================creadRedBillInfo() 创建红票信息的时候出现了异常");
										he.printStackTrace();
										status.setRollbackOnly();
										return redErrorReturnXnl(RequestType, uuid, businessId, billCode, billNo, he.getMessage());
									
								} catch (Exception e) {
									
									System.out.println("=====================creadRedBillInfo() 创建红票信息的时候出现了异常");
									e.printStackTrace();
									status.setRollbackOnly();
									return redErrorReturnXnl(RequestType, uuid, businessId, billCode, billNo, "电子发票开具失败");
								
									}
							    
							    }  
								Element rePackageResp = doc.addElement("PACKAGE");
								HeaderResp = rePackageResp.addElement("HEADER");
								HeaderResp.addElement("REQUESTTYPE").addText("0011");
								HeaderResp.addElement("UUID").addText(uuid);
								SimpleDateFormat reddf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
								Element redResponse = PackageResp.addElement("REQUEST");
								Element redErrorList=Response.addElement("BUSSLIST");
								boolean all_sucess=true;
								Element BUSSINFO=ErrorList.addElement("BUSSINFO");
								BUSSINFO.addElement("RESULTTYPE").addText("0");
								BUSSINFO.addElement("ERRORINFO").addText("红票信息开具成功");
								BUSSINFO.addElement("BUSINESSID").addText(businessId);
								BUSSINFO.addElement("YBILLCODE").addText(billCode);
								BUSSINFO.addElement("YBILLNO").addText(billNo);
								BUSSINFO.addElement("BILLCODE").addText("");
								BUSSINFO.addElement("BILLNO").addText("");
								BUSSINFO.addElement("AMT").addText("");
								BUSSINFO.addElement("TAX_AMT").addText("");
								
								for (final com.cjit.common.model.BillInfo billInfo : bills.keySet()) {
									new Thread(new Runnable() {
										
										@Override
										public void run() {
											createEInvoicePDF(billInfo, true);
											
										}
									}).start();
								}
								
						} catch (Exception e) {
						status.setRollbackOnly();
						e.printStackTrace();
						return e.getMessage();
						}
				
				} catch (Exception e) {
					System.out.println("解析报文错误");
					e.printStackTrace();
					//组件返回报文
					String redErrorResult=redErrorReturnXnl(RequestType, uuid, "", "", "", e.getMessage());
					return redErrorResult;
				
				}
			
				
			}*/
		
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("========================================1解析报文头错误");
					return e.getMessage();
				}
				
				/***
				 * 返回成功的报文
				 
				return doc.asXML();*/
				return "";
			/*}

		
		});
		System.out.println("========================================================"+result);
		return result;*/
		
	}
	
	
	/**
	 * 
	 * @param billInfo 发票信息
	 * @param qd_flag 团险个险标志：团险为G，个险为I
	 * @param uuid 
	 * @param repnum 
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws HangXinException 与航信对接抛出的异常
	 */
	private void createEInvoicePDF(com.cjit.common.model.BillInfo billInfo, boolean redBillFlag) {
		int maxRequestCount = 3;//最大请求次数
		Map<String,String> returnData = null;
		String fpdm = "";
		String fphm = "";
		String errorMessage = "";
		String PDF_CONTENT = "";
		boolean sucessFlag = false;
		try {
			for (int i = 1; i <= maxRequestCount; i++) {
				Thread.sleep(10000);
				returnData = callHXInterface_downloadEInvoiceInfo(billInfo);//掉航信接口
				if((i < maxRequestCount) && (returnData.get("msg").contains("发票正在排队开票")||returnData.get("msg").contains("此流水号的发票正在开具"))){
					//出现这种情况，重新请求
				}else{
					break;
				}
			}
			if("0000".equals(returnData.get("msgCode"))){//成功
				savePDF(returnData);
				fpdm = returnData.get("fpdm");
				fphm = returnData.get("fphm");
				PDF_CONTENT = fpdm + "_" + fphm + ".pdf";
				sucessFlag = true;
				//Date billdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(returnData.get("kprq"));
				System.out.println("date.............................................................................."+returnData.get("kprq"));
				/*billIssueDiskAssitService.updateBillIssueResult(returnData.get("fpdm"), returnData.get("fphm"),billInfo.getBillId(),"开票员", returnData.get("kprq"), "", true);*/
			}else{//失败
				errorMessage = returnData.get("msg");
				billInfoService.updateBillStatus(billInfo.getBillId(), null, null, DataUtil.BILL_STATUS_3, null);
			}
		}catch (HavaErrorMessageException he){
			he.printStackTrace();
			errorMessage = he.getMessage();
		}catch (Exception e) {
			e.printStackTrace();
			errorMessage = "生成pdf过程中出现错误";
		}finally{
			//生成pdf后，把结果回写给核心
			try {
				if(redBillFlag){
					afterCreateRedEInvoicePDFCallBackCore(billInfo, fpdm, fphm, errorMessage, PDF_CONTENT, sucessFlag);
				}else{
					afterCreateEInvoicePDFCallBackCore(billInfo, fpdm, fphm, errorMessage, PDF_CONTENT, sucessFlag);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				billInfoService.updateBillStatus(billInfo.getBillId(), null, null, DataUtil.BILL_STATUS_3, null);
			}
			
		}
		
	}
	
	
	public boolean createEInvoicePDF_manually(String billId) throws HavaErrorMessageException, ParseException, MalformedURLException, RemoteException, ServiceException {
		com.cjit.common.model.BillInfo billInfo = redReceiptApplyInfoService.findBillInfo1(billId);
		Map<String,String>  returnData = callHXInterface_downloadEInvoiceInfo(billInfo);//掉航信接口
		if("0000".equals(returnData.get("msgCode"))){//成功
			savePDF(returnData);
			
			//生成pdf后，把结果回写给核心
			String PDF_CONTENT = returnData.get("fpdm") + "_" + returnData.get("fphm") + ".pdf";
			if(billInfo.getAmtSum().doubleValue() < 0){
				afterCreateRedEInvoicePDFCallBackCore(billInfo, returnData.get("fpdm"), returnData.get("fphm"), "", PDF_CONTENT, true);
			}else{
				afterCreateEInvoicePDFCallBackCore(billInfo, returnData.get("fpdm"), returnData.get("fphm"), "", PDF_CONTENT, true);
			}
			
			
			/*billIssueDiskAssitService.updateBillIssueResult(returnData.get("fpdm"), returnData.get("fphm"),billInfo.getBillId(),"开票员", returnData.get("kprq"), "", true);*/
			
			return true;
		}else{
			throw new HavaErrorMessageException(returnData.get("msg"));
		}
		
	}
	
	
	private void afterCreateEInvoicePDFCallBackCore(com.cjit.common.model.BillInfo billInfo,
			String fpdm, String fphm, String errorMessage,
			String PDF_CONTENT, boolean sucessFlag) throws HavaErrorMessageException, MalformedURLException, RemoteException, ServiceException {
		String repnum = "";
		String qd_flag = "";
		List<Map<String,Object>> list = getPartofTransInfoByBillid(billInfo.getBillId());
		if(list!=null && list.size()>0){
			repnum = (String) list.get(0).get("TRANS_BUS_ID");
			qd_flag = (String) list.get(0).get("INSTNAME");
		}else{
			throw new HavaErrorMessageException("无法获取发票（billid："+billInfo.getBillId()+"）关联的交易信息");
		}
		
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("GBK");
		Element PackageResp = doc.addElement("Package");
		Element HeaderResp = PackageResp.addElement("Header");
		HeaderResp.addElement("RequestType").addText("0011");
		HeaderResp.addElement("UUID").addText(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HeaderResp.addElement("SendTime").addText(df.format(new Date()));
		Element Response = PackageResp.addElement("REQUEST");
		Element BUSSLIST=Response.addElement("BUSSLIST");
		Element BUSSINFO = BUSSLIST.addElement("BUSSINFO");
		BUSSINFO.addElement("AMT").addText(billInfo.getSumAmt().toString());
		BUSSINFO.addElement("TAX_AMT").addText(billInfo.getTaxAmtSum().toString());
		BUSSINFO.addElement("CONTNO").addText(billInfo.getInsureId());
		BUSSINFO.addElement("PAYNO").addText(repnum);
		BUSSINFO.addElement("ILLCODE").addText(fpdm);
		BUSSINFO.addElement("BILLNO").addText(fphm);
		BUSSINFO.addElement("ERRORINFO").addText(errorMessage);
		BUSSINFO.addElement("PDF_CONTENT").addText(PDF_CONTENT);
		if(sucessFlag){
			BUSSINFO.addElement("RESULTTYPE").addText("01");
		}else{
			BUSSINFO.addElement("RESULTTYPE").addText("02");
		}
		
		String requestXml = doc.asXML();
		System.out.print("生成pdf后回写报文：");
		System.out.println(requestXml);
		
		String responceStr;
		Axis2Client ac = new Axis2Client();
		responceStr = ac.invokeRPCClient("http://10.6.8.70:9812/services/MinShengVATInvoice?wsdl", requestXml, "http://10.6.8.70:9812/services/MinShengVATInvoice", "dealData");
		System.out.println("电子票生成pdf回写核心回传信息："+responceStr);
		
		if("I".equals(qd_flag)){//如果是个险还要回写微管家
			Axis2Client4CXF ac2 = new Axis2Client4CXF();
			responceStr = ac2.requestWebservice("http://10.6.96.91:9092//electronicinvoice/services/invoiceservice?wsdl", "invoiceservice", "http://platform.sss.sinosoft.com/", requestXml);
			System.out.println("电子票生成pdf回写微管家回传信息："+responceStr);
		}
	}
	
	
	private void afterCreateRedEInvoicePDFCallBackCore(com.cjit.common.model.BillInfo billInfo,
			String fpdm, String fphm, String errorMessage,
			String PDF_CONTENT, boolean sucessFlag) throws AxisFault, HavaErrorMessageException{
		String repnum = "";
		String qd_flag = "";
		List<Map<String,Object>> list = getPartofTransInfoByBillid(billInfo.getBillId());
		if(list!=null && list.size()>0){
			repnum = (String) list.get(0).get("TRANS_BUS_ID");
			qd_flag = (String) list.get(0).get("INSTNAME");
		}else{
			throw new HavaErrorMessageException("无法获取发票（billid："+billInfo.getBillId()+"）关联的交易信息");
		}
		
		
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("GBK");
		Element PackageResp = doc.addElement("Package");
		Element HeaderResp = PackageResp.addElement("Header");
		HeaderResp.addElement("RequestType").addText("0012");
		HeaderResp.addElement("UUID").addText(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HeaderResp.addElement("SendTime").addText(df.format(new Date()));
		Element Response = PackageResp.addElement("REQUEST");
		Element BUSSLIST=Response.addElement("BUSSLIST");
		Element BUSSINFO = BUSSLIST.addElement("BUSSINFO");
		BUSSINFO.addElement("AMT").addText(billInfo.getSumAmt().toString());
		BUSSINFO.addElement("TAX_AMT").addText(billInfo.getTaxAmtSum().toString());
		/*BUSSINFO.addElement("CONTNO").addText(billInfo.getInsureId());
		BUSSINFO.addElement("PAYNO").addText(repnum);*/
		BUSSINFO.addElement("BILLCODE").addText(fpdm);
		BUSSINFO.addElement("BILLNO").addText(fphm);
		BUSSINFO.addElement("ERRORINFO").addText(errorMessage);
		BUSSINFO.addElement("PDF_CONTENT").addText(PDF_CONTENT);
		BUSSINFO.addElement("BUSINESSID").addText(repnum);
		BUSSINFO.addElement("YBILLCODE").addText(billInfo.getOriBillCode());
		BUSSINFO.addElement("YBILLNO").addText(billInfo.getOriBillNo());
		if(sucessFlag){
			BUSSINFO.addElement("RESULTTYPE").addText("01");
		}else{
			BUSSINFO.addElement("RESULTTYPE").addText("02");
		}
		
		String requestXml = doc.asXML();
		System.out.print("生成pdf后回写报文：");
		System.out.println(requestXml);
		
		Axis2Client ac = new Axis2Client();
		String responceStr = ac.invokeRPCClient("http://10.6.8.70:9812/services/MinShengVATInvoice", requestXml, "http://10.6.8.70:9812/services/MinShengVATInvoice", "dealData");
		System.out.println("电子票生成pdf回写核心回传信息："+responceStr);
	}
	
	
	private List<Map<String,Object>> getPartofTransInfoByBillid(String billid){
		return this.getJdbcTemplate().queryForList("select tr.instname as INSTNAME,tr.trans_bus_id as TRANS_BUS_ID from vms_trans_info tr left join vms_trans_bill tb on tb.trans_id = tr.trans_id where tb.bill_id = ?",new Object[]{billid},new int[]{java.sql.Types.VARCHAR});
	}
	
	

	private void savePDF(Map<String, String> returnData) throws HavaErrorMessageException {
		try {
			String fileName = returnData.get("fpdm") + "_" + returnData.get("fphm") + ".pdf";
			returnData.put("fileName", fileName);
			String filePath = "/pdfdir/";
			BASE64Decoder decoder = new BASE64Decoder();
			decoder.decodeBuffer(new StringBufferInputStream(returnData.get("pdfContent")), new FileOutputStream(new File(filePath+fileName)));
			
			try {
				Thread.sleep(5000);
				StringBuilder result =new StringBuilder();
				Process process =	Runtime.getRuntime().exec("ls -l "+filePath+fileName);
				BufferedReader bufrIn = new	BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
				String line = null;
				while
				((line = bufrIn.readLine()) !=
				null) {
				result.append(line).append('\n');
				}	
				System.out.println("执行权限的脚本---------------------------"+result.toString());
				Process process1 =	Runtime.getRuntime().exec("chmod +rwxrwxrwx "+filePath+fileName);
				System.out.println("执行：chmod +rwxrwxrwx "+filePath+fileName);
				StringBuilder result2 =new StringBuilder();
				Process process2 =	Runtime.getRuntime().exec("ls -l "+filePath+fileName);
				BufferedReader bufrIn2 = new	BufferedReader(new InputStreamReader(process2.getInputStream(),"UTF-8"));
				String line2 = null;
				while
				((line2 = bufrIn2.readLine()) !=
				null) {
				result2.append(line2).append('\n');
				}	
				System.out.println("执行权限的脚本---------------------------"+result2.toString());
				
			   int result1=	process1.waitFor();
		
			System.out.println("执行shell命令返回的结果--------------buzhihuowu-----------------"+result1);
				
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new HavaErrorMessageException("保存PDF文件时出错",e);
		}
		
	}
	
	
	private Map<String,String> callHXInterface_downloadEInvoiceInfo(com.cjit.common.model.BillInfo billInfo) throws HavaErrorMessageException{
		Map<String,String> returnData = new HashMap<String, String>();
		returnData.put("msgCode", "");
		returnData.put("msg", "");
		returnData.put("fpdm", "");
		returnData.put("fphm", "");
		returnData.put("kprq", "");
		returnData.put("fileName", "");
		
		try {
			StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><InvoInfo><swno>");
			sb.append(billInfo.getBillId());
			sb.append("</swno><saleTax>");
			sb.append(billInfo.getTaxno());
			sb.append("</saleTax></InvoInfo>");
			System.out.println("电子发票下载请求报文："+sb.toString());
			
			//请求报文
			Axis2Client ac = new Axis2Client();                                                                                                                    
			String responceStr = ac.invokeRPCClient("http://10.6.97.163:8083/eisp-msrs/ws/zkiService?wsdl", sb.toString(), "http://service.ejinshui.com/", "downloadEInvoiceInfo");
			System.out.println("请求航信生成pdf文件航行给返回的结果信息====================="+responceStr);
			
			Document returnXml = DocumentHelper.parseText(responceStr);
			Element DownloadInfo = returnXml.getRootElement();
			Element returnMsg = DownloadInfo.element("returnMsg");
			returnData.put("msgCode", returnMsg.elementText("msgCode"));
			returnData.put("msg", returnMsg.elementText("msg"));
			if("0000".equals(returnData.get("msgCode"))){//请求成功
				returnData.put("fpdm", DownloadInfo.elementText("fpdm"));
				returnData.put("fphm", DownloadInfo.elementText("fphm"));
				returnData.put("kprq", DownloadInfo.elementText("kprq"));
				returnData.put("pdfContent", DownloadInfo.elementText("pdfContent"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HavaErrorMessageException("调航信\"下载pdf\"接口时，出现未知错误", e);
		}
		
		return returnData;
	}
	
	
	
	/**
	 * 
	 * @param billInfo
	 * @return
	 * @throws AxisFault 
	 * @throws DocumentException 
	 * @throws HangXinException
	 */

	private boolean issueBill(com.cjit.common.model.BillInfo billInfo,String option) throws HavaErrorMessageException, AxisFault, DocumentException {
		
		String sqlStr = "select nvl(vc.customer_email,'') from vms_bill_info b left join vms_trans_bill tb on b.bill_id = tb.bill_id left join vms_trans_info tr on tb.trans_id = tr.trans_id left join vms_customer_info vc on tr.customer_id = vc.customer_id where b.bill_id = ?";
		String customerEmail = "";
		try {
			customerEmail = this.getJdbcTemplate().queryForObject(sqlStr, new Object[]{billInfo.getBillId()}, new int[]{java.sql.Types.VARCHAR}, String.class);
		} catch (Exception e) {
		}
				
		billInfo.setCustomerEmail(customerEmail);
		List<com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo> list=billIssueDiskAssitService.findBillItemByBillIdDisk(billInfo.getBillId());
		List<Product> goodsList=new ArrayList<Product>();
		if(list.size()>0){
			for (int i=0;i<list.size();i++){
				com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo billItem=list.get(i);
				Product goods=new Product(billItem);
				goodsList.add(goods);
			}
		}
		
		
		EInvoice eInvoice = new EInvoice(billInfo, goodsList);
		Axis2Client ac = new Axis2Client();
		
		System.out.println("电子发票开具请求报文："+eInvoice.getBillIssueXml(option));
		String responceStr;
		try {
			responceStr = ac.invokeRPCClient("http://10.6.97.163:8083/eisp-msrs/ws/Service?wsdl", eInvoice.getBillIssueXml(option), "http://service.ejinshui.com/", "submitEInvoiceInfo");
		} catch (AxisFault e) {
			if("Connection timed out: connect".equals(e.getMessage())){
				throw new HavaErrorMessageException("请求航信“电子发票开具”接口超时");
			}else{
				throw e;
			}
		}
		System.out.println("电子发票开具返回报文："+responceStr);
		Document returnXml = DocumentHelper.parseText(responceStr);
		Element root = returnXml.getRootElement();
		if("0000".equals(root.elementText("msgCode"))){
			return true;
		}else{
			throw new HavaErrorMessageException("电子发票开具错误，错误代码："+root.elementText("msgCode")+"错误信息："+root.elementText("msg"));
		}
	}


	/**
	 * 解析核心发送的xml报文，保存交易信息
	 * 
	 * @param document 需要解析的xml文件
	 * @return
	 * @throws Exception 解析时报的异常
	 */
	
	/*private Map<BillInfo, String> paseXml(Document document) throws Exception{*/
	private Map<BillInfo, String> paseXml(Map xmlmap,List<TransInfoTemp> batchRunTransInfoINSList) throws Exception{
		/*Document doc=null;
		Element HeaderResp=null;
		
	
		//虏氓脠毛脢媒戮脻驴芒路碌禄脴碌脛陆谩鹿没脨脜脧垄
	
		String RequestType=null;
		
		String SendTime=null;*/
		String invtypValue=null;
		String UUID=java.util.UUID.randomUUID().toString().replace("-", "").toLowerCase();
		String result=null;
		Map<BillInfo,String> billInfomation = new HashMap<BillInfo,String>();
		
		System.out.println("6666666666666666666666-------封装报文1");
		
			//System.out.println(document.asXML()+"-----");
			/*Element Package=document.getRootElement();
			Element Header=Package.element("HEADER");			
			Element RequestTypeElement=Header.element("REQUESTTYPE");
			Element UUIDElement=Header.element("UUID");
			Element SendTimeElement=Header.element("SENDTIME");
			Element ResponseCode=Header.element("RESPONSECODE");
			Element Response = Package.element("RESPONSE");
			UUID=UUIDElement.getText();*/

			/*RequestType=RequestTypeElement.getText();
		
			SendTime=SendTimeElement.getText();*/
			
			/*ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"com/cjit/ws/service/config/applicationcontext.xml"});
			
			
			vmsTransTypeDao=(VmsTransTypeDao) ac.getBean("VmsTransTypeBean");
			vmsCustomerInfoDao=(VmsCustomerInfoDao) ac.getBean("VmsCustomerInfoBean");
			vmsTransInfoDao=(VmsTransInfoDao) ac.getBean("VmsTransInfoBean");
			*/
			/*Element bussListElement=Response.element("BUSSLIST");
			List<Element> bussInfoList=bussListElement.elements();*/
			/**
			 * 
			 * 0001解析交易信息
			 * */
			for(TransInfoTemp bussInfo:batchRunTransInfoINSList){
				
				xmlmap.put("TRANS_ID", bussInfo.getTRANS_ID());
				xmlmap.put("TRANS_DATE",bussInfo.getTRANS_DATE());
				xmlmap.put("TRANS_TYPE",bussInfo.getTRANS_TYPE());
				xmlmap.put("TAX_FLAG", bussInfo.getTAX_FLAG());
				xmlmap.put("TAX_RATE", bussInfo.getTAX_RATE());
				xmlmap.put("AMT_CCY", bussInfo.getAMT_CCY());
				xmlmap.put("BALANCE", bussInfo.getBALANCE());
				xmlmap.put("INSTCODE", bussInfo.getINSTCODE());
				xmlmap.put("CUSTOMER_ID", bussInfo.getCUSTOMER_ID());
				xmlmap.put("AMT_CNY", bussInfo.getAMT_CNY());
				xmlmap.put("TAX_AMT_CNY", bussInfo.getTAX_AMT_CNY());
				xmlmap.put("INCOME_CNY", bussInfo.getINCOME_CNY());
				xmlmap.put("TRANS_CURR", bussInfo.getTRANS_CURR());
				xmlmap.put("SURTAX1_AMT_CNY", bussInfo.getSURTAX1_AMT_CNY());
				xmlmap.put("SURTAX2_AMT_CNY", bussInfo.getSURTAX2_AMT_CNY());
				xmlmap.put("SURTAX3_AMT_CNY", bussInfo.getSURTAX3_AMT_CNY());
				xmlmap.put("SURTAX4_AMT_CNY", bussInfo.getSURTAX4_AMT_CNY());
				xmlmap.put("FAPIAO_TYPE", bussInfo.getFAPIAO_TYPE());
				xmlmap.put("VAT_RATE_CODE", bussInfo.getVAT_RATE_CODE());
				xmlmap.put("INSTNAME", bussInfo.getINSTNAME());
				xmlmap.put("CHERNUM", bussInfo.getCHERNUM());
				xmlmap.put("REPNUM", bussInfo.getREPNUM());
				xmlmap.put("TTMPRCNO", bussInfo.getTTMPRCNO());
				xmlmap.put("FEETYP", bussInfo.getFEETYP());
				xmlmap.put("BILLFREQ", bussInfo.getBILLFREQ());
				xmlmap.put("POLYEAR", bussInfo.getPOLYEAR());
				xmlmap.put("HISSDTE", bussInfo.getHISSDTE());
				xmlmap.put("OCCDATE", bussInfo.getOCCDATE());
				xmlmap.put("INSTFROM", bussInfo.getINSTFROM());
				xmlmap.put("INSTTO", bussInfo.getINSTTO());
				xmlmap.put("PREMTERM", bussInfo.getPREMTERM());
				xmlmap.put("WITHDRAWYN", bussInfo.getWITHDRAWYN());
				xmlmap.put("PLANLONGDESC", bussInfo.getPLANLONGDESC());
				xmlmap.put("INSCOD", bussInfo.getINSCOD());
				xmlmap.put("INSNAM", bussInfo.getINSNAM());
				xmlmap.put("BUSINESSID", bussInfo.getBUSINESSID());
				xmlmap.put("QDFLAG", bussInfo.getQDFLAG());
				xmlmap.put("HESITATE_PERIOD", bussInfo.getHESITATE_PERIOD());
				xmlmap.put("SYNCH_DATE", bussInfo.getSYNCH_DATE());
				xmlmap.put("ISYK", bussInfo.getISYK());
				xmlmap.put("CANCLE_STATE", bussInfo.getCANCLE_STATE());
				xmlmap.put("REMARK", bussInfo.getREMARK());
				
				System.out.println("77777777777777777777777777-------封装报文2");
				
				/*String businessId=bussInfoElement.elementText("BUSINESS_ID");
				String instId=bussInfoElement.elementText("INST_ID");
				String qdFlag=bussInfoElement.elementText("QD_FLAG");
				String chernum=bussInfoElement.elementText("CHERNUM");
				String repnum=bussInfoElement.elementText("REPNUM");
				String ttmprcno=bussInfoElement.elementText("TTMPRCNO");
				//新增 客户编号
				String customerNo=bussInfoElement.elementText("CUSTOMER_NO");
				String customerName=bussInfoElement.elementText("CUSTOMER_NAME");
				String customerTaxno=bussInfoElement.elementText("CUSTOMER_TAXNO");
				String customerAddressand=bussInfoElement.elementText("CUSTOMER_ADDRESSAND");
				String taxpayerType=bussInfoElement.elementText("TAXPAYER_TYPE");
				String customerPhone=bussInfoElement.elementText("CUSTOMER_PHONE");
				String customerBankand=bussInfoElement.elementText("CUSTOMER_BANKAND");
				String customerAccount=bussInfoElement.elementText("CUSTOMER_ACCOUNT");
				String origcurr=bussInfoElement.elementText("ORIGCURR");
				String origamt=bussInfoElement.elementText("ORIGAMT");
				String acctamt=bussInfoElement.elementText("ACCTAMT");
				String trdt=bussInfoElement.elementText("TRDT");
				List<String> errors=new ArrayList<String>();
				String invtyp=bussInfoElement.elementText("INVTYP");
				String bustyp=bussInfoElement.elementText("BUSTYP");
				String billfreq=bussInfoElement.elementText("BILLFREQ");
				String polyear=bussInfoElement.elementText("POLYEAR");
				String hissdte=bussInfoElement.elementText("HISSDTE");
				String planlongdesc=bussInfoElement.elementText("PLANLONGDESC");
				String instfrom=bussInfoElement.elementText("INSTFROM");
				String instto=bussInfoElement.elementText("INSTTO");
				String occdate=bussInfoElement.elementText("OCCDATE");
				String premterm=bussInfoElement.elementText("PREMTERM");
				String customerEmail=bussInfoElement.elementText("CUSTOMER_EMAIL"); */
				String businessId=(String) xmlmap.get("BUSINESSID");
				String instId=(String) xmlmap.get("INSTCODE");
				String qdFlag=(String) xmlmap.get("QDFLAG");
				String chernum=(String) xmlmap.get("CHERNUM");
				String repnum=(String) xmlmap.get("REPNUM");
				String ttmprcno=(String) xmlmap.get("TTMPRCNO");
				String customerNo=(String) xmlmap.get("CUSTOMER_ID");
				String customerName=(String) xmlmap.get("CUSTOMER_CNAME");
				String customerTaxno=(String) xmlmap.get("CUSTOMER_TAXNO");
				String customerAddressand=(String) xmlmap.get("CUSTOMER_ADDRESS");
				String taxpayerType=(String) xmlmap.get("TAXPAYER_TYPE");
				String customerPhone=(String) xmlmap.get("CUSTOMER_PHONE");
				String customerBankand=(String) xmlmap.get("CUSTOMER_CBANK");
				String customerAccount=(String) xmlmap.get("CUSTOMER_ACCOUNT");
				String origcurr=(String) xmlmap.get("TRANS_CURR");
				String origamt=xmlmap.get("AMT_CCY").toString();
				String acctamt=xmlmap.get("AMT_CNY").toString();
				String trdt=(String) xmlmap.get("TRANS_DATE");
				List<String> errors=new ArrayList<String>();
				
				String invtyp=(String) xmlmap.get("FAPIAO_TYPE");
				String bustyp=(String) xmlmap.get("FEETYP");
				String billfreq=(String) xmlmap.get("BILLFREQ");
				String polyear=xmlmap.get("POLYEAR").toString();
				String hissdte=(String) xmlmap.get("HISSDTE");
				String planlongdesc=(String) xmlmap.get("PLANLONGDESC");
				String instfrom=(String) xmlmap.get("INSTFROM");
				String instto=(String) xmlmap.get("INSTTO");
				String occdate=(String) xmlmap.get("OCCDATE");
				String premterm=xmlmap.get("PREMTERM").toString();
				//客户邮箱
				String customerEmail=(String) xmlmap.get("CUSTOMER_EMAIL");

				invtypValue=invtyp;
				
				/*if("1".equals(qdFlag)&&(customerName==null||"".equals(customerName)||customerTaxno==null||"".equals(customerTaxno)
						||customerAddressand==null||"".equals(customerAddressand)||customerPhone==null||"".equals(customerPhone)
						||customerBankand==null||"".equals(customerBankand)||customerAccount==null||"".equals(customerAccount))){
			
					BillInfo billInfo = new BillInfo();
					System.out.println("==============================================团险客户信息不完整");
					throw new  HavaErrorMessageException("团险客户信息不完整");
					
				}*/
			/*if("0".equals(invtyp)&&(customerName==null||"".equals(customerName)||(customerTaxno.length()!=15&&customerTaxno.length()!=18)
						||customerAddressand==null||"".equals(customerAddressand)||customerPhone==null||"".equals(customerPhone)
						||customerBankand==null||"".equals(customerBankand)||customerAccount==null||"".equals(customerAccount))){
						
				BillInfo billInfo = new BillInfo();
				result=returnXmlInformation(doc, "专票客户信息不完整", UUID, billInfo, "1","");
				throw new RuntimeException(result);
				}*/
				
				
				/*Map map = new HashMap();				
				map.put("instId",instId);				
				instId = vmsTransInfoDao.find(map);*/
				
				//2018-06-14新增，校验当前机构是否配置过机构映射
				/*String parentId = vmsTransInfoDao.findMechanism(instId,invtyp); //机构代码和发票类型
				if(parentId != null && !"".equals(parentId)){
					instId = parentId;
				}*/
		        
				System.out.println("8888888888888888-------封装报文3");
				
				VmsTransInfo vmsTransInfo=new VmsTransInfo();
				
				vmsTransInfo.setTransUUID(UUID);
				vmsTransInfo.setBusinessId(businessId);
				vmsTransInfo.setInstId(instId);
				vmsTransInfo.setQdFlag(qdFlag);
				vmsTransInfo.setChernum(chernum);
				vmsTransInfo.setRepnum(repnum);
				vmsTransInfo.setTtmprcno(ttmprcno);
				vmsTransInfo.setOrigcurr(origcurr);
				vmsTransInfo.setOrigamt(Double.parseDouble(origamt));
				vmsTransInfo.setAcctamt(Double.parseDouble(acctamt));
				vmsTransInfo.setTrdt(trdt);
				vmsTransInfo.setInvtyp(invtyp);
				vmsTransInfo.setCustomerid(customerNo);
				vmsTransInfo.setCustomerAccount(customerAccount);
				vmsTransInfo.setBustyp(bustyp);
				vmsTransInfo.setBillfreq(billfreq);
				if(polyear!=null&&!"".equals(polyear)){
					System.out.println(polyear);
					System.out.println(polyear.substring(0, polyear.indexOf(".")));
					vmsTransInfo.setPolyear(Integer.parseInt(polyear.substring(0, polyear.indexOf("."))));
				}
				vmsTransInfo.setHissdte(hissdte);
				vmsTransInfo.setPlanlongdesc(planlongdesc);
				vmsTransInfo.setInstfrom(instfrom);
				vmsTransInfo.setInstto(instto);
				vmsTransInfo.setOccdate(occdate);
				if(premterm!=null&&!"".equals(premterm)){
					System.out.println(premterm);
					vmsTransInfo.setPremterm(Integer.parseInt(premterm));
				}
				
				VmsCustomerInfo vmsCustomerInfo=new VmsCustomerInfo();
				vmsCustomerInfo.setCustomerNo(customerNo);
				vmsCustomerInfo.setCustomerName(customerName);
				vmsCustomerInfo.setCustomerTaxno(customerTaxno);
				vmsCustomerInfo.setCustomerAddressand(customerAddressand);
				vmsCustomerInfo.setTaxpayerType(taxpayerType);
				vmsCustomerInfo.setCustomerPhone(customerPhone);
				vmsCustomerInfo.setCustomerBankand(customerBankand);
				vmsCustomerInfo.setCustomerAccount(customerAccount);
				vmsCustomerInfo.setInvtyp(invtyp);
				vmsCustomerInfo.setBusinessId(businessId);
				vmsCustomerInfo.setCustomerEmail(customerEmail);
				vmsCustomerInfo.setChernum(chernum);
				
				
				/*Element covListElement=bussInfoElement.element("COVLIST");
				List<Element> covInfoList=covListElement.elements("COVINFO");*/
				
				/*if(batchRunTransInfoINSList==null||batchRunTransInfoINSList.size()<1){
					throw new HavaErrorMessageException("险种报文信息解析为空");
				}*/
				int i = 1;
//				List<CovInfo> covList=new ArrayList<CovInfo>();
				/*
				 * transIdList 存放生成交易的id存放的id
				 * */
				List<String> transIdList = new ArrayList<String>();
				for(TransInfoTemp covInfoElement:batchRunTransInfoINSList){
					String transType = covInfoElement.getTRANS_TYPE();
					String insCod=covInfoElement.getINSCOD();
					String insNam=covInfoElement.getINSNAM();
					String feetyp=covInfoElement.getFEETYP();
					String amtCny=covInfoElement.getAMT_CNY().toString();
					String taxAmtCny=covInfoElement.getTAX_AMT_CNY().toString();
					String incomeCny=covInfoElement.getINCOME_CNY().toString();
					String taxRate=covInfoElement.getTAX_RATE().toString();
					System.out.println(insCod);
					System.out.println(insNam);
					/*if("Z".equals(taxRate)||"F".equals(taxRate)){
						invtypValue="1";
					}*/
					double taxRateValue = 0;
					if("S".equals(taxRate)){
						taxRateValue=0.06;
					}else if("N".equals(taxRate)){
						taxRateValue=0.03;
					}else if("Z".equals(taxRate)){
						taxRateValue=0;
					}else if("P".equals(taxRate)){
						taxRateValue=0.17;
					}else if("F".equals(taxRate)){
						taxRateValue=0;
					}else{
						throw new HavaErrorMessageException("税率不合法");
					}
					i++;
					transIdList.add(UUID+i);
					vmsTransInfo.setTransUUID(UUID+i);
					vmsTransInfo.setInvtyp(invtypValue);
					vmsTransInfo.setFeetyp(feetyp);
					vmsTransInfo.setAmtCny(Double.parseDouble(amtCny));
					vmsTransInfo.setTaxAmtCny(Double.parseDouble(taxAmtCny));
					vmsTransInfo.setIncomeCny(Double.parseDouble(incomeCny));
					vmsTransInfo.setTaxRate(taxRateValue);
					vmsTransInfo.setVatRateCode(taxRate);
					vmsTransInfo.setTransType(transType);
					vmsTransInfo.setInsCod(insCod);
					vmsTransInfo.setInsNam(insNam);
					
					System.out.println("9999999999999999999999-------封装报文4");
					
					/*result=vmsTransInfoDao.insert(vmsTransInfo);*/
					
					result = vmsTransInfoDao.update(vmsTransInfo);
					
					System.out.println("101010101010101010101010-------封装报文5");

				}
				vmsCustomerInfo.setInvtyp(invtypValue);
				/*result=vmsCustomerInfoDao.insert(vmsCustomerInfo);*/
				System.out.println("101010101010101010101010-------封装报文6");
				
				if("Y".equals(result)){	
					List<BillInfo> billList = transToEachBill(transIdList);
					for (BillInfo billInfo : billList) {
						billInfo.setCustomerEmail(customerEmail);
						billInfo.setCherNum(chernum);
						billInfomation.put(billInfo, vmsTransInfo.getRepnum());
					}
				}else{
					throw new HavaErrorMessageException("解析报文插入客户信息或者交易信息失败");
				}
			
			/*}
			
			 * 00002保存交易信息成功
			 * */
			
			}
			System.out.println("101010101010101010101010-------封装报文7");
		return billInfomation;
		
	}

	/**
	 * 创建票据信息
	 * @param transIdList
	 * @return
	 * @throws Exception
	 * @list 存放合并开票之后的票据信息
	 */

	public List<BillInfo> transToEachBill(List<String> transIdList) throws Exception {
		 List<BillInfo> list = new ArrayList<BillInfo>();
		 List<TransInfo> transLists = new ArrayList<TransInfo>();
			/***
			 * 2017/12/24
			 * 合并开票
			 */
		
		CheckResult result =null;
		// 循环构建交易信息List
	/*	for (int i = 0; i < transIdList.size(); i++) {
			List<TransInfo> list2 = new ArrayList<TransInfo>();
			list2 = createBillServiceManual.findEletronTrandsInfo(transIdList.get(i));
			//transIds += this.selectTransIds[i];
			transLists.add(list2.get(0));
		}*/
		for (int i = 0; i < transIdList.size(); i++) {

			TransInfo searPar = new TransInfo();
			//searPar.setTransId(transIdList.get(i));
			searPar.setTransUUID(transIdList.get(i));
			searPar = createBillServiceManual.findTransInfo(searPar); //需增加一个标识字段，否则查询不到对应的票据信息


			StringBuffer sb = new StringBuffer();
			sb.append("投保单号：【");
			sb.append((searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno()) ? searPar.getTtmpRcno() : "         "));
			sb.append("】\n保单号：【");
			sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum() : "         "));
			sb.append("】\n");
			searPar.setRemark(sb.toString());
			transLists.add(searPar);
		}
	
		// 作全局校验
		CheckResult checkResult = (CheckResult) billValidationServices
				.shortCircuitValidation(transLists);
		

		// 构建票据 并保存
		if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
			throw new HavaErrorMessageException(checkResult.getCheckResultMsg());
		} else {
			
			// 构建票据对象
			ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
			createBillServiceManual =  (CreateBillService) applicationContext.getBean("createBillServiceManual");
			list=	createBillServiceManual.eleConstructBillAndSaveAsMerge(transLists);						
			//获取构建的所有的票据list					
			if(list.size()<=0){
				throw new HavaErrorMessageException(" 调用----createBillServiceManual.constructBill(transLists, null)-------------------------组件票据信息错误");
			}
		}			
			return list;
	}
	
	

		
	
public	 class HavaErrorMessageException extends Exception{
		public HavaErrorMessageException(String sbMessage) {
			super(sbMessage);
		}
		
		public HavaErrorMessageException(String sbMessage,Throwable e) {
			super(sbMessage, e);
		}
		
	}
	
	//封装返回蓝票信息的报文
	private String returnXmlInformation(Document doc,String information,String uuid,BillInfo billInfo,String result,String pdfName){
		Element HeaderResp=null;	
		//String result=null;//虏氓脠毛脢媒戮脻驴芒路碌禄脴碌脛陆谩鹿没脨脜脧垄
		String invtypValue=null;
		String RequestType=null;
		doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("GBK");
		Element PackageResp = doc.addElement("PACKAGE");
		HeaderResp = PackageResp.addElement("HEADER");
		HeaderResp.addElement("REQUESTTYPE").addText("0011");
		HeaderResp.addElement("UUID").addText(uuid);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
		
		Element Request = PackageResp.addElement("Request");
		
		Element ErrorList=Request.addElement("BUSSLIST");
		Element BUSSINFO=ErrorList.addElement("BUSSINFO");
		BUSSINFO.addElement("RESULTTYPE").addText("1");
		BUSSINFO.addElement("ERRORINFO").addText(information);
		BUSSINFO.addElement("ILLCODE").addText("");
		BUSSINFO.addElement("BILLNO").addText("");
		if(billInfo.getAmtSum().toString()==null||"".equals(billInfo.getAmtSum().toString())){
			BUSSINFO.addElement("AMT").addText("");
			BUSSINFO.addElement("TAX_AMT").addText("");	
		}
		else{
			BUSSINFO.addElement("AMT").addText(billInfo.getAmtSum().toString());
			BUSSINFO.addElement("TAX_AMT").addText(billInfo.getTaxAmtSum().toString());
		}
		if(pdfName==null||"".equals(pdfName)){
			BUSSINFO.addElement("PDF_CONTENT").addText("");	
		}
		else{
			BUSSINFO.addElement("PDF_CONTENT").addText(pdfName+".pdf");
		}
		return doc.asXML();
	}
	/**
	 * TODO  解析核心请求的红票开具的报文
	 * @param document
	 * 
	 */
	
	private List<String> paseRedXml(Document document,String uuid,String requesType) throws Exception {
		// TODO Auto-generated method stub  解析成功返回list集合，失败返回失败的报文
		Element Package=document.getRootElement();
		Element Request = Package.element("REQUEST");
		Document doc=null;
		List<String> resultString = new ArrayList<String>();
		
		try {
			//解析报文
		
			Element bussListElement=Request.element("BUSSLIST");
			List<Element> bussInfoList=bussListElement.elements();
			
		
			for (Element element : bussInfoList) {
				StringBuffer xmlStringBuffer = new StringBuffer();
				String  businessId =element.elementText("BUSINESSID");
				String billCode=element.elementText("YBILLCODE");
				String billNo = element.elementText("YBILLNO");
				xmlStringBuffer.append(billCode+"@");
				xmlStringBuffer.append(billNo+"@");
				xmlStringBuffer.append(businessId);
				resultString.add(xmlStringBuffer.toString());
				
			}
				
			return resultString;
			
		} catch (Exception e) {
			System.out.println("==============================================解析红冲报文失败");
			// TODO: handle exception 组件失败的报文并抛出
			e.printStackTrace();
		
		
			throw new RuntimeException("解析红冲报文失败");
			
		}
		
		
	}
	
	/**
	 * 根据发票代码和发票号码查询出蓝票的billid
	 * @param list
	 * @return
	 * @throws Exception
	 */
	protected List<String> finBlueBillId(List<String> list) throws Exception{
		List<String> billListInfor = new ArrayList<String>();
		
			
			for (String string : list) {
				//根据发票代码和发票号码查询出发票信息
				String[] CodeNo=string.split("@");
				String billId= findBillInfo(CodeNo[0], CodeNo[1]);
				//根据蓝票id创建红票的票据信息
				billListInfor.add(billId);
				
			}
	
		
		// TODO Auto-generated method stub
		return billListInfor;
	}
	
	
	/**
	 * 
	 * TODO 请求红票过程中错误时返回的报文
	 * 
	 * @param requestType 请求类型 0012红冲申请
	 * @param uuid  报文中请求的uuid
	 * @param businessId  
	 * @param billCode
	 * @param billNo
	 * @param errorInfo
	 * @return
	 */
	
	
	public String redErrorReturnXnl(String requestType,String uuid,String businessId,String billCode,String billNo,String errorInfo){
		Document doc = null;
		doc= DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element HeaderResp=null;
		Element PackageResp = doc.addElement("PACKAGE");
		HeaderResp = PackageResp.addElement("HEADER");
		HeaderResp.addElement("REQUESTTYPE").addText(requestType);
		HeaderResp.addElement("UUID").addText(uuid);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
		Element Request = PackageResp.addElement("REQUEST");
		Element BUSSLIST = Request.addElement("BUSSLIST");
		Element BUSSINFO = BUSSLIST.addElement("BUSSINFO");
		BUSSINFO.addElement("RESULTTYPE").addText("1");
		BUSSINFO.addElement("ERRORINFO").addText(errorInfo);
		BUSSINFO.addElement("BUSINESSID").addText(businessId);
		BUSSINFO.addElement("YBILLCODE").addText(billCode);
		BUSSINFO.addElement("YBILLNO").addText(billNo);
		BUSSINFO.addElement("BILLCODE").addText("");
		BUSSINFO.addElement("BILLNO").addText("");
		BUSSINFO.addElement("AMT").addText("");
		BUSSINFO.addText("TAX_AMT").addText("");
		return doc.asXML();
	}
	
	
	public String blueErrorReturnXnl(String requestType,String uuid,String businessId,String billCode,String billNo,String errorInfo){
		Document doc = null;
		doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element HeaderResp=null;
		Element PackageResp = doc.addElement("PACKAGE");
		HeaderResp = PackageResp.addElement("HEADER");
		HeaderResp.addElement("REQUESTTYPE").addText(requestType);
		HeaderResp.addElement("UUID").addText(uuid);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HeaderResp.addElement("SENDTIME").addText(df.format(new Date()));
		Element Request = PackageResp.addElement("REQUEST");
		Element BUSSLIST = Request.addElement("BUSSLIST");
		Element BUSSINFO = BUSSLIST.addElement("BUSSINFO");
		BUSSINFO.addElement("RESULTTYPE").addText("1");
		BUSSINFO.addElement("ERRORINFO").addText(errorInfo);
		BUSSINFO.addElement("ILLCODE").addText("");
		BUSSINFO.addElement("BILLNO").addText("");
		BUSSINFO.addElement("AMT").addText("");
		BUSSINFO.addElement("TAX_AMT").addText("");
		BUSSINFO.addElement("PDF_CONTENT").addText("");
		return doc.asXML();
	}

	/**
	 * 组件开票过程中的报文体
	 * @param billNode
	 * @param billNo
	 * @return
	 * @throws Exception
	 */
	public void creadBlueXmlBody(Element BUSSINFO,String RESULTTYPE,String ERRORINFO,String ILLCODE,String BILLNO,String AMT,String TAX_AMT,String PDF_CONTENT){
		  BUSSINFO.addElement("RESULTTYPE").addText(RESULTTYPE);
			BUSSINFO.addElement("ERRORINFO").addText(ERRORINFO);
			BUSSINFO.addElement("ILLCODE").addText(ILLCODE);
			BUSSINFO.addElement("BILLNO").addText(BILLNO);
			BUSSINFO.addElement("AMT").addText(AMT);
			BUSSINFO.addElement("TAX_AMT").addText(TAX_AMT);
			BUSSINFO.addElement("PDF_CONTENT").addText(PDF_CONTENT);
	}
	/***
	 * 组件红票返回的报文体
	 * @param BUSSINFO
	 * @param RESULTTYPE
	 * @param ERRORINFO
	 * @param ILLCODE
	 * @param BILLNO
	 * @param AMT
	 * @param TAX_AMT
	 * @param PDF_CONTENT
	 */
	public void creadRedXmlBody(Element BUSSINFO,String RESULTTYPE,String ERRORINFO,String BUSINESSID,String BILLCODE,String BILLNO,String AMT,String TAX_AMT,String PDF_CONTENT){
		  BUSSINFO.addElement("RESULTTYPE").addText(RESULTTYPE);
			BUSSINFO.addElement("ERRORINFO").addText(ERRORINFO);
			BUSSINFO.addElement("BUSINESSID").addText(BUSINESSID);
			BUSSINFO.addElement("YBILLCODE").addText(BILLCODE);
			BUSSINFO.addElement("YBILLNO").addText(BILLNO);
			BUSSINFO.addElement("BILLCODE").addText("");
			BUSSINFO.addElement("BILLNO").addText("");
			BUSSINFO.addElement("AMT").addText(AMT);
			BUSSINFO.addElement("TAX_AMT").addText(TAX_AMT);
			BUSSINFO.addElement("PDF_CONTENT").addText(PDF_CONTENT);
	}
	
	
	
	/***
	 * 根据蓝票发票代码和发票号码查找出发票id
	 * @param billNode
	 * @param billNo
	 * @return
	 * @throws Exception
	 */
		public String findBillInfo(String billNode,String billNo) throws Exception{
		RedReceiptApplyInfo bInfo =null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("billNode", billNode);
			map.put("billNo", billNo);
			List<RedReceiptApplyInfo>  findBillInfo = null; /*redReceiptApplyInfoService.findBillInfo(map);*/
			bInfo = findBillInfo.get(0);
			
		} catch (Exception e) {
			System.out.println("根据发票代码和发票号码查询发票id出现错误=====");
			e.printStackTrace();
			// TODO: handle exception
		}	
		return bInfo.getBillId();
	}
	
	/**
	 * 根据蓝票的发票id创建红票的信息
	 * @param billId
	 */
	public com.cjit.vms.trans.model.BillInfo creadRedBillInfo(String billId)  {
		// TODO Auto-generated method stub
		com.cjit.vms.trans.model.BillInfo newRedBillInfo=null;
	
			com.cjit.vms.trans.model.BillInfo bill = redReceiptApplyInfoService.findBillInfo1(billId);
			
			List<TransBillInfo> list = null;/* redReceiptApplyInfoService.finTransBillByBillId(billId);*/
			//生成红票信息
			newRedBillInfo=	 transBillMergeOrSplit_New2(billId,list,bill.getIsHandiwork());//added 20170110

			// 获取所有红冲后的新bill
			List redBill = redReceiptApplyInfoService
					.findRedBillByOriBillId(billId);
			com.cjit.vms.trans.model.BillInfo redBillInfo;
			// 合计金额净额
			BigDecimal redAmtSum = new BigDecimal("0");// 净价
			BigDecimal redSumAmt = new BigDecimal("0");// 价税合计

			for (int i = 0; i < redBill.size(); i++) {
				redBillInfo = (com.cjit.vms.trans.model.BillInfo) redBill.get(i);
				redAmtSum = redAmtSum.add(redBillInfo.getAmtSum());
				redSumAmt = redSumAmt.add(redBillInfo.getSumAmt());
			}
			
			// 获取原bill
			// System.out.println("*********************************" + billId);
			
			// 设定Balance:原balance + 红冲掉的amtSum(后者已为负值)

			bill.setBalance(bill.getBalance() == null ? bill.getSumAmt().add(
					redSumAmt) : bill.getBalance().add(redSumAmt));
			bill.setOperateStatus(bill.getDataStatus());
			// 原billinfo状态：[16：红冲待审核]
			bill.setDataStatus(DataUtil.BILL_STATUS_16);
			
			bill.setCancelReason(null);
			// 更新原billInfo
			redReceiptApplyInfoService.saveBillInfo1(bill, true);
		
		return newRedBillInfo;
		
	}
	
	/**
	 * added 20161230
	 * 对原发票，生成红票及相应的vms_trans_bill,vms_bill_item_info信息
	 * @param billId2 
	 * @param transBillList  原发票关联的所有的交易信息
	 * @param ISHANDIWORK 
	 */
	private com.cjit.vms.trans.model.BillInfo transBillMergeOrSplit_New2 (String oldBillId,
			List<TransBillInfo> transBillList, String ISHANDIWORK)  {
			com.cjit.vms.trans.model.BillInfo billInfo = null;
			
				//
			if(transBillList!=null && transBillList.size()>0){
				// 生成新billId
				String strBillId = createBillId("B");
				
				BillItemInfo billItem = new BillItemInfo();
				billItem.setBillId(oldBillId);
				//蓝票的发票明细
				List<BillItemInfo> billItemInfolist = redReceiptApplyInfoService.findBillItemInfoList(billItem);
				
				Map<String, List<TransBillInfo>> map = groupTransBillByItemId(transBillList);
				for (Object itemId : map.keySet()) {
					String strBillItemId = createBusinessId("BI");//生成新billItemId
					String oldBillItemId = map.get(itemId).get(0).getBillItemId();
					
					create_TransBill(strBillId,strBillItemId,map.get(itemId));
					for (int i = 0; i < billItemInfolist.size(); i++) {
						if(oldBillItemId.equals(billItemInfolist.get(i).getBillItemId())){
							create_BillItem(strBillId,strBillItemId,billItemInfolist.get(i));
							billItemInfolist.remove(i);
							break;
						}
					}
				}
				
				
				billInfo=	createAndSaveRedBill(strBillId,oldBillId);
			
		}
			
			return billInfo;
	}
	
	
	
	protected String createBillId(String tabFlag) {
		String temp = DateUtils.serverCurrentDetailDate();
		String sequence = transInfoService.getBillIdSequence();
		return tabFlag + temp + sequence;
	}
	
	protected String createBusinessId(String tabFlag) {
		if (tabFlag == null) {
			tabFlag = "";
		}
		String temp = DateUtils.serverCurrentTimeStamp();
		long random = Math.round(Math.random() * 110104 + 100000);
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return tabFlag + timeStamp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		} else {
			return tabFlag + temp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		}
	}
	
	/**
	 * 按billItemId分组
	 * @param transBillList
	 * @return
	 */
	private Map<String, List<TransBillInfo>> groupTransBillByItemId(List<TransBillInfo> transBillList) {
		Map<String, List<TransBillInfo>> result = new HashMap<String, List<TransBillInfo>>();
		for (TransBillInfo tb : transBillList) {
			if(!result.containsKey(tb.getBillItemId())){
				List<TransBillInfo> temp = new ArrayList<TransBillInfo>();
				temp.add(tb);
				result.put(tb.getBillItemId(), temp);
			}else{
				result.get(tb.getBillItemId()).add(tb);
			}
		}
		return result;
	}
	/**
	 * 添加交易bill关联表信息
	 * @param strBillId
	 * @param strBillItemId
	 * @param transList
	 */
	private void create_TransBill(String strBillId,
			String strBillItemId, List<TransBillInfo> transList) {
		for (int i = 0; i < transList.size(); i++) {
			TransBillInfo transBill =  transList.get(i);
			
			// 插入交易票据对应信息  
			// TODO 需要修改
			if(!"".equals(transBill.getTransId())&&transBill.getTransId()!=null){
			transInfoService.saveTransBill(transBill.getTransId(), strBillId,
					strBillItemId,
					new BigDecimal(0).subtract(transBill.getAmtCny()),
					new BigDecimal(0).subtract(transBill.getTaxAmtCny()),
					new BigDecimal(0).subtract(transBill.getIncomeCny()));
			}
		}
	}
	/**
	 *生成红票的发票明细
	 * @param strBillId
	 * @param strBillItemId
	 * @param billItem
	 */
	private void create_BillItem(String strBillId, String strBillItemId,
			BillItemInfo billItem) {
		billItem.setBillId(strBillId);
		billItem.setBillItemId(strBillItemId);
		billItem.setAmt(new BigDecimal(0).subtract(billItem.getAmt()));
		billItem.setTaxAmt(new BigDecimal(0).subtract(billItem.getTaxAmt()));
		billItem.setGoodsNo(new BigDecimal(0).subtract(billItem.getGoodsNo()));
		billItem.setTaxFlag("N");
		// 保存新生成billItem
		redReceiptApplyInfoService.saveBillItemInfo(billItem, false);
	}
	
	/***
	 * 保存红票信息
	 * @param newBilled
	 * @param oldBillId
	 */
	
	private com.cjit.vms.trans.model.BillInfo createAndSaveRedBill(String newBilled, String oldBillId) {
		com.cjit.vms.trans.model.BillInfo billInfo=null;
		
			 billInfo = (com.cjit.vms.trans.model.BillInfo) (billInfoService.findBillInfo(oldBillId));
			billInfo.setBillId(newBilled);
			billInfo.setAmtSum(new BigDecimal(0).subtract(billInfo.getAmtSum()));
			billInfo.setBalance(new BigDecimal(0).subtract(billInfo.getSumAmt()));//对整张发票红冲
			billInfo.setTaxAmtSum(new BigDecimal(0).subtract(billInfo.getTaxAmtSum()));
			billInfo.setSumAmt(new BigDecimal(0).subtract(billInfo.getSumAmt()));
			billInfo.setDataStatus(DataUtil.BILL_STATUS_20);// 红冲待审核
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_1);// 人工审核
			billInfo.setOriBillCode(billInfo.getBillCode());
			billInfo.setOriBillNo(billInfo.getBillNo());
			billInfo.setBillCode("");
			billInfo.setBillNo("");
			billInfo.setBillDate("");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			billInfo.setApplyDate(sf.format(new Date()));
			// 保存bill
			billInfoService.saveBillInfo(billInfo, false);
		
		
		return billInfo;
	
	
	}
	
	//测试类
	public static void main(String[] args) throws AxisFault {
		
		
	/*	String xml="<?xml version=\"1.0\" encoding=\"GBK\"?> <PACKAGE><HEADER><REQUESTTYPE>0011</REQUESTTYPE><UUID>f7e51e804ac9486e9d9acd4cec4de766</UUID><SENDTIME>2017-12-25 14:23:12</SENDTIME></HEADER><REQUEST><BUSSLIST><BUSSINFO><ILLCODE>12345</ILLCODE><BILLNO>1234534</BILLNO><AMT>100</AMT><TAX_AMT>6</TAX_AMT><CONTNO>111111</CONTNO><PAYNO>222222</PAYNO><PDF_CONTENT>电子发票票样文本</PDF_CONTENT></BUSSINFO></BUSSLIST></REQUEST></PACKAGE>";
     	Service service = new Service();
		Call _call=null;
		try {
			_call = (Call) service.createCall();
		
		_call.setTargetEndpointAddress("http://10.0.11.19:8001///electronicinvoice/services/invoiceservice?wsdl");
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setUseSOAPAction(true);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		
		_call.addParameter("xml",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
		
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		//_call.setOperationName("http://EWebBuss.lis.sinosoft.com");
		javax.xml.namespace.QName q = new javax.xml.namespace.QName("http://platform.sss.sinosoft.com/", "invoiceservice");
		_call.setOperationName(q);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		String res;
		try {
			res = (String) _call.invoke(
					new String[] { xml });
			System.out.println(res);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
		
		
		SAXReader reader = new SAXReader();
	
		reader.setEncoding("UTF-8");//这里设置文件编码
		Document document;
		String documentStr = null;
		String u=Class.class.getClass().getResource("/").getPath();
		System.out.println(u);
		try {
		//	document = reader.read(new File("/pdfdir/pdfbw.xml"));
			//document = reader.read(new File("E:/buss1.xml"));
			document = reader.read(new File("E:/redBill.xml"));
			documentStr = document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Axis2Client ac = new Axis2Client();
		/**
		 * 本地测试本地服务
		 */
		String str = ac.invokeRPCClient("http://10.0.22.78:7001/vmss/services/vmsEletronService?wsdl", documentStr, "http://impl.service.ws.cjit.com", "transService");
		
		
		/**
		 * 
		 */
	//	String responceStr = ac.invokeRPCClient("http://218.249.166.22:6000/eisp-zk/ws/zkiService?wsdl",documentStr, "http://service.ejinshui.com/", "submitEInvoiceInfo");
	//	System.out.println("请求航信的报文返回信息"+responceStr);
		
		//String str = ac.invokeRPCClient("http://10.0.11.19:8001/electronicinvoice/services/invoiceservice?wsdl", documentStr, "http://platform.sss.sinosoft.com/", "invoiceservice");
		System.out.println(str);
		
	
	}
		
}
