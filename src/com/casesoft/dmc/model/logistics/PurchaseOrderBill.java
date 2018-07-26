package com.casesoft.dmc.model.logistics;

import com.casesoft.dmc.model.logistics.vo.PurchaseStyleVo;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Alvin  2017-06-13.
 */
@Entity
@Table(name = "LOGISTICS_PurchaseOrderBill")
public class PurchaseOrderBill extends BaseBill {
    @Id
    @Column()
    private String id;
    @Column()
    private Long totOutQty=0L;//总出库数量
    @Column()
    private Long totInQty=0L;
    @Column()
    private int outStatus=0;//出库状态
    @Column()
    private int inStatus =0;
    @Column()
    private Double totOutVal=0D;//出库金额
    @Column()
    private Double totInVal=0D;//
    @Column()
    private String buyahandId;//买手的id
    @Transient
    private String buyahandName;//买手名
    @Column()
    private String orderWarehouseId; //订货仓库Id
    @Column
    private String orderWarehouseName; //订货仓库名
    @Column
    private String cageId;//入库仓库id
    @Column
    private String rackId;//入库货架id
    @Column
    private String levelId;//入库货层id
    @Column
    private String allocationId;//入库货位id

    public String getCageId() {
        return cageId;
    }

    public void setCageId(String cageId) {
        this.cageId = cageId;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getBuyahandId() {
        return buyahandId;
    }

    public void setBuyahandId(String buyahandId) {
        this.buyahandId = buyahandId;
    }

    public String getBuyahandName() {
        return buyahandName;
    }

    public void setBuyahandName(String buyahandName) {
        this.buyahandName = buyahandName;
    }

    public String getOrderWarehouseId() {
        return orderWarehouseId;
    }

    public void setOrderWarehouseId(String orderWarehouseId) {
        this.orderWarehouseId = orderWarehouseId;
    }

    public String getOrderWarehouseName() {
        return orderWarehouseName;
    }

    public void setOrderWarehouseName(String orderWarehouseName) {
        this.orderWarehouseName = orderWarehouseName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public Long getTotInQty() {
        return totInQty;
    }

    public void setTotInQty(Long totInQty) {
        this.totInQty = totInQty;
    }

    public Double getTotInVal() {
        return totInVal;
    }

    public void setTotInVal(Double totInVal) {
        this.totInVal = totInVal;
    }

    public Long getTotOutQty() {
        return totOutQty;
    }

    public void setTotOutQty(Long totOutQty) {
        this.totOutQty = totOutQty;
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

    public Double getTotOutVal() {
        return totOutVal;
    }

    public void setTotOutVal(Double totOutVal) {
        this.totOutVal = totOutVal;
    }

    @Transient
    private List<PurchaseOrderBillDtl> dtlList;

    public List<PurchaseOrderBillDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<PurchaseOrderBillDtl> dtlList) { this.dtlList = dtlList; }


    /**
     * add by Anna
     */
    @Transient
    private List<PurchaseStyleVo> styleList;

    public List<PurchaseStyleVo> getStyleList() { return styleList; }

    public void setStyleList(List<PurchaseStyleVo> styleList) { this.styleList = styleList; }


}
