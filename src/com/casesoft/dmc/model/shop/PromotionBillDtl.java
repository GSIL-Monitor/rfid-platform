package com.casesoft.dmc.model.shop;

import javax.persistence.*;
@Entity
@Table(name="Shop_PromotionBillDtl")
public class PromotionBillDtl {
	private String priceType;//

	private String redSDate;
	private String redEDate;	
	private Double discount;
	private Double point;
	private Double realPrice;
	private String id;
	private String billId;
	private String billNo;
    private String billDate;
	private String styleId;
	private Double price;
    private String ownerId;
    @Column(name="qty")
    private String qty;
    

	
    public PromotionBillDtl(){
    	super();
    }
	public PromotionBillDtl(String code, String provinceId, String cityId,
			String redSDate, String redEDate, String styleId, String priceType,
			Double discount, Double point) {
		super();
		this.code = code;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.redSDate = redSDate;
		this.redEDate = redEDate;
		this.styleId = styleId;
		this.priceType = priceType;
		this.discount = discount;
		this.point = point;
	}

	@Column(name = "oi", nullable = false, length = 20)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
	@Id
	@Column(name = "id", nullable = false, length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "bill_Id", nullable = false, length = 50)
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(name = "bill_No", nullable = false, length = 50)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	@Column(name = "style_Id", nullable = false, length = 20)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(name="price")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	@Column(name="point")
	public Double getPoint() {
		return point;
	}
	public void setPoint(Double point) {
		this.point = point;
	} 

	@Column(name="real_Price")
	public Double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	@Column(name="price_Type")
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	@Column(name="red_S_Date")
	public String getRedSDate() {
		return redSDate;
	}
	public void setRedSDate(String redSDate) {
		this.redSDate = redSDate;
	}
	@Column(name="red_E_Date")
	public String getRedEDate() {
		return redEDate;
	}
	public void setRedEDate(String redEDate) {
		this.redEDate = redEDate;
	}
	@Column(name="discount")
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
    @Column(name = "bill_Date", nullable = false)
    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String styleName;


    @Transient
	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	private String provinceId;
	private String cityId;
	private String code;
	private String name;
	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	@Transient
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
