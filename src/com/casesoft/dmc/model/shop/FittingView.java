package com.casesoft.dmc.model.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEARCH_FITVIEW")
public class FittingView implements java.io.Serializable{
    @Id
    @Column(length = 100)
    private String id;
    @Column(length = 50)
    private String fitTime;
    @Column(length = 50)
    private String shop;
    @Column(length = 50)
    private String sku;
    @Column(length = 50)
    private String style;
    @Column(length = 50)
    private String styleId;
    @Column(length = 50)
    private String colorId;
    @Column(length = 50)
    private String color;
    @Column(length = 50)
    private String sizeId;
    @Column(length = 50)
    private String sizeName;
    @Column(length = 50)
    private Float price;
    @Column(length = 50)
    private String brand;
    @Column(length = 50)
    private String class3;
    @Column(length = 50)
    private String class4;
    @Column(length = 50)
    private String class10;
    @Column(length = 50)
    private Integer fT;

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public Integer getfT() {
        return fT;
    }

    public void setfT(Integer fT) {
        this.fT = fT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFitTime() {
        return fitTime;
    }

    public void setFitTime(String fitTime) {
        this.fitTime = fitTime;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    public String getClass10() {
        return class10;
    }

    public void setClass10(String class10) {
        this.class10 = class10;
    }

    public Integer getFT() {
        return fT;
    }

    public void setFT(Integer FT) {
        this.fT = FT;
    }
}
