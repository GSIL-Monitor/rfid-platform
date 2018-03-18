package com.casesoft.dmc.model.factory;

import javax.persistence.*;

/**
 * 成衣流程
 */
@Entity
@Table(name="Factory_Token")
public class Token implements java.io.Serializable{


    private static final long serialVersionUID = 1L;
    @Id
    private Integer token;
    private String name;
    private String types;

    private Integer locked;
    @Column(length=2)
    private String multiple;//Y允许多刷
    @Column(length=2)
    private String necessary;//Y必要流程

    private Integer lastToken;

    private Integer sortIndex;

    @Column(length=2)//是否是第一个流程 Y/N
    private String isFirst;

    @Column(length = 2)//是否是最后一个流程 Y/N
    private String isLast;

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

    public Integer getToken() {
        return token;
    }
    public void setToken(Integer token) {
        this.token = token;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTypes() {
        return types;
    }
    public void setTypes(String types) {
        this.types = types;
    }
    public Integer getLocked() {
        return locked;
    }
    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getNecessary() {
        return necessary;
    }

    public void setNecessary(String necessary) {
        this.necessary = necessary;
    }

    public Integer getLastToken() {
        return lastToken;
    }

    public void setLastToken(Integer lastToken) {
        this.lastToken = lastToken;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    @Transient
    private String categoryName;

    @Transient
    private String lastTokenName;

    public String getLastTokenName() {
        return lastTokenName;
    }

    public void setLastTokenName(String lastTokenName) {
        this.lastTokenName = lastTokenName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }
}
