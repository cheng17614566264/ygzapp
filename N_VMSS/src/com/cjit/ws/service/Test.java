package com.cjit.ws.service;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Test {
	public static void main(String[] args) {
		new Test().parse("e:/buss.xml");
	}
	
	public void parse(String xml){
		SAXReader reader = new SAXReader();
		Document document=null;
		try {
			document = reader.read(new File(xml));
			Element Package=document.getRootElement();
			Element Header=Package.element("Header");
			Element Request=Package.element("Request");
			Element RequestTypeElement=Header.element("RequestType");
			Element UUIDElement=Header.element("UUID");
			Element SendTimeElement=Header.element("SendTime");

			String RequestType=RequestTypeElement.getText();
			String UUID=UUIDElement.getText();
			String SendTime=SendTimeElement.getText();
			
			System.out.println(RequestType);
			System.out.println(UUID);
			System.out.println(SendTime);
			
			Element bussList=Request.element("BUSSLIST");
			List<Element> bussInfoList=bussList.elements();
			for(Element bussInfo:bussInfoList){
				String businessId=bussInfo.elementText("BUSINESS_ID");
				String instId=bussInfo.elementText("INST_ID");
				String qdFlag=bussInfo.elementText("QD_FLAG");
				String chernum=bussInfo.elementText("CHERNUM");
				String repnum=bussInfo.elementText("REPNUM");
				String ttmprcno=bussInfo.elementText("TTMPRCNO");
				String customerName=bussInfo.elementText("CUSTOMER_NAME");
				String customerTaxno=bussInfo.elementText("CUSTOMER_TAXNO");
				String customerAddressand=bussInfo.elementText("CUSTOMER_ADDRESSAND");
				String taxpayerType=bussInfo.elementText("TAXPAYER_TYPE");
				String customerPhone=bussInfo.elementText("CUSTOMER_PHONE");
				String customerBankand=bussInfo.elementText("CUSTOMER_BANKAND");
				String customerAccount=bussInfo.elementText("CUSTOMER_ACCOUNT");
				String origcurr=bussInfo.elementText("ORIGCURR");
				String origamt=bussInfo.elementText("ORIGAMT");
				String acctamt=bussInfo.elementText("ACCTAMT");
				String trdt=bussInfo.elementText("TRDT");
				String invtyp=bussInfo.elementText("INVTYP");
				String bustyp=bussInfo.elementText("BUSTYP");
				String billfreq=bussInfo.elementText("BILLFREQ");
				String polyear=bussInfo.elementText("POLYEAR");
				String hissdte=bussInfo.elementText("HISSDTE");
				String planlongdesc=bussInfo.elementText("PLANLONGDESC");
				String instfrom=bussInfo.elementText("INSTFROM");
				String instto=bussInfo.elementText("INSTTO");
				String occdate=bussInfo.elementText("OCCDATE");
				String premterm=bussInfo.elementText("PREMTERM");
				
				System.out.println();
				System.out.println(businessId);
				System.out.println(instId);
				System.out.println(qdFlag);
				System.out.println(chernum);
				
				Element covList=bussInfo.element("COVLIST");
				List<Element> covInfoList=covList.elements();
				for(Element covInfo:covInfoList){
					String insCod=covInfo.elementText("INS_COD");
					String insNam=covInfo.elementText("INS_NAM");
					String feetyp=covInfo.elementText("FEETYP");
					String amtCny=covInfo.elementText("AMT_CNY");
					String taxAmtCny=covInfo.elementText("TAX_AMT_CNY");
					String incomeCny=covInfo.elementText("INCOME_CNY");
					String taxRate=covInfo.elementText("TAX_RATE");
					
					System.out.println();
					System.out.println(insCod);
					System.out.println(insNam);
					System.out.println(feetyp);
					System.out.println(amtCny);
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
