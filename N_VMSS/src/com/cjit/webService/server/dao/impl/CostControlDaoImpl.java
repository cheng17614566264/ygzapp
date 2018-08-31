package com.cjit.webService.server.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.webService.server.dao.CostControlDao;
import com.cjit.webService.server.entity.InputInfoTemp;
import com.cjit.webService.server.util.DataUtil;

public class CostControlDaoImpl extends GenericServiceImpl implements CostControlDao {

	@Override
	public void saveWebserviceInfo(String info, String source) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", source);
		map.put("info", info);
		this.save("saveWebserviceInfo", map);
	}


	@Override
	public void updateRollOut(List<BillDetailEntity> list) {
		this.updateBatch("rollOutInfo", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findReInputInfoTemp(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		return this.find("reInputInfoTemp", map);
	}

	@Override
	public void saveInputInfoTemp(List<InputInfoTemp> insertList) {
		this.insertBatch("insertInputInfoTemp", insertList);
	}

	@Override
	public void deleteReInputInfoTemp(String reId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", reId);
		this.delete("deleteReInputInfoTemp", map);
	}

	@Override
	public void deleteInputInfoNew(String bill) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("bill", bill);
		this.delete("deleteInputInfoNew", map);
	}

	@Override
	public void deleteInputInfoNewItem(String id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", id);
		this.delete("deleteInputInfoNewItem", map);
		
	}

	@Override
	public void saveInputInfo(InputInfoTemp inputInfoTemp) {
		Map<String, InputInfoTemp> map=new HashMap<String, InputInfoTemp>();
		map.put("inputInfoTemp", inputInfoTemp);
		this.save("insertInputInfoNew", map);
		this.save("insertInputInfoNewItem", map);
		// 新需求添加
		if(DataUtil.FAPIAO_TOLL.equals(inputInfoTemp.getBillType())){
			this.updateInputInfoBillStatu(inputInfoTemp);
		}
	}
	private void updateInputInfoBillStatu(InputInfoTemp inputInfoTemp){
		Map<String, String> idMap=new HashMap<String, String>();
		idMap.put("billId", inputInfoTemp.getBillId());
		idMap.put("billCode", inputInfoTemp.getBillCode());
		idMap.put("billStatu", inputInfoTemp.getBillStatus());
		idMap.put("operateStatus", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		this.update("updateInputInfoBillStatu", idMap);
	}
	
	@Override
	public void updateInputInfoTemp(List<InputInfoTemp> list) {
		this.updateBatch("updateInputInfoTemp", list);
	}


	@Override
	public void updateInputInfo(InputInfoTemp inputInfo) {
		Map<String, InputInfoTemp> map=new HashMap<String, InputInfoTemp>();
		map.put("inputInfoTemp", inputInfo);
		this.save("insertInputInfoNew", map);
		this.update("updateInputInfo", map);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> findWebServiceUrl(String name) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", name);
		return this.find("findWebserviceUrl", map);
	}


	@Override
	public List<String> getRoutAmt(String id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("ID", id);
		return this.find("findGetRoutAmt", map);
	}

}
