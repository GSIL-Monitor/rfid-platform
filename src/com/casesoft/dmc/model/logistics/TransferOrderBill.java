package com.casesoft.dmc.model.logistics;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.List;

/**
 * Created by yushen on 2017/7/3.
 */
@Entity
@Table(name="LOGISTICS_TRANSFERORDERBILL")
public class TransferOrderBill extends BaseBill{
    @Id
    @Column()
    private String id;

    @Column()
    @Excel(name="已出库数量")
    private Long totOutQty=0L;//出库总数量
    @Column()
    private Double totOutVal=0D;//出库总金额
    @Column()
    @Excel(name="已入库数量")
    private Long totInQty =0L;//实际入库总数量
    @Column()
    private Double totInVal =0D;//实际入库总金额
    @Column()
    @Excel(name="出库状态",replace = { "订单状态_0", "已出库_1" , "出库中_2"})
    private int outStatus=0;
    @Column()
    @Excel(name="入库状态",replace = { "订单状态_0", "已入库_1" , "入库中_2"})
    private int inStatus =0;

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

    public Long getTotOutQty() { return totOutQty; }

    public void setTotOutQty(Long totOutQty) { this.totOutQty = totOutQty; }

    public Double getTotOutVal() { return totOutVal; }

    public void setTotOutVal(Double totOutVal) { this.totOutVal = totOutVal; }

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

}
