package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * Created by Alvin on 2018/2/3.
 * 进销存SKU统计
 */
@Entity
@Table(name = "PURCHASE_SALE_STOCK")
public class PurchaseSaleStock {
    @Id
    @Column()
    private String id;
    @Column()
    @Excel(name="仓库编号",width = 20D)
    private String warehId;
    @Transient
    @Excel(name="仓库名",width = 20D)
    private String warehName;
    @Column()
    @Excel(name="SKU",width = 20D)
    private String sku;
    @Transient
    @Excel(name="款号",width = 20D)
    private String styleId;
    @Transient
    @Excel(name="款名",width = 20D)
    private String styleName;
    @Transient
    @Excel(name="颜色",width = 10)
    private String colorId;
    @Transient
    @Excel(name="尺码",width = 10)
    private String sizeId;
    @Transient
    @Excel(name="价格",width = 20D)
    private Double price;
    @Transient
    @Excel(name="图片",width = 20D,height = 20D,type = 2)
    private String imgUrl;
    @Column()
    @Excel(name="入库数量",width = 20D)
    private Long inQty;
    @Column()
    @Excel(name="在途数量",width = 20D)
    private Long notInQty;
    @Column()
    @Excel(name="库存数量",width = 20D)
    private Long stockQty;
    @Column()
    @Excel(name="总销售量",width = 20D)
    private Long saleQty;
    @Column()
    @Excel(name="零售数量",width = 20D)
    private Long saleLSQty;
    @Column()
    @Excel(name="销售加盟商(在途)",width = 20D)
    private Long saleJMnotInQty;
    @Column()
    @Excel(name="调拨出库数量",width = 20D)
    private Long transferOutQty;
    @Column()
    @Excel(name="返厂数量",width = 20D)
    private Long backQty;
    @Column()
    @Excel(name="调整数量",width = 20D)
    private Long otherOutQty;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getInQty() {
        return inQty;
    }

    public void setInQty(Long inQty) {
        this.inQty = inQty;
    }

    public Long getNotInQty() {
        return notInQty;
    }

    public void setNotInQty(Long notInQty) {
        this.notInQty = notInQty;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Long getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Long saleQty) {
        this.saleQty = saleQty;
    }

    public Long getSaleJMnotInQty() {
        return saleJMnotInQty;
    }

    public Long getSaleLSQty() {
        return saleLSQty;
    }

    public void setSaleLSQty(Long saleLSQty) {
        this.saleLSQty = saleLSQty;
    }

    public void setSaleJMnotInQty(Long saleJMnotInQty) {
        this.saleJMnotInQty = saleJMnotInQty;
    }

    public Long getTransferOutQty() {
        return transferOutQty;
    }

    public void setTransferOutQty(Long transferOutQty) {
        this.transferOutQty = transferOutQty;
    }

    public Long getBackQty() {
        return backQty;
    }

    public void setBackQty(Long backQty) {
        this.backQty = backQty;
    }

    public Long getOtherOutQty() {
        return otherOutQty;
    }

    public void setOtherOutQty(Long otherOutQty) {
        this.otherOutQty = otherOutQty;
    }
}



