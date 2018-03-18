package com.casesoft.dmc.model.chart;



public class StockStatistics implements java.io.Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String storageId;
	private String storageName;
	private Integer storageType;	
	private Long inQty;
	private Double inValue;
	private Long outQty;
	private Double outValue;
	private Long inventoryQty;
	private Double inventoryValue;
	private Long stockQty;
	private Double stockValue;
	public StockStatistics(){}
	
	public StockStatistics(String storageId, Integer storageType, Long inQty,
			Double inValue, Long outQty, Double outValue, Long inventoryQty,
			Double inventoryValue, Long stockQty, Double stockValue) {
		super();
		this.storageId = storageId;
		this.storageType = storageType;
		this.inQty = inQty;
		this.inValue = inValue;
		this.outQty = outQty;
		this.outValue = outValue;
		this.inventoryQty = inventoryQty;
		this.inventoryValue = inventoryValue;
		this.stockQty = stockQty;
		this.stockValue = stockValue;
	}
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}	
	public Long getInQty() {
		return inQty;
	}
	public void setInQty(Long inQty) {
		this.inQty = inQty;
	}
	public Long getOutQty() {
		return outQty;
	}
	public void setOutQty(Long outQty) {
		this.outQty = outQty;
	}
	public Long getInventoryQty() {
		return inventoryQty;
	}
	public void setInventoryQty(Long inventoryQty) {
		this.inventoryQty = inventoryQty;
	}
	public Long getStockQty() {
		return stockQty;
	}
	public void setStockQty(Long stockQty) {
		this.stockQty = stockQty;
	}
	public Double getInValue() {
		return inValue;
	}
	public void setInValue(Double inValue) {
		this.inValue = inValue;
	}
	public Double getOutValue() {
		return outValue;
	}
	public void setOutValue(Double outValue) {
		this.outValue = outValue;
	}
	public Double getInventoryValue() {
		return inventoryValue;
	}
	public void setInventoryValue(Double inventoryValue) {
		this.inventoryValue = inventoryValue;
	}
	public Double getStockValue() {
		return stockValue;
	}
	public void setStockValue(Double stockValue) {
		this.stockValue = stockValue;
	}
	
	

}
