package com.cjit.webService.server.entity;

import java.util.List;

import com.cjit.vms.input.model.BillDetailEntity;
import com.google.gson.Gson;

public class RollOutBody {
	private List<BillDetailEntity> billDetailList;

	public List<BillDetailEntity> getBillDetailList() {
		return billDetailList;
	}

	public void setBillDetailList(List<BillDetailEntity> billDetailList) {
		this.billDetailList = billDetailList;
	}

}
