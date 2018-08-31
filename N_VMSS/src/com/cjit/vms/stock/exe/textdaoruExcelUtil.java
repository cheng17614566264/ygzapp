package com.cjit.vms.stock.exe;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




public class textdaoruExcelUtil {
	/**
	 *    导入的文件
	 * @param file
	 *    类
	 * @param className
	 *    名称--字段
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes" }) 
	public static void importExcel(File file,Class className,Map<String, Object> map){
		try {
			HSSFWorkbook  hssfworkBook =new HSSFWorkbook(org.apache.commons.io.FileUtils.openInputStream(file));
			HSSFSheet hssfSheet=hssfworkBook.getSheetAt(0);
			int lastRowNum=hssfSheet.getLastRowNum();// 获取最后一行
			Map<Integer, Object> idMap=new HashMap<Integer, Object>();
			List<Map<Integer, Object>> biglist=new ArrayList<Map<Integer,Object>>();
			Object val=null;
			for(int r=0;r<=lastRowNum;r++){
				HSSFRow hssfRow=hssfSheet.getRow(r);
				int lastCellNum=hssfRow.getLastCellNum();
				for(int i=0;i<=lastCellNum;i++){
					//第n个单元格
					HSSFCell cell=hssfRow.getCell(i);
					if(HSSFDateUtil.isCellDateFormatted(cell)){
						Date date=cell.getDateCellValue();
						SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY-MM-dd");
						val=simpleDateFormat.format(date);
					}else{
						val=cell.getRichStringCellValue().toString();//String 
					}
					//获取字段
					if (r==0) {
						Set<String> keyset=map.keySet();
						for(String key:keyset){
							if(key.equals(val)){//表第一行数据=要保存的字段
								val=map.get(key);
							}
						}
					}
					idMap.put(i, val);
				}
				biglist.add(idMap);
			}
			
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
	}
	
//	public static void main(String[] args) {
//		ChildInfo cInfo=new ChildInfo();
//		Map<Integer , Object> map=new HashMap<Integer, Object>();
//		/*private String userName;
//	private int age;
//	private double height;
//	private Date date;
//	private String times;
//	private boolean flag;*/
//		List biglist=new ArrayList();
//		map.put(1, "userName");
//		map.put(2, "age");
//		map.put(3, "height");
//		map.put(4, "date");
//		map.put(5, "times");
//		map.put(6, "flag");
//		biglist.add(map);
//		map.clear();
//		map.put(1, "张三");
//		map.put(2, "19");
//		map.put(3, "179.00");
//		map.put(4, "2017-01-18");
//		map.put(5, "1123");
//		map.put(6, "1");
//		biglist.add(map);
//		
//		try {
//			textdaoruExcelUtil.setvalue(biglist, cInfo.getClass());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })// 大集合                类                                      
	public static void setvalue(List<Map<Integer, Object>> biglist,Class className) throws Exception{
	//	Object clas=className.newInstance();
		Method [] methods=className.getDeclaredMethods();
		Field[]fields=className.getDeclaredFields();
		List<Field> list=new ArrayList<Field>();
		if(biglist!=null&&biglist.size()>0){
			for(int i=0;i<biglist.size();i++){
				Map<Integer,Object> map=biglist.get(i);
				Set<Integer> keyset=map.keySet();
				for(Integer key:keyset){
					String val=(String)map.get(key);
					if(i==0){
						for(Field field:fields){
							if (val.equals(field.getName())) {
								// 的到对应字段
								list.add(field);
							}
						}
					}else{
						for(Field field:list){
							// 构造set方法
							String fieldSetName=parSetName(field.getName());
							if(!checkSetMet(methods, fieldSetName)){
								continue;
							}
							Method fieldSetMet=className.getMethod(fieldSetName, field.getType());
							String fieldType=field.getType().getSimpleName();
							if("String".equals(fieldType)){
								fieldSetMet.invoke(className,val);
							}else if("Date".equals(fieldType)){
								Date date=parseDate(val);
								fieldSetMet.invoke(className, date);
							}else if("Integer".equals(fieldType)){
								int inte=Integer.parseInt(val);
								fieldSetMet.invoke(className, inte);
							}else if("Double".equals(fieldType)){
								double doub=Double.parseDouble(val);
							}else if("Boolean".equals(fieldType)){
								boolean bool=Boolean.parseBoolean(val);
							}else if("BigDecimal".equals(fieldType)){
								BigDecimal bigDecimal=new BigDecimal(val);
								fieldSetMet.invoke(className, bigDecimal);
							}else{
								System.out.println("类型不存在");
								System.out.println(fieldType);
							}
						}
					}
					
				}
				
			}
		}
	}
	
	
	
//	 public static Map<String, String> getFieldValueMap(Object bean) {  
//	        Class<?> cls = bean.getClass();  
//	        Map<String, String> valueMap = new HashMap<String, String>();  
//	        // 取出bean里的所有方法  
//	        Method[] methods = cls.getDeclaredMethods();  
//	        Field[] fields = cls.getDeclaredFields();  
//	   
//	        for (Field field : fields) {  
//	            try {  
//	                String fieldType = field.getType().getSimpleName();  
//	                String fieldGetName = parGetName(field.getName());  
//	                if (!checkGetMet(methods, fieldGetName)) {  
//	                    continue;  
//	                }  
//	                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});  
//	                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});  
//	                String result = null;  
//	                if ("Date".equals(fieldType)) {  
//	                    result = fmtDate((Date) fieldVal);  
//	                } else {  
//	                    if (null != fieldVal) {  
//	                        result = String.valueOf(fieldVal);  
//	                    }  
//	                }  
//	                valueMap.put(field.getName(), result);  
//	            } catch (Exception e) {  
//	                continue;  
//	            }  
//	        }  
//	        return valueMap;  
//	   
//	    } 
//	 
	/**
	 * 将String转化成Date
	 * @param Sdate
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unused")
	private static Date parseDate(String Sdate) throws ParseException{
		 if(Sdate==null&&Sdate==""){
			 return new Date();
		 }
		 SimpleDateFormat sfd=new SimpleDateFormat("YYYY-MM-dd");
		 return sfd.parse(Sdate);
	 }
	 
	 /** 
	     * 日期转化为String 
	     * @param date 
	     * @return date string 
	     */ 
	    @SuppressWarnings("unused")
		private static String fmtDate(Date date) {  
	        if (null == date) {  
	            return null;  
	        }  
	        try {  
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);  
	            return sdf.format(date);  
	        } catch (Exception e) {  
	            return null;  
	        }  
	    }  
	 /** 
	     * 判断是否存在某属性的 get方法 
	     * @param methods 
	     * @param fieldGetMet 
	     * @return boolean 
	     */ 
	    @SuppressWarnings("unused")
		private static boolean checkGetMet(Method[] methods, String fieldGetMet) {  
	        for (Method met : methods) {  
	            if (fieldGetMet.equals(met.getName())) {  
	                return true;  
	            }  
	        }  
	        return false;  
	    } 
	    /**
	     * 判断是否存在某属性的set方法
	     * @param methods
	     * @param fieldSetMet
	     * @return
	     */
		private static boolean checkSetMet(Method[] methods, String fieldSetMet){
	    	for(Method met:methods){
	    		if(fieldSetMet.equals(met.getName())){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	 
	  /** 
	     * 拼接某属性的 get方法 
	     * @param fieldName 
	     * @return String 
	     */ 
	    @SuppressWarnings("unused")
		private static String parGetName(String fieldName) {  
	        if (null == fieldName || "".equals(fieldName)) {  
	            return null;  
	        }  
	        return "get" + fieldName.substring(0, 1).toUpperCase()  
	                + fieldName.substring(1);  
	    }  
	    /** 
	     * 拼接在某属性的 set方法 
	     * @param fieldName 
	     * @return String 
	     */ 
	    private static String parSetName(String fieldName) {  
	        if (null == fieldName || "".equals(fieldName)) {  
	            return null;  
	        }  
	        return "set" + fieldName.substring(0, 1).toUpperCase()  
	                + fieldName.substring(1);  
	    } 
	    
}
