package com.cjit.vms.trans.service.storage;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;

/**
 * @author tom
 *
 */
public interface PaperInvoiceService {
	
	/**
	 * 根据纸质发票库存ID，获取库存发票信息
	 * 
	 * @param storeId
	 * @return
	 * @author jobell
	 */
	public List findPaperInvoiceStoreByStoreIds(String[] storeIds);
	
	/**
	 * 保存纸质发票分发结果
	 * 
	 * @param lstDistribute
	 * @author jobell
	 */
	public int savePaperInvoiceDistribute(List lstDistribute);
	

	

	
	/**
	 * 纸质发票一览初期化表示
	 * 
	 * @param PaperInfo
	 * @param paginationList
	 * @return
	 * @author cylenve
	 */
	public List getPaperInvoiceListInfo(PaperInvoiceListInfo PaperInfo, PaginationList paginationList);
	
	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet2 【发票使用情况】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet2(PaperInvoiceListInfo PaperInfo);
	
	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet3【发票领用与归还统计】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet3(PaperInvoiceListInfo PaperInfo);
	
	/**
	 * 纸质发票使用明细件数的取得
	 * 
	 * @param invoiceCode 发票代码
	 * @param invoiceNo 发票号码
	 * @return
	 * @author cylenve
	 */
	public Long getPaperInvoiceUseDetailCnt(String invoiceCode, String invoiceNo);
	
	/**
	 * 空白作废发票信息的更新
	 * 
	 * @param invoiceCode 发票代码
	 * @param invoiceNo 发票号码
	 * @param invalidReason 作废原因
	 * @return
	 * @author cylenve
	 */
	public void updateInvalidPaperInvoiceUseDetail(String invoiceCode, String invoiceNo, String invalidReason);

	/**
	 * 保存纸质发票信息
	 * 
	 * @param lstDistribute
	 */
	public int savePaperInvoiceStock(String operType,List lstStock);
	
	/**
	 * 验证纸质发票代码是否存在
	 * 
	 * @param lstDistribute
	 */
	public Long CountPaperInvoiceCode(String stockId, String invoiceCode) ;
	/**
	 * 取得纸质发票代码主体信息
	 * 
	 * @param lstDistribute
	 */
	public PaperInvoiceStock getPaperInvoiceStock(String invoiceStockId);
	/**
	 * 取得纸质发票代码信息
	 * 
	 * @param lstDistribute
	 */
	public List getPaperInvoiceStockDetail(String invoiceStockId);

	/**
	 * @param beginNo
	 * @param endNo 已打印的数量
	 * @return
	 */
	public long findYPrintInvoiceNum(int beginNo,int endNo,String billCode);
	/**
	 * @param beginNo
	 * @param endNo
	 * @return 查询 已作废的数量
	 */
	public long findYBancelInvoiceNum(int beginNo,int endNo,String billCode);

	
	public List getDictionarys(String codeType, String codeSym);
	
	public int savepaperInvoice(Map map);
	
	
}
