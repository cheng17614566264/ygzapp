package com.cjit.vms.stock.util;

import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.JXLTool;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
	/**
	 * 将List的数据写入到excel中
	 * 
	 * @param fileName
	 *            文件名
	 * @param className
	 *            类
	 * @param fields
	 *            属性数组
	 * @param titles
	 *            表头
	 */
	@SuppressWarnings("rawtypes")
	public static void exportExcel(String fileName, Class className, String[] fields, String[] titles,
			List dataList, OutputStream os) {
		WritableWorkbook wb=null;
		try {
			wb = Workbook.createWorkbook(os);
			WritableSheet ws = wb.createSheet(fileName, 0);
			
			for (int i = 0; i < titles.length; i++) {
				Label header = new Label(i, 0, titles[i], JXLTool.getHeader());
				ws.addCell(header);
			}
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < dataList.size(); i++) {
				Object object=dataList.get(i);
				for (int j = 0; j < fields.length; j++) {
					PropertyDescriptor pd = new PropertyDescriptor(fields[j], className);
					Method readMethod = pd.getReadMethod();
					Object invoke = readMethod.invoke(object);
					String value=null;
					if (invoke!=null) {
						String returnType=readMethod.getReturnType().toString();
						String dateType=Date.class.toString();
						value=invoke.toString();
						if (dateType.equals(returnType)) {
							Date date=(Date) invoke;
							value=simpleDateFormat.format(date);
						}
					}else {
						value="";
					}
					Label cellText=new Label(j,i+1,value);
					ws.addCell(cellText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb!=null) {
					wb.write();
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}
}
