package com.casesoft.dmc.model.pad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ppayment entity.
 * @author liutinaci 2018/5/30
 */
@Entity
@Table(name = "MOBILE_PAYMENT")
public class MobilePayment {
    @Id
    @Column(nullable = false,length = 50)
    //流水id
    private String id;
    @Column(length = 50)
    //用户标识
    private String openid;
    @Column(length = 50)
    //商户订单号
    private String tradeNo;
    @Column(length = 50)
    //公众账号ID
    private String appid;
    @Column(length = 12)
    //订单金额
    private String fee;
    @Column(length = 14)
    //订单时间
    private String time;
    //支付类型
    @Column(length = 50)
    private String paymentType;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
