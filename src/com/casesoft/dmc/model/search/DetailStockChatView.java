package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/12/11.
 */
@Entity
@Table(name = "search_chatdetailstockviews")
public class DetailStockChatView {
    @Id
    @Column()
    private String id;

    @Column
    @Excel(name = "款号", width = 20D)
    private String styleId;
    @Column
    @Excel(name = "款名", width = 20D)
    private String styleName;
    @Column()
    @Excel(name = "库存", width = 20D)
    private Integer qty;

    @Column()
    private Double stockprice = 0D;
    @Column()
    @Excel(name = "吊牌价", width = 20D)
    private Double price=0D;
    @Column()
    @Excel(name = "仓库编号", width = 20D)
    private String warehId;
    @Column
    @Excel(name = "仓库", width = 20D)
    private String warehName;
    @Column()
    private Double precast =0D;
    @Column()
    private Double puprice=0D;
    @Column(length=5)
    private String sizeSortId;
    @Column()
    private Double wsprice=0D;
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
    @Column(length=5)
    private String class7;
    @Column(length=5)
    private String class8;
    @Column(length=5)
    private String class9;
    @Column(length=5)
    private String class10;
    @Column()
    private String ownerId;
    @Column()
    private Integer warehType;



    @Column()
    @Excel(name = "厂家")
    private String class1Name;
    @Column()
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }



    @Transient
    @Excel(name = "图片", type = 2 ,width = 20 , height = 20)
    private String url;

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getStockprice() {
        return stockprice;
    }

    public void setStockprice(Double stockprice) {
        this.stockprice = stockprice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getSizeSortId() {
        return sizeSortId;
    }

    public void setSizeSortId(String sizeSortId) {
        this.sizeSortId = sizeSortId;
    }

    public Double getWsprice() {
        return wsprice;
    }

    public void setWsprice(Double wsprice) {
        this.wsprice = wsprice;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getWarehType() {
        return warehType;
    }

    public void setWarehType(Integer warehType) {
        this.warehType = warehType;
    }

    public String getWarehId() {
        return warehId;
    }

    public void setWarehId(String warehId) {
        this.warehId = warehId;
    }

    public String getWarehName() {
        return warehName;
    }

    public void setWarehName(String warehName) {
        this.warehName = warehName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
