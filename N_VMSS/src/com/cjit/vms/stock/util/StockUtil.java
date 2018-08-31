package com.cjit.vms.stock.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cjit.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import org.quartz.listeners.FilterAndBroadcastJobListener;

public class StockUtil {
	/**
	 * 接收未确认
	 */
	public static final String JS_ENTENER_NO="0";
	/**
	 * 接收已确认
	 */
	public static final String JS_ENTENER_YES="1";
	
	public static String getDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String Sdate=sdf.format(date);
		return Sdate;
	}

	/**
	 * 发票接收确认MAP
	 */
	public static final Map<String, String> JS_ENTER_MAP;
	static{
		JS_ENTER_MAP=new HashMap<String, String>();
		JS_ENTER_MAP.put("0", "未确认");
		JS_ENTER_MAP.put("1", "已确认");
	}
	
	/**
	 *  发票状态： 遗失 回收  
	 */
	public static final String FLAG_0="0";
	public static final String FLAG_0_CH="空白发票遗失";
	
	public static final String FLAG_1="1";
	public static final String FLAG_1_CH="空白发票回收";
	
	public static final String FLAG_2="2";
	public static final String FLAG_2_CH="空白发票作废";
	public static String getflag(String flag){
		if (FLAG_0.equals(flag)) {
			return FLAG_0_CH;
		}else if(FLAG_1.equals(flag)){
			return FLAG_1_CH;
		}else if(FLAG_2.equals(flag)){
			return FLAG_2_CH ;
		}else{
			return "";
		}
	}
	
	/**
	 *  发票确认 ：是否确认 0:未确认 1:已确认
	 */
	public static final String STATE_0="0";
	public static final String STATE_0_CH="未确认";
	
	public static final String  STATE_1="1";
	public static final String STATE_1_CH="已确认";
	
	public static String getstate(String state){
		if (STATE_0.equals(state)) {
			return STATE_0_CH;
		}else if(STATE_1.equals(state)){
			return STATE_1_CH;
		}
		return "";
	}
	
	/**
	 * 未回收
	 */
	public static final String PRINT_BILL_RECYCLE_NO="0";
	/**
	 * 已回收
	 */
	public static final String PRINT_BILL_RECYCLE_YES="1";
	/**
	 * 已打印发票回收状态MAP
	 */
	public static final Map<String, String> PRINT_BILL_RECYCLE_MAP;
	static{
		PRINT_BILL_RECYCLE_MAP=new HashMap<String, String>();
		PRINT_BILL_RECYCLE_MAP.put(null, "未回收");
		PRINT_BILL_RECYCLE_MAP.put("0", "未回收");
		PRINT_BILL_RECYCLE_MAP.put("1", "已回收");
	}
	/**
	 * 票据修改
	 * @param count
	 * @param sycount
	 * @return
	 */
	public static String getvalue(String count,String sycount){
		if ((int)Integer.valueOf(count)>(int)Integer.valueOf(sycount)) {
			return "Y";
		}else{
			return "N";
		}
	}
}
