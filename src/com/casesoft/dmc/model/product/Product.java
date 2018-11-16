package com.casesoft.dmc.model.product;

import javax.persistence.*;
import java.util.List;

/**
 * Sku entity. @author
 */
@Entity
@Table(name = "PRODUCT_PRODUCT")
public class Product extends BaseProduct implements java.io.Serializable {

	private static final long serialVersionUID = 8283433680366628900L;

	private String id;
	private String code;
	private String styleId;	
	private String colorId;
	private String sizeId;
	private String remark;
	private String isSample;

	private String sizeSortId;
	private String image;
	
	private Long version;//版本号

	private Integer boxQty;//规格
	
	private List<Product> collocation;
	private List<String> images;
	private int isDeton;
	private String isUse;
	@Column()
	private String push;

	@Column
	private Integer styleCycle; //款销售退货周期，默认20天

	public Integer getStyleCycle() { return styleCycle; }

	public void setStyleCycle(Integer styleCycle) { this.styleCycle = styleCycle; }

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}
	// Constructors


	@Column()
	public Integer getBoxQty() {
		return boxQty;
	}

	public void setBoxQty(Integer boxQty) {
		this.boxQty = boxQty;
	}

	@Column()
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/** default constructor */
	public Product() {
	}

	/** minimal constructor */
	public Product(String code, String styleId, String colorId, String sizeId) {
		this.code = code;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
	}

	/** full constructor */
	public Product(String code, String styleId, String colorId, String sizeId,
			String remark) {
		this.code = code;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.remark = remark;
	}
	public Product(String id,String code,String styleId, String colorId, String sizeId,int isDeton,
				   String remark,String styleName,String class1,String class2,String class3,
				   String class4,String class5,String class6,String class7,String class8,String class9,
				   String class10,String styleRemark,Double preCast,Double puPrice,Double wsPrice,
				   Double price,String sizeName){
		this.id = id;
		this.code =code;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.remark =remark;
		this.styleName = styleName;
		this.class1 = class1;
		this.class2 = class2;
		this.class3 = class3;
		this.class4 = class4;
		this.class5 = class5;
		this.class6 = class6;
		this.class7 = class7;
		this.class8 = class8;
		this.class9 = class9;
		this.class10 = class10;
		this.styleRemark = styleRemark;
		this.preCast = preCast;
		this.puPrice = puPrice;
		this.wsPrice = wsPrice;
		this.price = price;
		this.sizeName = sizeName;

	}
    @Column(length = 50)
	public String getSizeSortId() {
		return sizeSortId;
	}

	public void setSizeSortId(String sizeSortId) {
		this.sizeSortId = sizeSortId;
	}

	// Property accessors
	@Id
	@Column(nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false, unique = true, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 20)
	public String getStyleId() {
		return this.styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(nullable = false, length = 15)
	public String getColorId() {
		return this.colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Column(nullable = false, length = 10)
	public String getSizeId() {
		return this.sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	@Column(length = 400)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 500)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(length=1)
	public String getIsSample() {
		return isSample;
	}

	public void setIsSample(String isSample) {
		this.isSample = isSample;
	}

	@Transient
	public List<Product> getCollocation() {
		return collocation;
	}

	public void setCollocation(List<Product> collocation) {
		this.collocation = collocation;
	}

	
	@Transient
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	@Column()
	public int getIsDeton() {
		return isDeton;
	}

	public void setIsDeton(int isDeton) {
		this.isDeton = isDeton;
	}

	@Column(length = 2)
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((collocation == null) ? 0 : collocation.hashCode());
		result = prime * result + ((colorId == null) ? 0 : colorId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + isDeton;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((sizeId == null) ? 0 : sizeId.hashCode());
		result = prime * result + ((sizeSortId == null) ? 0 : sizeSortId.hashCode());
		result = prime * result + ((styleId == null) ? 0 : styleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (collocation == null) {
			if (other.collocation != null)
				return false;
		} else if (!collocation.equals(other.collocation))
			return false;
		if (colorId == null) {
			if (other.colorId != null)
				return false;
		} else if (!colorId.equals(other.colorId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (isDeton != other.isDeton)
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (sizeId == null) {
			if (other.sizeId != null)
				return false;
		} else if (!sizeId.equals(other.sizeId))
			return false;
		if (sizeSortId == null) {
			if (other.sizeSortId != null)
				return false;
		} else if (!sizeSortId.equals(other.sizeSortId))
			return false;
		if (styleId == null) {
			if (other.styleId != null)
				return false;
		} else if (!styleId.equals(other.styleId))
			return false;
		return true;
	}


}