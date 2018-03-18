package com.casesoft.dmc.model.hall;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "Hall_Sample")
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String epc;

    private String ownerId;// 所属样衣间，// 如果是出库时，为收货样衣间ID

    private int isPreBorrow;//0无，1有
    private String preBorrowBillId;
    private String preBorrowUser;
    private String preBorrowDate;

    private String preBackDate;

    private int status;// 0:未入库1在厅2外借3出库4.投产下单5丢失
    private String styleId;
    private String colorId;
    private String sizeId;
    private String image;

    private Date inDate;
    private String inTaskId;
    private String inDeviceId;
    private String inOwnerId;

    private String floor;

    private Date businessDate;
    private String businessTaskId;
    private String businessDeviceId;
    private String businessOwnerId;// 如果是出库，为出库样衣间ID，如果是借用，归还时，为customerId
    private Integer backStatus;// 0:正常1:丢失2：损坏 3：超期
    private String businessOwnerNa;
    private String remark;
    private String group;
    private Double tagPrice;

    @Id
    @Column(nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 10)
    public Double getTagPrice() {
        return tagPrice;
    }

    public void setTagPrice(Double tagPrice) {
        this.tagPrice = tagPrice;
    }

    @Column(length = 100)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(unique = true, nullable = false, length = 50)
    public String getEpc() {
        return this.epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(nullable = false, length = 20)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column(nullable = false, length = 10)
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @Column(nullable = false, length = 10)
    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    @Column(length = 50)
    public String getInTaskId() {
        return inTaskId;
    }

    public void setInTaskId(String inTaskId) {
        this.inTaskId = inTaskId;
    }

    @Column(nullable = false, length = 20)
    public String getInDeviceId() {
        return inDeviceId;
    }

    public void setInDeviceId(String inDeviceId) {
        this.inDeviceId = inDeviceId;
    }

    @Column(length = 50)
    public String getInOwnerId() {
        return inOwnerId;
    }

    public void setInOwnerId(String inOwnerId) {
        this.inOwnerId = inOwnerId;
    }

    @Column(length = 50)
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    @Column(length = 50)
    public String getBusinessTaskId() {
        return businessTaskId;
    }

    public void setBusinessTaskId(String businessTaskId) {
        this.businessTaskId = businessTaskId;
    }

    @Column(length = 50)
    public String getBusinessDeviceId() {
        return businessDeviceId;
    }

    public void setBusinessDeviceId(String businessDeviceId) {
        this.businessDeviceId = businessDeviceId;
    }

    @Column(length = 50)
    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    // //===================>
    private String styleName;
    private String colorName;
    private String sizeName;

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

    private String deviceName;
    private String storageName;
    private String unitName;

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

    @Column()
    public int getIsPreBorrow() {
        return isPreBorrow;
    }

    public void setIsPreBorrow(int isPreBorrow) {
        this.isPreBorrow = isPreBorrow;
    }

    @Column(length = 20)
    public String getPreBorrowBillId() {
        return preBorrowBillId;
    }

    public void setPreBorrowBillId(String preBorrowBillId) {
        this.preBorrowBillId = preBorrowBillId;
    }

    @Column(length = 50)
    public String getPreBorrowUser() {
        return preBorrowUser;
    }

    public void setPreBorrowUser(String preBorrowUser) {
        this.preBorrowUser = preBorrowUser;
    }

    @Column(length = 20)
    public String getPreBorrowDate() {
        return preBorrowDate;
    }

    public void setPreBorrowDate(String preBorrowDate) {
        this.preBorrowDate = preBorrowDate;
    }

    @Column(length = 20)
    public String getPreBackDate() {
        return preBackDate;
    }

    public void setPreBackDate(String preBackDate) {
        this.preBackDate = preBackDate;
    }

    private Integer preStatus;

    @Column()
    public Integer getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(Integer preStatus) {
        this.preStatus = preStatus;
    }

    private String floorName;

    @Transient
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    private String employeeName;
    private String depId;
    private Date borrowDate;

    @Transient
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    @Transient
    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    @Transient
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(Integer backStatus) {
        this.backStatus = backStatus;
    }

    public String getBusinessOwnerNa() {
        return businessOwnerNa;
    }

    public void setBusinessOwnerNa(String businessOwnerNa) {
        this.businessOwnerNa = businessOwnerNa;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
