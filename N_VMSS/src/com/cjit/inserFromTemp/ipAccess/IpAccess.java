package com.cjit.inserFromTemp.ipAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericDatacoreServiceImpl;

public class IpAccess extends GenericDatacoreServiceImpl{
	
	public List selectCustomerIp(String linkName){
		/*Map map=new HashMap();
		map.put("linkName", linkName);*/
		
		System.out.println(linkName);
		return find("vms.selectCustomerIp",linkName);
	}
	
}
