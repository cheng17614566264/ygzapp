package com.cjit.vms.trans.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class JSPUtil {
	/**
	 * 判断是否在犹豫期内
	 * @param hesitatePeriod
	 * @return
	 */
	public static String isInHesitatePeriod(String hesitatePeriod){
		String time = (hesitatePeriod == null) ? null : hesitatePeriod.toString();
		String value = "否";
	 	Date date = new Date();
	 	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	 	if (time != null) {
	 		if (time.compareTo(sf.format(date)) >= 0) {
	 			value = "是";
	 		}
	 	}
		return value;
	}
	/**
	 * 是否为预开票
	 * @param isYK
	 * @return
	 */
	public static String isYK(String isYK){
		return DataUtil.isYK.get(isYK);
	}
	
	public static String getValue(Map<String, String> map,String key){
		return map.get(key);
	}
}
