package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/20.
 */
@Entity
@Table(name = "LOGISTICS_ChangeReplenish")
public class ChangeReplenishBillDtl {
    @Id
    @Column()
    private String id;
    @Column()
    private String ReplenishNo;
    @Column()
    private String purchaseNo;
    @JSONField(format="yyyy-MM-dd")
    @Column()
    private Date billDate;
    @Column()
    private String sku;
    @Column()
    private String qty;
    @Column()
    private String userId;
    @Column()
    private String ownerId;

    @Transient
    private String styleId;

    @Transient
    private String styleName;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JSONField(format="yyyy-MM-dd")
    @Column()
    private Date expectTime;//预计时间

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplenishNo() {
        return ReplenishNo;
    }

    public void setReplenishNo(String replenishNo) {
        ReplenishNo = replenishNo;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
