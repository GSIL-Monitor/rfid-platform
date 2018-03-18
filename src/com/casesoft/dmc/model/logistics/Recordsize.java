package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/1/30.
 */
@Entity
@Table(name = "LOGISTICS_Merge_Recordsize")
public class Recordsize {
    @Id
    @Column()
    private String id;
    @Column()
    private String sizeid;
    @Column()
    private String qty;
    @Column()
    private String recordid;
    @Column()
    private Long stockQty;//库存数量
    @Column()
    private String billNo;
    @Column()
    private Integer alreadyChange=0;//记录以转换的数量
    @Column()
    private Integer nowChange=0;//记录本次转换的数量
    @Column()
    private String isChange;//记录是否全部转换
    @Column()
    private String origUnitId;//记录供应商的id
    @Column()
    private String origUnitName;//记录供应上的名字

    public String getOrigUnitName() {
        return origUnitName;
    }

    public void setOrigUnitName(String origUnitName) {
        this.origUnitName = origUnitName;
    }

    public String getOrigUnitId() {
        return origUnitId;
    }

    public void setOrigUnitId(String origUnitId) {
        this.origUnitId = origUnitId;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public Integer getAlreadyChange() {
        return alreadyChange;
    }

    public void setAlreadyChange(Integer alreadyChange) {
        this.alreadyChange = alreadyChange;
    }

    public Integer getNowChange() {
        return nowChange;
    }

    public void setNowChange(Integer nowChange) {
        this.nowChange = nowChange;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Long getFranchiseeStockQty() {
        return franchiseeStockQty;
    }

    public void setFranchiseeStockQty(Long franchiseeStockQty) {
        this.franchiseeStockQty = franchiseeStockQty;
    }

    @Column()

    private Long franchiseeStockQty;//加盟店库存数量


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }
}
