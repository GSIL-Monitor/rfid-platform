package com.casesoft.dmc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lly on 2018/11/2.
 */
@Entity
@Table(name="Product_components")
public class Components {

    @Id
    @Column
    private String id ;
    @Column
    private String code;//成分id
    @Column
    private String name;//成分名称
    @Column
    private String rule;//成分约束条件（正则表达式）
    @Column
    private String parentId;//所属父类成分
    @Column
    private Integer deep;//所属层级，0：最上级
    @Column
    @JSONField(format="yyyy-MM-dd")
    private Date createDate;//创建时间
    @Column
    @JSONField(format="yyyy-MM-dd")
    private Date updateDate;//更新时间
    @Column
    private String createrId;//创建人
    @Column
    private String updaterId;//更新人

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

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }
}
