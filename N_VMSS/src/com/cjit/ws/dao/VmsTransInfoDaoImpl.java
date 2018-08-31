package com.cjit.ws.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.vms.input.model.CommonCode;
import com.cjit.ws.entity.VmsTransInfo;

public class VmsTransInfoDaoImpl  extends SqlMapClientDaoSupport  implements VmsTransInfoDao{

	@Override
	public String insert(VmsTransInfo vmsTransInfo) {
		try{
			getSqlMapClientTemplate().insert("insertVmsTransInfo",vmsTransInfo);
		}catch(Exception e){
			return "插入交易流水号、交易日期、险种代码和费用类型失败,可能是重复数据，请检查报文是否正确，稍后重试！";
		}
		return "Y";
	}

	@Override
	public String find(Map map) {
		List list = new ArrayList();
		list =  getSqlMapClientTemplate().queryForList("selectCommonCode", map);

		if (list.size() > 0 && list != null){
			CommonCode commonCode = (CommonCode) list.get(0);
			return commonCode.getName();

		}
		return null;
	}
	
	/**
	 * 新增
	 * 日期：2018-08-29
	 * 作者：刘俊杰
	 * 功能：将uuid插入到数据库表vms_trans_info中，方便后面开票时根据险种合并开票
	 */
	@Override
	public String update(VmsTransInfo vmsTransInfo) {
		try{
			getSqlMapClientTemplate().update("updateVmsTransInfo",vmsTransInfo);
		}catch(Exception e){
			return "插入交易流水号、交易日期、险种代码和费用类型失败,可能是重复数据，请检查报文是否正确，稍后重试！";
		}
		return "Y";
	}
	//end 2018-08-29
	
	/**
	 * 新增
	 * 日期：2018-08-30
	 * 作者：刘俊杰
	 * 功能：updateBillMessage---更新数据库中票据信息
	 * 		updateTransState----更改交易状态
	 * @param maps
	 */
	@Override
	public void updateBillMessage(Map maps) {
		try {
			String billDate = maps.get("BillDate").toString();
			SimpleDateFormat formatStr = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = formatStr.parse(billDate);
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			billDate = formatDate.format(date);
			maps.put("BillDate", billDate);
			getSqlMapClientTemplate().update("updateVmsBillInfoMessage", maps);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateTransState(Map maps) {
		try {
			String billDate = maps.get("BillDate").toString();
			SimpleDateFormat formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = formatStr.parse(billDate);
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			billDate = formatTime.format(date);
			maps.put("BillTime", billDate);
			getSqlMapClientTemplate().update("updateVmsTransInfoState", maps);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	//end 2018-08-30

}
