package com.casesoft.dmc.model.logistics;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/5/9.
 */
@Entity
@Table(name = "LOGISTICS_LabelChangeBill_DTL")
public class LabelChangeBillDel extends BaseBillDtl{
    @Id
    @Column()
    private String id;
    @Transient
    private String styleNew;

    public String getStyleNew() {
        return styleNew;
    }

    public void setStyleNew(String styleNew) {
        this.styleNew = styleNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOutQty() {
        return outQty;
    }

    public void setOutQty(Integer outQty) {
        this.outQty = outQty;
    }

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    //已出库数量
    @Column()

    private Integer outQty=0;

    //已入库数量
    @Column()
    private Integer inQty =0;

    @Column()
    private Integer outStatus=0;
    @Column()
    private Integer inStatus =0;
    @Column()
    private Double preCast;//采购价

    public Double getPreCast() {
        return preCast;
    }

    public void setPreCast(Double preCast) {
        this.preCast = preCast;
    }
}
