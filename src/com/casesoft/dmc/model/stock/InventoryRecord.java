package com.casesoft.dmc.model.stock;


import com.alibaba.fastjson.annotation.JSONField;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yushen on 2017/10/31.
 *
 * 上传盘点任务时，保存实时数据库库存
 */

@Entity
@Table(name = "STOCK_INVENTORYRECORD")
public class InventoryRecord {

    @Id
    @Column
    private String id; //billNo-code  eg: IV1503396694071-0136390000124
    @Column()
    private String billNo;
    @Column()
    @Excel(name = "唯一码", width = 20D)
    private String code;
    @Column()
    private String warehouseId;
    @Column()
    @Excel(name = "SKU", width = 20D)
    private String sku;
    @Column()
    @Excel(name = "款号", width = 20D)
    private String styleId;
    @Transient
    @Excel(name = "款名")
    private String styleName;
    @Column()
    @Excel(name = "颜色")
    private String colorId;
    @Column()
    @Excel(name = "尺寸")
    private String sizeId;
    @JSONField(format = "yyyy-MM-dd")
    @Column()
    private Date recordDate;
    @Column()
    @Excel(name = "盘点情况")
    private Integer isScanned = 0;// 1：表示扫描到了 0：表示差异

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
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

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getIsScanned() {
        return isScanned;
    }

    public void setIsScanned(Integer isScaned) {
        this.isScanned = isScaned;
    }
}
