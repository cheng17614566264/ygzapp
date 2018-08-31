package com.cjit.vms.trans.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
/**
 * 图片上传/展示/下载工具类
 *
 */
public class ImageUtil {
	/**
	 * 
	 * @param fileName  文件名
	 * @param files     上传的文件
	 * @param flNames   上传文件的类型
	 * @param bool      是否追加图片：true-追加  false-清空
	 * @return
	 */
	public static String info(String fileName,File [] files,String [] flNames ,boolean bool){
		String msgString="OK";
		String massage="Y";
		try {
			File fl=findfile(fileName);
			String[] fileTypes = new String[] { "jpg","png","jpeg","pdf" }; 
			//File fl=new File("E:\\workspacechongqing\\uploadFile\\"+fileName);
			if(!fl.exists()){
				fl.mkdirs();
			}else{
				if(false==bool){
					removefile(fl);
				}
			}
			for(int i=0 ;i<files.length;i++){
				String flName=flNames[i];
				String fileExt = flName.substring(flName.lastIndexOf(".") + 1)
					      .toLowerCase();
				File file = files[i];
				 // 检查扩展名
			    if (!Arrays. asList(fileTypes).contains(fileExt)) {
			    	msgString = "上传文件扩展名是不允许的扩展名。";
			    	massage="N";
			    	break;
			    }
			    if("Y".equals(massage)){
			    	FileInputStream bis=new FileInputStream(file);
			    	FileOutputStream bos=new FileOutputStream(fl+"/"+flName);
			    	byte [] by=new byte[1024*8];
			    	int b=0;
			    	while((b=bis.read(by))!=-1){
			    		bos.write(by, 0, b);
			    	}
			    	bis.read();
			    	bos.flush();
			    	bos.close();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgString;
	}
	
	public static void removefile(File file){
		if(file.exists()&&file.isDirectory()){
			for(File fl :file.listFiles()){
				if(!fl.isDirectory()){
					fl.delete();
				}else{
					removefile(fl);
				}
			}
		}else{
			file.delete();
		}
	}
	
	/**
	 * 获取weblogic文件路径
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File findfile(String fileName) throws IOException{
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
		Properties properties=new Properties();
		properties.load(inputStream);
		String filePath=properties.getProperty("path");
		// /vms/software/web/log
		filePath=filePath.split("/")[0]+"/"+filePath.split("/")[1]+"/"+filePath.split("/")[2]+"/"+filePath.split("/")[3]+"/image";
		File fl=new File(filePath+"/"+fileName);
		return fl;
	}
	
    /**
     * 图片展示
     * @param fileName
     * @param index
     * @return
     */
	public static String imageShow(String fileName,int index){
		
		try {
			File fl=findfile(fileName);
			//File fl=new File("E:\\workspacechongqing\\uploadFile\\"+fileName);
			if(fl.exists()&&fl.isDirectory()){
				File [] files=fl.listFiles();
				if(index<=files.length&&index>=0){
					String flName = files[index].getName();
					return fl.getAbsolutePath()+"/"+flName ;
				}else{
					return "";
				}
			}else{
				return fl.getAbsolutePath()+"\\";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}  
	public static String imageShowEach(String fileName,int index){
		File fl;
		try {
			fl = findfile(fileName);
			List<String> pathlist=new ArrayList<String>();
			//File fl=new File("E:\\workspacechongqing\\uploadFile\\"+fileName);
			if(fl.exists()&&fl.isDirectory()){
				File [] files=fl.listFiles();
				for (File file : files) {
					if(file.exists()&&file.isDirectory()){
						File [] filexs = file.listFiles();
						for (File filex : filexs) {
							pathlist.add(filex.getAbsolutePath());
						}
					}
				}
			}else{
				pathlist.add(fl.getAbsolutePath()+"/");
			}
			return pathlist.get(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static int FilelengthEach(String fileName){
		File fl;
		try {
			fl = findfile(fileName);
			List<String> pathlist=new ArrayList<String>();
			//File fl=new File("E:\\workspacechongqing\\uploadFile\\"+fileName);
			if(fl.exists()&&fl.isDirectory()){
				File [] files=fl.listFiles();
				for (File file : files) {
					if(file.exists()&&file.isDirectory()){
						File [] filexs = file.listFiles();
						for (File filex : filexs) {
							pathlist.add(filex.getAbsolutePath());
						}
					}
				}
			}else{
				pathlist.add(fl.getAbsolutePath()+"/");
			}
			return pathlist.size();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("unused")
	public static int Filelength(String fileName){
		try {
			File fl=findfile(fileName);
			//File fl=new File("E:\\workspacechongqing\\uploadFile\\"+fileName);
			int count = 0;
			if(fl.exists()&&fl.isDirectory()){
				return fl.listFiles().length;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
