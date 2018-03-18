package com.casesoft.dmc.model.mirror;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.model.product.Product;

@Entity
@Table(name = "MIRROR_NEWPRODUCT")
public class NewProduct implements Serializable {

    @Column(length=100)
    private String brandCode; //品牌
    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    @Transient
	private List<Collocat> collocatList;

	@Transient
	private String[] images;

    @Column(length=500)
    private String url;
	
	@Transient
	private List<Product> productList;
	
    
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	
	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	
	public List<Collocat> getCollocatList() {
		return collocatList;
	}

	public void setCollocatList(List<Collocat> collocatList) {
		this.collocatList = collocatList;
	}

	/**
	 * 新品款号
	 * */
    @Id
    @Column(length=20)
    private String styleId;
    
    @Column(length=20)
    private String name;
    /**
	 * 新品款号对应颜色以，分隔
	 * */
    @Column(length=255)
    private String colorIds;
    
    @Column(length=255)
    private String colorNames;
    
    /**
	 * 新品款号对应尺码以，分隔
	 * */
    @Column(length=255)
    private String sizeIds;
    
    @Column(length=255)
    private String sizeNames;
    
    @Column(length=20)
	private Integer seqNo;
		
    public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 *库存数
	 * */
    @Column(length=20)
    private Integer stockQty;
    
    /**
   	 *价格
   	 * */
    @Column(length=20)
    private Double price;
    
    /**
   	 *是否展示
   	 * */
    @Column(length = 2)
    private String isShow;
    
    @Column(length = 2)
    private String isDet;
    
    
	@Column(length=20)
    private String updater;
	
	@Column(length=20)
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date updateTime;

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColorIds() {
		return colorIds;
	}

	public void setColorIds(String colorIds) {
		this.colorIds = colorIds;
	}

	public String getColorNames() {
		return colorNames;
	}

	public void setColorNames(String colorNames) {
		this.colorNames = colorNames;
	}

	public String getSizeIds() {
		return sizeIds;
	}

	public void setSizeIds(String sizeIds) {
		this.sizeIds = sizeIds;
	}

	public String getSizeNames() {
		return sizeNames;
	}

	public void setSizeNames(String sizeNames) {
		this.sizeNames = sizeNames;
	}

	public Integer getStockQty() {
		return stockQty;
	}

	public void setStockQty(int stockQty) {
		this.stockQty = stockQty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getIsDet() {
		return isDet;
	}

	public void setIsDet(String isDet) {
		this.isDet = isDet;
	}
}
