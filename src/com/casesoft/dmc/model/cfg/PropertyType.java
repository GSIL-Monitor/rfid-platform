package com.casesoft.dmc.model.cfg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CFG_PROPERTYTYPE")
public class PropertyType implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;// id
  private String keyId;//同id
  private String value;//名称
  private String remark;//备注
  private String brand;//所属品牌//可以为空
  private String type;//分类名称
  private String isUse;//是否使用（Y/N）
  private Integer seqNo;//序号

  public PropertyType(){}

  public PropertyType(String id, String keyId, String value, String type, String brand){
	  this.id = id;
	  this.keyId = keyId;
	  this.value = value;
	  this.type = type;
	  this.brand  = brand;
  }
  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  @Column( length =10)
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(nullable = false, length = 50)
  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  @Column(nullable = false, length = 200)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Column(length = 500)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
  @Column(length = 20)
public String getBrand() {
	return brand;
	
}
  @Column(length = 2)
  public String getIsUse() {
    return isUse;
  }

  public void setIsUse(String isUse) {
    this.isUse = isUse;
  }

  @Column(nullable = false, length = 50)
public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public void setBrand(String brand) {
	this.brand = brand;
}

}
