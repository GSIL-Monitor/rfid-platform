package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.sys.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-08.
 */

@Entity
@Table(name = "Hall_TaskDetail_View")
public class HallTaskDetailView implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String styleId;
    private String colorId;
    private String sizeId;
    private String ownerId;
    private String taskId;
    private String code;

    private String remark;
    private String businessTaskId;
    private Date scanDate;
    private String deviceId;

    private String borrowTaskId;// 借出任务ID，调拨出库任务ID，投产出库任务ID
    private Date borrowDate;
    private String customerId; // 出库时为收货样衣间ID
    private Date preBackDate;
    private String backTaskId;// 归还任务ID,调拨入库任务ID
    private Date backDate;
    private String backOwnerId;//归还的样衣间ID
    private Integer backStatus;//0:归还1:未归还
    private Integer type;// 0:借还  3.初始化入库 5投产下单6调拨
    //还款字段
    private Double actPrice;
    private Double tagPrice;
    private String sampleRemark;
    private String tagRemark;
    private Integer repayStatus;//0未还款1：已还款
    private String group;
    private String backTaskStatus;//0:正常1:丢失2：损坏 3：超期


    private String styleName;
    private String colorName;
    private String sizeName;
    private String image;

    private String deviceName;
    private String storageName;
    private String unitName;
    private String backUnitName;

    private String customerName;

    private String epc;
    private String oprId;
    private String remark2;
    private String centerName;
    private String floor;

    private Integer backScore;// 归还评分
    private Integer qty;
    private String businessOwnerId;//为调拨入库时获取调拨方使用

    @Id
    @Column(nullable = false, length = 45)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Column(length = 20)
    public String getStyleId() {
        return styleId;
    }
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    @Column(length = 10)
    public String getColorId() {
        return colorId;
    }
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    @Column(length = 10)
    public String getSizeId() {
        return sizeId;
    }
    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }
    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    @Column(length = 50, nullable = false)
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    @Column(nullable = false, length = 50)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(length=200)
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    //@Column(length=50)
    @Transient
    public String getBusinessTaskId() {
        return businessTaskId;
    }
    public void setBusinessTaskId(String businessTaskId) {
        this.businessTaskId = businessTaskId;
    }
    @Column(length = 50)
    public String getBorrowTaskId() {
        return borrowTaskId;
    }
    public void setBorrowTaskId(String borrowTaskId) {
        this.borrowTaskId = borrowTaskId;
    }
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
    @Column(length = 50)
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getPreBackDate() {
        return preBackDate;
    }
    public void setPreBackDate(Date preBackDate) {
        this.preBackDate = preBackDate;
    }
    @Column(length = 50)
    public String getBackTaskId() {
        return backTaskId;
    }
    public void setBackTaskId(String backTaskId) {
        this.backTaskId = backTaskId;
    }
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column( length = 19)
    public Date getBackDate() {
        return backDate;
    }
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    @Column(length = 50)
    public String getBackOwnerId() {
        return backOwnerId;
    }
    public void setBackOwnerId(String backOwnerId) {
        this.backOwnerId = backOwnerId;
    }
    @Column()
    public Integer getBackStatus() {
        return backStatus;
    }
    public void setBackStatus(Integer backStatus) {
        this.backStatus = backStatus;
    }
    @Column()
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @Column()
    public Double getActPrice() {
        return actPrice;
    }
    public void setActPrice(Double actPrice) {
        this.actPrice = actPrice;
    }
    @Column()
    public Double getTagPrice() {
        return tagPrice;
    }
    public void setTagPrice(Double tagPrice) {
        this.tagPrice = tagPrice;
    }


    @Column(length=100)
    public String getSampleRemark() {
        return sampleRemark;
    }
    public void setSampleRemark(String sampleRemark) {
        this.sampleRemark = sampleRemark;
    }
    @Column(length=100)
    public String getTagRemark() {
        return tagRemark;
    }
    public void setTagRemark(String tagRemark) {
        this.tagRemark = tagRemark;
    }
    @Column()
    public Integer getRepayStatus() {
        return repayStatus;
    }
    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column( length = 19)
    public Date getScanDate() {
        return scanDate;
    }
    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }
    @Column(length = 50)
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    @Transient
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }


    @Transient
    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Transient
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @Transient
    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    @Transient
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Transient
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Transient
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Transient
    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    @Transient
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }



    @Transient
    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }


    @Column()
    public String getFloor() {
        return floor;
    }


    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Transient
    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    @Column()
    public Integer getBackScore() {
        return backScore;
    }

    public void setBackScore(Integer backScore) {
        this.backScore = backScore;
    }

    @Column(length = 50)
    public String getOprId() {
        return oprId;
    }
    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    @Column()
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Transient
    public String getRemark2() {
        return remark2;
    }
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    @Transient
    public String getCenterName() {
        return centerName;
    }
    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }
    @Column()
    public String getBackTaskStatus() {
        return backTaskStatus;
    }
    public void setBackTaskStatus(String backTaskStatus) {
        this.backTaskStatus = backTaskStatus;
    }
    @Transient
    public String getBackUnitName() {
        return backUnitName;
    }
    public void setBackUnitName(String backUnitName) {
        this.backUnitName = backUnitName;
    }


    private String floorName;


    private User customer;


    @Transient
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Transient
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
}
