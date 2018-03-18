package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/1/30.
 * 合并补货单详情
 */
@Entity
@Table(name = "LOGISTICS_Merge_BillDtl")
public class MergeReplenishBillDtl {
    @Id
    @Column()
    private String id;
    @Column()
    private String styleid;
    @Column()
    private String colorid;
    @Column()
    private String allqyt;
    @Column()
    private String recordunits;
    @Column()
    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getRecordunits() {
        return recordunits;
    }

    public void setRecordunits(String recordunits) {
        this.recordunits = recordunits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public String getColorid() {
        return colorid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
    }

    public String getAllqyt() {
        return allqyt;
    }

    public void setAllqyt(String allqyt) {
        this.allqyt = allqyt;
    }
}
