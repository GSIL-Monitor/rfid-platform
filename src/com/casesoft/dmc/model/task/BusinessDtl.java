package com.casesoft.dmc.model.task;

import javax.persistence.*;
import java.util.List;

/**
 * BusinessDtl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TASK_BUSINESSDTL")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BusinessDtl implements java.io.Serializable {

    private static final long serialVersionUID = 1145893475448737925L;
    private String id;
    private String taskId;
    private String ownerId;
    private Integer token;
    private String deviceId;
    private String sku;
    private long qty;
    private String origId;//
    private String origUnitId;

    private String styleId;
    private String colorId;
    private String sizeId;

    private String srcTaskId;
    private String styleName;
    private String colorName;
    private String sizeName;

    private String destId;
    private String destUnitId; //如果是品牌商自己的门店，值为null
    private Integer type;//0:出库1：入库3：盘点

    private String destName;
    private String destUnitName;
    private String origName;
    private String deviceName;
    private String origUnitName;

    private Double preVal;

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
    public BusinessDtl() {
    }

    /**
     * full constructor
     */
    public BusinessDtl(String taskId, String ownerId, Integer token, String deviceId, String sku,
                       long qty) {
        this.taskId = taskId;
        this.ownerId = ownerId;
        this.token = token;
        this.deviceId = deviceId;
        this.sku = sku;
        this.qty = qty;
    }

    public BusinessDtl(String sku, String styleId,
                       String colorId, String sizeId, long qty) {
        super();
        this.sku = sku;
        this.qty = qty;
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
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

    @Column(nullable = false)
    public Integer getToken() {
        return this.token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    @Column(nullable = false, length = 50)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    @Column(length = 50)
    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }
    @Column(length = 50)
    public String getOrigUnitId() {
        return origUnitId;
    }

    public void setOrigUnitId(String origUnitId) {
        this.origUnitId = origUnitId;
    }

    // //===================>
   


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
    public String getDestUnitName() {
        return destUnitName;
    }

    public void setDestUnitName(String destUnitName) {
        this.destUnitName = destUnitName;
    }

    @Transient
    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    @Column(length = 50)
    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    @Column(length = 50)
    public String getDestUnitId() {
        return destUnitId;
    }

    public void setDestUnitId(String destUnitId) {
        this.destUnitId = destUnitId;
    }

    @Column()
    public Integer getType() {
        return type;
    }

    private List<String> codeList;

    public void setType(Integer type) {
        this.type = type;
    }

    @Column()
    public Double getPreVal() {
        return preVal;
    }

    public void setPreVal(Double preVal) {
        this.preVal = preVal;
    }

    @Transient
    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    @Transient
    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    @Transient
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Transient
    public String getOrigUnitName() {
        return origUnitName;
    }

    public void setOrigUnitName(String origUnitName) {
        this.origUnitName = origUnitName;
    }
}