package com.casesoft.dmc.model.logistics;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/5/9.
 */
@Entity
@Table(name = "LOGISTICS_LabelChangeBill")
public class LabelChangeBill extends BaseBill{
    @Id
    @Column()
    private String id;
    @Column()
    private String beforeclass9;//
    @Transient
    private String beforeclass9Name;
    @Column()
    private String nowclass9;
    @Transient
    private String nowclass9Name;
    @Column()
    private String changeType;//CS(系列的转换)，PC(价格的转换)
    @Transient
    private String changeTypeName;


    public String getBeforeclass9Name() {
        return beforeclass9Name;
    }

    public void setBeforeclass9Name(String beforeclass9Name) {
        this.beforeclass9Name = beforeclass9Name;
    }

    public String getNowclass9Name() {
        return nowclass9Name;
    }

    public void setNowclass9Name(String nowclass9Name) {
        this.nowclass9Name = nowclass9Name;
    }

    public String getChangeTypeName() {
        return changeTypeName;
    }

    public void setChangeTypeName(String changeTypeName) {
        this.changeTypeName = changeTypeName;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeforeclass9() {
        return beforeclass9;
    }

    public void setBeforeclass9(String beforeclass9) {
        this.beforeclass9 = beforeclass9;
    }

    public String getNowclass9() {
        return nowclass9;
    }

    public void setNowclass9(String nowclass9) {
        this.nowclass9 = nowclass9;
    }
}
