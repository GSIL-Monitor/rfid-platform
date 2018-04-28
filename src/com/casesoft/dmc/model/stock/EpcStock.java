package com.casesoft.dmc.model.stock;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "STOCK_EPCSTOCK")
public class EpcStock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 表名为： TTE_STK， 中文为：EPC的库存 字段有 code 唯一码， warehouseId 发货仓库1ID， ownerId
	 * 发货组织1ID， inStock 是否在库， purIng 购买途中 ， tranIng调拨途中， refundIng退货途中，
	 * warehouse2Id收货仓库2Id， owner2Id收货组织2
	 */
	public static final int INSOTRAGE = 0;
	public static final int PURING = 1;
	public static final int TRANING = 2;
	public static final int REFUNDING = 3;
	public static final int ADJUSTING = 4;
	private String id;
	private String code;
	private String sku;
	private String warehouseId;
	private String warehouse2Id;
	private String ownerId;
	private String owner2Id;
	private int inStock;
	private String styleId;
	private String colorId;
	private String sizeId;
	private String deviceId;
	private Date updateDate;
	private int token;
	private String taskId;
	private int progress;// 0:库中，1:购买中，2:调拨中，3：退货中，4：调整

	private String floorArea;// 区
	private String floor;// 库位
	private String floorRack;// 货架
	private Boolean isOvered = false;// 是否过期

	private String inSotreType;//入库类型
	private Double stockPrice = 0D;//库存金额（入库时候单据价格）

	private Integer dressingStatus=0; //店员套版状态, 0表示在库，1表示穿着中

	@Transient
	private String styleName;
	@Transient
	private String colorName;
	@Transient
	private String sizeName;



	@Column()
	public Boolean isOvered() {
		return isOvered;
	}

	public void setOvered(Boolean isOvered) {
		this.isOvered = isOvered;
	}

	@Column()
	public String getFloorRack() {
		return floorRack;
	}

	public void setFloorRack(String floorRack) {
		this.floorRack = floorRack;
	}

	@Column()
	public String getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(String floorArea) {
		this.floorArea = floorArea;
	}

	@Column()
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	@Column(nullable = false, length = 50)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(nullable = false)
	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	@Column(length = 50)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(length = 19)
	public Date getUpdateDate() {
		return updateDate;
	}

	@Column()
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(length = 50)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Id
	@Column(nullable = false, length = 45)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false, length = 45)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 50)
	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	@Column(length = 50)
	public String getWarehouse2Id() {
		return warehouse2Id;
	}

	public void setWarehouse2Id(String warehouse2Id) {
		this.warehouse2Id = warehouse2Id;
	}

	@Column(nullable = false, length = 50)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(length = 50)
	public String getOwner2Id() {
		return owner2Id;
	}

	public void setOwner2Id(String owner2Id) {
		this.owner2Id = owner2Id;
	}

	@Column(nullable = false)
	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	@Column(length = 20)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(length = 20)
	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	@Column(length = 10)
	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	public Boolean getOvered() {
		return isOvered;
	}
	@Column(length = 20)
	public String getInSotreType() {
		return inSotreType;
	}

	public void setInSotreType(String inSotreType) {
		this.inSotreType = inSotreType;
	}
	@Column()
	public Double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Double stockPrice) {
		this.stockPrice = stockPrice;
	}

	@Column()
	public Integer getDressingStatus() {
		return dressingStatus;
	}

	public void setDressingStatus(Integer dressingStatus) {
		this.dressingStatus = dressingStatus;
	}


	private Double preCast;//事前成本价(采购价)

	private Double price;//吊牌价格

	private Double puPrice;//代理商批发价格

	private Double wsPrice;//门店批发价格

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
	@Transient
	public Double getPreCast() {
		return preCast;
	}

	public void setPreCast(Double preCast) {
		this.preCast = preCast;
	}
	@Transient
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
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


	private String isAlert;
	@Transient
	public String getIsAlert() {
		return isAlert;
	}

	public void setIsAlert(String isAlert) {
		this.isAlert = isAlert;
	}

	private String storage;
	@Transient
	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	private String unicode;
	@Transient
	public String getUnicode() {
		return unicode;
	}

	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}




	private String originBillNo; //原始销售单号
	@Transient
	public String getOriginBillNo() { return originBillNo; }
	public void setOriginBillNo(String originBillNo) { this.originBillNo = originBillNo; }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastSaleTime;//最近一次销售时间

	@Transient
	public Date getLastSaleTime() { return lastSaleTime; }
	public void setLastSaleTime(Date lastSaleTime) { this.lastSaleTime = lastSaleTime; }


	private Long saleCycle; //销售周期（开单当天时间－销售单时间）
	@Transient
	public Long getSaleCycle() { return saleCycle; }
	public void setSaleCycle(Long saleCycle) { this.saleCycle = saleCycle; }

	public EpcStock() { }

	public EpcStock(String code, String sku, String styleId, String colorId, String sizeId, String originBillNo, Date lastSaleTime,String floor,String warehouseId) {
		this.code = code;
		this.sku = sku;
		this.styleId = styleId;
		this.colorId = colorId;
		this.sizeId = sizeId;
//		this.price = price;
		this.originBillNo = originBillNo;
		this.lastSaleTime = lastSaleTime;
		this.floor=floor;
		this.warehouseId=warehouseId;
//		this.saleCycle = saleCycle;
//		this.inStock=inStock;
	}









}
