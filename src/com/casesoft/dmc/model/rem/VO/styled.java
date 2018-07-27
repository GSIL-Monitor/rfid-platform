package com.casesoft.dmc.model.rem.VO;

import java.math.BigDecimal;

/**
 * Created by lly on 2018/7/25.
 */
public class styled {
    private String styleId;
    private String warehouseId;
    private String floorRack;
    private String floorArea;
    private String floorAllocation;

    private String rackName;
    private String areaName;
    private String allocationName;
    private BigDecimal qty;

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
