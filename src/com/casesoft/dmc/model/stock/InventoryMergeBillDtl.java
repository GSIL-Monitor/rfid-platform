package com.casesoft.dmc.model.stock;





import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * Created by yushen on 2017/12/4.
 */
@Entity
@Table(name = "STOCK_InventoryMergeBillDtl")
public class InventoryMergeBillDtl {
    @Id
    @Column
    private String id;              //随机数
    @Column
    @Excel(name = "单号", width = 20D)
    private String billNo;          //合并单据编号
    @Column
    @Excel(name = "唯一码", width = 20D)
    private String code;
    @Column
    @Excel(name = "SKU", width = 20D)
    private String sku;
    @Column
    @Excel(name = "款号")
    private String styleId;
    @Transient
    @Excel(name = "款名")
    private String styleName;
    @Column
    @Excel(name = "颜色")
    private String colorId;
    @Column
    @Excel(name = "尺寸")
    private String sizeId;
    @Column
    @Excel(name = "统计次数")
    private Long countTimes;     //唯一码被统计次数，在库存不变的情况下 InventoryMergeBill.mergeBillQty = InventoryMergeBillDtl.countTimes
    @Column
    @Excel(name = "被扫描次数")
    private Long scannedQty;     //唯一码被扫描次数
    @Column
    @Excel(name = "在库")
    private Integer inStock;        //是否在库
    @Transient
    @Excel(name = "单价")
    private Double price;           //吊牌价
    @Column
    @Excel(name = "状态")      //状态
    private String state="N";

    public InventoryMergeBillDtl() {

    }

    @Override
    public String toString() {
        return "InventoryMergeBillDtl{" +
                "id='" + id + '\'' +
                ", billNo='" + billNo + '\'' +
                ", code='" + code + '\'' +
                ", sku='" + sku + '\'' +
                ", styleId='" + styleId + '\'' +
                ", styleName='" + styleName + '\'' +
                ", colorId='" + colorId + '\'' +
                ", sizeId='" + sizeId + '\'' +
                ", countTimes=" + countTimes +
                ", scannedQty=" + scannedQty +
                ", inStock=" + inStock +
                ", price=" + price +
                ", state='" + state + '\'' +
                '}';
    }

    public InventoryMergeBillDtl(String sku, String styleId, String colorId, String sizeId, String code,
                                 Long countTimes, Long scannedQty, Integer inStock) {
        this.sku = sku;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.code = code;
        this.countTimes = countTimes;
        this.scannedQty = scannedQty;
        this.inStock = inStock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getCountTimes() {
        return countTimes;
    }

    public void setCountTimes(Long countTimes) {
        this.countTimes = countTimes;
    }

    public Long getScannedQty() {
        return scannedQty;
    }

    public void setScannedQty(Long scannedQty) {
        this.scannedQty = scannedQty;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getState() {

        return state;
    }
}
