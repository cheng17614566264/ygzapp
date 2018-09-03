package com.cjit.vms.BatchRun.model;

public class BatchRunTime {
	private int hour;
	private int minute;
	private int second;
	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：batchruntime表中增加字段，用于存储跑批任务的时间间隔
	 */
	private int id;
	private int intervalHour;
	private int intervalMinute;
	private int intervalSecond;
	//end 2018-09-03
	private String cname; 
	
	public BatchRunTime() {
		super();
	}
	public BatchRunTime(int hour, int minute, int second) {
		super();
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public int getIntervalHour() {
		return intervalHour;
	}
	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
	}
	public int getIntervalMinute() {
		return intervalMinute;
	}
	public void setIntervalMinute(int intervalMinute) {
		this.intervalMinute = intervalMinute;
	}
	public int getIntervalSecond() {
		return intervalSecond;
	}
	public void setIntervalSecond(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}
	
	
}
