package com.cjit.vms.trans.service.createBill;

import java.util.List;
import java.util.Set;

import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.webService.client.entity.Customer;
import com.cjit.webService.client.entity.Policy;

public interface BillValidationService {

	public CheckResult shortCircuitValidation(List transInfoList) ;
	public List<CheckResult> validation(List transInfoList);
	public List<CheckResult> validationAll(List transInfoList);
	public List checkingTransByCherNum(TransInfo transInfo, boolean flag);
	public CheckResult checkMaxAmt(TransInfo transInfo);
	public List findTransInfoListByTransId(String[] selectTransId);
	/**
	 * 从核心系统同步开票申请
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public void synchTransInfo(List<Policy> list);
	/**
	 * 保存客户信息
	 * @param list
	 */
	public void saveCustomer(Set<Customer> set);
	/**
	 * 查找webService的url
	 * @param serviceName
	 * @return
	 */
	public List<String> findWebServiceUrl(String serviceName);
	
	//查找Vmss与Vms的连接地址
	public List<String> findVmsUrl(String urlName);
}
