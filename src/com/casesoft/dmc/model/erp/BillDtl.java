package com.casesoft.dmc.model.erp;


import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author WinstonLi
 */
@Entity
@Table(name = "ERP_BILLDTL")
public class BillDtl implements Serializable {

	private static final long serialVersionUID = 5901956985596784358L;

	private String id;
	private String billId;
	private String billNo;
	@Excel(name = "SKU", width = 20D)
	private String sku;
	@Excel(name = "款号", width = 20D)
	private String styleId;
	@Excel(name = "款名")
	private String styleName;
	@Excel(name = "颜色")
	private String colorId;
	@Excel(name = "尺寸")
	private String sizeId;
	private String unitId;// 仓库id
	@Excel(name = "库存数量")
	private Long qty;
	@Excel(name = "实际数量")
	private Long actQty;
	@Excel(name = "差异数量")
	private Long diffQty;
	private String unitName;// 仓库
	// 电商单据增加字段
	private String remark;
	private int type;
	private String colorName;
	private String sizeName;

	private String image;
	@Excel(name = "吊牌价")
	private double price;
	private double discount;
	private double prePrice;
	private Long initQty=0l;
	private Long scanQty=0l;
	private Long manualQty=0l;//本次

	private Long preManualQty=0l;//累计

	@Column(length = 20)
	private String barcode;


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Column(name = "preManualQty")
	public Long getPreManualQty() {
		return preManualQty;
	}

	public void setPreManualQty(Long preManualQty) {
		this.preManualQty = preManualQty;
	}

	@Column(name = "manualQty")
	public Long getManualQty() {
		return manualQty;
	}

	public void setManualQty(Long manualQty) {
		this.manualQty = manualQty;
	}

	@Column(name = "scanQty")
	public Long getScanQty() {
		return scanQty;
	}

	public void setScanQty(Long scanQty) {
		this.scanQty = scanQty;
	}

	@Column(name = "initQty")
	public Long getInitQty() {
		return initQty;
	}

	public void setInitQty(Long initQty) {
		this.initQty = initQty;
	}
	private List<String> codeList;
	@Transient
	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}



	@Transient
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public BillDtl() {
		super();
	}



	// @Column(name = "type", nullable = false)
	@Transient
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Id
	@Column(nullable = false, length = 55)
	public String getId() {
		return id;
	}

	public BillDtl(String unitId, String sku, String styleId, String colorId,
			String sizeId, Long actQty) {
		super();
		this.unitId = unitId;
		this.sku = sku;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.actQty = actQty;
	}
	public BillDtl(String unitId, String sku, String styleId, String colorId,
				   String sizeId, int actQty) {
		super();
		this.unitId = unitId;
		this.sku = sku;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.actQty =(long) actQty;
	}
	public BillDtl(String sku, String styleId, String colorId, String sizeId,
			Long actQty) {
		super();
		this.sku = sku;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.actQty = actQty;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false, length = 50)
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(nullable = false, length = 50)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(nullable = false, length = 50)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(nullable = false, length = 20)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(nullable = false, length = 20)
	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Column(nullable = false, length = 10)
	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	@Column(nullable = false)
	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	@Column()
	public Long getActQty() {
		return actQty;
	}

	public void setActQty(Long actQty) {
		this.actQty = actQty;
	}

	@Column(length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// //===================>

	
	@Transient
	public double getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(double prePrice) {
		this.prePrice = prePrice;
	}

	@Transient
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	@Transient
	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	@Transient
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	@Transient
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	@Transient
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Transient
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Transient
	public Long getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(Long diffQty) {
		this.diffQty = diffQty;
	}
}
