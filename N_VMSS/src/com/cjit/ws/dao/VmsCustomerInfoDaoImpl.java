package com.cjit.ws.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.ws.entity.VmsCustomerInfo;

public class VmsCustomerInfoDaoImpl  extends SqlMapClientDaoSupport implements VmsCustomerInfoDao{

	@Override
	public String insert(VmsCustomerInfo vmsCustomerInfo) {
		try{
			getSqlMapClientTemplate().insert("insertVmsCustomerInfo",vmsCustomerInfo);
		}catch(Exception e){
			return "插入交易流水号失败,可能是重复数据，请检查报文是否正确，稍后重试！";
		}
		return "Y";
	}

	@Override
	public boolean existSameCustomerByCustomerTaxno(String customerId) {
		Map map = new HashMap();
		map.put("customerId", customerId);
		List list = getSqlMapClientTemplate().queryForList("existSameCustomerByCustomerTaxno", map);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public String update(VmsCustomerInfo vmsCustomerInfo){
		try{
			getSqlMapClientTemplate().insert("updateVmsCustomerInfo",vmsCustomerInfo);
		}catch(Exception e){
			return "更新用户信息失败！";
		}
		return "Y";
	}

	@Override
	public String insertORUpdate(VmsCustomerInfo vmsCustomerInfo) {
		if(existSameCustomerByCustomerTaxno(vmsCustomerInfo.getBusinessId())){
			return update(vmsCustomerInfo);
		}else{
			return insert(vmsCustomerInfo);
		}
	}
	
	

}
