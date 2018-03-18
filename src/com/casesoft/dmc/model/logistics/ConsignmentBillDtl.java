package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/8/14.
 */
@Entity
@Table(name = "LOGISTICS_ConsignmentBill_Dtl")
public class ConsignmentBillDtl extends BaseBillDtl{
    @Id
    @Column()
    private String id;
    //已出库数量
    @Column()
    private Integer outQty=0;

    //已入库数量
    @Column()
    private Integer inQty =0;
    //已销售数量
    @Column()
    private Integer sale = 0;
    //准备销售数量（记录每次退款的销售数量）
    @Column()
    private Integer readysale=0;
    //记录之前退货件数
    @Column()
    private Integer beforeoutQty=0;
    @Column()
    //退款数量
    private Integer outMonyQty=0;

    public Integer getOutMonyQty() {
        return outMonyQty;
    }

    public void setOutMonyQty(Integer outMonyQty) {
        this.outMonyQty = outMonyQty;
    }

    public Integer getBeforeoutQty() {
        return beforeoutQty;
    }

    public void setBeforeoutQty(Integer beforeoutQty) {
        this.beforeoutQty = beforeoutQty;
    }

    public Integer getReadysale() {
        return readysale;
    }

    public void setReadysale(Integer readysale) {
        this.readysale = readysale;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }
    //入库类型
    @Column()
    private String inStockType;

    public String getInStockType() {
        return inStockType;
    }

    public void setInStockType(String inStockType) {
        this.inStockType = inStockType;
    }

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
    private Double profitRate = 0D;//stockVal/outVal*100% 保留两位小数

    @Column()
    private Integer returnNumber=0;//退货数量

    public Integer getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(Integer returnNumber) {
        this.returnNumber = returnNumber;
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
    @Column()
    private String savehaveuniqueCodes;
    @Column()
    private String savenohanveuniqueCodes;

    public String getSavehaveuniqueCodes() {
        return savehaveuniqueCodes;
    }

    public void setSavehaveuniqueCodes(String savehaveuniqueCodes) {
        this.savehaveuniqueCodes = savehaveuniqueCodes;
    }

    public String getSavenohanveuniqueCodes() {
        return savenohanveuniqueCodes;
    }

    public void setSavenohanveuniqueCodes(String savenohanveuniqueCodes) {
        this.savenohanveuniqueCodes = savenohanveuniqueCodes;
    }
}
