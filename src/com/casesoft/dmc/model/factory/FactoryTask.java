package com.casesoft.dmc.model.factory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Factory_Task")
@Inheritance(strategy = InheritanceType.JOINED)
public class FactoryTask implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="task_Id", nullable = false)
	private String taskId;
	@Column(name="operator", nullable = false)
	private String operator;
	@Column(name="token", nullable = false)
	private Integer token;
	@Column(name="type",length=1,nullable = false)
	private String type;
	@Column(name="task_Time", nullable = false)
	private String taskTime;
	@Column(name="device_Id", length = 20, nullable = false)
	private String deviceId;
	@Column(name="factory", length = 50)
	private String factory;
	@Column(name="out_Taks_Id")
	private String outTaskId;
	@Column(name ="totQty")
	private long qty;	
	@Column(name="remark", length = 200)
	private String remark;
	@Column(name="is_OutSource",length=1)
	private String isOutSource;
	
	@Column(name="isBack",length=1)
	private String isBack;
	
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getToken() {
		return token;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getOutTaskId() {
		return outTaskId;
	}
	public void setOutTaskId(String outTaskId) {
		this.outTaskId = outTaskId;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIsOutSource() {
		return isOutSource;
	}
	public void setIsOutSource(String isOutSource) {
		this.isOutSource = isOutSource;
	}

	@Transient
	private List<InitEpc> epcList;
	@Transient
	private List<FactoryRecord> recordList;

	public List<InitEpc> getEpcList() {
		return epcList;
	}
	public void setEpcList(List<InitEpc> epcList) {
		this.epcList = epcList;
	}
	public List<FactoryRecord> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<FactoryRecord> recordList) {
		this.recordList = recordList;
	}	
	
	@Transient
	private String factoryName;

	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	@Transient
	private String operatorName;

	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	@Transient
	private String tokenName;
	
	public String getTokenName() {
		return tokenName;
	}


	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public String getIsBack() {
		return isBack;
	}
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}
	
	

}
