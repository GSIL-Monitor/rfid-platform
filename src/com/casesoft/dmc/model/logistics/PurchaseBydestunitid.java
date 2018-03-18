package com.casesoft.dmc.model.logistics;

/**
 * Created by Administrator on 2017/10/20.
 */
public class PurchaseBydestunitid {
    private String destunitname;
    private String qty;
    private String totactprice;

    public String getDestunitname() {
        return destunitname;
    }

    public void setDestunitname(String destunitname) {
        this.destunitname = destunitname;
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
