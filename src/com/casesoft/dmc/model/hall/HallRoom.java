package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by sessison on 2017/3/9 0009.
 */

@Entity
@Table(name="Hall_Room")
public class HallRoom implements  Serializable{
	//必填项:id name code ownerId type status
	@Id
	@Column(nullable = false,length=50,name="id")
	private String id;//id 、名称、编号、所属单位、类型、状态

	@Column(nullable = false,length = 50,name="name")
	private String name;

	@Column(unique = true,nullable = false,length=50,name="code")
	private String code;

	@Column(length = 50,name="ownerId")
	private String ownerId;

//	@Column(length = 50,name="")
//	private String deviceId;

	@Column()
	private Integer type;

	@Column()
	private Integer status;

	@Column(length = 20)
	private String linktel;

	@Column(length = 20)
	private String linkman;

	@Column(length = 20)
	private String email;

	@Column(length = 19)
	private Date createTime;

	@Column(length = 50)
	private String creator;

	@Column(length = 19)
	private Date updateTime;

	@Column(length = 50)
	private String updator;

	@Column(length = 200)
	private String remark;

	@Transient
	private String unitName;

	@Transient
	private String creatorName;

	@Transient
	private String updaterName;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLinktel() {
		return linktel;
	}

//	public String getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}

	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}
}
