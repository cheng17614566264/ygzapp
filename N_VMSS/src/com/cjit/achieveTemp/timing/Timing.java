package com.cjit.achieveTemp.timing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cjit.achieveTemp.achieveTemp;
import com.cjit.vms.trans.action.createBill.CreateBillAction;

//定时抓取中间表中信息
public class Timing {
	public static void main(String[]args){
		achieveTemp at=new achieveTemp();
		
		Runnable runnable = new Runnable() {  
            public void run() {  
            	
            	System.out.println("我是计时器");
            	/*CreateBillAction cba=new CreateBillAction();
                cba.insertIntoMyselfFromCustomerTemp();*/
            }  
        };  
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();  
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
        service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);  
	}
}








