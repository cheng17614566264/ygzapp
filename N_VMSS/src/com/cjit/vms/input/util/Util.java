package com.cjit.vms.input.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Util {

	public static final String  datasource_0="0";
	public static final String  datasource_0_CH="比例计算";
	public static final String  datasource_1="1";
	public static final String  datasource_1_CH="手工调整";
	// 2018-0427 优化
	public static final String datasource_2 = "2";
	public static final String datasource_2_CH = "尚未调整";
	
	public static String getDatasource(String datasource){
		if(datasource!=null&&datasource!=" "){
			if(datasource_0.equals(datasource)){
				return datasource_0_CH;
			}else if(datasource_1.equals(datasource)){
				return datasource_1_CH;
			}else{
				return datasource_2_CH;
			}
		}else{
			return datasource_2_CH;
		}
	}
	// 0-无效  1-有效 2-待审核 3-审核拒绝
	public static final String available_0="0";
	public static final String available_0_CH="无效";
	public static final String available_1="1";
	public static final String available_1_CH="有效";
	public static final String available_2="2";
	public static final String available_2_CH="待审核";
	public static final String available_3="3";
	public static final String available_3_CH="审核拒绝";
	
	public static String getavailableCH(String str){
		if(str!=null&& str!=" "){
			if(available_0.equals(str)){
				return available_0_CH;
			}else if(available_1.equals(str)){
				return available_1_CH;
			}else if(available_2.equals(str)){
				return available_2_CH;
			}else if(available_3.equals(str)){
				return available_3_CH;
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	public static List<String> getMonthlist(String minDate ,String maxDate) throws ParseException{
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMM");
		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.add(Calendar.MONTH, -1);//2018-07-20新增，向前移一个月
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
		System.out.println(sFormat.format(min.getTime()));

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sFormat.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}
		result.remove(result.size()-1);
		return result;
	}
	
	public static void main(String[] args) {
		try {
			List<String> list=getMonthlist("2017-08-01", "2018-07-20");
			for(int i=list.size()-1;i>=0;i--){
				System.out.println(list.get(i));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
