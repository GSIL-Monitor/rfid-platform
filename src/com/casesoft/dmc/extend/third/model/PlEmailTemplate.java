package com.casesoft.dmc.extend.third.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 2017-03-06.
 * PL 邮件库存预警模板
 */
@Entity
@Table(name = "Third_Pl_EmailTemplate")
public class PlEmailTemplate implements Serializable {
    private String id;//PL前缀
    private String title;//主题
    /*******类别编码（以“,”隔开）可为空************/
    private String shopCode;//店铺
    private String class1;//品牌
    private String class2;//年份
    private String class3;//大类
    private String class4;//小类
    private String class10;//季节
    /*******************/
    private String sendCycle;//发送周期（w：每周:m每月）
    private String toUser;//收件人;以“;”隔开）
    private String fromUser;//发件人
    private String warmLevel;//等级(1.2.3)
    private String updateCode;//更新人编号
    private Date updateDate;//更新日期
    private String remark;
    private Integer status;//0：未开启，1:开启

    @Id
    @Column(name = "id", length = 50, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "title", length = 1000, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "shopCode", length = 2000)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Column(name = "class1", length = 2000)
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Column(name = "class2", length = 2000)
    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    @Column(name = "class3", length = 2000)
    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    @Column(name = "class4", length = 2000)
    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    @Column(name = "class10", length = 2000)
    public String getClass10() {
        return class10;
    }

    public void setClass10(String class10) {
        this.class10 = class10;
    }

    @Column(name = "sendCycle", length = 20, nullable = false)
    public String getSendCycle() {
        return sendCycle;
    }

    public void setSendCycle(String sendCycle) {
        this.sendCycle = sendCycle;
    }

    @Column(name = "toUser", length = 2000, nullable = false)
    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    @Column(name = "fromUser", length = 100, nullable = false)
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @Column(name = "warmLevel", length = 20)
    public String getWarmLevel() {
        return warmLevel;
    }

    public void setWarmLevel(String warmLevel) {
        this.warmLevel = warmLevel;
    }

    @Column(name = "updateCode", length = 50, nullable = false)
    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    @Column(name = "updateDate", length = 20, nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "remark", length = 2000)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "status", length = 20, nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
