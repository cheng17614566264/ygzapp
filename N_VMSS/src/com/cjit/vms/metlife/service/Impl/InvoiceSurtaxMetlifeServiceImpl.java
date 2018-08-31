package com.cjit.vms.metlife.service.Impl;
/** 
 *  createTime:2016.3
 * 	author:沈磊
 *	content:会计分录  metlife
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.service.InvoiceSurtaxMetlifeService;
import com.cjit.vms.trans.model.InputInvoiceInfo;

public class InvoiceSurtaxMetlifeServiceImpl extends GenericServiceImpl implements InvoiceSurtaxMetlifeService{

	@Override
	public List findInvoiceInSurtaxList(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findInvoiceSurtaxMetlife", map, paginationList);
	}

	@Override
	public List findListInvoice(InputInvoiceInfo inputInvoiceInfo) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findListInvoice", map);
	}

	@Override
	public void updateInvoiceInfo(List infolist) {
		this.updateBatch("updateInvoiceInfo", infolist);
		
	}

	@Override
	public List findTansferOutRatio(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findTansferOutRatio", map, paginationList);
	}

	@Override
	public List updatetransferOutRatio() {
		//this.updateBatch("updatetransferOutRatio", transferOutRatioList);
		Map map=new HashMap();
		return this.find("updatetransferOutRatio", map);
	}

	
	
	@Override
	public void updatetransferOutRatio1(List<InputInvoiceInfo> transferOutRatio) {
		// TODO Auto-generated method stub
		this.updateBatch("updatetransferOutRatio1", transferOutRatio);
	}

	@Override
	public void insertInvoceInfo(List listbillinfo) {
		try{
			
		this.insertBatch("insertInvoceInfoBatch", listbillinfo);
		this.insertBatch("insertInvoceInfoBatch1",listbillinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updatetransInfo(List listbillinfo) {
		this.updateBatch("updatetransInfostatusbatch", listbillinfo);
		
	}

	@Override
	public void insertcommission(List listbillinfo) {

			this.insertBatch("insertInvoiceCommission", listbillinfo);
			this.insertBatch("insertInvoiceCommission1", listbillinfo);
			this.insertBatch("insertInvoiceCommission2", listbillinfo);
			this.updateBatch("updateIncome", listbillinfo);

		
	}

	@Override
	public List findFinanceMonth(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findFinanceMonth", map, paginationList);
	}

	@Override
	public void insertFinanceMonth(List<Map<String, String>> dataList) {
			this.insertBatch("insertFinanceMonth", dataList);
	}

	@Override
	public void cancelFinanceMonth(List cancelFinanceMonth) {
		this.deleteBatch("cancelFinanceMonth", cancelFinanceMonth);
		
	}



}
