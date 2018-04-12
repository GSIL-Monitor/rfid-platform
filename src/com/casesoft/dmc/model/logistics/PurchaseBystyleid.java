package com.casesoft.dmc.model.logistics;

/**
 * Created by Administrator on 2017/10/20.
 */
public class PurchaseBystyleid {
    private String sku;
    private String stylename;
    private String qty;
    private String totactprice;
    private String destunitname;

    public String getDestunitname() {
        return destunitname;
    }

    public void setDestunitname(String destunitname) {
        this.destunitname = destunitname;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotactprice() {
        return totactprice;
    }

    public void setTotactprice(String totactprice) {
        this.totactprice = totactprice;
    }
}
