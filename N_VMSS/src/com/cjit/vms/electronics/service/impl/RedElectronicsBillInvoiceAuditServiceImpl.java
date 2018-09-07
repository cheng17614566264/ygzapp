package com.cjit.vms.electronics.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.electronics.service.RedElectronicsBillInvoiceAuditService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;

/**
 * 红票开具审核
 * 
 * @author Administrator
 *
 */
public class RedElectronicsBillInvoiceAuditServiceImpl extends
		GenericServiceImpl implements RedElectronicsBillInvoiceAuditService {

	private static final long serialVersionUID = 1L;

	@Override
	public List findRedElectronicsAuditList(String sqlId, BillInfo billInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billInfo", billInfo);
		if (paginationList == null) {
			return find(sqlId, map);
		} else {
			return find(sqlId, map, paginationList);
		}

	}

	
	/**
	 * 电子发票红冲查询票据
	 * cheng 0907 新增
	 * @return
	 */
	@Override
	public BillInfo findElectronicsBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List list = find("findElectronicsBillInfoByBillId", map);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 电子发票红冲审核更新
	 * cheng 0907 新增
	 * @return
	 */
	/**
	 * 【红冲审核】页面[审核通过]
	 */
	@Override
	public void saveElectronicsBillInfo(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		if (isUpdate) {
			this.save("updateElectronicesRedBill", param);
		} else {
			this.save("saveBill", param);
		}
	}
	
}
