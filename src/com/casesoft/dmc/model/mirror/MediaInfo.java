package com.casesoft.dmc.model.mirror;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "MIRROR_MEDIAINFO")
public class MediaInfo implements Serializable {
	/**
	 * 视频Id
	 * */
	@Id
	@Column(nullable = false,length=20)
	private String id;
	/**
	 * 视频名称
	 * */
	@Column(length=20)
	private String name;
	
	/**
	 * 是否展示
	 * */
	@Column(length=2)
	private String isShow;
	
	/**
	 * 展示区域
	 * */
	@Column(length=20)
	private String showArea;
	
	@Column(length=500)
	private String remark;
	
	@Column(length=20)
    private String updater;
	
	@Column(length=20)
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date updateTime;

	@Column(length=255)
    private String url;
	@Transient
    private String imageUrl;
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(length=20)
	private Integer seqNo;
		
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getShowArea() {
		return showArea;
	}

	public void setShowArea(String showArea) {
		this.showArea = showArea;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}
}
