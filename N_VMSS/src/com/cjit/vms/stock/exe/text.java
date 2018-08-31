package com.cjit.vms.stock.exe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class text {

	public static void main(String[] args) {
		//date1(); 81964000
		//calender(); 2017-02-07 14:13:56  -4436000
		
//		String dats="2017-01-06 14:13:56";
//		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			Date date=sf.parse(dats);
//			Date zt=new Date(date.getTime()-24*60*60*1000);
//			Date mt=new Date(date.getTime()+24*60*60*1000);
//			System.out.println(sf.format(zt)+"\tzt");
//			System.out.println(sf.format(mt)+"\tmt");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		calend2(dats, sf);
//		Calendar calendar=Calendar.getInstance();
//		System.out.println(calendar.get(Calendar.MONDAY));
		TimeUnit timeUnit=TimeUnit.HOURS;
		System.out.println(timeUnit);
	}

	/**
	 * @param dats
	 * @param sf
	 */
	private static void calend2(String dats, SimpleDateFormat sf) {
		try {
			Date date=sf.parse(dats);//2017-02-06 14:13:56
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			Calendar currentdate=Calendar.getInstance();
			calendar.set(Calendar.YEAR, currentdate.get(Calendar.YEAR));
			calendar.set(Calendar.MONTH, currentdate.get(Calendar.MONTH));
			calendar.set(Calendar.DAY_OF_MONTH, currentdate.get(Calendar.DAY_OF_MONTH));
			date=calendar.getTime();
			System.out.println(sf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	} 

	/**
	 * 
	 */
	private static void calender() {
		String dats="2017-02-06 14:13:56";
		String curent="2017-02-07 13:00:00";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date sdate=sf.parse(dats);
			Calendar scalendar=Calendar.getInstance();
			scalendar.setTime(sdate);
			Calendar calendar=Calendar.getInstance();
			scalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			scalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
			scalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			sdate=scalendar.getTime();
			System.out.println(sf.format(sdate));
			System.out.println(sf.parse(curent).getTime()-sdate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static void date1() {
		String dats="2017-02-06 14:13:56";
		String curent="2017-02-07 13:00:00";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date sdate=sf.parse(dats);
			Date current=sf.parse(curent);
			System.out.println(current.getTime()-sdate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前cpu方法
	 */
	private static void dom1() {
		System.out.println(Runtime.getRuntime().toString());
		Runtime.getRuntime().availableProcessors();// 获取的到当前计算机的cpu个数
	}
}
