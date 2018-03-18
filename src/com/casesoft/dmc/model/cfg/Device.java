package com.casesoft.dmc.model.cfg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Device entity. @author
 */
@Entity
@Table(name = "CFG_DEVICE")
public class Device implements java.io.Serializable {

  private static final long serialVersionUID = 7549441435019745701L;
  private String id;
  private String code;
  private Integer type;
  private String name;
  private String ownerId;
  private String storageId;
  private String hostId;
  private String remark;
  private int locked;
  private String storageName;
  private String unitName;
  private String creator;
  private Date createTime;
  private String updater;
  private Date updateTime;
  private String appCode;//用户ID
  private String mchId;//商户电子ID
  private String key;//电子签名
  private String callbackUrl;
  @Column(nullable = false)
  public int getLocked() {
    return locked;
  }

  public void setLocked(int locked) {
    this.locked = locked;
  }

  // Constructors
@Column(length=45)
public String getCreator() {
	return creator;
}

public void setCreator(String creator) {
	this.creator = creator;
}

@JSONField(format="yyyy-MM-dd HH:mm:ss")
@Column()
public Date getCreateTime() {
	return createTime;
}

public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

@Column(length=45)
public String getUpdater() {
	return updater;
}

public void setUpdater(String updater) {
	this.updater = updater;
}

@JSONField(format="yyyy-MM-dd HH:mm:ss")
@Column()
public Date getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}

/** default constructor */
  public Device() {
  }

  /** minimal constructor */
  public Device(String code, Integer type, String ownerId, String storageId, String hostId) {
    this.code = code;
    this.type = type;
    this.ownerId = ownerId;
    this.storageId = storageId;
    this.hostId = hostId;
  }

  /** full constructor */
  public Device(String code, Integer type, String name, String ownerId, String storageId,
      String hostId, String remark) {
    this.code = code;
    this.type = type;
    this.name = name;
    this.ownerId = ownerId;
    this.storageId = storageId;
    this.hostId = hostId;
    this.remark = remark;
  }

  // Property accessors

  @Id
  @Column(nullable = false, length = 45)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(unique = true, nullable = false, length = 45)
  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column()
  public Integer getType() {
    return this.type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(length = 45)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 50)
  public String getStorageId() {
    return this.storageId;
  }

  public void setStorageId(String storageId) {
    this.storageId = storageId;
  }

  @Column(length = 50)
  public String getHostId() {
    return this.hostId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  @Column(length = 400)
  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
  @Column(length = 60,name = "appCode")
  public String getAppCode() {
    return appCode;
  }

  public void setAppCode(String appCode) {
    this.appCode = appCode;
  }
  @Column(length = 60,name = "mchId")
  public String getMchId() {
    return mchId;
  }

  public void setMchId(String mchId) {
    this.mchId = mchId;
  }
  @Column(length = 200,name = "key")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
  @Column(length = 300,name = "callbackUrl")
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  // /------------------>
  

  @Transient
  public String getStorageName() {
    return storageName;
  }

  public void setStorageName(String storageName) {
    this.storageName = storageName;
  }


  @Transient
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

}