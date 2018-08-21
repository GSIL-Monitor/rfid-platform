package com.casesoft.dmc.model.syn;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by pc on 2016-12-18.
 * 同步设置
 */
@Entity
@Table(name = "SYN_BasicConfiguration")
public class BasicConfiguration {

    private String id;//唯一标志
    private String name;//功能描述
    private String description;//备注值
    private String remark;
    private Boolean enable=true;//是否可用
    private Integer configState= Config.ConfigState.Ready;
    private Date updateDate;//更新日期
    private Integer type;
    private String api;//接口名称
    @Column(name="name",length =200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConfigState(Integer configState) {
        this.configState = configState;
    }
    @Column(name="configState")
    public Integer getConfigState() {
        return configState;
    }

    @Column(name="api",length = 300,nullable = false)
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
    @Column(name="type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Id
    @Column(name="id",length =20)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="description",length = 200,nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name="remark",length = 500)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name="enable",nullable = false)
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    @Column(name="updateDate",nullable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
