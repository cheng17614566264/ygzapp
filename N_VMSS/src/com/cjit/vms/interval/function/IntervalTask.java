package com.cjit.vms.interval.function;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.cjit.common.util.SpringContextUtil;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.input.action.InvoiceScanAuthAction;
import com.cjit.vms.input.jdbcLink.JdbcGetGeneralIedger;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.service.InvoiceScanAuthService;
import com.cjit.vms.input.service.impl.InvoiceScanAuthServiceImpl;
import com.cjit.vms.interval.function.service.IntervalDao;
import com.cjit.vms.trans.action.createBill.CreateBillAction;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.webService.client.entity.ApplicationForm;
import com.cjit.ws.common.utils.Utils;
import com.cjit.ws.service.impl.VmsElectronWebServiceImp;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-08
 * @描述： 定时器执行的具体任务类
 *
 */
public class IntervalTask {
	
	/**
	 * 新增
	 * 日期：2018-09-04
	 * 作者：刘俊杰
	 * @description: 定时执行跑批的方法 （核心）
	 */
	public void TaskOfbatchRun() {
		System.out.println("开始更新核心数据...");
		ApplicationForm applicationForm = new ApplicationForm();
		applicationForm.setRequestionType("auto");  //设置跑批类型为自动跑批
		//通过spring获取CreateBillAction实例对象
		ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
		CreateBillAction createBillAction = (CreateBillAction)applicationContext.getBean("createBillAction");
		createBillAction.syncDataOfCore(applicationForm);
		System.out.println("核心数据更新成功...");
	}
	
	
	/**
	 * 新增
	 * 日期：2018-09-04
	 * 作者：刘俊杰
	 *  @description: 定时执行跑批的方法 （总账）
	 */
	public void TaskOfGeneralIedger() {
		System.out.println("开始更新总账数据...");
		//通过spring获取InvoiceScanAuthAction实例对象
		ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
		InvoiceScanAuthAction invoiceScanAuthAction = (InvoiceScanAuthAction)applicationContext.getBean("invoiceScanAuthAction");
		//远程连接总账数据库获取数据
		List list =invoiceScanAuthAction.getGeneralIedger();
		System.out.println("远程从总账中获取到数据...");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		//获取上月日期
		String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		System.out.println("上月:"+yearMonth);
		Map monthMap = new HashMap();
		monthMap.put("month", yearMonth);
		//通过spring获取InvoiceScanAuthService实例对象
		InvoiceScanAuthService invoiceScanAuthService = (InvoiceScanAuthServiceImpl)applicationContext.getBean("invoiceScanAuthService");
		//删除重复数据
		invoiceScanAuthService.deleteGeneralLedger(monthMap);
		
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			Map map=(Map) list.get(i);
			
			System.out.println(map);
			invoiceScanAuthService.insertGeneralLedger(map);
		}
	
		System.out.println("总账数据更新成功...");
		
	}
	/**
	 * 新增
	 * 日期：2018-09-04
	 * 作者：刘俊杰
	 * @description: 定时执行跑批的方法 （费控）
	 */
	public void TaskOfdataUpdate(){
		System.out.println("开始更新费控数据...");
		
		//通过spring获取InvoiceScanAuthService实例对象
		ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
		InvoiceScanAuthService invoiceScanAuthService = (InvoiceScanAuthServiceImpl)applicationContext.getBean("invoiceScanAuthService");
		
		List<InputInfo> inputInfo = new ArrayList<InputInfo>();
		List<InputInvoiceNew> inputInvoiceNew = new ArrayList<InputInvoiceNew>();
		
		//从中间表中查数据（主表）
		inputInfo = invoiceScanAuthService.findDataByPrimary();
		//从中间表中查数据（明细表）
		inputInvoiceNew = invoiceScanAuthService.findDataByDetails();
		
		System.out.println("从中间表中获取到数据");
		
		//将数据插入到应用表中（主表）
		if(inputInfo != null){
			invoiceScanAuthService.insertDataByPrimary(inputInfo);
		}
		//将数据插入到应用表中（明细表）
		if(inputInvoiceNew != null){
			invoiceScanAuthService.insertDataByDetails(inputInvoiceNew);
		}
		System.out.println("费控数据更新成功...");
	}


	/**
	 * 新增
	 * 日期：2018-09-03
	 * 作者：刘俊杰
	 * 功能：从vms_trans_info表中查询出犹豫期的电子发票(个险,首期),如果过了犹豫期,则调用税控开票
	 * @param intervalDao
	 */
	public void TaskOfYouyuqi(IntervalDao intervalDao) {
		try {
			//从vms_trans_info表中查询出犹豫期的电子发票(个险,首期)
			List<TransInfoTemp> transinfoList = intervalDao.selectTransInfoOfYouyuqi();
			//通过spring获取CreateBillAction的实例
			ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
			CreateBillAction createBillAction =  (CreateBillAction) applicationContext.getBean("createBillAction");
			//封装数据
			for(TransInfoTemp temp : transinfoList) {
				createBillAction.getTransInfoForINSCOD(temp.getCHERNUM(),temp.getCUSTOMER_ID(),false);
			}
			//开具电子发票
			createBillAction.batchRunTimeOfElectron(Utils.dfxj1001);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
