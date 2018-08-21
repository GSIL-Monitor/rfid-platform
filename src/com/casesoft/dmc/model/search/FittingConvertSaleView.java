package com.casesoft.dmc.model.search;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SEARCH_FITTINGCONVERTSALEVIEW")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class FittingConvertSaleView implements Serializable {
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1830107307189342088L;
	/**
	 * 
	 */
	@Id
	@Column(nullable = false,length=80) 
	private String id;
	@Column(nullable = false, length = 20)
	private String styleId;
	@Column(length = 50)
	private String warehId;
	@Column(length = 30)
	private String brandCode;
	@Column(length = 10)
	private String scanDay;
	@Column()
	private double totPrice;
	//@Column()
	@Transient
	private long qty;
	@Column()
	private long fittingQty;
	@Column()
	private long saleQty;
	@Transient
	private String scale;
	@Column(length=100)
	private String styleName;
	@Transient
	private String warehName;
	@Transient
	private String _parentId;
	@Transient
	public String get_parentId() {
		return _parentId;
	}
	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}
	@Transient
	public String getWarehName() {
		return warehName;
	}
	public void setWarehName(String warehName) {
		this.warehName = warehName;
	}
	@Transient
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public double getTotPrice() {
		return totPrice;
	}
	public void setTotPrice(double totPrice) {
		this.totPrice = totPrice;
	}
	public String getStyleId() {
		return styleId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getWarehId() {
		return warehId;
	}
	public void setWarehId(String warehId) {
		this.warehId = warehId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getScanDay() {
		return scanDay;
	}
	public void setScanDay(String scanDay) {
		this.scanDay = scanDay;
	}
	@Transient
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public long getFittingQty() {
		return fittingQty;
	}
	public void setFittingQty(long fittingQty) {
		this.fittingQty = fittingQty;
	}
	public long getSaleQty() {
		return saleQty;
	}
	public void setSaleQty(long saleQty) {
		this.saleQty = saleQty;
	}
	

}
