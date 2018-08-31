package com.cjit.ws.service.impl;

import com.cjit.ws.service.VMSTransService;

public class VMSWebServiceImpl {

	private VMSTransService vmsTransService;

	public VMSTransService getVmsTransService() {
		return vmsTransService;
	}

	public void setVmsTransService(VMSTransService vmsTransService) {
		this.vmsTransService = vmsTransService;
	}

	public String transService(String xml){
		return vmsTransService.transService(xml);
	}
	
}
