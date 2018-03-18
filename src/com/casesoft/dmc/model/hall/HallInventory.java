package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-08.
 */
@Entity
@Table(name = "Hall_Inventory")
public class HallInventory implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String taskId;
    private Date scanDate;
    private String deviceId;
    private String ownerId;
    private Integer skuQty;
    private Integer qty;    //应盘数量
    private Date beginTime;
    private Date endTime;

    private int status;//0:正常 1 盘盈 2 盘亏 3 有亏有盈  4装箱盘点
    private String isCheck;// null未审核

    private Date billDate;
    private String oprId;
    private String floor;
    private long actQty;    //实际数量


    private String ownerName;


    private String floorName;

    public HallInventory() {
        super();
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column( unique = true, nullable = false, length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(length = 19)
    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    @Column(nullable = false, length = 50)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column()
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column()
    public Integer getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Integer skuQty) {
        this.skuQty = skuQty;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @Column(length = 20)
    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    @Column(length = 50)
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Column()
    public long getActQty() {
        return actQty;
    }

    public void setActQty(long actQty) {
        this.actQty = actQty;
    }

    @Column
    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    @Transient
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Transient
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
