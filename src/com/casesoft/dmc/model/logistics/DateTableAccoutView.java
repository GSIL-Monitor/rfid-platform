package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/25.
 */
@Entity
@Table(name="FIND_ACCOUNT_VIEW")
public class DateTableAccoutView {
    @Id
    @Column()
    private String id;
    @Column()
    private String billNo;//单据编号
    @Column()
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;
    @Column()
    private String billType;//订单类型 收款，付款，储值，销售，采购
    @Column()
    private String unitType;//对象类型，0供应商，1客户
    @Column()
    private String unitId;
    @Column()
    private Double actPrice;//实际价格
    @Column()
    private Double payPrice;//支付价格
    @Column()
    private Double diffPrice;//本单差额(actPrice-payPrice)
    @Column()
    private String oprId;//操作人
    @Column()
    private String remark;//备注
    @Column()
    private String ownerId;
    @Column()
    private String ownerIdname;

    public String getOwnerIdname() {
        return ownerIdname;
    }

    public void setOwnerIdname(String ownerIdname) {
        this.ownerIdname = ownerIdname;
    }

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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Double getActPrice() {
        return actPrice;
    }

    public void setActPrice(Double actPrice) {
        this.actPrice = actPrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(Double diffPrice) {
        this.diffPrice = diffPrice;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Transient
    private String groupId;   //用于分组显示

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Transient
    private Double totalOwingVal;

    public Double getTotalOwingVal() {

        return totalOwingVal;
    }

    public void setTotalOwingVal(Double totalOwingVal) {
        this.totalOwingVal = totalOwingVal;
    }
}
