package com.casesoft.dmc.model.rem;

import com.casesoft.dmc.model.logistics.BaseBill;

import java.io.Serializable;
import javax.persistence.*;

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
    private String newRmId;//新入库库位id

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


    public String getNewRmId() {
        return newRmId;
    }

    public void setNewRmId(String newRmId) {
        this.newRmId = newRmId;
    }
}
