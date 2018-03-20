package com.casesoft.dmc.model.logistics;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Alvin on 2018/1/27.
 * 补货单
 */
@Entity
@Table(name = "LOGISTICS_ReplenishBill")
public class ReplenishBill extends BaseBill{
    @Id
    @Column()
    private String id;

    @Column()
    private Long totConvertQty=0L;//

    @Column()
    private String buyahandId;//买手的id

    public String getBuyahandId() {
        return buyahandId;
    }

    public void setBuyahandId(String buyahandId) {
        this.buyahandId = buyahandId;
    }
    @Transient
    private String buyahandName;

    public String getBuyahandName() {
        return buyahandName;
    }

    public void setBuyahandName(String buyahandName) {
        this.buyahandName = buyahandName;
    }

    @Transient
    private List<ReplenishBillDtl> dtlList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotConvertQty() {
        return totConvertQty;
    }

    public void setTotConvertQty(Long totConvertQty) {
        this.totConvertQty = totConvertQty;
    }

    public List<ReplenishBillDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<ReplenishBillDtl> dtlList) {
        this.dtlList = dtlList;
    }
}
