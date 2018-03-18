package com.casesoft.dmc.model.wms.info;

import javax.persistence.*;

/**
 * Created by john on 2017-03-08.
 */
@MappedSuperclass
public class BaseWmsRelation {
    private String sku;//
    private String barcode;// 商品条码
    private String styleId;
    private String colorId;
    private String sizeId;
    private String rackId;//货架编号
    private String styleName;
    private String colorName;
    private String sizeName;

    @Transient
    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
    @Transient
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    @Transient
    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    @Id
    @Column(name = "rackId", length = 32)
    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }
    @Id
    @Column(name = "sku", nullable = false, length = 50)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    @Column(name = "barcode", length = 50)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    @Column(name = "styleId", nullable = false, length = 30)

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    @Column(name = "colorId", nullable = false, length = 20)

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    @Column(name = "sizeId", length = 20)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

}
