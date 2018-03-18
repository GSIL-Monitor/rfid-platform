package com.casesoft.dmc.model.wms.pl;

import com.casesoft.dmc.extend.third.model.pl.PlWmsWarehouseBindingRelation;
import com.casesoft.dmc.model.wms.BaseFloor;
import com.casesoft.dmc.model.wms.WmsRack;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Entity
@Table(name = "pl_wms_Floor")
public class PlWmsFloor extends BaseFloor implements Serializable {

    /**
     *
     */
    @Id
    @Column(name = "id", length = 32)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "parentId", length = 60, nullable = false)
    private String parentId;
    @Column(name = "skuQty")
    private Integer skuQty;
    @Transient
    private String parentName;

    public Integer getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Integer skuQty) {
        this.skuQty = skuQty;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Transient
    private List<WmsRack> racks = new ArrayList<>();

    @Transient
    public List<WmsRack> getRacks() {
        return racks;
    }

    public void setRacks(List<WmsRack> racks) {
        this.racks = racks;
    }

    @Transient
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    @Transient
    List<PlWmsWarehouseBindingRelation> relations;

    public List<PlWmsWarehouseBindingRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<PlWmsWarehouseBindingRelation> relations) {
        this.relations = relations;
    }
}
