package com.cjit.achieveTemp.timing;

	import java.util.TimerTask;    
    
	import javax.servlet.ServletContext;

import com.cjit.insertIntoMysqlFromTemp.InsertIntoCustomer;
import com.cjit.insertIntoMysqlFromTemp.InsertIntotransInfo;    
	    
	/**  
	 * 定时器   
	 * @author sxl  
	 *  
	 */    
	public class DelFileTask extends TimerTask{    
	    
	    private static boolean isRunning = false;      
	      
	    private ServletContext context = null;      
	      
	    public DelFileTask() {    
	        super();    
	    }    
	        
	    public DelFileTask(ServletContext context) {      
	        this.context = context;      
	    }      
	    @Override    
	    public void run() {    
	            
	        if (!isRunning) {    
	                 
	            context.log("开始执行指定任务");     
	            /**  
	             * 自己的业务代码  
	             */    
	            Timing tt=new Timing();
   
	            System.out.println("dddddddddddddddddddddddddddddddddddddd");
	            
	            InsertIntoCustomer insertIntoCustomer=new InsertIntoCustomer();
		        insertIntoCustomer.insertIntoTransInfo();
	            
		        InsertIntotransInfo insertIntotransInfo=new InsertIntotransInfo();
		        insertIntotransInfo.insertIntoTransInfo();           
		       /* InsertIntoCustomer insertIntoCustomer=new InsertIntoCustomer();
		        insertIntoCustomer.insertIntoTransInfo();*/
	            
	            isRunning = false;      
	            context.log("指定任务执行结束");    
	        } else {      
	            context.log("上一次任务执行还未结束");      
	     }    
	    }    
	    
	}  

