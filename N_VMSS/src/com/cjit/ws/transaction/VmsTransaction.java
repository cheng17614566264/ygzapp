package com.cjit.ws.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjit.ws.dao.VmsCustomerInfoDao;
import com.cjit.ws.dao.VmsTransInfoDao;
import com.cjit.ws.dao.VmsTransTypeDao;
import com.cjit.ws.entity.VmsCustomerInfo;
import com.cjit.ws.entity.VmsTransInfo;
import com.cjit.ws.entity.VmsTransType;

@Service
public class VmsTransaction {
	@Autowired
	VmsCustomerInfoDao vmsCustomerInfoDao;
	@Autowired
	VmsTransInfoDao vmsTransInfoDao;
	@Autowired
	VmsTransTypeDao vmsTransTypeDao;

	@Transactional
	public String insertBatch(List<VmsCustomerInfo> vmsCustomerInfos,List<VmsTransInfo> vmsTransInfos, List<VmsTransType> vmsTransTypes) {
		try {
			for (VmsCustomerInfo vmsCustomerInfo : vmsCustomerInfos) {
				vmsCustomerInfoDao.insert(vmsCustomerInfo);
			}
		} catch (Exception e) {
			return "插入交易流水号失败,可能是重复数据，请检查报文是否正确，稍后重试！";
		}
		try {
			for(VmsTransInfo vmsTransInfo:vmsTransInfos){
				vmsTransInfoDao.insert(vmsTransInfo);
			}
		} catch (Exception e) {
			return "插入交易流水号或交易日期失败,可能是重复数据，请检查报文是否正确，稍后重试！";
		}
		try {
			for(VmsTransType vmsTransType:vmsTransTypes){
				vmsTransTypeDao.insert(vmsTransType);
			}
		} catch (Exception e) {
			return "插入险种代码失败,可能是重复数据，请检查报文是否正确，稍后重试";
		}
		return "Y";
	}
}
