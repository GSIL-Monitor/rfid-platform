package com.casesoft.dmc.model.location;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Province entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LOCATION_PROVINCE")
public class Province implements java.io.Serializable {

  private static final long serialVersionUID = -8935961155142272223L;

  private Integer id;
  private String provinceId;
  private String province;

  // Constructors

  /** default constructor */
  public Province() {
  }

  /** minimal constructor */
  public Province(Integer id) {
    this.id = id;
  }

  /** full constructor */
  public Province(Integer id, String provinceId, String province) {
    this.id = id;
    this.provinceId = provinceId;
    this.province = province;
  }

  // Property accessors
  @Id
  @Column(nullable = false,length = 20)
  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(length = 20)
  public String getProvinceId() {
    return this.provinceId;
  }

  public void setProvinceId(String provinceId) {
    this.provinceId = provinceId;
  }

  @Column(length = 40)
  public String getProvince() {
    return this.province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

}