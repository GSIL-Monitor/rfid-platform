package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/20.
 */
public class PurchaseBystyleid implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name = "SKU")
    private String sku;
    @Excel(name = "商品名称")
    private String stylename;
    @Excel(name = "数量")
    private String qty;
    @Excel(name = "金额")
    private String totactprice;
    @Excel(name = "厂家")
    private String destunitname;

    /*新增*/
    private String styleId;
    public String getStyleId() {
        return styleId;
    }
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    private Long totQty; //采购数量求和
    public Long getTotQty() {
        return totQty;
    }
    public void setTotQty(Long totQty) {
        this.totQty = totQty;
    }

    private String buyHandId;  //买手id
    public String getBuyHandId() { return buyHandId; }
    public void setBuyHandId(String buyHandId) { this.buyHandId = buyHandId; }

    @JSONField(format="yyyy-MM-dd")
    private Date billDate;
    public Date getBillDate() {
        return billDate;
    }
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }


    public PurchaseBystyleid() { }

    //采购数量详情显示 日期＋买手＋数量  （暂无买手）
    public PurchaseBystyleid(Date billDate, String buyHandId, Long totQty) {
        this.totQty = totQty;
        this.buyHandId = buyHandId;
        this.billDate = billDate;
    }

    //采购数量看总和
    public PurchaseBystyleid(String styleId, Long totQty) {
        this.styleId = styleId;
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
