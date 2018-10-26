package com.casesoft.dmc.model.search;


import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;
/**
 * Created by Administrator on 2017/9/4.
 */
@Entity
@Table(name = "search_salenodeatielcountviews")
public class SaleNodeatilViews {
    @Id
    @Column()
    private String id;
    @Column()
    @Excel(name = "单号", width = 20D)
    private String billno;
    @Column()
    @Excel(name = "应付付金额")
    private Double actprice;
    @Column()
    private Integer actqty;
    @Column()
    private Integer actskuqty;
    @JSONField(format="yyyy-MM-dd")
    @Column(nullable = false,length = 19)
    @Excel(name = "日期", width = 30D)
    private Date billDate;
    @Column()
    private String billtype;
    @Column()
    private String busnissid;
    @Column()
    private String destaddr;
    @Column()
    private String destid;
    @Column()
    private String destname;
    @Column()
    private String destunitid;
    @Column()
    private String destunitname;
    @Column()
    private String oprid;
    @Column()
    private String origaddr;
    @Column()
    @Excel(name = "仓库ID", width = 20D)
    private String origid;
    @Column()
    @Excel(name = "仓库")
    private String origname;
    @Column()
    @Excel(name = "客户ID")
    private String origunitid;
    @Column()
    @Excel(name = "客户")
    private String origunitname;
    @Column()
    private String ownerid;
    @Column()
    @Excel(name = "实付金额")
    private Double  payprice;
    @Column()
    private String paytype;
    @Column()
    @Excel(name = "备注")
    private String remark;
    @Column()
    private Integer skuqty;
    private Integer status;
    @Column()
    private Double totprice;
    @Column()
    @Excel(name = "单据数量")
    private Integer totqty;
    @Column()
    @Excel(name = "客户类型",replace = { "省代客户_CT-AT", "门店客户_CT-ST","零售客户_CT-LS"})
    private String customertypeid;
    @Column()
    private String srcbillno;
    @Column()
    @Excel(name = "已出库数量")
    private Integer totoutqty;
    @Column()
    private Double totoutval;
    @Column()
    private Integer instatus;
    @Column()
    private Integer outstatus;
    @Column()
    @Excel(name = "已入库数量")
    private Integer totinqty;
    @Column()
    private Double totstockval;
    @Column()
    private Double totinval;
    @Column()
    @Excel(name="大类")
    private String class3Name;

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

    public Double getActprice() {
        return actprice;
    }

    public void setActprice(Double actprice) {
        this.actprice = actprice;
    }

    public Integer getActqty() {
        return actqty;
    }

    public void setActqty(Integer actqty) {
        this.actqty = actqty;
    }

    public Integer getActskuqty() {
        return actskuqty;
    }

    public void setActskuqty(Integer actskuqty) {
        this.actskuqty = actskuqty;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBusnissid() {
        return busnissid;
    }

    public void setBusnissid(String busnissid) {
        this.busnissid = busnissid;
    }

    public String getDestaddr() {
        return destaddr;
    }

    public void setDestaddr(String destaddr) {
        this.destaddr = destaddr;
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

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid;
    }

    public String getOrigaddr() {
        return origaddr;
    }

    public void setOrigaddr(String origaddr) {
        this.origaddr = origaddr;
    }

    public String getOrigid() {
        return origid;
    }

    public void setOrigid(String origid) {
        this.origid = origid;
    }

    public String getOrigname() {
        return origname;
    }

    public void setOrigname(String origname) {
        this.origname = origname;
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

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public Double getPayprice() {
        return payprice;
    }

    public void setPayprice(Double payprice) {
        this.payprice = payprice;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSkuqty() {
        return skuqty;
    }

    public void setSkuqty(Integer skuqty) {
        this.skuqty = skuqty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getTotprice() {
        return totprice;
    }

    public void setTotprice(Double totprice) {
        this.totprice = totprice;
    }

    public Integer getTotqty() {
        return totqty;
    }

    public void setTotqty(Integer totqty) {
        this.totqty = totqty;
    }

    public String getCustomertypeid() {
        return customertypeid;
    }

    public void setCustomertypeid(String customertypeid) {
        this.customertypeid = customertypeid;
    }

    public String getSrcbillno() {
        return srcbillno;
    }

    public void setSrcbillno(String srcbillno) {
        this.srcbillno = srcbillno;
    }

    public Integer getTotoutqty() {
        return totoutqty;
    }

    public void setTotoutqty(Integer totoutqty) {
        this.totoutqty = totoutqty;
    }

    public Double getTotoutval() {
        return totoutval;
    }

    public void setTotoutval(Double totoutval) {
        this.totoutval = totoutval;
    }

    public Integer getInstatus() {
        return instatus;
    }

    public void setInstatus(Integer instatus) {
        this.instatus = instatus;
    }

    public Integer getOutstatus() {
        return outstatus;
    }

    public void setOutstatus(Integer outstatus) {
        this.outstatus = outstatus;
    }

    public Integer getTotinqty() {
        return totinqty;
    }

    public void setTotinqty(Integer totinqty) {
        this.totinqty = totinqty;
    }

    public Double getTotstockval() {
        return totstockval;
    }

    public void setTotstockval(Double totstockval) {
        this.totstockval = totstockval;
    }

    public Double getTotinval() {
        return totinval;
    }

    public void setTotinval(Double totinval) {
        this.totinval = totinval;
    }

    public String getBusnissname() {
        return busnissname;
    }

    public void setBusnissname(String busnissname) {
        this.busnissname = busnissname;
    }

    public String getDeport() {
        return deport;
    }

    public void setDeport(String deport) {
        this.deport = deport;
    }
    @Column()
    @Excel(name = "销售员")
    private String busnissname;
    @Column()
    private String deport;
    @Column()
    @Excel(name = "单据类型")
    private String saletype;

    public String getSaletype() {
        return saletype;
    }

    public void setSaletype(String saletype) {
        this.saletype = saletype;
    }
    @Transient
    private boolean ishow;

    public boolean isIshow() {
        return ishow;
    }

    public void setIshow(boolean ishow) {
        this.ishow = ishow;
    }
    @Column()
    private String  groupid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getClass3Name() {
        return class3Name;
    }

    public void setClass3Name(String class3Name) {
        this.class3Name = class3Name;
    }
}
