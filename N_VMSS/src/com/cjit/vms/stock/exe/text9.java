package com.cjit.vms.stock.exe;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjit.webService.server.entity.InputInfoData;
import com.cjit.webService.server.entity.ResponseBody;
import com.cjit.webService.server.entity.ResponseHeader;
import com.cjit.webService.server.entity.ResponseWsdl;
import com.cjit.webService.server.entity.RollOutInfo;
import com.cjit.webService.server.util.DataUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class text9 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		 String json=dom1();
//		 System.out.println(json);
//		String json =dom2();
		Gson gson=new Gson();
		tax tax=gson.fromJson(json, tax.class);
//		if(backR ==null){
//			System.out.println("kong");
//		}else{
//			System.out.println("bill : "+backR.getBill()+"\nbillCode : "+backR.getBillCode()+"\nbillDate : "+backR.getBillDate());
//		}
//		 cshi();
		System.out.println("!");
	}

	/**
	 * @throws JsonSyntaxException
	 */
	private static String cshi() throws JsonSyntaxException {
		Gson gson = new Gson();
		String inputInfoJson="";
		InputInfoData inputInfoData = gson.fromJson(inputInfoJson, InputInfoData.class);
		if (inputInfoData == null) {
			return  faultWsdl("", "报文信息为空");
		}
		if (DataUtil.INPUT_DATE_REQUEST_TYPE_1.equals(inputInfoData.getHeader().getRequestType())) {
			String message = inputInfo(inputInfoData);
			if (DataUtil.RESPONSE_SUCCESS.equals(message)) {
				return  successWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_1);
			} else {
				return  faultWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_1, message);
			}

		} else {
			return  faultWsdl(inputInfoData.getHeader().getRequestType(), "请求类型不存在");
		}
	}

	/**
	 * 
	 */
	private static String dom1() {
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonObject.put("tax", jsonArray);
		for(int i=0;i<1;i++){
			JSONObject bill=new JSONObject();
			bill.put("bill", "bill"+i);
			bill.put("billCode", "billCode"+i);
			bill.put("billDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(new Date()));
			jsonArray.add(bill);
		}
		return jsonObject.toJSONString();
	}
	
	private static String dom2(){
		
		JSONObject jsonObject =new JSONObject();
		jsonObject.put("bill", "1");
		jsonObject.put("billCode", "2");
		jsonObject.put("billDate", "2017-04-01 09:12:34");
		
		return jsonObject.toJSONString();
	}
	
	private static String faultWsdl(String requestType, String message) {
		ResponseHeader header = new ResponseHeader();
		header.setResponseType(requestType);
		header.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ResponseBody body = new ResponseBody();
		body.setResult(DataUtil.RESPONSE_FAULT);
		body.setMessage(message);
		ResponseWsdl responseWsdl = new ResponseWsdl(header, body);
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(responseWsdl);
	}
	
	private static String successWsdl(String requestType) {
		ResponseHeader header = new ResponseHeader();
		header.setResponseType(requestType);
		header.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ResponseBody body = new ResponseBody();
		body.setResult(DataUtil.RESPONSE_SUCCESS);
		body.setMessage("");
		ResponseWsdl responseWsdl = new ResponseWsdl(header, body);
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(responseWsdl);
	}
	
	private static String inputInfo(InputInfoData inputInfoData){
		return "success";
	}
}
