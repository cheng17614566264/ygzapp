package com.cjit.vms.stock.exe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class text6 {
	
	public static void main(String[] args) {
//		Student student=Student.getInstance();
//		System.out.println(student.toString());
		final boolean flag =true;
		new Thread(){
			@Override
			public void run() {
				try {
					while(flag){
						this.wait();
						System.out.println(getdat());
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
//		new Thread(){
//			public void run() {
//				for(int i=0;i<100;i++){
//					System.out.println("次数："+i);
//				}
//			};
//		}.start();
		
	}
	
	public static String getdat(){
		return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
	}
	
}

class Student{
	private final int age =15;
	private final String name="susan";
	private Student(){
	}
	public static Student getInstance(){
		return new Student();
	}
	@Override
	public String toString() {
		return "Student [age=" + age + ", name=" + name + "]";
	}
	
}

