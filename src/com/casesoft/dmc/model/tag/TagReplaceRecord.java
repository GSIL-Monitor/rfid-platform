package com.casesoft.dmc.model.tag;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yushen on 2017/11/6.
 */

@Entity
@Table(name="TAG_TAGREPLACERECORD")
public class TagReplaceRecord {
    @Id
    @Column()
    private String id;      //随机数
    @Column
    private Integer status; //记录替换状态，1表示替换成功，0表示替换失败
    @Column
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date recordDate;
    @Column
    private String origCode; //原唯一码
    @Column
    private String origSku;
    @Column
    private String origStyleId;
    @Column
    private String origColorId;
    @Column
    private String origSizeId;
    @Column
    private String newCode;  //新唯一码
    @Column
    private String newSku;
    @Column
    private String newStyleId;
    @Column
    private String newColorId;
    @Column
    private String newSizeId;
    @Column
    private String remark;
    @Column
    private String oprId;  //操作人ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getOrigCode() {
        return origCode;
    }

    public void setOrigCode(String origCode) {
        this.origCode = origCode;
    }

    public String getOrigSku() {
        return origSku;
    }

    public void setOrigSku(String origSku) {
        this.origSku = origSku;
    }

    public String getOrigStyleId() {
        return origStyleId;
    }

    public void setOrigStyleId(String origStyleId) {
        this.origStyleId = origStyleId;
    }

    public String getOrigColorId() {
        return origColorId;
    }

    public void setOrigColorId(String origColorId) {
        this.origColorId = origColorId;
    }

    public String getOrigSizeId() {
        return origSizeId;
    }

    public void setOrigSizeId(String origSizeId) {
        this.origSizeId = origSizeId;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getNewSku() {
        return newSku;
    }

    public void setNewSku(String newSku) {
        this.newSku = newSku;
    }

    public String getNewStyleId() {
        return newStyleId;
    }

    public void setNewStyleId(String newStyleId) {
        this.newStyleId = newStyleId;
    }

    public String getNewColorId() {
        return newColorId;
    }

    public void setNewColorId(String newColorId) {
        this.newColorId = newColorId;
    }

    public String getNewSizeId() {
        return newSizeId;
    }

    public void setNewSizeId(String newSizeId) {
        this.newSizeId = newSizeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }
}
