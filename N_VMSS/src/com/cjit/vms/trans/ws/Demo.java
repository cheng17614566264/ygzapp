package com.cjit.vms.trans.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(serviceName="demo")
@SOAPBinding(style=Style.RPC)
public class Demo {
	@WebMethod
	public String demo1(){
		return "wecodfj demo1";
	}
	
	public String demo2(String name){
		return "demo "+name;
	}
}
