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
@Table(name = "PL_SHOP_WMS_VIEW")
public class PlShopWmsView {
    @Id
    @Column
    private String id;

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

    @Column(length = 32)
    private String rackId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date rackCreateDate;
    @Column(length = 60)
    private String rackBarcode;
    @Column(length = 10)
    private Integer rackSkuQty;
    @Column(length = 200)
    private String rackName;
    @Column(length = 40)
    private String rackUpdateCode;
    @Column(length = 500)
    private String rackRemark;
    @Column(length = 60)
    private String rackDeviceId;
    @Column(length = 300)
    private String rackImage;
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

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public Date getRackCreateDate() {
        return rackCreateDate;
    }

    public void setRackCreateDate(Date rackCreateDate) {
        this.rackCreateDate = rackCreateDate;
    }

    public String getRackBarcode() {
        return rackBarcode;
    }

    public void setRackBarcode(String rackBarcode) {
        this.rackBarcode = rackBarcode;
    }

    public Integer getRackSkuQty() {
        return rackSkuQty;
    }

    public void setRackSkuQty(Integer rackSkuQty) {
        this.rackSkuQty = rackSkuQty;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getRackUpdateCode() {
        return rackUpdateCode;
    }

    public void setRackUpdateCode(String rackUpdateCode) {
        this.rackUpdateCode = rackUpdateCode;
    }

    public String getRackRemark() {
        return rackRemark;
    }

    public void setRackRemark(String rackRemark) {
        this.rackRemark = rackRemark;
    }

    public String getRackDeviceId() {
        return rackDeviceId;
    }

    public void setRackDeviceId(String rackDeviceId) {
        this.rackDeviceId = rackDeviceId;
    }

    public String getRackImage() {
        return rackImage;
    }

    public void setRackImage(String rackImage) {
        this.rackImage = rackImage;
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
