package fmss.dao.entity;

import java.io.Serializable;

public class UAuthRoleUserInfoDO extends BaseDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleId;// 角色编号 U_AUTH_ROLE.ROLE_ID
	private String userId;// 描述 U_BASE_USER.USER_ID
	private UAuthRoleDO authrole;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public UAuthRoleDO getAuthrole() {
		return authrole;
	}
	public void setAuthrole(UAuthRoleDO authrole) {
		this.authrole = authrole;
	}
	
	


}
