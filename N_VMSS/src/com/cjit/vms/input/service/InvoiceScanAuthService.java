package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.vms.input.model.InputInfo;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.model.TimeTaskEntity;
import com.cjit.vms.trans.model.UBaseInst;

public interface InvoiceScanAuthService {
	/**
	 * 票据查询
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 */
	public List<InputInfo> findListInvoiceScanAuth(InputInfo info,	PaginationList paginationList);
	public List<InputInfo> findListInvoiceScanAuth(InputInfo info);
	
	/**
	 * 获取票据信息
	 * 
	 * @param billId
	 * @return
	 */
	public InputInfo findInvoiceScanAuthByBillId(String billId); 
	
	public List<InputInfo> findInvoiceScanAuthListByBillId(String billId); 
	
	/**
	 * 获取商品列表
	 * 
	 * @param billId
	 * @return
	 */
	public List findInvoiceScanAuthItemsByBillId(String billId);

	/**
	 * 获取票据交易数据
	 * 
	 * @param billId
	 * @return
	 */
	public InputTransInfo findInvoiceScanAuthTransInfoByBillId(String bill_code,String bill_no);

//	public void updateVmsInputInvoiceInfoForScanAuth(InputInvoiceInfo inputInvoiceInfo, List lstGoodAdded,String o_bill_id); 
	public void updateVmsInputInvoiceInfoForScanAuth(InputInfo inputInfo, String o_bill_id); 
	
	public void updateVmsInputInvoiceInfoForAuthSubmit(String datastatus, String billId,String billCode);
	public void saveVmsInputInvoiceInfoImport(List dataList);
	
	/**
	 * 新增商品信息
	 * @param item
	 */
	public void insertVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 编辑更新商品信息
	 * @param item 
	 */
	public void updateVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 删除商品信息
	 * @param item
	 */
	public void deleteVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 获取新增商品信息ID
	 * 
	 * @return
	 */
	public String findSequenceBillItemId();

	public InputInvoiceInfo findInvoiceScanAuthByBillCodeAndBillNo(
			String billCode, String billNo); 
	public UBaseInst findUbaseInstByTaxNo(String taxNo);
	
	public void saveInputinvoiceData(Map<String,String> map);
	public void saveInputinvoiceInfo(Map<String,String> map);
	public List<String> findinputInvoiceCompareinvoiceData(Map<String,String> map);
	
	public void updateInputInvoiceYconformFlg(Map<String,String> map);

	public void updateNeedEnterInputBill(List<String> billList);
	public List<BillDetailEntity> finBillDetailEntites(List<String> billList);
	public List<String> findRepeatBill(List<String> bills);
	public void insertEnterOkInputBill(List<String> bills);
	public List<String> findWebServiceUrl(String name);
	
	//数据更新
	//从中间表中查出数据（主表）
	public List<InputInfo> findDataByPrimary();
	//从中间表中查出数据（明细表）
	public List<InputInvoiceNew> findDataByDetails();
	
	//将数据插入到应用表（主表）
	public void insertDataByPrimary(List<InputInfo> data);
	//将数据插入到应用表（明细表）
	public void insertDataByDetails(List<InputInvoiceNew> data);
	
	//定时器
	public List<TimeTaskEntity> findTimeTisk(String dataMold);
	/**
	 * 新增
	 * 日期：2018-08-22
	 * 作者：刘俊杰
	 * 功能：通过远程连接总账的数据库(oracle)
	 */
	//start
	public void deleteGeneralLedger(Map monthMap);
	public void insertGeneralLedger(Map map);
	//end
}
