package com.casesoft.dmc.model.logistics;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/20.
 */
@Entity
@Table(name = "search_replenishbillviews")
public class ReplenishBillDtlViews {
    @Id
    @Column()
    private String id;
    @Column()
    private String  billno;
    @Column(nullable = false, length = 19)
    @JSONField(format = "yyyy-MM-dd")
    private Date billdate;
    @Column()
    private String  ownerid;
    @Column()
    private String ownername;

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    @Column()
    private String  styleid;
    @Column()
    private String stylename;
    @Column()
    private Integer convertquitqty;
    @Column
    private Integer actConvertquitQty;
    @Column()
    private Integer  franchiseestockqty;
    @Column()
    private Integer  actconvertqty;
    @Column()
    private Integer  convertqty;
    @Column()
    private String class1name;//厂家名称
    @Column()
    private String class1;//厂家
    @Column()
    private Integer qty;
    @Column()
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getActConvertquitQty() { return actConvertquitQty; }

    public void setActConvertquitQty(Integer actConvertquitQty) { this.actConvertquitQty = actConvertquitQty; }

    @Transient
    private String url;

    public String getClass1name() {
        return class1name;
    }

    public void setClass1name(String class1name) {
        this.class1name = class1name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public Date getBilldate() {
        return billdate;
    }

    public void setBilldate(Date billdate) {
        this.billdate = billdate;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public Integer getConvertquitqty() {
        return convertquitqty;
    }

    public void setConvertquitqty(Integer convertquitqty) {
        this.convertquitqty = convertquitqty;
    }

    public Integer getFranchiseestockqty() {
        return franchiseestockqty;
    }

    public void setFranchiseestockqty(Integer franchiseestockqty) {
        this.franchiseestockqty = franchiseestockqty;
    }

    public Integer getActconvertqty() {
        return actconvertqty;
    }

    public void setActconvertqty(Integer actconvertqty) {
        this.actconvertqty = actconvertqty;
    }

    public Integer getConvertqty() {
        return convertqty;
    }

    public void setConvertqty(Integer convertqty) {
        this.convertqty = convertqty;
    }
}
