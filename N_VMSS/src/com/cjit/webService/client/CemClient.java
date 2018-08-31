package com.cjit.webService.client;


import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.cjit.webService.client.service.cemreceiverserver.CemReceiver_CType;
import com.cjit.webService.client.service.cemreceiverserver._1_0.CemReceiver;
import com.cjit.webService.client.service.cemreceiverserver._1_0.CemReceiverResponse;
import com.cjit.webService.client.service.cemreceiverserver._1_0.CemReceiverServerStub;


public class CemClient {
	private String webserviceUrl;
	public CemClient() {
	}
	public CemClient(String webserviceUrl){
		this.webserviceUrl=webserviceUrl;
	}
	public String receiver(String serverName,String methodName,String data){
		try {
			CemReceiverServerStub stub=new CemReceiverServerStub(webserviceUrl);
			CemReceiver cemReceiver=new CemReceiver();
			CemReceiver_CType cType=new CemReceiver_CType();
			cType.setCemServerName(serverName);
			cType.setCemMethod(methodName);
			cType.setCemIn(data);
			cemReceiver.setCemReceiver(cType);
			CemReceiverResponse cemReceiverResponse=stub.cemReceiver(cemReceiver);
			return cemReceiverResponse.getCemReceiverResponse().getCemOut();
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getWebserviceUrl() {
		return webserviceUrl;
	}
	public void setWebserviceUrl(String webserviceUrl) {
		this.webserviceUrl = webserviceUrl;
	}
	
}
