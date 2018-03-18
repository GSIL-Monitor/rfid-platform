package com.casesoft.dmc.model.location;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LOCATION_AREA")
public class Area implements java.io.Serializable {

  private static final long serialVersionUID = -4600377402331629473L;
  private Integer id;
  private String areaId;
  private String area;
  private String father;

  // Constructors

  /** default constructor */
  public Area() {
  }

  /** minimal constructor */
  public Area(Integer id) {
    this.id = id;
  }

  /** full constructor */
  public Area(Integer id, String areaId, String area, String father) {
    this.id = id;
    this.areaId = areaId;
    this.area = area;
    this.father = father;
  }

  // Property accessors
  @Id
  @Column(nullable = false,length = 30)
  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(length = 50)
  public String getAreaId() {
    return this.areaId;
  }

  public void setAreaId(String areaId) {
    this.areaId = areaId;
  }

  @Column(length = 60)
  public String getArea() {
    return this.area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  @Column(length = 30)
  public String getFather() {
    return this.father;
  }

  public void setFather(String father) {
    this.father = father;
  }

}