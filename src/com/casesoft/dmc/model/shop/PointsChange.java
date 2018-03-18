package com.casesoft.dmc.model.shop;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2017/11/3.
 *
 * 积分变动记录
 */

@Entity
@Table(name = "SHOP_POINTSCHANGE")
public class PointsChange implements java.io.Serializable{
    @Id
    @Column()
    private String id;

    @Column()
    private String customerId;

    @Transient
    private String customerName;

    @Column()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordDate;

    @Column()
    private Long pointsChange;

    @Column()
    private Double transactionVal;  //交易金额

    @Column()
    private String ownerId;

    @Column()
    private String pointsRuleId;

    @Column()
    private Integer status = 0; //0 默认 ; -1 已撤销

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Long getPointsChange() {
        return pointsChange;
    }

    public void setPointsChange(Long pointsChange) {
        this.pointsChange = pointsChange;
    }

    public Double getTransactionVal() {
        return transactionVal;
    }

    public void setTransactionVal(Double transactionVal) {
        this.transactionVal = transactionVal;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPointsRuleId() {
        return pointsRuleId;
    }

    public void setPointsRuleId(String pointsRuleId) {
        this.pointsRuleId = pointsRuleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
