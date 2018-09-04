package com.cjit.vms.interval.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cjit.common.util.SpringContextUtil;
import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.interval.function.service.IntervalDao;
import com.cjit.vms.interval.function.service.impl.IntervalDaoImpl;

/**
 * @作者： 刘俊杰
 * @日期： 2018-09-03
 * @描述：定时器工具类，用于调度定时器以及初始化一些功能 
 *
 */
public class IntervalUtil {
	//存储每个任务对应的定时器实体对象
	public static List<Interval> intervalList = new ArrayList<Interval>();
	
	
	/**
	 * 初始化跑批任务表
	 * 若表为空，则向表中初始化数据，设置各初始跑批时间为每天凌晨0点开始跑批
	 */
	public void initBatchRunTimeTable() {
		IntervalDao intervalDao = IntervalUtil.getIntervalDao();
		//查询表中的数据
		Long batchCount = intervalDao.findBatchRunTimeCount();
		if(batchCount < 4) {
			//删除表中原有的数据
			intervalDao.deleteBatchRunTimeForInit();
			//重新插入数据，进行初始化
			intervalDao.insertBatchRunTimeInit();
		}
	}
	
	
	
	
	/**
	 * 初始化任务队列，从数据库batchruntime表中读取各任务对应的开始跑批时间和跑批时间间隔
	 */
	public void setIntervalTaskList() {
		IntervalDao intervalDao =IntervalUtil.getIntervalDao();
		List<BatchRunTime> batchRunTimeAllList = intervalDao.findBatchRunTimeAllList();
		for(BatchRunTime blist : batchRunTimeAllList) {
			Interval interval = new Interval();
			interval.setTimerId(blist.getCname());
			interval.setHour(blist.getHour());
			interval.setMinute(blist.getMinute());
			interval.setSecond(blist.getSecond());
			long intervalHour = blist.getIntervalHour() * 60 * 60 * 1000;
			long intervalMinute = blist.getIntervalMinute() * 60 * 1000;
			long intervalSecond = blist.getIntervalSecond() * 1000;
			interval.setSendInterval(intervalHour + intervalMinute + intervalSecond);
			intervalList.add(interval);
		}
	}
	
	
	/**
	 * 初始化定时器，仅当服务器重启时或者需要重置对应任务的定时器时，才重新开启对应的定时器
	 */
	public void setInterval(String cname) {
		int i = 0;
		for(Interval interval : intervalList) {
			if("核心".equals(interval.getTimerId())) {
				if("核心".equals(cname) || "cname".equals(cname)) {
					intervalList.set(i, intervalOfHexin(interval));
				}
			}
			else if("总账".equals(interval.getTimerId())) {
				if("总账".equals(cname) || "cname".equals(cname)) {
					intervalList.set(i, intervalOfZongzhang(interval));
				}
			}
			else if("费控".equals(interval.getTimerId())) {
				if("费控".equals(cname) || "cname".equals(cname)) {
					intervalList.set(i, intervalOfFeikong(interval));
				}
			}
			else if("犹豫期".equals(interval.getTimerId())) {
				if("犹豫期".equals(cname) || "cname".equals(cname)) {
					intervalList.set(i, intervalOfYouyuqi(interval));
				}
			}
			i++;
		}
		
	}
	
	/**
	 * 定时器执行的任务--核心
	 * @param interval 当前的定时器实体对象
	 * @return 包含定时器对象的定时器实体对象
	 */
	private Interval intervalOfHexin(Interval interval) {
		Interval newInterval = new Interval();
		if(interval.getSendInterval() == null || interval.getSendInterval().equals(0)) {
			interval.setSendInterval(1000*60*60*24L);
		}
		long sendInterval = interval.getSendInterval();
		
		Date sendtime = getSendTime(interval);  //设置发送时间
		
		//设置定时器（任务调度）
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				System.out.println("执行任务---核心" + new Date().getSeconds());
				
				
				IntervalTask intervalTask = new IntervalTask();
				intervalTask.TaskOfbatchRun();
				
			}
		}, sendtime,sendInterval);
		
		newInterval.setSendInterval(sendInterval);
		newInterval.setTimer(timer);
		newInterval.setHour(interval.getHour());
		newInterval.setMinute(interval.getMinute());
		newInterval.setSecond(interval.getSecond());
		newInterval.setTimerId(interval.getTimerId());
		
		return newInterval;
		
	}
	
	
	
	/**
	 * 定时器执行的任务--总账
	 * @param interval 当前的定时器实体对象
	 * @return 包含定时器对象的定时器实体对象
	 */
	private Interval intervalOfZongzhang(Interval interval) {
		Interval newInterval = new Interval();
		if(interval.getSendInterval() == null || interval.getSendInterval().equals(0)) {
			interval.setSendInterval(1000*60*60*24L);
		}
		long sendInterval = interval.getSendInterval();
		
		Date sendtime = getSendTime(interval);  //设置发送时间
		
		
		//设置定时器（任务调度）
		Timer timer = new Timer();
		interval.setTimer(timer);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				System.out.println("执行任务---总账" + new Date().getSeconds());
				
				IntervalTask intervalTask = new IntervalTask();
				intervalTask.TaskOfGeneralIedger();
				
				
			}
		}, sendtime, sendInterval);

		newInterval.setSendInterval(sendInterval);
		newInterval.setTimer(timer);
		newInterval.setHour(interval.getHour());
		newInterval.setMinute(interval.getMinute());
		newInterval.setSecond(interval.getSecond());
		newInterval.setTimerId(interval.getTimerId());
		
		return newInterval;
	}
	
	/**
	 * 定时器执行的任务--费控
	 * @param interval 当前的定时器实体对象
	 * @return 包含定时器对象的定时器实体对象
	 */
	private Interval intervalOfFeikong(Interval interval) {
		Interval newInterval = new Interval();
		if(interval.getSendInterval() == null || interval.getSendInterval().equals(0)) {
			interval.setSendInterval(1000*60*60*24L);
		}
		long sendInterval = interval.getSendInterval();
		
		Date sendtime = getSendTime(interval);  //设置发送时间
		
		//设置定时器（任务调度）
		Timer timer = new Timer();
		interval.setTimer(timer);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				System.out.println("执行任务---费控" + new Date().getSeconds());
				
				IntervalTask intervalTask = new IntervalTask();
				intervalTask.TaskOfdataUpdate();
				
				
			}
		}, sendtime, sendInterval);

		newInterval.setSendInterval(sendInterval);
		newInterval.setTimer(timer);
		newInterval.setHour(interval.getHour());
		newInterval.setMinute(interval.getMinute());
		newInterval.setSecond(interval.getSecond());
		newInterval.setTimerId(interval.getTimerId());
		
		return newInterval;
	}
	
	/**
	 * 定时器执行的任务--犹豫期
	 * @param interval 当前的定时器实体对象
	 * @return 包含定时器对象的定时器实体对象
	 */
	private Interval intervalOfYouyuqi(Interval interval) {
		Interval newInterval = new Interval();
		if(interval.getSendInterval() == null || interval.getSendInterval().equals(0)) {
			interval.setSendInterval(1000*60*60*24L);
		}
		long sendInterval = interval.getSendInterval();
		
		Date sendtime = getSendTime(interval);  //设置发送时间
		
		//设置定时器（任务调度）
		Timer timer = new Timer();
		interval.setTimer(timer);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				System.out.println("执行任务---犹豫期" + new Date().getSeconds());
				
				IntervalTask intervalTask = new IntervalTask();
				intervalTask.TaskOfYouyuqi(IntervalUtil.getIntervalDao());
				
				
			}
		}, sendtime, sendInterval);

		newInterval.setSendInterval(sendInterval);
		newInterval.setTimer(timer);
		newInterval.setHour(interval.getHour());
		newInterval.setMinute(interval.getMinute());
		newInterval.setSecond(interval.getSecond());
		newInterval.setTimerId(interval.getTimerId());
		
		return newInterval;
	}
	
	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：获取当前任务的开始时间
	 * @param interval
	 * @return  当前任务的开始时间
	 */
	private Date getSendTime(Interval interval) {
		Calendar calendar = Calendar.getInstance();
		
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		if(hour > interval.getHour()) day += 1;
		else if(hour == interval.getHour()) {
			if(minute > interval.getMinute()) day += 1;
			else if(minute == interval.getMinute()) {
				if(second >= interval.getSecond()) day += 1;
			}
		}
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, interval.getHour());
		calendar.set(Calendar.MINUTE, interval.getMinute());
		calendar.set(Calendar.SECOND, interval.getSecond());
		Date sendtime = calendar.getTime();  //设置发送时间
		return sendtime;
	}
	
	/**
	 * 销毁定时器
	 * @param timer 当前定时器对象
	 */
	public void destory(Timer timer) {
		timer.cancel();
	}
	
	public static IntervalDao getIntervalDao() {
		ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
		IntervalDao intervalDao =  (IntervalDaoImpl) applicationContext.getBean("intervalDao");
		return intervalDao;
	}
	
	public List<Interval> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(List<Interval> intervalList) {
		this.intervalList = intervalList;
	}
}
