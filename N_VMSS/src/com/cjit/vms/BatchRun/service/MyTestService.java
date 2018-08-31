package com.cjit.vms.BatchRun.service;

import java.util.List;

public interface MyTestService {
	//查询测试内容
	public List findTestContent(String cname) throws Exception;
}
