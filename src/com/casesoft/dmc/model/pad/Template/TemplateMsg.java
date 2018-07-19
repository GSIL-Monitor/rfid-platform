package com.casesoft.dmc.model.pad.Template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *模板消息存储信息
 * Created by ltc on 2018/6/14.
 */
@Entity
@Table(name = "Template_Msg")
public class TemplateMsg {
    @Id
    @Column(length = 50)
    private String billNo;
    @Column(length = 200)
    private String openId;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getActPrice() {
        return actPrice;
    }

    public void setActPrice(String actPrice) {
        this.actPrice = actPrice;
    }

    public String getTotQty() {
        return totQty;
    }

    public void setTotQty(String totQty) {
        this.totQty = totQty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(length = 15)

    private String actPrice;
    @Column(length = 10)
    private String totQty;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String time;
    @Column(length = 1)
    private int type;

}
