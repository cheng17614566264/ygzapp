package fmss.dao.entity;

import java.io.Serializable;

public class VmsCostCenterInst implements Serializable{
	//private int id;
	private String instId; //业务机构代码
	private String instName; //业务机构名称
	private String costCenter; //财务机构代码
	private String costCenterName; //财务机构名称
	private String parentInstId; //财务机构上级机构代码
	//private String parentInstName; //财务机构上级机构名称
	//private String datastatus; //关联状态 0-未关联 1-已关联
	private String dsource; //来源，总账和费控
	private String instLevel; //机构级别
	
	/*public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}*/
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getParentInstId() {
		return parentInstId;
	}
	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}
	
	/*public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}*/

	public String getCostCenterName() {
		return costCenterName;
	}
	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}
	/*public String getParentInstName() {
		return parentInstName;
	}
	public void setParentInstName(String parentInstName) {
		this.parentInstName = parentInstName;
	}*/
	public String getDsource() {
		return dsource;
	}
	public void setDsource(String dsource) {
		this.dsource = dsource;
	}
	public String getInstLevel() {
		return instLevel;
	}
	public void setInstLevel(String instLevel) {
		this.instLevel = instLevel;
	}
	
	
	
	public VmsCostCenterInst() {
		super();
	}
	public VmsCostCenterInst(String instName,String instId) {
		super();
		this.instName = instName;
		this.instId = instId;
	}
	

}
