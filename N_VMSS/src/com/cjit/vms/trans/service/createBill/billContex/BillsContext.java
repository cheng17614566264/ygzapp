package com.cjit.vms.trans.service.createBill.billContex;

import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.util.DataUtil;

public class BillsContext {

	List<BillContext> billListZhuanPiao = new ArrayList<BillContext>();
	List<BillContext> billListPuPiao = new ArrayList<BillContext>();
    //电子发票  2018-07-10
	List<BillContext> billListDianziPiao = new ArrayList<BillContext>();

	public List<BillContext> getBillListByFapiaoType(String fapiaoType) {
		if (DataUtil.VAT_TYPE_0.equals(fapiaoType)) {
			if (null == billListZhuanPiao) {
				billListZhuanPiao = new ArrayList<BillContext>();
			}
			return billListZhuanPiao;
		}
		if (DataUtil.VAT_TYPE_1.equals(fapiaoType)) {
			if (null == billListPuPiao) {
				billListPuPiao = new ArrayList<BillContext>();
			}
			return billListPuPiao;
		}
		//2018-07-10 电票新增
		if (DataUtil.VAT_TYPE_2.equals(fapiaoType)) {
			if (null == billListDianziPiao) {
				billListDianziPiao = new ArrayList<BillContext>();
			}
			return billListDianziPiao;
		}
		
		return new ArrayList<BillContext>();
	}

	public List<BillContext> getAllBillList() {
		List<BillContext> list = new ArrayList<BillContext>();
		list.addAll(getBillListByFapiaoType(DataUtil.VAT_TYPE_0));
		list.addAll(getBillListByFapiaoType(DataUtil.VAT_TYPE_1));
		//电子发票 2018-07-10
		list.addAll(getBillListByFapiaoType(DataUtil.VAT_TYPE_2));
		return list;
	}
}
