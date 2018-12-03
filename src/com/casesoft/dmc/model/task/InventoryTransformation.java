package com.casesoft.dmc.model.task;

import java.io.Serializable;

/**
 * @ClassName InventoryTransformation
 * @Description TODO 批量盘点中间转换表
 * @Author liutianci
 * @Date 2018/12/1 17:30
 * @Version 1.0
 **/
public class InventoryTransformation implements Serializable{
    private String styleId;
    private String colorId;
    private String sizeId;
    private Long qty;
    private String sku;
    private String uniqueCodes;

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


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUniqueCodes() {
        return uniqueCodes;
    }

    public void setUniqueCodes(String uniqueCodes) {
        this.uniqueCodes = uniqueCodes;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
}
