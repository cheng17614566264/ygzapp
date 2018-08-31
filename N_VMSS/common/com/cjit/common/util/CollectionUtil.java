/************************************************************

 ***********************************************************/
package com.cjit.common.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author huboA
 * @since 2008-7-15
 * @version 1.0
 */
public class CollectionUtil extends CollectionUtils{

	/**
	 * �ж�һ�������Ƿ�ΪNULL
	 * @param collection
	 * @return
	 */
	public static boolean isNull(Collection collection){
		return collection == null ? true : false;
	}

	/**
	 * �ж�һ�������Ƿ�ΪNULL
	 * @param collection
	 * @return
	 */
	public static boolean isNotNull(Collection collection){
		return !isNull(collection);
	}

	/**
	 * �ж�һ�������Ƿ�Ϊ��
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection collection){
		if(isNull(collection)){
			return true;
		}
		if(collection.size() == 0){
			return true;
		}
		return false;
	}

	/**
	 * �ж�һ�������Ƿ�Ϊ��
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection collection){
		return !isEmpty(collection);
	}

	/**
	 * ����Collection�õ�������","������ַ���(��������������)
	 * @param collection
	 * @return
	 */
	public static String getNumberSQLIds(Collection collection){
		if(isNotEmpty(collection)){
			StringBuffer sb = new StringBuffer();
			Iterator it = collection.iterator();
			while(it.hasNext()){
				String id = (String) it.next();
				sb.append(id + ",");
			}
			return sb.substring(0, sb.length() - 1).toString();
		}
		return "";
	}

	/**
	 * ����Collection�õ�������","������ַ���(�������ַ�������)
	 * @param collection
	 * @return
	 */
	public static String getStringSQLIds(Collection collection){
		if(isNotEmpty(collection)){
			StringBuffer sb = new StringBuffer();
			Iterator it = collection.iterator();
			while(it.hasNext()){
				String id = (String) it.next();
				sb.append("'" + id + "',");
			}
			return sb.substring(0, sb.length() - 1).toString();
		}
		return "";
	}
}
