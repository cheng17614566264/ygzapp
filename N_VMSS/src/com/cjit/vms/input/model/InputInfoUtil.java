package com.cjit.vms.input.model;

import java.util.HashMap;
import java.util.Map;

public class InputInfoUtil {
	/**
	 * 发票状态
	 */
	public static final Map<String, String> billStatuMap;
	/**
	 * 发票类型
	 */
	public static final Map<String, String> billTypeMap ;
	/**
	 * 发票用途
	 */
	public static final Map<String, String> purposeMap ;
	/**
	 * 转出原因
	 */
	public static final Map<String, String> remarkMap ;
	/**
	 * 是否抵免
	 */
	public static final Map<String, String> isCreditMap ;
	
	/**
	 * 转出状态
	 */
	public static final Map<String, String> rollOutStatusMap ;
	static{
		billStatuMap=new HashMap<String, String>();
		billStatuMap.put("1", "已认证");
		billStatuMap.put("0", "未认证");
		billStatuMap.put("2", "无需认证");
		billStatuMap.put("3", "认证不通过");
		
		billTypeMap=new HashMap<String, String>();
		billTypeMap.put("0", "增值税专用发票");
		billTypeMap.put("1", "增值税普通发票");
		billTypeMap.put("2", "通行费发票");
		
		purposeMap=new HashMap<String, String>();
		purposeMap.put("i01", "有形动产租赁");
		purposeMap.put("i02", "不动产租赁");
		purposeMap.put("i03", "运输服务");
		purposeMap.put("i04", "电信服务");
		purposeMap.put("i05", "建筑安装服务");
		purposeMap.put("i06", "金融保险服务");
		purposeMap.put("i07", "生活服务");
		purposeMap.put("i08", "取得无形资产");
		purposeMap.put("i09", "货物及加工、修理修配劳务");
		purposeMap.put("i10", "受让土地使用权");
		purposeMap.put("i11", "通行费");
		purposeMap.put("i12", "固定资产（动产）");
		purposeMap.put("i13", "其他");
		
		remarkMap=new HashMap<String, String>();
		remarkMap.put("a", "免税项目用");
		remarkMap.put("b", "集体福利、个人消费");
		remarkMap.put("c", "非正常损失");
		remarkMap.put("d", "简易计税方法征税项目用");
		remarkMap.put("e", "免抵退税办法不得抵扣的进项税额");
		remarkMap.put("f", "纳税检查调减进项税额");
		remarkMap.put("g", "红字专用发票信息表注明的进项税额");
		remarkMap.put("h", "上期留抵税额抵减欠税");
		remarkMap.put("i", "上期留抵税额退税");
		remarkMap.put("j", "其他应作进项税额转出的情形");
		
		isCreditMap=new HashMap<String, String>();
		isCreditMap.put("0", "全部抵扣");
		isCreditMap.put("1", "部分抵扣");
		isCreditMap.put("2", "不可抵扣");
		
		rollOutStatusMap=new HashMap<String, String>();
		rollOutStatusMap.put("0", "未转出");
		rollOutStatusMap.put("1", "已转出");
	}
}
