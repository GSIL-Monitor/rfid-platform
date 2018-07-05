package com.casesoft.dmc.model.shop;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yushen on 2018/7/3.
 *
 * 客户欠款金额变动记录
 */
@Entity
@Table(name = "SHOP_GUESTVALUECHANGE")
public class GuestValueChange implements java.io.Serializable{
    @Id
    @Column()
    private String id; //orderId-unitId 拼接

    @Column()
    private String orderId;

    @Column()
    private String unitId;

    @Column()
    private String unitName;

    @Column()
    private Double actPrice;

    @Column()
    private Double payPrice;

    @Column()
    private Double diffPrice;

    @Column()
    private Double preBalance;

    @Column()
    private Double afterBalance;

    @Column
    private Integer status;

    @Column()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordDate;

    @Column()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Double getActPrice() {
        return actPrice;
    }

    public void setActPrice(Double actPrice) {
        this.actPrice = actPrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(Double diffPrice) {
        this.diffPrice = diffPrice;
    }

    public Double getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(Double preBalance) {
        this.preBalance = preBalance;
    }

    public Double getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Double afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}

