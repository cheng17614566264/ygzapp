package com.cjit.ws.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.ws.entity.VmsTransType;

public class VmsTransTypeDaoImpl  extends SqlMapClientDaoSupport implements VmsTransTypeDao{

	@Override
	public String insert(VmsTransType vmsTransType){
		try{
			getSqlMapClientTemplate().insert("insertVmsTransType",vmsTransType);
		}catch(Exception e){
			return "�������ִ���ʧ��,�������ظ����ݣ����鱨���Ƿ���ȷ���Ժ����ԣ�";
		}
		return "Y";
	}

}
