package com.casesoft.dmc.model.search;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/12/25.
 */
@Entity
@Table(name = "search_detailstockviews_sku")
public class DetailStockskuView  extends BaseProductView {
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

    @Column()
    @Excel(name = "SKU", width = 20D)
    private String sku;
    @Column()
    @Excel(name = "库存数量")
    private Integer qty;
    @Column()
    private Double precast =0D;

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

    @Column()

    private Double puprice=0D;
    @Column()
    private Double wsprice=0D;

    @Transient
    @Excel(name = "仓库")
    private String warehName;
    @Transient
    private String colorname;
    @Transient
    @Excel(name = "库存金额")
    private Double inStockPrice;
}
