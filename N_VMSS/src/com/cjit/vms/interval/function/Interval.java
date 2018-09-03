package com.cjit.vms.interval.function;

import java.util.Timer;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-08
 * @描述： 定时器实体类
 */
public class Interval {
	private String timerId;  //定时器id，用于存储batchruntime表中的cname字段值
	private int hour;	//定时器开启时间(小时)，存储batchruntime表中的hour字段值
	private int minute;  //定时器开启时间(分钟)，存储batchruntime表中的minute字段值
	private int second;  //定时器开启时间(秒钟)，存储batchruntime表中的second字段值
	private Long sendInterval;  //定时器间隔时间，存储batchruntime表中的intervalHour,intervalMinute,intervalSecond字段值之和的毫秒数
	private Timer timer;  //存储当前定时器对象
	public String getTimerId() {
		return timerId;
	}
	public void setTimerId(String timerId) {
		this.timerId = timerId;
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
	public Long getSendInterval() {
		return sendInterval;
	}
	public void setSendInterval(Long sendInterval) {
		this.sendInterval = sendInterval;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
