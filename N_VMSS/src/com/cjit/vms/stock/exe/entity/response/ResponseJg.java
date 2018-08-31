package com.cjit.vms.stock.exe.entity.response;

public class ResponseJg {

	private ResponseBody responseBody;
	private ResponseHeader responseHeader;
	public ResponseBody getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(ResponseBody responseBody) {
		this.responseBody = responseBody;
	}
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public ResponseJg(ResponseBody responseBody, ResponseHeader responseHeader) {
		super();
		this.responseBody = responseBody;
		this.responseHeader = responseHeader;
	}
	
}
