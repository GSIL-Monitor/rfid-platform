package com.casesoft.dmc.model.stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yushen on 2017/12/5.
 */
@Entity
@Table(name = "STOCK_InventoryMergeOrigBill")
public class InventoryMergeOrigBill {
    @Id
    @Column
    private String id; // MergeBillNo-InventoryBillNo
    @Column
    private String mergeBillNo;
    @Column
    private String inventoryBillNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMergeBillNo() {
        return mergeBillNo;
    }

    public void setMergeBillNo(String mergeBillNo) {
        this.mergeBillNo = mergeBillNo;
    }

    public String getInventoryBillNo() {
        return inventoryBillNo;
    }

    public void setInventoryBillNo(String inventoryBillNo) {
        this.inventoryBillNo = inventoryBillNo;
    }
}
