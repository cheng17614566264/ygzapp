package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;

public interface VmsCommonService {
	
	public Map findCodeDictionary(String codeType);
	
	public String createMark(Map map);
	public String createRedMark(Map map);
	
	public String createWord(Map map);
	
	public Map saveRedNotic(RedReceiptApplyInfo redReceiptApplyInfo);
	
	/**
	 * 纳税人名称的取得
	 * 
	 * @param info
	 * @return
	 * @author lee
	 */
	public List getAjaxTaxperNameList(Organization info);
}
