package com.cjit.vms.input.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.service.InputInvoiceInfoService;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.trans.util.SqlUtil;

public class InputInvoiceInfoServiceImpl  extends GenericServiceImpl  implements InputInvoiceInfoService {

	public List getInputInvoiceInfoList(InputInvoiceInfo info,
			PaginationList paginationList) {
		String defV = "180";
		if(null!=info.getBillStartDate() && !"".equals(info.getBillStartDate())){
			defV = "";
		}else if(null!=info.getBillEndDate() && !"".equals(info.getBillEndDate())){
			defV = "";
		}
		Map map = new HashMap();
		map.put("instCode", info.getInstcode());
		map.put("billDate", info.getBillDate());
		map.put("vendorTaxno", info.getVendorTaxno());
		map.put("datastatus", info.getDatastatus());
		map.put("billCode", info.getBillCode());
		map.put("billNo", info.getBillNo());
		map.put("fapiaoType", info.getFapiaoType());
		map.put("billStartDate", info.getBillStartDate());
		map.put("billEndDate", info.getBillEndDate());
		map.put("defV", defV);
		map.put("userId", info.getUserId());
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", SqlUtil.arr2String(lstTmp.toArray(), ",")); 
		if(paginationList==null){
			return this.find("getInputInvoiceInfoList", map);
		}else{
			return this.find("getInputInvoiceInfoList", map,paginationList);
		}
	}

	public InputInvoiceInfo getInputInvoiceInfoDetail(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return (InputInvoiceInfo) this.findForObject("findInputInvoiceInfoDetail", map);
	}

	public List getInputInvoiceItemList(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		return this.find("getInputInvoiceItemList", map);
	}

	public List getInputInvoiceTransList(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		return this.find("getInputInvoiceTransInfoList", map);
	}
	
}
