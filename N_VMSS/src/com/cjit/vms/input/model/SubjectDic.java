package com.cjit.vms.input.model;

import java.math.BigDecimal;

/**
 * 新增
 * 日期：2018-08-27
 * 作者：刘俊杰
 * 说明：科目字典实体类--vms_dic_subject表
 */
public class SubjectDic {
	int ID;
	String SUBJECT_ID;
	String SUBJECT_NAME;
	String CATEGORY;
	BigDecimal VALID_STATE;
	String REMARK;
	BigDecimal RATIO;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getSUBJECT_ID() {
		return SUBJECT_ID;
	}
	public void setSUBJECT_ID(String sUBJECT_ID) {
		SUBJECT_ID = sUBJECT_ID;
	}
	public String getSUBJECT_NAME() {
		return SUBJECT_NAME;
	}
	public void setSUBJECT_NAME(String sUBJECT_NAME) {
		SUBJECT_NAME = sUBJECT_NAME;
	}
	public String getCATEGORY() {
		return CATEGORY;
	}
	public void setCATEGORY(String cATEGORY) {
		CATEGORY = cATEGORY;
	}
	public BigDecimal getVALID_STATE() {
		return VALID_STATE;
	}
	public void setVALID_STATE(BigDecimal vALID_STATE) {
		VALID_STATE = vALID_STATE;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	public BigDecimal getRATIO() {
		return RATIO;
	}
	public void setRATIO(BigDecimal rATIO) {
		RATIO = rATIO;
	}
	
}
