package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column()
    private String nowclass9;
    @Column()
    private String changeType;//CS(系列的转换)，PC(价格的转换)

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
