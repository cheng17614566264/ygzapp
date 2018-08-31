package com.cjit.vms.BatchRun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.BatchRun.service.MyTestService;

public class MyTestServiceImpl extends GenericServiceImpl implements MyTestService {
	
	//查询测试内容
	@Override
	public List findTestContent(String cname) throws Exception {
		Map map = new HashMap();
		map.put("cname", cname); // 对参数进行封装
		return this.find("findTestContent",map);
	}

}
