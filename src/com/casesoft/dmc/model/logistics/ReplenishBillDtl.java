package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Alvin on 2018/1/27.
 * 补货单明细
 */
@Entity
@Table(name = "LOGISTICS_ReplenishBillDtl")
public class ReplenishBillDtl extends BaseBillDtl{
    @Id
    @Column()
    private String id;

    @Column()
    private Integer actConvertQty=0;//已转换数量

    @Column()
    private Integer convertQty=0;//本次转换数量

    @Column()
    private Long stockQty;//库存数量

    @Column()
    private Long franchiseeStockQty;//加盟店库存数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getActConvertQty() {
        return actConvertQty;
    }

    public void setActConvertQty(Integer actConvertQty) {
        this.actConvertQty = actConvertQty;
    }

    public Integer getConvertQty() {
        return convertQty;
    }

    public void setConvertQty(Integer convertQty) {
        this.convertQty = convertQty;
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
}
