package com.cjit.ws.utils;


import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class Axis2Client4CXF {
	public String requestWebservice(String serviceUrl,String method,String qName,String param) throws MalformedURLException, ServiceException, RemoteException{
        Service service = new Service();  
        Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(serviceUrl));  
	    call.setOperationName(method);// WSDL里面描述的操作名称  
	    call.addParameter("xml",  
	                    org.apache.axis.encoding.XMLType.XSD_STRING,  
	                    javax.xml.rpc.ParameterMode.IN);// 操作的参数  

	    call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型  
	    call.setUseSOAPAction(true);
	    
         
	    QName opName = new QName(qName, method);
        Object[] obj = new Object[] { param };  
        String result = (String) call.invoke(opName,obj);  

		return result;
	}
}
