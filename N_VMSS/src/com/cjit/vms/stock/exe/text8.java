package com.cjit.vms.stock.exe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.emory.mathcs.backport.java.util.Arrays;

public class text8 {
	public static void main(String[] args) {
	//	dom1();
	//	dom2();
		try {
			ChildInfo childInfo=new ChildInfo("好好", 18, 180, new SimpleDateFormat("yyyy-MM-dd").parse("1993-09-09"), "", true);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 */
	private static void dom2() {
		String []strings={"a","b"," ","d","e"};
		List<String> list=new ArrayList<String>();
		list=Arrays.asList(strings);
		Gson gson=new GsonBuilder().serializeNulls().create();
 		String json=gson.toJson(list);
		System.out.println("{\"outRateInfo\":"+json+"}");
	}

	/**
	 * 
	 */
	private static void dom1() {
		String []strings={"a","b","c","d","e"};
		List<String> list=new ArrayList<String>();
		list=Arrays.asList(strings);
		Gson gson=new GsonBuilder().serializeNulls().create();
 		String json=gson.toJson(list);
		System.out.println("{\"outRateInfo\":"+json+"}");
	}
}
