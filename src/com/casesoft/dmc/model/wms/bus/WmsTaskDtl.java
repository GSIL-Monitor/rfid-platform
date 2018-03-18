package com.casesoft.dmc.model.wms.bus;

import javax.persistence.*;
import java.io.Serializable;

//@Table(name="wms_tskdtl")
//@Entity
public class WmsTaskDtl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6442458557709816763L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_WMSTaskDtl")
	@SequenceGenerator(name = "S_WMSTaskDtl", allocationSize = 1, initialValue = 1, sequenceName = "S_WMSTaskDtl")
	private String id;
	@Column(name = "wmstskcd", nullable = false, length = 50)
	private String wmsTaskCode;
 	@Column(name = "sku",nullable = false, length = 50)
	private String sku;
	@Column(name="bd",length=32)
	private String barcode;
	@Column(name = "stn", nullable = false, length = 20)
	private String styleId;
	@Column(name = "con", nullable = false, length = 10)
	private String colorId;
	@Column(name = "sin", nullable = false, length = 10)
	private String sizeId;
	@Column(name = "qty")
	private long qty;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWmsTaskCode() {
		return wmsTaskCode;
	}
	public void setWmsTaskCode(String wmsTaskCode) {
		this.wmsTaskCode = wmsTaskCode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public String getSizeId() {
		return sizeId;
	}
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	
	

}
