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

  private String billNo;
  private String importType;
  private String fileName;
  private long totEpc;
  private long totSku;
  private long totStyle;
  private String ownerId;
  private String hostId;
  private Date billDate;
  private Integer status;

  private Long detectTotQty;
  private Long receiveTotQty;

  protected String detectTaskId;
  protected String receiveTaskId;

  protected String deliverNo;

  protected String remark;

  private String deviceId;

  private Integer unit2Type;
  private String destId;
  
  private List<InitDtl> dtlList;
  private String destName;

  @Column(length = 50)
  public String getDestId() {
    return destId;
  }

  public void setDestId(String destId) {
    this.destId = destId;
  }
  @Column(length = 20)
  public Integer getUnit2Type() {
    return unit2Type;
  }

  public void setUnit2Type(Integer unit2Type) {
    this.unit2Type = unit2Type;
  }

  @Column()
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Column()
  public Long getDetectTotQty() {
    return detectTotQty;
  }

  public void setDetectTotQty(Long detectTotQty) {
    this.detectTotQty = detectTotQty;
  }

  @Column()
  public Long getReceiveTotQty() {
    return receiveTotQty;
  }

  public void setReceiveTotQty(Long receiveTotQty) {
    this.receiveTotQty = receiveTotQty;
  }

  /** default constructor */
  public Init() {
  }

  @Id
  @Column(nullable = false, length = 50)
  public String getBillNo() {
    return this.billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  @Column(length = 7)
  public String getImportType() {
    return importType;
  }

  public void setImportType(String importType) {
    this.importType = importType;
  }

  @Column(nullable = false, length = 200)
  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Column(nullable = false)
  public long getTotEpc() {
    return this.totEpc;
  }

  public void setTotEpc(long totEpc) {
    this.totEpc = totEpc;
  }

  @Column(nullable = false)
  public long getTotSku() {
    return this.totSku;
  }

  public void setTotSku(long totSku) {
    this.totSku = totSku;
  }

  @Column()
  public long getTotStyle() {
    return this.totStyle;
  }

  public void setTotStyle(long totStyle) {
    this.totStyle = totStyle;
  }

  @Column(nullable = false, length = 50)
  public String getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Column(nullable = false, length = 50)
  public String getHostId() {
    return this.hostId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @Column(nullable = false, length = 19)
  public Date getBillDate() {
    return this.billDate;
  }

  public void setBillDate(Date billDate) {
    this.billDate = billDate;
  }

  @Column(nullable = false)
  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(length = 50)
  public String getDetectTaskId() {
    return detectTaskId;
  }

  public void setDetectTaskId(String detectTaskId) {
    this.detectTaskId = detectTaskId;
  }

  @Column(length = 50)
  public String getReceiveTaskId() {
    return receiveTaskId;
  }

  public void setReceiveTaskId(String receiveTaskId) {
    this.receiveTaskId = receiveTaskId;
  }

  @Column(length = 50)
  public String getDeliverNo() {
    return deliverNo;
  }

  public void setDeliverNo(String deliverNo) {
    this.deliverNo = deliverNo;
  }

  @Column(length = 500)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  // ==========>
 

  @Transient
  public List<InitDtl> getDtlList() {
    return dtlList;
  }

  public void setDtlList(List<InitDtl> dtlList) {
    this.dtlList = dtlList;
  }

 
 @Transient
  public String getDestName() {
    return destName;
  }

  public void setDestName(String destName) {
    this.destName = destName;
  }
}