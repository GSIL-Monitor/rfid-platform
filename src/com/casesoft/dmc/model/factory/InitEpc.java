package com.casesoft.dmc.model.factory;

import javax.persistence.*;


@Entity
@Table(name = "Init_Epc")
public class InitEpc implements java.io.Serializable{
	
	@Id
	@Column(name="code", nullable = false, length = 50)
	private String code;
	@Column(name="billNo", nullable = false, length = 20)
	private String billNo;
	@Column(name="bill_Date",length=20)
	private String billDate;
	@Column(name="end_Date",length=20)
	private String endDate;
	@Column(name="uploadNo", nullable = false, length = 20)
	private String uploadNo;
	
	@Column(name="owner_Id", nullable = false, length = 20)
	private String ownerId;	
	@Column(name="epc",  nullable = false, length = 50)
	private String epc;
	@Column(name="sku", nullable = false, length = 60)
	private String sku;
	@Column(name="style_Id", nullable = false, length = 30)
	private String styleId;
	@Column(name="color_Id", nullable = false, length = 30)
	private String colorId;
	@Column(name="size_Id", nullable = false, length = 10)
	private String sizeId;
	@Column(name="type")
	private String type;
	@Column(name="progress")
	private String progress;
	
	@Column(name="taskId")
	private String taskId;
	@Column(name="taskTime")
	private String taskTime;
	@Column(name="totalTime")
	private Double totalTime;
	
	
	public InitEpc() {}
	
	
	public InitEpc(String code, String billNo, String billDate, String endDate,
			String uploadNo, String ownerId, String epc, String sku,
			String styleId, String colorId, String sizeId, String type,
			long boxQty) {
		
		this.code = code;
		this.billNo = billNo;
		this.billDate = billDate;
		this.endDate = endDate;
		this.uploadNo = uploadNo;
		this.ownerId = ownerId;
		this.epc = epc;
		this.sku = sku;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
		this.type = type;
		this.boxQty = boxQty;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUploadNo() {
		return uploadNo;
	}
	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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

	@Transient
	private String styleName;
	@Transient
	private String colorName;
	@Transient
	private String sizeName;

	@Transient
	private long boxQty;
	
	@Transient
	private String category; 
	
	@Transient
	private String customerId;
	
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

	public Double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public long getBoxQty() {
		return boxQty;
	}
	public void setBoxQty(long boxQty) {
		this.boxQty = boxQty;
	}
}
