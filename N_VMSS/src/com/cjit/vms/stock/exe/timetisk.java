package com.cjit.vms.stock.exe;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.PullDataService;

public class timetisk implements ServletContextListener{

	private static HashMap<String, TimeUnit> timeUnitMap;
	public Logger logger=Logger.getLogger(timetisk.class);
	private ApplicationContext applicationContex;
	public ScheduledExecutorService scheduledExecutorService;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		applicationContex=WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
		PullDataService service=(PullDataService) applicationContex.getBean("pullDataServiceImpl");
		//StockService service=(StockService) applicationContex.getBean("stockServiceImpl");
		//service.findLostRecycle(lostRecycle)
		List<TimeTaskEntity> list=service.findTimeTisk("0");
		logger.info("当前的定时任务有："+list);
		int cpuNum=Runtime.getRuntime().availableProcessors();//获取当前计算机的cpu数量
		if(list.size()==0){
			return;
		}
		if(list.size()>=cpuNum){
			scheduledExecutorService=Executors.newScheduledThreadPool(cpuNum);
		}else{
			scheduledExecutorService=Executors.newScheduledThreadPool(list.size());
		}
		for(TimeTaskEntity timeTaskEntity:list){
			try {
				//
				Class<Runnable> classEntity=(Class<Runnable>)Class.forName(timeTaskEntity.getClassName());
				Constructor<Runnable> constructor=classEntity.getConstructor(PullDataService.class);
				Runnable runnable=constructor.newInstance(service);
				
//		//=====================================================
//				ChildInfo childInfo =new ChildInfo();
//				Class<Runnable> cl=(Class<Runnable>) Class.forName(childInfo.toString());
//				runnable=cl.newInstance();
//				
			} catch (Exception  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private long timeBetween(TimeTaskEntity entity) throws ParseException{
		String startTime=entity.getStartTime();
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date taskDate=sFormat.parse(startTime);
		Calendar taskCalender=Calendar.getInstance();
		taskCalender.setTime(taskDate);
		Calendar calendar=Calendar.getInstance();
		taskCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		taskCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		taskCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		taskDate=taskCalender.getTime();
		long taskTime=taskDate.getTime();
		long currentTime=new Date().getTime();
		return taskTime-currentTime;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
