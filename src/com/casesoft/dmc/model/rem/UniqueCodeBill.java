package com.casesoft.dmc.model.rem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by lly on 2018/7/20.
 */
@Entity
@Table(name = "REM_UniqueCodeAdjustBill")
public class UniqueCodeBill implements Serializable{
    private static final long serialVersionUID = 3422290396896902617L;

    @Id
    @Column
    private String id;
    @Column
    private String uniqueCode;//唯一码
    @Column
    private String billNo;//关联
    @Column
    private String userId;//操作人

    @Column
    private String warehouseId;//仓库id

    @Column
    private String oldRm;//原库位名字

    @Column
    private String newRm;//新库位名字

    @Column
    private String updateTime;//调库时间

    @Column
    private String sku;//款式

    @Column
    private String oldRmId;//原库位id

    @Column
    private String newRmId;//新库位id

    public String getOldRmId() {
        return oldRmId;
    }

    public void setOldRmId(String oldRmId) {
        this.oldRmId = oldRmId;
    }

    public String getNewRmId() {
        return newRmId;
    }

    public void setNewRmId(String newRmId) {
        this.newRmId = newRmId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getOldRm() {
        return oldRm;
    }

    public void setOldRm(String oldRm) {
        this.oldRm = oldRm;
    }

    public String getNewRm() {
        return newRm;
    }

    public void setNewRm(String newRm) {
        this.newRm = newRm;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
