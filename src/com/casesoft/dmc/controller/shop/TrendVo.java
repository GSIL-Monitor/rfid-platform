package com.casesoft.dmc.controller.shop;

public class TrendVo implements java.io.Serializable {

  private static final long serialVersionUID = -5610372755556682889L;

  private String ownerId;
  private int year;
  private int month;
  private int day;
  private long totQty;

  private String unitName;

  public TrendVo() {
    super();
  }

  public TrendVo(String ownerId, int year, int month, int day, long totQty) {
    super();
    this.ownerId = ownerId;
    this.year = year;
    this.month = month;
    this.day = day;
    this.totQty = totQty;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public long getTotQty() {
    return totQty;
  }

  public void setTotQty(long totQty) {
    this.totQty = totQty;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

}
