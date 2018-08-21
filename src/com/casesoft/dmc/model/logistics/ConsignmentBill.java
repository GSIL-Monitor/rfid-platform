package com.casesoft.dmc.model.logistics;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/14.
 */
@Entity
@Table(name = "LOGISTICS_ConsignmentBill")
public class ConsignmentBill extends BaseBill{
    @Id
    @Column
    private String id;
    @Column
    private String customerType;
    @Column
    private String customer;
    @Transient
    private String customerName;
    @Column
    private Long totOutQty=0L;
    @Column
    private Long totInQty=0L;
    @Column
    private Integer outStatus = 0;
    @Column
    private Double totOutVal=0D;//出库总金额
    @Column
    private Integer inStatus = 0;
    @Column
    private Double totInVal=0D;//实际入库总金额

    @Column
    private Double totStockVal =0D;//库存成本
    @Column
    private String returnCode;
    @Column
    private String saleRetrunBillNom;//寄存退库退钱单号
    @Column
    private String saleRetrunBillNoq;//寄存退库退货单号

    public String getSaleRetrunBillNom() {
        return saleRetrunBillNom;
    }

    public void setSaleRetrunBillNom(String saleRetrunBillNom) {
        this.saleRetrunBillNom = saleRetrunBillNom;
    }

    public String getSaleRetrunBillNoq() {
        return saleRetrunBillNoq;
    }

    public void setSaleRetrunBillNoq(String saleRetrunBillNoq) {
        this.saleRetrunBillNoq = saleRetrunBillNoq;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Double getTotStockVal() {
        return totStockVal;
    }

    public void setTotStockVal(Double totStockVal) {
        this.totStockVal = totStockVal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }


    public Long getTotOutQty() {
        return totOutQty;
    }

    public void setTotOutQty(Long totOutQty) {
        this.totOutQty = totOutQty;
    }


    public Long getTotInQty() {
        return totInQty;
    }

    public void setTotInQty(Long totInQty) {
        this.totInQty = totInQty;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getTotOutVal() {
        return totOutVal;
    }

    public void setTotOutVal(Double totOutVal) {
        this.totOutVal = totOutVal;
    }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    public Double getTotInVal() {
        return totInVal;
    }

    public void setTotInVal(Double totInVal) {
        this.totInVal = totInVal;
    }

}
