package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2017/7/8.
 */
@Entity
@Table(name="MONTH_ACCOUNT_STATEMENT")
public class MonthAccountStatement {
    @Id
    @Column()
    private String id;       //UUID
    @Column()
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;
    @Column()
    private String month;   //月份
    @Column()
    private String billType; //区分付款，收款
    @Column()
    private String unitId;   //(客户或供应商)
    @Column()
    private String unitType; //区分客户类型
    @Column()
    private String ownerId;  //单据所属方
    @Column()
    private double totVal;   //总欠款or总收款金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Double getTotVal() {
        return totVal;
    }

    public void setTotVal(Double totVal) {
        this.totVal = totVal;
    }
}
