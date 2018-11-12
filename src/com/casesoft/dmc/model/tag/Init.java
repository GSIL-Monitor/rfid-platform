package com.casesoft.dmc.model.tag;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Init entity. @author
 */
@Entity
@Table(name = "TAG_INIT")
public class Init implements java.io.Serializable {

    private static final long serialVersionUID = 5263118560626275031L;
    @Id
    @Column(nullable = false, length = 50)
    private String billNo;

    @Column(length = 7)
    private String importType;

    @Column(nullable = false, length = 200)
    private String fileName;

    @Column(nullable = false)
    private long totEpc;

    @Column()
    private long totPrintQty;

    @Column(nullable = false)
    private long totSku;

    @Column()
    private long totStyle;

    @Column(nullable = false, length = 50)
    private String ownerId;

    @Column(nullable = false, length = 50)
    private String hostId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    private Date billDate;

    @Column(nullable = false)
    private Integer status;//-1 打印中,0录入,1已确定,2已完成

    @Column()
    private Long detectTotQty;

    @Column()
    private Long receiveTotQty;

    @Column(length = 50)
    protected String detectTaskId;

    @Column(length = 50)
    protected String receiveTaskId;

    @Column(length = 50)
    protected String deliverNo;

    @Column(length = 500)
    protected String remark;
    @Column()
    private String deviceId;
    @Column(length = 20)
    private Integer unit2Type;
    @Column(length = 50)
    private String destId;

    @Transient
    private List<InitDtl> dtlList;
    @Transient
    private String destName;


    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public Integer getUnit2Type() {
        return unit2Type;
    }

    public void setUnit2Type(Integer unit2Type) {
        this.unit2Type = unit2Type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDetectTotQty() {
        return detectTotQty;
    }

    public void setDetectTotQty(Long detectTotQty) {
        this.detectTotQty = detectTotQty;
    }

    public Long getReceiveTotQty() {
        return receiveTotQty;
    }

    public void setReceiveTotQty(Long receiveTotQty) {
        this.receiveTotQty = receiveTotQty;
    }

    /**
     * default constructor
     */
    public Init() {
    }

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }


    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public long getTotEpc() {
        return this.totEpc;
    }

    public void setTotEpc(long totEpc) {
        this.totEpc = totEpc;
    }

    public long getTotPrintQty() {
        return totPrintQty;
    }

    public void setTotPrintQty(long totPrintQty) {
        this.totPrintQty = totPrintQty;
    }

    public long getTotSku() {
        return this.totSku;
    }

    public void setTotSku(long totSku) {
        this.totSku = totSku;
    }


    public long getTotStyle() {
        return this.totStyle;
    }

    public void setTotStyle(long totStyle) {
        this.totStyle = totStyle;
    }


    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    public String getHostId() {
        return this.hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }


    public Date getBillDate() {
        return this.billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }


    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getDetectTaskId() {
        return detectTaskId;
    }

    public void setDetectTaskId(String detectTaskId) {
        this.detectTaskId = detectTaskId;
    }


    public String getReceiveTaskId() {
        return receiveTaskId;
    }

    public void setReceiveTaskId(String receiveTaskId) {
        this.receiveTaskId = receiveTaskId;
    }


    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // ==========>


    public List<InitDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<InitDtl> dtlList) {
        this.dtlList = dtlList;
    }


    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }
}