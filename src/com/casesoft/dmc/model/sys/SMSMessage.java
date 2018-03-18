package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/1.
 */
@Entity
@Table(name = "SYS_SMSMESSAGE")
public class SMSMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator = "S_SMSMESSAGE")
    @SequenceGenerator(name = "S_SMSMESSAGE", allocationSize = 1, initialValue = 1, sequenceName = "S_SMSMESSAGE")
    private Long id;
    @Column()
    private String customerid;
    @Column()
    private String customer;
    @Column()
    private String iphone;
    @Column()
    private String message;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    private Date sendTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
}
