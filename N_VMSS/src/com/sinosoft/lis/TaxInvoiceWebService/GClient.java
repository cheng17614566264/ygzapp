package com.sinosoft.lis.TaxInvoiceWebService;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class GClient {
	public static void main(String[] args) throws MalformedURLException, RemoteException, ServiceException {
		GClient gClient=new GClient();
		gClient.invoke("");
	}
	public String invoke(String xml){
		TaxInvoiceWebServicePortServiceLocator vsl=new TaxInvoiceWebServicePortServiceLocator();
		TaxInvoiceWebServicePort vms;
		String res =null;
		try {
			vms = vsl.getTaxInvoiceWebService(new URL("http://slebgi.sleb.cn/services/TaxInvoiceWebService?wsdl"));
			res = vms.transBillNo(xml);
			System.out.println(res);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return res;
	}
}
