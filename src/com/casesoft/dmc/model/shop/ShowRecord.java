package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.product.BaseProductInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SHOP_SHOWRECORD")
public class ShowRecord extends BaseProductInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String code;/* 唯一码 */
  private String deviceId;
  private String ownerId;//门店ID
  private String taskId;// 值为所属score的Id

  private Date scanTime;

  private String sku;
  private String styleId;
  private String colorId;
  private String sizeId;

  private String parentId;//2014-06-07 父组织id



  // Constructors

  /** default constructor */
  public ShowRecord() {
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

  @Column(nullable = false, length = 50)
  public String getTaskId() {
    return this.taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @Column(nullable = false, length = 50)
  public String getCode() {
    return this.code;
  }

  public void setCode(String epc) {
    this.code = epc;
  }

  @Column(nullable = false, length = 50)
  public String getDeviceId() {
    return this.deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Column(nullable = false,length = 50)
  public String getOwnerId() {
    return ownerId;
  }



  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

  @Column(length = 10)
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

    @Column(nullable = false, length = 50)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String shopId) {
        this.parentId = shopId;
    }
}
