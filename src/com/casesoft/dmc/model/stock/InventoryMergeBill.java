package com.casesoft.dmc.model.stock;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/12/4.
 */
@Entity
@Table(name = "STOCK_InventoryMergeBill")
public class InventoryMergeBill {
    @Id
    @Column
    private String id;
    @Column
    private String billNo;
    @Column
    private String warehouseId;
    @Transient
    private String warehouseName;
    @Column
    private String ownerId;
    @Column
    private Integer mergeBillQty;   //合并盘点单据数量
    @Column
    private Long totMergeQty;    //合并后总库存数量
    @Column
    private Long totScannedQty;  //合并后在库数量(被扫描到的数量)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date mergeDate;         //合并日期
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date inventoryDate;     //盘点日期
    @Transient
    private List<InventoryMergeOrigBill> inventoryBillList; //合并盘点单据的单号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMergeBillQty() {
        return mergeBillQty;
    }

    public void setMergeBillQty(Integer mergeBillQty) {
        this.mergeBillQty = mergeBillQty;
    }

    public Long getTotMergeQty() {
        return totMergeQty;
    }

    public void setTotMergeQty(Long totMergeQty) {
        this.totMergeQty = totMergeQty;
    }

    public Long getTotScannedQty() {
        return totScannedQty;
    }

    public void setTotScannedQty(Long totScannedQty) {
        this.totScannedQty = totScannedQty;
    }

    public Date getMergeDate() {
        return mergeDate;
    }

    public void setMergeDate(Date mergeDate) {
        this.mergeDate = mergeDate;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public List<InventoryMergeOrigBill> getInventoryBillList() {
        return inventoryBillList;
    }

    public void setInventoryBillList(List<InventoryMergeOrigBill> inventoryBillList) {
        this.inventoryBillList = inventoryBillList;
    }
}
