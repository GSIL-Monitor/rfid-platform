package com.casesoft.dmc.model.wms;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017/3/9 0009.
 */
@Entity
@Table(name = "WMS_FLOOR_VIEW")
public class WmsFloorView {
    @Id
    @Column
    private String id;
    @Column(length = 1)
    private Boolean sales;
    @Column(length = 32)
    private String flId;
    @Column(length = 32)
    private String flBarcode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date flCreateDate;
    @Column(length = 10)
    private Integer flSkuQty;
    @Column(length = 200)
    private String flName;
    @Column(length = 40)
    private String flUpdateCode;
    @Column(length = 500)
    private String flRemark;
    @Column(length = 60)
    private String flDeviceID;
    @Column(length = 300)
    private String flImage;
    @Column(length = 32)
    private String faId;
    @Column(length = 60)
    private String faBarcode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date faCreateDate;
    @Column(length = 10)
    private Integer faSkuQty;
    @Column(length = 200)
    private String faName;
    @Column(length = 40)
    private String faUpdateCode;
    @Column(length = 500)
    private String faRemark;
    @Column(length = 60)
    private String faDeviceId;
    @Column(length = 300)
    private String faImage;

    @Column(length = 32)
    private String raId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date raCreateDate;
    @Column(length = 60)
    private String raBarcode;
    @Column(length = 10)
    private Integer raSkuQty;
    @Column(length = 200)
    private String raName;
    @Column(length = 40)
    private String raUpdateCode;
    @Column(length = 500)
    private String raRemark;
    @Column(length = 60)
    private String raDeviceId;
    @Column(length = 300)
    private String raImage;
    @Column(length = 60)
    private String faParentCode;
    @Column(length = 100)
    private String faParentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSales() {
        return sales;
    }

    public void setSales(Boolean sales) {
        this.sales = sales;
    }

    public String getFlId() {
        return flId;
    }

    public void setFlId(String flId) {
        this.flId = flId;
    }

    public String getFlBarcode() {
        return flBarcode;
    }

    public void setFlBarcode(String flBarcode) {
        this.flBarcode = flBarcode;
    }

    public Date getFlCreateDate() {
        return flCreateDate;
    }

    public void setFlCreateDate(Date flCreateDate) {
        this.flCreateDate = flCreateDate;
    }

    public Integer getFlSkuQty() {
        return flSkuQty;
    }

    public void setFlSkuQty(Integer flSkuQty) {
        this.flSkuQty = flSkuQty;
    }

    public String getFlName() {
        return flName;
    }

    public void setFlName(String flName) {
        this.flName = flName;
    }

    public String getFlUpdateCode() {
        return flUpdateCode;
    }

    public void setFlUpdateCode(String flUpdateCode) {
        this.flUpdateCode = flUpdateCode;
    }

    public String getFlRemark() {
        return flRemark;
    }

    public void setFlRemark(String flRemark) {
        this.flRemark = flRemark;
    }

    public String getFlDeviceID() {
        return flDeviceID;
    }

    public void setFlDeviceID(String flDeviceID) {
        this.flDeviceID = flDeviceID;
    }

    public String getFlImage() {
        return flImage;
    }

    public void setFlImage(String flImage) {
        this.flImage = flImage;
    }

    public String getFaId() {
        return faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getFaBarcode() {
        return faBarcode;
    }

    public void setFaBarcode(String faBarcode) {
        this.faBarcode = faBarcode;
    }

    public Date getFaCreateDate() {
        return faCreateDate;
    }

    public void setFaCreateDate(Date faCreateDate) {
        this.faCreateDate = faCreateDate;
    }

    public Integer getFaSkuQty() {
        return faSkuQty;
    }

    public void setFaSkuQty(Integer faSkuQty) {
        this.faSkuQty = faSkuQty;
    }

    public String getFaName() {
        return faName;
    }

    public void setFaName(String faName) {
        this.faName = faName;
    }

    public String getFaUpdateCode() {
        return faUpdateCode;
    }

    public void setFaUpdateCode(String faUpdateCode) {
        this.faUpdateCode = faUpdateCode;
    }

    public String getFaRemark() {
        return faRemark;
    }

    public void setFaRemark(String faRemark) {
        this.faRemark = faRemark;
    }

    public String getFaDeviceId() {
        return faDeviceId;
    }

    public void setFaDeviceId(String faDeviceId) {
        this.faDeviceId = faDeviceId;
    }

    public String getFaImage() {
        return faImage;
    }

    public void setFaImage(String faImage) {
        this.faImage = faImage;
    }

    public String getRaId() {
        return raId;
    }

    public void setRaId(String raId) {
        this.raId = raId;
    }

    public Date getRaCreateDate() {
        return raCreateDate;
    }

    public void setRaCreateDate(Date raCreateDate) {
        this.raCreateDate = raCreateDate;
    }

    public String getRaBarcode() {
        return raBarcode;
    }

    public void setRaBarcode(String raBarcode) {
        this.raBarcode = raBarcode;
    }

    public Integer getRaSkuQty() {
        return raSkuQty;
    }

    public void setRaSkuQty(Integer raSkuQty) {
        this.raSkuQty = raSkuQty;
    }

    public String getRaName() {
        return raName;
    }

    public void setRaName(String raName) {
        this.raName = raName;
    }

    public String getRaUpdateCode() {
        return raUpdateCode;
    }

    public void setRaUpdateCode(String raUpdateCode) {
        this.raUpdateCode = raUpdateCode;
    }

    public String getRaRemark() {
        return raRemark;
    }

    public void setRaRemark(String raRemark) {
        this.raRemark = raRemark;
    }

    public String getRaDeviceId() {
        return raDeviceId;
    }

    public void setRaDeviceId(String raDeviceId) {
        this.raDeviceId = raDeviceId;
    }

    public String getRaImage() {
        return raImage;
    }

    public void setRaImage(String raImage) {
        this.raImage = raImage;
    }

    public String getFaParentCode() {
        return faParentCode;
    }

    public void setFaParentCode(String faParentCode) {
        this.faParentCode = faParentCode;
    }

    public String getFaParentName() {
        return faParentName;
    }

    public void setFaParentName(String faParentName) {
        this.faParentName = faParentName;
    }
}

