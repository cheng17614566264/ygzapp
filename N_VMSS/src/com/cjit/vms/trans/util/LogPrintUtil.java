package com.cjit.vms.trans.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import edu.emory.mathcs.backport.java.util.Arrays;

public class LogPrintUtil {
	public static void info(String message) {
		FileWriter fileWriter = null;
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
			Properties properties=new Properties();
			properties.load(inputStream);
			String filePath=properties.getProperty("path");
			File file=new File(filePath);
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			if (!file.exists()) {
				file.mkdirs();
			}
			String name = sf.format(date) + ".log";
			File logFile = new File(file.getPath() + "/" + name);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fileWriter = new FileWriter(logFile, true);
			fileWriter.write(new SimpleDateFormat("HH:mm:ss").format(date) + "\t" + message);
			fileWriter.close();
			File[] files = file.listFiles();
			String[] filePaths=new String[files.length];
			for (int i=0;i<files.length;i++) {
				filePaths[i]=files[i].getPath();
			}
			if (files.length > 7) {
				Arrays.sort(filePaths);
				new File(filePaths[0]).delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter!=null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		LogPrintUtil.info("aaaaaaaaa");
	}
}
