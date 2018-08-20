package com.casesoft.dmc.model.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by lly on 2018/8/15.
 */
@Entity
@Table(name = "SHOP_PAYDETAIL")
public class payDetail implements Serializable{

    private static final long serialVersionUID = -3401786740340682556L;

    @Id
    @Column
    private String id;//销售单号+支付方式

    @Column
    private String payDate;//支付时间

    @Column
    private String customerId;//客户id

    @Column
    private String shop;//店铺

    @Column
    private String billNo;//销售单号

    @Column
    private String returnBillNo;//退货单号

    @Column
    private String payType;//支付方式

    @Column
    private String payPrice;//应付金额

    @Column
    private String actPayPrice;//实付金额

    @Column
    private String status;//脏数据处理字段


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getReturnBillNo() {
        return returnBillNo;
    }

    public void setReturnBillNo(String returnBillNo) {
        this.returnBillNo = returnBillNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getActPayPrice() {
        return actPayPrice;
    }

    public void setActPayPrice(String actPayPrice) {
        this.actPayPrice = actPayPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
