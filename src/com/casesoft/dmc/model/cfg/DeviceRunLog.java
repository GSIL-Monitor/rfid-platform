package com.casesoft.dmc.model.cfg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-29.
 */
@Entity
@Table(name="CFG_DEVICE_RUN_LOG")
public class DeviceRunLog implements Serializable {
    @Id
    @Column(length = 10)
    private String deviceId;

    @Id
    @Column(length = 10)
    private String logDate;//记录时间
    @Column(length = 20)
    private String openPcTime;//开机时间
    @Column(length = 16)
    private String deviceIp;//设备IP

    @Column
    private BigDecimal smdjHighKm;//扫描电机高速里程
    @Column
    private BigDecimal smdjLowKm;//扫描电机低速里程
    @Column
    private BigDecimal sldjKm;//送料电机里程
    @Column
    private BigDecimal cldjKm;//出料电机里程

    @Column
    private BigInteger qmOnCs;//前门开次数
    @Column
    private BigInteger qmOffCs;//前门关次数
    @Column
    private BigInteger hmOnCs;//后门开次数
    @Column
    private BigInteger hmOffCs;//后门关次数

    @Column
    private BigInteger qmAlarmOnCs;//前门开门报警次数
    @Column
    private BigInteger qmAlarmOffCs;//前门关门报警次数
    @Column
    private BigInteger hmAlarmOnCs;
    @Column
    private BigInteger hmAlarmOffCs;

    @Column
    private Date uploadTime;


    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getOpenPcTime() {
        return openPcTime;
    }

    public void setOpenPcTime(String openPcTime) {
        this.openPcTime = openPcTime;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public BigDecimal getSmdjHighKm() {
        return smdjHighKm;
    }

    public void setSmdjHighKm(BigDecimal smdjHighKm) {
        this.smdjHighKm = smdjHighKm;
    }

    public BigDecimal getSmdjLowKm() {
        return smdjLowKm;
    }

    public void setSmdjLowKm(BigDecimal smdjLowKm) {
        this.smdjLowKm = smdjLowKm;
    }

    public BigDecimal getSldjKm() {
        return sldjKm;
    }

    public void setSldjKm(BigDecimal sldjKm) {
        this.sldjKm = sldjKm;
    }

    public BigDecimal getCldjKm() {
        return cldjKm;
    }

    public void setCldjKm(BigDecimal cldjKm) {
        this.cldjKm = cldjKm;
    }

    public BigInteger getQmOnCs() {
        return qmOnCs;
    }

    public void setQmOnCs(BigInteger qmOnCs) {
        this.qmOnCs = qmOnCs;
    }

    public BigInteger getQmOffCs() {
        return qmOffCs;
    }

    public void setQmOffCs(BigInteger qmOffCs) {
        this.qmOffCs = qmOffCs;
    }

    public BigInteger getHmOnCs() {
        return hmOnCs;
    }

    public void setHmOnCs(BigInteger hmOnCs) {
        this.hmOnCs = hmOnCs;
    }

    public BigInteger getHmOffCs() {
        return hmOffCs;
    }

    public void setHmOffCs(BigInteger hmOffCs) {
        this.hmOffCs = hmOffCs;
    }

    public BigInteger getQmAlarmOnCs() {
        return qmAlarmOnCs;
    }

    public void setQmAlarmOnCs(BigInteger qmAlarmOnCs) {
        this.qmAlarmOnCs = qmAlarmOnCs;
    }

    public BigInteger getQmAlarmOffCs() {
        return qmAlarmOffCs;
    }

    public void setQmAlarmOffCs(BigInteger qmAlarmOffCs) {
        this.qmAlarmOffCs = qmAlarmOffCs;
    }

    public BigInteger getHmAlarmOnCs() {
        return hmAlarmOnCs;
    }

    public void setHmAlarmOnCs(BigInteger hmAlarmOnCs) {
        this.hmAlarmOnCs = hmAlarmOnCs;
    }

    public BigInteger getHmAlarmOffCs() {
        return hmAlarmOffCs;
    }

    public void setHmAlarmOffCs(BigInteger hmAlarmOffCs) {
        this.hmAlarmOffCs = hmAlarmOffCs;
    }
}
