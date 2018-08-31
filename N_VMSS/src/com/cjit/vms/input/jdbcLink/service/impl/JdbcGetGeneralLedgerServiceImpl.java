package com.cjit.vms.input.jdbcLink.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.dao.GenericDao;
import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.input.jdbcLink.service.JdbcGetGeneralLedgerService;

public class JdbcGetGeneralLedgerServiceImpl extends GenericServiceImpl implements JdbcGetGeneralLedgerService {

	@Override
	public List findGeneralLedgerUrl(String query,Map parameters ) {
		System.out.println(query);
		System.out.println(parameters);
		List list=find(query, parameters);
		
		return list;
	}


	@Override
	public List findGeneralLedgerUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
