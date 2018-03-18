package com.casesoft.dmc.model.wms;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区
 **/
@Entity
@Table(name = "wms_FloorArea")
public class WmsFloorArea extends BaseFloor implements Serializable {
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

    @Column(name = "parentCode", length = 60, nullable = false)
    private String parentCode;// 父类(店仓编号)
    @Transient
    private String parentName;
    @Column(name = "sales", nullable = false)
    private Boolean sales; //是否可销售
    @Column(name = "skuQty")
    private Integer skuQty;

    public WmsFloorArea() {
        super();
    }

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

    @Transient
    private List<WmsFloor> floors = new ArrayList<>();

    @Transient
    public List<WmsFloor> getFloors() {
        return floors;
    }

    public void setFloors(List<WmsFloor> floors) {
        this.floors = floors;
    }

    @Column(name = "parentCode", nullable = false, length = 45)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Transient
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


}
