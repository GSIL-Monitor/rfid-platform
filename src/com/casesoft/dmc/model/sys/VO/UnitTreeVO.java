package com.casesoft.dmc.model.sys.VO;

import com.casesoft.dmc.model.sys.Unit;

import java.util.Date;

/**
 * Created by yushen on 2018/5/25.
 */
public class UnitTreeVO extends Unit{
    private String multiLevelType;
    private String parentId;
    private String relatedId;
    private String treePath;
    private Integer treeSeqNo;
    private Boolean isRoot;
    private Boolean archive;
    private Integer depth;

    public UnitTreeVO(){}

    public UnitTreeVO(String id, String code, String name, String ownerId,
                      String tel, String phone, String linkman, String email, Integer type,
                      String province, String city, String areaId, String address,
                      Date createTime, Date updateTime, String treePath, Integer depth){
        setId(id);
        setCode(code);
        setName(name);
        setOwnerId(ownerId);
        setTel(tel);
        setPhone(phone);
        setLinkman(linkman);
        setEmail(email);
        setType(type);
        setProvince(province);
        setCity(city);
        setAreaId(areaId);
        setAddress(address);
        setCreateTime(createTime);
        setUpdateTime(updateTime);
        this.treePath = treePath;
        this.depth = depth;
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

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }
}
