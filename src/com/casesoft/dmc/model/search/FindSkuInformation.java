package com.casesoft.dmc.model.search;

/**
 * Created by Administrator on 2018/5/4.
 */
public class FindSkuInformation {

    private String sku;//sku
    private String fristtime;//第一次到货时间
    private String endtime;//最后到货时间
    private String billdate;//下单时间
    private String id;//单号
    private String qty;//数量
    private String inqty;//到货数量
    private String destname;//分配仓库
    private String destid;
    private String origunitname;//供应商
    private String class1name;//厂家名
    private String instocktype;//入库类型
    private String buyahandname;

    public String getBuyahandname() {
        return buyahandname;
    }

    public void setBuyahandname(String buyahandname) {
        this.buyahandname = buyahandname;
    }

    public String getOrigunitname() {
        return origunitname;
    }

    public void setOrigunitname(String origunitname) {
        this.origunitname = origunitname;
    }

    public String getClass1name() {
        return class1name;
    }

    public void setClass1name(String class1name) {
        this.class1name = class1name;
    }

    public String getInstocktype() {
        return instocktype;
    }

    public void setInstocktype(String instocktype) {
        this.instocktype = instocktype;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getFristtime() {
        return fristtime;
    }

    public void setFristtime(String fristtime) {
        this.fristtime = fristtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getInqty() {
        return inqty;
    }

    public void setInqty(String inqty) {
        this.inqty = inqty;
    }

    public String getDestname() {
        return destname;
    }

    public void setDestname(String destname) {
        this.destname = destname;
    }

    public String getDestid() {
        return destid;
    }

    public void setDestid(String destid) {
        this.destid = destid;
    }
}
