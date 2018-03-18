package com.casesoft.dmc.model.wms;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/*@Entity
@Table(name = "wms_baseFloor")
@Inheritance(strategy = InheritanceType.JOINED)*/
@MappedSuperclass
public abstract class BaseFloor {

    @Column(name = "barcode", length = 32)
    private String barcode;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "updateCode", length = 40)
    private String updateCode;//更新人
    @Column(name = "createDate")
    private Date createDate;
    @Column(name = "deviceId", length = 60)
    private String deviceId;
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name="image",length = 300)
    private String image;//图片

    @Column(length = 2)
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdateCode() {
        return updateCode;
    }
    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

//    public String getId() {
//        return id;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }
}
