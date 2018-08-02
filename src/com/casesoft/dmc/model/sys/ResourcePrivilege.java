package com.casesoft.dmc.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/6/11.
 */
@Entity
@Table(name = "SYS_RESOURCEPRIVILEGE")
public class ResourcePrivilege {
    @Id
    @Column()
    private String id;
    @Column()
    private String code;//页面code sys_resource中code
    @Column()
    private String privilegeId;//权限元素ID
    @Column()
    private String privilegeName;
    @Column()
    private Integer isShow;//0:显示，1：不显示
    @Column()
    private String roleId;//角色ID
    @Column()
    private String type;//元素类型 div,table,button

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

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
