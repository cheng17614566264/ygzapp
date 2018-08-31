package com.cjit.achieveTemp.timing;

import java.util.Date;
import java.util.TimerTask;

public class TimerTaskTest01 extends TimerTask {
	 public void run() {  
		 
		 Date date = new Date(this.scheduledExecutionTime());  
	        System.out.println("Time's up!!!!");  
	    }  
}
