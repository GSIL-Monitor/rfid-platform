package com.casesoft.dmc.extend.third.model.pl;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Entity
@Table(name = "Pl_Shop_BindingRelation")
public class PlWmsShopBindingRelation implements Serializable {
    @Id
    @Column(name = "Id", nullable = false, length = 80)
    private String  id;
    @Column(name = "styleId", nullable = false, length = 30)
    private String styleId;
    @Column(name = "colorId", nullable = false, length = 20)
    private String colorId;
    @Column(name = "rackId", length = 32)
    private String rackId;//货架编号
    @Transient
    private String styleName;
    @Transient
    private String colorName;
    @Transient
    private String rackBarcode;//货架条码
    @Transient
    private String rackImage;
    @Transient
    private String rackRemark;
    @Transient
    private String rackName;
    @Transient
    private String floorAreaId;
    @Transient
    private String floorAreaBarcode;
    @Transient
    private String floorAreaName;
    @Transient
    private String floorAreaImage;//
    @Transient
    private String floorAreaRemark;//
    @Transient
    private String unitCode;//店仓编码

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateDate")
    private Date updateDate;

    public PlWmsShopBindingRelation() {
    }
    public PlWmsShopBindingRelation(String styleId, String colorId, String rackBarcode) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.rackBarcode = rackBarcode;
    }

    public PlWmsShopBindingRelation(String styleId, String colorId,
                                    String rackBarcode, String rackName,
                                    String floorAreaBarcode, String floorAreaName) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.rackBarcode = rackBarcode;
        this.rackName = rackName;
        this.floorAreaBarcode = floorAreaBarcode;
        this.floorAreaName = floorAreaName;
    }
    public PlWmsShopBindingRelation(String styleId, String colorId,
                                    String rackId, String rackBarcode, String rackName,String rackImage, String rackRemark,
                                     String floorAreaId,String floorAreaBarcode, String floorAreaName, String floorAreaImage, String floorAreaRemark,String unitCode) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.rackId = rackId;
        this.floorAreaId=floorAreaId;
        this.rackBarcode = rackBarcode;
        this.rackImage = rackImage;
        this.rackRemark = rackRemark;
        this.rackName = rackName;
        this.floorAreaBarcode = floorAreaBarcode;
        this.floorAreaName = floorAreaName;
        this.floorAreaImage = floorAreaImage;
        this.floorAreaRemark = floorAreaRemark;
        this.unitCode=unitCode;
    }
    public PlWmsShopBindingRelation(String styleId, String colorId,
                                    String rackId, String rackBarcode, String rackName,String rackImage, String rackRemark,
                                    String floorAreaId,String floorAreaBarcode, String floorAreaName, String floorAreaImage, String floorAreaRemark) {
        this.styleId = styleId;
        this.colorId = colorId;
        this.rackId = rackId;
        this.floorAreaId=floorAreaId;
        this.rackBarcode = rackBarcode;
        this.rackImage = rackImage;
        this.rackRemark = rackRemark;
        this.rackName = rackName;
        this.floorAreaBarcode = floorAreaBarcode;
        this.floorAreaName = floorAreaName;
        this.floorAreaImage = floorAreaImage;
        this.floorAreaRemark = floorAreaRemark;
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

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
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

    public String getRackBarcode() {
        return rackBarcode;
    }

    public void setRackBarcode(String rackBarcode) {
        this.rackBarcode = rackBarcode;
    }

    public String getRackImage() {
        return rackImage;
    }

    public void setRackImage(String rackImage) {
        this.rackImage = rackImage;
    }

    public String getRackRemark() {
        return rackRemark;
    }

    public void setRackRemark(String rackRemark) {
        this.rackRemark = rackRemark;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getFloorAreaId() {
        return floorAreaId;
    }

    public void setFloorAreaId(String floorAreaId) {
        this.floorAreaId = floorAreaId;
    }

    public String getFloorAreaBarcode() {
        return floorAreaBarcode;
    }

    public void setFloorAreaBarcode(String floorAreaBarcode) {
        this.floorAreaBarcode = floorAreaBarcode;
    }

    public String getFloorAreaName() {
        return floorAreaName;
    }

    public void setFloorAreaName(String floorAreaName) {
        this.floorAreaName = floorAreaName;
    }

    public String getFloorAreaImage() {
        return floorAreaImage;
    }

    public void setFloorAreaImage(String floorAreaImage) {
        this.floorAreaImage = floorAreaImage;
    }

    public String getFloorAreaRemark() {
        return floorAreaRemark;
    }

    public void setFloorAreaRemark(String floorAreaRemark) {
        this.floorAreaRemark = floorAreaRemark;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
