package com.casesoft.dmc.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Role entity. @author
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role implements java.io.Serializable {

  private static final long serialVersionUID = -3219055093974840571L;
  private String id;
  private String code;
  private String name;

  private String remark;
  private Date createTime;
  private String creatorId;
  private String ownerId;

  private int locked;
  private String authIds;
  private String authNames;

  @Column(nullable = false)
  public int getLocked() {
    return locked;
  }

  public void setLocked(int locked) {
    this.locked = locked;
  }

  // Constructors

  /** default constructor */
  public Role() {
  }

  /** minimal constructor */
  public Role(String code, String name, Date createTime, String creatorId, String ownerId) {
    this.code = code;
    this.name = name;
    this.createTime = createTime;
    this.creatorId = creatorId;
    this.ownerId = ownerId;
  }

  // Property accessors
  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(unique = true, nullable = false, length = 50)
  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(nullable = false, length = 50)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column()
  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @Column(nullable = false, length = 19)
  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(nullable = false, length = 50)
  public String getCreatorId() {
    return this.creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  // /------------------>


  @Transient
  public String getAuthIds() {
    return authIds;
  }

  public void setAuthIds(String authIds) {
    this.authIds = authIds;
  }

 

  @Transient
  public String getAuthNames() {
    return authNames;
  }

  public void setAuthNames(String authNames) {
    this.authNames = authNames;
  }

}