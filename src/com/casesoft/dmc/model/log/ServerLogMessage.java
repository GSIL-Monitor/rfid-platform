package com.casesoft.dmc.model.log;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "LOG_SERVERLOGMESSAGE")

public class ServerLogMessage implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1041689531813791528L;


    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ACTIVITIESSCOPE_SEQ")
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.AUTO)
    @Id/*
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    @Column(nullable = false)
    private long id;
    @Column()
    private String message;
    @Column(length = 10)
    private String type;
    @Column(length = 100)
    private String Logclass;
    @Column(length = 500)
    private String method;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column()
    private Date createTime;
    @Column(length = 20)
    private String creatorId;
    @Column()
    private Long consumeTime;

    public Long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogclass() {
        return Logclass;
    }

    public void setLogclass(String logclass) {
        Logclass = logclass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
