package com.casesoft.dmc.extend.sqlite.model;/**
 * Created by aa on 2018/8/29.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author aa
 * @date 2018/8/29 16:05
 */
@DatabaseTable(tableName = "STOCK_EPCSTOCK")
public class SqliteEpcStock implements java.io.Serializable {

    @DatabaseField(id=true,columnName="id",index = true )
    private String id;
    @DatabaseField(columnName="code",index = true)
    private String code;
    @DatabaseField
    private String sku;
    @DatabaseField
    private String warehouseId;
    @DatabaseField
    private String warehouse2Id;
    @DatabaseField
    private String ownerId;
    @DatabaseField
    private String owner2Id;
    @DatabaseField
    private int inStock;
    @DatabaseField
    private String styleId;
    @DatabaseField
    private String colorId;
    @DatabaseField
    private String sizeId;
    @DatabaseField
    private String deviceId;
    @DatabaseField
    private Date updateDate;
    @DatabaseField
    private int token;
    @DatabaseField
    private String taskId;
    @DatabaseField
    private int progress;// 0:库中，1:购买中，2:调拨中，3：退货中，4：调整
    @DatabaseField
    private String floorArea;// 货层
    @DatabaseField
    private String floor;// 仓库名
    @DatabaseField
    private String floorAllocation;//货位
    @DatabaseField
    private String floorRack;// 货架
    @DatabaseField
    private Boolean isOvered = false;// 是否过期
    @DatabaseField
    private String inSotreType;//入库类型
    @DatabaseField
    private Double stockPrice = 0D;//库存金额（入库时候单据价格）
    private Integer dressingStatus=0; //店员套版状态, 0表示在库，1表示穿着中
    @DatabaseField
    private Long version;//版本号

    @DatabaseField
    private String styleName;
    @DatabaseField
    private String colorName;
    @DatabaseField
    private String sizeName;
    @DatabaseField
    private Double preCast;//事前成本价(采购价)
    @DatabaseField
    private Double price;//吊牌价格
    @DatabaseField
    private Double puPrice;//代理商批发价格
    @DatabaseField
    private Double wsPrice;//门店批发价格
    @DatabaseField
    private Double bargainPrice;//特價

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouse2Id() {
        return warehouse2Id;
    }

    public void setWarehouse2Id(String warehouse2Id) {
        this.warehouse2Id = warehouse2Id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner2Id() {
        return owner2Id;
    }

    public void setOwner2Id(String owner2Id) {
        this.owner2Id = owner2Id;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloorAllocation() {
        return floorAllocation;
    }

    public void setFloorAllocation(String floorAllocation) {
        this.floorAllocation = floorAllocation;
    }

    public String getFloorRack() {
        return floorRack;
    }

    public void setFloorRack(String floorRack) {
        this.floorRack = floorRack;
    }

    public Boolean getOvered() {
        return isOvered;
    }

    public void setOvered(Boolean overed) {
        isOvered = overed;
    }

    public String getInSotreType() {
        return inSotreType;
    }

    public void setInSotreType(String inSotreType) {
        this.inSotreType = inSotreType;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Integer getDressingStatus() {
        return dressingStatus;
    }

    public void setDressingStatus(Integer dressingStatus) {
        this.dressingStatus = dressingStatus;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

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

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Double getPreCast() {
        return preCast;
    }

    public void setPreCast(Double preCast) {
        this.preCast = preCast;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPuPrice() {
        return puPrice;
    }

    public void setPuPrice(Double puPrice) {
        this.puPrice = puPrice;
    }

    public Double getWsPrice() {
        return wsPrice;
    }

    public void setWsPrice(Double wsPrice) {
        this.wsPrice = wsPrice;
    }

    public Double getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(Double bargainPrice) {
        this.bargainPrice = bargainPrice;
    }
}
