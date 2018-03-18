package com.casesoft.dmc.model.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.product.BaseProductInfo;

@Entity
@Table(name = "SHOP_SCORE")
public class Score extends BaseProductInfo implements Serializable {

  private static final long serialVersionUID = -3542397287687963060L;

  private String id;
  private String code;
  private String styleNo;
  private String colorNo;
  private String sizeNo;

  private String ownerId;
  private String deviceId;

  private int styleScore;
  private int qualityScore;
  private int priceScore;

  private Date scoreTime;
  private int count;

    private String parentId;//2014-06-07 父组织id
    @Column(nullable = false, length = 50)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String shopId) {
        this.parentId = shopId;
    }

  public Score() {
    super();
  }

  public Score(String code, String styleNo, String colorNo, String sizeNo, String ownerId) {
    super();
    this.code = code;
    this.styleNo = styleNo;
    this.colorNo = colorNo;
    this.sizeNo = sizeNo;
    this.ownerId = ownerId;
  }

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

  public String getOwnerId() {
    return ownerId;
  }

  @Column(nullable = false, length = 50)
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column()
  public int getStyleScore() {
    return styleScore;
  }

  public void setStyleScore(int styleScore) {
    this.styleScore = styleScore;
  }

  @Column()
  public int getQualityScore() {
    return qualityScore;
  }

  public void setQualityScore(int qualityScore) {
    this.qualityScore = qualityScore;
  }

  @Column()
  public int getPriceScore() {
    return priceScore;
  }

  public void setPriceScore(int priceScore) {
    this.priceScore = priceScore;
  }

  @Column()
  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Column()
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Column(length = 19)
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  public Date getScoreTime() {
    return scoreTime;
  }

  public void setScoreTime(Date scoreTime) {
    this.scoreTime = scoreTime;
  }

  private String uniqueCodes;

  @Transient
  public String getUniqueCodes() {
    return uniqueCodes;
  }

  public void setUniqueCodes(String uniqueCodes) {
    this.uniqueCodes = uniqueCodes;
  }

}
