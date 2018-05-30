package com.casesoft.dmc.model.cfg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by yushen on 2018/5/16.
 */
@Entity
@Table(name = "CFG_MultiLevelRelation")
public class MultiLevelRelation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String multiLevelType;
    @Column
    private String parentId;
    @Column
    private String relatedId;
    @Column
    private String treePath;
    @Column
    private Integer treeSeqNo;
    @Column
    private Boolean isRoot;
    @Column
    private Boolean archive;
    @Column
    private Boolean openedState; //jstree属性字段，该结点是否打开
    @Column
    private Integer depth;
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

    public String getMultiLevelType() {
        return multiLevelType;
    }

    public void setMultiLevelType(String multiLevelType) {
        this.multiLevelType = multiLevelType;
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

    public Integer getTreeSeqNo() {
        return treeSeqNo;
    }

    public void setTreeSeqNo(Integer treeSeqNo) {
        this.treeSeqNo = treeSeqNo;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Boolean getOpenedState() {
        return openedState;
    }

    public void setOpenedState(Boolean openedState) {
        this.openedState = openedState;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
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
