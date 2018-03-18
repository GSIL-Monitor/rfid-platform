package com.casesoft.dmc.model.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.erp.Bill;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by WingLi on 2014/11/21.
 */

/**
 * [Serializable]
 public class Task
 {

 public Task()
 {

 }

 #region 属性定义
 /// <summary>
 ///  TSK+年份后两位+月份+日期+设备号前两位+设备号最后两位+4位流水号
 /// </summary>
 [JsonProperty(Order = 0)]
 [XmlAttribute]
 public string taskId { get; set; }

 [XmlAttribute]
 public string flagFileUploadStatus { get; set; }

 [JsonProperty(Order = 1)]
 [XmlAttribute]
 public string flagTaskStatus { get; set; }

 [JsonProperty(Order = 2)]
 [XmlAttribute]
 public string taskDeviceId { get; set; }

 [JsonProperty(Order = 3)]
 [XmlAttribute]
 public string taskBeginTime { get; set; }

 [JsonProperty(Order = 4)]
 [XmlAttribute]
 public string taskEndTime { get; set; }

 [JsonProperty(Order = 5)]
 [XmlAttribute]
 public int taskTotEpc { get; set; }
 [JsonProperty(Order = 6)]
 [XmlAttribute]
 public int taskTotCarton { get; set; }

 [JsonProperty(Order = 7)]
 [XmlAttribute]
 public int taskTotSku { get; set; }

 [JsonProperty(Order = 8)]
 [XmlAttribute]
 public int taskTotStyle { get; set; }

 [JsonProperty(Order = 9)]
 [XmlAttribute]
 public int taskRfidToken { get; set; }

 [JsonProperty(Order = 10)]
 [XmlAttribute]
 public string taskRemark { get; set; }

 [XmlAttribute]
 [JsonProperty]
 public string billId { get; set; }

 [XmlAttribute]
 [JsonProperty]
 public string signImage
 {
 get { return "/images/continue.png"; }
 }

 #endregion
 }
 *
 */
@Entity
@Table(name = "task_CloudBusiness")
public class CloudBusiness implements java.io.Serializable{

    private static final long serialVersionUID = 6072395948739316396L;
    private String id;
    private Integer status;
    private String deviceId;
    private String ownerId;
    private Integer token;
    private Date beginTime;
    private Date endTime;
    private long totEpc;
    private long totStyle;
    private long totSku;
    private long totCarton;

    private long unitQty;
    private String storageId;

    private String billId;
    private String billNo;

    private String srcTaskId;
    private int locked;
    private String flagFileUploadStatus = "1";
    private String signImage = "/images/continue.png";
    private long totTime;
    private String storageName;
    private String deviceName;
    private String unitName;
    private Bill bill;
    private Integer unit2Type;
    private String storage2Id;
    private String unit2Id; //如果是品牌商自己的门店，值为null
    private Integer type;//0:出库1：入库3：盘点
    private List<CloudBusinessDtl> cloudBusinessDtlList;
    private List<CloudBox> cloudBoxList;
    // private List<BoxDtl> boxDtlList;
    private List<CloudRecord> cloudRecordList;
    
    @Column(length = 500)
    public String getSrcTaskId() {
        return srcTaskId;
    }

    public void setSrcTaskId(String srcTaskId) {
        this.srcTaskId = srcTaskId;
    }

   

    @Column()
    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    /** default constructor */
    public CloudBusiness() {
    }

    /** full constructor */
    public CloudBusiness(Integer status, String deviceId, String ownerId, Integer token, Date beginTime,
                    Date endTime, long totEpc, long totStyle, long totSku, long totCarton) {
        this.status = status;
        this.deviceId = deviceId;
        this.ownerId = ownerId;
        this.token = token;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.totEpc = totEpc;
        this.totStyle = totStyle;
        this.totSku = totSku;
        this.totCarton = totCarton;
    }

  

    @Transient
    public String getFlagFileUploadStatus() {
        return flagFileUploadStatus;
    }

    public void setFlagFileUploadStatus(String flagFileUploadStatus) {
        this.flagFileUploadStatus = flagFileUploadStatus;
    }


    
    @Transient
    public String getSignImage() {
        return signImage;
    }

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }

    // Property accessors
    @JSONField()
    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @JSONField(name="flagTaskStatus")
    @Column(nullable = false)
    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @JSONField(name="taskDeviceId")
    @Column(nullable = false, length = 50)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    @JSONField(name="taskRfidToken")
    @Column(nullable = false)
    public Integer getToken() {
        return this.token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    @JSONField(name="taskBeginTime",format="yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JSONField(name="taskEndTime",format="yyyy-MM-dd HH:mm:ss")
    @Column(nullable = true, length = 19)
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    @JSONField(name="taskTotEpc")
    @Column(nullable = false)
    public long getTotEpc() {
        return this.totEpc;
    }

    public void setTotEpc(long totEpc) {
        this.totEpc = totEpc;
    }
    @JSONField(name="taskTotStyle")
    @Column(nullable = false)
    public long getTotStyle() {
        return this.totStyle;
    }

    public void setTotStyle(long totStyle) {
        this.totStyle = totStyle;
    }
    @JSONField(name="taskTotSku")
    @Column(nullable = false)
    public long getTotSku() {
        return this.totSku;
    }

    public void setTotSku(long totSku) {
        this.totSku = totSku;
    }
    @JSONField(name="taskTotCarton")
    @Column(nullable = false)
    public long getTotCarton() {
        return this.totCarton;
    }

    public void setTotCarton(long totCarton) {
        this.totCarton = totCarton;
    }

    @Column(nullable = false)
    public long getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(long unitQty) {
        this.unitQty = unitQty;
    }

    @Column(length = 50)
    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    @Column(length = 50)
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Column(length = 50)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }



    

    @Transient
    public long getTotTime() {
        return totTime;
    }

    public void setTotTime(long totTime) {
        this.totTime = totTime;
    }

    

    @Transient
    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    @Transient
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Transient
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

   

    @Transient
    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

   

    @Column()
    public Integer getUnit2Type() {
        return unit2Type;
    }

    public void setUnit2Type(Integer unit2Type) {
        this.unit2Type = unit2Type;
    }
    @Column(length = 50)
    public String getStorage2Id() {
        return storage2Id;
    }

    public void setStorage2Id(String storage2Id) {
        this.storage2Id = storage2Id;
    }
    @Column(length = 50)
    public String getUnit2Id() {
        return unit2Id;
    }

    public void setUnit2Id(String unit2Id) {
        this.unit2Id = unit2Id;
    }
    @Column()
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

   

    @Transient
    public List<CloudBusinessDtl> getCloudBusinessDtlList() {
        return cloudBusinessDtlList;
    }

    public void setCloudBusinessDtlList(List<CloudBusinessDtl> cloudBusinessDtlList) {
        this.cloudBusinessDtlList = cloudBusinessDtlList;
    }
    @Transient
    public List<CloudBox> getCloudBoxList() {
        return cloudBoxList;
    }

    public void setCloudBoxList(List<CloudBox> cloudBoxList) {
        this.cloudBoxList = cloudBoxList;
    }
    @Transient
    public List<CloudRecord> getCloudRecordList() {
        return cloudRecordList;
    }

    public void setCloudRecordList(List<CloudRecord> cloudRecordList) {
        this.cloudRecordList = cloudRecordList;
    }
}
