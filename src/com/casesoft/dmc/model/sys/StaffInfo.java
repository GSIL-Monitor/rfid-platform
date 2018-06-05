package com.casesoft.dmc.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 包含微信openId
 *
 * Created by yushen on 2017/11/30.
 */
@Entity
@Table(name = "SYS_StaffInfo")
public class StaffInfo {
    @Id
    @Column
    private String openId;
    @Column
    private String phone;
    @Column
    private String appId;
    @Column
    private String unionId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAppId() { return appId; }

    public void setAppId(String appId) { this.appId = appId; }

    public String getUnionId() { return unionId; }

    public void setUnionId(String unionId) { this.unionId = unionId; }
}
