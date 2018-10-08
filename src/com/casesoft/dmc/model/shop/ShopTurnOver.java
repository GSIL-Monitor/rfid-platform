package com.casesoft.dmc.model.shop;


import java.math.BigDecimal;

/**
 * Created by lly on 2018/9/29.
 */
public class ShopTurnOver {

    private String shop;//店铺id

    private String shopName;//店铺名称

    private String payType;//支付方式

    private String payDate;//支付事件

    private BigDecimal recivePrice;//收款金额

    private BigDecimal savePrice;//储值金额

    private BigDecimal returnPrice;//付款金额

    private BigDecimal totPrice;//总金额

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

    public BigDecimal getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(BigDecimal totPrice) {
        this.totPrice = totPrice;
    }

    public BigDecimal getRecivePrice() {
        return recivePrice;
    }

    public void setRecivePrice(BigDecimal recivePrice) {
        this.recivePrice = recivePrice;
    }

    public BigDecimal getSavePrice() {
        return savePrice;
    }

    public void setSavePrice(BigDecimal savePrice) {
        this.savePrice = savePrice;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
