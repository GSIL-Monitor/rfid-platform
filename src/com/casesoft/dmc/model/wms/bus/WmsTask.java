package com.casesoft.dmc.model.wms.bus;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 仓库管理任务
 */
//@Entity
//@Table(name="wms_tsk")
public class WmsTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3071858415484727360L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_WMSTask")
	@SequenceGenerator(name = "S_WMSTask", allocationSize = 1, initialValue = 1, sequenceName = "S_WMSTask")
	private String id;
	@Column(name = "tskcd", nullable = false, length = 50)
	private String taskCode;
	@Column(name = "bustskid", length = 50)
	private String bussinessTaskId;
	@Column(name = "cmd",nullable=false)
	private int cmd;
	@Column(name = "st",nullable=false)
	private int status;//状态
	@Column(name = "oruncd",nullable=false,length = 45)
	private String origUnitCode;//调整方
	@Column(name = "orunflarcd",nullable=false,length = 20)
	private String origFloorAreaCode;
	@Column(name = "orunflcd",nullable=false,length = 20)
	private String origFloorCode; 
	
	@Column(name = "deuncd",nullable=false,length = 20)
	private String destUnitCode;//收货方
	@Column(name = "deunflarcd",nullable=false,length = 20)
	private String destFloorAreaCode;
	@Column(name = "deunflcd",nullable=false,length = 20)
	private String destFloorCode; 
	@Column(name = "qty",nullable=false )
	private int qty;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getBussinessTaskId() {
		return bussinessTaskId;
	}
	public void setBussinessTaskId(String bussinessTaskId) {
		this.bussinessTaskId = bussinessTaskId;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrigUnitCode() {
		return origUnitCode;
	}
	public void setOrigUnitCode(String origUnitCode) {
		this.origUnitCode = origUnitCode;
	}
	public String getOrigFloorAreaCode() {
		return origFloorAreaCode;
	}
	public void setOrigFloorAreaCode(String origFloorAreaCode) {
		this.origFloorAreaCode = origFloorAreaCode;
	}
	public String getOrigFloorCode() {
		return origFloorCode;
	}
	public void setOrigFloorCode(String origFloorCode) {
		this.origFloorCode = origFloorCode;
	}
	public String getDestUnitCode() {
		return destUnitCode;
	}
	public void setDestUnitCode(String destUnitCode) {
		this.destUnitCode = destUnitCode;
	}
	public String getDestFloorAreaCode() {
		return destFloorAreaCode;
	}
	public void setDestFloorAreaCode(String destFloorAreaCode) {
		this.destFloorAreaCode = destFloorAreaCode;
	}
	public String getDestFloorCode() {
		return destFloorCode;
	}
	public void setDestFloorCode(String destFloorCode) {
		this.destFloorCode = destFloorCode;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	@Transient
	private List<WmsTaskDtl> wmsTaskDtls;
	@Transient
	public List<WmsTaskDtl> getWmsTaskDtls() {
		return wmsTaskDtls;
	}
	public void setWmsTaskDtls(List<WmsTaskDtl> wmsTaskDtls) {
		this.wmsTaskDtls = wmsTaskDtls;
	}
	
	
}
