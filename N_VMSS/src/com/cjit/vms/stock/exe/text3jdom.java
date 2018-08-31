package com.cjit.vms.stock.exe;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class text3jdom {
	public static final String business_cH = "business"; // 身体
	public static final String id_cH = "id"; // 身体
	public static final String comment_cH = "comment"; //

	protected String Id; // 交易编号
	protected String comment;// 交易描述

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static void main(String[] args) {
		// jdomff();

	}

	/**
	 * jdom生成xml
	 */
	private static void jdomff() {
		Element root = new Element(business_cH).setAttribute(id_cH, "10009").setAttribute(comment_cH, "发票作废");
		Document document = new Document(root);
		Element elements = new Element("body").setAttribute("yylxdm", "1");
		elements.addContent(new Element("returncode").setText("返回代码"));
		elements.addContent(new Element("returnmsg").setText("返回信息"));
		Element element = new Element("returndata");
		element.addContent(new Element("fpdm").setText("发票代码"));
		element.addContent(new Element("fphm").setText("发票号码"));
		element.addContent(new Element("zfrq").setText("作废日期"));
		elements.addContent(element);
		root.addContent(elements);
		String xml = "";
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xml = xmlOutputter.outputString(root);
		System.out.println(xml);
	}
}
