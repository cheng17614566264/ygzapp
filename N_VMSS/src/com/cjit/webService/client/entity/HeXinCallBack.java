package com.cjit.webService.client.entity;
/**
 * 核心回写返回结果
 * @author jxjin
 *
 */
public class HeXinCallBack {
	//状态：0.成功；1.失败
	private String resulttype;
	//失败原因
	private String errorInfo;
	public String getResulttype() {
		return resulttype;
	}
	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
}
