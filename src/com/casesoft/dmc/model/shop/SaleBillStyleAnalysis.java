package com.casesoft.dmc.model.shop;

import java.io.Serializable;

public class SaleBillStyleAnalysis implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String styleId;
	private String styleName;
	private String class4;
	private String class4Name;
	private Long class4BuyCount;
	private String class8;
	private String class8Name;
	private Long class8BuyCount;
	private String class10;
	private Long clas10BuyCount;
	private Double totalPrice;
	private Long buyCount;
	
	
	public SaleBillStyleAnalysis(){}
	public SaleBillStyleAnalysis(String styleId,String styleName,String class4,Long class4BuyCount,String class8,
			                     Long class8BuyCount,String class10,Long clas10BuyCount,Double totalPrice,Long buyCount){
		this.styleId = styleId;
		this.styleName = styleName;
		this.class4 = class4;
		this.class4BuyCount = class4BuyCount;
		this.class8 = class8;
		this.class8BuyCount = class8BuyCount;
		this.class10 = class10;
		this.clas10BuyCount = clas10BuyCount;
		this.totalPrice = totalPrice;
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

	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}

	public String getClass4Name() {
		return class4Name;
	}

	public void setClass4Name(String class4Name) {
		this.class4Name = class4Name;
	}

	public String getClass8() {
		return class8;
	}

	public void setClass8(String class8) {
		this.class8 = class8;
	}

	public String getClass8Name() {
		return class8Name;
	}

	public void setClass8Name(String class8Name) {
		this.class8Name = class8Name;
	}
	public Long getClass4BuyCount() {
		return class4BuyCount;
	}
	public void setClass4BuyCount(Long class4BuyCount) {
		this.class4BuyCount = class4BuyCount;
	}
	public Long getClass8BuyCount() {
		return class8BuyCount;
	}
	public void setClass8BuyCount(Long class8BuyCount) {
		this.class8BuyCount = class8BuyCount;
	}
	public String getClass10() {
		return class10;
	}
	public void setClass10(String class10) {
		this.class10 = class10;
	}
	public Long getClas10BuyCount() {
		return clas10BuyCount;
	}
	public void setClas10BuyCount(Long clas10BuyCount) {
		this.clas10BuyCount = clas10BuyCount;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
    
	public Long getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Long buyCount) {
		this.buyCount = buyCount;
	}
	
	
	
	
}