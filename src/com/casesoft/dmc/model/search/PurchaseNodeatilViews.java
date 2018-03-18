package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/16.
 */
@Entity
@Table(name = "search_nodetailpurchchaseviews")
public class PurchaseNodeatilViews {
    @Id
    @Column()
    private String id;
    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    private Date billDate;
    @Column()
    private String billno;
    @Column()
    private String origunitid;
    @Column()
    private String origunitname;
    @Column()
    private String destid;
    @Column()
    private String destname;
    @Column()
    private String destunitid;
    @Column()
    private String  destunitname;
    @Column()
    private Integer totqty;
    @Column()
    private Integer  totinqty;
    @Column()
    private Double totinval;
    @Column()
    private String  remark;

    @Transient
    private String saletype;

    public String getSaletype() {
        return saletype;
    }

    public void setSaletype(String saletype) {
        this.saletype = saletype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
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

    public String getDestname() {
        return destname;
    }

    public void setDestname(String destname) {
        this.destname = destname;
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

    public Integer getTotqty() {
        return totqty;
    }

    public void setTotqty(Integer totqty) {
        this.totqty = totqty;
    }

    public Integer getTotinqty() {
        return totinqty;
    }

    public void setTotinqty(Integer totinqty) {
        this.totinqty = totinqty;
    }

    public Double getTotinval() {
        return totinval;
    }

    public void setTotinval(Double totinval) {
        this.totinval = totinval;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
