package com.casesoft.dmc.model.product;

import javax.persistence.*;

/**
 * Style 实体类
 */
@Entity
@Table(name = "PRODUCT_STYLE")
public class Style extends BaseStyle implements java.io.Serializable {

    private static final long serialVersionUID = 8879949042280757413L;
    //@JSONField(serialize = false)
    private String id;

    private String styleEname;

    private Integer seqNo;

    private String remark;

    private String isUse;

    @Column()
    private String ispush;

    @Column()
    private String pushsuccess;

    @Column
    private Integer styleCycle; //款销售退货周期，默认20天

    @Transient
    private String url;

    @Transient
    private String class1Name;

    @Column()
    private String isSeries;

    public String getIsSeries() {
        return isSeries;
    }

    public void setIsSeries(String isSeries) {
        this.isSeries = isSeries;
    }

    public Integer getStyleCycle() { return styleCycle; }

    public void setStyleCycle(Integer styleCycle) { this.styleCycle = styleCycle; }

    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    public String getPushsuccess() {
        return pushsuccess;
    }

    public void setPushsuccess(String pushsuccess) {
        this.pushsuccess = pushsuccess;
    }
// Constructors

    /**
     * default constructor
     */
    public Style() {
    }

    public Style(String id, String styleId, String styleName, double price) {
        super();
        this.id = id;
        this.styleId = styleId;
        this.styleName = styleName;
        this.price = price;

    }

    public Style(String id, String styleId, int isDeton, String sizeSort, String styleName, String styleEname, double price
            , double preCast, double wsPrice,double purPrice, String brandCode, String brand
            , String class1, String class2, String class3, String class4
            , String class5, String class6, String class7, String class8
            , String class9, String class10) {
        super();
        this.id = id;
        this.styleId = styleId;
        this.sizeSortId = sizeSort;
        this.styleName = styleName;
        this.styleEname = styleEname;
        this.price = price;
        super.setPreCast(preCast);
        super.setWsPrice(wsPrice);
        super.setPuPrice(purPrice);
        super.setBrandCode(brandCode);
        super.setClass1(class1);
        super.setClass2(class2);
        super.setClass3(class3);
        super.setClass4(class4);
        super.setClass5(class5);
        super.setClass6(class6);
        super.setClass7(class7);
        super.setClass8(class8);
        super.setClass9(class9);
        super.setClass10(class10);
//        super.setRules(rules);
    }

    /**
     * minimal constructor
     */
    public Style(String styleNo, String styleName, double price) {
        this.styleId = styleNo;
        this.styleName = styleName;
        this.price = price;
    }

    // Property accessors
    @Id
    @Column(nullable = false, length = 20)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column()
    public Integer getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }


    @Column(length = 400)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(length = 100)
    public String getStyleEname() {
        return styleEname;
    }

    public void setStyleEname(String styleEname) {
        this.styleEname = styleEname;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
}