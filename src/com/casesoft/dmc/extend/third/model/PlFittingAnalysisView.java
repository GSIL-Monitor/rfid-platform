package com.casesoft.dmc.extend.third.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 2017-03-01.
 */
@Entity
@Table(name = "THIRD_PL_FITTINGANALYSIS_VIEW")//THIRD_PLFITTINGANALYSIS_VIEW
public class PlFittingAnalysisView implements Serializable {
    private String stockDay;
    private String stockCode;

    private String styleId;
    private String colorId;
    private Date inDate;
    private String styleName;
    private String colorName;
    private String stockName;
    private Long fittingQty;//试衣数量
    private Long stockQty;//库存数量
    private Long saleQty;//销售数量
    private Double avgPercent;//平均折扣
    private Double actPrice;
    private Long backQty;//退货数量
    private Long backPrice;

    private String class1;
    private String class2;
    private String class3;
    private String class4;
    private String class10;
    private String lastestSaleDate;//最新销售日期
    private String lastestFittingDate;//最新试衣日期
    private Double stockPrice;//库存金额
    private Long inDays;//进店天数
    private String imageUrl;
    private Integer lazyDays;//未动天数

    /**
     * 月统计
     * */
    private Long fittingMonthQty;
    private Long saleMonthQty;
    private Double avgMonthPercent;
    private Long backMonthQty;
    private Double backMonthPrice;
    @Column(name = "backQty")
    public Long getBackQty() {
        return backQty;
    }

    public void setBackQty(Long backQty) {
        this.backQty = backQty;
    }
    @Column(name = "backPrice")
    public Long getBackPrice() {
        return backPrice;
    }

    public void setBackPrice(Long backPrice) {
        this.backPrice = backPrice;
    }
    @Column(name = "fittingMonthQty")
    public Long getFittingMonthQty() {
        return fittingMonthQty;
    }

    public void setFittingMonthQty(Long fittingMonthQty) {
        this.fittingMonthQty = fittingMonthQty;
    }
    @Column(name = "saleMonthQty")
    public Long getSaleMonthQty() {
        return saleMonthQty;
    }

    public void setSaleMonthQty(Long saleMonthQty) {
        this.saleMonthQty = saleMonthQty;
    }
    @Column(name = "avgMonthPercent")
    public Double getAvgMonthPercent() {
        return avgMonthPercent;
    }
    public void setAvgMonthPercent(Double avgMonthPercent) {
        this.avgMonthPercent = avgMonthPercent;
    }
    @Column(name = "backMonthQty")
    public Long getBackMonthQty() {
        return backMonthQty;
    }

    public void setBackMonthQty(Long backMonthQty) {
        this.backMonthQty = backMonthQty;
    }
    @Column(name = "backMonthPrice")
    public Double getBackMonthPrice() {
        return backMonthPrice;
    }

    public void setBackMonthPrice(Double backMonthPrice) {
        this.backMonthPrice = backMonthPrice;
    }

    @Column(name = "lazyDays")
    public Integer getLazyDays() {
        return lazyDays;
    }

    public void setLazyDays(Integer lazyDays) {
        this.lazyDays = lazyDays;
    }

    @Column(name = "inDays")
    public Long getInDays() {
        return inDays;
    }

    public void setInDays(Long inDays) {
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
    @Column(name = "stockCode", length = 50)
    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
    @Id
    @Column(name = "styleId", length = 50)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    @Id
    @Column(name = "colorId", length = 50)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
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