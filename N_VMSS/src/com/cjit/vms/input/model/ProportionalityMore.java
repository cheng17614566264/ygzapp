package com.cjit.vms.input.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 通过List<Proportionality> 获取得到机构对应 -- 比例值数据
 *
 */
public class ProportionalityMore {
	//机构编号
	private String instId;
	//会计机构编号
	private String kjInstId;
	//会计月度
	private String yearMonth;
	//比例值
	private  List<String> resultlist;
	//有效值 0-无效  1-有效 2-待审核 3-审核拒绝
	private List<String> availablelist;
	//数据来源(0:定时任务  1:人工计算)
	private List<String> datasourcelist;
	// 被除数 分子
	private List<String> dividendlist ;
	//除数  分母
	private List<String> divisorlist ;
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getKjInstId() {
		return kjInstId;
	}
	public void setKjInstId(String kjInstId) {
		this.kjInstId = kjInstId;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public List<String> getResultlist() {
		return resultlist;
	}
	public void setResultlist(List<String> resultlist) {
		this.resultlist = resultlist;
	}
	public List<String> getAvailablelist() {
		return availablelist;
	}
	public void setAvailablelist(List<String> availablelist) {
		this.availablelist = availablelist;
	}
	public List<String> getDatasourcelist() {
		return datasourcelist;
	}
	public void setDatasourcelist(List<String> datasourcelist) {
		this.datasourcelist = datasourcelist;
	}
	public List<String> getDividendlist() {
		return dividendlist;
	}
	public void setDividendlist(List<String> dividendlist) {
		this.dividendlist = dividendlist;
	}
	public List<String> getDivisorlist() {
		return divisorlist;
	}
	public void setDivisorlist(List<String> divisorlist) {
		this.divisorlist = divisorlist;
	}
	
}
