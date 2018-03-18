package com.casesoft.dmc.model.factory;

import javax.persistence.*;

/**
 * Created by Alvin-PC on 2017/2/21 0021.
 */
@Entity
@Table(name="WorkCalendar")
public class WorkCalendar implements java.io.Serializable{

    @Id
    @Column(length=20)
    private String day;// yyyy-MM-dd
    @Column(length=2)
    private Integer status;//值参考FactoryConstant WorkCalendarStatus 值 0 ： 休息日，1：全天班：2：上午班
    @Column(length=50)
    private String updateId;
    @Column(length=30)
    private String updateTime;

    @Transient
    private String statusName;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
