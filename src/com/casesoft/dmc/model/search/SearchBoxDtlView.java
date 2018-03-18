package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017-04-11.
 */

@Entity
@Table(name = "SEARCH_BOXDTLVIEW")
public class SearchBoxDtlView implements Serializable {
    @Id
    @Column(length = 50,nullable = false)
    private String id;
    @Column(length = 50,nullable = false)
    private String cartonId;
    @Column(length = 20,nullable = false)
    private String styleId;
    @Column(length = 50,nullable = false)
    private String colorId;
    @Column(length = 10,nullable = false)
    private String sizeId;
    @Column(length = 50,nullable = false)
    private String deviceId;
    @Column(nullable = false)
    private Integer qty;
    @Column
    private Integer preQty;

    @Column(length = 50,nullable = false)
    private String sku;
    @Column(length = 50)
    private String srcTaskId;
    @Column(length = 50)
    private String destId;
    @Column(length = 50)
    private String origId;
    @Column(length = 50)
    private String destUnitId;
    @Column(length = 50)
    private String origUnitId;
    @Column(length = 50,nullable = false)
    private String taskId;
    @Column(nullable = false)
    private Integer token;
    @Column(length = 50)
    private String billNo;

    @Column
    private String scanTime;
    @Column
    private String scanDate;

    @Transient
    private String styleName;
    @Transient
    private String destName;
    @Transient
    private String origName;




    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartonId() {
        return cartonId;
    }

    public void setCartonId(String cartonId) {
        this.cartonId = cartonId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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


    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public Integer getPreQty() {
        return preQty;
    }

    public void setPreQty(Integer preQty) {
        this.preQty = preQty;
    }

    public String getSrcTaskId() {
        return srcTaskId;
    }

    public void setSrcTaskId(String srcTaskId) {
        this.srcTaskId = srcTaskId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }

    public String getDestUnitId() {
        return destUnitId;
    }

    public void setDestUnitId(String destUnitId) {
        this.destUnitId = destUnitId;
    }

    public String getOrigUnitId() {
        return origUnitId;
    }

    public void setOrigUnitId(String origUnitId) {
        this.origUnitId = origUnitId;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }
}