package com.casesoft.dmc.model.product;

import javax.persistence.*;
import java.util.List;

/**
 * Color entity. @author
 */
@Entity
@Table(name = "PRODUCT_SUITDTL")
public class SuitDtl implements java.io.Serializable {

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 7492860998809009053L;
 
	@Id
	@Column(nullable = false)
	private String id;
	
	@Column(nullable = false, length = 10)
	private String suitCode;
	
	@Column(nullable = false, length = 20)
	private String styleId;
	@Column(nullable = false, length = 20)
	private String colorId;

	public String getSuitCode() {
		return suitCode;
	}

	public void setSuitCode(String suitCode) {
		this.suitCode = suitCode;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Transient
	private String styleName;
	@Transient
	private String colorName;

	@Transient
	private Double price;
	@Transient
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	@Transient
	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	@Transient
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Transient
	List<String> images;

	@Transient
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
}