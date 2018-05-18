package com.casesoft.dmc.model.logistics.vo;

import java.io.Serializable;

/**
 * Created by yushen on 2018/5/12.
 * 补货追踪显示每一个唯一码
 */
public class ReplenishCodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sku;
    private String code;
    private String warehouseId;
    private String warehouseName;
    private Integer inStock;

    public ReplenishCodeVO() { }

    public ReplenishCodeVO(String sku, String code, String warehouseId,Integer inStock) {
        this.sku = sku;
        this.code = code;
        this.warehouseId = warehouseId;
        this.inStock=inStock;
    }


    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() { return warehouseName; }

    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
}
