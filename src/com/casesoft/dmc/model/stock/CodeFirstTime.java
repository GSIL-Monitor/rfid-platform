package com.casesoft.dmc.model.stock;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/14.
 */
@Entity
@Table(name = "STOCK_CodeFirstTime")
public class CodeFirstTime {
    @Id
    @Column()
    private String id;
    @Column()
    private String code;
    @Column()
    private String warehouseId;
    @Column()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date firstTime;
    @Column()
    private Double warehousePrice;

    public Double getWarehousePrice() {
        return warehousePrice;
    }

    public void setWarehousePrice(Double warehousePrice) {
        this.warehousePrice = warehousePrice;
    }

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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }
}
