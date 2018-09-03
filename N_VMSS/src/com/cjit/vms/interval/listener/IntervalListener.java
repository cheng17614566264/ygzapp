package com.cjit.vms.interval.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;

import com.cjit.common.util.SpringContextUtil;
import com.cjit.vms.interval.function.IntervalUtil;
/**
 *新增
 *日期：2018-09-03
 *作者：刘俊杰
 *功能：核心、总账、费控、犹豫期等定时器的监听器
 */
public class IntervalListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public IntervalListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	IntervalUtil intervalUtil = new IntervalUtil();
    	intervalUtil.initBatchRunTimeTable();
    	intervalUtil.setIntervalTaskList();
    	intervalUtil.setInterval("cname");
    }
	
}
