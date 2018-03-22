package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column()
    private Date billDate;
    @Column()
    private String sku;
    @Column()
    private String qty;
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
}
