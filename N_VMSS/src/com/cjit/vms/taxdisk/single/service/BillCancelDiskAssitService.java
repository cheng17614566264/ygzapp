package com.cjit.vms.taxdisk.single.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.cjit.vms.taxdisk.servlet.model.parseXml.BillCancelReturnXml;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public interface BillCancelDiskAssitService {

	
	/**查看票据
	 *   @param billId  
	 *   @return
	 */
	 public BillInfo findBillInfo(String billId);
	 

	/**查看税控盘
	 * @param taxDiskNo 
	 * @return
	 */
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo);
	
	
	/**更改票据状态
	 * @param StringXml
	 * @throws Exception 
	 */
	public AjaxReturn updateBillCancelResult(String billId,String returnMsg,boolean flag) throws Exception;
	/**更改票据状态
	 * @param StringXml
	 * @throws Exception 
	 */
	public AjaxReturn updateBillCancelResult(List<String> billId,List<BillCancelReturnXml> billCancelList,boolean flag) throws Exception;
	public void updateBillStatisticsCount(String billId) ;
	
	/**发票作废交易类型
	 * @param billId
	 * @return
	 */
	public List billCancelTransInfo(String billId);
	
	
	/**释放交易
	 * @param billId
	 * @param transId
	 */
	public void openBillCancelTrans(String billId);
	
}
