package com.cjit.vms.input.listener;

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

import com.cjit.vms.input.jdbcLink.JdbcGetGeneralIedger;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.input.service.PullDataService;

/**
 * 定时任务处理类
 * 
 * @author jxjin
 *
 */
public class TimeTaskListener implements ServletContextListener {
	public Logger logger = Logger.getLogger(TimeTaskListener.class);
	private ApplicationContext applicationContex;
	public ScheduledExecutorService sheduledService;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (sheduledService != null) {
			sheduledService.shutdown();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		applicationContex = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		PullDataService service = (PullDataService) applicationContex.getBean("pullDataServiceImpl");
		List<TimeTaskEntity> timeTaskEntities = service.findTimeTisk("0");
		logger.info("当前的定时任务有:" + timeTaskEntities);
		sheduledService = Executors.newScheduledThreadPool(timeTaskEntities.size());
		for (TimeTaskEntity timeTaskEntity : timeTaskEntities) {
			if("com.cjit.vms.input.listener.GeneralIedger".equals(timeTaskEntity.getClassName())){
				try {
					@SuppressWarnings("unchecked")
					Class<Runnable> classEntity = (Class<Runnable>) Class.forName(timeTaskEntity.getClassName());
					Constructor<Runnable> constructor = classEntity.getConstructor(PullDataService.class);
					//Constructor<Runnable> constructor = classEntity.getConstructor(JdbcGetGeneralIedger.class);
					Runnable runnable = constructor.newInstance(service);
					long timeBetween = this.timeBetween(timeTaskEntity);
					// 如果过了指定时间，则立刻执行
					if (timeBetween < 0) {
						timeBetween = 0;
					}
					sheduledService.scheduleAtFixedRate(runnable, timeBetween, Long.valueOf(timeTaskEntity.getPeriod()),
							TimeUnit.SECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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
}
