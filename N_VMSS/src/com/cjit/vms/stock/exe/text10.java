package com.cjit.vms.stock.exe;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjit.vms.stock.exe.entity.InputInfoy;
import com.cjit.vms.stock.exe.entity.ParasJson;
import com.cjit.vms.stock.exe.entity.response.ResponseBody;
import com.cjit.vms.stock.exe.entity.response.ResponseHeader;
import com.cjit.vms.stock.exe.entity.response.ResponseJg;
import com.google.gson.Gson;

public class text10 {

	public static void main(String[] args) {
		//System.out.println(paraString("{'body':{'inputInfoList':{'amt':10,'available':'1','billCode':'22331144','billDate':'2017-03-16','billId':'3123123111','billType':'0','curreny':'RMB','id':'BI2017032300000895','isCredit':'1','name':'1223','purpose':'i07','shareInst':'200011','sumAmt':11.3,'tax':1.3,'taxNo':'3213213213','taxRate':0.13}},'header':{'requestType':'0001','time':'2017-03-23 17:01:26'}}"));
	
	  rrrr rrrr=new rrrr();
	  rrrr.setAa("ii");
	  rrrr.setAge(13);
	  
	  rrrr.setAge(15);
	  System.out.println(rrrr.toString());
	  
	}
	
	public static String paraString(String pString){
		
		Gson sGson=new Gson();
		ParasJson parasJson=sGson.fromJson(pString, ParasJson.class);
		if("00012".equals(parasJson.getHeader().getRequestType())){
			InputInfoy infoy=parasJson.getBody().getInputInfoList();
			return infoy.toString();
		}else{
			return sbjg("异常");
		}
	}
	
	private static String sbjg(String string){
		ResponseHeader header=new ResponseHeader();
		header.setReslut("shibai");
		header.setMassage(string);
		ResponseBody body=new ResponseBody();
		body.setRequestType("0001");
		body.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ResponseJg rJg=new ResponseJg(body,header);
		Gson gson=new Gson();
		return gson.toJson(rJg);
	}
	
	
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
}
