package com.casesoft.dmc.controller.search.temp;

public class SaleAndFittingStatistics {

	private String scanDay;
	private String sku;
	private String styleId;
	private String sizeId;
	private String colorId;
	private long qty;
	private double totPrice;
	private String warehId;
	private String warehName;
	private String styleName;
	private String sizeName;
	private String colorName;
	
    //销售
	public SaleAndFittingStatistics(String warehId,String scanDay,
			String sku, String styleId, String sizeId,
			String colorId, long qty, double totPrice ) {
		super();
		this.sku = sku;
		this.styleId = styleId;
		this.sizeId = sizeId;
		this.colorId = colorId;
		this.qty = qty;
		this.totPrice = totPrice;
		this.warehId = warehId;
	}

	//试衣
	public SaleAndFittingStatistics(String warehId,String scanDay, String sku, String styleId,
			String sizeId, String colorId, long qty) {
		super();
		this.scanDay = scanDay;
		this.sku = sku;
		this.styleId = styleId;
		this.sizeId = sizeId;
		this.colorId = colorId;
		this.qty = qty;
		this.warehId = warehId;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getWarehId() {
		return warehId;
	}

	public void setWarehId(String warehId) {
		this.warehId = warehId;
	}

	public String getWarehName() {
		return warehName;
	}

	public void setWarehName(String warehName) {
		this.warehName = warehName;
	}

	public String getScanDay() {
		return scanDay;
	}
	public void setScanDay(String scanDay) {
		this.scanDay = scanDay;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getSizeId() {
		return sizeId;
	}
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public double getTotPrice() {
		return totPrice;
	}
	public void setTotPrice(double totPrice) {
		this.totPrice = totPrice;
	}
	
}
