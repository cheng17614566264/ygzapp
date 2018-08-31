package com.cjit.ws.dao;

import com.cjit.ws.entity.VmsCustomerInfo;


public interface VmsCustomerInfoDao{
	public String insert(VmsCustomerInfo vmsCustomerInfo) throws Exception;

	public String insertORUpdate(VmsCustomerInfo vmsCustomerInfo);

	public boolean existSameCustomerByCustomerTaxno(String customerId);
}
