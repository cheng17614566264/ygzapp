package com.cjit.vms.input.listener;

import org.apache.log4j.Logger;

import com.cjit.vms.input.service.InvoiceScanAuthService;


/**
 * 数据更新定时任务
 */

public class RegularlyUpdateData implements Runnable{
	public Logger logger=Logger.getLogger(RegularlyUpdateData.class);
	private InvoiceScanAuthService service;
	public RegularlyUpdateData(InvoiceScanAuthService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		System.out.println("============");
		logger.info("数据更新任务开始执行...");
		System.out.println("执行进行中...");
		//主表数据更新
		service.insertDataByPrimary(service.findDataByPrimary());
		//明细表数据更新
		service.insertDataByDetails(service.findDataByDetails());
		System.out.println("执行完成...");
	}

}
