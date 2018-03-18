package com.casesoft.dmc.model.mirror;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="MIRROR_ACTIVITYINFO") 
public class ActivityInfo implements Serializable {
	
	@Id
	@Column(nullable = false)
	private String id;
	
	@Column(length=20)
    @JSONField(format="yyyy-MM-dd")
	private Date activityTime;

	@Column(length=500)
	private String remark;
	
	@Column(length=20)
    private String updater;
	
	@Column(length=20)
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date updateTime;
	
	@Column(length=20)
	private Integer seqNo;
		
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Column(length=500)
    private String url;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(length=2)
	private String isShow;
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	


	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	
	
	
}
