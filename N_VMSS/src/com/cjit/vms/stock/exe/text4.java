package com.cjit.vms.stock.exe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * 在文件夹下查找要匹配数据的文件的方法
 * @author 
 *
 */
public class text4 {
	public static void main(String[] args) {
		File file = new File("C:/Users/JXJIN/Desktop/时间/ssq");
		String contstr="redReceiptApplyInfo";
		List<String> list = getfileName(file.listFiles(),contstr);
		if(list!=null&&list.size()>0){
			for (String string : list) {
				System.out.println("查询的字符在文件:\t"+string.split("-")[1]+"中\t"+"文件的路径为:\t"+string.split("-")[0]);
			}
		}else{
			System.out.println("抱歉，你索要找的文件不存在");
		}
	}
	/**
	 * 获取所有文件/方法（递归获取文件）
	 * @param files
	 * @param str
	 * @return
	 */
	public static List<String> getfileName(File[] files,String str) {
		List<String> list = new ArrayList<String>();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					getfileName(file.listFiles(),str);
				} else {
					String path = getStringInfile(file,str);
					if (path != null && path.length() > 0) {
						list.add(path);
					}
				}
			}
			return list;
		}
		return null;
	}
	/**
	 * 在文件中查找是否有匹配字符（io流的方法读文件）
	 * @param file
	 * @param str
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String getStringInfile(File file,String str) {
		String path = "";
		if (file != null) {
			try {
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					if (line.contains(str)) {
						//System.out.println(file.getName() + "\t" + line);
						path = file.getAbsolutePath()+"-"+file.getName();
					}
				}
				return path;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}

