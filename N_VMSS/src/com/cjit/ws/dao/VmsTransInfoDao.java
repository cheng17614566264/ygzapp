package com.cjit.ws.dao;

import java.util.Map;

import com.cjit.ws.entity.VmsTransInfo;


public interface VmsTransInfoDao {
	public String insert(VmsTransInfo vmsTransInfo);
	
	public String find(Map map);
	/**
	 * 新增
	 * 日期：2018-08-29
	 * 作者：刘俊杰
	 */
	public String update(VmsTransInfo vmsTransInfo);
	//end 2018-08-29

	/**
	 * 新增
	 * 日期：2018-08-30
	 * 作者：刘俊杰
	 * 功能：updateBillMessage---更新数据库中票据信息
	 * 		updateTransState----更改交易状态
	 * @param maps
	 */
	public void updateBillMessage(Map maps);
	public void updateTransState(Map maps);
	//end 2018-08-30

	/**
	 * 新增
	 * 日期：2018-09-04
	 * 作者：刘俊杰
	 * 功能：电子发票开具失败，将错误原因回写到数据库表vms_trans_info ---- CANCELREASON
	 * @param maps
	 */
	public void insertCANCELREASON(Map maps);
	//end 2018-09-04
}
