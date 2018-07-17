package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/1/4.
 */
@Entity
@Table(name = "PRODUCT_NOPUSH_STYLE")
public class NoPushStyle extends BaseStyle{
    private String id;

    private String styleEname;
    private Integer seqNo;

    private String remark;
    private String isUse;
    @Column()
    private String ispush;
    @Column()
    private String pushsuccess;
    @Column()
    private String erroMessage;

    public String getPushsuccess() {
        return pushsuccess;
    }

    public void setPushsuccess(String pushsuccess) {
        this.pushsuccess = pushsuccess;
    }

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    // Constructors

    /**
     * default constructor
     */


    // Property accessors
    @Id
    @Column(nullable = false, length = 20)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column()
    public Integer getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }


    @Column(length = 400)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(length = 100)
    public String getStyleEname() {
        return styleEname;
    }

    public void setStyleEname(String styleEname) {
        this.styleEname = styleEname;
    }

    @Column(length = 2)
    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getErroMessage() {
        return erroMessage;
    }

    public void setErroMessage(String erroMessage) {
        this.erroMessage = erroMessage;
    }
}
