package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 * 合并补货单
 */
@Entity
@Table(name = "LOGISTICS_Merge_ReplenishBill")
public class MergeReplenishBill{
    @Id
    @Column()
    private String id;

    @Column()
    private Long totConvertQty=0L;//

    public Long getTotQty() {
        return totQty;
    }

    public void setTotQty(Long totQty) {
        this.totQty = totQty;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column()
    private Long totQty;//单据数量
    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;
    @Column(length = 2)
    private Integer status ; //单据状态
    @Column(nullable = false, length = 50)
    private String billNo;
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
