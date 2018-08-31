package com.sinosoft.ws.job.client.callservice;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Main2 {
	public static void main(String[] args) {
		String xml = "" + "<?xml version=\"1.0\" encoding=\"GBK\"?>  "
				+ "<Package> " + "<Header> "
				+ "<RequestType>LIS_DocumentPrint</RequestType> "
				+ "<UUID>c0d258b4-eb69-427a-b8a9-9cb8ea0de86a</UUID> "
				+ "<SendTime>2016-05-19 14:36:36</SendTime> " + "</Header> "
				+ "<Request> " + "<BUSSLIST> " + "<BUSSINFO> "
				+ "<BUSINESS_ID>200000000054</BUSINESS_ID> "
				+ "<INST_ID>861100</INST_ID> " + "<QD_FLAG>I</QD_FLAG> "
				+ "<CHERNUM>W11150945204</CHERNUM> "
				+ "<REPNUM>86110020160320000815</REPNUM> "
				+ "<TTMPRCNO>W030012015646260</TTMPRCNO> "
				+ "<CUSTOMER_NAME>电子保单1</CUSTOMER_NAME> " + "<CUSTOMER_TAXNO> "
				+ "</CUSTOMER_TAXNO> " + "<CUSTOMER_ADDRESSAND> "
				+ "</CUSTOMER_ADDRESSAND> "
				+ "<TAXPAYER_TYPE>O</TAXPAYER_TYPE> " + "<CUSTOMER_PHONE> "
				+ "</CUSTOMER_PHONE> " + "<CUSTOMER_BANKAND> "
				+ "</CUSTOMER_BANKAND> " + "<CUSTOMER_ACCOUNT>  "
				+ "</CUSTOMER_ACCOUNT> " + "<ORIGCURR>CNY</ORIGCURR> "
				+ "<ORIGAMT>10000.0</ORIGAMT>   "
				+ "<ACCTAMT>10000.0</ACCTAMT> " + "<TRDT>2016-12-30</TRDT> "
				+ "<INVTYP>1</INVTYP> " + "<BUSTYP>A</BUSTYP> "
				+ "<BILLFREQ>E</BILLFREQ> " + "<POLYEAR> " + "</POLYEAR> "
				+ "<HISSDTE>2016-12-30</HISSDTE> " + "<PLANLONGDESC> "
				+ "</PLANLONGDESC> " + "<INSTFROM> " + "</INSTFROM> "
				+ "<INSTTO> " + "</INSTTO> " + "<OCCDATE>2015-09-16</OCCDATE> "
				+ "<PREMTERM> " + "</PREMTERM> " + "<COVLIST> " + "<COVINFO> "
				+ "<INS_COD>NPV005</INS_COD> "
				+ "<INS_NAM>光大永明光明财富2号B款年金保险（投资连结型）</INS_NAM> "
				+ "<FEETYP>BF</FEETYP> " + "<AMT_CNY>10000</AMT_CNY> "
				+ "<TAX_AMT_CNY>10000</TAX_AMT_CNY> "
				+ "<INCOME_CNY>0</INCOME_CNY> " + "<TAX_RATE>F</TAX_RATE> "
				+ "</COVINFO> " + "</COVLIST> " + "</BUSSINFO> "
				+ "</BUSSLIST> " + "</Request> " + "</Package> ";
		String wsdl = "http://10.156.14.92:7003/services/CallService?wsdl";
		CallServiceFactory.setURL(wsdl);
		CallServiceFactory factory = new CallServiceFactory();
		CallService service = factory.getCallServicePort();

		String result = service.invoke("InvoiceBack", "Invoice", "Password",
				new Main2().releaseXML(), null);
		System.out.println(result);
	}
	
	public static String releaseXML() {
		SAXReader reader = new SAXReader();
		Document document;
		String documentStr = null;
		try {
			document = reader.read(new File("f:/return.xml"));
			documentStr = document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return documentStr;
	}
}
