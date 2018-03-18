package com.casesoft.dmc.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * RoleRes entity. @author
 */
@Entity
@Table(name = "SYS_ROLERES")
public class RoleRes implements java.io.Serializable {

    private static final long serialVersionUID = 3862476258103067410L;

    private String id;
    private String roleId;
    private String resId;

    private Date createTime;
    private String creatorId;

    // Constructors

    /**
     * default constructor
     */
    public RoleRes() {
    }

    /**
     * full constructor
     */
    public RoleRes(String roleId, String resId) {
        this.roleId = roleId;
        this.resId = resId;
    }

    // Property accessors
    @Id
    @Column(nullable = false, length = 50)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false, length = 50)
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(nullable = false, length = 30)
    public String getResId() {
        return this.resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    @Column(length = 30)
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    @Column(length = 30)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}