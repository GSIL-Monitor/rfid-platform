package com.casesoft.dmc.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/6/11.
 */
@Entity
@Table(name = "SYS_RESOURCEBUTTON")
public class ResourceButton {
    @Id
    @Column()
    private String id;
    @Column()
    private String code;
    @Column()
    private String buttonId;
    @Column()
    private String buttonName;
    @Column()
    private Integer ishow;//0:显示，1：不显示
    @Column()
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public Integer getIshow() {
        return ishow;
    }

    public void setIshow(Integer ishow) {
        this.ishow = ishow;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
