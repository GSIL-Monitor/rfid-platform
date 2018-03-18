package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * StyleSort entity. @author
 */

@Entity
@Table(name = "PRODUCT_STYLESORT")
public class StyleSort implements java.io.Serializable {

  private static final long serialVersionUID = -2140378982759164780L;
  private String id;
  private String sortNo;
  private String sortName;

  // Constructors

  /** default constructor */
  public StyleSort() {
  }

  /** full constructor */
  public StyleSort(String sortNo, String sortName) {
    this.sortNo = sortNo;
    this.sortName = sortName;
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
  public String getSortNo() {
    return this.sortNo;
  }

  public void setSortNo(String sortNo) {
    this.sortNo = sortNo;
  }

  @Column(unique = true, nullable = false, length = 45)
  public String getSortName() {
    return this.sortName;
  }

  public void setSortName(String sortName) {
    this.sortName = sortName;
  }

}