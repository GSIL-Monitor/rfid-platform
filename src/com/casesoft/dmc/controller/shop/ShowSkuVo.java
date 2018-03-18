package com.casesoft.dmc.controller.shop;

import java.util.Date;

/**
 * Created by WingLi on 2015-03-03.
 */
public class ShowSkuVo implements java.io.Serializable {

    private Date showTime;

    private String ownerId;
    private String unitName;
    private String styleId;
    private String colorId;
    private String sizeId;

    private Double price;
    private String styleName;
    private String colorName;
    private String sizeName;
    private int year;
    private int month;
    private int day;
    private String name;//返回echart所需字段
    private Double longitude;
	private Double latitude;
	private String province;
    private long qty; //次数

    public ShowSkuVo() {
    }
    public ShowSkuVo(String name,long qty){
    	this.name = name;
    	this.qty = qty;
    } 
    public ShowSkuVo(String name,long qty,double longitude,double latitude,String province){
    	this.name = name;
    	this.qty = qty;
    	this.longitude = longitude;
    	this.latitude = latitude;
    	this.province = province;
    	
    } 

    public ShowSkuVo(String ownerId, String styleId, String colorId, String sizeId, long qty) {
        this.ownerId = ownerId;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }
    public ShowSkuVo(String styleId, String colorId, String sizeId, long qty) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }
    public ShowSkuVo(String styleId, long qty,String name) {
        this.styleId = styleId;        
        this.qty = qty;
        this.name = name;
    }
    public ShowSkuVo(String styleId,int year,int month,int day, long qty){
    	this.styleId = styleId;
    	this.year = year;
    	this.month = month;
    	this.day = day;
    	this.qty = qty;
    }
    public ShowSkuVo(int year,int month,int day, long qty){    	
    	this.year = year;
    	this.month = month;
    	this.day = day;
    	this.qty = qty;
    }
    public ShowSkuVo(Date showTime, String ownerId, String styleId, String colorId, String sizeId, long qty) {
        this.showTime = showTime;
        this.ownerId = ownerId;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.qty = qty;
    }

    public ShowSkuVo(Date showTime, String ownerId, String unitName, String styleId, String colorId, String sizeId, Double price, String styleName, String colorName, String sizeName, long qty) {
        this.showTime = showTime;
        this.ownerId = ownerId;
        this.unitName = unitName;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.price = price;
        this.styleName = styleName;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.qty = qty;
    }

    public Date getshowTime() {
        return showTime;
    }

    public void setshowTime(Date showTime) {
        this.showTime = showTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
}
