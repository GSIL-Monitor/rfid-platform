package com.casesoft.dmc.model.logistics;

import com.casesoft.dmc.model.logistics.vo.ReplenishStyleVO;

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
    private Long totConvertQty=0L;//总转换数量
    @Column()
    private Long totCancelQty = 0L;//总撤销数量

    @Column()
    private String buyahandId;//买手的id
    @Column()
    private String replenishType;//补货类型 1.是购货 2.是退货

    @Column()
    private Long totInQty=0L;

    public String getReplenishType() {
        return replenishType;
    }

    public void setReplenishType(String replenishType) {
        this.replenishType = replenishType;
    }

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
    private String warehouseName;

    public String getWarehouseName() {return warehouseName;}

    public void setWarehouseName(String warehouseName) {this.warehouseName = warehouseName;}

    @Transient
    private List<ReplenishBillDtl> dtlList;

    @Transient
    private List<ReplenishStyleVO> styleVOList;

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

    public Long getTotCancelQty() {
        return totCancelQty;
    }

    public void setTotCancelQty(Long totCancelQty) {
        this.totCancelQty = totCancelQty;
    }

    public List<ReplenishBillDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<ReplenishBillDtl> dtlList) {
        this.dtlList = dtlList;
    }


    public List<ReplenishStyleVO> getStyleVOList() { return styleVOList; }

    public void setStyleVOList(List<ReplenishStyleVO> styleVOList) { this.styleVOList = styleVOList; }

    public Long getTotInQty() {
        return totInQty;
    }

    public void setTotInQty(Long totInQty) {
        this.totInQty = totInQty;
    }
}
