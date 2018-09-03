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
import com.cjit.vms.input.jdbcLink.JdbcGetGeneralIedger;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.interval.function.service.IntervalDao;
import com.cjit.vms.trans.action.createBill.CreateBillAction;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.ws.service.impl.VmsElectronWebServiceImp;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-08
 * @描述： 定时器执行的具体任务类
 *
 */
public class IntervalTask {
	
	/**
	 * @description: 定时执行跑批的方法 （核心）
	 */
	public void TaskOfbatchRun(IntervalDao intervalDao) {
		System.out.println("开始更新核心数据...");
	/*	List<TransInfoTemp> batchRunTransInfo=intervalDao.batchRunTransInfo();
		if(batchRunTransInfo != null) {
			for(int i=0;i<batchRunTransInfo.size();i++){
				TransInfoTemp batchRunTransInfo1=batchRunTransInfo.get(i);
				Map map=new HashMap();
				map.put("BUSINESS_ID", batchRunTransInfo1.getBUSINESS_ID());
				map.put("INST_ID", batchRunTransInfo1.getINST_ID());
				map.put("QD_FLAG", batchRunTransInfo1.getQD_FLAG());
				map.put("CHERNUM", batchRunTransInfo1.getCHERNUM());
				map.put("REPNUM", batchRunTransInfo1.getREPNUM());
				map.put("TTMPRCNO", batchRunTransInfo1.getTTMPRCNO());
				map.put("CUSTOMER_NO", batchRunTransInfo1.getCUSTOMER_NO());
				map.put("ORIGCURR", batchRunTransInfo1.getORIGCURR());
				map.put("ORIGAMT", batchRunTransInfo1.getORIGAMT());
				map.put("ACCTAMT", batchRunTransInfo1.getACCTAMT());
				
				map.put("TRDT", batchRunTransInfo1.getTRDT());
				
				map.put("BATCTRCDE", batchRunTransInfo1.getBATCTRCDE());
				
				map.put("INVTYP", batchRunTransInfo1.getINVTYP());
				
				map.put("FEETYP", batchRunTransInfo1.getFEETYP());
				
				map.put("BILLFREQ", batchRunTransInfo1.getBILLFREQ());
				
				map.put("POLYEAR", batchRunTransInfo1.getPOLYEAR());
				
				map.put("HISSDTE", batchRunTransInfo1.getHISSDTE());
				
				map.put("PLANLONGDESC", batchRunTransInfo1.getPLANLONGDESC());
				
				map.put("INSTFROM", batchRunTransInfo1.getINSTFROM());
				
				map.put("INSTTO", batchRunTransInfo1.getINSTTO());
				
				map.put("OCCDATE", batchRunTransInfo1.getOCCDATE());
				
				map.put("PREMTERM", batchRunTransInfo1.getPREMTERM());
								
				map.put("TRANSTYPE", batchRunTransInfo1.getTRANSTYPE());
				
				map.put("INS_COD", batchRunTransInfo1.getINS_COD());
				
				map.put("INS_NAM", batchRunTransInfo1.getINS_NAM());
				
				map.put("AMT_CNY", batchRunTransInfo1.getAMT_CNY());
				
				map.put("TAX_AMT_CNY", batchRunTransInfo1.getTAX_AMT_CNY());
				
				map.put("INCOME_CNY", batchRunTransInfo1.getINCOME_CNY());
				
				String TAX_RATE=batchRunTransInfo1.getTAX_RATE();
				if(TAX_RATE.equals("s")||TAX_RATE.equals("S")){
					map.put("TAX_RATE", 0.06);
				}else if(TAX_RATE.equals("z")||TAX_RATE.equals("Z")){
					map.put("TAX_RATE", 0.00);
				}else if(TAX_RATE.equals("p")||TAX_RATE.equals("P")){
					map.put("TAX_RATE", 0.17);
				}else if(TAX_RATE.equals("n")||TAX_RATE.equals("N")){
					map.put("TAX_RATE", 0.03);
				}else{
					map.put("TAX_RATE", 0.00);
				}
				
				map.put("valueflage", batchRunTransInfo1.getValueflage());
				
				map.put("TransferTime", batchRunTransInfo1.getTransferTime());
				//2018-03-26新增数据来源
				map.put("DSOURCE", "HX");
			    //中间交易表插入到应用交易表
				intervalDao.insertBatchRunTransInfo(map);
				//修改中间表状态
				intervalDao.updateTempStatus(map);
			}
		}
		
		
		//客户信息遍历保存到map
		List<CustomerTemp> batchRunCustomerInfo=intervalDao.batchRunCustomerInfo();
		if(batchRunCustomerInfo != null) {
			intervalDao.deleteBatchRunCustomerInfo(); //清空即将要写入信息的表
	        for(int i=0;i<batchRunCustomerInfo.size();i++){
	        	CustomerTemp customerTemp=batchRunCustomerInfo.get(i);
	        	Map map=new HashMap();
	        	map.put("CUSTOMER_NO", customerTemp.getCUSTOMER_NO());
	        	map.put("CUSTOMER_NAME", customerTemp.getCUSTOMER_NAME());
	        	map.put("CUSTOMER_TAXNO", customerTemp.getCUSTOMER_TAXNO());
	        	map.put("CUSTOMER_ADDRESSAND", customerTemp.getCUSTOMER_ADDRESSAND());
	        	map.put("TAXPAYER_TYPE", customerTemp.getTAXPAYER_TYPE());
	        	map.put("CUSTOMER_PHONE", customerTemp.getCUSTOMER_PHONE());
	        	map.put("CUSTOMER_BANKAND", customerTemp.getCUSTOMER_BANKAND());
	        	map.put("CUSTOMER_ACCOUNT", customerTemp.getCUSTOMER_ACCOUNT());
	        	map.put("chernum", customerTemp.getChernum());
	        	
	        	try{
	        		intervalDao.insertBatchRunCustomerInfo(map);
	        		}
	        	catch(Exception e){
	        		System.out.println(e.getMessage());
	        	}
	        	
			}
	        System.out.println("batchRunCustomerInfo.size():"+batchRunCustomerInfo.size());
		}*/
		System.out.println("核心数据更新成功...");
	}
	
	
	/**
	 *  @description: 定时执行跑批的方法 （总账）
	 */
	public void TaskOfGeneralIedger(IntervalDao intervalDao) {
		/*System.out.println("开始更新总账数据...");
		GetGeneralIedger getGeneralIedger = new GetGeneralIedger();
		List list = getGeneralIedger.getGeneralIedger();
		System.out.println("远程从总账中获取到数据...");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		//获取上月日期
		String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		
		Map monthMap = new HashMap();
		intervalDao.deleteGeneralLedger(monthMap);
		
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			Map map=(Map) list.get(i);
			
			System.out.println(map);
			intervalDao.insertGeneralLedger(map);
		}
	
		System.out.println("总账数据更新成功...");*/
		
	}
	/**
	 * @description: 定时执行跑批的方法 （费控）
	 */
	public void TaskOfdataUpdate(IntervalDao intervalDao){
		System.out.println("开始更新费控数据...");
		/*List<InputInfo> inputInfo = new ArrayList<InputInfo>();
		List<InputInvoiceNew> inputInvoiceNew = new ArrayList<InputInvoiceNew>();
		
		//从中间表中查数据（主表）
		inputInfo = intervalDao.findDataByPrimary();
		//从中间表中查数据（明细表）
		inputInvoiceNew = intervalDao.findDataByDetails();
		
		System.out.println("从中间表中获取到数据");
		
		//将数据插入到应用表中（主表）
		if(inputInfo != null){
			intervalDao.insertDataByPrimary(inputInfo);
		}
		//将数据插入到应用表中（明细表）
		if(inputInvoiceNew != null){
			intervalDao.insertDataByDetails(inputInvoiceNew);
		}*/
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
				createBillAction.getTransInfoForINSCOD(temp.getCHERNUM(),temp.getCUSTOMER_ID());
			}
			//开具电子发票
			createBillAction.batchRunTimeOfElectron();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
