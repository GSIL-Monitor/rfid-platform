package com.casesoft.dmc.model.hall;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.Transient;
import java.util.Date;

/**
 * Created by WinLi on 2017-03-08.
 */
@Entity
@Table(name = "Hall_InventoryDetail")
public class HallInventoryDetail implements java.io.Serializable {
    private static final long serialVersionUID = 6259324156458612597L;
    private String id;
    private String styleId;
    private String colorId;
    private String sizeId;
    private String ownerId;
    private String taskId;
    private String code;
    private Integer qty;
    private Integer actQty;
    private String floor;

    private int scanStatus;// 扫描状态0:未扫描1:已扫描
    private int status;// 在库状态// 0:未入库1在厅2外借3出库4.投产下单5丢失 -1：非法

    private String styleName;
    private String colorName;
    private String sizeName;

    private String floorName;

    private Date scanDate;
    private String remark;


    public HallInventoryDetail() {
        super();
    }

    public HallInventoryDetail(String code,String styleId, String colorId, String sizeId,
                              String floor, int status,int scanStatus) {
        super();
        this.sizeId=sizeId;
        this.styleId = styleId;
        this.colorId = colorId;
        this.floor = floor;
        this.status = status;
        this.code=code;
        this.scanStatus=scanStatus;
    }

    public HallInventoryDetail(String styleId, String colorId,String sizeId, String code,
                              String floor, int scanStatus, int status, Date scanDate) {
        super();
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId=sizeId;
        this.code = code;
        this.floor = floor;
        this.scanStatus = scanStatus;
        this.status = status;
        this.scanDate = scanDate;
    }
    public HallInventoryDetail(String styleId, String colorId,String sizeId, String code,
                              String floor, int scanStatus, int status, Date scanDate,String taskId) {
        super();
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId=sizeId;
        this.code = code;
        this.floor = floor;
        this.scanStatus = scanStatus;
        this.status = status;
        this.scanDate = scanDate;
        this.taskId = taskId;
    }

    @Transient
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Id
    @Column( nullable = false, length = 45)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false, length = 50)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column(nullable = false, length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(length = 20)
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Column(length = 10)
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

    @Column()
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Column
    public Integer getActQty() {
        return actQty;
    }

    public void setActQty(Integer actQty) {
        this.actQty = actQty;
    }

    @Column(nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }





    @Column( nullable = false)
    public int getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(int scanStatus) {
        this.scanStatus = scanStatus;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String barCode;

    @Transient
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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


    @Transient
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Column(length=200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(length = 19)
    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }
}
