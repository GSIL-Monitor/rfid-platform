package com.casesoft.dmc.model.product;

import javax.persistence.Transient;

public class BaseProductInfo extends BaseProduct {

  private String deviceName;
  private String storageName;
  private String unitName;

  @Transient
  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

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
