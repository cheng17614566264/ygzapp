package com.cjit.vms.input.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@SuppressWarnings("deprecation")
public class Recorder {
	
	
	static{
		Recorder.recordPath = URLDecoder.decode(Recorder.class.getResource("/").getFile().toString()) + "config/record.properties";
		System.out.println("recordPath:"+Recorder.recordPath);
	}

	public static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	public static String recordPath;

	public static String getRecoredDate() throws IOException {
		InputStream is =null;
		try {
			File recordFile = new File(recordPath);
			recordFile.createNewFile();
			is = new FileInputStream(recordFile);
			Properties pro = new Properties();
			pro.load(is);
			return pro.getProperty("lastSucessExcutedDate");
		} finally {
			if(is!=null)
				is.close();
		}
	}
	
	public static void record() throws IOException{
		Properties pro = new Properties();
		pro.setProperty("lastSucessExcutedDate", new SimpleDateFormat(Recorder.DATE_FORMAT).format(new Date()));
		File recordFile = new File(recordPath);
		recordFile.createNewFile();
		OutputStream out = null;
		try {
			out = new FileOutputStream(recordFile);
			pro.store(out,"执行成功，记录日期。");
		} finally {
			if(out!=null)
				out.close();
		}
	}
	

}
