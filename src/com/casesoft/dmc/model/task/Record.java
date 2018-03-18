package com.casesoft.dmc.model.task;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * TblTaskRecord entity. @author
 */
@Entity
@Table(name = "TASK_RECORD")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Record implements java.io.Serializable {

  private static final long serialVersionUID = -6159224187040781317L;

  private String id;
  private String code;/* 唯一码 */
  private String taskId;
  private String cartonId;
  private Integer token;
  private String deviceId;
  private String ownerId;

  private Date scanTime;

  private String sku;
  private String styleId;
  private String colorId;
  private String sizeId;

  private String srcTaskId;
  private String styleName;
  private String colorName;
  private String sizeName;
  private String deviceName;
  private String origUnitId;//发货方组织
  private String origUnitName;
  private String origId;//发货方仓库
  private String origName;

  private String destId;//收货方仓库
  private String destName;

  private String destUnitId; //如果是品牌商自己的门店，值为null
  private String destUnitName;
  private Integer type;//0:出库1：入库3：盘点

  private String extField;//扩展字段

  private Double price;

  @Column(length = 50)
  public String getExtField() {
    return extField;
  }

  public void setExtField(String extField) {
    this.extField = extField;
  }

  @Column(length = 50)
  public String getOrigUnitId() {
    return origUnitId;
  }

  public void setOrigUnitId(String origUnitId) {
    this.origUnitId = origUnitId;
  }

  @Column(length = 50)
  public String getSrcTaskId() {
    return srcTaskId;
  }

  public void setSrcTaskId(String srcTaskId) {
    this.srcTaskId = srcTaskId;
  }

  // Constructors

  /** default constructor */
  public Record() {
  }

  /** full constructor */
  public Record(String epc, String taskId, String cartonId, Integer token, String deviceId,
      String storageId) {
    this.code = epc;
    this.taskId = taskId;
    this.cartonId = cartonId;
    this.token = token;
    this.deviceId = deviceId;
    this.origId = storageId;
  }

  // Property accessors
  @JSONField(serialize = false)
  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(nullable = false, length = 50)
  public String getCode() {
    return this.code;
  }

  public void setCode(String epc) {
    this.code = epc;
  }

  @Column(nullable = false, length = 50)
  public String getTaskId() {
    return this.taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @Column(nullable = false, length = 50)
  public String getCartonId() {
    return this.cartonId;
  }

  public void setCartonId(String cartonId) {
    this.cartonId = cartonId;
  }

  @Column(nullable = false)
  public Integer getToken() {
    return this.token;
  }

  public void setToken(Integer token) {
    this.token = token;
  }

  @Column(nullable = false, length = 50)
  public String getDeviceId() {
    return this.deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Column(length = 50)
  public String getOrigId() {
    return this.origId;
  }

  public void setOrigId(String origId) {
    this.origId = origId;
  }

  @Column(length = 50)
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 19)
  public Date getScanTime() {
    return scanTime;
  }

  public void setScanTime(Date scanTime) {
    this.scanTime = scanTime;
  }

  @Column(nullable = false, length = 50)
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  @Column(length = 20)
  public String getStyleId() {
    return styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  @Column(length = 20)
  public String getColorId() {
    return colorId;
  }

  public void setColorId(String colorId) {
    this.colorId = colorId;
  }

  @Column(length = 10)
  public String getSizeId() {
    return sizeId;
  }

  public void setSizeId(String sizeId) {
    this.sizeId = sizeId;
  }

  // //===================>

  @Transient
  public String getStyleName() {
    return styleName;
  }

  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  @Transient
  public String getColorName() {
    return colorName;
  }

  public void setColorName(String colorName) {
    this.colorName = colorName;
  }

  @Transient
  public String getSizeName() {
    return sizeName;
  }

  public void setSizeName(String sizeName) {
    this.sizeName = sizeName;
  }

  

  @Transient
  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  @Transient
  public String getOrigName() {
    return origName;
  }

  public void setOrigName(String origName) {
    this.origName = origName;
  }

  @Transient
  public String getOrigUnitName() {
    return origUnitName;
  }

  public void setOrigUnitName(String origUnitName) {
    this.origUnitName = origUnitName;
  }

  private String tokenName;

  @Transient
  public String getTokenName() {
    return tokenName;
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  @Transient
  public String getDestUnitName() {
    return destUnitName;
  }

  public void setDestUnitName(String destUnitName) {
    this.destUnitName = destUnitName;
  }
  @Transient
  public String getDestName() {
    return destName;
  }

  public void setDestName(String destName) {
    this.destName = destName;
  }

  @Column(length = 50)
  public String getDestId() {
    return destId;
  }

  public void setDestId(String destId) {
    this.destId = destId;
  }
  @Column(length = 50)
  public String getDestUnitId() {
    return destUnitId;
  }

  public void setDestUnitId(String destUnitId) {
    this.destUnitId = destUnitId;
  }
  @Column()
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column()
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  private String onlibrary;
  @Transient
  public String getOnlibrary() {
    return onlibrary;
  }

  public void setOnlibrary(String onlibrary) {
    this.onlibrary = onlibrary;
  }
}