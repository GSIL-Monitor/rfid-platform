package com.casesoft.dmc.model.logistics.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yushen on 2018/5/12.
 * 补货追踪以款汇总
 */
public class ReplenishStyleVO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String styleId;
    private String styleName;
    private String imgUrl;
    private List<ReplenishSkuVO> skuVOList;
    private Long styleTotQty;
    private Integer styleTotActConvertQty;
    private Long styleTotInstockQty;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<ReplenishSkuVO> getSkuVOList() {
        return skuVOList;
    }

    public void setSkuVOList(List<ReplenishSkuVO> skuVOList) {
        this.skuVOList = skuVOList;
    }

    public Long getStyleTotQty() { return styleTotQty; }

    public void setStyleTotQty(Long styleTotQty) { this.styleTotQty = styleTotQty; }

    public Integer getStyleTotActConvertQty() { return styleTotActConvertQty; }

    public void setStyleTotActConvertQty(Integer styleTotActConvertQty) { this.styleTotActConvertQty = styleTotActConvertQty; }

    public Long getStyleTotInstockQty() { return styleTotInstockQty; }

    public void setStyleTotInstockQty(Long styleTotInstockQty) { this.styleTotInstockQty = styleTotInstockQty; }
}