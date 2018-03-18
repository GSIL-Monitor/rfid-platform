package com.casesoft.dmc.extend.third.searcher.model;

import com.casesoft.dmc.model.erp.ErpStock;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 2017-04-17.
 * 找货
 */
@Entity
@Table(name = "Third_search_dtl")
public class SearchDtl implements Serializable{


    private String id;
    private String mainId;//找货主单Id
    private String sku;
    private String barcode;
    private String styleId;
    private String colorId;
    private String sizeId;
    private Boolean searchSuccess =false;
    private String status = SearchMain.Status.MAIN_STATUS_SEND;//状态
    private String remark;
    private String origCode;//所属门店
    @Column(name = "status", nullable = false,length = 5)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "origCode", length = 55,nullable = false)
    public String getOrigCode() {
        return origCode;
    }

    public void setOrigCode(String origCode) {
        this.origCode = origCode;
    }
    @Transient
    private Long stockQty;

    @Transient
    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    @Id
     @Column(name="id",length = 100)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="mainId",length = 55,nullable = false)
    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }
    @Column(name="sku",length = 55,nullable = false)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    @Column(name="barcode",length = 55,nullable = false)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    @Column(name="styleId",length = 55,nullable = false)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    @Column(name="colorId",length = 55,nullable = false)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    @Column(name="sizeId",length = 55,nullable = false)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }
    @Column(name="searchSuccess",nullable = false)
    public Boolean getSearchSuccess() {
        return searchSuccess;
    }

    public void setSearchSuccess(Boolean searchSuccess) {
        this.searchSuccess = searchSuccess;
    }
    @Column(name="remark",length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Transient
    protected String styleName;
    @Transient
    protected String colorName;
    @Transient
    protected String sizeName;
    @Transient
    private List<ErpStock> otherErpStocks;
    @Transient
    private String floorId;
    @Transient
    private String image;
    @Transient
    private String rackId;//货架号
    @Transient
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    @Column(name="rackId",length = 50)
    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    @Transient
    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    @Transient
    public List<ErpStock> getOtherErpStocks() {
        return otherErpStocks;
    }

    public void setOtherErpStocks(List<ErpStock> otherErpStocks) {
        this.otherErpStocks = otherErpStocks;
    }
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
}
