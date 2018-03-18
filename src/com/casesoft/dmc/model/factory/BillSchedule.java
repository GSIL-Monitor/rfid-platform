package com.casesoft.dmc.model.factory;

import javax.persistence.*;

@Entity
@Table(name="BillSchedule")
public class BillSchedule implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="bill_No",length=50)
	private String billNo;
	@Column(name="bill_Date",length=20)
	private String billDate;
	@Column(name="upload_No",length=20)
	private String uploadNo;
	@Column(name="end_Date",length=20)
	private String endDate;	
	@Id
    @Column(name="token",length=10)
	private Integer token;
	@Id
	@Column(name="type",length=2)
	private String type;
	@Id
	@Column(name="schedule")
	private String schedule;
	
	@Column(name="taskTime")
	private String taskTime;
   
    @Column(name="updateTime")
    private String updateTime;
    @Column(name="updateId")
    private String updateId;
    @Column(name="remark",length=1000)
    private String remark;

    public BillSchedule() {
    }

    public BillSchedule(String billNo, String billDate, String uploadNo, String endDate, Integer token,
                        String type, String schedule, String updateTime, String updateId) {
        this.billNo = billNo;
        this.billDate=billDate;
        this.uploadNo = uploadNo;
        this.endDate = endDate;
        this.token = token;
        this.type = type;
        this.schedule = schedule;
        this.updateTime = updateTime;
        this.updateId = updateId;
    }

    public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	public String getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	@Transient
	private String tokenName;

    @Transient
    private Integer tokenIndex;

    @Column(name="preSchedule")
    private String preSchedule;
	
	public String getTokenName() {
		return tokenName;
	}


	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

    public Integer getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(Integer tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public String getPreSchedule() {
        return preSchedule;
    }

    public void setPreSchedule(String preSchedule) {
        this.preSchedule = preSchedule;
    }

    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
