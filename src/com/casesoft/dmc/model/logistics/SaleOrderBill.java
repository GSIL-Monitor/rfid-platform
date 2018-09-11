package com.casesoft.dmc.model.logistics;

import javax.persistence.*;
import java.util.List;

/**
 * Created by koudepei on 2017/6/18.
 */
@Entity
@Table(name = "LOGISTICS_SaleOrderBill")
public class SaleOrderBill extends BaseBill {
    @Id
    @Column()
    private String id;

    //客户类别
    @Column()
    private String customerTypeId;
    @Column()
    private Long totOutQty=0L;//
    @Column()
    private Long totInQty=0L;
    @Column()
    private Integer outStatus=0;
    @Column()
    private Integer inStatus =0;
    @Column()
    private Double totOutVal=0D;//
    @Column()
    private Double totInVal=0D;//

    @Column()
    private Double totStockVal = 0D;//采购成本
    @Column()
    private Double profit = 0D;//OutVal - totStockVal 利润
    private Double profitRate = 0D;//totStockVal/outVal*100% 保留两位小数 毛利率
    @Column()
    private Integer totRetrunQty =0;//

    @Column()
    private Double preBalance = 0D;

    @Column()
    private Double afterBalance = 0D;

    @Transient
    private List<SaleOrderBillDtl> dtlList;

    public List<SaleOrderBillDtl> getDtlList() { return dtlList; }

    public void setDtlList(List<SaleOrderBillDtl> dtlList) { this.dtlList = dtlList; }

    public Integer getTotRetrunQty() {
        return totRetrunQty;
    }

    public void setTotRetrunQty(Integer totRetrunQty) {
        this.totRetrunQty = totRetrunQty;
    }



    public Long getTotInQty() {
        return totInQty;
    }

    public void setTotInQty(Long totInQty) {
        this.totInQty = totInQty;
    }

    public String getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(String customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotOutQty() {
        return totOutQty;
    }

    public void setTotOutQty(Long totOutQty) { this.totOutQty = totOutQty; }

    public Double getTotOutVal() {
        return totOutVal;
    }

    public void setTotOutVal(Double totOutVal) {
        this.totOutVal = totOutVal;
    }

    public Double getTotInVal() { return totInVal; }

    public void setTotInVal(Double totInVal) { this.totInVal = totInVal; }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    public Double getTotStockVal() {
        return totStockVal;
    }

    public void setTotStockVal(Double totStockVal) {
        this.totStockVal = totStockVal;
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

    public Double getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(Double preBalance) {
        this.preBalance = preBalance;
    }

    public Double getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Double afterBalance) {
        this.afterBalance = afterBalance;
    }


    @Transient
    private List<SaleOrderBillDtl> dtlList;
    @Transient
    private String errorMessage;//redis记录单据保存的错误信息

    public List<SaleOrderBillDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<SaleOrderBillDtl> dtlList) {
        this.dtlList = dtlList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
