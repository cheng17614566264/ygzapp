package com.cjit.vms.trans.model;

import java.util.ArrayList;
import java.util.List;

public class VmsBussInfo {
	private List<BussInfo> bussList=new ArrayList<BussInfo>();

	public List<BussInfo> getBussList() {
		return bussList;
	}

	public void setBussList(List<BussInfo> bussList) {
		this.bussList = bussList;
	}
	
}
