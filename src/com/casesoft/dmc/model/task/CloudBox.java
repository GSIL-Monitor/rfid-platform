package com.casesoft.dmc.model.task;

import javax.persistence.*;
import java.util.List;

/**
 * Created by WingLi on 2014/11/23.
 */
@Entity
@Table(name = "TASK_CLOUDBOX")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CloudBox extends Box{
    private List<CloudBoxDtl> cloudBoxDtlList;

    @Transient
    public List<CloudBoxDtl> getCloudBoxDtlList() {
        return cloudBoxDtlList;
    }

    public void setCloudBoxDtlList(List<CloudBoxDtl> cloudBoxDtlList) {
        this.cloudBoxDtlList = cloudBoxDtlList;
    }
}
