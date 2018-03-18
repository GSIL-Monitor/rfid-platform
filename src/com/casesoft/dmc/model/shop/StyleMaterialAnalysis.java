package com.casesoft.dmc.model.shop;

import java.io.Serializable;

public class StyleMaterialAnalysis implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String styleId;
	private String styleName;
	private String materiaName;
	private Long buyCount;
	
	public StyleMaterialAnalysis(){}
	
	public StyleMaterialAnalysis(String styleId,String styleName,String materiaName,Long buyCount) {
		this.styleId = styleId;
		this.styleName = styleName;
		this.materiaName = materiaName;
		this.buyCount = buyCount;
	}

	public StyleMaterialAnalysis(String materiaName,Long buyCount) {
		this.materiaName = materiaName;
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

	public String getMateriaName() {
		return materiaName;
	}

	public void setMateriaName(String materiaName) {
		this.materiaName = materiaName;
	}

	public Long getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}


	
	
	
}