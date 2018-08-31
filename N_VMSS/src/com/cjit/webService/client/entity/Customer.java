package com.cjit.webService.client.entity;

public class Customer {
	//客户编号
	private String customerNo;
	//客户纳税人名称
	private String customerName;
	//客户纳税人识别号
	private String customerTaxno;
	//客户地址
	private String customerAddressand;
	//客户纳税人类别	S-小规模纳税人 G-一般纳税人 O-其他
	private String taxpayerType;
	//客户电话
	private String customerPhone;
	//客户开户行
	private String customerBankand;
	//客户开户行账号
	private String customerAccount;
	//客户是否打票 A - 自动打印 M - 手动打印 N - 永不打印
	private String customerFapiaoFlag;
	//数据来源  1 - 手工 2 - 系统
	private String dataSource;
	//发票类型 0-增值税专用发票 1-增值税普通发票
	private String fapiaoType;
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerTaxno() {
		return customerTaxno;
	}
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}
	public String getCustomerAddressand() {
		return customerAddressand;
	}
	public void setCustomerAddressand(String customerAddressand) {
		this.customerAddressand = customerAddressand;
	}
	public String getTaxpayerType() {
		return taxpayerType;
	}
	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerBankand() {
		return customerBankand;
	}
	public void setCustomerBankand(String customerBankand) {
		this.customerBankand = customerBankand;
	}
	public String getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerNo == null) ? 0 : customerNo.hashCode());
		result = prime * result + ((customerTaxno == null) ? 0 : customerTaxno.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customerNo == null) {
			if (other.customerNo != null)
				return false;
		} else if (!customerNo.equals(other.customerNo))
			return false;
		if (customerTaxno == null) {
			if (other.customerTaxno != null)
				return false;
		} else if (!customerTaxno.equals(other.customerTaxno))
			return false;
		return true;
	}
	public String getCustomerFapiaoFlag() {
		return customerFapiaoFlag;
	}
	public void setCustomerFapiaoFlag(String customerFapiaoFlag) {
		this.customerFapiaoFlag = customerFapiaoFlag;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	
}
