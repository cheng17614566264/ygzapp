package com.cjit.vms.stock.exe;

import java.util.Date;

public class ChildInfo {

	
	private String userName;
	private int age;
	private double height;
	private Date date;
	private String times;
	private boolean flag;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public ChildInfo(String userName, int age, double height, Date date, String times, boolean flag) {
		super();
		this.userName = userName;
		this.age = age;
		this.height = height;
		this.date = date;
		this.times = times;
		this.flag = flag;
	}
	public ChildInfo() {
		super();
	}
	@Override
	public String toString() {
		return "ChildInfo [userName=" + userName + ", age=" + age + ", height=" + height + ", date=" + date + ", times="
				+ times + ", flag=" + flag + "]";
	}
	
	
}
