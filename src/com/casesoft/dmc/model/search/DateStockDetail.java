package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/25.
 */
@Entity
@Table(name = "search_DateStockDetail")
public class DateStockDetail {
    @Id
    @Column()
    private String id;
    @Column()
    private String ownerId;
    @Column()
    @Excel(name = "仓库ID", width = 20D)
    private String warehId;
    @Column()
    private Integer warehType;
  /*  @Column()
    private Double stockprice = 0D;*/
    @Column()
    @Excel(name = "SKU", width = 20D)
    private String sku;
    @Column()
    @Excel(name = "库存数量")
    private Integer qty;
    @Column()
    private Double precast =0D;
    @Column()
    private Double puprice=0D;
    @Column()
    private Double wsprice=0D;
  /*  @Column()
    private String insotretype;*/
    @Transient
    @Excel(name = "仓库")
    private String warehName;
    @Transient
    private String colorname;
    @Transient
    @Excel(name = "库存金额")
    private Double inStockPrice;

    @Column(nullable = false)
    @JSONField(format = "yyyy-MM-dd")
    @Excel(name = "时间")
    private Date billDate;
    @Column(length=200)
    private  String prodRemark;

    @Column(nullable = false, length = 20)
    @Excel(name = "颜色")
    private String colorId;

    @Column(nullable = false, length = 10)
    @Excel(name = "尺寸")
    private String sizeId;
    @Column(nullable=false,length=20)
    @Excel(name = "款号")
    private String styleId;
    @Column(nullable=false,length=100)
    @Excel(name = "款名")
    private String styleName;

    @Column(nullable=false)
    @Excel(name = "吊牌价")
    private double price;
    @Column(length=30)
    private String brandCode;
    @Column(length=5)
    private String sizeSortId;
    @Column(length=30)
    private String styleEName;//英文名
    @Column(length=5)
    private String class1;
    @Column(length=5)
    private String class2;
    @Column(length=5)
    private String class3;
    @Column(length=5)
    private String class4;
    @Column(length=5)
    private String class5;
    @Column(length=5)
    private String class6;
    @Column(length=20)
    private String class7;
    @Column(length=5)
    private String class8;
    @Column(length=5)
    private String class9;
    @Column(length=5)
    private String class10;


    public String getProdRemark() {
        return prodRemark;
    }

    public void setProdRemark(String prodRemark) {
        this.prodRemark = prodRemark;
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

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
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

    public String getSizeSortId() {
        return sizeSortId;
    }

    public void setSizeSortId(String sizeSortId) {
        this.sizeSortId = sizeSortId;
    }

    public String getStyleEName() {
        return styleEName;
    }

    public void setStyleEName(String styleEName) {
        this.styleEName = styleEName;
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

    public String getClass9() {
        return class9;
    }

    public void setClass9(String class9) {
        this.class9 = class9;
    }

    public String getClass10() {
        return class10;
    }

    public void setClass10(String class10) {
        this.class10 = class10;
    }

    public Date getBillDate() {
        return billDate;

    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getWarehId() {
        return warehId;
    }

    public void setWarehId(String warehId) {
        this.warehId = warehId;
    }

    public Integer getWarehType() {
        return warehType;
    }

    public void setWarehType(Integer warehType) {
        this.warehType = warehType;
    }

  /*  public Double getStockprice() {
        return stockprice;
    }

    public void setStockprice(Double stockprice) {
        this.stockprice = stockprice;
    }*/

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPrecast() {
        return precast;
    }

    public void setPrecast(Double precast) {
        this.precast = precast;
    }

    public Double getPuprice() {
        return puprice;
    }

    public void setPuprice(Double puprice) {
        this.puprice = puprice;
    }

    public Double getWsprice() {
        return wsprice;
    }

    public void setWsprice(Double wsprice) {
        this.wsprice = wsprice;
    }

   /* public String getInsotretype() {
        return insotretype;
    }

    public void setInsotretype(String insotretype) {
        this.insotretype = insotretype;
    }*/

    public String getWarehName() {
        return warehName;
    }

    public void setWarehName(String warehName) {
        this.warehName = warehName;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public Double getInStockPrice() {
        return inStockPrice;
    }

    public void setInStockPrice(Double inStockPrice) {
        this.inStockPrice = inStockPrice;
    }
}
