package com.casesoft.dmc.model.rem;

import com.casesoft.dmc.model.logistics.BaseBillDtl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
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
    private String OrackId;//旧货架
    @Column
    private String OlevelId;//旧货层
    @Column
    private String OallocationId;//旧货位
    @Column
    private Long inStock =0L;//已入库数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getOrackId() {
        return OrackId;
    }

    public void setOrackId(String orackId) {
        OrackId = orackId;
    }

    public String getOlevelId() {
        return OlevelId;
    }

    public void setOlevelId(String olevelId) {
        OlevelId = olevelId;
    }

    public String getOallocationId() {
        return OallocationId;
    }

    public void setOallocationId(String oallocationId) {
        OallocationId = oallocationId;
    }

    public Long getInStock() {
        return inStock;
    }

    public void setInStock(Long inStock) {
        this.inStock = inStock;
    }
}
