package com.casesoft.dmc.model.shop;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2017/9/7.
 */
@Entity
@Table(name = "shop_dressRecord")
public class DressRecord implements java.io.Serializable {
    @Id
    @Column()
    private String id;

    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordStartTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordEndTime;

    @Column(nullable = false, length = 50)
    private String ownerId;

    @Column(nullable = false)
    private String businessId;
    @Column(nullable = false)
    private String businessName;

    @Column(length = 2) //0表示在库，1表示穿着中
    private Integer status = 0;

    @Column(nullable = false)
    private String dressCode;

    @Column(nullable = false, length = 20)
    private String styleId;
    @Column(nullable = false, length = 20)
    private String colorId;
    @Column(nullable = false, length = 20)
    private String sizeId;

    @Column(length = 500)
    private String remark;

    @Transient
    private String styleName;
    @Transient
    private String colorName;
    @Transient
    private String sizeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(Date recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public Date getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(Date recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
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

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
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

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
