package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by session on 2017/3/9 0009.
 */

@Entity
@Table (name ="Hall_Department")
public class Department implements java.io.Serializable{
//必填项：id、name、code、ownerId
	@Id
	@Column(nullable = false,length=50)
	private String id;//部门id、名称、编号、所属公司、邮箱、联系电话、联系人

	@Column(nullable = false,length=50)
	private String name;

	@Column(unique = true,nullable = false,length=50)
	private String code;

	@Column(length=50,nullable = false)
	private String ownerId;

	@Column(length = 50)
	private String email;

	@Column(length=50)
	private String linkTel;

	@Column(length = 50)
	private String linkman;

	@Column(length = 19)
	private Date createTime;//创建人、时间，更新人、时间，备注

	@Column(length=50)
	private String creator;

	@Column(length = 19)
	private Date updateTime;

	@Column(length=50)
	private String updater;

	@Column(length=200)
	private String remark;

	@Transient
	private String unitName;//公司名称

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

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
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
}
