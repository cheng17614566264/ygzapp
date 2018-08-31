package com.cjit.achieveTemp.timing;

import java.util.Calendar;    
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;    
import javax.servlet.ServletContextListener;

import com.cjit.insertIntoMysqlFromTemp.InsertIntoCustomer;
import com.cjit.vms.BatchRun.model.BatchRunTime;
import com.cjit.vms.BatchRun.service.BatchRunTimeService;
import com.cjit.vms.BatchRun.service.impl.BatchRunTimeServiceImpl;    
    
public class ContextListener implements ServletContextListener{    
	InsertIntoCustomer iic=new InsertIntoCustomer();
	 
    public ContextListener() {    
    }    
        
     private java.util.Timer timer = null;      
         
    /**  
     * 初始化定时器  
     * web 程序运行时候自动加载    
     */    
    @Override    
    public void contextInitialized(ServletContextEvent arg0) {    
            
         /**   
         * 设置一个定时器   
         */      
        timer = new java.util.Timer(true);      
              
        arg0.getServletContext().log("定时器已启动");     
      
        /**   
         * 定时器到指定的时间时,执行某个操作(如某个类,或方法) 
         * 执行从数据库中读取跑批时间  
         */      
        iic.getConnction("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/ygzapp3","ygzapp", "ygzapp");
        
        List batchRunTime=iic.findBatchRunTime();
        int hour=Integer.parseInt(String.valueOf(batchRunTime.get(0)));
        int minute=Integer.parseInt(String.valueOf(batchRunTime.get(1)));
        int second=Integer.parseInt(String.valueOf(batchRunTime.get(2)));
        System.out.println(second);
        //设置执行时间    
        Calendar calendar =Calendar.getInstance();    
        int year = calendar.get(Calendar.YEAR);    
        int month = calendar.get(Calendar.MONTH);    
        int day =calendar.get(Calendar.DAY_OF_MONTH);//每天    
        //定制每天的1:00:00执行，    
        calendar.set(year, month, day, hour, minute, second);    
        Date date = calendar.getTime();    
//        System.out.println(date);    
            
       /* int period = 24 * 60 * 60 * 1000; */   
        int period =  24 * 60 * 60 * 1000; 
        //每天的date时刻执行task，每隔persion 时间重复执行    
        timer.schedule(new DelFileTask(arg0.getServletContext()), date, period);    
//        在 指定的date时刻执行task, 仅执行一次    
//        timer.schedule(new DelFileTask(arg0.getServletContext()), date);    
            
            
        arg0.getServletContext().log("已经添加任务调度表");      
            
    }    
    
    /**  
     * 销毁  
     */    
    @Override    
    public void contextDestroyed(ServletContextEvent arg0){    
    
        timer.cancel();    
        arg0.getServletContext().log("定时器销毁");    
    }    
}  