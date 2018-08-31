package com.cjit.vms.stock.exe;

import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class LX implements ServletContextListener{

	public Logger logger=Logger.getLogger(LX.class);
	private ApplicationContext applicationContext;
	public ScheduledExecutorService sheduledService;
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
	}

}
