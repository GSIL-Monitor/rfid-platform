package com.casesoft.dmc.model.factory;

import javax.persistence.*;

/**
 * Created by Alvin-PC on 2017/3/6 0006.
 */

@Entity
@Table(name="factory_workTime")
public class FactoryWorkTime implements java.io.Serializable {
    @Id
    @Column(name = "code",length=50)
    public String code;

    @Transient
    private String codeName;

    @Id
    @Column(name = "token",length=10)
    private Integer token;

    @Column(name = "morningStartTime",length=40)
    public String morningStartTime;

    @Column(name = "morningEndTime",length=40)
    public String morningEndTime;

    @Column(name = "afternoonStartTime",length=40)
    public String afternoonStartTime;

    @Column(name = "afternoonEndTime",length=40)
    public String afternoonEndTime;

    @Column(name = "morningTotalTime",length=10)
    public Double morningTotalTime;

    @Transient
    public Double afternoonTotalTime;

    @Column(name = "dayTotalTime",length=10)
    public Double dayTotalTime;

    @Column(name = "locked",length=10)
    public Integer locked;

    @Column(name="updateTime")
    private String updateTime;

    @Column(name="updateId")
    private String updateId;


    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Double getAfternoonTotalTime() {
        return afternoonTotalTime;
    }

    public void setAfternoonTotalTime(Double afternoonTotalTime) {
        this.afternoonTotalTime = afternoonTotalTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public String getMorningStartTime() {
        return morningStartTime;
    }

    public void setMorningStartTime(String morningStartTime) {
        this.morningStartTime = morningStartTime;
    }

    public String getMorningEndTime() {
        return morningEndTime;
    }

    public void setMorningEndTime(String morningEndTime) {
        this.morningEndTime = morningEndTime;
    }

    public String getAfternoonStartTime() {
        return afternoonStartTime;
    }

    public void setAfternoonStartTime(String afternoonStartTime) {
        this.afternoonStartTime = afternoonStartTime;
    }

    public String getAfternoonEndTime() {
        return afternoonEndTime;
    }

    public void setAfternoonEndTime(String afternoonEndTime) {
        this.afternoonEndTime = afternoonEndTime;
    }

    public Double getMorningTotalTime() {
        return morningTotalTime;
    }

    public void setMorningTotalTime(Double morningTotalTime) {
        this.morningTotalTime = morningTotalTime;
    }

    public Double getDayTotalTime() {
        return dayTotalTime;
    }

    public void setDayTotalTime(Double dayTotalTime) {
        this.dayTotalTime = dayTotalTime;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    @Transient
    private String tokenName;

    public String getTokenName() {
        return tokenName;
    }


    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
