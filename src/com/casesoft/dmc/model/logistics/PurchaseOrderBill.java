package com.casesoft.dmc.model.logistics;

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
    private Long totOutQty=0L;//
    @Column()
    private Long totInQty=0L;
    @Column()
    private int outStatus=0;
    @Column()
    private int inStatus =0;
    @Column()
    private Double totOutVal=0D;//
    @Column()
    private Double totInVal=0D;//
    @Column()
    private String buyahandId;//买手的id
    @Column()
    public String getBuyahandId() {
        return buyahandId;
    }

    public void setBuyahandId(String buyahandId) {
        this.buyahandId = buyahandId;
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

    public void setDtlList(List<PurchaseOrderBillDtl> dtlList) {
        this.dtlList = dtlList;
    }

}
