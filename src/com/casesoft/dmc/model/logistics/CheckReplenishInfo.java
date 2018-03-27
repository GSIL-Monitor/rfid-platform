package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by chenzhifan on 2018/3/27.
 * 审核和反审核操作记录
 */
@Entity
@Table(name = "LOGISTICS_CheckReplenishInfo")
public class CheckReplenishInfo {
    @Id
    @Column()
    private String id;
    @Column()
    private Integer checkType;
    @Column()
    private String handlersId;
    @Column()
    private String ReplenishBillNo;
    @Column()
    private Date billDate;
    @Column()
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public String getHandlersId() {
        return handlersId;
    }

    public void setHandlersId(String handlersId) {
        this.handlersId = handlersId;
    }

    public String getReplenishBillNo() {
        return ReplenishBillNo;
    }

    public void setReplenishBillNo(String replenishBillNo) {
        ReplenishBillNo = replenishBillNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
