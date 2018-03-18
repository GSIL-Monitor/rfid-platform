package com.casesoft.dmc.model.shop;

import java.io.Serializable;

public class SaleBillSizeAnalysis implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sizeId;
	private String sizeName;
	private Long buyCount;
	
	public SaleBillSizeAnalysis(){}
	
	public SaleBillSizeAnalysis(String sizeId, String sizeName, Long buyCount) {
		this.sizeId = sizeId;
		this.sizeName = sizeName;
		this.buyCount = buyCount;
	}

	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Long getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}
	
	
	
}