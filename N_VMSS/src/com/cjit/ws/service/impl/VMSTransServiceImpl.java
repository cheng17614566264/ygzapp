package com.cjit.ws.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cjit.ws.dao.VmsCustomerInfoDao;
import com.cjit.ws.dao.VmsTransInfoDao;
import com.cjit.ws.dao.VmsTransTypeDao;
import com.cjit.ws.entity.VmsCustomerInfo;
import com.cjit.ws.entity.VmsTransInfo;
import com.cjit.ws.entity.VmsTransType;
import com.cjit.ws.service.VMSTransService;

public class VMSTransServiceImpl implements VMSTransService {
	private VmsTransTypeDao vmsTransTypeDao;
	private VmsCustomerInfoDao vmsCustomerInfoDao;
	private VmsTransInfoDao vmsTransInfoDao;

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

	@Override
	public String transService(String xml){
		System.out.println("XML:"+xml);
		Document document=null;
		Document doc=null;
		Element HeaderResp=null;
		String RequestType=null;
		String UUID=null;
		String SendTime=null;
		String result=null;//插入数据库返回的结果信息
		String invtypValue=null;
		List<String> errors=new ArrayList<String>();
		try {
			document = DocumentHelper.parseText(xml);
			Element Package=document.getRootElement();
			Element Header=Package.element("Header");
			Element Request=Package.element("Request");
			Element RequestTypeElement=Header.element("RequestType");
			Element UUIDElement=Header.element("UUID");
			Element SendTimeElement=Header.element("SendTime");

			RequestType=RequestTypeElement.getText();
			UUID=UUIDElement.getText();
			SendTime=SendTimeElement.getText();
			
			System.out.println("&&&");
			
			ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"com/cjit/ws/service/config/applicationcontext.xml"});
			System.out.println("111");
			vmsTransTypeDao=(VmsTransTypeDao) ac.getBean("VmsTransTypeBean");
			System.out.println("222");
			vmsCustomerInfoDao=(VmsCustomerInfoDao) ac.getBean("VmsCustomerInfoBean");
			System.out.println("333");
			vmsTransInfoDao=(VmsTransInfoDao) ac.getBean("VmsTransInfoBean");
			
			Element bussListElement=Request.element("BUSSLIST");
			List<Element> bussInfoList=bussListElement.elements();
			
			List<VmsTransInfo> vmsTransInfos=new ArrayList<VmsTransInfo>();
			List<VmsTransType> vmsTransTypes=new ArrayList<VmsTransType>();
			List<VmsCustomerInfo> vmsCustomerInfos=new ArrayList<VmsCustomerInfo>();
			System.out.println("$$$");
			for(Element bussInfoElement:bussInfoList){
				String businessId=bussInfoElement.elementText("BUSINESS_ID");
				String instId=bussInfoElement.elementText("INST_ID");
				String qdFlag=bussInfoElement.elementText("QD_FLAG");
				String chernum=bussInfoElement.elementText("CHERNUM");
				String repnum=bussInfoElement.elementText("REPNUM");
				String ttmprcno=bussInfoElement.elementText("TTMPRCNO");
				String customerName=bussInfoElement.elementText("CUSTOMER_NAME");
				String customerTaxno=bussInfoElement.elementText("CUSTOMER_TAXNO");//个险暂时传为客户号
				String customerAddressand=bussInfoElement.elementText("CUSTOMER_ADDRESSAND");
				String taxpayerType=bussInfoElement.elementText("TAXPAYER_TYPE");
				String customerPhone=bussInfoElement.elementText("CUSTOMER_PHONE");
				String customerBankand=bussInfoElement.elementText("CUSTOMER_BANKAND");
				String customerAccount=bussInfoElement.elementText("CUSTOMER_ACCOUNT");
				String origcurr=bussInfoElement.elementText("ORIGCURR");
				String origamt=bussInfoElement.elementText("ORIGAMT");
				String acctamt=bussInfoElement.elementText("ACCTAMT");
				String trdt=bussInfoElement.elementText("TRDT");
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

				invtypValue=invtyp;
				//机构转换
				if("863301".equals(instId)||"863302".equals(instId)||"863304".equals(instId)||"863305".equals(instId)||
						"863306".equals(instId)||"863307".equals(instId)||"863308".equals(instId)||"8633".equals(instId)){
					instId = "863300";
				}
				if("862301".equals(instId)||"862302".equals(instId)||"8623".equals(instId)){
					instId = "862300";
				}
				if("8628".equals(instId)){
					instId = "862800";
				}
				if("866101".equals(instId)||"866102".equals(instId)||"8661".equals(instId)){
					instId = "866100";
				}
				if("8611".equals(instId)){
					instId = "861100";
				}
				if("8641".equals(instId)){
					instId = "864100";
				}
				if("863501".equals(instId)||"863502".equals(instId)||"8635".equals(instId)){
					instId = "863500";
				}
				if("8651".equals(instId)||"865101".equals(instId)||"865102".equals(instId)||"865103".equals(instId)||"865104".equals(instId)||"865105".equals(instId)||
						"865106".equals(instId)||"865107".equals(instId)||"865108".equals(instId)||
							"865109".equals(instId)){
					instId = "865100";
				}
				if("8632".equals(instId)){
					instId = "863200";
				}
				if("8621".equals(instId)){
					instId = "862100";
				}
				if("8614".equals(instId)){
					instId = "861400";
				}
				if("8653".equals(instId)){
					instId = "865300";
				}
				if("8627".equals(instId)){
					instId = "862700";
				}
				if("8636".equals(instId)){
					instId = "863600";
				}
				if("8613".equals(instId)){
					instId = "861300";
				}
				if("8622".equals(instId)){
					instId = "862200";
				}
				if("8631".equals(instId)){
					instId = "863100";
				}
				if("8634".equals(instId)){
					instId = "863400";
				}
				if("8648".equals(instId)){
					instId = "864800";
				}
				if("8637".equals(instId)){
					instId = "863700";
				}
				if("8612".equals(instId)){
					instId = "861200";
				}
				if("8645".equals(instId)){
					instId = "864500";
				}
				//机构的转换 end
				
				System.out.println("*@交易流水号： "+businessId);
				System.out.println("*@业务机构： "+instId);
				System.out.println("*@团个标识： "+qdFlag);
				System.out.println("*@保单号： "+chernum);
				System.out.println("*@业务交易号： "+repnum);
				System.out.println("*@投保单号： "+ttmprcno);
				System.out.println("*@ "+customerName);
				System.out.println("*@客户纳税人识别号： "+customerTaxno);
				System.out.println("*@ "+customerAddressand);
				System.out.println("*@"+taxpayerType);
				System.out.println("*@ "+customerPhone);
				System.out.println("*@ "+customerBankand);
				System.out.println("*@ "+customerAccount);
				System.out.println("*@交易币种：  "+origcurr);
				System.out.println("*@ "+origamt);
				System.out.println("*@ "+acctamt);
				System.out.println("*@交易日期： "+trdt);
				System.out.println("*@ 发票类型"+invtyp);
				System.out.println("*@ "+bustyp);
				System.out.println("*@ "+billfreq);
				System.out.println("*@ "+polyear);
				System.out.println("*@ "+hissdte);
				System.out.println("*@ "+planlongdesc);
				System.out.println("*@ "+instfrom);
				System.out.println("*@ "+instto);
				System.out.println("*@ "+occdate);
				System.out.println("*@ "+premterm);

				
				if("0".equals(invtyp)&&(customerName==null||"".equals(customerName)||customerTaxno==null||"".equals(customerTaxno)
						||customerAddressand==null||"".equals(customerAddressand)||customerPhone==null||"".equals(customerPhone)
						||customerBankand==null||"".equals(customerBankand)||customerAccount==null||"".equals(customerAccount))){
					doc = DocumentHelper.createDocument();
					doc.setXMLEncoding("GBK");
					Element PackageResp = doc.addElement("Package");
					HeaderResp = PackageResp.addElement("Header");
					HeaderResp.addElement("RequestType").addText(RequestType);
					HeaderResp.addElement("UUID").addText(UUID);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					HeaderResp.addElement("SendTime").addText(df.format(new Date()));
					
					HeaderResp.addElement("ResponseCode").addText("N");
					Element ErrorList=HeaderResp.addElement("ErrorList");
					Element ERROR=ErrorList.addElement("ERROR");
					ERROR.addElement("ERROR_ID").addText("error");
					ERROR.addElement("ERRMSG").addText("客户信息不完整，请完善！");
					return doc.asXML();
				}
				
//				if(("0").equals(invtyp)&&(customerTaxno.length()>18||customerTaxno.length()<15)){
//					doc = DocumentHelper.createDocument();
//					doc.setXMLEncoding("GBK");
//					Element PackageResp = doc.addElement("Package");
//					HeaderResp = PackageResp.addElement("Header");
//					HeaderResp.addElement("RequestType").addText(RequestType);
//					HeaderResp.addElement("UUID").addText(UUID);
//					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					HeaderResp.addElement("SendTime").addText(df.format(new Date()));
//					
//					HeaderResp.addElement("ResponseCode").addText("N");
//					Element ErrorList=HeaderResp.addElement("ErrorList");
//					Element ERROR=ErrorList.addElement("ERROR");
//					ERROR.addElement("ERROR_ID").addText("error");
//					ERROR.addElement("ERRMSG").addText("专票客户税号不合法！");
//					return doc.asXML();
//				}
				
				
		        
				VmsTransInfo vmsTransInfo=new VmsTransInfo();
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
				vmsTransInfo.setBustyp(bustyp);
				vmsTransInfo.setBillfreq(billfreq);
				if(polyear!=null&&!"".equals(polyear)){
					vmsTransInfo.setPolyear(Integer.parseInt(polyear));
				}
				vmsTransInfo.setHissdte(hissdte);
				vmsTransInfo.setPlanlongdesc(planlongdesc);
				vmsTransInfo.setInstfrom(instfrom);
				vmsTransInfo.setInstto(instto);
				vmsTransInfo.setOccdate(occdate);
				vmsTransInfo.setCustomerid(customerTaxno);//modify by LQ 20160704 个人客户存放投保人客户信息   团单暂时也去纳税人识别号为客户ID但是团单没有纳税人识别号呢？？？
				if(premterm!=null&&!"".equals(premterm)){
					vmsTransInfo.setPremterm(Integer.parseInt(premterm));
				}
				
				VmsCustomerInfo vmsCustomerInfo=new VmsCustomerInfo();
				vmsCustomerInfo.setCustomerName(customerName);
				vmsCustomerInfo.setCustomerTaxno(customerTaxno);
				if(!"G".equals(qdFlag)){//mofify by LQ 20160704 个人为空 
					vmsCustomerInfo.setCustomerTaxno("");
				}
				vmsCustomerInfo.setCustomerAddressand(customerAddressand);
				vmsCustomerInfo.setTaxpayerType(taxpayerType);
				vmsCustomerInfo.setCustomerPhone(customerPhone);
				vmsCustomerInfo.setCustomerBankand(customerBankand);
				vmsCustomerInfo.setCustomerAccount(customerAccount);
				vmsCustomerInfo.setInvtyp(invtyp);
				//vmsCustomerInfo.setBusinessId(businessId);
				//个险之前为流水号 现在为投保人客户号  modify by LQ 20160704
				if(!"G".equals(qdFlag)){
					vmsCustomerInfo.setBusinessId(customerTaxno);
				}
				vmsCustomerInfo.setBusinessId(customerTaxno);//不分团个都要取纳税人识别号
				Element covListElement=bussInfoElement.element("COVLIST");
				List<Element> covInfoList=covListElement.elements();
				
//				List<CovInfo> covList=new ArrayList<CovInfo>();
				for(Element covInfoElement:covInfoList){
					String insCod=covInfoElement.elementText("INS_COD");
					String transType = null;
					String insNam=covInfoElement.elementText("INS_NAM");
					String feetyp=covInfoElement.elementText("FEETYP");
					String amtCny=covInfoElement.elementText("AMT_CNY");
					String taxAmtCny=covInfoElement.elementText("TAX_AMT_CNY");
					String incomeCny=covInfoElement.elementText("INCOME_CNY");
					String taxRate=covInfoElement.elementText("TAX_RATE");
					
					if("Z".equals(taxRate)||"F".equals(taxRate)){
						invtypValue="1";
					}
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
						throw new Exception();
					}
					
					if("BF".equals(feetyp)&&"S".equals(taxRate)){
						transType="1001";
					}else if("BF".equals(feetyp)&&"Z".equals(taxRate)){
						transType="1002";
					}else if("BF".equals(feetyp)&&"F".equals(taxRate)){
						transType="1002";
					}else if("MF".equals(feetyp)&&"S".equals(taxRate)){
						transType="2001";
					}else if("NF".equals(feetyp)&&"S".equals(taxRate)){
						transType="3001";
					}else if("NF".equals(feetyp)&&"P".equals(taxRate)){
						transType="3002";
					}else if("IF".equals(feetyp)&&"S".equals(taxRate)){
						transType="4001";
					}
					

					System.out.println();
					System.out.println("**费用类型： "+feetyp);
					System.out.println("**税率： "+taxRate);
					System.out.println("**交易流水号： "+businessId);
					System.out.println("**交易日期： "+trdt);
					System.out.println("**交易类型： "+transType);
					System.out.println("**险种代码： "+insCod);
					System.out.println("**险种名称 "+insNam);
					
					vmsTransInfo.setInvtyp(invtypValue);
					vmsTransInfo.setInsCod(insCod);
					vmsTransInfo.setInsNam(insNam);
					vmsTransInfo.setFeetyp(feetyp);
					vmsTransInfo.setAmtCny(Double.parseDouble(amtCny));
					vmsTransInfo.setTaxAmtCny(Double.parseDouble(taxAmtCny));
					vmsTransInfo.setIncomeCny(Double.parseDouble(incomeCny));
					vmsTransInfo.setTaxRate(taxRateValue);
					if(transType==null){
						throw new NullPointerException();
					}else{
						vmsTransInfo.setTransType(transType);
					}
					
					vmsTransInfos.add(vmsTransInfo);
					result=vmsTransInfoDao.insert(vmsTransInfo);
					
					if(!"Y".equals(result))
						break;
					
					VmsTransType vmsTransType=new VmsTransType();
					vmsTransType.setInsCod(transType);
					vmsTransType.setInsNam(insNam);
					
					vmsTransTypes.add(vmsTransType);
//					result=vmsTransTypeDao.insert(vmsTransType);
				}
				vmsCustomerInfo.setInvtyp(invtypValue);
				vmsCustomerInfos.add(vmsCustomerInfo);
				if("Y".equals(result)){
					result=vmsCustomerInfoDao.insertORUpdate(vmsCustomerInfo);
				}else{
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
			
			doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element PackageResp = doc.addElement("Package");
			HeaderResp = PackageResp.addElement("Header");
			HeaderResp.addElement("RequestType").addText(RequestType);
			HeaderResp.addElement("UUID").addText(UUID);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HeaderResp.addElement("SendTime").addText(df.format(new Date()));
			
			if("Y".equals(result)){
				HeaderResp.addElement("ResponseCode").addText("Y");
			}else{
				HeaderResp.addElement("ResponseCode").addText("N");
				Element ErrorList=HeaderResp.addElement("ErrorList");
				Element ERROR=ErrorList.addElement("ERROR");
				ERROR.addElement("ERROR_ID").addText(String.valueOf("error"));
				ERROR.addElement("ERRMSG").addText(result);
			}
			System.out.println("*result="+result);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element PackageResp = doc.addElement("Package");
			HeaderResp = PackageResp.addElement("Header");
			HeaderResp.addElement("RequestType").addText(RequestType);
			HeaderResp.addElement("UUID").addText(UUID);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HeaderResp.addElement("SendTime").addText(df.format(new Date()));
			
			HeaderResp.addElement("ResponseCode").addText("N");
			Element ErrorList=HeaderResp.addElement("ErrorList");
			Element ERROR=ErrorList.addElement("ERROR");
			ERROR.addElement("ERROR_ID").addText("error");
			ERROR.addElement("ERRMSG").addText("请求报文不符合标准，请检查后重试！");
//			e.printStackTrace();
			return doc.asXML();
		}
	
		return doc.asXML();
	}

}
