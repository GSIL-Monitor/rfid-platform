package com.casesoft.dmc.model.logistics.vo;

import java.io.Serializable;

/**
 * Created by Anna on 2018/5/18.
 * 采购单以款汇总
 */
public class PurchaseStyleVo  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String styleId;
    private String styleName;
    private String imgUrl;

    public PurchaseStyleVo() { }

    public PurchaseStyleVo(String styleId, String styleName, String imgUrl) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.imgUrl = imgUrl;
    }

    public PurchaseStyleVo(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleId() { return styleId; }

    public void setStyleId(String styleId) { this.styleId = styleId; }

    public String getStyleName() { return styleName; }

    public void setStyleName(String styleName) { this.styleName = styleName; }

    public String getImgUrl() { return imgUrl; }

    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

}
