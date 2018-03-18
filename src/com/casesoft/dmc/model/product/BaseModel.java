package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by WinstonLi on 2014/6/17.
 */
@MappedSuperclass
public class BaseModel {
  @Column(length = 30)
  private String updateTime;
  @Column(length = 50)
  private String oprId;

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getOprId() {
    return oprId;
  }

  public void setOprId(String oprId) {
    this.oprId = oprId;
  }
}
