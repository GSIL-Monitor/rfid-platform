package com.casesoft.dmc.model.task;

import javax.persistence.*;
import java.util.List;

/**
 * TblTaskBusinessBoxDtl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TASK_BOXDTL")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BoxDtl implements java.io.Serializable {

	private static final long serialVersionUID = 1369118938344073964L;
	private String id;
	private String cartonId;
	private String taskId;
	private String ownerId;
	private String deviceId;

	private Integer token;
	private String sku;
	private long qty;
	private String styleId;
	private String colorId;
	private String sizeId;

	private String srcTaskId;
	private String styleName;
	private String colorName;
	private String sizeName;



	private Integer type;// 0:出库1：入库3：盘点

	private String storage2Name;
	private String unit2Name;

	@Column(length = 50)
	public String getSrcTaskId() {
		return srcTaskId;
	}

	public void setSrcTaskId(String srcTaskId) {
		this.srcTaskId = srcTaskId;
	}

	// Constructors
	/**
	 * default constructor
	 */
	public BoxDtl() {
	}

	/**
	 * full constructor
	 */
	public BoxDtl(String cartonId, String taskId, String ownerId,
			String deviceId, Integer token, String sku, Integer qty) {
		this.cartonId = cartonId;
		this.taskId = taskId;
		this.ownerId = ownerId;
		this.deviceId = deviceId;
		this.token = token;
		this.sku = sku;
		this.qty = qty;
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

	@Column(nullable = false, length = 50)
	public String getCartonId() {
		return this.cartonId;
	}

	public void setCartonId(String cartonId) {
		this.cartonId = cartonId;
	}

	@Column(nullable = false, length = 50)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	@Column(nullable = false, length = 50)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(nullable = false)
	public Integer getToken() {
		return this.token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	@Column(nullable = false, length = 50)
	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(nullable = false)
	public long getQty() {
		return this.qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	@Column(nullable = false, length = 20)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(nullable = false, length = 20)
	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Column(nullable = false, length = 10)
	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	// //===================>

	@Transient
	private String code;
	@Transient
	private String cartonScanTime;
	@Transient
	public String getCartonScanTime() {
		return cartonScanTime;
	}

	public void setCartonScanTime(String cartonScanTime) {
		this.cartonScanTime = cartonScanTime;
	}

	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	protected List<String> codeList;
	@Transient
	protected List<String> epcList;
	@Transient
	public List<String> getEpcList() {
		return epcList;
	}

	public void setEpcList(List<String> epcList) {
		this.epcList = epcList;
	}

	@Transient
	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}



	@Transient
	public String getUnit2Name() {
		return unit2Name;
	}

	public void setUnit2Name(String unit2Name) {
		this.unit2Name = unit2Name;
	}

	@Transient
	public String getStorage2Name() {
		return storage2Name;
	}

	public void setStorage2Name(String storage2Name) {
		this.storage2Name = storage2Name;
	}


	@Column()
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Transient
	private Long skuCount;
	@Transient
	public Long getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Long skuCount) {
		this.skuCount = skuCount;
	}
	@Transient
	private Long preQty;

	public Long getPreQty() {
		return preQty;
	}

	public void setPreQty(Long preQty) {
		this.preQty = preQty;
	}
}