package com.casesoft.dmc.model.wms.pl;

import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.model.wms.BaseFloor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Entity
@Table(name = "pl_wms_Rack")
public class PlWmsRack extends BaseFloor implements Serializable {
    /**
     *
     */
    @Id
    @Column(name = "id", length = 32)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "parentId", nullable = false, length = 60)
    private String parentId;
    @Transient
    private String parentName;
    @Column(name = "skuQty")
    private Integer skuQty;
    @Column(name = "saleTotQty")
    private Long saleTotQty;//销售数量
    @Column(name = "saleTotPrice")
    private Double saleTotPrice;//
    @Column(name = "backTotQty")
    private Long backTotQty;
    @Column(name = "backTotPrice")
    private Double backTotPrice;//

    public Long getBackTotQty() {
        return backTotQty;
    }

    public void setBackTotQty(Long backTotQty) {
        this.backTotQty = backTotQty;
    }

    public Double getBackTotPrice() {
        return backTotPrice;
    }

    public void setBackTotPrice(Double backTotPrice) {
        this.backTotPrice = backTotPrice;
    }

    public Long getSaleTotQty() {
        return saleTotQty;
    }

    public void setSaleTotQty(Long saleTotQty) {
        this.saleTotQty = saleTotQty;
    }

    public Double getSaleTotPrice() {
        return saleTotPrice;
    }

    public void setSaleTotPrice(Double saleTotPrice) {
        this.saleTotPrice = saleTotPrice;
    }

    public PlWmsRack() {
        super();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Integer skuQty) {
        this.skuQty = skuQty;
    }


    @Transient
    private String floorAreaBarcode;
    @Transient
    private String floorAreaName;
    @Transient
    private String warehouseCode;
    @Transient
    private String warehouseName;

    @Transient
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Transient
    public String getFloorAreaBarcode() {
        return floorAreaBarcode;
    }

    public void setFloorAreaBarcode(String floorAreaBarcode) {
        this.floorAreaBarcode = floorAreaBarcode;
    }

    @Transient
    public String getFloorAreaName() {
        return floorAreaName;
    }

    public void setFloorAreaName(String floorAreaName) {
        this.floorAreaName = floorAreaName;
    }

    @Transient
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }


    @Transient
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Transient
    List<PlWmsShopBindingRelation> relations;

    public List<PlWmsShopBindingRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<PlWmsShopBindingRelation> relations) {
        this.relations = relations;
    }
}

