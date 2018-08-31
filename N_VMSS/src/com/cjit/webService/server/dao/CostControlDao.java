package com.cjit.webService.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.webService.server.entity.InputInfoTemp;

public interface CostControlDao {
	/**
	 * 保存webservice报文信息到vms_webservice_log
	 * @param info
	 * @param source
	 */
	public void saveWebserviceInfo(String info,String source);
	/**
	 * 保存进项信息到vms_input_invoice_new和vms_input_invoice_new_item
	 */
	public void saveInputInfo(InputInfoTemp inputInfoTemp);
	/**
	 * 更新转出税额
	 * @param list
	 */
	public void updateRollOut(List<BillDetailEntity> list);
	/**
	 * 查询中间表已经存在的ID
	 * @param idSet
	 */
	public List<String> findReInputInfoTemp(String id);
	
	public void saveInputInfoTemp(List<InputInfoTemp> insertList);
	/**
	 * 删除重复的再重新插入
	 * @param reId
	 */
	public void deleteReInputInfoTemp(String reId);
	public void deleteInputInfoNew(String bill);
	public void deleteInputInfoNewItem(String id);
	public void updateInputInfoTemp(List<InputInfoTemp> list);
	public void updateInputInfo(InputInfoTemp inputInfo);
	public List<String> findWebServiceUrl(String string);
	
	public List<String> getRoutAmt(String id);
}
