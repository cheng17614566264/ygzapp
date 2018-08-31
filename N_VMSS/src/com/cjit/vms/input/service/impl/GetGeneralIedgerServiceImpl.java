package com.cjit.vms.input.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.vms.input.service.GetGeneralIedgerService;

/**
 * 新增
 * @作者： 刘俊杰
 * @日期： 2018-08-22
 * @描述： 远程从总账中获取到数据
 */
public class GetGeneralIedgerServiceImpl extends SqlMapClientDaoSupport implements GetGeneralIedgerService {

	@Override
	public List getGeneralIedgerTempOfLongdistance(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getGeneralIedgerTempOfLongdistance", map);
	}
}
