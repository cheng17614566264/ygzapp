package com.cjit.webService.client.Util;

import java.util.HashMap;
import java.util.Map;

public class WebServiceUtil {
	/**
	 * 核心webservice路径
	 */
	public static final String HEXIN_SERVICE_NAME = "hexin";
	/**
	 * 发票已开具
	 */
	public static final String BILL_ISSUE = "1";
	/**
	 * 发票已作废
	 */
	public static final String BILL_CANCEL = "2";
	/**
	 * 发票已红冲
	 */
	public static final String BILL_RED="3";
	/**
	 * 增值税专用发票
	 */
	public static final String BILL_EXCLUSIVE="0";
	/**
	 * 增值税普通发票
	 */
	public static final String BILL_ORDINARY="1";
	/**
	 * 团险
	 */
	public static final String GROUP_INSURANCE="1";
	/**
	 * 个险
	 */
	public static final String  A_RISK="2";
	/**
	 * 合并开票
	 */
	public static final String MERGE_BILL="2";
	/**
	 * 纳税人识别号长度
	 */
	public static final Map<Integer, Integer> NSR_NO_MAP;
	
	//vmss连接vms的地址名字
	public static final String urlVmsName="vms";
	static{
		NSR_NO_MAP=new HashMap<Integer, Integer>();
		NSR_NO_MAP.put(15, 1);
		NSR_NO_MAP.put(18, 1);
		NSR_NO_MAP.put(20, 1);
	}
}
