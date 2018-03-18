package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Wang yushen on 2017/7/8.
 */

@Entity
@Table(name="LOGISTICS_PAYMENTGATHERINGBILL")
public class PaymentGatheringBill {
    @Id
    @Column()
    private String id;
    @Column()
    private String billNo;
    @Column()
    private String billType;//账单类型 收款0, 储值1, 付款2
    @Column()
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;//支付日期
    @Column()
    private String customsId;//客户ID
    @Transient
    private String customsName;//客户名
    @Column()
    private String vendorId;//供应商ID
    @Transient
    private String vendorName;//供应商民
    @Column()
    private Double payPrice;//支付金额
    @Column()
    private String oprId;//更新单据用户ID
    @Column()
    private String ownerId;//当前用户的ownerID
    @Column()
    private String remark;

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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getCustomsId() {
        return customsId;
    }

    public void setCustomsId(String customsId) {
        this.customsId = customsId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomsName() {
        return customsName;
    }

    public void setCustomsName(String customsName) {
        this.customsName = customsName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
