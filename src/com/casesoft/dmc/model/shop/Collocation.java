package com.casesoft.dmc.model.shop;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SHOP_COLLOCATION")
public class Collocation implements Serializable {

  private static final long serialVersionUID = -8438019203076417239L;

  private String id;
  private String code;
  private String styleNo;
  private String colorNo;
  private String sizeNo;

  private String code2;
  private String styleNo2;
  private String colorNo2;
  private String sizeNo2;

  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(nullable = false, length = 50)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(nullable = false, length = 20)
  public String getStyleNo() {
    return styleNo;
  }

  public void setStyleNo(String styleNo) {
    this.styleNo = styleNo;
  }

  @Column(nullable = false, length = 10)
  public String getColorNo() {
    return colorNo;
  }

  public void setColorNo(String colorNo) {
    this.colorNo = colorNo;
  }

  @Column(nullable = false, length = 10)
  public String getSizeNo() {
    return sizeNo;
  }

  public void setSizeNo(String sizeNo) {
    this.sizeNo = sizeNo;
  }

  @Column(nullable = false, length = 50)
  public String getCode2() {
    return code2;
  }

  public void setCode2(String code2) {
    this.code2 = code2;
  }

  @Column(nullable = false, length = 20)
  public String getStyleNo2() {
    return styleNo2;
  }

  public void setStyleNo2(String styleNo2) {
    this.styleNo2 = styleNo2;
  }

  @Column(nullable = false, length = 10)
  public String getColorNo2() {
    return colorNo2;
  }

  public void setColorNo2(String colorNo2) {
    this.colorNo2 = colorNo2;
  }

  @Column(nullable = false, length = 10)
  public String getSizeNo2() {
    return sizeNo2;
  }

  public void setSizeNo2(String sizeNo2) {
    this.sizeNo2 = sizeNo2;
  }

}
