package com.casesoft.dmc.model.factory;

import javax.persistence.*;

@Entity
@Table(name="Factory_Bill_Dtl")
public class FactoryBillDtl implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="bill_No",length=30)
	private String billNo;
	@Column(name="upload_No",length=50)
	private String uploadNo;

	@Column(name="bill_Date",length=20)
	private String billDate;
	@Column(name="type",length=255)
	private String type;
	@Column(name="style_Id",length=30)
	private String styleId;
	@Column(name="color_Id",length=30)
	private String colorId;
	@Column(name="size_id",length=10)
	private String sizeId;
	@Id
	@Column(name="sku")
	private String sku;
	@Column(name="owner_Id",length=20)
	private String ownerId;
	@Id
	@Column(name="end_Date",length=20)
	private String endDate;	
	@Column(name="wash_Type",length=100)
	private String washType;
	@Column(name="shirt_Type",length=50)
	private String shirtType;
	@Column(name="sex",length=50)
	private String sex;
	@Column(name="status")
	private String status;
	@Column(name="customer_Id",length=80)
	private String customerId;
	@Column(name="group_Id",length=255)
	private String groupId;
	@Column(name="operator",length=50)
	private String operator;
	@Column(name="qty")
	private long qty;
	@Column(name="start_num")
	private long startNum;
	@Column(name="end_num")
	private long endNum;
	@Column(name="remark",length=500)
	private String remark;
	
	
	
	
	public FactoryBillDtl() {
		super();
	}
	
	
	public FactoryBillDtl(String billNo,String billDate,String type,
			String styleId, String colorId, String sizeId,String sku, 
			String endDate, String washType,String shirtType, String sex,
			String customerId, String groupId,String operator, long qty,
			String styleName, String colorName, String imgUrl, String factory, String season, String category) {
		this.billNo = billNo;
		this.billDate = billDate;
		this.type = type;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.sku = sku;
		this.endDate = endDate;
		this.washType = washType;
		this.shirtType = shirtType;
		this.sex = sex;
		this.customerId = customerId;
		this.groupId = groupId;
		this.operator = operator;
		this.qty = qty;
		this.styleName = styleName;
		this.colorName = colorName;
		this.imgUrl = imgUrl;
		this.factory = factory;
		this.season = season;
		this.category = category;
	}

    public FactoryBillDtl(String billNo, String uploadNo, String billDate,String endDate, String styleId,
                          String colorId, String sizeId, String sku, String ownerId,String type,
                          String washType, String shirtType, String sex, String status, String customerId,
                          String groupId, String operator, long qty, String season, String category, String factory,String imgUrl) {
        this.billNo = billNo;
        this.uploadNo = uploadNo;
        this.billDate = billDate;
        this.type = type;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.sku = sku;
        this.ownerId = ownerId;
        this.endDate = endDate;
        this.washType = washType;
        this.shirtType = shirtType;
        this.sex = sex;
        this.status = status;
        this.customerId = customerId;
        this.groupId = groupId;
        this.operator = operator;
        this.qty = qty;
        this.season = season;
        this.category = category;
        this.factory = factory;
        this.imgUrl = imgUrl;
    }

    public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getWashType() {
		return washType;
	}
	public void setWashType(String washType) {
		this.washType = washType;
	}
	public String getShirtType() {
		return shirtType;
	}
	public void setShirtType(String shirtType) {
		this.shirtType = shirtType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public long getStartNum() {
		return startNum;
	}
	public void setStartNum(long startNum) {
		this.startNum = startNum;
	}
	public long getEndNum() {
		return endNum;
	}
	public void setEndNum(long endNum) {
		this.endNum = endNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Transient
	private String styleName;
	
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	@Transient
	private String colorName;
	
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	@Column(name="season",length=200)
	private String season;
	
	@Column(name="category",length=40)
	private String category;
	
	@Column(name="factory",length=40)
	private String factory;
	
	@Column(name="url",length=200)
	private String url;
	
	@Column(name="imgUrl",length=200)
	private String imgUrl;
	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getFactory() {
		return factory;
	}


	public void setFactory(String factory) {
		this.factory = factory;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
