package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SYS_EMAIL")
public class Email implements java.io.Serializable{

    @Id
    @Column(nullable = false,length = 50)
    private String id;
    @Column(nullable = false,length = 255)
    private String recipients;//收件人
    @Column(nullable = false,length = 255)
    private String addresser;//发件人

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(length = 255)
    private String copy;//抄送
    @Column(nullable = false,length = 255)
    private String title;//主题
    @Column(length = 2000)
    private String content;//内容
    @Column(length = 255)
    private String adjunctUrl;//附件地址
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 50)
    private Date sendTime;//发送时间;

    @Transient
    @Column(length = 50)
    private String type;//邮件类型

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getAddresser() {
        return addresser;
    }

    public void setAddresser(String addresser) {
        this.addresser = addresser;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdjunctUrl() {
        return adjunctUrl;
    }

    public void setAdjunctUrl(String adjunctUrl) {
        this.adjunctUrl = adjunctUrl;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Column(length = 2)

    private Boolean status;//状态

}
