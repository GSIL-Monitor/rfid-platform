package com.casesoft.dmc.model.exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by WinLi on 2017-06-05.
 * 接口模型
 */
@Entity
@Table(name = "EXCHANGE_InterfaceModel")
public class InterfaceModel implements java.io.Serializable {
    @Id
    @Column(nullable = false, length = 20)
    private String code;
    @Column(length = 20)
    private String parentCode;
    @Column(length = 200)
    private String name;
    @Column(length = 2)
    private String type;

    @Column(length = 20)
    private String origDs;//源 数据源
    @Column(length = 20)
    private String destDs;//目标 数据源
    @Column(length = 20)
    private String origTable;// 源数据库对应表
    @Column(length = 20)
    private String destTable;//目标 数据源 对应表

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrigDs() {
        return origDs;
    }

    public void setOrigDs(String origDs) {
        this.origDs = origDs;
    }

    public String getDestDs() {
        return destDs;
    }

    public void setDestDs(String destDs) {
        this.destDs = destDs;
    }

    public String getOrigTable() {
        return origTable;
    }

    public void setOrigTable(String origTable) {
        this.origTable = origTable;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }
}
