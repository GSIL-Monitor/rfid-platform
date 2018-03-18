package com.casesoft.dmc.model.wms.info;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 库位操作记录（预留）
 * */

/*@Entity
@Table(name = "wms_binding_record")*/
public class WmsFloorRecord implements Serializable{
	 
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -3337970359562950734L;
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	@Column(name = "operator",  nullable = false, length = 20)
	private String operator;
	@Column(name = "scanDate",  nullable = false)
	private Date scanDate;
	@Column(name = "deviceId", length = 20)
	private String deviceId;
	@Column(name = "warehouse_id", nullable = false, length = 50)
	private String warehouseId;//店仓编号
	@Column(name = "floorAreaCode", length = 20)
	private String floorAreaCode;// 区
	@Column(name = "floorcode", length = 20)
	private String floorCode;// 库位
	@Column(name = "floorRackCode", length = 20)
	private String floorRackCode;// 货架
	@Column(name = "code", length = 50)
	private String code;
	@Column(name = "sku", nullable = false, length = 50)
	private String sku;
	@Column(name = "barcode", length = 32)
	private String barcode;// 条码
	@Column(name = "styleId", nullable = false, length = 50)
	private String styleId;
	@Column(name = "colorId", nullable = false, length = 20)
	private String colorId;
	@Column(name = "sizeId", nullable = false, length = 20)
	private String sizeId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getFloorAreaCode() {
		return floorAreaCode;
	}
	public void setFloorAreaCode(String floorAreaCode) {
		this.floorAreaCode = floorAreaCode;
	}
	public String getFloorCode() {
		return floorCode;
	}
	public void setFloorCode(String floorCode) {
		this.floorCode = floorCode;
	}
	public String getFloorRackCode() {
		return floorRackCode;
	}
	public void setFloorRackCode(String floorRackCode) {
		this.floorRackCode = floorRackCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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

	
}
