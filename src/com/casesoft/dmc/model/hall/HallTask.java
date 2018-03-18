package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by WinLi on 2017-03-08.
 */
@Entity
@Table(name = "Hall_Task")
public class HallTask implements java.io.Serializable {
    private String id;

    private Date scanDate;
    private String deviceId;
    private String ownerId;
    private Integer skuQty;
    private Integer qty;
    private Date beginTime;
    private Date endTime;
    private String floorId;
    private String floor;
    private Integer combineStatus;

    private String combineId;

    private String srcTaskId;

    private String oprId;// 管理员ID
    private String adminName;


    private Integer type;//
    // 0:借出 1:返还 3.初始化入库 4调拨入库 5投产下单6调拨出库

    private String customerId;// 借出时，为借用人ID,调拨出库时,为收货方样衣间ID
    private String centerName;//中心（借用人）

    private Date preBackDate;
    private String remark;

    // 返还样衣时统计字段
    private Integer borrowQty;//借出数量
    private Integer badQty;         //报损数量
    private Integer lostQty;        //
    private Integer overdueQty;
    private Integer backScore;// 归还评分

    private String remark2;


    private Integer status;

    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column()
    public Integer getCombineStatus() {
        return combineStatus;
    }

    public void setCombineStatus(Integer combineStatus) {
        this.combineStatus = combineStatus;
    }

    @Column(length=150)
    public String getCombineId() {
        return combineId;
    }

    public void setCombineId(String combineId) {
        this.combineId = combineId;
    }


    @Column(length=50)
    public String getSrcTaskId() {
        return srcTaskId;
    }

    public void setSrcTaskId(String srcTaskId) {
        this.srcTaskId = srcTaskId;
    }

    @Column(length = 30)
    public String getFloorId() {
        return floorId;
    }


    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }




    @Column(nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @JSONField(format="yyyy-MM-dd HH:mm:ss")
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
    @Column( length = 19)
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

    @Column()
    public Integer getBackScore() {
        return backScore;
    }

    public void setBackScore(Integer backScore) {
        this.backScore = backScore;
    }

    @Column(length = 50)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(length = 50)
    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    @JSONField(format="yyyy-MM-dd")
    @Column(length = 19)
    public Date getPreBackDate() {
        return preBackDate;
    }

    public void setPreBackDate(Date preBackDate) {
        this.preBackDate = preBackDate;
    }

    @Column(length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column()
    public Integer getBorrowQty() {
        return borrowQty;
    }

    public void setBorrowQty(Integer borrowQty) {
        this.borrowQty = borrowQty;
    }

    @Column()
    public Integer getBadQty() {
        return badQty;
    }

    public void setBadQty(Integer badQty) {
        this.badQty = badQty;
    }

    @Column()
    public Integer getLostQty() {
        return lostQty;
    }

    public void setLostQty(Integer lostQty) {
        this.lostQty = lostQty;
    }

    @Column()
    public Integer getOverdueQty() {
        return overdueQty;
    }

    public void setOverdueQty(Integer overdueQty) {
        this.overdueQty = overdueQty;
    }


    @Column(length=200)
    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }


    private List<HallTaskDetail> detailList;

    @Transient
    public List<HallTaskDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<HallTaskDetail> detailList) {
        this.detailList = detailList;
    }
}
