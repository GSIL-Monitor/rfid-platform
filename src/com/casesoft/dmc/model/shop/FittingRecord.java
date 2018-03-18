package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.erp.ErpStock;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SHOP_FITTINGRECORD")
public class FittingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String code;/* 唯一码 */
    private String deviceId;
    private String ownerId;//门店ID

    private Date scanTime;

    private String sku;
    private String styleId;
    private String colorId;
    private String sizeId;

    private String parentId;//2014-06-07 父组织id


    private String styleName;
    private String colorName;
    private String sizeName;
    private String unitName;
    private Double price;

    private String taskId;//关联Sore

    // Constructors

    public FittingRecord(Date scanTime, String ownerId, String styleId, String colorId, String sizeId, Long qty) {
        this.ownerId = ownerId;
        this.scanTime = scanTime;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }

    /**
     * default constructor
     */
    public FittingRecord() {
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

    @Column(nullable = false, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String epc) {
        this.code = epc;
    }

    @Column(nullable = false, length = 50)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    public Date getScanTime() {
        return scanTime;
    }

    public void setScanTime(Date scanTime) {
        this.scanTime = scanTime;
    }

    @Column(nullable = false, length = 50)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Column(length = 20)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column(length = 10)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @Column(length = 10)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    @Column(length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(nullable = false, length = 50)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Transient
    private Long qty;
    @Transient
    private String image;
    @Transient
    private String floorId;//库位号
    @Transient
    private String rackId;//货架号
    @Transient
    private List<ErpStock> otherErpStocks;//其他库存

    @Transient
    private Long stockQty;

    @Transient
    public List<ErpStock> getOtherErpStocks() {
        return otherErpStocks;
    }

    public void setOtherErpStocks(List<ErpStock> otherErpStocks) {
        this.otherErpStocks = otherErpStocks;
    }
    @Transient
    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    @Transient
    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
    @Transient
    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    @Transient
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Transient
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    @Transient
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Transient
    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    @Transient
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @Transient
    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Transient
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
