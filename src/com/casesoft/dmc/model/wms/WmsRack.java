package com.casesoft.dmc.model.wms;

import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 货架
 * */
@Entity
@Table(name="wms_rack")
public class WmsRack extends BaseFloor implements Serializable{
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

    @Column(name = "parentId", nullable = false, length = 60)
	private String parentId;
	@Transient
	private String parentName;
	@Column(name = "sales", nullable = false)
	private Boolean sales; //是否可销售
	@Column(name = "skuQty")
	private Integer skuQty;

    public WmsRack() {
        super();
    }

    public Boolean getSales() {
		return sales;
	}
	public void setSales(Boolean sales) {
		this.sales = sales;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSkuQty() {
		return skuQty;
	}

	public void setSkuQty(Integer skuQty) {
		this.skuQty = skuQty;
	}

	@Transient
	private List<WmsPlRackBindingRelation> relations;

	@Transient
	private String floorAreaBarcode;
	@Transient
	private String floorAreaName;
	@Transient
	private String warehouseCode;
	@Transient
	private String warehouseName;
	@Transient
	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	@Transient
	public String getFloorAreaBarcode() {
		return floorAreaBarcode;
	}

	public void setFloorAreaBarcode(String floorAreaBarcode) {
		this.floorAreaBarcode = floorAreaBarcode;
	}
	@Transient
	public String getFloorAreaName() {
		return floorAreaName;
	}

	public void setFloorAreaName(String floorAreaName) {
		this.floorAreaName = floorAreaName;
	}
	@Transient
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	@Transient
	public List<WmsPlRackBindingRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<WmsPlRackBindingRelation> relations) {
		this.relations = relations;
	}

	@Transient
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
