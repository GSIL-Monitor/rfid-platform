package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by koudepei on 2017/6/18.
 */
@Entity
@Table(name = "LOGISTICS_SaleOrderBill_DTL")
public class SaleOrderBillDtl extends BaseBillDtl {


    @Id
    @Column()
    private String id;
    //已出库数量
    @Column()
    private Integer outQty=0;

    //已入库数量
    @Column()
    private Integer inQty =0;

    @Column()
    private Integer outStatus=0;
    @Column()
    private Integer inStatus =0;
    @Column()
    private Double outVal=0D;
    @Column()
    private Double inVal=0D;

    @Column()
    private Double avgPrePrice=0D;//采购均价 stockVal/outQty

    @Column()
    private Double stockVal = 0D;//采购成本
    @Column()
    private Double profit = 0D;//OutVal - stockVal
    @Column()
    private Double profitRate = 0D;//stockVal/outVal*100% 保留两位小数

    @Transient
    private Double tagPrice;//吊牌价格

    public SaleOrderBillDtl() {
    }

    public SaleOrderBillDtl(String billId, Date billDate, String busnissName,String styleId,String colorId,String sizeId,String sku/*,String styleName,String colorName,String sizeName*/) {
        super.billId = billId;
        this.billDate = billDate;
        this.busnissName = busnissName;
        super.styleId=styleId;
        super.colorId=colorId;
        super.sizeId=sizeId;
        super.sku=sku;
     /*   super.styleName=styleName;
        super.colorName=colorName;
        super.sizeName=sizeName;*/
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBusnissName() {
        return busnissName;
    }

    public void setBusnissName(String busnissName) {
        this.busnissName = busnissName;
    }

    @Column()
    private Integer returnQty =0;//退货数量
    @JSONField(format = "yyyy-MM-dd")
    @Transient
    private Date billDate;
    @Transient
    protected String busnissName;//业务员

    public Integer getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(Integer returnQty) {
        this.returnQty = returnQty;
    }

    public String getReturnbillNo() {
        return returnbillNo;
    }

    public void setReturnbillNo(String returnbillNo) {
        this.returnbillNo = returnbillNo;
    }

    @Column()
    private String  returnbillNo;//退货单号

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public Double getInVal() { return inVal; }

    public void setInVal(Double inVal) { this.inVal = inVal; }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOutQty() {
        return outQty;
    }

    public void setOutQty(Integer outQty) { this.outQty = outQty; }

    public Double getOutVal() {
        return outVal;
    }

    public void setOutVal(Double outVal) {
        this.outVal = outVal;
    }


    public Double getAvgPrePrice() {
        return avgPrePrice;
    }

    public void setAvgPrePrice(Double avgPrePrice) {
        this.avgPrePrice = avgPrePrice;
    }

    public Double getStockVal() {
        return stockVal;
    }

    public void setStockVal(Double stockVal) {
        this.stockVal = stockVal;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(Double profitRate) {
        this.profitRate = profitRate;
    }

    public Double getTagPrice() {
        return tagPrice;
    }

    public void setTagPrice(Double tagPrice) {
        this.tagPrice = tagPrice;
    }
}
