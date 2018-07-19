package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2017/11/2.
 *
 * 积分规则
 */
@Entity
@Table(name = "SYS_POINTSRULE")
public class
PointsRule implements java.io.Serializable {
    @Id
    @Column
    private String id; //店铺ID，起止时间拼接

    @Column
    private String unitId; //店铺ID

    @Transient
    private String unitName;

    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date startDate; //规则开始时间

    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;  //规则结束时间

    @Column
    private Integer unitPoints; //每消费100元人民币克对应的积分

    @Column
    private Integer status; //是启用状态 1表示启用，0表示废除

    @Column
    private String oprId;

    @Column
    private boolean defaultRule; //是否默认规则

    @Column
    private String remark;//备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(Integer unitPoints) {
        this.unitPoints = unitPoints;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public boolean isDefaultRule() {
        return defaultRule;
    }

    public void setDefaultRule(boolean defaultRule) {
        this.defaultRule = defaultRule;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
