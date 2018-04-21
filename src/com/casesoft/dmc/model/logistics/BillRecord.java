package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by admin on 2017/8/10.
 */

@Entity
@Table(name = "LOGISTICS_BillRecord")
public class BillRecord {
    @Id
    @Column()
    private String id; //billNo-code格式

    @Column()
    private String code;//code 唯一码

    @Column()
    private String billNo;//单号
    @Column()
    private String sku;



    /* 新增 by Anna */
    @Column()
    private String originBillNo; //原始单号
    @Column()
    private Date lastSaleTime; //最近一次销售时间
    @Column()
    private Integer saleCycle; //销售周期（开单当天时间－销售单时间）

    public String getOriginBillNo() { return originBillNo; }

    public void setOriginBillNo(String originBillNo) { this.originBillNo = originBillNo; }

    public Date getLastSaleTime() { return lastSaleTime; }

    public void setLastSaleTime(Date lastSaleTime) { this.lastSaleTime = lastSaleTime; }

    public Integer getSaleCycle() { return saleCycle; }

    public void setSaleCycle(Integer saleCycle) { this.saleCycle = saleCycle; }
    /* end */

    public BillRecord() {
    }

    public BillRecord(String id, String code, String billNo, String sku) {
        this.id = id;
        this.code = code;
        this.billNo = billNo;
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
