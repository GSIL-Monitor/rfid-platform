package com.casesoft.dmc.model.shop;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by Wing Li on 2014/6/21.
 */
@MappedSuperclass
public abstract class BaseModelDtl {
  private String  id;
  private String billId;
  private String billNo;
  private String code;
  private String styleId;
  private String colorId;
  private String sizeId;

  private long qty;
  //
  private String remark;
  private String styleName;
  private String colorName;
  private String sizeName;
  private double price;// 吊牌价格

  private String refBillId;

  @Id
  @Column(name="id" ,length=50 ,nullable = false)
  public String getId() {
    return id;
  }

  public void setId(String  id) {
    this.id = id;
  }

  @Column( nullable = false, length = 50)
  public String getBillId() {
    return billId;
  }

  public void setBillId(String billId) {
    this.billId = billId;
  }

  @Column( nullable = false, length = 50)
  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  @Column( nullable = false, length = 50)
  public String getCode() {
    return code;
  }

  public void setCode(String sku) {
    this.code = sku;
  }

  @Column( nullable = false, length = 20)
  public String getStyleId() {
    return styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  @Column(nullable = false, length = 10)
  public String getColorId() {
    return colorId;
  }

  public void setColorId(String colorId) {
    this.colorId = colorId;
  }

  @Column( nullable = false, length = 10)
  public String getSizeId() {
    return sizeId;
  }

  public void setSizeId(String sizeId) {
    this.sizeId = sizeId;
  }

  @Column(nullable = false)
  public long getQty() {
    return qty;
  }

  public void setQty(long qty) {
    this.qty = qty;
  }
  @Column(length = 200)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Transient
  public String getStyleName() {
    return styleName;
  }

  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  @Transient
  public String getColorName() {
    return colorName;
  }

  public void setColorName(String colorName) {
    this.colorName = colorName;
  }

  @Transient
  public String getSizeName() {
    return sizeName;
  }

  public void setSizeName(String sizeName) {
    this.sizeName = sizeName;
  }

   @Transient
  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getRefBillId() {
    return refBillId;
  }

  public void setRefBillId(String refBillId) {
    this.refBillId = refBillId;
  }
}
