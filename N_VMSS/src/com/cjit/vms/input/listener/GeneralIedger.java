package com.cjit.vms.input.listener;

import org.apache.log4j.Logger;

import com.cjit.vms.input.service.PullDataService;

/**
 * 总账定时任务
 * @author jxjin
 *
 */
public class GeneralIedger implements Runnable{
	public Logger logger=Logger.getLogger(GeneralIedger.class);
	private PullDataService service;
	public GeneralIedger(PullDataService service) {
		super();
		this.service = service;
	}
	
	/*private JdbcGetGeneralIedger service;
	public GeneralIedger(JdbcGetGeneralIedger service) {
		super();
		this.service = service;
	}*/
	@Override
	public void run() {
		System.out.println("============");
		logger.info("总账数据同步任务开始执行...");
		System.out.println("执行进行中...");
		service.getGeneralIedger();
		System.out.println("执行完成...");
	}

}
