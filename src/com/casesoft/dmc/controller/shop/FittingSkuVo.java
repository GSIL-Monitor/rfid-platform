package com.casesoft.dmc.controller.shop;

import java.util.Date;

/**
 * Created by WingLi on 2015-03-03.
 */
public class FittingSkuVo implements java.io.Serializable {

    private Date fittingTime;

    private String ownerId;
    private String unitName;
    private String styleId;
    private String colorId;
    private String sizeId;

    private Double price;
    private String styleName;
    private String colorName;
    private String sizeName;

    private long qty; //次数

    public FittingSkuVo() {
    }

    public FittingSkuVo(String ownerId, String styleId, String colorId, String sizeId, long qty) {
        this.ownerId = ownerId;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }
    public FittingSkuVo(String styleId, String colorId, String sizeId, long qty) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }
    public FittingSkuVo(Date fittingTime, String ownerId, String styleId, String colorId, String sizeId, long qty) {
        this.fittingTime = fittingTime;
        this.ownerId = ownerId;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }

    public FittingSkuVo(Date fittingTime, String ownerId, String unitName, String styleId, String colorId, String sizeId, Double price, String styleName, String colorName, String sizeName, long qty) {
        this.fittingTime = fittingTime;
        this.ownerId = ownerId;
        this.unitName = unitName;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.price = price;
        this.styleName = styleName;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.qty = qty;
    }

    public Date getFittingTime() {
        return fittingTime;
    }

    public void setFittingTime(Date fittingTime) {
        this.fittingTime = fittingTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
}
