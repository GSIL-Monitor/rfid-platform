package com.casesoft.dmc.model.sys;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by WingLi on 2016-04-25.
 */
@Entity
@Table(name = "SYS_SETTING")
public class Setting implements java.io.Serializable {
    @Id
    @Column(nullable = false, length=50)
    private String id;
    @Column(nullable = false, length=100)
    private String name;
    @Column( length=20)
    private String valueType;
    @Column(nullable = false, length=50)
    private String value;
    @Column(length=50)
    private String refValue;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column()
    private Date updateTime;
    @Column(nullable = false,  length=20)
    private String updaterCode;
    @Column(length=10)
    private String codeType;//关联code_type，选择时显示列表

    public Setting() {
    }

    public String getUpdaterCode() {
        return updaterCode;
    }

    public void setUpdaterCode(String updaterCode) {
        this.updaterCode = updaterCode;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
