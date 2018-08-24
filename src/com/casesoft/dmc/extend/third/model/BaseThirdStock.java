package com.casesoft.dmc.extend.third.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 2017-02-27.
 */
@MappedSuperclass
public abstract class BaseThirdStock implements Serializable {

    private String sku;//sku
     

    private String barcode;//条码

    private String stockCode;//店仓编码
    private String styleId;
    private String colorId;
    private String sizeId;
    private Long qty;
    private Date inDate;
    private String styleName;
    private String colorName;
    private String sizeName;
    private String stockName;
    @Column(name="qty")
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    @Id
    @Column(name="sku",nullable = true,length = 50)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    @Id
    @Column(name="barcode",length = 50)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    @Id
    @Column(name="stockCode",length = 50)
    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
    @Column(name="styleId",nullable = true,length = 50)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    @Column(name="colorId",nullable = true,length = 50)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    @Column(name="sizeId",nullable = true,length = 50)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }
    @Column(name="inDate",nullable = true,length = 50)
    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
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
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Transient
    private Long totQty;//弄影总库存字段

    public Long getTotQty() {
        return totQty;
    }

    public void setTotQty(Long totQty) {
        this.totQty = totQty;
    }
}

