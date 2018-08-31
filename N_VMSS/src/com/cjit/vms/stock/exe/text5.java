package com.cjit.vms.stock.exe;

import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class text5 {

	public static void main(String[] args) {
		String str="";
		String massage="";
		Element rElement=DocumentHelper.createElement("bussinss");
		Document document=DocumentHelper.createDocument(rElement);
		rElement.addAttribute("id",str).addAttribute("massage", massage);
		
		Element chlid1=rElement.addElement("bodye");
		Element childdm=chlid1.addElement("fpdm").addAttribute("id", "1").addAttribute("value", "1");
		childdm.setText("发票代码");
		Element childno=chlid1.addElement("fphm");
		childno.setText("发票号码");
		Element childlx=chlid1.addElement("fplx");
		childlx.setText("发票类型");
		
		String xml=document.asXML();
		System.out.println(xml);
		//================
		OutputFormat outputFormat=new OutputFormat(null, true);
		outputFormat.setEncoding("utf-8");
		try {
			XMLWriter xmlWriter=new XMLWriter(new FileOutputStream(new File("F://20170209.xml")), outputFormat);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
}
