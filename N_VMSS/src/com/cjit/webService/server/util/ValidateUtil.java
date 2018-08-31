package com.cjit.webService.server.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class ValidateUtil {
	public static String fieldsisEmpty(Object bean,String[] fields){
		try{
		for(int i=0;i<fields.length;i++){
			PropertyDescriptor pd = new PropertyDescriptor(fields[i], bean.getClass());
			Method readMethod = pd.getReadMethod();
			Object invoke = readMethod.invoke(bean);
			System.out.println(fields[i]+"-"+invoke);
			if (invoke==null) {
				return fields[i]+"-不能为空";
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
}
