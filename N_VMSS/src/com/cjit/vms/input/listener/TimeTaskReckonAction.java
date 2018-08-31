package com.cjit.vms.input.listener;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cjit.common.util.SpringContextUtil;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.PullDataService;

public class TimeTaskReckonAction implements ServletContextListener{
	public Logger logger = Logger.getLogger(TimeTaskReckonAction.class);
	private ScheduledExecutorService sheduledService;
	private ApplicationContext applicationContex;
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		/*applicationContex = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());*/
		this.applicationContex = SpringContextUtil.getApplicationContext();
		PullDataService service = (PullDataService) applicationContex.getBean("pullDataServiceImpl");
		List<TimeTaskEntity> timeTaskEntities = service.findTimeTisk("0");
		logger.info("当前的定时任务有:" + timeTaskEntities);
		sheduledService=Executors.newScheduledThreadPool(timeTaskEntities.size()+1);
		for (TimeTaskEntity timeTaskEntity : timeTaskEntities) {
			if("com.cjit.vms.input.listener.ProportionalityAction".equals(timeTaskEntity.getClassName())){
				try {
					Class<Runnable> clasEntity=(Class<Runnable>) Class.forName(timeTaskEntity.getClassName());
					Constructor<Runnable> constructor = clasEntity.getConstructor(PullDataService.class);
					Runnable runnable = constructor.newInstance(service);
					///Runnable runnable=clasEntity.newInstance();
					long timeBetween=timeBetween(timeTaskEntity);
					if(compareTotime(timeTaskEntity)<=0){
						if (timeBetween < 0) {
							timeBetween = 0;
						}
						sheduledService.scheduleAtFixedRate(runnable, timeBetween, Long.valueOf(timeTaskEntity.getPeriod()),
								TimeUnit.SECONDS);
					}else{
						System.err.println("比例计算时间已结束，跳过本次计算");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 比较时间
	 * @param entity
	 * @return
	 */
	private int compareTotime(TimeTaskEntity entity){
		String startTime =entity.getStartTime();
		SimpleDateFormat sFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate= sFormat.parse(startTime);
			Calendar curren =Calendar.getInstance();
			//开始时间
			Calendar startCal=Calendar.getInstance();
			startCal.setTime(startDate);
			if(startCal.get(Calendar.DATE)==curren.get(Calendar.DATE)){
				startCal.set(Calendar.YEAR, curren.get(Calendar.YEAR));
 				startCal.set(Calendar.MONTH, curren.get(Calendar.MONTH));
 				System.out.println(sFormat.format(startCal.getTime())+"\tstartCal");
 				System.out.println(sFormat.format(curren.getTime())+"\tcurren");
 				return curren.compareTo(startCal);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	/**
	 * 计算距离任务开始的时间间隔
	 */
	private long timeBetween(TimeTaskEntity entity) {
		String startTime = entity.getStartTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date taskDate = sf.parse(startTime);
			Calendar taskCalendar = Calendar.getInstance();
			taskCalendar.setTime(taskDate);
			Calendar calendar = Calendar.getInstance();
			taskCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			taskCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			taskCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			taskDate = taskCalendar.getTime();
			long taskTime = taskDate.getTime();
			long currentTime = new Date().getTime();
			return (taskTime - currentTime)/1000;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void main(String[] args) {
	}
}
