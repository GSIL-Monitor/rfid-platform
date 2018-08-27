package com.casesoft.dmc.model.sys;



import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/11.
 */
public class BusinessAccount {
    @JSONField(format = "yyyy-MM-dd")
    @Excel(name = "日期", width = 25D)
    private Date billDate;
    @Excel(name = "名店名称")
    private String destunitname;
    private String destunitid;
    @Excel(name = "发货金额")
    private BigDecimal saleprice;
    @Excel(name = "退货金额")
    private BigDecimal salereturnprice;
    @Excel(name = "发货数量")
    private BigDecimal saleqty;
    @Excel(name = "退货数量")
    private BigDecimal salereturnqty;
    @Excel(name = "充值")
    private BigDecimal payprice;
    @Excel(name = "欠款金额")
    private BigDecimal owingValue;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getDestunitname() {
        return destunitname;
    }

    public void setDestunitname(String destunitname) {
        this.destunitname = destunitname;
    }

    public String getDestunitid() {
        return destunitid;
    }

    public void setDestunitid(String destunitid) {
        this.destunitid = destunitid;
    }

    public BigDecimal getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(BigDecimal saleprice) {
        this.saleprice = saleprice;
    }

    public BigDecimal getSalereturnprice() {
        return salereturnprice;
    }

    public void setSalereturnprice(BigDecimal salereturnprice) {
        this.salereturnprice = salereturnprice;
    }

    public BigDecimal getSaleqty() {
        return saleqty;
    }

    public void setSaleqty(BigDecimal saleqty) {
        this.saleqty = saleqty;
    }

    public BigDecimal getSalereturnqty() {
        return salereturnqty;
    }

    public void setSalereturnqty(BigDecimal salereturnqty) {
        this.salereturnqty = salereturnqty;
    }

    public BigDecimal getPayprice() {
        return payprice;
    }

    public void setPayprice(BigDecimal payprice) {
        this.payprice = payprice;
    }

    public BigDecimal getOwingValue() {
        return owingValue;
    }

    public void setOwingValue(BigDecimal owingValue) {
        this.owingValue = owingValue;
    }
}
