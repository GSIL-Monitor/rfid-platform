package com.casesoft.dmc.model.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRODUCT_STYLECOMBINE")
public class StyleCombine {
	private String id;
	private String styleId;
	private String colorId;
	private String combineStyleId;
	private String combineColorId;
	private Double price;
	public StyleCombine() {
		super();
	}
	public StyleCombine(String styleId, String colorId) {
		super();
		this.styleId = styleId;
		this.colorId = colorId;
	}
	@Id
	@Column(nullable = false, length = 45)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(nullable = false, length = 20)
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	@Column(nullable = false, length = 10)
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	@Column(nullable = false, length = 20)
	public String getCombineStyleId() {
		return combineStyleId;
	}
	public void setCombineStyleId(String combineStyleId) {
		this.combineStyleId = combineStyleId;
	}
	@Column(nullable = false, length = 10)
	public String getCombineColorId() {
		return combineColorId;
	}
	public void setCombineColorId(String combineColorId) {
		this.combineColorId = combineColorId;
	}
	private String styleName;
	private String colorName;
	private String combineStyleName;
	private String combineColorName;
	private List<String> images;
	
	@Transient
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	@Transient
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	@Transient
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	@Transient
	public String getCombineStyleName() {
		return combineStyleName;
	}
	public void setCombineStyleName(String combineStyleName) {
		this.combineStyleName = combineStyleName;
	}
	@Transient
	public String getCombineColorName() {
		return combineColorName;
	}
	
	public void setCombineColorName(String combineColorName) {
		this.combineColorName = combineColorName;
	}
	@Transient
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}
