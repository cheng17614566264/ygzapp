package com.cjit.achieveTemp.timing;

import java.util.Timer;

public class TimerTest01 {
	Timer timer;  
    public TimerTest01(){  
        timer = new Timer();  
        timer.schedule(new TimerTaskTest01(), 1000,2000);  
    }  
      
    public static void main(String[] args) {  
        System.out.println("timer begin....");  
        new TimerTest01();  
    }  
}
