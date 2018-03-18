package com.casesoft.dmc.model.wms;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 库位
 */
@Entity
@Table(name = "wms_Floor")
public class WmsFloor extends BaseFloor implements Serializable {

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
    @Column(name = "sales", nullable = false)
    private Boolean sales; //是否可销售
    @Column(name = "skuQty")
    private Integer skuQty;
    @Transient
    private String parentName;

    public Boolean getSales() {
        return sales;
    }

    public void setSales(Boolean sales) {
        this.sales = sales;
    }

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
}
