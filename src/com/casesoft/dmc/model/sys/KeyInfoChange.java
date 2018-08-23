package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2018/8/23.
 *
 * 关键信息变动记录
 */
@Entity
@Table(name = "SYS_KeyInfoChange")
public class KeyInfoChange implements java.io.Serializable{
    @Id
    @Column
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ACTIVITIESSCOPE_SEQ")
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String oprId; //记录操作人
    @Column
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate; //记录操作时间
    @Column
    private String method;//记录操作的方法
    @Column
    private String preInfo;//记录操作前信息
    @Column
    private String aftInfo;//记录操作后信息
    @Column
    private String remarks;//备注

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPreInfo() {
        return preInfo;
    }

    public void setPreInfo(String preInfo) {
        this.preInfo = preInfo;
    }

    public String getAftInfo() {
        return aftInfo;
    }

    public void setAftInfo(String aftInfo) {
        this.aftInfo = aftInfo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
