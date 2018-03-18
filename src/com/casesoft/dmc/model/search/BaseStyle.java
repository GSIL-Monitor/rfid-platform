package com.casesoft.dmc.model.search;





import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;

/**
 * Created by WingLi on 2016-01-15.
 *  保持到数据库的字段
 */

@MappedSuperclass
public class BaseStyle implements Serializable {
    @Column(unique = true,nullable=false,length=20)
    @Excel(name = "款号")
    protected String styleId;
    @Column(nullable=false,length=100)
    @Excel(name = "款名")
    protected String styleName;

    @Column(nullable=false)
    @Excel(name = "吊牌价")
    protected double price;
    @Column(length=30)
    protected String brandCode;
    @Column(length=5)
    protected String sizeSortId;
    @Column(length=30)
    protected String styleEName;//英文名
    @Column(length=5)
    protected String class1;
    @Column(length=5)
    protected String class2;
    @Column(length=5)
    protected String class3;
    @Column(length=5)
    protected String class4;
    @Column(length=5)
    protected String class5;
    @Column(length=5)
    protected String class6;
    @Column(length=5)
    protected String class7;
    @Column(length=5)
    protected String class8;
    @Column(length=5)
    protected String class9;
    @Column(length=5)
    protected String class10;



    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }


    public String getStyleEName() {
        return styleEName;
    }

    public void setStyleEName(String styleEName) {
        this.styleEName = styleEName;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }





    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }




    public String getSizeSortId() {
        return sizeSortId;
    }

    public void setSizeSortId(String sizeSortId) {
        this.sizeSortId = sizeSortId;
    }



    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }



    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
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


    public String getClass5() {
        return class5;
    }

    public void setClass5(String class5) {
        this.class5 = class5;
    }



    public String getClass6() {
        return class6;
    }

    public void setClass6(String class6) {
        this.class6 = class6;
    }


    public String getClass7() {
        return class7;
    }

    public void setClass7(String class7) {
        this.class7 = class7;
    }



    public String getClass8() {
        return class8;
    }

    public void setClass8(String class8) {
        this.class8 = class8;
    }

    public String getClass10() {
        return class10;
    }

    public void setClass10(String class10) {
        this.class10 = class10;
    }

    public String getClass9() {
        return class9;
    }

    public void setClass9(String class9) {
        this.class9 = class9;
    }
}
