package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class BaseProduct {

    @Transient
    protected String styleName;
    @Transient
    protected String colorName;
    @Transient
    protected String sizeName;


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

    protected double price;
    protected double preCast;
    protected double wsPrice;
    protected double puPrice;

    @Transient
    public Double getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(Double bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    protected Double bargainPrice;//特价

    @Transient
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Transient
    public double getPuPrice() {
        return puPrice;
    }

    public void setPuPrice(double puPrice) {
        this.puPrice = puPrice;
    }

    @Column(length = 30)
    protected String updateTime;
    @Column(length = 50)
    protected String oprId;
    //品牌信息
    @Column(length = 30)
    protected String brandCode;
    @Column(length = 50)
    protected String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Column(length = 50)
    protected String ean;

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    protected String class1;//年份
    protected String class2;//季节
    protected String class3;//性别
    protected String class4;//大类
    protected String class5;//小类
    protected String class6;//面料
    protected String class7;//主题系列
    protected String class8;//商品质量
    protected String class9;//商品分类
    protected String class10;//商品级别

    protected String class1Name;
    protected String class2Name;
    protected String class3Name;
    protected String class4Name;
    protected String class5Name;
    protected String class6Name;
    protected String class7Name;
    protected String class8Name;
    protected String class9Name;
    protected String class10Name;

    protected String styleRemark;
    protected String brandName;
    protected String styleSortName;
    protected String sizeSortName;

    @Transient
    public String getStyleSortName() {
        return styleSortName;
    }

    public void setStyleSortName(String styleSortName) {
        this.styleSortName = styleSortName;
    }

    @Transient
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Transient
    public String getStyleRemark() {
        return styleRemark;
    }

    public void setStyleRemark(String styleRemark) {
        this.styleRemark = styleRemark;
    }

    @Transient
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Transient
    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    @Transient
    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    @Transient
    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    @Transient
    public String getClass5() {
        return class5;
    }

    public void setClass5(String class5) {
        this.class5 = class5;
    }

    @Transient
    public String getClass6() {
        return class6;
    }

    public void setClass6(String class6) {
        this.class6 = class6;
    }

    @Transient
    public String getClass7() {
        return class7;
    }

    public void setClass7(String class7) {
        this.class7 = class7;
    }

    @Transient
    public String getClass8() {
        return class8;
    }

    public void setClass8(String class8) {
        this.class8 = class8;
    }

    @Transient
    public String getClass9() {
        return class9;
    }

    public void setClass9(String class9) {
        this.class9 = class9;
    }

    @Transient
    public String getClass10() {
        return class10;
    }

    public void setClass10(String class10) {
        this.class10 = class10;
    }

    @Transient
    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    @Transient
    public String getClass2Name() {
        return class2Name;
    }

    public void setClass2Name(String class2Name) {
        this.class2Name = class2Name;
    }

    @Transient
    public String getClass3Name() {
        return class3Name;
    }

    public void setClass3Name(String class3Name) {
        this.class3Name = class3Name;
    }

    @Transient
    public String getClass4Name() {
        return class4Name;
    }

    public void setClass4Name(String class4Name) {
        this.class4Name = class4Name;
    }

    @Transient
    public String getClass5Name() {
        return class5Name;
    }

    public void setClass5Name(String class5Name) {
        this.class5Name = class5Name;
    }

    @Transient
    public String getClass6Name() {
        return class6Name;
    }

    public void setClass6Name(String class6Name) {
        this.class6Name = class6Name;
    }

    @Transient
    public String getClass7Name() {
        return class7Name;
    }

    public void setClass7Name(String class7Name) {
        this.class7Name = class7Name;
    }

    @Transient
    public String getClass8Name() {
        return class8Name;
    }

    public void setClass8Name(String class8Name) {
        this.class8Name = class8Name;
    }

    @Transient
    public String getClass9Name() {
        return class9Name;
    }

    public void setClass9Name(String class9Name) {
        this.class9Name = class9Name;
    }

    @Transient
    public String getClass10Name() {
        return class10Name;
    }

    public void setClass10Name(String class10Name) {
        this.class10Name = class10Name;
    }

    @Transient
    public double getPreCast() {
        return preCast;
    }

    public void setPreCast(double preCast) {
        this.preCast = preCast;
    }

    @Transient
    public double getWsPrice() {
        return wsPrice;
    }

    public void setWsPrice(double wsPrice) {
        this.wsPrice = wsPrice;
    }

    @Transient
    public String getSizeSortName() {
        return sizeSortName;
    }

    public void setSizeSortName(String sizeSortName) {
        this.sizeSortName = sizeSortName;
    }
}
