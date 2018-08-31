package com.cjit.ws.service.request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cjit.common.action.BaseAction;


public class ReqCore extends BaseAction{
	String ids;
	
	
//	String ids= (String) this.request.getAttribute("ids");;
	
	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String demo() throws IOException{
		String result = "Helloooo";
//		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(JSONObject.fromObject(result).toString());
		out.close();
		System.out.println("!@#!$");
		return "AAA";
	}

}
