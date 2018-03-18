package com.casesoft.dmc.model.tag;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TAG_PRINTINFO")
public class PrintInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String sku;
  private String billNo;

  private String ownerId;// 部门ID
  private String propertyKey;// 属性ID
  private String propertyValue;
  private String propertyName;
  @Transient
  public String getPropertyName() {
	return propertyName;
}

public void setPropertyName(String propertyName) {
	this.propertyName = propertyName;
}


  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(nullable = false, length = 50)
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  @Column(nullable = false, length = 50)
  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 50)
  public String getPropertyKey() {
    return propertyKey;
  }

  public void setPropertyKey(String propertyKey) {
    this.propertyKey = propertyKey;
  }

  @Column(length = 200)
  public String getPropertyValue() {
    return propertyValue;
  }

  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

}
