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
    private Double payPrice;//应付金额

    @Column
    private Double actPayPrice;//实付金额

    @Column
    private String returnPrice;//找零

    @Column
    private String status;//脏数据处理字段
    @Column
    private String billType;//账单类型 收款0, 储值1, 付款2, 销售3, 退货4
    @Column
    private String donationPrice;//赠送金额


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

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getActPayPrice() {
        return actPayPrice;
    }

    public void setActPayPrice(Double actPayPrice) {
        this.actPayPrice = actPayPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(String returnPrice) {
        this.returnPrice = returnPrice;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getDonationPrice() {
        return donationPrice;
    }

    public void setDonationPrice(String donationPrice) {
        this.donationPrice = donationPrice;
    }
}
