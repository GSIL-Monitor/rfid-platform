package com.casesoft.dmc.model.search;

import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

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
    @Excel(name="时间")
    private Date billDate;
    @Column()
    @Excel(name="单号")
    private String billno;
    @Column()
    private String origunitid;
    @Column()
    private String origunitname;
    @Column()
    private String destid;
    @Column()
    @Excel(name="出入仓库")
    private String destname;
    @Column()
    private String destunitid;
    @Column()
    private String  destunitname;
    @Column()
    @Excel(name="数量")
    private Integer totqty;
    @Column()
    @Excel(name="出入库数量")
    private Integer  totinqty;
    @Column()
    @Excel(name="金额")
    private Double totinval;
    @Column()
    @Excel(name="备注")
    private String  remark;

    @Column()
    @Excel(name="单据类型")
    private String saletype;
    @Column()
    @Excel(name="任务号")
    private String taskid;

    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    @Excel(name="入库时间")
    private Date intime;
    @Column()
    @Excel(name="入库数量")
    private Integer inqty;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getInqty() {
        return inqty;
    }

    public void setInqty(Integer inqty) {
        this.inqty = inqty;
    }

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
