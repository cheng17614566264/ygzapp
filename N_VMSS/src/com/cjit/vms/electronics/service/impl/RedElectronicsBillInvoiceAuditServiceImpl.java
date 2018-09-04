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
		// 查询负数红票
		billInfo.setDataStatus("20");
		map.put("auth_inst_ids", lstTmp);
		map.put("billInfo", billInfo);
		if (paginationList == null) {
			return find(sqlId, map);
		} else {
			return find(sqlId, map, paginationList);
		}

	}
}
