package com.casesoft.dmc.model.logistics;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/20.
 */
public class PurchaseBystyleid implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sku;
    private String stylename;
    private String qty;
    private String totactprice;
    private String destunitname;
    /*新增*/
    private String styleId;
    private Date billDate;
    private Long totQty;

    public PurchaseBystyleid() {
    }

    public PurchaseBystyleid(String styleId, String sku, Long totQty) {
        this.styleId = styleId;
        this.sku = sku;
        this.totQty = totQty;
    }

    public PurchaseBystyleid(String styleId,Long totQty) {
        this.styleId = styleId;
        this.totQty = totQty;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }


    public Long getTotQty() {
        return totQty;
    }

    public void setTotQty(Long totQty) {
        this.totQty = totQty;
    }


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
