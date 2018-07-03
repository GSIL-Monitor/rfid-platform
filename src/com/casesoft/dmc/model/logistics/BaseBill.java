package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;

/**
 * Created by CaseSoft-Software on 2017-06-13.
 */
@MappedSuperclass
public abstract class BaseBill {
    @Column(nullable = false, length = 50)
    protected String ownerId;
    @Column(nullable = false, length = 50)
    @Excel(name="单号")
    protected String billNo;

    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd")
    @Excel(name="日期")
    protected Date billDate;
    @Column(length = 50)
    protected String srcBillNo;//原始单号（入库单关联的出库单单号）可以为空
    @Column(length = 50)
    protected String returnBillNo;//未完成订单结束时关联的退货单号

    public String getReturnBillNo() {
        return returnBillNo;
    }

    public void setReturnBillNo(String returnBillNo) {
        this.returnBillNo = returnBillNo;
    }

    @Column(length = 50)
    protected String destUnitId;// 收货方组织ID
    @Column()
    protected String billType;// "E":表示正在编辑 "S" 表示已经保存
    @Column(length = 200)
    @Excel(name="收货方")
    protected String destUnitName;//
    @Column(length = 50)
    protected String destId;
    @Column(length = 200)
    @Excel(name="入库仓库")
    protected String destName;// 收货仓库

    @Column(length = 200)
    protected String destAddr;// 收货地址
    @Column(length = 50)
    protected String origId;//发货仓库

    @Column(length = 200)
    @Excel(name="出库仓库")
    protected String origName;// 发货仓库
    @Column(length = 50)
    protected String origUnitId;// 发货方组织ID

    @Column(length = 200)
    @Excel(name="发货方")
    protected String origUnitName;// 发货方

    @Column(length = 200)
    protected String origAddr;// 发货地址
    @Column(length = 2)
    @Excel(name="状态",replace = {  "撤销_-1","录入_0", "审核_1" , "结束_2", "操作中_3", "申请撤销_4"})
    protected Integer status ; //单据状态
    @Column(length = 50)
    protected String oprId;// 操作者Id
    @Column()
    protected String busnissId;//业务员
    @Column()
    protected String busnissName;//业务员名
    @Column()
    @Excel(name="单据数量")
    protected Long totQty;//单据数量
    @Column()
    protected Long skuQty;//单据sku数量
    @Column()
    protected Long actQty;//实际数量
    @Column()
    protected Long actSkuQty;//实际sku数
    //到貨數量
    @Column()
    protected Long arrival;

    @Column()
    protected Double totPrice;//总价格
    @Column()
    protected Double actPrice;//实际价格
    @Column()
    protected Double payPrice;//支付金额
    @Column()
    protected String payType;//类型定义参照Constant下PayType值

    @Column()
    protected Double discount; //整单折扣

    @Column(length = 500)
    @Excel(name="备注")
    protected String remark;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getSrcBillNo() {
        return srcBillNo;
    }

    public void setSrcBillNo(String srcBillNo) {
        this.srcBillNo = srcBillNo;
    }

    public String getDestUnitId() {
        return destUnitId;
    }

    public void setDestUnitId(String destUnitId) {
        this.destUnitId = destUnitId;
    }

    public String getDestUnitName() {
        return destUnitName;
    }

    public void setDestUnitName(String destUnitName) {
        this.destUnitName = destUnitName;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }


    public String getBusnissId() {
        return busnissId;
    }

    public void setBusnissId(String busnissId) {
        this.busnissId = busnissId;
    }

    public String getBusnissName() {
        return busnissName;
    }

    public void setBusnissName(String busnissName) {
        this.busnissName = busnissName;
    }
    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getOrigUnitId() {
        return origUnitId;
    }

    public void setOrigUnitId(String origUnitId) {
        this.origUnitId = origUnitId;
    }

    public String getOrigUnitName() {
        return origUnitName;
    }

    public void setOrigUnitName(String origUnitName) {
        this.origUnitName = origUnitName;
    }

    public String getOrigAddr() {
        return origAddr;
    }

    public void setOrigAddr(String origAddr) {
        this.origAddr = origAddr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public Long getTotQty() {
        return totQty;
    }

    public void setTotQty(Long totQty) {
        this.totQty = totQty;
    }

    public Long getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Long skuQty) {
        this.skuQty = skuQty;
    }

    public Long getActQty() {
        return actQty;
    }

    public void setActQty(Long actQty) {
        this.actQty = actQty;
    }

    public Long getActSkuQty() {
        return actSkuQty;
    }

    public void setActSkuQty(Long actSkuQty) {
        this.actSkuQty = actSkuQty;
    }

    public Double getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(Double totPrice) {
        this.totPrice = totPrice;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    @Transient
    private List<BillRecord> billRecordList;

    public Long getArrival() {
        return arrival;
    }

    public void setArrival(Long arrival) {
        this.arrival = arrival;
    }

    public List<BillRecord> getBillRecordList() {
        return billRecordList;
    }

    public void setBillRecordList(List<BillRecord> billRecordList) {
        this.billRecordList = billRecordList;
    }
}
