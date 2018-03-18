package com.casesoft.dmc.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * City entity.
 */
@Entity
@Table(name = "LOCATION_CITY")
public class City implements java.io.Serializable {

  private static final long serialVersionUID = 6373914259798353819L;
  private Integer id;
  private String cityId;
  private String city;
  private String father;

  // Constructors

  /** default constructor */
  public City() {
  }

  /** minimal constructor */
  public City(Integer id) {
    this.id = id;
  }

  /** full constructor */
  public City(Integer id, String cityId, String city, String father) {
    this.id = id;
    this.cityId = cityId;
    this.city = city;
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

  @Column(length = 30)
  public String getCityId() {
    return this.cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  @Column(length = 50)
  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(length = 30)
  public String getFather() {
    return this.father;
  }

  public void setFather(String father) {
    this.father = father;
  }

}