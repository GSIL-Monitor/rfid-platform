package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by session on 2017/3/9 0009.
 */

@Entity
@Table(name = "Hall_Employee")
public class Employee implements java.io.Serializable{
	/*
		必填项：id、code、post、ownerId
	  工号、姓名、电话、邮箱、职位、所属部门 */
	@Id
	@Column(nullable = false, length = 50)
	private String id;

	@Column(unique = true, nullable = false, length = 50)
	private String code;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(length = 20)
	private String post;//部门职位

	@Column(length = 50, nullable = false)
	private String ownerId;

	@Column(length = 50)
	private String tel;

	@Column()
	private Integer isUser;

	@Column(length = 50)
	private String email;

	@Column(length = 19)
	private Date createTime;

	@Column(length = 50)
	private String creator;

	@Column(length = 19)
	private Date updateTime;

	@Column(length = 50)
	private String updater;

	@Column(length = 200)
	private String remark;

	private String password;

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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getIsUser() {
		return isUser;
	}

	public void setIsUser(Integer isUser) {
		this.isUser = isUser;
	}

	@JSONField(format ="yyyy-MM-dd HH:mm:ss" )
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

	@Transient
	private String unitName;

	@Transient
	private String creatorName;

	@Transient
	private String updatorName;

	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}


	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
