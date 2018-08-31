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
			return "���뽻����ˮ�š��������ڡ����ִ���ͷ�������ʧ��,�������ظ����ݣ����鱨���Ƿ���ȷ���Ժ����ԣ�";
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
	 * ����
	 * ���ڣ�2018-08-29
	 * ���ߣ�������
	 * ���ܣ���uuid���뵽���ݿ��vms_trans_info�У�������濪Ʊʱ�������ֺϲ���Ʊ
	 */
	@Override
	public String update(VmsTransInfo vmsTransInfo) {
		try{
			getSqlMapClientTemplate().update("updateVmsTransInfo",vmsTransInfo);
		}catch(Exception e){
			return "���뽻����ˮ�š��������ڡ����ִ���ͷ�������ʧ��,�������ظ����ݣ����鱨���Ƿ���ȷ���Ժ����ԣ�";
		}
		return "Y";
	}
	//end 2018-08-29
	
	/**
	 * ����
	 * ���ڣ�2018-08-30
	 * ���ߣ�������
	 * ���ܣ�updateBillMessage---�������ݿ���Ʊ����Ϣ
	 * 		updateTransState----���Ľ���״̬
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
