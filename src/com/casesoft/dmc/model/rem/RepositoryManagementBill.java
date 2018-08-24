package com.casesoft.dmc.model.rem;

import com.casesoft.dmc.model.logistics.BaseBill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by lly on 2018/7/20.
 */
@Entity
@Table(name = "REM_RepositoryAdjustBill")
public class RepositoryManagementBill extends BaseBill implements Serializable{
    private static final long serialVersionUID = 7925744518394823164L;

    @Id
    @Column
    private String id;
    @Column
    private String AdjustCount;//调整数量
    @Column
    private String NrackId;//新货架
    @Column
    private String NlevelId;//新货层
    @Column
    private String NallocationId;//新货位

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdjustCount() {
        return AdjustCount;
    }

    public void setAdjustCount(String adjustCount) {
        AdjustCount = adjustCount;
    }

    public String getNrackId() {
        return NrackId;
    }

    public void setNrackId(String nrackId) {
        NrackId = nrackId;
    }

    public String getNlevelId() {
        return NlevelId;
    }

    public void setNlevelId(String nlevelId) {
        NlevelId = nlevelId;
    }

    public String getNallocationId() {
        return NallocationId;
    }

    public void setNallocationId(String nallocationId) {
        NallocationId = nallocationId;
    }
}
