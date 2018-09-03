package com.cjit.vms.stock.exe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.JXLTool;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
/*import sun.awt.windows.WDataTransferer;*/

public class textdaochuExcelUtil {
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
	public static void exportExcel(String fileName,  Class className, String[] fields, String[] titles,
			List dataList, OutputStream os){
		
		
		WritableWorkbook wb=null;
		
		try {
			wb=Workbook.createWorkbook(os);
			WritableSheet ws=wb.createSheet(fileName, 0);
			
			for(int i=0;i<titles.length;i++){
				Label label =new Label(i, 0, titles[i], JXLTool.getHeader());
				ws.addCell(label);
				ws.setColumnView(i, 25);
				// 表格工具类 设置颜色
				JxlExcelInfo excelInfo = new JxlExcelInfo();
		        excelInfo.setBgColor(Colour.YELLOW2);
		        excelInfo.setBorderColor(Colour.BLACK);
			}
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			for(int i=0;i<dataList.size();i++){
				Object object=dataList.get(i);
				for(int j=0;j<fields.length;j++){
					// 内省得到对应得类
					PropertyDescriptor propertyDescriptor=new PropertyDescriptor(fields[j], className);
					Method method=propertyDescriptor.getReadMethod();
					Object invoke=method.invoke(object);
					String value=null;
					if(invoke!=null){ 
						//得到所对应的数据类型  属性 的数据类型
						String returnType=method.getReturnType().toString();
						String dateType=Date.class.toString();
						value=invoke.toString();
						if (dateType.equals(returnType)) {
							Date date=(Date) invoke;
							value=simpleDateFormat.format(date);
						}
					}else{
						value="";
					}
					Label celltext=new Label(j, i+1, value);
					ws.addCell(celltext);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(wb!=null){
				try {
					wb.write();
					wb.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
