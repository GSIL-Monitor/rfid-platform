package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.sys.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-08.
 */
@Entity
@Table(name = "Hall_TaskDetail")
public class HallTaskDetail implements Serializable {
    private String id;
    private String styleId;
    private String colorId;
    private String sizeId;
    private String ownerId;
    private String taskId;
    private String code;
    private Integer qty;

    private String remark;

    private String businessTaskId;

    private String oprId;

    private String borrowTaskId;// 借出任务ID，调拨出库任务ID，投产出库任务ID
    private Date borrowDate;
    private String borrowDeviceId;
    private String customerId; // 出库时为收货样衣间ID
    private Date preBackDate;

    private String backTaskId;// 归还任务ID,调拨入库任务ID
    private Date backDate;
    private String backDeviceId;
    private String backOwnerId;//归还的样衣间ID
    private Integer backStatus;//0:正常1:丢失2：损坏 3：超期

    private Integer type;// 0:借出 1:返还 3.初始化入库 4调拨入库5投产下单6调拨出库


    //还款字段
    private Double actPrice;
    private Double tagPrice;
    private Double losePrice;
    private String sampleRemark;
    private String tagRemark;
    private Integer repayStatus;//0未还款1：已还款

    private String businessOwnerId;//为调拨入库时获取调拨方使用

    private Integer backScore;// 归还评分


    private String epc;

    private String floorId;
    private String floorName;

    @Column(length=50)
    public String getBusinessTaskId() {
        return businessTaskId;
    }

    public void setBusinessTaskId(String businessTaskId) {
        this.businessTaskId = businessTaskId;
    }

    public HallTaskDetail() {
    }
    public HallTaskDetail(String code,String styleId, String colorId, String sizeId, String floorId,
                         String ownerId, String taskId,
                         String oprId, String borrowTaskId, Date borrowDate,
                         String borrowDeviceId, String customerId, Date preBackDate,
                         String backTaskId, Date backDate, String backDeviceId,String backOwnerId,
                         Integer backStatus, Integer type,Integer qty,String businessTaskId) {
        super();
        this.qty=qty;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.ownerId = ownerId;
        this.taskId = taskId;
        this.code = code;
        this.oprId = oprId;
        this.borrowTaskId = borrowTaskId;
        this.borrowDate = borrowDate;
        this.borrowDeviceId = borrowDeviceId;
        this.customerId = customerId;
        this.preBackDate = preBackDate;
        this.backTaskId = backTaskId;
        this.backDate = backDate;
        this.backDeviceId = backDeviceId;
        this.backOwnerId = backOwnerId;
        this.backStatus = backStatus;
        this.businessTaskId=businessTaskId;
        this.type = type;
        this.floorId = floorId;
    }
    public HallTaskDetail(String code,String styleId, String colorId, String sizeId, String floorId,
                         String ownerId, String taskId,
                         String oprId, String borrowTaskId, Date borrowDate,
                         String borrowDeviceId, String customerId, Date preBackDate,
                         String backTaskId, Date backDate, String backDeviceId,String backOwnerId,
                         Integer backStatus, Integer type,Integer qty) {
        super();
        this.qty=qty;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.ownerId = ownerId;
        this.taskId = taskId;
        this.code = code;
        this.oprId = oprId;
        this.borrowTaskId = borrowTaskId;
        this.borrowDate = borrowDate;
        this.borrowDeviceId = borrowDeviceId;
        this.customerId = customerId;
        this.preBackDate = preBackDate;
        this.backTaskId = backTaskId;
        this.backDate = backDate;
        this.backDeviceId = backDeviceId;
        this.backOwnerId = backOwnerId;
        this.backStatus = backStatus;
        this.type = type;
        this.floorId = floorId;
    }

    public HallTaskDetail(String code,String styleId, String colorId, String sizeId, String floorId,
                         String ownerId, String taskId,String remark,
                         String oprId, String borrowTaskId, Date borrowDate,
                         String borrowDeviceId, String customerId, Date preBackDate,
                         String backTaskId, Date backDate, String backDeviceId,String backOwnerId,
                         Integer backStatus, Integer type,Integer qty) {
        super();
        this.qty=qty;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.ownerId = ownerId;
        this.taskId = taskId;
        this.remark = remark;
        this.code = code;
        this.oprId = oprId;
        this.borrowTaskId = borrowTaskId;
        this.borrowDate = borrowDate;
        this.borrowDeviceId = borrowDeviceId;
        this.customerId = customerId;
        this.preBackDate = preBackDate;
        this.backTaskId = backTaskId;
        this.backDate = backDate;
        this.backDeviceId = backDeviceId;
        this.backOwnerId = backOwnerId;
        this.backStatus = backStatus;
        this.type = type;
        this.floorId = floorId;
    }
    public HallTaskDetail(String styleId, String colorId, String ownerId,
                         Date borrowDate, String customerId, Date backDate, Integer type,
                         String floorId,String remark) {
        super();
        this.styleId = styleId;
        this.colorId = colorId;
        this.ownerId = ownerId;
        this.borrowDate = borrowDate;
        this.customerId = customerId;
        this.backDate = backDate;
        this.type = type;
        this.floorId = floorId;
        this.remark=remark;
    }

    public HallTaskDetail(String styleId, String colorId, String ownerId,
                         Date borrowDate, String customerId, Date backDate, Integer type,
                         String floorId,String remark,Integer backScore,String oprId) {
        super();
        this.styleId = styleId;
        this.colorId = colorId;
        this.ownerId = ownerId;
        this.borrowDate = borrowDate;
        this.customerId = customerId;
        this.backDate = backDate;
        this.type = type;
        this.floorId = floorId;
        this.remark=remark;
        this.backScore = backScore;
        this.oprId = oprId;
    }
    @Column(length=200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Id
    @Column(nullable = false, length = 45)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Column(length = 40)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column( length = 10)
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

    @Column()
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Column(nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Column(length = 50)
    public String getBackOwnerId() {
        return backOwnerId;
    }

    public void setBackOwnerId(String backOwnerId) {
        this.backOwnerId = backOwnerId;
    }

    @Column(length = 50)
    public String getBorrowTaskId() {
        return borrowTaskId;
    }
    public void setBorrowTaskId(String borrowTaskId) {
        this.borrowTaskId = borrowTaskId;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }


    @Column(length = 50)
    public String getBorrowDeviceId() {
        return borrowDeviceId;
    }

    public void setBorrowDeviceId(String borrowDeviceId) {
        this.borrowDeviceId = borrowDeviceId;
    }

    @Column(length = 50)
    public String getBackTaskId() {
        return backTaskId;
    }

    public void setBackTaskId(String backTaskId) {
        this.backTaskId = backTaskId;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    @Column( length = 50)
    public String getBackDeviceId() {
        return backDeviceId;
    }

    public void setBackDeviceId(String backDeviceId) {
        this.backDeviceId = backDeviceId;
    }

    @Column(length = 50)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(length = 50)
    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    @Column(length = 19)
    public Date getPreBackDate() {
        return preBackDate;
    }

    public void setPreBackDate(Date preBackDate) {
        this.preBackDate = preBackDate;
    }

    @Column()
    public Integer getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(Integer backStatus) {
        this.backStatus = backStatus;
    }

    // //===================>





    @Transient
    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    @Column()
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @Transient
    public String getFloorId() {
        return floorId;
    }

    @Transient
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
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
    @Column()
    public Double getLosePrice() {
        return losePrice;
    }

    public void setLosePrice(Double losePrice) {
        this.losePrice = losePrice;
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


    @Column()
    public Integer getBackScore() {
        return backScore;
    }

    public void setBackScore(Integer backScore) {
        this.backScore = backScore;
    }

    @Transient
    private String styleName;

    @Transient
    private String ColorName;

    @Transient
    private String unitName;

    @Transient
    private String sizeName;


    private User customer;

    @Transient
    private String customerName;

    @Transient
    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    @Transient
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
