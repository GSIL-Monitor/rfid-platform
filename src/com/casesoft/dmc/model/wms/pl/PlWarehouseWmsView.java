package com.casesoft.dmc.model.wms.pl;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Entity
@Table(name = "PL_WAREHOUHSE_WMS_VIEW")
public class PlWarehouseWmsView {
    @Id
    @Column
    private String id;
    @Column(length = 32)
    private String floorId;
    @Column(length = 32)
    private String floorBarcode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date floorCreateDate;
    @Column(length = 10)
    private Integer floorSkuQty;
    @Column(length = 200)
    private String floorName;
    @Column(length = 40)
    private String floorUpdateCode;
    @Column(length = 500)
    private String floorRemark;
    @Column(length = 60)
    private String floorDeviceID;
    @Column(length = 300)
    private String floorImage;
    @Column(length = 32)
    private String floorAreaId;
    @Column(length = 60)
    private String floorAreaBarcode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date floorAreaCreateDate;
    @Column(length = 10)
    private Integer floorAreaSkuQty;
    @Column(length = 200)
    private String floorAreaName;
    @Column(length = 40)
    private String floorAreaUpdateCode;
    @Column(length = 500)
    private String floorAreaRemark;
    @Column(length = 60)
    private String floorAreaDeviceId;
    @Column(length = 300)
    private String floorAreaImage;
    @Column(length = 60)
    private String floorAreaParentCode;
    @Column(length = 100)
    private String floorAreaParentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorBarcode() {
        return floorBarcode;
    }

    public void setFloorBarcode(String floorBarcode) {
        this.floorBarcode = floorBarcode;
    }

    public Date getFloorCreateDate() {
        return floorCreateDate;
    }

    public void setFloorCreateDate(Date floorCreateDate) {
        this.floorCreateDate = floorCreateDate;
    }

    public Integer getFloorSkuQty() {
        return floorSkuQty;
    }

    public void setFloorSkuQty(Integer floorSkuQty) {
        this.floorSkuQty = floorSkuQty;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorUpdateCode() {
        return floorUpdateCode;
    }

    public void setFloorUpdateCode(String floorUpdateCode) {
        this.floorUpdateCode = floorUpdateCode;
    }

    public String getFloorRemark() {
        return floorRemark;
    }

    public void setFloorRemark(String floorRemark) {
        this.floorRemark = floorRemark;
    }

    public String getFloorDeviceID() {
        return floorDeviceID;
    }

    public void setFloorDeviceID(String floorDeviceID) {
        this.floorDeviceID = floorDeviceID;
    }

    public String getFloorImage() {
        return floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public String getFloorAreaId() {
        return floorAreaId;
    }

    public void setFloorAreaId(String floorAreaId) {
        this.floorAreaId = floorAreaId;
    }

    public String getFloorAreaBarcode() {
        return floorAreaBarcode;
    }

    public void setFloorAreaBarcode(String floorAreaBarcode) {
        this.floorAreaBarcode = floorAreaBarcode;
    }

    public Date getFloorAreaCreateDate() {
        return floorAreaCreateDate;
    }

    public void setFloorAreaCreateDate(Date floorAreaCreateDate) {
        this.floorAreaCreateDate = floorAreaCreateDate;
    }

    public Integer getFloorAreaSkuQty() {
        return floorAreaSkuQty;
    }

    public void setFloorAreaSkuQty(Integer floorAreaSkuQty) {
        this.floorAreaSkuQty = floorAreaSkuQty;
    }

    public String getFloorAreaName() {
        return floorAreaName;
    }

    public void setFloorAreaName(String floorAreaName) {
        this.floorAreaName = floorAreaName;
    }

    public String getFloorAreaUpdateCode() {
        return floorAreaUpdateCode;
    }

    public void setFloorAreaUpdateCode(String floorAreaUpdateCode) {
        this.floorAreaUpdateCode = floorAreaUpdateCode;
    }

    public String getFloorAreaRemark() {
        return floorAreaRemark;
    }

    public void setFloorAreaRemark(String floorAreaRemark) {
        this.floorAreaRemark = floorAreaRemark;
    }

    public String getFloorAreaDeviceId() {
        return floorAreaDeviceId;
    }

    public void setFloorAreaDeviceId(String floorAreaDeviceId) {
        this.floorAreaDeviceId = floorAreaDeviceId;
    }

    public String getFloorAreaImage() {
        return floorAreaImage;
    }

    public void setFloorAreaImage(String floorAreaImage) {
        this.floorAreaImage = floorAreaImage;
    }

    public String getFloorAreaParentCode() {
        return floorAreaParentCode;
    }

    public void setFloorAreaParentCode(String floorAreaParentCode) {
        this.floorAreaParentCode = floorAreaParentCode;
    }

    public String getFloorAreaParentName() {
        return floorAreaParentName;
    }

    public void setFloorAreaParentName(String floorAreaParentName) {
        this.floorAreaParentName = floorAreaParentName;
    }
}
