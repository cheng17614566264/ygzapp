package com.cjit.vms.stock.exe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class text11 {

	public static void main(String[] args) {
		
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		int month=calendar.get(Calendar.MONTH)+1;
		String mon=month+"";
		if (mon.length()<2) {
			mon="0"+mon;
		}
		Map<String, String> map=new HashMap<String, String>();
		map.put("month", year+"-"+mon);
		System.out.println(map);
	}
	
	
	public static void getGeneralIedger() {
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		int month=calendar.get(Calendar.MONTH)+1;
		String mon=month+"";
		if (mon.length()<2) {
			mon="0"+mon;
		}
		Map<String, String> map=new HashMap<String, String>();
		map.put("month", year+"-"+mon);
		System.out.println(map);
		//this.delete("deleteGeneralLedger", map);
 		//this.save("insertGeneralLedger", map);
	}
}
