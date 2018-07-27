package com.casesoft.dmc.model.rem.VO;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Created by lly on 2018/7/25.
 */
public class sku {
    private String sku;
    private String styleId;
    private String colorId;
    private String sizeId;
    private String warehouseId;
    private String floorRack;
    private String floorArea;
    private String floorAllocation;

    private String rackName;
    private String areaName;
    private String allocationName;
    private BigDecimal qty;

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAllocationName() {
        return allocationName;
    }

    public void setAllocationName(String allocationName) {
        this.allocationName = allocationName;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getFloorRack() {
        return floorRack;
    }

    public void setFloorRack(String floorRack) {
        this.floorRack = floorRack;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getFloorAllocation() {
        return floorAllocation;
    }

    public void setFloorAllocation(String floorAllocation) {
        this.floorAllocation = floorAllocation;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
