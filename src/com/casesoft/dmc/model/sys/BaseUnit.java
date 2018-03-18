package com.casesoft.dmc.model.sys;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by Wing Li on 2014/6/16.
 */
@MappedSuperclass
public abstract class BaseUnit {
  private String bankCode;
  private String bankAccount;
  private String businessNo;
  private String business;
  private String industry;
  private String countryNo;
  private Double tax;
  private Double supply;
  private Double comm;
  private Double earnest;
  private String groupId;//区分ID

  @Column(length = 50)
  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }
  @Column(length = 50)
  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }
  @Column(length = 50)
  public String getBusinessNo() {
    return businessNo;
  }

  public void setBusinessNo(String businessNo) {
    this.businessNo = businessNo;
  }
  @Column(length = 50)
  public String getBusiness() {
    return business;
  }

  public void setBusiness(String business) {
    this.business = business;
  }
  @Column(length = 50)
  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }
  @Column(length = 20)
  public String getCountryNo() {
    return countryNo;
  }

  public void setCountryNo(String countryNo) {
    this.countryNo = countryNo;
  }
  @Column(length = 20)
  public Double getTax() {
    return tax;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }
  @Column(length = 20)
  public Double getSupply() {
    return supply;
  }

  public void setSupply(Double supply) {
    this.supply = supply;
  }
  @Column(length = 20)
  public Double getComm() {
    return comm;
  }

  public void setComm(Double comm) {
    this.comm = comm;
  }
  @Column(length = 20)
  public Double getEarnest() {
    return earnest;
  }

  public void setEarnest(Double earnest) {
    this.earnest = earnest;
  }
  @Column(length = 20)
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
//  @Column(nullable=true)
  private Integer isThird;//是否第三方1，第三方
//  @Column(nullable=true, length = 50)
  private String thirdName;//第三方名称

  @Column()
public Integer getIsThird() {
	return isThird;
}

public void setIsThird(Integer isThird) {
	this.isThird = isThird;
}
@Column(length = 50)
public String getThirdName() {
	return thirdName;
}

public void setThirdName(String thirdName) {
	this.thirdName = thirdName;
}
  
}
