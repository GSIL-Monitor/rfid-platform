package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/10.
 */
@Entity
@Table(name = "search_purchaseorcountviews")
public class PurchaseorCountviews {
    @Id
    @Column()
    private String id;
    @Column()
    @Excel(name = "SKU", width = 20D)
    private String sku;
    @Column()
    @Excel(name="款号")
    private String styleid;
    @Column()
    @Excel(name="颜色")
    private String colorid;
    @Column()
    @Excel(name="尺号")
    private String sizeid;
    @Column()
    @Excel(name="数量")
    private Integer qty;
    @Column()
    @Excel(name="吊牌价")
    private Double price;
    @Column()
    private Double totprice;
    @Column()
    @Excel(name="实际价格")
    private Double actprice;
    @Column()
    @Excel(name="实际金额")
    private Double totactprice;
    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    @Excel(name="日期")
    private Date billDate;
    @Column()
    @Excel(name="单号")
    private String billid;
    @Column()
    private String origunitid;
    @Column()
    private String origunitname;
    @Column()
    private String destunitid;
    @Column()
    @Excel(name="款名")
    private String stylename;
    @Column()
    @Excel(name="出入仓库")
    private String destname;
    @Column()
    @Excel(name="出库状态",replace = { "订单状态_0" , "已入库_1", "入库中_4"})
    private Integer instatus;
    @Column()
    @Excel(name="出库状态",replace = { "订单状态_0" , "已出库_2", "出库中_3"})
    private Integer outstatus;
    @Transient
    @Excel(name="图片", width = 25D,type = 2)
    private String url;
    @Column()
    @Excel(name="单据状态",replace = { "撤销_-1", "录入_0" , "审核_1", "结束_2", "操作中_3", "申请撤销_4"})
    private Integer status;
    @Column()
    @Excel(name="入库数量")
    private Integer inQty;
    @Column()
    @Excel(name="任务号")
    private String taskid;

    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    @Excel(name="入库时间")
    private Date intime;

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOutstatus() {
        return outstatus;
    }

    public void setOutstatus(Integer outstatus) {
        this.outstatus = outstatus;
    }

    public Integer getInstatus() {
        return instatus;
    }

    public void setInstatus(Integer instatus) {
        this.instatus = instatus;
    }

    public String getDestname() {
        return destname;
    }

    public void setDestname(String destname) {
        this.destname = destname;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public String getDestunitid() {
        return destunitid;
    }

    public void setDestunitid(String destunitid) {
        this.destunitid = destunitid;
    }

    public String getDestunitname() {
        return destunitname;
    }

    public void setDestunitname(String destunitname) {
        this.destunitname = destunitname;
    }

    @Column()
    @Excel(name="供应商")
    private String destunitname;
    @Column()
    private String class1;

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Column()
    @Excel(name="厂家")
    private String class1Name;

    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    @Column()
    @Excel(name="单据类型")
    private String saletype;

    public String getSaletype() {
        return saletype;
    }

    public void setSaletype(String saletype) {
        this.saletype = saletype;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @Column()
    private String destid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public String getColorid() {
        return colorid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotprice() {
        return totprice;
    }

    public void setTotprice(Double totprice) {
        this.totprice = totprice;
    }

    public Double getActprice() {
        return actprice;
    }

    public void setActprice(Double actprice) {
        this.actprice = actprice;
    }

    public Double getTotactprice() {
        return totactprice;
    }

    public void setTotactprice(Double totactprice) {
        this.totactprice = totactprice;
    }



    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getOrigunitid() {
        return origunitid;
    }

    public void setOrigunitid(String origunitid) {
        this.origunitid = origunitid;
    }

    public String getOrigunitname() {
        return origunitname;
    }

    public void setOrigunitname(String origunitname) {
        this.origunitname = origunitname;
    }

    public String getDestid() {
        return destid;
    }

    public void setDestid(String destid) {
        this.destid = destid;
    }
}
