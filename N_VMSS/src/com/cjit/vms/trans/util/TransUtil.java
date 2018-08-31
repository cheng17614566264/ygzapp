package com.cjit.vms.trans.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cjit.webService.client.Util.WebServiceUtil;
import com.cjit.webService.client.entity.Cover;
import com.cjit.webService.client.entity.Customer;
import com.cjit.webService.client.entity.Policy;

/**
 * 交易信息校验类
 */
public class TransUtil {
	/**
	 * 检查数据完整性
	 * 
	 * @param list
	 * @return
	 */
	public static String checkTransInfo(List<Policy> list) {
		String message = "ERROR";
		for (Policy policy : list) {
			Cover cover = policy.getCover();
			Customer customer = policy.getCustomer();
			if (StringUtils.isBlank(policy.getQdFlag()) || StringUtils.isBlank(policy.getInstId())
					|| StringUtils.isBlank(policy.getRepnum()) || StringUtils.isBlank(policy.getTtmprcno())
					|| policy.getCustomer() == null || StringUtils.isBlank(policy.getBatchNo())
					|| StringUtils.isBlank(policy.getInvtyp()) || StringUtils.isBlank(policy.getHesitatePeriod())
					|| policy.getCover() == null) {

				return message;
			}
			if (policy.getIsYK().equals("1")) {
				if (StringUtils.isBlank(policy.getChernum())) {
					return message;
				}
			}
			if (StringUtils.isBlank(customer.getCustomerName())) {
				return message;
			}
			if (StringUtils.isBlank(cover.getTranstype()) || StringUtils.isBlank(cover.getInsCod())
					|| cover.getAmtCny() == null || cover.getTaxAmtCny() == null || cover.getIncomeCny() == null) {
				return message;
			}
			if (policy.getInvtyp().equals(WebServiceUtil.BILL_EXCLUSIVE)) {
				if (StringUtils.isBlank(customer.getCustomerTaxno())
						|| StringUtils.isBlank(customer.getCustomerAddressand())
						|| StringUtils.isBlank(customer.getTaxpayerType())
						|| StringUtils.isBlank(customer.getCustomerAddressand())
						|| StringUtils.isBlank(customer.getCustomerPhone())
						|| StringUtils.isBlank(customer.getCustomerBankand())
						|| StringUtils.isBlank(customer.getCustomerAccount())
						|| StringUtils.isBlank(customer.getCustomerFapiaoFlag())) {
					return message;
				}
				if (WebServiceUtil.NSR_NO_MAP.get(customer.getCustomerTaxno().length()) == null) {
					return message;
				}
			}
		}
		return "TRUE";
	}
}
