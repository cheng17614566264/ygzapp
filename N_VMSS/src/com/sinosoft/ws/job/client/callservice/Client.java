package com.sinosoft.ws.job.client.callservice;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Client {
	public static void main(String[] args) {
		Client ma=new Client();
		String res=ma.invoke(ma.releaseXML());
		System.out.println(res);
	}
	
	public String invoke(String xml){
		String wsdl="http://10.156.14.92:7003/services/CallService?wsdl";
		CallServiceFactory.setURL(wsdl);
		CallServiceFactory factory = new CallServiceFactory();
		CallService service = factory.getCallServicePort();
		String result = service.invoke("InvoiceBack", "Invoice", "Password", xml, null);
		return result;
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
	
	public void parseXML(Element e) {
		List<Element> list = e.elements();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTextTrim() != null
					&& !"".equals(list.get(i).getTextTrim())) {
				System.out.println("---" + list.get(i).getName() + " = "+ list.get(i).getText());
			} else {
				System.out.println(list.get(i).getName());
			}
			parseXML(list.get(i));
		}
	}
	
}
