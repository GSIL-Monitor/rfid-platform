package com.casesoft.dmc.model.logistics;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by Administrator on 2017/10/20.
 */
public class PurchaseBydestunitid {
    @Excel(name="供应商")
    private String destunitname;
    @Excel(name="数量")
    private String qty;
    @Excel(name="金额")
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
