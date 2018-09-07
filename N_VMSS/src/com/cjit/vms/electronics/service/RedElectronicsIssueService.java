package com.cjit.vms.electronics.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;

/**
 * 新建
 * 日期：2018-09-07
 * 作者：刘俊杰
 * 功能：电子发票红票开具页面对应的所有service的实现类
 */
public interface RedElectronicsIssueService {
	
	/**
	 * 新增
	 * 日期：2018-09-07
	 * 作者：刘俊杰
	 * 功能：准备电子红票开具页面数据，查询电子发票红票申请通过的数据
	 * @param redReceiptApplyInfo
	 * @param paginationList
	 * @return
	 */
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo,PaginationList paginationList,String dataStatus); //带分页
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo,String dataStatus); //不带分页(页面红票导出)

}
