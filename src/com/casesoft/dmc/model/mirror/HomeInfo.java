package com.casesoft.dmc.model.mirror;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MIRROR_HOMEINFO")
public class HomeInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*@SequenceGenerator(name = "sequenceGenerator", sequenceName = "mirror_home")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.AUTO)*/
	@Id
	@Column(nullable = false)
	private String id;
	
	@Column(nullable=false,length=50)
	private String name;
	
	@Column(length=1000)
	private String remark;//描述
	
    @Column(nullable=false,length=2)
	private String fileType;//I:图片V:视频

	
	@Column(length=100)
	private String url;
	
	@Column(length=20)
	private String creator;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(length=20)
	private Date createTime;
	
	@Column(length=20)
	private String updater;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(length=50)
	private Date updateTime;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	

}
