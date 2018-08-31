package com.cjit.ws.utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * 新增
 * 日期：2018-08-30
 * @author 刘俊杰
 * 功能：远程连接url工具类
 */
public class HttpUtil{
	
	private static String filePath = null;
	private static int i = 0;
	
	static {
    	Properties prop = new Properties();
		try {
			InputStream in = HttpUtil.class.getClassLoader().getResourceAsStream("file.properties");
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		filePath = prop.getProperty("savePdfFilePath");
	}
	
	
	
	/**
	 * 使用get方式连接url
	 * @param urlStr url地址
	 * @return
	 */
	public static String getURLContent(String urlStr) {             
	     URL url = null;
	     HttpURLConnection httpConn = null; 
	     InputStream in = null;
	     FileOutputStream out = null;
	     try{   
				url = new URL(urlStr);   
				
				//in = new InputStreamReader(url.openStream());
				in = url.openStream();
				String saveFilename = createFileName();
				out = new FileOutputStream(filePath + "\\" + saveFilename);
				byte buffer[] = new byte[1024];
				int len = 0;
	            while((len = in.read(buffer)) >0) {
	            	out.write(buffer, 0, len);
	            }
	            return "保存pdf成功";
	     }catch (Exception ex) {
	    	 ex.printStackTrace();
	    	 return "保存pdf发生异常,失败";
	     } finally{
	          try{           
	        	  if(in!=null) {
	        		  in.close();
	        	  }
	        	  if(out!=null) {
	        		  out.close();
	        	  }
	          }catch(IOException ex) {}   
	     }
     }
	
	
	/**
	 * @描述： 生成上传文件的文件名
	 */
	private static String createFileName(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = format.format(Calendar.getInstance().getTime());
		i++;
		return str + i + ".pdf";
	}


	/**
	 * 功能：使用post方式连接url
	 * @param urlStr url地址
	 * @param params post请求参数
	 * @return
	 * @throws Exception
	 */
    public static String getURLByPost(String urlStr,String params)throws Exception{
        URL url=new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.flush();        
        BufferedReader in = null; 
        StringBuilder sb = new StringBuilder(); 
        try{   
            in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") ); 
            String str = null;  
            while((str = in.readLine()) != null) {  
                sb.append(str);   
            }   
         } catch (Exception ex) { 
            throw ex; 
         } finally{  
          try{ 
              conn.disconnect();
              if(in!=null){
                  in.close();
              }
              if(printWriter!=null){
                  printWriter.close();
              }
          }catch(IOException ex) {   
              throw ex; 
          }   
         }   
         return sb.toString(); 
    }
}