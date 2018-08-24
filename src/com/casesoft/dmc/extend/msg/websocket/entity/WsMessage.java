package com.casesoft.dmc.extend.msg.websocket.entity;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * websocket 消息存储
 *
 * */
@Table(name = "cfg_msg_push")
@Entity
public class WsMessage implements Serializable {



    /**
     *
     */
    private static final long serialVersionUID = 8409332337397906701L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    //发送者
    @Column(name = "fromCode", length = 50,nullable = false)
    private String fromCode;
    //发送者名称
    @Column(name = "fromName", length = 100)
    private String fromName;
    //接收者
    @Column(name = "toCode", nullable = false, length = 50)
    private String toCode;
    @Column(name = "toName", length = 100)
    private String toName;
    //发送的文本
    @Column(name = "content",length=1000)
    private String content;
    @Column(nullable = false, name = "success")
    private Boolean success = false;//是否发送

    @Column(nullable = false, name = "accepted")
    private Boolean accepted = false;//已读

    //发送日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, name = "sendDate")
    private Date sendDate;
    @Column(name = "acceptDate")
    private Date acceptDate;
    @Deprecated
    @Column(name = "msgType")
    private Integer msgType;

    @Column(length=10)
    private String msgPath;
    @Column(length = 10)
    private String type;

    @Column(length=500)
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getMsgPath() {
        return msgPath;
    }

    public void setMsgPath(String msgPath) {
        this.msgPath = msgPath;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
