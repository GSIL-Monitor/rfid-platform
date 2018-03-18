package com.casesoft.dmc.model.shop;

import java.io.Serializable;

public class StyleSubClassAnalysis implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String styleId;
	private String styleName;
	private String subName;
	private Long buyCount;
	
	public StyleSubClassAnalysis(){}
	
	public StyleSubClassAnalysis(String styleId,String styleName,String subName,Long buyCount) {
		this.styleId = styleId;
		this.styleName = styleName;
		this.setSubName(subName);
		this.buyCount = buyCount;
	}

	public StyleSubClassAnalysis(String subName,Long buyCount) {
		this.subName = subName;
		this.buyCount = buyCount;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	

	public Long getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}


	
	
	
}