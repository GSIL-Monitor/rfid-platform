package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ColorGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRODUCT_COLORGROUP")
public class ColorGroup implements java.io.Serializable {

  private static final long serialVersionUID = -8140471170026642769L;
  // Fields

  private String id;
  private String groupNo;
  private String groupName;

  // Constructors

  /** default constructor */
  public ColorGroup() {
  }

  /** full constructor */
  public ColorGroup(String groupNo, String groupName) {
    this.groupNo = groupNo;
    this.groupName = groupName;
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

  @Column(unique = true, nullable = false, length = 45)
  public String getGroupNo() {
    return this.groupNo;
  }

  public void setGroupNo(String groupNo) {
    this.groupNo = groupNo;
  }

  @Column(unique = true, nullable = false, length = 45)
  public String getGroupName() {
    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

}