package com.cjit.vms.input.model;

public class TimeTaskEntity {
	//定时任务编号
	private String id;
	//名称
	private String name;
	//类名
	private String className;
	//开始时间
	private String startTime;
	//周期
	private String period;
	// 数据类型 0- 定时任务 1-时间节点
	private String dataMold;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	@Override
	public String toString() {
		return "{编号:" + id + ", 任务名称:" + name +"}";
	}
	public String getDataMold() {
		return dataMold;
	}
	public void setDataMold(String dataMold) {
		this.dataMold = dataMold;
	}
	
}
