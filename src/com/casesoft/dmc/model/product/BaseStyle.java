package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by Wing Li on 2014/6/16.
 */
@MappedSuperclass
public abstract class BaseStyle {
    protected String sizeSortName;
    protected Double preCast;//事前成本价(采购价)
    protected String sizeSortId;
    protected String styleId;
    protected String styleName;
    protected double price;//吊牌价格

    protected String updateTime;//登记时间
    protected String oprId;//登记人

    //展示信息
    protected String sampleCode;
    protected String image;
    protected String thumb;


    //品牌信息
    protected String brandCode; //品牌编号
    protected String brandName;
    protected String salesDate;//上市日期
    protected String expDate;//退市日期
    protected Double puPrice;//代理商批发价格
    protected Double wsPrice;//门店批发价格

    protected Integer isReexp;//能否退货
    protected Integer ident;//能否订货

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


    @Column(unique = true, nullable = false, length = 20)
    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String styleNo) {
        this.styleId = styleNo;
    }

    @Column(nullable = false, length = 100)
    public String getStyleName() {
        return this.styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Column()
    public String getSizeSortId() {
        return this.sizeSortId;
    }

    public void setSizeSortId(String sizeSortId) {
        this.sizeSortId = sizeSortId;
    }

    @Column(nullable = false, precision = 10)
    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column()
    public Integer getIsReexp() {
        return isReexp;
    }

    public void setIsReexp(Integer isReexp) {
        this.isReexp = isReexp;
    }

    @Column()
    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    @Column(length = 15)
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Column(length = 15)
    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    @Column(length = 15)
    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    @Column(length = 15)
    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    @Column(length = 15)
    public String getClass5() {
        return class5;
    }

    public void setClass5(String class5) {
        this.class5 = class5;
    }


    @Transient
    public String getSizeSortName() {
        return sizeSortName;
    }

    public void setSizeSortName(String sizeSortName) {
        this.sizeSortName = sizeSortName;
    }

    @Column()
    public Double getPreCast() {
        return preCast;
    }

    public void setPreCast(Double preCast) {
        this.preCast = preCast;
    }


    @Column(length = 30)
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Column(length = 50)
    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    @Column(length = 30)
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    @Column(length = 500)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(length = 50)
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Column(length = 30)
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    @Column()
    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    @Column()
    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }


    @Column()
    public Double getPuPrice() {
        return puPrice;
    }

    public void setPuPrice(Double puPrice) {
        this.puPrice = puPrice;
    }

    @Column()
    public Double getWsPrice() {
        return wsPrice;
    }

    public void setWsPrice(Double wsPrice) {
        this.wsPrice = wsPrice;
    }

    @Column(length = 15)
    public String getClass6() {
        return class6;
    }

    public void setClass6(String class6) {
        this.class6 = class6;
    }

    @Column(length = 15)
    public String getClass7() {
        return class7;
    }

    public void setClass7(String class7) {
        this.class7 = class7;
    }

    @Column(length = 15)
    public String getClass8() {
        return class8;
    }

    public void setClass8(String class8) {
        this.class8 = class8;
    }

    @Column(length = 15)
    public String getClass9() {
        return class9;
    }

    public void setClass9(String class9) {
        this.class9 = class9;
    }

    @Column(length = 15)
    public String getClass10() {
        return class10;
    }

    //  @Column(length = 50)
    public void setClass10(String class10) {
        this.class10 = class10;
    }

    @Transient
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
