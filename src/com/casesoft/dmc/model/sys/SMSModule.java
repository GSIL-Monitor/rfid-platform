package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/31.
 */
@Entity
@Table(name = "SYS_SMSMODULE")
public class SMSModule{
    @Id
    @Column(nullable = false, length = 50)
    private String id;
    @Column()
    private String title;
    @Column()
    private String content;
    @Column()
    private String templateid;
    @Column()
    private String approval;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, length = 19)
    private Date saveTime;//保存时间;
    @Column()
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }


}
