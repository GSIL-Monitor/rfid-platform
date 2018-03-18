package com.casesoft.dmc.model.stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "STOCK_INVENTORYDTO")
public class InventoryDto implements java.io.Serializable {

  private static final long serialVersionUID = 3726523282583502850L;

  private String id;

  private String ownerId;
  private String storageId;
  private String sku;
  private String styleId;
  private String colorId;
  private String sizeId;

  private Long qty;

  private Long iqty;
  private Long oqty;

  private Boolean isLack;

  private int storeType;
  
  private String styleName;
  private String colorName;
  private String sizeName;

  private String storageName;

  private String unitName;


  public InventoryDto() {
  }

  @Column()
  public Boolean getIsLack() {
    return isLack;
  }

  public void setIsLack(Boolean isLack) {
    this.isLack = isLack;
  }

  @Id
  @Column(nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 50)
  public String getStorageId() {
    return storageId;
  }

  public void setStorageId(String storageId) {
    this.storageId = storageId;
  }

  @Column(nullable = false, length = 50)
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  @Column(nullable = false, length = 20)
  public String getStyleId() {
    return styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  @Column(nullable = false, length = 20)
  public String getColorId() {
    return colorId;
  }

  public void setColorId(String colorId) {
    this.colorId = colorId;
  }

  @Column(nullable = false, length = 20)
  public String getSizeId() {
    return sizeId;
  }

  public void setSizeId(String sizeId) {
    this.sizeId = sizeId;
  }

  @Column(nullable = false)
  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  // 使用hql查询时，iQty将报错，iqty不报错(估计是Hibernate的bug)
  @JSONField(name = "iQty")
  @Column(nullable = false)
  public Long getIqty() {
    return iqty;
  }

  public void setIqty(Long iqty) {
    this.iqty = iqty;
  }

  @JSONField(name = "oQty")
  @Column()
  public Long getOqty() {
    return oqty;
  }

  public void setOqty(Long oqty) {
    this.oqty = oqty;
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
  public String getStorageName() {
    return storageName;
  }

  public void setStorageName(String storageName) {
    this.storageName = storageName;
  }

  @Transient
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

    @Column(nullable = false)
    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }
}
