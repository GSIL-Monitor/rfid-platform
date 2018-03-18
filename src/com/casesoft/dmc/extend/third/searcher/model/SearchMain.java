package com.casesoft.dmc.extend.third.searcher.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017-04-17.
 * 请求找货主单
 */
@Entity
@Table(name = "third_search_main")
public class SearchMain implements Serializable {

    public static class Status {
        public static final String MAIN_STATUS_SEND = "1";//未处理
        public static final String MAIN_STATUS_SEARCHING = "2";//已读
        public static final String MAIN_STATUS_END = "3";//已执行
        public static final String MAIN_STATUS_PARTON = "4";//部分执行
        public static final String MAIN_STATUS_DESTROY = "-1";//作废

    }

    private String id;
    private String origCode;//所属门店
    private String toCode;//接收人编号
    private String toName;
    private Date updateDate;//状态更新日期
    private Date sendDate;//发送日期
    private String fromCode;//发送人编号
    private String fromName;//发送人姓名

    private Integer skuQty;//找货sku数量
    private Integer searchQty=0;//找到数量
    private Integer lostQty=0;//未找到
    private String remark;
    private String updateRemark;//更新备注
    private String type;//等级;1：普通，2：紧急
    private String status = Status.MAIN_STATUS_SEND;//状态

    @Column(name = "origCode", length = 55, nullable = false)
    public String getOrigCode() {
        return origCode;
    }

    public void setOrigCode(String origCode) {
        this.origCode = origCode;
    }

    @Column(name = "searchQty")
    public Integer getSearchQty() {
        return searchQty;
    }

    public void setSearchQty(Integer searchQty) {
        this.searchQty = searchQty;
    }

    @Column(name = "lostQty")
    public Integer getLostQty() {
        return lostQty;
    }

    public void setLostQty(Integer lostQty) {
        this.lostQty = lostQty;
    }

    @Id
    @Column(name = "id", length = 55)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "toCode", length = 55, nullable = false)
    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    @Column(name = "toName", length = 100)
    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sendDate", nullable = false)
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateDate")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "fromCode", length = 55, nullable = false)
    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    @Column(name = "fromName", length = 100)
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @Column(name = "status", nullable = false,length = 5)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "skuQty", nullable = false)
    public Integer getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Integer skuQty) {
        this.skuQty = skuQty;
    }

    @Column(name = "remark", length = 500)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "updateRemark", length = 500)
    public String getUpdateRemark() {
        return updateRemark;
    }

    public void setUpdateRemark(String updateRemark) {
        this.updateRemark = updateRemark;
    }

    @Column(name = "type", nullable = false,length = 5)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    private List<SearchDtl> searchDtlList;
     private String diffDate;
    @Column(name = "diffDate", length = 50)
    public String getDiffDate() {
        return diffDate;
    }

    public void setDiffDate(String diffDate) {
        this.diffDate = diffDate;
    }

    @Transient
    public List<SearchDtl> getSearchDtlList() {
        return searchDtlList;
    }

    public void setSearchDtlList(List<SearchDtl> searchDtlList) {
        this.searchDtlList = searchDtlList;
    }
}
