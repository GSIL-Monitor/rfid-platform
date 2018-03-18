package com.casesoft.dmc.model.shop;

import java.io.Serializable;

public class SaleBillColorAnalysis implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String colorId;
	private String colorName;
	private Long buyCount;
	
	public SaleBillColorAnalysis(){}

	public SaleBillColorAnalysis(String colorId, String colorName, Long buyCount) {
		this.colorId = colorId;
		this.colorName = colorName;
		this.buyCount = buyCount;
	}
	
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public Long getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
}