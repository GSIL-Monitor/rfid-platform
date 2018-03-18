package com.casesoft.dmc.extend.third.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 2017-02-28.
 */
@Entity
/*SaleStockThirdStockView
@Table(name = "THIRD_STOCKSALE_VIEW1")
*/
@Table(name = "THIRD_SaleStockThird_VIEW")//THIRD_STOCKSALE_VIEW
public class SaleStockThirdStockView implements Serializable {
    private String stockDay;
    private String sku;//sku
    private String stockCode;


    private String styleId;
    private String colorId;
    private String sizeId;
    private Date inDate;
    private String styleName;
    private String colorName;
    private String sizeName;
    private String stockName;
    private Long fittingQty;//试衣数量
    private Long stockQty;//库存数量
    private Long saleQty;//销售数量
    private Double avgPercent;//平均折扣
    private Double actPrice;
    private String class1;//年份
    private String class2;//季节
    private String class3;//性别
    private String class4;//大类
    private String class10;//季度
    private String lastestSaleDate;//最新销售日期
    private String lastestFittingDate;//最新试衣日期
    private Double stockPrice;//库存金额
    private Long inDays;//进店天数
    private String imageUrl;
    @Column(name = "inDays")
    public Long getinDays() {
        return inDays;
    }

    public void setinDays(Long inDays) {
        this.inDays = inDays;
    }
    @Transient
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "stockPrice")
    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Column(name = "actPrice")
    public Double getActPrice() {
        return actPrice;
    }

    public void setActPrice(Double actPrice) {
        this.actPrice = actPrice;
    }

    @Id
    @Column(name = "stockDay", length = 40)
    public String getStockDay() {
        return stockDay;
    }

    public void setStockDay(String stockDay) {
        this.stockDay = stockDay;
    }

    @Id
    @Column(name = "sku", length = 50)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Id
    @Column(name = "stockCode", length = 50)
    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    @Column(name = "styleId", length = 50)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column(name = "colorId", length = 50)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @Column(name = "sizeId", length = 50)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "inDate")
    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    @Column(name="styleName",length = 200)
    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

   @Column(name="colorName",length = 200)
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

     @Column(name="sizeName",length = 200)
    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    //@Column(name="sizeName",length = 300)
    @Transient
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Column(name = "fittingQty")
    public Long getFittingQty() {
        return fittingQty;
    }

    public void setFittingQty(Long fittingQty) {
        this.fittingQty = fittingQty;
    }

    @Column(name = "stockQty")
    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    @Column(name = "saleQty")
    public Long getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Long saleQty) {
        this.saleQty = saleQty;
    }
    @Column(name = "class1",length = 40)
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }
    @Column(name = "class2",length = 40)
    public String getClass2() {
        return class2;
    }
    public void setClass2(String class2) {
        this.class2 = class2;
    }
    @Column(name = "class3",length = 40)
    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }
    @Column(name = "class4",length = 40)
    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }
    @Column(name = "class10",length = 40)
    public String getClass10() {
        return class10;
    }
    public void setClass10(String class10) {
        this.class10 = class10;
    }
    @Column(name = "lastestSaleDate",length = 40)
    public String getLastestSaleDate() {
        return lastestSaleDate;
    }

    public void setLastestSaleDate(String lastestSaleDate) {
        this.lastestSaleDate = lastestSaleDate;
    }
    @Column(name = "lastestFittingDate",length = 40)
    public String getLastestFittingDate() {
        return lastestFittingDate;
    }

    public void setLastestFittingDate(String lastestFittingDate) {
        this.lastestFittingDate = lastestFittingDate;
    }

    @Column(name = "avgPercent")
    public Double getAvgPercent() {
        return avgPercent;
    }

    public void setAvgPercent(Double avgPercent) {
        this.avgPercent = avgPercent;
    }
}
