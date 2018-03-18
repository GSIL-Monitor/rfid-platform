package com.casesoft.dmc.model.cfg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CFG_DEVICE_CONFIG")
public class DeviceConfig implements java.io.Serializable {

    @Id
    @Column(nullable = false, length = 45)
    private String id;
    @Column(length = 100)
    private String fileName;
    @Column(length = 100)
    private String path;
    @Column(length = 500)
    private String val;
    @Column(length = 50)
    private String app;
    @Column(length = 5)
    private String type;//XML/BIN/JSON
    @Column(length = 12)
    private String deviceId;
    @Column(length = 20)
    private String updater;
    @Column(length = 22)
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
