package com.casesoft.dmc.controller.search.temp;

public class SaleRanking {
	private String sku;
	private String warehId;
	private String styleId;
	private long totQty;
	private double totPrice;
	private double totAllPrice;

	private int stQty;
	private String warehName;
	private String styleName;
	
	public double getTotAllPrice() {
		return totAllPrice;
	}
	public void setTotAllPrice(double totAllPrice) {
		this.totAllPrice = totAllPrice;
	}
	private String proportion;
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getWarehName() {
		return warehName;
	}
	public void setWarehName(String warehName) {
		this.warehName = warehName;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public SaleRanking(String warehId, long totQty, double totPrice,double totAllPrice) {
		super();
		this.warehId = warehId;
		this.totQty = totQty;
		this.totPrice = totPrice;
		this.totAllPrice=totAllPrice;
	}
	public SaleRanking(String styleId, long totQty, double totPrice,String proportion) {
		super();
		this.styleId = styleId;
		this.totQty = totQty;
		this.totPrice = totPrice;
		this.proportion=proportion;
	}
	public SaleRanking(String styleId, long totQty, double totPrice) {
		super();
		this.styleId = styleId;
		this.totQty = totQty;
		this.totPrice = totPrice;
	}
	public SaleRanking(String styleId, long totQty, double totPrice,
			int stQty) {
		super();
		this.styleId = styleId;
		this.totQty = totQty;
		this.totPrice = totPrice;
		this.stQty = stQty;
	}
 
 	public SaleRanking(String warehId, String styleId, long totQty,
			double totPrice, int stQty) {
		super();
		this.warehId = warehId;
		this.styleId = styleId;
		this.totQty = totQty;
		this.totPrice = totPrice;
		this.stQty = stQty;
	}
	public SaleRanking(String warehId, String styleId, long totQty,
			double totPrice) {
		super();
		this.warehId = warehId;
		this.styleId = styleId;
		this.totQty = totQty;
		this.totPrice = totPrice;
	}
	
/*	public SaleRanking(String warehId,String sku,  long totQty,
			double totPrice, int stQty) {
		super();
		this.sku = sku;
		this.warehId = warehId;
		this.totQty = totQty;
		this.totPrice = totPrice;
		this.stQty = stQty;
	}*/

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getWarehId() {
		return warehId;
	}
	public void setWarehId(String warehId) {
		this.warehId = warehId;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public long getTotQty() {
		return totQty;
	}
	public void setTotQty(long totQty) {
		this.totQty = totQty;
	}
	public double getTotPrice() {
		return totPrice;
	}
	public void setTotPrice(double totPrice) {
		this.totPrice = totPrice;
	}
	public int getStQty() {
		return stQty;
	}
	public void setStQty(int stQty) {
		this.stQty = stQty;
	}
	
}
