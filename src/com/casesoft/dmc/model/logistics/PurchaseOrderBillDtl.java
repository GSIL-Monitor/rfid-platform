package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alvin on 2017-06-13.
 */
@Entity
@Table(name = "LOGISTICS_PurchaseOrder_DTL")
public class PurchaseOrderBillDtl extends BaseBillDtl {

    @Id
    @Column()
    private String id;

    @Column()
    private Integer inQty =0;

    @Column()
    private int outStatus=0;
    @Column()
    private int inStatus =0;
    @Column()
    private Double outVal=0D;
    @Column()
    private Double inVal=0D;
    @Column(length=1)
    private Integer printStatus;

    @Column(length =5)
    private String inStockType;//关联商品入库类型字段

    @Column()
    private Integer actPrintQty;//已打印数量

    @Column()
    private Integer printQty;//待打印数量

    @Column()
    @JSONField(format = "yyyy-MM-dd")
    private Date expectTime;//预计时间

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInStockType() {
        return inStockType;
    }

    public void setInStockType(String inStockType) {
        this.inStockType = inStockType;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public Double getInVal() {
        return inVal;
    }

    public void setInVal(Double inVal) {
        this.inVal = inVal;
    }

    public Integer getActPrintQty() {
        return actPrintQty;
    }

    public void setActPrintQty(Integer actPrintQty) {
        this.actPrintQty = actPrintQty;
    }

    public Integer getPrintQty() {
        return printQty;
    }

    public void setPrintQty(Integer printQty) {
        this.printQty = printQty;
    }

    public int getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(int outStatus) {
        this.outStatus = outStatus;
    }

    public int getInStatus() {
        return inStatus;
    }

    public void setInStatus(int inStatus) {
        this.inStatus = inStatus;
    }

    public Double getOutVal() {
        return outVal;
    }

    public void setOutVal(Double outVal) {
        this.outVal = outVal;
    }
}
