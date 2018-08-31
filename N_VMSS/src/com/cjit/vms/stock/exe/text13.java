package com.cjit.vms.stock.exe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class text13 {

	/**
	 * private String userName;
	private int age;
	private double height;
	private Date date;
	private String times;
	private boolean flag;
	 * @param args
	 */
	public static void main(String[] args) {
		List<ChildInfo> list1=new ArrayList<ChildInfo>();
		List<ChildInfo> list2=new ArrayList<ChildInfo>();
		for(int i=0;i<10;i++){
			ChildInfo cInfo =new ChildInfo("", 24, 45.89, new Date(), "888", true);
			list1.add(cInfo);
			list2.add(cInfo);
		}
		List list=new ArrayList();
		list.add(list1.toArray());
		list.add(list2.toArray());
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i));
			Object[]objects=(Object[]) list.get(i);
			//System.out.println(objects.length+" ChildInfo []");
			/*for (Object object : objects) {
				ChildInfo cInfo=(ChildInfo) object;
				System.out.println(cInfo.toString());
			}*/
			List list3=Arrays.asList(objects);
			for(int k=0 ;k<list3.size();k++){
				ChildInfo cInfo=(ChildInfo) list3.get(k);
				System.out.println(cInfo.toString());
			}
		}
		System.out.println(list.size());
	}
}
