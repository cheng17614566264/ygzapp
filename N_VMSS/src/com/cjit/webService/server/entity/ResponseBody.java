package com.cjit.webService.server.entity;

public class ResponseBody {
	//处理结果 成功或失败
	private String result;
	//失败时用于记录失败原因
	private String message;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
