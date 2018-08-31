package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.vms.metlife.model.TtmPrcnoMatch;
import com.cjit.vms.taxdisk.servlet.model.parseXml.Bill;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.webService.client.entity.BillEntity;
import com.cjit.webService.client.entity.HeXinCallBack;

import org.apache.commons.logging.Log;

public interface BillIssueService {

	public List findBillInfoList(BillInfo billInfo, PaginationList paginationList);

	public List findBillInfoList(BillInfo billInfo);

	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType,String instId);

	public void deleteBillItemInfo(String billId);

	public void deleteTransBillInfo(String billId);

	public void deleteBillInfoById(String billId);

	public void updateTransInfoStatus(String dataStatus, String billId);

	public List findTransByBillId(String billId);

	public List findTransByBillId(String billId, PaginationList paginationList);
	
	//2018-05-02新增
	public List findBillByBillId(String billId, PaginationList paginationList);

	public BillInfo findBillInfoById(String billId);

	public List findBillItemByBillId(String billId);

	public String findRegisteredInfo(String taxDisNo);

	public List findInvalidPaperInvoice(String dataStatus, String fapiaoType);

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice);

	public void updatebillInfoIssueResult(BillInfo bill);

	public void updateBillInfoStatus(BillInfo bill);

	public TaxDiskInfo findTaxDiskInfoByTaxDiskNo(String taxDiskNo);

	public PaperInvoiceUseDetail getPaperInvoiceUseDetail(String dataStatus,String invoiceCode,String invoiceNo);
	
	public void updatePaperInvoiceListInfo(BillInfo billInfo);
	/**
	 * Abel:Metlife Begin 
	 * @param matchSaveList
	 */
	public void saveMatchInfoList(List<TtmPrcnoMatch> matchSaveList);

	public List findMatchInfoList(List matchFindList,boolean flag);

	public void updateMatchInfoList(List<TtmPrcnoMatch> matchUpdateList);

	public List findAllMatchInfoList(TtmPrcnoMatch match);
	public List findTransInfo(String billId);
	public void updateTransTemp(TransInfo transTemp);
	/**
	 * 向核心发送接口请求
	 * @return
	 */
	public String invoiceIssueAccessCore(LogManagerService logManagerService,HttpServletRequest request,User user,List transList,BillInfo billInfo,Log log);
	/**
	 *  查找web service路径
	 */

	public List<String> findWebServiceUrl(String string);
	
	/**
	 * 将核心回写失败的数据插入日志表
	 */
	public void insertFailInfo(List<BillEntity> list);
	/**
	 * 根据保单号和投保单号查询当前保单未开票的数据
	 */
	public boolean isAllIssure(String billId);
}
