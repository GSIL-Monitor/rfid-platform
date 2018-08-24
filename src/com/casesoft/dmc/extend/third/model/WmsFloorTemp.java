package com.casesoft.dmc.extend.third.model;

import com.casesoft.dmc.core.util.CommonUtil;

/**
 * Created by GuoJunwen on 2017/3/14 0014.
 */
public class WmsFloorTemp {
    private String id;
    private String name;
    private String barcode;
    private String parentId;
    private String remark;
    private String deviceId;
    private String image;
    private String sales;
    private Integer type;

    public WmsFloorTemp() {
    }

    public WmsFloorTemp(String id, String name, String barcode, String parentId, String remark,String deviceId,String image,Integer type) {
        this.id = id;
        if(CommonUtil.isBlank(name)){
            this.name = "";
        }else{
            this.name =name;
        }
        if (CommonUtil.isBlank(barcode)){
            this.barcode="";
        }else {
            this.barcode = barcode;
        }

        if (CommonUtil.isBlank(parentId)){
            this.parentId ="";
        }else{
            this.parentId = parentId;
        }

        if(CommonUtil.isBlank(remark)){
            this.remark = "";
        }else{
            this.remark = remark;
        }


        if (CommonUtil.isBlank(deviceId)){
            this.deviceId="";
        }else{
            this.deviceId=deviceId;
        }

        if (CommonUtil.isBlank(image)){
            this.image="";
        }else {
            this.image=image;
        }

        this.type=type;
    }

    public WmsFloorTemp(String id, String name, String barcode, String parentId, String remark,String deviceId,String image,String sales,Integer type) {
        this.id = id;
        if(CommonUtil.isBlank(name)){
            this.name = "";
        }else{
            this.name =name;
        }
        if (CommonUtil.isBlank(barcode)){
            this.barcode="";
        }else {
            this.barcode = barcode;
        }

        if (CommonUtil.isBlank(parentId)){
            this.parentId ="";
        }else{
            this.parentId = parentId;
        }

        if(CommonUtil.isBlank(remark)){
            this.remark = "";
        }else{
            this.remark = remark;
        }


        if (CommonUtil.isBlank(deviceId)){
            this.deviceId="";
        }else{
            this.deviceId=deviceId;
        }

        if (CommonUtil.isBlank(image)){
            this.image="";
        }else {
            this.image=image;
        }
        if (CommonUtil.isBlank(sales)){
            this.sales="";
        }else {
            this.sales=sales;
        }
        this.type=type;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
