package com.casesoft.dmc.model.logistics;

import java.math.BigDecimal;

/**
 * Created by ChenZhiFan on 2018/8/11.
 */
public class FloorallocationAndSku {
    private String sku;
    private String floorallocation;
    private BigDecimal sum;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getFloorallocation() {
        return floorallocation;
    }

    public void setFloorallocation(String floorallocation) {
        this.floorallocation = floorallocation;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
