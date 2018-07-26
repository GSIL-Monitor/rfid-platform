package com.casesoft.dmc.model.rem;

import com.casesoft.dmc.model.logistics.BaseBillDtl;

import java.io.Serializable;
import javax.persistence.*;
/**
 * Created by lly on 2018/7/20.
 */
@Entity
@Table(name = "REM_RepositoryAdjustBill_DTL")
public class RepositoryManagementBillDtl extends BaseBillDtl implements Serializable{
    private static final long serialVersionUID = 8405385879305454147L;

    @Id
    @Column
    private String id;
    @Column
    private String warehouseId;//所属仓库
    @Column
    private String oldRmId;//旧库位id
    @Column
    private Long inStock =0L;//已入库数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldRmId() {
        return oldRmId;
    }

    public void setOldRmId(String oldRmId) {
        this.oldRmId = oldRmId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }


    public Long getInStock() {
        return inStock;
    }

    public void setInStock(Long inStock) {
        this.inStock = inStock;
    }
}
