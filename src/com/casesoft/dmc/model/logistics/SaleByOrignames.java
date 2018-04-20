package com.casesoft.dmc.model.logistics;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by Administrator on 2018/4/20.
 */
public class SaleByOrignames {
    @Excel(name = "部门")
    private String origname;
    @Excel(name = "成本价")
    private String precast;
    @Excel(name = "毛利")
    private String gross;
    @Excel(name = "销售额")
    private String  totactprice;
    @Excel(name = "销售单品数")
    private String salesum;
    @Excel(name = "销售退货单品数")
    private String salereturnsum;
    @Excel(name = "销售金额")
    private String salemoney;
    @Excel(name = "销售退货金额")
    private String salereturnmoney;
    @Excel(name = "销售毛利率(%)")
    private String grossprofits;

    public String getGrossprofits() {
        return grossprofits;
    }

    public void setGrossprofits(String grossprofits) {
        this.grossprofits = grossprofits;
    }

    public String getOrigname() {
        return origname;
    }

    public void setOrigname(String origname) {
        this.origname = origname;
    }

    public String getPrecast() {
        return precast;
    }

    public void setPrecast(String precast) {
        this.precast = precast;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getTotactprice() {
        return totactprice;
    }

    public void setTotactprice(String totactprice) {
        this.totactprice = totactprice;
    }

    public String getSalesum() {
        return salesum;
    }

    public void setSalesum(String salesum) {
        this.salesum = salesum;
    }

    public String getSalereturnsum() {
        return salereturnsum;
    }

    public void setSalereturnsum(String salereturnsum) {
        this.salereturnsum = salereturnsum;
    }

    public String getSalemoney() {
        return salemoney;
    }

    public void setSalemoney(String salemoney) {
        this.salemoney = salemoney;
    }

    public String getSalereturnmoney() {
        return salereturnmoney;
    }

    public void setSalereturnmoney(String salereturnmoney) {
        this.salereturnmoney = salereturnmoney;
    }
}
