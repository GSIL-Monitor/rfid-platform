package com.casesoft.dmc.model.mirror;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "MIRROR_STARINFO")
public class StarInfo implements Serializable {
	
	
    @Id
    @Column()
    private String id;
    
    
    @Column()
    private String starName;
    
    @Column(length=1)
	private String isShow;
	
    @Column()
	private String url;
    
    @Column()
    private String updater;
	
	@Column()
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date updateTime;
	
	@Column()
	private int seqNo;
		
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStarName() {
		return starName;
	}

	public void setStarName(String startName) {
		this.starName = startName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
}
