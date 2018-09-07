package com.cjit.vms.electronics.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.electronics.service.RedElectronicsIssueService;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;

/**
 * 新建
 * 日期：2018-09-07
 * 作者：刘俊杰
 * 功能：电子发票红票开具页面对应的所有service的实现类
 */
public class RedElectronicsIssueServiceImpl extends GenericServiceImpl implements RedElectronicsIssueService {

	private static final long serialVersionUID = 6611717935290267576L;

	/**
	 * 新增
	 * 日期：2018-09-07
	 * 作者：刘俊杰
	 * 功能：准备电子红票开具页面数据，查询电子发票红票申请通过的数据
	 * @param redReceiptApplyInfo
	 * @param paginationList
	 * @return
	 */
	@Override
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo, PaginationList paginationList, String dataStatus) {
		Map map = new HashMap(); 
		List instIds = redReceiptApplyInfo.getLstAuthInstId(); 
		List lstTmp = new ArrayList(); 
		for (int i = 0; i < instIds.size(); i++) { 
			Organization org = (Organization) instIds.get(i); 
			lstTmp.add(org.getId()); 
		} 
		map.put("auth_inst_ids", lstTmp);
		System.err.println("dataStatus: "+dataStatus);
		if (dataStatus != null && "3,7".equals(dataStatus)) { 
			redReceiptApplyInfo.setDatastatus(null); 
			map.put("issueRedStatuses", dataStatus.split(",")); 
		} 
		map.put("redReceiptApplyInfo", redReceiptApplyInfo); 
		map.put("dataStatus", dataStatus);
		return find("findElectronicsBillInfoIssueList", map, paginationList);
	}

	@Override
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo, String dataStatus) {
		Map map = new HashMap();
		if (dataStatus != null && "3,7".equals(dataStatus)) {
			redReceiptApplyInfo.setDatastatus(null);
			map.put("issueRedStatuses", dataStatus.split(","));
		}
		map.put("redReceiptApplyInfo", redReceiptApplyInfo);
		map.put("dataStatus", dataStatus);
		return find("findElectronicsBillInfoIssueList", map);
	}

}
