package com.cjit.webService.server.entity;

public class ResponseWsdl {
	private ResponseHeader header;
	private ResponseBody body;
	public ResponseWsdl() {
	}
	
	public ResponseWsdl(ResponseHeader header, ResponseBody body) {
		super();
		this.header = header;
		this.body = body;
	}

	public ResponseHeader getHeader() {
		return header;
	}
	public void setHeader(ResponseHeader header) {
		this.header = header;
	}
	public ResponseBody getBody() {
		return body;
	}
	public void setBody(ResponseBody body) {
		this.body = body;
	}
	
}
