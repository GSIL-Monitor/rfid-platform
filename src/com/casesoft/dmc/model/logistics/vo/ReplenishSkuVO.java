package com.casesoft.dmc.model.logistics.vo;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yushen on 2018/5/12.
 * 补货追踪以SKU汇总
 */
public class ReplenishSkuVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sku;
    private String styleId;
    private String styleName;
    private String colorId;
    private String colorName;
    private String sizeId;
    private String sizeName;
    private List<ReplenishCodeVO> codeVOList;
    private Integer skuTotQty;
    private Integer skuTotActConvertQty;
    private Integer skuTotInstockQty;

    public ReplenishSkuVO(){}

    public ReplenishSkuVO(String sku, String styleId, String colorId, String sizeId, Integer skuTotQty, Integer skuTotActConvertQty, Integer skuTotInstockQty){
        this.sku = sku;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.skuTotQty = skuTotQty;
        this.skuTotActConvertQty = skuTotActConvertQty;
        this.skuTotInstockQty = skuTotInstockQty;
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

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public List<ReplenishCodeVO> getCodeVOList() {
        return codeVOList;
    }

    public void setCodeVOList(List<ReplenishCodeVO> codeVOList) {
        this.codeVOList = codeVOList;
    }

    public Integer getSkuTotQty() {
        return skuTotQty;
    }

    public void setSkuTotQty(Integer skuTotQty) {
        this.skuTotQty = skuTotQty;
    }

    public Integer getSkuTotActConvertQty() {
        return skuTotActConvertQty;
    }

    public void setSkuTotActConvertQty(Integer skuTotActConvertQty) {
        this.skuTotActConvertQty = skuTotActConvertQty;
    }

    public Integer getSkuTotInstockQty() {
        return skuTotInstockQty;
    }

    public void setSkuTotInstockQty(Integer skuTotInstockQty) {
        this.skuTotInstockQty = skuTotInstockQty;
    }
}
