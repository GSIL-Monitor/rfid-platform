package com.casesoft.dmc.model.exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by WinLi on 2017-06-05.
 */
@Entity
@Table(name = "EXCHANGE_DataSrcInfo")
public class DataSrcInfo implements java.io.Serializable {
    @Id
    @Column(nullable = false, length = 50)
    private String id;
    @Column(nullable = false, length = 200)
    private String name;//描述
    @Column(length = 20)
    private String dsIp;//ip地址
    @Column(length = 6)
    private String dsPort;//端口
    @Column(length = 20)
    private String dbName;//数据库名称/oracle服务名
    @Column(length = 20)
    private String dbUser;//用户名
    @Column(length = 20)
    private String dbPass;//密码
    @Column(length = 2)
    private String status;//状态
    @Column(length = 10)
    private String type;//数据库类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDsIp() {
        return dsIp;
    }

    public void setDsIp(String dsIp) {
        this.dsIp = dsIp;
    }

    public String getDsPort() {
        return dsPort;
    }

    public void setDsPort(String dsPort) {
        this.dsPort = dsPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
