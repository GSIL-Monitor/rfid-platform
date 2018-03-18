package com.casesoft.dmc.model.wms.pl;

import com.casesoft.dmc.model.wms.BaseFloor;
import com.casesoft.dmc.model.wms.WmsFloor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Entity
@Table(name = "pl_Shop_wms_FloorArea")
public class PlShopWmsFloorArea extends BaseFloor implements Serializable {
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

    @Column(name = "skuQty")
    private Integer skuQty;

    public PlShopWmsFloorArea() {
        super();
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
