package com.casesoft.dmc.extend.third.model.pl;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Entity
@Table(name = "Pl_Warehouse_BindingRelation")
public class PlWmsWarehouseBindingRelation implements Serializable {
    @Id
    @Column(name = "Id", nullable = false, length = 80)
    private String  id;
    @Column(name = "styleId", nullable = false, length = 30)
    private String styleId;
    @Column(name = "colorId", nullable = false, length = 20)
    private String colorId;
    @Column(name = "floorId", length = 32)
    private String floorId;//库位编号

    @Transient
    private String styleName;
    @Transient
    private String colorName;
    @Transient
    private String floorBarcode;//库位条码
    @Transient
    private String floorName;
    @Transient
    private String floorImage;
    @Transient
    private String floorRemark;
    @Transient
    private String WarehouseFloorAreaId;
    @Transient
    private String WarehouseFloorAreaBarcode;
    @Transient
    private String WarehouseFloorAreaName;
    @Transient
    private String WarehouseFloorAreaImage;//
    @Transient
    private String WarehouseFloorAreaRemark;//
    @Transient
    private String unitCode;//店仓编码

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateDate")
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getFloorBarcode() {
        return floorBarcode;
    }

    public void setFloorBarcode(String floorBarcode) {
        this.floorBarcode = floorBarcode;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorImage() {
        return floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public String getFloorRemark() {
        return floorRemark;
    }

    public void setFloorRemark(String floorRemark) {
        this.floorRemark = floorRemark;
    }

    public String getWarehouseFloorAreaId() {
        return WarehouseFloorAreaId;
    }

    public void setWarehouseFloorAreaId(String warehouseFloorAreaId) {
        WarehouseFloorAreaId = warehouseFloorAreaId;
    }

    public String getWarehouseFloorAreaBarcode() {
        return WarehouseFloorAreaBarcode;
    }

    public void setWarehouseFloorAreaBarcode(String warehouseFloorAreaBarcode) {
        WarehouseFloorAreaBarcode = warehouseFloorAreaBarcode;
    }

    public String getWarehouseFloorAreaName() {
        return WarehouseFloorAreaName;
    }

    public void setWarehouseFloorAreaName(String warehouseFloorAreaName) {
        WarehouseFloorAreaName = warehouseFloorAreaName;
    }

    public String getWarehouseFloorAreaImage() {
        return WarehouseFloorAreaImage;
    }

    public void setWarehouseFloorAreaImage(String warehouseFloorAreaImage) {
        WarehouseFloorAreaImage = warehouseFloorAreaImage;
    }

    public String getWarehouseFloorAreaRemark() {
        return WarehouseFloorAreaRemark;
    }

    public void setWarehouseFloorAreaRemark(String warehouseFloorAreaRemark) {
        WarehouseFloorAreaRemark = warehouseFloorAreaRemark;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
