package com.cjit.vms.input.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentPeriod {
	private Calendar begin;
	private Calendar end;
	private Calendar currenttime;
	
	public static String day = "08";
	public static String hour = "12";
	public static String minutes = "30";

	public CurrentPeriod(Calendar currenttime) throws ParseException {
		init(currenttime);
	}
	
	public CurrentPeriod(Date currenttime) throws ParseException {
		Calendar current = Calendar.getInstance();
		current.setTime(currenttime);
		init(current);
	}
	
	private void init(Calendar currenttime) throws ParseException {
		this.currenttime = currenttime;
		Calendar currentMonthExcuteTimePoint =  getCurrentMonthExcuteTimePoint(currenttime);
		this.begin=Calendar.getInstance();
		if(currenttime.getTimeInMillis()>currentMonthExcuteTimePoint.getTimeInMillis()){
			this.begin.set(Calendar.MONTH, currenttime.get(Calendar.MONTH));
		}else{
			this.begin.set(Calendar.MONTH, currenttime.get(Calendar.MONTH)-1);
		}
		this.begin.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		this.begin.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		this.begin.set(Calendar.MINUTE, Integer.parseInt(minutes));
		this.end=(Calendar) this.begin.clone();
		this.end.add(Calendar.MONTH, 1);
	}

	/**
	 * 获取本月执行时间点
	 * @param currentTime
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getCurrentMonthExcuteTimePoint(Calendar currentTime) throws ParseException{
		Calendar result= (Calendar) currentTime.clone();
		result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		result.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		result.set(Calendar.MINUTE, Integer.parseInt(minutes));  
		return result;
	}

	public boolean isInCurrentPeriod() {
		if(this.begin.getTimeInMillis()<this.currenttime.getTimeInMillis() && this.currenttime.getTimeInMillis()<this.end.getTimeInMillis())
			return true;
		return false;
	}

	public boolean isInCurrentPeriod(Date recordDate) {
		if(this.begin.getTimeInMillis()<recordDate.getTime() && recordDate.getTime()<this.end.getTimeInMillis())
			return true;
		return false;
	}
}
