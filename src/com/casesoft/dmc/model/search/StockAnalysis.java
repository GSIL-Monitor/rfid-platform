package com.casesoft.dmc.model.search;

/**
 * Created by yushen on 2017/11/17.
 */
public class StockAnalysis {
    private String wareHouseId;
    private String wareHouseName;
    private String sku;
    private Integer inStockQty;
    private Integer inStockTime;

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getInStockQty() {
        return inStockQty;
    }

    public void setInStockQty(Integer inStockQty) {
        this.inStockQty = inStockQty;
    }

    public Integer getInStockTime() {
        return inStockTime;
    }

    public void setInStockTime(Integer inStockTime) {
        this.inStockTime = inStockTime;
    }
}
