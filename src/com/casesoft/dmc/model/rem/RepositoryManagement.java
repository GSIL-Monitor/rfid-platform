package com.casesoft.dmc.model.rem;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lly on 2018/7/7.
 */
@Entity
@Table(name = "REM_RepositoryManagement")
public class RepositoryManagement implements Serializable{

    private static final long serialVersionUID = 1L;
    /* @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")*/


    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String remLevelType;
    @Column
    private String parentId;
    @Column
    private String relatedId;
    @Column
    private String treePath;
    @Column
    private Boolean isRoot;
    @Column
    private String repath;//深度
    @Column
    private String img;  //jstree属性字段，预留
    @Column
    private String tinyImg;  //jstree属性字段，预留
    @Column
    private String fontColor;  //jstree属性字段，预留

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

    public String getRemLevelType() {
        return remLevelType;
    }

    public void setRemLevelType(String remLevelType) {
        this.remLevelType = remLevelType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public String getRepath() {
        return repath;
    }

    public void setRepath(String repath) {
        this.repath = repath;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTinyImg() {
        return tinyImg;
    }

    public void setTinyImg(String tinyImg) {
        this.tinyImg = tinyImg;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
