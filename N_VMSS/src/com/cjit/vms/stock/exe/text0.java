package com.cjit.vms.stock.exe;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class text0 {

	
	public static void main(String[] args) {
		FileInputStream bis;
		try {
			bis = new FileInputStream("C:\\Users\\JXJIN\\Desktop\\12.jpeg");
			FileOutputStream bos=new FileOutputStream("E:\\workspacechongqing\\uploadFile\\fileName\\cope.jpeg");
			byte [] by=new byte[1024*8];
			int b=0;
			while((b=bis.read(by))!=-1){
				bos.write(by, 0, b);
			}
			bis.read();
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
