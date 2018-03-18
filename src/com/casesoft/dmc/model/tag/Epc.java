package com.casesoft.dmc.model.tag;

import com.casesoft.dmc.cache.CacheManager;

import javax.persistence.*;

/**
 * Epc entity. @author
 */
@Entity
@Table(name = "TAG_EPC")
public class Epc implements java.io.Serializable {

	private static final long serialVersionUID = 790925789806122627L;

	private String billNo;
	private String ownerId;
	private String code;
	private String epc;
	private String dimension;
	private String sku;
	private String styleId;
	private String colorId;
	private String sizeId;

	private String tid;// 2014-10-23 TID号

	private Integer isDestruct;// 被毁坏0否1是2人工破坏
	private Integer destructToken;// 被毁坏的token
	private String brandCode;
	private String id;
	private String styleName;
	private String colorName;
	private String sizeName;

	
	@Column(nullable = true, length = 20)
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	@Column(unique = true, nullable = true, length = 100)
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Column()
	public Integer getIsDestruct() {
		return isDestruct;
	}

	public void setIsDestruct(Integer isDestruct) {
		this.isDestruct = isDestruct;
	}

	// Constructors

	

	@Id
	@Column(nullable = false, length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/** default constructor */
	public Epc() {
	}

	public Epc(String code,String billNo,String sku, String epc, String styleId, String colorId, String sizeId) {
		this.code = code;
		this.billNo = billNo;
		this.sku = sku;
		this.epc = epc;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
	}

	@Column(unique = true, nullable = false, length = 50)
	public String getEpc() {
		return this.epc;
	}

	@Column(nullable = false, length = 50)
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(unique = true, nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	@Column(unique = true, nullable = false, length = 100)
	public String getDimension() {
		return this.dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	@Column(nullable = false, length = 50)
	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(nullable = false, length = 20)
	public String getStyleId() {
		return this.styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(nullable = false, length = 20)
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
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}


	private Double preCast = 0d;//事前成本价(采购价)

	private Double price =0d;//吊牌价格

	private Double puPrice =0d;//代理商批发价格
	@Transient
	public Double getPreCast() {
		return preCast;
	}

	public void setPreCast(Double preCast) {
		this.preCast = preCast;
	}

	@Transient
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Transient
	public Double getPuPrice() {
		return puPrice;
	}

	public void setPuPrice(Double puPrice) {
		this.puPrice = puPrice;
	}

	@Transient
	public Double getWsPrice() {
		return wsPrice;
	}

	public void setWsPrice(Double wsPrice) {
		this.wsPrice = wsPrice;
	}


	private Double wsPrice =0D;//门店批发价格

	private String class6;//

	@Transient
	public String getClass6() {
		return class6;
	}

	public void setClass6(String class6) {
		this.class6 = class6;
	}
}