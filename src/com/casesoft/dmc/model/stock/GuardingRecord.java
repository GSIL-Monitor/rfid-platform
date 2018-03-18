package com.casesoft.dmc.model.stock;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yushen on 2017/9/12.
 */


@Entity
@Table(name = "Stock_GuardingRecord")
public class GuardingRecord  implements Serializable {
    @Id
    @Column()
    private String id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordDate;

    @Column()
    private String deviceId;

    @Column()
    private String deviceOwnerId;

    @Column()
    private String isAlert;

    @Column(nullable = false, length = 20)
    private String styleId;
    @Column(nullable = false, length = 20)
    private String colorId;
    @Column(nullable = false, length = 20)
    private String sizeId;

    @Transient
    private String styleName;
    @Transient
    private String colorName;
    @Transient
    private String sizeName;

    @Transient
    private String shopId;
    @Transient
    private  String shopName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getDeviceOwnerId() {
        return deviceOwnerId;
    }

    public void setDeviceOwnerId(String deviceOwnerId) {
        this.deviceOwnerId = deviceOwnerId;
    }

    public String getIsAlert() {
        return isAlert;
    }

    public void setIsAlert(String isAlert) {
        this.isAlert = isAlert;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

