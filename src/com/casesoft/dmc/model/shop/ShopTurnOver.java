package com.casesoft.dmc.model.shop;


import java.math.BigDecimal;

/**
 * Created by lly on 2018/9/29.
 */
public class ShopTurnOver {

    private String shop;//店铺id

    private String payType;//支付方式

    private String payDate;//支付事件

    private Double recivePrice;//收款金额

    private Double savePrice;//储值金额

    private Double returnPrice;//付款金额

    private Double totPrice;//总金额

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public Double getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(Double totPrice) {
        this.totPrice = totPrice;
    }

    public Double getRecivePrice() {
        return recivePrice;
    }

    public void setRecivePrice(Double recivePrice) {
        this.recivePrice = recivePrice;
    }

    public Double getSavePrice() {
        return savePrice;
    }

    public void setSavePrice(Double savePrice) {
        this.savePrice = savePrice;
    }

    public Double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(Double returnPrice) {
        this.returnPrice = returnPrice;
    }

}
