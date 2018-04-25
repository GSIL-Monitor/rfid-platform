package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by Administrator on 2018/4/20.
 */
public class TransByStyleId {
    @Excel(name="款式")
    private String styleid;
    @Excel(name="款式名称")
    private String stylename;
    @Excel(name="调拨数量")
    private String totqty;
    @Excel(name="大分类")
    private String class3;
    @Excel(name="小分类")
    private String class4;
    @Excel(name="材质")
    private String class8;
    @Excel(name="年份")
    private String class2;

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public String getTotqty() {
        return totqty;
    }

    public void setTotqty(String totqty) {
        this.totqty = totqty;
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

    public String getClass8() {
        return class8;
    }

    public void setClass8(String class8) {
        this.class8 = class8;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }
}
