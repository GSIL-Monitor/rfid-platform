package com.casesoft.dmc.model.factory;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvin-PC on 2017/5/11 0011.
 */

@Entity
@Table(name="FACTORYBILL_VIEW")
public class FactoryBillView implements java.io.Serializable{

    @Id
    @Column(name="id")
    private String id;
    @Column(name="bill_No")
    private String billNo;

    @JSONField(format="yyyy-MM-dd")
    @Column(name="bill_date")
    private Date billDate;
    @Column(name="bill_qty")
    private Integer billQty;

    @Column(name="owner_Id")
    private String ownerId;

    @Column(name="customer_Id")
    private String customerId;

    @Column(name="group_Id")
    private String groupId;

    @Column(name="wash_Type")
    private String washType;

    @Column(name="sex")
    private String sex;

    @Column(name="type")
    private String type;

    @Column(name="shirt_Type")
    private String shirtType;

    @Column(name="remark")
    private String remark;

    @Column(name="bill_operator")
    private String billOperator;

    @JSONField(format="yyyy-MM-dd")
    @Column(name="end_Date")
    private Date endDate;

    @JSONField(format="yyyy-MM-dd")
    @Column(name="out_Date")
    private Date outDate;

    @JSONField(format="yyyy-MM-dd")
    @Column(name="PRINT_DATE")
    private Date printDate;

    @Column(name="UPLOAD_NO")
    private String uploadNo;

    @Column(name="isSchedule",length=1)
    private String isSchedule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShirtType() {
        return shirtType;
    }

    public void setShirtType(String shirtType) {
        this.shirtType = shirtType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBillOperator() {
        return billOperator;
    }

    public void setBillOperator(String billOperator) {
        this.billOperator = billOperator;
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public String getUploadNo() {
        return uploadNo;
    }

    public void setUploadNo(String uploadNo) {
        this.uploadNo = uploadNo;
    }

    public String getIsSchedule() {
        return isSchedule;
    }

    public void setIsSchedule(String isSchedule) {
        this.isSchedule = isSchedule;
    }

    @Column(name="season")
    private String season;

    @Column(name="category")
    private String category;

    @Column(name="factory")
    private String factory;

    @Column(name="imgUrl")
    private String imgUrl;



    @Column(name="progress")
    private String progress;


    @Column(name="scheduleEndDate")
    private String scheduleEndDate;//排期结束日期

    @Column(name="scheduleStartDate")
    private String scheduleStartDate;//排期开始日期

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    @Column(name="scheduleUpDateTime")
    private Date scheduleDate;




    public String getSeason() {
        return season;
    }


    public void setSeason(String season) {
        this.season = season;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getFactory() {
        return factory;
    }


    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getImgUrl() {
        return imgUrl;
    }


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }


    public String getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(String scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
    }

    public String getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(String scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    @Transient
    private Integer schedulePeriod;// scheduleEndDate - scheduleStartDate 中有效工作日
    public Integer getSchedulePeriod() {
        return schedulePeriod;
    }

    public void setSchedulePeriod(Integer schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
    }

    @Transient
    private Integer totDay;

    public Integer getTotDay() {
        return totDay;
    }

    public void setTotDay(Integer totDay) {
        this.totDay = totDay;
    }

    @Transient
    private String progressName;

    public String getProgressName() {
        return progressName;
    }

    public void setProgressName(String progressName) {
        this.progressName = progressName;
    }

    @Transient
    private List<BillSchedule> billScheduleList;

    public List<BillSchedule> getBillScheduleList() {
        return billScheduleList;
    }

    public void setBillScheduleList(List<BillSchedule> billScheduleList) {
        this.billScheduleList = billScheduleList;
    }
}
