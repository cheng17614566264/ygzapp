package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

/**
 * 新增
 * @作者： 刘俊杰
 * @日期： 2018-08-22
 * @描述： 远程从总账中获取到数据
 */
public interface GetGeneralIedgerService {
	
	public List getGeneralIedgerTempOfLongdistance(Map map);
}
