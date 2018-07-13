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
    private String orderId;

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
    private Long prePoints;

    @Column()
    private Long afterPoints;

    @Column()
    private String ownerId;

    @Column()
    private String pointsRuleId;

    @Column()
    private Integer status = 0; //0 默认 ; -1 已撤销

    @Column
    private Double ratio; //销售单对应积分规则的比例，退货单比例设置为 -1

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Long getPrePoints() {
        return prePoints;
    }

    public void setPrePoints(Long prePoints) {
        this.prePoints = prePoints;
    }

    public Long getAfterPoints() {
        return afterPoints;
    }

    public void setAfterPoints(Long afterPoints) {
        this.afterPoints = afterPoints;
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

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
