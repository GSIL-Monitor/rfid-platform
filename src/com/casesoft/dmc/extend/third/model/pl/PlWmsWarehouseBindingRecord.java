package com.casesoft.dmc.extend.third.model.pl;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Entity
@Table(name = "Pl_Warehouse_BindingRecord")
public class PlWmsWarehouseBindingRecord implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "Id", nullable = false, length = 80)
    private String  id;
    @Column(name = "styleId", nullable = false, length = 30)
    private String styleId;
    @Column(name = "colorId", nullable = false, length = 20)
    private String colorId;
    @Column(name = "floorId", length = 32)
    private String floorId;//货架编号
    @Column(name = "floorBarcode", length = 32)
    private String floorBarcode;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateDate",nullable = false)
    private Date updateDate;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorBarcode() {
        return floorBarcode;
    }

    public void setFloorBarcode(String floorBarcode) {
        this.floorBarcode = floorBarcode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
