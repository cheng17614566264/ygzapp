package com.cjit.vms.trans.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {
public static void main(String[] args) {
	Date tDate=new Date(); 
	System.out.println("tDate:"+tDate);
	SimpleDateFormat tSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2016-06-08 14:36:36
	String currentDate=tSimpleDateFormat.format(tDate);
	System.out.println("currentDate"+currentDate);
	System.out.println("currentDate"+currentDate.substring(0,10));
	String billDate="2016-07-04 14:11:41";
	System.out.println(billDate+"  "+billDate);
	if(billDate.length()>10){
	System.out.println(billDate.substring(0, 10));
	}
	
}
}
