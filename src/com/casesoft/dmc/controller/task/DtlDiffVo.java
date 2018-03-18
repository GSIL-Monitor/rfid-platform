package com.casesoft.dmc.controller.task;

public class DtlDiffVo {

	private String styleId;
	private String colorId;
	private String sizeId;
	
	private String sku;
	
	private long qty1;
	private long qty2;
	
	private String styleName;
    private String colorName;
    private String sizeName;
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
	public long getQty1() {
		return qty1;
	}
	public void setQty1(long qty1) {
		this.qty1 = qty1;
	}
	public long getQty2() {
		return qty2;
	}
	public void setQty2(long qty2) {
		this.qty2 = qty2;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
    
    
}
