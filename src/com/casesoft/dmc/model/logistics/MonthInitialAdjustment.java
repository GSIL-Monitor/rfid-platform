package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yushen on 2017/10/20.
 */
@Entity
@Table(name = "MONTH_INITIAL_ADJUSTMENT")
public class MonthInitialAdjustment {
    @Id
    @Column()
    private String id;
    @Column()
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;
    @Column()
    private String month;    //月份
    @Column()
    private String unitId;   //(客户或供应商)
    @Column()
    private String oprId;    //单据所属方
    @Column()
    private double preVal;   //调整前金额
    @Column()
    private double afterVal;   //调整后金额
    @Column()
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public double getPreVal() {
        return preVal;
    }

    public void setPreVal(double preVal) {
        this.preVal = preVal;
    }

    public double getAfterVal() {
        return afterVal;
    }

    public void setAfterVal(double afterVal) {
        this.afterVal = afterVal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
